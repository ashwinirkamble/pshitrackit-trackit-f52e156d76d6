<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Training Workflow Chart"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>
<jsp:useBean id="summaryByMonthList" scope="request" class="java.util.ArrayList"/>

<html>
<head>
  <title>TrackIT - <bean:write name="pageTitle"/></title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>

	<link href="css/jquery.jqplot.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div id="chartdiv" style="width:1024px;height:450px;"></div>
	<div id="chartimg"></div>

<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery.jqplot.min.js"></script>

<script type="text/javascript" src="js/jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript" src="js/jqplot/jqplot.pointLabels.min.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		var s1 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value3"/></logic:iterate>];
		var s2 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value"/></logic:iterate>];
		var s3 = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %><bean:write name="chartBean" property="value2"/></logic:iterate>];

		// Can specify a custom tick Array.
		// Ticks should match up one for each y value (category) in the series.
		var ticks = [<logic:iterate id="chartBean" name="summaryByMonthList" indexId="i"><% if (i > 0) { %>,<% } %>'<bean:write name="chartBean" property="label"/>'</logic:iterate>];

		var plot1 = $.jqplot('chartdiv', [s1, s2, s3], {
			title: 'FACET Support Issues By Month',
			captureRightClick: true,
			seriesDefaults:{
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					barMargin: 30,
					fillToZero: true
				},
				pointLabels: {
					show: true,
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

		//after creating your plot do
		var imgData = $('#chartdiv').jqplotToImageStr({}); // given the div id of your plot, get the img data
		var imgElem = $('<img/>').attr('src', imgData); // create an img and add the data to it
		imgElem.attr('name', 'something.png');
		$('#chartimg').append(imgElem);  // append the img to the DOM
		$('#chartdiv').hide();
	});
</script>

</body>
</html>
