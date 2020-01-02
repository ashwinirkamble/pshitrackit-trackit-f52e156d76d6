package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

public class PageAction extends BaseAction {
    public PageAction() {
        super(PageAction.class);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = FORWARD_INDEX;
        if (getProjectPk() != null) {
            action = FORWARD_VIEW;
        }
        else {
            request.setAttribute("contentHeader_projectName", "");
        }
        return action;
    }

    @Override
    protected String path() {
        return "page.do";
    }
}
