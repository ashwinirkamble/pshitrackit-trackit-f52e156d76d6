package com.premiersolutionshi.common.domain;

import java.util.ArrayList;

public class PocGroup extends ModifiedDomain {
    private static final long serialVersionUID = -8119722523144662525L;
    private ArrayList<Poc> pocList;

    public String getPocEmails() {
        ArrayList<Poc> pocs = getPocList();
        if (pocs != null && !pocs.isEmpty()) {
            return buildPocEmailList(pocs);
        }
        return null;
    }

    public String getPrimaryPocEmails() {
        ArrayList<Poc> pocs = getPrimaryPocList();
        if (pocs != null && !pocs.isEmpty()) {
            return buildPocEmailList(pocs);
        }
        return null;
    }

    private String buildPocEmailList(ArrayList<Poc> pocs) {
        StringBuilder pocEmails = new StringBuilder();
        pocEmails.append(pocs.get(0).getEmail());
        int size = pocs.size();
        for (int i = 1; i < size; i++) {
            pocEmails.append("; ").append(pocs.get(i).getEmail());
        }
        return pocEmails.toString();
    }

    public ArrayList<Poc> getPocList() {
        return pocList;
    }

    public void setPocList(ArrayList<Poc> pocList) {
        this.pocList = pocList;
    }

    public ArrayList<Poc> getPrimaryPocList() {
        ArrayList<Poc> primaryPocList = new ArrayList<>();
        if (pocList != null && !pocList.isEmpty()) {
            primaryPocList = new ArrayList<>();
            for (Poc poc : pocList) {
                if (poc.isPrimaryPoc()) {
                    primaryPocList.add(poc);
                }
            }
        }
        return primaryPocList;
    }

}
