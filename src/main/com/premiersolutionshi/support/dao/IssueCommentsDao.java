package com.premiersolutionshi.support.dao;

import com.premiersolutionshi.common.dao.BulkDao;
import com.premiersolutionshi.support.domain.IssueComments;

public interface IssueCommentsDao extends BulkDao<IssueComments>{

    /**
     * Deletes by issueFk.
     * @param issueFk
     * @return Number of rows deleted.
     */
    int deleteByIssueFk(int issueFk);
}
