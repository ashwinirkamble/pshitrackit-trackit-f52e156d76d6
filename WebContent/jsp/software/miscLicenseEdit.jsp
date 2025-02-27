<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Misc License Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="inputBean"   scope="request" class="com.premiersolutionshi.old.bean.SoftwareBean"/>
<jsp:useBean id="editType"    scope="request" class="java.lang.String"/>

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
<%--ashwini /software.do to ./software.do --%>
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="./software.do?action=miscLicenseList&projectPk=${projectPk}" parentTitle="Misc License List" />

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<center>
		<html:form action="software.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<% if (editType.equals("add")) { %>
			<input type="hidden" name="action" value="miscLicenseAddDo"/>
		<% } else { %>
			<input type="hidden" name="action" value="miscLicenseEditDo"/>
			<html:hidden name="inputBean" property="miscLicensePk"/>
		<% } %>
		<tbody>
			<tr><th>Misc Software</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup><col width="125"/></colgroup>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Product Name:</td>
						<td>
							<html:select name="inputBean" property="currProductName" onchange="checkNew(this, 'productName');">
								<html:options name="productNameList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="productName" size="25" maxlength="50" style="display:none;"/>
						</td>
					</tr>

					<tr>
						<td class="fieldName">Product Key:</td>
						<td><html:text name="inputBean" property="productKey" size="34" maxlength="50"/></td>
						<td class="fieldName">Seats Activated:</td>
						<td>
							<a id="decrementCnt" href="javascript:void(0);"><img src="images/icon_minus.gif"/></a>
							<html:text styleId="installedCnt" name="inputBean" property="installedCnt" style="text-align:center;" size="2" maxlength="4"/>
							<a id="incrementCnt" href="javascript:void(0);"><img src="images/icon_plus.gif"/></a>
						</td>
					</tr>

					<tr>
						<td class="fieldName TOP" nowrap>Status:</td>
						<td class="TOP"><html:text name="inputBean" property="status" size="20" maxlength="50"/></td>
						<td class="statusNotesTd fieldName TOP" nowrap>Status Notes:</td>
						<td class="statusNotesTd TOP"><html:textarea name="inputBean" property="statusNotes" styleId="statusNotes" rows="3" cols="40"/></td>
					</tr>

					<tr>
						<td class="fieldName">Date Received:</td>
						<td><html:text name="inputBean" property="receivedDate" styleClass="datepicker" size="9"/></td>
					</tr>

					<tr>
						<td class="fieldName">License Expires:</td>
						<td><html:text name="inputBean" property="licenseExpiryDate" styleClass="datepicker" size="9"/></td>
					</tr>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Customer/Contract Info</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3" width="550">
				<colgroup><col width="100"/><col width="125"/></colgroup>
				<tbody>
					<tr>
						<td class="fieldName" nowrap>Customer:</td>
						<td>
							<html:select name="inputBean" property="currCustomer" onchange="checkNew(this, 'customer');">
								<html:option value="">&nbsp;</html:option>
								<html:options name="customerList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="customer" size="15" maxlength="50" style="display:none;"/>
						</td>
					</tr>
					<tr>
						<td class="fieldName" nowrap>Contract Number:</td>
						<td>
							<html:select name="inputBean" property="currContractNumber" onchange="checkNew(this, 'contractNumber');">
								<html:option value="">&nbsp;</html:option>
								<html:options name="contractNumberList"/>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text name="inputBean" property="contractNumber" size="15" maxlength="50" style="display:none;"/>
						</td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Notes</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<html:textarea name="inputBean" property="notes" style="width:100%;height:100px;"/>
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
					<a class="btn btn-default" href="software.do?action=miscLicenseList&projectPk=<bean:write name="projectPk"/>">Cancel</a>
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
		$('.datepicker').attr('autocomplete', 'off');
		$('.datepicker').datepicker();
	});

	$('#decrementCnt').on('click', function() {
		if (($('#installedCnt').val() && !$.isNumeric($('#installedCnt').val())) || parseInt($('#installedCnt').val()) <= 0) {
			$('#installedCnt').val(0);
		} else {
			$('#installedCnt').val(parseInt($('#installedCnt').val())-1);
		} //end of else
	});

	$('#incrementCnt').on('click', function() {
		if (($('#installedCnt').val() && !$.isNumeric($('#installedCnt').val()))) {
			$('#installedCnt').val(1);
		} else if (parseInt($('#installedCnt').val()) >= 9999) {
			$('#installedCnt').val(9999);
		} else {
			$('#installedCnt').val(parseInt($('#installedCnt').val())+1);
		} //end of else
	});

	$(document).ready(function () {
		checkNew(document.softwareForm.currProductName, 'productName');
		checkNew(document.softwareForm.currCustomer, 'customer');
		checkNew(document.softwareForm.currContractNumber, 'contractNumber');

		document.softwareForm.currProductName.focus();
	});

	function valFields() {
		var productName = stripSpaces(document.softwareForm.productName.value);
		var productKey = stripSpaces(document.softwareForm.productKey.value);
		var installedCnt = parseInt(stripSpaces(document.softwareForm.installedCnt.value));
		var receivedDate = stripSpaces(document.softwareForm.receivedDate.value);
		var licenseExpiryDate = stripSpaces(document.softwareForm.licenseExpiryDate.value);

		document.softwareForm.productName.value = productName;
		document.softwareForm.productKey.value = productKey;
		document.softwareForm.installedCnt.value = installedCnt;
		document.softwareForm.receivedDate.value = receivedDate;
		document.softwareForm.licenseExpiryDate.value = licenseExpiryDate;

		if (productName.length < 1) {
			alert("You must enter in a product name.");
			document.softwareForm.productName.focus();
			return false;
		} //end of if

		if (isNaN(installedCnt) || installedCnt < 0) {
			alert("You must enter in a valid number for number of seats activated.");
			document.softwareForm.installedCnt.focus();
			return false;
		} //end of if

		if (receivedDate.length >= 1 && !validateDate(receivedDate, "Date Received")) {
			document.softwareForm.receivedDate.focus();
			return false;
		} //end of if

		if (licenseExpiryDate.length >= 1 && !validateDate(licenseExpiryDate, "License Expires")) {
			document.softwareForm.licenseExpiryDate.focus();
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
