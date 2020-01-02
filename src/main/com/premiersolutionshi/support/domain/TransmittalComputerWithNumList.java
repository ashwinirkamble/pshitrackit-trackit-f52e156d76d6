package com.premiersolutionshi.support.domain;

import java.util.ArrayList;
import java.util.List;

import com.premiersolutionshi.common.util.StringUtils;

/**
 * This contains the list of Transmittal Numbers and Missing Transmittal Numbers
 * to a given Computer Name.
 */
public class TransmittalComputerWithNumList {
    private static final String TRANSMITTAL_NUM_PAD_STR = "0";
    private static final int TRANSMITTAL_NUM_PAD_SIZE = 6;

    private String computerName;
    private List<Integer> numList;
    private List<Integer> missingTransmittals;

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public List<Integer> getNumList() {
        return numList;
    }

    public void setNumList(List<Integer> numList) {
        this.numList = numList;
    }

    public int getMissingCount() {
        List<Integer> missingTransmittalNums = getMissingTransmittalNums();
        return missingTransmittalNums == null ? 0 : missingTransmittalNums.size();
    }

    public List<Integer> getMissingTransmittalNums() {
        if (numList == null || numList.isEmpty() || numList.size() < 3) {
            return null;
        }
        if (missingTransmittals == null) {
            missingTransmittals = new ArrayList<>();
            int size = numList.size();
            for (int i = 0; i < size - 1; i++) {
                int predictedNum = numList.get(i) + 1;
                Integer nextTransNum = numList.get(i + 1);
                while (predictedNum != nextTransNum) {
                    missingTransmittals.add(predictedNum);
                    predictedNum++;
                }
            }
        }
        return missingTransmittals;
    }

    public ArrayList<String> getMissingTransmittalStrNums() {
        ArrayList<String> strList = new ArrayList<>();
        if (getMissingCount() > 0) {
            for (Integer transmittalNum : missingTransmittals) {
                strList.add(StringUtils.padLeft(transmittalNum, TRANSMITTAL_NUM_PAD_STR, TRANSMITTAL_NUM_PAD_SIZE));
            }
        }
        return strList;
    }


}
