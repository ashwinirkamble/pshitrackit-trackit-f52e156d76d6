package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's PROJECT module
 */
public class ProjectModel {
    private static Logger logger = Logger.getLogger(ProjectModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%9s%-30s | %-34s | %s", "", type, functionName, statement));
        } else if (type.equals("ERROR")) {
            logger.error(String.format("%9s%-30s | %-34s | %s", "", type, functionName, statement));
        } else {
            logger.debug(String.format("%9s%-30s | %-34s | %s", "", type, functionName, statement));
        }
    }

    private static ArrayList<String> getStrList(Connection conn, String sqlStmt) {
        ArrayList<String> resultList = new ArrayList<String>();

        //debugLog("SQL", "getStrList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog("ERROR", "getStrList", e);
        }

        return resultList;
    }

    private static ArrayList<String> getStrList(Connection conn, String sqlStmt, int projectPk) {
        ArrayList<String> resultList = new ArrayList<String>();

        //debugLog("SQL", "getStrList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog("ERROR", "getStrList", e);
        }

        return resultList;
    }

    public static ArrayList<String> getPersonAssignedList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT person_assigned FROM task WHERE project_fk = ? AND person_assigned IS NOT NULL ORDER BY person_assigned", projectPk);
    }

    public static ArrayList<String> getCategoryList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT category FROM task WHERE project_fk = ? AND category IS NOT NULL ORDER BY category", projectPk);
    }

    public static ArrayList<String> getStatusList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT status FROM task WHERE status IS NOT NULL ORDER BY status");
    }

    public static ArrayList<String> getSourceList(Connection conn, int projectPk) {
        return getStrList(conn, "SELECT DISTINCT source FROM task WHERE project_fk = ? AND source IS NOT NULL ORDER BY source", projectPk);
    }

    public static ArrayList<String> getPriorityList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT priority FROM task WHERE priority IS NOT NULL ORDER BY priority");
    }

    public static ArrayList<ProjectBean> getProjectList(Connection conn) {
        return getProjectList(conn, null);
    }

    public static ArrayList<ProjectBean> getProjectList(Connection conn, LoginBean loginBean) {
        StringBuffer sqlStmt = new StringBuffer("SELECT project_pk, project_name, description, customer, current_task_cnt, completed_task_cnt "
            + "FROM project_vw");

        if (loginBean != null) sqlStmt.append(" WHERE project_pk IN (SELECT project_fk from user_project WHERE user_fk = ?)");

        sqlStmt.append(" ORDER BY customer, project_name");
        ArrayList<ProjectBean> resultList = new ArrayList<ProjectBean>();

        //debugLog("SQL", "getProjectList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            if (loginBean != null) pStmt.setInt(1, CommonMethods.cInt(loginBean.getUserPk()));

            ResultSet rs = pStmt.executeQuery();
            ProjectBean parentBean = null;
            String currCustomer = null;
            while (rs.next()) {
                if (!rs.getString("customer").equals(currCustomer)) {
                    if (parentBean != null) resultList.add(parentBean);
                    parentBean = new ProjectBean();
                    parentBean.setCustomer(rs.getString("customer"));
                    parentBean.setTaskList(new ArrayList<ProjectBean>());
                    currCustomer = parentBean.getCustomer();
                }

                ProjectBean resultBean = new ProjectBean();
                resultBean.setProjectPk(rs.getString("project_pk"));
                resultBean.setProjectName(rs.getString("project_name"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrentTaskCnt(rs.getString("current_task_cnt"));
                resultBean.setCompletedTaskCnt(rs.getString("completed_task_cnt"));
                parentBean.getTaskList().add(resultBean);
            }

            if (parentBean != null) resultList.add(parentBean);

        } catch (Exception e) {
            debugLog("ERROR", "getProjectList", e);
        }

        return resultList;
    }

    public static ProjectBean getProjectBean(Connection conn, int projectPk) {
        String sqlStmt = "SELECT project_pk, project_name, description, customer, current_task_cnt FROM project_vw WHERE project_pk = ?";
        ProjectBean resultBean = new ProjectBean();

        if (projectPk <= -1) return null;

        debugLog("SQL", "getProjectBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultBean.setProjectPk(rs.getString("project_pk"));
                resultBean.setProjectName(rs.getString("project_name"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrentTaskCnt(rs.getString("current_task_cnt"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getProjectBean", e);
        }

        return resultBean;
    }

    public static boolean insertProject(Connection conn, ProjectBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO project (project_name, description, customer, created_by) VALUES (?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertProject", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProjectName());
            CommonMethods.setString(pStmt, 2, inputBean.getDescription());
            pStmt.setString(3, inputBean.getCustomer());
            CommonMethods.setInt(pStmt, 4, loginBean.getUserPk());
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "insertProject", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteProject(Connection conn, ProjectBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM project WHERE project_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getProjectPk()) <= -1) return false;

        debugLog("SQL", "deleteProject", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getProjectPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteProject", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    public static ArrayList<ProjectBean> getShipTaskList(Connection conn, int projectPk, String uic) {
        ProjectBean inputBean = new ProjectBean();
        inputBean.setUic(uic);
        return getTaskList(conn, projectPk, inputBean, "due_date", "ASC");
    }

    public static ArrayList<ProjectBean> getTaskList(Connection conn, int projectPk, ProjectBean inputBean, String sortBy, String sortDir) {
        StringBuffer sqlStmt = new StringBuffer("SELECT task_pk, project_fk, title, description, status, priority, category, ship_name, type, hull, homeport, person_assigned, sub_tasks, notes, strftime('%m/%d/%Y', created_date) AS created_date_fmt, strftime('%m/%d/%Y', due_date) AS due_date_fmt, strftime('%m/%d/%Y', completed_date) AS completed_date_fmt, quarter_year, effort_area, loe, effort_type, doc_notes, version_included, is_client_approved, client_priority, is_pshi_approved, recommendation, doc_updated_ind, deployed_date_fmt, staff_meeting_ind, client_meeting_ind, contract_number, last_updated_by, strftime('%m/%d/%Y %H:%M:%S', last_updated_date) AS last_updated_date_fmt FROM task_vw WHERE project_fk = ?");
        ArrayList<ProjectBean> resultList = new ArrayList<ProjectBean>();

        if (projectPk <= -1) return null;

        //Optional WHERE Variables
        if (!CommonMethods.isEmpty(inputBean.getSearchTitleDescription())) sqlStmt.append(" AND (title LIKE ? OR description LIKE ?)");
        //if (!CommonMethods.isEmpty(inputBean.getStatus())) sqlStmt.append(" AND status = ?"); else sqlStmt.append(" AND (status IS NULL OR status <> 'Completed')");
        if (!CommonMethods.isEmpty(inputBean.getPriority())) sqlStmt.append(" AND priority = ?");
        if (!CommonMethods.isEmpty(inputBean.getPersonAssigned())) sqlStmt.append(" AND person_assigned = ?");
        if (!CommonMethods.isEmpty(inputBean.getCategory())) sqlStmt.append(" AND category = ?");
        if (!CommonMethods.isEmpty(inputBean.getDueDateStart())) sqlStmt.append(" AND due_date >= ?");
        if (!CommonMethods.isEmpty(inputBean.getDueDateEnd())) sqlStmt.append(" AND due_date <= ?");
        if (!CommonMethods.isEmpty(inputBean.getUic())) sqlStmt.append(" AND uic = ?");
        if (!CommonMethods.isEmpty(inputBean.getHomeport())) sqlStmt.append(" AND homeport = ?");
        if (!CommonMethods.isEmpty(inputBean.getNotes())) sqlStmt.append(" AND notes LIKE ?");
        if (!CommonMethods.isEmpty(inputBean.getSearchSubTask())) sqlStmt.append(" AND task_pk IN (SELECT task_fk FROM sub_task WHERE description LIKE ?)");
        if (!CommonMethods.isEmpty(inputBean.getQuarterYear())) sqlStmt.append(" AND quarter_year = ?");
        if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND contract_number = ?");

        int length = inputBean.getSearchStatusArr().length;
        if (length == 0) {
            sqlStmt.append(" AND status NOT IN ('Completed', 'Resolved', 'Not Needed')"); //default
        } else {
            sqlStmt.append(" AND status IN (");
            boolean firstElem = true;
            for (int i = 0; i < length; i++) {
                sqlStmt.append((!firstElem ? "," : "") + "?");
                firstElem = false;
            }
            sqlStmt.append(")");
        }

        if (inputBean.getEffortAreaArr().length > 0) {
            sqlStmt.append(" AND (");
            for (int i = 0; i < inputBean.getEffortAreaArr().length; i++) {
                if (i > 0) sqlStmt.append(" OR ");
                sqlStmt.append("INSTR(effort_area, ?) > 0");
            }
            sqlStmt.append(")");
        }

        if (inputBean.getSearchMeetingArr().length > 0) {
            sqlStmt.append(" AND (");
            if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Staff")) sqlStmt.append("staff_meeting_ind = 'Y'");
            if (inputBean.getSearchMeetingArr().length == 2) sqlStmt.append(" OR ");
            if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Client")) sqlStmt.append("client_meeting_ind = 'Y'");
            sqlStmt.append(")");
        }

        //ORDER BY
        switch (sortBy + "_" + sortDir) {
            case "task_pk_ASC":                         sqlStmt.append(" ORDER BY task_pk DESC");break; //Inverted
            case "task_pk_DESC":                         sqlStmt.append(" ORDER BY task_pk ASC");break; //Inverted
            case "person_assigned_ASC":     sqlStmt.append(" ORDER BY person_assigned IS NULL OR person_assigned='', person_assigned ASC");break;
            case "person_assigned_DESC":    sqlStmt.append(" ORDER BY person_assigned IS NULL OR person_assigned='', person_assigned DESC");break;
            case "category_ASC":               sqlStmt.append(" ORDER BY category IS NULL OR category='', category ASC, due_date, title");break;
            case "category_DESC":              sqlStmt.append(" ORDER BY category IS NULL OR category='', category DESC, due_date, title");break;
            case "status_ASC":                   sqlStmt.append(" ORDER BY status ASC, due_date, title");break;
            case "status_DESC":                  sqlStmt.append(" ORDER BY status DESC, due_date, title");break;
            case "priority_ASC":                   sqlStmt.append(" ORDER BY priority IS NULL OR priority='', priority ASC, due_date, title");break;
            case "priority_DESC":                  sqlStmt.append(" ORDER BY priority IS NULL OR priority='', priority DESC, due_date, title");break;
            case "ship_name_ASC":                 sqlStmt.append(" ORDER BY ship_name IS NULL OR ship_name='', ship_name ASC");break;
            case "ship_name_DESC":                sqlStmt.append(" ORDER BY ship_name IS NULL OR ship_name='', ship_name DESC");break;
            case "created_date_ASC":        sqlStmt.append(" ORDER BY created_date IS NULL OR created_date='', created_date ASC");break;
            case "created_date_DESC":       sqlStmt.append(" ORDER BY created_date IS NULL OR created_date='', created_date DESC");break;
            case "due_date_ASC":            sqlStmt.append(" ORDER BY due_date IS NULL OR due_date='', due_date ASC");break;
            case "due_date_DESC":           sqlStmt.append(" ORDER BY due_date IS NULL OR due_date='', due_date DESC");break;
            case "completed_date_ASC":      sqlStmt.append(" ORDER BY completed_date IS NULL OR completed_date='', completed_date ASC");break;
            case "completed_date_DESC":     sqlStmt.append(" ORDER BY completed_date IS NULL OR completed_date='', completed_date DESC");break;
            case "quarter_year_ASC":          sqlStmt.append(" ORDER BY quarter_year IS NULL OR quarter_year='', quarter_year ASC");break;
            case "quarter_year_DESC":         sqlStmt.append(" ORDER BY quarter_year IS NULL OR quarter_year='', quarter_year DESC");break;
            case "effort_area_ASC":              sqlStmt.append(" ORDER BY effort_area IS NULL OR effort_area='', effort_area ASC");break;
            case "effort_area_DESC":             sqlStmt.append(" ORDER BY effort_area IS NULL OR effort_area='', effort_area DESC");break;
            case "staff_meeting_ind_ASC":     sqlStmt.append(" ORDER BY staff_meeting_ind DESC");break; //Inverted Y/N
            case "staff_meeting_ind_DESC":     sqlStmt.append(" ORDER BY staff_meeting_ind ASC");break; //Inverted Y/N
            case "client_meeting_ind_ASC":     sqlStmt.append(" ORDER BY client_meeting_ind DESC");break; //Inverted Y/N
            case "client_meeting_ind_DESC": sqlStmt.append(" ORDER BY client_meeting_ind ASC");break; //Inverted Y/N
            case "last_updated_date_ASC":   sqlStmt.append(" ORDER BY last_updated_date IS NULL OR last_updated_date='', last_updated_date DESC");break; //Inverted
            case "last_updated_date_DESC":  sqlStmt.append(" ORDER BY last_updated_date IS NULL OR last_updated_date='', last_updated_date ASC");break; //Inverted
        }

        debugLog("SQL", "getTaskList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, projectPk);

            //Optional WHERE Variables
            int i = 2;

            if (!CommonMethods.isEmpty(inputBean.getSearchTitleDescription())) {pStmt.setString(i++, "%" + inputBean.getSearchTitleDescription() + "%"); pStmt.setString(i++, "%" + inputBean.getSearchTitleDescription() + "%"); }
            //replaced with searchStatusArr if (!CommonMethods.isEmpty(inputBean.getStatus()))                 pStmt.setString(i++, inputBean.getStatus());
            if (!CommonMethods.isEmpty(inputBean.getPriority()))             pStmt.setString(i++, inputBean.getPriority());
            if (!CommonMethods.isEmpty(inputBean.getPersonAssigned())) pStmt.setString(i++, inputBean.getPersonAssigned());
            if (!CommonMethods.isEmpty(inputBean.getCategory()))             pStmt.setString(i++, inputBean.getCategory());
            if (!CommonMethods.isEmpty(inputBean.getDueDateStart()))     pStmt.setString(i++, CommonMethods.getDate(inputBean.getDueDateStart(), "YYYY-MM-DD"));
            if (!CommonMethods.isEmpty(inputBean.getDueDateEnd()))         pStmt.setString(i++, CommonMethods.getDate(inputBean.getDueDateEnd(), "YYYY-MM-DD"));
            if (!CommonMethods.isEmpty(inputBean.getUic()))                     pStmt.setString(i++, inputBean.getUic());
            if (!CommonMethods.isEmpty(inputBean.getHomeport()))             pStmt.setString(i++, inputBean.getHomeport());
            if (!CommonMethods.isEmpty(inputBean.getNotes()))                 pStmt.setString(i++, "%" + inputBean.getNotes() + "%");
            if (!CommonMethods.isEmpty(inputBean.getSearchSubTask())) pStmt.setString(i++, "%" + inputBean.getSearchSubTask() + "%");
            if (!CommonMethods.isEmpty(inputBean.getQuarterYear()))     pStmt.setString(i++, inputBean.getQuarterYear());
            if (!CommonMethods.isEmpty(inputBean.getContractNumber()))     pStmt.setString(i++, inputBean.getContractNumber());

            if (length > 0) {
                for (String searchStatus : inputBean.getSearchStatusArr()) {
                    pStmt.setString(i++, searchStatus);
                }
            }

            for (String effortArea : inputBean.getEffortAreaArr()) {
                pStmt.setString(i++, effortArea);
            }

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ProjectBean resultBean = new ProjectBean();
                resultBean.setTaskPk(rs.getString("task_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setCategory(rs.getString("category"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setPriority(rs.getString("priority"));
                //if (!CommonMethods.isEmpty(rs.getString("ship_name"))) resultBean.setShipName(rs.getString("ship_name") + " (" + rs.getString("type") + " " + rs.getString("hull") + ")");
                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setPersonAssigned(rs.getString("person_assigned"));
                resultBean.setCreatedDate(rs.getString("created_date_fmt"));
                resultBean.setDueDate(rs.getString("due_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setSubTasks(rs.getString("sub_tasks"));

                if (!resultBean.getStatus().equals("Completed") && CommonMethods.isValidDateStr(resultBean.getDueDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getDueDate()) <= 0) {
                    resultBean.setDueDateCss("color:#f00;");
                }

                resultBean.setQuarterYear(rs.getString("quarter_year"));
                resultBean.setEffortArea(rs.getString("effort_area"));
                if (!CommonMethods.isEmpty(resultBean.getEffortArea())) resultBean.setEffortAreaArr(resultBean.getEffortArea().split(", "));
                resultBean.setLoe(rs.getString("loe"));
                resultBean.setEffortType(rs.getString("effort_type"));
                resultBean.setDocNotes(rs.getString("doc_notes"));
                resultBean.setVersionIncluded(rs.getString("version_included"));
                resultBean.setIsClientApproved(CommonMethods.nvl(rs.getString("is_client_approved"), "N"));
                resultBean.setClientPriority(rs.getString("client_priority"));
                resultBean.setIsPshiApproved(CommonMethods.nvl(rs.getString("is_pshi_approved"), "N"));
                resultBean.setRecommendation(rs.getString("recommendation"));
                resultBean.setDocUpdatedInd(CommonMethods.nvl(rs.getString("doc_updated_ind"), "N"));
                resultBean.setDeployedDate(rs.getString("deployed_date_fmt"));

                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCompletedDate(rs.getString("completed_date_fmt"));
                resultBean.setStaffMeetingInd(rs.getString("staff_meeting_ind"));
                resultBean.setClientMeetingInd(rs.getString("client_meeting_ind"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getTaskList", e);
        }

        return resultList;
    }

    public static ProjectBean getTaskBean(Connection conn, ProjectBean inputBean) {
        String sqlStmt = "SELECT task_pk, project_fk, title, description, category, source, status, priority, uic, ship_name, type, hull, homeport, person_assigned, is_internal, sub_tasks, notes, created_by, created_date_fmt, follow_up_date_fmt, due_date_fmt, completed_date_fmt, effort_area, loe, effort_type, quarter_year, doc_notes, version_included, is_client_approved, client_priority, is_pshi_approved, recommendation, doc_updated_ind, deployed_date_fmt, staff_meeting_ind, client_meeting_ind, contract_number, last_updated_by, last_updated_date_fmt FROM task_vw WHERE task_pk = ?";
        ProjectBean resultBean = new ProjectBean();

        if (CommonMethods.cInt(inputBean.getTaskPk().trim()) <= -1) return null;

        debugLog("SQL", "getTaskBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk().trim()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setTaskPk(rs.getString("task_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setCategory(rs.getString("category"));
                resultBean.setCurrCategory(rs.getString("category"));
                resultBean.setSource(rs.getString("source"));
                resultBean.setCurrSource(rs.getString("source"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setCurrStatus(rs.getString("status"));
                resultBean.setPriority(rs.getString("priority"));
                resultBean.setCurrPriority(rs.getString("priority"));
                resultBean.setUic(rs.getString("uic"));
                if (!CommonMethods.isEmpty(rs.getString("ship_name"))) resultBean.setShipName(rs.getString("ship_name") + " (" + rs.getString("type") + " " + rs.getString("hull") + ")");
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setPersonAssigned(rs.getString("person_assigned"));
                resultBean.setCurrPersonAssigned(rs.getString("person_assigned"));
                resultBean.setIsInternal(rs.getString("is_internal"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCreatedBy(rs.getString("created_by"));
                resultBean.setCreatedDate(rs.getString("created_date_fmt"));
                resultBean.setFollowUpDate(rs.getString("follow_up_date_fmt"));
                resultBean.setDueDate(rs.getString("due_date_fmt"));

                if (!resultBean.getStatus().equals("Completed") && CommonMethods.isValidDateStr(resultBean.getDueDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getDueDate()) <= 0) {
                    resultBean.setDueDateCss("color:#f00;");
                }

                resultBean.setQuarterYear(rs.getString("quarter_year"));
                resultBean.setEffortArea(rs.getString("effort_area"));
                if (!CommonMethods.isEmpty(resultBean.getEffortArea())) resultBean.setEffortAreaArr(resultBean.getEffortArea().split(", "));
                resultBean.setLoe(rs.getString("loe"));
                resultBean.setEffortType(rs.getString("effort_type"));
                resultBean.setDocNotes(rs.getString("doc_notes"));
                resultBean.setVersionIncluded(rs.getString("version_included"));
                resultBean.setIsClientApproved(CommonMethods.nvl(rs.getString("is_client_approved"), "N"));
                resultBean.setClientPriority(rs.getString("client_priority"));
                resultBean.setIsPshiApproved(CommonMethods.nvl(rs.getString("is_pshi_approved"), "N"));
                resultBean.setRecommendation(rs.getString("recommendation"));
                resultBean.setDocUpdatedInd(CommonMethods.nvl(rs.getString("doc_updated_ind"), "N"));
                resultBean.setDeployedDate(rs.getString("deployed_date_fmt"));

                resultBean.setCurrContractNumber(rs.getString("contract_number"));
                resultBean.setContractNumber(rs.getString("contract_number"));

                resultBean.setCompletedDate(rs.getString("completed_date_fmt"));
                resultBean.setStaffMeetingInd(rs.getString("staff_meeting_ind"));
                resultBean.setClientMeetingInd(rs.getString("client_meeting_ind"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(rs.getString("last_updated_date_fmt"));
                resultBean.setSubTasks(rs.getString("sub_tasks")); //Old plain text data entry
                resultBean.setSubTaskList(getSubTaskList(conn, rs.getInt("task_pk")));
                resultBean.setTaskFileList(getTaskFileList(conn, rs.getInt("task_pk")));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getTaskBean", e);
        }

        return resultBean;
    }

    public static String getCurrRecordStr(ArrayList<ProjectBean> taskList, int taskPk) {
        String returnStr = null;

        int i = 0;
        while (i < taskList.size() && returnStr == null) {
            if (CommonMethods.cInt(((ProjectBean)taskList.get(i)).getTaskPk()) == taskPk) returnStr = "Record " + (i+1) + " of " + taskList.size();
            i++;
        }

        return returnStr;
    }

    public static ProjectBean getPrevTaskBean(ArrayList<ProjectBean> taskList, int taskPk) {
        ProjectBean resultBean = null;

        int i = 0;
        while (i < taskList.size() && resultBean == null) {
            if (CommonMethods.cInt(((ProjectBean)taskList.get(i)).getTaskPk()) == taskPk && i-1 >= 0) resultBean = taskList.get(i-1);
            i++;
        }

        return resultBean;
    }

    public static ProjectBean getNextTaskBean(ArrayList<ProjectBean> taskList, int taskPk) {
        ProjectBean resultBean = null;

        int i = 0;
        while (i < taskList.size() && resultBean == null) {
            if (CommonMethods.cInt(((ProjectBean)taskList.get(i)).getTaskPk()) == taskPk && i+1 < taskList.size()) resultBean = taskList.get(i+1);
            i++;
        }

        return resultBean;
    }

    private static int getInsertedTaskPk(Connection conn) {
        String sqlStmt = "SELECT MAX(task_pk) FROM task";
        int returnVal = -1;

        debugLog("SQL", "getInsertedTaskPk", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnVal = rs.getInt(1);
        } catch (Exception e) {
            debugLog("ERROR", "getInsertedTaskPk", e);
        }

        return returnVal;
    }

    public static int insertTask(Connection conn, ProjectBean inputBean, LoginBean loginBean, String uploadDir) throws Exception {
        String sqlStmt = "INSERT INTO task (title, project_fk, description, category, source, status, priority, uic, person_assigned, is_internal, sub_tasks, notes, follow_up_date, due_date, completed_date, effort_area, loe, effort_type, quarter_year, doc_notes, version_included, is_client_approved, client_priority, is_pshi_approved, recommendation, doc_updated_ind, deployed_date, staff_meeting_ind, client_meeting_ind, contract_number, last_updated_by, last_updated_date, created_by, created_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int newTaskPk = -1;
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getProjectPk()) <= -1) throw new Exception("No project_pk provided");

        debugLog("SQL", "insertTask", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getTitle());
            pStmt.setInt(2, CommonMethods.cInt(inputBean.getProjectPk()));
            CommonMethods.setString(pStmt, 3, inputBean.getDescription().trim());
            CommonMethods.setString(pStmt, 4, inputBean.getCategory());
            CommonMethods.setString(pStmt, 5, inputBean.getSource());
            CommonMethods.setString(pStmt, 6, inputBean.getStatus());
            CommonMethods.setString(pStmt, 7, inputBean.getPriority());
            CommonMethods.setString(pStmt, 8, inputBean.getUic());
            CommonMethods.setString(pStmt, 9, inputBean.getPersonAssigned());
            pStmt.setString(10, CommonMethods.nvl(inputBean.getIsInternal(), "N"));
            CommonMethods.setString(pStmt, 11, inputBean.getSubTasks().trim());
            CommonMethods.setString(pStmt, 12, inputBean.getNotes().trim());
            CommonMethods.setDate(pStmt, 13, inputBean.getFollowUpDate());
            CommonMethods.setDate(pStmt, 14, inputBean.getDueDate());

            if ((inputBean.getStatus().equals("Completed") || inputBean.getStatus().equals("Not Needed")) && !CommonMethods.isEmpty(inputBean.getCompletedDate())) {
                pStmt.setString(15, CommonMethods.getDate(inputBean.getCompletedDate(), "YYYY-MM-DD"));
            } else {
                pStmt.setNull(15, java.sql.Types.DATE);
            }

            if (inputBean.getCategory().equals("Future Requests")) {
                CommonMethods.setString(pStmt, 16, CommonMethods.printArray(inputBean.getEffortAreaArr(), ", ", ""));
                CommonMethods.setString(pStmt, 17, inputBean.getLoe().trim());
                CommonMethods.setString(pStmt, 18, inputBean.getEffortType());
                CommonMethods.setString(pStmt, 19, inputBean.getQuarterYear());
                CommonMethods.setString(pStmt, 20, inputBean.getDocNotes().trim());
                CommonMethods.setString(pStmt, 21, inputBean.getVersionIncluded().trim());
                CommonMethods.setString(pStmt, 22, CommonMethods.nvl(inputBean.getIsClientApproved(), "N"));
                CommonMethods.setString(pStmt, 23, inputBean.getClientPriority());
                CommonMethods.setString(pStmt, 24, CommonMethods.nvl(inputBean.getIsPshiApproved(), "N"));
                CommonMethods.setString(pStmt, 25, inputBean.getRecommendation());
                pStmt.setString(26, CommonMethods.nvl(inputBean.getDocUpdatedInd(), "N"));
                CommonMethods.setDate(pStmt, 27, inputBean.getDeployedDate());
            } else {
                pStmt.setNull(16, java.sql.Types.VARCHAR);
                pStmt.setNull(17, java.sql.Types.VARCHAR);
                pStmt.setNull(18, java.sql.Types.VARCHAR);
                pStmt.setNull(19, java.sql.Types.VARCHAR);
                pStmt.setNull(20, java.sql.Types.VARCHAR);
                pStmt.setNull(21, java.sql.Types.VARCHAR);
                pStmt.setNull(22, java.sql.Types.VARCHAR);
                pStmt.setNull(23, java.sql.Types.VARCHAR);
                pStmt.setNull(24, java.sql.Types.VARCHAR);
                pStmt.setNull(25, java.sql.Types.VARCHAR);
                pStmt.setNull(26, java.sql.Types.VARCHAR);
                pStmt.setNull(27, java.sql.Types.VARCHAR);
            }

            pStmt.setString(28, CommonMethods.nvl(inputBean.getStaffMeetingInd(), "N"));
            pStmt.setString(29, CommonMethods.nvl(inputBean.getClientMeetingInd(), "N"));
            CommonMethods.setString(pStmt, 30, inputBean.getContractNumber());
            pStmt.setString(31, loginBean.getFullName());
            pStmt.setString(32, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setString(33, loginBean.getFullName());
            CommonMethods.setDate(pStmt, 34, CommonMethods.nvl(inputBean.getCreatedDate(), CommonMethods.getDate("MM/DD/YYYY")));
            ranOk = (pStmt.executeUpdate() == 1);

            //Get inserted task pk to use for creating sub-tasks
            newTaskPk = getInsertedTaskPk(conn);
            inputBean.setTaskPk(String.valueOf(newTaskPk));

            ranOk &= insertSubTasks(conn, inputBean, loginBean) && insertTaskFiles(conn, inputBean, loginBean, uploadDir);
        } catch (Exception e) {
            debugLog("ERROR", "insertTask", e);
            ranOk = false;
            throw e;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk ? newTaskPk : -1;
    }

    public static boolean updateTask(Connection conn, ProjectBean inputBean, LoginBean loginBean, String uploadDir) throws Exception {
        String sqlStmt = "UPDATE task SET title = ?, description = ?, category = ?, source = ?, status = ?, priority = ?, uic = ?, person_assigned = ?, is_internal = ?, sub_tasks = ?, notes = ?, follow_up_date = ?, due_date = ?, completed_date = ?, effort_area = ?, loe = ?, effort_type = ?, quarter_year = ?, doc_notes = ?, version_included = ?, is_client_approved = ?, client_priority = ?, is_pshi_approved = ?, recommendation = ?, doc_updated_ind = ?, deployed_date = ?, staff_meeting_ind = ?, client_meeting_ind = ?, contract_number = ?, created_date = ?, last_updated_by = ?, last_updated_date = ? WHERE task_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) throw new Exception("No task_pk provided");

        //RULE :: Darby 2015-09-22: add "Documentation Updated" with values of "Yes" or "N/A" and don't allow task to be Completed without a value selected in the field
        if (inputBean.getCategory().equals("Future Requests") && inputBean.getStatus().equals("Completed") && CommonMethods.nvl(inputBean.getDocUpdatedInd(), "N").equals("N")) throw new Exception("Future Requests marked as completed must have the doc_updated_ind set to Y or N/A");

        debugLog("SQL", "updateTask", sqlStmt + " (taskPk = " + inputBean.getTaskPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getTitle());
            CommonMethods.setString(pStmt, 2, inputBean.getDescription().trim());
            CommonMethods.setString(pStmt, 3, inputBean.getCategory());
            CommonMethods.setString(pStmt, 4, inputBean.getSource());
            CommonMethods.setString(pStmt, 5, inputBean.getStatus());
            CommonMethods.setString(pStmt, 6, inputBean.getPriority());
            CommonMethods.setString(pStmt, 7, inputBean.getUic());
            CommonMethods.setString(pStmt, 8, inputBean.getPersonAssigned());
            pStmt.setString(9, CommonMethods.nvl(inputBean.getIsInternal(), "N"));
            CommonMethods.setString(pStmt, 10, inputBean.getSubTasks().trim());
            CommonMethods.setString(pStmt, 11, inputBean.getNotes().trim());
            CommonMethods.setDate(pStmt, 12, inputBean.getFollowUpDate());
            CommonMethods.setDate(pStmt, 13, inputBean.getDueDate());

            if ((inputBean.getStatus().equals("Completed") || inputBean.getStatus().equals("Not Needed")) && !CommonMethods.isEmpty(inputBean.getCompletedDate())) {
                pStmt.setString(14, CommonMethods.getDate(inputBean.getCompletedDate(), "YYYY-MM-DD"));
            } else {
                pStmt.setNull(14, java.sql.Types.DATE);
            }

            if (inputBean.getCategory().equals("Future Requests")) {
                CommonMethods.setString(pStmt, 15, CommonMethods.printArray(inputBean.getEffortAreaArr(), ", ", ""));
                CommonMethods.setString(pStmt, 16, inputBean.getLoe().trim());
                CommonMethods.setString(pStmt, 17, inputBean.getEffortType());
                CommonMethods.setString(pStmt, 18, inputBean.getQuarterYear());
                CommonMethods.setString(pStmt, 19, inputBean.getDocNotes().trim());
                CommonMethods.setString(pStmt, 20, inputBean.getVersionIncluded().trim());
                CommonMethods.setString(pStmt, 21, CommonMethods.nvl(inputBean.getIsClientApproved(), "N"));
                CommonMethods.setString(pStmt, 22, inputBean.getClientPriority());
                CommonMethods.setString(pStmt, 23, CommonMethods.nvl(inputBean.getIsPshiApproved(), "N"));
                CommonMethods.setString(pStmt, 24, inputBean.getRecommendation());
                pStmt.setString(25, CommonMethods.nvl(inputBean.getDocUpdatedInd(), "N"));
                CommonMethods.setDate(pStmt, 26, inputBean.getDeployedDate());
            } else {
                pStmt.setNull(15, java.sql.Types.VARCHAR);
                pStmt.setNull(16, java.sql.Types.VARCHAR);
                pStmt.setNull(17, java.sql.Types.VARCHAR);
                pStmt.setNull(18, java.sql.Types.VARCHAR);
                pStmt.setNull(19, java.sql.Types.VARCHAR);
                pStmt.setNull(20, java.sql.Types.VARCHAR);
                pStmt.setNull(21, java.sql.Types.VARCHAR);
                pStmt.setNull(22, java.sql.Types.VARCHAR);
                pStmt.setNull(23, java.sql.Types.VARCHAR);
                pStmt.setNull(24, java.sql.Types.VARCHAR);
                pStmt.setNull(25, java.sql.Types.VARCHAR);
                pStmt.setNull(26, java.sql.Types.VARCHAR);
            }

            pStmt.setString(27, CommonMethods.nvl(inputBean.getStaffMeetingInd(), "N"));
            pStmt.setString(28, CommonMethods.nvl(inputBean.getClientMeetingInd(), "N"));
            CommonMethods.setString(pStmt, 29, inputBean.getContractNumber());
            CommonMethods.setDate(pStmt, 30, CommonMethods.nvl(inputBean.getCreatedDate(), CommonMethods.getDate("MM/DD/YYYY")));
            pStmt.setString(31, loginBean.getFullName());
            pStmt.setString(32, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(33, CommonMethods.cInt(inputBean.getTaskPk()));
            ranOk = (pStmt.executeUpdate() == 1) && updateSubTasks(conn, inputBean, loginBean) && updateTaskFiles(conn, inputBean, loginBean, uploadDir);
        } catch (Exception e) {
            debugLog("ERROR", "updateTask", e);
            ranOk = false;
            throw e;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteTask(Connection conn, ProjectBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM task WHERE task_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) return false;

        debugLog("SQL", "deleteTask", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteTask", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    public static ArrayList<ProjectBean> getSubTaskList(Connection conn, int taskPk) {
        String sqlStmt = "SELECT sub_task_id, description, person_assigned, status, strftime('%m/%d/%Y', due_date) AS due_date_fmt, strftime('%m/%d/%Y', completed_date) AS completed_date_fmt FROM sub_task WHERE task_fk = ? ORDER BY sub_task_pk";
        ArrayList<ProjectBean> resultList = new ArrayList<ProjectBean>();

        debugLog("SQL", "getSubTaskList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, taskPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ProjectBean resultBean = new ProjectBean();
                resultBean.setSubTaskId(rs.getString("sub_task_id"));
                resultBean.setDescription(rs.getString("description"));
                resultBean.setPersonAssigned(rs.getString("person_assigned"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setDueDate(rs.getString("due_date_fmt"));
                if (!resultBean.getStatus().equals("Completed") && CommonMethods.isValidDateStr(resultBean.getDueDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getDueDate()) <= 0) {
                    resultBean.setDueDateCss("color:#f00;");
                }
                resultBean.setCompletedDate(rs.getString("completed_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getSubTaskList", e);
        }

        return resultList;
    }

    private static boolean updateSubTasks(Connection conn, ProjectBean inputBean, LoginBean loginBean) {
        //get current subtasks
        //email changes in person assigned
        //ArrayList<ProjectBean> origTaskList = getSubTaskList(conn, CommonMethods.cInt(inputBean.getTaskPk()));
        return deleteSubTasks(conn, inputBean) && insertSubTasks(conn, inputBean, loginBean);
    }

    private static boolean insertSubTasks(Connection conn, ProjectBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO sub_task (task_fk, sub_task_id, description, person_assigned, status, due_date, completed_date) VALUES (?,?,?,?,?,?,?)";
        boolean ranOk = true;
        HashMap<String, ArrayList<String>> subTaskNotificationMap = new HashMap<>();

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) return false;

        debugLog("SQL", "insertSubTasks", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk()));

            for (int i = 0; i < inputBean.getDescriptionArr().length; i++) {
                if (!CommonMethods.isEmpty(inputBean.getDescriptionArr()[i])) {
                    pStmt.setInt(2, i);
                    CommonMethods.setString(pStmt, 3, inputBean.getDescriptionArr()[i]);
                    CommonMethods.setString(pStmt, 4, inputBean.getPersonAssignedArr()[i]);
                    CommonMethods.setString(pStmt, 5, inputBean.getStatusArr()[i]);
                    CommonMethods.setDate(pStmt, 6, inputBean.getDueDateArr()[i]);
                    CommonMethods.setDate(pStmt, 7, inputBean.getCompletedDateArr()[i]);
                    ranOk &= (pStmt.executeUpdate() == 1);

                    //Determine email notification for sub-task assignment
                    //Send notification to original assignee if original is not null, original is not same as new, and original is not current user, and original is not main task assignee
                    if (!CommonMethods.isEmpty(inputBean.getOrigPersonAssignedArr()[i]) &&
                    !inputBean.getOrigPersonAssignedArr()[i].equals(inputBean.getPersonAssignedArr()[i]) &&
                    !inputBean.getOrigPersonAssignedArr()[i].equals(loginBean.getFullName()) &&
                    !inputBean.getOrigPersonAssignedArr()[i].equals(inputBean.getPersonAssigned())) {
                        if (CommonMethods.isEmpty(inputBean.getPersonAssignedArr()[i])) { //de-assigned
                            if (subTaskNotificationMap.get(inputBean.getOrigPersonAssignedArr()[i]) == null) subTaskNotificationMap.put(inputBean.getOrigPersonAssignedArr()[i], new ArrayList<String>());
                            subTaskNotificationMap.get(inputBean.getOrigPersonAssignedArr()[i]).add("Sub-task \"" + inputBean.getDescriptionArr()[i] + "\" has been unassigned from you.");
                        } else { //re-assigned
                            if (subTaskNotificationMap.get(inputBean.getOrigPersonAssignedArr()[i]) == null) subTaskNotificationMap.put(inputBean.getOrigPersonAssignedArr()[i], new ArrayList<String>());
                            subTaskNotificationMap.get(inputBean.getOrigPersonAssignedArr()[i]).add("Sub-task \"" + inputBean.getDescriptionArr()[i] + "\" has been re-assigned to " + inputBean.getPersonAssignedArr()[i] + ".");
                        }
                        //if statement for de-assigned or re-assigned
                    }

                    if (!CommonMethods.isEmpty(inputBean.getPersonAssignedArr()[i]) &&
                    !inputBean.getPersonAssignedArr()[i].equals(inputBean.getOrigPersonAssignedArr()[i]) &&
                    !inputBean.getPersonAssignedArr()[i].equals(loginBean.getFullName()) &&
                    !inputBean.getPersonAssignedArr()[i].equals(inputBean.getPersonAssigned())) {
                        if (subTaskNotificationMap.get(inputBean.getPersonAssignedArr()[i]) == null) subTaskNotificationMap.put(inputBean.getPersonAssignedArr()[i], new ArrayList<String>());
                        subTaskNotificationMap.get(inputBean.getPersonAssignedArr()[i]).add("Sub-task \"" + inputBean.getDescriptionArr()[i] + "\" has been assigned to you.");
                    }
                }
            }

            EmailModel emailModel = new EmailModel();
            emailModel.sendSubTaskEmail(conn, subTaskNotificationMap, inputBean, loginBean);
        } catch (Exception e) {
            debugLog("ERROR", "insertSubTasks", e);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean deleteSubTasks(Connection conn, ProjectBean inputBean) {
        String sqlStmt = "DELETE FROM sub_task WHERE task_fk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) return false;

        debugLog("SQL", "deleteSubTasks", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk()));
            pStmt.executeUpdate();
            ranOk = true;
        } catch (Exception e) {
            debugLog("ERROR", "deleteSubTasks", e);
        }

        return ranOk;
    }

    private static ArrayList<FileBean> getTaskFileList(Connection conn, int taskPk) {
        String sqlStmt = "SELECT file_fk, filename, extension, filesize, uploaded_by, strftime('%m/%d/%Y %H:%M:%S', uploaded_date) as uploaded_date_fmt FROM task_file_vw WHERE task_fk = ? ORDER BY filename";
        ArrayList<FileBean> resultList = new ArrayList<FileBean>();

        debugLog("SQL", "getTaskFileList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, taskPk);
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
        } catch (Exception e) {
            debugLog("ERROR", "getTaskFileList", e);
        }

        return resultList;
    }

    private static boolean updateTaskFiles(Connection conn, ProjectBean inputBean, LoginBean loginBean, String uploadDir) {
        return deleteTaskFiles(conn, inputBean, uploadDir) && insertTaskFiles(conn, inputBean, loginBean, uploadDir);
    }

    private static boolean insertTaskFiles(Connection conn, ProjectBean inputBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "INSERT INTO task_file (task_fk, file_fk) VALUES (?,?)";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) return false;
        if (inputBean.getFileList() == null || inputBean.getFileList().size() <= 0) return true; //No files to upload

        debugLog("SQL", "insertTaskFiles", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk()));

            for (FormFile file : inputBean.getFileList()) {
                if (file.getFileSize() > 0) {
                    debugLog("INFO", "insertTaskFiles", "Saving: " + file.getFileName());
                    int newFilePk = FileModel.saveFile(conn, file, loginBean, uploadDir);
                    pStmt.setInt(2, newFilePk);
                    ranOk &= (newFilePk != -1 && pStmt.executeUpdate() == 1);
                }
            }
        } catch (Exception e) {
            debugLog("ERROR", "insertTaskFiles", e);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean deleteTaskFiles(Connection conn, ProjectBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM task_file WHERE task_fk = ? AND file_fk = ?";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getTaskPk()) <= -1) return false;
        if (inputBean.getDeleteFilePkArr().length <= 0) return true; //Nothing to delete

        debugLog("SQL", "deleteTaskFiles", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getTaskPk()));
            for (String filePk : inputBean.getDeleteFilePkArr()) {
                pStmt.setInt(2, CommonMethods.cInt(filePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        } catch (Exception e) {
            debugLog("ERROR", "deleteTaskFiles", e);
            ranOk = false;
        } finally {
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    public static ArrayList<ProjectBean> getUserTaskList(Connection conn, int days, LoginBean loginBean) {
        StringBuffer sqlStmt = new StringBuffer("SELECT task_pk, t.project_fk, t.title, strftime('%m/%d/%Y', t.due_date) AS due_date_fmt "
                + "FROM task_vw t INNER JOIN user_project u ON t.project_fk = u.project_fk AND u.user_fk = ? "
                + "WHERE t.status <> 'Completed' AND t.person_assigned = ?");
        ArrayList<ProjectBean> resultList = new ArrayList<ProjectBean>();

        //Optional WHERE Variables
        if (days < 0) sqlStmt.append(" AND t.due_date < date('now', '-10 hours')");
        else if (days == 0) sqlStmt.append(" AND t.due_date = date('now', '-10 hours')");
        else if (days == 7) sqlStmt.append(" AND t.due_date BETWEEN date('now', '-10 hours', '1 day') AND date('now', '-10 hours', '7 days')");
        else if (days == 31) sqlStmt.append(" AND t.due_date BETWEEN date('now', '-10 hours', '8 days') AND date('now', '-10 hours', '31 days')");

        //ORDER BY
        sqlStmt.append(" ORDER BY t.due_date");

        //debugLog("SQL", "getUserTaskList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, CommonMethods.cInt(loginBean.getUserPk()));
            pStmt.setString(2, loginBean.getFullName());

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ProjectBean resultBean = new ProjectBean();
                resultBean.setTaskPk(rs.getString("task_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setDueDate(rs.getString("due_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getUserTaskList", e);
        }

        return resultList;
    }
    public static ArrayList<ProjectBean> getUserFollowUpTaskList(Connection conn, LoginBean loginBean) {
        String sqlStmt = "SELECT t.task_pk, t.project_fk, t.title, strftime('%m/%d/%Y', t.follow_up_date) AS follow_up_date_fmt FROM task_vw t INNER JOIN user_project u ON t.project_fk = u.project_fk AND u.user_fk = ? WHERE t.status <> 'Completed' AND t.person_assigned = ? AND t.follow_up_date <= date('now', '-10 hours', '7 days') ORDER BY t.follow_up_date";
        ArrayList<ProjectBean> resultList = new ArrayList<ProjectBean>();

        debugLog("SQL", "getUserFollowUpTaskList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(loginBean.getUserPk()));
            pStmt.setString(2, loginBean.getFullName());

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ProjectBean resultBean = new ProjectBean();
                resultBean.setTaskPk(rs.getString("task_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setFollowUpDate(rs.getString("follow_up_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getUserFollowUpTaskList", e);
        }

        return resultList;
    }

    public static ArrayList<String> getContractNumberList(Connection conn, int projectPk) {
        String sqlStmt =
            "SELECT DISTINCT contract_number FROM task WHERE project_fk = ? AND contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM configured_system_vw WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM laptop WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM scanner WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM misc_hardware WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM kofax_license WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM ms_office_license WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM vrs_license WHERE contract_number IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT contract_number FROM misc_license WHERE contract_number IS NOT NULL";

        ArrayList<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getContractNumberList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog("ERROR", "getContractNumberList", e);
        }

        //Add additional contract numbers as specified by Amanda
        if (!CommonMethods.isIn(resultList, "N00604-16-P-3377")) resultList.add("N00604-16-P-3377");

        return CommonMethods.arraySort(resultList);
    }

    public static ArrayList<String> getCustomerList(Connection conn) {
        String sqlStmt =
            "SELECT DISTINCT customer FROM laptop WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM scanner WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM misc_hardware WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM kofax_license WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM ms_office_license WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM vrs_license WHERE customer IS NOT NULL " +
            " UNION " +
            "SELECT DISTINCT customer FROM misc_license WHERE customer IS NOT NULL";

        ArrayList<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getCustomerList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog("ERROR", "getCustomerList", e);
        }

        return resultList;
    }

/*
    public static void writeOutlookCsv(Connection conn, LoginBean loginBean, OutputStream out) throws Exception {
        String sqlStmt = "SELECT title, strftime('%m/%d/%Y', due_date) AS due_date_fmt, priority, description FROM task_vw WHERE person_assigned = ? AND due_date IS NOT NULL";
        String newline = System.getProperty("line.separator");
        String delimiter = "\t";

        debugLog("SQL", "writeExportCsv", sqlStmt + " (" + loginBean.getFullName() + ")");

        try (PreparedStatement stmt = conn.prepareStatement(sqlStmt)) {
            stmt.setString(1, loginBean.getFullName());
            ResultSet rs = stmt.executeQuery();

            String headerRow = "\"Subject\"\t\"Start Date\"\t\"Start Time\"\t\"End Date\"\t\"End Time\"\t\"All day event\"\t\"Reminder on/off\"\t\"Reminder Date\"\t\"Reminder Time\"\t\"Meeting Organizer\"\t\"Required Attendees\"\t\"Optional Attendees\"\t\"Meeting Resources\"\t\"Billing Information\"\t\"Categories\"\t\"Description\"\t\"Location\"\t\"Mileage\"\t\"Priority\"\t\"Private\"\t\"Sensitivity\"\t\"Show time as\"";

            out.write(headerRow.getBytes());
            out.write(newline.getBytes());

            while (rs.next()) {
                out.write(String.valueOf("\"" + CommonMethods.nes(rs.getString("title")) + "\"\t\"" + CommonMethods.nes(rs.getString("due_date_fmt")) + "\"\t\"12:00:00 AM\"\t\"" + CommonMethods.getDate(rs.getString("due_date_fmt"), "MM/DD/YYYY", 1) + "\"\t\"12:00:00 AM\"\t\"True\"\t\"False\"\t\"" + CommonMethods.nes(rs.getString("due_date_fmt")) + "\"\t\"6:00:00 AM\"\t\"" + CommonMethods.nes(loginBean.getFullName()) + "\"\t\t\t\t\t\"" + CommonMethods.nes(rs.getString("priority")) + "\"\t\"" + CommonMethods.nes(rs.getString("description")) + "\"\t\"\"\t\t\"Normal\"\t\"False\"\t\"Normal\"\t\"3\"").getBytes());
                out.write(newline.getBytes());
            }
        } catch (Exception e) {
            debugLog("ERROR", "writeExportCsv", e);
            throw e;
        }
    }
*/
}
