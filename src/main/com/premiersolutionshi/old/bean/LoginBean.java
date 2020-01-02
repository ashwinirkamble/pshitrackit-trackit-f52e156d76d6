package com.premiersolutionshi.old.bean;

import org.apache.struts.action.ActionForm;

/**
 * Data holder for the LOGIN form
 *
 * @author   LOGCOP Team
 * @version  2.0, 10/10/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class LoginBean extends ActionForm {
    private static final long serialVersionUID = -4357702927028011508L;
    private boolean isEmpty(String tData) { return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null")); }
    //private String nvl(String value, String nullValue) { return (isEmpty(value) ? nullValue : value); }
    private String nes(String tStr) { return (isEmpty(tStr) ? "" : tStr); }

    private String userPk = null;
    public String getUserPk() { return nes(this.userPk); }
    public void setUserPk(String newUserPk) { this.userPk = newUserPk; }

    private String username = null;
    public String getUsername() { return nes(this.username); }
    public void setUsername(String newUsername) { this.username = newUsername; }

    private String password = null;
    public String getPassword() { return nes(this.password); }
    public void setPassword(String newPassword) { this.password = newPassword; }

    private String passwordConfirm = null;
    public String getPasswordConfirm() { return nes(this.passwordConfirm); }
    public void setPasswordConfirm(String newPasswordConfirm) { this.passwordConfirm = newPasswordConfirm; }

    private String oldPassword = null;
    public String getOldPassword() { return nes(this.oldPassword); }
    public void setOldPassword(String newOldPassword) { this.oldPassword = newOldPassword; }

    private String lastName = null;
    public String getLastName() { return nes(this.lastName); }
    public void setLastName(String newLastName) { this.lastName = newLastName; }

    private String firstName = null;
    public String getFirstName() { return nes(this.firstName); }
    public void setFirstName(String newFirstName) { this.firstName = newFirstName; }

    private String[] projectPkArr = null;
    public String[] getProjectPkArr() { return this.projectPkArr == null ? new String[0] : this.projectPkArr; }
    public void setProjectPkArr(String[] projectPkArr) { this.projectPkArr = projectPkArr; }

    private String fullName = null;
    public String getFullName() { return nes(this.fullName); }
    public void setFullName(String newFullName) { this.fullName = newFullName; }
} //end of class