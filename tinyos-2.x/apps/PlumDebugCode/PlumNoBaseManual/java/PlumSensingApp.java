/**
 * @author Jay Taneja <taneja@cs.berkeley.edu>
 * @date September 6, 2011
 */

import net.tinyos.message.*;
import net.tinyos.util.*;
import java.io.*;
import java.util.*;
import net.tinyos.sf.SerialForwarder;
import net.tinyos.packet.*;
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

class CollectedDataStore {
	private List<Integer> addrList;
	private List<CollectedData> cdataList;
	private PlumSensingApp app;
	
	public CollectedDataStore(PlumSensingApp app) {
		this.addrList = new ArrayList<Integer>();
		this.cdataList = new ArrayList<CollectedData>();
		this.app = app;
	}

	public void addRead(int addr) {
		int indexAddr = addrList.indexOf(addr);
		if (indexAddr >= 0) {
		    cdataList.get(indexAddr).reset();
			
			System.out.print("Added message to data store (existing @ " + indexAddr + ") : addr = " + addr + "\n");
		}
		else {
			CollectedData cdata = new CollectedData(app, addr);
			addrList.add(addr);
			cdataList.add(cdata);
			System.out.print("Added address to data store (not existing @ " + Integer.toString(cdataList.size() - 1) + ") : addr = " + addr + "\n");
		}
	}
	
	public void addSample(int addr, int seqno, long unixTime, int blockID, PlumSampleMsg message) {
		int indexAddr = addrList.indexOf(addr);
		if (indexAddr >= 0 && message.get_sampleRate() < 60 && message.get_statusRate() < 1200) {
			cdataList.get(indexAddr).add(seqno, unixTime, blockID, message);
//			System.out.print("Added sample to data store (existing @ " + indexAddr + ") : addr = " + addr + ", seqno = " + seqno + "\n");
		}
		else {
			System.out.print("Could not add sample to data store (addr " + addr + " does not exist, sampleRate (" + message.get_statusRate() + ") is invalid, and/or sampleRate (" + message.get_sampleRate() + ") is invalid)\n");
		}
	}

	public CollectedData getCD(int addr) {
		int indexAddr = addrList.indexOf(addr);
		if (indexAddr >= 0) {
			return cdataList.get(indexAddr);
		}
		else {
			System.out.print("ERROR: Could not return CD from collected data store (addr = " + addr + ")\n");
			return null;
		}
	}
}

class CollectedData {
	private List<Integer> seqnoList;
	private List<Long> unixTimeList;
	private List<Integer> blockIDList;
	private List<PlumSampleMsg> messageList;
	private PlumSensingApp app;
	public int addr;
	public int blockRcvd;
	public Integer size;
	
	public CollectedData(PlumSensingApp app, int addr) {
		this.seqnoList = new ArrayList<Integer>();
		this.unixTimeList = new ArrayList<Long>();
		this.blockIDList = new ArrayList<Integer>(); 
		this.messageList = new ArrayList<PlumSampleMsg>();
		this.app = app;
		this.addr = addr;
		this.blockRcvd = 0;
		this.size = 0;
	}

	public void reset() {
		this.seqnoList = new ArrayList<Integer>();
		this.unixTimeList = new ArrayList<Long>();
		this.blockIDList = new ArrayList<Integer>(); 
		this.messageList = new ArrayList<PlumSampleMsg>();
		this.blockRcvd = 0;
		this.size = 0;
	}
	
	public void add(int seqno, long unixTime, int blockID, PlumSampleMsg message) {
		int indexSeqno = seqnoList.indexOf(seqno);
		int indexUnixTime = unixTimeList.indexOf(unixTime);
		int indexBlockID = blockIDList.indexOf(blockID);

//		if ((indexSeqno == indexUnixTime && indexUnixTime == indexBlockID) && (indexBlockID >= 0)) {
//		if (indexSeqno == indexUnixTime && indexUnixTime >= 0) {
		if (indexSeqno >= 0) {
//		if (indexSeqno >= 0 && indexUnixTime >= 0 && blockIDList.get(indexUnixTime) == blockID)
			// Duplicate!
//			System.out.print("Received duplicate - dumping: seqno = " + seqno + " , unixTime = " + unixTime + " , blockID = " + blockID + "\n");
		}
		else {					
			seqnoList.add(seqno);
			unixTimeList.add(unixTime);
			blockIDList.add(blockID);
			messageList.add(message);
			size = size + 1;

//			System.out.print("Received non-duplicate - adding: seqno = " + seqno + " , unixTime = " + unixTime + " , blockID = " + blockID + "indices = " + indexSeqno + " " + indexUnixTime + " " + indexBlockID + " , indices added = " + Integer.toString(seqnoList.size() - 1) + " " + Integer.toString(unixTimeList.size() - 1) + " " + Integer.toString(blockIDList.size() - 1) + "\n");
			
//			Collections.sort(seqnoList);

			if (blockID > this.blockRcvd || (blockID == 2 && this.blockRcvd > 4)) {
				this.blockRcvd = blockID;
			}
		}
	}

	public int findHoles(MoteIF mote) throws InterruptedException {
		int first = 0;
		int last = seqnoList.size();
		int index = 1;
		int holes = -1;
		List<Integer> holeListBlockID = new ArrayList<Integer>();
		int holeIndex;
		int firstBlock, lastBlock;
		List<Integer> sortedSeqnoList = seqnoList;
		Collections.sort(sortedSeqnoList);

		System.out.print("Finding holes in data from address " + addr + " : first = " + first + " (" + seqnoList.get(first) + ") , last = " + last + " (" + seqnoList.get(last-1) + ")\n");

		while (holes != 0) {
			holes = 0;
			while (index < last - first) {
				if (sortedSeqnoList.get(index) > sortedSeqnoList.get(index-1) + 1) {
					holeIndex = sortedSeqnoList.get(index-1) + 1;
					firstBlock = blockIDList.get(seqnoList.indexOf(sortedSeqnoList.get(index-1)));
					if (index + 1 < sortedSeqnoList.size()) {
						lastBlock = blockIDList.get(seqnoList.indexOf(sortedSeqnoList.get(index+1)));
					}
					else {
						lastBlock = firstBlock;
					}
					while (holeIndex < sortedSeqnoList.get(index)) {
//						holeIndex = sortedSeqnoList.get(index-1) + 1;
						// JKT: Workaround for large sequence number jumping bug.
						// Not sure of the cause of this bug - worthy of further investigation.
						if (holeIndex + 1000 < sortedSeqnoList.get(index)) {
							System.out.print("Large jump in sequence numbers (" + holeIndex + " - " + sortedSeqnoList.get(index) + "). Ignore.\n");
							holeIndex = sortedSeqnoList.get(index);
						}
						else {
							System.out.print("Hole at " + holeIndex + " : indices = " + (index-1) + " (" + sortedSeqnoList.get(index-1) + ") , " + index + " (" + sortedSeqnoList.get(index) + ")\n");
//						System.out.print("holeIndex = " + holeIndex + ", sortedSeqnoList(-1) = " + sortedSeqnoList.get(index-1) + ", sortedSeqnoList() = " + sortedSeqnoList.get(index) + ", " + seqnoList.indexOf(sortedSeqnoList.get(index)) + "\n");
							holeIndex = holeIndex + 1;
							holes = holes + 1;
						}
					}
					if (firstBlock < lastBlock) {
						while (firstBlock <= lastBlock) {						
							holeListBlockID.add(firstBlock);
							firstBlock = firstBlock + 1;
						}
					}
					else {
						holeListBlockID.add(firstBlock);
					}
				}
				index = index + 1;					
			}
			if (holes > 0) {
//				Set<Integer> holeSetBlockID = Collections.synchronizedSet(new HashSet<Integer>(holeListBlockID));				
//				Collections.sort(holeSetBlockID);

				BlockIDSet holeSetBlockID = new BlockIDSet(holeListBlockID);

				System.out.print("\nFound " + holes + " total hole(s) in " + holeSetBlockID.size() + " total block(s). Retrying...\n\n");
				
				int firstHole = -1, lastHole = -1;
				for (int i = 0 ; i < holeSetBlockID.size() ; i++) {
					if (firstHole == -1) {
						firstHole = holeSetBlockID.get(i);
						lastHole = holeSetBlockID.get(i);
//						System.out.print("Starting new hole batch at block " + firstHole + "\n");
					}
					else if (lastHole + 5 >= holeSetBlockID.get(i)) {
						lastHole = holeSetBlockID.get(i);
//						System.out.print("Adding to hole batch --> " + firstHole + " to " + lastHole + "\n");
					}
					else {
//						System.out.print("Hole batch complete --> " + firstHole + " to " + lastHole + "\n");
						app.requestSamples(addr, mote.getSource().getPacketSource().getName(), firstHole, lastHole, false);
						Thread.sleep(500L);
						firstHole = holeSetBlockID.get(i);
						lastHole = holeSetBlockID.get(i);
//						System.out.print("Starting new hole batch --> " + firstHole + " to " + lastHole + "\n");
					}
				}

				if (firstHole != -1) {
					// Request last group of samples
					app.requestSamples(addr, mote.getSource().getPacketSource().getName(), firstHole, lastHole, false);
				}
				
				Thread.sleep(32000L);
				
				break;
//				holes = -1;
			}
			else {
				System.out.print("\nFound " + holes + " total hole(s). Finished!\n\n");
			}
		}

		return holes;
	}
}

class BlockIDSet {
	private List<Integer> blockIDList;

	public BlockIDSet(List<Integer> bIDList) {
		this.blockIDList = new ArrayList<Integer>();

		int index = 0;
		for (index = 0 ; index < bIDList.size() ; index++) {
			int indexBlockID = blockIDList.indexOf(bIDList.get(index));

			if (indexBlockID < 0) {
				blockIDList.add(bIDList.get(index));
				Collections.sort(blockIDList);
			}
		}
	}

	public int get(int index) {
		return blockIDList.get(index);
	}

	public int size() {
		return blockIDList.size();
	}
}
	
class FlashState { 
	private List<Integer> addrList;
	private List<Integer> firstList;
	private List<Integer> lastList;
	private List<Integer> sampleRateList;
	private List<Integer> statusRateList;	

	public FlashState() { 
		this.addrList = new ArrayList<Integer>();
		this.firstList = new ArrayList<Integer>(); 
		this.lastList = new ArrayList<Integer>(); 
		this.sampleRateList = new ArrayList<Integer>(); 
		this.statusRateList = new ArrayList<Integer>(); 
	}

	public void add(int addr, int first, int last, int sampleRate, int statusRate) {
		int index = addrList.indexOf(addr);
		
//		System.out.print("Adding address " + addr + " with locations : " + first + " , " + last + ", index = " + index + "\n");
	
		if (index >= 0) {
			firstList.set(index, first);
			// Plus one to make sure we get flushed written block, which is incomplete
			lastList.set(index, last+1);
			sampleRateList.set(index, sampleRate);
			statusRateList.set(index, statusRate);
		}
		else {
			addrList.add(addr);
			firstList.add(first);
			// Plus one to make sure we get flushed written block, which is incomplete
			lastList.add(last+1);
			sampleRateList.add(sampleRate);
			statusRateList.add(statusRate);
		}
	}

	public int getFirst(int addr) throws IllegalAccessException {
		int index = addrList.indexOf(addr);
		if (index >= 0) {
			return firstList.get(index);
		}
		else {
			throw new IllegalAccessException();
		}
	}

	public int getLast(int addr) throws IllegalAccessException {
		int index = addrList.indexOf(addr);
		if (index >= 0) {
			return lastList.get(index);
		}
		else {
			throw new IllegalAccessException();
		}
	}

	public int getSampleRate(int addr) throws IllegalAccessException {
		int index = addrList.indexOf(addr);
		if (index >= 0) {
			return sampleRateList.get(index);
		}
		else {
			throw new IllegalAccessException();
		}
	}

	public int getStatusRate(int addr) throws IllegalAccessException {
		int index = addrList.indexOf(addr);
		if (index >= 0) {
			return statusRateList.get(index);
		}
		else {
			throw new IllegalAccessException();
		}
	}
	
	public void printList() {
		System.out.print("Node ID\t\tFirst Block\tLast Block\tSample Rate\tStatus Rate\n");
		System.out.print("-------------------------------------------------------------------\n");
		for (int i = 0 ; i < addrList.size() ; i++) {
			System.out.print(addrList.get(i) + "\t\t" + firstList.get(i) + "\t\t" + lastList.get(i) + "\t\t" + sampleRateList.get(i) + "\t\t" + statusRateList.get(i) + "\n");
		}
	}
}

class DataCollector implements Runnable {
	private PlumSensingApp app;
	private CollectedData cdata;
	private int addr;
	private MoteIF mote;
	private int blockStart;
	private int blockEnd;
	private int retries = 0;
	private int readRetries = 0;
	int READ_RETRIES = 3;
	int MAX_RETRIES = 5;
	int SHORT_TIMEOUT_COUNT = 5;
	int LONG_TIMEOUT_COUNT = 32;

	public DataCollector(PlumSensingApp app, CollectedData collectedData, int addr, MoteIF mote, int blockStart, int blockEnd) {
		this.app = app;
		this.cdata = collectedData;
		this.addr = addr;
		this.mote = mote;
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
	}
	
	public void run() {
		try {
			int size = cdata.size;
			int shortTimeoutCount = 0;
			int longTimeoutCount = 0;
			double expectedSize = (blockEnd - blockStart - 1) * (Math.floor(522.0 / PlumSampleMsg.DEFAULT_MESSAGE_SIZE) - 0);
			int numHoles = 0;
		  
			if (expectedSize < 0) {				
				expectedSize = 0;
			}
			
			System.out.print("Expected number of samples : " + expectedSize + "\n\n");
			System.out.print("Received samples : ");
// 			while (cdata.size < expectedSize) {
// 				if (size != cdata.size) {
// 					System.out.print(cdata.size + " / " + expectedSize + " ");
// 					size = cdata.size;
// 				}
// 				else {
// 					timeoutCount = timeoutCount + 1;
// 					if (timeoutCount == TIMEOUT_COUNT*10) {
// 						break;
// 					}
// 				}
// 				Thread.sleep(100L);				
// 			}

			while (true) {
				if (cdata.size == 0) {
					longTimeoutCount = longTimeoutCount + 1;
					if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
						break;
					}
				}
				else {
					if (cdata.size > size) {
						System.out.print(cdata.size + " / " + expectedSize + " ");
						size = cdata.size;
						shortTimeoutCount = 0;
						longTimeoutCount = 0;
					}
					else {
						shortTimeoutCount = shortTimeoutCount + 1;
						longTimeoutCount = longTimeoutCount + 1;
						if (shortTimeoutCount >= SHORT_TIMEOUT_COUNT*10 && cdata.size >= expectedSize) {
							break;
						}
						else if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
							break;
						}							
					}					
				}
				Thread.sleep(100L);								
			}

			// in case of any straggler messages
			Thread.sleep(1000L);

			if (cdata.size > 0 && expectedSize > 0) {
				System.out.print("\nCompleted message - total received: " + cdata.size + " , total expected: at least " + expectedSize + "\n");
				System.out.print("Checking for holes in the data...\n");
			}

			longTimeoutCount = 0;
			while (true) {
				System.out.print("\nCurrent Status: " + cdata.size + " / " + expectedSize + " \n");
				size = cdata.size;
				if (retries >= MAX_RETRIES) {
					// need to write file here
					System.out.print("\nReached maximum number of retries. Ending hole finding with " + numHoles + " holes left...\n");

					if (!mote.getSource().getPacketSource().getName().contains("sf@localhost:9002")) {
						System.out.println("Closing source : " + mote.getSource().getPacketSource().getName());
						mote.getSource().shutdown();
					}
					
					break;
				}
				
				if (cdata.size > 0 && expectedSize > 0) {
					numHoles = cdata.findHoles(mote);
					retries = retries + 1;
					if (numHoles == 0) {

						if (!mote.getSource().getPacketSource().getName().contains("sf@localhost:9002")) {
							System.out.println("Closing source : " + mote.getSource().getPacketSource().getName());
							mote.getSource().shutdown();
						}
						
						break;
					}
				}
				else if (expectedSize == 0) {
					break;
				}
				else {
					if (readRetries >= READ_RETRIES) {
						System.out.print("\nError - incomplete message - maximum number of retries reached. Total received: " + cdata.size + " , total expected: at least " + expectedSize + "\n");

						if (!mote.getSource().getPacketSource().getName().contains("sf@localhost:9002")) {
							System.out.println("Closing source : " + mote.getSource().getPacketSource().getName());
							mote.getSource().shutdown();
						}
						
						break;
					}
					else {
						app.requestSamples(addr, mote.getSource().getPacketSource().getName(), blockStart, blockEnd, false);
						Thread.sleep(32000L);
					}
					readRetries = readRetries + 1;
				}

				longTimeoutCount = longTimeoutCount + 1;
				if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
					break;
				}

				Thread.sleep(100L);
			}
			return;

		} catch (InterruptedException iex) {}
	}		
}


public class PlumSensingApp implements MessageListener
{	
	MoteIF baseMote, localMote;
	boolean m_scan = false, m_read = false;
	FlashState flashState = new FlashState();
	CollectedDataStore collectedDataStore = new CollectedDataStore(this);

	boolean drop_state = false;

	PlumCmdMsg requestMsg = new PlumCmdMsg();

	/* Main entry point */
	void run() {
//		baseMote = new MoteIF(PrintStreamMessenger.err);
//		baseMote.registerListener(new PlumSampleMsg(), this);
//		baseMote.registerListener(new PlumStatusMsg(), this);
//		baseMote.registerListener(new PlumCmdMsg(), this);
	}
	
	synchronized public void messageReceived(int dest_addr, Message msg) {
//		Random generator = new Random();
		
		if (msg instanceof PlumSampleMsg) {
			if (m_read == true) {
				PlumSampleMsg receivedMsg = (PlumSampleMsg) msg;

//				if (drop_state == false) {
//					if (generator.nextInt(50) < 2)
//					if (generator.nextInt(50) < 0)
//						drop_state = true;
//				}
//				else {
//					if (generator.nextInt(50) > 20)
//						drop_state = false;
//				}

//				if (drop_state == true && generator.nextInt(50) > 20) {
//						System.out.print("Dropped sample packet randomly from node " + receivedMsg.get_sender() + " with sequence number " + receivedMsg.get_seqno() + "\n");
					// drop packet
//				}
//				else {
//					System.out.print("Received sample from node " + receivedMsg.get_sender() + " with sequence number " + receivedMsg.get_seqno() + "\n");
				collectedDataStore.addSample(receivedMsg.get_sender(), receivedMsg.get_seqno(), receivedMsg.get_unixTime(), receivedMsg.get_blockID(), receivedMsg);
//				}
				
//		System.out.print("Received from " + receivedMsg.get_sender() + " (seq no, unix time, block) : ( " + receivedMsg.get_seqno() + " , " + receivedMsg.get_unixTime() + " , " + receivedMsg.get_blockID() + " )\n");
		  
//				System.out.print("Sample : " + msg.toString());
			}
		}
		else if (msg instanceof PlumStatusMsg) {
			if (m_scan == true) {
				PlumStatusMsg receivedMsg = (PlumStatusMsg) msg;
//				System.out.print("Status : " + msg.toString());
//				System.out.print("First : " + receivedMsg.get_first_blockID() + "\n");
//				System.out.print("Last : " + receivedMsg.get_last_blockID() + "\n");
				if (receivedMsg.get_sampleRate() < 60 && receivedMsg.get_statusRate() < 1200) {
					System.out.print("Received status: addr = " + receivedMsg.get_sender() + ", First,Last = " + receivedMsg.get_first_blockID() + "," + receivedMsg.get_last_blockID() + "\n");
					flashState.add(receivedMsg.get_sender(), receivedMsg.get_first_blockID(), receivedMsg.get_last_blockID(), receivedMsg.get_sampleRate(), receivedMsg.get_statusRate());
                    //If the time difference is more than six months (15552000) then reset the time on the plum device
					if (Math.abs(receivedMsg.get_last_unixTime() - (System.currentTimeMillis() / 1000L)) > 15552000) {
						System.out.print("Sending node " + receivedMsg.get_sender() + " new time value: " + (System.currentTimeMillis() / 1000L) + ". Old time value: " + receivedMsg.get_last_unixTime() + ".\n");
						sendTime(receivedMsg.get_sender());			
					}
				}
				else {
					System.out.print("Dumped out-of-range status message.\n");
				}
			}
		}
		else if (msg instanceof PlumCmdMsg) {
			// check message
//			System.out.print("Pending message notification received.\n");
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
				baseMote.send(MoteIF.TOS_BCAST_ADDR, requestMsg);
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
			baseMote.send(addr, requestMsg);
		}
		catch (IOException e) {
			System.err.println("Cannot send message to mote: " + e);
		}
	}
	
	synchronized public void requestSamples(int addr, String source, int first, int last, boolean newThread) {
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

		if (source.contains("@") == false) {
			source = baseMote.getSource().getPacketSource().getName();
		}
	
		System.out.print("Retrieving Data from " + addr + " : start = " + requestMsg.get_blockStart() + ", end = " + requestMsg.get_blockEnd() + "\n\n");
		m_read = true;

		if (source.contains("sf@localhost:9002")) {
			try {
				baseMote.send(addr, requestMsg);
			}
			catch (IOException e) {
				System.err.println("Cannot send message to mote: " + e);
			}
		}
		else {
			try {
				PhoenixSource pSource = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
				localMote = new MoteIF(pSource);
				localMote.registerListener(new PlumSampleMsg(), this);
			}
			catch (Exception e) {
				System.err.println("Cannot listen to local mote: " + e);
			}

			try {
				localMote.send(addr, requestMsg);
			}
			catch (IOException e) {
				System.err.println("Cannot send message to mote: " + e);
			}
		}

		if (newThread == true) {
			collectedDataStore.addRead(addr);
			DataCollector dc;
			if (source.contains("sf@localhost:9002")) {
				dc = new DataCollector(this, collectedDataStore.getCD(addr), addr, baseMote, requestMsg.get_blockStart(), requestMsg.get_blockEnd());
			}
			else {
				dc = new DataCollector(this, collectedDataStore.getCD(addr), addr, localMote, requestMsg.get_blockStart(), requestMsg.get_blockEnd());
			}
			Thread dc_t = new Thread(dc);
			dc_t.start();
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
			baseMote.send(addr, requestMsg);
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
			baseMote.send(addr, requestMsg);
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
			
//		System.out.print("Setting time on node " + addr + ".\n\n");
			
		try {
			baseMote.send(addr, requestMsg);
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
		System.out.print("3 : Read data from PLUM node over radio connection\n");
		System.out.print("4 : Read data from PLUM node over serial connection\n");
		System.out.print("5 : Erase data from PLUM node\n");
		System.out.print("6 : Reset configuration from PLUM node\n");
		System.out.print("7 : Set time on PLUM node\n");				
		System.out.print("l : Print list of nodes heard\n");
		System.out.print("h : Print this help message\n");				
	}

	public static void main(String[] args) {
	/*
	try {
			SerialForwarder sfr=new SerialForwarder(new String[]{"-comm","serial@COM23:telos","-no-gui"});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sfr.startListenServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		PlumSensingApp me = new PlumSensingApp();
		me.run();

		InputStreamReader cin = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(cin);
		String input = "", sampleRate = "", statusRate = "", firstBlock = "", lastBlock = "";

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
					me.requestSamples(Integer.parseInt(input), "", 0, 0, true);
				}
				else if(input.equals("4")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					System.out.print("Enter Last Block [default: last heard]: ");
					lastBlock = in.readLine();

					if (("").equals(lastBlock)) {
						firstBlock = "0";
						lastBlock = "0";
					}
					else {
						firstBlock = "2";
					}
					
					String defaultSource = new String();

					try {
						// Execute command
						String command = "bash -c motelist";
						Process child = Runtime.getRuntime().exec(command);
						
						// Get the input stream and read from it
						InputStream inStream = child.getInputStream();
						int c;
						StringBuffer output = new StringBuffer();
						while ((c = inStream.read()) != -1) {
							output.append(((char)c));
						}
						inStream.close();
						String[] lines = output.toString().split("\n");

						if (lines.length < 3) {
							throw new IOException();
						}
						else {
							int lineIndex;
							for (lineIndex = 2 ; lineIndex < lines.length ; lineIndex++) {
								String[] line = lines[lineIndex].split("\\s+");
								if (line[0].charAt(0) == 'F') {
									defaultSource = "serial@" + line[1] + ":telos";
									break;
								}
							}
						}
					} catch (IOException e) {
						System.out.print("Error (mote not connected?): " + e);
					}

					System.out.println("Retrieving data from : " + defaultSource);
					me.requestSamples(Integer.parseInt(input), defaultSource, Integer.parseInt(firstBlock), Integer.parseInt(lastBlock), true);
//					System.out.print("Enter Data Source (ex. serial@/dev/ttyUSB0:telos) : ");
//					input2 = in.readLine();
//					me.requestSamples(Integer.parseInt(input), input2, 0, 0, true);
				}
				else if(input.equals("5")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.requestErase(Integer.parseInt(input));			
				}
				else if(input.equals("6")) {
					System.out.print("Enter Address: ");
					input = in.readLine();
					me.requestEraseConfig(Integer.parseInt(input));			
				}
				else if(input.equals("7")) {
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
}
