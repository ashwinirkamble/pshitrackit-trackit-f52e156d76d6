package com.premiersolutionshi.old.bean;

import org.apache.struts.upload.FormFile;

/**
 * Data holder for a DTS form
 */
public class DtsBean extends BaseBean {
    private static final long serialVersionUID = 2594760371575783376L;

    private FormFile file = null;

    public FormFile getFile() {
        return this.file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }
}
