package com.premiersolutionshi.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.premiersolutionshi.common.util.ConfigUtils;

/**
* @author Ashwini
* @version 1.1, 3-Dec-2019
* @since   JDK 8, Apache Struts 1.3.10
*/ 
public class JobSchedular implements Job
{
    private static Logger logger = Logger.getLogger("MailLogger");    //get mailLogger of the class
    public void execute(JobExecutionContext context)
	throws JobExecutionException {
	
		logger.info("------------ inside JobSchedular.execute() method------------");
		
		try {
				DBSchedular db=new DBSchedular();
				List toEmailIdArrayList=new ArrayList();
				toEmailIdArrayList=(List) db.connect();

				String toEmailId = null;
				int size=toEmailIdArrayList.size();
				
				//if toEmailIdArrayList size 0 then no Current user found
				if(size<1)
				{
					logger.info("No Current User Found");
					
				}
				else
				{
					//otherwise Send mail to every email address from list 
					for(int j=0;j<toEmailIdArrayList.size();j++)
					{
						System.out.println("sendmail"+(String) toEmailIdArrayList.get(j));
						
			    		toEmailId=(String) toEmailIdArrayList.get(j);
						
			    		logger.info("----------toEmail address -------"+toEmailId);
					  	sendMail(toEmailId);
			  		
					}
						 
				}
			}
			
			catch (Exception e) {
			logError("Job Schedular | execute","----- Job Schedular exception-----", e);
			}	
	}
	private void sendMail(String toEmailId)
	{
		logger.info("----------inside sendMail() method -------"+toEmailId);
        Properties prop = ConfigUtils.getConfigProperties();
        //if properties file is null
        if (prop == null)
        {
        	logger.debug("Could not load configuration file.");
            return;
        }
        String smtpServer = prop.getProperty("RE_SMTP_SERVER");
        final String smtpUser = prop.getProperty("RE_SMTP_USER");
        final String smtpPass = prop.getProperty("RE_SMTP_PASS");
        Integer smtpPort = Integer.parseInt(prop.getProperty("RE_SMTP_PORT"));
         //if all configuration are not found then.
        if (smtpServer.length()<=0 || smtpUser.length()<=0 || smtpPass.length()<=0 || smtpPort == null) {
       logger.info("Not all configurations were found. smtpServer=" + smtpServer + ", smtpUser=" + smtpUser + ", smtpPort=" + smtpPort);
    	 return;
       }
         else
         {
        	 try
     		{
             prop.put("mail.smtp.host", smtpServer);		
             prop.put("mail.smtp.port",smtpPort);
             prop.put("mail.smtp.auth", "true");				//Authentication of user true
             prop.put("mail.smtp.starttls.enable", "true");
             prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                    	
                        return new PasswordAuthentication(smtpUser, smtpPass);
                    }
                });
        		Message message = new MimeMessage(session);
        		message.setFrom(new InternetAddress(smtpUser));
        		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailId));
        		message.setSubject("Travel Report Reminder");
        		String content="Your scheduled travel ends today. This is a reminder to submit your travel report to management within 3 business days.";
        		content+="<h5 style='color:blue'>\n \n Please Do not reply to this email as it is sent from an automated account.</h5>";
        		message.setContent(content,"text/html");
        		Transport.send(message);
        		logger.info("------- eMail sent Successfully to user id ----"+toEmailId);
        		}catch(MessagingException e){
        			logError("sendMail","Exception in Sending eMail",e);

        		}catch(Exception e){
        			logError("sendMail","Exception in Sending eMail",e);
         }
         }
		
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
	

