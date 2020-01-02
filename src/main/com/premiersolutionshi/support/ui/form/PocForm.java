package com.premiersolutionshi.support.ui.form;

import com.premiersolutionshi.common.constant.PocType;
import com.premiersolutionshi.common.domain.Poc;

public class PocForm extends Poc {
    private static final long serialVersionUID = -5709883858778233095L;

    private PocType pocType;

    public PocType getPocType() {
        if (pocType == null) {
            Integer shipFk = getShipFk();
            if (shipFk != null && shipFk > 0) {
                pocType = PocType.SHIP;
            }
            else {
                pocType = PocType.ORGANIZATION;
            }
        }
        return pocType;
    }

    public void setPocType(PocType pocType) {
        this.pocType = pocType;
    }

    public int getPocTypeCode() {
        return getPocType().getCode();
    }

    public void setPocTypeCode(int code) {
        this.pocType = PocType.getByCode(code);
    }

    public void copy(Poc domain) {
        super.copy(domain);
        setOrganization(domain.getOrganization());
        setShip(domain.getShip());

        setProjectFk(domain.getProjectFk());
        setOrganizationFk(domain.getOrganizationFk());
        setShipFk(domain.getShipFk());
        setPrimaryPoc(domain.isPrimaryPoc());
        setLastName(domain.getLastName());
        setFirstName(domain.getFirstName());
        setTitle(domain.getTitle());
        setRank(domain.getRank());
        setEmail(domain.getEmail());
        setWorkNumber(domain.getWorkNumber());
        setWorkNumberExt(domain.getWorkNumberExt());
        setFaxNumber(domain.getFaxNumber());
        setCellNumber(domain.getCellNumber());
        setAltEmail(domain.getAltEmail());
        setDept(domain.getDept());
        setNotes(domain.getNotes());
    }
}
