package com.premiersolutionshi.support.domain;

import com.premiersolutionshi.common.domain.ModifiedDomain;

public class IssueCategory extends ModifiedDomain {
    private static final long serialVersionUID = -5275025036519892559L;

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
