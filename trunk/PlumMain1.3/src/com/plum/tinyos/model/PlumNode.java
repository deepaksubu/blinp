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
	public PlumNode(int id, int batteryLeft, int spaceLeft){
		super();
		this.id=id;
		this.batteryLeft=batteryLeft;
		this.spaceLeft=spaceLeft;
	}
 
}
