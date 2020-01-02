<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Training Workflow List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>
<jsp:useBean id="inProdList"	 		scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="unschedList"	 		scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="completedList"	 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="overdueList"			scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contractNumberList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="sortBy"    	 		scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir"   	 		scope="request" class="java.lang.String"/>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link type="text/css" href="css/bootstrap.min.css"/>
  <link type="text/css" href="css/stylesheet.css" rel="stylesheet"/>
  <style>
body {
  position: relative;
  font: 10px sans-serif;
}

.main section {
  margin-bottom:20px;
}

.bs-docs-sidebar .nav > li > a:focus, .bs-docs-sidebar .nav > li > a:hover {
    padding-left: 19px;
    color: #563D7C;
    text-decoration: none;
    background-color: transparent;
    border-left: 1px solid #563D7C;
}

.bs-docs-sidebar .nav > li > a {
    display: block;
    padding: 4px 20px;
    font-size: 13px;
    font-weight: 500;
    color: #767676;
    text-align: left;
}

.bs-docs-sidebar .nav > .active:focus > a, .bs-docs-sidebar .nav > .active:hover > a, .bs-docs-sidebar .nav > .active > a {
    padding-left: 18px;
    font-weight: 700;
    color: #563D7C;
    background-color: transparent;
    border-left: 2px solid #563D7C;
}

.axis path,
.axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.x.axis path {
  display: none;
}

.d3-tip {
  line-height: 1;
  font-weight: bold;
  padding: 12px;
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  border-radius: 2px;
}

/* Creates a small triangle extender for the tooltip */
.d3-tip:after {
  box-sizing: border-box;
  display: inline;
  font-size: 10px;
  width: 100%;
  line-height: 1;
  color: rgba(0, 0, 0, 0.8);
  content: "\25BC";
  position: absolute;
  text-align: center;
}

/* Style northward tooltips differently */
.d3-tip.n:after {
  margin: -1px 0 0 0;
  top: 100%;
  left: 0;
}
  </style>
</head>

<body data-spy="scroll" data-target="#navbar-example">
<div class="container">
	<div id="navbar-example" class="row">
		<div class="col-md-9 main">
			<section id="one">
					 Section 1
			</section>
			<section id="two">
					 Section 2
			</section>
			<section id="three">
					 Section 3
			</section>
			<section id="sec_trainingBarChart">
				<h2>Training Bar Chart</h2>
				<div id="trainingBarChart"></div>
				<button class="btn btn-primary" id="refreshBtn"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Refresh</button>
			</section>
			<section id="sec_issuePieChart">
				<h2>FACET Support Issues By Category Pie Chart</h2>
				<div align="center" id="issuePieChart"></div>
				<div id="issuePieChartErr" class="alert alert-danger" role="alert" style="display:none;"></div>

				<div class="btn-group" data-toggle="buttons">
					<label class="btn btn-default active">
						<input type="radio" name="daysBack" value="30" checked="true"/> Last 30 Days
					</label>
					<label class="btn btn-default selected">
						<input type="radio" name="daysBack" value="60"/> Last 60 Days
					</label>
					<label class="btn btn-default">
						<input type="radio" name="daysBack" value="90" /> Last 90 Days
					</label>
					<label class="btn btn-default">
						<input type="radio" name="daysBack" value="120" /> Last 120 Days
					</label>
				</div>
			</section>
		</div>
		<div class="col-md-3">
			<nav id="side-nav" class="col-sm-3 col-md-2 bs-docs-sidebar">
				<ul class="nav bs-docs-sidenav affix">
					<li><a href="#one">One</a></li>
					<li><a href="#two">Two</a></li>
					<li><a href="#three">Three</a></li>
					<li><a href="#sec_trainingBarChart">Training Bar Chart</a></li>
					<li><a href="#sec_issuePieChart">FACET Support Issues By Category Pie Chart</a></li>
				</ul>
			</nav>
		</div>
	</div>
</div>

<script type="text/javascript" src="js/javascript.js"></script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/d3.min.js"></script>
<script type="text/javascript" src="js/d3pie.min.js"></script>
<script type="text/javascript" src="js/dagre-d3.min.js"></script>
<script type="text/javascript" src="js/d3.tip.v0.6.3.js"></script>

<%@ include file="../include/report-trainingBarChart.jsp" %>
<%@ include file="../include/report-issuePieChart.jsp" %>

<script type="text/javascript">
	$(document).ready(function() {
		$('body').scrollspy({ target: '#navbar-example' })
		$.ajax({url: 'ajax.do?action=trainingBarChart&projectPk=1'});
	});
</script>

</body>
</html>