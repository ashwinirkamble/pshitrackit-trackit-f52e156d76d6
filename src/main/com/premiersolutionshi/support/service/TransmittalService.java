package com.premiersolutionshi.support.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.support.dao.TransmittalDao;
import com.premiersolutionshi.support.domain.TransmittalComputerWithNumList;
import com.premiersolutionshi.support.domain.TransmittalSummary;

@Component("transmittalService")
public class TransmittalService {
    private Logger logger;
    private TransmittalDao transmittalDao;

    public TransmittalService(SqlSession sqlSession) {
        this.transmittalDao = (TransmittalDao) sqlSession.getMapper(TransmittalDao.class);
        setLogger(Logger.getLogger(this.getClass().getSimpleName()));
   }

    public Map<String, TransmittalComputerWithNumList> getTransmittalMissingCountMap() {
        Map<String, TransmittalComputerWithNumList> map = new HashMap<>();
        ArrayList<TransmittalComputerWithNumList> transmittalComputerWithNumList = getTransmittalComputerWithNumList();
        String computerName = null;
        for (TransmittalComputerWithNumList transCompWithNumList : transmittalComputerWithNumList) {
            map.put(computerName, transCompWithNumList);
        }
        return map;
    }

    public ArrayList<TransmittalComputerWithNumList> getTransmittalComputerWithNumList() {
        try {
            return getDao().getTransmittalComputerWithNumList();
        }
        catch(SqlSessionException e) {
            getLogger().error("Unable to getTransNumWithExceptions.");
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, TransmittalSummary> getComputerNameToTransmittalSummaryMap() {
        Map<String, TransmittalSummary> computerNameToTransSumMap = new HashMap<>();
        ArrayList<TransmittalSummary> transmittalSummaryList = getTransmittalSummaryList();
        for (TransmittalSummary transmittalSummary : transmittalSummaryList) {
            computerNameToTransSumMap.put(transmittalSummary.getComputerName(), transmittalSummary);
        }
        return computerNameToTransSumMap;
    }

    public ArrayList<TransmittalSummary> getTransmittalSummaryList() {
        try {
            return getDao().getTransmittalSummaryList();
        }
        catch(SqlSessionException e) {
            getLogger().error("Unable to getTransmittalSummaryList.");
            e.printStackTrace();
        }
        return null;
    }

    protected TransmittalDao getDao() {
        return transmittalDao;
    }
    protected Logger getLogger() {
        return logger;
    }
    protected void setLogger(Logger logger) {
        this.logger = logger;
    }
}
