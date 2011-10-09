#ifndef LOGBLOCK_H
#define LOGBLOCK_H

// If KEEP_RECOVERY_BLOCK is defined, after each sample, the current block will be written,
// regardless of whether it is full - this is meant to hedge against power failures.
// It is most useful in intermittent power situations, such as with solar energy harvesting.
// #define KEEP_RECOVERY_BLOCK

// AT45_PAGE_SIZE == 528 for AT45DB161 on Epic core
#define VOLUME_SIZE 1835008
// #define VOLUME_SIZE 262144
#define BLOCK_SIZE 528
#define CONFIG_BLOCK 0
#define RECOVERY_BLOCK 1

typedef uint16_t blocksqnnbr_t;

enum {
	BLOCK_HEADER_LEN = 4,
    DS_WORKQUEUE_LEN = 6,
    DS_BLOCK_POOL = 3,
    DS_BLOCK_DATA_LEN = BLOCK_SIZE - BLOCK_HEADER_LEN,
	CONFIG_BLOCK_DATA_LEN = BLOCK_SIZE - BLOCK_HEADER_LEN,
};

typedef struct {
    uint16_t len;
    blocksqnnbr_t blockID;
    char data[DS_BLOCK_DATA_LEN];
} Block;

typedef struct {
    uint16_t len;
    blocksqnnbr_t last_blockID;
    char data[CONFIG_BLOCK_DATA_LEN];
} ConfigBlock;


#endif
