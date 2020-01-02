package com.premiersolutionshi.old.bean;

/**
 * Data holder for a CALENDAR ITEM form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class CalendarItemBean {
	private String time = null;
	public String getTime() { return this.time; }
	public void setTime(String time) { this.time = time; }

	private String shipName = null;
	public String getShipName() { return this.shipName; }
	public void setShipName(String shipName) { this.shipName = shipName; }

	private String location = null;
	public String getLocation() { return this.location; }
	public void setLocation(String location) { this.location = location; }

	private String lineItem = null;
	public String getLineItem() { return this.lineItem; }
	public void setLineItem(String lineItem) { this.lineItem = lineItem; }

	private String url = null;
	public String getUrl() { return this.url; }
	public void setUrl(String url) { this.url = url; }

	private String cssStyle = null;
	public String getCssStyle() { return this.cssStyle; }
	public void setCssStyle(String cssStyle) { this.cssStyle = cssStyle; }

	private String comments = null;
	public String getComments() { return this.comments; }
	public void setComments(String comments) { this.comments = comments; }
} // end of class
