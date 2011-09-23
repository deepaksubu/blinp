package com.plum.tinyos.model;

/**
 * @author Kevin Klues <klueska@cs.stanford.edu>
 * @date July 24, 2007
 */

import net.tinyos.message.*;
import net.tinyos.sf.SerialForwarder;
import net.tinyos.util.*;
import java.io.*;
/**
 */

enum PlumCommands {
	PLUM_SCAN (1),
		PLUM_CONFIG (2),
		PLUM_READ (3),
		PLUM_ERASE (4),
		PLUM_ERASE_CONFIG (5),
		PLUM_TIME (6);

    private final int value;
	public int size;
    PlumCommands(int value) {
        this.value = value;
		this.size = 6; // keep updated
    }
    public int value()   { return value; }
}



public class PlumSensingApp implements MessageListener
{	
	private static final String DEFAULT_PORT = "23";
	MoteIF mote;
	boolean m_scan = false, m_read = false;
	FlashState flashState = new FlashState();
	CollectedData collectedData;

	PlumCmdMsg requestMsg = new PlumCmdMsg();

	/* Main entry point */
	public void run() {
		mote = new MoteIF(PrintStreamMessenger.err);
		mote.registerListener(new PlumSampleMsg(), this);
		mote.registerListener(new PlumStatusMsg(), this);
		mote.registerListener(new PlumCmdMsg(), this);
	}
	
	synchronized public void messageReceived(int dest_addr, Message msg) {
		if (msg instanceof PlumSampleMsg) {
			if (m_read == true) {
				PlumSampleMsg receivedMsg = (PlumSampleMsg) msg;

//				if (generator.nextInt(50) > 0) {
					collectedData.add(receivedMsg.get_seqno(), receivedMsg.get_unixTime(), receivedMsg.get_blockID(), receivedMsg);
//				}
//				else {
//					System.out.print("Dropped sample packet randomly\n");
					// drop packet
//				}
				
              System.out.print("Deepak: Received from " + receivedMsg.get_sender() + " (seq no, unix time, block) : ( " + receivedMsg.get_seqno() + " , " + receivedMsg.get_unixTime() + " , " + receivedMsg.get_blockID() + " )\n");
		  
//				System.out.print("Sample : " + msg.toString());
			}
		}
		else if (msg instanceof PlumStatusMsg) {
			if (m_scan == true) {
				PlumStatusMsg receivedMsg = (PlumStatusMsg) msg;
				System.out.print("Received status: addr = " + receivedMsg.get_sender() + ", First,Last = " + receivedMsg.get_first_blockID() + "," + receivedMsg.get_last_blockID() + "\n");
//				System.out.print("Status : " + msg.toString());
//				System.out.print("First : " + receivedMsg.get_first_blockID() + "\n");
//				System.out.print("Last : " + receivedMsg.get_last_blockID() + "\n");
				flashState.add(receivedMsg.get_sender(), receivedMsg.get_first_blockID(), receivedMsg.get_last_blockID(), receivedMsg.get_sampleRate(), receivedMsg.get_statusRate());

				if (Math.abs(receivedMsg.get_last_unixTime() - (System.currentTimeMillis() / 1000L)) > receivedMsg.get_statusRate() * 3) {
					System.out.print("Sending node " + receivedMsg.get_sender() + " new time value: " + (System.currentTimeMillis() / 1000L) + ". Old time value: " + receivedMsg.get_last_unixTime() + ".\n");
					sendTime(receivedMsg.get_sender());			
				}					
			}
		}
		else if (msg instanceof PlumCmdMsg) {
			// check message
//			System.out.print("Pending message notification received.\n");
			@SuppressWarnings("unused")
			PlumCmdMsg receivedMsg = (PlumCmdMsg) msg;
//			System.out.print("Time = " + receivedMsg.get_unixTime() + "\n");
//			requestPending = false;
		}
	}

	synchronized public void requestScan() {
		try {
			PlumCmdMsg requestMsg = new PlumCmdMsg();

			requestMsg.set_addr(0);
			requestMsg.set_cmdID(PlumCommands.PLUM_SCAN.value());

			try {
				mote.send(MoteIF.TOS_BCAST_ADDR, requestMsg);
			}
			catch (IOException e) {
				System.err.println("Cannot send message to mote: " + e);
			}			
			
			if (m_scan == false) {
				System.out.print("Scanning...\n");		  
				m_scan = true;
			}
			else {
				System.out.print("Stopping scanning...\n");
				m_scan = false;
			}
		}
		catch (Exception e) {
			System.err.println("Cannot scan for nodes: " + e);
		}	
	}

	synchronized public void requestConfig(int addr, int sampleRate, int statusRate) {
		PlumCmdMsg requestMsg = new PlumCmdMsg();

		requestMsg.set_addr(addr);
		requestMsg.set_cmdID(PlumCommands.PLUM_CONFIG.value());
		try {
			if (sampleRate == 0) {
				requestMsg.set_sampleRate(flashState.getSampleRate(addr));
			}
			else {
				if (sampleRate > 0 && sampleRate < 30) {
					requestMsg.set_sampleRate(sampleRate);
				}
				else {
					System.err.println("Cannot set config because sample rate is out of range (0-30)\n");
					return;
				}
			}
						
			if (statusRate == 0) {
				requestMsg.set_statusRate(flashState.getStatusRate(addr));
			}
			else {
				if (statusRate > 0 && statusRate < 30) {
					requestMsg.set_statusRate(statusRate);
				}
				else {
					System.err.println("Cannot set config because status rate is out of range (0-300)\n");
					return;
				}
			}
		}
		catch (Exception e) {
			System.err.println("Cannot set config because node has not reported its status\n");
			return;
		}
	
		System.out.print("Setting configuration on node " + addr + " : sampleRate = " + requestMsg.get_sampleRate() + ", statusRate = " + requestMsg.get_statusRate() + "\n\n");

		try {
			mote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}
	
	synchronized public void requestSamples(int addr, int first, int last, boolean newThread) {
		PlumCmdMsg requestMsg = new PlumCmdMsg();

		requestMsg.set_addr(addr);
		requestMsg.set_cmdID(PlumCommands.PLUM_READ.value());
		if (first == 0 && last == 0)
			try {
				requestMsg.set_blockStart(flashState.getFirst(addr));
				requestMsg.set_blockEnd(flashState.getLast(addr));
			}
			catch (Exception e) {
				System.err.println("Cannot request samples because node has not reported its status\n");
				return;
			}
		else {
			requestMsg.set_blockStart(first);
			requestMsg.set_blockEnd(last);
		}
	
		System.out.print("Retrieving Data from " + addr + " : start = " + requestMsg.get_blockStart() + ", end = " + requestMsg.get_blockEnd() + "\n\n");
		m_read = true;

		if (newThread == true) {
			collectedData = new CollectedData(this, addr, requestMsg.get_blockStart(), requestMsg.get_blockEnd());
			DataCollector dc = new DataCollector(collectedData);
			Thread dc_t = new Thread(dc);
			dc_t.start();
		}
			
		try {
			mote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}

	synchronized public void requestErase(int addr) {
		PlumCmdMsg requestMsg = new PlumCmdMsg();
		m_read = true;
		requestMsg.set_addr(addr);
		requestMsg.set_cmdID(PlumCommands.PLUM_ERASE.value());
		requestMsg.set_blockStart(0);
		requestMsg.set_blockEnd(0);

		try {
			mote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}
	
	synchronized public void requestEraseConfig(int addr) {
		PlumCmdMsg requestMsg = new PlumCmdMsg();
		requestMsg.set_addr(addr);
		requestMsg.set_cmdID(PlumCommands.PLUM_ERASE_CONFIG.value());
		requestMsg.set_blockStart(0);
		requestMsg.set_blockEnd(0);

		try {
			mote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}

	synchronized public void sendTime(int addr) {
		PlumCmdMsg requestMsg = new PlumCmdMsg();
		requestMsg.set_addr(addr);
		requestMsg.set_cmdID(PlumCommands.PLUM_TIME.value());
		requestMsg.set_blockStart(5888);
		requestMsg.set_blockEnd(0);
		int unixTime = (int) (System.currentTimeMillis() / 1000L);
		requestMsg.set_unixTime(unixTime);
			
		System.out.print("Setting time on node " + addr + ".\n\n");
			
		try {
			mote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}
	
	synchronized public void requestAddrList() {
		flashState.printList();
	}

	public void listCommands() {
		System.out.print("Command IDs:\n");
			System.out.print("PLUM_SCAN (1)\n");
			System.out.print("PLUM_CONFIG (2)\n");
			System.out.print("PLUM_READ (3)\n");
			System.out.print("PLUM_ERASE (4)\n");
			System.out.print("PLUM_ERASE_CONFIG (5)\n");
			System.out.print("PLUM_TIME (6)\n\n");
	}
		
	public void usage() {
		System.out.print("Enter option:\n");
		System.out.print("1 : Scan for nodes");
		if (m_scan == true)
			System.out.print(" (Currently ON)\n");
		else
			System.out.print(" (Currently OFF)\n");
		System.out.print("2 : Configure PLUM node\n");
		System.out.print("3 : Read data from PLUM node\n");
		System.out.print("4 : Erase data from PLUM node\n");
		System.out.print("5 : Reset configuration from PLUM node\n");
		System.out.print("6 : Set time on PLUM node\n");				
		System.out.print("l : Print list of nodes heard\n");
		System.out.print("h : Print this help message\n");				
	}
    
		
	public static void main(String[] args) {
	String port=DEFAULT_PORT;
	if (args.length!=0){
		port=args[0];
	}
	else{
		port=DEFAULT_PORT;
	}
	//runSerialForwarder(port);
		
	
		PlumSensingApp me = new PlumSensingApp();
		me.run();

		InputStreamReader cin = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(cin);
		String input = "", sampleRate = "", statusRate = "";

		me.usage();
		System.out.print(">> ");
		for(;;) {
			try {
				input = in.readLine();
				if(input.equals("1")) {
					me.requestScan();			
				}
				else if(input.equals("2")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					System.out.print("Enter desired sample rate in seconds (0 to keep same): ");
					sampleRate = in.readLine();
					System.out.print("Enter desired status rate in seconds (0 to keep same): ");
					statusRate = in.readLine();
					me.requestConfig(Integer.parseInt(input), Integer.parseInt(sampleRate), Integer.parseInt(statusRate));
				}
				else if(input.equals("3")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.requestSamples(Integer.parseInt(input), 0, 0, true);
				}
				else if(input.equals("4")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.requestErase(Integer.parseInt(input));			
				}
				else if(input.equals("5")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.requestEraseConfig(Integer.parseInt(input));			
				}
				else if(input.equals("6")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.sendTime(Integer.parseInt(input));			
				}
				else if(input.equals("l")) {
					me.requestAddrList();
				}
				else if(input.equals("h")) {
					me.usage();
				}
				else {
					System.out.println("Invalid Input.\n");
					me.usage();
				}		
				System.out.print(">> ");
			}
			catch (IOException e) {
				System.out.print("Error On Input");
				me.usage();
			}
		}
	}

	public FlashState getFlashState() {
		return flashState;
	}

	public void setFlashState(FlashState flashState) {
		this.flashState = flashState;
	}
}
