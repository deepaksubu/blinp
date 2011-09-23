package com.plum.tinyos.model;

import java.util.ArrayList;
import java.util.List;

public class FlashState { 
	private List<Integer> addrList;
	public List<Integer> getAddrList() {
		return addrList;
	}

	public void setAddrList(List<Integer> addrList) {
		this.addrList = addrList;
	}

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

