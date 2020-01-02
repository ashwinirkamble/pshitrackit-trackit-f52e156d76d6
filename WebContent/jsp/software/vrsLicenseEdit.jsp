<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="VRS License Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
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
<%--ashwini /software.do ./software.do --%>
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="./software.do?action=vrsLicenseList&projectPk=${projectPk}" parentTitle="VRS License List" />

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<center>
		<html:form action="software.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<% if (editType.equals("add")) { %>
			<input type="hidden" name="action" value="vrsLicenseAddDo"/>
		<% } else { %>
			<input type="hidden" name="action" value="vrsLicenseEditDo"/>
			<html:hidden name="inputBean" property="vrsLicensePk"/>
		<% } %>
		<tbody>
			<tr><th>VRS License</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup>
					<col width="100"/>
					<col width="125"/>
					<col width="125"/>
					<col width="125"/>
					<col width="125"/>
				</colgroup>
					<tr>
						<td rowspan="2" class="TOP" align="center"><img src="images/logo_vrs.png"/></td>
						<td class="fieldName"><span class="regAsterisk">*</span> License Key:</td>
						<td><html:text name="inputBean" property="licenseKey" maxlength="7" styleClass="form-control input-sm"/></td>
						<td class="fieldName"><span class="regAsterisk">*</span> Product Code:</td>
						<td><html:text name="inputBean" property="productCode" maxlength="9" styleClass="form-control input-sm"/></td>
					</tr>
					<tr>
						<td class="fieldName">Date Received:</td>
						<td><html:text name="inputBean" property="receivedDate" styleClass="datepicker form-control input-sm"/></td>
					</tr>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Customer/Contract Info</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup>
					<col width="250"/>
					<col width="250"/>
				</colgroup>
				<tbody>
					<tr>
						<td class="fieldName" nowrap>Customer:</td>
						<td>
							<html:select name="inputBean" property="currCustomer" onchange="checkNew(this, 'customer');" styleClass="form-control input-sm">
								<html:option value="">&nbsp;</html:option>
								<html:options name="customerList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="customer" maxlength="50" style="display:none;" styleClass="form-control input-sm"/>
						</td>
					</tr>
					<tr>
						<td class="fieldName" nowrap>Contract Number:</td>
						<td>
							<html:select name="inputBean" property="currContractNumber" onchange="checkNew(this, 'contractNumber');" styleClass="form-control input-sm">
								<html:option value="">&nbsp;</html:option>
								<html:options name="contractNumberList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="contractNumber" maxlength="50" style="display:none;" styleClass="form-control input-sm"/>
						</td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Notes</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<html:textarea name="inputBean" property="notes" rows="5" styleClass="form-control input-sm"/>
			</td></tr> <!-- end tan_table -->
		</tbody>
		</table>


		<table id="borderlessTable" border="0" cellspacing="0"><tbody>
			<tr>
				<td align="center">
					<% if (editType.equals("add")) { %>
						<html:submit value="Insert" styleClass="btn btn-success"/>
					<% } else { %>
						<html:submit value="Save" styleClass="btn btn-primary"/>
					<% } %>
				</td>
				<td align="center">
					<a class="btn btn-default" href="software.do?action=vrsLicenseList&projectPk=<bean:write name="projectPk"/>">Cancel</a>
				</td>
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

	$(document).ready(function () {
		<% if (editType.equals("add")) { %>
			var d = new Date();
			document.softwareForm.receivedDate.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();
		<% } %>

		checkNew(document.softwareForm.currCustomer, 'customer');
		checkNew(document.softwareForm.currContractNumber, 'contractNumber');

		document.softwareForm.licenseKey.focus();
	});

	function valFields() {
		var licenseKey = stripSpaces(document.softwareForm.licenseKey.value);
		var productCode = stripSpaces(document.softwareForm.productCode.value);
		var receivedDate = stripSpaces(document.softwareForm.receivedDate.value);

		document.softwareForm.licenseKey.value = licenseKey;
		document.softwareForm.productCode.value = productCode;
		document.softwareForm.receivedDate.value = receivedDate;

		if (licenseKey.length < 1) {
			alert("You must enter in a license key.");
			document.softwareForm.licenseKey.focus();
			return false;
		} //end of if

		if (productCode.length < 1) {
			alert("You must enter in a product code.");
			document.softwareForm.productCode.focus();
			return false;
		} //end of if

		if (receivedDate.length >= 1 && !validateDate(receivedDate, "Date Received")) {
			document.softwareForm.receivedDate.focus();
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
