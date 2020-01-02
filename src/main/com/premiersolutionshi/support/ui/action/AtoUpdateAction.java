package com.premiersolutionshi.support.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAdminAction;
import com.premiersolutionshi.support.domain.AtoUpdate;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.service.AtoUpdateService;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.ui.form.AtoUpdateForm;

public class AtoUpdateAction extends BaseAdminAction<AtoUpdate> {
    private AtoUpdateService atoUpdateService;
    private FileInfoService fileInfoService;
    private IssueService issueService;
    private IssueCategoryService issueCategoryService;
    private IssueCommentsService issueCommentsService;
    private ConfiguredSystemService configuredSystemService;
    private PocService pocService;

    public AtoUpdateAction() {
        super(AtoUpdateAction.class);
    }

    @Override
    protected String handleSave(HttpServletRequest request, HttpServletResponse response, ActionForm form, String action)
            throws IOException {
        AtoUpdateForm atoUpdateForm = (AtoUpdateForm) form;
        boolean isNew = atoUpdateForm.getId() == null;
        AtoUpdate savedAtoUpdate = getService().saveForm(atoUpdateForm, configuredSystemService, issueService, issueCategoryService,
                issueCommentsService);
        String returnAction = FORWARD_LIST;
        if (savedAtoUpdate != null) {
            setMessage("Successfully submitted an ATO Update.");
            Integer atoId = savedAtoUpdate.getId();
            redirectWithMessage(response, CommonMessage.SAVE_SUCCESS, atoId);
        }
        else {
            if (isNew) {
                response.sendRedirect(path() + "?action=list&msg=" + CommonMessage.SAVE_FAILED.getKey() + "&projectPk=" + getProjectPk());
            }
            else {
                redirectWithMessage(response, CommonMessage.SAVE_FAILED, atoUpdateForm.getId());
            }
        }
        return returnAction;
    }

    @Override
    protected String handleList(HttpServletRequest request) throws IOException {
        int currentPage = getCurrentPage();
        int size = getPageSize();
        int startRow = (currentPage - 1) * size;
        if (startRow > 0) {
            startRow = startRow - 1;
        }
        request.setAttribute("atoUpdateList", getService().getAll());
        return FORWARD_LIST;
    }

    @Override
    protected String handleAdd(HttpServletRequest request, String action) throws IOException {
        return handleView(request, action, null);
    }

    @Override
    protected String handleView(HttpServletRequest request, String action, Integer id) throws IOException {
        AtoUpdate atoUpdate = null;
        ArrayList<Issue> atoIssueList = null;
        boolean isNew = id == null;
        if (action.equals(FORWARD_EDIT) && !isNew) {
            atoUpdate = getService().getById(id);
            if (atoUpdate != null) {
                atoIssueList = issueService.getByAtoFk(id);
                setProjectPk(atoUpdate.getProjectFk());
            }
        }
        else {
            atoUpdate = createNewDomain();
        }
        AtoUpdateForm atoUpdateForm = new AtoUpdateForm(atoUpdate);
        ArrayList<ConfiguredSystem> configuredSystemList = configuredSystemService.getAllNotRetiredWithValidShip();
        // remove configured systems that aren't already created as an issue.
        if (!isNew) {
            cleanConfiguredSystemList(atoIssueList, configuredSystemList);
        }
        request.setAttribute("atoUpdateForm", atoUpdateForm);
        request.setAttribute("projectPk", getProjectPk() + "");
        request.setAttribute("atoIssueList", atoIssueList);
        request.setAttribute("configuredSystemList", configuredSystemList);
        request.setAttribute("editType", action);
        return isNew ? FORWARD_ADD : FORWARD_EDIT;
    }

    /**
     * If the configured system already has a issue created for it, we want to
     * remove it off the list.
     * 
     * @param atoIssueList
     * @param configuredSystemList
     */
    private void cleanConfiguredSystemList(ArrayList<Issue> atoIssueList, ArrayList<ConfiguredSystem> configuredSystemList) {
        if (atoIssueList == null) {
            return;
        }
        Set<Integer> csPkSet = new HashSet<>();
        for (Issue issue : atoIssueList) {
            csPkSet.add(issue.getConfiguredSystemFk());
        }
        int size = configuredSystemList.size();
        for (int i = 0; i < size; i++) {
            ConfiguredSystem cs = configuredSystemList.get(i);
            if (csPkSet.contains(cs.getId())) {
                configuredSystemList.remove(i--);
                size--;
            }
        }
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        return super.run(conn, request, response, form);
    }

    @Override
    protected AtoUpdateService getService() {
        return atoUpdateService;
    }

    @Override
    protected AtoUpdate createNewDomain() {
        AtoUpdate atoUpdate = new AtoUpdate();
        atoUpdate.setProjectFk(getProjectPk());
        return atoUpdate;
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        atoUpdateService = new AtoUpdateService(sqlSession, getUserService());
        fileInfoService = new FileInfoService(sqlSession, userService);
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, 
                configuredSystemService, fileInfoService, getManagedListItemService(), pocService);
    }

    @Override
    protected String path() {
        return "atoUpdate.do";
    }
}
