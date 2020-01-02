<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Bulk E-mail Tool"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="bulkIssueForm" scope="request" class="com.premiersolutionshi.support.ui.form.BulkIssueForm"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currOsVersion"    scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"   scope="request" class="java.lang.String"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"/>

<html:form action="bulkIssue.do?projectPk=${projectPk}" method="POST" style="width:1145px;"> 
  <input type="hidden" name="action" value="preview"/>
  <input type="hidden" name="projectFk" value="${projectPk}"/>

  <table id="tanTable_style2" class="border-zero cellspacing-zero">
    <tbody>
      <tr><th>General Information</th></tr>
      <tr>
        <td class="nobordered" align="left">
          <table class="border-zero cellspacing-zero cellpadding-3">
            <colgroup>
              <col width="120" />
            </colgroup>
            <tbody>
              <tr>
                <td class="fieldName"><span class="regAsterisk">*</span> Category:</td>
                <td>
                  <html:select name="bulkIssueForm" property="categoryName" styleId="category" styleClass="form-control input-sm">
                    <html:option value="Monthly E-Mail Notification"/>
                    <html:option value="Follow-Up Training"/>
                    <html:option value="OS Update"/>
                    <html:option value="FACET Update"/>
                  </html:select>
                </td>
              </tr>
              <tr>
                <td class="fieldName"><span class="regAsterisk">*</span> Title/Summary:</td>
                <td><html:text name="bulkIssueForm" property="title" styleId="title"
                  styleClass="form-control input-sm" size="80" maxlength="100"/></td>
              </tr>
            </tbody>
          </table>
        </td>
      </tr>

      <tr><th>Detailed Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup>
          <col width="120"/>
          <col width="230"/>
          <col width="100"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Person Assigned:</td>
            <td>
              <html:select name="bulkIssueForm" property="personAssigned" styleClass="form-control input-sm">
                <logic:present name="supportTeamList"><html:options collection="supportTeamList" property="itemValue"/></logic:present>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Opened By:</td>
            <td><html:text name="bulkIssueForm" property="openedBy" styleId="openedBy" size="25" maxlength="75"
              styleClass="form-control input-sm"/></td>
            <td class="fieldName"><span class="regAsterisk">*</span> Opened Date:</td>
            <td>
              <html:text name="bulkIssueForm" property="openedDateStr" styleId="openedDate" styleClass="datepicker form-control input-sm" size="9"/>
            </td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Current Status</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col width="120"/></colgroup>
        <tbody>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Status:</td>
            <td>
              <html:select name="bulkIssueForm" property="status" styleId="status" styleClass="form-control input-sm">
                <html:option value="1 - New"/>
                <html:option value="2 - Active"/>
                <html:option value="3 - Resolved"/>
                <html:option value="4 - Pending Possible Resolution"/>
                <html:option value="6 - Closed (Successful)"/>
                <html:option value="7 - Closed (No Response)"/>
                <html:option value="8 - Closed (Unavailable)"/>
              </html:select>
            </td>
            <td class="closedDateTd fieldName">Closed Date:</td>
            <td class="closedDateTd">
              <html:text name="bulkIssueForm" property="closedDateStr" styleId="closedDate" styleClass="datepicker form-control input-sm" size="9"/>
            </td>
          </tr>
          <tr id="resolutionRow">
            <td class="fieldName"><span class="regAsterisk">*</span> Resolution:</td>
            <td><html:text name="bulkIssueForm" property="resolution" styleId="resolution"
              styleClass="form-control input-sm" size="40" maxlength="50"/></td>
            <td class="fieldName">Total Time:</td>
            <td>
              <html:select name="bulkIssueForm" property="totalTime" styleId="totalTime" styleClass="form-control input-sm">
                <html:option value="15">15 min</html:option>
                <html:option value="30">30 min</html:option>
                <html:option value="45">45 min</html:option>
                <html:option value="60">1 hour</html:option>
                <html:option value="90">1 hour 30 min</html:option>
                <html:option value="120">2 hours</html:option>
                <html:option value="150">2 hours 30 min</html:option>
                <html:option value="180">3 hours</html:option>
                <html:option value="210">3 hours 30 min</html:option>
                <html:option value="240">4 hours</html:option>
                <html:option value="270">4 hours 30 min</html:option>
                <html:option value="300">5 hours</html:option>
                <html:option value="330">5 hours 30 min</html:option>
                <html:option value="360">6 hours</html:option>
                <html:option value="390">6 hours 30 min</html:option>
                <html:option value="420">7 hours</html:option>
                <html:option value="450">7 hours 30 min</html:option>
                <html:option value="480">8 hours</html:option>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="autoCloseTd fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close On:</td>
            <td class="autoCloseTd">
              <html:text name="bulkIssueForm" property="autoCloseDateStr" size="9" styleClass="datepicker form-control input-sm"/>
            </td>
            <td class="autoCloseTd fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close To:</td>
            <td class="autoCloseTd">
              <html:select name="bulkIssueForm" property="autoCloseToStatus" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:option value="6 - Closed (Successful)"/>
                <html:option value="7 - Closed (No Response)"/>
                <html:option value="8 - Closed (Unavailable)"/>
              </html:select>
            </td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr class="emailBody"><th>E-Mail Body</th></tr>
      <tr class="emailBody"><td class="nobordered" align="center">
        <html:textarea styleId="commentsInput" name="bulkIssueForm" property="comments" rows="10" styleClass="form-control input-sm"/>
        <br/>
        Note: Do not use any &quot; character in the e-mail body as the e-mail may not be properly generated
      </td></tr>

      <tr><th>Select FACET Units</th></tr>

      <tr><td align="center"><html:submit value="Preview"/></td></tr>

      <tr><td class="nobordered" align="left">
        <table id="configuredSystemTable" class="display cellspacing-zero" style="width:1000px">
        <thead>
          <tr>
            <th>Unit</th>
            <th>Homeport</th>
            <th>Computer<br/>Name</th>
            <th>Location</th>
            <th>Outstanding Issues</th>
            <th style="width:60px;">Monthly<br/>E-Mail</th>
            <th style="width:60px;">E-Mail</th>
            <th>
              <div>Include</div>
              <div><input type="checkbox" id="selectAll" title="Select All" /></div>
              <a id="emailAllLink" class="emailBody" href="javascript:void(0);">E-Mail Selected</a>
            </th>
          </tr>
        </thead>
        <tbody>
          <% Integer currShipPk = -1; %>
          <logic:present name="configuredSystemList">
            <logic:iterate id="configuredSystem" name="configuredSystemList"
              type="com.premiersolutionshi.support.domain.ConfiguredSystemWithIssues">
              <bean:define id="ship" name="configuredSystem" property="ship" />
              <bean:define id="laptop" name="configuredSystem" property="laptop" />
              <tr style="vertical-align: top;" align="left">
                <td>
                  <span class="bulk-issue-shipname">${ship.fullName}</span>
                </td>
                <td>${ship.homeport}</td>
                <td><b>${laptop.computerName}</b></td>
                <td>${configuredSystem.location}</td>
                <td>
                  <logic:iterate id="outstandingIssue" type="java.lang.String" name="configuredSystem" property="outstandingIssueList" >
                    <span style="white-space: nowrap;"><img src="images/icon_error.gif"/> ${outstandingIssue}</span> <br/>
                  </logic:iterate>
                </td>
                <td align="center">
                  <a class="email-monthly-dialog-btn" title="Open E-mail Dialog">
                    <i class="material-icons">email</i>
                  </a>
                  <html:hidden name="configuredSystem" property="monthlyEmailMessage" />
                </td>
                <td align="center" style="width: 60px">
                  <a class="email-standard-dialog-btn" title="Open E-mail Dialog">
                    <i class="material-icons">email</i>
                  </a>
                </td>
                <td align="center">
                  <%-- <input type="checkbox" name="includeShipPkArr" value="${configuredSystem.shipFk}" /> --%>
                  <input type="checkbox" name="includeConfiguredSystemPkArr" value="${configuredSystem.id}" />
                  <input type="hidden" name="configuredSystemPk" value="${configuredSystem.id}" />
  
                  <html:hidden name="laptop" property="computerName" />
                  <html:hidden name="ship" property="shipName" />
                  <html:hidden name="ship" property="pocEmails" />
                  <html:hidden name="ship" property="primaryPocEmails" />
                  <input type="hidden" name="shipPkArr" value="${configuredSystem.shipFk}"/>
                </td>
              </tr>
              <% currShipPk = configuredSystem.getShipFk(); %>
            </logic:iterate>
          </logic:present>
        </tbody>
      </table>
    </td></tr>
    </tbody>
  </table>

  <div class="center">
    <html:submit value="Preview"/>
  </div>
</html:form>
<%@ include file="../../layout/old/layout-footer.jsp" %>

<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="shipPopup" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <iframe src="" width="900" height="600"></iframe>
    </div>
  </div>
</div>

<tags:emailDialog />

<script type="text/javascript">
var openedByList = [];
<logic:iterate id="item" name="supportTeamList" type="com.premiersolutionshi.common.domain.ManagedListItem">
  openedByList.push("${item.itemValue}");
</logic:iterate>
var currOsVersion = "${currOsVersion}";
var currFacetVersion = "${currFacetVersion}";
var fullName = '${loginBean.fullName}';
</script>
<script type="text/javascript" src="js/support/includeConfiguredSystemPkArr.js"></script>
<script type="text/javascript" src="js/support/bulkIssue.js"></script>

