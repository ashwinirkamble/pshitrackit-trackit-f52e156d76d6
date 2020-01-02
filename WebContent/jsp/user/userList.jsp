<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Manage Users"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="sortBy"     scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir"    scope="request" class="java.lang.String"/>


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

		<a href="user.do?action=userAdd"><img src="images/icon_plus.gif"/> Add New User</a>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
				No Users Found
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="dataTable_style2" border="0" cellspacing="0" class="alt-color">
			<thead>
				<tr class="ignore">
					<td colspan="2" class="recordCnt">
					<b><%= resultList.size() %> users found</b>
					</td>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('username', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Username
						<% if (sortBy.equals("username") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("username") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('last_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Last Name
						<% if (sortBy.equals("last_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("last_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('first_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">First Name
						<% if (sortBy.equals("first_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("first_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('organization', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Organization
						<% if (sortBy.equals("organization") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("organization") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('title', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Title
						<% if (sortBy.equals("title") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("title") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<!--th nowrap>
						<a href="javascript: changeSort('email', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Email
						<% if (sortBy.equals("email") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("email") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th-->
					<th nowrap>
						<a href="javascript: changeSort('work_number', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Work Number
						<% if (sortBy.equals("work_number") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("work_number") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('quick_dial', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Quick<br/>Dial
						<% if (sortBy.equals("quick_dial") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("quick_dial") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<!--th nowrap>
						<a href="javascript: changeSort('fax_number', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Fax Number
						<% if (sortBy.equals("fax_number") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("fax_number") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('cell_number', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Cell Number
						<% if (sortBy.equals("cell_number") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("cell_number") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th-->
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.UserBean">
				<tr>
					<td><bean:write name="resultBean" property="username"/></td>
					<td><bean:write name="resultBean" property="lastName"/></td>
					<td><bean:write name="resultBean" property="firstName"/></td>
					<td><bean:write name="resultBean" property="organization"/></td>
					<td><bean:write name="resultBean" property="title"/></td>
					<td><bean:write name="resultBean" property="workNumber"/></td>
					<td align="center"><bean:write name="resultBean" property="quickDial"/></td>
					<!--td><bean:write name="resultBean" property="email"/></td>
					<td><bean:write name="resultBean" property="faxNumber"/></td>
					<td><bean:write name="resultBean" property="cellNumber"/></td-->
					<td align="center" nowrap>
						<a href="user.do?action=userEdit&userPk=<bean:write name="resultBean" property="userPk"/>"><img src="images/icon_edit.png" title="Edit"/></a>
						&nbsp;<a href="user.do?action=userDeleteDo&userPk=<bean:write name="resultBean" property="userPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="usernameJs"/>');"><img src="images/icon_delete.png" title="Delete"/></a>
					</td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
		<!-- Main Column 1 end -->
	</div></div>
	<%@ include file="../include/app-col2.jsp" %>
	<%@ include file="../include/app-col3.jsp" %>
</div></div></div>

<form name="sortForm" action="user.do" method="GET">
	<input type="hidden" name="action" value="userList"/>
	<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
	<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
	<input type="hidden" name="searchPerformed" value="Y"/>
</form>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	function confirmDelete(username) {
		return confirm("Are you sure you want to delete " + username);
	} //end of confirmDelete
</script>

</body>
</html>
