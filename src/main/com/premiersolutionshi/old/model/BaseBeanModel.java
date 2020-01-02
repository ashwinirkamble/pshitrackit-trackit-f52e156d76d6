package com.premiersolutionshi.old.model;

import org.apache.log4j.Logger;

public class BaseBeanModel extends BaseModel {
    protected static void debugLog(String type, String functionName, Exception e, Logger logger) {
        debugLog(type, functionName, e.toString(), logger);
    }
}
