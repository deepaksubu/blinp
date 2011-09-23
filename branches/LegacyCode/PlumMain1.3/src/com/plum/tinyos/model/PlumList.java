package com.plum.tinyos.model;

import java.util.ArrayList;


public class PlumList {
	public ArrayList<PlumNode> plums;
	private String[] plumIds;

	//TODO: This constructor should be modified so that it gets the list of the actual nodes from the mote
	public PlumList() {
		ArrayList<PlumNode> plums=new ArrayList<PlumNode>();
		for (int i = 0; i < 10; i++) {
			plums.add(new PlumNode(i, (int) (Math.random() * 100), (int) (Math
					.random() * 100)));
		}
		this.plums=plums;
		
	}

	public ArrayList<PlumNode> getPlums() {
		return plums;
	}

	public void setPlums(ArrayList<PlumNode> plums) {
		this.plums = plums;
	}

	public String[] getPlumIds() {
		ArrayList<String> temp=new ArrayList<String>();
		String[] a = {};
		int i=0;
 		for (PlumNode p:plums){
 			temp.add(Integer.toString(p.getId()));
		}
		return temp.toArray(a);
	}


}
