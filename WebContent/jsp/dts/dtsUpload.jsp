<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested"  uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Upload DACS Reports"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="summaryBean"						scope="request" class="com.premiersolutionshi.old.bean.ReportBean"/>
<jsp:useBean id="differenceBean"				scope="request" class="com.premiersolutionshi.old.bean.ReportBean"/>
<jsp:useBean id="differenceList"				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="facetDiscrepancyList"	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="facetUnknownList"			scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="inputBean"	scope="request" class="com.premiersolutionshi.old.bean.DtsBean"/>

<jsp:useBean id="projectPk"	scope="request" class="java.lang.String"/>

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

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

		<center>
		<div id="uploadForm" class="panel panel-primary" style="width:650px;">
			<div class="panel-heading">
				<h3 class="panel-title">Upload DACS Report</h3>
			</div>
			<html:form action="dts.do" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="action" value="dtsUploadDo"/>
			<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
			<div class="panel-body">
				<table id="borderlessTable" border="0" cellspacing="0"><tbody>
					<tr>
						<td><input id="dts-file" type="file" name="file" size="70"/></td>
						<td><button id="dts-submit-btn" type="submit" value="Submit" class="btn btn-primary disabled"><span class="glyphicon glyphicon-ok"></span> Upload</button></td>
					</tr>
				</table>
			</div>
			</html:form>
		</div>
		</center>

		<p align="center">
		<center>
		<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color">
			<tr>
				<th colspan="8">Latest DACS Report Loaded</th>
			</tr>
			<tr>
				<td class="fieldName">Report Date:</td>
				<td style="font-weight:bold;"><bean:write name="summaryBean" property="reportDate"/></td>
				<td class="fieldName">Total Transmittals:</td>
				<td style="font-weight:bold;"><bean:write name="summaryBean" property="transmittalCnt"/></td>
				<td class="fieldName">Total Ships:</td>
				<td style="font-weight:bold;"><bean:write name="summaryBean" property="shipCnt"/></td>
				<td class="fieldName">Total Documents:</td>
				<td style="font-weight:bold;"><bean:write name="summaryBean" property="docCnt"/></td>
			</tr>
		</table>
		</p>

		<logic:notEmpty name="facetUnknownList">
		<p align="left">
		<img src="images/icon_error.gif"/>
		The following activity names in the DACS Report do not match any activity names in the <a href="ship.do?action=shipList">UIC List</a>:<br/>
		<span style="color:red;">
		<logic:iterate id="systemBean" name="facetUnknownList" type="com.premiersolutionshi.old.bean.SystemBean">
			<bean:write name="systemBean" property="shipName"/> (<bean:write name="systemBean" property="computerName"/>)<br/>
		</logic:iterate>
		</span>
		</p>
		</logic:notEmpty>

		<logic:notEmpty name="facetDiscrepancyList">
		<p align="left">
		<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" align="left">
		<thead>
			<tr class="ignore">
				<td colspan="4" style="border:none;">
					<img src="images/icon_error.gif"/>
					FACET Upgrades Reported in DACS
				</td>
			</tr>
			<tr>
				<th>Unit Name</th>
				<th>FACET Name</th>
				<th>FACET Version<br/>in PSHI DB</th>
				<th>Reported Version<br/>in DACS</th>
				<th>Edit</th>
			</tr>
		</thead>
		<tbody>
		<logic:iterate id="resultBean" name="facetDiscrepancyList" type="com.premiersolutionshi.old.bean.SystemBean">
			<tr>
				<td><bean:write name="resultBean" property="shipName"/></td>
				<td><bean:write name="resultBean" property="computerName"/></td>
				<td>
					<bean:write name="resultBean" property="facetVersion"/>
					<% if (CommonMethods.cInt(resultBean.getFacetVersionOrder()) < CommonMethods.cInt(resultBean.getTransmittalFacetVersionOrder())) { %>
						<span style="color:green;">(Newer)</span>
					<% } %>
				</td>
				<td>
					<bean:write name="resultBean" property="transmittalFacetVersion"/>
					<% if (CommonMethods.cInt(resultBean.getTransmittalFacetVersionOrder()) < CommonMethods.cInt(resultBean.getFacetVersionOrder())) { %>
						<span style="color:green;">(Newer)</span>
					<% } %>
				</td>
				<td align="center"><a href="system.do?action=configuredSystemEdit&configuredSystemPk=<bean:write name="resultBean" property="configuredSystemPk"/>&projectPk=<bean:write name="projectPk"/>">Edit</a></td>
			</tr>
		</logic:iterate>
		</table>
		</p>
		</logic:notEmpty>

		<logic:notEmpty name="differenceList">
		<p align="left">
		<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" align="left">
		<thead>
			<tr class="ignore">
				<td colspan="4" style="border:none;">
					Differences from previous report on <b><bean:write name="differenceBean" property="reportDate"/></b>:
						<b><bean:write name="differenceBean" property="shipCnt"/></b> units uploaded
						<b><bean:write name="differenceBean" property="transmittalCnt"/></b> transmittals for a total of
						<b><bean:write name="differenceBean" property="docCnt"/></b> documents
				</td>
			</tr>
			<tr>
				<th>Ship Name</th>
				<th>FACET Name</th>
				<th>Transmittals Uploaded</th>
				<th>Last Upload Date</th>
			</tr>
		</thead>
		<tbody>
		<logic:iterate id="resultBean" name="differenceList" type="com.premiersolutionshi.old.bean.ReportBean">
			<tr>
				<td><bean:write name="resultBean" property="shipName"/></td>
				<td><bean:write name="resultBean" property="facetName"/></td>
				<td><bean:write name="resultBean" property="transmittalNum"/></td>
				<td align="center"><bean:write name="resultBean" property="uploadDate"/></td>
			</tr>
		</logic:iterate>
		</tbody>
		</table>
		</p>
		</logic:notEmpty>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script src="js/jquery-altcolor.js" type="text/javascript"></script>

<script>
	$('#dts-file').on('change', function() {
		if ($(this).val() != '') {
			$('#dts-submit-btn').removeClass('disabled');
		} //end of if
	});
</script>

</body>
</html>
