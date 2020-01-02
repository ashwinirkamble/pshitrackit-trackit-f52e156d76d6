<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="currDmsVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currOsVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<bean:define id="pageTitle" value="${action == 'copy' ? 'Copying' : issueForm.id == null ? 'Add' : 'Edit'} Support Issue${issueForm.id == null ? '' : ' #'.concat(issueForm.id)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<logic:present name="project"><logic:notEmpty name="project">

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentUrl="./support.do?action=${pageFrom}&projectPk=${projectPk}"
  parentTitle="Issue List${pageFrom.equals('issueListAll') ? ' (All)' : ''}"/>

<logic:notEmpty name="issueForm" property="id">
  <p class="center">
    <logic:notEqual name="action" value="copy">
    <a href="issue.do?action=${action == 'edit' ? 'view' : 'edit'}&id=${issueForm.id}&projectPk=${projectPk}&pageFrom=${pageFrom}">
      <i class="material-icons" style="font-size: 13px;">${action == 'edit' ? 'pageview' : 'edit'}</i>
      ${action == 'edit' ? 'View' : 'Edit'} Issue #${issueForm.id}
    </a>
    <br/>
    </logic:notEqual>
    <%@ include file="../../include/support/issue/jumpToIssuePk.jsp" %>
  </p>
</logic:notEmpty>

<html:form action="issue.do" styleId="issueForm" onsubmit="return valFields();" style="width:740px"
  method="POST" enctype="multipart/form-data">
  <input type="hidden" name="projectFk" value="${projectPk}"/>
  <input type="hidden" name="pageFrom" value="${pageFrom}"/>
  <input type="hidden" name="action" value="save"/>
  <html:hidden name="issueForm" property="id"/>
  <html:hidden name="issueForm" property="atoFk"/>
  <html:hidden name="issueForm" property="phase"/>
  <html:hidden name="issueForm" property="createdBy"/>
  <html:hidden name="issueForm" property="createdDateStr"/>
  <html:hidden name="issueForm" property="lastUpdatedBy"/>
  <table class="tantable" style="width:740px">
  <tbody>
    <tr id="priority-normal-banner"><th>Priority</th></tr>
    <tr id="priority-high-banner" style="display:none;"><th style="color:#fff;background:#d00;font-weight:bold;text-align:center;">High Priority</th></tr>
    <tr><td align="left">
      <table id="bootstrapFormTable" class="border-zero">
      <colgroup>
        <col width="120"/>
        <col width="130"/>
        <col width="200"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName">Priority:</td>
          <td>
            <label class="checkbox-inline" style="color:#f00;font-weight:bold;">
              <html:checkbox name="issueForm" property="priority" value="High" styleId="priority"/>
              High Priority
            </label>
          </td>
          <td></td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName">Reason:</td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="issueForm" property="priorityReasonArr" styleId="laptop-issue" value="Laptop Down"/>
              Laptop Down
            </label>
          </td>
          <td>
            <html:select name="issueForm" property="laptopIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <html:options collection="laptopIssueList" property="itemValue"/>
            </html:select>
          </td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName"></td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="issueForm" property="priorityReasonArr" styleId="scanner-issue" value="Scanner Down"/>
              Scanner Down
            </label>
          </td>
          <td>
            <html:select name="issueForm" property="scannerIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <html:options collection="scannerIssueList" property="itemValue"/>
            </html:select>
          </td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName"></td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="issueForm" property="priorityReasonArr" styleId="software-issue" value="Software Issue"/>
              Software Issue
            </label>
          </td>
          <td>
            <html:select name="issueForm" property="softwareIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <html:options collection="softwareIssueList" property="itemValue"/>
            </html:select>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <tr><th>Unit Information</th></tr>
    <tr><td align="left">
      <table id="bootstrapFormTable" class="border-zero" style="width:100%;">
      <colgroup>
        <col width="120"/>
        <col width="130"/>
        <col width="450"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName">Unit:</td>
          <td colspan="2">
            <html:select name="issueForm" property="shipFk" styleId="shipPk" styleClass="form-control input-sm j255 j284 j269">
              <html:option value=""/>
              <logic:present name="shipList"><html:options collection="shipList" property="id" labelProperty="fullName"/></logic:present>
            </html:select>
          </td>
        </tr>

        <tr>
          <td class="fieldName">Configured System: <span id="configuredSystemCount"></span></td>
          <td colspan="2">
            <html:select name="issueForm" property="configuredSystemFk" styleId="configuredSystemFk" styleClass="form-control input-sm">
              <html:option value=""/>
            </html:select>
          </td>
        </tr>

<%-- <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> lo:</td>
       <td colspan="2">
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-1"/>
              S-1
            </label>
            
             <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-2"/>
              S-2
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-3"/>
              S-3
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-4"/>
              S-4
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-6"/>
              S-6
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="S-8"/>
              S-8
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="dept" value="N/A"/>
              N/A
            </label>
          </td>
</tr> --%>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Division:</td>
          <td colspan="2">
            <logic:iterate name="deptList" id="listItem" type="com.premiersolutionshi.common.domain.ManagedListItem">
              <label class="radio-inline">
                <html:radio name="issueForm" property="dept" value="${listItem.itemValue}"/>
                ${listItem.itemValue}
              </label>
            </logic:iterate>
          </td>
        </tr>
  
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Initiated By:</td>
          <td class="form-group">
            <label class="radio-inline">
              <html:radio name="issueForm" property="initiatedBy" value="PSHI"/>
              PSHI
            </label>
            <label class="radio-inline">
              <html:radio name="issueForm" property="initiatedBy" value="Unit"/>
              Unit
            </label>
          </td>
          <td>
            <label class="checkbox-inline">
              <html:checkbox name="issueForm" property="isEmailSent" value="Y" styleId="isEmailSent"/>
              PSHI Email Sent?
            </label>
            <label class="checkbox-inline emailResponseTd">
              <html:checkbox name="issueForm" property="isEmailResponded" value="Y" styleId="isEmailResponded"/>
              Unit Responded?
            </label>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <tr><th>Issue Summary</th></tr>
    <tr><td align="left">
      <table id="bootstrapFormTable" class="border-zero" style="width:100%;">
      <colgroup>
        <col width="120"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Title/Summary:</td>
          <td><html:text name="issueForm" property="title" maxlength="100" styleClass="form-control input-sm"/></td>
        </tr>
  
        <tr>
          <td class="fieldName TOP" style="padding-top:10px;">Description:</td>
          <td><html:textarea name="issueForm" property="description" rows="4" styleClass="form-control input-sm"/></td>
        </tr>
  
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Category:</td>
          <td>
            <html:select name="issueForm" property="issueCategoryFk" styleId="category" styleClass="form-control input-sm">
              <html:option value=""/>
              <html:options collection="issueCategoryList" property="id" labelProperty="category"/>
            </html:select>
          </td>
        </tr>

        <tr class="followup-training">
          <td></td>
          <td colspan="3" class="form-group">
            <span class="fieldName"><span id="followUpTrainingProvidedReq" class="regAsterisk">*</span> Follow-Up Training Provided?</span>
            &nbsp;
            <label class="radio-inline">
              <html:radio name="issueForm" property="isTrainingProvided" value="Y"/> Y
            </label>
            <label class="radio-inline">
              <html:radio name="issueForm" property="isTrainingProvided" value="N"/> N
            </label>
          </td>
        </tr>

        <tr class="followup-training followup-onsite">
          <td></td>
          <td colspan="3" class="form-group">
            <span class="fieldName">Follow-Up Training Onsite?</span>
            &nbsp;
            <label class="radio-inline">
              <html:radio name="issueForm" property="isTrainingOnsite" value="Y" styleId="isTrainingOnsite"/> Y
            </label>
            <label class="radio-inline">
              <html:radio name="issueForm" property="isTrainingOnsite" value="N" styleId="isTrainingOnsite"/> N
            </label>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <tr><th>Issue Information</th></tr>
    <tr><td align="left">
      <table class="border-zero cellspacing-zero cellpadding-3">
      <colgroup>
        <col width="120"/>
        <col width="230"/>
        <col width="100"/>
        <col width="100"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName">Person Assigned:</td>
          <td>
            <html:select name="issueForm" property="currPersonAssigned" styleClass="form-control input-sm" onchange="handleAddNewSelect(this, 'personAssigned');">
              <html:options collection="supportTeamList" name="listItem" property="itemValue" />
            </html:select>
          </td>
          <td colspan="2"><html:text name="issueForm" property="personAssigned" styleId="personAssigned" style="display:none;" styleClass="form-control input-sm"/></td>
        </tr>
        <tr>
          <td class="fieldName">
            <logic:empty name="issueForm" property="id">
              <span class="regAsterisk">*</span>
            </logic:empty>
            Opened By:
          </td>
          <td>
            <logic:empty name="issueForm" property="id">
              <html:text name="issueForm" property="openedBy" maxlength="75" styleId="openedBy" styleClass="form-control input-sm"/>
            </logic:empty>
            <logic:notEmpty name="issueForm" property="id">
              ${issueForm.openedBy}
              <html:hidden name="issueForm" property="openedBy" />
            </logic:notEmpty>
          </td>
          
          <td class="fieldName">
            <logic:empty name="issueForm" property="id">
              <span class="regAsterisk">*</span>
            </logic:empty>
            Opened Date:
          </td>
          <td>
            <logic:empty name="issueForm" property="id">
              <html:text name="issueForm" property="openedDateStr" styleClass="form-control input-sm datepicker"/>
            </logic:empty>
            <logic:notEmpty name="issueForm" property="id">
              ${issueForm.openedDateStr}
              <html:hidden name="issueForm" property="openedDateStr" />
            </logic:notEmpty>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <tr><th>Status</th></tr>
    <tr><td align="left">
      <table class="border-zero cellspacing-zero cellpadding-3">
      <colgroup><col width="120"/></colgroup>
      <tbody>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Status:</td>
          <td>
            <html:select name="issueForm" property="status" styleId="status" styleClass="form-control input-sm">
              <html:option value="1 - New"/>
              <html:option value="2 - Active"/>
              <logic:equal name="issueForm" property="status" value="3 - Resolved">
                 <html:option value="3 - Resolved"/>
              </logic:equal>
              <html:option value="4 - Pending Possible Resolution"/>
              <logic:equal name="issueForm" property="status" value="5 - Closed">
                <html:option value="5 - Closed"/>
              </logic:equal>
              <html:option value="6 - Closed (Successful)"/>
              <html:option value="7 - Closed (No Response)"/>
              <html:option value="8 - Closed (Unavailable)"/>
            </html:select>
          </td>
          <td class="closedDateTd fieldName"><span class="regAsterisk">*</span> Closed Date:</td>
          <td class="closedDateTd"><html:text name="issueForm" property="closedDateStr" styleClass="form-control input-sm datepicker"/></td>
        </tr>
        <tr id="resolution-row">
          <td class="fieldName"><span class="regAsterisk">*</span> Resolution:</td>
          <td><html:textarea name="issueForm" property="resolution" rows="5" styleClass="form-control input-sm"/></td>
          <td class="fieldName"><span class="regAsterisk">*</span> Total Time:</td>
          <td>
            <html:select name="issueForm" property="totalTime" styleClass="form-control input-sm">
              <html:option value=""/>
              <html:options collection="totalTimeList" property="value" labelProperty="label" />
            </html:select>
          </td>
        </tr>
        <tr class="auto-close-section">
          <td class="auto-close-info fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close On:</td>
          <td class="auto-close-info"><html:text name="issueForm" property="autoCloseDateStr" styleClass="form-control input-sm datepicker"/></td>
          <td class="auto-close-info fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close To:</td>
          <td class="auto-close-info">
            <html:select name="issueForm" property="autoCloseStatus" styleClass="form-control input-sm">
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

    <tr class="close-related-issues"><th>Close Other Ship Issues</th></tr>
    <tr class="close-related-issues"><td align="left" id="related-issues">
    </td></tr>
  
    <tr class="support-visit-section"><th>On-Site Support Visit</th></tr>
    <tr class="support-visit-section"><td align="left">
      <table class="border-zero cellspacing-zero cellpadding-3">
      <colgroup>
        <col width="120"/>
        <col width="100"/>
        <col width="110"/>
        <col width="60"/>
        <col width="10"/>
        <col width="60"/>
        <col width="75"/>
        <col width="185"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName"><span id="supportVisitDateReq" class="regAsterisk" style="display:none;">*</span> Scheduled Date:</td>
          <td><html:text name="issueForm" property="supportVisitDateStr" styleId="supportVisitDate" styleClass="form-control input-sm datepicker"/></td>
          <td class="support-visit-info fieldName">Time(HHMM):</td>
          <td class="support-visit-info"><html:text name="issueForm" property="supportVisitTime" maxlength="4" styleClass="form-control input-sm" style="text-align:center;"/></td>
          <td class="support-visit-info" align="center">-</td>
          <td class="support-visit-info"><html:text name="issueForm" property="supportVisitEndTime" maxlength="4" styleClass="form-control input-sm" style="text-align:center;"/></td>
          <td class="support-visit-info fieldName">Reason:</td>
          <td class="support-visit-info">
            <html:select name="issueForm" property="supportVisitReason" styleClass="form-control input-sm">
              <html:option value="Follow-Up Training"/>
              <html:option value="Support"/>
              <html:option value="Training/Support"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="support-visit-info fieldName">Trainer:</td>
          <td class="support-visit-info" colspan="2">
            <html:select name="issueForm" property="currTrainer" styleClass="form-control input-sm" onchange="handleAddNewSelect(this, 'trainer');">
              <html:options collection="supportTeamList" property="itemValue" />
            </html:select>
          </td>
          <td class="support-visit-info" colspan="3"><html:text name="issueForm" property="trainer" styleId="trainer" style="display:none;" styleClass="form-control input-sm"/></td>
        </tr>
        <tr>
          <td class="support-visit-info fieldName"><span class="regAsterisk">*</span> Location:</td>
          <td class="support-visit-info" colspan="2">
            <html:select name="issueForm" property="supportVisitLoc" styleClass="form-control input-sm">
              <html:option value=""/>
              <html:options collection="locationList" property="itemValue"/>
            </html:select>
          </td>
  
        </tr>
        <tr>
          <td class="support-visit-info fieldName">Location Notes:</td>
          <td class="support-visit-info" colspan="7"><html:text name="issueForm" property="supportVisitLocNotes" styleClass="form-control input-sm"/></td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <%@ include file="../../include/issue/issueAdminButtons.jsp" %>

    <%@ include file="../../include/issue/issueCommentsDisplay.jsp" %>

    <%@ include file="../../include/issue/issueFilesDisplay.jsp" %>

    <%@ include file="../../include/issue/issueAdminButtons.jsp" %>
    </tbody>
  </table>
</html:form>

</logic:notEmpty></logic:present>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js?build=${buildNumber}"></script>
<script type="text/javascript" src="js/valdate.js?build=${buildNumber}"></script>
<script type="text/javascript" src="js/support/issueAdmin.js?build=${buildNumber}"></script>

<script type="text/javascript">
var projectPk = '${projectPk}';
var pageFrom = '${pageFrom}';
var editType = '${editType}';
var shipPk = '${issueForm.shipFk}';
var originalShipPk = '${issueForm.shipFk}';
var configuredSystemFk = "${issueForm.configuredSystemFk}";
var issuePk = '${issueForm.id}';
var userFullName = '${loginBean.fullName}';

$(function() {
  <logic:iterate id="homeport" name="homeportList" type="java.lang.String">homeportList.push('${homeport}');
  </logic:iterate>
  <logic:iterate name="supportTeamList" id="listItem" type="com.premiersolutionshi.common.domain.ManagedListItem">openedByList.push('${listItem.itemValue}');
  </logic:iterate>
  <logic:iterate name="configuredSystemList" id="cs" type="com.premiersolutionshi.support.domain.ConfiguredSystem">addShipFkToCsListMap('${cs.shipFk}', { id:'${cs.id}', computerName:'${cs.computerName}' });
  </logic:iterate>
  $('#shipPk').trigger('change');
});
</script>
</body>
</html>
