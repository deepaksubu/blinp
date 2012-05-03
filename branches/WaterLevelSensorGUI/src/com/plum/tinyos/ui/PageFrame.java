package com.plum.tinyos.ui;

//PageFrame.java
//A simple extension of the JInternalFrame class that contains a list
//object.  Elements of the list represent HTML pages for a web site.
//

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.*;

import com.plum.tinyos.log.WindowHandler;
import com.plum.tinyos.model.FlashState;
import com.plum.tinyos.model.PlumNode;
import com.plum.tinyos.model.PlumSensingApp;
import com.plum.tinyos.ui.PlumNodeJPanel;

public class PageFrame extends JInternalFrame implements ActionListener {

	SiteManager parent;
	PlumNodeJPanel pnPanel;
	String localPlumId;
	private WindowHandler h;

	public PageFrame(String plumId, SiteManager sm) {
		super("Node: " + plumId, true, true, true, true);
		parent = sm;
		setBounds(50, 0, 300, 175);
		localPlumId = plumId;
		Container contentPane = getContentPane();

		// Create a text area to display the contents of our file in
		// and stick it in a scrollable pane so we can see everything
		// ta = new JTextArea();
		// JScrollPane jsp = new JScrollPane(ta);
		// contentPane.add(jsp, BorderLayout.CENTER);

		// Create a custom JPanel for getting the data for each node
		// TODO: Change this with logic to show Critical Battery
		pnPanel = new PlumNodeJPanel();
		contentPane.add(pnPanel, BorderLayout.WEST);

		JMenuBar jmb = new JMenuBar();
		JMenu fileMenu = new JMenu("Menu");
		JMenuItem saveItem = new JMenuItem("Erase");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);
		jmb.add(fileMenu);
		setJMenuBar(jmb);

		loadContent(Integer.parseInt(plumId));
	}

	public void actionPerformed(ActionEvent ae) {
		Object o = ae.getSource();
		if (o != null) {
			PlumSensingApp localPsa = parent.getPsa();
			int input = JOptionPane.showConfirmDialog((Component) o,
					"Are you sure you want to erase the data?");
			// localPsa.requestSamples(Integer.parseInt(input),"" ,0, 0, true);
			if (input == 0)
				h = parent.getWindowHandler();
				LogRecord r = new LogRecord(Level.INFO,
				        "Start Erase...\n.");
				    h.publish(r);
				localPsa.requestErase(Integer.parseInt(localPlumId));
		}
	}

	public void loadContent(int plumId) {
		try {
			// FileReader fr = new FileReader(filename);
			// ta.read(fr, null);
			// fr.close();
			PlumNode currPlumNode = new PlumNode();
			PlumSensingApp parentPSA = parent.getPsa();
			FlashState localFs = parentPSA.getFlashState();
			ArrayList<PlumNode> plumList = (ArrayList<PlumNode>) localFs
					.getPNList();
			for (PlumNode pn : plumList) {
				if (pn.getId() == plumId) {
					currPlumNode = pn;
					break;
				}
			}
			PlumNode pn = currPlumNode;
			try {
				pnPanel.setPlumNode(pn);
			} catch (NullPointerException NE) {
				NE.printStackTrace();
			}

		} catch (Exception e) {
			System.err.println("Could not load page: " + pnPanel.toString());
		}
	}

}
