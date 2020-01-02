<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Misc Hardware Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"	scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.HardwareBean"/>
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

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="hardware.do?action=miscList&projectPk=${projectPk}" parentTitle="Misc Hardware List" />

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<center>
		<html:form action="hardware.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<% if (editType.equals("add")) { %>
			<input type="hidden" name="action" value="miscAddDo"/>
		<% } else { %>
			<input type="hidden" name="action" value="miscEditDo"/>
		<% } %>
		<html:hidden name="inputBean" property="miscHardwarePk"/>
		<tbody>
			<tr><th>Misc Hardware</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3" width="750">
				<colgroup><col width="110"/><col width="100"/><col width="175"/><col width="100"/><col width="265"/></colgroup>
					<tr>
						<td rowspan="7" class="TOP"><img id="hardwareImg"/></td>
						<td class="fieldName" nowrap><span class="regAsterisk">*</span> Product Type:</td>
						<td colspan="3">
							<html:select styleId="currProductType" name="inputBean" property="currProductType">
								<logic:present name="productTypeList"><html:options name="productTypeList"/></logic:present>
								<html:option value="null">Add new...</html:option>
							</html:select>
							<html:text styleId="productType" name="inputBean" property="productType" size="20" maxlength="50" style="display:none;"/>
						</td>
					</tr>

					<tr>
						<td class="fieldName" nowrap><span class="regAsterisk">*</span> Product Name:</td>
						<td><html:text name="inputBean" property="productName" size="30" maxlength="50"/></td>
						<td class="fieldName" nowrap>Tag:</td>
						<td><html:text name="inputBean" property="tag" size="15" maxlength="50"/></td>
					</tr>

					<tr>
						<td class="fieldName" nowrap>Model Number:</td>
						<td><html:text name="inputBean" property="modelNumber" size="15" maxlength="50"/></td>
						<td class="fieldName" nowrap>Serial Number:</td>
						<td><html:text name="inputBean" property="serialNumber" size="12" maxlength="50"/></td>
					</tr>

					<tr>
						<td class="fieldName TOP" nowrap>Status:</td>
						<td class="TOP"><html:text name="inputBean" property="status" size="20" maxlength="50"/></td>
						<td class="statusNotesTd fieldName TOP" nowrap>Status Notes:</td>
						<td class="statusNotesTd TOP"><html:textarea name="inputBean" property="statusNotes" styleId="statusNotes" rows="3" cols="40"/></td>
					</tr>

					<tr>
						<td class="fieldName" nowrap>Origin:</td>
						<td><html:text name="inputBean" property="origin" size="25" maxlength="50"/></td>
					</tr>

					<tr>
						<td class="fieldName" nowrap>Date Received:</td>
						<td><html:text name="inputBean" property="receivedDate" styleClass="datepicker" size="9"/></td>
					</tr>

					<tr>
						<td class="fieldName" nowrap>Warranty Expires:</td>
						<td><html:text name="inputBean" property="warrantyExpiryDate" styleClass="datepicker" size="9"/></td>
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
				<html:textarea name="inputBean" property="notes" style="width:99%;height:100px;"/>
			</td></tr> <!-- end tan_table -->
		</tbody>
		</table>
		</p>

		<p align="center">
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
					<a class="btn btn-default" href="hardware.do?action=miscList&projectPk=<bean:write name="projectPk"/>">Cancel</a>
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

	$('#productType').on('change', function() {
		var arr = ['laptop', 'scanner', 'printer', 'monitor', 'keyboard', 'mouse', 'cac_reader', 'router', 'external_hard_drive'];
		var idx = jQuery.inArray($(this).val().toLowerCase().replace(/ /g, '_'), arr);
		if (idx > -1) {
			$('#hardwareImg').attr('src', 'images/hardware/' + arr[idx] + '.png');
		} else {
			$('#hardwareImg').attr('src', 'images/hardware/generic.png');
		} //end of else
	});

	$('#currProductType').on('change', function() {
		 checkNew(document.hardwareForm.currProductType, 'productType');
		 $('#productType').change();
	});

	$(document).ready(function () {
		checkNew(document.hardwareForm.currProductType, 'productType');
		checkNew(document.hardwareForm.currCustomer, 'customer');
		checkNew(document.hardwareForm.currContractNumber, 'contractNumber');

		$('#productType').change();

		document.hardwareForm.currProductType.focus();
	});

	function valFields() {
		var productType = stripSpaces(document.hardwareForm.productType.value);
		var productName = stripSpaces(document.hardwareForm.productName.value);
		var modelNumber = stripSpaces(document.hardwareForm.modelNumber.value);
		var serialNumber = stripSpaces(document.hardwareForm.serialNumber.value);
		var origin = stripSpaces(document.hardwareForm.origin.value);
		var receivedDate = stripSpaces(document.hardwareForm.receivedDate.value);
		var warrantyExpiryDate = stripSpaces(document.hardwareForm.warrantyExpiryDate.value);

		document.hardwareForm.productName.value = productName;
		document.hardwareForm.modelNumber.value = modelNumber;
		document.hardwareForm.serialNumber.value = serialNumber;
		document.hardwareForm.origin.value = origin;
		document.hardwareForm.receivedDate.value = receivedDate;
		document.hardwareForm.warrantyExpiryDate.value = warrantyExpiryDate;

		if (productType.length < 1) {
			alert("You must enter in a product type.");
			document.hardwareForm.currProductType.focus();
			return false;
		} //end of if

		if (productName.length < 1) {
			alert("You must enter in a product name.");
			document.hardwareForm.productName.focus();
			return false;
		} //end of if

		if (receivedDate.length >= 1 && !validateDate(receivedDate, "Date Received")) {
			document.hardwareForm.receivedDate.focus();
			return false;
		} //end of if

		if (warrantyExpiryDate.length >= 1 && !validateDate(warrantyExpiryDate, "Warranty Expires")) {
			document.hardwareForm.warrantyExpiryDate.focus();
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
