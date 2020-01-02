package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.util.JsonUtils;
import com.premiersolutionshi.common.util.StringUtils;

public abstract class BaseJsonAction<T extends BaseDomain> extends BaseAction {
    public BaseJsonAction(Class<?> clazz) {
        super(clazz);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = getAction();
        StringBuilder json = new StringBuilder("{");
        if (StringUtils.isEmpty(action)) {
            return "error";
        }
        if (action.equals("getAll")) {
            json.append(JsonUtils.toJson(getService().getAll()));
        }
        else {
            @SuppressWarnings("unchecked")
            T domain = (T) form;
            if (action.equals("save")) {
                if (getService().save(domain)) {
                    json.append("success:1,message:'Successfully saved.',id:").append(domain.getId());
                }
                else {
                    json.append("success:0,message:'Failed to save.'");
                }
            }
            else if (action.equals("delete")) {
                if (getService().deleteById(domain.getId())) {
                    //print { success:1, message: "Successfully deleted.", id: getId() };
                }
                else {
                    //print { success:0, message: "Failed to delete." };
                }
            }
        }
        response.getOutputStream().write(json.append("}").toString().getBytes());
        return null;
    }

    /**
     * The URL path to this set of pages. "i.e. atoUpdate.do"
     * @return The URL path.
     */
    protected abstract String path();

    /**
     * Gets the default service for this action.
     * @return
     */
    protected abstract BaseService<T> getService();
}
