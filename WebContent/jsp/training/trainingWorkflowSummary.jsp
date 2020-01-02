<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Training Workflow List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>

<jsp:useBean id="inProdList"	 		scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="unschedList"	 		scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="completedList"	 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="overdueList"			scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="shipList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contractNumberList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="sortBy"  scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir" scope="request" class="java.lang.String"/>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery.jqplot.min.css"/>

  <style>
  	.datefield {
			text-align: center;
			}
		#tanTable_style2 th {
			font-size: 11px;
			vertical-align: bottom;
			}
  </style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1 col1bootstrap">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

		<% if (contractNumberList.size() > 0) { %>
			<div>
				<form class="form-inline" action="training.do" method="GET">
					<input type="hidden" name="action" value="workflowSummary"/>
					<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
					<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
					<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
					<input type="hidden" name="searchPerformed" value="Y"/>
					<div class="form-group">
						<label for="contractNumber">Contract Number:</label>
						<html:select name="inputBean" property="contractNumber" styleClass="form-control input-sm">
							<html:option value="">View All</html:option>
							<html:options name="contractNumberList"/>
						</html:select>
					</div>
					<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
				</form>
			</div><br/>
		<% } %>

		<div>
		<a href="export.do?action=trainingWorkflowXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
		</div><br/>

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">In Production (<%= inProdList.size() %>)</a></li>
				<li><a href="#tabs-2">Unscheduled (<%= unschedList.size() %>)</a></li>
				<li><a href="#tabs-3">Overdue (<%= overdueList.size() %>)</a></li>
				<li><a href="#tabs-4">Completed (<%= completedList.size() %>)</a></li>
				<li><a href="#tabs-5">Monthly Bar Chart</a></li>
			</ul>
			<div id="tabs-1">
				<% if (shipList.size() > 0) { %>
				<form class="form-inline" action="training.do" method="POST">
					<input type="hidden" name="action" value="workflowAddDo"/>
					<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
					<input type="hidden" id="shipName" name="shipName" value=""/>
					<div class="form-group">
						<label for="shipPk">Start Tracking Ship:</label>
						<html:select name="inputBean" property="shipPk" styleId="shipPk" styleClass="form-control input-sm">
							<html:options collection="shipList" property="shipPk" labelProperty="shipName"/>
						</html:select>
					</div>
					<button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</button>
				</form><br/>
				<% } %>

				<% if (inProdList.size() == 0) { %>
					<p class="error" align="center">
						No Workflows in Production
					</p>
				<% } else { %>
					<p>
					<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
					<thead>
						<tr class="ignore">
							<td colspan="14" class="recordCnt">
								<b><%= inProdList.size() %> workflows found</b>
							</td>
						</tr>
						<tr>
							<th colspan="4">Ship Information</th>
							<th rowspan="2">Action</th>
							<th colspan="10">Training Workflow</th>
						</tr>
						<tr>
							<th nowrap>
								<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
								<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
								<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
								<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th>RSupply</th>
							<th>Location<br/>File<br/>Received</th>
							<th>Location<br/>File<br/>Reviewed</th>
							<th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
							<th>System<br/>Shipped</th>
							<th>Computer<br/>Name<br/>in Database</th>
							<th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
							<th>Training Kit<br/>Ready</th>
							<th>Estimated<br/>Training<br/>Month</th>
							<th>Scheduled<br/>Training<br/>Date</th>
							<th>Confirmed<br/>By Client</th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="resultBean" name="inProdList" type="com.premiersolutionshi.old.bean.TrainingBean">
						<tr valign="top">
							<td>
								<bean:write name="resultBean" property="shipName"/>
								<logic:notEmpty name="resultBean" property="comments">
									<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
								</logic:notEmpty>
							</td>
							<td>
								<bean:write name="resultBean" property="type"/>
								<bean:write name="resultBean" property="hull"/>
							</td>
							<td><bean:write name="resultBean" property="homeport"/></td>
							<td><bean:write name="resultBean" property="rsupply"/></td>
							<td align="center" nowrap>
								<a href="training.do?action=workflowEdit&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
								&nbsp;<a href="training.do?action=workflowDeleteDo&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
							</td>
							<td class="datefield" title="Location File Received"><bean:write name="resultBean" property="locFileRecvDate"/></td>
							<td class="datefield" title="Location File Reviewed"><bean:write name="resultBean" property="locFileRevDate"/></td>
							<td class="datefield" title="PacFlt Sent Notification to Send Food Report"><bean:write name="resultBean" property="pacfltFoodReportDate"/></td>
							<td class="datefield" title="System Shipped"><bean:write name="resultBean" property="systemReadyDate"/></td>
							<td class="datefield" title="Computer Name in Database"><bean:write name="resultBean" property="computerNameDbDate"/></td>
							<td class="datefield" title="Computer Name Provided to Logcop"><bean:write name="resultBean" property="computerNameLogcopDate"/></td>
							<td class="datefield" title="Training Kit Ready"><bean:write name="resultBean" property="trainingKitReadyDate"/></td>
							<td class="datefield" title="Estimated Training Month"><bean:write name="resultBean" property="estTrainingMonth"/></td>
							<td class="datefield" title="Scheduled Training Date" style="<bean:write name="resultBean" property="schedTrainingDateCss"/>">
								<bean:write name="resultBean" property="schedTrainingDate"/>
								<bean:write name="resultBean" property="schedTrainingTime"/><logic:notEmpty name="resultBean" property="trainerFullName"><br/></logic:notEmpty>
								<bean:write name="resultBean" property="trainerFullName"/>
							</td>
							<td class="datefield"><logic:equal name="resultBean" property="clientConfirmedInd" value="Yes"><img src="images/icon_checkmark.png"/></logic:equal></td>
						</tr>
					</logic:iterate>
					</tbody>
					</table>
					</p>
				<% } %>
			</div>

			<div id="tabs-2">
				<% if (unschedList.size() == 0) { %>
					<p class="error" align="center">
						No Unscheduled Workflows Found
					</p>
				<% } else { %>
					<p>
					<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
					<thead>
						<tr class="ignore">
							<td colspan="13" class="recordCnt">
								<b><%= unschedList.size() %> workflows found</b>
							</td>
						</tr>
						<tr>
							<th colspan="4">Ship Information</th>
							<th rowspan="2">Action</th>
							<th colspan="8">Training Workflow</th>
						</tr>
						<tr>
							<th nowrap>
								<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
								<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
								<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
								<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th>RSupply</th>
							<th>Location<br/>File<br/>Received</th>
							<th>Location<br/>File<br/>Reviewed</th>
							<th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
							<th>System<br/>Shipped</th>
							<th>Computer<br/>Name<br/>in Database</th>
							<th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
							<th>Training Kit<br/>Ready</th>
							<th>Estimated<br/>Training<br/>Month</th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="resultBean" name="unschedList" type="com.premiersolutionshi.old.bean.TrainingBean">
						<tr valign="top">
							<td>
								<bean:write name="resultBean" property="shipName"/>
								<logic:notEmpty name="resultBean" property="comments">
									<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
								</logic:notEmpty>
							</td>
							<td>
								<bean:write name="resultBean" property="type"/>
								<bean:write name="resultBean" property="hull"/>
							</td>
							<td><bean:write name="resultBean" property="homeport"/></td>
							<td><bean:write name="resultBean" property="rsupply"/></td>
							<td align="center" nowrap>
								<a href="training.do?action=workflowEdit&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
								&nbsp;<a href="training.do?action=workflowDeleteDo&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
							</td>
							<td class="datefield" title="Location File Received"><bean:write name="resultBean" property="locFileRecvDate"/></td>
							<td class="datefield" title="Location File Reviewed"><bean:write name="resultBean" property="locFileRevDate"/></td>
							<td class="datefield" title="PacFlt Sent Notification to Send Food Report"><bean:write name="resultBean" property="pacfltFoodReportDate"/></td>
							<td class="datefield" title="System Shipped"><bean:write name="resultBean" property="systemReadyDate"/></td>
							<td class="datefield" title="Computer Name in Database"><bean:write name="resultBean" property="computerNameDbDate"/></td>
							<td class="datefield" title="Computer Name Provided to Logcop"><bean:write name="resultBean" property="computerNameLogcopDate"/></td>
							<td class="datefield" title="Training Kit Ready"><bean:write name="resultBean" property="trainingKitReadyDate"/></td>
							<td class="datefield" title="Estimated Training Month">
								<bean:write name="resultBean" property="estTrainingMonth"/><logic:notEmpty name="resultBean" property="trainerFullName"><br/></logic:notEmpty>
								<bean:write name="resultBean" property="trainerFullName"/>
							</td>
						</tr>
					</logic:iterate>
					</tbody>
					</table>
					</p>
				<% } %>
			</div>

			<div id="tabs-3">
				<% if (overdueList.size() == 0) { %>
					<p class="error" align="center">
						No Overdue Workflows Found
					</p>
				<% } else { %>
					<p>
					<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
					<thead>
						<tr class="ignore">
							<td colspan="14" class="recordCnt">
								<b><%= overdueList.size() %> workflows found</b>
							</td>
						</tr>
						<tr>
							<th colspan="4">Ship Information</th>
							<th rowspan="2">Action</th>
							<th colspan="10">Training Workflow</th>
						</tr>
						<tr>
							<th nowrap>
								<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
								<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
								<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
								<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th>RSupply</th>
							<th>Location<br/>File<br/>Received</th>
							<th>Location<br/>File<br/>Reviewed</th>
							<th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
							<th>System<br/>Shipped</th>
							<th>Computer<br/>Name<br/>in Database</th>
							<th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
							<th>Training Kit<br/>Ready</th>
							<th>Scheduled<br/>Training<br/>Date</th>
							<th>Confirmed<br/>By Client</th>
							<th>Actual<br/>Training<br/>Date</th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="resultBean" name="overdueList" type="com.premiersolutionshi.old.bean.TrainingBean">
						<tr valign="top">
							<td>
								<bean:write name="resultBean" property="shipName"/>
								<logic:notEmpty name="resultBean" property="comments">
									<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
								</logic:notEmpty>
							</td>
							<td>
								<bean:write name="resultBean" property="type"/>
								<bean:write name="resultBean" property="hull"/>
							</td>
							<td><bean:write name="resultBean" property="homeport"/></td>
							<td><bean:write name="resultBean" property="rsupply"/></td>
							<td align="center" nowrap>
								<a href="training.do?action=workflowEdit&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
								&nbsp;<a href="training.do?action=workflowDeleteDo&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
							</td>
							<td class="datefield" title="Location File Received"><bean:write name="resultBean" property="locFileRecvDate"/></td>
							<td class="datefield" title="Location File Reviewed"><bean:write name="resultBean" property="locFileRevDate"/></td>
							<td class="datefield" title="PacFlt Sent Notification to Send Food Report"><bean:write name="resultBean" property="pacfltFoodReportDate"/></td>
							<td class="datefield" title="System Shipped"><bean:write name="resultBean" property="systemReadyDate"/></td>
							<td class="datefield" title="Computer Name in Database"><bean:write name="resultBean" property="computerNameDbDate"/></td>
							<td class="datefield" title="Computer Name Provided to Logcop"><bean:write name="resultBean" property="computerNameLogcopDate"/></td>
							<td class="datefield" title="Training Kit Ready"><bean:write name="resultBean" property="trainingKitReadyDate"/></td>
							<td class="datefield" title="Scheduled Training Date" style="<bean:write name="resultBean" property="schedTrainingDateCss"/>">
								<bean:write name="resultBean" property="schedTrainingDate"/>
								<bean:write name="resultBean" property="schedTrainingTime"/><logic:notEmpty name="resultBean" property="trainerFullName"><br/></logic:notEmpty>
								<bean:write name="resultBean" property="trainerFullName"/>
							</td>
							<td class="datefield"><logic:equal name="resultBean" property="clientConfirmedInd" value="Yes"><img src="images/icon_checkmark.png"/></logic:equal></td>
							<td title="Actual Training Date" style="background:#f00;"></td>
						</tr>
					</logic:iterate>
					</tbody>
					</table>
					</p>
				<% } %>
			</div>

			<div id="tabs-4">
				<% if (completedList.size() == 0) { %>
					<p class="error" align="center">
						No Completed Workflows Found
					</p>
				<% } else { %>
					<p>
					<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
					<thead>
						<tr class="ignore">
							<td colspan="13" class="recordCnt">
								<b><%= completedList.size() %> workflows found</b>
							</td>
						</tr>
						<tr>
							<th colspan="4">Ship Information</th>
							<th rowspan="2">Action</th>
							<th colspan="9">Training Workflow</th>
						</tr>
						<tr>
							<th nowrap>
								<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
								<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
								<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th nowrap>
								<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
								<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
								<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
							</th>
							<th>RSupply</th>
							<th>Location<br/>File<br/>Received</th>
							<th>Location<br/>File<br/>Reviewed</th>
							<th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
							<th>System<br/>Shipped</th>
							<th>Computer<br/>Name<br/>in Database</th>
							<th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
							<th>Training Kit<br/>Ready</th>
							<th>Scheduled<br/>Training<br/>Date</th>
							<th>Actual<br/>Training<br/>Date</th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="resultBean" name="completedList" type="com.premiersolutionshi.old.bean.TrainingBean">
						<tr valign="top">
							<td>
								<bean:write name="resultBean" property="shipName"/>
								<logic:notEmpty name="resultBean" property="comments">
									<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
								</logic:notEmpty>
							</td>
							<td>
								<bean:write name="resultBean" property="type"/>
								<bean:write name="resultBean" property="hull"/>
							</td>
							<td><bean:write name="resultBean" property="homeport"/></td>
							<td><bean:write name="resultBean" property="rsupply"/></td>
							<td align="center" nowrap>
								<a href="training.do?action=workflowEdit&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
								&nbsp;<a href="training.do?action=workflowDeleteDo&trainingWorkflowPk=<bean:write name="resultBean" property="trainingWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
							</td>
							<td class="datefield" title="Location File Received"><bean:write name="resultBean" property="locFileRecvDate"/></td>
							<td class="datefield" title="Location File Reviewed"><bean:write name="resultBean" property="locFileRevDate"/></td>
							<td class="datefield" title="PacFlt Sent Notification to Send Food Report"><bean:write name="resultBean" property="pacfltFoodReportDate"/></td>
							<td class="datefield" title="System Shipped"><bean:write name="resultBean" property="systemReadyDate"/></td>
							<td class="datefield" title="Computer Name in Database"><bean:write name="resultBean" property="computerNameDbDate"/></td>
							<td class="datefield" title="Computer Name Provided to Logcop"><bean:write name="resultBean" property="computerNameLogcopDate"/></td>
							<td class="datefield" title="Training Kit Ready"><bean:write name="resultBean" property="trainingKitReadyDate"/></td>
							<td class="datefield" title="Scheduled Training Date" style="<bean:write name="resultBean" property="schedTrainingDateCss"/>"><bean:write name="resultBean" property="schedTrainingDate"/></td>
							<td class="datefield" title="Actual Training Date">
								<bean:write name="resultBean" property="actualTrainingDate"/><logic:notEmpty name="resultBean" property="trainerFullName"><br/></logic:notEmpty>
								<bean:write name="resultBean" property="trainerFullName"/>
							</td>
						</tr>
					</logic:iterate>
					</tbody>
					</table>
					</p>
				<% } %>
			</div>

			<div id="tabs-5">
				<center>
					<div id="chartdiv" style="width:850px;height:300px;"></div>
					<a href="training.do?action=workflowChart&projectPk=<bean:write name="projectPk"/>" target="_blank"><img src="images/icon_barchart.png" width="70" height="70"/><br/>View Full Image</a>
				</center>
			</div>
		</div>

		<form name="sortForm" action="training.do" method="GET">
			<input type="hidden" name="action" value="workflowSummary"/>
			<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
			<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
			<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
			<input type="hidden" name="searchPerformed" value="Y"/>
		</form>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<jsp:include page="../include/app-footer.jsp"/>

<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/jquery.jqplot.min.js"></script>

<script type="text/javascript" src="js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.pointLabels.min.js"></script>

<script type="text/javascript">
	$(function() {
		$(document).tooltip();

		$("#shipPk").on("change", function() {
			$("#shipName").val($(this).children("option").filter(":selected").text());
		});
	});

	$(document).ready(function () {
		$('.datefield').each(function() {
			if ($(this).html().trim() == "") $(this).css("background-color", "#aaa");
			$(this).css("font-size", "85%");
		});

		$("#shipPk").change();

		var s1 = [<logic:present name="actualValueList"><logic:iterate id="str" name="actualValueList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="str"/></logic:iterate></logic:present>];
		var s2 = [<logic:present name="schedValueList"><logic:iterate id="str" name="schedValueList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="str"/></logic:iterate></logic:present>];
		var s3 = [<logic:present name="schedValueList"><logic:iterate id="str" name="schedValueList" indexId="i"><% if (i > 0) { %>,<% } %>10</logic:iterate></logic:present>];

		// Can specify a custom tick Array.
		// Ticks should match up one for each y value (category) in the series.
		var ticks = [<logic:present name="barGraphLabelList"><logic:iterate id="str" name="barGraphLabelList" indexId="i"><% if (i > 0) { %>,<% } %>'<bean:write name="str"/>'</logic:iterate></logic:present>];

		var plot1 = $.jqplot('chartdiv', [s1, s2, s3], {
			title: 'FACET Training By Month',
			stackSeries: true,
			captureRightClick: true,
			seriesDefaults:{
				rendererOptions: {
					barMargin: 30,
					fillToZero: true
				},
				pointLabels: {
					show: true,
					stackedValue: true,
					hideZeros: true
				}
			},
			series:[
				{
					label: 'Training Completed',
					renderer:$.jqplot.BarRenderer,
					color: '#eaa228'
				},
				{
					label: 'Total Scheduled',
					renderer:$.jqplot.BarRenderer,
					color: '#4bb2c5'
				},
				{
					label: 'Monthly Target',
					renderer: $.jqplot.LineRenderer,
					color: 'red',
					disableStack: true,
					showMarker: false,
					pointLabels: {
						show: false
					}
				}
			],
			axes: {
				xaxis: {
					renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
				},
				yaxis: {
					padMin: 0
				}
			},
			legend: {
				show: true,
				renderer: $.jqplot.EnhancedLegendRenderer,
				rendererOptions: {
					numberRows: 1,
					seriesToggle: false
				},
				placement: "outsideGrid",
				location: 's'
			}
		});

		$("#tabs").tabs();
	});

	function confirmDelete(shipName) {
		return confirm("Are you sure you want to delete the training workflow for " + shipName);
	} //end of confirmDelete
</script>
</body>
</html>
