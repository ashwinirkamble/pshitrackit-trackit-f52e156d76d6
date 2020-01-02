<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="ATO List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>

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

		<p align="center">
		<a href="support.do?action=atoAdd&projectPk=<bean:write name="projectPk"/>">Add New ATO</a>
		</p>

		<p align="center">
		<table id="tanTable_style2" class="alt-color" style="border:0; margin:0 auto;">
		<thead>
			<tr>
				<th>ATO Name</th>
				<th>Opened</th>
				<th>Total Affected</th>
				<th># Applied</th>
				<th>Last Updated</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
		<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SupportBean">
			<tr>
				<td><a href="support.do?action=atoEdit&atoPk=<bean:write name="resultBean" property="atoPk"/>&projectPk=<bean:write name="projectPk"/>">ATOUpdates_<bean:write name="resultBean" property="atoFilename"/></a></td>
				<td align="center"><bean:write name="resultBean" property="openedDate"/></td>
				<td align="center"><bean:write name="resultBean" property="totalCnt"/></td>
				<td align="center"><bean:write name="resultBean" property="appliedCnt"/></td>
				<td align="center"><bean:write name="resultBean" property="lastUpdatedDate"/></td>
				<td align="center">
					<a href="support.do?action=atoEdit&atoPk=<bean:write name="resultBean" property="atoPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit"/></a>
					&nbsp;<a href="support.do?action=atoDeleteDo&atoPk=<bean:write name="resultBean" property="atoPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="atoFilename"/>');"><img src="images/icon_delete.png" title="Delete"/></a>
				</td>
			</tr>
		</logic:iterate>
		</tbody>
		</table>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	function confirmDelete(atoFilename) {
		return confirm("Are you sure you want to delete " + atoFilename);
	} //end of confirmDelete
</script>

</body>
</html>
