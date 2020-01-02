package com.premiersolutionshi.support.domain;

import com.premiersolutionshi.common.domain.PocGroup;
import com.premiersolutionshi.common.util.StringUtils;

/**
 * Also known as "Unit"
 */
public class Ship extends PocGroup {
    private static final long serialVersionUID = 1981391958421834482L;
    private int shipPk;
    private String uic;
    private String shipName;
    private String type;
    private int hull;
    private String hullStr;
    private String serviceCode;
    private String tycom;
    private String homeport;
    private String rsupply;

    public int getShipPk() {
        return shipPk;
    }

    public void setShipPk(int shipPk) {
        this.shipPk = shipPk;
    }

    public String getUic() {
        return uic;
    }

    public void setUic(String uic) {
        this.uic = uic;
    }

    public String getShipName() {
        return shipName;
    }

    public String getFullName() {
        if (!StringUtils.isEmpty(shipName)) {
            StringBuilder str = new StringBuilder(shipName);
            if (!StringUtils.isEmpty(type)) {
                str.append(" (").append(type);
                if (hull > 0) {
                    str.append(" ").append(hull);
                }
                str.append(")");
            }
            return str.toString();
        }
        return "N/A";
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHull() {
        return hull;
    }

    public void setHull(int hull) {
        this.hull = hull;
    }

    public String getHullStr() {
        return hullStr;
    }

    public void setHullStr(String hullStr) {
        this.hullStr = hullStr;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getTycom() {
        return tycom;
    }

    public void setTycom(String tycom) {
        this.tycom = tycom;
    }

    public String getHomeport() {
        return homeport;
    }

    public void setHomeport(String homeport) {
        this.homeport = homeport;
    }

    public String getRsupply() {
        return rsupply;
    }

    public void setRsupply(String rsupply) {
        this.rsupply = rsupply;
    }
}
