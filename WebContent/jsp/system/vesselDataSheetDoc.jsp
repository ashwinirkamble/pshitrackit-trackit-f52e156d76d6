<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Installation Checklist"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="systemBean" 		scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>
<jsp:useBean id="backfileBean" 	scope="request" class="com.premiersolutionshi.old.bean.BackfileBean"/>
<jsp:useBean id="trainingBean" 	scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-word");
	response.setHeader("Content-Disposition", "attachment;filename=vessel_data_sheet_" + systemBean.getUic() + ".doc");
%>

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
		font-weight: bold;
	}

	.headerTable td {
		font-size: 10pt;
		font-weight: normal;
	}

	.docTable {
		border-collapse: collapse;
	}

	.docTable td {
		border: 0;
		padding: 5px 0 10px 0;
		vertical-align: top;
		}

	.docTable td.fieldName {
		font-weight: bold;
		text-transform: uppercase;
		}

	.docTable td.lineBlock {
		border-bottom: 1px solid black;
		padding-bottom: 0;
		}

	td.fieldValue {
		font-weight: normal;
	}

	td.workflowBox {
		padding: 5px 3px;
		border: 1px solid black;
		font-size: 11px;
		font-weight: normal;
		width: 80px;
		text-align: center;
	}

	td.workflowValue {
		font-size: 14px;
		font-weight: normal;
		text-align: center;
		border: 1px solid white;
		border-bottom: 1px solid black;
		width: 80px;
	}

	div.section {
		margin-left: 30px;
	}

	ul {
		padding-top: 0;
		padding-bottom: 0;
		margin-top: 0;
		margin-bottom: 0;
	}

	ul li {
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
<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="300" style="font-size:12pt;">PREMIER SOLUTIONS HI, LLC</td>
		<td width="420" style="font-size:16pt;text-align:right;">COMMAND DATA SHEET</td>
	</tr>
</tbody>
</table>

<table class="headerTable" border="0" cellspacing="0">
<tbody>
	<tr>
		<td width="140" style="font-weight:bold;">FACET SUPPORT</td>
		<td width="220">&nbsp;</td>
		<td width="360" rowspan="5" style="text-align:right;vertical-align:bottom;font-size:16pt;font-weight:bold;"><bean:write name="systemBean" property="shipName"/></td>
	</tr>
	<tr>
		<td>HAWAII OFFICE:</td>
		<td>(808) 396-4444</td>
	</tr>
	<tr>
		<td>VIRGINIA OFFICE:</td>
		<td>(757) 698-4005</td>
	</tr>
	<tr>
		<td>TOLL-FREE:</td>
		<td>(844) 283-9444</td>
	</tr>
	<tr>
		<td>EMAIL:</td>
		<td>support@premiersolutionshi.com</td>
	</tr>
</tbody>
</table>


<hr style="color:#777;background-color:#777;height:3px;"/>


<p align="left">
<b>TRAINING INFO:</b><br/>
<div class="section">
<table class="docTable" border="0" cellspacing="0">
<tbody>
	<tr>
		<td width="200">TRAINING DATE:</td>
		<td width="240" class="fieldValue">
			<% if (!CommonMethods.isEmpty(trainingBean.getSchedTrainingDate())) { %>
				<bean:write name="trainingBean" property="schedTrainingDate"/>
				<% if (!CommonMethods.isEmpty(trainingBean.getSchedTrainingTime())) { %>
					<bean:write name="trainingBean" property="schedTrainingTime"/>
				<% } %>
			<% } else { %>
				_________________
			<% } %>
		</td>
		<td width="100">TRAINER:</td>
		<td width="160" class="fieldValue">
			<% if (!CommonMethods.isEmpty(trainingBean.getTrainerFullName())) { %>
				<bean:write name="trainingBean" property="trainerFullName"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>TRAINING LOCATION:</td>
		<td class="fieldValue" colspan="3">
			<% if (!CommonMethods.isEmpty(trainingBean.getSchedTrainingLoc())) { %>
				<bean:write name="trainingBean" property="schedTrainingLoc"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>TRAINING POC:</td>
		<td class="fieldValue"></td>
		<td>POC TEL.:</td>
		<td class="fieldValue"></td>
	</tr>
</tbody>
</table>
</div>
</p>


<hr style="color:#777;background-color:#777;height:3px;"/>


<p align="left">
<b>SECTION 1: COMMAND</b><br/>
<div class="section">
<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="180">UNIT NAME:</td>
		<td width="290" class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getShipName())) { %>
				<bean:write name="systemBean" property="shipName"/>
			<% } else { %>
				--
			<% } %>
		</td>
		<td width="150" style="vertical-align:bottom;">LOGCOP ACCESS <br/> STATUS:</td>
		<td width="60" class="fieldValue" style="vertical-align:bottom;">Pending</td>
	</tr>
	<tr>
		<td>REGION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getHomeport())) { %>
				<bean:write name="systemBean" property="homeport"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>R-SUPPLY VERSION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getRsupply())) { %>
				<bean:write name="systemBean" property="rsupply"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
</tbody>
</table>
</div>
</p>

<hr style="color:#777;background-color:#777;height:3px;"/>

<p align="left">
<b>SECTION 2: ACCESS</b><br/>
<div class="section">
<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="280">BASE ACCESS	GRANTED ON:</td>
		<td class="fieldValue"></td>
	</tr>
	<tr>
		<td>BASE ACCESS GRANTED BY:</td>
		<td class="fieldValue"></td>
	</tr>

	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>

	<tr>
		<td>VESSEL ACCESS GRANTED ON:</td>
		<td class="fieldValue"></td>
	</tr>
	<tr>
		<td>VESSEL ACCESS GRANTED BY:</td>
		<td class="fieldValue"></td>
	</tr>
</tbody>
</table>
</div>
</p>

<hr style="color:#777;background-color:#777;height:3px;"/>

<p align="left">
<b>SECTION 3: BACKFILE INFO</b><br/>
<div class="section">
<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="220">FY15 BACKFILE STATUS:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(backfileBean.getFy15MailedDate())) { %>
				Completed
			<% } else if (!CommonMethods.isEmpty(backfileBean.getFy15CompletedDate())) { %>
				Completed
			<% } else if (!CommonMethods.isEmpty(backfileBean.getReceivedDate())) { %>
				Received
			<% } else { %>
				Pending
			<% } %>
		</td>
	</tr>
</tbody>
</table>

<table class="workflowTable" border="0" cellspacing="20">
<tbody>
	<tr>
		<td class="workflowBox">FY15 <br/> Backfile <br/> Received</td>
		<td class="workflowBox">FY15 <br/> Backfile <br/> Completed</td>
		<td class="workflowBox">FY15 <br/> Backfile to <br/> Vessel</td>
	</tr>
	<tr>
		<td class="workflowValue"><bean:write name="backfileBean" property="receivedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="fy15CompletedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="fy15MailedDate"/></td>
	</tr>
</tbody>
</table>

<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="220">FYNET BACKFILE STATUS:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(backfileBean.getFy1314MailedDate())) { %>
				Completed
			<% } else if (!CommonMethods.isEmpty(backfileBean.getFy1314CompletedDate())) { %>
				Completed
			<% } else if (!CommonMethods.isEmpty(backfileBean.getReceivedDate())) { %>
				Received
			<% } else { %>
				Pending
			<% } %>
		</td>
	</tr>
</tbody>
</table>

<table class="workflowTable" border="0" cellspacing="20">
<tbody>
	<tr>
		<td class="workflowBox">FYNet <br/> Backfile <br/> Received</td>
		<td class="workflowBox">FY14 <br/> Backfile <br/> Completed</td>
		<td class="workflowBox">FY13 <br/> Backfile <br/> Completed</td>
		<td class="workflowBox">FY13/14/15 <br/> Backfile to <br/> Vessel</td>
		<td class="workflowBox">FY13/14/15 <br/> Backfile to <br/> LOGCOP</td>
		<td class="workflowBox">FY13/14/15 <br/> Backfile to <br/> Laptop</td>
	</tr>
	<tr>
		<td class="workflowValue"><bean:write name="backfileBean" property="receivedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="fy1314CompletedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="fy1314CompletedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="fy1314MailedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="logcopUploadedDate"/></td>
		<td class="workflowValue"><bean:write name="backfileBean" property="laptopInstalledDate"/></td>
	</tr>
</tbody>
</table>
</div>
</p>

<hr style="color:#777;background-color:#777;height:3px;"/>

<p align="left">
<b>SECTION 4: EQUIPMENT INFO</b><br/>
<div class="section">

<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="220">COMPUTER NAME:</td>
		<td width="220" class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getComputerName())) { %>
				<bean:write name="systemBean" property="computerName"/>
			<% } else { %>
				--
			<% } %>
		</td>
		<td width="150">LAPTOP TAG:</td>
		<td width="110" class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getLaptopTag())) { %>
				<bean:write name="systemBean" property="laptopTag"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>COMPUTER MODEL:</td>
		<td class="fieldValue" colspan="3">
			<% if (!CommonMethods.isEmpty(systemBean.getLaptopProductName())) { %>
				<bean:write name="systemBean" property="laptopProductName"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>SERIAL NO:</td>
		<td class="fieldValue" colspan="3">
			<% if (!CommonMethods.isEmpty(systemBean.getLaptopSerialNumber())) { %>
				<bean:write name="systemBean" property="laptopSerialNumber"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>SCANNER MODEL:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getScannerProductName())) { %>
				<bean:write name="systemBean" property="scannerProductName"/>
			<% } else { %>
				--
			<% } %>
		</td>
		<td>SCANNER TAG:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getScannerTag())) { %>
				<bean:write name="systemBean" property="scannerTag"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td>SERIAL NO.:</td>
		<td class="fieldValue" colspan="3">
			<% if (!CommonMethods.isEmpty(systemBean.getScannerSerialNumber())) { %>
				<bean:write name="systemBean" property="scannerSerialNumber"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
</tbody>
</table><br/>

<hr style="color:#777;background-color:#777;height:1px;"/><br/>

<table class="border-zero cellspacing-zero">
<tbody>
	<tr>
		<td width="320">MICROSOFT:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getMsOfficeProductName())) { %>
				<bean:write name="systemBean" property="msOfficeProductName"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td style="padding-left:30px;">ACCESS VERSION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getAccessVersion())) { %>
				<bean:write name="systemBean" property="accessVersion"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>KOFAX:
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getKofaxProductName())) { %>
				<bean:write name="systemBean" property="kofaxProductName"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td style="padding-left:30px;">KOFAX SERIAL NO.:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getKofaxLicenseKey())) { %>
				<bean:write name="systemBean" property="kofaxLicenseKey"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td style="padding-left:30px;">KOFAX PRODUCT CODE:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getKofaxProductCode())) { %>
				<bean:write name="systemBean" property="kofaxProductCode"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>
	<tr>
		<td style="padding-left:30px;">KOFAX BATCH CLASS VERSION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getKofaxVersion())) { %>
				<bean:write name="systemBean" property="kofaxVersion"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>FACET VERSION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getFacetVersion())) { %>
				<bean:write name="systemBean" property="facetVersion"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>DOCUMENTATION VERSION:</td>
		<td class="fieldValue">
			<% if (!CommonMethods.isEmpty(systemBean.getDocumentationVersion())) { %>
				<bean:write name="systemBean" property="documentationVersion"/>
			<% } else { %>
				--
			<% } %>
		</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td style="vertical-align: top;">NOTES:</td>
		<td class="fieldValue" style="vertical-align: top;">
			<% if (!CommonMethods.isEmpty(systemBean.getNotes())) { %>
				<logic:iterate id="str" name="systemBean" property="notesBr" indexId="i">
					<bean:write name="str"/><% if (i+1 < systemBean.getNotesBr().size()) { %><br/><% } %>
				</logic:iterate>
			<% } else { %>
				--
			<% } %>

		</td>
	</tr>
</tbody>
</table>
</div>
</p>

<hr style="color:#777;background-color:#777;height:3px;"/>

<p align="left">
I acknowledge receipt of the FACET equipment and all components listed above from Premier Solutions HI, LLC.<br/><br/>

<table class="docTable" cellspacing="0">
<tbody>
	<tr>
		<td width="100" class="fieldName">POC NAME:</td>
		<td width="310" class="lineBlock">&nbsp;</td>
		<td width="50">&nbsp;</td>
		<td width="55" class="fieldName">DATE:</td>
		<td width="185" class="lineBlock">&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="fieldName">SIGNATURE:</td>
		<td class="lineBlock">&nbsp;</td>
	</tr>
</tbody>
</table>
</p>

</div>
</body>
</html>
