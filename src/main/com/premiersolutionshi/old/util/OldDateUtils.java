package com.premiersolutionshi.old.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This utility is to handle old Java 7 (and under) Date objects.
 */
public final class OldDateUtils {
    private static final String BASIC_DATE_PATTERN = "MM/dd/yyyy";
    private static final SimpleDateFormat BASIC_FORMAT = new SimpleDateFormat(BASIC_DATE_PATTERN);

    public static Date parseBasicDate(String dateStr) {
        try {
            return BASIC_FORMAT.parse(dateStr);
        }
        catch (ParseException e) {
            System.err.println("OldDateUtils | Could not prase dateStr='" + dateStr + "', pattern='" + BASIC_DATE_PATTERN + "'");
            e.printStackTrace();
        }
        return null;
    }

    public static java.sql.Date parseBasicDateToSqlDate(String dateStr) {
        Date date = parseBasicDate(dateStr);
        if (date != null) {
            return new java.sql.Date(date.getTime());
        }
        return null;
    }
}
