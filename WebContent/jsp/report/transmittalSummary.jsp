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
<jsp:useBean id="projectPk"	scope="request" class="java.lang.String"/>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<!--center>
			<div id="jqplot-canvas" style="width:900px;height:320px;"></div>
		</center-->

		<p align="center">
		<a href="export.do?action=transmittalSummaryXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
		</p>

		<p align="center">
		<center>
		<table id="dataTable" class="compact row-border cell-border" cellspacing="0">
		<thead>
			<tr>
				<th>Unit Name</th>
				<th>FACET Name</th>
				<th>Homeport</th>
				<th>Last Activity</th>
				<th>1348</th>
				<th>1149</th>
				<th>Food Approval</th>
				<th>Food Receipt</th>
				<th>Pcard Admin</th>
				<th>Pcard Invoice</th>
				<th>Price Change Report</th>
				<th>SFOEDL</th>
				<th>UOL</th>
				<th># of missing transmittals</th>
			</tr>
		</thead>
		<tbody>
		<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ReportBean">
			<tr align="left" valign="middle">
				<td><a href="report.do?action=transmittalDetail&facetName=<bean:write name="resultBean" property="facetName"/>&projectPk=<bean:write name="projectPk"/>"><bean:write name="resultBean" property="shipName"/></a></td>
				<td><bean:write name="resultBean" property="facetName"/></td>
				<td><bean:write name="resultBean" property="homeport"/></td>
				<td align="center" style="<bean:write name="resultBean" property="lastUploadDateCss"/>"><bean:write name="resultBean" property="lastUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="form1348UploadDateCss"/>"><bean:write name="resultBean" property="form1348UploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="form1149UploadDateCss"/>" nowrap>
					<bean:write name="resultBean" property="form1149UploadDate"/>
					<logic:notEmpty name="resultBean" property="fuelClosureNotes">
						<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="fuelClosureNotes"/>"/>
					</logic:notEmpty>
				</td>
				<td align="center" style="<bean:write name="resultBean" property="foodApprovalUploadDateCss"/>" nowrap>
					<bean:write name="resultBean" property="foodApprovalUploadDate"/>
					<logic:notEmpty name="resultBean" property="s2ClosureNotes">
						<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="s2ClosureNotes"/>"/>
					</logic:notEmpty>
				</td>
				<td align="center" style="<bean:write name="resultBean" property="foodReceiptUploadDateCss"/>" nowrap>
					<bean:write name="resultBean" property="foodReceiptUploadDate"/>
					<logic:notEmpty name="resultBean" property="s2ClosureNotes">
						<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="s2ClosureNotes"/>"/>
					</logic:notEmpty>
				</td>
				<td align="center" style="<bean:write name="resultBean" property="pcardAdminUploadDateCss"/>"><bean:write name="resultBean" property="pcardAdminUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="pcardInvoiceUploadDateCss"/>"><bean:write name="resultBean" property="pcardInvoiceUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="priceChangeUploadDateCss"/>"><bean:write name="resultBean" property="priceChangeUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="sfoedlUploadDateCss"/>"><bean:write name="resultBean" property="sfoedlUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="uolUploadDateCss"/>"><bean:write name="resultBean" property="uolUploadDate"/></td>
				<td align="center" style="<bean:write name="resultBean" property="missingTransmittalCss"/>"><bean:write name="resultBean" property="missingTransmittalListSize"/></td>
			</tr>
		</logic:iterate>
		</tbody>
		</table>
		</center>
		</p>

		<p align="center">
		<a href="export.do?action=transmittalSummaryXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>

<script type="text/javascript">
	$(function() {
		$(document).tooltip();

		var table = $('#dataTable').DataTable( {
			"paging": false,
			"aoColumns": [
				null,
				null,
				null,
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				{ "orderSequence": [ "desc", "asc", "asc" ] }
			],
			stateSave: false
		});
	});

<% if (false) { %>
<script src="js/jquery.jqplot.min.js" type="text/javascript"></script>
<link href="css/jquery.jqplot.min.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.pointLabels.min.js"></script>
<% } %>
<% if (false) { %>
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
<% } %>
</script>

</body>
</html>
