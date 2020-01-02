package com.premiersolutionshi.common.domain;

public class Organization extends PocGroup {
    private static final long serialVersionUID = -5216167950744873326L;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String stateProvince;
    private String zip;
    private String country;
    private String email;
    private String url;
    private String phone;
    private String faxNumber;
    private Integer primaryPocFk;
    private String notes;
    private Poc primaryPoc;

    public Poc getPrimaryPoc() {
        return primaryPoc;
    }

    public void setPrimaryPoc(Poc primaryPoc) {
        this.primaryPoc = primaryPoc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPrimaryPocFk() {
        return primaryPocFk;
    }

    public void setPrimaryPocFk(Integer primaryPocFk) {
        this.primaryPocFk = primaryPocFk;
    }
}
