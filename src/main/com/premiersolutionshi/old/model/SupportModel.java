package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.PrintClassPropertiesUtil;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.CalendarBean;
import com.premiersolutionshi.old.bean.CalendarItemBean;
import com.premiersolutionshi.old.bean.ChartBean;
import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.bean.TrainingBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.ColumnType;
import com.premiersolutionshi.old.util.CommonMethods;
import com.premiersolutionshi.old.util.SqlColumn;
import com.premiersolutionshi.support.constant.IssueStatus;
import com.premiersolutionshi.support.constant.MonthlyIssueCategory;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueComments;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;

/**
 * Business logic for the application's SUPPORT module
 */
public class SupportModel extends BaseModel {
    private static Logger logger = Logger.getLogger(SupportModel.class.getSimpleName());

    public static String[] closedLabels = { "5 - Closed", "6 - Closed (Successful)", "7 - Closed (No Response)",
            "8 - Closed (Unavailable)" };
    private static final String STATUS_CLOSED = "'5 - Closed', '6 - Closed (Successful)', '7 - Closed (No Response)', '8 - Closed (Unavailable)'";
    private static final String ISSUE_NOT_CLOSED = " AND status NOT IN (" + STATUS_CLOSED + ")";
    private static final String ISSUE_NOT_MONTHLY = " AND category NOT IN ('OS Update', 'FACET Update', 'ATO Maintenance Release', 'DMS Release', 'LOGCOP Inactivity', 'LOGCOP Missing Transmittals')";

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString(), logger);
        e.printStackTrace();
    }

    private static void debugLog(String type, String functionName, String str) {
        debugLog(type, functionName, str, logger);
    }

    private static ArrayList<String> getStrList(Connection conn, String sqlStmt, int projectPk) {
        ArrayList<String> resultList = new ArrayList<String>();
        debugLog("SQL", "getStrList", sqlStmt);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getStrList", e);
        }
        return resultList;
    }

    public static ArrayList<String> getStatusList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT status FROM issue WHERE project_fk = ? AND status IS NOT NULL ORDER BY status", projectPk);
    }

    public static ArrayList<String> getPriorityList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT priority FROM issue WHERE project_fk = ? AND priority IS NOT NULL ORDER BY priority",
                projectPk);
    }

    public static int getCategoryPk(Connection conn, String categoryName) throws Exception {
        String sqlStmt = "SELECT issue_category_pk FROM issue_category WHERE category = ?";
        int resultPk = -1;

        debugLog("SQL", "getCategoryPk", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, categoryName);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultPk = rs.getInt("issue_category_pk");
        }
        catch (Exception e) {
            throw e;
        }

        if (resultPk <= -1)
            throw new Exception("Category not found");

        return resultPk;
    }

    public static String getCategoryName(Connection conn, String issueCategoryPk) {
        String sqlStmt = "SELECT category FROM issue_category WHERE issue_category_pk = ?";
        String resultStr = null;

        debugLog("SQL", "getCategoryName", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(issueCategoryPk));
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultStr = rs.getString("category");
        }
        catch (Exception e) {
            debugLog("ERROR", "getCategoryName", e);
        }

        return resultStr;
    }

    public static ArrayList<String> getPhaseList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT phase FROM issue WHERE project_fk = ? AND phase IS NOT NULL ORDER BY phase", projectPk);
    }

    public static ArrayList<String> getOpenedByList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT opened_by FROM issue WHERE project_fk = ? AND opened_by IS NOT NULL ORDER BY opened_by",
                projectPk);
    }

    public static int getIssueCnt(Connection conn, SupportBean inputBean, int projectPk) {
        StringBuffer sqlStmt = new StringBuffer("SELECT COUNT(1) FROM issue_vw WHERE project_fk = ?");
        int resultCnt = -1;

        if (projectPk == -1)
            return -1;

        buildIssueQuery(inputBean, sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            int i = 1;
            pStmt.setInt(i++, projectPk);

            buildIssueQueryWhereValues(inputBean, pStmt, i);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultCnt = rs.getInt(1);
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueCnt", e);
        }

        return resultCnt;
    }

    private static void buildIssueQuery(SupportBean issueBean, StringBuffer sqlStmt) {
        String searchMode = CommonMethods.nes(issueBean.getSearchMode());
        switch (CommonMethods.nes(searchMode)) {
        case "all_non_monthly_60":
            sqlStmt.append(" AND opened_date >= date('now', '-10 hours', '-60 days')" + ISSUE_NOT_MONTHLY);
            break;
        case "open_non_monthly":
            sqlStmt.append(ISSUE_NOT_MONTHLY);
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "open":
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "closed":
            sqlStmt.append(" AND status IN (" + STATUS_CLOSED + ")");
            break;
        case "osupdate":
            sqlStmt.append(" AND category = 'OS Update'");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "facetupdate":
            sqlStmt.append(" AND category = 'FACET Update'");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "ato":
            sqlStmt.append(" AND category = 'ATO Maintenance Release'");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "dms":
            sqlStmt.append(" AND category = 'DMS Release'");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "logcopinactivity":
        case "dacsinactivity":
            sqlStmt.append(" AND (category = 'LOGCOP Inactivity' OR category = 'DACS Inactivity')");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        case "missingtransmittal":
        case "dacsmissingtransmittal":
            sqlStmt.append(" AND (category = 'LOGCOP Missing Transmittals' OR category = 'DACS Missing Transmittals')");
            sqlStmt.append(ISSUE_NOT_CLOSED);
            break;
        }

        if (!CommonMethods.isEmpty(issueBean.getTitle()))
            sqlStmt.append(" AND INSTR(LOWER(title), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(issueBean.getStatus()))
            sqlStmt.append(" AND status = ?");
        if (!CommonMethods.isEmpty(issueBean.getIssueCategoryFk()))
            sqlStmt.append(" AND issue_category_fk = ?");
        if (!CommonMethods.isEmpty(issueBean.getPhase()))
            sqlStmt.append(" AND phase = ?");
        if (!CommonMethods.isEmpty(issueBean.getDept()))
            sqlStmt.append(" AND dept = ?");
        if (!CommonMethods.isEmpty(issueBean.getUic()))
            sqlStmt.append(" AND uic = ?");
        if (!CommonMethods.isEmpty(issueBean.getContractNumber()))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        if (!CommonMethods.isEmpty(issueBean.getPriority()))
            sqlStmt.append(" AND priority = ?");
        if (!CommonMethods.isEmpty(issueBean.getPersonAssigned()))
            sqlStmt.append(" AND person_assigned = ?");
        if (!CommonMethods.isEmpty(issueBean.getComments()))
            sqlStmt.append(" AND issue_pk IN (SELECT x.issue_fk FROM issue_comments x WHERE INSTR(LOWER(x.comments), LOWER(?)) > 0)");
    }

    public static ArrayList<SupportBean> getIssueList(Connection conn, int projectPk, LoginBean loginBean, String sortBy, String sortDir) {
        SupportBean inputBean = new SupportBean();
        inputBean.setSearchMode("open");
        inputBean.setPersonAssigned(loginBean.getFullName());
        inputBean.setSortBy(sortBy);
        inputBean.setSortDir(sortDir);
        return getIssueList(conn, inputBean, projectPk);
    }

    public static ArrayList<SupportBean> getIssueList(Connection conn, int projectPk, String searchMode) {
        SupportBean inputBean = new SupportBean();
        inputBean.setSearchMode(searchMode);
        return getIssueList(conn, inputBean, projectPk);
    }

    public static ArrayList<SupportBean> getIssueList(Connection conn, int projectPk, String uic, String searchMode) {
        SupportBean inputBean = new SupportBean();
        inputBean.setUic(uic);
        inputBean.setSearchMode(searchMode);
        return getIssueList(conn, inputBean, projectPk);
    }

    public static ArrayList<SupportBean> getIssueList(Connection conn, int projectPk, String uic, String searchMode,
            String contractNumber) {
        SupportBean inputBean = new SupportBean();
        inputBean.setUic(uic);
        inputBean.setSearchMode(searchMode);
        inputBean.setContractNumber(contractNumber);
        return getIssueList(conn, inputBean, projectPk);
    }

    public static ArrayList<SupportBean> getIssueList(Connection conn, SupportBean inputBean, int projectPk) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT issue_pk, project_fk, title, description, status, priority, priority_reason, category, "
                        + "phase, ship_fk, ship_name, type, hull, homeport, opened_by, opened_date_fmt, IFNULL(closed_date_fmt, closed_date) AS closed_date_fmt, "
                        + "person_assigned, resolution, update_configured_system_ind, update_facet_version, update_os_version, "
                        + "total_time, initiated_by, dept, is_email_sent, is_email_responded, support_visit_date_fmt, support_visit_time, "
                        + "support_visit_end_time, support_visit_reason, support_visit_loc, support_visit_loc_notes, trainer, is_training_provided, "
                        + "is_training_onsite, laptop_issue, scanner_issue, software_issue, last_updated_by, last_updated_date_fmt "
                        + "FROM issue_vw  WHERE project_fk = ?");
        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();
        HashMap<String, String> contractNumberMap = SystemModel.getContractNumberMap(conn);

        if (projectPk == -1)
            return null;

        buildIssueQuery(inputBean, sqlStmt);

        // ORDER BY
        switch (inputBean.getSortBy() + "_" + inputBean.getSortDir()) {
        case "issue_pk_ASC":
            sqlStmt.append(" ORDER BY CAST(issue_pk AS NUMERIC) ASC");
            break;
        case "issue_pk_DESC":
            sqlStmt.append(" ORDER BY CAST(issue_pk AS NUMERIC) DESC");
            break;
        case "title_ASC":
            sqlStmt.append(" ORDER BY title ASC");
            break;
        case "title_DESC":
            sqlStmt.append(" ORDER BY title DESC");
            break;
        case "status_ASC":
            sqlStmt.append(" ORDER BY status ASC");
            break;
        case "status_DESC":
            sqlStmt.append(" ORDER BY status DESC");
            break;
        case "category_ASC":
            sqlStmt.append(" ORDER BY category ASC");
            break;
        case "category_DESC":
            sqlStmt.append(" ORDER BY category DESC");
            break;
        case "ship_name_ASC":
            sqlStmt.append(" ORDER BY ship_name IS NULL OR ship_name='', ship_name ASC");
            break;
        case "ship_name_DESC":
            sqlStmt.append(" ORDER BY ship_name IS NULL OR ship_name='', ship_name DESC");
            break;
        case "person_assigned_ASC":
            sqlStmt.append(" ORDER BY person_assigned IS NULL OR person_assigned='', person_assigned ASC");
            break;
        case "person_assigned_DESC":
            sqlStmt.append(" ORDER BY person_assigned IS NULL OR person_assigned='', person_assigned DESC");
            break;
        case "opened_date_ASC":
            sqlStmt.append(" ORDER BY opened_date DESC");
            break;
        case "opened_date_DESC":
            sqlStmt.append(" ORDER BY opened_date ASC");
            break;
        case "last_updated_date_ASC":
            sqlStmt.append(" ORDER BY last_updated_date DESC");
            break;
        case "last_updated_date_DESC":
            sqlStmt.append(" ORDER BY last_updated_date ASC");
            break;
        default:
            sqlStmt.append(" ORDER BY issue_pk DESC");
        }

        //debugLog("SQL", "getIssueList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            int i = 1;
            pStmt.setInt(i++, projectPk);

            buildIssueQueryWhereValues(inputBean, pStmt, i);

            ResultSet rs = pStmt.executeQuery();
            int recNum = 0;
            while (rs.next() && (!inputBean.isPagination() || resultList.size() < 100)) {
                if (!inputBean.isPagination() || (recNum >= (inputBean.getPageNum() - 1) * 100)) {
                    SupportBean resultBean = new SupportBean();
                    resultBean.setIssuePk(rs.getString("issue_pk"));
                    resultBean.setProjectPk(rs.getString("project_fk"));
                    resultBean.setTitle(rs.getString("title"));
                    resultBean.setDescription(rs.getString("description"));
                    resultBean.setStatus(rs.getString("status"));
                    resultBean.setPriority(rs.getString("priority"));
                    resultBean.setPriorityReason(rs.getString("priority_reason"));
                    resultBean.setCategory(rs.getString("category"));
                    resultBean.setPhase(rs.getString("phase"));
                    resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                            + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                    ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                    : ""));
                    resultBean.setHomeport(rs.getString("homeport"));

                    resultBean.setSupportVisitDate(rs.getString("support_visit_date_fmt"));
                    if (!CommonMethods.isEmpty(rs.getString("support_visit_time")))
                        resultBean.setSupportVisitTime(CommonMethods.padString(rs.getString("support_visit_time"), "0", 4));
                    if (!CommonMethods.isEmpty(rs.getString("support_visit_end_time")))
                        resultBean.setSupportVisitEndTime(CommonMethods.padString(rs.getString("support_visit_end_time"), "0", 4));
                    resultBean.setSupportVisitReason(rs.getString("support_visit_reason"));
                    resultBean.setSupportVisitLoc(rs.getString("support_visit_loc"));
                    resultBean.setSupportVisitLocNotes(rs.getString("support_visit_loc_notes"));
                    resultBean.setTrainer(rs.getString("trainer"));

                    resultBean.setOpenedBy(rs.getString("opened_by"));
                    resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                    resultBean.setClosedDate(rs.getString("closed_date_fmt"));
                    resultBean.setPersonAssigned(rs.getString("person_assigned"));
                    resultBean.setResolution(rs.getString("resolution"));
                    resultBean.setTotalTime(rs.getString("total_time"));
                    resultBean.setInitiatedBy(rs.getString("initiated_by"));
                    resultBean.setDept(rs.getString("dept"));
                    resultBean.setIsEmailSent(rs.getString("is_email_sent"));
                    resultBean.setIsEmailResponded(rs.getString("is_email_responded"));
                    resultBean.setIsTrainingProvided(rs.getString("is_training_provided"));
                    resultBean.setIsTrainingOnsite(rs.getString("is_training_onsite"));

                    resultBean.setLaptopIssue(rs.getString("laptop_issue"));
                    resultBean.setScannerIssue(rs.getString("scanner_issue"));
                    resultBean.setSoftwareIssue(rs.getString("software_issue"));

                    resultBean.setUpdateConfiguredSystemInd(rs.getString("update_configured_system_ind"));
                    resultBean.setUpdateFacetVersion(rs.getString("update_facet_version"));
                    resultBean.setUpdateOsVersion(rs.getString("update_os_version"));

                    resultBean.setContractNumber(contractNumberMap.get(rs.getString("ship_fk")));

                    resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                    resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
                    resultList.add(resultBean);
                }
                recNum++;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueList", e);
        }
        return resultList;
    }

    private static void buildIssueQueryWhereValues(SupportBean inputBean, PreparedStatement pStmt, int i) throws SQLException {
        if (!CommonMethods.isEmpty(inputBean.getTitle()))
            pStmt.setString(i++, inputBean.getTitle());
        if (!CommonMethods.isEmpty(inputBean.getStatus()))
            pStmt.setString(i++, inputBean.getStatus());
        if (!CommonMethods.isEmpty(inputBean.getIssueCategoryFk()))
            pStmt.setString(i++, inputBean.getIssueCategoryFk());
        if (!CommonMethods.isEmpty(inputBean.getPhase()))
            pStmt.setString(i++, inputBean.getPhase());
        if (!CommonMethods.isEmpty(inputBean.getDept()))
            pStmt.setString(i++, inputBean.getDept());
        if (!CommonMethods.isEmpty(inputBean.getUic()))
            pStmt.setString(i++, inputBean.getUic());
        if (!CommonMethods.isEmpty(inputBean.getContractNumber()))
            pStmt.setString(i++, inputBean.getContractNumber());
        if (!CommonMethods.isEmpty(inputBean.getPriority()))
            pStmt.setString(i++, inputBean.getPriority());
        if (!CommonMethods.isEmpty(inputBean.getPersonAssigned()))
            pStmt.setString(i++, inputBean.getPersonAssigned());
        if (!CommonMethods.isEmpty(inputBean.getComments()))
            pStmt.setString(i++, inputBean.getComments());
    }

    public static int getRecentIssueTotal(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT COUNT(1) FROM issue_vw WHERE project_fk = ? AND opened_date > date('now', '-10 hours', '-7 days')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");

        int resultCnt = 0;

        if (projectPk == -1)
            return 0;

        debugLog("SQL", "getRecentIssueTotal", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultCnt = rs.getInt(1);
        }
        catch (Exception e) {
            debugLog("ERROR", "getRecentIssueTotal", e);
        }

        return resultCnt;
    }

    public static ArrayList<SupportBean> getRecentIssueSummaryList(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT category, COUNT(1) AS cnt FROM issue_vw WHERE project_fk = ? AND opened_date > date('now', '-10 hours', '-7 days')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" GROUP BY category ORDER BY 2 DESC");

        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();

        if (projectPk == -1)
            return null;

        debugLog("SQL", "getRecentIssueSummaryList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setCategory(rs.getString("category"));
                resultBean.setIssueCnt(rs.getString("cnt"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getRecentIssueSummaryList", e);
        }

        return resultList;
    }

    public static ArrayList<CalendarBean> getRecentIssueList(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT issue_pk, ship_name, type, hull, homeport, title, status, priority, category, phase, opened_date_fmt, IFNULL(closed_date_fmt, closed_date) AS closed_date_fmt, person_assigned, resolution FROM issue_vw WHERE project_fk = ? AND opened_date > date('now', '-10 hours', '-7 days')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" ORDER BY opened_date DESC");

        ArrayList<CalendarBean> resultList = new ArrayList<CalendarBean>();

        if (projectPk == -1)
            return null;

        debugLog("SQL", "getRecentIssueList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();

            HashMap<String, ArrayList<SupportBean>> resultMap = new HashMap<String, ArrayList<SupportBean>>();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                        + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setCategory(rs.getString("category"));
                resultBean.setClosedDate(rs.getString("closed_date_fmt"));
                resultBean.setResolution(rs.getString("resolution"));

                if (resultMap.get(rs.getString("opened_date_fmt")) == null)
                    resultMap.put(rs.getString("opened_date_fmt"), new ArrayList<SupportBean>());
                resultMap.get(rs.getString("opened_date_fmt")).add(resultBean);
            }

            for (int i = 0; i > -7; i--) {
                CalendarBean dateBean = new CalendarBean();
                dateBean.setDate(CommonMethods.getDate("DOW MM/DD", i));
                dateBean.setIssueList(resultMap.get(CommonMethods.getDate("MM/DD/YYYY", i)));
                resultList.add(dateBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getRecentIssueList", e);
        }

        return resultList;
    }

    public static int getRecentClosedTotal(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT COUNT(1) FROM issue_vw WHERE project_fk = ? AND closed_date > date('now', '-10 hours', '-7 days')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");

        int resultCnt = 0;

        if (projectPk == -1)
            return 0;

        debugLog("SQL", "getRecentClosedTotal", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultCnt = rs.getInt(1);
        }
        catch (Exception e) {
            debugLog("ERROR", "getRecentClosedTotal", e);
        }

        return resultCnt;
    }

    public static ArrayList<CalendarBean> getRecentClosedList(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT issue_pk, ship_name, type, hull, homeport, title, status, priority, category, phase, "
                        + "IFNULL(opened_date_fmt, opened_date) AS opened_date_fmt, IFNULL(closed_date_fmt, closed_date) AS closed_date_fmt, person_assigned, resolution "
                        + "FROM issue_vw " + "WHERE project_fk = ? AND closed_date > date('now', '-10 hours', '-7 days')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" ORDER BY closed_date DESC");

        ArrayList<CalendarBean> resultList = new ArrayList<CalendarBean>();

        if (projectPk == -1)
            return null;

        debugLog("SQL", "getRecentClosedList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();

            HashMap<String, ArrayList<SupportBean>> resultMap = new HashMap<String, ArrayList<SupportBean>>();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                        + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setCategory(rs.getString("category"));
                resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                resultBean.setResolution(rs.getString("resolution"));

                if (resultMap.get(rs.getString("closed_date_fmt")) == null)
                    resultMap.put(rs.getString("closed_date_fmt"), new ArrayList<SupportBean>());
                resultMap.get(rs.getString("closed_date_fmt")).add(resultBean);
            }

            for (int i = 0; i > -7; i--) {
                CalendarBean dateBean = new CalendarBean();
                dateBean.setDate(CommonMethods.getDate("DOW MM/DD", i));
                dateBean.setIssueList(resultMap.get(CommonMethods.getDate("MM/DD/YYYY", i)));
                resultList.add(dateBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getRecentClosedList", e);
        }

        return resultList;
    }

    public static String getCurrRecordStr(ArrayList<SupportBean> issueList, int issuePk) {
        String returnStr = null;

        int i = 0;
        while (i < issueList.size() && returnStr == null) {
            if (CommonMethods.cInt(((SupportBean) issueList.get(i)).getIssuePk()) == issuePk)
                returnStr = "Record " + (i + 1) + " of " + issueList.size();
            i++;
        }

        return returnStr;
    }

    public static SupportBean getPrevIssueBean(ArrayList<SupportBean> issueList, int issuePk) {
        SupportBean resultBean = null;

        int i = 0;
        while (i < issueList.size() && resultBean == null) {
            if (CommonMethods.cInt(((SupportBean) issueList.get(i)).getIssuePk()) == issuePk && i - 1 >= 0)
                resultBean = issueList.get(i - 1);
            i++;
        }
        return resultBean;
    }

    public static SupportBean getNextIssueBean(ArrayList<SupportBean> issueList, int issuePk) {
        SupportBean resultBean = null;

        int i = 0;
        while (i < issueList.size() && resultBean == null) {
            if (CommonMethods.cInt(((SupportBean) issueList.get(i)).getIssuePk()) == issuePk && i + 1 < issueList.size()) {
                resultBean = issueList.get(i + 1);
            }
            i++;
        }
        return resultBean;
    }

    public static String getShipFullName(String shipName, String type, String hull) {
        if (!StringUtils.isEmpty(shipName)) {
            StringBuilder str = new StringBuilder(shipName);
            if (!StringUtils.isEmpty(type) && !StringUtils.safeEquals("ATG", type)) {
                str.append(" (").append(type);
                if (!StringUtils.isEmpty(hull)) {
                    str.append(" ").append(hull);
                }
                str.append(")");
            }
            return str.toString();
        }
        return "N/A";
    }

    public static SupportBean getIssueBean(Connection conn, SupportBean inputBean) {
        StringBuilder str = new StringBuilder("SELECT ");
        str.append("i.issue_pk, i.project_fk, i.ship_fk, i.title, i.description, i.status, i.priority, i.priority_reason, ")
        .append("i.issue_category_fk, c.category, i.phase, i.opened_by, ")
        .append("i.opened_date, strftime('%m/%d/%Y', i.opened_date) AS opened_date_fmt, ")
        .append("i.closed_date, strftime('%m/%d/%Y', i.closed_date) AS closed_date_fmt, ")
        .append("i.person_assigned, i.resolution, i.total_time, i.created_by, ")
        .append("i.created_date, strftime('%m/%d/%Y %H:%M:%S', i.created_date) AS created_date_fmt,")
        .append("i.support_visit_date, strftime('%m/%d/%Y', i.support_visit_date) AS support_visit_date_fmt, ")
        .append("i.support_visit_time, i.support_visit_end_time, i.support_visit_reason, i.trainer, support_visit_loc, ")
        .append("i.initiated_by, i.dept, i.is_email_sent, i.is_email_responded, is_training_provided, i.is_training_onsite, ")
        .append("i.ato_fk, i.auto_close_date, strftime('%m/%d/%Y', i.auto_close_date) AS auto_close_date_fmt, ")
        .append("i.auto_close_status, i.laptop_issue, i.scanner_issue, i.software_issue, support_visit_loc_notes, ")
        .append("i.update_configured_system_ind, i.update_facet_version, i.update_os_version, i.configured_system_fk, ")
        .append("i.last_updated_by,")
        .append("i.last_updated_date, strftime('%m/%d/%Y %H:%M:%S', i.last_updated_date) AS last_updated_date_fmt,")
        .append("s.uic,")
        .append("s.ship_name,")
        .append("s.type,")
        .append("s.hull,")
        .append("s.tycom,")
        .append("s.homeport,")
        .append("s.rsupply,")
        .append("cs.configured_system_pk,")
        .append("cs.os_version,")
        .append("cs.facet_version,")
        .append("cs.access_version, ")
        .append("l.laptop_pk, ")
        .append("l.computer_name ")
        .append("FROM issue i ")
        .append("LEFT OUTER JOIN issue_category c ON i.issue_category_fk = c.issue_category_pk ")
        .append("LEFT OUTER JOIN ship s ON i.ship_fk = s.ship_pk ")
        .append("LEFT OUTER JOIN configured_system cs ON cs.configured_system_pk = i.configured_system_fk ")
        .append("LEFT OUTER JOIN laptop l ON l.laptop_pk = cs.laptop_fk ")
        .append("WHERE issue_pk = ?")
        ;
        String sqlStmt = str.toString();
        SupportBean resultBean = new SupportBean();

        if (CommonMethods.cInt(inputBean.getIssuePk().trim()) <= -1)
            return null;

        debugLog("SQL", "getIssueBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk().trim()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setShipPk(rs.getString("ship_fk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setType(rs.getString("type"));
                resultBean.setShipName(getShipFullName(rs.getString("ship_name"), rs.getString("type"), rs.getString("hull")));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setPriority(rs.getString("priority"));
                resultBean.setPriorityReason(rs.getString("priority_reason"));
                resultBean.setIssueCategoryFk(rs.getString("issue_category_fk"));
                resultBean.setCategory(rs.getString("category"));
                resultBean.setPhase(rs.getString("phase"));
                // For update page
                resultBean.setCurrPhase(rs.getString("phase"));
                resultBean.setOpenedBy(rs.getString("opened_by"));
                resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                resultBean.setClosedDate(rs.getString("closed_date_fmt"));
                resultBean.setPersonAssigned(rs.getString("person_assigned"));
                // For update page
                resultBean.setCurrPersonAssigned(rs.getString("person_assigned"));
                resultBean.setResolution(rs.getString("resolution"));
                resultBean.setTotalTime(rs.getString("total_time"));
                resultBean.setSupportVisitDate(rs.getString("support_visit_date_fmt"));
                if (!CommonMethods.isEmpty(rs.getString("support_visit_time")))
                    resultBean.setSupportVisitTime(CommonMethods.padString(rs.getString("support_visit_time"), "0", 4));
                if (!CommonMethods.isEmpty(rs.getString("support_visit_end_time")))
                    resultBean.setSupportVisitEndTime(CommonMethods.padString(rs.getString("support_visit_end_time"), "0", 4));
                resultBean.setSupportVisitReason(rs.getString("support_visit_reason"));
                resultBean.setSupportVisitLoc(rs.getString("support_visit_loc"));
                resultBean.setSupportVisitLocNotes(rs.getString("support_visit_loc_notes"));
                resultBean.setTrainer(rs.getString("trainer"));
                resultBean.setCurrTrainer(rs.getString("trainer"));
                resultBean.setInitiatedBy(rs.getString("initiated_by"));
                resultBean.setDept(rs.getString("dept"));
                resultBean.setIsEmailSent(rs.getString("is_email_sent"));
                resultBean.setIsEmailResponded(rs.getString("is_email_responded"));
                resultBean.setIsTrainingProvided(rs.getString("is_training_provided"));
                resultBean.setIsTrainingOnsite(rs.getString("is_training_onsite"));
                resultBean.setIssueCommentsList(getIssueCommentsList(conn, rs.getInt("issue_pk")));
                resultBean.setIssueFileList(getIssueFileList(conn, rs.getInt("issue_pk")));
                resultBean.setRelatedIssuePkArr(getRelatedIssuePkArr(conn, rs.getInt("issue_pk")));
                resultBean.setAutoCloseDate(rs.getString("auto_close_date_fmt"));
                resultBean.setAutoCloseStatus(rs.getString("auto_close_status"));

                resultBean.setLaptopIssue(rs.getString("laptop_issue"));
                resultBean.setScannerIssue(rs.getString("scanner_issue"));
                resultBean.setSoftwareIssue(rs.getString("software_issue"));

                resultBean.setUpdateConfiguredSystemInd(rs.getString("update_configured_system_ind"));
                resultBean.setUpdateFacetVersion(rs.getString("update_facet_version"));
                resultBean.setUpdateOsVersion(rs.getString("update_os_version"));

                resultBean.setConfiguredSystemFk(StringUtils.parseInt(rs.getString("configured_system_fk")));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setOsVersion(rs.getString("os_version"));

                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueBean", e);
        }

        return resultBean;
    }

    private static int getInsertedIssuePk(Connection conn) {
        String sqlStmt = "SELECT MAX(issue_pk) FROM issue";
        int returnVal = -1;

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnVal = rs.getInt(1);
        }
        catch (Exception e) {
            debugLog("SQL", "getInsertedIssuePk", sqlStmt);
            debugLog("ERROR", "getInsertedIssuePk", e);
        }

        return returnVal;
    }

    public static int insertIssue(Connection conn, SupportBean inputBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "INSERT INTO issue (project_fk, ship_fk, title, description, status, priority, priority_reason, issue_category_fk, category, phase, "
                + "opened_by, opened_date, closed_date, person_assigned, resolution, total_time, support_visit_date, support_visit_time, support_visit_end_time, "
                + "support_visit_reason, trainer, support_visit_loc, initiated_by, dept, is_email_sent, is_email_responded, is_training_provided, "
                + "is_training_onsite, auto_close_date, auto_close_status, laptop_issue, scanner_issue, software_issue, support_visit_loc_notes, created_by, "
                + "created_date, last_updated_by, last_updated_date) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int newIssuePk = -1;
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getProjectPk()) <= -1)
            return -1;

        debugLog("SQL", "insertIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, Integer.parseInt(inputBean.getProjectPk()));
            CommonMethods.setInt(pStmt, 2, inputBean.getShipPk());
            CommonMethods.setString(pStmt, 3, inputBean.getTitle());
            CommonMethods.setString(pStmt, 4, inputBean.getDescription().trim());
            CommonMethods.setString(pStmt, 5, inputBean.getStatus());
            CommonMethods.setString(pStmt, 6, inputBean.getPriority());
            if (CommonMethods.nes(inputBean.getPriority()).equals("High")) {
                CommonMethods.setString(pStmt, 7, CommonMethods.printArray(inputBean.getPriorityReasonArr(), ", ", ""));
                CommonMethods.setString(pStmt, 31, inputBean.getLaptopIssue());
                CommonMethods.setString(pStmt, 32, inputBean.getScannerIssue());
                CommonMethods.setString(pStmt, 33, inputBean.getSoftwareIssue());
            }
            else {
                pStmt.setNull(7, 12);
                pStmt.setNull(31, 12);
                pStmt.setNull(32, 12);
                pStmt.setNull(33, 12);
            }
            pStmt.setInt(8, Integer.parseInt(inputBean.getIssueCategoryFk()));
            pStmt.setString(9, "null");
            CommonMethods.setString(pStmt, 10, inputBean.getPhase());
            CommonMethods.setString(pStmt, 11, inputBean.getOpenedBy());
            CommonMethods.setDate(pStmt, 12, inputBean.getOpenedDate());
            if ((CommonMethods.isIn(closedLabels, inputBean.getStatus())) && (!CommonMethods.isEmpty(inputBean.getClosedDate()))) {
                pStmt.setString(13, CommonMethods.getDate(inputBean.getClosedDate(), "YYYY-MM-DD"));
                pStmt.setNull(29, 91);
                pStmt.setNull(30, 12);
            }
            else {
                pStmt.setNull(13, 91);
                CommonMethods.setDate(pStmt, 29, inputBean.getAutoCloseDate());
                CommonMethods.setString(pStmt, 30, inputBean.getAutoCloseStatus());
            }
            CommonMethods.setString(pStmt, 14, inputBean.getPersonAssigned());
            CommonMethods.setString(pStmt, 15, inputBean.getResolution());
            CommonMethods.setString(pStmt, 16, inputBean.getTotalTime());

            CommonMethods.setDate(pStmt, 17, inputBean.getSupportVisitDate());
            if (!CommonMethods.isEmpty(inputBean.getSupportVisitDate())) {
                CommonMethods.setInt(pStmt, 18, inputBean.getSupportVisitTime());
                CommonMethods.setInt(pStmt, 19, inputBean.getSupportVisitEndTime());
                CommonMethods.setString(pStmt, 20, inputBean.getSupportVisitReason());
                CommonMethods.setString(pStmt, 21, inputBean.getTrainer());
                CommonMethods.setString(pStmt, 22, inputBean.getSupportVisitLoc());
                CommonMethods.setString(pStmt, 33, inputBean.getSupportVisitLocNotes());
            }
            else {
                pStmt.setNull(18, 2);
                pStmt.setNull(19, 2);
                pStmt.setNull(20, 12);
                pStmt.setNull(21, 12);
                pStmt.setNull(22, 12);
                pStmt.setNull(34, 12);
            }
            CommonMethods.setString(pStmt, 23, inputBean.getInitiatedBy());
            CommonMethods.setString(pStmt, 24, inputBean.getDept());
            CommonMethods.setString(pStmt, 25, inputBean.getIsEmailSent());
            if (CommonMethods.nes(inputBean.getIsEmailSent()).equals("Y")) {
                pStmt.setString(26, CommonMethods.nvl(inputBean.getIsEmailResponded(), "N"));
            }
            else {
                pStmt.setNull(26, 12);
            }
            if (getCategoryName(conn, inputBean.getIssueCategoryFk()).equals("Follow-Up Training")) {
                CommonMethods.setString(pStmt, 27, inputBean.getIsTrainingProvided());
                CommonMethods.setString(pStmt, 28, inputBean.getIsTrainingOnsite());
            }
            else {
                pStmt.setNull(27, 12);
                pStmt.setNull(28, 12);
            }
            pStmt.setString(35, loginBean.getFullName());
            pStmt.setString(36, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setString(37, loginBean.getFullName());
            pStmt.setString(38, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            ranOk = pStmt.executeUpdate() == 1;

            newIssuePk = getInsertedIssuePk(conn);
            inputBean.setIssuePk(String.valueOf(newIssuePk));

            ranOk = ranOk & ((insertIssueComments(conn, inputBean, loginBean)) && (insertIssueFiles(conn, inputBean, loginBean, uploadDir))
                    && (insertRelatedIssue(conn, inputBean, loginBean)) && (closeRelatedIssue(conn, inputBean, loginBean)));
        }
        catch (Exception e) {
            debugLog("ERROR", "insertIssue", e);
            ranOk = false;
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
                logError("insertIssue commit or rollback", sqlStmt, logger, e);
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
                logError("insertIssue setAutoCommit", sqlStmt, logger, e);
            }
        }

        return ranOk ? newIssuePk : -1;
    }

    public static boolean updateIssue(Connection conn, SupportBean inputBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "UPDATE issue SET ship_fk = ?, title = ?, description = ?, status = ?, priority = ?, priority_reason = ?, issue_category_fk = ?, phase = ?, "
                + "opened_by = ?, opened_date = ?, closed_date = ?, person_assigned = ?, resolution = ?, total_time = ?, support_visit_date = ?, "
                + "support_visit_time = ?, support_visit_end_time = ?, support_visit_reason = ?, trainer = ?, support_visit_loc = ?, initiated_by = ?, dept = ?, "
                + "is_email_sent = ?, is_email_responded = ?, is_training_provided = ?, is_training_onsite = ?, auto_close_date = ?, auto_close_status = ?, "
                + "laptop_issue = ?, scanner_issue = ?, software_issue = ?, support_visit_loc_notes = ?, update_configured_system_ind = ?, last_updated_by = ?, "
                + "last_updated_date = ?, configured_system_fk = ? "
                + "WHERE issue_pk = ?";
        boolean ranOk = false;
        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1) {
            return false;
        }
        debugLog("SQL", "updateIssue", sqlStmt + " (issuePk = " + inputBean.getIssuePk() + ")");
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            String categoryName = getCategoryName(conn, inputBean.getIssueCategoryFk()).trim();
            CommonMethods.setInt(pStmt, 1, inputBean.getShipPk());
            CommonMethods.setString(pStmt, 2, inputBean.getTitle());
            CommonMethods.setString(pStmt, 3, inputBean.getDescription().trim());
            CommonMethods.setString(pStmt, 4, inputBean.getStatus());
            CommonMethods.setString(pStmt, 5, inputBean.getPriority());
            if (CommonMethods.nes(inputBean.getPriority()).equals("High")) {
                CommonMethods.setString(pStmt, 6, CommonMethods.printArray(inputBean.getPriorityReasonArr(), ", ", ""));
                CommonMethods.setString(pStmt, 29, inputBean.getLaptopIssue());
                CommonMethods.setString(pStmt, 30, inputBean.getScannerIssue());
                CommonMethods.setString(pStmt, 31, inputBean.getSoftwareIssue());
            }
            else {
                pStmt.setNull(6, 12);
                pStmt.setNull(29, 12);
                pStmt.setNull(30, 12);
                pStmt.setNull(31, 12);
            }
            pStmt.setInt(7, Integer.parseInt(inputBean.getIssueCategoryFk()));
            CommonMethods.setString(pStmt, 8, inputBean.getPhase());
            CommonMethods.setString(pStmt, 9, inputBean.getOpenedBy());
            CommonMethods.setDate(pStmt, 10, inputBean.getOpenedDate());
            if ((CommonMethods.isIn(closedLabels, inputBean.getStatus())) && (!CommonMethods.isEmpty(inputBean.getClosedDate()))) {
                pStmt.setString(11, CommonMethods.getDate(inputBean.getClosedDate(), "YYYY-MM-DD"));
                pStmt.setNull(27, 91);
                pStmt.setNull(28, 12);
            }
            else {
                pStmt.setNull(11, 91);
                CommonMethods.setDate(pStmt, 27, inputBean.getAutoCloseDate());
                CommonMethods.setString(pStmt, 28, inputBean.getAutoCloseStatus());
            }
            CommonMethods.setString(pStmt, 12, inputBean.getPersonAssigned());
            CommonMethods.setString(pStmt, 13, inputBean.getResolution());
            CommonMethods.setString(pStmt, 14, inputBean.getTotalTime());
            CommonMethods.setDate(pStmt, 15, inputBean.getSupportVisitDate());
            if (!CommonMethods.isEmpty(inputBean.getSupportVisitDate())) {
                CommonMethods.setInt(pStmt, 16, inputBean.getSupportVisitTime());
                CommonMethods.setInt(pStmt, 17, inputBean.getSupportVisitEndTime());
                CommonMethods.setString(pStmt, 18, inputBean.getSupportVisitReason());
                CommonMethods.setString(pStmt, 19, inputBean.getTrainer());
                CommonMethods.setString(pStmt, 20, inputBean.getSupportVisitLoc());
                CommonMethods.setString(pStmt, 32, inputBean.getSupportVisitLocNotes());
            }
            else {
                pStmt.setNull(16, 2); //is_email_sent
                pStmt.setNull(17, 2);
                pStmt.setNull(18, 12);
                pStmt.setNull(19, 12);
                pStmt.setNull(20, 12);
                pStmt.setNull(32, 12);
            }
            CommonMethods.setString(pStmt, 21, inputBean.getInitiatedBy());
            CommonMethods.setString(pStmt, 22, inputBean.getDept());
            CommonMethods.setString(pStmt, 23, inputBean.getIsEmailSent());
            if (CommonMethods.nes(inputBean.getIsEmailSent()).equals("Y")) {
                pStmt.setString(24, CommonMethods.nvl(inputBean.getIsEmailResponded(), "N"));
            }
            else {
                pStmt.setNull(24, 12);
            }
            if (categoryName.equals("Follow-Up Training")) {
                CommonMethods.setString(pStmt, 25, inputBean.getIsTrainingProvided());
                CommonMethods.setString(pStmt, 26, inputBean.getIsTrainingOnsite());
            }
            else {
                pStmt.setNull(25, 12);
                pStmt.setNull(26, 12);
            }
            pStmt.setString(33, inputBean.getUpdateConfiguredSystemInd());

            pStmt.setString(34, loginBean.getFullName());
            pStmt.setString(35, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(36, CommonMethods.cInt(inputBean.getConfiguredSystemFk()));
            pStmt.setInt(37, CommonMethods.cInt(inputBean.getIssuePk()));

            //if the update is successful and is a FACET, OS,
            System.out.println("\n\nSupportModel | updateIssue | categoryName=" + categoryName + "\n");
            if (StringUtils.safeEquals(categoryName, MonthlyIssueCategory.FACET.getName())
                || StringUtils.safeEquals(categoryName, MonthlyIssueCategory.OS.getName())
            ) {
                inputBean.setCategory(categoryName);
                updateConfiguredSystemVersions(conn, inputBean, loginBean, uploadDir);
            }
            ranOk = (insertIssueComments(conn, inputBean, loginBean)) && (pStmt.executeUpdate() == 1)
                    //&& (updateConfiguredSystemVersion(conn, inputBean, loginBean))
                    && (updateIssueFiles(conn, inputBean, loginBean, uploadDir)) && (updateRelatedIssue(conn, inputBean, loginBean))
                    && (closeRelatedIssue(conn, inputBean, loginBean));
        }
        catch (Exception e) {
            debugLog("ERROR", "updateIssue", e);
        }
        finally {
            try {
                if (ranOk) conn.commit();
                else conn.rollback();
            }
            catch (Exception e) { 
                debugLog("ERROR", "updateIssue2", e);}
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) { 
                debugLog("ERROR", "updateIssue3", e); }
        }
        return ranOk;
    }

    /**
     * 
     * @param conn
     * @param issueBean
     * @param loginBean
     * @param uploadDir
     */
    private static void updateConfiguredSystemVersions(Connection conn, SupportBean issueBean, LoginBean loginBean, String uploadDir) {
        System.out.println("SupportModel | updateConfiguredSystemVersions | 1 issueBean=" + PrintClassPropertiesUtil.toString(issueBean));
        if (issueBean == null) {
            return;
        }
        System.out.println("SupportModel | updateConfiguredSystemVersions | 2");
        String issuePk = issueBean.getIssuePk();
        Integer configuredSystemFk = issueBean.getConfiguredSystemFk();
        String categoryName = issueBean.getCategory();
        IssueStatus issueStatus = issueBean.getIssueStatus();
        Integer projectPk = StringUtils.parseInt(issueBean.getProjectPk());

        if (configuredSystemFk != null && configuredSystemFk < 0) {
            System.out.println("Cannot updateConfiguredSystemVersions. configuredSystemFk IS NULL.");
            return;
        }
        if (!issueStatus.equals(IssueStatus.CLOSED_SUCCESSFUL)) {
            System.out.println("not closed successful. no further action taken.");
            return;
        }
        boolean isFacetUpdate = StringUtils.safeEquals(categoryName, MonthlyIssueCategory.FACET.getName());
        boolean isOsUpdate = StringUtils.safeEquals(categoryName, MonthlyIssueCategory.OS.getName());
        System.out.println("SupportModel | updateConfiguredSystemVersions | 3 categoryName=" + categoryName + ", isFacetUpdate=" + isFacetUpdate + ", isOsUpdate=" + isOsUpdate);
        if (issuePk != null && (isFacetUpdate || isOsUpdate)
        ) {
            System.out.println("SupportModel | updateConfiguredSystemVersions | 4");
            //if the data on the database is not previously successful, we should update it.
            SupportBean issueOnDb = getIssueBean(conn, issueBean);
            if (!issueOnDb.getIssueStatus().equals(IssueStatus.CLOSED_SUCCESSFUL)) {
                SystemBean queryBean = new SystemBean();
                queryBean.setConfiguredSystemPk(configuredSystemFk + "");
                SystemBean systemBean = SystemModel.getConfiguredSystemBean(conn, queryBean);
                if (systemBean == null) {
                    debugLog("ERROR", "updateConfiguredSystemVersions", "configured_system not found. configuredSystemFk=" + configuredSystemFk, logger);
                    return;
                }
                if (isOsUpdate) {
                    String currOsVersion = LookupModel.getCurrOsVersion(conn, projectPk);
                    systemBean.setOsVersion(currOsVersion);
                    System.out.println("SupportModel | updateConfiguredSystemVersions | "
                            + "configured_system UPDATED OS VERSION: " + currOsVersion + ", id=" + systemBean.getConfiguredSystemPk());
                }
                else if (isFacetUpdate) {
                    String currFacetVersion = LookupModel.getCurrFacetVersion(conn, projectPk);
                    systemBean.setFacetVersion(currFacetVersion);
                    System.out.println("SupportModel | updateConfiguredSystemVersions | "
                            + "configured_system UPDATED FACET VERSION: " + currFacetVersion + ", id=" + systemBean.getConfiguredSystemPk());
                }
                SystemModel.updateConfiguredSystem(conn, systemBean, loginBean, uploadDir);
            }
        }
        
    }

    public static boolean deleteIssue(Connection conn, SupportBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM issue WHERE issue_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;

        debugLog("SQL", "deleteIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteIssue", e);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
            if (ranOk)
                FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    /****************************************************************************
     * Function: bulkInsertIssue (for ATO)
     ****************************************************************************/
    public static int bulkInsertIssue(Connection conn, SupportBean inputBean, LoginBean loginBean) {
        String columnsStr = "project_fk, ship_fk, title, description, status, issue_category_fk, category, phase, opened_by, opened_date, "
                + "closed_date, person_assigned, resolution, total_time, initiated_by, is_email_sent, ato_fk, auto_close_date, auto_close_status, "
                + "update_configured_system_ind, update_os_version, update_facet_version, dept, created_by, created_date, last_updated_by, "
                + "last_updated_date";
        String sqlStmt = "INSERT INTO issue (" + columnsStr + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = true;
        int insertCnt = 0;
        if (CommonMethods.cInt(inputBean.getProjectPk()) <= -1)
            return -1;
        if (inputBean.getIncludeShipPkArr() == null || inputBean.getIncludeShipPkArr().length == 0)
            return 0;

        debugLog("SQL", "bulkInsertIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setInt(pStmt, 1, inputBean.getProjectPk());
            String categoryName = inputBean.getCategory();
            pStmt.setString(4, "Auto-generated by the bulk e-mail tool");
            pStmt.setString(5, inputBean.getStatus());
            CommonMethods.setString(pStmt, 8, "Ships Support");
            CommonMethods.setString(pStmt, 9, inputBean.getOpenedBy());
            CommonMethods.setDate(pStmt, 10, inputBean.getOpenedDate());
            if ((CommonMethods.isIn(closedLabels, inputBean.getStatus())) && (!CommonMethods.isEmpty(inputBean.getClosedDate()))) {
                pStmt.setString(11, CommonMethods.getDate(inputBean.getClosedDate(), "YYYY-MM-DD"));
                pStmt.setNull(18, 91);
                pStmt.setNull(19, 12);
            }
            else {
                pStmt.setNull(11, 91);
                CommonMethods.setDate(pStmt, 18, inputBean.getAutoCloseDate());
                CommonMethods.setString(pStmt, 19, inputBean.getAutoCloseStatus());
            }
            CommonMethods.setString(pStmt, 12, inputBean.getPersonAssigned());
            CommonMethods.setString(pStmt, 13, inputBean.getResolution());
            CommonMethods.setString(pStmt, 14, inputBean.getTotalTime());

            pStmt.setString(15, "PSHI"); // initiated_by
            pStmt.setString(16, "Y"); // is_email_sent

            CommonMethods.setInt(pStmt, 17, inputBean.getAtoPk());
            if (((categoryName.equals("OS Update")) && (!CommonMethods.isEmpty(inputBean.getUpdateOsVersion())))
                    || ((categoryName.equals("FACET Update")) && (!CommonMethods.isEmpty(inputBean.getUpdateFacetVersion())))) {
                pStmt.setString(20, "Y"); // update_configured_system_ind
                pStmt.setString(23, "N/A");
            }
            else {
                pStmt.setNull(20, 12); // update_configured_system_ind
                pStmt.setNull(23, 12);
            }
            if ((categoryName.equals("OS Update")) && (!CommonMethods.isEmpty(inputBean.getUpdateOsVersion()))) {
                pStmt.setString(21, inputBean.getUpdateOsVersion()); // update_os_version
            }
            else {
                pStmt.setNull(21, 12); // update_os_version
            }
            if ((categoryName.equals("FACET Update")) && (!CommonMethods.isEmpty(inputBean.getUpdateFacetVersion()))) {
                pStmt.setString(22, inputBean.getUpdateFacetVersion());
            }
            else {
                pStmt.setNull(22, 12);
            }
            pStmt.setString(24, loginBean.getFullName());
            pStmt.setString(25, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setString(26, loginBean.getFullName());
            pStmt.setString(27, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            for (int i = 0; i < inputBean.getShipPkArr().length; i++) {
                if (CommonMethods.isIn(inputBean.getIncludeShipPkArr(), inputBean.getShipPkArr()[i])) {
                    int shipPk = CommonMethods.cInt(inputBean.getShipPkArr()[i]);
                    pStmt.setInt(2, shipPk);
                    if (categoryName.equals("Monthly E-Mail Notification")) {
                        String[] commentsArr = new String[1];
                        commentsArr[0] = inputBean.getMonthlyEmailArr()[i].replaceAll("%0A%0D", "");
                        inputBean.setCommentsArr(commentsArr);
                        pStmt.setString(3, "Monthly E-Mail Notification: Latest DMS");
                        pStmt.setInt(6, getCategoryPk(conn, "DMS Release"));
                        pStmt.setString(7, "null"); // category
                        ranOk &= pStmt.executeUpdate() == 1;
                        if (ranOk) {
                            insertCnt++;
                        }
                        inputBean.setIssuePk(String.valueOf(getInsertedIssuePk(conn)));
                        ranOk &= insertIssueComments(conn, inputBean, loginBean);
                        if (inputBean.getMonthlyEmailArr()[i].indexOf("UPLOAD ACTIVITY:") > -1) {
                            pStmt.setString(3, "Monthly E-Mail Notification: DACS Inactivity");
                            pStmt.setInt(6, getCategoryPk(conn, "DACS Inactivity"));
                            pStmt.setString(7, "null"); // category
                            ranOk &= pStmt.executeUpdate() == 1;
                            if (ranOk) {
                                insertCnt++;
                            }
                            inputBean.setIssuePk(String.valueOf(getInsertedIssuePk(conn)));
                            ranOk &= insertIssueComments(conn, inputBean, loginBean);
                        }
                        if (inputBean.getMonthlyEmailArr()[i].indexOf("MISSING TRANSMITTALS:") > -1) {
                            pStmt.setString(3, "Monthly E-Mail Notification: Missing DACS Transmittals");
                            pStmt.setInt(6, getCategoryPk(conn, "DACS Missing Transmittals"));
                            pStmt.setString(7, "null"); // category
                            ranOk &= pStmt.executeUpdate() == 1;
                            if (ranOk) {
                                insertCnt++;
                            }
                            inputBean.setIssuePk(String.valueOf(getInsertedIssuePk(conn)));
                            ranOk &= insertIssueComments(conn, inputBean, loginBean);
                        }
                        bulkInsertIssueComments(conn, getOpenIssuePkArr(conn, shipPk, "FACET Update"),
                                "Reminder e-mail sent via Monthly E-Mail Notification module", loginBean);
                        bulkInsertIssueComments(conn, getOpenIssuePkArr(conn, shipPk, "RSupply Upgrade"),
                                "Reminder e-mail sent via Monthly E-Mail Notification module", loginBean);
                        bulkInsertIssueComments(conn, getOpenIssuePkArr(conn, shipPk, "ATO Maintenance Release"),
                                "Reminder e-mail sent via Monthly E-Mail Notification module", loginBean);
                    }
                    else {
                        pStmt.setString(3, inputBean.getTitle());
                        pStmt.setInt(6, getCategoryPk(conn, categoryName));
                        pStmt.setString(7, "null"); // category
                        ranOk = pStmt.executeUpdate() == 1;
                        if (ranOk) {
                            insertCnt++;
                        }
                        inputBean.setIssuePk(String.valueOf(getInsertedIssuePk(conn)));
                        ranOk &= insertIssueComments(conn, inputBean, loginBean);
                    }
                }
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "bulkInsertIssue", e);
            ranOk = false;
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk ? insertCnt : -1;
    }

    private static String[] getOpenIssuePkArr(Connection conn, int shipPk, String category) {
        String sqlStmt = "SELECT issue_pk FROM issue WHERE ship_fk = ? AND category = ?" + ISSUE_NOT_CLOSED;
        ArrayList<String> resultList = new ArrayList<String>();

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            pStmt.setString(2, category);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getOpenIssuePkArr", e);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    public static String[] getRsupplyUpgradeShipPkArr(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_fk FROM issue WHERE category = 'RSupply Upgrade'" + ISSUE_NOT_CLOSED;
        ArrayList<String> resultList = new ArrayList<String>();

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getRsupplyUpgradeShipPkArr", e);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    private static boolean bulkUpdateIssue(Connection conn, SupportBean inputBean, String[] issuePkArr, LoginBean loginBean)
            throws Exception {
        String sqlStmt = "UPDATE issue SET "
                + "status = ?, closed_date = ?, resolution = ?, total_time = ?, last_updated_by = ?, last_updated_date = ? "
                + "WHERE issue_pk = ?";
        int rsCnt = 0;

        if (issuePkArr == null || issuePkArr.length == 0)
            return true; // Nothing to insert

        debugLog("SQL", "bulkUpdateIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, inputBean.getStatus());
            pStmt.setString(2, inputBean.getClosedDate());
            pStmt.setString(3, inputBean.getResolution());
            pStmt.setString(4, inputBean.getTotalTime());
            pStmt.setString(5, loginBean.getFullName()); // Updated By
            pStmt.setString(6, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS")); // Updated Date
            for (String issuePk : issuePkArr) {
                pStmt.setInt(7, CommonMethods.cInt(issuePk));
                rsCnt += pStmt.executeUpdate();
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "bulkUpdateIssue", e);
            rsCnt = -1;
        }

        if (rsCnt != issuePkArr.length)
            throw new Exception("Error occurred in bulkUpdateIssue");

        return true;
    }

    private static boolean bulkRemoveIssue(Connection conn, String[] issuePkArr, LoginBean loginBean) throws Exception {
        String sqlStmt = "DELETE FROM issue WHERE issue_pk = ?";
        int rsCnt = 0;

        if (issuePkArr == null || issuePkArr.length == 0)
            return true; // Nothing to insert

        debugLog("SQL", "bulkRemoveIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            for (String issuePk : issuePkArr) {
                pStmt.setInt(1, CommonMethods.cInt(issuePk));
                rsCnt += pStmt.executeUpdate();
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "bulkRemoveIssue", e);
            rsCnt = -1;
        }

        if (rsCnt != issuePkArr.length)
            throw new Exception("Error occurred in bulkRemoveIssue");

        return true;
    }

    private static ArrayList<SupportBean> getIssueCommentsList(Connection conn, int issuePk) {
        String sqlStmt = "SELECT " + "comments, created_by, created_date, "
                + "strftime('%m/%d/%Y %H:%M', created_date) AS created_date_fmt " + "FROM issue_comments " + "WHERE issue_fk = ? "
                + "ORDER BY issue_comments_pk DESC";
        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();

        debugLog("SQL", "getIssueCommentsList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, issuePk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setComments(rs.getString("comments"));
                resultBean.setCreatedBy(rs.getString("created_by"));
                String createdDate = rs.getString("created_date_fmt");
                if (StringUtils.isEmpty(createdDate)) {
                    Long timestamp = Long.parseLong(rs.getString("created_date"));
                    LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    createdDate = localDateTime.format(DateUtils.COMMON_LONG_FORMAT);
                }
                resultBean.setCreatedDate(createdDate);
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueCommentsList", e);
        }

        return resultList;
    }

    private static boolean insertIssueComments(Connection conn, SupportBean inputBean, LoginBean loginBean) throws Exception {
        String sqlStmt = "INSERT INTO issue_comments (issue_fk, comments, created_by, created_date) VALUES (?,?,?,?)";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;
        if (inputBean.getCommentsArr() == null || inputBean.getCommentsArr().length == 0)
            return true; // Nothing to insert

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));
            pStmt.setString(3, loginBean.getFullName()); // Created By
            pStmt.setString(4, CommonMethods.getNow()); // Created Date
            for (int i = inputBean.getCommentsArr().length - 1; i >= 0; i--) {
                if (!CommonMethods.isEmpty(inputBean.getCommentsArr()[i])) {
                    CommonMethods.setString(pStmt, 2, inputBean.getCommentsArr()[i]);
                    ranOk &= (pStmt.executeUpdate() == 1);
                }
            }
        }
        catch (Exception e) {
            debugLog("SQL", "insertIssueComments", sqlStmt);
            debugLog("ERROR", "insertIssueComments", e);
            ranOk = false;
        }

        if (!ranOk)
            throw new Exception("Error occurred in insertIssueComments");

        return ranOk;
    }

    private static boolean bulkInsertIssueComments(Connection conn, String[] issuePkArr, String comments, LoginBean loginBean)
            throws Exception {
        String sqlStmt = "INSERT INTO issue_comments (issue_fk, comments, created_by, created_date) VALUES (?,?,?,?)";
        int rsCnt = 0;

        if (CommonMethods.isEmpty(comments) || issuePkArr == null || issuePkArr.length == 0)
            return true; // Nothing to insert

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(2, comments);
            pStmt.setString(3, loginBean.getFullName()); // Created By
            pStmt.setString(4, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS")); // Created
                                                                                // Date
            for (String issuePk : issuePkArr) {
                pStmt.setInt(1, CommonMethods.cInt(issuePk));
                rsCnt += pStmt.executeUpdate();
            }
        }
        catch (Exception e) {
            debugLog("SQL", "bulkInsertIssueComments", sqlStmt + " (" + CommonMethods.printArray(issuePkArr, ", ", "") + ")");
            debugLog("ERROR", "bulkInsertIssueComments", e);
            rsCnt = -1;
        }

        if (rsCnt != issuePkArr.length)
            throw new Exception("Error occurred in bulkInsertIssueComments");

        return true;
    }

    private static ArrayList<FileBean> getIssueFileList(Connection conn, int issuePk) {
        String sqlStmt = "SELECT file_fk, filename, extension, filesize, uploaded_by, strftime('%m/%d/%Y %H:%M:%S', uploaded_date) as uploaded_date_fmt FROM issue_file_vw WHERE issue_fk = ? ORDER BY filename";
        ArrayList<FileBean> resultList = new ArrayList<FileBean>();

        debugLog("SQL", "getIssueFileList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, issuePk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                FileBean resultBean = new FileBean();
                resultBean.setFilePk(rs.getString("file_fk"));
                resultBean.setFilename(rs.getString("filename"));
                resultBean.setExtension(rs.getString("filesize"));
                resultBean.setImage(CommonMethods.getFileIcon(rs.getString("extension"), "lrg"));
                resultBean.setSmlImage(CommonMethods.getFileIcon(rs.getString("extension"), "sml"));
                resultBean.setUploadedBy(rs.getString("uploaded_by"));
                resultBean.setUploadedDate(rs.getString("uploaded_date_fmt"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueFileList", e);
        }

        return resultList;
    }

    private static boolean updateIssueFiles(Connection conn, SupportBean inputBean, LoginBean loginBean, String uploadDir) {
        return deleteIssueFiles(conn, inputBean, uploadDir) && insertIssueFiles(conn, inputBean, loginBean, uploadDir);
    }

    private static boolean insertIssueFiles(Connection conn, SupportBean inputBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "INSERT INTO issue_file (issue_fk, file_fk) VALUES (?,?)";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;
        if (inputBean.getFileList() == null || inputBean.getFileList().size() <= 0)
            return true; // No files to upload

        debugLog("SQL", "insertIssueFiles", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));

            for (FormFile file : inputBean.getFileList()) {
                if (file.getFileSize() > 0) {
                    logger.debug("Saving: " + file.getFileName());
                    int newFilePk = FileModel.saveFile(conn, file, loginBean, uploadDir);
                    pStmt.setInt(2, newFilePk);
                    ranOk &= (newFilePk != -1 && pStmt.executeUpdate() == 1);
                }
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "insertIssueFiles", e);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean deleteIssueFiles(Connection conn, SupportBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM issue_file WHERE issue_fk = ? AND file_fk = ?";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;
        if (inputBean.getDeleteFilePkArr() == null || inputBean.getDeleteFilePkArr().length <= 0)
            return true; // Nothing to delete

        debugLog("SQL", "deleteIssueFiles", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));
            for (String filePk : inputBean.getDeleteFilePkArr()) {
                pStmt.setInt(2, CommonMethods.cInt(filePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteIssueFiles", e);
            ranOk = false;
        }
        finally {
            if (ranOk)
                FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    private static String[] getRelatedIssuePkArr(Connection conn, int issuePk) {
        String sqlStmt = "SELECT related_issue_fk FROM issue_related WHERE issue_fk = ? ORDER BY related_issue_fk";
        ArrayList<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getRelatedIssuePkArr", sqlStmt + " (issue_fk = " + issuePk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, issuePk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getRelatedIssuePkArr", e);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    private static boolean updateRelatedIssue(Connection conn, SupportBean inputBean, LoginBean loginBean) {
        return deleteRelatedIssue(conn, inputBean) && insertRelatedIssue(conn, inputBean, loginBean);
    }

    private static boolean insertRelatedIssue(Connection conn, SupportBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO issue_related (issue_fk, related_issue_fk) VALUES (?,?)";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;
        if (inputBean.getRelatedIssuePkArr() == null || inputBean.getRelatedIssuePkArr().length <= 0)
            return true; // No related issue pks

        debugLog("SQL", "insertRelatedIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));

            for (String relatedIssuePk : inputBean.getRelatedIssuePkArr()) {
                CommonMethods.setInt(pStmt, 2, relatedIssuePk);
                ranOk &= pStmt.executeUpdate() == 1;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "insertRelatedIssue", e);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean deleteRelatedIssue(Connection conn, SupportBean inputBean) {
        String sqlStmt = "DELETE FROM issue_related WHERE issue_fk = ? AND related_issue_fk = ?";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getIssuePk()) <= -1)
            return false;
        if (inputBean.getDeleteRelatedIssuePkArr() == null || inputBean.getDeleteRelatedIssuePkArr().length <= 0)
            return true; // Nothing to delete

        debugLog("SQL", "deleteRelatedIssue", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getIssuePk()));
            for (String relatedIssuePk : inputBean.getDeleteRelatedIssuePkArr()) {
                pStmt.setInt(2, CommonMethods.cInt(relatedIssuePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteRelatedIssue", e);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean closeRelatedIssue(Connection conn, SupportBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE issue SET " + "status = ?, " + "closed_date = ?, " + "resolution = ?, "
                + "person_assigned = IFNULL(trainer, person_assigned), " + "total_time = IFNULL(total_time, ?), " + "last_updated_by = ?, "
                + "last_updated_date = ? " + "WHERE issue_pk = ?";
        boolean ranOk = true;

        if (!CommonMethods.nes(inputBean.getStatus()).equals("6 - Closed (Successful)"))
            return true; // Not a closing issue
        if (CommonMethods.isEmpty(inputBean.getSupportVisitDate()))
            return true; // No support date
        if (inputBean.getCloseIssuePkArr() == null || inputBean.getCloseIssuePkArr().length <= 0)
            return true; // Nothing to close

        debugLog("SQL", "closeRelatedIssue", "support_visit_date: " + inputBean.getSupportVisitDate() + ", issue_pk: "
                + CommonMethods.printArray(inputBean.getCloseIssuePkArr(), ", ", ""));

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, inputBean.getStatus());
            pStmt.setString(2, CommonMethods.getDate(inputBean.getClosedDate(), "YYYY-MM-DD"));
            pStmt.setString(3, "Completed during onsite visit " + inputBean.getSupportVisitDate());
            pStmt.setInt(4, 15);//total time
            pStmt.setString(5, loginBean.getFullName()); // Updated By
            pStmt.setString(6, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS")); // Updated
                                                                                // Date
            for (String issuePk : inputBean.getCloseIssuePkArr()) {
                pStmt.setInt(7, CommonMethods.cInt(issuePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "closeRelatedIssue", e);
            ranOk = false;
        }

        return ranOk;
    }

    public static String findDupeScheduledDate(Connection conn, SupportBean inputBean, int projectPk) {
        StringBuilder sqlStmt = new StringBuilder(
                "SELECT issue_pk FROM issue WHERE project_fk = ? AND ship_fk = ? AND dept = ? AND support_visit_date = ?");
        if (CommonMethods.cInt(inputBean.getIssuePk()) > -1)
            sqlStmt.append(" AND issue_pk <> ?");

        String issuePk = null;

        if (projectPk == -1)
            return null;
        if (CommonMethods.isEmpty(inputBean.getShipPk()) || CommonMethods.isEmpty(inputBean.getDept())
                || CommonMethods.isEmpty(inputBean.getSupportVisitDate()))
            return null;

        debugLog("SQL", "findDupeScheduledDate", "shipPk = " + inputBean.getShipPk() + ", dept = " + inputBean.getDept()
                + ", supportVisitDate = " + inputBean.getSupportVisitDate());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            pStmt.setString(2, inputBean.getShipPk());
            pStmt.setString(3, inputBean.getDept());
            CommonMethods.setDate(pStmt, 4, inputBean.getSupportVisitDate());
            if (CommonMethods.cInt(inputBean.getIssuePk()) > -1)
                pStmt.setInt(5, CommonMethods.cInt(inputBean.getIssuePk()));
            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                issuePk = rs.getString(1);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "findDupeScheduledDate", e);
        }

        return issuePk;
    }

    public static ArrayList<ChartBean> getSummaryByMonthList(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT strftime('%Y%m', opened_date) AS yyyymm, COUNT(1) AS issue_cnt, SUM(closed_date IS NULL) AS open_issue_cnt FROM issue_vw WHERE project_fk = ?");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" GROUP BY strftime('%Y%m', opened_date)");

        ArrayList<ChartBean> resultList = new ArrayList<ChartBean>();
        HashMap<String, String> issueCntMap = new HashMap<String, String>();
        HashMap<String, String> openIssueCntMap = new HashMap<String, String>();

        debugLog("SQL", "getSummaryByMonthList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                issueCntMap.put(rs.getString("yyyymm"), String.valueOf(rs.getInt("issue_cnt") - rs.getInt("open_issue_cnt")));
                openIssueCntMap.put(rs.getString("yyyymm"), rs.getString("open_issue_cnt"));
            }

            int startYear = CommonMethods.cInt(CommonMethods.getDate("YYYY", -(30 * 9))); // 9
                                                                                          // months
                                                                                          // back
            int startMonth = CommonMethods.cInt(CommonMethods.getDate("MM", -(30 * 9))); // 9
                                                                                         // months
                                                                                         // back

            for (int year = startYear; year <= CommonMethods.cInt(CommonMethods.getDate("YYYY")); year++) {
                for (int month = (year == startYear ? startMonth : 1); month <= (year == CommonMethods.cInt(CommonMethods.getDate("YYYY"))
                        ? CommonMethods.cInt(CommonMethods.getDate("MM"))
                        : 12); month++) {
                    ChartBean resultBean = new ChartBean();
                    resultBean.setLabel(CommonMethods.getMonthNameShort(month - 1) + " " + year);
                    resultBean.setValue(CommonMethods.nvl(issueCntMap.get(year + CommonMethods.padString(month, "0", 2)), "0"));
                    resultBean.setValue2(CommonMethods.nvl(openIssueCntMap.get(year + CommonMethods.padString(month, "0", 2)), "0"));
                    resultBean.setValue3(
                            String.valueOf(CommonMethods.cInt(resultBean.getValue()) + CommonMethods.cInt(resultBean.getValue2())));
                    resultList.add(resultBean);
                }
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getSummaryByMonthList", e);
        }

        return resultList;
    }

    public static ArrayList<SupportBean> getSummaryByShipList(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT uic, ship_name, type, hull, homeport, COUNT(1) AS issue_cnt, SUM(closed_date IS NULL) AS open_issue_cnt, "
                        + "SUM(support_visit_date IS NOT NULL AND status IN ('5 - Closed', '6 - Closed (Successful)')) AS visit_cnt, "
                        + "SUM(CASE WHEN category = 'LOGCOP' THEN 1 ELSE 0 END) AS logcop_cnt, "
                        + "SUM(CASE WHEN category = 'FACET DB' THEN 1 ELSE 0 END) AS facet_cnt, "
                        + "SUM(CASE WHEN category = 'Kofax' THEN 1 ELSE 0 END) AS kofax_cnt, "
                        + "SUM(CASE WHEN category = 'Administrative Receipt Tool' THEN 1 ELSE 0 END) AS dummy_cnt, "
                        + "SUM(CASE WHEN category = 'FACET Update' THEN 1 ELSE 0 END) AS update_cnt, "
                        + "SUM(CASE WHEN category = 'Laptop' THEN 1 ELSE 0 END) AS laptop_cnt, "
                        + "SUM(CASE WHEN category = 'Follow-Up Training' THEN 1 ELSE 0 END) AS training_cnt, "
                        + "SUM(CASE WHEN category = 'Backfile' THEN 1 ELSE 0 END) AS backfile_cnt, "
                        + "SUM(CASE WHEN category NOT IN ('LOGCOP', 'FACET DB', 'Kofax', 'Administrative Receipt Tool', 'FACET Update', 'Laptop', 'Follow-Up Training', 'Backfile') THEN 1 ELSE 0 END) AS other_cnt "
                        + "FROM issue_vw WHERE project_fk = ?");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" GROUP BY ship_fk, ship_name, type, hull, homeport ORDER BY issue_cnt DESC, ship_name");

        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();
        HashMap<String, SupportBean> lastVisitMap = SupportModel.getLastVisitMap(conn);

        debugLog("SQL", "getSummaryByShipList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                        + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setIssueCnt(rs.getString("issue_cnt"));
                resultBean.setOpenIssueCnt(rs.getString("open_issue_cnt"));
                resultBean.setSupportVisitCnt(rs.getInt("visit_cnt") > 0 ? rs.getString("visit_cnt") : "-");
                resultBean.setLogcopCnt(rs.getInt("logcop_cnt") > 0 ? rs.getString("logcop_cnt") : "-");
                resultBean.setFacetCnt(rs.getInt("facet_cnt") > 0 ? rs.getString("facet_cnt") : "-");
                resultBean.setKofaxCnt(rs.getInt("kofax_cnt") > 0 ? rs.getString("kofax_cnt") : "-");
                resultBean.setDummyCnt(rs.getInt("dummy_cnt") > 0 ? rs.getString("dummy_cnt") : "-");
                resultBean.setUpdateCnt(rs.getInt("update_cnt") > 0 ? rs.getString("update_cnt") : "-");
                resultBean.setLaptopCnt(rs.getInt("laptop_cnt") > 0 ? rs.getString("laptop_cnt") : "-");
                resultBean.setTrainingCnt(rs.getInt("training_cnt") > 0 ? rs.getString("training_cnt") : "-");
                resultBean.setBackfileCnt(rs.getInt("backfile_cnt") > 0 ? rs.getString("backfile_cnt") : "-");
                resultBean.setOtherCnt(rs.getInt("other_cnt") > 0 ? rs.getString("other_cnt") : "-");

                SupportBean lastVisitBean = lastVisitMap.get(rs.getString("uic"));
                if (lastVisitBean != null) {
                    resultBean.setSupportVisitDate(lastVisitBean.getSupportVisitDate());
                    resultBean.setCategory(lastVisitBean.getCategory());
                }

                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getSummaryByShipList", e);
        }

        return resultList;
    }

    public static HashMap<String, SupportBean> getSummaryByShipMap(Connection conn, int projectPk) {
        String sqlStmt = "SELECT " + "uic, " + "COUNT(1) AS issue_cnt, " + "SUM(closed_date IS NULL) AS open_issue_cnt, "
                + "SUM(support_visit_date IS NOT NULL AND status IN ('5 - Closed', '6 - Closed (Successful)')) AS visit_cnt "
                + "FROM issue_vw " + "WHERE project_fk = ? " + "GROUP BY uic";
        HashMap<String, SupportBean> resultMap = new HashMap<String, SupportBean>();
        HashMap<String, SupportBean> lastVisitMap = SupportModel.getLastVisitMap(conn);

        debugLog("SQL", "getSummaryByShipMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssueCnt(rs.getString("issue_cnt"));
                resultBean.setOpenIssueCnt(rs.getString("open_issue_cnt"));
                resultBean.setSupportVisitCnt(rs.getInt("visit_cnt") > 0 ? rs.getString("visit_cnt") : "0");

                SupportBean lastVisitBean = lastVisitMap.get(rs.getString("uic"));
                if (lastVisitBean != null) {
                    resultBean.setSupportVisitDate(lastVisitBean.getSupportVisitDate());
                    resultBean.setCategory(lastVisitBean.getCategory());
                }

                resultMap.put(rs.getString("uic"), resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getSummaryByShipMap", e);
        }

        return resultMap;
    }

    public static SupportBean getTotalByShipBean(Connection conn, int projectPk, String contractNumber) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT ship_name, type, hull, homeport, COUNT(1) AS issue_cnt, SUM(closed_date IS NULL) AS open_issue_cnt, SUM(support_visit_date IS NOT NULL AND status IN ('5 - Closed', '6 - Closed (Successful)')) AS visit_cnt, SUM(CASE WHEN category = 'LOGCOP' THEN 1 ELSE 0 END) AS logcop_cnt, SUM(CASE WHEN category = 'FACET DB' THEN 1 ELSE 0 END) AS facet_cnt, SUM(CASE WHEN category = 'Kofax' THEN 1 ELSE 0 END) AS kofax_cnt, SUM(CASE WHEN category = 'Administrative Receipt Tool' THEN 1 ELSE 0 END) AS dummy_cnt, SUM(CASE WHEN category = 'FACET Update' THEN 1 ELSE 0 END) AS update_cnt, SUM(CASE WHEN category = 'Laptop' THEN 1 ELSE 0 END) AS laptop_cnt, SUM(CASE WHEN category = 'Follow-Up Training' THEN 1 ELSE 0 END) AS training_cnt, SUM(CASE WHEN category = 'Backfile' THEN 1 ELSE 0 END) AS backfile_cnt, SUM(CASE WHEN category NOT IN ('LOGCOP', 'FACET DB', 'Kofax', 'Administrative Receipt Tool', 'FACET Update', 'Laptop', 'Follow-Up Training', 'Backfile') THEN 1 ELSE 0 END) AS other_cnt FROM issue_vw WHERE project_fk = ?");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        SupportBean resultBean = new SupportBean();

        debugLog("SQL", "getTotalByShipBean", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setIssueCnt(rs.getString("issue_cnt"));
                resultBean.setOpenIssueCnt(rs.getString("open_issue_cnt"));
                resultBean.setSupportVisitCnt(rs.getInt("visit_cnt") > 0 ? rs.getString("visit_cnt") : "-");
                resultBean.setLogcopCnt(rs.getInt("logcop_cnt") > 0 ? rs.getString("logcop_cnt") : "-");
                resultBean.setFacetCnt(rs.getInt("facet_cnt") > 0 ? rs.getString("facet_cnt") : "-");
                resultBean.setKofaxCnt(rs.getInt("kofax_cnt") > 0 ? rs.getString("kofax_cnt") : "-");
                resultBean.setDummyCnt(rs.getInt("dummy_cnt") > 0 ? rs.getString("dummy_cnt") : "-");
                resultBean.setUpdateCnt(rs.getInt("update_cnt") > 0 ? rs.getString("update_cnt") : "-");
                resultBean.setLaptopCnt(rs.getInt("laptop_cnt") > 0 ? rs.getString("laptop_cnt") : "-");
                resultBean.setTrainingCnt(rs.getInt("training_cnt") > 0 ? rs.getString("training_cnt") : "-");
                resultBean.setBackfileCnt(rs.getInt("backfile_cnt") > 0 ? rs.getString("backfile_cnt") : "-");
                resultBean.setOtherCnt(rs.getInt("other_cnt") > 0 ? rs.getString("other_cnt") : "-");
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getTotalByShipBean", e);
        }

        return resultBean;
    }

    public static HashMap<String, Integer> getCategorySummaryMap(Connection conn, int daysBack, int projectPk) {
        String sqlStmt = "SELECT category, COUNT(1) AS issue_cnt FROM issue_vw WHERE project_fk = ? AND opened_date >= date('now', '-10 hours', ?) GROUP BY category";
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();

        debugLog("SQL", "getCategorySummaryMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            pStmt.setString(2, -daysBack + " days");

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultMap.put(rs.getString(1), rs.getInt(2));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getCategorySummaryMap", e);
        }

        return resultMap;
    }

    public static ArrayList<CalendarBean> getReportCalendarList(Connection conn, SupportBean inputBean, int month, int year) {
        ArrayList<CalendarBean> resultList = new ArrayList<CalendarBean>();
        HashMap<String, ArrayList<CalendarItemBean>> schedTrainingMap = getShipVisitScheduleMap(conn, inputBean, month, year);
        HashMap<String, List<CalendarItemBean>> ptoTravelMap = UserModel.getPtoTravelMap(conn, month, year);
        HashMap<String, String> holidayMap = CommonMethods.getHolidayMap();

        int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
        int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));

        int i = 0;
        while (i * 7 + 1 - startDow + 1 <= daysInMonth) {
            ArrayList<CalendarBean> dayList = new ArrayList<CalendarBean>();
            for (int j = 1; j <= 7; j++) {
                CalendarBean dayBean = new CalendarBean();
                int day = i * 7 + j - startDow + 1;
                dayBean.setDate(day >= 1 && day <= daysInMonth ? String.valueOf(day) : null);
                dayBean.setHoliday(holidayMap.get(month + "/" + day + "/" + year));

                // Scheduled ship visit or training scheduled
                if (schedTrainingMap.get(CommonMethods.padString(day, "0", 2)) != null) {
                    ArrayList<CalendarItemBean> lineItemList = new ArrayList<CalendarItemBean>();
                    for (CalendarItemBean calendarItemBean : schedTrainingMap.get(CommonMethods.padString(day, "0", 2))) {
                        lineItemList.add(calendarItemBean);
                    }
                    dayBean.setLineItemList(lineItemList);
                }

                // PTO or travel line item
                if (ptoTravelMap.get(CommonMethods.padString(day, "0", 2)) != null) {
                    ArrayList<CalendarItemBean> ptoTravelList = new ArrayList<CalendarItemBean>();
                    for (CalendarItemBean calendarItemBean : ptoTravelMap.get(CommonMethods.padString(day, "0", 2))) {
                        ptoTravelList.add(calendarItemBean);
                    }
                    dayBean.setPtoTravelList(ptoTravelList);
                }

                dayList.add(dayBean);
            }
            CalendarBean weekBean = new CalendarBean();
            weekBean.setDayList(dayList);
            resultList.add(weekBean);
            i++;
        }

        return resultList;
    }

    private static HashMap<String, ArrayList<CalendarItemBean>> getShipVisitScheduleMap(Connection conn, SupportBean inputBean, int month,
            int year) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT day, visit_time, category, category_fk, ship_name, type, hull, location, trainer FROM ship_visit_schedule_vw WHERE month = ? AND year = ?");

        // WHERE
        if (!CommonMethods.isEmpty(inputBean.getContractNumber()))
            sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

        // ORDER BY
        sqlStmt.append(" ORDER by day, visit_time");

        HashMap<String, ArrayList<CalendarItemBean>> resultMap = new HashMap<String, ArrayList<CalendarItemBean>>();

        debugLog("SQL", "getShipVisitScheduleMap", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setString(1, CommonMethods.padString(month, "0", 2));
            pStmt.setString(2, String.valueOf(year));
            if (!CommonMethods.isEmpty(inputBean.getContractNumber()))
                pStmt.setString(3, inputBean.getContractNumber());
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                CalendarItemBean resultBean = new CalendarItemBean();
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                        + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));

                resultBean.setLocation(rs.getString("location"));

                if (!CommonMethods.isEmpty(rs.getString("trainer"))) {
                    resultBean.setComments("Trainer: " + rs.getString("trainer"));
                }

                if (!CommonMethods.isEmpty(rs.getString("visit_time")))
                    resultBean.setTime(CommonMethods.padString(rs.getString("visit_time"), "0", 4));

                // Determine CSS style
                switch (rs.getString("category")) {
                case "SUPPORT":
                    resultBean.setUrl("issue.do?id=" + rs.getString("category_fk"));
                    resultBean.setCssStyle("color:#eaa228;");
                    break;
                case "TRAINING":
                    resultBean.setUrl("training.do?action=workflowEdit&trainingWorkflowPk=" + rs.getString("category_fk"));
                    resultBean.setCssStyle("color:#4bb2c5;");
                    break;
                }

                // Add item to HashMap
                if (resultMap.get(rs.getString("day")) == null)
                    resultMap.put(rs.getString("day"), new ArrayList<CalendarItemBean>());
                resultMap.get(rs.getString("day")).add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipVisitScheduleMap", e);
        }

        return resultMap;
    }

    public static ArrayList<TrainingBean> getEstSchedShipList(Connection conn, int month, int year) {
        String sqlStmt = "SELECT training_workflow_pk, ship_name, type, hull, IFNULL(sched_training_loc, homeport) AS location FROM training_workflow_vw WHERE sched_training_date IS NULL AND actual_training_date IS NULL AND est_training_month = ? ORDER BY ship_name";
        ArrayList<TrainingBean> resultList = new ArrayList<TrainingBean>();

        debugLog("SQL", "getEstSchedShipList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, CommonMethods.getMonthNameShort(month - 1) + " " + year);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                TrainingBean resultBean = new TrainingBean();
                resultBean.setTrainingWorkflowPk(rs.getString("training_workflow_pk"));
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name"))
                        + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setSchedTrainingLoc(rs.getString("location"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getEstSchedShipList", e);
        }

        return resultList;
    }

    public static SupportBean getLastVisitBean(Connection conn, String uic) {
        String sqlStmt = "SELECT issue_pk, support_visit_date_fmt, support_visit_loc, category FROM issue_vw WHERE uic = ? AND support_visit_date IS NOT NULL AND status IN ('5 - Closed', '6 - Closed (Successful)') ORDER BY support_visit_date DESC";
        SupportBean resultBean = new SupportBean();

        debugLog("SQL", "getLastVisitBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, uic);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setSupportVisitDate(rs.getString("support_visit_date_fmt"));
                resultBean.setSupportVisitLoc(rs.getString("support_visit_loc"));
                resultBean.setCategory(rs.getString("category"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getLastVisitBean", e);
        }

        return resultBean;
    }

    public static HashMap<String, SupportBean> getLastVisitMap(Connection conn) {
        String sqlStmt = "SELECT uic, issue_pk, support_visit_date_fmt, support_visit_loc, category FROM issue_vw WHERE support_visit_date IS NOT NULL ORDER BY support_visit_date ASC";
        HashMap<String, SupportBean> resultMap = new HashMap<String, SupportBean>();

        debugLog("SQL", "getLastVisitMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setSupportVisitDate(rs.getString("support_visit_date_fmt"));
                resultBean.setSupportVisitLoc(rs.getString("support_visit_loc"));
                resultBean.setCategory(rs.getString("category"));
                resultMap.put(rs.getString("uic"), resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getLastVisitMap", e);
        }

        return resultMap;
    }

    public static ArrayList<SupportBean> getUpcomingVisitList(Connection conn, String uic) {
        String sqlStmt = "SELECT issue_pk, support_visit_date_fmt, support_visit_time, support_visit_loc, trainer, category "
                + "FROM issue_vw WHERE uic = ? AND support_visit_date IS NOT NULL " + ISSUE_NOT_CLOSED + " ORDER BY support_visit_date ASC";
        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();

        debugLog("SQL", "getUpcomingVisitList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, uic);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setSupportVisitDate(rs.getString("support_visit_date_fmt"));
                resultBean.setSupportVisitTime(rs.getString("support_visit_time"));
                resultBean.setSupportVisitLoc(rs.getString("support_visit_loc"));
                resultBean.setTrainer(rs.getString("trainer"));
                resultBean.setCategory(rs.getString("category"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getUpcomingVisitList", e);
        }

        return resultList;
    }

    public static ArrayList<SupportBean> getAtoSummaryList(Connection conn, int projectPk) {
        String sqlStmt = "SELECT ato_pk, ato_filename, " + "IFNULL(opened_date_fmt, opened_date) AS opened_date_fmt, "
                + "total_cnt, applied_cnt, last_updated_date_fmt " + "FROM ato_summary_vw " + "WHERE project_fk = ? "
                + "ORDER BY ato_date DESC";
        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();

        debugLog("SQL", "getAtoSummaryList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setAtoPk(rs.getString("ato_pk"));
                resultBean.setAtoFilename(rs.getString("ato_filename"));
                resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                resultBean.setTotalCnt(rs.getString("total_cnt"));
                resultBean.setAppliedCnt(rs.getString("applied_cnt"));
                resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoSummaryList", e);
        }

        return resultList;
    }

    public static SupportBean getAtoSummaryBean(Connection conn, int atoPk) {
        String sqlStmt = "SELECT ato_date_fmt, ato_filename, comments, IFNULL(opened_date_fmt, opened_date) AS opened_date_fmt, "
                + "total_cnt, applied_cnt, last_updated_date_fmt " + "FROM ato_summary_vw " + "WHERE ato_pk = ?";
        SupportBean resultBean = new SupportBean();

        debugLog("SQL", "getAtoSummaryBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, atoPk);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setAtoPk(String.valueOf(atoPk));
                resultBean.setAtoDate(rs.getString("ato_date_fmt"));
                resultBean.setAtoFilename(rs.getString("ato_filename"));
                resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                String[] commentsArr = new String[1];
                commentsArr[0] = rs.getString("comments");
                resultBean.setCommentsArr(commentsArr);
                resultBean.setTotalCnt(rs.getString("total_cnt"));
                resultBean.setAppliedCnt(rs.getString("applied_cnt"));
                resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoSummaryBean", e);
        }

        return resultBean;
    }

    public static ArrayList<SupportBean> getAtoDetailList(Connection conn, int atoPk) {
        String sqlStmt = "SELECT issue_pk, ship_fk, uic, ship_name, type, hull, homeport, status, IFNULL(opened_date_fmt, opened_date) AS opened_date_fmt,"
                + "IFNULL(closed_date_fmt, closed_date) AS closed_date_fmt " + "FROM ato_detail_vw " + "WHERE ato_pk = ? "
                + "ORDER BY ship_name DESC";
        ArrayList<SupportBean> resultList = new ArrayList<SupportBean>();
        HashMap<String, ArrayList<UserBean>> pocMap = ShipModel.getPocMap(conn);

        debugLog("SQL", "getAtoDetailList", sqlStmt + " (ato_pk = " + atoPk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, atoPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SupportBean resultBean = new SupportBean();
                resultBean.setIssuePk(rs.getString("issue_pk"));
                resultBean.setShipPk(rs.getString("ship_fk"));
                resultBean.setUic(rs.getString("uic"));
                String shipNamePostfix = !CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                        ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                        : "";
                resultBean.setShipName(rs.getString("ship_name") + shipNamePostfix);
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setOpenedDate(rs.getString("opened_date_fmt"));
                resultBean.setClosedDate(rs.getString("closed_date_fmt"));

                if (!CommonMethods.isEmpty(rs.getString("uic"))) {
                    resultBean.setPrimaryPocEmails(ShipModel.getPrimaryPocEmails(pocMap.get(rs.getString("uic"))));
                    resultBean.setPocEmails(ShipModel.getPocEmails(pocMap.get(rs.getString("uic"))));
                }

                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoDetailList", e);
        }

        return resultList;
    }

    private static int getInsertedAtoPk(Connection conn) {
        String sqlStmt = "SELECT MAX(ato_pk) FROM ato";
        int returnVal = -1;

        debugLog("SQL", "getInsertedAtoPk", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnVal = rs.getInt(1);
        }
        catch (Exception e) {
            debugLog("ERROR", "getInsertedAtoPk", e);
        }

        return returnVal;
    }

    public static boolean insertAto(Connection conn, SupportBean supportBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO ato (project_fk, ato_date, comments, opened_date, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?)";
        boolean ranOk = false;
        int newPk = -1;

        debugLog("SQL", "insertAto", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(supportBean.getProjectPk()));
            CommonMethods.setDate(pStmt, 2, supportBean.getAtoDate());
            pStmt.setString(3, supportBean.getCommentsArr()[0]);
            CommonMethods.setDate(pStmt, 4, supportBean.getOpenedDate());
            pStmt.setString(5, loginBean.getFullName());
            pStmt.setString(6, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            ranOk = (pStmt.executeUpdate() == 1);

            // Get inserted issue pk to use for creating child records
            newPk = getInsertedAtoPk(conn);
            supportBean.setAtoPk(String.valueOf(newPk));

            // Set fields for bulk support issues
            supportBean.setTitle(
                    "Monthly ATO Maintenance Release - ATOUpdates_" + CommonMethods.getDate(supportBean.getAtoDate(), "YYYYMMDD"));
            supportBean.setStatus("2 - Active");
            supportBean.setCategory("ATO Maintenance Release");
            supportBean.setOpenedBy(loginBean.getFullName());
            supportBean.setPersonAssigned("Other"); // Default person assigned
                                                    // is 'other' per Darby
                                                    // Meyer 2017-11-28

            ranOk = ranOk && (newPk > -1) && bulkInsertIssue(conn, supportBean, loginBean) >= 0;
        }
        catch (Exception e) {
            debugLog("ERROR", "insertAto", e);
            ranOk = false;
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean updateAto(Connection conn, SupportBean supportBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE ato SET ato_date = ?, comments = ?, opened_date = ?, last_updated_by = ?, last_updated_date = ? WHERE ato_pk = ?";
        boolean ranOk = false;

        debugLog("SQL", "updateAto", sqlStmt + " (ato_pk = " + supportBean.getAtoPk() + ")");
        if (CommonMethods.cInt(supportBean.getAtoPk()) <= -1)
            return false;

        debugLog("SQL", "updateAto", sqlStmt + " (ato_pk = " + supportBean.getAtoPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setDate(pStmt, 1, supportBean.getAtoDate());
            pStmt.setString(2, supportBean.getCommentsArr()[0]);
            CommonMethods.setDate(pStmt, 3, supportBean.getOpenedDate());
            pStmt.setString(4, loginBean.getFullName());
            pStmt.setString(5, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(6, CommonMethods.cInt(supportBean.getAtoPk()));

            // Set fields for bulk insert support issues
            supportBean.setTitle(
                    "Monthly ATO Maintenance Release - ATOUpdates_" + CommonMethods.getDate(supportBean.getAtoDate(), "YYYYMMDD"));
            supportBean.setStatus("2 - Active");
            supportBean.setCategory("ATO Maintenance Release");
            supportBean.setOpenedBy(loginBean.getFullName());
            supportBean.setPersonAssigned("Other"); // Default person assigned
                                                    // is 'other' per Darby
                                                    // Meyer 2017-11-28

            ranOk = (pStmt.executeUpdate() == 1) && bulkInsertIssue(conn, supportBean, loginBean) >= 0;

            // Set fields for bulk apply (close) support issues
            supportBean.setStatus("6 - Closed (Successful)");
            supportBean.setClosedDate(CommonMethods.getDate("YYYY-MM-DD"));
            supportBean.setResolution("Closed by ATO Update module");
            supportBean.setTotalTime("15");

            ranOk &= bulkInsertIssueComments(conn, supportBean.getReminderIssuePkArr(), "Reminder e-mail sent via ATO Update module",
                    loginBean) && bulkUpdateIssue(conn, supportBean, supportBean.getAppliedIssuePkArr(), loginBean)
                    && bulkRemoveIssue(conn, supportBean.getRemoveIssuePkArr(), loginBean);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateAto", e);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean deleteAto(Connection conn, SupportBean inputBean, LoginBean loginBean) {
        String sqlStmt = "DELETE FROM ato WHERE ato_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getAtoPk()) <= -1)
            return false;

        debugLog("SQL", "deleteAto", sqlStmt + " (ato_pk = " + inputBean.getAtoPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getAtoPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteAto", e);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static ArrayList<String> getAtoInstalledList(Connection conn, int shipPk) {
        String sqlStmt = "SELECT ship_fk, ato_filename FROM ato_detail_vw WHERE ship_fk = ? AND status = '6 - Closed (Successful)' ORDER BY ato_filename";
        ArrayList<String> resultList = new ArrayList<String>();
        debugLog("SQL", "getAtoInstalledList", sqlStmt);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultList.add(rs.getString("ato_filename"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoInstalledList", e);
        }
        return resultList;
    }

    public static HashMap<String, ArrayList<String>> getAtoInstalledMap(Connection conn) {
        String sqlStmt = "SELECT ship_fk, ato_filename FROM ato_detail_vw WHERE status = '6 - Closed (Successful)' ORDER BY ship_fk, ato_filename";
        HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

        debugLog("SQL", "getAtoInstalledMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (resultMap.get(rs.getString("ship_fk")) == null)
                    resultMap.put(rs.getString("ship_fk"), new ArrayList<String>());
                resultMap.get(rs.getString("ship_fk")).add(rs.getString("ato_filename"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoInstalledMap", e);
        }

        return resultMap;
    }

    public static HashMap<String, ArrayList<String>> getAtoMissingMap(Connection conn) {
        String sqlStmt = "SELECT ship_fk, ato_filename FROM ato_detail_vw WHERE status <> '6 - Closed (Successful)' ORDER BY ship_fk, ato_filename";
        HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

        debugLog("SQL", "getAtoMissingMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (resultMap.get(rs.getString("ship_fk")) == null)
                    resultMap.put(rs.getString("ship_fk"), new ArrayList<String>());
                resultMap.get(rs.getString("ship_fk")).add(rs.getString("ato_filename"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAtoMissingMap", e);
        }

        return resultMap;
    }

    public static void cleanConfiguredSystemList(ArrayList<SystemBean> configuredSystemList, ArrayList<SupportBean> atoDetailList) {
        ArrayList<String> shipPkList = new ArrayList<String>();
        for (SupportBean atoDetailBean : atoDetailList) {
            shipPkList.add(atoDetailBean.getShipPk());
        }

        int i = 0;
        while (i < configuredSystemList.size()) {
            if (CommonMethods.isIn(shipPkList, configuredSystemList.get(i).getShipPk())) {
                configuredSystemList.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public static void cleanConfiguredSystemList2(ArrayList<ConfiguredSystem> configuredSystemList, ArrayList<SupportBean> atoDetailList) {
        ArrayList<String> shipPkList = new ArrayList<String>();
        for (SupportBean atoDetailBean : atoDetailList) {
            shipPkList.add(atoDetailBean.getShipPk());
        }

        int size = configuredSystemList.size();
        // Set<String> shipPkSet = new HashSet<>(shipPkList);
        for (int i = 0; i < size; i++) {
            Ship ship = configuredSystemList.get(i).getShip();
            if (ship != null && shipPkList.contains(ship.getId() + "")) {
                configuredSystemList.remove(i--);
                size--;
            }
        }
    }

    public static ArrayList<ShipBean> getShipWithIssueList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_fk, uic, ship_name, type, hull FROM issue_vw WHERE ship_fk IS NOT NULL "
                + "ORDER BY ship_name IS NULL OR ship_name='', ship_name";
        ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

        debugLog("SQL", "getShipWithIssueList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ShipBean resultBean = new ShipBean();
                resultBean.setShipPk(rs.getString("ship_fk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setShipName(
                        rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipWithIssueList", e);
        }

        return resultList;
    }

    public static ArrayList<String> getSupportTeamList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT person_assigned FROM issue WHERE person_assigned IS NOT NULL " + " UNION ALL "
                + "SELECT DISTINCT trainer FROM issue WHERE trainer IS NOT NULL";

        String[] defaultArr = { "Amanda Crabtree-Loo", "Lloyd Sueyoshi", "Chivas Nousianen", "Corey Kelley", "Darby Meyer", "Esmil Feliz",
                "Joshua Crabtree", "Michael Fernandez", "Norman Newson", "Rob Hardisty", "Shandale Graham", "Tiffaney Scott",
                "Miracle Leao", "Russell Houlton", "Steve Brennen", "Other" };
        ArrayList<String> resultList = new ArrayList<String>(Arrays.asList(defaultArr));

        debugLog("SQL", "getSupportTeamList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (!CommonMethods.isIn(resultList, rs.getString(1)))
                    resultList.add(rs.getString(1));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getSupportTeamList", e);
        }

        return CommonMethods.arraySort(resultList);
    }

    public static String getIssueCntByDayJson(Connection conn, int projectPk, String contractNumber) {
        StringBuilder sqlStmt = new StringBuilder("SELECT " + "IFNULL(closed_date_fmt, closed_date) AS closed_date_fmt, COUNT(1) "
                + "FROM issue_vw " + "WHERE project_fk = ? "
                + "AND category NOT IN ('FACET Update', 'ATO Maintenance Release', 'DMS Release', 'DACS Inactivity', 'DACS Missing Transmittals', 'LOGCOP Inactivity', 'LOGCOP Missing Transmittals')");
        if (!CommonMethods.isEmpty(contractNumber))
            sqlStmt.append(" AND uic IN (SELECT uic FROM configured_system_vw WHERE contract_number = ?)");
        sqlStmt.append(" GROUP BY closed_date_fmt");

        StringBuilder returnStr = new StringBuilder();

        returnStr.append("[");

        if (projectPk == -1)
            return null;

        debugLog("SQL", "getIssueCntByDayJson", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);
            if (!CommonMethods.isEmpty(contractNumber))
                pStmt.setString(2, contractNumber);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (returnStr.length() > 1)
                    returnStr.append(",");
                returnStr.append("{\"date\":\"" + rs.getString(1) + "\",\"value\":" + rs.getString(2) + "}");
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getIssueCntByDayJson", e);
        }

        returnStr.append("]");

        return returnStr.toString();
    }

    /**
     * This code doesn't work.
     * @param conn
     * @param supportBean
     * @param loginBean
     * @return
     */
    @Deprecated
    protected static boolean updateConfiguredSystemVersion(Connection conn, SupportBean supportBean, LoginBean loginBean) {
        if (!CommonMethods.nes(supportBean.getStatus()).equals("6 - Closed (Successful)")) {
            return true;
        }
        if (CommonMethods.nvl(supportBean.getUpdateConfiguredSystemInd(), "N").equals("N")) {
            return true;
        }
        String categoryName = getCategoryName(conn, supportBean.getIssueCategoryFk());
        if (
            (!categoryName.equals("OS Update") && !categoryName.equals("FACET Update"))
            || (categoryName.equals("OS Update") && CommonMethods.isEmpty(supportBean.getUpdateOsVersion()))
            || (categoryName.equals("FACET Update") && CommonMethods.isEmpty(supportBean.getUpdateFacetVersion()))
        ) {
            //returning true here means, "Moving on, we don't need to update."
            return true;
        }

        StringBuffer sqlStmt = new StringBuffer("UPDATE configured_system SET");
        if (categoryName.equals("OS Update")) {
            sqlStmt.append(" os_version = ?");
        }
        else if (categoryName.equals("FACET Update")) {
            sqlStmt.append(" facet_version = ?");
        }
        sqlStmt.append(", last_updated_by = ?, last_updated_date = ? WHERE configured_system_pk = ?");
        boolean ranOk = true;
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            if (categoryName.equals("OS Update")) {
                pStmt.setString(1, supportBean.getUpdateOsVersion());
                debugLog("SQL", "updateConfiguredSystemVersion",
                        sqlStmt.toString() + String.format(" (os_version: %s)", new Object[] { supportBean.getUpdateOsVersion() }));
            }
            else if (categoryName.equals("FACET Update")) {
                pStmt.setString(1, supportBean.getUpdateFacetVersion());
                debugLog("SQL", "updateConfiguredSystemVersion", sqlStmt.toString()
                        + String.format(" (facet_version: %s)", new Object[] { supportBean.getUpdateFacetVersion() }));
            }
            pStmt.setString(2, loginBean.getFullName());
            pStmt.setString(3, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            String shipUic = ShipModel.getShipUic(conn, CommonMethods.cInt(supportBean.getShipPk()));
            ArrayList<SystemBean> configuredSystemList = SystemModel.getConfiguredSystemListByUic(conn, shipUic);
            int rsCnt = 0;
            for (SystemBean systemBean : configuredSystemList) {
                String configuredSystemPk = systemBean.getConfiguredSystemPk();
                debugLog("INFO", "updateConfiguredSystemVersion",
                        String.format("Executing configured_system_pk: %s", configuredSystemPk));
                pStmt.setInt(4, CommonMethods.cInt(configuredSystemPk));
                pStmt.addBatch();
                rsCnt += 1;
            }
            ranOk &= rsCnt == 1;
        }
        catch (Exception e) {
            debugLog("ERROR", "updateConfiguredSystemVersion", e);
            ranOk = false;
        }
        finally {
            try {
                if (ranOk) conn.commit();
                else conn.rollback();
            } catch (Exception e) { }
            try { conn.setAutoCommit(true); } catch (Exception e) { }
        }
        return ranOk;
    }

    public static List<SqlColumn> getIssueSqlColumns() {
        //"category" is deprecated
        List<SqlColumn> columns = new ArrayList<>();
        int index = 1;
        columns.add(new SqlColumn(index++, "project_fk", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "ship_fk", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "title", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "description", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "status", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "priority", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "category", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "phase", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "opened_by", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "opened_date", ColumnType.DATETIME));
        columns.add(new SqlColumn(index++, "closed_date", ColumnType.DATETIME));
        columns.add(new SqlColumn(index++, "person_assigned", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "support_visit_date", ColumnType.DATE));
        columns.add(new SqlColumn(index++, "support_visit_loc", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "support_visit_time", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "trainer", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "resolution", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "total_time", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "created_by", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "created_date", ColumnType.DATETIME));
        columns.add(new SqlColumn(index++, "last_updated_by", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "last_updated_date", ColumnType.DATETIME));
        columns.add(new SqlColumn(index++, "initiated_by", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "dept", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "is_email_sent", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "is_email_responded", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "is_training_provided", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "is_training_onsite", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "ato_fk", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "auto_close_date", ColumnType.DATE));
        columns.add(new SqlColumn(index++, "issue_category_fk", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "priority_reason", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "support_visit_end_time", ColumnType.INTEGER));
        columns.add(new SqlColumn(index++, "support_visit_reason", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "laptop_issue", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "scanner_issue", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "software_issue", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "support_visit_loc_notes", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "auto_close_status", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "update_facet_version", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "update_os_version", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "update_configured_system_ind", ColumnType.TEXT));
        columns.add(new SqlColumn(index++, "configured_system_fk", ColumnType.INTEGER));
        return columns;
    }

    public static Issue saveIssue(IssueService issueService, IssueCommentsService issueCommentsService, SupportBean supportBean, Connection conn, LoginBean loginBean, String uploadDir) throws Exception {
        Issue issue = saveIssue(issueService, supportBean);
        if (issue != null && insertIssueComments(conn, supportBean, loginBean)
            && insertIssueFiles(conn, supportBean, loginBean, uploadDir)
            && insertRelatedIssue(conn, supportBean, loginBean) 
            && closeRelatedIssue(conn, supportBean, loginBean)
        ) {
            return issue;
        }
        return null;
    }

    public static boolean insertIssueComments(IssueCommentsService issueCommentsService, SupportBean supportBean, Issue issue) {
        String[] commentsArr = supportBean.getCommentsArr();
        for (String comments : commentsArr) {
            IssueComments issueComment = new IssueComments();
            issueComment.setComments(comments);
            issueComment.setIssueFk(issue.getId());
            if (!issueCommentsService.save(issueComment)) {
                return false;
            }
        }
        return true;
    }

    public static Issue saveIssue(IssueService issueService, SupportBean supportBean) {
        if (supportBean == null) {
            return null;
        }
        Issue issue = new Issue();
        issue.setId(StringUtils.parseInt(supportBean.getIssuePk()));
        issue.setAtoFk(StringUtils.parseInt(supportBean.getAtoPk()));
        issue.setProjectFk(StringUtils.parseInt(supportBean.getProjectPk()));
        issue.setShipFk(StringUtils.parseInt(supportBean.getShipPk()));
        issue.setTitle(supportBean.getTitle());
        issue.setDescription(supportBean.getDescription());
        issue.setStatus(supportBean.getStatus());
        issue.setPriority(supportBean.getPriority());
        issue.setIssueCategoryFk(StringUtils.parseInt(supportBean.getIssueCategoryFk()));
        issue.setPhase(supportBean.getPhase());
        issue.setOpenedBy(supportBean.getOpenedBy());
        issue.setOpenedDateStr(supportBean.getOpenedDate());
        issue.setClosedDateStr(supportBean.getClosedDate());
        issue.setPersonAssigned(supportBean.getPersonAssigned());
        issue.setSupportVisitDateStr(supportBean.getSupportVisitDate());
        issue.setSupportVisitLoc(supportBean.getSupportVisitLoc());
        issue.setSupportVisitTime(StringUtils.parseInt(supportBean.getSupportVisitTime()));
        issue.setTrainer(supportBean.getTrainer());
        issue.setResolution(supportBean.getResolution());
        issue.setTotalTime(StringUtils.parseInt(supportBean.getTotalTime()));
        issue.setInitiatedBy(supportBean.getInitiatedBy());
        issue.setDept(supportBean.getDept());
        issue.setIsEmailSent(supportBean.getIsEmailSent());
        issue.setIsEmailResponded(supportBean.getIsEmailResponded());
        issue.setIsTrainingProvided(supportBean.getIsTrainingProvided());
        issue.setIsTrainingOnsite(supportBean.getIsTrainingOnsite());
        issue.setAutoCloseDateStr(supportBean.getAutoCloseDate());
        issue.setIssueCategoryFk(StringUtils.parseInt(supportBean.getIssueCategoryFk()));
        issue.setPriorityReason(supportBean.getPriorityReason());
        issue.setSupportVisitEndTime(StringUtils.parseInt(supportBean.getSupportVisitEndTime()));
        issue.setSupportVisitReason(supportBean.getSupportVisitReason());
        issue.setLaptopIssue(supportBean.getLaptopIssue());
        issue.setScannerIssue(supportBean.getScannerIssue());
        issue.setSoftwareIssue(supportBean.getSoftwareIssue());
        issue.setSupportVisitLocNotes(supportBean.getSupportVisitLocNotes());
        issue.setAutoCloseStatus(supportBean.getAutoCloseStatus());
        issue.setConfiguredSystemFk(supportBean.getConfiguredSystemFk());
        issue.setCreatedBy(supportBean.getCreatedBy());
        issue.setCreatedDateStr(supportBean.getCreatedDate());
        issue.setLastUpdatedBy(supportBean.getLastUpdatedBy());
        if (issueService.save(issue)) {
            supportBean.setIssuePk(issue.getId() + "");
            return issue;
        }
        return null;
    }
}
