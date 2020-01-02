<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="User Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask holygrail"><div class="colmid"><div class="colleft">
  <div class="col1wrap"><div class="col1">
    <!-- Main Column 1 start -->
    <%@ include file="../include/content-header.jsp" %>

    <tags:adminBreadcrumb pageTitle="${pageTitle}" 
      parentUrl="user.do?action=ptoTravelList" parentTitle="Manage PTO/Travel"/>

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <p align="center">
      <logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
        <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
          <bean:define id="projectPk" value="${leftbar_resultBean.projectPk}"/>
          <logic:equal name="leftbar_resultBean" property="projectPk" value="1">
            <br/>
            <span style="margin:5px;">
              <a href="support.do?action=shipVisitCalendar&projectPk=1">
                <img src="/images/icon_view.png"/>
                View ${leftbar_projectBean.customer} ${leftbar_resultBean.projectName} Support Calendar
              </a>
            </span>
          </logic:equal>
        </logic:iterate>
      </logic:iterate>
    </p>

    <html:form action="user.do" onsubmit="return valFields();" method="POST">
      <html:hidden name="inputBean" property="projectPk"/>
      <% if (editType.equals("add")) { %>
        <input type="hidden" name="action" value="ptoTravelAddDo"/>
      <% } else { %>
        <input type="hidden" name="action" value="ptoTravelEditDo"/>
        <html:hidden name="inputBean" property="ptoTravelPk"/>
      <% } %>
      <div class="center">
        <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width: 500px;">
        <tbody>
          <tr><th>Calendar Event Information</th></tr>
          <tr><td class="nobordered" align="left">
            <table class="border-zero cellspacing-zero cellpadding-3">
            <colgroup><col style="width:120px"/></colgroup>
            <tbody>
              <% if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) { %>
              <tr>
                <td class="fieldName"><span class="regAsterisk">*</span> User:
                <td colspan="3">
                  <html:select name="inputBean" property="userPk" styleClass="form-control input-sm">
                    <html:option value=""/>
                    <html:options collection="userList" property="userPk" labelProperty="fullName"/>
                  </html:select>
                </td>
              </tr>
              <% } else { %>
                <html:hidden name="loginBean" property="userPk"/>
              <% } %>
              <tr>
                <td class="fieldName"><span class="regAsterisk">*</span> Type:</td>
                <td>
                  <html:select name="inputBean" property="leaveType" styleClass="form-control input-sm">
                    <html:option value="PTO"/>
                    <html:option value="Travel"/>
                  </html:select>
                </td>
              </tr>
              <tr>
                <td class="fieldName"><span class="regAsterisk">*</span> From </td>
                <td><html:text name="inputBean" property="startDate" styleClass="form-control input-sm datepicker"/></td>
                <td class="fieldName">to</td>
                <td><html:text name="inputBean" property="endDate" styleClass="form-control input-sm datepicker"/></td>
              </tr>
              <tr>
                <td class="fieldName">Location:</td>
                <td colspan="3"><html:text name="inputBean" property="location" styleClass="form-control input-sm"/></td>
              </tr>
            </tbody>
            </table>
          </td></tr>
        </tbody>
        </table>
    
        <table id="borderlessTable" class="border-zero cellspacing-zero" style="width: 150px;"><tbody>
          <tr>
            <td align="center">
              <% if (editType.equals("add")) { %>
                <html:submit value="Insert" styleClass="btn btn-success"/>
              <% } else { %>
                <html:submit value="Save" styleClass="btn btn-primary"/>
              <% } %>
            </td>
            <td align="center">
              <a class="btn btn-default" href="user.do?action=ptoTravelList">Cancel</a>
            </td>
          </tr>
        </tbody></table>
      </div>
    </html:form>
    </div>
    <!-- Main Column 1 end -->
  </div>
  <%@ include file="../include/app-col2.jsp" %>
  <%@ include file="../include/app-col3.jsp" %>
</div></div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
  $(function() {
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();
  });

  $(document).ready(function () {
    <% if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) { %>
      document.userForm.userPk.focus();
    <% } else { %>
      document.userForm.leaveType.focus();
    <% } %>
  });

  function valFields() {
    var userPk = document.userForm.userPk.value;
    var leaveType = document.userForm.leaveType.value;
    var startDate = stripSpaces(document.userForm.startDate.value);
    var endDate = stripSpaces(document.userForm.endDate.value);

    document.userForm.startDate.value = startDate;
    document.userForm.endDate.value = endDate;

    if (userPk.length < 1) {
      alert("You must select a user.");
      document.userForm.userPk.focus();
      return false;
    } //end of if

    if (leaveType.length < 1) {
      alert("You must select a leave type.");
      document.userForm.leaveType.focus();
      return false;
    } //end of if

    if (startDate.length < 1) {
      alert("You must enter in a begin date.");
      document.userForm.startDate.focus();
      return false;
    } else if (!validateDate(startDate, "Begin Date")) {
      document.userForm.startDate.focus();
      return false;
    } //end of if

    if (endDate.length < 1) {
      alert("You must enter in an end date.");
      document.userForm.endDate.focus();
      return false;
    } else if (!validateDate(endDate, "End Date")) {
      document.userForm.endDate.focus();
      return false;
    } //end of if

    if (dateDiff(startDate, endDate) < 0) {
      alert("End date must be after start date.");
      document.userForm.endDate.focus();
      return false;
    } //end of if

    return true;
  } //end of valFields
</script>

</body>
</html>
