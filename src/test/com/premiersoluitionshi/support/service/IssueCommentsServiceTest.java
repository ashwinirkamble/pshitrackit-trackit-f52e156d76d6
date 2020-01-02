package com.premiersoluitionshi.support.service;

import com.premiersoluitionshi.common.service.BulkDomainServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.support.domain.IssueComments;
import com.premiersolutionshi.support.service.IssueCommentsService;

public class IssueCommentsServiceTest extends BulkDomainServiceTest<IssueComments> {
    public IssueCommentsServiceTest() {
        super(IssueCommentsServiceTest.class);
    }

    private IssueCommentsService issueCommentsService;

    public static void main(String[] args) {
        IssueCommentsServiceTest test = new IssueCommentsServiceTest();
        test.runBaseTests();
        test.runBulkDomainTests();
    }

    @Override
    public IssueComments createInstance() {
        IssueComments issueComments = new IssueComments();
        applyRandomData(issueComments);
        return issueComments;
    }

    @Override
    public IssueCommentsService getService() {
        return issueCommentsService;
    }

    @Override
    protected void init() {
        issueCommentsService = new IssueCommentsService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(IssueComments domain1, IssueComments domain2) {
        if (domain1 == null || domain2 == null) {
            logError("FAILED : Cannot compare with null values.");
            return false;
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getIssueFk(), domain2.getIssueFk(), "IssueFk");
        testEquals(domain1.getComments(), domain2.getComments(), "Comments");
        testEquals(domain1.getCreatedBy(), domain2.getCreatedBy(), "CreatedBy");
        testEquals(domain1.getCreatedDate(), domain2.getCreatedDate(), "CreatedDate");
        return true;
    }

    @Override
    public void applyRandomData(IssueComments issueComments) {
        issueComments.setIssueFk(TestData.genRandInt());
        issueComments.setComments(TestData.genRandString());
        issueComments.setCreatedBy(TestData.genRandString());
        issueComments.setCreatedDate(TestData.genRandDateTime());
    }
}
