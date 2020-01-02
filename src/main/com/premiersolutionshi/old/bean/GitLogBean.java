package com.premiersolutionshi.old.bean;

import org.apache.struts.action.ActionForm;

/**
 * Data holder for a GIT LOG form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 8, Apache Struts 1.3.10
 */
public class GitLogBean extends ActionForm {
	private String id = null;
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }

	private String date = null;
	public String getDate() { return this.date; }
	public void setDate(String date) { this.date = date; }

	private String msg = null;
	public String getMsg() { return this.msg; }
	public void setMsg(String msg) { this.msg = msg; }

} // end of class
