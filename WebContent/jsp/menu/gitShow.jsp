<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Application Change Log"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="id"    scope="request" class="java.lang.String"/>
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
				Commit <<span style="color:green;"><bean:write name="id"/></span>> not found
			</p>
		<% } else { %>
			<p align="left">
			<logic:iterate id="str" name="resultList" type="java.lang.String" indexId="i">
				<% if (i == 0) { /* commit id line */ %>
					<span style="color:black;"><bean:write name="str"/></span><br/>

				<% } else if (i == 1) { /* Author line */ %>
					<span style="color:black;"><bean:write name="str"/></span><br/>

				<% } else if (i == 2) { /* Date line */ %>
					<span style="color:black;"><bean:write name="str"/></span><br/>

				<% } else if (i == 4) { /* Msg line */ %>
					<span style="color:black;font-weight:bold;"><bean:write name="str"/></span><br/>

				<% } else if (str.startsWith("+++")) { %>
					<span style="color:darkgreen;"><bean:write name="str"/></span><br/>

				<% } else if (str.startsWith("---")) { %>
					<span style="color:darkred;"><bean:write name="str"/></span><br/>

				<% } else if (str.startsWith("+")) { %>
					<span style="color:green;"><bean:write name="str"/></span><br/>

				<% } else if (str.startsWith("-")) { %>
					<span style="color:red;"><bean:write name="str"/></span><br/>

				<% } else if (str.startsWith("@@")) { %>
					<span style="color:darkcyan;"><bean:write name="str"/></span><br/>

				<% } else { %>
					<span style="color:gray;"><bean:write name="str"/></span><br/>
				<% } %>


			</logic:iterate>
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
