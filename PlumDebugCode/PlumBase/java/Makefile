GEN=PlumSampleMsg.java PlumStatusMsg.java PlumCmdMsg.java

# SENSOR_DIR = universal_sensors
# SENSOR_DEPS = ../$(SENSOR_DIR)/SensorSample.h

all: PlumSensingApp.jar

PlumSensingApp.jar: PlumSensingApp.class
	jar cf $@ *.class

PlumSampleMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumSampleMsg java ../Plum.h nx_plum_sample -o $@ 

PlumStatusMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumStatusMsg java ../Plum.h nx_plum_status -o $@

PlumCmdMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumCmdMsg java ../Plum.h plum_cmd_msg -o $@

# Constants.java: ../LowPowerSensingConstants.h
#	ncg -I../$(SENSOR_DIR) -DLOWPOWERSENSING_JAVA_MAKEFILE -target=null \
-java-classname=Constants java ../LowPowerSensingConstants.h BASE_STATION_ADDR SAMPLING_INTERVAL \
MSG_QUEUE_SIZE LPL_INTERVAL  -o $@

PlumSensingApp.class: $(wildcard *.java) $(GEN)
	javac *.java

clean:
	rm -f *.class $(GEN)

veryclean: clean
	rm -f PlumSensingApp.jar
