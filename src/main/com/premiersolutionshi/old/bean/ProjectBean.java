package com.premiersolutionshi.old.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * Data holder for a PROJECT form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class ProjectBean extends ActionForm {
	private boolean isEmpty(String tData) { return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null")); }
	private String nvl(String value, String nullValue) { return (isEmpty(value) ? nullValue : value); }
	private String nes(String tStr) { return (isEmpty(tStr) ? "" : tStr); }
	private String js(String tStr) { return nes(tStr).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "&quot;").replaceAll("'", "\\\\\'"); }

	private String taskPk = null;
	public String getTaskPk() { return nes(this.taskPk); }
	public void setTaskPk(String newTaskPk) { this.taskPk = newTaskPk; }

	private String title = null;
	public String getTitle() { return nes(this.title); }
	public String getTitleJs() { return js(this.title); }
	public void setTitle(String newTitle) { this.title = newTitle; }

	private String description = null;
	public String getDescription() { return nes(this.description); }
	public List<String> getDescriptionBr() { return java.util.Arrays.asList(nes(this.description).split("\n", -1)); }
	public void setDescription(String newDescription) { this.description = newDescription; }

	private String source = null;
	public String getSource() { return nes(this.source); }
	public void setSource(String newSource) { this.source = newSource; }

	private String currSource = null;
	public String getCurrSource() { return nes(this.currSource); }
	public void setCurrSource(String newCurrSource) { this.currSource = newCurrSource; }

	private String status = null;
	public String getStatus() { return nes(this.status); }
	public void setStatus(String newStatus) { this.status = newStatus; }

	private String currStatus = null;
	public String getCurrStatus() { return nes(this.currStatus); }
	public void setCurrStatus(String newCurrStatus) { this.currStatus = newCurrStatus; }

	public String getStatusCss() {
		return nes(this.status).equals("Completed")		? "color:#8c8c8c;" :
					 nes(this.status).equals("Resolved") 		? "color:#FF0000;" :
					 nes(this.status).equals("In Progress") ? "color:#FFA500;" :
					 nes(this.status).equals("Not Started") ? "color:#008000;" :
																										"color:inherit;";
	}

	private String priority = null;
	public String getPriority() { return nes(this.priority); }
	public void setPriority(String newPriority) { this.priority = newPriority; }

	public String getPriorityCss() {
		return nes(this.status).equals("Completed")		 ? "color:#8c8c8c;" :
					 nes(this.priority).equals("1-Critical") ? "color:#800080;" :
					 nes(this.priority).equals("2-High") 		 ? "color:#FF0000;" :
					 nes(this.priority).equals("3-Medium") 	 ? "color:#FFA500;" :
					 nes(this.priority).equals("4-Low") 		 ? "color:#008000;" :
						 																				 "color:inherit;";
	}

	private String currPriority = null;
	public String getCurrPriority() { return nes(this.currPriority); }
	public void setCurrPriority(String newCurrPriority) { this.currPriority = newCurrPriority; }

	private String uic = null;
	public String getUic() { return nes(this.uic); }
	public void setUic(String newUic) { this.uic = newUic; }

	private String shipName = null;
	public String getShipName() { return nes(this.shipName); }
	public void setShipName(String newShipName) { this.shipName = newShipName; }

	private String homeport = null;
	public String getHomeport() { return this.homeport; }
	public void setHomeport(String homeport) { this.homeport = homeport; }

	private String personAssigned = null;
	public String getPersonAssigned() { return nes(this.personAssigned); }
	public void setPersonAssigned(String newPersonAssigned) { this.personAssigned = newPersonAssigned; }

	private String currPersonAssigned = null;
	public String getCurrPersonAssigned() { return nes(this.currPersonAssigned); }
	public void setCurrPersonAssigned(String newCurrPersonAssigned) { this.currPersonAssigned = newCurrPersonAssigned; }

	private String isInternal = null;
	public String getIsInternal() { return nes(this.isInternal); }
	public void setIsInternal(String newIsInternal) { this.isInternal = newIsInternal; }

	private String notes = null;
	public String getNotes() { return nes(this.notes); }
	public List<String> getNotesBr() { return java.util.Arrays.asList(nes(this.notes).split("\n", -1)); }
	public void setNotes(String newNotes) { this.notes = newNotes; }

	private String createdBy = null;
	public String getCreatedBy() { return nes(this.createdBy); }
	public void setCreatedBy(String newCreatedBy) { this.createdBy = newCreatedBy; }

	private String createdDate = null;
	public String getCreatedDate() { return nes(this.createdDate); }
	public void setCreatedDate(String newCreatedDate) { this.createdDate = newCreatedDate; }

	private String followUpDate = null;
	public String getFollowUpDate() { return nes(this.followUpDate); }
	public void setFollowUpDate(String newFollowUpDate) { this.followUpDate = newFollowUpDate; }

	private String dueDate = null;
	public String getDueDate() { return nes(this.dueDate); }
	public void setDueDate(String newDueDate) { this.dueDate = newDueDate; }

	private String dueDateStart = null;
	public String getDueDateStart() { return nes(this.dueDateStart); }
	public void setDueDateStart(String newDueDateStart) { this.dueDateStart = newDueDateStart; }

	private String dueDateEnd = null;
	public String getDueDateEnd() { return nes(this.dueDateEnd); }
	public void setDueDateEnd(String newDueDateEnd) { this.dueDateEnd = newDueDateEnd; }

	private String dueDateCss = null;
	public String getDueDateCss() { return nes(this.dueDateCss); }
	public void setDueDateCss(String newDueDateCss) { this.dueDateCss = newDueDateCss; }

	private String completedDate = null;
	public String getCompletedDate() { return nes(this.completedDate); }
	public void setCompletedDate(String newCompletedDate) { this.completedDate = newCompletedDate; }

	private String lastUpdatedBy = null;
	public String getLastUpdatedBy() { return nes(this.lastUpdatedBy); }
	public void setLastUpdatedBy(String newLastUpdatedBy) { this.lastUpdatedBy = newLastUpdatedBy; }

	private String lastUpdatedDate = null;
	public String getLastUpdatedDate() { return nes(this.lastUpdatedDate); }
	public void setLastUpdatedDate(String newLastUpdatedDate) { this.lastUpdatedDate = newLastUpdatedDate; }

	private String category = null;
	public String getCategory() { return nes(this.category); }
	public void setCategory(String newCategory) { this.category = newCategory; }

	private String currCategory = null;
	public String getCurrCategory() { return nes(this.currCategory); }
	public void setCurrCategory(String newCurrCategory) { this.currCategory = newCurrCategory; }

	private String projectPk = null;
	public String getProjectPk() { return nes(this.projectPk); }
	public void setProjectPk(String newProjectPk) { this.projectPk = newProjectPk; }

	private String projectName = null;
	public String getProjectName() { return nes(this.projectName); }
	public String getProjectNameJs() { return js(this.projectName); }
	public void setProjectName(String newProjectName) { this.projectName = newProjectName; }

	private String customer = null;
	public String getCustomer() { return nes(this.customer); }
	public void setCustomer(String newCustomer) { this.customer = newCustomer; }

	private String currentTaskCnt = null;
	public String getCurrentTaskCnt() { return nes(this.currentTaskCnt); }
	public void setCurrentTaskCnt(String newCurrentTaskCnt) { this.currentTaskCnt = newCurrentTaskCnt; }

	private String completedTaskCnt = null;
	public String getCompletedTaskCnt() { return nes(this.completedTaskCnt); }
	public void setCompletedTaskCnt(String newCompletedTaskCnt) { this.completedTaskCnt = newCompletedTaskCnt; }

	private String subTaskId = null;
	public String getSubTaskId() { return nes(this.subTaskId); }
	public void setSubTaskId(String subTaskId) { this.subTaskId = subTaskId; }

	private ArrayList<ProjectBean> taskList = null;
	public ArrayList<ProjectBean> getTaskList() { return this.taskList == null ? new ArrayList<ProjectBean>() : this.taskList; }
	public void setTaskList(ArrayList<ProjectBean> newTaskList) { this.taskList = newTaskList; }

	private ArrayList<ProjectBean> subTaskList = null;
	public ArrayList<ProjectBean> getSubTaskList() { return this.subTaskList == null ? new ArrayList<ProjectBean>() : this.subTaskList; }
	public void setSubTaskList(ArrayList<ProjectBean> newSubTaskList) { this.subTaskList = newSubTaskList; }

	private String staffMeetingInd = null;
	public String getStaffMeetingInd() { return nes(this.staffMeetingInd); }
	public void setStaffMeetingInd(String newStaffMeetingInd) { this.staffMeetingInd = newStaffMeetingInd; }

	private String clientMeetingInd = null;
	public String getClientMeetingInd() { return nes(this.clientMeetingInd); }
	public void setClientMeetingInd(String newClientMeetingInd) { this.clientMeetingInd = newClientMeetingInd; }

	private String subTasks = null;
	public String getSubTasks() { return nes(this.subTasks); }
	public List<String> getSubTasksBr() { return java.util.Arrays.asList(nes(this.subTasks).split("\n", -1)); }
	public void setSubTasks(String newSubTasks) { this.subTasks = newSubTasks; }

	private String[] dueDateArr = null;
	public String[] getDueDateArr() { return this.dueDateArr == null ? new String[0] : this.dueDateArr; }
	public void setDueDateArr(String[] newDueDateArr) { this.dueDateArr = newDueDateArr; }

	private String[] subTaskIdArr = null;
	public String[] getSubTaskIdArr() { return this.subTaskIdArr == null ? new String[0] : this.subTaskIdArr; }
	public void setSubTaskIdArr(String[] subTaskIdArr) { this.subTaskIdArr = subTaskIdArr; }

	private String[] descriptionArr = null;
	public String[] getDescriptionArr() { return this.descriptionArr == null ? new String[0] : this.descriptionArr; }
	public void setDescriptionArr(String[] newDescriptionArr) { this.descriptionArr = newDescriptionArr; }

	private String[] personAssignedArr = null;
	public String[] getPersonAssignedArr() { return this.personAssignedArr == null ? new String[0] : this.personAssignedArr; }
	public void setPersonAssignedArr(String[] newPersonAssignedArr) { this.personAssignedArr = newPersonAssignedArr; }

	private String[] origPersonAssignedArr = null;
	public String[] getOrigPersonAssignedArr() { return this.origPersonAssignedArr == null ? new String[0] : this.origPersonAssignedArr; }
	public void setOrigPersonAssignedArr(String[] origPersonAssignedArr) { this.origPersonAssignedArr = origPersonAssignedArr; }

	private String[] statusArr = null;
	public String[] getStatusArr() { return this.statusArr == null ? new String[0] : this.statusArr; }
	public void setStatusArr(String[] newStatusArr) { this.statusArr = newStatusArr; }

	private String[] completedDateArr = null;
	public String[] getCompletedDateArr() { return this.completedDateArr == null ? new String[0] : this.completedDateArr; }
	public void setCompletedDateArr(String[] newCompletedDateArr) { this.completedDateArr = newCompletedDateArr; }

	private String searchTitleDescription = null;
	public String getSearchTitleDescription() { return nes(this.searchTitleDescription); }
	public void setSearchTitleDescription(String newSearchTitleDescription) { this.searchTitleDescription = newSearchTitleDescription; }

	private String[] searchMeetingArr = null;
	public String[] getSearchMeetingArr() { return this.searchMeetingArr == null ? new String[0] : this.searchMeetingArr; }
	public void setSearchMeetingArr(String[] newSearchMeetingArr) { this.searchMeetingArr = newSearchMeetingArr; }

	private String[] searchStatusArr = null;
	public String[] getSearchStatusArr() { return this.searchStatusArr == null ? new String[0] : this.searchStatusArr; }
	public void setSearchStatusArr(String[] newSearchStatusArr) { this.searchStatusArr = newSearchStatusArr; }

	private ArrayList<FormFile> fileList = null;
	public ArrayList<FormFile> getFileList() { return this.fileList; }
	public void setFileList(ArrayList<FormFile> newFileList) { this.fileList = newFileList; }

	private ArrayList<FileBean> taskFileList = null;
	public ArrayList<FileBean> getTaskFileList() { return this.taskFileList == null ? new ArrayList<FileBean>() : this.taskFileList; }
	public void setTaskFileList(ArrayList<FileBean> newTaskFileList) { this.taskFileList = newTaskFileList; }

	private String[] deleteFilePkArr = null;
	public String[] getDeleteFilePkArr() { return this.deleteFilePkArr == null ? new String[0] : this.deleteFilePkArr; }
	public void setDeleteFilePkArr(String[] newDeleteFilePkArr) { this.deleteFilePkArr = newDeleteFilePkArr; }

	private String searchSubTask = null;
	public String getSearchSubTask() { return nes(this.searchSubTask); }
	public void setSearchSubTask(String newSearchSubTask) { this.searchSubTask = newSearchSubTask; }

	private String quarterYear = null;
	public String getQuarterYear() { return this.quarterYear; }
	public void setQuarterYear(String quarterYear) { this.quarterYear = quarterYear; }

	private String effortArea = null;
	public String getEffortArea() { return this.effortArea; }
	public void setEffortArea(String effortArea) { this.effortArea = effortArea; }

	private String[] effortAreaArr = null;
	public String[] getEffortAreaArr() { return this.effortAreaArr == null ? new String[0] : this.effortAreaArr; }
	public void setEffortAreaArr(String[] effortAreaArr) { this.effortAreaArr = effortAreaArr; }

	private String effortType = null;
	public String getEffortType() { return this.effortType; }
	public void setEffortType(String effortType) { this.effortType = effortType; }

	private String loe = null;
	public String getLoe() { return this.loe; }
	public void setLoe(String loe) { this.loe = loe; }

	private String docNotes = null;
	public String getDocNotes() { return this.docNotes; }
	public void setDocNotes(String docNotes) { this.docNotes = docNotes; }

	private String versionIncluded = null;
	public String getVersionIncluded() { return this.versionIncluded; }
	public List<String> getVersionIncludedBr() { return java.util.Arrays.asList(nes(this.versionIncluded).split("\n", -1)); }
	public void setVersionIncluded(String versionIncluded) { this.versionIncluded = versionIncluded; }

	private String isClientApproved = null;
	public String getIsClientApproved() { return this.isClientApproved; }
	public void setIsClientApproved(String isClientApproved) { this.isClientApproved = isClientApproved; }

	private String isPshiApproved = null;
	public String getIsPshiApproved() { return this.isPshiApproved; }
	public void setIsPshiApproved(String isPshiApproved) { this.isPshiApproved = isPshiApproved; }

	private String recommendation = null;
	public String getRecommendation() { return this.recommendation; }
	public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

	private String clientPriority = null;
	public String getClientPriority() { return this.clientPriority; }
	public void setClientPriority(String clientPriority) { this.clientPriority = clientPriority; }

	private String currContractNumber = null;
	public String getCurrContractNumber() { return this.currContractNumber; }
	public void setCurrContractNumber(String currContractNumber) { this.currContractNumber = currContractNumber; }

	private String contractNumber = null;
	public String getContractNumber() { return this.contractNumber; }
	public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

	private String docUpdatedInd = null;
	public String getDocUpdatedInd() { return this.docUpdatedInd; }
	public void setDocUpdatedInd(String docUpdatedInd) { this.docUpdatedInd = docUpdatedInd; }

	private String deployedDate = null;
	public String getDeployedDate() { return this.deployedDate; }
	public void setDeployedDate(String deployedDate) { this.deployedDate = deployedDate; }
} // end of class
