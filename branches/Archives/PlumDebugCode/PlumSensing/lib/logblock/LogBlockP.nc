#include "LogBlock.h"
#include "printf.h"

module LogBlockP {
    provides interface LogBlock;
    uses {
        interface BlockRead;
        interface BlockWrite;

#ifdef KEEP_RECOVERY_BLOCK
		interface Timer<TMilli> as WriteTimer;
#endif
		
		interface Leds;
        interface Pool<Block> as BlockPool;
    }
} implementation {
    bool initDone = FALSE, processBusy, m_recovery = FALSE, m_config = FALSE, m_erase = FALSE, m_writeConfig = FALSE, m_reading = FALSE;

	Block readingBlock;
    Block *currentBlock;
	ConfigBlock configBlock;
	uint16_t configLen;
    blocksqnnbr_t currentBlockID;
    uint16_t currentBlockOffset;
	blocksqnnbr_t firstRead, lastRead;
	uint16_t readingLen;
	
#ifdef KEEP_RECOVERY_BLOCK
	Block recoveryBlock;
	task void backup();
#endif
	void recoverBlock();
    error_t get(Block *blockPtr, blocksqnnbr_t blockSqnNbr);
	
	
    command error_t LogBlock.init() {
		// Recovery Block is ID 1
        currentBlockID = RECOVERY_BLOCK + 1;
        currentBlock = call BlockPool.get();
        currentBlockOffset = 0;
		
        processBusy = FALSE;
        initDone = FALSE;

		recoverBlock();
		
        if (currentBlock == NULL)
            return FAIL;
        return SUCCESS;
    }
	
	command error_t LogBlock.writeConfig(void *buf, uint8_t len) {
		memcpy(&(configBlock.data), buf, len);
		configBlock.len = len;
		configBlock.last_blockID = currentBlockID;

		printf("LogBlock.writeConfig(): len = %d, currentBlockID = %d\n", configLen, currentBlockID);
		printfflush();
		
        if (!initDone) return EOFF;

		m_writeConfig = TRUE;
		processBusy = TRUE;
		configLen = len;

		if (call BlockWrite.write(BLOCK_SIZE * CONFIG_BLOCK, &configBlock, BLOCK_HEADER_LEN + configBlock.len) != SUCCESS) {
			processBusy = FALSE;
			return EBUSY;
		}		
		return SUCCESS;

		call LogBlock.sync();
	}

	command error_t LogBlock.readConfig(void *buf, uint8_t len) {
		error_t retval;

		printf("LogBlock.readConfig(): len = %d\n", len);
		printfflush();

		if (!initDone) return EOFF;

		m_config = TRUE;
		processBusy = TRUE;

		retval = call BlockRead.read(BLOCK_SIZE * CONFIG_BLOCK, &configBlock, BLOCK_HEADER_LEN + configBlock.len);
		
		if (retval != SUCCESS) {
			processBusy = FALSE;
		}
		return retval;
	}

	void recoverBlock() {
		m_recovery = TRUE;
		processBusy = TRUE;
#ifdef KEEP_RECOVERY_BLOCK	
		if (call BlockRead.read(BLOCK_SIZE * RECOVERY_BLOCK, (void *) currentBlock, BLOCK_SIZE) != SUCCESS) {
			processBusy = FALSE;
		}
#else
		if (call BlockRead.read(BLOCK_SIZE * CONFIG_BLOCK, (void *) &configBlock, BLOCK_SIZE) != SUCCESS) {
			processBusy = FALSE;
		}
#endif
	}
	
    command error_t LogBlock.append(void *buf, uint8_t len, void *blkid) {
		error_t retval;

//		printf("append(): cur: %p, addr: %lu, bid: %lu, boffset: %u, len: %u, seq: %d\n", currentBlock, currentBlock->blockID, currentBlockID, currentBlockOffset, len, ((plum_sample_t *) buf)->seqno);
//		printfflush();
		
        if (!initDone) return EOFF;
        if (currentBlock == NULL) return EBUSY;
		
        if (DS_BLOCK_DATA_LEN - currentBlockOffset < len) {
            currentBlock->len = currentBlockOffset;
            currentBlock->blockID = currentBlockID;
			retval = call BlockWrite.write(BLOCK_SIZE * currentBlockID, (void *)currentBlock, BLOCK_SIZE);
			
            if (retval != SUCCESS) {
//                printfUART("Write request failed (%u): addr: %lu, data: %p, len: %u\n", retval, BLOCK_SIZE * currentBlockID, currentBlock, BLOCK_SIZE);
                processBusy = FALSE;
			}
//			printfUART("Writing block: addr: %p, bid: %lu, boffset: %u\n", currentBlock, currentBlock->blockID, currentBlock->len);
//			printf("Writing block: bid = %d, offset = %d\n", currentBlock->blockID, currentBlock->len);
//			printfflush();
			
//            printfUART("Flushing and rotating, %d < %d\n", DS_BLOCK_DATA_LEN-currentBlockOffset, len);
			
            currentBlock = call BlockPool.get();
            if (currentBlock == NULL) {
                return EBUSY;
            }

			if (currentBlockID + 1 >= VOLUME_SIZE / BLOCK_SIZE) {
				// Circular buffer
				// Assumes recovery block is before the data blocks
				// JKT: Need to add fix to not write circularly
				currentBlockID = RECOVERY_BLOCK + 1;
			}
			else {
				currentBlockID++;
			}
			
            currentBlockOffset = 0;
        }

		// Write current block ID into parameter - useful for the application
		// to keep track of where the appended data is being written
		memcpy(blkid, &currentBlockID, sizeof(currentBlockID));
//		*((nx_uint32_t *) blkid) = currentBlockID;
		
//		printfUART("append(): addr: %p, bid: %lu, boffset: %u, len: %u\n", currentBlock, currentBlockID, currentBlockOffset, len);
//		printf("append(): bid: %lu, boffset: %u, len: %u\n", currentBlockID, currentBlockOffset, len);
//		printfflush();
		memcpy(&(currentBlock->data[currentBlockOffset]), buf, len);
		currentBlockOffset += len;

		currentBlock->len = currentBlockOffset;
		currentBlock->blockID = currentBlockID;

		// JKT: 30 ms wasn't enough for the first flash write to finish
#ifdef KEEP_RECOVERY_BLOCK
		call WriteTimer.startOneShot(100);
#endif
		
		return SUCCESS;
	}

#ifdef KEEP_RECOVERY_BLOCK
	event void WriteTimer.fired() {
		post backup();		
	}

	task void backup() {
		error_t retval;

		memcpy(&(recoveryBlock.data), currentBlock->data, DS_BLOCK_DATA_LEN);
		recoveryBlock.len = currentBlock->len;
		recoveryBlock.blockID = currentBlock->blockID;
		retval = call BlockWrite.write(BLOCK_SIZE * RECOVERY_BLOCK, &recoveryBlock, BLOCK_SIZE);
		
		if (retval != SUCCESS) {
//			printfUART("Write request failed (%u): addr: %u, blk: %p, len: %u\n", retval, BLOCK_SIZE * RECOVERY_BLOCK, &recoveryBlock, BLOCK_SIZE);
			processBusy = FALSE;
		}
//		printfUART("Writing backup block: blk: %p, bid: %lu/%u, boffset: %u\n", &recoveryBlock, recoveryBlock.blockID, RECOVERY_BLOCK, recoveryBlock.len);

		call LogBlock.sync();
    }
#endif

    command error_t LogBlock.read(blocksqnnbr_t firstBlock, blocksqnnbr_t lastBlock, uint8_t len) {
		error_t retval;

        if (!initDone) return EOFF;
//        if (blockSqnNbr > currentBlockID) return FAIL;

		m_reading = TRUE;
		firstRead = firstBlock;
		lastRead = lastBlock;
		readingLen = len;

		printf("LogBlock.read() : firstRead,lastRead = %d,%d, len = %d\n", firstRead, lastRead, readingLen);
		printfflush();
		retval = get(&readingBlock, firstRead);
		if (retval != SUCCESS) {
			return retval;
		}
		return SUCCESS;
	}
						
    error_t get(Block *blockPtr, blocksqnnbr_t blockSqnNbr) {
		error_t retval;
		uint16_t offsetCounter = 0;
		bool moreData = FALSE;

        if (!initDone) return EOFF;
		printf("LogBlock get() : blockID = %d\n", blockSqnNbr);
		printfflush();
        if (blockSqnNbr > currentBlockID) return FAIL;

		// if we haven't flushed to flash yet, we don't do a read
		if (blockSqnNbr == currentBlockID) {
//                printfUART("Using unflushed block for read: id: %lu\n", currentBlockID);
			currentBlock->len = currentBlockOffset;
			currentBlock->blockID = currentBlockID;
			memcpy((void *)blockPtr, currentBlock, BLOCK_SIZE);

				
//                signal LogBlock.getDone(blockPtr, currentBlockID, SUCCESS);

			moreData = FALSE;
//			while (offsetCounter < currentBlock->len) {
				signal LogBlock.readDone((blockPtr->data)+offsetCounter, currentBlock->len/readingLen, currentBlock->blockID, moreData, SUCCESS);
//				offsetCounter = offsetCounter + readingLen;
//			}

			m_reading = FALSE;				
			processBusy = FALSE;
		} else {
			retval = call BlockRead.read(BLOCK_SIZE * blockSqnNbr, (void *) blockPtr, BLOCK_SIZE);
			if (retval != SUCCESS) {
				signal LogBlock.readDone(blockPtr, 0, blockPtr->blockID, FALSE, FAIL);
				processBusy = FALSE;
				m_reading = FALSE;
			}
		}
				
		return SUCCESS;
    }

    command error_t LogBlock.erase() {
		printf("Erasing flash...\n");
		printfflush();
        if (!initDone) return EOFF;

		m_erase = TRUE;
		processBusy = TRUE;
		if (call BlockRead.read(BLOCK_SIZE * CONFIG_BLOCK, &configBlock, BLOCK_HEADER_LEN + configLen) != SUCCESS) {
			processBusy = FALSE;
			return EBUSY;
		}		
		return SUCCESS;
    }

    // SDH : Should flush unwritten tail of the log to flash.
    command error_t LogBlock.sync() {
/* 		error_t retval; */
		
/* 		currentBlock->len = currentBlockOffset; */
/* 		currentBlock->blockID = currentBlockID; */
/* 		retval = call BlockWrite.write(BLOCK_SIZE * currentBlockID, (void *)currentBlock, BLOCK_SIZE); */
			
/* 		if (retval != SUCCESS) { */
/* //                printfUART("Write request failed (%u): addr: %lu, data: %p, len: %u\n", retval, BLOCK_SIZE * currentBlockID, currentBlock, BLOCK_SIZE); */
/* 			processBusy = FALSE; */
/* 		} */
/* //			printfUART("Writing block: addr: %p, bid: %lu, boffset: %u\n", currentBlock, currentBlock->blockID, currentBlock->len); */
/* //			printf("Writing block: bid = %d, offset = %d\n", currentBlock->blockID, currentBlock->len); */
/* //			printfflush(); */
			
/* //            printfUART("Flushing and rotating, %d < %d\n", DS_BLOCK_DATA_LEN-currentBlockOffset, len); */
			
/* 		currentBlock = call BlockPool.get(); */
/* 		if (currentBlock == NULL) { */
/* 			return EBUSY; */
/* 		} */

/* 		if (currentBlockID + 1 >= VOLUME_SIZE / BLOCK_SIZE) { */
/* 			// Circular buffer */
/* 			// Assumes recovery block is before the data blocks */
/* 			// JKT: Need to add fix to not write circularly */
/* 			currentBlockID = RECOVERY_BLOCK + 1; */
/* 		} */
/* 		else { */
/* 			currentBlockID++; */
/* 		} */
		
/* 		currentBlockOffset = 0; */

/* 		// JKT: 30 ms wasn't enough for the first flash write to finish */
/* #ifdef KEEP_RECOVERY_BLOCK */
/* 		call WriteTimer.startOneShot(100); */
/* #endif */
		
        return call BlockWrite.sync();
    }

    command error_t LogBlock.readNextBlock() {
		bool retval;
		retval = get(&readingBlock, firstRead);
		if (retval != SUCCESS) {
			signal LogBlock.readDone(0, 0, firstRead, FALSE, FAIL);
			processBusy = FALSE;
			m_reading = FALSE;
		}
		return SUCCESS;
	}

    //
    // LogWrite events
    //

    event void BlockWrite.writeDone(storage_addr_t addr, void* buf, storage_len_t len, error_t error) {
//        printfUART("BlockWrite.writeDone(bid: %lu/%lu)\n", ((Block *) buf)->blockID, addr);

		if (addr != CONFIG_BLOCK*BLOCK_SIZE) {
			printf("Write done: addr = %ld, block = %d, err = %d, len = %d, m_e, m_wC = %d,%d\n", addr, ((Block *) buf)->blockID, error, len, m_erase, m_writeConfig);
		}
		else {
			printf("Write done: addr = %ld, err = %d, len = %d, m_e, m_wC = %d,%d\n", addr, error, len, m_erase, m_writeConfig);
		}
		printfflush();

		if (addr == CONFIG_BLOCK*BLOCK_SIZE && m_erase == TRUE) {
			m_erase = FALSE;
			signal LogBlock.eraseDone(error);
		}
		else if (addr == CONFIG_BLOCK*BLOCK_SIZE && m_writeConfig == TRUE) {
			m_writeConfig = FALSE;
			signal LogBlock.writeConfigDone(error);
		}
		else if (addr != RECOVERY_BLOCK*BLOCK_SIZE) {
			// Clear out the block before it goes back into the pool
			((Block *) (buf))->blockID = 0;
			((Block *) (buf))->len = 0;

			// JKT: setting the memory causes the block to have strange state -
			// not sure why
			// memset(((Block *) (currentRequest->data))->data, 0, DS_BLOCK_DATA_LEN);

			call BlockPool.put((Block *) buf);
		}

        processBusy = FALSE;
    }

    event void BlockWrite.eraseDone(error_t error) {
		printf("BlockWrite.eraseDone(), m_e, m_wC = %d,%d\n", m_erase, m_writeConfig);
		printfflush();		

		// Assumes data blocks are immediately after recovery block
        currentBlockID = RECOVERY_BLOCK + 1;
        currentBlockOffset = 0;
        // SDH : this should really dequeue pending requests and deal
        // with the buffers, but will probably never be an issue.

//        initDone = TRUE;

		// restore config block
		configBlock.last_blockID = currentBlockID;
		call BlockWrite.write(BLOCK_SIZE * CONFIG_BLOCK, &configBlock, BLOCK_HEADER_LEN + configBlock.len);
    }

    event void BlockWrite.syncDone(error_t error) {
        signal LogBlock.syncDone(error);
    }

    //
    // LogRead events
    //
    event void BlockRead.readDone(storage_addr_t addr, void* buf, storage_len_t len, error_t error) {		
        Block *readBlock = (Block *)buf;
		uint16_t offsetCounter = 0;
		bool moreData;
		
		if (m_config == TRUE) {
			printf("Read config done: addr = %ld, err = %d, len = %d\n", addr, error, len);
			printfflush();
			// send back configuration block
			if (addr == BLOCK_SIZE * CONFIG_BLOCK) {
				signal LogBlock.readConfigDone(((ConfigBlock *) buf)->data, ((ConfigBlock *) buf)->len, error);
			}
			else {
				// shouldn't reach here
			}
			
			processBusy = FALSE;
			m_config = FALSE;
		}
		else if (m_erase == TRUE) {
			printf("Read config done (erase): addr = %ld, err = %d, len = %d\n", addr, error, len);
			printfflush();
			memcpy((void *) &configBlock, buf, len);
			configLen = configBlock.len;
			call BlockWrite.erase();			
		}
		else if (m_recovery == FALSE) {

//        printfUART("readDonelu(%lu, %lu)\n", readBlock->blockID, currentRequest->blockID);
//			printf("BlockRead.readDone() : addr = %ld, err = %d, len = %d\n", addr, error, len);
//			printfflush();

			if (error == SUCCESS) {
				// Only set moreData if we are on the last sample of a non-final block
//				if (firstRead != lastRead && offsetCounter + readingLen >= readBlock->len) {
				if (firstRead != lastRead) {
					moreData = TRUE;
//					printf("LogBlock.readDone() : blkid: %d (%d), offsetCounter: %d, seqno: %d, moreData = %d\n", readBlock->blockID, readBlock->len, offsetCounter, ((plum_sample_t *) (readBlock->data+offsetCounter))->seqno, moreData);
//					printfflush();
				}
				else {
					moreData = FALSE;
				}
				
//				while (offsetCounter < readBlock->len) {
//					if (offsetCounter == 0 || moreData == TRUE) {
				printf("LogBlock.readDone() : blkid: %d (%d), offsetCounter: %d, seqno: %d, firstRead,lastRead = %d,%d, moreData = %d\n", readBlock->blockID, readBlock->len, offsetCounter, ((plum_sample_t *) (readBlock->data))->seqno, firstRead, lastRead, moreData);
				printfflush();
//					}
						signal LogBlock.readDone(readBlock->data, readBlock->len/readingLen, readBlock->blockID, moreData, SUCCESS);
//						signal LogBlock.readDone(readBlock->data+offsetCounter, readBlock->len/readingLen, readBlock->blockID, moreData, SUCCESS);
//					offsetCounter = offsetCounter + readingLen;
//				}

				if (firstRead == lastRead) {
					m_reading = FALSE;
					processBusy = FALSE;
				}
				else {
					if (readBlock->blockID + 1 >= VOLUME_SIZE / BLOCK_SIZE) {
						firstRead = RECOVERY_BLOCK + 1;
					}
					else {
						firstRead = firstRead + 1;
					}

//					retval = call BlockRead.read(BLOCK_SIZE * firstRead, (void *) buf, BLOCK_SIZE);
//					if (retval != SUCCESS) {
//						signal LogBlock.readDone(buf, firstRead, FALSE, FAIL);
//						processBusy = FALSE;
//						m_reading = FALSE;
//					}
				}
			}
			else {
				signal LogBlock.readDone(buf, 0, firstRead, FALSE, FAIL);
				processBusy = FALSE;
				m_reading = FALSE;
			}					
			
//			signal LogBlock.readDone(((Block *)buf)->data, readBlock->blockID, error);

//			signal LogBlock.getDone((Block *)buf, readBlock->blockID, error);
		}
		else {
			// JKT: need to find volume size not defined by LogBlock.h
			if (addr == CONFIG_BLOCK * BLOCK_SIZE) {
				configLen = ((ConfigBlock *)buf)->len;
				currentBlockID = ((ConfigBlock *)buf)->last_blockID;
				if (currentBlockID <= RECOVERY_BLOCK || currentBlockID >= VOLUME_SIZE / BLOCK_SIZE) {
					// Invalid block number
					currentBlockID = RECOVERY_BLOCK + 1;
				}
				else if (currentBlockID == (VOLUME_SIZE / BLOCK_SIZE) - 1) {
					// Need to circle around
					currentBlockID = RECOVERY_BLOCK + 1;
				}
				else {
					// Go to next block so we don't overwrite last block
					currentBlockID = currentBlockID + 1;
				}
				currentBlockOffset = 0;
			}
			else if (readBlock->blockID != RECOVERY_BLOCK && readBlock->blockID < VOLUME_SIZE / BLOCK_SIZE) {
				currentBlockID = readBlock->blockID;
				currentBlockOffset = readBlock->len;
			}
			else {
				currentBlockID = RECOVERY_BLOCK + 1;
				currentBlockOffset = 0;
			}
			m_recovery = FALSE;			
			processBusy = FALSE;
			initDone = TRUE;

//			printfUART("Recovered: bid: %lu,%lu, boffset: %u,%u, addr: %lu, len: %lu\n", currentBlockID, readBlock->blockID, currentBlockOffset, readBlock->len, addr, len);
		}
    }


    event void BlockRead.computeCrcDone(storage_addr_t addr, storage_len_t len,
                                        uint16_t crc, error_t error) {
        
    }

    default event void LogBlock.eraseDone(error_t error) { }
    default event void LogBlock.syncDone(error_t error) { }
}
