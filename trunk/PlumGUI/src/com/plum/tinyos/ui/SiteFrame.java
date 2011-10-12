package com.plum.tinyos.ui;

//SiteFrame.java
//A simple extension of the JInternalFrame class that contains a list
//object. Elements of the list represent HTML pages for a web site.
//
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

import com.plum.tinyos.model.FlashState;
import com.plum.tinyos.model.PlumConstants;
import com.plum.tinyos.model.PlumSensingApp;

public class SiteFrame extends JInternalFrame {

JList nameList;
SiteManager parent;
// Hardcode the pages of our "site" to keep things simple
String[] plums={};


public SiteFrame(String name, List<Integer> localAddressList, SiteManager sm) {
 super("Nodes Detected ", true, true, true);
 parent = sm;
 setBounds(0,0,PlumConstants.WINDOW_WIDTH-15,PlumConstants.SITE_FRAME_HEIGHT);
 //setBounds(0,0,250,parent.getHeight());
 this.maximizable=false;
 String[] reference=new String[localAddressList.size()];
 int counter=0;
 for (Integer i:localAddressList){
	 reference[counter]=Integer.toString(i);
	 counter++;
 }
 PlumSensingApp localPsa=parent.getPsa();
 FlashState localFs=localPsa.getFlashState();
 DefaultListModel localListModel=localFs.getListModel();
 nameList = new JList(localListModel);
// sm.getPsa().requestScan();
 //this.nameList.setModel(psa.getFlashState().getPlumListModel());

 nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 nameList.addListSelectionListener(new ListSelectionListener() {
   public void valueChanged(ListSelectionEvent lse) {
     // We know this is the list, so pop up the page.
     if (!lse.getValueIsAdjusting()) {
       parent.addPageFrame((String) nameList.getSelectedValue());
     }
   }
 });
 Container contentPane = getContentPane();
 contentPane.add(nameList, BorderLayout.CENTER);
}
}

