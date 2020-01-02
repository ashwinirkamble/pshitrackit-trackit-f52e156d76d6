<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested"  uri="http://struts.apache.org/tags-nested" %>

<bean:define id="defaultPageTitle" value="Transmittal Summary"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="chartList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="inputBean"	scope="request" class="com.premiersolutionshi.old.bean.ReportBean"/>
<jsp:useBean id="projectPk"	scope="request" class="java.lang.String"/>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery.jqplot.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<p align="center">
		<html:form action="report.do" method="GET">
		<input type="hidden" name="action" value="transmittalDetail"/>
		<input type="hidden" name="scrollable" value="true"/>
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<html:select name="inputBean" property="facetName" styleId="facetName">
			<html:option value=""/>
			<html:options collection="configuredSystemList" property="computerName" labelProperty="shipNameFacetName"/>
		</html:select>
		</html:form>
		</p>

		<center>
			<div id="jqplot-canvas" style="width:900px;height:320px;"></div>
		</center>

		<p align="center">
		<center>
		<table id="dataTable_style2" border="0" cellspacing="0">
		<thead>
			<tr>
				<th align="center">Transmittal #</th>
				<th align="left">Doc Type</th>
				<th align="center">Document Count</th>
				<th align="center">Uploaded Date</th>
				<th align="left">Uploaded By</th>
			</tr>
		</thead>
		<tbody>
		<logic:empty name="resultList">
			<tr>
				<td align="center" colspan="5">No Transmittals Uploaded</td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="resultList">
		<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ReportBean">
			<logic:notEmpty name="resultBean" property="uploadDate">
				<tr valign="middle">
					<td align="center"><bean:write name="resultBean" property="transmittalNum"/></td>
					<td align="left"><bean:write name="resultBean" property="docType"/></td>
					<td align="center"><bean:write name="resultBean" property="docCnt"/></td>
					<td align="center"><bean:write name="resultBean" property="uploadDate"/></td>
					<td align="left"><bean:write name="resultBean" property="uploadUser"/></td>
				</tr>
			</logic:notEmpty>
			<logic:empty name="resultBean" property="uploadDate">
				<logic:notEmpty name="resultBean" property="exceptionReason">
					<tr valign="middle" style="background:#ccc;">
						<td align="center"><bean:write name="resultBean" property="transmittalNum"/></td>
						<td align="left" colspan="4">
							Missing Transmittal - <bean:write name="resultBean" property="exceptionReason"/>
							<!--a href="X">Up</a-->
						</td>
					</tr>
				</logic:notEmpty>
				<logic:empty name="resultBean" property="exceptionReason">
					<tr valign="middle" style="background:#d00;">
						<td align="center"><bean:write name="resultBean" property="transmittalNum"/></td>
						<td align="left" colspan="4">Missing Transmittal</td>
					</tr>
				</logic:empty>
			</logic:empty>
		</logic:iterate>
		</logic:notEmpty>
		</tbody>
		</table>
		</center>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.pointLabels.min.js"></script>

<script type="text/javascript">
	$(function() {
		$('#facetName').on('change', function() {
			if ($(this).val() != '') this.form.submit();
		});
	});

	$(document).ready(function () {
		var s1 = [<logic:iterate id="chartBean" name="chartList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value"/></logic:iterate>];

		// Can specify a custom tick Array.
		// Ticks should match up one for each y value (category) in the series.
		var ticks = [<logic:iterate id="chartBean" name="chartList" indexId="i"><% if (i > 0) { %>,<% } %>'<bean:write name="chartBean" property="label"/>'</logic:iterate>];

		var plot1 = $.jqplot('jqplot-canvas', [s1], {
			title: 'Documents Uploaded By Month',
			captureRightClick: true,
			seriesDefaults:{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					fillToZero: true
				},
				pointLabels: {
					show: true
				}
			},
			series:[
				{ label: 'Documents Uploaded (excludes TLs 000000 and 999999)' }
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
				renderer: $.jqplot.EnhancedLegendRenderer,
				show: true,
				rendererOptions: {
					numberRows: 1,
					seriesToggle: false
				},
				placement: "outsideGrid",
				location: 's'
			}
		});

		<% if (CommonMethods.nes(request.getParameter("scrollable")).equals("true")) { %>
		$('#facetName').focus();
		<% } %>

	});
</script>

</body>
</html>
