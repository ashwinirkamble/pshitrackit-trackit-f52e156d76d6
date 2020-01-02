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

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="/user.do?action=pocList&projectPk=${projectPk}" parentTitle="POC List"/>

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<center>
		<html:form action="user.do" onsubmit="return valFields();" method="POST">
		<html:hidden name="inputBean" property="projectPk"/>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<% if (editType.equals("add")) { %>
			<input type="hidden" name="action" value="pocAddDo"/>
		<% } else { %>
			<input type="hidden" name="action" value="pocEditDo"/>
			<html:hidden name="inputBean" property="pocPk"/>
		<% } %>
		<tbody>
			<tr><th>POC Information</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup><col width="120"/></colgroup>
				<tbody>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Last Name:</td>
						<td><html:text name="inputBean" property="lastName" size="20" maxlength="50"/></td>
						<td class="fieldName"><span class="regAsterisk">*</span> First Name:</td>
						<td><html:text name="inputBean" property="firstName" size="20" maxlength="50"/></td>
						<td class="fieldName">Rank:</td>
						<td><html:text name="inputBean" property="rank" size="5" maxlength="10"/></td>
					</tr>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span>Company/Org:</td>
						<td colspan="5">
							<html:select name="inputBean" property="currOrganization" onchange="checkNew(this, 'organization');">
								<html:option value=""/>
								<html:options name="organizationList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="organization" style="display:none;"/>
						</td>
					</tr>
					<tr>
						<td class="fieldName">Job Title:</td>
						<td colspan="5"><html:text name="inputBean" property="title" size="45" maxlength="50"/></td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->

			<tr><th>Contact Information</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup><col width="120"/></colgroup>
				<tbody>
					<tr>
						<td class="fieldName">E-Mail:</td>
						<td colspan="5"><html:text name="inputBean" property="email" size="38" maxlength="255"/></td>
					</tr>
					<tr>
						<td class="fieldName">Work Phone:</td>
						<td><html:text name="inputBean" property="workNumber" size="15" maxlength="25"/></td>
						<td class="fieldName">Fax Number:</td>
						<td><html:text name="inputBean" property="faxNumber" size="15" maxlength="25"/></td>
						<td class="fieldName">Cell Number:</td>
						<td><html:text name="inputBean" property="cellNumber" size="15" maxlength="25"/></td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->
		</tbody>
		</table>

		<table id="borderlessTable" border="0" cellspacing="0"><tbody>
			<tr>
				<td align="center">
					<% if (editType.equals("add")) { %>
						<html:submit value="Insert"/>
					<% } else { %>
						<html:submit value="Save"/>
					<% } %>
				</td>
				<td align="center"><input type="button" onclick="window.location='user.do?action=pocList&projectPk=<bean:write name="inputBean" property="projectPk"/>';" value="Cancel"/></td>
			</tr>
		</tbody></table>
		</html:form>
		</center>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		document.userForm.lastName.focus();
	});

	function valFields() {
		var lastName = stripSpaces(document.userForm.lastName.value);
		var firstName = stripSpaces(document.userForm.firstName.value);
		var organization = stripSpaces(document.userForm.organization.value);

		document.userForm.lastName.value = lastName;
		document.userForm.firstName.value = firstName;
		document.userForm.organization.value = organization;

		if (lastName.length < 1) {
			alert("You must enter in a last name.");
			document.userForm.lastName.focus();
			return false;
		} //end of if

		if (firstName.length < 1) {
			alert("You must enter in a first name.");
			document.userForm.firstName.focus();
			return false;
		} //end of if

		if (organization.length < 1) {
			alert("You must enter in an organization.");
			document.userForm.currOrganization.focus();
			return false;
		} //end of if

		return true;
	} //end of valFields

	function checkNew(currObj, elementName) {
		if (currObj.value == 'null') {
			document.getElementsByName(elementName)[0].value = '';
			document.getElementsByName(elementName)[0].style.display = 'inline';
			document.getElementsByName(elementName)[0].focus();
		} else {
			document.getElementsByName(elementName)[0].value = currObj.value;
			document.getElementsByName(elementName)[0].style.display = 'none';
		} //end of if
	} //end of checkNew
</script>

</body>
</html>
