
package com.premiersolutionshi.common.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;



import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.premiersolutionshi.common.util.DateUtils;

public class DemoApplication extends TimerTask{
		  	public static void main(String[] args) throws Exception {
			
			DemoApplication task=new DemoApplication();
	        Timer timer = new Timer();  
	        
	        timer.scheduleAtFixedRate(task,1000,1000*60*60*24);// for your case u need to give 1000*60*60*24


    }
	
		 
		public static void sendMail(String Emailto) 
	    {
	    	try {
	        	Email email = new SimpleEmail();
	        	email.setHostName("smtp.gmail.com");
	        	email.setSmtpPort(587);
	        	email.setAuthenticator(new DefaultAuthenticator("ashwinik695@gmail.com", "Ayush@2511"));
	        	email.setSSLOnConnect(true);
	    		email.setFrom("ashwinik695@gmail.com");
	    		email.setSubject("Travel Report Reminder");
	    		email.setMsg("Your scheduled travel ends today. This is a reminder to submit your travel report to management within 3 business days. \n Do not reply to this email as it is sent from an automated account.");
	    		email.addTo(Emailto);
	    		email.send();
	    		System.out.println("Mail send successfully");
	    }
	    	 catch (EmailException e) {
	 			 
	    		 System.out.print(e);
	 			e.printStackTrace();
	 		}
	    }
		@Override
		public void run() {
			Connection conn=null;

			try{
			    String url = "jdbc:sqlite:/C:/sqlite3_db/pshi.db";
			      conn = DriverManager.getConnection(url);
			      System.out.println("Connection Got it!");

			      try{
					  System.out.println("inside try");

			    	  String sqlStmt="SELECT user_fk,first_name, last_name,email, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw";
			    	  PreparedStatement pStmt=conn.prepareStatement(sqlStmt);
					  ResultSet rs = pStmt.executeQuery();
					
					    String date;
				        DateUtils dateUtils=new DateUtils();
				        date=dateUtils.getNowInBasicFormatDate();
				        Calendar c = Calendar.getInstance(); 
				        
				        int hours=c.get(Calendar.HOUR_OF_DAY);
				        int min=c.get(Calendar.MINUTE);
				        
					  int size=0;
					  while(rs.next())
					  {
						  
						 size++;
						 String mailTo=null;
						  if(rs.getString("end_date_fmt").equals(date) && rs.getString("leave_type").equals("Travel"))
						  {
							  System.out.println("mail ="+ rs.getString("email"));
							  if(hours==5 && min==30)
							  {
							  mailTo=rs.getString("email");
							  sendMail(mailTo);
							  }
							  else
							  {}
						  
					  }}
			      }catch(Exception e){}
			      
			}catch(Exception e){}

					}
	    
}