package com.premiersolutionshi.common.domain;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.ui.form.BaseForm;
import com.premiersolutionshi.common.util.PrintClassPropertiesUtil;

public class BaseDomain extends ActionForm implements Domain, BaseForm {
    private static final long serialVersionUID = 7803642382107372834L;

    private Integer id;
    private Integer projectFk;
    private String action;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(Integer projectFk) {
        this.projectFk = projectFk;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return toString(0, true);
    }

    public String toString(int indent) {
        return toString(indent, false);
    }

    public String toString(int indent, boolean printSubDomains) {
        return PrintClassPropertiesUtil.toString(this, indent, printSubDomains);
    }

    public void copy(BaseDomain domain) {
        if (domain == null) {
            return;
        }
        setId(domain.getId());
        setProjectFk(domain.getProjectFk());
        setAction(domain.getAction());
    }
}
