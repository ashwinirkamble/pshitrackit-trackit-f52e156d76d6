package com.premiersolutionshi.report.ui.form;

import java.util.ArrayList;

import com.premiersolutionshi.report.domain.Contract;
import com.premiersolutionshi.report.domain.StatusReport;

public class StatusReportForm extends StatusReport {
    private static final long serialVersionUID = -4899001503506834829L;

    private String[] contractFkArr;

    public String[] getContractFkArr() {
        return contractFkArr;
    }

    public void copy(StatusReport domain) {
        if (domain == null) {
            return;
        }
        super.copy(domain);
        setProjectName(domain.getProjectName());
        setStatusReportTypeCode(domain.getStatusReportTypeCode());
        setContractFk(domain.getContractFk());
        setReportStartDate(domain.getReportStartDate());
        setReportEndDate(domain.getReportEndDate());
        setOrganizationFk(domain.getOrganizationFk());
        setContractorOrgFk(domain.getContractorOrgFk());
        setName(domain.getName());
        setProjectManager(domain.getProjectManager());
        setContractingOfficerCotr(domain.getContractingOfficerCotr());
        setContractingOfficerCor(domain.getContractingOfficerCor());
        setObjective(domain.getObjective());
        setSummary(domain.getSummary());

        ArrayList<Contract> contractList = domain.getContractList();
        if (contractList != null && !contractList.isEmpty()) {
            contractFkArr = new String[contractList.size()];
            int index = 0;
            for (Contract contract : contractList) {
                contractFkArr[index++] = contract.getId() + "";
            }
        }
    }

    public void setContractFkArr(String[] contractFkArr) {
        this.contractFkArr = contractFkArr;
    }
}
