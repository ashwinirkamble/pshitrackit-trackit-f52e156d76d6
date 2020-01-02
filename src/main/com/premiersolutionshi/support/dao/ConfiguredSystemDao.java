package com.premiersolutionshi.support.dao;

import java.util.ArrayList;
import java.util.List;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.FkAndCount;

public interface ConfiguredSystemDao extends BaseDao<ConfiguredSystem> {

    /**
     * Gets all Configured Systems that are not "- RETIRED".
     * @return List of Configured Systems
     */
    ArrayList<ConfiguredSystem> getAllNotRetired();

    /**
     * Gets not "RETIRED" by PK list.
     * @param pkList
     * @return List of Configured Systems.
     */
    ArrayList<ConfiguredSystem> getAllNotRetiredByPkList(List<Integer> pkList);

    /**
     * Gets by list of IDs.
     * @param configuredSystemPkList
     * @return
     */
    ArrayList<ConfiguredSystem> getByIdList(List<String> configuredSystemPkList);

    /**
     * Gets counts of all configured systems with their issue count of given category name.
     * @param categoryName
     * @return List of Configured System IDs and counts.
     */
    ArrayList<FkAndCount> getConfiguredSystemFkOpenIssueCountByCategory(String categoryName);

    /**
     * Updates the given Configured System ID with the Current FACET Version.
     * @param id Configured System ID
     * @return 1 if successful (number of records affected)
     */
    int updateFacetVersionById(Integer id);

    /**
     * Get all configured systems by ship Fk
     * @param shipFk
     * @return List of Configured Systems.
     */
    List<ConfiguredSystem> getByShipFk(Integer shipFk);
}
