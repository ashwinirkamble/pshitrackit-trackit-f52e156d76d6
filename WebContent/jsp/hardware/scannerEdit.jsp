<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Scanner Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"    scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean"   scope="request" class="com.premiersolutionshi.old.bean.HardwareBean"/>
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

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="hardware.do?action=scannerList&projectPk=${projectPk}" parentTitle="Scanner List" />

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <p align="center">
    <center>
    <html:form action="hardware.do" onsubmit="return valFields();" method="POST">
    <input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
    <table id="tanTable_style2" border="0" cellspacing="0">
    <% if (editType.equals("add")) { %>
      <input type="hidden" name="action" value="scannerAddDo"/>
    <% } else { %>
      <input type="hidden" name="action" value="scannerEditDo"/>
      <html:hidden name="inputBean" property="scannerPk"/>
    <% } %>
    <tbody>
      <tr><th>Scanner</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3" width="550">
        <colgroup><col width="100"/><col width="125"/></colgroup>
          <tr>
            <td rowspan="5" class="TOP"><img src="images/hardware/scanner.png" width="100" height="100"/></td>
            <td class="fieldName" nowrap><span class="regAsterisk">*</span> Product Name:</td>
            <td>
              <html:select name="inputBean" property="currProductName" onchange="checkNew(this, 'productName');">
                <html:options name="productNameList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="productName" size="20" maxlength="50" style="display:none;"/>
            </td>
          </tr>

          <tr>
            <td class="fieldName TOP" nowrap>Status:</td>
            <td class="TOP">
              <html:select name="inputBean" property="status" styleId="status">
                <html:option value=""/>
                <html:option value="1 - In Transit to PSHI"/>
                <html:option value="2 - In Transit to manufacturer"/>
                <html:option value="3 - In Transit to vessel"/>
                <html:option value="4 - Under Repair at PSHI"/>
                <html:option value="5 - Defective (No repair)"/>
                <html:option value="5 - Testing & Prep"/>
                <html:option value="6 - Available & Ready"/>
                <html:option value="7 - Deployed / Onboard Vessel"/>
                <html:option value="8 - Not Returned to PSHI"/>
              </html:select>
            </td>
            <td class="statusNotesTd fieldName TOP" nowrap>Notes:</td>
            <td class="statusNotesTd TOP" colspan="3" rowspan="2"><html:textarea name="inputBean" property="statusNotes" styleId="statusNotes" rows="3" cols="40"/></td>
          </tr>

          <tr>
            <td class="fieldName" nowrap>Tag:</td>
            <td><html:text name="inputBean" property="tag" size="25" maxlength="50"/></td>
          </tr>

          <tr>
            <td class="fieldName" nowrap>Model Number:</td>
            <td>
              <html:select name="inputBean" property="currModelNumber" onchange="checkNew(this, 'modelNumber');">
                <html:options name="modelNumberList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="modelNumber" size="15" maxlength="50" style="display:none;"/>
            </td>
            <td class="fieldName" nowrap>Serial Number:</td>
            <td><html:text name="inputBean" property="serialNumber" size="12" maxlength="50"/></td>
            <td class="fieldName" nowrap>Origin:</td>
            <td>
              <html:select name="inputBean" property="currOrigin" onchange="checkNew(this, 'origin');">
                <html:options name="originList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="origin" size="15" maxlength="50" style="display:none;"/>
            </td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Date Received:</td>
            <td><html:text name="inputBean" property="receivedDate" styleClass="datepicker" size="9"/></td>
            <td class="fieldName" nowrap>Date Prepped:</td>
            <td><html:text name="inputBean" property="preppedDate" styleClass="datepicker" size="9"/></td>
          </tr>
        </table>
      </td></tr>


      <tr><th>Customer/Contract Info</th></tr>
      <tr><td class="nobordered" align="left">
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
      </td></tr>


      <tr><th>Notes</th></tr>
      <tr><td class="nobordered" align="left">
        <html:textarea name="inputBean" property="notes" style="width:99%;height:100px;"/>
      </td></tr>
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
          <a class="btn btn-default" href="hardware.do?action=scannerList&projectPk=<bean:write name="projectPk"/>">Cancel</a>
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

  $('#status').on('change', function() {
    if ($(this).val() == '1 - In Transit to PSHI') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('From Location: ');
    } else if ($(this).val() == '2 - In Transit to manufacturer') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('Location: \r\nContact: ');
    } else if ($(this).val() == '3 - In Transit to vessel') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('To Location: \r\nContact: ');
    } else if ($(this).val() == '4 - Under Repair at PSHI') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('Tech Assigned: ');
    } else if ($(this).val() == '5 - Defective (No repair)') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('PSHI Confirm By: \r\nManufacturer Confirm By: \r\nNotified Client: ');
    } else if ($(this).val() == '5 - Testing & Prep') {
      $('.statusNotesTd').hide();
      $('#statusNotes').val('');
    } else if ($(this).val() == '6 - Available & Ready') {
      $('.statusNotesTd').hide();
      $('#statusNotes').val('');
    } else if ($(this).val() == '7 - Deployed / Onboard Vessel') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('Name of Vessel: ');
    } else if ($(this).val() == '8 - Not Returned to PSHI') {
      $('.statusNotesTd').show();
      $('#statusNotes').val('Reason: ');
    } else {
      $('.statusNotesTd').hide();
    }
  });
});

$(document).ready(function () {
  if ($('#status').val() == '' || $('#status').val() == '5 - Testing & Prep' || $('#status').val() == '6 - Available & Ready') {
    $('.statusNotesTd').hide();
  } else {
    $('.statusNotesTd').show();
  }

  <% if (editType.equals("add")) { %>
    //Set default values for new record
    var d = new Date();
    document.hardwareForm.receivedDate.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();
  <% } %>
  checkNew(document.hardwareForm.currProductName, 'productName');
  checkNew(document.hardwareForm.currModelNumber, 'modelNumber');
  checkNew(document.hardwareForm.currOrigin, 'origin');
  checkNew(document.hardwareForm.currCustomer, 'customer');
  checkNew(document.hardwareForm.currContractNumbers, 'contractNumber');

  document.hardwareForm.currProductName.focus();
});

function valFields() {
  var productName = stripSpaces(document.hardwareForm.productName.value);
  var modelNumber = stripSpaces(document.hardwareForm.modelNumber.value);
  var serialNumber = stripSpaces(document.hardwareForm.serialNumber.value);
  var origin = stripSpaces(document.hardwareForm.origin.value);
  var receivedDate = stripSpaces(document.hardwareForm.receivedDate.value);
  var preppedDate = stripSpaces(document.hardwareForm.preppedDate.value);

  document.hardwareForm.productName.value = productName;
  document.hardwareForm.modelNumber.value = modelNumber;
  document.hardwareForm.serialNumber.value = serialNumber;
  document.hardwareForm.origin.value = origin;
  document.hardwareForm.receivedDate.value = receivedDate;
  document.hardwareForm.preppedDate.value = preppedDate;

  if (productName.length < 1) {
    alert("You must enter in a product name.");
    document.hardwareForm.productName.focus();
    return false;
  }

  if (!validateDate(receivedDate.length >= 1 && receivedDate, "Date Received")) {
    document.hardwareForm.receivedDate.focus();
    return false;
  }

  if (preppedDate && !validateDate(preppedDate.length >= 1 && preppedDate, "Date Prepped")) {
    document.hardwareForm.preppedDate.focus();
    return false;
  }

  return true;
}

function checkNew(currObj, elementName) {
  if (currObj.value == 'null') {
    document.getElementsByName(elementName)[0].value = '';
    document.getElementsByName(elementName)[0].style.display = 'inline';
    document.getElementsByName(elementName)[0].focus();
  } else {
    document.getElementsByName(elementName)[0].value = currObj.value;
    document.getElementsByName(elementName)[0].style.display = 'none';
  }
}
</script>
</body>
</html>
