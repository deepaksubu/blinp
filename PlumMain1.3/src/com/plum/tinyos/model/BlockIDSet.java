package com.plum.tinyos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockIDSet {
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