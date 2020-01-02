package com.premiersolutionshi.report.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.report.constant.StatusReportType;

public class StatusReport extends ModifiedDomain {
    private static final long serialVersionUID = 1609770372563490354L;
    private StatusReportType statusReportType;
    private String projectName;
    private Integer contractFk;
    private LocalDate reportStartDate;
    private LocalDate reportEndDate;
    private Integer organizationFk;
    private Integer contractorOrgFk;
    private String name;
    private String projectManager;
    private String contractingOfficerCotr;
    private String contractingOfficerCor;
    private String objective;
    private String summary;
    private Contract contract;
    private Organization organization;
    private Organization contractorOrg;
    private ArrayList<Contract> contractList;

    public StatusReportType getStatusReportType() {
        return statusReportType;
    }

    public void setStatusReportType(StatusReportType statusReportType) {
        this.statusReportType = statusReportType;
    }

    public Integer getStatusReportTypeCode() {
        return statusReportType == null ? null : statusReportType.getCode();
    }

    public void setStatusReportTypeCode(Integer code) {
        setStatusReportType(code == null ? null : StatusReportType.getByCode(code));
    }

    public String getReportStartDateSql() {
        return DateUtils.formatToSqliteDate(reportStartDate);
    }

    public String getReportStartDateStr() {
        return reportStartDate == null ? null : DateUtils.COMMON_BASIC_FORMAT.format(reportStartDate);
    }

    public void setReportStartDateStr(String reportStartDateStr) {
        if (StringUtils.isEmpty(reportStartDateStr)) {
            return;
        }
        setReportStartDate(DateUtils.parseBasicDate(reportStartDateStr));
    }
    public String getReportEndDateSql() {
        return DateUtils.formatToSqliteDate(reportEndDate);
    }

    public String getReportEndDateStr() {
        return reportEndDate == null ? null : DateUtils.COMMON_BASIC_FORMAT.format(reportEndDate);
    }

    public void setReportEndDateStr(String reportEndDateStr) {
        if (StringUtils.isEmpty(reportEndDateStr)) {
            return;
        }
        setReportEndDate(DateUtils.parseBasicDate(reportEndDateStr));
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getContractFk() {
        return contractFk;
    }

    public void setContractFk(Integer contractFk) {
        this.contractFk = contractFk;
    }

    public LocalDate getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(LocalDate reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public LocalDate getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(LocalDate reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public Integer getOrganizationFk() {
        return organizationFk;
    }

    public void setOrganizationFk(Integer organizationFk) {
        this.organizationFk = organizationFk;
    }

    public Integer getContractorOrgFk() {
        return contractorOrgFk;
    }

    public void setContractorOrgFk(Integer contractorOrgFk) {
        this.contractorOrgFk = contractorOrgFk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getContractingOfficerCotr() {
        return contractingOfficerCotr;
    }

    public void setContractingOfficerCotr(String contractingOfficerCotr) {
        this.contractingOfficerCotr = contractingOfficerCotr;
    }

    public String getContractingOfficerCor() {
        return contractingOfficerCor;
    }

    public void setContractingOfficerCor(String contractingOfficerCor) {
        this.contractingOfficerCor = contractingOfficerCor;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Organization getContractorOrg() {
        return contractorOrg;
    }

    public void setContractorOrg(Organization contractorOrg) {
        this.contractorOrg = contractorOrg;
    }

    public ArrayList<Contract> getContractList() {
        if (contractList == null) {
            contractList = new ArrayList<>();
        }
        return contractList;
    }

    public void setContractList(ArrayList<Contract> contractList) {
        this.contractList = contractList;
    }
}

