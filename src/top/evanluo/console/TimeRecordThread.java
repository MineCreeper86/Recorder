package top.evanluo.console;

import top.evanluo.minecreeper.gui.ProgramFrame;

public class TimeRecordThread implements Runnable {
	private Thread t;
	public static TimeRecordThread body = new TimeRecordThread();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		  ConsoleManager.sendInfo("Time Record Thread", "Thread Running");
	      while(true) {
	    	  long val = (System.currentTimeMillis()-PublicVariables.startTime)/10;
	    	if(PublicVariables.isCounting) {
              if(val >= 0) {
	    	  try {
				Thread.sleep(10);
			  } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
              ProgramFrame.rLabel.setValue(val);
              } else ProgramFrame.rLabel.setValue(0);
	    	}else Thread.yield();
	      }
	}
	
	public void start () {
	      ConsoleManager.sendInfo("Time Record Thread", "Thread Starting");
	      if (t == null) {
	         t = new Thread (this, "TU");
	         t.start();
	      }
	}

}
