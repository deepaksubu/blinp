package com.plum.tinyos.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

public class CollectedData {
	private List<Integer> seqnoList;
	private List<Long> unixTimeList;
	private List<Integer> blockIDList;
	private List<PlumSampleMsg> messageList;
	private PlumSensingApp app;
	public int addr;
	public int blockStart;
	public int blockEnd;
	public int blockRcvd;
	public Integer size;
	private Random generator;
	File f;

	public CollectedData(PlumSensingApp app, int addr, int blockStart,
			int blockEnd) {
		this.seqnoList = new ArrayList<Integer>();
		this.unixTimeList = new ArrayList<Long>();
		this.blockIDList = new ArrayList<Integer>();
		this.messageList = new ArrayList<PlumSampleMsg>();
		this.app = app;
		this.addr = addr;
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.blockRcvd = 0;
		this.size = 0;
		this.generator = new Random();
		this.f = new File(System.getProperty("user.home") + "\\Plumdata" + ".csv");
		System.out.println("Current User Directory"+this.getCurrentUserDirectory());
		System.out.println(System.getProperty("user.home")); 
	}

	public void add(int seqno, long unixTime, int blockID, PlumSampleMsg message) {
		int indexSeqno = seqnoList.indexOf(seqno);
		int indexUnixTime = unixTimeList.indexOf(unixTime);
		int indexBlockID = blockIDList.indexOf(blockID);

		if ((indexSeqno == indexUnixTime && indexUnixTime == indexBlockID)
				&& (indexBlockID >= 0)) {
			// Duplicate!
			System.out.print("Received duplicate - dumping: seqno = " + seqno
					+ " , unixTime = " + unixTime + " , blockID = " + blockID
					+ "\n");
		} else {
			seqnoList.add(seqno);
			unixTimeList.add(unixTime);
			blockIDList.add(blockID);
			messageList.add(message);
			size = size + 1;

			Collections.sort(seqnoList);

			if (blockID > this.blockRcvd
					|| (blockID == 2 && this.blockRcvd > 4)) {
				this.blockRcvd = blockID;
			}
		}
	}

	public String getCurrentUserDirectory() {

		try {
			Process p = Runtime.getRuntime().exec("whoami");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			String completedStream = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				completedStream = completedStream + line;

			}
			String name = completedStream.substring(completedStream
					.indexOf('\\') + 1);
			String directory = "C:/Users/LSHTM/";
			return directory;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "LSHTM";
		}

	}

	@SuppressWarnings("finally")
	public boolean writeFile() {
		// TODO Auto-generated method stub
		// if (messageList.size()==0) return;
		long start = System.currentTimeMillis();
		long TIMEOUT_ONE = 300000;
		long end = start + TIMEOUT_ONE;

		try {
             
			if (f.exists() && f.canWrite()) {

				System.out.print(f);
				FileWriter fstream;
				fstream = new FileWriter(f);

				BufferedWriter out1 = new BufferedWriter(fstream);
				PrintWriter out = new PrintWriter(out1);
				out.write(new Date(System.currentTimeMillis()).toString());
				out.write("Sender,SampleRate,UnixPlumTime,PlumTime");
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
				}
				out.close();
			} else if (!f.canWrite()) {
				System.out.println("Not write");
			}
			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block

			System.out.println("Not good to be here: File write");
			//File f1=new File(this.getCurrentUserDirectory() + "Plumbackup" + ".csv");;
			//BufferedWriter out1 = new BufferedWriter(fstream);
			//PrintWriter out = new PrintWriter(out1);
			//out.flush(this);
			
			
			e.printStackTrace();
			return false;
		} finally {
			System.out.println("So much for that:File Write");
			return false;
		}
	}

	public int findHoles() throws InterruptedException {
		int first = 0;
		int last = seqnoList.size();
		int index = 1;
		int holes = -1;
		List<Integer> holeListBlockID = new ArrayList<Integer>();
		int holeIndex;

		System.out.print("Finding holes : first = " + first + " ("
				+ seqnoList.get(first) + ") , last = " + last + " ("
				+ seqnoList.get(last - 1) + ")\n");

		while (holes != 0) {
			holes = 0;
			while (index < last - first) {
				if (seqnoList.get(index) > seqnoList.get(index - 1) + 1) {
					holeIndex = seqnoList.get(index - 1) + 1;
					while (holeIndex < seqnoList.get(index)) {
						System.out.print("Hole at " + holeIndex
								+ " : indices = " + (index - 1) + " ("
								+ seqnoList.get(index - 1) + ") , " + index
								+ " (" + seqnoList.get(index) + ")\n");
						holeListBlockID.add(blockIDList.get(index));
						holeIndex = holeIndex + 1;
					}
					holes = holes + 1;
				}
				index = index + 1;
			}
			if (holes > 0) {
				System.out.print("\nFound " + holes
						+ " total hole(s). Retrying...\n\n");

				Set<Integer> holeSetBlockID = Collections
						.synchronizedSet(new HashSet<Integer>(holeListBlockID));

				for (int i : holeSetBlockID) {
					app.requestSamples(addr, i, i, false);
					Thread.sleep(500L);
				}

				Thread.sleep(32000L);

				break;
				// holes = -1;
			} else {
				System.out.print("\nFound " + holes
						+ " total hole(s). Finished!\n\n");
			}
		}

		return holes;
	}
}
