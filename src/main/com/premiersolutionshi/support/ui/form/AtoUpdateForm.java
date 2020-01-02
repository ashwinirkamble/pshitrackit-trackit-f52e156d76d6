package com.premiersolutionshi.support.ui.form;

import com.premiersolutionshi.support.domain.AtoUpdate;

public class AtoUpdateForm extends AtoUpdate {
    private static final long serialVersionUID = -79024145051282739L;

    private String[] configuredSystemPkArr = null;
    private String[] includeConfiguredSystemPkArr = null;
    private String[] reminderIssuePkArr = null;
    private String[] appliedIssuePkArr = null;
    private String[] removeIssuePkArr = null;

    public AtoUpdateForm() {
    }

    public AtoUpdateForm(AtoUpdate atoUpdate) {
        copy(atoUpdate);
    }

    public String[] getConfiguredSystemPkArr() {
        return configuredSystemPkArr;
    }

    public void setConfiguredSystemPkArr(String[] configuredSystemPkArr) {
        this.configuredSystemPkArr = configuredSystemPkArr;
    }

    public String[] getIncludeConfiguredSystemPkArr() {
        return includeConfiguredSystemPkArr;
    }

    public void setIncludeConfiguredSystemPkArr(String[] includeConfiguredSystemPkArr) {
        this.includeConfiguredSystemPkArr = includeConfiguredSystemPkArr;
    }

    public String[] getReminderIssuePkArr() {
        return reminderIssuePkArr;
    }

    public void setReminderIssuePkArr(String[] reminderIssuePkArr) {
        this.reminderIssuePkArr = reminderIssuePkArr;
    }

    public String[] getAppliedIssuePkArr() {
        return appliedIssuePkArr;
    }

    public void setAppliedIssuePkArr(String[] appliedIssuePkArr) {
        this.appliedIssuePkArr = appliedIssuePkArr;
    }

    public String[] getRemoveIssuePkArr() {
        return removeIssuePkArr;
    }

    public void setRemoveIssuePkArr(String[] removeIssuePkArr) {
        this.removeIssuePkArr = removeIssuePkArr;
    }

    public void copy(AtoUpdate domain) {
        if (domain == null) {
            return;
        }
        super.copy(domain);
        setId(domain.getId());
        setProjectFk(domain.getProjectFk());
        setAtoDate(domain.getAtoDate());
        setOpenedDate(domain.getOpenedDate());
        setComments(domain.getComments());
        setLastUpdatedBy(domain.getLastUpdatedBy());
        setLastUpdatedDate(domain.getLastUpdatedDate());
        setProjectFk(domain.getProjectFk());
    }
}
