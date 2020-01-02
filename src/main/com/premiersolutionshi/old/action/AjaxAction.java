package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.model.SupportModel;
import com.premiersolutionshi.old.model.SystemModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's AJAX process
 */
public final class AjaxAction extends BaseOldAction {
    private static Logger logger = Logger.getLogger(AjaxAction.class.getSimpleName());

    private Logger getLogger() {
        return logger;
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another web component that
     * will create it). Return an <code>ActionForward</code> instance describing where and how control should be forwarded, or
     * <code>null</code> if the response has already been completed.
     *
     * @param mapping           The ActionMapping used to select this instance
     * @param actionForm        The optional ActionForm bean for this request (if any)
     * @param request           The HTTP request we are processing
     * @param response          The HTTP response we are creating
     * @return                  The forward name of the jsp page to load into the browser
     * @throws IOException      If an input/output error occurs
     * @throws ServletException If a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String action = CommonMethods.nes(request.getParameter("action")).trim();
        LoginBean loginBean = new LoginBean();
        //long startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));

            //getLogger().info(String.format("%9s%-32s | %-34s | %s", "", "AJAX FILE [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            response.reset();
            response.setContentType("text/javascript");

            switch (action) {
                case "currShipTasks":
                    response.getOutputStream().write("taskArr.clear();".getBytes());
                    response.getOutputStream().write("issueArr.clear();".getBytes());

                    String uic = CommonMethods.nes(request.getParameter("uic"));

                    if (!CommonMethods.isEmpty(uic)) {
                        //Thread.sleep(250);

                        for (ProjectBean resultBean : ProjectModel.getShipTaskList(conn, CommonMethods.cInt(projectPk), uic)) {
                            response.getOutputStream().write(("taskArr.push('Task #" + resultBean.getTaskPk() + ": " + resultBean.getTitle().replaceAll("'", "&#39;") + "');").getBytes());
                            for (ProjectBean subTaskBean : ProjectModel.getSubTaskList(conn, CommonMethods.cInt(resultBean.getTaskPk()))) {
                                if (!subTaskBean.getStatus().equals("Completed")) response.getOutputStream().write(("taskArr.push('&nbsp;&nbsp;&nbsp;Task #" + resultBean.getTaskPk() + ": " + subTaskBean.getDescription().replaceAll("'", "&#39;") + "');").getBytes());
                            }
                        }

                        for (SupportBean resultBean : SupportModel.getIssueList(conn, CommonMethods.cInt(projectPk), uic, "open")) {
                            response.getOutputStream().write(("issueArr.push('Issue #" + resultBean.getIssuePk() + ": " + resultBean.getTitle().replaceAll("'", "&#39;") + "');").getBytes());
                        }
                    }

                    response.getOutputStream().write("refreshShipTaskList();".getBytes());
                    break;

                case "shipComputerName":
                    String facetName = SystemModel.getFacetName(conn, CommonMethods.cInt(request.getParameter("shipPk")));
                    if (!CommonMethods.isEmpty(facetName)) {
                        response.getOutputStream().write(("$('#facetInfoTr').show();").getBytes());
                        response.getOutputStream().write(("$('#facetInfoTd').html('" + facetName + "');").getBytes());
                    } else {
                        response.getOutputStream().write(("$('#facetInfoTr').hide();").getBytes());
                    }
                    break;

                case "issuePieChart":
                    //String contractNumber = CommonMethods.nes(request.getParameter("contractNumber"));
                    int daysBack = CommonMethods.cInt(request.getParameter("daysBack"), 30);

                    response.getOutputStream().write(("issuePieChartData.length = 0;").getBytes());

                    HashMap<String, Integer> categorySummaryMap = SupportModel.getCategorySummaryMap(conn, daysBack, projectPk);

                    if (categorySummaryMap.keySet().size() <= 0) {
                        response.getOutputStream().write(("$('#issuePieChart').hide();").getBytes());
                        response.getOutputStream().write(("$('#issuePieChartErr').show();").getBytes());
                        response.getOutputStream().write(String.format("$('#issuePieChartErr').html('No data found for the past %d days');", daysBack).getBytes());
                    } else {
                        response.getOutputStream().write(("$('#issuePieChart').show();").getBytes());
                        response.getOutputStream().write(("$('#issuePieChartErr').hide();").getBytes());
                        for (String key : categorySummaryMap.keySet()) {
                            response.getOutputStream().write(String.format("issuePieChartData.push({ label: '%s', value: %d });", key, categorySummaryMap.get(key)).getBytes());
                        }

                        response.getOutputStream().write(String.format("issuePieChart.updateProp('header.subtitle.text', 'Last %d Days');", daysBack).getBytes());
                        //response.getOutputStream().write(String.format("issuePieChart.updateProp('footer.text', 'Data as of %s');", CommonMethods.getDate("MM/DD/YYYY HH24:MI:SS")).getBytes());
                        response.getOutputStream().write(("issuePieChart.updateProp('data.content', issuePieChartData);").getBytes());
                    }
                    break;

                case "trainingBarChart":
                    //ArrayList<String> barGraphLabelList = TrainingModel.getBarGraphLabelList(conn);
                    //ArrayList<String> schedValueList = TrainingModel.getSchedValueList(conn, new TrainingBean());
                    //ArrayList<String> actualValueList = TrainingModel.getActualValueList(conn, new TrainingBean());

                    int fiscalYear = -1;

                    if (CommonMethods.cInt(request.getParameter("fiscalYear")) > 1900) {
                        fiscalYear = CommonMethods.cInt(request.getParameter("fiscalYear"));
                    } else {
                        fiscalYear = CommonMethods.cInt(CommonMethods.getDate("YYYY")) + (CommonMethods.cInt(CommonMethods.getDate("MM")) >= 10 ? 1 : 0);
                    }

                    response.getOutputStream().write(("$('#trainingBarChart').empty();").getBytes());
                    response.getOutputStream().write(("data.length = 0;").getBytes());

                    //Previous Fiscal Year
                    for (int i = 10; i <= 12; i++) {
                        response.getOutputStream().write(String.format("data.push({ label:'%s %d', Actual: Math.round(10*Math.random()), Scheduled: Math.round(10*Math.random()), sched_ships: 'xxx', actual_ships: 'yyy' });", CommonMethods.getMonthNameShort(i-1), fiscalYear-1).getBytes());
                    }

                    //Current Fiscal Year
                    for (int i = 1; i <= 9; i++) {
                        response.getOutputStream().write(String.format("data.push({ label:'%s %d', Actual: Math.round(10*Math.random()), Scheduled: Math.round(10*Math.random()), sched_ships: 'xxx', actual_ships: 'yyy' });", CommonMethods.getMonthNameShort(i-1), fiscalYear).getBytes());
                    }
                    response.getOutputStream().write(("paintBarChart();").getBytes());
                    break;

                case "updateFacetVersion":
                    SystemBean inputBean = new SystemBean();
                    inputBean.setFacetVersion(request.getParameter("facetVersion"));

                    if (SystemModel.updateSystemVariables(conn, inputBean, loginBean)) {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"facetVersion\": \"" + inputBean.getFacetVersion() + "\"}");
                    }
                    break;

                case "updateIssueCount":
                    SupportBean supportBean = new SupportBean();

                    //Get open non-monthly issue count
                    supportBean.setSearchMode("open_non_monthly");
                    int issueCnt = SupportModel.getIssueCnt(conn, supportBean, projectPk);

                    //Get user's current open issue count
                    supportBean.setSearchMode("open");
                    supportBean.setPersonAssigned(loginBean.getFullName());
                    int myIssueCnt = SupportModel.getIssueCnt(conn, supportBean, projectPk);

                    response.getWriter().write("if(" + issueCnt + " > parseInt($('.issueCnt').text()) && parseInt($('.issueCnt').text()) > 0) { $('#issueCntNotif').show(); }");
                    response.getWriter().write("$('.issueCnt').text('" + issueCnt + "');");

                    response.getWriter().write("if(" + myIssueCnt + " > parseInt($('.myIssueCnt').text()) && parseInt($('.myIssueCnt').text()) > 0) { $('#myIssueCntNotif').show(); }");
                    response.getWriter().write("$('.myIssueCnt').text('" + myIssueCnt + "');");
                    break;

                case "shipCloseOtherIssues":
                    ShipBean shipBean = ShipModel.getShipBean(conn, CommonMethods.cInt(request.getParameter("shipPk")));
                    ArrayList<SupportBean> issueList = SupportModel.getIssueList(conn, projectPk, shipBean.getUic(), "open");

                    if (issueList.size() > 0) {
                        StringBuilder html = new StringBuilder();
                        int currIssuePk = CommonMethods.cInt(request.getParameter("currIssuePk"));
                        for (SupportBean issueBean : issueList) {
                            if (CommonMethods.cInt(issueBean.getIssuePk()) != currIssuePk) {
                                html.append("<label class='checkbox-inline'>"
                                    + "<input type='checkbox' name='closeIssuePkArr' value='" + issueBean.getIssuePk() + "'/>"
                                    + "Issue #" + issueBean.getIssuePk() + " - " + issueBean.getTitle().replaceAll("'", "")
                                    + "</label><br/>");
                            }
                        }

                        response.getWriter().write("$(\"#related-issues\").html(\"" + html.toString() + "\");");
                        response.getWriter().write("$('.close-related-issues').fadeIn(200);");
                    } else {
                        response.getWriter().write("$('.close-related-issues').hide();");
                    }

                    break;

                case "updateFacetVersionOrder":
                    String[] keyArr = CommonMethods.nes(request.getParameter("keys")).split(",");
                    response.getWriter().write("{\"result\": " + LookupModel.updateFacetVersionOrder(conn, keyArr, loginBean) + "}");
                    break;

                case "getIssueSummaryJson":
                    response.getWriter().write(SupportModel.getIssueCntByDayJson(conn, projectPk, request.getParameter("contractNumber")));
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            getLogger().error(String.format("%11s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
        }

        //long endTime = System.currentTimeMillis();
        //long elapsedTime = endTime - startTime;
        //getLogger().info(String.format("%9s%-32s | %-34s | %s\r\n", "", "AJAX FILE [END]", "Username: " + loginBean.getUsername(), "Elapsed Time: " + (elapsedTime / 1000) + " sec"));

        return null;
    }
}
