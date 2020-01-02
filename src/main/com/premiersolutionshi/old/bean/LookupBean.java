package com.premiersolutionshi.old.bean;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

/**
 * Data holder for a LOOKUP form
 */
public class LookupBean extends ActionForm {
    private static final long serialVersionUID = 8400272168493048776L;
    private String key = null;
	public String getKey() { return this.key; }
	public void setKey(String key) { this.key = key; }

	private String value = null;
	public String getValue() { return this.value; }
	public void setValue(String value) { this.value = value; }

	private ArrayList<String> valueList = null;
	public ArrayList<String> getValueList() { return this.valueList == null ? new ArrayList<String>() : this.valueList; }
	public void setValueList(ArrayList<String> valueList) { this.valueList = valueList; }

	private String cssStyle = null;
	public String getCssStyle() { return this.cssStyle; }
	public void setCssStyle(String cssStyle) { this.cssStyle = cssStyle; }
} // end of class
