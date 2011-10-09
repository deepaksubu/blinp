#include "LogBlock.h"

interface LogBlock {

    command error_t init();

    command error_t append(void *data, uint8_t len, void *blkid);

    command error_t read(blocksqnnbr_t firstBlock, blocksqnnbr_t lastBlock, uint8_t len);

    event void readDone(void *data, uint16_t numSamples, blocksqnnbr_t blockSqnNbr, bool moreData, error_t error);

    command error_t readNextBlock();
	
//    command error_t get(Block *blockPtr, blocksqnnbr_t blockSqnNbr);

//    event void getDone(Block *blockPtr, blocksqnnbr_t blockSqnNbr, error_t error);

	command error_t writeConfig(void *buf, uint8_t len);

	command error_t readConfig(void *buf, uint8_t len);
	
	event void readConfigDone(void *config, uint8_t len, error_t error);
	
	event void writeConfigDone(error_t error);

    command error_t erase();

    event void eraseDone(error_t error);

    command error_t sync();

    event void syncDone(error_t error);

}
