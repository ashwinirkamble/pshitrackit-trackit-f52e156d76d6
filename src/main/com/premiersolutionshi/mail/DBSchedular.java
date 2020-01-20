package com.premiersolutionshi.mail;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
* @author Ashwini
* @version 1.1, 4-Dec-2019
* @since   JDK 8, Apache Struts 1.3.10
*/ 

public class DBSchedular{
	
	public static final DateTimeFormatter COMMON_BASIC_FORMAT_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static Logger logger = Logger.getLogger("MailLogger");
	
	//convert date to basic format
   String getNowInBasicFormatDate() {
 	    return LocalDate.now().format(COMMON_BASIC_FORMAT_DATE);
	}

  public Collection<String> connect()
	{
		ArrayList  mailTo=new ArrayList();					 //list of email address
		UserInfo resultBean = new UserInfo();
				try{
			    Connection conn=null;
			    try
			    {
                Class.forName("org.sqlite.JDBC");  											//database connection
				String url = "jdbc:sqlite:/C:/sqlite3_db/pshi.db";
				conn = DriverManager.getConnection(url);
				logger.info("Successfully Connected to Database");
				System.out.println("Connection Got it!");
			    }catch(Exception e){
			    	logError("DBSchedular | connect","Error in Database Connectivity", e);
			    }
			String date=getNowInBasicFormatDate();
			String sqlStmt="SELECT user_fk,first_name, last_name,email, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw";
	    	    //execute query
	    	    PreparedStatement pStmt=conn.prepareStatement(sqlStmt);
			    ResultSet rs = pStmt.executeQuery();
			     while(rs.next())
			      {
			    	 //check travel end date equals to current date and travel type is "Travel"
				  if(rs.getString("end_date_fmt").equals(date) && rs.getString("leave_type").equals("Travel"))
				  {
					  String email=rs.getString("email");
					  
					  //check email ID is null or not
					  if(email==null || email.equals(""))
					  	{
						 logger.error("No email ID provided for user "+rs.getString("first_name")+" "+rs.getString("last_name"));
					  	}
					  else
					  	{
						  //set mail
						  resultBean.setEmail(rs.getString("email"));
						  mailTo.add(resultBean.getEmail());
					  	}
				  }
			      }
			  
				}catch(Exception e){
					logError("DBSchedular | connect","Exception are",e);
					}
	return mailTo;                          //return list of e-Mail address
	}
  
 

 private void logError(String functionName, String statement, Exception e) {
      if (e != null) {
          logger.error(String.format("%11s%-30s | %s", "", "ERROR", functionName, statement), e);
          e.printStackTrace();
      	}
      else {
    	  		logger.error(String.format("%11s%-30s | %s", "", "ERROR", functionName, statement));
      	}
  }
 
}