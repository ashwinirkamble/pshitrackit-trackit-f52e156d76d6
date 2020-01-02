package com.premiersolutionshi.support.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.common.domain.Poc;

public interface PocDao extends BaseDao<Poc> {

    /**
     * Gets all POCs by Organization PK
     * @param organizationFk
     * @return List of POCs.
     */
    ArrayList<Poc> getByOrganizationFk(Integer organizationFk);

    /**
     * Gets all POCs by Ship PK
     * @param shipFk
     * @return List of POCs.
     */
    ArrayList<Poc> getByShipFk(Integer shipFk);
    
    /**
     * Gets all POCs by Project PK
     * @param projectFk
     * @return List of POCs.
     */
    ArrayList<Poc> getByProjectFk(Integer projectFk);

    /**
     * Gets all POCs by search terms in the firstName, lastName, and email columns.
     * @param searchTerms
     * @return List of POCs of Organization and Ship types.
     */
    ArrayList<Poc> search(@Param("searchTerms") ArrayList<String> searchTerms);
}
