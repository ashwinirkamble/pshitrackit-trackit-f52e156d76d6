package com.premiersolutionshi.support.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.PocType;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.OrganizationService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAction;
import com.premiersolutionshi.common.ui.action.OrganizationAction;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.ShipService;
import com.premiersolutionshi.support.ui.form.PocForm;

public class PocAction extends BaseAction {
    private PocService pocService;
    private OrganizationService organizationService;
    private ShipService shipService;

    public PocAction() {
        super(PocAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        pocService = new PocService(sqlSession, userService);
        organizationService = new OrganizationService(sqlSession, userService);
        shipService = new ShipService(sqlSession);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = getAction();
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id != null) {
            ((BaseDomain) form).setId(id);
        }
        if (action != null) {
            //this is for the "pocSearchBar.jsp" dropdown
            request.setAttribute("unitPocList", shipService.getAllWithPocs());
            request.setAttribute("organizationList", organizationService.getAll());
            request.setAttribute("action", action);
            if (action.equals(FORWARD_FORM) || action.equals(FORWARD_VIEW)) {
                return handleForm(request, response, action, id);
            }
            else if (action.equals(FORWARD_SAVE)) {
                return handleSave(conn, request, response, form);
            }
            else if (action.equals(FORWARD_SEARCH)) {
                return handleSearch(conn, request, response, form);
            }
            else if (action.equals(FORWARD_DELETE)) {
                return handleDelete(conn, request, response, form, id);
            }
        }
        return handleIndex(request, response, action);
    }

    private String handleSearch(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) 
        throws IOException {
        String searchText = StringUtils.removeXss(request.getParameter("searchText"));
        setProjectPk(request.getParameter("projectFk"));
        ArrayList<Poc> pocSearchResults = pocService.search(searchText);
        if (pocSearchResults != null && pocSearchResults.size() == 1) {
            Poc poc = pocSearchResults.get(0);
            response.sendRedirect("poc.do?action=view&projectPk=" + poc.getProjectFk() + "&id=" + poc.getId());
            return null;
        }
        request.setAttribute("searchText", searchText);
        request.setAttribute("pocSearchResults", pocSearchResults);
        return FORWARD_SEARCH;
    }

    private String handleDelete(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form, Integer id) 
        throws IOException {
        boolean isInvalidId = id == null || id <= 0;
        if (isInvalidId) {
            logError("ID '" + id + "' was either null or invalid. Could not proceed to delete.");
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, "invalid ID", null);
            return null;
        }
        Poc poc = pocService.getById(id);
        boolean success = pocService.deleteById(id);
        if (poc != null) {
            setProjectPk(poc.getProjectFk());
            Integer organizationFk = poc.getOrganizationFk();
            Integer shipFk = poc.getShipFk();
            String orgPath = "organization.do";
            String deleteSuccessKey = CommonMessage.DELETE_SUCCESS.getKey();
            String deleteFailedKey = CommonMessage.DELETE_FAILED.getKey();
            if (organizationFk != null && organizationFk > 0) {
                String baseRedirectUrl = orgPath + "?action=" + FORWARD_VIEW
                    + "&projectPk=" + getProjectPk()
                    + "&id=" + organizationFk
                    + "&additional=deleted POC " + id;
                if (success) {
                    response.sendRedirect(baseRedirectUrl + "&msg=" + deleteSuccessKey);
                }
                else {
                    response.sendRedirect(baseRedirectUrl + "&msg=" + deleteFailedKey);
                }
                return null;
            }
            else if (shipFk != null && shipFk > 0) {
                String baseRedirectUrl = orgPath + "?action=" + OrganizationAction.FORWARD_UNIT_POC_LIST
                    + "&projectPk=" + getProjectPk()
                    + "&shipFk=" + shipFk
                    + "&additional=deleted POC " + id;
                if (success) {
                    response.sendRedirect(baseRedirectUrl + "&msg=" + deleteSuccessKey);
                }
                else {
                    response.sendRedirect(baseRedirectUrl + "&msg=" + deleteFailedKey);
                }
                return null;
            }
        }
        if (success) {
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_SUCCESS, id, null, null);
        }
        else {
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, null, null);
        }
        return null;
    }

    private String handleSave(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form)
        throws IOException {
        PocForm pocForm = (PocForm) form;
        Integer id = pocForm.getId();
        boolean isNew = id == null || id <= 0;
        if (!isNew) {
            setProjectPk(pocForm.getProjectFk());
        }
        if (validateForm(pocForm, isNew, response)) {
            boolean success = pocService.save(pocForm);
            if (!isNew) {
                id = pocForm.getId();
            }
            setProjectPk(pocForm.getProjectFk());
            String projectParam = "&projectPk=" + getProjectPk();
            Integer newId = pocForm.getId();
            if (isNew) {
                if (success) {
                    redirectWithMessage(response, FORWARD_VIEW, CommonMessage.SAVE_SUCCESS, newId, null, projectParam);
                }
                else {
                    redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, newId, null, projectParam);
                }
            }
            else {
                if (success) {
                    redirectWithMessage(response, FORWARD_VIEW, CommonMessage.SAVE_SUCCESS, newId, null, projectParam);
                }
                else {
                    redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, newId, null, projectParam);
                }
            }
        }
        return null;
    }

    private boolean validateForm(PocForm pocForm, boolean isNew, HttpServletResponse response) throws IOException {
        if (pocForm == null) {
            logError("The entire Form was not even set.");
            return false;
        }
        Integer id = pocForm.getId();
        Integer projectFk = pocForm.getProjectFk();
        PocType pocType = pocForm.getPocType();
        Integer organizationFk = pocForm.getOrganizationFk();
        Integer shipFk = pocForm.getShipFk();
        if (projectFk == null || projectFk <= 0) {
            return redirectValidationError(isNew, response, id, "Project Fk field is Required.");
        }
        if (pocType.equals(PocType.ORGANIZATION) && (organizationFk == null || organizationFk <= 0)) {
            return redirectValidationError(isNew, response, id, "Organization field is Required.");
        }
        if (pocType.equals(PocType.SHIP) && (shipFk == null || shipFk <= 0)) {
            return redirectValidationError(isNew, response, id, "Ship field is Required.");
        }
        if (StringUtils.isEmpty(pocForm.getLastName())) {
            return redirectValidationError(isNew, response, id, "Last Name field is Required.");
        }
        if (StringUtils.isEmpty(pocForm.getFirstName())) {
            return redirectValidationError(isNew, response, id, "First Name field is Required.");
        }
        return true;
    }

    private String handleIndex(HttpServletRequest request, HttpServletResponse response, String action) throws IOException {
        response.sendRedirect("organization.do" + (getProjectPk() == null ? "" : "?projectPk=" + getProjectPk()));
        return null;
    }

    private String handleForm(HttpServletRequest request, HttpServletResponse response, String action, Integer id) throws IOException {
        boolean isNew = id == null || id <= 0;
        PocForm pocForm;
        Organization organization = null;
        Ship ship = null;
        if (isNew) {
            Integer pocTypeCode = StringUtils.parseInt(request.getParameter("pocTypeCode"));
            PocType pocType = PocType.ORGANIZATION;
            if (pocTypeCode != null) {
                pocType = PocType.getByCode(pocTypeCode);
            }
            pocForm = createNewInstance();
            pocForm.setPocType(pocType);
            if (pocType.equals(PocType.ORGANIZATION)) {
                Integer organizationFk = StringUtils.parseInt(request.getParameter("organizationFk"));
                if (organizationFk != null) {
                    organization = organizationService.getById(organizationFk);
                    if (organization != null) {
                        pocForm.setOrganizationFk(organizationFk);
                        pocForm.setOrganization(organization);
                    }
                }
            }
            else if (pocType.equals(PocType.SHIP)) {
                Integer shipFk = StringUtils.parseInt(request.getParameter("shipFk"));
                if (shipFk != null) {
                    ship = shipService.getById(shipFk);
                    if (ship != null) {
                        pocForm.setShipFk(shipFk);
                        pocForm.setShip(ship);
                    }
                }
            }
        }
        else {
            pocForm = new PocForm();
            Poc poc = pocService.getById(id);
            if (poc == null) {
                redirectWithMessage(response, CommonMessage.NOT_FOUND, id);
                return null;
            }
            else {
                pocForm.copy(poc);
                setProjectPk(pocForm.getProjectFk());
                PocType pocType = pocForm.getPocType();
                if (pocType.equals(PocType.ORGANIZATION)) {
                    organization = organizationService.getById(pocForm.getOrganizationFk());
                }
                else {
                    ship = shipService.getById(pocForm.getShipFk());
                }
            }
        }
        request.setAttribute("pocForm", pocForm);
        request.setAttribute("ship", ship);
        request.setAttribute("organization", organization);
        request.setAttribute("unitList", shipService.getAll());
        return action;
    }

    private PocForm createNewInstance() {
        PocForm pocForm = new PocForm();
        pocForm.setProjectFk(getProjectPk());
        return pocForm;
    }

    @Override
    protected String path() {
        return "poc.do";
    }
}
