COMPONENT=PlumSensingC

# uncomment this for network programming support
# BOOTLOADER=tosboot

CFLAGS += -DCC2420_DEF_CHANNEL=26

# disables support for the logblock library and flash logging
# CFLAGS += -DDISABLE_FLASH

# printf debugs.  works only on telosb/tmote sky
# CFLAGS += -DPRINTFUART_ENABLED

PFLAGS += -Idrivers/pir
# PFLAGS += -I$(TOSROOT)/tos/lib/icount
PFLAGS += -Ilib/logblock
PFLAGS += -I$(TOSROOT)/tos/lib/printf
include $(MAKERULES)

