package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.service.OrganizationService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.form.OrganizationForm;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.ShipService;

public class OrganizationAction extends BaseAction {
    public static final String FORWARD_UNIT_POC_LIST = "unitPocList";
    private OrganizationService organizationService;
    private PocService pocService;
    private ShipService shipService;

    public OrganizationAction() {
        super(OrganizationAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        organizationService = new OrganizationService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        shipService = new ShipService(sqlSession);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = request.getParameter("action");
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id != null) {
            ((BaseDomain) form).setId(id);
        }
        if (action == null || (action.equals(FORWARD_FORM)
                || action.equals(FORWARD_VIEW)
                || action.equals(FORWARD_UNIT_POC_LIST)
                || action.equals(FORWARD_INDEX)
            )
        ) {
            //this is for the "pocSearchBar.jsp" dropdown
            request.setAttribute("organizationList", organizationService.getAll());
            request.setAttribute("unitPocList", shipService.getAllWithPocs());
        }
        if (action != null) {
            if (action.equals(FORWARD_FORM) || action.equals(FORWARD_VIEW)) {
                return handleForm(request, response, action, id);
            }
            if (action.equals(FORWARD_UNIT_POC_LIST)) {
                return handleUnitPocList(request, response, action);
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
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, "invalid ID", null);
            return null;
        }
        boolean success = organizationService.deleteById(id);
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
        OrganizationForm organizationForm = (OrganizationForm) form;
        Integer id = organizationForm.getId();
        boolean isNew = id == null || id <= 0;
        if (!isNew) {
            setProjectPk(organizationForm.getProjectFk());
        }
        if (validateForm(organizationForm, isNew)) {
            boolean success = organizationService.save(organizationForm);
            if (!isNew) {
                id = organizationForm.getId();
            }
            if (success) {
                redirectWithMessage(response, FORWARD_VIEW, CommonMessage.SAVE_SUCCESS, id, null, null);
            }
            else {
                redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, id, null, null);
            }
        }
        else {
            redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, id, "check required fields.", null);
        }
        return null;
    }

    private boolean validateForm(OrganizationForm organizationForm, boolean isNew) {
        if (organizationForm == null) {
            logError("The entire Form was not even set.");
            return false;
        }
        if (StringUtils.isEmpty(organizationForm.getName())) {
            logError("'Name' is required, yet was submitted as NULL or as an Empty String.");
            return false;
        }
        return true;
    }

    private String handleIndex(HttpServletRequest request, HttpServletResponse response, String action) {
        ArrayList<Organization> organizationList = organizationService.getAll();
        request.setAttribute("organizationList", organizationList);
        return "index";
    }

    private String handleForm(HttpServletRequest request, HttpServletResponse response, String action, Integer id) throws IOException {
        boolean isNew = id == null || id <= 0;
        Organization organizationForm;
        if (isNew) {
            organizationForm = createNewInstance();
        }
        else {
            organizationForm = organizationService.getById(id);
        }
        if (action.equals(FORWARD_FORM)) {
            ArrayList<ManagedListItem> stateList = getManagedListItemService().getByList(ManagedList.STATE_PROVINCES);
            request.setAttribute("stateList", stateList);
        }
        else if (action.equals(FORWARD_VIEW)) {
            if (organizationForm == null) {
                redirectWithMessage(response, CommonMessage.NOT_FOUND, id);
            }
            else {
                ArrayList<Poc> pocList = pocService.getByOrganizationFk(id);
                organizationForm.setPocList(pocList);
                request.setAttribute("pocList", pocList);
            }
        }
        request.setAttribute("organizationForm", organizationForm);
        return action;
    }

    private String handleUnitPocList(HttpServletRequest request, HttpServletResponse response, String action) throws IOException {
        Integer shipFk = StringUtils.parseInt(request.getParameter("shipFk"));
        if (shipFk == null) {
            redirectWithMessage(response, CommonMessage.NOT_FOUND, shipFk, "For Unit POCs.");
            return null;
        }
        Ship ship = shipService.getById(shipFk);
        ArrayList<Poc> pocList = null;
        if (ship != null) {
            pocList = pocService.getByShipFk(shipFk);
            ship.setPocList(pocList);
        }
        request.setAttribute("ship", ship);
        request.setAttribute("pocList", pocList);
        return action;
    }

    private Organization createNewInstance() {
        OrganizationForm organizationForm = new OrganizationForm();
        organizationForm.setProjectFk(getProjectPk());
        return organizationForm;
    }

    @Override
    protected String path() {
        return "organization.do";
    }
}

