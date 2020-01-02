<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Change Password"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

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
<%@ include file="include/app-header.jsp" %>

<div class="colmask holygrail"><div class="colmid"><div class="colleft">
	<div class="col1wrap"><div class="col1">
		<!-- Main Column 1 start -->
		<%@ include file="include/content-header.jsp" %>

    <tags:adminBreadcrumb pageTitle="${pageTitle}" />

		<html:form action="login.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="action" value="changePasswordDo"/>
		<center>
		<div class="panel panel-primary" style="width:450px;">
			<div class="panel-heading">
				<h3 class="panel-title">Change Password</h3>
			</div>
			<div class="panel-body">
				<p>
				<table border="0" id="bootstrapFormTable">
				<colgroup>
					<col width="150"/>
					<col width="200"/>
				<tbody>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Old Password:</td>
						<td><html:password name="inputBean" property="oldPassword" styleClass="form-control input-sm"/></td>
					</tr>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> New Password:</td>
						<td><html:password name="inputBean" property="password" styleClass="form-control input-sm"/></td>
					</tr>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Re-Enter Password:</td>
						<td><html:password name="inputBean" property="passwordConfirm" styleClass="form-control input-sm"/></td>
					</tr>
				</tbody>
				</table>
				</p>

				<p align="center">
				<button type="submit" value="Submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Change Password</button>
				</p>
			</div>
		</div>
		</center>
		</html:form>

		<!-- Main Column 1 end -->
	</div></div>
	<%@ include file="include/app-col2.jsp" %>
	<%@ include file="include/app-col3.jsp" %>
</div></div></div>

<%@ include file="include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		document.loginForm.oldPassword.focus();
	});

	function valFields() {
		var password = stripSpaces(document.loginForm.password.value);
		var passwordConfirm = stripSpaces(document.loginForm.passwordConfirm.value);

		document.loginForm.password.value = password;
		document.loginForm.passwordConfirm.value = passwordConfirm;

		if (password.length < 1) {
			alert("You must enter in a password.");
			document.loginForm.password.focus();
			return false;
		} else if (password != passwordConfirm) {
			alert("Password do not match.");
			document.loginForm.password.focus();
			return false;d
		} else if (password.length >= 1 && password.length < 4) {
			alert("Password must be at least 4 characters long.");
			document.loginForm.password.focus();
			return false;
		} //end of if

		return true;
	} //end of valFields
</script>

</body>
</html>
