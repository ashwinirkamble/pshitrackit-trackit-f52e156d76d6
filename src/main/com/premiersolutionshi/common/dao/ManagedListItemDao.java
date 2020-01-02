package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.domain.ManagedListItem;

public interface ManagedListItemDao extends BaseDao<ManagedListItem> {
    /**
     * Gets list of Strings by managedListCode.
     * @param managedListCode
     * @return List of Strings.
     */
    ArrayList<ManagedListItem> getByListCodeAndProjectFk(@Param("managedListCode") int managedListCode, @Param("projectFk") Integer projectFk);

    /**
     * Gets the Current/Default item value in the given Managed List.
     * @param managedListCode
     * @param projectFk
     * @return An Item Value.
     */
    String getCurrentDefault(@Param("managedListCode") int managedListCode, @Param("projectFk") int projectFk);
}
