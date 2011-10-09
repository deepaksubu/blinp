#include "LogBlock.h"
#include "StorageVolumes.h"


configuration LogBlockC {

    provides interface LogBlock;

} implementation {

    components LogBlockP;
    components new PoolC(Block, DS_BLOCK_POOL) as BlockPool;
	components LedsC;

#ifdef KEEP_RECOVERY_BLOCK
	components new TimerMilliC() as WriteTimerC;  
	LogBlockP.WriteTimer -> WriteTimerC.Timer;
#endif
	
//	components UserButtonC;
//	LogBlockP.UserButtonNotify -> UserButtonC;
	
    components new BlockStorageC(VOLUME_HW_LOG);
    LogBlockP.BlockRead  -> BlockStorageC;
    LogBlockP.BlockWrite -> BlockStorageC;

    LogBlock = LogBlockP;

    LogBlockP.BlockPool -> BlockPool;
	LogBlockP.Leds -> LedsC;
}

