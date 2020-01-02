package com.premiersolutionshi.old.bean;

/**
 * Data holder for a SYSTEM LOCATION bean
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class SystemLocationBean {
	private String location = null;
	public String getLocation() { return this.location; }
	public void setLocation(String location) { this.location = location; }

	private String locationDate = null;
	public String getLocationDate() { return this.locationDate; }
	public void setLocationDate(String locationDate) { this.locationDate = locationDate; }

	private String reason = null;
	public String getReason() { return this.reason; }
	public void setReason(String reason) { this.reason = reason; }

	private String createdBy = null;
	public String getCreatedBy() { return this.createdBy; }
	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	private String createdDate = null;
	public String getCreatedDate() { return this.createdDate; }
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
} // end of class
