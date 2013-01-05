package com.duckie;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task 
 * to execute once 5 seconds have passed.
 */

public class JavaTimer {
    Timer timer;

    public JavaTimer(int i, int current_direction, int prev_direction) {
		// TODO Auto-generated constructor stub
	}

	class RemindTask extends TimerTask {
        public void run() {
//            Motion.flag = 1;
//            System.out.println("FLAG !"+Motion.flag);
        	
            timer.cancel(); //Terminate the timer thread
        }
    }

//    public static void main(String args[]) {
//        new Reminder(5);
//        System.out.format("Task scheduled.%n");
//    	//System.out.println("HELLO WORLD!");
//    }
}