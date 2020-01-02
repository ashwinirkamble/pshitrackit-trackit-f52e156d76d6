package com.premiersolutionshi.old.bean;

import org.apache.struts.action.ActionForm;

public class BaseBean extends ActionForm {
    private static final long serialVersionUID = -1932242073173311094L;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected String nvl(String value, String nullValue) {
        return (isEmpty(value) ? nullValue : value);
    }

    protected boolean isEmpty(String tData) {
        return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null"));
    }

    protected String nes(String tStr) {
        return (isEmpty(tStr) ? "" : tStr);
    }

    protected String js(String tStr) {
        return nes(tStr).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "&quot;").replaceAll("'", "\\\\\'");
    }
}
