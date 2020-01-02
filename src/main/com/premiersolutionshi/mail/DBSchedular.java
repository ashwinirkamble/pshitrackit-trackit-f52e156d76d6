package com.premiersolutionshi.mail;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DBSchedular{
	
	public static final DateTimeFormatter COMMON_BASIC_FORMAT_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static Logger logger = Logger.getLogger(DBSchedular.class.getSimpleName());
    String getNowInBasicFormatDate() {
 	    return LocalDate.now().format(COMMON_BASIC_FORMAT_DATE);
	}

  public Collection<String> connect()
	{
		ArrayList  mailTo=new ArrayList();
		// String[] mailTo;
		User resultBean = new User();
				try{
					
			    Connection conn=null;
			    try
			    {
                Class.forName("org.sqlite.JDBC");  
				String url = "jdbc:sqlite:/C:/sqlite3_db/pshi.db";
				conn = DriverManager.getConnection(url);
				logger.info("Successfully Connected to Database");

				System.out.println("Connection Got it!");

			    }catch(Exception e){
			    	logger.error("Error in Database Connectivity", e);

			    }
				String date=getNowInBasicFormatDate();
	    	    String sqlStmt="SELECT user_fk,first_name, last_name,email, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw";
	    	    PreparedStatement pStmt=conn.prepareStatement(sqlStmt);
			    ResultSet rs = pStmt.executeQuery();
			     int size=0;
			   while(rs.next())
			      {
				  if(rs.getString("end_date_fmt").equals(date) && rs.getString("leave_type").equals("Travel"))
				  {
					  size++;
					  String email=rs.getString("email");
					  if(email==null || email.equals(""))
					  {
						  logger.error("No email ID provided for user "+rs.getString("first_name")+" "+rs.getString("last_name"));
			    	  }
					  else{
					  resultBean.setEmail(rs.getString("email"));
					  mailTo.add(resultBean.getEmail());
					  	}
				  }
				 
			  }
			  
				}catch(Exception e){
					logError("connect","Exception are",e);
				}
	return mailTo;
	}
  
  private void logError(String functionName, String statement) {
      logError(functionName, statement, null);
  }

  private void logError(String functionName, String statement, Exception e) {
      if (e != null) {
          logger.error(String.format("%11s%-30s | %-34s | %s", "", "ERROR", functionName, statement), e);
          e.printStackTrace();
      }
      else {
          logger.error(String.format("%11s%-30s | %-34s | %s", "", "ERROR", functionName, statement));
      }
  }
}