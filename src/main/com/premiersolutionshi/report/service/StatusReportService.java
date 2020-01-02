package com.premiersolutionshi.report.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.DomainUtil;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.report.dao.StatusReportDao;
import com.premiersolutionshi.report.domain.Contract;
import com.premiersolutionshi.report.domain.StatusReport;
import com.premiersolutionshi.report.ui.form.StatusReportForm;

public class StatusReportService extends ModifiedService<StatusReport> {
    public StatusReportService(SqlSession sqlSession, UserService userService, ContractService contractService) {
        super(sqlSession, StatusReportDao.class, userService);
        this.contractService = contractService;
    }

    private ContractService contractService;

    public boolean saveForm(StatusReportForm domain) {
        if (domain == null) {
            logError("Cannot saveForm when domain IS NULL.");
            return false;
        }
        Integer id = domain.getId();
        ArrayList<Contract> prevContractList = (id != null && id > 0) ? contractService.getByStatusReportFk(id) : null;
        if (save(domain)) {
            Integer statusReportFk = domain.getId();
            //new contract list
            String[] contractFkArr = domain.getContractFkArr();
            for (String contractFkStr : contractFkArr) {
                Integer contractFk = StringUtils.parseInt(contractFkStr);
                if (contractFk != null) {
                    //if checkbox contractFk is not found on the old list, insert join
                    Contract contract = DomainUtil.findOnListById(prevContractList, contractFk);
                    if (contract == null) {
                        insertContractJoin(statusReportFk, contractFk);
                    }
                }
            }
            for (Contract contract : prevContractList) {
                boolean found = false;
                Integer prevListContractId = contract.getId();
                for (String contractFkStr : contractFkArr) {
                    Integer contractFk = StringUtils.parseInt(contractFkStr);
                    if (prevListContractId == contractFk) {
                        found = true;
                    }
                }
                //if old contract item is not found on the list, delete join
                if (!found) {
                    deleteContractJoin(statusReportFk, prevListContractId);
                }
            }
            return true;
        }
        return false;
    }

    public boolean insertContractJoin(Integer statusReportFk, Integer contractFk) {
        String parameters = "statusReportFk=" + statusReportFk + ", contractFk=" + contractFk;
        if (statusReportFk == null || contractFk == null) {
            logError("insertContractJoin | Neither " + parameters + " can be null.");
            return false;
        }
        try {
            boolean success = getDao().insertContractJoin(statusReportFk, contractFk) == 1;
            if (success) {
                logInfo("Successfully insertContractJoin. " + parameters);
            }
            else {
                logError("Failed to insertContractJoin. " + parameters);
            }
            return success;
        }
        catch (Exception e) {
            logError("Failed to insertContractJoin. " + parameters, e);
        }
        return false;
    }

    public boolean deleteContractJoin(Integer statusReportFk, Integer contractFk) {
        String parameters = "statusReportFk=" + statusReportFk + ", contractFk=" + contractFk;
        if (statusReportFk == null || contractFk == null) {
            logError("deleteContractJoin | Neither " + parameters + " can be null.");
            return false;
        }
        try {
            boolean success = getDao().deleteContractJoin(statusReportFk, contractFk) == 1;
            if (success) {
                logInfo("Successfully deleteContractJoin. " + parameters);
            }
            else {
                logError("Failed to deleteContractJoin. " + parameters);
            }
        }
        catch (Exception e) {
            logError("Failed to deleteContractJoin. " + parameters, e);
        }
        return false;
    }

    @Override
    protected StatusReportDao getDao() {
        return (StatusReportDao) super.getDao();
    }

    @Override
    protected void beforeSave(StatusReport domain) {
        if (domain == null) {
            return;
        }
        super.beforeSave(domain);
        //sanitize data with "escapeHtml", "removeXss", or "sanitizeUrl"
        domain.setProjectName(StringUtils.removeXss(domain.getProjectName()));
        domain.setName(StringUtils.removeXss(domain.getName()));
        domain.setProjectManager(StringUtils.removeXss(domain.getProjectManager()));
        domain.setContractingOfficerCotr(StringUtils.removeXss(domain.getContractingOfficerCotr()));
        domain.setContractingOfficerCor(StringUtils.removeXss(domain.getContractingOfficerCor()));
        domain.setObjective(StringUtils.removeXss(domain.getObjective()));
        domain.setSummary(StringUtils.sanitizeHtml(domain.getSummary()));
    }
}

