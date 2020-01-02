package com.premiersolutionshi.old.bean;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a TRAINING form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class TrainingBean extends ActionForm {
	private String trainingWorkflowPk = null;
	public String getTrainingWorkflowPk() { return this.trainingWorkflowPk; }
	public void setTrainingWorkflowPk(String trainingWorkflowPk) { this.trainingWorkflowPk = trainingWorkflowPk; }

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

	private String rsupply = null;
	public String getRsupply() { return this.rsupply; }
	public void setRsupply(String rsupply) { this.rsupply = rsupply; }

	private String laptopTag = null;
	public String getLaptopTag() { return this.laptopTag; }
	public void setLaptopTag(String laptopTag) { this.laptopTag = laptopTag; }

	private String scannerTag = null;
	public String getScannerTag() { return this.scannerTag; }
	public void setScannerTag(String scannerTag) { this.scannerTag = scannerTag; }

	private String backfileRecvDate = null;
	public String getBackfileRecvDate() { return this.backfileRecvDate; }
	public void setBackfileRecvDate(String backfileRecvDate) { this.backfileRecvDate = backfileRecvDate; }

	private String backfileCompletedDate = null;
	public String getBackfileCompletedDate() { return this.backfileCompletedDate; }
	public void setBackfileCompletedDate(String backfileCompletedDate) { this.backfileCompletedDate = backfileCompletedDate; }

	private String locFileRecvDate = null;
	public String getLocFileRecvDate() { return this.locFileRecvDate; }
	public void setLocFileRecvDate(String locFileRecvDate) { this.locFileRecvDate = locFileRecvDate; }

	private String locFileRevDate = null;
	public String getLocFileRevDate() { return this.locFileRevDate; }
	public void setLocFileRevDate(String locFileRevDate) { this.locFileRevDate = locFileRevDate; }

	private String pacfltFoodReportDate = null;
	public String getPacfltFoodReportDate() { return this.pacfltFoodReportDate; }
	public void setPacfltFoodReportDate(String pacfltFoodReportDate) { this.pacfltFoodReportDate = pacfltFoodReportDate; }

	private String systemReadyDate = null;
	public String getSystemReadyDate() { return this.systemReadyDate; }
	public void setSystemReadyDate(String systemReadyDate) { this.systemReadyDate = systemReadyDate; }

	private String computerNameDbDate = null;
	public String getComputerNameDbDate() { return this.computerNameDbDate; }
	public void setComputerNameDbDate(String computerNameDbDate) { this.computerNameDbDate = computerNameDbDate; }

	private String computerNameLogcopDate = null;
	public String getComputerNameLogcopDate() { return this.computerNameLogcopDate; }
	public void setComputerNameLogcopDate(String computerNameLogcopDate) { this.computerNameLogcopDate = computerNameLogcopDate; }

	private String trainingKitReadyDate = null;
	public String getTrainingKitReadyDate() { return this.trainingKitReadyDate; }
	public void setTrainingKitReadyDate(String trainingKitReadyDate) { this.trainingKitReadyDate = trainingKitReadyDate; }

	private String estTrainingMonth = null;
	public String getEstTrainingMonth() { return this.estTrainingMonth; }
	public void setEstTrainingMonth(String estTrainingMonth) { this.estTrainingMonth = estTrainingMonth; }

	private String schedTrainingTime = null;
	public String getSchedTrainingTime() { return this.schedTrainingTime; }
	public void setSchedTrainingTime(String schedTrainingTime) { this.schedTrainingTime = schedTrainingTime; }

	private String schedTrainingDate = null;
	public String getSchedTrainingDate() { return this.schedTrainingDate; }
	public void setSchedTrainingDate(String schedTrainingDate) { this.schedTrainingDate = schedTrainingDate; }

	private String schedTrainingDateCss = null;
	public String getSchedTrainingDateCss() { return this.schedTrainingDateCss; }
	public void setSchedTrainingDateCss(String schedTrainingDateCss) { this.schedTrainingDateCss = schedTrainingDateCss; }

	private String actualTrainingDate = null;
	public String getActualTrainingDate() { return this.actualTrainingDate; }
	public void setActualTrainingDate(String actualTrainingDate) { this.actualTrainingDate = actualTrainingDate; }

	private String comments = null;
	public String getComments() { return this.comments; }
	public List<String> getCommentsBr() { return java.util.Arrays.asList(CommonMethods.nes(this.comments).split("\n", -1)); }
	public void setComments(String comments) { this.comments = comments; }

	private String trainerUserFk = null;
	public String getTrainerUserFk() { return this.trainerUserFk; }
	public void setTrainerUserFk(String trainerUserFk) { this.trainerUserFk = trainerUserFk; }

	private String trainerFullName = null;
	public String getTrainerFullName() { return this.trainerFullName; }
	public void setTrainerFullName(String trainerFullName) { this.trainerFullName = trainerFullName; }

	private String trainer = null;
	public String getTrainer() { return this.trainer; }
	public void setTrainer(String trainer) { this.trainer = trainer; }

	private String schedTrainingLoc = null;
	public String getSchedTrainingLoc() { return this.schedTrainingLoc; }
	public void setSchedTrainingLoc(String schedTrainingLoc) { this.schedTrainingLoc = schedTrainingLoc; }

	private String clientConfirmedInd = null;
	public String getClientConfirmedInd() { return this.clientConfirmedInd; }
	public void setClientConfirmedInd(String clientConfirmedInd) { this.clientConfirmedInd = clientConfirmedInd; }

	private String contractNumber = null;
	public String getContractNumber() { return this.contractNumber; }
	public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
} // end of class
