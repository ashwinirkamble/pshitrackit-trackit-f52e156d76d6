package com.premiersolutionshi.common.ui.form;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.util.StringUtils;

public abstract class BaseFormImpl extends ActionForm implements BaseForm {
    private static final long serialVersionUID = 8696101082061362134L;
    private String action;
    private Integer projectFk;

    @Override
    public String getAction() {
        if (StringUtils.isEmpty(action)) {
            return "index";
        }
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public Integer getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(Integer projectFk) {
        this.projectFk = projectFk;
    }
}
