package com.premiersolutionshi.support.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAction;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.domain.GovProperty;
import com.premiersolutionshi.support.service.GovPropertyService;
import com.premiersolutionshi.support.ui.form.GovPropertyForm;

public class GovPropertyAction extends BaseAction {
    private GovPropertyService govPropertyService;

    public GovPropertyAction() {
        super(GovPropertyAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        govPropertyService = new GovPropertyService(sqlSession, userService);
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
            if (action.equals(FORWARD_FORM)) {
                return handleForm(request, response, action, id);
            }
            else if (action.equals(FORWARD_SAVE)) {
                return handleSave(conn, request, response, form);
            }
            else if (action.equals(FORWARD_EXPORT_TO_EXCEL)) {
                return handleExportToExcel(request, response);
            }
            else if (action.equals(FORWARD_DELETE)) {
                return handleDelete(conn, request, response, form, id);
            }
        }
        return handleIndex(request, response, action);
    }

    private String handleExportToExcel(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
        String filename = "Material Tracking - Manual(269).xlsx";
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        govPropertyService.writeXlsx(response.getOutputStream(), govPropertyService.getAll());
        return null;
    }

    private String handleDelete(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form, Integer id) 
        throws IOException {
        boolean isInvalidId = id == null || id <= 0;
        if (isInvalidId) {
            logError("ID '" + id + "' was either null or invalid. Could not proceed to delete.");
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_FAILED, id, "invalid ID", null);
            return null;
        }
        boolean success = govPropertyService.deleteById(id);
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
        GovPropertyForm govPropForm = (GovPropertyForm) form;
        Integer id = govPropForm.getId();
        boolean isNew = id == null || id <= 0;
        if (!isNew) {
            setProjectPk(govPropForm.getProjectFk());
        }
        if (validateForm(govPropForm, isNew)) {
            boolean success = govPropertyService.save(govPropForm);
            if (!isNew) {
                id = govPropForm.getId();
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

    private boolean validateForm(GovPropertyForm govPropForm, boolean isNew) {
        if (govPropForm == null) {
            logError("The entire Form was not even set.");
            return false;
        }
        else if (govPropForm.getDateListed() == null) {
            logError("'Date' is required, yet was submitted as NULL.");
            return false;
        }
        else if (StringUtils.isEmpty(govPropForm.getNationalStockNumber())) {
            logError("'National Stock Number' is required, yet was submitted as NULL or as an Empty String.");
            return false;
        }
        return true;
    }

    private String handleIndex(HttpServletRequest request, HttpServletResponse response, String action) {
        ArrayList<GovProperty> govPropertyList = govPropertyService.getAll();
        request.setAttribute("govPropertyList", govPropertyList);
        return "index";
    }

    private String handleForm(HttpServletRequest request, HttpServletResponse response, String action, Integer id) {
        boolean isNew = id == null || id <= 0;
        GovProperty govPropertyForm;
        if (isNew) {
            govPropertyForm = createNewInstance();
        }
        else {
            govPropertyForm = govPropertyService.getById(id);
        }
        request.setAttribute("govPropertyForm", govPropertyForm);
        return action;
    }

    private GovProperty createNewInstance() {
        GovPropertyForm govPropForm = new GovPropertyForm();
        govPropForm.setProjectFk(getProjectPk());
        return govPropForm;
    }

    @Override
    protected String path() {
        return "govProperty.do";
    }
}
