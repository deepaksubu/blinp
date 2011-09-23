package com.plum.tinyos.model;


class DataCollector implements Runnable {
	private CollectedData cdata;
	int SHORT_TIMEOUT_COUNT = 5;
	int LONG_TIMEOUT_COUNT = 32;

	public DataCollector(CollectedData collectedData) {
		this.cdata = collectedData;
	}

	public void run() {
		try {
			int size = cdata.size;
			int shortTimeoutCount = 0;
			int longTimeoutCount = 0;
			double expectedSize = (cdata.blockEnd - cdata.blockStart - 1) * (Math.floor(522.0 / PlumSampleMsg.DEFAULT_MESSAGE_SIZE) - 0);
			int numHoles = 0;
		  
			if (expectedSize < 0) {				
				expectedSize = 0;
			}
			
			System.out.print("Expected number of samples : " + expectedSize + "\n\n");
			System.out.print("Received samples : ");
// 			while (cdata.size < expectedSize) {
// 				if (size != cdata.size) {
// 					System.out.print(cdata.size + " / " + expectedSize + " ");
// 					size = cdata.size;
// 				}
// 				else {
// 					timeoutCount = timeoutCount + 1;
// 					if (timeoutCount == TIMEOUT_COUNT*10) {
// 						break;
// 					}
// 				}
// 				Thread.sleep(100L);				
// 			}

			while (true) {
				if (cdata.size == 0) {
					longTimeoutCount = longTimeoutCount + 1;
					if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
						break;
					}
				}
				else {
					if (cdata.size > size) {
						System.out.print(cdata.size + " / " + expectedSize + " ");
						size = cdata.size;
						shortTimeoutCount = 0;
						longTimeoutCount = 0;
					}
					else {
						shortTimeoutCount = shortTimeoutCount + 1;
						longTimeoutCount = longTimeoutCount + 1;
						if (shortTimeoutCount >= SHORT_TIMEOUT_COUNT*10 && cdata.size >= expectedSize) {
							break;
						}
						else if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
							break;
						}							
					}					
				}
				Thread.sleep(100L);								
			}

			// in case of any straggler messages
			Thread.sleep(1000L);

			if (cdata.size > 0 && expectedSize > 0) {
				System.out.print("\nCompleted message - total received: " + cdata.size + " , total expected: at least " + expectedSize + "\n");
				System.out.print("Checking for holes in the data...\n");
			}

			longTimeoutCount = 0;
			while (true) {
				if (cdata.size > 0 && expectedSize > 0) {
					numHoles = cdata.findHoles();
					cdata.writeFile();
					if (numHoles == 0) {
						break;
					}
				}
				else if (expectedSize == 0) {
					cdata.writeFile();
					break;
				}
				else {
					cdata.writeFile();
					System.out.print("\nError - incomplete message - total received: " + cdata.size + " , total expected: at least " + expectedSize + "\n");
					break;
				}

				longTimeoutCount = longTimeoutCount + 1;
				if (longTimeoutCount >= LONG_TIMEOUT_COUNT*10) {
					break;
				}

				Thread.sleep(100L);
			}
			return;

		} catch (InterruptedException iex) {}
	}		
}