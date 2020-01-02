package com.premiersolutionshi.mail;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.premiersolutionshi.mail.ConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class JobSchedular implements Job
{
    private static Logger logger = Logger.getLogger(JobSchedular.class.getSimpleName());
    FileHandler fh;  

	public void execute(JobExecutionContext context)
	throws JobExecutionException {
		System.out.println("Hello Quartz!");
		
		try {
			DBSchedular db=new DBSchedular();
	        fh = new FileHandler("C:/Java/logs/mail/mail.log");  
	        logger.info("Log Started");
	        

			List mailTo=new ArrayList();
			mailTo=(List) db.connect();
			
		
			String to = null;

			for(int j=0;j<mailTo.size();j++)
  			{
				System.out.println("sendmail"+(String) mailTo.get(j));

				to=(String) mailTo.get(j);
				sendMail(to);
  			}
	  
			}
			
			catch (Exception e) {
			logger.error("Job Schedular", e);
			}	
	}

	private void sendMail(String to)
	{
		/*gmail start*/
		
		System.out.println("you are under sendmail " +to);
        Properties prop = ConfigUtils.getConfigProperties();
        if (prop == null)
        {
        	logger.debug("Could not load configuration file.");
            return;
        }
        String smtpServer = prop.getProperty("SMTP_SERVER");
        final String smtpUser = prop.getProperty("SMTP_USER");
        final String smtpPass = prop.getProperty("SMTP_PASS");
        Integer smtpPort = Integer.parseInt(prop.getProperty("SMTP_PORT"));
        
		        
        prop.put("mail.smtp.host", smtpServer);
        prop.put("mail.smtp.port",smtpPort);
        prop.put("mail.smtp.auth", "true");				//Authentication of user true
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
         if (smtpServer.length()<=0 || smtpUser.length()<=0 || smtpPass.length()<=0 || smtpPort == null) {
	            logError("constructor", "Not all configurations were found.");

        	 logger.info("Not all configurations were found. smtpServer=" + smtpServer + ", smtpUser=" + smtpUser + ", smtpPort=" + smtpPort);
        	 return;
        }
         else
         {
     	
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                    	
                        return new PasswordAuthentication(smtpUser, smtpPass);
                    }
                });

        try
        {
        	Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(smtpUser));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));


	        message.setSubject("Travel Report Reminder");
	        String content="Your scheduled travel ends today. This is a reminder to submit your travel report to management within 3 business days.";
	        content+="<h5 style='color:blue'>\n \n Please Do not reply to this email as it is sent from an automated account.</h5>";
	        message.setContent(content,"text/html");

	        Transport.send(message);
	        System.out.println("Done"); 
	        logger.info("Mail Send Successfully");
         }catch(MessagingException e){
        	 logger.log(Level.INFO, e.getMessage(), e);
         }catch(Exception e){
        	 logError("sendMail","Exception in Send Mail");
        	 logger.debug("JobSchedular", e);
         }
         }
		
		
	}
	private void logError(String functionName, String statement) {
        logError(functionName, statement);

	}  
}
	

