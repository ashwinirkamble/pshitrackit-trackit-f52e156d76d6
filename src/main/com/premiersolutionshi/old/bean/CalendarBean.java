package com.premiersolutionshi.old.bean;

import java.util.ArrayList;

/**
 * Data holder for a CALENDAR form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class CalendarBean {
	private String date = null;
	public String getDate() { return this.date; }
	public void setDate(String date) { this.date = date; }

	private String holiday = null;
	public String getHoliday() { return this.holiday; }
	public void setHoliday(String holiday) { this.holiday = holiday; }

	private ArrayList<SupportBean> issueList = null;
	public ArrayList<SupportBean> getIssueList() { return this.issueList; }
	public void setIssueList(ArrayList<SupportBean> issueList) { this.issueList = issueList; }

	private ArrayList<CalendarBean> dayList = null;
	public ArrayList<CalendarBean> getDayList() { return this.dayList; }
	public void setDayList(ArrayList<CalendarBean> dayList) { this.dayList = dayList; }

	private ArrayList<CalendarItemBean> lineItemList = null;
	public ArrayList<CalendarItemBean> getLineItemList() { return this.lineItemList; }
	public void setLineItemList(ArrayList<CalendarItemBean> lineItemList) { this.lineItemList = lineItemList; }

	private ArrayList<CalendarItemBean> ptoTravelList = null;
	public ArrayList<CalendarItemBean> getPtoTravelList() { return this.ptoTravelList; }
	public void setPtoTravelList(ArrayList<CalendarItemBean> ptoTravelList) { this.ptoTravelList = ptoTravelList; }
} // end of class
