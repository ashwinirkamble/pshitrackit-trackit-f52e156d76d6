package com.premiersolutionshi.mail;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
@SuppressWarnings("serial")
public class TrackItEmailServlet  extends HttpServlet{

	static Logger logger=Logger.getLogger(TrackItEmailServlet.class.getSimpleName());

	public void init()	throws ServletException
	{
		
		  System.out.println("------------------------------------------------------------------");
          System.out.println("---------- TrackItEmailServlet Initialized successfully ----------");
		  System.out.println("------------------------------------------------------------------");

          try
          		{
      	         System.out.println("hii");
         	 	 JobDetail job = JobBuilder.newJob(JobSchedular.class)
      			.withIdentity("JobSchedule", "group1").build();
    	
         	 	 Trigger trigger = TriggerBuilder.newTrigger()
  .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ? *")).build();   // TZ="US/Hawaii"
      	//schedule it
      	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
      	scheduler.start();
      	scheduler.scheduleJob(job, trigger);

      }catch(Exception e)
      	{
      	logger.error(e);
      	}
      }

	}	

