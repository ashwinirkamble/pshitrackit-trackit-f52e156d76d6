<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="pageTitle" value="Unexpected Error"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

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
<%@ include file="include/app-header.jsp" %>

<div id="content-main">
	<%@ include file="include/content-header.jsp" %>
  <p class="error" align="center">An unexpected error occurred while trying to load the request page.  This error has been logged and we will try to correct this as soon as possible.</p>
  <p align="center"><a href="menu.do">Return Home</a></p>
</div>

<%@ include file="include/app-footer.jsp" %>
</body>
</html>
