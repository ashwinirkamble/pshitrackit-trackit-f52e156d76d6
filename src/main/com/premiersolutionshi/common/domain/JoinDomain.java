package com.premiersolutionshi.common.domain;

public class JoinDomain {

    private Integer fk1;
    private Integer fk2;

    public JoinDomain() {
    }

    public JoinDomain(Integer fk1, Integer fk2) {
        super();
        this.fk1 = fk1;
        this.fk2 = fk2;
    }

    public Integer getFk1() {
        return fk1;
    }

    public void setFk1(Integer fk1) {
        this.fk1 = fk1;
    }

    public Integer getFk2() {
        return fk2;
    }

    public void setFk2(Integer fk2) {
        this.fk2 = fk2;
    }
}
