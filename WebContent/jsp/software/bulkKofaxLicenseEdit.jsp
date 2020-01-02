<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Bulk Kofax License Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SoftwareBean"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<center>
		<html:form action="software.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="action" value="bulkKofaxLicenseEditDo"/>
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<table id="borderlessTable" border="0" cellspacing="0">
		<tbody>
			<tr>
				<td class="fieldName"><span class="regAsterisk">*</span> License Expires:</td>
				<td><html:text name="inputBean" property="licenseExpiryDate" styleClass="datepicker form-control input-sm"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<select name="kofaxLicensePkArr" multiple="multiple" size="25" required="true" class="form-control input-sm">
						<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SoftwareBean">
							<option value="<bean:write name="resultBean" property="kofaxLicensePk"/>">
								<bean:write name="resultBean" property="licenseKey"/>
								<bean:write name="resultBean" property="productCode"/>
								<logic:notEmpty name="resultBean" property="licenseExpiryDate">
									(Expires on <bean:write name="resultBean" property="licenseExpiryDate"/>)
								</logic:notEmpty>
							</option>
						</logic:iterate>
					</select>
				</td>
			</tr>
		</tbody>
		</table>
		</center>
		</p>

		<p>
		<center>
		<table id="borderlessTable" border="0" cellspacing="0"><tbody>
			<tr>
				<td align="center"><html:submit value="Save" styleClass="btn btn-primary"/></td>
			</tr>
		</tbody></table>
		</html:form>
		</center>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
	$(function() {
		$(".datepicker").attr('autocomplete', 'off');
		$(".datepicker").datepicker();
	});

	function valFields() {
		var licenseExpiryDate = stripSpaces(document.softwareForm.licenseExpiryDate.value);

		document.softwareForm.licenseExpiryDate.value = licenseExpiryDate;

		if (licenseExpiryDate.length < 1) {
			alert("You must enter in a license expiration date.");
			document.softwareForm.licenseExpiryDate.focus();
			return false;
		} else if (!validateDate(licenseExpiryDate, "License Expiration Date")) {
			document.softwareForm.licenseExpiryDate.focus();
			return false;
		} //end of if

		return true;
	} //end of valFields
</script>
</body>
</html>
