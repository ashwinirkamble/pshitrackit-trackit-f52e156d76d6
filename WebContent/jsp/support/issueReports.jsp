<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Support Issue Reports"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<jsp:useBean id="inputBean" 							scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="summaryByMonthList" 			scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="summaryByShipList" 			scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="totalByShipBean" 				scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="recentIssueTotal" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="recentIssueSummaryList" 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="recentIssueList" 				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="recentClosedTotal" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="recentClosedList" 				scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="contractNumberList" 			scope="request" class="java.util.ArrayList"/>

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
		.day {
			fill: #fff;
			stroke: #e0e0e0;
		}

		.month {
			fill: none;
			stroke: black;
			stroke-width: 2px;
		}

		.year-title {
			font-size: 12px;
			letter-spacing: 10px;
			fill: #00b0dd;
		}

		#d3-heat-calendar {
			padding-top: 10px;
			shape-rendering: crispEdges;
		}

		svg text {
			font-size: 10px;
		}
	</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<% if (contractNumberList.size() > 0) { %>
			<div>
				<form class="form-inline" action="support.do" method="GET">
					<input type="hidden" name="action" value="issueReports"/>
					<input type="hidden" name="searchPerformed" value="Y"/>
					<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
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

		<div id="tabs">
			<ul>
				<li><a href="#tabs-3">Summary By Ship</a></li>
				<li><a href="#tabs-1">Calendar Heat Map</a></li>
				<li><a href="#tabs-4">7-Day Summary</a></li>
			</ul>

			<div id="tabs-3">
				<center>
				<div id="jqplot-canvas" style="width:900px;height:320px;"></div>
				</center>

				<a href="support.do?action=issueMonthlyChart&projectPk=<bean:write name="projectPk"/>" target="_blank"><img src="images/icon_barchart.png" width="70" height="70"/><br/>View Full Image</a>

				<logic:empty name="summaryByShipList">
					<p class="error" align="center">
						No Ship Issues Found
					</p>
				</logic:empty>
				<logic:notEmpty name="summaryByShipList">
					<p align="center">
					<center>
					<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color">
					<thead>
						<tr class="ignore">
							<td colspan="14" class="NOBORDER jqplot-header">
								FACET Support Issues By Ship
							</td>
						</tr>
						<tr class="ignore">
							<td colspan="14" class="NOBORDER" align="center">
								<a href="export.do?action=issueSummaryByShipXlsx&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
							</td>
						</tr>

						<tr>
							<th rowspan="2">Unit Name</th>
							<th rowspan="2">Total<br/>Issues</th>
							<th rowspan="2">Open<br/>Issues</th>
							<th rowspan="2"># of<br/>Successful<br/>Support<br/>Visits</td>
							<th rowspan="2">Last Support Visit</td>
							<th colspan="9">Breakdown By Category</th>
						</tr>
						<tr>
							<th>LOGCOP</th>
							<th>FACET</th>
							<th>Kofax</th>
							<th>Administrative<br/>Receipt Tool</th>
							<th>FACET<br/>Update</th>
							<th>Laptop</th>
							<th>Follow-Up<br/>Training</th>
							<th>Backfile</th>
							<th>Misc/Other</th>
						</tr>
					</thead>
					<tbody>
					<logic:iterate id="resultBean" name="summaryByShipList" type="com.premiersolutionshi.old.bean.SupportBean">
						<tr style="vertical-align:top;">
							<td>
								<logic:notEmpty name="resultBean" property="shipName">
									<bean:write name="resultBean" property="shipName"/><br/>
									<span style="color:#777;font-style:italic;"><bean:write name="resultBean" property="homeport"/></span>&nbsp;
								</logic:notEmpty>
								<logic:empty name="resultBean" property="shipName">
									<i>Non-ship specific issue</i><br/>
									&nbsp;
								</logic:empty>
							</td>
							<td align="center" title="# of issues"><b><bean:write name="resultBean" property="issueCnt"/></b></td>
							<td align="center" title="# of issues currently open"><bean:write name="resultBean" property="openIssueCnt"/></td>

							<td align="center" title="# of support visits"><bean:write name="resultBean" property="supportVisitCnt"/></td>
							<td align="left" title="Last Support Visit">
								<logic:notEmpty name="resultBean" property="supportVisitDate">
									<bean:write name="resultBean" property="supportVisitDate"/>
									(<bean:write name="resultBean" property="category"/>)
								</logic:notEmpty>
							</td>

							<td align="center" title="# of LOGCOP-related issues"><bean:write name="resultBean" property="logcopCnt"/></td>
							<td align="center" title="# of FACET Database-related issues"><bean:write name="resultBean" property="facetCnt"/></td>
							<td align="center" title="# of Kofax-related issues"><bean:write name="resultBean" property="kofaxCnt"/></td>
							<td align="center" title="# of Administrative Receipt Tool-related issues"><bean:write name="resultBean" property="dummyCnt"/></td>
							<td align="center" title="# of FACET Update issues"><bean:write name="resultBean" property="updateCnt"/></td>
							<td align="center" title="# of Laptop-related issues"><bean:write name="resultBean" property="laptopCnt"/></td>
							<td align="center" title="# of Follow-Up Training issues"><bean:write name="resultBean" property="trainingCnt"/></td>
							<td align="center" title="# of Backfile-related issues"><bean:write name="resultBean" property="backfileCnt"/></td>
							<td align="center" title="# of Misc/Other issues"><bean:write name="resultBean" property="otherCnt"/></td>
						</tr>
					</logic:iterate>
					</tbody>
					<tfoot id="summary_totals">
						<tr>
							<th style="text-align:right;">Total:</th>
							<th title="Total # of issues"><bean:write name="totalByShipBean" property="issueCnt"/></th>
							<th title="Total # of issues currently open"><bean:write name="totalByShipBean" property="openIssueCnt"/></th>
							<th title="Total # of support visits"><bean:write name="totalByShipBean" property="supportVisitCnt"/></th>
							<th/>
							<th title="Total # of LOGCOP-related issues"><bean:write name="totalByShipBean" property="logcopCnt"/></th>
							<th title="Total # of FACET Database-related issues"><bean:write name="totalByShipBean" property="facetCnt"/></th>
							<th title="Total # of Kofax-related issues"><bean:write name="totalByShipBean" property="kofaxCnt"/></th>
							<th title="Total # of Administrative Receipt Tool Database-related issues"><bean:write name="totalByShipBean" property="dummyCnt"/></th>
							<th title="Total # of FACET Updates issues"><bean:write name="totalByShipBean" property="updateCnt"/></th>
							<th title="Total # of Laptop-related issues"><bean:write name="totalByShipBean" property="laptopCnt"/></th>
							<th title="Total # of Follow-Up Training issues"><bean:write name="totalByShipBean" property="trainingCnt"/></th>
							<th title="Total # of Backfile-related issues"><bean:write name="totalByShipBean" property="backfileCnt"/></th>
							<th title="Total # of Misc/Other issues"><bean:write name="totalByShipBean" property="otherCnt"/></th>
						</tr>
					</tfoot>
					</table>
					</center>
					</p>
				</logic:notEmpty>
			</div>

			<div id="tabs-1">
				<div>
					<div style="float:left;padding-right:5px;padding-top:3px;">Scale:</div>
					<div class="scalebox" style="background:rgb(208,228,239);" title="1-9"></div>
					<div class="scalebox" style="background:rgb(181,215,232);" title="10-19"></div>
					<div class="scalebox" style="background:rgb(157,202,225);" title="20-29"></div>
					<div class="scalebox" style="background:rgb(129,186,216);" title="30-39"></div>
					<div class="scalebox" style="background:rgb(106,172,208);" title="40-49"></div>
					<div class="scalebox" style="background:rgb(70,149,196);"  title="50-59"></div>
					<div class="scalebox" style="background:rgb(58,136,189);"  title="60-69"></div>
					<div class="scalebox" style="background:rgb(42,114,178);"  title="70-79"></div>
					<div class="scalebox" style="background:rgb(26,89,155);"   title="80-89"></div>
					<div class="scalebox" style="background:rgb(5,49,99);"     title="90-99"></div>
					<div class="scalebox" style="background:rgb(103,0,31);"    title="100+"></div>
				<br clear="all"/>
				</div>

				<div id="d3-heat-calendar"></div>
			</div>

			<div id="tabs-4">
				<p align="left">
				<b><bean:write name="recentIssueTotal"/> issue<% if (!recentIssueTotal.equals("1")) { %>s<% } %> opened in the last 7 days</b>
				</p>

				<logic:notEqual name="recentIssueTotal" value="0">
					<p align="left">
					<logic:iterate id="resultBean" name="recentIssueSummaryList" type="com.premiersolutionshi.old.bean.SupportBean">
						<b><bean:write name="resultBean" property="issueCnt"/></b>
						<bean:write name="resultBean" property="category"/>-related<br/>
					</logic:iterate>
					</p>

					<logic:iterate id="dateBean" name="recentIssueList" type="com.premiersolutionshi.old.bean.CalendarBean">
						<p align="left">
						<u><bean:write name="dateBean" property="date"/></u>
						</p>

						<p align="left">
						<logic:present name="dateBean" property="issueList">
							<ol style="text-align:left;">
							<logic:iterate id="resultBean" name="dateBean" property="issueList" type="com.premiersolutionshi.old.bean.SupportBean">
								<li>
								<logic:notEmpty name="resultBean" property="shipName">
									<b><bean:write name="resultBean" property="shipName"/></b>
								</logic:notEmpty>
								<logic:empty name="resultBean" property="shipName">
									<i>Non-ship specific</i>
								</logic:empty>
								- <bean:write name="resultBean" property="title"/>

								<logic:equal name="resultBean" property="status" value="2 - Active">
								 - <span style="color:red;"><i>Active</i></span>
								</logic:equal>
								<logic:notEqual name="resultBean" property="status" value="2 - Active">
									<logic:notEmpty name="resultBean" property="resolution">
										- <span style="color:green;"><bean:write name="resultBean" property="resolution"/></span>
									</logic:notEmpty>

									<logic:notEmpty name="resultBean" property="closedDate">
									<span style="color:#777;">(Closed on <bean:write name="resultBean" property="closedDate"/>)</span>
									</logic:notEmpty>
								</logic:notEqual>
								</li>
							</logic:iterate>
							</ol>
						</logic:present>

						<logic:notPresent name="dateBean" property="issueList">
							<ul style="text-align:left;"><li><i>None</i></li></ul>
						</logic:notPresent>
						</p>
					</logic:iterate>
				</logic:notEqual>

				<hr/>

				<p align="left">
				<b><bean:write name="recentClosedTotal"/> issue<% if (!recentClosedTotal.equals("1")) { %>s<% } %> closed in the last 7 days</b>
				</p>

				<logic:notEqual name="recentClosedTotal" value="0">
					<logic:iterate id="dateBean" name="recentClosedList" type="com.premiersolutionshi.old.bean.CalendarBean">
						<p align="left">
						<u><bean:write name="dateBean" property="date"/></u>
						</p>

						<p align="left">
						<logic:present name="dateBean" property="issueList">
							<ol style="text-align:left;">
							<logic:iterate id="resultBean" name="dateBean" property="issueList" type="com.premiersolutionshi.old.bean.SupportBean">
								<li>
								<logic:notEmpty name="resultBean" property="shipName">
									<b><bean:write name="resultBean" property="shipName"/></b>
								</logic:notEmpty>
								<logic:empty name="resultBean" property="shipName">
									<i>Non-ship specific</i>
								</logic:empty>
								- <bean:write name="resultBean" property="title"/>

								<logic:notEmpty name="resultBean" property="resolution">
									- <span style="color:green;"><bean:write name="resultBean" property="resolution"/></span>
								</logic:notEmpty>

								<span style="color:#777;">(Opened on <bean:write name="resultBean" property="openedDate"/>)</span>
								</li>
							</logic:iterate>
							</ol>
						</logic:present>

						<logic:notPresent name="dateBean" property="issueList">
							<ul style="text-align:left;"><li><i>None</i></li></ul>
						</logic:notPresent>
						</p>
					</logic:iterate>
				</logic:notEqual>

			</div>
		</div>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="js/d3.min.js"></script>

<script type="text/javascript" src="js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.pointLabels.min.js"></script>

<script type="text/javascript">
$("#tabs").tabs();

$(document).ready(function () {
	var s1 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value3"/></logic:iterate>];
	var s2 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value"/></logic:iterate>];
	var s3 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value2"/></logic:iterate>];

	// Can specify a custom tick Array.
	// Ticks should match up one for each y value (category) in the series.
	var ticks = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %>'<bean:write name="chartBean" property="label"/>'</logic:iterate>];

	var plot1 = $.jqplot('jqplot-canvas', [s1, s2, s3], {
		title: 'FACET Support Issues By Month',
		captureRightClick: true,
		seriesDefaults:{
			renderer:$.jqplot.BarRenderer,
			rendererOptions: {
				barMargin: 30,
				fillToZero: true
			},
			pointLabels: {
				show: true
			}
		},
		series:[
			{ color: '#579575', label: 'Total Issues' },
			{ color: '#4bb2c5', label: 'Closed Issues' },
			{ color: '#eaa228', label: 'Open Issues' }
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

	var startYear = 2013,
			endYear = (new Date()).getFullYear(),
			width = 950,
			height = 140*(endYear-startYear+1);

		var weekdays = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'],
			month = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

	var format = d3.time.format("%m/%d/%Y");

	function monthPath(t0) {
		var t1 = new Date(t0.getFullYear(), t0.getMonth() + 1, 0),
				d0 = t0.getDay(), w0 = d3.time.weekOfYear(t0),
				d1 = t1.getDay(), w1 = d3.time.weekOfYear(t1);
			return 'M' + (w0 + 1) * sizeByDay + ',' + d0 * sizeByDay + 'H' + w0 * sizeByDay + 'V' + 7 * sizeByDay + 'H' + w1 * sizeByDay + 'V' + (d1 + 1) * sizeByDay + 'H' + (w1 + 1) * sizeByDay + 'V' + 0 + 'H' + (w0 + 1) * sizeByDay + 'Z';
	}

	var color = d3.scale.quantize()
			.domain([0, 110])
			.range(d3.range(11).map(function(d) { return "q" + d + "-11"; }));

	var margin = {top: 20, right: 30, bottom: 20, left: 20};
	// update width and height to use margins for axis
	width = width - margin.left - margin.right;
	height = height - margin.top - margin.bottom;

	var years = d3.range(startYear, endYear+1).reverse(),
			sizeByYear = height/years.length+1,
			sizeByDay = d3.min([sizeByYear/8,width/54]);

	//Create the chart
	var svg = d3.select("#d3-heat-calendar")
		.append("svg")
			.attr({
				 class: 'heatmap',
				 width: width + margin.left + margin.right,
				 height: height + margin.top + margin.bottom
			})
		.append("g")
			.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

	var year = svg.selectAll('.year')
			.data(years)
			.enter().append('g')
					.attr('class', 'year')
					.attr('transform', function(d,i) { return 'translate(30,' + i * sizeByYear + ')'; });

	year.append('text')
			.attr({
					class: 'year-title',
					transform: 'translate(-38,' + sizeByDay * 3.5 + ')rotate(-90)',
					'text-anchor': 'middle',
					'font-weight': 'bold'
			})
			.text(function(d) { return d; });

	var rect = year.selectAll('.day')
				.data(function(d) { return d3.time.days(new Date(d, 0, 1), new Date(d + 1, 0, 1)); })
			.enter().append('rect')
					.attr({
							class: 'day',
							width: sizeByDay,
							height: sizeByDay,
							x: function(d) { return d3.time.weekOfYear(d) * sizeByDay; },
							y: function(d) { return d.getDay() * sizeByDay; }
					})
			.datum(format);

	year.selectAll('.month')
			.data(function (d) { return d3.time.months(new Date(d, 0, 1), new Date(d + 1, 0, 1)); })
			.enter().append('path')
					.attr({
							class: 'month',
							d: monthPath
					});

	// day and week titles
	var chartTitles = (function() {
		var titlesDays = svg.selectAll('.year')
			.selectAll('.titles-day')
			.data(weekdays)
			.enter().append('g')
			.attr('class', 'titles-day')
			.attr('transform', function (d, i) {
				return 'translate(-5,' + sizeByDay*(i+1) + ')';
			});

		titlesDays.append('text')
			.attr('class', function (d,i) { return weekdays[i]; })
			.style('text-anchor', 'end')
			.attr('dy', '-.25em')
			.text(function (d, i) { return weekdays[i]; });

		var titlesMonth = svg.selectAll('.year')
			.selectAll('.titles-month')
				.data(month)
			.enter().append('g')
				.attr('class', 'titles-month')
				.attr('transform', function (d, i) {
					return 'translate(' + (((i+1) * (width/12-(3 * i/12)))-30) + ',-5)';
				});

		titlesMonth.append('text')
			.attr('class', function (d,i) { return month[i]; })
			.style('text-anchor', 'end')
			.text(function (d,i) { return month[i]; });
	})();

	//  Tooltip Object
	var tooltip = d3.select("body")
		.append("div").attr("id", "tooltip")
		.style("position", "absolute")
		.style("z-index", "10")
		.style("visibility", "hidden")
		.text("a simple tooltip");

	$.ajax({
		url:  'ajax.do',
		type: 'POST',
		data: { action: 'getIssueSummaryJson', projectPk: <bean:write name="projectPk"/> },
		dataType: 'json',
		success: function(data) {
			nestedData = d3.nest()
					.key(function (d) { return d.date; })
					.rollup(function (n) {
							return d3.sum(n, function (d) {
									return d.value; // key
							});
					})
					.map(data);

			rect.filter(function(d) { return d in nestedData; })
					.attr("class", function(d) { return "day " + color(nestedData[d]); })

			//  Tooltip
			rect.on("mouseover", mouseover);
			rect.on("mouseout", mouseout);

			function mouseover(d) {
				tooltip.style("visibility", "visible");
				var purchase_text = weekdays[new Date(d).getDay()] + " " + d + ": " + ((nestedData[d] !== undefined) ? nestedData[d] : 0) + " count";

				tooltip.transition()
										.duration(200)
										.style("opacity", .9);
				tooltip.html(purchase_text)
										.style("left", (d3.event.pageX)+30 + "px")
										.style("top", (d3.event.pageY) + "px");
			} //end of mouseover

			function mouseout (d) {
				tooltip.transition()
								.duration(500)
								.style("opacity", 0);
				var $tooltip = $("#tooltip");
				$tooltip.empty();
			} //end of mouseout
		},
		error: function(e) {
		}
	});
});
</script>

</body>
</html>
