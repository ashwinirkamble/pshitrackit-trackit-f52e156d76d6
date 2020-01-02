package com.premiersolutionshi.support.domain;

import com.premiersolutionshi.common.domain.SortableDomain;
import com.premiersolutionshi.common.util.StringUtils;

public class FacetVersion extends SortableDomain {
    private static final long serialVersionUID = 7109055379750983700L;
    private boolean current;

    public String getIsCurr() {
        return current ? "Y" : "N";
    }

    public void setIsCurr(String isCurr) {
        this.current = !StringUtils.isEmpty(isCurr) && isCurr.equals("Y");
    }
}
