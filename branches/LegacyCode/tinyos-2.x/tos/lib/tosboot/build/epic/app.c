#define nx_struct struct
#define nx_union union
#define dbg(mode, format, ...) ((void)0)
#define dbg_clear(mode, format, ...) ((void)0)
#define dbg_active(mode) 0
# 38 "/stow/lib/gcc-lib/../../msp430/include/sys/inttypes.h"
typedef signed char int8_t;
typedef unsigned char uint8_t;

typedef int int16_t;
typedef unsigned int uint16_t;

typedef long int32_t;
typedef unsigned long uint32_t;

typedef long long int64_t;
typedef unsigned long long uint64_t;




typedef int16_t intptr_t;
typedef uint16_t uintptr_t;
# 385 "/stow/repository/nesc-1.2.9/lib/ncc/nesc_nx.h"
typedef struct { char data[1]; } __attribute__((packed)) nx_int8_t;typedef int8_t __nesc_nxbase_nx_int8_t  ;
typedef struct { char data[2]; } __attribute__((packed)) nx_int16_t;typedef int16_t __nesc_nxbase_nx_int16_t  ;
typedef struct { char data[4]; } __attribute__((packed)) nx_int32_t;typedef int32_t __nesc_nxbase_nx_int32_t  ;
typedef struct { char data[8]; } __attribute__((packed)) nx_int64_t;typedef int64_t __nesc_nxbase_nx_int64_t  ;
typedef struct { char data[1]; } __attribute__((packed)) nx_uint8_t;typedef uint8_t __nesc_nxbase_nx_uint8_t  ;
typedef struct { char data[2]; } __attribute__((packed)) nx_uint16_t;typedef uint16_t __nesc_nxbase_nx_uint16_t  ;
typedef struct { char data[4]; } __attribute__((packed)) nx_uint32_t;typedef uint32_t __nesc_nxbase_nx_uint32_t  ;
typedef struct { char data[8]; } __attribute__((packed)) nx_uint64_t;typedef uint64_t __nesc_nxbase_nx_uint64_t  ;


typedef struct { char data[1]; } __attribute__((packed)) nxle_int8_t;typedef int8_t __nesc_nxbase_nxle_int8_t  ;
typedef struct { char data[2]; } __attribute__((packed)) nxle_int16_t;typedef int16_t __nesc_nxbase_nxle_int16_t  ;
typedef struct { char data[4]; } __attribute__((packed)) nxle_int32_t;typedef int32_t __nesc_nxbase_nxle_int32_t  ;
typedef struct { char data[8]; } __attribute__((packed)) nxle_int64_t;typedef int64_t __nesc_nxbase_nxle_int64_t  ;
typedef struct { char data[1]; } __attribute__((packed)) nxle_uint8_t;typedef uint8_t __nesc_nxbase_nxle_uint8_t  ;
typedef struct { char data[2]; } __attribute__((packed)) nxle_uint16_t;typedef uint16_t __nesc_nxbase_nxle_uint16_t  ;
typedef struct { char data[4]; } __attribute__((packed)) nxle_uint32_t;typedef uint32_t __nesc_nxbase_nxle_uint32_t  ;
typedef struct { char data[8]; } __attribute__((packed)) nxle_uint64_t;typedef uint64_t __nesc_nxbase_nxle_uint64_t  ;
# 151 "/stow/lib/gcc-lib/msp430/3.2.3/include/stddef.h" 3
typedef int ptrdiff_t;
#line 213
typedef unsigned int size_t;
#line 325
typedef int wchar_t;
# 41 "/stow/lib/gcc-lib/../../msp430/include/sys/types.h"
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;
typedef unsigned short ushort;
typedef unsigned int uint;

typedef uint8_t u_int8_t;
typedef uint16_t u_int16_t;
typedef uint32_t u_int32_t;
typedef uint64_t u_int64_t;

typedef u_int64_t u_quad_t;
typedef int64_t quad_t;
typedef quad_t *qaddr_t;

typedef char *caddr_t;
typedef const char *c_caddr_t;
typedef volatile char *v_caddr_t;
typedef u_int32_t fixpt_t;
typedef u_int32_t gid_t;
typedef u_int32_t in_addr_t;
typedef u_int16_t in_port_t;
typedef u_int32_t ino_t;
typedef long key_t;
typedef u_int16_t mode_t;
typedef u_int16_t nlink_t;
typedef quad_t rlim_t;
typedef int32_t segsz_t;
typedef int32_t swblk_t;
typedef int32_t ufs_daddr_t;
typedef int32_t ufs_time_t;
typedef u_int32_t uid_t;
# 38 "/stow/lib/gcc-lib/../../msp430/include/string.h"
extern void *memcpy(void *, const void *, size_t );
# 59 "/stow/lib/gcc-lib/../../msp430/include/stdlib.h"
#line 55
typedef struct __nesc_unnamed4242 {

  int quot;
  int rem;
} div_t;







#line 63
typedef struct __nesc_unnamed4243 {

  long quot;
  long rem;
} ldiv_t;
# 122 "/stow/lib/gcc-lib/../../msp430/include/sys/config.h" 3
typedef long int __int32_t;
typedef unsigned long int __uint32_t;
# 12 "/stow/lib/gcc-lib/../../msp430/include/sys/_types.h"
typedef long _off_t;
typedef long _ssize_t;
# 28 "/stow/lib/gcc-lib/../../msp430/include/sys/reent.h" 3
typedef __uint32_t __ULong;


struct _glue {

  struct _glue *_next;
  int _niobs;
  struct __sFILE *_iobs;
};

struct _Bigint {

  struct _Bigint *_next;
  int _k, _maxwds, _sign, _wds;
  __ULong _x[1];
};


struct __tm {

  int __tm_sec;
  int __tm_min;
  int __tm_hour;
  int __tm_mday;
  int __tm_mon;
  int __tm_year;
  int __tm_wday;
  int __tm_yday;
  int __tm_isdst;
};







struct _atexit {
  struct _atexit *_next;
  int _ind;
  void (*_fns[32])(void );
};








struct __sbuf {
  unsigned char *_base;
  int _size;
};






typedef long _fpos_t;
#line 116
struct __sFILE {
  unsigned char *_p;
  int _r;
  int _w;
  short _flags;
  short _file;
  struct __sbuf _bf;
  int _lbfsize;


  void *_cookie;

  int (*_read)(void *_cookie, char *_buf, int _n);
  int (*_write)(void *_cookie, const char *_buf, int _n);

  _fpos_t (*_seek)(void *_cookie, _fpos_t _offset, int _whence);
  int (*_close)(void *_cookie);


  struct __sbuf _ub;
  unsigned char *_up;
  int _ur;


  unsigned char _ubuf[3];
  unsigned char _nbuf[1];


  struct __sbuf _lb;


  int _blksize;
  int _offset;

  struct _reent *_data;
};
#line 174
struct _rand48 {
  unsigned short _seed[3];
  unsigned short _mult[3];
  unsigned short _add;
};









struct _reent {


  int _errno;




  struct __sFILE *_stdin, *_stdout, *_stderr;

  int _inc;
  char _emergency[25];

  int _current_category;
  const char *_current_locale;

  int __sdidinit;

  void (*__cleanup)(struct _reent *);


  struct _Bigint *_result;
  int _result_k;
  struct _Bigint *_p5s;
  struct _Bigint **_freelist;


  int _cvtlen;
  char *_cvtbuf;

  union __nesc_unnamed4244 {

    struct __nesc_unnamed4245 {

      unsigned int _unused_rand;
      char *_strtok_last;
      char _asctime_buf[26];
      struct __tm _localtime_buf;
      int _gamma_signgam;
      __extension__ unsigned long long _rand_next;
      struct _rand48 _r48;
    } _reent;



    struct __nesc_unnamed4246 {


      unsigned char *_nextf[30];
      unsigned int _nmalloc[30];
    } _unused;
  } _new;


  struct _atexit *_atexit;
  struct _atexit _atexit0;


  void (**_sig_func)(int );




  struct _glue __sglue;
  struct __sFILE __sf[3];
};
#line 273
struct _reent;
# 18 "/stow/lib/gcc-lib/../../msp430/include/math.h"
union __dmath {

  __uint32_t i[2];
  double d;
};




union __dmath;
#line 208
struct exception {


  int type;
  char *name;
  double arg1;
  double arg2;
  double retval;
  int err;
};
#line 261
enum __fdlibm_version {

  __fdlibm_ieee = -1, 
  __fdlibm_svid, 
  __fdlibm_xopen, 
  __fdlibm_posix
};




enum __fdlibm_version;
# 40 "/opt/tinyos-2.x/tos/lib/safe/include/annots_stage1.h"
struct __nesc_attr_safe {
};
#line 41
struct __nesc_attr_unsafe {
};
# 23 "/opt/tinyos-2.x/tos/system/tos.h"
typedef uint8_t bool;
enum __nesc_unnamed4247 {
#line 24
  FALSE = 0, TRUE = 1
};
typedef nx_int8_t nx_bool;







struct __nesc_attr_atmostonce {
};
#line 35
struct __nesc_attr_atleastonce {
};
#line 36
struct __nesc_attr_exactlyonce {
};
# 40 "/opt/tinyos-2.x/tos/types/TinyError.h"
enum __nesc_unnamed4248 {
  SUCCESS = 0, 
  FAIL = 1, 
  ESIZE = 2, 
  ECANCEL = 3, 
  EOFF = 4, 
  EBUSY = 5, 
  EINVAL = 6, 
  ERETRY = 7, 
  ERESERVE = 8, 
  EALREADY = 9, 
  ENOMEM = 10, 
  ENOACK = 11, 
  ELAST = 11
};

typedef uint8_t error_t  ;

static inline error_t ecombine(error_t r1, error_t r2)  ;
# 39 "/stow/lib/gcc-lib/../../msp430/include/msp430/iostructures.h"
#line 27
typedef union port {
  volatile unsigned char reg_p;
  volatile struct __nesc_unnamed4249 {
    unsigned char __p0 : 1, 
    __p1 : 1, 
    __p2 : 1, 
    __p3 : 1, 
    __p4 : 1, 
    __p5 : 1, 
    __p6 : 1, 
    __p7 : 1;
  } __pin;
} __attribute((packed))  ioregister_t;
# 108 "/stow/lib/gcc-lib/../../msp430/include/msp430/iostructures.h" 3
struct port_full_t {
  ioregister_t in;
  ioregister_t out;
  ioregister_t dir;
  ioregister_t ifg;
  ioregister_t ies;
  ioregister_t ie;
  ioregister_t sel;
};









struct port_simple_t {
  ioregister_t in;
  ioregister_t out;
  ioregister_t dir;
  ioregister_t sel;
};




struct port_full_t;



struct port_full_t;



struct port_simple_t;



struct port_simple_t;



struct port_simple_t;



struct port_simple_t;
# 116 "/stow/lib/gcc-lib/../../msp430/include/msp430/gpio.h" 3
volatile unsigned char P1OUT __asm ("0x0021");

volatile unsigned char P1DIR __asm ("0x0022");
#line 137
volatile unsigned char P2OUT __asm ("0x0029");

volatile unsigned char P2DIR __asm ("0x002A");
#line 158
volatile unsigned char P3OUT __asm ("0x0019");

volatile unsigned char P3DIR __asm ("0x001A");

volatile unsigned char P3SEL __asm ("0x001B");










volatile unsigned char P4OUT __asm ("0x001D");

volatile unsigned char P4DIR __asm ("0x001E");
#line 188
volatile unsigned char P5OUT __asm ("0x0031");

volatile unsigned char P5DIR __asm ("0x0032");
#line 203
volatile unsigned char P6OUT __asm ("0x0035");

volatile unsigned char P6DIR __asm ("0x0036");
# 97 "/stow/lib/gcc-lib/../../msp430/include/msp430/usart.h"
volatile unsigned char U0CTL __asm ("0x0070");

volatile unsigned char U0TCTL __asm ("0x0071");

volatile unsigned char U0RCTL __asm ("0x0072");



volatile unsigned char U0BR0 __asm ("0x0074");

volatile unsigned char U0BR1 __asm ("0x0075");

volatile unsigned char U0RXBUF __asm ("0x0076");

volatile unsigned char U0TXBUF __asm ("0x0077");
# 20 "/stow/lib/gcc-lib/../../msp430/include/msp430/flash.h"
volatile unsigned int FCTL1 __asm ("0x0128");

volatile unsigned int FCTL2 __asm ("0x012A");

volatile unsigned int FCTL3 __asm ("0x012C");
# 127 "/stow/lib/gcc-lib/../../msp430/include/msp430/timera.h" 3
#line 118
typedef struct __nesc_unnamed4250 {
  volatile unsigned 
  taifg : 1, 
  taie : 1, 
  taclr : 1, 
  dummy : 1, 
  tamc : 2, 
  taid : 2, 
  tassel : 2;
} __attribute((packed))  tactl_t;
#line 143
#line 129
typedef struct __nesc_unnamed4251 {
  volatile unsigned 
  ccifg : 1, 
  cov : 1, 
  out : 1, 
  cci : 1, 
  ccie : 1, 
  outmod : 3, 
  cap : 1, 
  dummy : 1, 
  scci : 1, 
  scs : 1, 
  ccis : 2, 
  cm : 2;
} __attribute((packed))  tacctl_t;


struct timera_t {
  tactl_t ctl;
  tacctl_t cctl0;
  tacctl_t cctl1;
  tacctl_t cctl2;
  volatile unsigned dummy[4];
  volatile unsigned tar;
  volatile unsigned taccr0;
  volatile unsigned taccr1;
  volatile unsigned taccr2;
};



struct timera_t;
# 76 "/stow/lib/gcc-lib/../../msp430/include/msp430/timerb.h"
#line 64
typedef struct __nesc_unnamed4252 {
  volatile unsigned 
  tbifg : 1, 
  tbie : 1, 
  tbclr : 1, 
  dummy1 : 1, 
  tbmc : 2, 
  tbid : 2, 
  tbssel : 2, 
  dummy2 : 1, 
  tbcntl : 2, 
  tbclgrp : 2;
} __attribute((packed))  tbctl_t;
#line 91
#line 78
typedef struct __nesc_unnamed4253 {
  volatile unsigned 
  ccifg : 1, 
  cov : 1, 
  out : 1, 
  cci : 1, 
  ccie : 1, 
  outmod : 3, 
  cap : 1, 
  clld : 2, 
  scs : 1, 
  ccis : 2, 
  cm : 2;
} __attribute((packed))  tbcctl_t;


struct timerb_t {
  tbctl_t ctl;
  tbcctl_t cctl0;
  tbcctl_t cctl1;
  tbcctl_t cctl2;

  tbcctl_t cctl3;
  tbcctl_t cctl4;
  tbcctl_t cctl5;
  tbcctl_t cctl6;



  volatile unsigned tbr;
  volatile unsigned tbccr0;
  volatile unsigned tbccr1;
  volatile unsigned tbccr2;

  volatile unsigned tbccr3;
  volatile unsigned tbccr4;
  volatile unsigned tbccr5;
  volatile unsigned tbccr6;
};





struct timerb_t;
# 20 "/stow/lib/gcc-lib/../../msp430/include/msp430/basic_clock.h"
volatile unsigned char DCOCTL __asm ("0x0056");

volatile unsigned char BCSCTL1 __asm ("0x0057");
# 18 "/stow/lib/gcc-lib/../../msp430/include/msp430/adc12.h"
volatile unsigned int ADC12CTL0 __asm ("0x01A0");

volatile unsigned int ADC12CTL1 __asm ("0x01A2");

volatile unsigned int ADC12IFG __asm ("0x01A4");
#line 42
#line 30
typedef struct __nesc_unnamed4254 {
  volatile unsigned 
  adc12sc : 1, 
  enc : 1, 
  adc12tovie : 1, 
  adc12ovie : 1, 
  adc12on : 1, 
  refon : 1, 
  r2_5v : 1, 
  msc : 1, 
  sht0 : 4, 
  sht1 : 4;
} __attribute((packed))  adc12ctl0_t;
#line 54
#line 44
typedef struct __nesc_unnamed4255 {
  volatile unsigned 
  adc12busy : 1, 
  conseq : 2, 
  adc12ssel : 2, 
  adc12div : 3, 
  issh : 1, 
  shp : 1, 
  shs : 2, 
  cstartadd : 4;
} __attribute((packed))  adc12ctl1_t;
#line 74
#line 56
typedef struct __nesc_unnamed4256 {
  volatile unsigned 
  bit0 : 1, 
  bit1 : 1, 
  bit2 : 1, 
  bit3 : 1, 
  bit4 : 1, 
  bit5 : 1, 
  bit6 : 1, 
  bit7 : 1, 
  bit8 : 1, 
  bit9 : 1, 
  bit10 : 1, 
  bit11 : 1, 
  bit12 : 1, 
  bit13 : 1, 
  bit14 : 1, 
  bit15 : 1;
} __attribute((packed))  adc12xflg_t;


struct adc12_t {
  adc12ctl0_t ctl0;
  adc12ctl1_t ctl1;
  adc12xflg_t ifg;
  adc12xflg_t ie;
  adc12xflg_t iv;
};




struct adc12_t;
#line 100
volatile unsigned int ADC12MEM0 __asm ("0x0140");
#line 139
volatile unsigned char ADC12MCTL0 __asm ("0x0080");
# 71 "/stow/lib/gcc-lib/../../msp430/include/msp430/common.h" 3
volatile unsigned int WDTCTL __asm ("0x0120");
# 65 "/stow/lib/gcc-lib/../../msp430/include/msp430x16x.h"
volatile unsigned char IFG1 __asm ("0x0002");
#line 83
volatile unsigned char ME1 __asm ("0x0004");
# 193 "/opt/tinyos-2.x/tos/chips/msp430/msp430hardware.h"
typedef uint8_t mcu_power_t  ;



enum __nesc_unnamed4257 {
  MSP430_POWER_ACTIVE = 0, 
  MSP430_POWER_LPM0 = 1, 
  MSP430_POWER_LPM1 = 2, 
  MSP430_POWER_LPM2 = 3, 
  MSP430_POWER_LPM3 = 4, 
  MSP430_POWER_LPM4 = 5
};

static inline void __nesc_disable_interrupt(void )  ;





static inline void __nesc_enable_interrupt(void )  ;




typedef bool __nesc_atomic_t;
__nesc_atomic_t __nesc_atomic_start(void );
void __nesc_atomic_end(__nesc_atomic_t reenable_interrupts);
#line 248
typedef struct { char data[4]; } __attribute__((packed)) nx_float;typedef float __nesc_nxbase_nx_float  ;
# 37 "epic/hardware.h"
typedef uint16_t in_flash_addr_t;

typedef uint32_t ex_flash_addr_t;

static inline void wait(uint16_t t);




static inline void TOSH_SET_RED_LED_PIN(void)  ;
#line 46
static inline void TOSH_CLR_RED_LED_PIN(void)  ;
static inline void TOSH_SET_GREEN_LED_PIN(void)  ;
#line 47
static inline void TOSH_CLR_GREEN_LED_PIN(void)  ;
static inline void TOSH_SET_YELLOW_LED_PIN(void)  ;
#line 48
static inline void TOSH_CLR_YELLOW_LED_PIN(void)  ;



static inline void TOSH_SET_SIMO0_PIN(void)  ;
#line 52
static inline void TOSH_CLR_SIMO0_PIN(void)  ;
#line 52
static inline void TOSH_MAKE_SIMO0_OUTPUT(void)  ;
static inline void TOSH_SET_UCLK0_PIN(void)  ;
#line 53
static inline void TOSH_CLR_UCLK0_PIN(void)  ;
#line 53
static inline void TOSH_MAKE_UCLK0_OUTPUT(void)  ;







static inline void TOSH_SET_FLASH_CS_PIN(void)  ;
#line 61
static inline void TOSH_CLR_FLASH_CS_PIN(void)  ;
#line 61
static inline void TOSH_MAKE_FLASH_CS_OUTPUT(void)  ;

static inline void TOSH_SET_PIN_DIRECTIONS(void );
# 42 "../net/Deluge/Deluge.h"
#line 30
typedef nx_struct DelugeIdent {
  nx_uint32_t uidhash;
  nx_uint32_t size;
  nx_uint8_t numPgs;
  nx_uint8_t reserved;
  nx_uint16_t crc;
  nx_uint8_t appname[16];
  nx_uint8_t username[16];
  nx_uint8_t hostname[16];
  nx_uint8_t platform[16];
  nx_uint32_t timestamp;
  nx_uint32_t userhash;
} __attribute__((packed)) DelugeIdent;

enum __nesc_unnamed4258 {
  DELUGE_INVALID_UID = 0xffffffff, 
  DELUGE_NUM_VOLUMES = 2, 
  DELUGE_KEY = 0xDE00, 
  DELUGE_AM_FLASH_VOL_MANAGER = 0x53, 
  DELUGE_AM_DELUGE_MANAGER = 0x54
};

enum __nesc_unnamed4259 {
  DELUGE_CMD_STOP = 1, 
  DELUGE_CMD_LOCAL_STOP = 2, 
  DELUGE_CMD_ONLY_DISSEMINATE = 3, 
  DELUGE_CMD_DISSEMINATE_AND_REPROGRAM = 4, 
  DELUGE_CMD_REPROGRAM = 5, 
  DELUGE_CMD_REBOOT = 6
};









#line 64
typedef nx_struct DelugeCmd {
  nx_uint8_t type;
  nx_uint32_t uidhash;
  nx_uint8_t imgNum;
  nx_uint32_t size;
} __attribute__((packed)) DelugeCmd;






#line 71
typedef struct BootArgs {
  uint16_t address;
  uint32_t imageAddr;
  uint8_t gestureCount;
  bool noReprogram;
} BootArgs;
# 31 "../net/Deluge/extra/telosb/TOSBoot_platform.h"
enum __nesc_unnamed4260 {
  TOSBOOT_ARGS_ADDR = 0x70, 
  TOSBOOT_GESTURE_MAX_COUNT = 3, 
  TOSBOOT_GOLDEN_IMG_ADDR = 0xf0000L, 
  TOSBOOT_INT_PAGE_SIZE = 512L
};
# 39 "/opt/tinyos-2.x/tos/chips/cc2420/CC2420.h"
typedef uint8_t cc2420_status_t;
#line 93
#line 87
typedef nx_struct security_header_t {
  unsigned char __nesc_filler0[1];


  nx_uint32_t frameCounter;
  nx_uint8_t keyID[1];
} __attribute__((packed)) security_header_t;
#line 113
#line 95
typedef nx_struct cc2420_header_t {
  nxle_uint8_t length;
  nxle_uint16_t fcf;
  nxle_uint8_t dsn;
  nxle_uint16_t destpan;
  nxle_uint16_t dest;
  nxle_uint16_t src;







  nxle_uint8_t network;


  nxle_uint8_t type;
} __attribute__((packed)) cc2420_header_t;





#line 118
typedef nx_struct cc2420_footer_t {
} __attribute__((packed)) cc2420_footer_t;
#line 143
#line 128
typedef nx_struct cc2420_metadata_t {
  nx_uint8_t rssi;
  nx_uint8_t lqi;
  nx_uint8_t tx_power;
  nx_bool crc;
  nx_bool ack;
  nx_bool timesync;
  nx_uint32_t timestamp;
  nx_uint16_t rxInterval;
} __attribute__((packed)) 





cc2420_metadata_t;





#line 146
typedef nx_struct cc2420_packet_t {
  cc2420_header_t packet;
  nx_uint8_t data[];
} __attribute__((packed)) cc2420_packet_t;
#line 179
enum __nesc_unnamed4261 {

  MAC_HEADER_SIZE = sizeof(cc2420_header_t ) - 1, 

  MAC_FOOTER_SIZE = sizeof(uint16_t ), 

  MAC_PACKET_SIZE = MAC_HEADER_SIZE + 28 + MAC_FOOTER_SIZE, 

  CC2420_SIZE = MAC_HEADER_SIZE + MAC_FOOTER_SIZE, 

  AM_OVERHEAD = 2
};

enum cc2420_enums {
  CC2420_TIME_ACK_TURNAROUND = 7, 
  CC2420_TIME_VREN = 20, 
  CC2420_TIME_SYMBOL = 2, 
  CC2420_BACKOFF_PERIOD = 20 / CC2420_TIME_SYMBOL, 
  CC2420_MIN_BACKOFF = 20 / CC2420_TIME_SYMBOL, 
  CC2420_ACK_WAIT_DELAY = 256
};

enum cc2420_status_enums {
  CC2420_STATUS_RSSI_VALID = 1 << 1, 
  CC2420_STATUS_LOCK = 1 << 2, 
  CC2420_STATUS_TX_ACTIVE = 1 << 3, 
  CC2420_STATUS_ENC_BUSY = 1 << 4, 
  CC2420_STATUS_TX_UNDERFLOW = 1 << 5, 
  CC2420_STATUS_XOSC16M_STABLE = 1 << 6
};

enum cc2420_config_reg_enums {
  CC2420_SNOP = 0x00, 
  CC2420_SXOSCON = 0x01, 
  CC2420_STXCAL = 0x02, 
  CC2420_SRXON = 0x03, 
  CC2420_STXON = 0x04, 
  CC2420_STXONCCA = 0x05, 
  CC2420_SRFOFF = 0x06, 
  CC2420_SXOSCOFF = 0x07, 
  CC2420_SFLUSHRX = 0x08, 
  CC2420_SFLUSHTX = 0x09, 
  CC2420_SACK = 0x0a, 
  CC2420_SACKPEND = 0x0b, 
  CC2420_SRXDEC = 0x0c, 
  CC2420_STXENC = 0x0d, 
  CC2420_SAES = 0x0e, 
  CC2420_MAIN = 0x10, 
  CC2420_MDMCTRL0 = 0x11, 
  CC2420_MDMCTRL1 = 0x12, 
  CC2420_RSSI = 0x13, 
  CC2420_SYNCWORD = 0x14, 
  CC2420_TXCTRL = 0x15, 
  CC2420_RXCTRL0 = 0x16, 
  CC2420_RXCTRL1 = 0x17, 
  CC2420_FSCTRL = 0x18, 
  CC2420_SECCTRL0 = 0x19, 
  CC2420_SECCTRL1 = 0x1a, 
  CC2420_BATTMON = 0x1b, 
  CC2420_IOCFG0 = 0x1c, 
  CC2420_IOCFG1 = 0x1d, 
  CC2420_MANFIDL = 0x1e, 
  CC2420_MANFIDH = 0x1f, 
  CC2420_FSMTC = 0x20, 
  CC2420_MANAND = 0x21, 
  CC2420_MANOR = 0x22, 
  CC2420_AGCCTRL = 0x23, 
  CC2420_AGCTST0 = 0x24, 
  CC2420_AGCTST1 = 0x25, 
  CC2420_AGCTST2 = 0x26, 
  CC2420_FSTST0 = 0x27, 
  CC2420_FSTST1 = 0x28, 
  CC2420_FSTST2 = 0x29, 
  CC2420_FSTST3 = 0x2a, 
  CC2420_RXBPFTST = 0x2b, 
  CC2420_FMSTATE = 0x2c, 
  CC2420_ADCTST = 0x2d, 
  CC2420_DACTST = 0x2e, 
  CC2420_TOPTST = 0x2f, 
  CC2420_TXFIFO = 0x3e, 
  CC2420_RXFIFO = 0x3f
};

enum cc2420_ram_addr_enums {
  CC2420_RAM_TXFIFO = 0x000, 
  CC2420_RAM_RXFIFO = 0x080, 
  CC2420_RAM_KEY0 = 0x100, 
  CC2420_RAM_RXNONCE = 0x110, 
  CC2420_RAM_SABUF = 0x120, 
  CC2420_RAM_KEY1 = 0x130, 
  CC2420_RAM_TXNONCE = 0x140, 
  CC2420_RAM_CBCSTATE = 0x150, 
  CC2420_RAM_IEEEADR = 0x160, 
  CC2420_RAM_PANID = 0x168, 
  CC2420_RAM_SHORTADR = 0x16a
};

enum cc2420_nonce_enums {
  CC2420_NONCE_BLOCK_COUNTER = 0, 
  CC2420_NONCE_KEY_SEQ_COUNTER = 2, 
  CC2420_NONCE_FRAME_COUNTER = 3, 
  CC2420_NONCE_SOURCE_ADDRESS = 7, 
  CC2420_NONCE_FLAGS = 15
};

enum cc2420_main_enums {
  CC2420_MAIN_RESETn = 15, 
  CC2420_MAIN_ENC_RESETn = 14, 
  CC2420_MAIN_DEMOD_RESETn = 13, 
  CC2420_MAIN_MOD_RESETn = 12, 
  CC2420_MAIN_FS_RESETn = 11, 
  CC2420_MAIN_XOSC16M_BYPASS = 0
};

enum cc2420_mdmctrl0_enums {
  CC2420_MDMCTRL0_RESERVED_FRAME_MODE = 13, 
  CC2420_MDMCTRL0_PAN_COORDINATOR = 12, 
  CC2420_MDMCTRL0_ADR_DECODE = 11, 
  CC2420_MDMCTRL0_CCA_HYST = 8, 
  CC2420_MDMCTRL0_CCA_MOD = 6, 
  CC2420_MDMCTRL0_AUTOCRC = 5, 
  CC2420_MDMCTRL0_AUTOACK = 4, 
  CC2420_MDMCTRL0_PREAMBLE_LENGTH = 0
};

enum cc2420_mdmctrl1_enums {
  CC2420_MDMCTRL1_CORR_THR = 6, 
  CC2420_MDMCTRL1_DEMOD_AVG_MODE = 5, 
  CC2420_MDMCTRL1_MODULATION_MODE = 4, 
  CC2420_MDMCTRL1_TX_MODE = 2, 
  CC2420_MDMCTRL1_RX_MODE = 0
};

enum cc2420_rssi_enums {
  CC2420_RSSI_CCA_THR = 8, 
  CC2420_RSSI_RSSI_VAL = 0
};

enum cc2420_syncword_enums {
  CC2420_SYNCWORD_SYNCWORD = 0
};

enum cc2420_txctrl_enums {
  CC2420_TXCTRL_TXMIXBUF_CUR = 14, 
  CC2420_TXCTRL_TX_TURNAROUND = 13, 
  CC2420_TXCTRL_TXMIX_CAP_ARRAY = 11, 
  CC2420_TXCTRL_TXMIX_CURRENT = 9, 
  CC2420_TXCTRL_PA_CURRENT = 6, 
  CC2420_TXCTRL_RESERVED = 5, 
  CC2420_TXCTRL_PA_LEVEL = 0
};

enum cc2420_rxctrl0_enums {
  CC2420_RXCTRL0_RXMIXBUF_CUR = 12, 
  CC2420_RXCTRL0_HIGH_LNA_GAIN = 10, 
  CC2420_RXCTRL0_MED_LNA_GAIN = 8, 
  CC2420_RXCTRL0_LOW_LNA_GAIN = 6, 
  CC2420_RXCTRL0_HIGH_LNA_CURRENT = 4, 
  CC2420_RXCTRL0_MED_LNA_CURRENT = 2, 
  CC2420_RXCTRL0_LOW_LNA_CURRENT = 0
};

enum cc2420_rxctrl1_enums {
  CC2420_RXCTRL1_RXBPF_LOCUR = 13, 
  CC2420_RXCTRL1_RXBPF_MIDCUR = 12, 
  CC2420_RXCTRL1_LOW_LOWGAIN = 11, 
  CC2420_RXCTRL1_MED_LOWGAIN = 10, 
  CC2420_RXCTRL1_HIGH_HGM = 9, 
  CC2420_RXCTRL1_MED_HGM = 8, 
  CC2420_RXCTRL1_LNA_CAP_ARRAY = 6, 
  CC2420_RXCTRL1_RXMIX_TAIL = 4, 
  CC2420_RXCTRL1_RXMIX_VCM = 2, 
  CC2420_RXCTRL1_RXMIX_CURRENT = 0
};

enum cc2420_rsctrl_enums {
  CC2420_FSCTRL_LOCK_THR = 14, 
  CC2420_FSCTRL_CAL_DONE = 13, 
  CC2420_FSCTRL_CAL_RUNNING = 12, 
  CC2420_FSCTRL_LOCK_LENGTH = 11, 
  CC2420_FSCTRL_LOCK_STATUS = 10, 
  CC2420_FSCTRL_FREQ = 0
};

enum cc2420_secctrl0_enums {
  CC2420_SECCTRL0_RXFIFO_PROTECTION = 9, 
  CC2420_SECCTRL0_SEC_CBC_HEAD = 8, 
  CC2420_SECCTRL0_SEC_SAKEYSEL = 7, 
  CC2420_SECCTRL0_SEC_TXKEYSEL = 6, 
  CC2420_SECCTRL0_SEC_RXKEYSEL = 5, 
  CC2420_SECCTRL0_SEC_M = 2, 
  CC2420_SECCTRL0_SEC_MODE = 0
};

enum cc2420_secctrl1_enums {
  CC2420_SECCTRL1_SEC_TXL = 8, 
  CC2420_SECCTRL1_SEC_RXL = 0
};

enum cc2420_battmon_enums {
  CC2420_BATTMON_BATT_OK = 6, 
  CC2420_BATTMON_BATTMON_EN = 5, 
  CC2420_BATTMON_BATTMON_VOLTAGE = 0
};

enum cc2420_iocfg0_enums {
  CC2420_IOCFG0_BCN_ACCEPT = 11, 
  CC2420_IOCFG0_FIFO_POLARITY = 10, 
  CC2420_IOCFG0_FIFOP_POLARITY = 9, 
  CC2420_IOCFG0_SFD_POLARITY = 8, 
  CC2420_IOCFG0_CCA_POLARITY = 7, 
  CC2420_IOCFG0_FIFOP_THR = 0
};

enum cc2420_iocfg1_enums {
  CC2420_IOCFG1_HSSD_SRC = 10, 
  CC2420_IOCFG1_SFDMUX = 5, 
  CC2420_IOCFG1_CCAMUX = 0
};

enum cc2420_manfidl_enums {
  CC2420_MANFIDL_PARTNUM = 12, 
  CC2420_MANFIDL_MANFID = 0
};

enum cc2420_manfidh_enums {
  CC2420_MANFIDH_VERSION = 12, 
  CC2420_MANFIDH_PARTNUM = 0
};

enum cc2420_fsmtc_enums {
  CC2420_FSMTC_TC_RXCHAIN2RX = 13, 
  CC2420_FSMTC_TC_SWITCH2TX = 10, 
  CC2420_FSMTC_TC_PAON2TX = 6, 
  CC2420_FSMTC_TC_TXEND2SWITCH = 3, 
  CC2420_FSMTC_TC_TXEND2PAOFF = 0
};

enum cc2420_sfdmux_enums {
  CC2420_SFDMUX_SFD = 0, 
  CC2420_SFDMUX_XOSC16M_STABLE = 24
};

enum cc2420_security_enums {
  CC2420_NO_SEC = 0, 
  CC2420_CBC_MAC = 1, 
  CC2420_CTR = 2, 
  CC2420_CCM = 3, 
  NO_SEC = 0, 
  CBC_MAC_4 = 1, 
  CBC_MAC_8 = 2, 
  CBC_MAC_16 = 3, 
  CTR = 4, 
  CCM_4 = 5, 
  CCM_8 = 6, 
  CCM_16 = 7
};




enum __nesc_unnamed4262 {

  CC2420_INVALID_TIMESTAMP = 0x80000000L
};
# 6 "/opt/tinyos-2.x/tos/types/AM.h"
typedef nx_uint8_t nx_am_id_t;
typedef nx_uint8_t nx_am_group_t;
typedef nx_uint16_t nx_am_addr_t;

typedef uint8_t am_id_t;
typedef uint8_t am_group_t;
typedef uint16_t am_addr_t;

enum __nesc_unnamed4263 {
  AM_BROADCAST_ADDR = 0xffff
};









enum __nesc_unnamed4264 {
  TOS_AM_GROUP = 0x22, 
  TOS_AM_ADDRESS = 1
};
# 72 "/opt/tinyos-2.x/tos/lib/serial/Serial.h"
typedef uint8_t uart_id_t;



enum __nesc_unnamed4265 {
  HDLC_FLAG_BYTE = 0x7e, 
  HDLC_CTLESC_BYTE = 0x7d
};



enum __nesc_unnamed4266 {
  TOS_SERIAL_ACTIVE_MESSAGE_ID = 0, 
  TOS_SERIAL_CC1000_ID = 1, 
  TOS_SERIAL_802_15_4_ID = 2, 
  TOS_SERIAL_UNKNOWN_ID = 255
};


enum __nesc_unnamed4267 {
  SERIAL_PROTO_ACK = 67, 
  SERIAL_PROTO_PACKET_ACK = 68, 
  SERIAL_PROTO_PACKET_NOACK = 69, 
  SERIAL_PROTO_PACKET_UNKNOWN = 255
};
#line 110
#line 98
typedef struct radio_stats {
  uint8_t version;
  uint8_t flags;
  uint8_t reserved;
  uint8_t platform;
  uint16_t MTU;
  uint16_t radio_crc_fail;
  uint16_t radio_queue_drops;
  uint16_t serial_crc_fail;
  uint16_t serial_tx_fail;
  uint16_t serial_short_packets;
  uint16_t serial_proto_drops;
} radio_stats_t;







#line 112
typedef nx_struct serial_header {
  nx_am_addr_t dest;
  nx_am_addr_t src;
  nx_uint8_t length;
  nx_am_group_t group;
  nx_am_id_t type;
} __attribute__((packed)) serial_header_t;




#line 120
typedef nx_struct serial_packet {
  serial_header_t header;
  nx_uint8_t data[];
} __attribute__((packed)) serial_packet_t;



#line 125
typedef nx_struct serial_metadata {
  nx_uint8_t ack;
} __attribute__((packed)) serial_metadata_t;
# 48 "/opt/tinyos-2.x/tos/platforms/epic/platform_message.h"
#line 45
typedef union message_header {
  cc2420_header_t cc2420;
  serial_header_t serial;
} message_header_t;



#line 50
typedef union TOSRadioFooter {
  cc2420_footer_t cc2420;
} message_footer_t;



#line 54
typedef union TOSRadioMetadata {
  cc2420_metadata_t cc2420;
} message_metadata_t;
# 19 "/opt/tinyos-2.x/tos/types/message.h"
#line 14
typedef nx_struct message_t {
  nx_uint8_t header[sizeof(message_header_t )];
  nx_uint8_t data[28];
  nx_uint8_t footer[sizeof(message_footer_t )];
  nx_uint8_t metadata[sizeof(message_metadata_t )];
} __attribute__((packed)) message_t;
# 50 "../net/Deluge/DelugePageTransfer.h"
typedef int32_t object_id_t;
typedef nx_int32_t nx_object_id_t;
typedef uint32_t object_size_t;
typedef nx_uint32_t nx_object_size_t;
typedef uint8_t page_num_t;
typedef nx_uint8_t nx_page_num_t;

enum __nesc_unnamed4268 {
  DELUGET2_PKT_PAYLOAD_SIZE = 28 - sizeof(nx_object_id_t ) - sizeof(nx_page_num_t ) - sizeof(nx_uint8_t ), 
  DELUGET2_BYTES_PER_PAGE = 1024, 
  DELUGET2_PKTS_PER_PAGE = (DELUGET2_BYTES_PER_PAGE - 1) / DELUGET2_PKT_PAYLOAD_SIZE + 1, 
  DELUGET2_PKT_BITVEC_SIZE = (DELUGET2_PKTS_PER_PAGE - 1) / 8 + 1, 

  DELUGE_PKT_PAYLOAD_SIZE = 23, 
  DELUGE_PKTS_PER_PAGE = 48, 
  DELUGE_BYTES_PER_PAGE = DELUGE_PKTS_PER_PAGE * DELUGE_PKT_PAYLOAD_SIZE, 

  DELUGE_VERSION = 2, 
  DELUGE_MAX_ADV_PERIOD_LOG2 = 22, 
  DELUGE_NUM_NEWDATA_ADVS_REQUIRED = 2, 
  DELUGE_NUM_MIN_ADV_PERIODS = 2, 
  DELUGE_MAX_NUM_REQ_TRIES = 1, 
  DELUGE_REBOOT_DELAY = 4, 
  DELUGE_FAILED_SEND_DELAY = 16, 
  DELUGE_MIN_DELAY = 16, 

  DELUGE_IDENT_SIZE = 128, 
  DELUGE_INVALID_ADDR = 0x7fffffffL, 
  DELUGE_MIN_ADV_PERIOD_LOG2 = 9, 
  DELUGE_MAX_REQ_DELAY = 0x1L << (DELUGE_MIN_ADV_PERIOD_LOG2 - 1), 
  DELUGE_NACK_TIMEOUT = DELUGE_MAX_REQ_DELAY >> 0x1, 
  DELUGE_MAX_IMAGE_SIZE = 128L * 1024L, 
  DELUGE_MAX_PAGES = 128, 
  DELUGE_CRC_SIZE = sizeof(uint16_t ), 
  DELUGE_CRC_BLOCK_SIZE = DELUGE_MAX_PAGES * DELUGE_CRC_SIZE, 
  DELUGE_GOLDEN_IMAGE_NUM = 0x0, 
  DELUGE_INVALID_OBJID = 0xff, 
  DELUGE_INVALID_PKTNUM = 0xff, 
  DELUGE_INVALID_PGNUM = 0xff, 
  DELUGE_QSIZE = 2
};






#line 92
typedef struct DelugeAdvTimer {
  uint32_t timer : 32;
  uint8_t periodLog2 : 8;
  bool overheard : 1;
  uint8_t newAdvs : 7;
} DelugeAdvTimer;







#line 99
typedef nx_struct DelugeObjDesc {
  nx_object_id_t objid;
  nx_page_num_t numPgs;
  nx_uint16_t crc;
  nx_page_num_t numPgsComplete;
  nx_uint8_t reserved;
} __attribute__((packed)) DelugeObjDesc;
# 42 "crc.h"
static inline uint16_t crcByte(uint16_t crc, uint8_t b);
# 56 "/opt/tinyos-2.x/tos/chips/msp430/usart/msp430usart.h"
#line 48
typedef enum __nesc_unnamed4269 {

  USART_NONE = 0, 
  USART_UART = 1, 
  USART_UART_TX = 2, 
  USART_UART_RX = 3, 
  USART_SPI = 4, 
  USART_I2C = 5
} msp430_usartmode_t;










#line 58
typedef struct __nesc_unnamed4270 {
  unsigned int swrst : 1;
  unsigned int mm : 1;
  unsigned int sync : 1;
  unsigned int listen : 1;
  unsigned int clen : 1;
  unsigned int spb : 1;
  unsigned int pev : 1;
  unsigned int pena : 1;
} __attribute((packed))  msp430_uctl_t;









#line 69
typedef struct __nesc_unnamed4271 {
  unsigned int txept : 1;
  unsigned int stc : 1;
  unsigned int txwake : 1;
  unsigned int urxse : 1;
  unsigned int ssel : 2;
  unsigned int ckpl : 1;
  unsigned int ckph : 1;
} __attribute((packed))  msp430_utctl_t;










#line 79
typedef struct __nesc_unnamed4272 {
  unsigned int rxerr : 1;
  unsigned int rxwake : 1;
  unsigned int urxwie : 1;
  unsigned int urxeie : 1;
  unsigned int brk : 1;
  unsigned int oe : 1;
  unsigned int pe : 1;
  unsigned int fe : 1;
} __attribute((packed))  msp430_urctl_t;
#line 116
#line 99
typedef struct __nesc_unnamed4273 {
  unsigned int ubr : 16;

  unsigned int  : 1;
  unsigned int mm : 1;
  unsigned int  : 1;
  unsigned int listen : 1;
  unsigned int clen : 1;
  unsigned int  : 3;

  unsigned int  : 1;
  unsigned int stc : 1;
  unsigned int  : 2;
  unsigned int ssel : 2;
  unsigned int ckpl : 1;
  unsigned int ckph : 1;
  unsigned int  : 0;
} msp430_spi_config_t;





#line 118
typedef struct __nesc_unnamed4274 {
  uint16_t ubr;
  uint8_t uctl;
  uint8_t utctl;
} msp430_spi_registers_t;




#line 124
typedef union __nesc_unnamed4275 {
  msp430_spi_config_t spiConfig;
  msp430_spi_registers_t spiRegisters;
} msp430_spi_union_config_t;
#line 169
#line 150
typedef enum __nesc_unnamed4276 {

  UBR_32KHZ_1200 = 0x001B, UMCTL_32KHZ_1200 = 0x94, 
  UBR_32KHZ_1800 = 0x0012, UMCTL_32KHZ_1800 = 0x84, 
  UBR_32KHZ_2400 = 0x000D, UMCTL_32KHZ_2400 = 0x6D, 
  UBR_32KHZ_4800 = 0x0006, UMCTL_32KHZ_4800 = 0x77, 
  UBR_32KHZ_9600 = 0x0003, UMCTL_32KHZ_9600 = 0x29, 

  UBR_1MHZ_1200 = 0x0369, UMCTL_1MHZ_1200 = 0x7B, 
  UBR_1MHZ_1800 = 0x0246, UMCTL_1MHZ_1800 = 0x55, 
  UBR_1MHZ_2400 = 0x01B4, UMCTL_1MHZ_2400 = 0xDF, 
  UBR_1MHZ_4800 = 0x00DA, UMCTL_1MHZ_4800 = 0xAA, 
  UBR_1MHZ_9600 = 0x006D, UMCTL_1MHZ_9600 = 0x44, 
  UBR_1MHZ_19200 = 0x0036, UMCTL_1MHZ_19200 = 0xB5, 
  UBR_1MHZ_38400 = 0x001B, UMCTL_1MHZ_38400 = 0x94, 
  UBR_1MHZ_57600 = 0x0012, UMCTL_1MHZ_57600 = 0x84, 
  UBR_1MHZ_76800 = 0x000D, UMCTL_1MHZ_76800 = 0x6D, 
  UBR_1MHZ_115200 = 0x0009, UMCTL_1MHZ_115200 = 0x10, 
  UBR_1MHZ_230400 = 0x0004, UMCTL_1MHZ_230400 = 0x55
} msp430_uart_rate_t;
#line 200
#line 171
typedef struct __nesc_unnamed4277 {
  unsigned int ubr : 16;

  unsigned int umctl : 8;

  unsigned int  : 1;
  unsigned int mm : 1;
  unsigned int  : 1;
  unsigned int listen : 1;
  unsigned int clen : 1;
  unsigned int spb : 1;
  unsigned int pev : 1;
  unsigned int pena : 1;
  unsigned int  : 0;

  unsigned int  : 3;
  unsigned int urxse : 1;
  unsigned int ssel : 2;
  unsigned int ckpl : 1;
  unsigned int  : 1;

  unsigned int  : 2;
  unsigned int urxwie : 1;
  unsigned int urxeie : 1;
  unsigned int  : 4;
  unsigned int  : 0;

  unsigned int utxe : 1;
  unsigned int urxe : 1;
} msp430_uart_config_t;








#line 202
typedef struct __nesc_unnamed4278 {
  uint16_t ubr;
  uint8_t umctl;
  uint8_t uctl;
  uint8_t utctl;
  uint8_t urctl;
  uint8_t ume;
} msp430_uart_registers_t;




#line 211
typedef union __nesc_unnamed4279 {
  msp430_uart_config_t uartConfig;
  msp430_uart_registers_t uartRegisters;
} msp430_uart_union_config_t;
#line 248
#line 240
typedef struct __nesc_unnamed4280 {
  unsigned int i2cstt : 1;
  unsigned int i2cstp : 1;
  unsigned int i2cstb : 1;
  unsigned int i2cctrx : 1;
  unsigned int i2cssel : 2;
  unsigned int i2ccrm : 1;
  unsigned int i2cword : 1;
} __attribute((packed))  msp430_i2ctctl_t;
#line 276
#line 253
typedef struct __nesc_unnamed4281 {
  unsigned int  : 1;
  unsigned int mst : 1;
  unsigned int  : 1;
  unsigned int listen : 1;
  unsigned int xa : 1;
  unsigned int  : 1;
  unsigned int txdmaen : 1;
  unsigned int rxdmaen : 1;

  unsigned int  : 4;
  unsigned int i2cssel : 2;
  unsigned int i2crm : 1;
  unsigned int i2cword : 1;

  unsigned int i2cpsc : 8;

  unsigned int i2csclh : 8;

  unsigned int i2cscll : 8;

  unsigned int i2coa : 10;
  unsigned int  : 6;
} msp430_i2c_config_t;








#line 278
typedef struct __nesc_unnamed4282 {
  uint8_t uctl;
  uint8_t i2ctctl;
  uint8_t i2cpsc;
  uint8_t i2csclh;
  uint8_t i2cscll;
  uint16_t i2coa;
} msp430_i2c_registers_t;




#line 287
typedef union __nesc_unnamed4283 {
  msp430_i2c_config_t i2cConfig;
  msp430_i2c_registers_t i2cRegisters;
} msp430_i2c_union_config_t;
#line 309
typedef uint8_t uart_speed_t;
typedef uint8_t uart_parity_t;
typedef uint8_t uart_duplex_t;

enum __nesc_unnamed4284 {
  TOS_UART_1200 = 0, 
  TOS_UART_1800 = 1, 
  TOS_UART_2400 = 2, 
  TOS_UART_4800 = 3, 
  TOS_UART_9600 = 4, 
  TOS_UART_19200 = 5, 
  TOS_UART_38400 = 6, 
  TOS_UART_57600 = 7, 
  TOS_UART_76800 = 8, 
  TOS_UART_115200 = 9, 
  TOS_UART_230400 = 10
};

enum __nesc_unnamed4285 {
  TOS_UART_OFF, 
  TOS_UART_RONLY, 
  TOS_UART_TONLY, 
  TOS_UART_DUPLEX
};

enum __nesc_unnamed4286 {
  TOS_UART_PARITY_NONE, 
  TOS_UART_PARITY_EVEN, 
  TOS_UART_PARITY_ODD
};
# 3 "Exec.nc"
static  void ExecC$Exec$exec(void);
# 32 "ExtFlash.nc"
static  void ExtFlashM$ExtFlash$startRead(uint32_t arg_0x11dd770);

static  void ExtFlashM$ExtFlash$stopRead(void);
#line 33
static  uint8_t ExtFlashM$ExtFlash$readByte(void);
# 51 "/opt/tinyos-2.x/tos/interfaces/Init.nc"
static  error_t ExtFlashM$Init$init(void);
# 74 "/opt/tinyos-2.x/tos/interfaces/StdControl.nc"
static  error_t ExtFlashM$StdControl$start(void);









static  error_t ExtFlashM$StdControl$stop(void);
# 35 "msp430/HPLUSARTControl.nc"
static  void HPLUSART0M$HPLUSARTControl$disableSPI(void);



static  error_t HPLUSART0M$HPLUSARTControl$isTxEmpty(void);
#line 36
static  void HPLUSART0M$HPLUSARTControl$setModeSPI(void);





static  void HPLUSART0M$HPLUSARTControl$tx(uint8_t arg_0x123bce8);
static  uint8_t HPLUSART0M$HPLUSARTControl$rx(void);
#line 41
static  error_t HPLUSART0M$HPLUSARTControl$isRxIntrPending(void);
# 32 "Hardware.nc"
static  void HardwareC$Hardware$init(void);
static  void HardwareC$Hardware$reboot(void);
# 48 "/opt/tinyos-2.x/tos/interfaces/InternalFlash.nc"
static  error_t InternalFlashC$InternalFlash$read(void *arg_0x11e2928, void *arg_0x11e2ac8, uint16_t arg_0x11e2c58);
#line 60
static  error_t InternalFlashC$InternalFlash$write(void *arg_0x11f1348, void *arg_0x11f14e8, uint16_t arg_0x11f1678);
# 34 "Leds.nc"
static  void LedsC$Leds$glow(uint8_t arg_0x11eb4e8, uint8_t arg_0x11eb670);
#line 33
static  void LedsC$Leds$flash(uint8_t arg_0x11eb068);
#line 32
static  void LedsC$Leds$set(uint8_t arg_0x11ecba8);
# 74 "/opt/tinyos-2.x/tos/interfaces/StdControl.nc"
static  error_t PowerOffM$StdControl$start(void);









static  error_t PowerOffM$StdControl$stop(void);
# 32 "ProgFlash.nc"
static  error_t ProgFlashM$ProgFlash$write(in_flash_addr_t arg_0x11f6a48, uint8_t *arg_0x11f6bf0, in_flash_addr_t arg_0x11f6d80);
# 32 "Voltage.nc"
static  bool VoltageC$Voltage$okToProgram(void);
# 32 "Hardware.nc"
static  void TOSBootM$Hardware$init(void);
static  void TOSBootM$Hardware$reboot(void);
# 32 "ExtFlash.nc"
static  void TOSBootM$ExtFlash$startRead(uint32_t arg_0x11dd770);

static  void TOSBootM$ExtFlash$stopRead(void);
#line 33
static  uint8_t TOSBootM$ExtFlash$readByte(void);
# 32 "ProgFlash.nc"
static  error_t TOSBootM$ProgFlash$write(in_flash_addr_t arg_0x11f6a48, uint8_t *arg_0x11f6bf0, in_flash_addr_t arg_0x11f6d80);
# 51 "/opt/tinyos-2.x/tos/interfaces/Init.nc"
static  error_t TOSBootM$SubInit$init(void);
# 74 "/opt/tinyos-2.x/tos/interfaces/StdControl.nc"
static  error_t TOSBootM$SubControl$start(void);









static  error_t TOSBootM$SubControl$stop(void);
# 32 "Voltage.nc"
static  bool TOSBootM$Voltage$okToProgram(void);
# 34 "Leds.nc"
static  void TOSBootM$Leds$glow(uint8_t arg_0x11eb4e8, uint8_t arg_0x11eb670);
#line 33
static  void TOSBootM$Leds$flash(uint8_t arg_0x11eb068);
#line 32
static  void TOSBootM$Leds$set(uint8_t arg_0x11ecba8);
# 3 "Exec.nc"
static  void TOSBootM$Exec$exec(void);
# 48 "/opt/tinyos-2.x/tos/interfaces/InternalFlash.nc"
static  error_t TOSBootM$IntFlash$read(void *arg_0x11e2928, void *arg_0x11e2ac8, uint16_t arg_0x11e2c58);
#line 60
static  error_t TOSBootM$IntFlash$write(void *arg_0x11f1348, void *arg_0x11f14e8, uint16_t arg_0x11f1678);
# 50 "TOSBootM.nc"
enum TOSBootM$__nesc_unnamed4287 {
  TOSBootM$LEDS_LOWBATT = 1, 
  TOSBootM$LEDS_GESTURE = 7
};

enum TOSBootM$__nesc_unnamed4288 {
  TOSBootM$R_SUCCESS, 
  TOSBootM$R_INVALID_IMAGE_ERROR, 
  TOSBootM$R_PROGRAMMING_ERROR
};

static void TOSBootM$startupLeds(void);









static in_flash_addr_t TOSBootM$extFlashReadAddr(void);







static bool TOSBootM$verifyBlock(ex_flash_addr_t crcAddr, ex_flash_addr_t startAddr, uint16_t len);
#line 98
static inline bool TOSBootM$verifyImage(ex_flash_addr_t startAddr);
#line 130
static error_t TOSBootM$programImage(ex_flash_addr_t startAddr);
#line 204
static inline void TOSBootM$runApp(void);




static inline void TOSBootM$startupSequence(void);
#line 263
int main(void)   ;
# 36 "lib/ExecC.nc"
static inline  void ExecC$Exec$exec(void);
# 35 "msp430/HPLUSARTControl.nc"
static  void ExtFlashM$USARTControl$disableSPI(void);



static  error_t ExtFlashM$USARTControl$isTxEmpty(void);
#line 36
static  void ExtFlashM$USARTControl$setModeSPI(void);





static  void ExtFlashM$USARTControl$tx(uint8_t arg_0x123bce8);
static  uint8_t ExtFlashM$USARTControl$rx(void);
#line 41
static  error_t ExtFlashM$USARTControl$isRxIntrPending(void);
# 41 "epic/ExtFlashM.nc"
uint32_t ExtFlashM$addr;

static __inline void ExtFlashM$TOSH_wait(void);




static void ExtFlashM$TOSH_FLASH_AT45_DP_bit(bool set);








static inline  error_t ExtFlashM$Init$init(void);
#line 92
static inline  error_t ExtFlashM$StdControl$start(void);



static inline  error_t ExtFlashM$StdControl$stop(void);




static  void ExtFlashM$ExtFlash$startRead(uint32_t newAddr);
#line 123
static  uint8_t ExtFlashM$ExtFlash$readByte(void);
#line 135
static inline  void ExtFlashM$ExtFlash$stopRead(void);
# 36 "msp430/HPLUSART0M.nc"
static inline  void HPLUSART0M$HPLUSARTControl$disableSPI(void);










static inline  void HPLUSART0M$HPLUSARTControl$setModeSPI(void);
#line 97
static inline  error_t HPLUSART0M$HPLUSARTControl$isTxEmpty(void);
#line 112
static inline  error_t HPLUSART0M$HPLUSARTControl$isRxIntrPending(void);







static inline  void HPLUSART0M$HPLUSARTControl$tx(uint8_t data);



static inline  uint8_t HPLUSART0M$HPLUSARTControl$rx(void);
# 39 "msp430/HardwareC.nc"
static inline  void HardwareC$Hardware$init(void);




static inline  void HardwareC$Hardware$reboot(void);
# 48 "msp430/InternalFlashC.nc"
enum InternalFlashC$__nesc_unnamed4289 {
  InternalFlashC$IFLASH_OFFSET = 0x1000, 
  InternalFlashC$IFLASH_SIZE = 128, 
  InternalFlashC$IFLASH_SEG0_VNUM_ADDR = 0x107f, 
  InternalFlashC$IFLASH_SEG1_VNUM_ADDR = 0x10ff, 
  InternalFlashC$IFLASH_INVALID_VNUM = -1
};

static uint8_t InternalFlashC$chooseSegment(void);









static  error_t InternalFlashC$InternalFlash$write(void *addr, void *buf, uint16_t size);
#line 108
static inline  error_t InternalFlashC$InternalFlash$read(void *addr, void *buf, uint16_t size);
# 37 "lib/LedsC.nc"
enum LedsC$__nesc_unnamed4290 {
  LedsC$RED_BIT = 1, 
  LedsC$GREEN_BIT = 2, 
  LedsC$YELLOW_BIT = 4
};

static  void LedsC$Leds$set(uint8_t ledsOn);
#line 58
static  void LedsC$Leds$flash(uint8_t a);
#line 70
static inline  void LedsC$Leds$glow(uint8_t a, uint8_t b);
# 65 "msp430f1611/PowerOffM.nc"
static inline  error_t PowerOffM$StdControl$start(void);
#line 81
static inline  error_t PowerOffM$StdControl$stop(void);
# 39 "msp430/ProgFlashM.nc"
enum ProgFlashM$__nesc_unnamed4291 {
  ProgFlashM$RESET_ADDR = 0xfffe
};

static inline  error_t ProgFlashM$ProgFlash$write(in_flash_addr_t addr, uint8_t *buf, uint16_t len);
# 39 "msp430/VoltageC.nc"
enum VoltageC$__nesc_unnamed4292 {
  VoltageC$VTHRESH = 0xE66
};

static inline  bool VoltageC$Voltage$okToProgram(void);
# 50 "/opt/tinyos-2.x/tos/system/SchedulerBasicP.nc"
enum SchedulerBasicP$__nesc_unnamed4293 {

  SchedulerBasicP$NUM_TASKS = 0U, 
  SchedulerBasicP$NO_TASK = 255
};
# 206 "/opt/tinyos-2.x/tos/chips/msp430/msp430hardware.h"
static inline  void __nesc_disable_interrupt(void )
{
   __asm volatile ("dint");
   __asm volatile ("nop");}

# 63 "epic/hardware.h"
static inline void TOSH_SET_PIN_DIRECTIONS(void )
{


  P1DIR = 0xe0;
  P1OUT = 0x00;


  P2DIR = 0x7b;
  P2OUT = 0x10;

  P3DIR = 0xff;
  P3OUT = 0x0e;

  P4DIR = 0xff;
  P4OUT = 0xdd;

  P5DIR = 0xff;
  P5OUT = 0xff;

  P6DIR = 0xff;
  P6OUT = 0x00;
}

# 39 "msp430/HardwareC.nc"
static inline  void HardwareC$Hardware$init(void)
#line 39
{
  BCSCTL1 = ((0x01 | 0x02) | 0x04) | 0x80;
  DCOCTL = (0x20 | 0x40) | 0x80;
}

# 32 "Hardware.nc"
inline static  void TOSBootM$Hardware$init(void){
#line 32
  HardwareC$Hardware$init();
#line 32
}
#line 32
# 47 "msp430/HPLUSART0M.nc"
static inline  void HPLUSART0M$HPLUSARTControl$setModeSPI(void)
#line 47
{




  U0CTL = ((0x01 | 0x10) | 0x04) | 0x02;


  U0TCTL |= 0x02 + 0x80 + 0x20;


  U0BR0 = 0x02;
  U0BR1 = 0;


  ME1 |= 1 << 6;

  U0CTL &= ~0x01;


  IFG1 = 0;
}

# 36 "msp430/HPLUSARTControl.nc"
inline static  void ExtFlashM$USARTControl$setModeSPI(void){
#line 36
  HPLUSART0M$HPLUSARTControl$setModeSPI();
#line 36
}
#line 36
# 43 "epic/ExtFlashM.nc"
static __inline void ExtFlashM$TOSH_wait(void)
#line 43
{
   __asm volatile ("nop"); __asm volatile ("nop");}

# 52 "epic/hardware.h"
static inline  void TOSH_SET_SIMO0_PIN(void)
#line 52
{
#line 52
   static volatile uint8_t r __asm ("0x0019");

#line 52
  r |= 1 << 1;
}

#line 53
static inline  void TOSH_SET_UCLK0_PIN(void)
#line 53
{
#line 53
   static volatile uint8_t r __asm ("0x0019");

#line 53
  r |= 1 << 3;
}






static inline  void TOSH_SET_FLASH_CS_PIN(void)
#line 61
{
#line 61
   static volatile uint8_t r __asm ("0x001D");

#line 61
  r |= 1 << 4;
}

#line 53
static inline  void TOSH_CLR_UCLK0_PIN(void)
#line 53
{
#line 53
   static volatile uint8_t r __asm ("0x0019");

#line 53
  r &= ~(1 << 3);
}






static inline  void TOSH_CLR_FLASH_CS_PIN(void)
#line 61
{
#line 61
   static volatile uint8_t r __asm ("0x001D");

#line 61
  r &= ~(1 << 4);
}

#line 61
static inline  void TOSH_MAKE_FLASH_CS_OUTPUT(void)
#line 61
{
#line 61
   static volatile uint8_t r __asm ("0x001E");

#line 61
  r |= 1 << 4;
}

#line 53
static inline  void TOSH_MAKE_UCLK0_OUTPUT(void)
#line 53
{
#line 53
   static volatile uint8_t r __asm ("0x001A");

#line 53
  r |= 1 << 3;
}

#line 52
static inline  void TOSH_MAKE_SIMO0_OUTPUT(void)
#line 52
{
#line 52
   static volatile uint8_t r __asm ("0x001A");

#line 52
  r |= 1 << 1;
}

# 57 "epic/ExtFlashM.nc"
static inline  error_t ExtFlashM$Init$init(void)
#line 57
{

  P3SEL = 0;

  TOSH_MAKE_SIMO0_OUTPUT();
  TOSH_MAKE_UCLK0_OUTPUT();
  TOSH_MAKE_FLASH_CS_OUTPUT();
  TOSH_SET_FLASH_CS_PIN();

  ExtFlashM$TOSH_wait();


  TOSH_CLR_FLASH_CS_PIN();
  TOSH_CLR_UCLK0_PIN();


  ExtFlashM$TOSH_FLASH_AT45_DP_bit(TRUE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(FALSE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(TRUE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(FALSE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(TRUE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(FALSE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(TRUE);
  ExtFlashM$TOSH_FLASH_AT45_DP_bit(TRUE);

  TOSH_SET_FLASH_CS_PIN();
  TOSH_SET_UCLK0_PIN();
  TOSH_SET_SIMO0_PIN();

  P3SEL = 0x0e;
  ExtFlashM$TOSH_wait();
  ExtFlashM$USARTControl$setModeSPI();
  return SUCCESS;
}

# 51 "/opt/tinyos-2.x/tos/interfaces/Init.nc"
inline static  error_t TOSBootM$SubInit$init(void){
#line 51
  unsigned char result;
#line 51

#line 51
  result = ExtFlashM$Init$init();
#line 51

#line 51
  return result;
#line 51
}
#line 51
# 52 "epic/hardware.h"
static inline  void TOSH_CLR_SIMO0_PIN(void)
#line 52
{
#line 52
   static volatile uint8_t r __asm ("0x0019");

#line 52
  r &= ~(1 << 1);
}

# 58 "/opt/tinyos-2.x/tos/types/TinyError.h"
static inline  error_t ecombine(error_t r1, error_t r2)




{
  return r1 == r2 ? r1 : FAIL;
}

# 41 "epic/hardware.h"
static inline void wait(uint16_t t)
#line 41
{
  for (; t > 0; t--) ;
}

# 65 "msp430f1611/PowerOffM.nc"
static inline  error_t PowerOffM$StdControl$start(void)
#line 65
{

  int i;


  for (i = 0; i < 4; i++) 
    wait(0xffff);





  return SUCCESS;
}

# 92 "epic/ExtFlashM.nc"
static inline  error_t ExtFlashM$StdControl$start(void)
#line 92
{
  return SUCCESS;
}

# 74 "/opt/tinyos-2.x/tos/interfaces/StdControl.nc"
inline static  error_t TOSBootM$SubControl$start(void){
#line 74
  unsigned char result;
#line 74

#line 74
  result = ExtFlashM$StdControl$start();
#line 74
  result = ecombine(result, PowerOffM$StdControl$start());
#line 74

#line 74
  return result;
#line 74
}
#line 74
# 36 "lib/ExecC.nc"
static inline  void ExecC$Exec$exec(void)
#line 36
{



  typedef void __attribute((noreturn)) (*tosboot_exec)(void);

  (
#line 41
  (tosboot_exec )0x4a00)();
}

# 3 "Exec.nc"
inline static  void TOSBootM$Exec$exec(void){
#line 3
  ExecC$Exec$exec();
#line 3
}
#line 3
# 204 "TOSBootM.nc"
static inline void TOSBootM$runApp(void)
#line 204
{
  TOSBootM$SubControl$stop();
  TOSBootM$Exec$exec();
}

# 60 "/opt/tinyos-2.x/tos/interfaces/InternalFlash.nc"
inline static  error_t TOSBootM$IntFlash$write(void *arg_0x11f1348, void *arg_0x11f14e8, uint16_t arg_0x11f1678){
#line 60
  unsigned char result;
#line 60

#line 60
  result = InternalFlashC$InternalFlash$write(arg_0x11f1348, arg_0x11f14e8, arg_0x11f1678);
#line 60

#line 60
  return result;
#line 60
}
#line 60
# 44 "msp430/HardwareC.nc"
static inline  void HardwareC$Hardware$reboot(void)
#line 44
{
  WDTCTL = 0;
}

# 33 "Hardware.nc"
inline static  void TOSBootM$Hardware$reboot(void){
#line 33
  HardwareC$Hardware$reboot();
#line 33
}
#line 33
# 33 "Leds.nc"
inline static  void TOSBootM$Leds$flash(uint8_t arg_0x11eb068){
#line 33
  LedsC$Leds$flash(arg_0x11eb068);
#line 33
}
#line 33
# 108 "msp430/InternalFlashC.nc"
static inline  error_t InternalFlashC$InternalFlash$read(void *addr, void *buf, uint16_t size)
#line 108
{

  addr += InternalFlashC$IFLASH_OFFSET;
  if (InternalFlashC$chooseSegment()) {
    addr += InternalFlashC$IFLASH_SIZE;
    }
  memcpy(buf, addr, size);

  return SUCCESS;
}

# 48 "/opt/tinyos-2.x/tos/interfaces/InternalFlash.nc"
inline static  error_t TOSBootM$IntFlash$read(void *arg_0x11e2928, void *arg_0x11e2ac8, uint16_t arg_0x11e2c58){
#line 48
  unsigned char result;
#line 48

#line 48
  result = InternalFlashC$InternalFlash$read(arg_0x11e2928, arg_0x11e2ac8, arg_0x11e2c58);
#line 48

#line 48
  return result;
#line 48
}
#line 48
# 43 "msp430/VoltageC.nc"
static inline  bool VoltageC$Voltage$okToProgram(void)
#line 43
{

  int i;


  ADC12CTL0 = (0x0010 | (2 << 8)) | 0x0020;

  ADC12CTL1 = 0x0200;

  ADC12MCTL0 = (0x80 | (1 << 4)) | 11;

  for (i = 0; i < 0x3600; i++) ;


  ADC12CTL0 |= 0x0002;

  ADC12CTL0 |= 0x0001;

  while ((ADC12IFG & 0x0001) == 0) ;


  ADC12CTL0 &= ~0x0002;
  ADC12CTL0 = 0;


  return ADC12MEM0 > VoltageC$VTHRESH;
}

# 32 "Voltage.nc"
inline static  bool TOSBootM$Voltage$okToProgram(void){
#line 32
  unsigned char result;
#line 32

#line 32
  result = VoltageC$Voltage$okToProgram();
#line 32

#line 32
  return result;
#line 32
}
#line 32
# 209 "TOSBootM.nc"
static inline void TOSBootM$startupSequence(void)
#line 209
{

  BootArgs args;




  if (!TOSBootM$Voltage$okToProgram()) {

      TOSBootM$Leds$flash(TOSBootM$LEDS_LOWBATT);
      TOSBootM$startupLeds();
      TOSBootM$runApp();
    }


  TOSBootM$IntFlash$read((uint8_t *)TOSBOOT_ARGS_ADDR, &args, sizeof args);


  if (++ args.gestureCount >= TOSBOOT_GESTURE_MAX_COUNT - 1) {

      TOSBootM$Leds$flash(TOSBootM$LEDS_GESTURE);





      if (TOSBootM$programImage(TOSBOOT_GOLDEN_IMG_ADDR) == TOSBootM$R_PROGRAMMING_ERROR) {
          TOSBootM$Hardware$reboot();
        }
    }
  else {

      TOSBootM$IntFlash$write((uint8_t *)TOSBOOT_ARGS_ADDR, &args, sizeof args);
      if (! args.noReprogram) {


          if (TOSBootM$programImage(args.imageAddr) == TOSBootM$R_PROGRAMMING_ERROR) {
              TOSBootM$Hardware$reboot();
            }
        }
    }


  TOSBootM$startupLeds();


  args.gestureCount = 0xff;
  args.noReprogram = TRUE;
  TOSBootM$IntFlash$write((uint8_t *)TOSBOOT_ARGS_ADDR, &args, sizeof args);

  TOSBootM$runApp();
}

# 47 "epic/hardware.h"
static inline  void TOSH_CLR_GREEN_LED_PIN(void)
#line 47
{
#line 47
   static volatile uint8_t r __asm ("0x001D");

#line 47
  r &= ~(1 << 3);
}

#line 47
static inline  void TOSH_SET_GREEN_LED_PIN(void)
#line 47
{
#line 47
   static volatile uint8_t r __asm ("0x001D");

#line 47
  r |= 1 << 3;
}

#line 48
static inline  void TOSH_CLR_YELLOW_LED_PIN(void)
#line 48
{
#line 48
   static volatile uint8_t r __asm ("0x001D");

#line 48
  r &= ~(1 << 7);
}

#line 48
static inline  void TOSH_SET_YELLOW_LED_PIN(void)
#line 48
{
#line 48
   static volatile uint8_t r __asm ("0x001D");

#line 48
  r |= 1 << 7;
}

#line 46
static inline  void TOSH_CLR_RED_LED_PIN(void)
#line 46
{
#line 46
   static volatile uint8_t r __asm ("0x001D");

#line 46
  r &= ~(1 << 0);
}

#line 46
static inline  void TOSH_SET_RED_LED_PIN(void)
#line 46
{
#line 46
   static volatile uint8_t r __asm ("0x001D");

#line 46
  r |= 1 << 0;
}

# 70 "lib/LedsC.nc"
static inline  void LedsC$Leds$glow(uint8_t a, uint8_t b)
#line 70
{
  int i;

#line 72
  for (i = 1536; i > 0; i -= 4) {
      LedsC$Leds$set(a);
      wait(i);
      LedsC$Leds$set(b);
      wait(1536 - i);
    }
}

# 34 "Leds.nc"
inline static  void TOSBootM$Leds$glow(uint8_t arg_0x11eb4e8, uint8_t arg_0x11eb670){
#line 34
  LedsC$Leds$glow(arg_0x11eb4e8, arg_0x11eb670);
#line 34
}
#line 34
# 36 "msp430/HPLUSART0M.nc"
static inline  void HPLUSART0M$HPLUSARTControl$disableSPI(void)
#line 36
{




  ME1 = 0;
  U0CTL = 1;
  U0TCTL = 1;
  U0RCTL = 0;
}

# 35 "msp430/HPLUSARTControl.nc"
inline static  void ExtFlashM$USARTControl$disableSPI(void){
#line 35
  HPLUSART0M$HPLUSARTControl$disableSPI();
#line 35
}
#line 35
# 96 "epic/ExtFlashM.nc"
static inline  error_t ExtFlashM$StdControl$stop(void)
#line 96
{
  ExtFlashM$USARTControl$disableSPI();
  return SUCCESS;
}

# 81 "msp430f1611/PowerOffM.nc"
static inline  error_t PowerOffM$StdControl$stop(void)
#line 81
{
  return SUCCESS;
}

# 135 "epic/ExtFlashM.nc"
static inline  void ExtFlashM$ExtFlash$stopRead(void)
#line 135
{
  TOSH_SET_FLASH_CS_PIN();
}

# 34 "ExtFlash.nc"
inline static  void TOSBootM$ExtFlash$stopRead(void){
#line 34
  ExtFlashM$ExtFlash$stopRead();
#line 34
}
#line 34
#line 33
inline static  uint8_t TOSBootM$ExtFlash$readByte(void){
#line 33
  unsigned char result;
#line 33

#line 33
  result = ExtFlashM$ExtFlash$readByte();
#line 33

#line 33
  return result;
#line 33
}
#line 33
#line 32
inline static  void TOSBootM$ExtFlash$startRead(uint32_t arg_0x11dd770){
#line 32
  ExtFlashM$ExtFlash$startRead(arg_0x11dd770);
#line 32
}
#line 32
# 98 "TOSBootM.nc"
static inline bool TOSBootM$verifyImage(ex_flash_addr_t startAddr)
#line 98
{
  uint32_t addr;
  uint8_t numPgs;
  uint8_t i;


  if (!TOSBootM$verifyBlock(startAddr + (size_t )& ((DelugeIdent *)0)->crc, 
  startAddr, (size_t )& ((DelugeIdent *)0)->crc)) {
    return FALSE;
    }

  TOSBootM$ExtFlash$startRead(startAddr + (size_t )& ((DelugeIdent *)0)->numPgs);
  numPgs = TOSBootM$ExtFlash$readByte();
  TOSBootM$ExtFlash$stopRead();

  if (numPgs == 0 || numPgs == 0xff) {
    return FALSE;
    }
  startAddr += DELUGE_IDENT_SIZE;
  addr = DELUGE_CRC_BLOCK_SIZE;

  for (i = 0; i < numPgs; i++) {
      if (!TOSBootM$verifyBlock(startAddr + i * sizeof(uint16_t ), 
      startAddr + addr, DELUGE_BYTES_PER_PAGE)) {
          return FALSE;
        }
      addr += DELUGE_BYTES_PER_PAGE;
    }

  return TRUE;
}

# 120 "msp430/HPLUSART0M.nc"
static inline  void HPLUSART0M$HPLUSARTControl$tx(uint8_t data)
#line 120
{
  U0TXBUF = data;
}

# 42 "msp430/HPLUSARTControl.nc"
inline static  void ExtFlashM$USARTControl$tx(uint8_t arg_0x123bce8){
#line 42
  HPLUSART0M$HPLUSARTControl$tx(arg_0x123bce8);
#line 42
}
#line 42
# 97 "msp430/HPLUSART0M.nc"
static inline  error_t HPLUSART0M$HPLUSARTControl$isTxEmpty(void)
#line 97
{
  if (U0TCTL & 0x01) {
      return SUCCESS;
    }
  return FAIL;
}

# 39 "msp430/HPLUSARTControl.nc"
inline static  error_t ExtFlashM$USARTControl$isTxEmpty(void){
#line 39
  unsigned char result;
#line 39

#line 39
  result = HPLUSART0M$HPLUSARTControl$isTxEmpty();
#line 39

#line 39
  return result;
#line 39
}
#line 39
# 124 "msp430/HPLUSART0M.nc"
static inline  uint8_t HPLUSART0M$HPLUSARTControl$rx(void)
#line 124
{
  return U0RXBUF;
}

# 43 "msp430/HPLUSARTControl.nc"
inline static  uint8_t ExtFlashM$USARTControl$rx(void){
#line 43
  unsigned char result;
#line 43

#line 43
  result = HPLUSART0M$HPLUSARTControl$rx();
#line 43

#line 43
  return result;
#line 43
}
#line 43
# 112 "msp430/HPLUSART0M.nc"
static inline  error_t HPLUSART0M$HPLUSARTControl$isRxIntrPending(void)
#line 112
{
  if (IFG1 & (1 << 6)) {
      IFG1 &= ~(1 << 6);
      return SUCCESS;
    }
  return FAIL;
}

# 41 "msp430/HPLUSARTControl.nc"
inline static  error_t ExtFlashM$USARTControl$isRxIntrPending(void){
#line 41
  unsigned char result;
#line 41

#line 41
  result = HPLUSART0M$HPLUSARTControl$isRxIntrPending();
#line 41

#line 41
  return result;
#line 41
}
#line 41
# 42 "crc.h"
static inline uint16_t crcByte(uint16_t crc, uint8_t b)
{
  uint8_t i;

  crc = crc ^ (b << 8);
  i = 8;
  do 
    if (crc & 0x8000) {
      crc = (crc << 1) ^ 0x1021;
      }
    else {
#line 52
      crc = crc << 1;
      }
  while (
#line 53
  --i);

  return crc;
}

# 32 "Leds.nc"
inline static  void TOSBootM$Leds$set(uint8_t arg_0x11ecba8){
#line 32
  LedsC$Leds$set(arg_0x11ecba8);
#line 32
}
#line 32
# 43 "msp430/ProgFlashM.nc"
static inline  error_t ProgFlashM$ProgFlash$write(in_flash_addr_t addr, uint8_t *buf, uint16_t len)
#line 43
{

  volatile uint16_t *flashAddr = (uint16_t *)(uint16_t )addr;
  uint16_t *wordBuf = (uint16_t *)buf;
  uint16_t i = 0;



  if (addr < 0xffff - (len >> 1)) {
      FCTL2 = 0xA500 + 0x0080 + 0x0004;
      FCTL3 = 0xA500;
      FCTL1 = 0xA500 + 0x0002;
      *flashAddr = 0;
      FCTL1 = 0xA500 + 0x0040;
      for (i = 0; i < len >> 1; i++, flashAddr++) {
          if ((uint16_t )flashAddr != ProgFlashM$RESET_ADDR) {
            *flashAddr = wordBuf[i];
            }
          else {
#line 61
            *flashAddr = 0x4000;
            }
        }
#line 63
      FCTL1 = 0xA500;
      FCTL3 = 0xA500 + 0x0010;
      return SUCCESS;
    }
  return FAIL;
}

# 32 "ProgFlash.nc"
inline static  error_t TOSBootM$ProgFlash$write(in_flash_addr_t arg_0x11f6a48, uint8_t *arg_0x11f6bf0, in_flash_addr_t arg_0x11f6d80){
#line 32
  unsigned char result;
#line 32

#line 32
  result = ProgFlashM$ProgFlash$write(arg_0x11f6a48, arg_0x11f6bf0, arg_0x11f6d80);
#line 32

#line 32
  return result;
#line 32
}
#line 32
# 212 "/opt/tinyos-2.x/tos/chips/msp430/msp430hardware.h"
static inline  void __nesc_enable_interrupt(void )
{
   __asm volatile ("eint");}

# 263 "TOSBootM.nc"
  int main(void)
#line 263
{

  __nesc_disable_interrupt();

  TOSH_SET_PIN_DIRECTIONS();
  TOSBootM$Hardware$init();

  TOSBootM$SubInit$init();
  TOSBootM$SubControl$start();

  TOSBootM$startupSequence();

  return 0;
}

# 48 "epic/ExtFlashM.nc"
static void ExtFlashM$TOSH_FLASH_AT45_DP_bit(bool set)
#line 48
{
  if (set) {
    TOSH_SET_SIMO0_PIN();
    }
  else {
#line 52
    TOSH_CLR_SIMO0_PIN();
    }
#line 53
  TOSH_SET_UCLK0_PIN();
  TOSH_CLR_UCLK0_PIN();
}

# 58 "lib/LedsC.nc"
static  void LedsC$Leds$flash(uint8_t a)
#line 58
{
  uint8_t i;
#line 59
  uint8_t j;

#line 60
  for (i = 3; i; i--) {
      LedsC$Leds$set(a);
      for (j = 4; j; j--) 
        wait(0xffff);
      LedsC$Leds$set(0);
      for (j = 4; j; j--) 
        wait(0xffff);
    }
}

#line 43
static  void LedsC$Leds$set(uint8_t ledsOn)
#line 43
{
  if (ledsOn & LedsC$GREEN_BIT) {
    TOSH_CLR_GREEN_LED_PIN();
    }
  else {
#line 47
    TOSH_SET_GREEN_LED_PIN();
    }
#line 48
  if (ledsOn & LedsC$YELLOW_BIT) {
    TOSH_CLR_YELLOW_LED_PIN();
    }
  else {
#line 51
    TOSH_SET_YELLOW_LED_PIN();
    }
#line 52
  if (ledsOn & LedsC$RED_BIT) {
    TOSH_CLR_RED_LED_PIN();
    }
  else {
#line 55
    TOSH_SET_RED_LED_PIN();
    }
}

# 61 "TOSBootM.nc"
static void TOSBootM$startupLeds(void)
#line 61
{

  uint8_t output = 0x7;
  uint8_t i;

  for (i = 3; i; i--, output >>= 1) 
    TOSBootM$Leds$glow(output, output >> 1);
}

# 84 "/opt/tinyos-2.x/tos/interfaces/StdControl.nc"
static  error_t TOSBootM$SubControl$stop(void){
#line 84
  unsigned char result;
#line 84

#line 84
  result = ExtFlashM$StdControl$stop();
#line 84
  result = ecombine(result, PowerOffM$StdControl$stop());
#line 84

#line 84
  return result;
#line 84
}
#line 84
# 56 "msp430/InternalFlashC.nc"
static uint8_t InternalFlashC$chooseSegment(void)
#line 56
{
  int8_t vnum0 = * (int8_t *)InternalFlashC$IFLASH_SEG0_VNUM_ADDR;
  int8_t vnum1 = * (int8_t *)InternalFlashC$IFLASH_SEG1_VNUM_ADDR;

#line 59
  if (vnum0 == InternalFlashC$IFLASH_INVALID_VNUM) {
    return 1;
    }
  else {
#line 61
    if (vnum1 == InternalFlashC$IFLASH_INVALID_VNUM) {
      return 0;
      }
    }
#line 63
  return (int8_t )(vnum0 - vnum1) < 0;
}

# 130 "TOSBootM.nc"
static error_t TOSBootM$programImage(ex_flash_addr_t startAddr)
#line 130
{
  uint8_t buf[TOSBOOT_INT_PAGE_SIZE];
  uint32_t pageAddr;
#line 132
  uint32_t newPageAddr;
  in_flash_addr_t intAddr;
  in_flash_addr_t secLength;
  ex_flash_addr_t curAddr;

  if (!TOSBootM$verifyImage(startAddr)) {
    return TOSBootM$R_INVALID_IMAGE_ERROR;
    }
  curAddr = startAddr + DELUGE_IDENT_SIZE + DELUGE_CRC_BLOCK_SIZE;

  TOSBootM$ExtFlash$startRead(curAddr);

  intAddr = TOSBootM$extFlashReadAddr();
  secLength = TOSBootM$extFlashReadAddr();
  curAddr = curAddr + 8;


  if (intAddr != 0x4a00) {







      TOSBootM$ExtFlash$stopRead();
      return TOSBootM$R_INVALID_IMAGE_ERROR;
    }

  TOSBootM$ExtFlash$stopRead();

  while (secLength) {

      pageAddr = newPageAddr = intAddr / TOSBOOT_INT_PAGE_SIZE;

      TOSBootM$ExtFlash$startRead(curAddr);

      do {


          if (secLength == 0xffffffff) {
              TOSBootM$ExtFlash$stopRead();
              return FAIL;
            }

          buf[(uint16_t )intAddr % TOSBOOT_INT_PAGE_SIZE] = TOSBootM$ExtFlash$readByte();
          intAddr++;
#line 178
          curAddr++;

          if (--secLength == 0) {
              intAddr = TOSBootM$extFlashReadAddr();
              secLength = TOSBootM$extFlashReadAddr();
              curAddr = curAddr + 8;
            }

          newPageAddr = intAddr / TOSBOOT_INT_PAGE_SIZE;
        }
      while (pageAddr == newPageAddr && secLength);
      TOSBootM$ExtFlash$stopRead();

      TOSBootM$Leds$set(pageAddr);



      if (
#line 194
      TOSBootM$ProgFlash$write(pageAddr * TOSBOOT_INT_PAGE_SIZE, buf, 
      TOSBOOT_INT_PAGE_SIZE) == FAIL) {
          return TOSBootM$R_PROGRAMMING_ERROR;
        }
    }

  return TOSBootM$R_SUCCESS;
}

#line 79
static bool TOSBootM$verifyBlock(ex_flash_addr_t crcAddr, ex_flash_addr_t startAddr, uint16_t len)
{
  uint16_t crcTarget;
#line 81
  uint16_t crcTmp;


  TOSBootM$ExtFlash$startRead(crcAddr);
  crcTarget = (uint16_t )(TOSBootM$ExtFlash$readByte() & 0xff) << 8;
  crcTarget |= (uint16_t )(TOSBootM$ExtFlash$readByte() & 0xff);
  TOSBootM$ExtFlash$stopRead();


  TOSBootM$ExtFlash$startRead(startAddr);
  for (crcTmp = 0; len; len--) 
    crcTmp = crcByte(crcTmp, TOSBootM$ExtFlash$readByte());
  TOSBootM$ExtFlash$stopRead();

  return crcTarget == crcTmp;
}

# 101 "epic/ExtFlashM.nc"
static  void ExtFlashM$ExtFlash$startRead(uint32_t newAddr)
#line 101
{

  uint8_t cmd[4];
  uint8_t i;
  uint32_t page = newAddr / 512;
  uint32_t offset = newAddr % 512;

  ExtFlashM$addr = newAddr;

  cmd[0] = 0x03;
  cmd[1] = page >> 6;
  cmd[2] = (page << 2) | (offset >> 8);
  cmd[3] = offset;

  TOSH_CLR_FLASH_CS_PIN();

  for (i = 0; i < sizeof cmd; i++) {
      ExtFlashM$USARTControl$tx(cmd[i]);
      while (ExtFlashM$USARTControl$isTxEmpty() != SUCCESS) ;
    }
}

static  uint8_t ExtFlashM$ExtFlash$readByte(void)
#line 123
{
  if (!(ExtFlashM$addr & 0x1ff)) {
      ExtFlashM$ExtFlash$stopRead();
      ExtFlashM$ExtFlash$startRead(ExtFlashM$addr);
    }
  ExtFlashM$addr++;
  ExtFlashM$USARTControl$rx();
  ExtFlashM$USARTControl$tx(0);
  while (ExtFlashM$USARTControl$isRxIntrPending() != SUCCESS) ;
  return ExtFlashM$USARTControl$rx();
}

# 71 "TOSBootM.nc"
static in_flash_addr_t TOSBootM$extFlashReadAddr(void)
#line 71
{
  in_flash_addr_t result = 0;
  int8_t i;

#line 74
  for (i = 3; i >= 0; i--) 
    result |= ((in_flash_addr_t )TOSBootM$ExtFlash$readByte() & 0xff) << i * 8;
  return result;
}

# 66 "msp430/InternalFlashC.nc"
static  error_t InternalFlashC$InternalFlash$write(void *addr, void *buf, uint16_t size)
#line 66
{

  volatile int8_t *newPtr;
  int8_t *oldPtr;
  int8_t *bufPtr = (int8_t *)buf;
  int8_t version;
  uint16_t i;

  addr += InternalFlashC$IFLASH_OFFSET;
  newPtr = oldPtr = (int8_t *)InternalFlashC$IFLASH_OFFSET;
  if (InternalFlashC$chooseSegment()) {
      oldPtr += InternalFlashC$IFLASH_SIZE;
    }
  else {
      addr += InternalFlashC$IFLASH_SIZE;
      newPtr += InternalFlashC$IFLASH_SIZE;
    }

  FCTL2 = 0xA500 + 0x0080 + 0x0004;
  FCTL3 = 0xA500;
  FCTL1 = 0xA500 + 0x0002;
  *newPtr = 0;
  FCTL1 = 0xA500 + 0x0040;

  for (i = 0; i < InternalFlashC$IFLASH_SIZE - 1; i++, newPtr++, oldPtr++) {
      if ((uint16_t )newPtr < (uint16_t )addr || (uint16_t )addr + size <= (uint16_t )newPtr) {
        *newPtr = *oldPtr;
        }
      else {
#line 94
        *newPtr = * bufPtr++;
        }
    }
#line 96
  version = *oldPtr + 1;
  if (version == InternalFlashC$IFLASH_INVALID_VNUM) {
    version++;
    }
#line 99
  *newPtr = version;

  FCTL1 = 0xA500;
  FCTL3 = 0xA500 + 0x0010;

  return SUCCESS;
}
