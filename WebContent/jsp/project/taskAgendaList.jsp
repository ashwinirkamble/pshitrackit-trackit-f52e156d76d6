<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Weekly Agenda"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="agenda" scope="request" class="java.lang.String"/>
<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>TrackIT - <bean:write name="pageTitle"/></title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  <link href="css/stylesheet.css" rel="stylesheet" type="text/css"/>
  <script src="js/javascript.js" type="text/javascript"></script>

	<script src="js/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="js/jquery-altcolor.js" type="text/javascript"></script>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
				No Tasks Found
			</p>
		<% } else { %>
			<p align="center">
			<table id="dataTable_style2" border="0" cellspacing="0" class="alt-color">
			<thead>
				<tr class="ignore">
					<td colspan="1" class="recordCnt">
						<b><%= resultList.size() %> tasks found</b>
						<a href="project.do?action=taskAgendaListXls&projectPk=<bean:write name="projectPk"/>&agenda=<bean:write name="agenda"/>">
							<img src="images/file_icons/sml_file_xls.gif"/>
							Export to Excel
						</a>
					</td>
				</tr>
				<tr>
					<th>Title/Description</th>
					<th>Category</th>
					<th>Status</th>
					<th>Priority</th>
					<th>Ship Name</th>
					<th>Person Assigned</th>
					<th>Date Created</th>
					<th>Due Date</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ProjectBean">
				<tr style="vertical-align:top;">
					<td width="400">
						<b>Task #<bean:write name="resultBean" property="taskPk"/>: <bean:write name="resultBean" property="title"/></b><br/>
						<logic:iterate id="description" name="resultBean" property="descriptionBr">
							<bean:write name="description"/><br/>
						</logic:iterate>
					</td>
					<td align="center"><bean:write name="resultBean" property="category"/></td>
					<td align="center" style="<bean:write name="resultBean" property="statusCss"/>"><bean:write name="resultBean" property="status"/></td>
					<td align="center" style="<bean:write name="resultBean" property="priorityCss"/>"><bean:write name="resultBean" property="priority"/></td>
					<td><bean:write name="resultBean" property="shipName"/></td>
					<td><bean:write name="resultBean" property="personAssigned"/></td>
					<td align="center"><bean:write name="resultBean" property="createdDate"/></td>
					<td align="center" style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</p>
		<% } %>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>
</body>
</html>
