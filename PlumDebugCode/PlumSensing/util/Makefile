# -*- makefile -*-

all: UdpReport.py ClimateConstants.py ConfigCmd.py TimeCmd.py

clean:
	rm -f ClimateConstants.py UdpReport.py ConfigCmd.py TimeCmd.py

ClimateConstants.py: ../Climate.h
	ncg python -python-classname=ShellConstants ../Climate.h BSHELL_BLINK BSHELL_SENSE BSHELL_LERASE BSHELL_CONFIG BSHELL_TIME > ClimateConstants.py

UdpReport.py: ../UDPReport.h
	mig -DMIG -I$(TOSROOT)/tos/lib/net/blip -I$(TOSROOT)/support/sdk/c/blip/lib6lowpan -I$(TOSROOT)/support/sdk/c/blip/include -I$(TOSROOT)/tos/chips/cc2420 python -python-classname=UdpReport ../UDPReport.h udp_report -o UdpReport.py

ConfigCmd.py: ../Climate.h
        mig python -python-classname=ConfigCmd ../Climate.h config_cmd > ConfigCmd.py

TimeCmd.py: ../Climate.h
        mig python -python-classname=TimeCmd ../Climate.h time_cmd > TimeCmd.py
