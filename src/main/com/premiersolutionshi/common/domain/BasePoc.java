package com.premiersolutionshi.common.domain;

import com.premiersolutionshi.common.util.StringUtils;

/**
 * Point Of Contact
 */
public class BasePoc extends ModifiedDomain {
    private static final long serialVersionUID = -5633294857978999286L;
    private String lastName;
    private String firstName;
    private String email;
    private String workNumber;
    private Integer workNumberExt;
    private String cellNumber;
    private String faxNumber;

    public String getFullName() {
        if (StringUtils.isEmpty(firstName)) {
            return StringUtils.isEmpty(lastName) ? null : lastName;
        }
        return firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public Integer getWorkNumberExt() {
        return workNumberExt;
    }

    public void setWorkNumberExt(Integer workNumberExt) {
        this.workNumberExt = workNumberExt;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }
}
