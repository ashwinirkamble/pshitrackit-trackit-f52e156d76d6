package com.premiersolutionshi.report.dao;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.report.domain.StatusReport;

public interface StatusReportDao extends BaseDao<StatusReport> {

    int insertContractJoin(@Param("statusReportFk") int statusReportFk, @Param("contractFk") int contractFk);

    int deleteContractJoin(@Param("statusReportFk") int statusReportFk, @Param("contractFk") int contractFk);
}