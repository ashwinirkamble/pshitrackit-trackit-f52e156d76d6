<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Manage Projects"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="inputBean"  scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>

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
		<center>
		<table id="lineTable" border="0" cellspacing="0" width="300">
		<logic:iterate id="projectBean" name="resultList" type="com.premiersolutionshi.old.bean.ProjectBean">
			<tr>
				<td class="header" align="left"><bean:write name="projectBean" property="customer"/></td>
				<td class="header" align="right">Current<br/>Tasks</td>
				<td class="header" align="right">Completed</td>
				<td class="header" align="right">Delete</td>
			</tr>
			<logic:iterate id="resultBean" name="projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
				<tr>
					<td align="left"><bean:write name="resultBean" property="projectName"/></td>
					<td align="right"><a href="project.do?action=taskList&searchPerformed=Y&projectPk=<bean:write name="resultBean" property="projectPk"/>"><bean:write name="resultBean" property="currentTaskCnt"/></a></td>
					<td align="right"><a href="project.do?action=taskList&searchPerformed=Y&searchStatusArr=Completed&projectPk=<bean:write name="resultBean" property="projectPk"/>"><bean:write name="resultBean" property="completedTaskCnt"/></a></td>
					<td align="right"><a href="project.do?action=projectDeleteDo&projectPk=<bean:write name="resultBean" property="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="projectNameJs"/>');">Delete</a></td>
				</tr>
			</logic:iterate>
			<tr><td colspan="2" class="ignore">&nbsp;</td></tr>
		</logic:iterate>
		</tbody>
		</table>
		</center>
		</p>

		<p align="center">
		<center>
		<html:form action="project.do" onsubmit="return valFields();" method="POST">
		<table id="tanTable_style2" border="0" cellspacing="0">
		<input type="hidden" name="action" value="projectAddDo"/>
		<tbody>
			<tr><th>Add New Project</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero">
					<tr>
						<td class="fieldName">Customer</td>
						<td><html:text name="inputBean" property="customer"/></td>
						<td class="fieldName">Project Name</td>
						<td><html:text name="inputBean" property="projectName"/></td>
						<td><html:submit value="Add"/></td>
					</tr>
				</table>
			</td></tr> <!-- end tan_table -->
		</tbody>
		</table>
		</html:form>
		</center>
		</p>
		<!-- Main Column 1 end -->
	</div></div>
	<%@ include file="../include/app-col2.jsp" %>
	<%@ include file="../include/app-col3.jsp" %>
</div></div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript">
	function valFields() {
		var projectName = stripSpaces(document.projectForm.projectName.value);
		var customer = stripSpaces(document.projectForm.customer.value);
		document.projectForm.projectName.value = projectName;
		document.projectForm.customer.value = customer;

		if (projectName.length < 1) {
			alert("You must enter in a project name.");
			document.projectForm.projectName.focus();
			return false;
		} //end of if

		if (customer.length < 1) {
			alert("You must enter in a customer.");
			document.projectForm.customer.focus();
			return false;
		} //end of if

		return true;
	} //end of valFields

	function confirmDelete(projectName) {
		return confirm("Are you sure you want to delete " + projectName);
	} //end of confirmDelete
</script>

</body>
</html>
