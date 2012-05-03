#ifndef PLUM_H_
#define PLUM_H_

#define VOLUME_SIZE 1835008
#define BLOCK_SIZE 528

#define KEEP_CONFIG 1 // define if we should recover the config from the flash 

enum {
  MSG_QUEUE_SIZE = 50,
  BASE_STATION_ADDR = 0,
  MAX_SAMPLE_PERIOD = 30, // in seconds
  SAMPLE_PERIOD = 5L, // in seconds
  MAX_STATUS_PERIOD = 300, // in seconds
  STATUS_PERIOD = 30L, // in seconds
  LISTEN_PERIOD = 128 // in ms
};

enum plum_msgs {
  AM_PLUM_CMD_MSG = 50,
  AM_NX_PLUM_SAMPLE = 51,
  AM_NX_PLUM_STATUS = 52
};

enum plum_cmds {
  PLUM_SCAN = 1,
  PLUM_CONFIG = 2,
  PLUM_READ = 3,
  PLUM_ERASE = 4,
  PLUM_ERASE_CONFIG = 5,
  PLUM_TIME = 6
};

typedef struct plum_sample {
  uint16_t seqno;
  uint32_t lTime;
  uint32_t unixTime;
  uint16_t sender;
  uint16_t sampleRate;
  uint16_t statusRate;
  uint16_t intvol;
  uint16_t blockID;
} plum_sample_t;

typedef nx_struct nx_plum_sample {
  nx_uint16_t seqno;
  nx_uint32_t lTime;
  nx_uint32_t unixTime;
  nx_uint16_t sender;
  nx_uint16_t sampleRate;
  nx_uint16_t statusRate;
  nx_uint16_t intvol;
  nx_uint16_t blockID;
} nx_plum_sample_t;

typedef nx_struct plum_cmd_msg {
  nx_uint16_t cmdID;
  nx_uint16_t addr;
  nx_uint16_t sampleRate;
  nx_uint16_t statusRate;
  nx_uint16_t blockStart;
  nx_uint16_t blockEnd;
  nx_uint32_t unixTime;
} plum_cmd_msg_t;

typedef struct nx_plum_status {
  nx_uint16_t sender;
  nx_uint16_t last_seqno;
  nx_uint32_t last_unixTime;
  nx_uint16_t sampleRate;
  nx_uint16_t statusRate;
  nx_uint16_t intvol;
  nx_uint16_t first_blockID;
  nx_uint16_t last_blockID;
} nx_plum_status_t;

#endif
