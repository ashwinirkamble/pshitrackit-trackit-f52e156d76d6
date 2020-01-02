package com.premiersolutionshi.support.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.dao.ConfiguredSystemDao;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.ConfiguredSystemWithIssues;
import com.premiersolutionshi.support.domain.FkAndCount;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.domain.TransmittalComputerWithNumList;
import com.premiersolutionshi.support.domain.TransmittalSummary;

@Component("configuredSystemService")
public class ConfiguredSystemService extends BaseService<ConfiguredSystem> {
    private static final String ISSUE_CATEGORY_RSUPPLY_UPGRADE = "RSupply Upgrade";
    private Logger logger;

    public ConfiguredSystemService(SqlSession sqlSession) {
        super(sqlSession, ConfiguredSystemDao.class);
        setLogger(Logger.getLogger(this.getClass().getSimpleName()));
    }

    @Override
    protected ConfiguredSystemDao getDao() {
        return (ConfiguredSystemDao) super.getDao();
    }

    public ArrayList<ConfiguredSystem> getAllNotRetiredWithValidShip() {
        ArrayList<ConfiguredSystem> allNotRetired = getAllNotRetired();
        ArrayList<ConfiguredSystem> withValidShipList = new ArrayList<ConfiguredSystem>();
        for (ConfiguredSystem configuredSystem : allNotRetired) {
            if (configuredSystem.getShip() != null) {
                withValidShipList.add(configuredSystem);
            }
        }
        return withValidShipList;
    }

    public ArrayList<ConfiguredSystem> getAllNotRetired() {
        try {
            return getDao().getAllNotRetired();
        }
        catch (Exception e) {
            logError("Unable to getAllNotRetired.", e);
        }
        return new ArrayList<>();
    }

    public ArrayList<ConfiguredSystem> getAllNotRetiredByPkList(List<Integer> pkList) {
        try {
            return getDao().getAllNotRetiredByPkList(pkList);
        }
        catch (Exception e) {
            logError("Unable to getAllNotRetired.", e);
        }
        return new ArrayList<>();
    }

    public ArrayList<ConfiguredSystem> getByPkList(List<String> configuredSystemPkList) {
        try {
            return getDao().getByIdList(configuredSystemPkList);
        }
        catch (Exception e) {
            logError("Unable to getByIdList.", e);
        }
        return new ArrayList<>();
    }

    public List<ConfiguredSystem> getByShipFk(Integer shipFk) {
        try {
            return getDao().getByShipFk(shipFk);
        }
        catch (Exception e) {
            logError("Unable to getByShipFk.", e);
        }
        return new ArrayList<>();
    }

    public ArrayList<ConfiguredSystemWithIssues> getWithIssuesByPkList(List<String> configuredSystemPkList, TransmittalService transmittalService,
            IssueService issueService, String currFacetVersion, String currOsVersion, PocService pocService) {
        ArrayList<ConfiguredSystem> csList = getByPkList(configuredSystemPkList);
        return applyWithIssues(transmittalService, issueService, currFacetVersion, currOsVersion, csList);
    }

    public ArrayList<ConfiguredSystemWithIssues> getAllNotRetiredWithIssues(TransmittalService transmittalService,
        IssueService issueService, String currFacetVersion, String currOsVersion, PocService pocService) {
        ArrayList<ConfiguredSystem> allNotRetired = getAllNotRetired();
        ArrayList<ConfiguredSystemWithIssues> csWithIssues = applyWithIssues(transmittalService, issueService, currFacetVersion, currOsVersion, allNotRetired);
        applyShipPocs(pocService, csWithIssues);
        return csWithIssues;
    }

    private void applyShipPocs(PocService pocService, ArrayList<ConfiguredSystemWithIssues> csWithIssues) {
        Map<Integer, ArrayList<Poc>> shipFkToPocListMap = getShipFkToPocListMap(pocService);
        for (ConfiguredSystemWithIssues cs : csWithIssues) {
            Ship ship = cs.getShip();
            if (ship != null) {
                ship.setPocList(shipFkToPocListMap.get(cs.getShipFk()));
            }
        }
    }

    public Map<Integer, ArrayList<Poc>> getShipFkToPocListMap(PocService pocService) {
        Map<Integer, ArrayList<Poc>> map = new HashMap<>();
        ArrayList<Poc> allPocs = pocService.getAll();
        for (Poc poc : allPocs) {
            Integer shipFk = poc.getShipFk();
            if (shipFk != null) {
                ArrayList<Poc> pocList = map.get(shipFk);
                if (pocList == null) {
                    pocList = new ArrayList<Poc>();
                    map.put(shipFk, pocList);
                }
                pocList.add(poc);
            }
        }
        return map;
    }

    private ArrayList<ConfiguredSystemWithIssues> applyWithIssues(TransmittalService transmittalService, IssueService issueService,
            String currFacetVersion, String currOsVersion, ArrayList<ConfiguredSystem> allNotRetired) {
        ArrayList<ConfiguredSystemWithIssues> withIssues = new ArrayList<>();
        Map<String, TransmittalSummary> compNameToTransmittalSummaryMap = transmittalService.getComputerNameToTransmittalSummaryMap();
        Map<String, TransmittalComputerWithNumList> compNameToTransmittalMissingCountMap = transmittalService.getTransmittalMissingCountMap();
        Map<Integer, ArrayList<String>> missingAtoMap = issueService.getCsPkToMissingAtoMap();
        Map<String, Integer> csFkRsupplyIndicatorMap = getConfiguredSystemFkRsupplyIndicatorMap();
        for (ConfiguredSystem configuredSystem : allNotRetired) {
            //for some reason, I'm getting results for configured systems with "ship_fk = 0"
            //don't process these configured systems.
            if (configuredSystem.getShip() != null) {
                ConfiguredSystemWithIssues csWithIssues = ConfiguredSystemWithIssues.copy(configuredSystem);
                csWithIssues.setCurrFacetVersion(currFacetVersion);
                csWithIssues.setCurrOsVersion(currOsVersion);

                Integer rsupplyUpgradeIssueCount = csFkRsupplyIndicatorMap.get(csWithIssues.getShipFk().toString());
                csWithIssues.setRsupplyUpgradeIssueCount(rsupplyUpgradeIssueCount == null ? 0 : rsupplyUpgradeIssueCount);

                String computerName = csWithIssues.getComputerName();
                if (!StringUtils.isEmpty(computerName)) {
                    TransmittalSummary transmittalSummary = compNameToTransmittalSummaryMap.get(computerName);
                    TransmittalComputerWithNumList transmittalComputerWithNumList = compNameToTransmittalMissingCountMap.get(computerName);
                    csWithIssues.setTransmittalSummary(transmittalSummary);
                    csWithIssues.setTransmittalComputerWithNumList(transmittalComputerWithNumList);
                }

                ArrayList<String> missingAtoStrList = missingAtoMap.get(csWithIssues.getId());
                csWithIssues.setMissingAtoStrList(missingAtoStrList);

                withIssues.add(csWithIssues);
            }
        }
        return withIssues;
    }

    private Map<String, Integer> getConfiguredSystemFkRsupplyIndicatorMap() {
        Map<String, Integer> csRsupplyUpgradeOpenIssueCountMap = new HashMap<>();
        ArrayList<FkAndCount> csFkOpenIssueCountByCategory = getConfiguredSystemFkOpenIssueCountByCategory(ISSUE_CATEGORY_RSUPPLY_UPGRADE);
        for (FkAndCount fkAndCount : csFkOpenIssueCountByCategory) {
            String shipFk = fkAndCount.getFk();
            Integer count = fkAndCount.getCount();
            if (count != null) {
                csRsupplyUpgradeOpenIssueCountMap.put(shipFk, count);
            }
        }
        return csRsupplyUpgradeOpenIssueCountMap;
    }

    private ArrayList<FkAndCount> getConfiguredSystemFkOpenIssueCountByCategory(String categoryName) {
        try {
            return getDao().getConfiguredSystemFkOpenIssueCountByCategory(categoryName);
        }
        catch (Exception e) {
            logError("Unable to getConfiguredSystemFkOpenIssueCountByCategory.", e);
        }
        return new ArrayList<>();
    }

    @Override
    protected Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(this.getClass());
        }
        return logger;
    }

    public boolean updateFacetVersionById(Integer id) {
        try {
            if (getDao().updateFacetVersionById(id) == 1) {
                logInfo("Successfully updated FACET Version to Configured System ID: " + id);
            }
            else {
                logInfo("Attempted to update FACET Version to Configured System ID: " + id);
            }
            return true;
        }
        catch (Exception e) {
            logError("Could not updateConfiguredSystemFacet.", e);
        }
        return false;
    }

    
}
