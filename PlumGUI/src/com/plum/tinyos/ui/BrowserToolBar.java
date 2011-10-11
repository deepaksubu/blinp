package com.plum.tinyos.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import com.plum.tinyos.model.FlashState;
import com.plum.tinyos.model.PlumConstants;
import com.plum.tinyos.model.PlumSensingApp;

public class BrowserToolBar extends JToolBar{
	SiteManager sm;
	PlumSensingApp localPsa;
	FlashState fs;
	List<Integer> localAddressList;
	HashSet<Long> localSet;

	public BrowserToolBar() {
		String[] toolbarLabels = { PlumConstants.START_SCAN, PlumConstants.READ };
		this.setPreferredSize(new Dimension(40, 40));
		// this.setFont(new Font(name, CENTER, alignmentX));
		Insets margins = new Insets(20, 20, 20, 20);
		for (int i = 0; i < toolbarLabels.length; i++) {
			ToolBarButton button = new ToolBarButton(toolbarLabels[i], sm);
			button.addActionListener(new ActionListener() {

			

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					localPsa = sm.getPsa();
					// localAddressList = new ArrayList<Long>();
					// localSet = new HashSet<Long>();

					if (e.getActionCommand() == PlumConstants.START_SCAN) {
						// System.out.println("Deepak" + e.getActionCommand());
						localPsa.requestScan();
						// localAddressList = new ArrayList<Long>();

						/*
						 * try { Thread.sleep(PlumConstants.SCAN_PERIOD); }
						 * catch (InterruptedException e1) { // TODO
						 * Auto-generated catch block e1.printStackTrace(); }
						 */

						localAddressList = localPsa.getFlashState()
								.getAddrList();
						// localSet = new HashSet<Long>();
						try {
							fs = localPsa.getFlashState();
							sm.addSiteFrame("scan", localAddressList);
						} catch (NullPointerException NE) {
							NE.printStackTrace();
						}

					} else if (e.getActionCommand() == PlumConstants.READ) {
						localPsa.requestScan();
						// JFileChooser fc = new JFileChooser();

						// int returnVal = fc.showSaveDialog((ToolBarButton) e
						// .getSource());
						// if (returnVal == JFileChooser.APPROVE_OPTION) {
						// File file = fc.getSelectedFile();
						// localPsa.setFile();
						// }
						// localAddressList = new ArrayList<Long>();
						// localAddressList = fs.getAddrList();
						// localSet = new HashSet<Long>(localAddressList);
						Long start = System.currentTimeMillis();
						Long end = System.currentTimeMillis()
								+ PlumConstants.READ_TIMEOUT_UI * 1000L;

						// for (int i : localAddressList) {
						String input = JOptionPane
								.showInputDialog("Enter Plum Node ID:");
						localPsa.requestSamples(Integer.parseInt(input), "", 0,
								0, true);
						
						/*
						 * try { Thread.sleep(100000L); } catch
						 * (InterruptedException e1) { // TODO Auto-generated
						 * catch block e1.printStackTrace(); }
						 */

						// }

					}
					// In response to a button click:
					// Write data from tempCd to a file in csv format

					else {
					}

				}

			}

			);
			button.setToolTipText(toolbarLabels[i]);
			button.setMargin(margins);
			add(button);
		}
	}

	public BrowserToolBar(SiteManager siteManager) {
		this();
		this.sm = siteManager;
	}

	public void setTextLabels(boolean labelsAreEnabled) {
		Component c;
		int i = 0;
		while ((c = getComponentAtIndex(i++)) != null) {
			ToolBarButton button = (ToolBarButton) c;
			if (labelsAreEnabled)
				button.setText(button.getToolTipText());
			else
				button.setText(null);
		}
	}


}