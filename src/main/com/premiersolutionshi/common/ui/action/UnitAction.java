package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.ConfiguredSystemWithIssues;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueSupportVisit;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.ShipService;
import com.premiersolutionshi.support.service.TransmittalService;

public class UnitAction extends BaseAction {
    private ShipService shipService;
    private ConfiguredSystemService configuredSystemService;
    private IssueService issueService;
    private TransmittalService transmittalService;
    private IssueCommentsService issueCommentsService;
    private IssueCategoryService issueCategoryService;
    private FileInfoService fileInfoService;
    private PocService pocService;

    public UnitAction() {
        super(UnitAction.class);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        Ship unit = null;
        ArrayList<ConfiguredSystemWithIssues> configuredSystemList = null;
        IssueSupportVisit lastSupportVisit = null;
        ArrayList<Issue> issueList = null;

        Integer shipPk = StringUtils.parseInt(request.getParameter("id"));
        if (shipPk != null) {
            unit = shipService.getById(shipPk);
            if (unit != null) {
                setProjectPk(unit.getProjectFk());
                ArrayList<String> configuredSystemPkList = new ArrayList<>();
                List<ConfiguredSystem> csList = configuredSystemService.getByShipFk(unit.getId());
                if (csList != null && !csList.isEmpty()) {
                    for (ConfiguredSystem configuredSystem : csList) {
                        configuredSystemPkList.add("" + configuredSystem.getId());
                    }
                    configuredSystemList = configuredSystemService.getWithIssuesByPkList(configuredSystemPkList, transmittalService,
                            issueService, getCurrFacetVersion(), getCurrOsVersion(), pocService);
                }
                lastSupportVisit = issueService.getLastSupportVisitByShipFk(shipPk);
                issueList = issueService.getOpenedByShipPk(shipPk);
            }
        }
        request.setAttribute("unit", unit);
        request.setAttribute("configuredSystemList", configuredSystemList);
        request.setAttribute("lastSupportVisit", lastSupportVisit);
        request.setAttribute("issueList", issueList);
        return FORWARD_INDEX;
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        shipService = new ShipService(sqlSession);
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        transmittalService = new TransmittalService(sqlSession);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        fileInfoService = new FileInfoService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, 
                configuredSystemService, fileInfoService, getManagedListItemService(), pocService);
    }

    @Override
    protected String path() {
        return "unit.do";
    }
}
