package com.premiersolutionshi.common.action;

import java.util.*;
import java.text.*;
 
public class ScheduleTask {
 
   static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
   static Timer timer = new Timer();
 
   
   private static class MyTimeTask extends TimerTask {
      public void run() {
    	  try
    	  {
    		  while(true)
    		  {
         System.out.println("Running Task");
         System.out.println("Current Time: " + df.format( new Date()));
         //Thread.sleep(2000);
         Thread.sleep(1000*60);
        // timer.cancel();
    		  }}catch(Exception e){
    		  System.out.println(e);
    	  }
      }
   }
 
   public static void main(String[] args) throws ParseException {
 
      System.out.println("Current Time: " + df.format( new Date()));
 
      //Date and time at which you want to execute
      Date date = df.parse("2019-12-11 04:05:00");
      System.out.println("Current Time"+date.getTime());
     
      timer.schedule(new MyTimeTask(), date);
   }
}