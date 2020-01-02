package com.premiersolutionshi.old.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.premiersolutionshi.common.util.StringUtils;


/**
 * Data holder for a SHIP form
 */
public class ShipBean extends BaseBean {
    private static final long serialVersionUID = 3543421365439746584L;
    private String shipPk = null;
    private String uic = null;
    private String shipName = null;
    private String shipNameTypeHull = null;
    private String type = null;
    private String hull = null;
    private String serviceCode = null;
    private String homeport = null;
    private String tycom = null;
    private String tycomDisplay = null;
    private String rsupply = null;
    private ArrayList<UserBean> pocList = null;

    public String getHomeport() {
        return nes(this.homeport);
    }

    public String getHull() {
        return nes(this.hull);
    }

    public ArrayList<UserBean> getPocList() {
        return this.pocList;
    }

    public String getRsupply() {
        return nes(this.rsupply);
    }

    public String getServiceCode() {
        return nes(this.serviceCode);
    }

    public String getShipName() {
        return nes(this.shipName);
    }

    public String getShipNameJs() {
        return js(this.shipName);
    }

    public String getShipNameTypeHull() {
        return nes(this.shipNameTypeHull);
    }

    public String getShipPk() {
        return nes(this.shipPk);
    }

    public String getTycom() {
        return nes(this.tycom);
    }

    public String getTycomDisplay() {
        return nes(this.tycomDisplay);
    }

    public String getType() {
        return nes(this.type);
    }

    public String getUic() {
        return nes(this.uic);
    }

    public void setHomeport(String newHomeport) {
        this.homeport = newHomeport;
    }

    public void setHull(String newHull) {
        this.hull = newHull;
    }

    public void setPocList(ArrayList<UserBean> pocList) {
        this.pocList = pocList;
    }

    public void setRsupply(String rsupply) {
        this.rsupply = rsupply;
    }

    public void setServiceCode(String newServiceCode) {
        this.serviceCode = newServiceCode;
    }

    public void setShipName(String newShipName) {
        this.shipName = newShipName;
    }

    public void setShipNameTypeHull(String newShipNameTypeHull) {
        this.shipNameTypeHull = newShipNameTypeHull;
    }

    public void setShipPk(String newShipPk) {
        this.shipPk = newShipPk;
    }

    public void setTycom(String newTycom) {
        this.tycom = newTycom;
    }

    public void setTycomDisplay(String newTycomDisplay) {
        this.tycomDisplay = newTycomDisplay;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    public void setUic(String newUic) {
        this.uic = newUic;
    }

    public String getAllPocEmails() {
        if (pocList != null && !pocList.isEmpty()) {
            Set<String> pocEmailSet = new HashSet<>();
            for (UserBean poc : pocList) {
                String email = poc.getEmail();
                if (!StringUtils.isEmpty(email)) {
                    pocEmailSet.add(email);
                }
            }
            List<String> pocEmailList = new ArrayList<>(pocEmailSet);
            return StringUtils.delimitList(pocEmailList, ";");
        }
        return null;
    }
    
    public String getPrimaryPocEmails() {
        if (pocList != null && !pocList.isEmpty()) {
            Set<String> pocEmailSet = new HashSet<>();
            for (UserBean poc : pocList) {
                String email = poc.getEmail();
                if (!StringUtils.isEmpty(email) && poc.isPrimaryPoc()) {
                    pocEmailSet.add(email);
                }
            }
            List<String> pocEmailList = new ArrayList<>(pocEmailSet);
            return StringUtils.delimitList(pocEmailList, ";");
        }
        return null;
    }

}