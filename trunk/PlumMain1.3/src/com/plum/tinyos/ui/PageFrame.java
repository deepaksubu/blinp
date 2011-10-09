package com.plum.tinyos.ui;

//PageFrame.java
//A simple extension of the JInternalFrame class that contains a list
//object.  Elements of the list represent HTML pages for a web site.
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.plum.tinyos.model.PlumNode;
import com.plum.tinyos.ui.PlumNodeJPanel;

public class PageFrame extends JInternalFrame implements ActionListener {

SiteManager parent;
PlumNodeJPanel pnPanel;

public PageFrame(String plumId, SiteManager sm) {
 super("Node: " + plumId, true, true, true, true);
 parent = sm;
 setBounds(0,0,300,150);

 Container contentPane = getContentPane();

 // Create a text area to display the contents of our file in
 // and stick it in a scrollable pane so we can see everything
 //ta = new JTextArea();
 //JScrollPane jsp = new JScrollPane(ta);
 //contentPane.add(jsp, BorderLayout.CENTER); 
 
 //Create a custom JPanel for getting the data for each node
 //TODO: Change this with logic to show Critical Battery
 /*pnPanel=new PlumNodeJPanel();
 contentPane.add(pnPanel, BorderLayout.WEST);
 

 JMenuBar jmb = new JMenuBar();
 JMenu fileMenu = new JMenu("File");
 JMenuItem saveItem = new JMenuItem("Save");
 saveItem.addActionListener(this);
 fileMenu.add(saveItem);
 jmb.add(fileMenu);
 setJMenuBar(jmb);

 loadContent(Integer.parseInt(plumId));*/
}

public void actionPerformed(ActionEvent ae) {
 // Can only be the save menu
 //ssaveContent();
}

public void loadContent(int plumId) {
 try {
   //FileReader fr = new FileReader(filename);
   //ta.read(fr, null);
   //fr.close();
	PlumNode pn=parent.plumList.getPlums().get(plumId);
	pnPanel.setPlumNode(pn);
	
	
 }
 catch (Exception e) {
   System.err.println("Could not load page: " + pnPanel.toString());
 }
}

}



