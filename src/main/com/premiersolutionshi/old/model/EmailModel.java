package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.common.util.ConfigUtils;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's EMAIL module
 */
public class EmailModel {
    private static final String BASE_URL = "http://10.28.31.55/";
    //private static final String BASE_URL = "http://premiersol-web/";

    private static final String CHECK_APPROVED = "<span style='font-size: 12px'>âœ…</span>";
    private static final String CHECK_UNAPPROVED = "<span style='font-size: 12px'>â˜�</span>";
    private static final String FILE_IMAGE = "<span style='font-size: 55px'>ðŸ“„</span>";


    private static final String EMAIL_SUPPORT = "support@premiersolutionshi.com";
    //private static final String EMAIL_ANTONE3X7 = "antone3x7@gmail.com";//Why were emails going to a personal email address?

    private static final String EMAIL_FOOTER = "<p><i>This e-mail was automatically generated from the PSHI TrackIT Application</i></p>";
    private Logger logger = Logger.getLogger(EmailModel.class.getName());
    private HtmlEmail email = null;
    private boolean isProd = false;
    private String emailDev;
    private String emailProjectManager;

    public EmailModel() throws EmailException {
        Properties prop = ConfigUtils.getConfigProperties();
        if (prop == null) {
            logError("constructor", "Could not load configuration file.");
            return;
        }
        String smtpServer = prop.getProperty("SMTP_SERVER");
        String smtpUser = prop.getProperty("SMTP_USER");
        String smtpPass = prop.getProperty("SMTP_PASS");
        Integer smtpPort = StringUtils.parseInt(prop.getProperty("SMTP_PORT"));
        setEmailDev(prop.getProperty("EMAIL_DEV"));
        setEmailProjectManager(prop.getProperty("EMAIL_PROJECT_MANAGER"));

        if (StringUtils.isEmpty(smtpServer) || StringUtils.isEmpty(smtpUser) || StringUtils.isEmpty(smtpPass) || smtpPort == null) {
            logError("constructor", "Not all configurations were found. smtpServer=" + smtpServer + ", smtpUser=" + smtpUser + ", smtpPort=" + smtpPort);
            return;
        }

        String isProdStr = prop.getProperty("IS_PROD");
        setProd(!StringUtils.isEmpty(isProdStr) && isProdStr.equals("true"));

        //PSHI
        this.email = new HtmlEmail();
        this.email.setHostName(smtpServer);
        this.email.setSmtpPort(smtpPort);
        this.email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPass));
        this.email.setStartTLSEnabled(true);

        Properties mailSessionProperties = this.email.getMailSession().getProperties();
        mailSessionProperties.put("mail.smtps.auth", "true");
        mailSessionProperties.put("mail.debug", "true");
        mailSessionProperties.put("mail.smtps.port", smtpPort);
        mailSessionProperties.put("mail.smtps.socketFactory.port", smtpPort);
        mailSessionProperties.put("mail.smtps.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
        mailSessionProperties.put("mail.smtps.socketFactory.fallback", "false");
        mailSessionProperties.put("mail.smtp.starttls.enable", "true");
    }

    private void logInfo(String functionName, String statement) {
        String logMessage = String.format("%11s%-30s | %-34s | %s", "", "INFO", functionName, statement);
        logger.info(logMessage);
    }

    private void logError(String functionName, String statement) {
        logError(functionName, statement, null);
    }

    private void logError(String functionName, String statement, Exception e) {
        if (e != null) {
            logger.error(String.format("%11s%-30s | %-34s | %s", "", "ERROR", functionName, statement), e);
            e.printStackTrace();
        }
        else {
            logger.error(String.format("%11s%-30s | %-34s | %s", "", "ERROR", functionName, statement));
        }
    }

    private void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString(), e);
    }

    private void debugLog(String type, String functionName, String statement, Exception e) {
        logger.error(String.format("%11s%-30s | %-34s | %s", "", type, functionName, statement), e);
        e.printStackTrace();
    }

    /*ashwini*/
   // @SuppressWarnings("static-access")
	public static void mailConf(Connection conn, LoginBean loginBean,HttpServletRequest request)
    {
    	 ArrayList<UserBean> res = new ArrayList<UserBean>();
         
         res=UserModel.getCurrentPtoTravelList1(conn, loginBean,request);
         CommonMethods commonMethods=new CommonMethods();
         
         EmailModel emailModel;
		try {
			emailModel = new EmailModel();
		
         String date;
         DateUtils dateUtils=new DateUtils();
         date=dateUtils.getNowInBasicFormatDate();
         
         LocalTime localTime = LocalTime.now();
             for (int i = 0; i < res.size(); i++) 
             {
             	String email=res.get(i).getEmail();
             	String type=res.get(i).getLeaveType();
                 System.out.println("email="+res.get(i).getEmail());
                 	if(commonMethods.isValidDateStr(res.get(i).getEndDate()))
                 	{ 
                 	 if(res.get(i).getEndDate().equals(date) && type.equals("Travel"))
                 	 emailModel.sendMail(email);
                 	}
             }
     		} catch (EmailException e) {

     			e.printStackTrace();
         }	
    }
    
    public void sendTaskAddHtmlEmail(Connection conn, ProjectBean newBean, int taskPk, LoginBean loginBean, HttpServletRequest request) {
        //Only send to new poc if not null and new POC is not user
        String personAssigned = newBean.getPersonAssigned();
        String loginFullName = loginBean.getFullName();
        String newPoc = !CommonMethods.isEmpty(personAssigned) && !personAssigned.equals(loginFullName) ? personAssigned : null;
        String newPocEmail = UserModel.getEmployeeEmail(conn, newPoc);

        StringBuilder subjectStr = new StringBuilder();
        applyTaskPkToSubject(taskPk, subjectStr);
        subjectStr.append("New Task [Automated]");
        String emailSubject = subjectStr.toString();

        StringBuffer msgBody = new StringBuffer();
        applyEmailHeader(msgBody);

        /* E-Mail Intro & Notification Summary */
        String serverName = request.getServerName();
        if (!CommonMethods.isEmpty(newPoc)) { //Notify 1 person
            msgBody.append("<p>" + newPoc + ",</p>\n");
            msgBody.append("<p>This is a notification that a new <a href='http://" + serverName + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + taskPk + "'>Task #" + taskPk + "</a> has been assigned to you by " + loginFullName + ".</p>\n");
        } else { //Only notify PM
            msgBody.append("<p>Project Manager,</p>\n");
            msgBody.append("<p>This is a notification that a new <a href='http://" + serverName + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + taskPk + "'>Task #" + taskPk + "</a> has been created by " + loginFullName + ".</p>\n");
        }

        /* General Information */
        msgBody.append("<table id='tanTable_style2' border='0' cellspacing='0' width='800'>\n");
        msgBody.append("<tbody>\n");
        msgBody.append("    <tr><th>General Information</th></tr>\n");
        msgBody.append("    <tr><td class='nobordered' align='left'>\n");
        msgBody.append("        <table id='detailTable' border='0' cellspacing='0'>\n");
        msgBody.append("        <tbody>\n");
        msgBody.append("            <tr>\n");
        msgBody.append("                <td width='95' class='fieldName'>Category:</td>\n");
        msgBody.append("                <td width='250'>" + CommonMethods.nes(newBean.getCategory()) + "</td>\n");

        msgBody.append("                <td class='fieldName' width='135'>Person Assigned:</td>\n");
        msgBody.append("                <td width='250'>" + CommonMethods.nvl(personAssigned, "<i>None</i>") + "</td>\n");

        if (newBean.getIsInternal().equals("Y")) {
            msgBody.append("                    <td class='fieldName' width='50'>Internal:</td>\n");
            msgBody.append("                    <td width='20'><span style='font-size: 12px'>âœ…</span></td>\n");
        } else {
            msgBody.append("                    <td width='50'/>\n");
            msgBody.append("                    <td width='20'/>\n");
        }
        msgBody.append("            </tr>\n");

        msgBody.append("            <tr>\n");
        msgBody.append("                <td class='fieldName'>Title:</td>\n");
        msgBody.append("                <td colspan='5'>" + CommonMethods.nes(newBean.getTitle()) + "</td>\n");
        msgBody.append("            </tr>\n");

        msgBody.append("            <tr>\n");
        msgBody.append("                <td class='fieldName'>Description:</td>\n");
        msgBody.append("                <td colspan='5'>" + CommonMethods.nes(newBean.getDescription()).replaceAll("\r\n", "<br/>") + "</td>\n");
        msgBody.append("            </tr>\n");

        if (!CommonMethods.isEmpty(newBean.getSource())) {
            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Source:</td>\n");
            msgBody.append("                <td colspan='5'>" + newBean.getSource() + "</td>\n");
            msgBody.append("            </tr>\n");
        }

        if (!CommonMethods.isEmpty(newBean.getUic())) {
            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Ship:</td>\n");
            msgBody.append("                <td colspan='5'>" + CommonMethods.nes(ShipModel.getShipName(conn, newBean.getUic())) + "</td>\n");
            msgBody.append("            </tr>\n");
        }

        msgBody.append("    </tbody>\n");
        msgBody.append("    </table>\n");
        msgBody.append("</td></tr>\n");


        /* Status */
        msgBody.append("<tr><th>Status</th></tr>\n");
        msgBody.append("<tr><td class='nobordered' align='left'>\n");
        msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");
        msgBody.append("    <tbody>\n");
        msgBody.append("        <tr>\n");

        msgBody.append("            <td width='95' class='fieldName'>Priority:</td>\n");
        msgBody.append("                <td width='140' style='" + newBean.getPriorityCss() + "'>" + CommonMethods.nes(newBean.getPriority()) + "</td>\n");

        msgBody.append("            <td class='fieldName' width='105'>Date Created:</td>\n");
        msgBody.append("            <td width='100'>" + CommonMethods.getDate("MM/DD/YYYY") + "</td>\n");

        msgBody.append("            <td width='80' class='fieldName'>Created By:</td>\n");
        msgBody.append("            <td colspan='2' width='180'>" + loginFullName + "</td>\n");

        msgBody.append("            <td width='80'/>\n");
        msgBody.append("        </tr>\n");

        msgBody.append("        <tr>\n");
        msgBody.append("            <td class='fieldName'>Status:</td>\n");
        msgBody.append("                <td style='" + newBean.getStatusCss() + "'>" + CommonMethods.nes(newBean.getStatus()) + "</td>\n");

        if (!CommonMethods.isEmpty(newBean.getDueDate())) {
            msgBody.append("                <td class='fieldName'>Due Date:</td>\n");
            msgBody.append("                <td style='" + newBean.getDueDateCss() + "'>" + newBean.getDueDate() + "</td>\n");
        } else {
            msgBody.append("            <td></td>\n");
            msgBody.append("            <td></td>\n");
        }
        if (!CommonMethods.isEmpty(newBean.getFollowUpDate())) {
            msgBody.append("                <td class='fieldName'>Follow Up:</td>\n");
            msgBody.append("                <td>" + newBean.getFollowUpDate() + "</td>\n");
        } else {
            msgBody.append("            <td></td>\n");
            msgBody.append("            <td></td>\n");
        }
        if (CommonMethods.nes(newBean.getStatus()).equals("Completed") && !CommonMethods.isEmpty(newBean.getCompletedDate())) {
            msgBody.append("                <td class='fieldName'>Completed:</td>\n");
            msgBody.append("                <td>" + newBean.getCompletedDate() + "</td>\n");
        }
        msgBody.append("        </tr>\n");
        msgBody.append("    </tbody>\n");
        msgBody.append("    </table>\n");
        msgBody.append("</td></tr>\n");

        /* Future Requests */
        if (newBean.getCategory().equals("Future Requests")) {
            msgBody.append("<tr><th>Future Features</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left'>\n");
            msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");

            msgBody.append("    <tbody>\n");
            msgBody.append("        <tr>\n");

            if (!CommonMethods.isEmpty(newBean.getQuarterYear())) {
                msgBody.append("<td class='fieldName' width='95'>Target Quarter:</td>\n");
                msgBody.append("<td width='135'>" + newBean.getQuarterYear() + "</td>\n");
            }
            if (!CommonMethods.isEmpty(newBean.getEffortArea())) {
                msgBody.append("<td class='fieldName' width='80'>Area of Effort:</td>\n");
                msgBody.append("<td width='130'>" + newBean.getEffortArea() + "</td>\n");
            }
            if (!CommonMethods.isEmpty(newBean.getEffortType())) {
                msgBody.append("<td class='fieldName' width='100'>Type of Effort:</td>\n");
                msgBody.append("<td width='140'>" + newBean.getEffortType() + "</td>\n");
            }

            if (!CommonMethods.isEmpty(newBean.getLoe())) {
                msgBody.append("<td class='fieldName' width='50'>LOE:</td>\n");
                msgBody.append("<td width='50'>" + newBean.getLoe() + "</td>\n");
            }

            msgBody.append("    </tr>\n");

            msgBody.append("    <tr>\n");
            msgBody.append("        <td class='fieldName' rowspan='2'>Approval:</td>\n");
            msgBody.append("        <td rowspan='2'\n");

            if (CommonMethods.nes(newBean.getIsClientApproved()).equals("Y")) {
                msgBody.append(CHECK_APPROVED);
            } else {
                msgBody.append(CHECK_UNAPPROVED);
            }
            msgBody.append("Client<br/>\n");

            if (CommonMethods.nes(newBean.getIsPshiApproved()).equals("Y")) {
                msgBody.append(CHECK_APPROVED);
            } else {
                msgBody.append(CHECK_UNAPPROVED);
            }
            msgBody.append("&nbsp;PSHI\n");
            msgBody.append("        </td>\n");
            if (!CommonMethods.isEmpty(newBean.getClientPriority())) {
                msgBody.append("        <td class='fieldName'>Priority:</td>\n");
                msgBody.append("        <td>" + newBean.getClientPriority() + "</td>\n");
            }
            if (!CommonMethods.isEmpty(newBean.getVersionIncluded())) {
                msgBody.append("        <td class='fieldName' rowspan='2'>Fixed/Added<br/>in Version:</td>\n");
                msgBody.append("        <td rowspan='2'>" + newBean.getVersionIncluded().replaceAll("\r\n", "<br/>") + "</td>\n");
            }
            msgBody.append("    </tr>\n");

            if (!CommonMethods.isEmpty(newBean.getRecommendation())) {
                msgBody.append("    <tr>\n");
                msgBody.append("        <td class='fieldName'>Recommendation:</td>\n");
                msgBody.append("        <td>" + newBean.getRecommendation() + "</td>\n");
                msgBody.append("    </tr>\n");
            }

            if (!CommonMethods.isEmpty(newBean.getDocNotes())) {
                msgBody.append("    <tr>\n");
                msgBody.append("        <td class='fieldName'>Doc Updated:</td>\n");
                msgBody.append("        <td colspan='3'>" + newBean.getDocNotes() + "</td>\n");
                msgBody.append("    </tr>\n");
            }
            msgBody.append("    </tbody>\n");
            msgBody.append("    </table>\n");
            msgBody.append("</td></tr>\n");
        }

        /* Sub-Tasks */
        if (newBean.getDescriptionArr().length > 0 || !CommonMethods.isEmpty(newBean.getSubTasks())) {
            //String imgChecked = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_checked.png"), "Completed");
            //String imgUnchecked = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Outstanding");

            msgBody.append("<tr><th>Sub-Tasks</th></tr>\n");
            if (newBean.getDescriptionArr().length > 0) {
                msgBody.append("    <tr><td class='nobordered' align='left' style='padding-left:25px;'>\n");
                msgBody.append("        <div id='user-task-list'>\n");

                for (int i = 0; i < newBean.getDescriptionArr().length; i++) {
                    msgBody.append("<p>\n");

                    boolean isCompleted = newBean.getStatusArr()[i].equals("Completed");
                    msgBody.append(displayCheckedOrUnchecked(isCompleted) + "&nbsp;");
                    msgBody.append(newBean.getDescriptionArr()[i]);
                    msgBody.append("<span style='color:#888;'>\n");

                    if (!CommonMethods.isEmpty(newBean.getPersonAssignedArr()[i]))
                        msgBody.append(" [Assigned to " + newBean.getPersonAssignedArr()[i] + "]");

                    if (isCompleted && !CommonMethods.isEmpty(newBean.getCompletedDateArr()[i]))
                        msgBody.append(" (Completed " + newBean.getCompletedDateArr()[i] + ")");
                    else if (isCompleted)
                        msgBody.append(" (Completed)");
                    else if (!CommonMethods.isEmpty(newBean.getStatusArr()[i]) && !CommonMethods.isEmpty(newBean.getDueDateArr()[i]))
                        msgBody.append(" (" + newBean.getStatusArr()[i] + " - Due: " + newBean.getDueDateArr()[i] + ")");
                    else if (!CommonMethods.isEmpty(newBean.getStatusArr()[i]))
                        msgBody.append(" (" + newBean.getStatusArr()[i] + ")");

                    msgBody.append(" </span>\n");
                    msgBody.append("</p>\n");
                }

                msgBody.append("    </div>\n");
                msgBody.append("</td></tr>\n");
            }

            if (!CommonMethods.isEmpty(newBean.getSubTasks())) {
                msgBody.append("<tr><td class='nobordered' align='left' style='padding:10px 10px 10px 25px;'>\n");
                msgBody.append(newBean.getSubTasks().replaceAll("\r\n", "<br/>"));
                msgBody.append("</td></tr>\n");
            }
        }

        /* Files */
        if (newBean.getFileList() != null) {
            msgBody.append("<tr><th>Files</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left'>\n");

            //New files uploaded
            if (newBean.getFileList() != null) {
                for (FormFile f : newBean.getFileList()) {
                    if (f.getFileSize() > 0) {
                        String filename = f.getFileName();
                        String extension = filename.lastIndexOf(".") > -1 ? filename.substring(filename.lastIndexOf(".") + 1) : "";
                        if (extension.length() > 4) extension = extension.substring(0, 4);
                        //String imgFile = this.email.embed(new java.io.File("C:/cloaked/pshi/" + CommonMethods.getFileIcon(extension, "lrg")), extension);
                        msgBody.append("<div style='text-align:center;color:green;'>" + FILE_IMAGE + "<br/>" + filename + "</div>\n");
                    }
                }
            }
            msgBody.append("</td></tr>\n");
        }

        /* Weekly Meeting Agenda */
        if (newBean.getStaffMeetingInd().equals("Y") || newBean.getClientMeetingInd().equals("Y")) {
            msgBody.append("<tr><th>Weekly Meeting Agenda</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left' style='padding:0 10px 0 25px;'>\n");

            if (newBean.getStaffMeetingInd().equals("Y")) {
                msgBody.append(CHECK_APPROVED + " Weekly Staff Meeting Agenda<br/>\n");
            }
            if (newBean.getClientMeetingInd().equals("Y")) {
                msgBody.append(CHECK_APPROVED + " Weekly Client Meeting Agenda");
            }
            msgBody.append("</td></tr>\n");
        }

        /* Internal Notes */
        if (!CommonMethods.isEmpty(newBean.getNotes())) {
            msgBody.append("<tr><th>Internal Notes</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left'>\n");
            msgBody.append(newBean.getNotes().replaceAll("\r\n", "<br/>"));
            msgBody.append("</td></tr>\n");
        }

        msgBody.append("        </tbody>\n");
        msgBody.append("        </table>\n");
        msgBody.append("    </td></tr>\n");
        msgBody.append("</tbody>\n");
        msgBody.append("</table>\n");

        msgBody.append(EMAIL_FOOTER);

        msgBody.append("</body>\n");
        msgBody.append("</html>\n");

        String emailTextMsg = "http://" + serverName + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + taskPk + "\r\n\r\n"
            + "HTML is not suppported by your e-mail client";
        String emailBody = msgBody.toString();
        String emailTo = newPocEmail;
        String emailCc = "";
        if (CommonMethods.isEmpty(newPocEmail)) { //Only send to PM
            emailTo = emailProjectManager;
        } else if (!newPocEmail.equals(emailProjectManager)) {
            emailCc = emailProjectManager;
        }
        sendEmail(emailSubject, emailTextMsg, emailBody, emailTo, EMAIL_SUPPORT, null, emailCc);
    }

    private void sendSimpleEmail(String subject, String textMsg, String to, String from) {
        sendEmail(subject, textMsg, null, to, from, null, null);
    }

    private void sendHtmlEmail(String subject, String htmlBody, String to, String from) {
        sendEmail(subject, null, htmlBody, to, from, null, null);
        System.out.println("Email send successfully");

    }

    /**
     * Send email.
     * Bcc is inherently evil. Let's not use it.
     * @param subject
     * @param textMsg
     * @param body
     * @param to
     * @param from
     * @param replyTo
     * @param cc
     */
    private void sendEmail(String subject, String textMsg, String body, String to, String from, String replyTo, String cc) {
        if (StringUtils.isEmpty(to)) {
            logError("sendEmail", "The 'to' value is required.");
            return;
        }
        if (StringUtils.isEmpty(from)) {
            logError("sendEmail", "The 'from' value is required.");
            return;
        }
        if (StringUtils.isEmpty(subject)) {
            logError("sendEmail", "The 'subject' value is required.");
            return;
        }
        StringBuilder str = new StringBuilder();
        str.append("==================================================================\n");
        str.append("To: ").append(to).append("\n");
        str.append("From: ").append(from).append("\n");
        str.append((StringUtils.isEmpty(replyTo) ? "" : "Reply To: " + replyTo + "\n"));
        str.append((StringUtils.isEmpty(cc) ? "" : "Cc: " + cc + "\n"));
        str.append("Subject: ").append(subject).append("\n");
        str.append("TextMsg: ").append((StringUtils.isEmpty(textMsg) ? "N/A" : textMsg)).append("\n");
        str.append("Body: ").append((StringUtils.isEmpty(body) ? "N/A" : body)).append("\n");
        str.append("==================================================================\n");
        String logMessage = str.toString();
        try {
            this.email.addTo(to);
            this.email.setFrom(from);
            this.email.setSubject(subject);
            if (!StringUtils.isEmpty(replyTo)) {
                this.email.addReplyTo(replyTo);
            }
            if (!StringUtils.isEmpty(cc)) {
                this.email.addCc(cc);
            }
            if (!StringUtils.isEmpty(body)) {
                this.email.setHtmlMsg(body);
            }
            if (!StringUtils.isEmpty(textMsg)) {
                this.email.setTextMsg(textMsg);
            }
            if (isProd) {
                this.email.send();
            }
            logInfo("sendEmail", "Successfully " + (isProd ? "" : "SIMULATED") + " sent email:\n" + logMessage);
        } catch (Exception e) {
            logError("sendEmail", "Failed to send E-mail:" + logMessage, e);
        }
    }

    private void applyEmailHeader(StringBuffer msgBody) {
        msgBody.append("<html>\n");
        msgBody.append("<style>\n");

        //original
        msgBody.append("body { font-family: Calibri; font-size: 11pt; }\n");
        msgBody.append("    .fieldName { font-family: Arial, Helvetica, sans-serif;color: #777;text-align: right;vertical-align: top; }\n");
        msgBody.append("    #tanTable_style2 { border-collapse: collapse; }\n");
        msgBody.append("    #tanTable_style2 tr { text-align: left; }\n");
        msgBody.append("    #tanTable_style2 th { font-family:'Lucida Sans Unicode','Lucida Grande',Sans-Serif;font-size:13px;font-weight:normal;\n");
        msgBody.append("      color:#039;background:#9bb4d7;padding:3px 10px;border:1px solid #E1EAF0;text-align:center;vertical-align:top;\n");
        msgBody.append("    }\n");
        msgBody.append("    #tanTable_style2 td { border:1px solid #E1EAF0;padding:3px;font:12px Arial; vertical-align:top; }\n");
        msgBody.append("    #tanTable_style2 td.nobordered { border:1px solid #E1EAF0; background-color:#fdfeff;vertical-align:top;padding:5px;color:#000; }");
        msgBody.append("    #tanTable_style2 td.nobordered td { border:none; vertical-align:top; }\n");
        msgBody.append("</style>\n");
        msgBody.append("<body>\n");
    }

    public void sendTaskEditHtmlEmail(Connection conn, ProjectBean oldBean, ProjectBean newBean, LoginBean loginBean, HttpServletRequest request) {
        try {
            //Only send to old POC if not null, old POC is not user, and old POC is not the same as new POC
            String oldPoc = !CommonMethods.isEmpty(oldBean.getPersonAssigned()) && !oldBean.getPersonAssigned().equals(loginBean.getFullName()) && !oldBean.getPersonAssigned().equals(newBean.getPersonAssigned()) ? oldBean.getPersonAssigned() : null;
            String oldPocEmail = UserModel.getEmployeeEmail(conn, oldPoc);

            //Only send to new poc if not null and new POC is not user
            String newPoc = !CommonMethods.isEmpty(newBean.getPersonAssigned()) && !newBean.getPersonAssigned().equals(loginBean.getFullName()) ? newBean.getPersonAssigned() : null;
            String newPocEmail = UserModel.getEmployeeEmail(conn, newPoc);

            if (CommonMethods.isEmpty(oldPocEmail) && CommonMethods.isEmpty(newPocEmail)) { //Only send to PM
                this.email.addTo(emailProjectManager);
                //this.email.addBcc(emailDev);
            } else {
                if (!CommonMethods.isEmpty(oldPocEmail)) this.email.addTo(oldPocEmail); //Only add the email if it can be found
                if (!CommonMethods.isEmpty(newPocEmail)) this.email.addTo(newPocEmail); //Only add the email if it can be found
                if (!CommonMethods.nes(oldPocEmail).equals(emailProjectManager) && !CommonMethods.nes(newPocEmail).equals(emailProjectManager)) this.email.addCc(emailProjectManager);
                //this.email.addBcc(emailDev);
            }

            StringBuilder subjectStr = new StringBuilder();
            applyTaskPkToSubject(newBean.getTaskPk(), subjectStr);
            subjectStr.append("Task Updated");
            this.email.setSubject(subjectStr.toString());

            StringBuffer msgBody = new StringBuffer();
            applyEmailHeader(msgBody);

            /* E-Mail Intro & Notification Summary */
            if (!CommonMethods.isEmpty(oldPoc) && !CommonMethods.isEmpty(newPoc)) { //Notify 2 people
                msgBody.append("<p>" + oldPoc + " & " + newPoc + ",</p>\n");
                msgBody.append("<p>This is a notification that <a href='http://" + request.getServerName() + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + newBean.getTaskPk() + "'>Task #" + newBean.getTaskPk() + "</a> assigned to you has been updated by " + loginBean.getFullName() + ".</p>\n");
            } else if (!CommonMethods.isEmpty(oldPoc) || !CommonMethods.isEmpty(newPoc)) { //Notify 1 person
                msgBody.append("<p>" + CommonMethods.nvl(oldPoc, newPoc) + ",</p>\n");
                msgBody.append("<p>This is a notification that <a href='http://" + request.getServerName() + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + newBean.getTaskPk() + "'>Task #" + newBean.getTaskPk() + "</a> assigned to you has been updated by " + loginBean.getFullName() + ".</p>\n");
            } else { //Only notify PM
                msgBody.append("<p>Project Manager,</p>\n");
                msgBody.append("<p>This is a notification that <a href='http://" + request.getServerName() + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + newBean.getTaskPk() + "'>Task #" + newBean.getTaskPk() + "</a> has been updated by " + loginBean.getFullName() + ".</p>\n");
            }

            /* General Information */
            msgBody.append("<table id='tanTable_style2' border='0' cellspacing='0' width='800'>\n");
            msgBody.append("<tbody>\n");
            msgBody.append("    <tr><th>General Information</th></tr>\n");
            msgBody.append("    <tr><td class='nobordered' align='left'>\n");
            msgBody.append("        <table id='detailTable' border='0' cellspacing='0'>\n");
            msgBody.append("        <tbody>\n");
            msgBody.append("            <tr>\n");
            msgBody.append("                <td width='95' class='fieldName'>Category:</td>\n");
            msgBody.append("                <td width='250'>" + CommonMethods.nes(newBean.getCategory()) + "</td>\n");

            msgBody.append("                <td class='fieldName' width='135'>Person Assigned:</td>\n");
            msgBody.append("                <td width='250'>\n");
            if (CommonMethods.nes(oldBean.getPersonAssigned()).equals(CommonMethods.nes(newBean.getPersonAssigned()))) {
                msgBody.append(CommonMethods.nvl(newBean.getPersonAssigned(), "<i>None</i>"));
            } else {
                msgBody.append("                <span style='color:red'><strike>" + CommonMethods.nes(oldBean.getPersonAssigned()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getPersonAssigned()) + "</span>\n");
            }
            msgBody.append("                </td>\n");

            if (oldBean.getIsInternal().equals("Y") && newBean.getIsInternal().equals("Y")) {
                //String imgCheckmark = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Checkmark");
                msgBody.append("                    <td class='fieldName' width='50'>Internal:</td>\n");
                msgBody.append("                    <td width='20'>" + CHECK_APPROVED + "</td>\n");
            } else if (oldBean.getIsInternal().equals("Y") && !newBean.getIsInternal().equals("Y")) {
                //String imgCheckmark = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Checkmark");
                msgBody.append("                    <td class='fieldName' width='50'><span style='color:red'><strike>Internal:</strike></span></td>\n");
                msgBody.append("                    <td width='20'><strike>" + CHECK_APPROVED + "</strike></td>\n");
            } else if (!oldBean.getIsInternal().equals("Y") && newBean.getIsInternal().equals("Y")) {
                //String imgCheckmark = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Checkmark");
                msgBody.append("                    <td class='fieldName' width='50'><span style='color:green'>Internal:</span></td>\n");
                msgBody.append("                    <td width='20'>" + CHECK_APPROVED + "</td>\n");
            } else {
                msgBody.append("                    <td width='50'/>\n");
                msgBody.append("                    <td width='20'/>\n");
            }
            msgBody.append("            </tr>\n");

            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Title:</td>\n");
            if (CommonMethods.nes(oldBean.getTitle()).equals(CommonMethods.nes(newBean.getTitle()))) {
                msgBody.append("                <td colspan='5'>" + CommonMethods.nes(newBean.getTitle()) + "</td>\n");
            } else {
                msgBody.append("                <td colspan='5'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getTitle()) + "</strike></span><br/><span style='color:green'>" + CommonMethods.nes(newBean.getTitle()) + "</span></td>\n");
            }
            msgBody.append("            </tr>\n");

            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Description:</td>\n");
            if (CommonMethods.nes(oldBean.getDescription()).equals(CommonMethods.nes(newBean.getDescription()))) {
                msgBody.append("                <td colspan='5'>" + CommonMethods.nes(newBean.getDescription()).replaceAll("\r\n", "<br/>") + "</td>\n");
            } else {
                msgBody.append("                <td colspan='5'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getDescription()).replaceAll("\r\n", "<br/>") + "</strike></span><br/><span style='color:green'>" + CommonMethods.nes(newBean.getDescription()).replaceAll("\r\n", "<br/>") + "</span></td>\n");
            }
            msgBody.append("            </tr>\n");

            if (!CommonMethods.isEmpty(oldBean.getSource()) || !CommonMethods.isEmpty(newBean.getSource())) {
                msgBody.append("            <tr>\n");
                msgBody.append("                <td class='fieldName'>Source:</td>\n");
                if (CommonMethods.nes(oldBean.getSource()).equals(CommonMethods.nes(newBean.getSource()))) {
                    msgBody.append("                <td colspan='5'>" + newBean.getSource() + "</td>\n");
                } else {
                    msgBody.append("                <td colspan='5'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getSource()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getSource()) + "</span></td>\n");
                }
                msgBody.append("            </tr>\n");
            }

            if (!CommonMethods.isEmpty(oldBean.getUic()) || !CommonMethods.isEmpty(newBean.getUic())) {
                msgBody.append("            <tr>\n");
                msgBody.append("                <td class='fieldName'>Ship:</td>\n");
                if (CommonMethods.nes(oldBean.getUic()).equals(CommonMethods.nes(newBean.getUic()))) {
                    msgBody.append("                <td colspan='5'>" + CommonMethods.nes(ShipModel.getShipName(conn, newBean.getUic())) + "</td>\n");
                } else {
                    msgBody.append("                <td colspan='5'><span style='color:red'><strike>" + CommonMethods.nes(ShipModel.getShipName(conn, oldBean.getUic())) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(ShipModel.getShipName(conn, newBean.getUic())) + "</span></td>\n");
                }
                msgBody.append("            </tr>\n");
            }

            msgBody.append("    </tbody>\n");
            msgBody.append("    </table>\n");
            msgBody.append("</td></tr>\n");


            /* Status */
            msgBody.append("<tr><th>Status</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left'>\n");
            msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");
            msgBody.append("    <tbody>\n");
            msgBody.append("        <tr>\n");

            msgBody.append("            <td width='95' class='fieldName'>Priority:</td>\n");
            if (CommonMethods.nes(oldBean.getPriority()).equals(CommonMethods.nes(newBean.getPriority()))) {
                msgBody.append("                <td width='140' style='" + newBean.getPriorityCss() + "'>" + CommonMethods.nes(newBean.getPriority()) + "</td>\n");
            } else {
                msgBody.append("                <td width='140'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getPriority()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getPriority()) + "</span></td>\n");
            }

            msgBody.append("            <td class='fieldName' width='105'>Date Created:</td>\n");
            msgBody.append("            <td width='100'>" + oldBean.getCreatedDate() + "</td>\n");

            if (!CommonMethods.isEmpty(oldBean.getCreatedBy())) {
                msgBody.append("            <td width='80' class='fieldName'>Created By:</td>\n");
                msgBody.append("            <td colspan='2' width='180'>" + oldBean.getCreatedBy() + "</td>\n");
            } else {
                msgBody.append("            <td width='80'/>\n");
                msgBody.append("            <td width='100'/>\n");
                msgBody.append("            <td width='80'/>\n");
            }

            msgBody.append("            <td width='80'/>\n");
            msgBody.append("        </tr>\n");

            msgBody.append("        <tr>\n");
            msgBody.append("            <td class='fieldName'>Status:</td>\n");
            if (CommonMethods.nes(oldBean.getStatus()).equals(CommonMethods.nes(newBean.getStatus()))) {
                msgBody.append("                <td style='" + newBean.getStatusCss() + "'>" + CommonMethods.nes(newBean.getStatus()) + "</td>\n");
            } else {
                msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getStatus()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getStatus()) + "</span></td>\n");
            }

            if (!CommonMethods.isEmpty(oldBean.getDueDate()) || !CommonMethods.isEmpty(newBean.getDueDate())) {
                msgBody.append("                <td class='fieldName'>Due Date:</td>\n");

                if (CommonMethods.nes(oldBean.getDueDate()).equals(CommonMethods.nes(newBean.getDueDate()))) {
                    msgBody.append("                <td style='" + newBean.getDueDateCss() + "'>" + newBean.getDueDate() + "</td>\n");
                } else {
                    msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getDueDate()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getDueDate()) + "</span></td>\n");
                }
            } else {
                msgBody.append("            <td></td>\n");
                msgBody.append("            <td></td>\n");
            }

            if (!CommonMethods.isEmpty(oldBean.getFollowUpDate()) || !CommonMethods.isEmpty(newBean.getFollowUpDate())) {
                msgBody.append("                <td class='fieldName'>Follow Up:</td>\n");
                if (CommonMethods.nes(oldBean.getFollowUpDate()).equals(CommonMethods.nes(newBean.getFollowUpDate()))) {
                    msgBody.append("                <td>" + newBean.getFollowUpDate() + "</td>\n");
                } else {
                    msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getFollowUpDate()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getFollowUpDate()) + "</span></td>\n");
                }
            } else {
                msgBody.append("            <td></td>\n");
                msgBody.append("            <td></td>\n");
            }

            if (CommonMethods.nes(newBean.getStatus()).equals("Completed") && (!CommonMethods.isEmpty(oldBean.getCompletedDate()) || !CommonMethods.isEmpty(newBean.getCompletedDate()))) {
                msgBody.append("                <td class='fieldName'>Completed:</td>\n");
                if (CommonMethods.nes(oldBean.getCompletedDate()).equals(CommonMethods.nes(newBean.getCompletedDate()))) {
                    msgBody.append("                <td>" + newBean.getCompletedDate() + "</td>\n");
                } else {
                    msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getCompletedDate()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getCompletedDate()) + "</span></td>\n");
                }
            }

            msgBody.append("        </tr>\n");
            msgBody.append("    </tbody>\n");
            msgBody.append("    </table>\n");
            msgBody.append("</td></tr>\n");

            /* Future Requests */
            if (newBean.getCategory().equals("Future Requests")) {
                msgBody.append("<tr><th>Future Features</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");
                msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");
                msgBody.append("    <tbody>\n");
                msgBody.append("        <tr>\n");

                if (!CommonMethods.isEmpty(oldBean.getQuarterYear()) || !CommonMethods.isEmpty(newBean.getQuarterYear())) {
                    msgBody.append("                <td class='fieldName' width='95'>Target Quarter:</td>\n");
                    if (CommonMethods.nes(oldBean.getQuarterYear()).equals(CommonMethods.nes(newBean.getQuarterYear()))) {
                        msgBody.append("                <td width='135'>" + newBean.getQuarterYear() + "</td>\n");
                    } else {
                        msgBody.append("                <td width='135'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getQuarterYear()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getQuarterYear()) + "</span></td>\n");
                    }
                }

                if (!CommonMethods.isEmpty(oldBean.getEffortArea()) || !CommonMethods.isEmpty(newBean.getEffortArea())) {
                    msgBody.append("            <td class='fieldName' width='80'>Area of Effort:</td>\n");
                    if (CommonMethods.nes(oldBean.getEffortArea()).equals(CommonMethods.nes(newBean.getEffortArea()))) {
                        msgBody.append("                <td width='130'>" + newBean.getEffortArea() + "</td>\n");
                    } else {
                        msgBody.append("                <td width='130'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getEffortArea()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getEffortArea()) + "</span></td>\n");
                    }
                }

                if (!CommonMethods.isEmpty(oldBean.getEffortType()) || !CommonMethods.isEmpty(newBean.getEffortType())) {
                    msgBody.append("            <td class='fieldName' width='100'>Type of Effort:</td>\n");
                    if (CommonMethods.nes(oldBean.getEffortType()).equals(CommonMethods.nes(newBean.getEffortType()))) {
                        msgBody.append("                <td width='140'>" + newBean.getEffortType() + "</td>\n");
                    } else {
                        msgBody.append("                <td width='140'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getEffortType()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getEffortType()) + "</span></td>\n");
                    }
                }

                if (!CommonMethods.isEmpty(oldBean.getLoe()) || !CommonMethods.isEmpty(newBean.getLoe())) {
                    msgBody.append("        <td class='fieldName' width='50'>LOE:</td>\n");
                    if (CommonMethods.nes(oldBean.getLoe()).equals(CommonMethods.nes(newBean.getLoe()))) {
                        msgBody.append("                <td width='50'>" + newBean.getLoe() + "</td>\n");
                    } else {
                        msgBody.append("                <td width='50'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getLoe()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getLoe()) + "</span></td>\n");
                    }
                }
                msgBody.append("    </tr>\n");

                msgBody.append("    <tr>\n");
                msgBody.append("        <td class='fieldName' rowspan='2'>Approval:</td>\n");
                msgBody.append("        <td rowspan='2'>\n");

                boolean isClientApprovedOld = CommonMethods.nes(oldBean.getIsClientApproved()).equals("Y");
                boolean isClientApprovedNew = CommonMethods.nes(newBean.getIsClientApproved()).equals("Y");
                if (isClientApprovedOld && isClientApprovedNew) {
                    //String imgApproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Approved");
                    //msgBody.append("<img src='cid:" + imgApproved + "' height='12' width='12'/>\n");
                    msgBody.append(CHECK_APPROVED + "\n");
                } else if (CommonMethods.nvl(oldBean.getIsClientApproved(), "N").equals("N") && CommonMethods.nvl(newBean.getIsClientApproved(), "N").equals("N")) {
                    //String imgUnapproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Unapproved");
                    //msgBody.append("<img src='cid:" + imgUnapproved + "' height='10' width='10'/>\n");
                    msgBody.append(CHECK_UNAPPROVED + "\n");
                } else {
                    //String imgApproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Approved");
                    //String imgUnapproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Unapproved");
                    //msgBody.append("<span style='color:red'><strike><img src='cid:" + (CommonMethods.nes(oldBean.getIsClientApproved()).equals("Y") ? imgApproved : imgUnapproved) + "' height='12' width='12'/></strike></span> <img src='cid:" + (CommonMethods.nes(newBean.getIsClientApproved()).equals("Y") ? imgApproved : imgUnapproved) + "' height='12' width='12'/>\n");
                    msgBody.append("<span style='color:red'><strike>" + displayCheckedOrUnchecked(isClientApprovedOld) + "</strike></span>"
                            + " " + displayCheckedOrUnchecked(isClientApprovedNew) + "\n");
                }
                msgBody.append("&nbsp;Client<br/>\n");

                boolean isPshiApprovedOld = CommonMethods.nes(oldBean.getIsPshiApproved()).equals("Y");
                boolean isPshiApprovedNew = CommonMethods.nes(newBean.getIsPshiApproved()).equals("Y");
                if (isPshiApprovedOld && isPshiApprovedNew) {
                    //String imgApproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Approved");
                    msgBody.append(CHECK_APPROVED + "\n");
                } else if (CommonMethods.nvl(oldBean.getIsPshiApproved(), "N").equals("N") && CommonMethods.nvl(newBean.getIsPshiApproved(), "N").equals("N")) {
                    //String imgUnapproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Unapproved");
                    msgBody.append(CHECK_UNAPPROVED + "\n");
                } else {
                    //String imgApproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Approved");
                    //String imgUnapproved = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Unapproved");
                    msgBody.append(changedThisToThat(displayCheckedOrUnchecked(isPshiApprovedOld), displayCheckedOrUnchecked(isPshiApprovedNew)));
                }
                msgBody.append("&nbsp;PSHI");
                msgBody.append("        </td>\n");

                if (!CommonMethods.isEmpty(oldBean.getClientPriority()) || !CommonMethods.isEmpty(newBean.getClientPriority())) {
                    msgBody.append("        <td class='fieldName'>Priority:</td>\n");
                    if (CommonMethods.nes(oldBean.getClientPriority()).equals(CommonMethods.nes(newBean.getClientPriority()))) {
                        msgBody.append("                <td>" + newBean.getClientPriority() + "</td>\n");
                    } else {
                        msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getClientPriority()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getClientPriority()) + "</span></td>\n");
                    }
                }

                if (!CommonMethods.isEmpty(oldBean.getVersionIncluded()) || !CommonMethods.isEmpty(newBean.getVersionIncluded())) {
                    msgBody.append("        <td class='fieldName' rowspan='2'>Fixed/Added<br/>in Version:</td>\n");
                    if (CommonMethods.nes(oldBean.getVersionIncluded()).equals(CommonMethods.nes(newBean.getVersionIncluded()))) {
                        msgBody.append("                <td rowspan='2'>" + newBean.getVersionIncluded().replaceAll("\r\n", "<br/>") + "</td>\n");
                    } else {
                        msgBody.append("                <td rowspan='2'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getVersionIncluded()).replaceAll("\r\n", "<br/>") + "</strike></span><br/><span style='color:green'>" + CommonMethods.nes(newBean.getVersionIncluded()).replaceAll("\r\n", "<br/>") + "</span></td>\n");
                    }
                } else {
                    msgBody.append("<td colspan='4'/>\n");
                }

                msgBody.append("    </tr>\n");


                if (!CommonMethods.isEmpty(oldBean.getRecommendation()) || !CommonMethods.isEmpty(newBean.getRecommendation())) {
                    msgBody.append("    <tr>\n");
                    msgBody.append("        <td class='fieldName'>Recommendation:</td>\n");
                    if (CommonMethods.nes(oldBean.getRecommendation()).equals(CommonMethods.nes(newBean.getRecommendation()))) {
                        msgBody.append("                <td>" + newBean.getRecommendation() + "</td>\n");
                    } else {
                        msgBody.append("                <td><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getRecommendation()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getRecommendation()) + "</span></td>\n");
                    }
                    msgBody.append("    </tr>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getDocNotes()) || !CommonMethods.isEmpty(newBean.getDocNotes())) {
                    msgBody.append("    <tr>\n");
                    msgBody.append("        <td class='fieldName'>Doc Updated:</td>\n");
                    if (CommonMethods.nes(oldBean.getDocNotes()).equals(CommonMethods.nes(newBean.getDocNotes()))) {
                        msgBody.append("                <td colspan='3'>" + newBean.getDocNotes() + "</td>\n");
                    } else {
                        msgBody.append("                <td colspan='3'><span style='color:red'><strike>" + CommonMethods.nes(oldBean.getDocNotes()) + "</strike></span> <span style='color:green'>" + CommonMethods.nes(newBean.getDocNotes()) + "</span></td>\n");
                    }
                    msgBody.append("    </tr>\n");
                } else {
                    msgBody.append("    <tr>\n");
                    msgBody.append("        <td colspan='4'/>\n");
                    msgBody.append("    </tr>\n");
                }

                msgBody.append("    </tbody>\n");
                msgBody.append("    </table>\n");
                msgBody.append("</td></tr>\n");
            }


            /* Sub-Tasks */
            if ((oldBean.getSubTaskList() != null && oldBean.getSubTaskList().size() > 0) || (newBean.getDescriptionArr() != null && newBean.getDescriptionArr().length > 0) || !CommonMethods.isEmpty(oldBean.getSubTasks()) || !CommonMethods.isEmpty(newBean.getSubTasks())) {
                //String imgChecked = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_checked.png"), "Completed");
                //String imgUnchecked = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\checkbox_unchecked.png"), "Outstanding");

                msgBody.append("<tr><th>Sub-Tasks</th></tr>\n");
                if (oldBean.getSubTaskList().size() > 0 || newBean.getDescriptionArr().length > 0) {
                    msgBody.append("    <tr><td class='nobordered' align='left' style='padding-left:25px;'>\n");
                    msgBody.append("        <div id='user-task-list'>\n");

                    for (int i = 0; i < Math.max(oldBean.getSubTaskList().size(), newBean.getDescriptionArr().length); i++) {
                        String oldImgElem = null, oldDescElem = null, oldAssignedToElem = null, oldStatusElem = null;
                        String newImgElem = null, newDescElem = null, newAssignedToElem = null, newStatusElem = null;

                        if (i < oldBean.getSubTaskList().size()) {
                            ProjectBean oldTaskBean = oldBean.getSubTaskList().get(i);

                            String statusOld = oldTaskBean.getStatus();
                            String personAssignedOld = oldTaskBean.getPersonAssigned();
                            String completedDateOld = oldTaskBean.getCompletedDate();
                            String dueDateOld = oldTaskBean.getDueDate();

                            boolean isCompletedOld = statusOld.equals("Completed");
                            oldImgElem = displayCheckedOrUnchecked(isCompletedOld) + "&nbsp;";
                            oldDescElem = oldTaskBean.getDescription();

                            if (!CommonMethods.isEmpty(personAssignedOld))
                                oldAssignedToElem = personAssignedOld;

                            if (isCompletedOld && !CommonMethods.isEmpty(completedDateOld))
                                oldStatusElem = "Completed " + completedDateOld;
                            else if (isCompletedOld)
                                oldStatusElem = "Completed";
                            else {
                                if (!CommonMethods.isEmpty(statusOld) && !CommonMethods.isEmpty(dueDateOld))
                                    oldStatusElem = statusOld + " - Due: " + dueDateOld;
                                else if (!CommonMethods.isEmpty(statusOld))
                                    oldStatusElem = statusOld;
                            }
                        }

                        if (i < newBean.getDescriptionArr().length) {
                            boolean statsArrNew = newBean.getStatusArr()[i].equals("Completed");
                            newImgElem = displayCheckedOrUnchecked(statsArrNew) + "&nbsp;";

                            newDescElem = newBean.getDescriptionArr()[i];

                            if (!CommonMethods.isEmpty(newBean.getPersonAssignedArr()[i]))
                                newAssignedToElem = newBean.getPersonAssignedArr()[i];

                            if (statsArrNew && !CommonMethods.isEmpty(newBean.getCompletedDateArr()[i]))
                                newStatusElem = "Completed " + newBean.getCompletedDateArr()[i];
                            else if (statsArrNew)
                                newStatusElem = "Completed";
                            else if (!CommonMethods.isEmpty(newBean.getStatusArr()[i]) && !CommonMethods.isEmpty(newBean.getDueDateArr()[i]))
                                newStatusElem = newBean.getStatusArr()[i] + " - Due: " + newBean.getDueDateArr()[i];
                            else if (!CommonMethods.isEmpty(newBean.getStatusArr()[i]))
                                newStatusElem = newBean.getStatusArr()[i];
                        }

                        msgBody.append("<p>\n");
                        msgBody.append(!CommonMethods.isEmpty(newImgElem) ? newImgElem : "<strike>" + oldImgElem + "</strike>\n");

                        if (CommonMethods.nes(oldDescElem).equals(CommonMethods.nes(newDescElem))) {
                            msgBody.append(CommonMethods.nes(newDescElem));
                        } else {
                            msgBody.append("<span style='color:red;'><strike>" + CommonMethods.nes(oldDescElem) + "</strike></span>\n");
                            if (!CommonMethods.isEmpty(oldDescElem) && !CommonMethods.isEmpty(newDescElem)) msgBody.append(" ");
                            msgBody.append("<span style='color:green;'>" + CommonMethods.nes(newDescElem) + "</span>\n");
                        }

                        if (!CommonMethods.isEmpty(newAssignedToElem) || !CommonMethods.isEmpty(newStatusElem)) {
                            msgBody.append("<span style='color:#888;'>\n");
                        } else {
                            msgBody.append("<span style='color:red;text-decoration:line-through;'>\n");
                        }

                        if (!CommonMethods.isEmpty(oldAssignedToElem) || !CommonMethods.isEmpty(newAssignedToElem)) {
                            msgBody.append(" [Assigned to ");

                            if (CommonMethods.nes(oldAssignedToElem).equals(CommonMethods.nes(newAssignedToElem))) {
                                msgBody.append(CommonMethods.nes(newAssignedToElem));
                            } else {
                                msgBody.append("<span style='color:red;'><strike>" + CommonMethods.nes(oldAssignedToElem) + "</strike></span>\n");
                                if (!CommonMethods.isEmpty(oldAssignedToElem) && !CommonMethods.isEmpty(newAssignedToElem)) msgBody.append(" ");
                                msgBody.append("<span style='color:green;'>" + CommonMethods.nes(newAssignedToElem) + "</span>\n");
                            }

                            msgBody.append("]");
                        }

                        if (!CommonMethods.isEmpty(oldStatusElem) || !CommonMethods.isEmpty(newStatusElem)) {
                            msgBody.append(" (");

                            if (CommonMethods.nes(oldStatusElem).equals(CommonMethods.nes(newStatusElem))) {
                                msgBody.append(CommonMethods.nes(newStatusElem));
                            } else {
                                msgBody.append("<span style='color:red;'><strike>" + CommonMethods.nes(oldStatusElem) + "</strike></span>\n");
                                if (!CommonMethods.isEmpty(oldStatusElem) && !CommonMethods.isEmpty(newStatusElem)) msgBody.append(" ");
                                msgBody.append("<span style='color:green;'>" + CommonMethods.nes(newStatusElem) + "</span>\n");
                            }

                            msgBody.append(")");
                        }

                        msgBody.append("</span>\n");

                        msgBody.append("</p>\n");
                    }

                    msgBody.append("    </div>\n");
                    msgBody.append("</td></tr>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getSubTasks()) || !CommonMethods.isEmpty(newBean.getSubTasks())) {
                    msgBody.append("<tr><td class='nobordered' align='left' style='padding:10px 10px 10px 25px;'>\n");
                    if (CommonMethods.nes(oldBean.getSubTasks()).equals(CommonMethods.nes(newBean.getSubTasks()))) {
                        msgBody.append(newBean.getSubTasks().replaceAll("\r\n", "<br/>"));
                    } else {
                        msgBody.append("<span style='color:red'><strike>" + CommonMethods.nes(oldBean.getSubTasks()).replaceAll("\r\n", "<br/>") + "</strike></span>" + (!CommonMethods.isEmpty(oldBean.getSubTasks()) ? "<br/>" : "") + "<span style='color:green'>" + CommonMethods.nes(newBean.getSubTasks()).replaceAll("\r\n", "<br/>") + "</span>\n");
                    }
                    msgBody.append("</td></tr>\n");
                }
            }


            /* Files */
            if (oldBean.getTaskFileList().size() > 0 || newBean.getFileList() != null) {
                msgBody.append("<tr><th>Files</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");

                //Current Files
                for (FileBean fileBean : oldBean.getTaskFileList()) {
                        String imgFile = this.email.embed(new java.io.File("C:/cloaked/pshi/" + fileBean.getImage()), fileBean.getExtension());
                        msgBody.append("<div style='text-align:center;'>\n");
                        if (CommonMethods.isIn(newBean.getDeleteFilePkArr(), fileBean.getFilePk())) msgBody.append("<span style='color:red'><strike>\n");
                        msgBody.append("<img src='cid:" + imgFile + "' height='55' width='55'/><br/>\n");
                        msgBody.append(fileBean.getFilename());
                        if (CommonMethods.isIn(newBean.getDeleteFilePkArr(), fileBean.getFilePk())) msgBody.append("</strike></span>\n");
                        msgBody.append("</div>\n");
                }

                //New files uploaded
                if (newBean.getFileList() != null) {
                    String imgNewIcon = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_new.gif"), "New");
                    for (FormFile f : newBean.getFileList()) {
                        if (f.getFileSize() > 0) {
                            String filename = f.getFileName();
                            String extension = filename.lastIndexOf(".") > -1 ? filename.substring(filename.lastIndexOf(".") + 1) : "";
                            if (extension.length() > 4) extension = extension.substring(0, 4);

                            String imgFile = this.email.embed(new java.io.File("C:/cloaked/pshi/" + CommonMethods.getFileIcon(extension, "lrg")), extension);
                            msgBody.append("<div style='text-align:center;color:green;'><img src='cid:" + imgFile + "' height='55' width='55'/><br/>" + filename + "&nbsp;<img src='cid:" + imgNewIcon + "'/></div>\n");
                        }
                    }
                }

                msgBody.append("</td></tr>\n");
            }

            /* Weekly Meeting Agenda */
            boolean isStaffMeetingIndOld = oldBean.getStaffMeetingInd().equals("Y");
            boolean isClientMeetingIndOld = oldBean.getClientMeetingInd().equals("Y");
            boolean isStaffMeetingIndNew = newBean.getStaffMeetingInd().equals("Y");
            boolean isClientMeetingIndNew = newBean.getClientMeetingInd().equals("Y");
            if (isStaffMeetingIndOld || isClientMeetingIndOld || isStaffMeetingIndNew || isClientMeetingIndNew) {
                msgBody.append("<tr><th>Weekly Meeting Agenda</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left' style='padding:0 10px 0 25px;'>\n");
                if (isStaffMeetingIndOld && isStaffMeetingIndNew) {
                    msgBody.append(CHECK_APPROVED + " Weekly Staff Meeting Agenda<br/>\n");
                } else if (isStaffMeetingIndOld) {
                    msgBody.append("<span style='color:red'><strike>" + CHECK_APPROVED + " Weekly Staff Meeting Agenda</strike></span><br/>\n");
                } else if (isStaffMeetingIndNew) {
                    msgBody.append("<span style='color:green'>" + CHECK_APPROVED + " Weekly Staff Meeting Agenda</span><br/>\n");
                }
                if (isClientMeetingIndOld && isClientMeetingIndNew) {
                    msgBody.append(CHECK_APPROVED + " Weekly Client Meeting Agenda");
                } else if (isClientMeetingIndOld) {
                    msgBody.append("<span style='color:red'><strike>" + CHECK_APPROVED + " Weekly Client Meeting Agenda</strike></span>\n");
                } else if (isClientMeetingIndNew) {
                    msgBody.append("<span style='color:green'>" + CHECK_APPROVED + " Weekly Client Meeting Agenda</span>\n");
                }
                msgBody.append("</td></tr>\n");
            }

            /* Internal Notes */
            if (!CommonMethods.isEmpty(oldBean.getNotes()) || !CommonMethods.isEmpty(newBean.getNotes())) {
                msgBody.append("<tr><th>Internal Notes</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");
                if (CommonMethods.nes(oldBean.getNotes()).equals(CommonMethods.nes(newBean.getNotes()))) {
                    msgBody.append(newBean.getNotes().replaceAll("\r\n", "<br/>"));
                } else {
                    msgBody.append("<span style='color:red'><strike>" + CommonMethods.nes(oldBean.getNotes()).replaceAll("\r\n", "<br/>") + "</strike></span>" + (!CommonMethods.isEmpty(oldBean.getNotes()) ? "<br/>" : "") + "<span style='color:green'>" + CommonMethods.nes(newBean.getNotes()).replaceAll("\r\n", "<br/>") + "</span>\n");
                }
                msgBody.append("</td></tr>\n");
            }

            msgBody.append("        </tbody>\n");
            msgBody.append("        </table>\n");
            msgBody.append("    </td></tr>\n");
            msgBody.append("</tbody>\n");
            msgBody.append("</table>\n");

            msgBody.append(EMAIL_FOOTER);

            msgBody.append("</body>\n");
            msgBody.append("</html>\n");

            this.email.setFrom(EMAIL_SUPPORT);
            this.email.setHtmlMsg(msgBody.toString());
            this.email.setTextMsg("http://" + request.getServerName() + "/project.do?action=taskDetail&projectPk=" + newBean.getProjectPk() + "&taskPk=" + newBean.getTaskPk() + "\r\n\r\n" + "HTML is not suppported by your e-mail client");

            // send the email
            this.email.send();
        } catch (Exception e) {
            debugLog("ERROR", "sendTaskEditHtmlEmail", e);
        }
    }

    private String changedThisToThat(String oldValue, String newValue) {
        return "<span style='color:red'><strike>" + oldValue + "</strike></span> <span style='color:green'>" + newValue + "</span>\n";
    }

    private String displayCheckedOrUnchecked(boolean approved) {
        return approved ? CHECK_APPROVED : CHECK_UNAPPROVED;
    }

    public void sendTaskDeleteHtmlEmail(Connection conn, ProjectBean oldBean, LoginBean loginBean, HttpServletRequest request) {
        try {
            //Only send to new poc if not null and new POC is not user
            String oldPoc = !CommonMethods.isEmpty(oldBean.getPersonAssigned()) && !oldBean.getPersonAssigned().equals(loginBean.getFullName()) ? oldBean.getPersonAssigned() : null;
            String oldPocEmail = UserModel.getEmployeeEmail(conn, oldPoc);

            if (CommonMethods.isEmpty(oldPocEmail)) { //Only send to PM
                this.email.addTo(emailProjectManager);
                //this.email.addBcc(emailDev);
            } else {
                this.email.addTo(oldPocEmail);
                if (!CommonMethods.nes(oldPocEmail).equals(emailProjectManager)) this.email.addCc(emailProjectManager);
                //this.email.addBcc(emailDev);
            }

            this.email.setSubject("[Automated] Task #" + oldBean.getTaskPk() + " Deleted");

            StringBuffer msgBody = new StringBuffer();
            applyEmailHeader(msgBody);

            /* E-Mail Intro & Notification Summary */
            if (!CommonMethods.isEmpty(oldPoc)) { //Notify 1 person
                msgBody.append("<p>" + oldPoc + ",</p>\n");
                msgBody.append("<p>This is a notification that Task #" + oldBean.getTaskPk() + " assigned to you has been deleted by " + loginBean.getFullName() + ".</p>\n");
            } else { //Only notify PM
                msgBody.append("<p>Project Manager,</p>\n");
                msgBody.append("<p>This is a notification that Task #" + oldBean.getTaskPk() + " has been deleted by " + loginBean.getFullName() + ".</p>\n");
            }


            /* General Information */
            msgBody.append("<table id='tanTable_style2' border='0' cellspacing='0' width='800'>\n");
            msgBody.append("<tbody>\n");
            msgBody.append("    <tr><th>General Information</th></tr>\n");
            msgBody.append("    <tr><td class='nobordered' align='left'>\n");
            msgBody.append("        <table id='detailTable' border='0' cellspacing='0'>\n");
            msgBody.append("        <tbody>\n");
            msgBody.append("            <tr>\n");
            msgBody.append("                <td width='95' class='fieldName'>Category:</td>\n");
            msgBody.append("                <td width='250'>" + CommonMethods.nes(oldBean.getCategory()) + "</td>\n");
            msgBody.append("                <td class='fieldName' width='135'>Person Assigned:</td>\n");
            msgBody.append("                <td width='250'>" + CommonMethods.nvl(oldBean.getPersonAssigned(), "<i>None</i>") + "</td>\n");
            if (oldBean.getIsInternal().equals("Y")) {
                msgBody.append("                    <td class='fieldName' width='50'>Internal:</td>\n");
                msgBody.append("                    <td width='20'>" + CHECK_APPROVED + "</td>\n");
            } else {
                msgBody.append("                    <td width='50'/>\n");
                msgBody.append("                    <td width='20'/>\n");
            }
            msgBody.append("            </tr>\n");
            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Title:</td>\n");
            msgBody.append("                <td colspan='5'>" + CommonMethods.nes(oldBean.getTitle()) + "</td>\n");
            msgBody.append("            </tr>\n");
            msgBody.append("            <tr>\n");
            msgBody.append("                <td class='fieldName'>Description:</td>\n");
            msgBody.append("                <td colspan='5'>" + CommonMethods.nes(oldBean.getDescription()).replaceAll("\r\n", "<br/>") + "</td>\n");
            msgBody.append("            </tr>\n");
            if (!CommonMethods.isEmpty(oldBean.getSource())) {
                msgBody.append("            <tr>\n");
                msgBody.append("                <td class='fieldName'>Source:</td>\n");
                msgBody.append("                <td colspan='5'>" + oldBean.getSource() + "</td>\n");
                msgBody.append("            </tr>\n");
            }
            if (!CommonMethods.isEmpty(oldBean.getUic())) {
                msgBody.append("            <tr>\n");
                msgBody.append("                <td class='fieldName'>Ship:</td>\n");
                msgBody.append("                <td colspan='5'>" + CommonMethods.nes(ShipModel.getShipName(conn, oldBean.getUic())) + "</td>\n");
                msgBody.append("            </tr>\n");
            }
            msgBody.append("    </tbody>\n");
            msgBody.append("    </table>\n");
            msgBody.append("</td></tr>\n");

            /* Status */
            msgBody.append("<tr><th>Status</th></tr>\n");
            msgBody.append("<tr><td class='nobordered' align='left'>\n");
            msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");
            msgBody.append("    <tbody>\n");
            msgBody.append("        <tr>\n");

            msgBody.append("            <td width='95' class='fieldName'>Priority:</td>\n");
            msgBody.append("                <td width='140' style='" + oldBean.getPriorityCss() + "'>" + CommonMethods.nes(oldBean.getPriority()) + "</td>\n");

            msgBody.append("            <td class='fieldName' width='105'>Date Created:</td>\n");
            msgBody.append("            <td width='100'>" + CommonMethods.getDate("MM/DD/YYYY") + "</td>\n");

            msgBody.append("            <td width='80' class='fieldName'>Created By:</td>\n");
            msgBody.append("            <td colspan='2' width='180'>" + loginBean.getFullName() + "</td>\n");

            msgBody.append("            <td width='80'/>\n");
            msgBody.append("        </tr>\n");

            msgBody.append("        <tr>\n");
            msgBody.append("            <td class='fieldName'>Status:</td>\n");
            msgBody.append("                <td style='" + oldBean.getStatusCss() + "'>" + CommonMethods.nes(oldBean.getStatus()) + "</td>\n");

            if (!CommonMethods.isEmpty(oldBean.getDueDate())) {
                msgBody.append("                <td class='fieldName'>Due Date:</td>\n");
                msgBody.append("                <td style='" + oldBean.getDueDateCss() + "'>" + oldBean.getDueDate() + "</td>\n");
            } else {
                msgBody.append("            <td></td>\n");
                msgBody.append("            <td></td>\n");
            }

            if (!CommonMethods.isEmpty(oldBean.getFollowUpDate())) {
                msgBody.append("                <td class='fieldName'>Follow Up:</td>\n");
                msgBody.append("                <td>" + oldBean.getFollowUpDate() + "</td>\n");
            } else {
                msgBody.append("            <td></td>\n");
                msgBody.append("            <td></td>\n");
            }

            if (CommonMethods.nes(oldBean.getStatus()).equals("Completed") && !CommonMethods.isEmpty(oldBean.getCompletedDate())) {
                msgBody.append("                <td class='fieldName'>Completed:</td>\n");
                msgBody.append("                <td>" + oldBean.getCompletedDate() + "</td>\n");
            }

            msgBody.append("        </tr>\n");
            msgBody.append("    </tbody>\n");
            msgBody.append("    </table>\n");
            msgBody.append("</td></tr>\n");

            /* Future Requests */
            if (oldBean.getCategory().equals("Future Requests")) {
                msgBody.append("<tr><th>Future Features</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");
                msgBody.append("    <table id='detailTable' border='0' cellspacing='0'>\n");

                msgBody.append("    <tbody>\n");
                msgBody.append("        <tr>\n");

                if (!CommonMethods.isEmpty(oldBean.getQuarterYear())) {
                    msgBody.append("<td class='fieldName' width='95'>Target Quarter:</td>\n");
                    msgBody.append("<td width='135'>" + oldBean.getQuarterYear() + "</td>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getEffortArea())) {
                    msgBody.append("<td class='fieldName' width='80'>Area of Effort:</td>\n");
                    msgBody.append("<td width='130'>" + oldBean.getEffortArea() + "</td>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getEffortType())) {
                    msgBody.append("<td class='fieldName' width='100'>Type of Effort:</td>\n");
                    msgBody.append("<td width='140'>" + oldBean.getEffortType() + "</td>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getLoe())) {
                    msgBody.append("<td class='fieldName' width='50'>LOE:</td>\n");
                    msgBody.append("<td width='50'>" + oldBean.getLoe() + "</td>\n");
                }

                msgBody.append("    </tr>\n");

                msgBody.append("    <tr>\n");
                msgBody.append("        <td class='fieldName' rowspan='2'>Approval:</td>\n");
                msgBody.append("        <td rowspan='2'>\n");

                msgBody.append(displayCheckedOrUnchecked(CommonMethods.nes(oldBean.getIsClientApproved()).equals("Y")) + "\n");
                msgBody.append("&nbsp;Client<br/>\n");
                msgBody.append(displayCheckedOrUnchecked(CommonMethods.nes(oldBean.getIsPshiApproved()).equals("Y")) + "\n");

                msgBody.append("&nbsp;PSHI");
                msgBody.append("        </td>\n");

                if (!CommonMethods.isEmpty(oldBean.getClientPriority())) {
                    msgBody.append("        <td class='fieldName'>Priority:</td>\n");
                    msgBody.append("        <td>" + oldBean.getClientPriority() + "</td>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getVersionIncluded())) {
                    msgBody.append("        <td class='fieldName' rowspan='2'>Fixed/Added<br/>in Version:</td>\n");
                    msgBody.append("        <td rowspan='2'>" + oldBean.getVersionIncluded().replaceAll("\r\n", "<br/>") + "</td>\n");
                }

                msgBody.append("    </tr>\n");

                if (!CommonMethods.isEmpty(oldBean.getRecommendation())) {
                    msgBody.append("    <tr>\n");
                    msgBody.append("        <td class='fieldName'>Recommendation:</td>\n");
                    msgBody.append("        <td>" + oldBean.getRecommendation() + "</td>\n");
                    msgBody.append("    </tr>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getDocNotes())) {
                    msgBody.append("    <tr>\n");
                    msgBody.append("        <td class='fieldName'>Doc Updated:</td>\n");
                    msgBody.append("        <td colspan='3'>" + oldBean.getDocNotes() + "</td>\n");
                    msgBody.append("    </tr>\n");
                }

                msgBody.append("    </tbody>\n");
                msgBody.append("    </table>\n");
                msgBody.append("</td></tr>\n");
            }

            /* Sub-Tasks */
            if (oldBean.getSubTaskList().size() > 0 || !CommonMethods.isEmpty(oldBean.getSubTasks())) {
                msgBody.append("<tr><th>Sub-Tasks</th></tr>\n");
                if (oldBean.getSubTaskList().size() > 0) {
                    msgBody.append("    <tr><td class='nobordered' align='left' style='padding-left:25px;'>\n");
                    msgBody.append("        <div id='user-task-list'>\n");

                    for (ProjectBean subTaskBean : oldBean.getSubTaskList()) {
                        msgBody.append("<p>\n");

                        msgBody.append(displayCheckedOrUnchecked(subTaskBean.getStatus().equals("Completed")) + "&nbsp;");
                        msgBody.append(subTaskBean.getDescription());
                        msgBody.append("<span style='color:#888;'>\n");

                        if (!CommonMethods.isEmpty(subTaskBean.getPersonAssigned()))
                            msgBody.append(" [Assigned to " + subTaskBean.getPersonAssigned() + "]");

                        if (subTaskBean.getStatus().equals("Completed") && !CommonMethods.isEmpty(subTaskBean.getCompletedDate()))
                            msgBody.append(" (Completed " + subTaskBean.getCompletedDate() + ")");
                        else if (subTaskBean.getStatus().equals("Completed"))
                            msgBody.append(" (Completed)");
                        else if (!CommonMethods.isEmpty(subTaskBean.getStatus()) && !CommonMethods.isEmpty(subTaskBean.getDueDate()))
                            msgBody.append(" (" + subTaskBean.getStatus() + " - Due: " + subTaskBean.getDueDate() + ")");
                        else if (!CommonMethods.isEmpty(subTaskBean.getStatus()))
                            msgBody.append(" (" + subTaskBean.getStatus() + ")");

                        msgBody.append(" </span>\n");
                        msgBody.append("</p>\n");
                    }

                    msgBody.append("    </div>\n");
                    msgBody.append("</td></tr>\n");
                }

                if (!CommonMethods.isEmpty(oldBean.getSubTasks())) {
                    msgBody.append("<tr><td class='nobordered' align='left' style='padding:10px 10px 10px 25px;'>\n");
                    msgBody.append(oldBean.getSubTasks().replaceAll("\r\n", "<br/>"));
                    msgBody.append("</td></tr>\n");
                }
            }

            /* Files */
            if (oldBean.getTaskFileList().size() > 0) {
                msgBody.append("<tr><th>Files</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");
                //New files uploaded
                for (FileBean fileBean : oldBean.getTaskFileList()) {
                    msgBody.append("<div style='text-align:center;'>" + FILE_IMAGE + "<br/>" + fileBean.getFilename() + "</div>\n");
                }
                msgBody.append("</td></tr>\n");
            }

            /* Weekly Meeting Agenda */
            if (oldBean.getStaffMeetingInd().equals("Y") || oldBean.getClientMeetingInd().equals("Y")) {
                String imgCheckmark = this.email.embed(new java.io.File("C:\\cloaked\\pshi\\images\\icon_checkmark.png"), "Checkmark");

                msgBody.append("<tr><th>Weekly Meeting Agenda</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left' style='padding:0 10px 0 25px;'>\n");

                if (oldBean.getStaffMeetingInd().equals("Y")) {
                    msgBody.append("<img src='cid:" + imgCheckmark + "' height='12' width='12'/> Weekly Staff Meeting Agenda<br/>\n");
                }

                if (oldBean.getClientMeetingInd().equals("Y")) {
                    msgBody.append("<img src='cid:" + imgCheckmark + "' height='12' width='12'/> Weekly Client Meeting Agenda");
                }

                msgBody.append("</td></tr>\n");
            }

            /* Internal Notes */
            if (!CommonMethods.isEmpty(oldBean.getNotes())) {
                msgBody.append("<tr><th>Internal Notes</th></tr>\n");
                msgBody.append("<tr><td class='nobordered' align='left'>\n");
                msgBody.append(oldBean.getNotes().replaceAll("\r\n", "<br/>"));
                msgBody.append("</td></tr>\n");
            }

            msgBody.append("        </tbody>\n");
            msgBody.append("        </table>\n");
            msgBody.append("    </td></tr>\n");
            msgBody.append("</tbody>\n");
            msgBody.append("</table>\n");

            msgBody.append(EMAIL_FOOTER);

            msgBody.append("</body>\n");
            msgBody.append("</html>\n");

            this.email.setFrom(EMAIL_SUPPORT);
            this.email.setHtmlMsg(msgBody.toString());
            this.email.setTextMsg(oldBean.getTaskPk() + " has been deleted\r\n\r\n" + "HTML is not suppported by your e-mail client");
            this.email.send();
        } catch (Exception e) {
            debugLog("ERROR", "sendTaskDeleteHtmlEmail", e);
        }
    }

    public void sendNewIssueEmail(Connection conn, SupportBean inputBean, int newIssuePk, LoginBean loginBean) {
        if (loginBean == null) {
            logError("sendNewIssueEmail", "loginBean was not set");
            return;
        }
        String currUserFullName = loginBean.getFullName();
        String personAssigned = inputBean.getPersonAssigned();
        if (StringUtils.safeEquals(currUserFullName, personAssigned)) {
            //do nothing email does not need to be sent.
            return;
        }

        //Exit if person assigned has no email
        String emailTo = UserModel.getEmployeeEmail(conn, personAssigned);
        if (CommonMethods.isEmpty(emailTo)) {
            return;
        }

        StringBuilder subject = new StringBuilder();
        applyIssuePkToSubject(newIssuePk, subject);
        subject.append(" New Issue");
        if (CommonMethods.nes(inputBean.getPriority()).equals("High")) {
            subject.append(" - HIGH PRIORITY");
            String priorityReason = inputBean.getPriorityReason();
            if (!CommonMethods.isEmpty(priorityReason)) {
                subject.append(" (" + priorityReason + ")");
            }
        }

        String projectPk = inputBean.getProjectPk();
        StringBuilder str = new StringBuilder();
        str.append("Issue #").append(newIssuePk).append(" has been created by ").append(currUserFullName)
            .append(" and assigned to you.\n\n");
        addIssueLink(newIssuePk, projectPk, str);
        String textMsg = str.toString();
        String body = null;
        String cc = null;
        sendEmail(subject.toString(), textMsg, body, emailTo, EMAIL_SUPPORT, null, cc);
    }

    private void addIssueLink(int issuePk, String projectPk, StringBuilder str) {
        str.append(BASE_URL)
            .append("issue.do?id=")
            .append(issuePk)
            .append("&projectPk=")
            .append(projectPk)
            .append("&pageFrom=issueList")
        ;
    }

    public void sendEditIssueEmail(Connection conn, SupportBean inputBean, SupportBean oldBean, LoginBean loginBean) {
        if (inputBean == null) {
            logError("sendEditIssueEmail", "inputBean was null. Email Cannot be sent without it.");
            return;
        }
        if (loginBean == null) {
            logError("sendEditIssueEmail", "loginBean was null. Email Cannot be sent without it.");
            return;
        }
        if (oldBean == null) {
            logError("sendEditIssueEmail", "oldBean was null. Email Cannot be sent without it.");
            return;
        }
        Integer issuePk = StringUtils.parseInt(inputBean.getIssuePk());
        boolean changeDetected = false;

        //Send to current assignee only if it isn't the current user
        String currUserFullName = loginBean.getFullName();
        String personAssigned = inputBean.getPersonAssigned();
        if (personAssigned != null) {
            personAssigned = personAssigned.trim();
        }

        //---- Determine who this email will be sent to
        //email will not be sent if the user who created the Issue is assigning it to him/herself.
        String emailTo = null;
        String personAssignedOldEmail = null;
        if (!StringUtils.safeEquals(personAssigned, currUserFullName)) {
            emailTo = UserModel.getEmployeeEmail(conn, personAssigned);
        }

        //---- Build subject
        StringBuilder subject = new StringBuilder();
        applyIssuePkToSubject(issuePk, subject);
        subject.append("Issue Updated [Automated]");

        String priorityReason = inputBean.getPriorityReason();
        boolean priorityIsHigh = StringUtils.safeEquals(inputBean.getPriority(), "High");
        boolean priorityIsHighOld = StringUtils.safeEquals(oldBean.getPriority(), "High");
        if (priorityIsHigh) {
            subject.append(" - HIGH PRIORITY");
            if (!CommonMethods.isEmpty(priorityReason)) {
                subject.append(" (" + priorityReason + ")");
            }
        }

        //---- Build e-mail message
        StringBuilder message = new StringBuilder();
        //---- A second email may be sent to the previously assigned person.
        //If person assigned has changed, email the original person assigned as long as it isn't the current user
        String personAssignedOld = oldBean.getPersonAssigned();
        if (!StringUtils.safeEquals(personAssigned, personAssignedOld)) {
            changeDetected = true;
            if (message.length() > 0) {
                message.append("\r\n\r\n");
            }
            message.append("Issue has been re-assigned to " + personAssigned);
            message.append("\n\n");
        }

        if (!StringUtils.safeEquals(personAssignedOld, currUserFullName)) {
            personAssignedOldEmail = UserModel.getEmployeeEmail(conn, personAssignedOld);
        }

        String projectPk = inputBean.getProjectPk();
        message.append("View Issue #").append(issuePk).append(":\r\n");
        addIssueLink(issuePk, projectPk, message);
        message.append("\n\n");

        //If Priority is elevated
        if (priorityIsHigh && !priorityIsHighOld) {
            changeDetected = true;
            message.append("Priority elevated to HIGH");
            if (!CommonMethods.isEmpty(priorityReason)) message.append(" - " + priorityReason);
            message.append("\n\n");
        }

        //If status has changed
        if (!inputBean.getStatus().equals(oldBean.getStatus())) {
            changeDetected = true;
            message.append("Status has been updated to " + inputBean.getStatus());
            message.append("\n\n");
        }

        //If new comments are added
        if (inputBean.getCommentsArr() != null && inputBean.getCommentsArr().length > 0) {
            changeDetected = true;
            message.append("New Comments Added:\r\n");
            for (String comments : inputBean.getCommentsArr()) {
                message.append(comments);
                message.append("\n\n");
            }
        }

        //---- Exit if person assigned current and previously has no email
        if (CommonMethods.isEmpty(emailTo) && CommonMethods.isEmpty(personAssignedOldEmail)) return;

        //Exit if no change conditions to send
        if (!changeDetected) return;

        //add first line
        message.insert(0, "Issue #" + inputBean.getIssuePk() + " has been updated by " + currUserFullName + ".\r\n\r\n");

        String subjectStr = subject.toString();
        String textMsg = message.toString();

        if (!StringUtils.isEmpty(emailTo)) {
            sendSimpleEmail(subjectStr, textMsg, emailTo, EMAIL_SUPPORT);
        }
        if (!StringUtils.isEmpty(personAssignedOldEmail)) {
            sendSimpleEmail(subjectStr, textMsg, personAssignedOldEmail, EMAIL_SUPPORT);
        }
    }

    private void applyIssuePkToSubject(Integer issuePk, StringBuilder subject) {
        subject.append("[Issue #").append(issuePk).append("] ");
    }

    private void applyTaskPkToSubject(Object taskPk, StringBuilder subject) {
        subject.append("[Task #").append(taskPk).append("] ");
    }

  /* ashwini 12/05 mail*/
    
    public void sendMail(String Emailto) 
    {
    	try {
        	Email email = new SimpleEmail();
        	email.setHostName("smtp.gmail.com");
        	email.setSmtpPort(587);
        	email.setAuthenticator(new DefaultAuthenticator("ashwinik695@gmail.com", "Ayush@2511"));
        	email.setSSLOnConnect(true);
    		email.setFrom("ashwinik695@gmail.com");
    		email.setSubject("TestMail");
    		email.setMsg("PLz Send the Travel report to management");
    		email.addTo(Emailto);
    		email.send();
    		System.out.println("Mail send successfully");
    }
    	 catch (EmailException e) {
 			
    		 System.out.print(e);
 			e.printStackTrace();
 		}
    }
    
    public void sendSubTaskEmail(Connection conn, HashMap<String, ArrayList<String>> inputMap, ProjectBean inputBean, LoginBean loginBean) {
        for (String key : inputMap.keySet()) {
            String emailTo = null;

            //Send to current assignee only if it isn't the current user
            if (!key.equals(loginBean.getFullName())) {
                emailTo = UserModel.getEmployeeEmail(conn, key);
            }

            String taskPk = inputBean.getTaskPk();
            StringBuilder subjectStr = new StringBuilder();
            applyTaskPkToSubject(taskPk, subjectStr);
            subjectStr.append("Sub-Task Notification [Automated]");

            String subject = subjectStr.toString();
            StringBuilder message = new StringBuilder();
            for (String str : inputMap.get(key)) {
                message.append(str);
                message.append("\r\n\r\n");
            }
            //Exit if person assigned has no email
            if (!CommonMethods.isEmpty(emailTo) && message.length() > 0) {
                //add first line
                message.insert(0, "The following sub-tasks have been updated by " + loginBean.getFullName() + ".\r\n\r\n");

                sendSimpleEmail(subject, message.toString(), emailTo, EMAIL_SUPPORT);
                //this.email.addBcc(EMAIL_ANTONE3X7);
            }
        }
    }

    
  
    
    
    public void sendTestEmail() {
        String to = "ashwinik695@gmail.com";
           	
       // String to = getEmailDev();
        String body = "Email body <i>Italicized</i><br/>"
            + "Approved: " + CHECK_APPROVED + "<br/>"
            + "Unapproved: " + CHECK_UNAPPROVED + "<br/>"
            + "Changed <span style='color:red'><strike>" + displayCheckedOrUnchecked(true) + "</strike></span>"
            + " to " + displayCheckedOrUnchecked(false) + "</strike><br/>"
            + changedThisToThat(displayCheckedOrUnchecked(true), displayCheckedOrUnchecked(false))
        ;
        sendHtmlEmail("Test Email Subject", body, to, EMAIL_SUPPORT);
        //System.out.println("Email send successfully");
    }

    public boolean isProd() {
        return isProd;
    }

    public void setProd(boolean isProd) {
        this.isProd = isProd;
    }

    protected String getEmailDev() {
        return emailDev;
    }

    protected void setEmailDev(String emailDev) {
        this.emailDev = emailDev;
    }

    protected String getEmailProjectManager() {
        return emailProjectManager;
    }

    protected void setEmailProjectManager(String emailProjectManager) {
        this.emailProjectManager = emailProjectManager;
    }

    public static void main(String[] args) {
        EmailModel email;
        try {
            email = new EmailModel();
            email.setProd(true);
            //sendMail();
           email.sendTestEmail();
           // email.sendMail("ashwinik695@gmail.com");
          //  System.out.println("Email send successfully");
        }
        catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
