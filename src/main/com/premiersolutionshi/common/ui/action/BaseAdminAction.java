package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.util.StringUtils;

public abstract class BaseAdminAction<T extends BaseDomain> extends BasePagedAction {

    /**
     * Gets the default service for this action.
     * 
     * @return
     */
    protected abstract BaseService<T> getService();

    /**
     * Create a new domain.
     * 
     * @return A domain.
     */
    protected abstract T createNewDomain();

    /**
     * The URL path to this set of pages. "i.e. atoUpdate.do"
     * 
     * @return The URL path.
     */
    protected abstract String path();

    public BaseAdminAction(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        super.run(conn, request, response, form);

        String action = getAction();
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id != null) {
            ((BaseDomain) form).setId(id);
        }
        if (action != null) {
            if (action.equals(FORWARD_LIST)) {
                return handleList(request);
            }
            else if (action.equals(FORWARD_ADD)) {
                return handleAdd(request, action);
            }
            else if (action.equals(FORWARD_VIEW)) {
                return handleView(request, action, id);
            }
            else if (action.equals(FORWARD_EDIT)) {
                return handleEdit(request, action, id);
            }
            else if (action.equals(FORWARD_SAVE)) {
                return handleSave(request, response, form, action);
            }
            else if (action.equals(FORWARD_DELETE)) {
                return handleDelete(request, response, action, id);
            }
            else if (action.equals(FORWARD_ERROR)) {
                return handleError(request, response);
            }
        }
        return handleIndex(request, action, id);
    }

    protected String handleIndex(HttpServletRequest request, String action, Integer id) throws IOException {
        return handleList(request);
    }

    protected String handleList(HttpServletRequest request) throws IOException {
        request.setAttribute("domainList", getService().getAll());
        return FORWARD_LIST;
    }

    protected abstract String handleView(HttpServletRequest request, String action, Integer id) throws IOException;

    protected String handleAdd(HttpServletRequest request, String action) throws IOException {
        return handleView(request, action, null);
    }

    protected String handleEdit(HttpServletRequest request, String action, Integer id) throws IOException {
        return handleView(request, action, id);
    }

    protected String handleDelete(HttpServletRequest request, HttpServletResponse response, String action, Integer id) throws IOException {
        if (getService().deleteById(id)) {
            redirectWithMessage(response, FORWARD_INDEX, CommonMessage.DELETE_SUCCESS);
        }
        else {
            redirectWithMessage(response, CommonMessage.DELETE_FAILED, id);
        }
        return action;
    }

    @SuppressWarnings("unchecked")
    protected String handleSave(HttpServletRequest request, HttpServletResponse response, ActionForm form, String action)
            throws IOException {
        T domain = (T) form;
        boolean isNew = domain.getId() == null;
        boolean success = getService().save(domain);
        String servletPath = request.getServletPath();
        if (isNew && !success) {
            response.sendRedirect(servletPath + "?action=" + FORWARD_ADD + "&operation=" + CommonMessage.SAVE_SUCCESS);
        }
        else {
            response.sendRedirect(
                    servletPath + "?action=" + FORWARD_EDIT + "&id=" + domain.getId() + "&operation=" + CommonMessage.SAVE_FAILED);
        }
        return null;
    }

    protected String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectWithMessage(response, CommonMessage.ERROR, "Something went wrong");
        return null;
    }

    @Override
    protected void setupPaging() {
        // setTotalCount(getService().getCount());
    }
}
