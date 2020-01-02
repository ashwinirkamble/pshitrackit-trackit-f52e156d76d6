package com.premiersolutionshi.support.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.support.domain.IssueCategory;

public interface IssueCategoryDao extends BaseDao<IssueCategory> {

    /**
     * Gets Issue Category by name.
     * @param categoryName
     * @return The issue category. Null if not found.
     */
    IssueCategory getByName(String categoryName);

    /**
     * Gets Issue Categories by Project FK.
     * @param projectFk
     * @return List of Issue Categories.
     */
    ArrayList<IssueCategory> getByProjectFk(@Param("projectFk") Integer projectFk);
}