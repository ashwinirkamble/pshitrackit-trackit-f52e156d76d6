package com.premiersolutionshi.support.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.support.domain.TransmittalComputerWithNumList;
import com.premiersolutionshi.support.domain.TransmittalSummary;

public interface TransmittalDao extends BaseDao<TransmittalSummary> {

    /**
     * Gets all Transmittal Summaries for all Computer Names.
     * @return List of Transmittal Summaries.
     */
    ArrayList<TransmittalSummary> getTransmittalSummaryList();
    
    /**
     * This gets a list of Computer Names with the Transmittal Numbers, including the ones in the
     * "transmittal_exception" table.
     * 
     * This list will be used to determine which transmittals are missing.
     * 
     * Recreated from {ReportModel.getMissingTransmittalMap()} which also has logic to build the map.
     */
    ArrayList<TransmittalComputerWithNumList> getTransmittalComputerWithNumList();
}
