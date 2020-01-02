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
import com.premiersolutionshi.report.domain.Contract;
import com.premiersolutionshi.report.service.ContractService;
import com.premiersolutionshi.report.ui.form.ContractForm;

public class ContractAction extends BaseAction {
    private ContractService contractService;
    private OrganizationService organizationService;

    public ContractAction() {
        super(ContractAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        contractService = new ContractService(sqlSession, userService);
        organizationService = new OrganizationService(sqlSession, userService);
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
                return handleSave(conn, request, response, form);
            }
            else if (action.equals(FORWARD_DELETE)) {
                return handleDelete(conn, request, response, form, id);
            }
        }
        return handleIndex(request, response, action);
    }

    private String handleDelete(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form, Integer id) 
        throws IOException {
        boolean isInvalidId = id == null || id <= 0;
        if (isInvalidId) {
            logError("ID '" + id + "' was either null or invalid. Could not proceed to delete.");
            redirectWithMessage(response, null, CommonMessage.DELETE_FAILED, id, "invalid ID", null);
            return null;
        }
        boolean success = contractService.deleteById(id);
        if (success) {
            redirectWithMessage(response, null, CommonMessage.DELETE_SUCCESS, id, null, null);
        }
        else {
            redirectWithMessage(response, null, CommonMessage.DELETE_FAILED, id, null, null);
        }
        return null;
    }

    private String handleSave(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form)
        throws IOException {
        ContractForm contractForm = (ContractForm) form;
        Integer id = contractForm.getId();
        boolean isNew = id == null || id <= 0;
        if (!isNew) {
            setProjectPk(contractForm.getProjectFk());
        }
        if (validateForm(contractForm, isNew, response)) {
            boolean success = contractService.save(contractForm);
            if (!isNew) {
                id = contractForm.getId();
            }
            if (success) {
                redirectWithMessage(response, null, CommonMessage.SAVE_SUCCESS, id, null, null);
            }
            else {
                redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, id, null, null);
            }
        }
        return null;
    }

    private boolean validateForm(ContractForm contractForm, boolean isNew, HttpServletResponse response) throws IOException {
        if (contractForm == null) {
            logError("The entire Form was not even set.");
            return false;
        }
        Integer id = contractForm.getId();
        if (StringUtils.isEmpty(contractForm.getContractNumber())) {
            return redirectValidationError(isNew, response, id, "Contract Number field is Required.");
        }
        return true;
    }

    private String handleIndex(HttpServletRequest request, HttpServletResponse response, String action) {
        ArrayList<Contract> contractList = contractService.getAll();
        request.setAttribute("contractList", contractList);
        return "index";
    }

    private String handleForm(HttpServletRequest request, HttpServletResponse response, String action, Integer id) {
        boolean isNew = id == null || id <= 0;
        Contract contractForm;
        if (isNew) {
            contractForm = createNewInstance();
        }
        else {
            contractForm = contractService.getById(id);
        }
        request.setAttribute("contractForm", contractForm);
        request.setAttribute("organizationList", organizationService.getAll());
        return action;
    }

    private Contract createNewInstance() {
        ContractForm contractForm = new ContractForm();
        contractForm.setProjectFk(getProjectPk());
        return contractForm;
    }

    @Override
    protected String path() {
        return "contract.do";
    }
}

