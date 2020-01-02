package com.premiersoluitionshi.report.service;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.report.constant.StatusReportType;
import com.premiersolutionshi.report.domain.StatusReport;
import com.premiersolutionshi.report.service.ContractService;
import com.premiersolutionshi.report.service.StatusReportService;

public class StatusReportServiceTest extends BaseServiceTest<StatusReport> {
    public StatusReportServiceTest() {
        super(StatusReportServiceTest.class);
    }

    private ContractService contractService;
    private StatusReportService statusReportService;

    public static void main(String[] args) {
        StatusReportServiceTest test = new StatusReportServiceTest();
        test.runBaseTests();
    }

    @Override
    public StatusReport createInstance() {
        StatusReport statusReport = new StatusReport();
        applyRandomData(statusReport);
        return statusReport;
    }

    @Override
    public StatusReportService getService() {
        return statusReportService;
    }

    @Override
    protected void init() {
        contractService = new ContractService(getSqlSession(), getUserService());
        statusReportService = new StatusReportService(getSqlSession(), getUserService(), contractService);
    }

    @Override
    public boolean compare(StatusReport domain1, StatusReport domain2) {
        if (domain1 == null || domain2 == null) {
            System.err.println("Cannot compare with null values.");
            System.exit(0);
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getProjectName(), domain2.getProjectName(), "Project Name");
        testEquals(domain1.getStatusReportTypeCode(), domain2.getStatusReportTypeCode(), "Status Report Type");
        testEquals(domain1.getContractFk(), domain2.getContractFk(), "Contract");
        testEquals(domain1.getReportStartDate(), domain2.getReportStartDate(), "Report Start Date");
        testEquals(domain1.getReportEndDate(), domain2.getReportEndDate(), "Report End Date");
        testEquals(domain1.getOrganizationFk(), domain2.getOrganizationFk(), "Organization");
        testEquals(domain1.getContractorOrgFk(), domain2.getContractorOrgFk(), "Contractor");
        testEquals(domain1.getName(), domain2.getName(), "Name");
        testEquals(domain1.getProjectManager(), domain2.getProjectManager(), "Project Manager");
        testEquals(domain1.getContractingOfficerCotr(), domain2.getContractingOfficerCotr(), "COTR");
        testEquals(domain1.getContractingOfficerCor(), domain2.getContractingOfficerCor(), "COR");
        testEquals(domain1.getObjective(), domain2.getObjective(), "Objective");
        testEquals(domain1.getSummary(), domain2.getSummary(), "Summary");
        testEquals(domain1.getLastUpdatedByFk(), domain2.getLastUpdatedByFk(), "Last Updated By");
        testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "Last Updated Date");
        return true;
    }

    @Override
    public void applyRandomData(StatusReport domain) {
        domain.setProjectName(TestData.genRandString());
        domain.setStatusReportTypeCode(TestData.genRandInt(StatusReportType.values().length - 1));
        domain.setContractFk(TestData.genRandInt());
        domain.setReportStartDate(TestData.genRandDate());
        domain.setReportEndDate(TestData.genRandDate());
        domain.setOrganizationFk(TestData.genRandInt());
        domain.setContractorOrgFk(TestData.genRandInt());
        domain.setName(TestData.genRandString());
        domain.setProjectManager(TestData.genRandString());
        domain.setContractingOfficerCotr(TestData.genRandString());
        domain.setContractingOfficerCor(TestData.genRandString());
        domain.setObjective(TestData.genRandText());
        domain.setSummary(TestData.genRandText());
        domain.setCreatedByFk(TestData.genRandInt());
        domain.setCreatedDate(TestData.genRandDateTime());
        domain.setLastUpdatedByFk(TestData.genRandInt());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}




