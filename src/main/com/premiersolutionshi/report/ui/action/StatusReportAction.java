package com.premiersolutionshi.report.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.service.OrganizationService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAction;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.report.constant.StatusReportType;
import com.premiersolutionshi.report.domain.StatusReport;
import com.premiersolutionshi.report.service.ContractService;
import com.premiersolutionshi.report.service.StatusReportService;
import com.premiersolutionshi.report.ui.form.StatusReportForm;

public class StatusReportAction extends BaseAction {
    private StatusReportService statusReportService;
    private OrganizationService organizationService;
    private ContractService contractService;

    public StatusReportAction() {
        super(StatusReportAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        contractService = new ContractService(sqlSession, userService);
        statusReportService = new StatusReportService(sqlSession, userService, contractService);
        organizationService = new OrganizationService(sqlSession, userService);
        contractService = new ContractService(sqlSession, userService);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = getAction();
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id != null) {
            ((BaseDomain) form).setId(id);
        }
        request.setAttribute("contentHeader_projectName", "");
        if (action != null) {
            if (action.equals(FORWARD_FORM) || action.equals(FORWARD_VIEW)) {
                return handleForm(request, response, action, id);
            }
            else if (action.equals(FORWARD_SAVE)) {
                return handleSave(request, response, form);
            }
            else if (action.equals(StatusReportType.BUSINESS_VOLUME.getRef())) {
                return handleBusinesVolumeMonthly(request, response, action, id);
            }
            else if (action.equals(FORWARD_DELETE)) {
                return handleDelete(request, response, form, id);
            }
        }
        return handleIndex(request, response, action);
    }

    private String handleBusinesVolumeMonthly(HttpServletRequest request, HttpServletResponse response,
        String action, Integer id) throws IOException {
        boolean isNew = id == null || id <= 0;
        StatusReport statusReportForm = null;
        if (!isNew) {
            statusReportForm = statusReportService.getById(id);
        }
        if (statusReportForm != null) {
        }
        request.setAttribute("statusReportForm", statusReportForm);
        return action;
    }

    private String handleDelete(HttpServletRequest request, HttpServletResponse response, ActionForm form, Integer id) 
        throws IOException {
        boolean isInvalidId = id == null || id <= 0;
        if (isInvalidId) {
            logError("ID '" + id + "' was either null or invalid. Could not proceed to delete.");
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, "invalid ID", null);
            return null;
        }
        boolean success = statusReportService.deleteById(id);
        if (success) {
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_SUCCESS, id, null, null);
        }
        else {
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, null, null);
        }
        return null;
    }

    private String handleSave(HttpServletRequest request, HttpServletResponse response, ActionForm form)
        throws IOException {
        StatusReportForm statusReportForm = (StatusReportForm) form;
        Integer id = statusReportForm.getId();
        boolean isNew = id == null || id <= 0;
        if (!isNew) {
            setProjectPk(statusReportForm.getProjectFk());
        }
        if (validateForm(statusReportForm, isNew, response)) {
            boolean success = statusReportService.saveForm(statusReportForm);
            if (!isNew) {
                id = statusReportForm.getId();
            }
            if (success) {
                redirectWithMessage(response, FORWARD_VIEW, CommonMessage.SAVE_SUCCESS, id, null, null);
            }
            else {
                redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, id, null, null);
            }
        }
        return null;
    }

    private boolean validateForm(StatusReportForm statusReportForm, boolean isNew, HttpServletResponse response) throws IOException {
        if (statusReportForm == null) {
            logError("The entire Form was not even set.");
            return false;
        }
        Integer id = statusReportForm.getId();
        if (StringUtils.isEmpty(statusReportForm.getProjectName())) {
            return redirectValidationError(isNew, response, id, "Project Name field is Required.");
        }
        if (statusReportForm.getStatusReportTypeCode() == null) {
            return redirectValidationError(isNew, response, id, "Report Type field is Required.");
        }
        Integer contractFk = statusReportForm.getContractFk();
        if (contractFk == null || contractFk <= 0) {
            return redirectValidationError(isNew, response, id, "Contract field is Required.");
        }
        if (statusReportForm.getReportStartDate() == null) {
            return redirectValidationError(isNew, response, id, "Report Start Date field is Required.");
        }
        if (statusReportForm.getReportEndDate() == null) {
            return redirectValidationError(isNew, response, id, "Report End Date field is Required.");
        }
        Integer organizationFk = statusReportForm.getOrganizationFk();
        if (organizationFk == null || organizationFk <= 0) {
            return redirectValidationError(isNew, response, id, "Organization field is Required.");
        }
        Integer contractorOrgFk = statusReportForm.getContractorOrgFk();
        if (contractorOrgFk == null || contractorOrgFk <= 0) {
            return redirectValidationError(isNew, response, id, "Contractor field is Required.");
        }
        return true;
    }

    private String handleIndex(HttpServletRequest request, HttpServletResponse response, String action) {
        ArrayList<StatusReport> statusReportList = statusReportService.getAll();
        request.setAttribute("statusReportList", statusReportList);
        return "index";
    }

    private String handleForm(HttpServletRequest request, HttpServletResponse response, String action, Integer id) {
        boolean isNew = id == null || id <= 0;
        StatusReportForm statusReportForm;
        if (isNew) {
            statusReportForm = createNewInstance();
        }
        else {
            StatusReport statusReport = statusReportService.getById(id);
            statusReportForm = new StatusReportForm();
            statusReportForm.copy(statusReport);
        }
        request.setAttribute("statusReportForm", statusReportForm);
        request.setAttribute("statusReportTypeList", StatusReportType.getList());
        request.setAttribute("contractList", contractService.getAll());
        request.setAttribute("organizationList", organizationService.getAll());
        return action;
    }

    private StatusReportForm createNewInstance() {
        StatusReportForm statusReportForm = new StatusReportForm();
        return statusReportForm;
    }

    @Override
    protected String path() {
        return "statusReport.do";
    }
}
