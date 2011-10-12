package com.plum.tinyos.ui;


import java.awt.BorderLayout;

import java.awt.Container;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;

import net.tinyos.message.MoteIF;
import net.tinyos.packet.BuildSource;
import net.tinyos.packet.PhoenixSource;
import net.tinyos.sf.SerialForwarder;
import net.tinyos.util.PrintStreamMessenger;

import com.plum.tinyos.model.PlumConstants;
import com.plum.tinyos.model.PlumList;
import com.plum.tinyos.model.PlumSampleMsg;
import com.plum.tinyos.model.PlumSensingApp;
import com.plum.tinyos.model.PlumStatusMsg;
import com.plum.tinyos.log.*;

public class SiteManager extends JFrame {


	  
	JLayeredPane desktop;
	  Vector popups = new Vector();
	  PlumSensingApp psa;
	  private WindowHandler handler = null;
	  private Logger logger = null;
	  
	  public WindowHandler getWindowHandler(){
		  return this.handler;
	  }
	  
	  public Logger getLogger(){
		  return this.logger;
	  }
	public PlumList plumList;

	  public PlumSensingApp getPsa() {
		return psa;
	}
	  
 public static void runSerialForwarder(){
		  
		  String defaultSource = new String();
		  try {
				// Execute command
				String command = "bash -c motelist";
				Process child = Runtime.getRuntime().exec(command);
				
				// Get the input stream and read from it
				InputStream inStream = child.getInputStream();
				int c;
				StringBuffer output = new StringBuffer();
				while ((c = inStream.read()) != -1) {
					output.append(((char)c));
				}
				inStream.close();
				String[] lines = output.toString().split("\n");

				if (lines.length < 3) {
					System.out.println("erewrew");
					throw new IOException();
				}
				else {
					int lineIndex;
					for (lineIndex = 2 ; lineIndex < lines.length ; lineIndex++) {
						String[] line = lines[lineIndex].split("\\s+");
						if (line[0].charAt(0) == 'U') {
							defaultSource = "serial@" + line[1] + ":telos";
							break;
						}
					}
				}
			} catch (IOException e) {
				System.out.print("Error (mote not connected?): " + e);
			}
	      
		  
/*		  try {
				PhoenixSource pSource = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
				baseMote = new MoteIF(pSource);
			}
			catch (Exception e) {
				System.err.println("Cannot listen to base station: " + e);
			}
*/
			try {
					SerialForwarder sfr=new SerialForwarder(new String[]{"-comm",defaultSource,"-no-gui","-quiet"});
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
  

	public SiteManager() {
	    super("Plum Manager");
	    
	    plumList=new PlumList();
	    setSize(450, 600);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    Container contentPane = getContentPane();
	    BrowserToolBar jtb = new BrowserToolBar(this);
	    contentPane.add(jtb, BorderLayout.NORTH);
	    
	    //Start Code for Actual devices here
	      
			psa = new PlumSensingApp(this);
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
		  runSerialForwarder();
		    
		/*  String port=PlumConstants.DEFAULT_PORT;
			if (args.length!=0){
				port=args[0];
			}
			else{
				port=PlumConstants.DEFAULT_PORT;
			}*/
		
		
		
		 SiteManager mgr = new SiteManager();
		 mgr.createWindowHandler();
		 mgr.setVisible(true);
	  }
	  
	 
	  

	
	  private void createWindowHandler() {
		// TODO Auto-generated method stub
		  handler = WindowHandler.getInstance(this);
		    //obtaining a logger instance and setting the handler
		    logger = Logger.getLogger("sam.logging.handler");
		    logger.addHandler(handler);
		
	}

	// Methods to create our internal frames
	  public void addSiteFrame(String name, List<Integer> localAddressList) {
		 
	    SiteFrame sf = new SiteFrame(name,localAddressList,this);
	    popups.addElement(sf);
	    desktop.add(sf, new Integer(2));  // Keep sites on top for now
	    sf.setVisible(true);
	  }
	  
	  public void addPageFrame(String name) {
	    PageFrame pf = new PageFrame(name, this);
	    desktop.add(pf, new Integer(3));
	    pf.setVisible(true);
	    pf.setIconifiable(true);
	    popups.addElement(pf);
	  }
	  
	  public LogWindow addLogWindow(String name,int length,int width) {
		    LogWindow lf = new LogWindow(name,length,width);
		    desktop.add(lf, new Integer(1));
		    lf.setVisible(true);
		    lf.setIconifiable(true);
		    popups.addElement(lf);
			return lf;
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
