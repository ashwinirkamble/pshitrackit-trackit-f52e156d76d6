package com.premiersolutionshi.old.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.springframework.util.StringUtils;

import com.premiersolutionshi.common.util.ConfigUtils;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.model.EmailModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.UserModel;

/**
 * Common Utility Functions
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class CommonMethods implements Serializable {
    private static final long serialVersionUID = 000000001L;
    private static Logger logger = Logger.getLogger(CommonMethods.class.getSimpleName());
    private final static String SQLITE_DATE_FORMAT = "YYYY-MM-DD HH24:MI:SS";

    /****************************************************************************
     * Function: debugLog
     ****************************************************************************/
    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%22s%-30s | %-34s | %s", "", type, functionName, statement));
        } else if (type.equals("ERROR")) {
            logger.error(String.format("%22s%-30s | %-34s | %s", "", type, functionName, statement));
        } else {
            logger.debug(String.format("%22s%-30s | %-34s | %s", "", type, functionName, statement));
        }
    }

    /****************************************************************************
     * Function: appFinally
     * @throws EmailException 
     ****************************************************************************/
  
	public static void appFinally(Connection conn, String returnAction, LoginBean loginBean, int projectPk, HttpServletRequest request) throws EmailException {
        //Pages with 3 column displays
        String[] threeColPageArr = { "index", "projectList", "userList", "userAdd", "userEdit", "changePassword", "shipList", "shipAdd", "shipEdit", "ptoTravelList", "ptoTravelEdit", "gitLog", "gitShow" };
        String[] projectPages = {"/jsp/project/taskList.jsp",
            "/jsp/common/managedList/managedList.jsp",
            "/jsp/project/taskDetail.jsp",
            "/jsp/project/taskEdit.jsp",
            //"/jsp/hardwareSoftwareSummary.jsp", //deprecated
            "/jsp/software/kofaxLicenseList.jsp",
            "/jsp/software/kofaxLicenseEdit.jsp",
            "/jsp/software/bulkKofaxLicenseEdit.jsp",
            "/jsp/software/msOfficeLicenseList.jsp",
            "/jsp/software/msOfficeLicenseEdit.jsp",
            "/jsp/software/vrsLicenseList.jsp",
            "/jsp/software/vrsLicenseEdit.jsp",
            //"/jsp/miscSoftwareList.jsp", //doesn't exist
            //"/jsp/miscSoftwareEdit.jsp", //doesn't exist
            "/jsp/hardware/laptopList.jsp",
            "/jsp/hardware/laptopEdit.jsp",
            "/jsp/hardware/scannerList.jsp",
            "/jsp/hardware/scannerEdit.jsp",
            "/jsp/hardware/miscHardwareList.jsp",
            "/jsp/hardware/miscHardwareEdit.jsp",
            "/jsp/software/miscLicenseList.jsp",
            "/jsp/software/miscLicenseEdit.jsp",
            "/jsp/system/configuredSystemList.jsp",
            "/jsp/system/configuredSystemEdit.jsp",
            "/jsp/system/configuredSystemDetail.jsp",
            "/jsp/decom/decomWorkflowSummary.jsp",
            "/jsp/decom/decomWorkflowEdit.jsp",
            "/jsp/training/trainingWorkflowSummary.jsp",
            "/jsp/training/trainingWorkflowEdit.jsp",
            "/jsp/backfile/backfileWorkflowSummary.jsp",
            "/jsp/backfile/backfileWorkflowEdit.jsp",
            "/jsp/user/pocList.jsp",
            "/jsp/user/pocEdit.jsp",
            "/jsp/user/shipPocEdit.jsp",
            "/jsp/support/shipVisitCalendar.jsp",
            //"/jsp/workflowSummary.jsp", //doesn't exist
            //"/jsp/workflowEdit.jsp", //doesn't exist
            "/jsp/support/issueList.jsp",
            "/jsp/support/issueListAll.jsp",
            "/jsp/support/issueDetail.jsp",
            "/jsp/support/issueEdit.jsp",
            "/jsp/support/issueReports.jsp",
            "/jsp/support/supportGenerateFeedbackForm.jsp",
            "/jsp/support/bulkEmailTool.jsp",
            "/jsp/support/atoList.jsp",
            "/jsp/support/atoEdit.jsp",
            "/jsp/dts/dtsUpload.jsp",
            "/jsp/report/missingTransmittal.jsp",
            "/jsp/report/transmittalSummary.jsp",
            "/jsp/report/transmittalDetail.jsp",
            "/jsp/system/systemVariables.jsp"
        };

//            "/jsp/taskAgendaList.jsp",

        request.setAttribute("loginBean", loginBean);
        request.setAttribute("leftbar_projectList", ProjectModel.getProjectList(conn, loginBean));
        request.setAttribute("leftbar_projectPages", projectPages);
        /*ashwini 12/04*/
        request.setAttribute("currentPto_travelList",UserModel.getCurrentPtoTravelList(conn, loginBean,request));
        request.setAttribute("currentPto_travelListSize",UserModel.getCurrentPtoTravelList1(conn, loginBean,request).size());
        request.setAttribute("currentPto_travelList1",UserModel.getCurrentPtoTravelList1(conn, loginBean,request));


        ArrayList<UserBean> res = new ArrayList<UserBean>();
       
        res=UserModel.getCurrentPtoTravelList1(conn, loginBean,request);
        
        
        EmailModel emailModel= new EmailModel();
        String date;
        DateUtils dateUtils=new DateUtils();
        date=dateUtils.getNowInBasicFormatDate();
        
     
      /*  LocalTime localTime = LocalTime.now();
        System.out.println("Hour : " + localTime.getHour());
        System.out.println("Minute : " + localTime.getMinute());
        System.out.println("Second : " + localTime.getSecond());
        
            for (int i = 0; i < res.size(); i++) 
            {
            	String email=res.get(i).getEmail();
            	String type=res.get(i).getLeaveType();
                System.out.println("email="+res.get(i).getEmail());
                	if(isValidDateStr(res.get(i).getEndDate()) && localTime.getHour()>=12 && localTime.getMinute()>=10)
                	{ 
                	 if(res.get(i).getEndDate().equals(date) && type.equals("Travel"))
                			emailModel.sendMail(email);
                	}
        
        }
            ashwini 12/04

      */
        
      //  request.setAttribute("currentPto_travelListSize",UserModel.getCurrentPtoTravelList(conn, loginBean).size());

        if (projectPk != -1) {
            com.premiersolutionshi.old.bean.ProjectBean projectBean = ProjectModel.getProjectBean(conn, projectPk);
            request.setAttribute("projectPk", String.valueOf(projectPk));
            request.setAttribute("contentHeader_projectName", projectBean.getProjectName() + " (" + projectBean.getCustomer() + ")");
            request.setAttribute("contentHeader_projectPages", projectPages);
        }

        if (isIn(threeColPageArr, returnAction)) {
            request.setAttribute("rightbar_followUpTasks", ProjectModel.getUserFollowUpTaskList(conn, loginBean));
            request.setAttribute("rightbar_overdueTasks", ProjectModel.getUserTaskList(conn, -1, loginBean));
            request.setAttribute("rightbar_dueTasks", ProjectModel.getUserTaskList(conn, 0, loginBean));
            request.setAttribute("rightbar_pending7Tasks", ProjectModel.getUserTaskList(conn, 7, loginBean));
            request.setAttribute("rightbar_pending31Tasks", ProjectModel.getUserTaskList(conn, 31, loginBean));
        }

        /* content notification */
        String successMsg = nes(request.getAttribute("successMsg"));
        String warningMsg = nes(request.getAttribute("warningMsg"));
        String errorMsg = nes(request.getAttribute("errorMsg"));

        if (!isEmpty(successMsg)) {
            request.setAttribute("contentNotification_text", successMsg);
            request.setAttribute("contentNotification_icon", "images/icon_success.png");
            request.setAttribute("contentNotification_css", "success-msg");
        } else if (!isEmpty(warningMsg)) {
            request.setAttribute("contentNotification_text", warningMsg);
            request.setAttribute("contentNotification_icon", "images/icon_warning.png");
            request.setAttribute("contentNotification_css", "warning-msg");
        } else if (!isEmpty(errorMsg)) {
            request.setAttribute("contentNotification_text", errorMsg);
            request.setAttribute("contentNotification_icon", "images/icon_error.png");
            request.setAttribute("contentNotification_css", "error-msg");
        }
    }

    public static void mailConf(Connection conn, LoginBean loginBean,HttpServletRequest request)
    {
    	 ArrayList<UserBean> res = new ArrayList<UserBean>();
         
         res=UserModel.getCurrentPtoTravelList1(conn, loginBean,request);

         
         EmailModel emailModel;
		try {
			emailModel = new EmailModel();
		
         String date;
         DateUtils dateUtils=new DateUtils();
         date=dateUtils.getNowInBasicFormatDate();
         
         LocalTime localTime = LocalTime.now();
         System.out.println("Hour : " + localTime.getHour());
         System.out.println("Minute : " + localTime.getMinute());
         System.out.println("Second : " + localTime.getSecond());
         
             for (int i = 0; i < res.size(); i++) 
             {
             	String email=res.get(i).getEmail();
             	String type=res.get(i).getLeaveType();
                 System.out.println("email="+res.get(i).getEmail());
                 	if(isValidDateStr(res.get(i).getEndDate()) && localTime.getHour()>=12 && localTime.getMinute()>=10)
                 	{ 
                 	 if(res.get(i).getEndDate().equals(date) && type.equals("Travel"))
                 	 emailModel.sendMail(email);
                 	}
             }
     		} catch (EmailException e) {
     		
     			e.printStackTrace();
         }	
    }
    
    /****************************************************************************
     * Function: arrayIntersect
     * Description: Returns items that are in both Array A and Array B
     ****************************************************************************/
    public static String[] arrayIntersect(String[] strArrA, String[] strArrB) {
        ArrayList<String> resultList = new ArrayList<String>();

        String[] sortedArr = new String[strArrA.length];
        System.arraycopy(strArrA, 0, sortedArr, 0, strArrA.length);
        Arrays.sort(sortedArr);

        for (String tStr : strArrB) {
            if (Arrays.binarySearch(sortedArr, tStr) >= 0) {
                resultList.add(tStr);
            }
        }

        return arraySort((String[]) resultList.toArray(new String[0]));
    }

    /****************************************************************************
     * Function: arraySort
     ****************************************************************************/
    public static String[] arraySort(String[] strArr) {
        Arrays.sort(strArr);
        return strArr;
    }

    public static ArrayList<String> arraySort(ArrayList<String> resultList) {
        return new ArrayList<String>(Arrays.asList(arraySort((String[])resultList.toArray(new String[0]))));
    }

    /****************************************************************************
     * Function: arraySubtract
     * Description: Returns items that are in Array A but not in Array B
     ****************************************************************************/
    public static String[] arraySubtract(String[] strArrA, String[] strArrB) {
        ArrayList<String> resultList = new ArrayList<String>();

        String[] sortedArr = new String[strArrB.length];
        System.arraycopy(strArrB, 0, sortedArr, 0, strArrB.length);
        Arrays.sort(sortedArr);

        for (String tStr : strArrA) {
            if (Arrays.binarySearch(sortedArr, tStr) < 0) {
                resultList.add(tStr);
            }
        }

        return arraySort((String[]) resultList.toArray(new String[0]));
    }

    /****************************************************************************
     * Function: cInt
     * Description: Returns -1 if given input cannot be converted to an int
     ****************************************************************************/
    public static int cInt(Object tData) {
        return cInt(nes(tData));
    }

    public static int cInt(Object tData, int falseValue) {
        return cInt(nes(tData), falseValue);
    }

    public static int cInt(String tStr) {
        return cInt(tStr, -1);
    }

    public static int cInt(String tStr, int falseValue) {
        int cInt = falseValue;
        try {
            cInt = Integer.parseInt(tStr);
        } catch (NumberFormatException nfe) {
            cInt = falseValue;
        }
        return cInt;
    }

    /****************************************************************************
     * Function: cDbl
     * Description: Returns -1 if given input cannot be converted to an double
     ****************************************************************************/
    public static double cDbl(Object tData) {
        return cDbl(nes(tData));
    }

    public static double cDbl(Object tData, double falseValue) {
        return cDbl(nes(tData), falseValue);
    }

    public static double cDbl(String tStr) {
        return cDbl(tStr, -1);
    }

    public static double cDbl(String tStr, double falseValue) {
        double cDbl = falseValue;
        try {
            cDbl = Double.parseDouble(tStr);
        } catch (NumberFormatException nfe) {
            cDbl = falseValue;
        }
        return cDbl;
    }

    /****************************************************************************
     * Function: decode
     ****************************************************************************/
    public static String decode(Object obj, String... args) {
        return decode(nes(obj), args);
    }

    public static String decode(String tStr, String... args) {
        String resultStr = (args.length % 2 == 0 ? tStr : args[args.length - 1]);
        for (int i = 1; i < args.length; i += 2) {
            if (tStr != null) {
                resultStr = (tStr.equals(args[i - 1]) ? args[i] : resultStr);
            } else if (args[i - 1] == null) {
                resultStr = args[i];
            }
        }
        return resultStr;
    }

    /****************************************************************************
     * Function: dedupeArrayList
     ****************************************************************************/
    public static ArrayList<String> dedupeArrayList(ArrayList<String> resultList) {
        int i = 0;
        while (i < resultList.size() - 1) {
            int k = i + 1;
            while (k < resultList.size()) {
                if (nes(resultList.get(i)).equals(nes(resultList.get(k)))) {
                    resultList.remove(k);
                } else {
                    k++;
                }
            }
            i++;
        }
        return resultList;
    }

    /****************************************************************************
     * Function: formatYear
     * Description: Returns a year value in 4-digit format
     ****************************************************************************/
    public static String formatYear(String yearStr) {
        String dateFormat = null;
        if (yearStr == null || yearStr.trim().length() == 0 || yearStr.equalsIgnoreCase("null")) {
            dateFormat = "";
        } else {
            int iYear = cInt(yearStr);
            if ((iYear >= 80) && (iYear <= 99)) {
                dateFormat = "19" + String.valueOf(iYear);
            } else if ((iYear >= 100) && (iYear <= 999)) {
                dateFormat = "2" + String.valueOf(iYear);
            } else if ((iYear >= 10) && (iYear <= 79)) {
                dateFormat = "20" + String.valueOf(iYear);
            } else if ((iYear >= 0) && (iYear <= 9)) {
                dateFormat = "200" + String.valueOf(iYear);
            } else {
                dateFormat = String.valueOf(iYear);
            }
        }
        return dateFormat;
    }

    public static Connection getConn(ActionServlet servlet) throws SQLException {
        return getConn(servlet, "jdbc/pshi");
    }

    public static Connection getConn(ActionServlet servlet, String key) throws SQLException {
        Connection conn = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup(key);
            conn = ds.getConnection();
        } catch (SQLException e) {
            debugLog("ERROR", "getConn[" + key + "]", e);
            throw new SQLException();
        } catch (Exception e) {
            debugLog("ERROR", "getConn[" + key + "]", e);
            throw new SQLException();
        }
        return conn; 
    }

    /****************************************************************************
     * Description: Gets today's date in the specified input format
     ****************************************************************************/
    public static String getDate() {
        return getDate("MM/DD/YY");
    }

    public static String getSqliteDate() {
        return getDate(SQLITE_DATE_FORMAT);
    }

    public static String getDate(long millis, String dateFormat) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(millis);
        return getDate(cal, dateFormat);
    }

    public static String getSqlDate() {
        GregorianCalendar cal = new GregorianCalendar();
        return getDate(cal, "YYYY-MM-DD HH24:MI:SS");
    }

    public static String getDate(String dateFormat) {
        GregorianCalendar cal = new GregorianCalendar();
        return getDate(cal, dateFormat);
    }

    public static String getNow() {
        GregorianCalendar cal = new GregorianCalendar();
        return getDate(cal, SQLITE_DATE_FORMAT);
    }

    public static String getDate(String dateFormat, int daysAdjust) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, daysAdjust);
        return getDate(cal, dateFormat);
    }

    public static String getDate(String dateStr, String dateFormat) {
        return getDate(dateStr, dateFormat, 0);
    }

    public static String getDate(String dateStr, String dateFormat, int daysAdjust) {
        if (!isValidDateStr(dateStr)) {
            return "-1";
        } else {
            int slash1 = dateStr.indexOf("/");
            int slash2 = dateStr.indexOf("/", slash1 + 1);

            int iMonth = Integer.parseInt(dateStr.substring(0, slash1)) - 1;
            int iDate = Integer.parseInt(dateStr.substring(slash1 + 1, slash2));
            int iYear = Integer.parseInt(formatYear(dateStr.substring(slash2 + 1)));

            GregorianCalendar cal = new GregorianCalendar(iYear, iMonth, iDate);
            cal.setLenient(true);
            cal.add(Calendar.DAY_OF_YEAR, daysAdjust);
            return getDate(cal, dateFormat);
        }
    }

    public static String getDate(GregorianCalendar cal, String dateFormat) {
        dateFormat = dateFormat.toUpperCase();
        dateFormat = dateFormat.replaceAll("MM", padString((cal.get(Calendar.MONTH) + 1), "0", 2));
        dateFormat = dateFormat.replaceAll("YYYY", String.valueOf(cal.get(Calendar.YEAR)));
        dateFormat = dateFormat.replaceAll("YY", String.valueOf(cal.get(Calendar.YEAR)).substring(2));
        dateFormat = dateFormat.replaceAll("HH24", padString((cal.get(Calendar.HOUR_OF_DAY)), "0", 2));
        dateFormat = dateFormat.replaceAll("HH12", padString((cal.get(Calendar.HOUR)), "0", 2));
        dateFormat = dateFormat.replaceAll("MI", padString((cal.get(Calendar.MINUTE)), "0", 2));
        dateFormat = dateFormat.replaceAll("SS", padString((cal.get(Calendar.SECOND)), "0", 2));
        dateFormat = dateFormat.replaceAll("AMPM", getAmPmName(cal.get(Calendar.AM_PM)));
        dateFormat = dateFormat.replaceAll("AM", getAmPmName(cal.get(Calendar.AM_PM)));
        dateFormat = dateFormat.replaceAll("PM", getAmPmName(cal.get(Calendar.AM_PM)));
        dateFormat = dateFormat.replaceAll("MONTH", getMonthName(cal.get(Calendar.MONTH)));
        dateFormat = dateFormat.replaceAll("MON", getMonthNameShort(cal.get(Calendar.MONTH)).toUpperCase());
        dateFormat = dateFormat.replaceAll("DOW#", String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));
        dateFormat = dateFormat.replaceAll("DOW", getDayOfWeekName(cal.get(Calendar.DAY_OF_WEEK)));
        dateFormat = dateFormat.replaceAll("DW", getDayOfWeekNameShort(cal.get(Calendar.DAY_OF_WEEK)).toUpperCase());
        dateFormat = dateFormat.replaceAll("DD", padString((cal.get(Calendar.DAY_OF_MONTH)), "0", 2));
        dateFormat = dateFormat.replaceAll("MAX", String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
        dateFormat = dateFormat.replaceAll("MS", padString((cal.get(Calendar.MILLISECOND)), "0", 2));
        dateFormat = dateFormat.replaceAll("WOM", String.valueOf(cal.getActualMaximum(Calendar.WEEK_OF_MONTH)));
        return dateFormat;
    }

    /****************************************************************************
     * Function: dateDiff
     * Description: Gets the number of days between 2 dates
     ****************************************************************************/
    public static int dateDiff(String dateStr1, String dateStr2) {
        if (!isValidDateStr(dateStr1) || !isValidDateStr(dateStr2)) {
            return -999999;
        } else {
            int slash1_1 = dateStr1.indexOf("/");
            int slash1_2 = dateStr1.indexOf("/", slash1_1 + 1);

            int iMonth1 = Integer.parseInt(dateStr1.substring(0, slash1_1)) - 1;
            int iDate1 = Integer.parseInt(dateStr1.substring(slash1_1 + 1, slash1_2));
            int iYear1 = Integer.parseInt(formatYear(dateStr1.substring(slash1_2 + 1)));

            GregorianCalendar cal1 = new GregorianCalendar(iYear1, iMonth1, iDate1);

            int slash2_1 = dateStr2.indexOf("/");
            int slash2_2 = dateStr2.indexOf("/", slash2_1 + 1);

            int iMonth2 = Integer.parseInt(dateStr2.substring(0, slash2_1)) - 1;
            int iDate2 = Integer.parseInt(dateStr2.substring(slash2_1 + 1, slash2_2));
            int iYear2 = Integer.parseInt(formatYear(dateStr2.substring(slash2_2 + 1)));

            GregorianCalendar cal2 = new GregorianCalendar(iYear2, iMonth2, iDate2);

            return (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 86400000);
        }
    }

    /****************************************************************************
     * Description: Gets the number of days in a given month
     ****************************************************************************/
    public static int getDaysInMonth(String dateStr) {
        if (!isValidDateStr(dateStr)) {
            return -999;
        } else {
            int slash1 = dateStr.indexOf("/");
            int slash2 = dateStr.indexOf("/", slash1 + 1);

            int iMonth = Integer.parseInt(dateStr.substring(0, slash1)) - 1;
            int iDate = Integer.parseInt(dateStr.substring(slash1 + 1, slash2));
            int iYear = Integer.parseInt(formatYear(dateStr.substring(slash2 + 1)));

            GregorianCalendar cal = new GregorianCalendar(iYear, iMonth, iDate);

            return (int) cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
    }

    /****************************************************************************
     * Description: Converts 0 to AM and 1 to PM
     ****************************************************************************/
    public static String getAmPmName(Object obj) {
        return getAmPmName(cInt(obj));
    }

    public static String getAmPmName(int amPmCode) {
        return amPmCode == Calendar.AM ? "AM" : amPmCode == Calendar.PM ? "PM" : "";
    }

    /****************************************************************************
     * Description: Returns the name of the month based on the month number
     ****************************************************************************/
    public static String getMonthNameShort(String monthNumber) {
        return getMonthNameShort(cInt(monthNumber));
    }

    public static String getMonthNameShort(int monthNumber) {
        String monthName = getMonthName(monthNumber);
        if (monthName.length() > 3) {
            return monthName.substring(0, 3);
        } else {
            return monthName;
        }
    }

    public static String getMonthName(String monthNumber) {
        return getMonthName(cInt(monthNumber));
    }

    public static String getMonthName(int monthNumber) {
        return monthNumber == Calendar.JANUARY ? "January" :
               monthNumber == Calendar.FEBRUARY ? "February" :
               monthNumber == Calendar.MARCH ? "March" :
               monthNumber == Calendar.APRIL ? "April" :
               monthNumber == Calendar.MAY ? "May" :
               monthNumber == Calendar.JUNE ? "June" :
               monthNumber == Calendar.JULY ? "July" :
               monthNumber == Calendar.AUGUST ? "August" :
               monthNumber == Calendar.SEPTEMBER ? "September" :
               monthNumber == Calendar.OCTOBER ? "October" :
               monthNumber == Calendar.NOVEMBER ? "November" :
               monthNumber == Calendar.DECEMBER ? "December" :
               monthNumber == Calendar.UNDECIMBER ? "Undecimber"
               : "";
    }

    /****************************************************************************
     * Description: Returns the month number of the given month as an Integer
     ****************************************************************************/
    public static int getMonthNumber(String monthLabel) {
        return monthLabel.equalsIgnoreCase("January") || monthLabel.equalsIgnoreCase("Jan") ? Calendar.JANUARY :
              monthLabel.equalsIgnoreCase("February") || monthLabel.equalsIgnoreCase("Feb") ? Calendar.FEBRUARY :
              monthLabel.equalsIgnoreCase("March") || monthLabel.equalsIgnoreCase("Mar") ? Calendar.MARCH :
              monthLabel.equalsIgnoreCase("April") || monthLabel.equalsIgnoreCase("Apr") ? Calendar.APRIL :
              monthLabel.equalsIgnoreCase("May") ? Calendar.MAY :
              monthLabel.equalsIgnoreCase("June") || monthLabel.equalsIgnoreCase("Jun") ? Calendar.JUNE :
              monthLabel.equalsIgnoreCase("July") || monthLabel.equalsIgnoreCase("Jul") ? Calendar.JULY :
              monthLabel.equalsIgnoreCase("August") || monthLabel.equalsIgnoreCase("Aug") ? Calendar.AUGUST :
              monthLabel.equalsIgnoreCase("September") || monthLabel.equalsIgnoreCase("Sep") || monthLabel.equalsIgnoreCase("Sept") ? Calendar.SEPTEMBER :
              monthLabel.equalsIgnoreCase("October") || monthLabel.equalsIgnoreCase("Oct") ? Calendar.OCTOBER :
              monthLabel.equalsIgnoreCase("November") || monthLabel.equalsIgnoreCase("Nov") ? Calendar.NOVEMBER :
              monthLabel.equalsIgnoreCase("December") || monthLabel.equalsIgnoreCase("Dec") ? Calendar.DECEMBER :
              monthLabel.equalsIgnoreCase("Undecimber") || monthLabel.equalsIgnoreCase("Und") ? Calendar.UNDECIMBER : -1;
    }

    /****************************************************************************
     * Description: Returns the name of the day of the week
     ****************************************************************************/
    public static String getDayOfWeekNameShort(String dayOfWeekNumber) {
        return getDayOfWeekNameShort(cInt(dayOfWeekNumber));
    }

    public static String getDayOfWeekNameShort(int dayOfWeekNumber) {
        String dayOfWeekName = getDayOfWeekName(dayOfWeekNumber);
        if (dayOfWeekName.length() > 3) {
            return dayOfWeekName.substring(0, 3);
        } else {
            return dayOfWeekName;
        }
    }

    public static String getDayOfWeekName(int dayOfWeekNumber) {
        return dayOfWeekNumber == Calendar.SUNDAY ? "Sunday" :
               dayOfWeekNumber == Calendar.MONDAY ? "Monday" :
               dayOfWeekNumber == Calendar.TUESDAY ? "Tuesday" :
               dayOfWeekNumber == Calendar.WEDNESDAY ? "Wednesday" :
               dayOfWeekNumber == Calendar.THURSDAY ? "Thursday" :
               dayOfWeekNumber == Calendar.FRIDAY ? "Friday" :
               dayOfWeekNumber == Calendar.SATURDAY ? "Saturday" :
               "";
    }

    /****************************************************************************
     * Function: getUploadDir
     ****************************************************************************/
    public static String getUploadDir(HttpServletRequest request) {
        return nes(request.getSession().getServletContext().getInitParameter("fileDirectory"));
    }

    /****************************************************************************
     * Description: Returns true if IS_PROD configuration is set to false.
     ****************************************************************************/
    public static boolean isDevEnv() {
        Properties properties = ConfigUtils.getConfigProperties();
        String isProdProp = properties.getProperty("IS_PROD");
        return StringUtils.isEmpty(isProdProp) || isProdProp.equals("false");
    }

    /****************************************************************************
     * Function: isEmpty
     * Description: Returns true if String is empty, false if not
     ****************************************************************************/
    public static boolean isEmpty(Object tData) {
        return (tData == null ? true : isEmpty(String.valueOf(tData)));
    }

    public static boolean isEmpty(Character tData) {
        return (tData == null);
    }

    public static boolean isEmpty(String tData) {
        return tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null");
    }

    public static boolean isEmpty(StringBuffer tData) {
        return tData == null || tData.toString().trim().length() == 0 || tData.toString().equalsIgnoreCase("null");
    }

    /****************************************************************************
     * Function: isFileExist
     ****************************************************************************/
    public static boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /****************************************************************************
     * Function: isIn
     ****************************************************************************/
    public static boolean isIn(String strList, String strKey) {
        return (strList == null || strKey == null ? false : strList.indexOf(strKey) > -1);
    }

    public static boolean isIn(int[] iArr, int key) {
        boolean found = false;

        if (iArr != null) {
            int[] sortedArr = new int[iArr.length];
            System.arraycopy(iArr, 0, sortedArr, 0, iArr.length);
            Arrays.sort(sortedArr);
            found = (Arrays.binarySearch(sortedArr, key) >= 0);
        }

        return found;
    }

    public static boolean isIn(ArrayList<String> arrList, String strKey) {
        return (arrList == null || strKey == null ? false : isIn((String[]) arrList.toArray(new String[0]), strKey));
    }

    public static boolean isIn(String[] strArr, int strKey) {
        return isIn(strArr, String.valueOf(strKey));
    }

    public static boolean isIn(String[] strArr, String strKey) {
        boolean found = false;

        if (strArr != null && strKey != null) {
            String[] sortedArr = new String[strArr.length];
            System.arraycopy(strArr, 0, sortedArr, 0, strArr.length);
            Arrays.sort(sortedArr);
            found = (Arrays.binarySearch(sortedArr, strKey) >= 0);
        }

        return found;
    }

    public static String isIn(String strList, String strKey, String trueValue) {
        return isIn(strList, strKey, trueValue, "");
    }

    public static String isIn(String strList, String strKey, String trueValue, String falseValue) {
        return (!isEmpty(strList) && !isEmpty(strKey) && strList.indexOf(strKey) > -1 ? trueValue : falseValue);
    }

    public static String isIn(String strList, char chrKey, String trueValue) {
        return isIn(strList, chrKey, trueValue, "");
    }

    public static String isIn(String strList, char chrKey, String trueValue, String falseValue) {
        return (!isEmpty(strList) && strList.indexOf(chrKey) > -1 ? trueValue : falseValue);
    }

    /****************************************************************************
     * Function: isNan
     * Description: Returns false if given String can be converted to a number
     ****************************************************************************/
    public static boolean isNan(String tStr) {
        boolean isNan = true;
        try {
            Double.parseDouble(tStr);
            isNan = false;
        } catch (Exception e) {
            isNan = true;
        }
        return isNan;
    }

    /****************************************************************************
     * Function: isValidDateStr
     * Description: Validates input string as a valid date
     * Input Format: MM/DD/YY or MM/DD/YYYY
     ****************************************************************************/
    public static boolean isValidDateStr(String dateStr) {
        boolean validDate = false;

        if (dateStr == null) return false;

        int slash1 = dateStr.indexOf("/");
        int slash2 = dateStr.indexOf("/", slash1 + 1);

        if (dateStr.length() >= 5 && slash1 > -1 && slash2 > -1) {
            try {
                int iMonth = Integer.parseInt(dateStr.substring(0, slash1)) - 1;
                int iDate = Integer.parseInt(dateStr.substring(slash1 + 1, slash2));
                int iYear = Integer.parseInt(formatYear(dateStr.substring(slash2 + 1)));

                Calendar cal = new GregorianCalendar();
                cal.setLenient(false);

                cal.set(iYear, iMonth, iDate);
                cal.get(Calendar.MONTH);
                cal.get(Calendar.DAY_OF_MONTH);
                cal.get(Calendar.YEAR);

                validDate = true;
            } catch (Exception e) {
                validDate = false;
            }
        }

        return validDate;
    }

    /****************************************************************************
     * Function: nes "Null to Empty String"
     * Description: Converts null values to empty string
     ****************************************************************************/
    public static String nes(Object tData) {
        return (isEmpty(tData) ? "" : String.valueOf(tData));
    }

    /**
     * If Null, return Empty String
     * @param str
     * @return Empty string if it is null.
     */
    public static String nes(String str) {
        return (isEmpty(str) ? "" : str);
    }

    /****************************************************************************
     * Function: nvl "Null Value"
     * Description: Returns nullValue if value is null, otherwise returns value
     ****************************************************************************/
    public static String nvl(String value, String nullValue) {
        return nvl(value, nullValue, value);
    }

    public static String nvl(String value, Object nullValue) {
        return nvl(value, nes(nullValue), value);
    }

    public static String nvl(String value, int nullValue) {
        return nvl(value, String.valueOf(nullValue), value);
    }

    public static String nvl(Object value, String nullValue) {
        return nvl(nes(value), nullValue, nes(value));
    }

    public static String nvl(Object value, Object nullValue) {
        return nvl(nes(value), nes(nullValue), nes(value));
    }

    public static String nvl(Object value, int nullValue) {
        return nvl(nes(value), String.valueOf(nullValue), nes(value));
    }

    public static String nvl(String value, String nullValue, String otherValue) {
        return (isEmpty(value) ? nullValue : otherValue);
    }

    /**
     * Pads a string with leading or trailing characters
     */
    public static String padString(Object tData, String padChar, int dataLength) {
        return padString(nes(tData), padChar, dataLength);
    }

    public static String padString(int cInt, String padChar, int dataLength) {
        return padString(String.valueOf(cInt), padChar, dataLength);
    }

    public static String padString(String tStr, String padChar, int dataLength) {
        tStr = nes(tStr);
        if (tStr.length() > Math.abs(dataLength)) {
            tStr = tStr.substring(0, Math.abs(dataLength));
        } else if (dataLength < 0) {
            for (int i = tStr.length(); i < Math.abs(dataLength); i++) {
                tStr = tStr + padChar;
            }
        } else {
            for (int i = tStr.length(); i < Math.abs(dataLength); i++) {
                tStr = padChar + tStr;
            }
        }
        return tStr;
    }

    /****************************************************************************
     * Function: printArray
     ****************************************************************************/
    public static String printArray(String[] strArr) {
        return printArray(strArr, ", ", "\"");
    }

    public static String printArray(String[] strArr, String delimiter, String prefix) {
        StringBuffer returnStr = new StringBuffer();
        if (strArr != null) for (String str : strArr) returnStr.append((returnStr.length() > 0 ? delimiter : "") + prefix + str + prefix);
        return returnStr.toString();
    }

    public static String printArray(ArrayList<String> strList) {
        return printArray(strList, ", ", "\"");
    }

    public static String printArray(ArrayList<String> strList, String delimiter, String prefix) {
        StringBuffer returnStr = new StringBuffer();
        if (strList != null) for (String str : strList) returnStr.append((returnStr.length() > 0 ? delimiter : "") + prefix + str + prefix);
        return returnStr.toString();
    }

    /****************************************************************************
     * Function: trim:all
     * Description: Removes all whitespaces
     ****************************************************************************/
    public static String atrim(String tStr) {
        return tStr.replaceAll("\\s", "");
    }

    /****************************************************************************
     * Function: trim:left
     * Description: Removes leading whitespace
     ****************************************************************************/
    public static String ltrim(String tStr) {
        return tStr.replaceAll("^\\s+", "");
    }

    /****************************************************************************
     * Function: trim:inner
     * Description: Removes multiple whitespaces between words
     ****************************************************************************/
    public static String itrim(String tStr) {
        return tStr.replaceAll("\\s{2,}", " ");
    }

    /****************************************************************************
     * Function: trim:right
     * Description: Removes trailing whitespace
     ****************************************************************************/
    public static String rtrim(String tStr) {
        return tStr.replaceAll("\\s+$", "");
    }

    /****************************************************************************
     * Function: trim
     * Description: Removes leading and trailing whitespace
     ****************************************************************************/
    public static String trim(String tStr) {
        return tStr.trim();
    }

    /****************************************************************************
     * Function: round
     ****************************************************************************/
    public static double round(double r, int decimalPlace) {
        int temp = (int) Math.round(r * (double) Math.pow(10, decimalPlace));
        return ((double) Math.round(temp)) / Math.pow(10, decimalPlace);
    }

    /****************************************************************************
     * Function: writeBinaryFile
     * Description: Writes a byte array to the default upload directory
     ****************************************************************************/
    public static boolean writeBinaryFile(byte[] contents, String directory, String filename) {
        boolean ranOk = true;
        BufferedOutputStream out = null;

        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                debugLog("INFO", "writeBinaryFile", "Creating directory: " + dir.getPath());
                dir.mkdir();
            }

            out = new BufferedOutputStream(new FileOutputStream(directory + "\\" + filename));
            out.write(contents);
            debugLog("INFO", "writeBinaryFile", "Successfully wrote " + directory + "\\" + filename + " to disk");
        } catch (Exception e) {
            debugLog("ERROR", "writeBinaryFile", directory + "\\" + filename + ": " + e.toString());
            ranOk = false;
        } finally {
            if (out != null) try { out.flush();out.close(); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean writeBinaryFile(InputStream in, String directory, String filename) throws Exception {
        boolean ranOk = true;
        String filepath = directory + "\\" + filename;

        //Validate directory
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                debugLog("INFO", "writeBinaryFile", "Creating directory: " + dir.getPath());
                dir.mkdir();
            }
        } catch (Exception e) {
            throw e;
        }

        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filepath))) {
            byte[] byteArr = new byte[512 * 1024]; // 512 KB buffer
            int byteRead = 0;
            double totalRead = 0;
            while ((byteRead = in.read(byteArr)) != -1) {
                out.write(byteArr, 0, byteRead);
                totalRead += byteRead;
            }
            debugLog("INFO", "writeBinaryFile", filepath + " successfully written to disk. Bytes read: " + totalRead + " bytes.");
        } catch (Exception e) {
            debugLog("ERROR", "writeBinaryFile", filepath + ": " + e.toString());
            throw e;
        }

        return ranOk;
    }

    /****************************************************************************
     * Function: deleteBinaryFile
     * Description: Deletes the file from the hard drive
     ****************************************************************************/
    public static boolean deleteBinaryFile(String filename, String directory) {
        boolean ranOk = true;
        try {
            logger.debug("deleting: " + directory + "\\" + filename);
            File file = new File(directory + "\\" + filename);
            ranOk = file.delete();
            logger.debug("ranOk: " + ranOk);
        } catch (Exception e) {
            debugLog("ERROR", "deleteBinaryFile", e);
            ranOk = false;
        }
        return ranOk;
    }

    public static String getLargeIcon(String extension) {
        return getFileIcon(extension, "lrg");
    }

    public static String getSmallIcon(String extension) {
        return getFileIcon(extension, "sml");
    }

    /****************************************************************************
     * Description: Gets the icon from the file extension
     ****************************************************************************/
    public static String getFileIcon(String extension, String iconSize) {
        String returnStr = null;

        if (iconSize == "sml") {
            switch (extension.toLowerCase()) {
                case "csv"  : returnStr = "images/file_icons/sml_file_csv.gif"; break;
                case "doc"  : case "docx" : returnStr = "images/file_icons/sml_file_doc.gif"; break;
                case "gif"  : returnStr = "images/file_icons/sml_file_gif.png"; break;
                case "htm"  : case "html" : returnStr = "images/file_icons/sml_file_html.gif"; break;
                case "jpg"  : case "jpeg" : returnStr = "images/file_icons/sml_file_jpg.png"; break;
                case "mdb"  : case "accdb" : returnStr = "images/file_icons/sml_file_mdb.gif"; break;
                case "msg"  : returnStr = "images/file_icons/sml_file_msg.png"; break;
                case "pdf"  : returnStr = "images/file_icons/sml_file_pdf.gif"; break;
                case "png"  : returnStr = "images/file_icons/sml_file_png.png"; break;
                case "ppt"  : case "pptx" : returnStr = "images/file_icons/sml_file_ppt.gif"; break;
                case "txt"  : returnStr = "images/file_icons/sml_file_txt.gif"; break;
                case "xls"  : case "xlsx" : returnStr = "images/file_icons/sml_file_xls.gif"; break;
                case "zip"  : returnStr = "images/file_icons/sml_file_zip.gif"; break;
                default     : returnStr = "images/file_icons/sml_file_binary.gif"; break;
            }
        } else if (iconSize == "lrg") {
            switch (extension.toLowerCase()) {
                case "csv"  : returnStr = "images/file_icons/file_csv.png"; break;
                case "doc"  : case "docx" : returnStr = "images/file_icons/file_doc.png"; break;
                case "gif"  : returnStr = "images/file_icons/file_gif.png"; break;
                case "htm"  : case "html" : returnStr = "images/file_icons/file_html.png"; break;
                case "jpg"  : case "jpeg" : returnStr = "images/file_icons/file_jpg.png"; break;
                case "mdb"  : case "accdb" : returnStr = "images/file_icons/file_mdb.png"; break;
                case "msg"  : returnStr = "images/file_icons/file_msg.png"; break;
                case "pdf"  : returnStr = "images/file_icons/file_pdf.png"; break;
                case "png"  : returnStr = "images/file_icons/file_png.png"; break;
                case "ppt"  : case "pptx" : returnStr = "images/file_icons/file_ppt.png"; break;
                case "txt"  : returnStr = "images/file_icons/file_txt.png"; break;
                case "xls"  : case "xlsx" : returnStr = "images/file_icons/file_xls.png"; break;
                case "zip"  : returnStr = "images/file_icons/file_zip.png"; break;
                default     : returnStr = "images/file_icons/file_binary.png"; break;
            }
        }

        return nes(returnStr);
    }

    /****************************************************************************
     * Function: updateString
     ****************************************************************************/
    public static void updateString(ResultSet rs, String field, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                rs.updateString(field, value);
            } else {
                rs.updateNull(field);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: updateInt
     ****************************************************************************/
    public static void updateInt(ResultSet rs, String field, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                rs.updateInt(field, cInt(value));
            } else {
                rs.updateNull(field);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void updateInt(ResultSet rs, String field, int value) throws SQLException {
        try {
            rs.updateInt(field, value);
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: updateDouble
     ****************************************************************************/
    public static void updateDouble(ResultSet rs, String field, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                rs.updateDouble(field, cDbl(value));
            } else {
                rs.updateNull(field);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void updateDouble(ResultSet rs, String field, double value) throws SQLException {
        try {
            rs.updateDouble(field, value);
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: updateDate
     ****************************************************************************/
    public static void updateDate(ResultSet rs, String field, String month, String day, String year) throws SQLException {
        try {
            if (!isEmpty(month) && !isEmpty(day) && !isEmpty(year)) {
                Calendar cal = new GregorianCalendar(cInt(year), cInt(month) - 1, cInt(day));
                rs.updateDate(field, new java.sql.Date(cal.getTimeInMillis()));
            } else {
                rs.updateNull(field);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void updateDate(ResultSet rs, String field, String value) throws Exception {
        SimpleDateFormat sDateformatter = null;

        try {
            if (value.indexOf("/") > -1 && isValidDateStr(value)) {
                sDateformatter = new SimpleDateFormat("MM/dd/yyyy");
                rs.updateDate(field, new java.sql.Date(sDateformatter.parse(value).getTime()));
            } else {
                rs.updateNull(field);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: setString
     ****************************************************************************/
    public static void setString(PreparedStatement pStmt, int col, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                pStmt.setString(col, value);
            } else {
                pStmt.setNull(col, java.sql.Types.VARCHAR);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void setString(PreparedStatement pStmt, int col, String value, int limit) throws SQLException {
        try {
            if (isEmpty(value)) {
                pStmt.setNull(col, java.sql.Types.VARCHAR);
            } else if (value.length() > limit) {
                pStmt.setString(col, value.substring(0, limit));
            } else {
                pStmt.setString(col, value);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: setInt
     ****************************************************************************/
    public static void setInt(PreparedStatement pStmt, int col, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                pStmt.setInt(col, cInt(value));
            } else {
                pStmt.setNull(col, java.sql.Types.NUMERIC);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: setDouble
     ****************************************************************************/
    public static void setDouble(PreparedStatement pStmt, int col, String value) throws SQLException {
        try {
            if (!isEmpty(value)) {
                pStmt.setDouble(col, cDbl(value));
            } else {
                pStmt.setNull(col, java.sql.Types.NUMERIC);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /****************************************************************************
     * Function: setDate
     ****************************************************************************/
    public static void setDate(PreparedStatement pStmt, int col, String value) throws Exception {
        try {
            if (!isEmpty(value) && isValidDateStr(value)) {
                pStmt.setString(col, getDate(value, "YYYY-MM-DD"));
            } else {
                pStmt.setNull(col, java.sql.Types.DATE);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void setDate(PreparedStatement pStmt, int col, int month, int day, int year) throws SQLException {
        try {
            if (month > 0 && day > 0 && year > 1900) {
                Calendar cal = new GregorianCalendar(year, month - 1, day);
                pStmt.setDate(col, new java.sql.Date(cal.getTimeInMillis()));
            } else {
                pStmt.setNull(col, java.sql.Types.DATE);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void setDate(PreparedStatement pStmt, int col, String month, String day, String year) throws SQLException {
        try {
            if (!isEmpty(month) && !isEmpty(day) && !isEmpty(year)) {
                Calendar cal = new GregorianCalendar(cInt(year), cInt(month) - 1, cInt(day));
                pStmt.setDate(col, new java.sql.Date(cal.getTimeInMillis()));
            } else {
                pStmt.setNull(col, java.sql.Types.DATE);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static HashMap<String, String> getHolidayMap() {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        //2014 Federal Holidays
        resultMap.put("1/1/2014", "New Year's Day");
        resultMap.put("1/20/2014", "Birthday of Martin Luther King, Jr.");
        resultMap.put("2/17/2014", "Washington's Birthday");
        resultMap.put("5/26/2014", "Memorial Day");
        resultMap.put("7/4/2014", "Independence Day");
        resultMap.put("9/1/2014", "Labor Day");
        resultMap.put("10/13/2014", "Columbus Day");
        resultMap.put("11/11/2014", "Veterans Day");
        resultMap.put("11/27/2014", "Thanksgiving Day");
        resultMap.put("12/25/2014", "Christmas Day");

        //2015 Federal Holidays
        resultMap.put("1/1/2015", "New Year's Day");
        resultMap.put("1/19/2015", "Birthday of Martin Luther King, Jr.");
        resultMap.put("2/16/2015", "Washington's Birthday");
        resultMap.put("5/25/2015", "Memorial Day");
        resultMap.put("7/3/2015", "Independence Day (observed)");
        resultMap.put("9/7/2015", "Labor Day");
        resultMap.put("10/12/2015", "Columbus Day");
        resultMap.put("11/11/2015", "Veterans Day");
        resultMap.put("11/26/2015", "Thanksgiving Day");
        resultMap.put("12/25/2015", "Christmas Day");

        //2016 Federal Holidays
        resultMap.put("1/1/2016", "New Year's Day");
        resultMap.put("1/18/2016", "Birthday of Martin Luther King, Jr.");
        resultMap.put("2/15/2016", "Washington's Birthday");
        resultMap.put("5/30/2016", "Memorial Day");
        resultMap.put("7/4/2016", "Independence Day");
        resultMap.put("9/5/2016", "Labor Day");
        resultMap.put("10/10/2016", "Columbus Day");
        resultMap.put("11/11/2016", "Veterans Day");
        resultMap.put("11/24/2016", "Thanksgiving Day");
        resultMap.put("12/26/2016", "Christmas Day (observed)");

        //2017 Federal Holidays
        resultMap.put("1/2/2017", "New Year's Day (observed)");
        resultMap.put("1/16/2017", "Birthday of Martin Luther King, Jr.");
        resultMap.put("2/20/2017", "Washington's Birthday");
        resultMap.put("5/29/2017", "Memorial Day");
        resultMap.put("7/4/2017", "Independence Day");
        resultMap.put("9/4/2017", "Labor Day");
        resultMap.put("10/9/2017", "Columbus Day");
        resultMap.put("11/10/2017", "Veterans Day (observed)");
        resultMap.put("11/23/2017", "Thanksgiving Day");
        resultMap.put("12/25/2017", "Christmas Day");

        return resultMap;
    }

    public static String urlClean(String str) {
        return str.replaceAll("\\%", "")
                .replaceAll("\\&", "")
                .replaceAll(" ", "%20")
                .replaceAll("\\!", "%21")
                .replaceAll("\\\"", "%22")
                .replaceAll("\\#", "%23")
                .replaceAll("\\$", "%24")
                .replaceAll("\\'", "%27")
                .replaceAll("\\(", "%28")
                .replaceAll("\\)", "%29")
                .replaceAll("\\*", "%2A")
                .replaceAll("\\+", "%2B")
                .replaceAll("\\,", "%2C")
                .replaceAll("\\.", "%2E")
                .replaceAll("\\/", "%2F");
    }

    public static Calendar getDateCal(String value, String format) throws Exception {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            cal.setTime(sdf.parse(value));
        } catch (Exception e) {
            logger.error("Cannot parse date " + value);
            throw e;
        }
        return cal;
    }
}
