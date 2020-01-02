<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Task Details"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"    scope="request" class="java.lang.String"/>
<jsp:useBean id="projectBean" scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="resultBean"  scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>

<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currOsVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"   scope="request" class="java.lang.String"/>

<jsp:useBean id="currStr"      scope="request" class="java.lang.String"/>
<jsp:useBean id="prevBean"    scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="nextBean"    scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-popup.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>
<%--ashwini project.do ./project.do--%>
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="./project.do?action=taskList&searchPerformed=Y&projectPk=${projectPk}" parentTitle="Task List"/>

    <div class="center">
      <table id="borderlessTable" class="border-zero cellspacing-zero">
      <tbody>
        <tr>
          <td align="left" style="width:16px" nowrap>
            <% if (prevBean != null && !CommonMethods.isEmpty(prevBean.getTaskPk())) { %>
              <a href="project.do?action=taskDetail&taskPk=${prevBean.taskPk}&projectPk=${projectPk}"><img src="images/arrow_left.png" height="16" style="width:16px"/></a>
            <% } %>
          </td>
          <td align="center" nowrap>${currStr}</td>
          <td align="right" style="width:16px" nowrap>
            <% if (nextBean != null && !CommonMethods.isEmpty(nextBean.getTaskPk())) { %>
              <a href="project.do?action=taskDetail&taskPk=${nextBean.taskPk}&projectPk=${projectPk}"><img src="images/arrow_right.png" height="16" style="width:16px"/></a>
            <% } %>
          </td>
        </tr>
      </tbody>
      </table>
    </div>

    <p align="center">
      <a class="btn btn-primary" href="project.do?action=taskEdit&taskPk=${resultBean.taskPk}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-pencil"></span>
        Edit
      </a>
      <a class="btn btn-info" href="project.do?action=taskCopy&taskPk=${resultBean.taskPk}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-retweet"></span>
        Copy
      </a>
      <a class="btn btn-warning" href="support.do?action=generateFeedbackForm&uic=${resultBean.uic}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-file"></span>
        Generate Support Feedback Form
        </a>
      <a class="btn btn-danger" href="javascript:confirmDelete(${resultBean.taskPk});">
        <span class="glyphicon glyphicon-trash"></span>
        Delete
      </a>
      <a class="btn btn-default" href="project.do?action=taskList&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-list-alt"></span>
        Back
      </a>
    </p>

    <div class="center">
    <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width:800px">
    <tbody>
      <tr><th>General Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="detailTable" class="border-zero cellspacing-zero">
        <colgroup>
          <col style="width:105px"/>
          <col style="width:245px"/>
          <col style="width:135px"/>
          <col style="width:245px"/>
          <col style="width:50px"/>
          <col style="width:20px"/>
        </colgroup>
        <tbody>
          <logic:notEmpty name="resultBean" property="contractNumber">
          <tr>
            <td class="fieldName">Contract Number:</td>
            <td>${resultBean.contractNumber}</td>
          </tr>
          </logic:notEmpty>
          <tr>
            <td class="fieldName">Category:</td>
            <td>${resultBean.category}</td>

            <td class="fieldName">Person Assigned:</td>
            <td>
              <logic:notEmpty name="resultBean" property="personAssigned">
                <a href="#" class="poc-popup">${resultBean.personAssigned}</a>
              </logic:notEmpty>
              <logic:empty name="resultBean" property="personAssigned">
                <i>None</i>
              </logic:empty>
            </td>

            <logic:equal name="resultBean" property="isInternal" value="Y">
              <td class="fieldName">Internal:</td>
              <td><img src="images/icon_checkmark.png" height="12" style="width:12px"/></td>
            </logic:equal>
            <logic:notEqual name="resultBean" property="isInternal" value="Y">
              <td></td>
              <td></td>
            </logic:notEqual>
          </tr>
          <tr>
            <td class="fieldName">Title:</td>
            <td colspan="5">${resultBean.title}</td>
          </tr>
          <tr>
            <td class="fieldName TOP">Description:</td>
            <td colspan="5">
              <logic:iterate id="description" name="resultBean" property="descriptionBr">
                ${description}<br/>
              </logic:iterate>
            </td>
          </tr>

          <logic:notEmpty name="resultBean" property="source">
          <tr>
            <td class="fieldName">Source:</td>
            <td colspan="5">${resultBean.source}</td>
          </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="shipName">
          <tr>
            <td class="fieldName TOP">Ship:</td>
            <td colspan="5">
              <a href="#" class="ship-popup">${resultBean.shipName}</a><br/>
              <span style="color:#777;"><i>${resultBean.homeport}</i></span>
            </td>
          </tr>
          </logic:notEmpty>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Status</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="detailTable" class="border-zero cellspacing-zero">
        <colgroup>
          <col style="width:95px"/>
          <col style="width:140px"/>
          <col style="width:105px"/>
          <col style="width:100px"/>
          <col style="width:80px"/>
          <col style="width:100px"/>
          <col style="width:80px"/>
          <col style="width:80px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Priority:</td>
            <td style="${resultBean.priorityCss}">${resultBean.priority}</td>
            <td class="fieldName">Date Created:</td>
            <td>${resultBean.createdDate}</td>

            <logic:notEmpty name="resultBean" property="createdBy">
              <td class="fieldName">Created By:</td>
              <td colspan="2">${resultBean.createdBy}</td>
            </logic:notEmpty>
            <logic:empty name="resultBean" property="createdBy">
              <td></td>
              <td></td>
              <td></td>
            </logic:empty>

            <td></td>
          </tr>
          <tr>
            <td class="fieldName">Status:</td>
            <td style="${resultBean.statusCss}">${resultBean.status}</td>

            <logic:notEmpty name="resultBean" property="dueDate">
              <td class="fieldName">Due Date:</td>
              <td style="${resultBean.dueDateCss}">${resultBean.dueDate}</td>
            </logic:notEmpty>
            <logic:empty name="resultBean" property="dueDate">
              <td></td>
              <td></td>
            </logic:empty>

            <logic:notEmpty name="resultBean" property="followUpDate">
              <td class="fieldName">Follow Up:</td>
              <td>${resultBean.followUpDate}</td>
            </logic:notEmpty>
            <logic:empty name="resultBean" property="followUpDate">
              <td></td>
              <td></td>
            </logic:empty>

            <% if (resultBean.getStatus().equals("Completed") || resultBean.getStatus().equals("Not Needed")) { %>
            <logic:notEmpty name="resultBean" property="completedDate">
              <td class="fieldName">Completed:</td>
              <td>${resultBean.completedDate}</td>
            </logic:notEmpty>
            <% } %>
          </tr>

          <logic:equal name="resultBean" property="category" value="Future Requests">
            <tr>
              <logic:notEmpty name="resultBean" property="deployedDate">
                <td class="fieldName TOP">Deployed Date:</td>
                <td class="TOP">${resultBean.deployedDate}</td>
              </logic:notEmpty>
              <td class="fieldName TOP">Doc Updated?</td>
              <td class="TOP">${resultBean.docUpdatedInd}</td>

              <logic:notEmpty name="resultBean" property="docNotes">
                <td class="fieldName TOP">Doc Updated Notes:</td>
                <td class="TOP">${resultBean.docNotes}</td>
              </logic:notEmpty>
            </tr>

            <logic:notEmpty name="resultBean" property="versionIncluded">
              <tr>
                <td class="fieldName TOP" colspan="3">Fixed/Added in Version:</td>
                <td class="TOP" colspan="5">
                  <logic:iterate id="versionIncluded" name="resultBean" property="versionIncludedBr">
                    ${versionIncluded}<br/>
                  </logic:iterate>
                </td>
              </tr>
            </logic:notEmpty>

          </logic:equal>
        </tbody>
        </table>
      </td></tr>

      <logic:equal name="resultBean" property="category" value="Future Requests">
        <tr><th>Future Features</th></tr>
        <tr><td class="nobordered" align="left">
          <table id="detailTable" class="border-zero cellspacing-zero">
          <colgroup>
            <col style="width:95px"/>
            <col style="width:135px"/>
            <col style="width:90px"/>
            <col style="width:135px"/>
            <col style="width:90px"/>
            <col style="width:135px"/>
            <col style="width:50px"/>
            <col style="width:50px"/>
          </colgroup>
          <tbody>
            <tr>
              <logic:notEmpty name="resultBean" property="quarterYear">
                <td class="fieldName TOP">Target Quarter:</td>
                <td class="TOP">
                  <logic:notEqual name="resultBean" property="quarterYear" value="OOS">
                    ${resultBean.quarterYear}
                    <logic:equal name="resultBean" property="quarterYear" value="2014Q1">(2/1)</logic:equal>
                    <logic:equal name="resultBean" property="quarterYear" value="2014Q2">(4/1)</logic:equal>
                  </logic:notEqual>
                  <logic:equal name="resultBean" property="quarterYear" value="OOS">Out of Scope</logic:equal>
                </td>
              </logic:notEmpty>
              <logic:notEmpty name="resultBean" property="effortArea">
                <td class="fieldName TOP">Area of Effort:</td>
                <td class="TOP">${resultBean.effortArea}</td>
              </logic:notEmpty>
              <logic:notEmpty name="resultBean" property="effortType">
                <td class="fieldName TOP">Type of Effort:</td>
                <td class="TOP">${resultBean.effortType}</td>
              </logic:notEmpty>
              <logic:notEmpty name="resultBean" property="loe">
                <td class="fieldName TOP">LOE:</td>
                <td class="TOP">${resultBean.loe}</td>
              </logic:notEmpty>
            </tr>

            <tr>
              <td class="fieldName TOP" rowspan="2">Approval:</td>
              <td class="TOP" rowspan="2">
              <logic:equal name="resultBean" property="isClientApproved" value="Y">
                <img src="images/icon_checkmark.png" height="12" style="width:12px"/> Client
              </logic:equal>
              <logic:notEqual name="resultBean" property="isClientApproved" value="Y">
                <img src="images/checkbox_unchecked.png" height="12" style="width:12px"/> Client
              </logic:notEqual>

              <br/>

              <logic:equal name="resultBean" property="isPshiApproved" value="Y">
                <img src="images/icon_checkmark.png" height="12" style="width:12px"/> PSHI
              </logic:equal>
              <logic:notEqual name="resultBean" property="isPshiApproved" value="Y">
                <img src="images/checkbox_unchecked.png" height="12" style="width:12px"/> PSHI
              </logic:notEqual>
              </td>

              <logic:notEmpty name="resultBean" property="clientPriority">
                <td class="fieldName TOP">Priority:</td>
                <td class="TOP">${resultBean.clientPriority}</td>
              </logic:notEmpty>
            </tr>

            <logic:notEmpty name="resultBean" property="recommendation">
              <tr>
                <td class="fieldName TOP">Recommendation:</td>
                <td class="TOP">${resultBean.recommendation}</td>
              </tr>
            </logic:notEmpty>
          </tbody>
          </table>
        </td></tr>
      </logic:equal>

      <% if (resultBean.getSubTaskList().size() > 0 || !CommonMethods.isEmpty(resultBean.getSubTasks())) { %>
        <tr><th>Sub-Tasks</th></tr>
        <% if (resultBean.getSubTaskList().size() > 0) { %>
          <tr><td class="nobordered" align="left" style="padding-left:25px;">
            <div id="user-task-list">
            <ul>
              <logic:iterate id="subTaskBean" name="resultBean" property="subTaskList" type="com.premiersolutionshi.old.bean.ProjectBean">
                <% if (subTaskBean.getStatus().equals("Completed")) { %>
                  <li class="checked">
                <% } else { %>
                  <li>
                <% } %>

                ${subTaskBean.description}

                <span style="color:#888;">
                <% if (!CommonMethods.isEmpty(subTaskBean.getPersonAssigned())) { %>
                  [Assigned to <span style="color:#00F;">${subTaskBean.personAssigned}</span>]
                <% } %>

                <% if (subTaskBean.getStatus().equals("Completed") && !CommonMethods.isEmpty(subTaskBean.getCompletedDate())) { %>
                  (Completed ${subTaskBean.completedDate})
                <% } else if (subTaskBean.getStatus().equals("Completed")) { %>
                  (Completed)
                <% } else if (!CommonMethods.isEmpty(subTaskBean.getStatus()) && !CommonMethods.isEmpty(subTaskBean.getDueDate())) { %>
                  (${subTaskBean.status} - Due: <span style="${subTaskBean.dueDateCss}">${subTaskBean.dueDate}</span>)
                <% } else if (!CommonMethods.isEmpty(subTaskBean.getStatus())) { %>
                  (${subTaskBean.status})
                <% } %>
                </span>
              </li>
              </logic:iterate>
            </ul>
            </div>
          </td></tr>
        <% } %>

        <% if (!CommonMethods.isEmpty(resultBean.getSubTasks())) { %>
          <tr><td class="nobordered" align="left" style="padding:10px 10px 10px 25px;">
            <logic:iterate id="subTasks" name="resultBean" property="subTasksBr">
              ${subTasks}<br/>
            </logic:iterate>
          </td></tr>
        <% } %>
      <% } %>


      <% if (resultBean.getTaskFileList().size() > 0) { %>
        <tr><th>Files</th></tr>
        <tr><td class="nobordered" align="left">
          <div style="text-align:left;">
            <logic:iterate id="fileBean" name="resultBean" property="taskFileList" type="com.premiersolutionshi.old.bean.FileBean">
            <div style="padding:10px;text-align:center;display:inline-block;">
              <a href="download.do?filePk=${fileBean.filePk}"><img src="${fileBean.image}" style="width:55px" height="55"/><br/>
              ${fileBean.filename}
              </a>
            </div>
            </logic:iterate>
          </div>
        </td></tr>
      <% } %>


      <% if (resultBean.getStaffMeetingInd().equals("Y") || resultBean.getClientMeetingInd().equals("Y")) { %>
      <tr><th>Weekly Meeting Agenda</th></tr>
      <tr><td class="nobordered" align="left" style="padding:0 10px 0 25px;">
      <% if (resultBean.getStaffMeetingInd().equals("Y")) { %><p><img src="images/icon_checkmark.png" height="12" style="width:12px"/> Weekly Staff Meeting Agenda</p><% } %>
      <% if (resultBean.getClientMeetingInd().equals("Y")) { %><p><img src="images/icon_checkmark.png" height="12" style="width:12px"/> Weekly Client Meeting Agenda</p><% } %>
      </td></tr>
      <% } %>

      <% if (!CommonMethods.isEmpty(resultBean.getNotes())) { %>
      <tr><th>Internal Notes</th></tr>
      <tr><td class="nobordered" align="left">
        <logic:iterate id="notes" name="resultBean" property="notesBr">
          ${notes}<br/>
        </logic:iterate>
      </td></tr>
      <% } %>
    </tbody>
    </table>
    </div>

    <p align="center" style="margin-top:10px;">
      <a class="btn btn-primary" href="project.do?action=taskEdit&taskPk=${resultBean.taskPk}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-pencil"></span>
        Edit
      </a>
      <a class="btn btn-info" href="project.do?action=taskCopy&taskPk=${resultBean.taskPk}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-retweet"></span>
        Copy
      </a>
      <a class="btn btn-warning" href="support.do?action=generateFeedbackForm&uic=${resultBean.uic}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-file"></span>
        Generate Support Feedback Form
        </a>
      <a class="btn btn-danger" href="javascript:confirmDelete(${resultBean.taskPk});">
        <span class="glyphicon glyphicon-trash"></span>
        Delete
      </a>
      <a class="btn btn-default" href="project.do?action=taskList&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-list-alt"></span>
        Back
      </a>
    </p>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<div id="toPopup">
  <div class="close"></div>
  <span class="ecs_tooltip">Press Esc to close <span class="arrow"></span></span>
  <div id="popup_content"> <!--your content start-->
    <div id="poc_content"> <!-- poc content -->
      <%@ include file="../include/popup-poc.jsp" %>
    </div> <!-- poc content -->

    <div id="ship-popup"> <!-- ship content -->
      <%@ include file="../include/popup-ship.jsp" %>
    </div> <!-- ship-content -->
  </div> <!--your content end-->
</div> <!--toPopup end-->
<div class="loader"></div>
<div id="backgroundPopup"></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-popup.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
  function confirmDelete(taskPk) {
    if (confirm("Are you sure you want to delete task #" + taskPk)) {
      window.location = "project.do?action=taskDeleteDo&taskPk=" + taskPk + "&projectPk=${projectPk}";
    } //end of if
  } //end of confirmDelete
</script>

</body>
</html>
