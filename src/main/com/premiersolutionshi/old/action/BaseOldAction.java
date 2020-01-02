package com.premiersolutionshi.old.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.struts.action.Action;

public class BaseOldAction extends Action {
    protected NumberFormat nf1 = new DecimalFormat("0.0");
}
