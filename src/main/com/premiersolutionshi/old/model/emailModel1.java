package com.premiersolutionshi.old.model;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.premiersolutionshi.common.util.ConfigUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class emailModel1 {
    private Logger logger = Logger.getLogger(EmailModel.class.getName());

    public static void sendMail(String host, int port, String username, String password, String recipients,
    		String subject, String content, String from) throws AddressException, MessagingException {
    	
    	Properties props = new Properties();
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.host", host);
    	props.put("mail.smtp.port", port);

    	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication(username, password);
    		}
    	});

    	Message message = new MimeMessage(session);
    	message.setFrom(new InternetAddress(from));
    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
    	message.setSubject(subject);
    	message.setText(content);

    	Transport.send(message);
		System.out.println("Mail send successfully");

    }
	 public void sendMail(String Emailto) 
	    {
			System.out.println("Mail From Email Model");

		 Properties prop = ConfigUtils.getConfigProperties();
	        if (prop == null) {
	            logError("constructor", "Could not load configuration file.");
	            return;
	        }
	        String smtpServer = prop.getProperty("SMTP_SERVER");
	        String smtpUser = prop.getProperty("SMTP_USER");
	        String smtpPass = prop.getProperty("SMTP_PASS");
	        Integer smtpPort = StringUtils.parseInt(prop.getProperty("SMTP_PORT"));
	        
	    	try {
	    		Email email = new SimpleEmail();
	        	email.setHostName(smtpServer);
	        	email.setSmtpPort(smtpPort);
	        	email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPass));
	        	//email.setSSLOnConnect(true);
	    		email.setFrom(smtpUser);
	    		email.setSubject("TestMail");
	    		email.setMsg("mail send from email model");
	    		email.addTo(Emailto);
	    		email.send();
	    		System.out.println("Mail send successfully");
	    }
	    	 catch (EmailException e) {
	 			
	    		 System.out.print(e);
	 			e.printStackTrace();
	 		}
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
	public static void main(String[] args) throws EmailException {
		emailModel1 emailmodel=new emailModel1();
		emailmodel.sendMail("ashwinik695@gmail.com");
	
		String SMTP_USER="ashwinik25794@outlook.com";
		String SMTP_PASS="Ayush@257";
		String recipients="ashwinik695@gmail.com";
		try {
			emailmodel.sendMail("m.outlook.com",587,SMTP_USER,SMTP_PASS,recipients,"test","Test",SMTP_USER);
		} catch (AddressException e) {
			System.out.println("Error"+e);
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("Error"+e);

			e.printStackTrace();
		}
	}

}
