package com.premiersolutionshi.support.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.premiersolutionshi.common.dao.BulkDao;
import com.premiersolutionshi.common.domain.JoinDomain;
import com.premiersolutionshi.support.domain.FkAndStringList;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueSupportVisit;

public interface IssueDao extends BulkDao<Issue> {

    /**
     * Get all Issues by the ATO Update ID.
     * @param atoFk
     * @return List of Issues.
     */
    ArrayList<Issue> getByAtoFk(int atoFk);

    /**
     * Gets all open issues by a list of category names.
     * @param categoryNames
     * @return List of Issues.
     */
    ArrayList<Issue> getOpenByCategoryNames(List<String> categoryNames);

    /**
     * Get all issues given the list of PKs.
     * @param issuePkList
     * @return List of Issues.
     */
    ArrayList<Issue> getByPkArr(List<String> issuePkList);

    /**
     * Get all users that support issues as a "person_assigned" or "trainer".
     * @return List of users' full names.
     */
    ArrayList<String> getSupportTeamList();

    /**
     * Get Opened Issue PKs by Ship PKs and Category Names
     * @param shipPkList
     * @param categoryNameList
     * @return List of Issue PKs
     */
    ArrayList<Integer> getOpenedIssuePksByShipPksAndCategoryNames(@Param("shipPkList") List<Integer> shipPkList,
            @Param("categoryNameList")  List<String> categoryNameList);

    /**
     * Gets list of Configured System FKs with open ATO Dates as a list of
     * strings
     * @return
     */
    ArrayList<FkAndStringList> getConfiguredSystemOpenAtoIssues();

    /**
     * Gets list of Configured System FKs with open ATO Dates as a list of
     * strings
     * @return
     */
    ArrayList<FkAndStringList> getShipOpenAtoIssues();

    /**
     * Inserts the join between a file and issue.
     * @param fileFk
     * @param issueFk
     * @return 1 if successful.
     */
    //int insertFileJoin(@Param("fileFk") Integer fileFk, @Param("issueFk") Integer issueFk);

    /**
     * Inserts the joins between file and issue.
     * @param joinList (fileFk, issueFk)
     * @return Number of inserts.
     */
    int insertMultipleFileJoins(ArrayList<JoinDomain> joinList);

    /**
     * Inserts the join between a file and issue.
     * @param fileFk
     * @param issueFk
     * @return 1 if successful.
     */
    int deleteFileJoin(@Param("fileFk") Integer fileFk, @Param("issueFk") Integer issueFk);

    /**
     * Gets the issue given the shipPk with the newest Support Visit Date.
     * @param shipPk
     * @return IssueSupportVisit.
     */
    IssueSupportVisit getLastSupportVisitByShipFk(Integer shipPk);

    /**
     * Gets all issues by shipPk
     * @param shipPk
     * @return List of Issues.
     */
    ArrayList<Issue> getOpenedByShipPk(Integer shipPk);

    /**
     * @return Number of issues that need to get closed by the auto closed date.
     */
    Integer getAutoCloseIssueCount();

    /**
     * Closes all Issues past the "auto_close_date" using their "auto_close_status"
     * 
     * If "auto_close_status" is null or invalid, "6 - Closed (Successful)" will be used.
     * 
     * @return Number of Issues closed.
     */
    int autoCloseIssues();
}
