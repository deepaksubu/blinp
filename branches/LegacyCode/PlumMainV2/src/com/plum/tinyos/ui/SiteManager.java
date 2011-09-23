package com.plum.tinyos.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import net.tinyos.sf.SerialForwarder;
import com.plum.tinyos.model.PlumConstants;
import com.plum.tinyos.model.PlumList;
import com.plum.tinyos.model.PlumSensingApp;

public class SiteManager extends JFrame {


	  
	JLayeredPane desktop;
	  Vector popups = new Vector();
	  PlumSensingApp psa;
	public PlumList plumList;

	  public PlumSensingApp getPsa() {
		return psa;
	}

	public SiteManager() {
	    super("Plum Manager");
	    plumList=new PlumList();
	    setSize(450, 250);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    Container contentPane = getContentPane();
	    BrowserToolBar jtb = new BrowserToolBar(this);
	    contentPane.add(jtb, BorderLayout.NORTH);
	    
	    //Start Code for Actual devices here
		
			psa = new PlumSensingApp();
			psa.run();
			



		//	InputStreamReader cin = new InputStreamReader(System.in);
		//	BufferedReader in = new BufferedReader(cin);
		//	String input = "";

		//	me.usage();
		

	    //Ends Here
	    // Add our LayeredPane object for the internal frames.
	    desktop = new JDesktopPane();
	    contentPane.add(desktop, BorderLayout.CENTER);
	   
	  }

	  public static void main(String args[]) {
		    
		  String port=PlumConstants.DEFAULT_PORT;
			if (args.length!=0){
				port=args[0];
			}
			else{
				port=PlumConstants.DEFAULT_PORT;
			}
		runSerialForwarder("22");
		SiteManager mgr = new SiteManager();
	    mgr.setVisible(true);
	  }
	  
	  static public void runSerialForwarder(String port){

			try {
					SerialForwarder sfr=new SerialForwarder(new String[]{"-comm","serial@COM"+port+":telos","-no-gui"});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sfr.startListenServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	
	  // Methods to create our internal frames
	  public void addSiteFrame(String name, List<Integer> localAddressList) {
		 
	    SiteFrame sf = new SiteFrame(name,localAddressList,this);
	    popups.addElement(sf);
	    desktop.add(sf, new Integer(1));  // Keep sites on top for now
	    sf.setVisible(true);
	  }
	  
	  public void addPageFrame(String name) {
	    PageFrame pf = new PageFrame(name, this);
	    desktop.add(pf, new Integer(2));
	    pf.setVisible(true);
	    pf.setIconifiable(true);
	    popups.addElement(pf);
	  }

	  public JInternalFrame getCurrentFrame() {
	    for (int i = 0; i < popups.size(); i++) {
	      JInternalFrame currentFrame = (JInternalFrame)popups.elementAt(i);
	      if (currentFrame.isSelected()) {
	        return currentFrame;
	      }
	    }
	    return null;
	  }
}
