package com.premiersolutionshi.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class DateUtils {
    public static final DateTimeFormatter COMMON_BASIC_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static final DateTimeFormatter COMMON_LONG_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm");
    public static final DateTimeFormatter SQLITE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter SQLITE_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter COMMON_BASIC_FORMAT_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public String getNowInBasicFormatDate() {
        return LocalDate.now().format(COMMON_BASIC_FORMAT_DATE);
    }
    
    
    public static String getNowInBasicFormat() {
        return LocalDate.now().format(COMMON_BASIC_FORMAT);
    }

    public static String formatToBasicFormat(LocalDate date) {
        return date == null ? null : date.format(COMMON_BASIC_FORMAT);
    }

    public static String formatToBasicFormat(LocalDateTime date) {
        return date == null ? null : date.format(COMMON_BASIC_FORMAT);
    }

    public static String formatToLongFormat(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        if (date.getHour() == 0 && date.getMinute() == 0 && date.getSecond() == 0) {
            return date.format(COMMON_BASIC_FORMAT);
        }
        return date.format(COMMON_LONG_FORMAT);
    }

    public static boolean equals(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return equals(date1.toLocalDate(), date2.toLocalDate())
            && date1.getHour() == date2.getHour()
            && date1.getMinute() == date2.getMinute()
            && date1.getSecond() == date2.getSecond()
        ;
    }

    public static boolean equalsToMinute(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return equals(date1.toLocalDate(), date2.toLocalDate())
            && date1.getHour() == date2.getHour()
            && date1.getMinute() == date2.getMinute()
            ;
    }

    public static boolean equals(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getYear() == date2.getYear()
            && date1.getMonthValue() == date2.getMonthValue()
            && date1.getDayOfMonth() == date2.getDayOfMonth()
        ;
    }

    public static LocalDate parseBasicDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, COMMON_BASIC_FORMAT);
        }
        catch(DateTimeParseException e) {
            System.err.println("Could not parseBasicDate from dateString='" + dateString + "'");
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime parseLongDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateString, COMMON_LONG_FORMAT);
        }
        catch(DateTimeParseException e) {
            System.err.println("Could not parseLongDate from dateString='" + dateString + "'");
            e.printStackTrace();
        }
        return null;
    }

    public static String formatToPattern(LocalDate date, String pattern) {
        return formatToPattern(LocalDateTime.of(date, LocalTime.of(0, 0)), pattern);
    }

    public static String formatToPattern(LocalDateTime date, String pattern) {
        if (date == null || StringUtils.isEmpty(pattern)) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseSqliteDatetime(String datetimeString) {
        if (!StringUtils.isEmpty(datetimeString)) {
            //2017-07-18 06:34:49
            return LocalDateTime.parse(datetimeString, SQLITE_DATETIME_FORMAT);
        }
        return null;
    }

    public static LocalDate parseSqliteDate(String datetimeString) {
        if (!StringUtils.isEmpty(datetimeString)) {
            //2017-07-18 06:34:49
            return LocalDate.parse(datetimeString, SQLITE_DATE_FORMAT);
        }
        return null;
    }

    /**
     * Returns date in string formatted like this "2018-11-07 06:34:49"
     * @param date
     * @return Formatted Date String
     */
    public static String formatToSqliteDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return formatToSqliteDate(LocalDateTime.of(date, LocalTime.of(0, 0)), false);
    }
    public static String formatToSqliteDate(LocalDateTime date) {
        return formatToSqliteDate(date, false);
    }
    public static String formatToSqliteDatetime(LocalDateTime date) {
        return formatToSqliteDate(date, true);
    }

    /**
     * Returns date in string formatted like this "2018-11-07"
     * @param date
     * @param isDatetime 
     * @return Formatted Date String
     */
    public static String formatToSqliteDate(LocalDateTime date, boolean isDatetime) {
        if (date != null) {
            if (isDatetime) {
                return SQLITE_DATETIME_FORMAT.format(date);
            }
            else {
                return SQLITE_DATE_FORMAT.format(date);
            }
        }
        return null;
    }

    public static long daysDiff(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        return daysBetween;
    }

    public static long daysDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long startTime = date1.getTime();
        long endTime = date2.getTime();
        long diffTime = endTime - startTime;
        return diffTime / (1000 * 60 * 60 * 24);
    }

    public static LocalDate getLocalDateFromDate(Date date) {
        return LocalDate.from(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));
     }

    public static String getCurrDmsVersion() {
        return formatToPattern(LocalDate.now().minusMonths(1), "MMMM YYYY");
    }
}
