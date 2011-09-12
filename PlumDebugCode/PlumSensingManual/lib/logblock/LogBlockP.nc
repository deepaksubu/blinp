#include "LogBlock.h"
//#include "printf.h"

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
		error_t err;
		memcpy(&(configBlock.data), buf, len);
		configBlock.len = len;
		configBlock.last_blockID = currentBlockID;

        if (!initDone) return EOFF;

		m_writeConfig = TRUE;
		processBusy = TRUE;
		configLen = len;

		err = call BlockWrite.write(BLOCK_SIZE * CONFIG_BLOCK, &configBlock, BLOCK_HEADER_LEN + configBlock.len);
		if (err != SUCCESS) {
			processBusy = FALSE;
		}
		else {
			call LogBlock.sync();
		}

		return err;
	}

	command error_t LogBlock.readConfig(void *buf, uint8_t len) {
		error_t retval;

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
		
        if (!initDone) return EOFF;
        if (currentBlock == NULL) return EBUSY;
		
        if (DS_BLOCK_DATA_LEN - currentBlockOffset < len) {
            currentBlock->len = currentBlockOffset;
            currentBlock->blockID = currentBlockID;
			retval = call BlockWrite.write(BLOCK_SIZE * currentBlockID, (void *)currentBlock, BLOCK_SIZE);
			
            if (retval != SUCCESS) {
                processBusy = FALSE;
			}
			
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
			processBusy = FALSE;
		}

		call LogBlock.sync();
    }
#endif

    command error_t LogBlock.read(blocksqnnbr_t firstBlock, blocksqnnbr_t lastBlock, uint8_t len) {
		error_t retval;

        if (!initDone) return EOFF;

		m_reading = TRUE;
		firstRead = firstBlock;
		lastRead = lastBlock;
		readingLen = len;

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
        if (blockSqnNbr > currentBlockID) return FAIL;

		// if we haven't flushed to flash yet, we don't do a read
		if (blockSqnNbr == currentBlockID) {
			currentBlock->len = currentBlockOffset;
			currentBlock->blockID = currentBlockID;
			memcpy((void *)blockPtr, currentBlock, BLOCK_SIZE);
				
			moreData = FALSE;
			signal LogBlock.readDone((blockPtr->data)+offsetCounter, currentBlock->len/readingLen, currentBlock->blockID, moreData, SUCCESS);

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
		bool moreData;
		
		if (m_config == TRUE) {
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
			memcpy((void *) &configBlock, buf, len);
			configLen = configBlock.len;
			call BlockWrite.erase();			
		}
		else if (m_recovery == FALSE) {

			if (error == SUCCESS) {
				if (firstRead != lastRead) {
					moreData = TRUE;
				}
				else {
					moreData = FALSE;
				}
				
				signal LogBlock.readDone(readBlock->data, readBlock->len/readingLen, readBlock->blockID, moreData, SUCCESS);

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
				}
			}
			else {
				signal LogBlock.readDone(buf, 0, firstRead, FALSE, FAIL);
				processBusy = FALSE;
				m_reading = FALSE;
			}								
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

			signal LogBlock.initDone(SUCCESS);
		}
    }


    event void BlockRead.computeCrcDone(storage_addr_t addr, storage_len_t len,
                                        uint16_t crc, error_t error) {
        
    }

    default event void LogBlock.eraseDone(error_t error) { }
    default event void LogBlock.syncDone(error_t error) { }
}
