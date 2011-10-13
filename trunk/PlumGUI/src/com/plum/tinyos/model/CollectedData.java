package com.plum.tinyos.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import net.tinyos.message.MoteIF;

import org.apache.commons.io.IOUtils;

import com.plum.tinyos.log.WindowHandler;
import com.plum.tinyos.ui.SiteManager;


public class CollectedData {
	private List<Integer> seqnoList;
	private List<Long> unixTimeList;
	private List<Integer> blockIDList;
	private List<PlumSampleMsg> messageList;
	private PlumSensingApp app;
	public int addr;
	public int blockRcvd;
	public Integer size;
	private File f;
	private SiteManager localSM;
	private WindowHandler h;
	
	public CollectedData(PlumSensingApp app, int addr) {
		this.seqnoList = new ArrayList<Integer>();
		this.unixTimeList = new ArrayList<Long>();
		this.blockIDList = new ArrayList<Integer>(); 
		this.messageList = new ArrayList<PlumSampleMsg>();
		this.app = app;
		this.addr = addr;
		this.blockRcvd = 0;
		this.size = 0;
		
		this.f=new File("PlumData.csv");
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
		//if (indexSeqno >= 0) {
//		if (indexSeqno >= 0 && indexUnixTime >= 0 && blockIDList.get(indexUnixTime) == blockID)
			// Duplicate!
		//	System.out.print("Received duplicate - dumping: seqno = " + seqno + " , unixTime = " + unixTime + " , blockID = " + blockID + "\n");
		//}
		//else {					
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
		//}
	}
	public boolean writeFile() throws FileNotFoundException {
		// TODO Auto-generated method stub
		// if (messageList.size()==0) return;
		long start = System.currentTimeMillis();
		long TIMEOUT_ONE = 300000;
		long end = start + TIMEOUT_ONE;


             
			

				System.out.print(f);
				//FileWriter fstream = null;
				try {
					FileWriter fstream = new FileWriter(f);
					BufferedWriter out = new BufferedWriter(fstream);
					//PrintWriter out = new PrintWriter(out1);
					try{
					out.write(new Date(System.currentTimeMillis()).toString());
					out.write("\n");
					out.write("Sender,SampleRate,UnixPlumTime,PlumTime,Voltage,Block Id,Sequence No");
					while (end > System.currentTimeMillis()) {
						// Thread.sleep(1000L);

						if (this.seqnoList.size() > 0) {
							break;
						}

					}
					localSM=app.getSiteManager();
					h = localSM.getWindowHandler();
				    LogRecord r = new LogRecord(Level.INFO,
				        "Start Writing...\n.");
				    h.publish(r);
					
					for (int seqno=0; seqno<this.messageList.size();seqno++) {
						PlumSampleMsg pSM = this.messageList.get(seqno);
						out.write("\n");
						out.write(pSM.get_sender() + ",");
						out.write(Integer.toString((pSM.get_sampleRate())));
						out.write(",");
						out.write(Long.toString(pSM.get_unixTime()));
						out.write(",");
						java.util.Date d = new java.util.Date(
								pSM.get_unixTime() * 1000L);
						// cal.s
						out.write(d.toString());
						out.write(",");
						out.write(Integer.toString((pSM.get_intvol())));
						out.write(",");
						out.write(Integer.toString((pSM.get_blockID())));
						out.write(",");
						out.write(Integer.toString((pSM.get_seqno())));
						out.write("");
						System.out.println("Writing to the files");
						r = new LogRecord(Level.INFO,
						        "Writing to the files...\n.");
						    h.publish(r);
							
						
					}
					} finally{
						out.close();
						LogRecord r1 = new LogRecord(Level.INFO,
						        "Writing finished...\n.");
						 h.publish(r1);
						System.out.println("Writing finished");
						
					}


					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception E){
					E.printStackTrace();
				}
				
				File srFile=f;
				File dtFile=new File("PlumData" +this.addr+".csv");
				
				

				  InputStream in = new FileInputStream(srFile);
				  OutputStream out = new FileOutputStream(dtFile); // appending output stream

				  try {
				     IOUtils.copy(in, out);
				     System.out.println("Copied the temp file to "+ dtFile.getName());
				  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  finally {
				      IOUtils.closeQuietly(in);
				      IOUtils.closeQuietly(out);
				  }
				return false;
				
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
/*
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CollectedData {
	private List<Integer> seqnoList;
	private List<Long> unixTimeList;
	private List<Integer> blockIDList;
	private List<PlumSampleMsg> messageList;
	private PlumSensingApp app;
	public int addr;
	public int blockRcvd;
	public Integer size;
	private File f;
	
	public CollectedData(PlumSensingApp app, int addr) {
		this.seqnoList = new ArrayList<Integer>();
		this.unixTimeList = new ArrayList<Long>();
		this.blockIDList = new ArrayList<Integer>(); 
		this.messageList = new ArrayList<PlumSampleMsg>();
		this.app = app;
		this.addr = addr;
		this.blockRcvd = 0;
		this.size = 0;
		this.f = new File("Plumdata.csv");
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

	public int findHoles() throws InterruptedException {
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
					firstBlock = blockIDList.get(seqnoList.indexOf(sortedSeqnoList.get(index)));
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
						app.requestSamples(addr, firstHole, lastHole, false);
						Thread.sleep(500L);
						firstHole = holeSetBlockID.get(i);
						lastHole = holeSetBlockID.get(i);
//						System.out.print("Starting new hole batch --> " + firstHole + " to " + lastHole + "\n");
					}
				}

				if (firstHole != -1) {
					// Request last group of samples
					app.requestSamples(addr, firstHole, lastHole, false);
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

	public boolean writeFile() throws FileNotFoundException {
		// TODO Auto-generated method stub
		// if (messageList.size()==0) return;
		long start = System.currentTimeMillis();
		long TIMEOUT_ONE = 300000;
		long end = start + TIMEOUT_ONE;


             
			

				System.out.print(f);
				//FileWriter fstream = null;
				try {
					FileWriter fstream = new FileWriter(f);
					BufferedWriter out = new BufferedWriter(fstream);
					//PrintWriter out = new PrintWriter(out1);
					try{
					out.write(new Date(System.currentTimeMillis()).toString());
					out.write("\n");
					out.write("Sender,SampleRate,UnixPlumTime,PlumTime,Voltage,Block Id,Sequence No");
					while (end > System.currentTimeMillis()) {
						// Thread.sleep(1000L);

						if (this.seqnoList.size() > 0) {
							break;
						}

					}
					for (int seqno : this.seqnoList) {
						PlumSampleMsg pSM = this.messageList.get(seqno);
						out.write("\n");
						out.write(pSM.get_sender() + ",");
						out.write(Integer.toString((pSM.get_sampleRate())));
						out.write(",");
						out.write(Long.toString(pSM.get_unixTime()));
						out.write(",");
						java.util.Date d = new java.util.Date(
								pSM.get_unixTime() * 1000L);
						// cal.s
						out.write(d.toString());
						out.write(",");
						out.write(Integer.toString((pSM.get_intvol())));
						out.write(",");
						out.write(Integer.toString((pSM.get_blockID())));
						out.write(",");
						out.write(Integer.toString((pSM.get_seqno())));
						out.write("");
						System.out.println("Writing to the files");
					}
					} finally{
						out.close();
						System.out.println("Writing finished");
						
					}


					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				File srFile=f;
				File dtFile=new File("PlumData" +this.addr+".csv");

				  InputStream in = new FileInputStream(srFile);
				  OutputStream out = new FileOutputStream(dtFile); // appending output stream

				  try {
				     IOUtils.copy(in, out);
				  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  finally {
				      IOUtils.closeQuietly(in);
				      IOUtils.closeQuietly(out);
				  }
				return false;
				
	}

}*/
