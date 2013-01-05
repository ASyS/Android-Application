package com.duckie;


abstract class AndroidTimer extends Thread {
    public abstract void onFinish();
    private boolean mCancelled = false;
    private long secs = 10;
    public void run() {
    	mCancelled = false;
    	start(secs);
    }
  
    public void setTimer(long secs){
    	this.secs = secs;
    }
    
    private final void start(long secs) {
    	long delay = secs * 1000;
    	do {
    		System.out.println("LEFT: "+delay/1000);
    		delay = delay - 1000;
    		try {
    			Thread.sleep(1000);
    		} catch (InterruptedException e) { }
    		
    		if(mCancelled) {
    			return;
    		}
    	}
    	while (delay > 0);
    		onFinish();
    		//cancel();
    	}
        
    public void cancel() {
    	mCancelled = true;
    }
}
