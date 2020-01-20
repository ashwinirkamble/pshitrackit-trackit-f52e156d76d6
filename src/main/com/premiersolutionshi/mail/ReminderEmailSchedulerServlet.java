package com.premiersolutionshi.mail;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
@SuppressWarnings("serial")

/**
 * Servlet for sending reminder email to users on their last day of travel or PTO
 *
 * @author Ashwini
 * @version 1.1, 2-Dec-2019
 * @since   JDK 8, Apache Struts 1.3.10
 */

public class ReminderEmailSchedulerServlet  extends HttpServlet{

	static Logger logger=Logger.getLogger("MailLogger");
	
	public void init()	throws ServletException
	{
		//configure log4j properties file
			//PropertyConfigurator.configure("C:/cloaked/pshi/WEB-INF/config/log4j.properties");
	
			logger.info("------------------------------------------------------------------");
			logger.info("-------------Inside TrackItEmailServlet init() method ------------");
			logger.info("------------------------------------------------------------------");
			System.out.println("------------------------------------------------------------------");
			System.out.println("---------- TrackItEmailServlet Initialized successfully ----------");
			System.out.println("------------------------------------------------------------------");
			try
          		{
        	  //Execute Schedular in servlet
         	 	 JobDetail job = JobBuilder.newJob(JobSchedular.class)
      			.withIdentity("JobSchedule", "group1").build();
         	 	 Trigger trigger = TriggerBuilder.newTrigger()
  .withSchedule(CronScheduleBuilder.cronSchedule("0 0 12 * * ?")).build();   // TZ="US/Hawaii" //setup time to execute code
      	//schedule it
      	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
      	scheduler.start();
      	scheduler.scheduleJob(job, trigger);

      }catch(Exception e)
      	{
      	logger.error(e);
      	}
          
         logger.info("----------- TrackItEmailServlet End of init() menthod for the hour ------------------------");

      }

	
	}	

