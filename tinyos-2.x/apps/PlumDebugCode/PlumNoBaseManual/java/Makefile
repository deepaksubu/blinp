GEN=PlumSampleMsg.java PlumSendMsg.java PlumStatusMsg.java PlumCmdMsg.java

all: PlumSensingApp.jar

PlumSensingApp.jar: PlumSensingApp.class
	jar cf $@ *.class

PlumSampleMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumSampleMsg java ../Plum.h nx_plum_sample -o $@ 

PlumSendMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumSendMsg java ../Plum.h nx_plum_send_msg -o $@ 

PlumStatusMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumStatusMsg java ../Plum.h nx_plum_status -o $@

PlumCmdMsg.java: ../Plum.h
	mig -I.. -target=null \
-java-classname=PlumCmdMsg java ../Plum.h plum_cmd_msg -o $@

PlumSensingApp.class: $(wildcard *.java) $(GEN)
	javac *.java

clean:
	rm -f *.class $(GEN)

veryclean: clean
	rm -f PlumSensingApp.jar
