<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Manage PTO/Travel"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask holygrail"><div class="colmid"><div class="colleft">
  <div class="col1wrap"><div class="col1">
    <!-- Main Column 1 start -->
    <%@ include file="../include/content-header.jsp" %>

    <tags:adminBreadcrumb pageTitle="${pageTitle}" />

    <p align="center">
      <a href="user.do?action=ptoTravelAdd">
        <img src="images/icon_plus.gif"/> Add New PTO/Travel Event
      </a>

      <logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
        <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
          <bean:define id="projectPk" value="${leftbar_resultBean.projectPk}"/>
          <logic:equal name="leftbar_resultBean" property="projectPk" value="1">
            <br/>
            <span style="margin:5px;">
              <a href="support.do?action=shipVisitCalendar&projectPk=1">
                <img src="./images/icon_view.png"/>
                View ${leftbar_projectBean.customer} ${leftbar_resultBean.projectName} Support Calendar
              </a>
            </span>
          </logic:equal>
        </logic:iterate>
      </logic:iterate>
    </p>

    <% if (resultList.size() == 0) { %>
      <p class="error" align="center">
        No Events Found
      </p>
    <% } else { %>
      <p align="center">
      <table id="dataTable_style2" border="0" cellspacing="0" class="alt-color">
      <thead>
        <tr align="center">
          <th align="left">User</th>
          <th>Type</th>
          <th align="left">Location</th>
          <th>Begin</th>
          <th>End</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
      <logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.UserBean">

        <% if (CommonMethods.isValidDateStr(resultBean.getEndDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getEndDate()) < 0) { %>
          <tr style="color:#ccc;">
        <% } else if (CommonMethods.isValidDateStr(resultBean.getStartDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getStartDate()) <= 0
                      && CommonMethods.isValidDateStr(resultBean.getEndDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getEndDate()) >= 0) { %>
          <tr style="color:#00f;">
        <% } else { %>
          <tr>
        <% } %>

          <td>
            <bean:write name="resultBean" property="firstName"/>
            <bean:write name="resultBean" property="lastName"/>
          </td>
          <td align="center"><bean:write name="resultBean" property="leaveType"/></td>
          <td><bean:write name="resultBean" property="location"/></td>
          <td align="center"><bean:write name="resultBean" property="startDate"/></td>
          <td align="center"><bean:write name="resultBean" property="endDate"/></td>
          <td align="center" nowrap>
            <a href="user.do?action=ptoTravelEdit&ptoTravelPk=<bean:write name="resultBean" property="ptoTravelPk"/>"><img src="images/icon_edit.png" title="Edit"/></a>
            &nbsp;<a href="user.do?action=ptoTravelDeleteDo&ptoTravelPk=<bean:write name="resultBean" property="ptoTravelPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="ptoTravelPk"/>');"><img src="images/icon_delete.png" title="Delete"/></a>
          </td>
        </tr>
      </logic:iterate>
      </tbody>
      </table>
      </p>
    <% } %>
    <!-- Main Column 1 end -->
  </div></div>
  <%@ include file="../include/app-col2.jsp" %>
  <%@ include file="../include/app-col3.jsp" %>
</div></div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
  function confirmDelete(username) {
    return confirm("Are you sure you want to delete " + username);
  }
</script>

</body>
</html>
