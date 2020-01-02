package com.premiersolutionshi.old.bean;

import java.util.ArrayList;

/**
 * Data holder for a USER form
 */
public class UserBean extends LastUpdatedBean {
    private static final long serialVersionUID = 6615818805739965896L;

    private String userPk = null;
    private String username = null;
    private String password = null;
    private String lastName = null;
    private String firstName = null;
    private String fullName = null;
    private String email = null;
    private String organization = null;
    private String currOrganization = null;
    private String title = null;
    private String workNumber = null;
    private String quickDial = null;
    private String faxNumber = null;
    private String cellNumber = null;
    private String notes = null;
    private String rank = null;
    private String[] roleArr = null;
    private String projectPk = null;
    private String[] projectPkArr = null;
    private String pocPk = null;
    private String shipPocPk = null;
    private String shipPk = null;
    private String dept = null;
    private String altEmail = null;
    private String ptoTravelPk = null;
    private String startDate = null;
    private String endDate = null;
    private String leaveType = null;
    private String location = null;
    private String isPrimaryPoc = null;
    private int size; 
    public String getAltEmail() {
        return this.altEmail;
    }

    public String getCellNumber() {
        return this.cellNumber;
    }

    public String getCurrOrganization() {
        return this.currOrganization;
    }

    public String getDept() {
        return this.dept;
    }

    public String getEmail() {
        return this.email;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getFirstNameJs() {
        return js(this.firstName);
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getIsPrimaryPoc() {
        return this.isPrimaryPoc;
    }

    public boolean isPrimaryPoc() {
        return isPrimaryPoc != null && isPrimaryPoc.equals("Y");
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getLastNameJs() {
        return js(this.lastName);
    }

    public String getLeaveType() {
        return this.leaveType;
    }

    public String getLocation() {
        return this.location;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getOrganization() {
        return this.organization;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPocPk() {
        return this.pocPk;
    }

    public String getProjectPk() {
        return this.projectPk;
    }

    public String[] getProjectPkArr() {
        return this.projectPkArr == null ? new String[0] : this.projectPkArr;
    }

    public String getPtoTravelPk() {
        return this.ptoTravelPk;
    }

    public String getQuickDial() {
        return this.quickDial;
    }

    public String getRank() {
        return this.rank;
    }

    public String[] getRoleArr() {
        return this.roleArr == null ? new String[0] : this.roleArr;
    }

    public String getShipPk() {
        return this.shipPk;
    }

    public String getShipPocPk() {
        return this.shipPocPk;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUsernameJs() {
        return js(this.username);
    }

    public String getUserPk() {
        return this.userPk;
    }

    public String getWorkNumber() {
        return this.workNumber;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public void setCellNumber(String newCellNumber) {
        this.cellNumber = newCellNumber;
    }

    public void setCurrOrganization(String newCurrOrganization) {
        this.currOrganization = newCurrOrganization;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setFaxNumber(String newFaxNumber) {
        this.faxNumber = newFaxNumber;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIsPrimaryPoc(String isPrimaryPoc) {
        this.isPrimaryPoc = isPrimaryPoc;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNotes(String newNotes) {
        this.notes = newNotes;
    }

    public void setOrganization(String newOrganization) {
        this.organization = newOrganization;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setPocPk(String pocPk) {
        this.pocPk = pocPk;
    }

    public void setProjectPk(String newProjectPk) {
        this.projectPk = newProjectPk;
    }

    public void setProjectPkArr(String[] projectPkArr) {
        this.projectPkArr = projectPkArr;
    }

    public void setPtoTravelPk(String ptoTravelPk) {
        this.ptoTravelPk = ptoTravelPk;
    }

    public void setQuickDial(String newQuickDial) {
        this.quickDial = newQuickDial;
    }

    public void setRank(String newRank) {
        this.rank = newRank;
    }

    public void setRoleArr(String[] roleArr) {
        this.roleArr = roleArr;
    }

    public void setShipPk(String shipPk) {
        this.shipPk = shipPk;
    }

    public void setShipPocPk(String shipPocPk) {
        this.shipPocPk = shipPocPk;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setUserPk(String newUserPk) {
        this.userPk = newUserPk;
    }

    public void setWorkNumber(String newWorkNumber) {
        this.workNumber = newWorkNumber;
 }
 
  public  int getSize(){
        return this.size;
    }
    
   public void setSize(int size) {
	    
    	this.size=size;
    }
  
/*set arraylist of currentpto ashwini*/  
   
   
    private ArrayList<UserBean> list = null;
	public ArrayList<UserBean> getList()
	{
		return this.list == null ? new ArrayList<UserBean>() : this.list;
	}
	public void setList(ArrayList<UserBean> newList)
	{
		this.list = newList;
		}
	

	
}
