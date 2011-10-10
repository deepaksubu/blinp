package com.plum.tinyos.model;

import java.io.FileNotFoundException;

import net.tinyos.message.MoteIF;

public class DataCollector implements Runnable {
	private PlumSensingApp app;
	private CollectedData cdata;
	private int addr;
	private MoteIF mote;
	private int blockStart;
	private int blockEnd;
	private int retries = 0;
	private int readRetries = 0;
	int READ_RETRIES = 3;
	int MAX_RETRIES = 5;
	int SHORT_TIMEOUT_COUNT = 5;
	int LONG_TIMEOUT_COUNT = 32;
	private boolean writeFlag;

	public DataCollector(PlumSensingApp app, CollectedData collectedData,
			int addr, MoteIF mote, int blockStart, int blockEnd) {
		this.app = app;
		this.cdata = collectedData;
		this.addr = addr;
		this.mote = mote;
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.writeFlag = true;
	}

	public void run() {
		try {
			int size = cdata.size;
			int shortTimeoutCount = 0;
			int longTimeoutCount = 0;
			double expectedSize = (blockEnd - blockStart - 1)
					* (Math.floor(522.0 / PlumSampleMsg.DEFAULT_MESSAGE_SIZE) - 0);
			int numHoles = 0;

			if (expectedSize < 0) {
				expectedSize = 0;
			}

			System.out.print("Expected number of samples : " + expectedSize
					+ "\n\n");
			System.out.print("Received samples : ");
			// while (cdata.size < expectedSize) {
			// if (size != cdata.size) {
			// System.out.print(cdata.size + " / " + expectedSize + " ");
			// size = cdata.size;
			// }
			// else {
			// timeoutCount = timeoutCount + 1;
			// if (timeoutCount == TIMEOUT_COUNT*10) {
			// break;
			// }
			// }
			// Thread.sleep(100L);
			// }

			while (true) {
				if (cdata.size == 0) {
					longTimeoutCount = longTimeoutCount + 1;
					if (longTimeoutCount >= LONG_TIMEOUT_COUNT * 10) {
						break;
					}
				} else {
					if (cdata.size > size) {
						System.out.print(cdata.size + " / " + expectedSize
								+ " ");
						size = cdata.size;
						shortTimeoutCount = 0;
						longTimeoutCount = 0;
					} else {
						System.out.println("shortTimeoutCount Increment 1"+longTimeoutCount);
						shortTimeoutCount = shortTimeoutCount + 1;
						System.out.println("longTimeoutCount Increment 1"+longTimeoutCount);
						longTimeoutCount = longTimeoutCount + 1;
						if (shortTimeoutCount >= SHORT_TIMEOUT_COUNT * 10
								&& cdata.size >= expectedSize) {
							break;
						} else if (longTimeoutCount >= LONG_TIMEOUT_COUNT * 10) {
							break;
						}
					}
				}
				Thread.sleep(100L);
			}

			// in case of any straggler messages
			Thread.sleep(1000L);

			if (cdata.size > 0 && expectedSize > 0) {
				System.out.print("\nCompleted message - total received: "
						+ cdata.size + " , total expected: at least "
						+ expectedSize + "\n");
				System.out.print("Checking for holes in the data...\n");
			}

			longTimeoutCount = 0;
			while (true) {
				System.out.print("\nCurrent Status: " + cdata.size + " / "
						+ expectedSize + " \n");
				size = cdata.size;
				if (retries >= MAX_RETRIES) {
					// need to write file here
					System.out
							.print("\nReached maximum number of retries. Ending hole finding with "
									+ numHoles + " holes left...\n");

					if (!mote.getSource().getPacketSource().getName()
							.contains("sf@localhost:9002")) {
						System.out.println("Closing source : "
								+ mote.getSource().getPacketSource().getName());
						mote.getSource().shutdown();
					}
					break;
				}

				if (cdata.size > 0 && expectedSize > 0) {
					readRetries = readRetries + 1;
					System.out.println("Read Retries Increment 1"+readRetries);
					numHoles = cdata.findHoles(mote);
					retries = retries + 1;
					System.out.println("Plain Retries Increment 1"+retries);
					if (numHoles == 0) {
                   
						if (!mote.getSource().getPacketSource().getName()
								.contains("sf@localhost:9002")) {
							System.out.println("Closing source : "
									+ mote.getSource().getPacketSource()
											.getName());
							mote.getSource().shutdown();
						}
						System.out.println("Number of Holes is zero");
						break;
					}
					
				} else if (expectedSize == 0) {
					
					break;
				} else {
					if (readRetries >= READ_RETRIES) {
						System.out
								.print("\nError - incomplete message - maximum number of retries reached. Total received: "
										+ cdata.size
										+ " , total expected: at least "
										+ expectedSize + "\n");

						if (!mote.getSource().getPacketSource().getName()
								.contains("sf@localhost:9002")) {
							System.out.println("Closing source : "
									+ mote.getSource().getPacketSource()
											.getName());
							mote.getSource().shutdown();
						}
                        writeFlag=false;
						break;
					} else {
						System.out.println("This is start of request samples");
						app.requestSamples(addr, mote.getSource()
								.getPacketSource().getName(), blockStart,
								blockEnd, false);
						Thread.sleep(32000L);
						System.out.println("This is end of request samples");
					}
					System.out.println("Read Retries Increment 2"+readRetries);
					readRetries = readRetries + 1;
				}
				System.out.println("longTimeoutCount Increment 2"+longTimeoutCount);
				longTimeoutCount = longTimeoutCount + 1;
				if (longTimeoutCount >= LONG_TIMEOUT_COUNT * 10) {
					break;
				}

				Thread.sleep(100L);
			}

			if (writeFlag & cdata.size > 0) {
				cdata.writeFile();
			}
			
			return;

		} catch (InterruptedException iex) {
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/**
 * public class DataCollector implements Runnable { private PlumSensingApp app;
 * private CollectedData cdata; private int addr; private int blockStart;
 * private int blockEnd; private int retries = 0; private int readRetries = 0;
 * int READ_RETRIES = 3; int MAX_RETRIES = 5; int SHORT_TIMEOUT_COUNT = 5; int
 * LONG_TIMEOUT_COUNT = 32;
 * 
 * public DataCollector(PlumSensingApp app, CollectedData collectedData, int
 * addr, int blockStart, int blockEnd) { this.app = app; this.cdata =
 * collectedData; this.addr = addr; this.blockStart = blockStart; this.blockEnd
 * = blockEnd; }
 * 
 * public void run() { try { int size = cdata.size; int shortTimeoutCount = 0;
 * int longTimeoutCount = 0; boolean writeFlag=false; double expectedSize =
 * (blockEnd - blockStart - 1) * (Math.floor(522.0 /
 * PlumSampleMsg.DEFAULT_MESSAGE_SIZE) - 0); int numHoles = 0;
 * 
 * if (expectedSize < 0) { expectedSize = 0; }
 * 
 * System.out.print("Expected number of samples : " + expectedSize + "\n\n");
 * System.out.print("Received samples : "); // while (cdata.size < expectedSize)
 * { // if (size != cdata.size) { // System.out.print(cdata.size + " / " +
 * expectedSize + " "); // size = cdata.size; // } // else { // timeoutCount =
 * timeoutCount + 1; // if (timeoutCount == TIMEOUT_COUNT*10) { // break; // }
 * // } // Thread.sleep(100L); // }
 * 
 * while (true) { if (cdata.size == 0) { longTimeoutCount = longTimeoutCount +
 * 1; if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) { break; } } else { if
 * (cdata.size > size) { System.out.print(cdata.size + " / " + expectedSize +
 * " "); size = cdata.size; shortTimeoutCount = 0; longTimeoutCount = 0; } else
 * { shortTimeoutCount = shortTimeoutCount + 1; longTimeoutCount =
 * longTimeoutCount + 1; if (shortTimeoutCount >= SHORT_TIMEOUT_COUNT*10 &&
 * cdata.size >= expectedSize) { break; } else if (longTimeoutCount >=
 * LONG_TIMEOUT_COUNT*10) { break; } } } Thread.sleep(100L); }
 * 
 * // in case of any straggler messages Thread.sleep(1000L);
 * 
 * if (cdata.size > 0 && expectedSize > 0) {
 * System.out.print("\nCompleted message - total received: " + cdata.size +
 * " , total expected: at least " + expectedSize + "\n");
 * System.out.print("Checking for holes in the data...\n"); }
 * 
 * longTimeoutCount = 0; while (true) { System.out.print("\nCurrent Status: " +
 * cdata.size + " / " + expectedSize + " \n"); size = cdata.size; if (retries >=
 * MAX_RETRIES) { // need to write file here writeFlag=true; System.out.print(
 * "\nReached maximum number of retries. Ending hole finding with " + numHoles +
 * " holes left...\n"); break; }
 * 
 * if (cdata.size > 0 && expectedSize > 0) { numHoles = cdata.findHoles();
 * writeFlag=true; retries = retries + 1; if (numHoles == 0) { break; } } else
 * if (expectedSize == 0) { writeFlag=true; break; } else { if (readRetries >=
 * READ_RETRIES) { System.out.print(
 * "\nError - incomplete message - maximum number of retries reached. Total received: "
 * + cdata.size + " , total expected: at least " + expectedSize + "\n");
 * writeFlag = false; break; } else { app.requestSamples(addr, blockStart,
 * blockEnd, false); } readRetries = readRetries + 1; }
 * 
 * longTimeoutCount = longTimeoutCount + 1; if (longTimeoutCount >=
 * LONG_TIMEOUT_COUNT*10) { break; }
 * 
 * Thread.sleep(100L); } if (writeFlag){ try { cdata.writeFile(); } catch
 * (FileNotFoundException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } return;
 * 
 * } catch (InterruptedException iex) { try { cdata.writeFile(); } catch
 * (FileNotFoundException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } } }
 */
