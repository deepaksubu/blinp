package com.plum.tinyos.model;

import java.util.ArrayList;
import java.util.List;

public class CollectedDataStore {
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
		if (indexAddr >= 0) {
			cdataList.get(indexAddr).add(seqno, unixTime, blockID, message);
//			System.out.print("Added sample to data store (existing @ " + indexAddr + ") : addr = " + addr + ", seqno = " + seqno + "\n");
		}
		else {
			System.out.print("Could not add sample to data store (addr " + addr + " does not exist)\n");
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
