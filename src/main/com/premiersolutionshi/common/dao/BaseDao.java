package com.premiersolutionshi.common.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.domain.BaseDomain;

public interface BaseDao<T extends BaseDomain> {

    /**
     * Gets all domains.
     * @return List of Domain.
     */
    ArrayList<T> getAll();

    /**
     * Gets "size" number of rows starting from "startRow".
     * @param startRow
     * @param size
     * @return List of Domain.
     */
    ArrayList<T> getLimit(@Param("startRow") int startRow, @Param("size") int size);

    /**
     * Gets the total number of records.
     * @return Number of records.
     */
    int getCount();

    /**
     * Gets a single record by ID.
     * @param id
     * @return A domain.
     */
    T getById(Integer id);

    /**
     * Inserts a domain record.
     * @param domain
     * @return 1 if successful. (number of results effected)
     */
    int insert(T domain);

    /**
     * Updates a domain record by ID.
     * @param domain
     * @return 1 if successful. (number of results effected)
     */
    int update(T domain);

    /**
     * Deletes a domain record by ID.
     * @param domain
     * @return 1 if successful. (number of results effected)
     */
    int deleteById(Integer id);
}
