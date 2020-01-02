package com.premiersoluitionshi.support.dao;

import java.util.ArrayList;

import com.premiersoluitionshi.common.service.BaseMybatisTest;
import com.premiersolutionshi.support.domain.TransmittalComputerWithNumList;
import com.premiersolutionshi.support.service.TransmittalService;

public class TransmittalTest extends BaseMybatisTest {

    private TransmittalService transmittalService;
    public TransmittalTest() {
        transmittalService = new TransmittalService(getSqlSession());
    }

    public static void main(String[] args) {
        TransmittalTest test = new TransmittalTest();
        test.runTests();
    }

    public void runTests() {
        ArrayList<TransmittalComputerWithNumList> transCompNumList = transmittalService.getTransmittalComputerWithNumList();
        for (TransmittalComputerWithNumList transCompNum : transCompNumList) {
            //System.out.println("computerName=" + transCompNum.getComputerName() + ", numList=" + transCompNum.getNumList());
            System.out.println("computerName=" + transCompNum.getComputerName() + ", missingTransmittalNums=" + transCompNum.getMissingTransmittalNums());
        }
    }
}
