package com.premiersolutionshi.report.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.report.dao.ContractDao;
import com.premiersolutionshi.report.domain.Contract;

public class ContractService extends ModifiedService<Contract> {
    public ContractService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, ContractDao.class, userService);
    }

    @Override
    protected ContractDao getDao() {
        return (ContractDao) super.getDao();
    }

    @Override
    protected void beforeSave(Contract domain) {
        if (domain == null) {
            return;
        }
        super.beforeSave(domain);
        //sanitize data with "escapeHtml", "removeXss", or "sanitizeUrl"
        domain.setContractNumber(StringUtils.removeXss(domain.getContractNumber()));
        domain.setName(StringUtils.removeXss(domain.getName()));
    }

    public ArrayList<Contract> getByStatusReportFk(Integer statusReportFk) {
        try {
            return getDao().getByStatusReportFk(statusReportFk);
        }
        catch (Exception e) {
            logError("Could not getByStatusReportFk.", e);
        }
        return null;
    }
}
