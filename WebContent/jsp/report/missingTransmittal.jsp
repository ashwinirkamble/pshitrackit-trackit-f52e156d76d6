<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested"  uri="http://struts.apache.org/tags-nested" %>

<bean:define id="defaultPageTitle" value="Missing Transmittal Report"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="projectPk"	scope="request" class="java.lang.String"/>

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

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<html:form action="report.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="action" value="exceptionListDo"/>
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>

		<p align="center">
		<center>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<tbody>
		<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ReportBean">
			<tr>
				<th><bean:write name="resultBean" property="shipName"/> - <bean:write name="resultBean" property="facetName"/></th>
				<th>Exception (Enter in reason to add missing transmittal to exclusion list from notifications)</th>
			</tr>
			<logic:iterate id="missingBean" name="resultBean" property="transmittalList" type="com.premiersolutionshi.old.bean.ReportBean">
			<input type="hidden" name="shipPkArr" value="<bean:write name="resultBean" property="shipPk"/>"/>
			<input type="hidden" name="transmittalNumArr" value="<bean:write name="missingBean" property="transmittalNum"/>"/>
			<tr>
				<td><bean:write name="missingBean" property="transmittalNum"/></td>
				<td><input type="text" name="exceptionReasonArr" size="100" value="<bean:write name="missingBean" property="exceptionReason"/>"/></td>
			</tr>
			</logic:iterate>
		</logic:iterate>
		</tbody>
		</table>
		</center>
		</p>

		<p align="center">
		<html:submit value="Save"/>
		</p>
		</html:form>

	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

</body>
</html>
