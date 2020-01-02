package com.premiersolutionshi.mail;

public class User {

	 private String email = null;
	 public String smtp_mail=null;
	 public String smtp_pass=null;
	 public String getEmail() {
	        return this.email;
	    }
	 public void setEmail(String newEmail) {
	        this.email = newEmail;
	    }
	 
	 
	 public String getSmtpEmail() {
	        return this.smtp_mail;
	    }
	 public void setSmtpEmail(String newEmail) {
	        this.smtp_mail = newEmail;
	    }
	
	 public String getSmtpPass() {
	        return this.smtp_pass;
	    }
	 public void setSmtpPass(String newPass) {
	        this.smtp_pass = newPass;
	    }
}
