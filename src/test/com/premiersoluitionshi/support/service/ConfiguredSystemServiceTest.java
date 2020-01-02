package com.premiersoluitionshi.support.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.ManagedListItemService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.ConfiguredSystemWithIssues;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.TransmittalService;

public class ConfiguredSystemServiceTest extends BaseServiceTest<ConfiguredSystem>{
    private FileInfoService fileInfoService;
    private UserService userService;
    private ConfiguredSystemService configuredSystemService;
    private TransmittalService transmittalService;
    private IssueService issueService;
    private IssueCategoryService issueCategoryService;
    private IssueCommentsService issueCommentsService;
    private ManagedListItemService managedListItemService;
    private PocService pocService;

    public ConfiguredSystemServiceTest() {
        super(ConfiguredSystemServiceTest.class);
    }

    public static void main(String[] args) {
        ConfiguredSystemServiceTest test = new ConfiguredSystemServiceTest();

        ConfiguredSystemService service = (ConfiguredSystemService) test.getService();
        ArrayList<ConfiguredSystem> all = service.getAll();
        System.out.println("all.size()=" + all.size());
//        System.out.println("all.get(all.size() - 1)=" + all.get(all.size() - 1));
        //System.out.println("allWithIssues.get(allWithIssues.size() - 1)=" + allWithIssues.get(allWithIssues.size() - 1));
        ArrayList<ConfiguredSystemWithIssues> allNotRetired = test.getAllWithIssues();
        for (ConfiguredSystem cs : allNotRetired) {
            //System.out.println(cs.getPrimaryPocEmails());
            Ship ship = cs.getShip();
            int pocCount = 0;
            if (ship != null) {
                ArrayList<Poc> primaryPocList = ship.getPrimaryPocList();
                if (primaryPocList != null && !primaryPocList.isEmpty()) {
                    pocCount = primaryPocList.size();
                }
            }
//            System.out.println("POC list size: " + pocCount + ", shipName=" + (ship == null ? "" : ship.getShipName()));
            System.out.println("getPrimaryPocEmails: " + cs.getPrimaryPocEmails() + ", count=" + pocCount);
            System.out.println("=====");
        }

//        ArrayList<ConfiguredSystemWithIssues> allWithIssues = test.getAllWithIssues();
//        System.out.println("allWithIssues.size()=" + allWithIssues.size());
//
//        System.out.println("has location list:");
//        for (ConfiguredSystem withIssue : allWithIssues) {
//            if (!StringUtils.isEmpty(withIssue.getLocation())) {
//                System.out.println("id=" + withIssue.getId() + ", location=" + withIssue.getLocation());
//            }
//        }
//        test.runBaseTests();
    }

    public ArrayList<ConfiguredSystemWithIssues> getAllWithIssues() {
        String currFacetVersion = "3.4.0";
        String currOsVersion = "Windows 10";
        return configuredSystemService.getAllNotRetiredWithIssues(transmittalService, issueService, currFacetVersion, currOsVersion, pocService);
    }

    @Override
    protected void init() {
        SqlSession sqlSession = getSqlSession();
        userService = new UserService(sqlSession);
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        transmittalService = new TransmittalService(sqlSession);
        fileInfoService = new FileInfoService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        managedListItemService = new ManagedListItemService(sqlSession, userService);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, configuredSystemService,
                fileInfoService, managedListItemService, pocService);
        pocService = new PocService(sqlSession, userService);
    }

    @Override
    public BaseService<ConfiguredSystem> getService() {
        return configuredSystemService;
    }

    @Override
    public ConfiguredSystem createInstance() {
        ConfiguredSystem configuredSystem = new ConfiguredSystem();
        applyRandomData(configuredSystem);
        return configuredSystem;
    }

    @Override
    public void applyRandomData(ConfiguredSystem domain) {
        
    }

    @Override
    public boolean compare(ConfiguredSystem domain1, ConfiguredSystem domain2) {
        return test(domain1.getId() == domain2.getId(), "compare Id");
    }
}
