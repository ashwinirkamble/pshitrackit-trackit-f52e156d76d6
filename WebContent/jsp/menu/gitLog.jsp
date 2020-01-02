<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Application Change Log"/>
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
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css">
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask holygrail"><div class="colmid"><div class="colleft">
	<div class="col1wrap"><div class="col1">
		<!-- Main Column 1 start -->
		<%@ include file="../include/content-header.jsp" %>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
				No Commits Found
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="dataTable_style2" border="0" cellspacing="0" class="alt-color">
			<thead>
				<tr class="ignore">
					<td colspan="2" class="recordCnt">
					<b><%= resultList.size() %> commits found</b>
					</td>
				</tr>
				<tr>
					<th>ID</th>
					<th align="center">Date</th>
					<th>Msg</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.GitLogBean">
				<tr>
					<td><a href="menu.do?action=gitShow&id=<bean:write name="resultBean" property="id"/>"><bean:write name="resultBean" property="id"/></a></td>
					<td align="center"><bean:write name="resultBean" property="date"/></td>
					<td><bean:write name="resultBean" property="msg"/></td>
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

<%@ include file="../include/app-footer.jsp" %>

<script src="js/jquery-altcolor.js" type="text/javascript"></script>
</body>
</html>
