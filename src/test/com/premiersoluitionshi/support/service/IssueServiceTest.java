package com.premiersoluitionshi.support.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersoluitionshi.common.service.BulkDomainServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.ManagedListItemService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueCategory;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;

public class IssueServiceTest extends BulkDomainServiceTest<Issue> {
    private FileInfoService fileInfoService;
    private ConfiguredSystemService configuredSystemService;
    private IssueService issueService;
    private IssueCategoryService issueCategoryService;
    private IssueCommentsService issueCommentsService;
    private ManagedListItemService managedListItemService;
    private PocService pocService;

    private ArrayList<IssueCategory> issueCategoryList;

    public IssueServiceTest() {
        super(IssueServiceTest.class);
    }

    public static void main(String[] args) {
        IssueServiceTest test = new IssueServiceTest();
        test.runBaseTests();
        test.runBulkDomainTests();
        test.testBean();
    }

    private IssueCategory getRandomIssueCategory() {
        if (issueCategoryList == null) {
            issueCategoryList = issueCategoryService.getAll();
        }
        int size = issueCategoryList.size();
        return issueCategoryList.get(TestData.genRandInt(size - 1));
    }

    public void testBean() {
        Issue createInstance = createInstance();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(createInstance.getBean());
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
    }

    @Override
    public Issue createInstance() {
        Issue issue = new Issue();
        applyRandomData(issue);
        IssueCategory category = getRandomIssueCategory();
        issue.setIssueCategory(category);
        issue.setIssueCategoryFk(category.getId());
        return issue;
    }

    @Override
    public IssueService getService() {
        return issueService;
    }

    @Override
    protected void init() {
        UserService userService = getUserService();
        SqlSession sqlSession = getSqlSession();
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        fileInfoService = new FileInfoService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        managedListItemService = new ManagedListItemService(sqlSession, userService);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, configuredSystemService,
                fileInfoService, managedListItemService, pocService);
    }

    @Override
    public boolean compare(Issue domain1, Issue domain2) {
        if (domain1 == null || domain2 == null) {
            logError("Cannot compare with null values.");
            return false;
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getProjectFk(), domain2.getProjectFk(), "ProjectFk");
        testEquals(domain1.getShipFk(), domain2.getShipFk(), "ShipFk");
        testEquals(domain1.getTitle(), domain2.getTitle(), "Title");
        testEquals(domain1.getDescription(), domain2.getDescription(), "Description");
        testEquals(domain1.getStatus(), domain2.getStatus(), "Status");
        testEquals(domain1.getPriority(), domain2.getPriority(), "Priority");
        testEquals(domain1.getCategory(), domain2.getCategory(), "Category");
        testEquals(domain1.getPhase(), domain2.getPhase(), "Phase");
        testEquals(domain1.getOpenedBy(), domain2.getOpenedBy(), "OpenedBy");
        testEquals(domain1.getOpenedDate(), domain2.getOpenedDate(), "OpenedDate");
        testEquals(domain1.getClosedDate(), domain2.getClosedDate(), "ClosedDate");
        testEquals(domain1.getPersonAssigned(), domain2.getPersonAssigned(), "PersonAssigned");
        testEquals(domain1.getSupportVisitDate(), domain2.getSupportVisitDate(), "SupportVisitDate");
        testEquals(domain1.getSupportVisitLoc(), domain2.getSupportVisitLoc(), "SupportVisitLoc");
        testEquals(domain1.getSupportVisitTime(), domain2.getSupportVisitTime(), "SupportVisitTime");
        testEquals(domain1.getTrainer(), domain2.getTrainer(), "Trainer");
        testEquals(domain1.getResolution(), domain2.getResolution(), "Resolution");
        testEquals(domain1.getTotalTime(), domain2.getTotalTime(), "TotalTime");
        testEquals(domain1.getCreatedBy(), domain2.getCreatedBy(), "CreatedBy");
        testEquals(domain1.getCreatedDate(), domain2.getCreatedDate(), "CreatedDate");
        testEquals(domain1.getLastUpdatedBy(), domain2.getLastUpdatedBy(), "LastUpdatedBy");
        testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "LastUpdatedDate");
        testEquals(domain1.getInitiatedBy(), domain2.getInitiatedBy(), "InitiatedBy");
        testEquals(domain1.getDept(), domain2.getDept(), "Dept");
        testEquals(domain1.getIsEmailSent(), domain2.getIsEmailSent(), "IsEmailSent");
        testEquals(domain1.getIsEmailResponded(), domain2.getIsEmailResponded(), "IsEmailResponded");
        testEquals(domain1.getIsTrainingProvided(), domain2.getIsTrainingProvided(), "IsTrainingProvided");
        testEquals(domain1.getIsTrainingOnsite(), domain2.getIsTrainingOnsite(), "IsTrainingOnsite");
        testEquals(domain1.getAtoFk(), domain2.getAtoFk(), "AtoFk");
        testEquals(domain1.getAutoCloseDate(), domain2.getAutoCloseDate(), "AutoCloseDate");
        testEquals(domain1.getIssueCategoryFk(), domain2.getIssueCategoryFk(), "IssueCategoryFk");
        testEquals(domain1.getPriorityReason(), domain2.getPriorityReason(), "PriorityReason");
        testEquals(domain1.getSupportVisitEndTime(), domain2.getSupportVisitEndTime(), "SupportVisitEndTime");
        testEquals(domain1.getSupportVisitReason(), domain2.getSupportVisitReason(), "SupportVisitReason");
        testEquals(domain1.getLaptopIssue(), domain2.getLaptopIssue(), "LaptopIssue");
        testEquals(domain1.getScannerIssue(), domain2.getScannerIssue(), "ScannerIssue");
        testEquals(domain1.getSoftwareIssue(), domain2.getSoftwareIssue(), "SoftwareIssue");
        testEquals(domain1.getSupportVisitLocNotes(), domain2.getSupportVisitLocNotes(), "SupportVisitLocNotes");
        testEquals(domain1.getAutoCloseStatus(), domain2.getAutoCloseStatus(), "AutoCloseStatus");
        testEquals(domain1.getConfiguredSystemFk(), domain2.getConfiguredSystemFk(), "ConfiguredSystemFk");
        return true;
    }

    @Override
    public void applyRandomData(Issue domain) {
        domain.setProjectFk(TestData.genRandInt());
        domain.setShipFk(TestData.genRandInt());
        domain.setTitle(TestData.genRandString());
        domain.setDescription(TestData.genRandString());
        domain.setStatus(TestData.genRandString());
        domain.setPriority(TestData.genRandString());
        domain.setPhase(TestData.genRandString());
        domain.setOpenedBy(TestData.genRandString());
        domain.setOpenedDate(TestData.genRandDate());
        domain.setClosedDate(TestData.genRandDate());
        domain.setPersonAssigned(TestData.genRandString());
        domain.setSupportVisitDate(TestData.genRandDate());
        domain.setSupportVisitLoc(TestData.genRandString());
        domain.setSupportVisitTime(TestData.genRandInt());
        domain.setTrainer(TestData.genRandString());
        domain.setResolution(TestData.genRandString());
        domain.setTotalTime(TestData.genRandInt());
        domain.setCreatedBy(TestData.genRandString());
        domain.setCreatedDate(TestData.genRandDateTime());
        domain.setLastUpdatedBy(TestData.genRandString());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
        domain.setInitiatedBy(TestData.genRandString());
        domain.setDept(TestData.genRandString());
        domain.setIsEmailSent(TestData.genRandString());
        domain.setIsEmailResponded(TestData.genRandString());
        domain.setIsTrainingProvided(TestData.genRandString());
        domain.setIsTrainingOnsite(TestData.genRandString());
        domain.setAtoFk(TestData.genRandInt());
        domain.setAutoCloseDate(TestData.genRandDate());
        domain.setPriorityReason(TestData.genRandString());
        domain.setSupportVisitEndTime(TestData.genRandInt());
        domain.setSupportVisitReason(TestData.genRandString());
        domain.setLaptopIssue(TestData.genRandString());
        domain.setScannerIssue(TestData.genRandString());
        domain.setSoftwareIssue(TestData.genRandString());
        domain.setSupportVisitLocNotes(TestData.genRandString());
        domain.setAutoCloseStatus(TestData.genRandString());
        domain.setConfiguredSystemFk(TestData.genRandInt());
    }
}

