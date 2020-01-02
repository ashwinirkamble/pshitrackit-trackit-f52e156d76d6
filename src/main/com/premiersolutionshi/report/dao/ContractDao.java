package com.premiersolutionshi.report.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.report.domain.Contract;

public interface ContractDao extends BaseDao<Contract> {

    /**
     * Gets Contracts by Status Report FK
     * @param statusReportFk
     * @return List of Contracts.
     */
    ArrayList<Contract> getByStatusReportFk(Integer statusReportFk);
}
