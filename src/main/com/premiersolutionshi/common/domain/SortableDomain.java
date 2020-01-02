package com.premiersolutionshi.common.domain;

public class SortableDomain extends ModifiedDomain {
    private static final long serialVersionUID = -6390287760879045494L;

    private Integer sortOrder;

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
