package com.premiersolutionshi.old.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.springframework.util.StringUtils;

/**
 * Data holder for a COMPANY/ORGANIZATION form
 */
public class OrganizationBean extends ActionForm {
    private static final long serialVersionUID = 360120461385912862L;

    private boolean isEmpty(String tData) {
        return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null"));
    }

    private String nes(String tStr) {
        return (isEmpty(tStr) ? "" : tStr);
    }

    public OrganizationBean() {
    }

    public OrganizationBean(String organization) {
        this.organization = organization;
    }

    private String organization = null;

    public String getOrganization() {
        return nes(this.organization);
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    private List<UserBean> pocList = null;

    public List<UserBean> getPocList() {
        return this.pocList;
    }

    public void setPocList(List<UserBean> pocList) {
        this.pocList = pocList;
    }

    public String getAllPocEmails() {
        if (pocList != null && !pocList.isEmpty()) {
            Set<String> pocEmailSet = new HashSet<>();
            for (UserBean poc : pocList) {
                String email = poc.getEmail();
                if (!StringUtils.isEmpty(email)) {
                    pocEmailSet.add(email);
                }
            }
            if (!pocEmailSet.isEmpty()) {
                List<String> pocEmailList = new ArrayList<>(pocEmailSet);
                StringBuilder pocEmailListStr = new StringBuilder();
                int size = pocEmailList.size();
                pocEmailListStr.append(pocEmailList.get(0));
                for (int i = 1; i < size; i++) {
                    pocEmailListStr.append("; ").append(pocEmailList.get(i));
                }
                return pocEmailListStr.toString();
            }
        }
        return null;
    }

}