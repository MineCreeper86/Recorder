package top.evanluo.console;

import top.evanluo.minecreeper.receiver.DeviceControl;
import top.evanluo.minecreeper.receiver.Serial;

public class SerialReadThread implements Runnable {
	private Thread t;
	public static SerialReadThread body = new SerialReadThread();
	@Override
	public void run() {
		String readStr = "";
		while(true) {
			readStr = readStr + new String(Serial.readFromPort(PublicVariables.arduino));
			if(readStr.contains(";")) {
				ConsoleManager.sendInfo("Serial Read Thread", readStr);
				DeviceControl.processOrder(readStr);
				readStr = "";
			} else Thread.yield();
		}

	}
	
	public void start () {
	      ConsoleManager.sendInfo("Time Record Thread", "Thread Starting");
	      if (t == null) {
	         t = new Thread (this, "SRS");
	         t.start();
	      }
	}
}
