<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Backfile Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"		scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean"   scope="request" class="com.premiersolutionshi.old.bean.BackfileBean"/>
<jsp:useBean id="projectList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="editType"    scope="request" class="java.lang.String"/>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>

	<style>
	 .pshiBoxCnt {
		 text-align: center;
		 	}
	 .boxCnt {
		 text-align: center;
		 	}
	 	.datepicker {
			font-size: inherit;
			text-align: center;
			}
		.workflowTd {
			font-size: 80%;
			text-align: center;
			vertical-align: bottom;
			}

		.input_hidden {
			position: absolute;
			left: -9999px;
			}

		.selected {
			border: 3px double #555;
			}

		.colorRadioBar {
			padding-top: 5px;
			padding-bottom: 3px;
			}

		.colorRadioBar label {
			display: inline-block;
			cursor: pointer;
			}

		.colorRadioBar label:hover {
			background-color: #efefef;
			}

		.colorRadioBar label img {
			padding: 3px;
			}
	 </style>
</head>
<body>
<%@ include file="../include/app-header.jsp" %>

<%@ include file="../include/content-header.jsp" %>
<%--ashwini /backfile.do to ./backfile.do--%>
<tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
  parentUrl="./backfile.do?action=workflowSummary&projectPk=${projectPk}" parentTitle="Backfile Workflow List" />

<p align="center">
Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
</p>

<html:form action="backfile.do" onsubmit="return valFields();" method="POST">
<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
<p align="center">
<div class="center">
<html:hidden name="inputBean" property="shipName" styleId="shipName"/>
<% if (editType.equals("add")) { %>
	<input type="hidden" name="action" value="workflowAddDo"/>
<% } else { %>
	<input type="hidden" name="action" value="workflowEditDo"/>
	<html:hidden name="inputBean" property="backfileWorkflowPk"/>
<% } %>
<table id="tanTable_style2 pshi-borderless-table">
<tbody>
	<tr><th colspan="17">Ship</th></tr>
	<tr><td colspan="17" class="nobordered" align="center"> <!-- tan_table -->
		<table class="pshi-borderless-table" cellpadding="3">
		<tbody>
			<tr>
				<td class="fieldName"><% if (editType.equals("add")) { %><span class="regAsterisk">*</span><% } %> Unit:</td>
				<td>
					<% if (editType.equals("add")) { %>
						<html:select name="inputBean" property="shipPk" styleId="shipPk">
							<html:option value=""/>
							<html:options collection="shipList" property="shipPk" labelProperty="shipName"/>
						</html:select>
					<% } else { %>
						<b><bean:write name="inputBean" property="shipName"/></b>
					<% } %>
				</td>
			</tr>
		</tbody>
		</table>
	</td></tr> <!-- end tan_table -->


	<tr><th colspan="17">Backfile Required</th></tr>
	<tr><td colspan="17" class="nobordered" align="center"> <!-- tan_table -->
		<div class="checkbox">
			<label><html:checkbox name="inputBean" styleId="isRequired" property="isRequired" value="Y"/>Backfile Required?</label>
		</div>
	</td></tr> <!-- end tan_table -->

	<tr class="requiredDiv"><th colspan="17">PSHI Boxes Received</th></tr>
	<tr class="requiredDiv"><td colspan="17" class="nobordered" align="center"> <!-- tan_table -->
		<div class="center">
		<table class="border-zero cellspacing-zero cellpadding-3">
		<tbody>
			<tr>
				<td align="center"><html:text name="inputBean" property="fy16PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy15PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy14PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy13PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy12PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy11PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy10PshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="otherPshiBoxCnt" size="2" maxlength="3" styleClass="pshiBoxCnt"/></td>
			</tr>
			<tr>
				<td class="fieldName" style="text-align:center;">FY16</td>
				<td class="fieldName" style="text-align:center;">FY15</td>
				<td class="fieldName" style="text-align:center;">FY14</td>
				<td class="fieldName" style="text-align:center;">FY13</td>
				<td class="fieldName" style="text-align:center;">FY12</td>
				<td class="fieldName" style="text-align:center;">FY11</td>
				<td class="fieldName" style="text-align:center;">FY10</td>
				<td class="fieldName" style="text-align:center;">Other / Reports</td>
			</tr>
			<tr>
				<td class="pshiBoxTotal" colspan="8"></td>
			</tr>
		</tbody>
		</table>
		</div>
	</td></tr> <!-- end tan_table -->

	<tr class="requiredDiv"><th colspan="17">Boxes Sent Scanning</th></tr>
	<tr class="requiredDiv"><td colspan="17" class="nobordered" align="center"> <!-- tan_table -->
		<div class="center">
		<table class="border-zero cellspacing-zero cellpadding-3">
		<tbody>
			<tr>
				<td align="center"><html:text name="inputBean" property="fy16BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy15BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy14BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy13BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy12BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy11BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="fy10BoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
				<td align="center"><html:text name="inputBean" property="otherBoxCnt" size="2" maxlength="3" styleClass="boxCnt"/></td>
			</tr>
			<tr>
				<td class="fieldName" style="text-align:center;">FY16</td>
				<td class="fieldName" style="text-align:center;">FY15</td>
				<td class="fieldName" style="text-align:center;">FY14</td>
				<td class="fieldName" style="text-align:center;">FY13</td>
				<td class="fieldName" style="text-align:center;">FY12</td>
				<td class="fieldName" style="text-align:center;">FY11</td>
				<td class="fieldName" style="text-align:center;">FY10</td>
				<td class="fieldName" style="text-align:center;">Other / Reports</td>
			</tr>
			<tr>
				<td class="boxTotal" colspan="8"></td>
			</tr>
		</tbody>
		</table>
		</div>
	</td></tr> <!-- end tan_table -->

	<tr class="requiredDiv">
		<th colspan="3">Initial</th>
		<th colspan="2">FY16 Scanning</th>
		<th colspan="2">FY15 Scanning</th>
		<th colspan="2">FY14/FY13 Scanning</th>
		<th colspan="1">FY12/FY11<br/>Scanning</th>
		<th colspan="7">Final Steps</th>
	</tr>

	<% if (!CommonMethods.isEmpty(inputBean.getSchedTrainingDate())) { %>
	<tr class="requiredDiv">
		<td colspan="17" style="background:#ccc;">
			Scheduled Training Date:
			<b><bean:write name="inputBean" property="schedTrainingDate"/></b>
		</td>
	</tr>
	<% } %>

	<tr class="requiredDiv">
		<td class="workflowTd">
			<br/><br/>Backfile<br/>Date<br/>Requested<br/><br/>
			<html:text name="inputBean" property="requestedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="requestedDateCss" styleId="requestedDateCss_none"   value=""                /><label for="requestedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="requestedDateCss" styleId="requestedDateCss_yellow" value="background:#ff0;"/><label for="requestedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="requestedDateCss" styleId="requestedDateCss_red"    value="background:#f00;"/><label for="requestedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="requestedDateCss" styleId="requestedDateCss_blue"   value="background:#0ae;"/><label for="requestedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>Date<br/>Received<br/>By PSHI<br/><br/>
			<html:text name="inputBean" property="receivedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="receivedDateCss" styleId="receivedDateCss_none"   value=""                /><label for="receivedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="receivedDateCss" styleId="receivedDateCss_yellow" value="background:#ff0;"/><label for="receivedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="receivedDateCss" styleId="receivedDateCss_red"    value="background:#f00;"/><label for="receivedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="receivedDateCss" styleId="receivedDateCss_blue"   value="background:#0ae;"/><label for="receivedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>Date<br/>Delivered<br/>to Scanning<br/><br/>
			<html:text name="inputBean" property="scanningDeliveredDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="scanningDeliveredDateCss" styleId="scanningDeliveredDateCss_none"   value=""                /><label for="scanningDeliveredDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="scanningDeliveredDateCss" styleId="scanningDeliveredDateCss_yellow" value="background:#ff0;"/><label for="scanningDeliveredDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="scanningDeliveredDateCss" styleId="scanningDeliveredDateCss_red"    value="background:#f00;"/><label for="scanningDeliveredDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="scanningDeliveredDateCss" styleId="scanningDeliveredDateCss_blue"   value="background:#0ae;"/><label for="scanningDeliveredDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>

		<td class="workflowTd">
			<br/><br/>FY16<br/>Completed<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy16CompletedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy16CompletedDateCss" styleId="fy16CompletedDateCss_none"   value=""                /><label for="fy16CompletedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16CompletedDateCss" styleId="fy16CompletedDateCss_yellow" value="background:#ff0;"/><label for="fy16CompletedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16CompletedDateCss" styleId="fy16CompletedDateCss_red"    value="background:#f00;"/><label for="fy16CompletedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16CompletedDateCss" styleId="fy16CompletedDateCss_blue"   value="background:#0ae;"/><label for="fy16CompletedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/>FY16<br/>CD Sent<br/>for Customer<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy16MailedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy16MailedDateCss" styleId="fy16MailedDateCss_none"   value=""                /><label for="fy16MailedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16MailedDateCss" styleId="fy16MailedDateCss_yellow" value="background:#ff0;"/><label for="fy16MailedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16MailedDateCss" styleId="fy16MailedDateCss_red"    value="background:#f00;"/><label for="fy16MailedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy16MailedDateCss" styleId="fy16MailedDateCss_blue"   value="background:#0ae;"/><label for="fy16MailedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>

		<td class="workflowTd">
			<br/><br/>FY15<br/>Completed<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy15CompletedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy15CompletedDateCss" styleId="fy15CompletedDateCss_none"   value=""                /><label for="fy15CompletedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15CompletedDateCss" styleId="fy15CompletedDateCss_yellow" value="background:#ff0;"/><label for="fy15CompletedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15CompletedDateCss" styleId="fy15CompletedDateCss_red"    value="background:#f00;"/><label for="fy15CompletedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15CompletedDateCss" styleId="fy15CompletedDateCss_blue"   value="background:#0ae;"/><label for="fy15CompletedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/>FY15<br/>CD Sent<br/>for Customer<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy15MailedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy15MailedDateCss" styleId="fy15MailedDateCss_none"   value=""                /><label for="fy15MailedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15MailedDateCss" styleId="fy15MailedDateCss_yellow" value="background:#ff0;"/><label for="fy15MailedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15MailedDateCss" styleId="fy15MailedDateCss_red"    value="background:#f00;"/><label for="fy15MailedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy15MailedDateCss" styleId="fy15MailedDateCss_blue"   value="background:#0ae;"/><label for="fy15MailedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>FY14/13<br/>Completed<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy1314CompletedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy1314CompletedDateCss" styleId="fy1314CompletedDateCss_none"   value=""                /><label for="fy1314CompletedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314CompletedDateCss" styleId="fy1314CompletedDateCss_yellow" value="background:#ff0;"/><label for="fy1314CompletedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314CompletedDateCss" styleId="fy1314CompletedDateCss_red"    value="background:#f00;"/><label for="fy1314CompletedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314CompletedDateCss" styleId="fy1314CompletedDateCss_blue"   value="background:#0ae;"/><label for="fy1314CompletedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/>FY14/13<br/>CD Sent<br/>for Customer<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy1314BurnedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy1314BurnedDateCss" styleId="fy1314BurnedDateCss_none"   value=""                /><label for="fy1314BurnedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314BurnedDateCss" styleId="fy1314BurnedDateCss_yellow" value="background:#ff0;"/><label for="fy1314BurnedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314BurnedDateCss" styleId="fy1314BurnedDateCss_red"    value="background:#f00;"/><label for="fy1314BurnedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314BurnedDateCss" styleId="fy1314BurnedDateCss_blue"   value="background:#0ae;"/><label for="fy1314BurnedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>FY12/11<br/>Completed<br/>Date<br/><br/>
			<html:text name="inputBean" property="fy1112CompletedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy1112CompletedDateCss" styleId="fy1112CompletedDateCss_none"   value=""                /><label for="fy1112CompletedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1112CompletedDateCss" styleId="fy1112CompletedDateCss_yellow" value="background:#ff0;"/><label for="fy1112CompletedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1112CompletedDateCss" styleId="fy1112CompletedDateCss_red"    value="background:#f00;"/><label for="fy1112CompletedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1112CompletedDateCss" styleId="fy1112CompletedDateCss_blue"   value="background:#0ae;"/><label for="fy1112CompletedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/><br/>Extract File<br/>Created<br/><br/>
			<html:text name="inputBean" property="extractDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="extractDateCss" styleId="extractDateCss_none"   value=""                /><label for="extractDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="extractDateCss" styleId="extractDateCss_yellow" value="background:#ff0;"/><label for="extractDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="extractDateCss" styleId="extractDateCss_red"    value="background:#f00;"/><label for="extractDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="extractDateCss" styleId="extractDateCss_blue"   value="background:#0ae;"/><label for="extractDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>All backfile<br/>CD mailed<br/>to Cust/SD<br/><br/>
			<html:text name="inputBean" property="fy1314MailedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="fy1314MailedDateCss" styleId="fy1314MailedDateCss_none"   value=""                /><label for="fy1314MailedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314MailedDateCss" styleId="fy1314MailedDateCss_yellow" value="background:#ff0;"/><label for="fy1314MailedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314MailedDateCss" styleId="fy1314MailedDateCss_red"    value="background:#f00;"/><label for="fy1314MailedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="fy1314MailedDateCss" styleId="fy1314MailedDateCss_blue"   value="background:#0ae;"/><label for="fy1314MailedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/>Verify<br/>All Backfiles<br/>Installed<br/>in FACET<br/><br/>
			<html:text name="inputBean" property="laptopInstalledDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="laptopInstalledDateCss" styleId="laptopInstalledDateCss_none"   value=""                /><label for="laptopInstalledDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="laptopInstalledDateCss" styleId="laptopInstalledDateCss_yellow" value="background:#ff0;"/><label for="laptopInstalledDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="laptopInstalledDateCss" styleId="laptopInstalledDateCss_red"    value="background:#f00;"/><label for="laptopInstalledDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="laptopInstalledDateCss" styleId="laptopInstalledDateCss_blue"   value="background:#0ae;"/><label for="laptopInstalledDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/>All Backfile<br/>CD Delivered<br/>to DACS<br/><br/>
			<html:text name="inputBean" property="logcopDeliveredDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="logcopDeliveredDateCss" styleId="logcopDeliveredDateCss_none"   value=""                /><label for="logcopDeliveredDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopDeliveredDateCss" styleId="logcopDeliveredDateCss_yellow" value="background:#ff0;"/><label for="logcopDeliveredDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopDeliveredDateCss" styleId="logcopDeliveredDateCss_red"    value="background:#f00;"/><label for="logcopDeliveredDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopDeliveredDateCss" styleId="logcopDeliveredDateCss_blue"   value="background:#0ae;"/><label for="logcopDeliveredDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/>Verified<br/>DACS<br/>Backfile<br/>Uploaded<br/><br/>
			<html:text name="inputBean" property="logcopUploadedDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="logcopUploadedDateCss" styleId="logcopUploadedDateCss_none"   value=""                /><label for="logcopUploadedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopUploadedDateCss" styleId="logcopUploadedDateCss_yellow" value="background:#ff0;"/><label for="logcopUploadedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopUploadedDateCss" styleId="logcopUploadedDateCss_red"    value="background:#f00;"/><label for="logcopUploadedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="logcopUploadedDateCss" styleId="logcopUploadedDateCss_blue"   value="background:#0ae;"/><label for="logcopUploadedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt<br/><br/>
			<html:text name="inputBean" property="finalReportDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="finalReportDateCss" styleId="finalReportDateCss_none"   value=""                /><label for="finalReportDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="finalReportDateCss" styleId="finalReportDateCss_yellow" value="background:#ff0;"/><label for="finalReportDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="finalReportDateCss" styleId="finalReportDateCss_red"    value="background:#f00;"/><label for="finalReportDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="finalReportDateCss" styleId="finalReportDateCss_blue"   value="background:#0ae;"/><label for="finalReportDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
		<td class="workflowTd">
			<br/><br/><br/>Destruction<br/>Date<br/><br/>
			<html:text name="inputBean" property="destructionDate" size="9" styleClass="datepicker"/>
			<div class="colorRadioBar">
				<html:radio name="inputBean" property="destructionDateCss" styleId="destructionDateCss_none"   value=""                /><label for="destructionDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
				<html:radio name="inputBean" property="destructionDateCss" styleId="destructionDateCss_yellow" value="background:#ff0;"/><label for="destructionDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
				<html:radio name="inputBean" property="destructionDateCss" styleId="destructionDateCss_red"    value="background:#f00;"/><label for="destructionDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
				<html:radio name="inputBean" property="destructionDateCss" styleId="destructionDateCss_blue"   value="background:#0ae;"/><label for="destructionDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
			</div>
		</td>
	</tr>

	<tr class="requiredDiv">
		<td colspan="3" class="nobordered" align="center"> <!-- tan_table -->
			<div class="center">
			<table id="borderlessTable" border="0" cellspacing="0"><tbody>
				<tr>
					<td style="font-weight:bold;" align="right">OVERALL DUE DATE</td>
					<td style="font-size:80%;" align="center" class="dueDate"></td>
				</tr>
				<tr>
					<td style="font-size:80%;" align="right">Estimated Completion Date</td>
					<td style="font-size:80%;"><html:text name="inputBean" property="estCompletedDate" styleClass="datepicker" size="9"/></td>
				</tr>
			</tbody></table>
			</div>
		</td> <!-- end tan_table -->
		<td colspan="2" class="nobordered" align="center"> <!-- tan_table -->
		</td> <!-- end tan_table -->
		<td colspan="2" class="nobordered" align="center"> <!-- tan_table -->
		</td> <!-- end tan_table -->
		<td colspan="2" class="nobordered" align="center"> <!-- tan_table -->
			<div class="center">
			<table class="border-zero cellspacing-zero"><tbody>
				<tr>
					<td style="font-size:80%;font-weight:bold;" align="right">Due Date</td>
					<td style="font-size:80%;" align="center" class="fy1314DueDate"></td>
				</tr>
				<tr>
					<td style="font-size:80%;" align="right">ETA</td>
					<td style="font-size:80%;"><html:text name="inputBean" property="estFy1314CompletedDate" styleClass="datepicker" size="9"/></td>
				</tr>
			</tbody></table>
			</div>
		</td> <!-- end tan_table -->
		<td colspan="1" class="nobordered" align="center"> <!-- tan_table -->
			<div class="center">
			<table class="border-zero cellspacing-zero"><tbody>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td style="font-size:80%;" align="right">ETA</td>
					<td style="font-size:80%;"><html:text name="inputBean" property="estFy1112CompletedDate" styleClass="datepicker" size="9"/></td>
				</tr>
			</tbody></table>
			</div>
		</td> <!-- end tan_table -->
		<td colspan="7" class="nobordered" align="left"> <!-- tan_table -->
			<table>
			<tbody>
				<tr>
					<td valign="top"><label><html:checkbox name="inputBean" property="returnInd" value="Y" styleId="returnInd"/> Return to Unit</label></td>
					<td class="workflowTd returnFields">
						<br/>Backfile<br/>Returned<br/>Date<br/><br/>
						<html:text name="inputBean" property="returnedDate" size="9" styleClass="datepicker"/>
						<div class="colorRadioBar">
							<html:radio name="inputBean" property="returnedDateCss" styleId="returnedDateCss_none"   value=""                /><label for="returnedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnedDateCss" styleId="returnedDateCss_yellow" value="background:#ff0;"/><label for="returnedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnedDateCss" styleId="returnedDateCss_red"    value="background:#f00;"/><label for="returnedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnedDateCss" styleId="returnedDateCss_blue"   value="background:#0ae;"/><label for="returnedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
						</div>
					</td>
					<td class="workflowTd returnFields">
						Client<br/>Received<br/>Confirmation<br/>Date<br/><br/>
						<html:text name="inputBean" property="returnConfirmedDate" size="9" styleClass="datepicker"/>
						<div class="colorRadioBar">
							<html:radio name="inputBean" property="returnConfirmedDateCss" styleId="returnConfirmedDateCss_none"   value=""                /><label for="returnConfirmedDateCss_none"   style="background:#eee;"><img src="images/empty.gif" alt="none"  width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnConfirmedDateCss" styleId="returnConfirmedDateCss_yellow" value="background:#ff0;"/><label for="returnConfirmedDateCss_yellow" style="background:#ff0;"><img src="images/empty.gif" alt="yellow" width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnConfirmedDateCss" styleId="returnConfirmedDateCss_red"    value="background:#f00;"/><label for="returnConfirmedDateCss_red"    style="background:#f00;"><img src="images/empty.gif" alt="red"   width="13" height="8"/></label>
							<html:radio name="inputBean" property="returnConfirmedDateCss" styleId="returnConfirmedDateCss_blue"   value="background:#0ae;"/><label for="returnConfirmedDateCss_blue"   style="background:#0ae;"><img src="images/empty.gif" alt="blue"  width="13" height="8"/></label>
						</div>
					</td>
				</tbody>
				</table>
		</td> <!-- end tan_table -->
	</tr>

	<tr><th colspan="17">Comments</th></tr>
	<tr><td colspan="17" class="nobordered" align="center"> <!-- tan_table -->
		<html:textarea name="inputBean" property="comments" rows="10" styleClass="form-control input-sm"/>
	</td></tr> <!-- end tan_table -->
</tbody>
</table>
</div>
</p>

<p align="center">
<div class="center">
<table id="borderlessTable" border="0" cellspacing="0">
<tbody>
	<tr>
		<td align="center">
			<% if (editType.equals("add")) { %>
				<button type="submit" value="Submit" class="btn btn-success"><span class="glyphicon glyphicon-ok"></span> Insert</button>
			<% } else { %>
				<button type="submit" value="Submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Save</button>
			<% } %>
		</td>
		<td align="center">
			<a class="btn btn-default" href="backfile.do?action=workflowSummary&projectPk=<bean:write name="projectPk"/>"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
		</td>
	</tr>
</tbody>
</table>
</div>
</p>
</html:form>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
	$(function() {
		$('#isRequired').on('change', function() {
			showFields = ($(this).is(':checked'));
			$('.requiredDiv').each(function() {
				if (showFields)
					$(this).show();
				else
					$(this).hide();
			});
		}).change();

		$('.colorRadioBar input:radio').addClass('input_hidden');

		$('.colorRadioBar label').click(function() {
			$(this).addClass('selected').siblings().removeClass('selected');
			$('#' + $(this).attr('for')).prop('checked', true);
		});

		$(".datepicker").attr('autocomplete', 'off');
		$(".datepicker").datepicker();

		$("#shipPk").on("change", function() {
			$("#shipName").val($(this).children("option").filter(":selected").text());
		});

		$('#returnInd').on('change', function() {
			showFields = ($(this).is(':checked'));
			$('.returnFields').each(function() {
				if (showFields)
					$(this).show();
				else
					$(this).hide();
			});
		}).change();

		//Set pshiBoxCnt change to calculate total boxes
		$(".pshiBoxCnt").on("change", function() {
			var cnt = 0;
			if ($(this).val() && !$.isNumeric($(this).val())) $(this).val("");
			$('.pshiBoxCnt').each(function() {
				if ($(this).val() && $.isNumeric($(this).val())) cnt += parseInt($(this).val());
			});

			$('.pshiBoxTotal').html('Total Boxes PSHI Received: ' + cnt);
		});

		//Set boxCnt change to calculate total boxes
		$(".boxCnt").on("change", function() {
			var cnt = 0;
			if ($(this).val() && !$.isNumeric($(this).val())) $(this).val("");
			$('.boxCnt').each(function() {
				if ($(this).val() && $.isNumeric($(this).val())) cnt += parseInt($(this).val());
			});

			$('.boxTotal').html('Total Boxes Sent to Scanning: ' + cnt);
		});
	});

	$(document).ready(function () {
		$(".pshiBoxCnt").change();
		$(".boxCnt").change();

		$('.colorRadioBar input:radio').each(function() {
			if ($(this).attr('checked') == 'checked') $(this).next('label').click();
		});

		<% if (editType.equals("add")) { %>
			$("#shipPk").focus();
		<% } %>
	});

	function valFields() {
		var shipPk = document.backfileForm.shipPk.value;

		<% if (editType.equals("add")) { %>
			if (shipPk.length < 1) {
				alert("You must select a ship.");
				document.backfileForm.shipPk.focus();
				return false;
			} //end of if
		<% } %>

		return true;
	} //end of valFields

/*
2014.03.05 ATT :: Disabled workflow code below
		//Workflow dates
		$(".workflowDate").on("change", function() {
			var keepGoing = true;
			var obj;
			$('.workflowDate').each(function() {
				if ($(this).attr('name') == "scanningDeliveredDate" && isDate($(this).val())) {
					var newDueDate = createDateObject($(this).val());
					newDueDate.setDate(newDueDate.getDate() + 15);
					$('.fy1314DueDate').html(formatDateString(newDueDate));
					newDueDate.setDate(newDueDate.getDate() + 30);
					$('.dueDate').html(formatDateString(newDueDate));
				} //end of if

				if (keepGoing && $(this).val().length > 0 && validateDate($(this).val(), "date")) {
//						$(this).show(); //enable
					$(this).closest('td').attr('style', 'background:#fff;font-weight:normal;color:#777;');
				} else {
					if (!keepGoing) {
//							$(this).hide(); //disable
						$(this).closest('td').attr('style', 'background:#fff;font-weight:normal;color:#ccc;');
					} else {
						$(this).show(); //enable
						$(this).closest('td').attr('style', 'background:#ff0;font-weight:bold;color:#000;');
						obj = $(this);
					}
					keepGoing = false;
				} //end of else
			});

//				obj.focus();
		});
*/
//			$("#firstWorkflow").change();
</script>

</body>
</html>
