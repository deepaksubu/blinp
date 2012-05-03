package com.plum.tinyos.model;

/**
 *  @author Deepak
 *  This class describes a single plum node
 * I am thinking of identification, batteryLeft, spaceLeft
 */
public class PlumNode{
	
	public PlumNode(){
		super();
		
	}
	public String unixTime;
	public String getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(String unixTime) {
		this.unixTime = unixTime;
	}
	public int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBatteryLeft() {
		return batteryLeft;
	}

	public void setBatteryLeft(int batteryLeft) {
		this.batteryLeft = batteryLeft;
	}

	public int getSpaceLeft() {
		return spaceLeft;
	}

	public void setSpaceLeft(int spaceLeft) {
		this.spaceLeft = spaceLeft;
	}

	public int batteryLeft;
	public int spaceLeft;
	
	//This below constructor is just a hardcoded placeholder. Replace it by actual logic
	public PlumNode(int id, int batteryLeft){
		super();
		this.id=id;
		float fbatteryLeft=((float)batteryLeft/4095)*100;
		this.batteryLeft=50;//Math.round(fbatteryLeft);
		this.spaceLeft=100;
	}
	
	public PlumNode(int id, int batteryLeft, int lastBlock,long unixTime){
		super();
		this.id=id;
		float fbatteryLeft=((float)batteryLeft/4095)*100;
		this.batteryLeft=Math.round(fbatteryLeft);
		float fspaceLeft=((float)(3475-lastBlock)/3475)*100;
		this.spaceLeft=Math.round(fspaceLeft);
		this.unixTime=Long.toString(unixTime);
	}
 
}
