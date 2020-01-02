<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Training Workflow Chart"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

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
