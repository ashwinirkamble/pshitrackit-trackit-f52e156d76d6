package com.premiersolutionshi.old.bean;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a BACKFILE form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class BackfileBean extends ActionForm {
	private String backfileWorkflowPk = null;
	public String getBackfileWorkflowPk() { return this.backfileWorkflowPk; }
	public void setBackfileWorkflowPk(String backfileWorkflowPk) { this.backfileWorkflowPk = backfileWorkflowPk; }

	private String shipPk = null;
	public String getShipPk() { return this.shipPk; }
	public void setShipPk(String shipPk) { this.shipPk = shipPk; }

	private String shipName = null;
	public String getShipName() { return this.shipName; }
	public void setShipName(String shipName) { this.shipName = shipName; }

	private String type = null;
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }

	private String hull = null;
	public String getHull() { return this.hull; }
	public void setHull(String hull) { this.hull = hull; }

	private String homeport = null;
	public String getHomeport() { return this.homeport; }
	public void setHomeport(String homeport) { this.homeport = homeport; }

	private String tycomDisplay = null;
	public String getTycomDisplay() { return this.tycomDisplay; }
	public void setTycomDisplay(String tycomDisplay) { this.tycomDisplay = tycomDisplay; }

	private String computerName = null;
	public String getComputerName() { return this.computerName; }
	public void setComputerName(String computerName) { this.computerName = computerName; }

//Boxes sent to scanning
	private String fy16BoxCnt = null;
	public String getFy16BoxCnt() { return this.fy16BoxCnt; }
	public void setFy16BoxCnt(String fy16BoxCnt) { this.fy16BoxCnt = fy16BoxCnt; }

	private String fy15BoxCnt = null;
	public String getFy15BoxCnt() { return this.fy15BoxCnt; }
	public void setFy15BoxCnt(String fy15BoxCnt) { this.fy15BoxCnt = fy15BoxCnt; }

	private String fy14BoxCnt = null;
	public String getFy14BoxCnt() { return this.fy14BoxCnt; }
	public void setFy14BoxCnt(String fy14BoxCnt) { this.fy14BoxCnt = fy14BoxCnt; }

	private String fy13BoxCnt = null;
	public String getFy13BoxCnt() { return this.fy13BoxCnt; }
	public void setFy13BoxCnt(String fy13BoxCnt) { this.fy13BoxCnt = fy13BoxCnt; }

	private String fy12BoxCnt = null;
	public String getFy12BoxCnt() { return this.fy12BoxCnt; }
	public void setFy12BoxCnt(String fy12BoxCnt) { this.fy12BoxCnt = fy12BoxCnt; }

	private String fy11BoxCnt = null;
	public String getFy11BoxCnt() { return this.fy11BoxCnt; }
	public void setFy11BoxCnt(String fy11BoxCnt) { this.fy11BoxCnt = fy11BoxCnt; }

	private String fy10BoxCnt = null;
	public String getFy10BoxCnt() { return this.fy10BoxCnt; }
	public void setFy10BoxCnt(String fy10BoxCnt) { this.fy10BoxCnt = fy10BoxCnt; }

	private String otherBoxCnt = null;
	public String getOtherBoxCnt() { return this.otherBoxCnt; }
	public void setOtherBoxCnt(String otherBoxCnt) { this.otherBoxCnt = otherBoxCnt; }

	private String totalBoxCnt = null;
	public String getTotalBoxCnt() { return this.totalBoxCnt; }
	public void setTotalBoxCnt(String totalBoxCnt) { this.totalBoxCnt = totalBoxCnt; }

//PSHI received boxes
	private String fy16PshiBoxCnt = null;
	public String getFy16PshiBoxCnt() { return this.fy16PshiBoxCnt; }
	public void setFy16PshiBoxCnt(String fy16PshiBoxCnt) { this.fy16PshiBoxCnt = fy16PshiBoxCnt; }

	private String fy15PshiBoxCnt = null;
	public String getFy15PshiBoxCnt() { return this.fy15PshiBoxCnt; }
	public void setFy15PshiBoxCnt(String fy15PshiBoxCnt) { this.fy15PshiBoxCnt = fy15PshiBoxCnt; }

	private String fy14PshiBoxCnt = null;
	public String getFy14PshiBoxCnt() { return this.fy14PshiBoxCnt; }
	public void setFy14PshiBoxCnt(String fy14PshiBoxCnt) { this.fy14PshiBoxCnt = fy14PshiBoxCnt; }

	private String fy13PshiBoxCnt = null;
	public String getFy13PshiBoxCnt() { return this.fy13PshiBoxCnt; }
	public void setFy13PshiBoxCnt(String fy13PshiBoxCnt) { this.fy13PshiBoxCnt = fy13PshiBoxCnt; }

	private String fy12PshiBoxCnt = null;
	public String getFy12PshiBoxCnt() { return this.fy12PshiBoxCnt; }
	public void setFy12PshiBoxCnt(String fy12PshiBoxCnt) { this.fy12PshiBoxCnt = fy12PshiBoxCnt; }

	private String fy11PshiBoxCnt = null;
	public String getFy11PshiBoxCnt() { return this.fy11PshiBoxCnt; }
	public void setFy11PshiBoxCnt(String fy11PshiBoxCnt) { this.fy11PshiBoxCnt = fy11PshiBoxCnt; }

	private String fy10PshiBoxCnt = null;
	public String getFy10PshiBoxCnt() { return this.fy10PshiBoxCnt; }
	public void setFy10PshiBoxCnt(String fy10PshiBoxCnt) { this.fy10PshiBoxCnt = fy10PshiBoxCnt; }

	private String otherPshiBoxCnt = null;
	public String getOtherPshiBoxCnt() { return this.otherPshiBoxCnt; }
	public void setOtherPshiBoxCnt(String otherPshiBoxCnt) { this.otherPshiBoxCnt = otherPshiBoxCnt; }

	private String totalPshiBoxCnt = null;
	public String getTotalPshiBoxCnt() { return this.totalPshiBoxCnt; }
	public void setTotalPshiBoxCnt(String totalPshiBoxCnt) { this.totalPshiBoxCnt = totalPshiBoxCnt; }

	private String schedTrainingDate = null;
	public String getSchedTrainingDate() { return this.schedTrainingDate; }
	public void setSchedTrainingDate(String schedTrainingDate) { this.schedTrainingDate = schedTrainingDate; }

	private String requestedDate = null;
	public String getRequestedDate() { return this.requestedDate; }
	public void setRequestedDate(String requestedDate) { this.requestedDate = requestedDate; }

	private String receivedDate = null;
	public String getReceivedDate() { return this.receivedDate; }
	public void setReceivedDate(String receivedDate) { this.receivedDate = receivedDate; }

	private String scanningDeliveredDate = null;
	public String getScanningDeliveredDate() { return this.scanningDeliveredDate; }
	public void setScanningDeliveredDate(String scanningDeliveredDate) { this.scanningDeliveredDate = scanningDeliveredDate; }

	private String fy1314BurnedDate = null;
	public String getFy1314BurnedDate() { return this.fy1314BurnedDate; }
	public void setFy1314BurnedDate(String fy1314BurnedDate) { this.fy1314BurnedDate = fy1314BurnedDate; }

	private String fy1314MailedDate = null;
	public String getFy1314MailedDate() { return this.fy1314MailedDate; }
	public void setFy1314MailedDate(String fy1314MailedDate) { this.fy1314MailedDate = fy1314MailedDate; }

	private String fy1314CompletedDate = null;
	public String getFy1314CompletedDate() { return this.fy1314CompletedDate; }
	public void setFy1314CompletedDate(String fy1314CompletedDate) { this.fy1314CompletedDate = fy1314CompletedDate; }

	private String fy1112CompletedDate = null;
	public String getFy1112CompletedDate() { return this.fy1112CompletedDate; }
	public void setFy1112CompletedDate(String fy1112CompletedDate) { this.fy1112CompletedDate = fy1112CompletedDate; }

	private String extractDate = null;
	public String getExtractDate() { return this.extractDate; }
	public void setExtractDate(String extractDate) { this.extractDate = extractDate; }

	private String logcopDeliveredDate = null;
	public String getLogcopDeliveredDate() { return this.logcopDeliveredDate; }
	public void setLogcopDeliveredDate(String logcopDeliveredDate) { this.logcopDeliveredDate = logcopDeliveredDate; }

	private String logcopUploadedDate = null;
	public String getLogcopUploadedDate() { return this.logcopUploadedDate; }
	public void setLogcopUploadedDate(String logcopUploadedDate) { this.logcopUploadedDate = logcopUploadedDate; }

	private String laptopInstalledDate = null;
	public String getLaptopInstalledDate() { return this.laptopInstalledDate; }
	public void setLaptopInstalledDate(String laptopInstalledDate) { this.laptopInstalledDate = laptopInstalledDate; }

	private String finalReportDate = null;
	public String getFinalReportDate() { return this.finalReportDate; }
	public void setFinalReportDate(String finalReportDate) { this.finalReportDate = finalReportDate; }

	private String destructionDate = null;
	public String getDestructionDate() { return this.destructionDate; }
	public void setDestructionDate(String destructionDate) { this.destructionDate = destructionDate; }

	private String estCompletedDate = null;
	public String getEstCompletedDate() { return this.estCompletedDate; }
	public void setEstCompletedDate(String estCompletedDate) { this.estCompletedDate = estCompletedDate; }

	private String estFy1314CompletedDate = null;
	public String getEstFy1314CompletedDate() { return this.estFy1314CompletedDate; }
	public void setEstFy1314CompletedDate(String estFy1314CompletedDate) { this.estFy1314CompletedDate = estFy1314CompletedDate; }

	private String estFy1112CompletedDate = null;
	public String getEstFy1112CompletedDate() { return this.estFy1112CompletedDate; }
	public void setEstFy1112CompletedDate(String estFy1112CompletedDate) { this.estFy1112CompletedDate = estFy1112CompletedDate; }

	private String dueDate = null;
	public String getDueDate() { return this.dueDate; }
	public void setDueDate(String dueDate) { this.dueDate = dueDate; }

	private String dueDateCss = null;
	public String getDueDateCss() { return this.dueDateCss; }
	public void setDueDateCss(String dueDateCss) { this.dueDateCss = dueDateCss; }

	private String fy1314DueDate = null;
	public String getFy1314DueDate() { return this.fy1314DueDate; }
	public void setFy1314DueDate(String fy1314DueDate) { this.fy1314DueDate = fy1314DueDate; }

	private String fy1314DueDateCss = null;
	public String getFy1314DueDateCss() { return this.fy1314DueDateCss; }
	public void setFy1314DueDateCss(String fy1314DueDateCss) { this.fy1314DueDateCss = fy1314DueDateCss; }

	private String completedDate = null;
	public String getCompletedDate() { return this.completedDate; }
	public void setCompletedDate(String completedDate) { this.completedDate = completedDate; }

	private String comments = null;
	public String getComments() { return this.comments; }
	public List<String> getCommentsBr() { return java.util.Arrays.asList(CommonMethods.nes(this.comments).split("\n", -1)); }
	public void setComments(String comments) { this.comments = comments; }

	private String receivedDateBgColor = null;
	public String getReceivedDateBgColor() { return this.receivedDateBgColor; }
	public void setReceivedDateBgColor(String receivedDateBgColor) { this.receivedDateBgColor = receivedDateBgColor; }

	private String requestedDateCss = null;
	public String getRequestedDateCss() { return this.requestedDateCss; }
	public void setRequestedDateCss(String requestedDateCss) { this.requestedDateCss = requestedDateCss; }

	private String receivedDateCss = null;
	public String getReceivedDateCss() { return this.receivedDateCss; }
	public void setReceivedDateCss(String receivedDateCss) { this.receivedDateCss = receivedDateCss; }

	private String scanningDeliveredDateCss = null;
	public String getScanningDeliveredDateCss() { return this.scanningDeliveredDateCss; }
	public void setScanningDeliveredDateCss(String scanningDeliveredDateCss) { this.scanningDeliveredDateCss = scanningDeliveredDateCss; }

	private String fy1314BurnedDateCss = null;
	public String getFy1314BurnedDateCss() { return this.fy1314BurnedDateCss; }
	public void setFy1314BurnedDateCss(String fy1314BurnedDateCss) { this.fy1314BurnedDateCss = fy1314BurnedDateCss; }

	private String fy1314MailedDateCss = null;
	public String getFy1314MailedDateCss() { return this.fy1314MailedDateCss; }
	public void setFy1314MailedDateCss(String fy1314MailedDateCss) { this.fy1314MailedDateCss = fy1314MailedDateCss; }

	private String fy1314CompletedDateCss = null;
	public String getFy1314CompletedDateCss() { return this.fy1314CompletedDateCss; }
	public void setFy1314CompletedDateCss(String fy1314CompletedDateCss) { this.fy1314CompletedDateCss = fy1314CompletedDateCss; }

	private String fy1112CompletedDateCss = null;
	public String getFy1112CompletedDateCss() { return this.fy1112CompletedDateCss; }
	public void setFy1112CompletedDateCss(String fy1112CompletedDateCss) { this.fy1112CompletedDateCss = fy1112CompletedDateCss; }

	private String extractDateCss = null;
	public String getExtractDateCss() { return this.extractDateCss; }
	public void setExtractDateCss(String extractDateCss) { this.extractDateCss = extractDateCss; }

	private String logcopDeliveredDateCss = null;
	public String getLogcopDeliveredDateCss() { return this.logcopDeliveredDateCss; }
	public void setLogcopDeliveredDateCss(String logcopDeliveredDateCss) { this.logcopDeliveredDateCss = logcopDeliveredDateCss; }

	private String logcopUploadedDateCss = null;
	public String getLogcopUploadedDateCss() { return this.logcopUploadedDateCss; }
	public void setLogcopUploadedDateCss(String logcopUploadedDateCss) { this.logcopUploadedDateCss = logcopUploadedDateCss; }

	private String laptopInstalledDateCss = null;
	public String getLaptopInstalledDateCss() { return this.laptopInstalledDateCss; }
	public void setLaptopInstalledDateCss(String laptopInstalledDateCss) { this.laptopInstalledDateCss = laptopInstalledDateCss; }

	private String finalReportDateCss = null;
	public String getFinalReportDateCss() { return this.finalReportDateCss; }
	public void setFinalReportDateCss(String finalReportDateCss) { this.finalReportDateCss = finalReportDateCss; }

	private String destructionDateCss = null;
	public String getDestructionDateCss() { return this.destructionDateCss; }
	public void setDestructionDateCss(String destructionDateCss) { this.destructionDateCss = destructionDateCss; }

	private String contractNumber = null;
	public String getContractNumber() { return this.contractNumber; }
	public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

	private String returnedDate = null;
	public String getReturnedDate() { return this.returnedDate; }
	public void setReturnedDate(String returnedDate) { this.returnedDate = returnedDate; }

	private String returnedDateCss = null;
	public String getReturnedDateCss() { return this.returnedDateCss; }
	public void setReturnedDateCss(String returnedDateCss) { this.returnedDateCss = returnedDateCss; }

	private String returnConfirmedDate = null;
	public String getReturnConfirmedDate() { return this.returnConfirmedDate; }
	public void setReturnConfirmedDate(String returnConfirmedDate) { this.returnConfirmedDate = returnConfirmedDate; }

	private String returnConfirmedDateCss = null;
	public String getReturnConfirmedDateCss() { return this.returnConfirmedDateCss; }
	public void setReturnConfirmedDateCss(String returnConfirmedDateCss) { this.returnConfirmedDateCss = returnConfirmedDateCss; }

	private String returnInd = null;
	public String getReturnInd() { return this.returnInd; }
	public void setReturnInd(String returnInd) { this.returnInd = returnInd; }

	private String fy15CompletedDate = null;
	public String getFy15CompletedDate() { return this.fy15CompletedDate; }
	public void setFy15CompletedDate(String fy15CompletedDate) { this.fy15CompletedDate = fy15CompletedDate; }

	private String fy15CompletedDateCss = null;
	public String getFy15CompletedDateCss() { return this.fy15CompletedDateCss; }
	public void setFy15CompletedDateCss(String fy15CompletedDateCss) { this.fy15CompletedDateCss = fy15CompletedDateCss; }

	private String fy15MailedDate = null;
	public String getFy15MailedDate() { return this.fy15MailedDate; }
	public void setFy15MailedDate(String fy15MailedDate) { this.fy15MailedDate = fy15MailedDate; }

	private String fy15MailedDateCss = null;
	public String getFy15MailedDateCss() { return this.fy15MailedDateCss; }
	public void setFy15MailedDateCss(String fy15MailedDateCss) { this.fy15MailedDateCss = fy15MailedDateCss; }

	private String fy16CompletedDate = null;
	public String getFy16CompletedDate() { return this.fy16CompletedDate; }
	public void setFy16CompletedDate(String fy16CompletedDate) { this.fy16CompletedDate = fy16CompletedDate; }

	private String fy16CompletedDateCss = null;
	public String getFy16CompletedDateCss() { return this.fy16CompletedDateCss; }
	public void setFy16CompletedDateCss(String fy16CompletedDateCss) { this.fy16CompletedDateCss = fy16CompletedDateCss; }

	private String fy16MailedDate = null;
	public String getFy16MailedDate() { return this.fy16MailedDate; }
	public void setFy16MailedDate(String fy16MailedDate) { this.fy16MailedDate = fy16MailedDate; }

	private String fy16MailedDateCss = null;
	public String getFy16MailedDateCss() { return this.fy16MailedDateCss; }
	public void setFy16MailedDateCss(String fy16MailedDateCss) { this.fy16MailedDateCss = fy16MailedDateCss; }

	private String isRequired = null;
	public String getIsRequired() { return this.isRequired; }
	public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
} // end of class
