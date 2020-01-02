package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.old.bean.BackfileBean;
import com.premiersolutionshi.old.bean.DecomBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.TrainingBean;
import com.premiersolutionshi.old.model.ExportModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.SupportModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Action logic for the application's EXPORT process
 */
public final class ExportAction extends Action {
    private static Logger logger = Logger.getLogger(ExportAction.class.getSimpleName());

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
        HttpSession session = request.getSession();
        LoginBean loginBean = new LoginBean();
        String returnAction = null;
        String contractNumber = null;
        StringBuffer filename = null;

        long startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));

            logger.info(String.format("%7s%-32s | %-34s | %s", "", "EXPORT FILE [START]", "action: " + action, "Username: " + loginBean.getUsername()));

            switch (action) {
                case "issueListXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        contractNumber = CommonMethods.nes(session.getAttribute("issueList_contractNumber"));
                        String issueListType = CommonMethods.nes(request.getParameter("type"));

                        filename = new StringBuffer("issueList");
                        filename.append("_" + issueListType);
                        if (!CommonMethods.isEmpty(contractNumber)) filename.append("_" + contractNumber);
                        filename.append(".xlsx");

                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=" + filename);

                        ExportModel.writeIssueListXlsx(response.getOutputStream(), SupportModel.getIssueList(conn, projectPk, new String(), issueListType, contractNumber));
                        returnAction = null;
                    }
                    break;

                case "issueListAllXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=issueList.xlsx");

//                        String sortBy = CommonMethods.nvl(session.getAttribute("issueListAll_sortBy"), "issue_pk");
//                        String sortDir = CommonMethods.nvl(session.getAttribute("issueListAll_sortDir"), "ASC");
                        SupportBean inputBean = (SupportBean)session.getAttribute("issueListAll_inputBean");
                        if (inputBean == null) inputBean = new SupportBean();
                        inputBean.setPagination(false);

                        ExportModel.writeIssueListXlsx(response.getOutputStream(), SupportModel.getIssueList(conn, inputBean, projectPk));
                        returnAction = null;
                    }
                    break;

                case "myIssueListXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=issueList_" + loginBean.getFullName() + ".xlsx");

                        String sortBy = CommonMethods.nvl(session.getAttribute("myIssueList_sortBy"), "issue_pk");
                        String sortDir = CommonMethods.nvl(session.getAttribute("myIssueList_sortDir"), "ASC");
                        ExportModel.writeIssueListXlsx(response.getOutputStream(), SupportModel.getIssueList(conn, projectPk, loginBean, sortBy, sortDir));
                        returnAction = null;
                    }
                    break;

                case "issueSummaryByShipXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        contractNumber = CommonMethods.nes(session.getAttribute("issueList_contractNumber"));

                        filename = new StringBuffer("issue_summary_by_ship");
                        if (!CommonMethods.isEmpty(contractNumber)) filename.append("_" + contractNumber);
                        filename.append(".xlsx");

                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=" + filename);

                        ExportModel.writeIssueSummaryByShipXlsx(response.getOutputStream(), conn, projectPk, contractNumber);
                        returnAction = null;
                    }
                    break;

                case "laptopXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=laptops.xlsx");

                        ExportModel.writeLaptopXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "scannerXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=scanners.xlsx");

                        ExportModel.writeScannerXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "miscHardwareXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=misc_hardware.xlsx");

                        ExportModel.writeMiscHardwareXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "kofaxLicenseXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=kofax_licenses.xlsx");

                        ExportModel.writeKofaxLicenseXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "msOfficeLicenseXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=ms_office_licenses.xlsx");

                        ExportModel.writeMsOfficeLicenseXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "vrsLicenseXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=vrs_licenses.xlsx");

                        ExportModel.writeVrsLicenseXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "miscLicenseXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=misc_licenses.xlsx");

                        ExportModel.writeMiscLicenseXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "backfileWorkflowXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        BackfileBean backfileBean = (BackfileBean)session.getAttribute("backfileWorkflowSummary_inputBean");
                        if (backfileBean == null) backfileBean = new BackfileBean();

                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=backfile_workflow.xlsx");

                        ExportModel.writeBackfileWorkflowXlsx(response.getOutputStream(), conn, backfileBean);
                        returnAction = null;
                    }
                    break;

                case "unitContactXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=unit_contacts.xlsx");

                        ExportModel.writeUnitContactXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "taskXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=tasks.xlsx");

                        String sortBy = CommonMethods.nvl(session.getAttribute("taskList_sortBy"), "task_pk");
                        String sortDir = CommonMethods.nvl(session.getAttribute("taskList_sortDir"), "ASC");
                        ProjectBean inputBean = (ProjectBean)session.getAttribute("taskList_inputBean");
                        if (inputBean == null) inputBean = new ProjectBean();

                        ExportModel.writeTaskXlsx(response.getOutputStream(), conn, inputBean, sortBy, sortDir, projectPk);
                        returnAction = null;
                    }
                    break;

                case "decomWorkflowXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=decom_workflow.xlsx");

                        DecomBean decomBean = (DecomBean)session.getAttribute("decomWorkflowSummary_inputBean");
                        if (decomBean == null) decomBean = new DecomBean();

                        ExportModel.writeDecomWorkflowXlsx(response.getOutputStream(), conn, decomBean, projectPk);
                        returnAction = null;
                    }
                    break;

                case "transmittalSummaryXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=logcop_transmittal_summary.xlsx");

                        ExportModel.writeTransmittalSummaryXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "configuredSystemXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=configured_systems.xlsx");

                        ExportModel.writeConfiguredSystemXlsx(response.getOutputStream(), conn, projectPk);
                        returnAction = null;
                    }
                    break;

                case "trainingWorkflowXlsx":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        response.reset();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.addHeader("Content-Disposition", "attachment;filename=training_workflow.xlsx");

                        TrainingBean trainingBean = (TrainingBean)session.getAttribute("trainingWorkflowSummary_inputBean");
                        if (trainingBean == null) trainingBean = new TrainingBean();

                        ExportModel.writeTrainingWorkflowXlsx(response.getOutputStream(), conn, trainingBean, projectPk);
                        returnAction = null;
                    }
                    break;

                default:
                    returnAction = "error404";
                    break;
            }

        } catch (Exception e) {
            logger.error(String.format("%9s%-30s | %s", "", "ERROR", e));
            System.out.println(String.format("%9s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnepected";
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info(String.format("%7s%-32s | %-34s | %-52s | %s\r\n", "", "EXPORT FILE [END]", "Return: " + CommonMethods.nvl(returnAction, "N/A"), "Username: " + loginBean.getUsername(), "Elapsed Time: " + (elapsedTime / 1000) + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}