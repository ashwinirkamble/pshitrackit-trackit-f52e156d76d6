package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.BaseDomain;

public interface BulkDao<T extends BaseDomain> extends BaseDao<T> {

    /**
     * Inserts all values within that list.
     * @param list
     * @return Number of inserted rows.
     */
    int insertAll(ArrayList<T> list);
}
