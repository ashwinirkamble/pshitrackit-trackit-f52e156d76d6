<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="FACET LAPTOP CONFIGURATION INFO"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultBean" scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-word");
	response.setHeader("Content-Disposition", "attachment;filename=facet_laptop_configuration_info_" + resultBean.getComputerName() + ".doc");
%>
FACET LaPtop Configuration Info
<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=windows-1252">
<meta name=ProgId content=Word.Document>
<meta name=Generator content="Microsoft Word 14">
<meta name=Originator content="Microsoft Word 14">
<style>
<!--
	body {
		font-family: Arial;
		font-size: 11pt;
	}


	#docTable {
		width: 680px;
		border-collapse: collapse;
		word-wrap: break-word;
		}

	#docTable td {
		border: 1px solid black;
		padding: 3px;
		}

	#docTable .header {
		font-size: 14pt;
		font-weight: bold;
		background: black;
		color: white;
		text-align: center;
	}

	#docTable .subHeader {
		background-color: #d9d9d9;
		font-weight: bold;
		text-align: center;
		}

	#subTable td {
		border: 0;
		}

	ul, ol {
		padding-top: 0;
		padding-bottom: 0;
		margin-top: 0;
		margin-bottom: 0;
	}

	ul li, ol li {
		mso-margin-top-alt: 0;
		mso-margin-bottom-alt: 0;
	}

	.numRow {
		font-weight: bold;
		text-align: center;
		vertical-align: top;
		}

	@page WordSection1
		{size:8.5in 11.0in;
		margin:.5in .5in .5in .5in;
		mso-header-margin:.5in;
		mso-footer-margin:.5in;
		mso-paper-source:0;}

div.WordSection1
	{page:WordSection1;}
-->
</style>
<!--[if gte mso 10]>
<style>
 /* Style Definitions */
 table.MsoNormalTable
	{mso-style-name:"Table Normal";
	mso-tstyle-rowband-size:0;
	mso-tstyle-colband-size:0;
	mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-parent:"";
	mso-padding-alt:0in 5.4pt 0in 5.4pt;
	mso-para-margin-top:0in;
	mso-para-margin-right:0in;
	mso-para-margin-bottom:10.0pt;
	mso-para-margin-left:0in;
	line-height:115%;
	mso-pagination:widow-orphan;
	font-size:11.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-ascii-theme-font:minor-latin;
	mso-hansi-font-family:Arial;
	mso-hansi-theme-font:minor-latin;}
</style>
<![endif]--><!--[if gte mso 9]><xml>
 <o:shapedefaults v:ext="edit" spidmax="1026"/>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <o:shapelayout v:ext="edit">
  <o:idmap v:ext="edit" data="1"/>
 </o:shapelayout></xml><![endif]-->
</head>

<body lang=EN-US style='tab-interval:.5in'>
<div class=WordSection1>
<p align="center" style="font-size:20pt;font-weight:bold;">
	<bean:write name="pageTitle"/>
</p>

<p align="center">
** THIS LIST SHOULD TRAVEL WITH THE LAPTOP **
</p>


<p align="center">
<center>
<table id="docTable" cellspacing="0">
<colgroup><col width="230"/><col width="170"/><col width="50"/></colgroup>
<tbody>
	<tr>
		<td width="230" style="font-weight:bold;">Unit Name</td>
		<td width="220" colspan="2"><bean:write name="resultBean" property="shipName"/></td>
	</tr>

	<tr>
		<td style="font-weight:bold;">System Configuration</td>
		<td colspan="2">
			<table id="subTable">
			<tbody>
			<tr>
				<td width="140">
					<logic:equal name="resultBean" property="multiShipInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="multiShipInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					Multi-Activity
				</td>
				<td>
					<logic:equal name="resultBean" property="nwcfInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="nwcfInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					Working Capital Fund
				</td>
			</tr>

			<tr>
				<td width="140">
					<logic:equal name="resultBean" property="form1348NoLocationInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="form1348NoLocationInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					1348 No Location
				</td>
				<td>
					<logic:equal name="resultBean" property="form1348NoClassInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="form1348NoClassInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					1348 No Class SL HAZMAT
				</td>
			</tr>
			</tbody>
			</table>
		</td>
	</tr>

	<tr>
		<td style="font-weight:bold;border-bottom:3px solid black;">RSupply Version</td>
		<td colspan="2" style="border-bottom:3px solid black;">
			<table id="subTable">
			<tbody>
				<tr>
					<td width="220">
						<% if (resultBean.getRsupply().equals("Viking") || resultBean.getRsupply().equals("Patriot")) { %>
							&#x2713;
						<% } else { %>
							<span style="font-size:17pt;">&#9633;</span>
						<% } %>
						Viking/Patriot
					</td>
					<td>
						<% if (resultBean.getRsupply().equals("CY04") || resultBean.getRsupply().equals("Charger")) { %>
							&#x2713;
						<% } else { %>
							<span style="font-size:17pt;">&#9633;</span>
						<% } %>
						CY04/Charger
					</td>
				</tr>
			</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Technician</td>
		<td colspan="2"/>
	</tr>
	<tr>
		<td style="font-weight:bold;">Date</td>
		<td colspan="2"/>
	</tr>
	<tr>
		<td style="font-weight:bold;border-bottom:3px solid black;">Ghost Version</td>
		<td colspan="2" style="border-bottom:3px solid black;"><bean:write name="resultBean" property="ghostVersion"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Computer Name</td>
		<td><bean:write name="resultBean" property="computerName"/></td>
		<td><b>Tag #</b> <bean:write name="resultBean" property="laptopTag"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Computer Serial #</td>
		<td colspan="2"><bean:write name="resultBean" property="laptopSerialNumber"/></td>
	</tr>

	<tr>
		<td style="font-weight:bold;">FACET Version</td>
		<td><bean:write name="resultBean" property="facetVersion"/></td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Kofax License/Product Code</td>
		<td>
			<% if (!CommonMethods.isEmpty(resultBean.getKofaxLicenseKey())) { %>
			<bean:write name="resultBean" property="kofaxLicenseKey"/>
			|
			<bean:write name="resultBean" property="kofaxProductCode"/>
			<% } %>
		</td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">VRS License</td>
		<td>
			<% if (!CommonMethods.isEmpty(resultBean.getVrsLicensePk()) && resultBean.getVrsLicensePk().equals("0")) { %>
				N/A
			<% } else if (!CommonMethods.isEmpty(resultBean.getVrsLicenseKey())) { %>
				<bean:write name="resultBean" property="vrsLicenseKey"/>
				|
				<bean:write name="resultBean" property="vrsProductCode"/>
			<% } %>
		</td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Microsoft License Key</td>
		<td colspan="2"><bean:write name="resultBean" property="msOfficeLicenseKey"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Admin Login Password (“Admin”<br/>or “facetadmin” user name)</td>
		<td><% if (!CommonMethods.isEmpty(resultBean.getAdminPassword())) { %><bean:write name="resultBean" property="adminPassword"/><% } else { %>Facetadmpword1348!<% } %></td>
		<td>&#9633; Verified</td>
	</tr>
</tbody>
</table>
</center>
</p><br>

<p align="center" style="font-size:16pt;font-weight:bold;">
** For laptop Ghost procedures, refer to the<br>
“final_laptop_prep_checklist_step001….docx” document.<br>
That checklist should be filled out accordingly and should<br>
stay with the laptop until it is delivered to the unit, but<br>
<span style="color:red;">DO NOT GIVE THAT CHECKLIST TO THE UNIT!</span> **
</p><br>

<p align="center" style="font-size:16pt;font-weight:bold;">
** If providing this system to a command right away,<br>
follow the procedures in the<br>
“trainer_laptop_checklist_step002.doc”<br>
document before deploying **
</p>

</div>
</body>
</html>
