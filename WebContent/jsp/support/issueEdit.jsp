<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Issue Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>
<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="configuredSystemAllList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="currStr"  scope="request" class="java.lang.String"/>
<jsp:useBean id="prevBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="nextBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="pageFrom" scope="request" class="java.lang.String"/>

<%@ include file="../layout/old/layout-header.jsp" %>

<% if (editType.equals("edit")) { %>
  <div class="center">
  <table id="borderlessTable" class="border-zero cellspacing-zero">
  <tbody>
    <tr>
      <td align="left" width="16" nowrap>
        <% if (prevBean != null && !CommonMethods.isEmpty(prevBean.getIssuePk())) { %>
          <a href="issue.do?action=edit&id=${prevBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}"><img src="images/arrow_left.png" height="16" width="16"/></a>
        <% } %>
      </td>
      <td align="center" nowrap>${currStr}</td>
      <td align="right" width="16" nowrap>
        <% if (nextBean != null && !CommonMethods.isEmpty(nextBean.getIssuePk())) { %>
          <a href="issue.do?action=edit&id=${nextBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}"><img src="images/arrow_right.png" height="16" width="16"/></a>
        <% } %>
      </td>
    </tr>
  </tbody>
  </table>
  </div>
<% } %>

<p align="center">
Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
</p>

<div class="center">
  <html:form action="support.do" styleId="issueForm" onsubmit="return valFields();" method="POST"
    enctype="multipart/form-data" style="width:740px;">
  <input type="hidden" name="projectPk" value="${projectPk}"/>
  <input type="hidden" name="pageFrom" value="${pageFrom}"/>
  <html:hidden name="inputBean" property="phase"/>
  <html:hidden name="inputBean" property="createdBy"/>
  <html:hidden name="inputBean" property="lastUpdatedBy"/>
  <% if (editType.equals("add") || editType.equals("copy") || editType.equals("edit")) { %>
    <input type="hidden" name="action" value="issueAddDo"/>
  <% } else { %>
    <input type="hidden" name="action" value="issueEditDo"/>
    <html:hidden name="inputBean" property="issuePk"/>
  <% } %>
  <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width:740px">
  <tbody>
    <tr id="priority-normal-banner"><th>Priority</th></tr>
    <tr id="priority-high-banner" style="display:none;"><th style="color:#fff;background:#d00;font-weight:bold;text-align:center;">High Priority</th></tr>
    <tr><td class="nobordered" align="left">
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
              <html:checkbox name="inputBean" property="priority" value="High" styleId="priority"/>
              High Priority
            </label>
          </td>
          <td></td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName">Reason:</td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="inputBean" property="priorityReasonArr" styleId="laptop-issue" value="Laptop Down"/>
              Laptop Down
            </label>
          </td>
          <td>
            <html:select name="inputBean" property="laptopIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <logic:present name="laptopIssueList"><html:options collection="laptopIssueList" property="value"/></logic:present>
            </html:select>
          </td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName"></td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="inputBean" property="priorityReasonArr" styleId="scanner-issue" value="Scanner Down"/>
              Scanner Down
            </label>
          </td>
          <td>
            <html:select name="inputBean" property="scannerIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <logic:present name="scannerIssueList"><html:options collection="scannerIssueList" property="value"/></logic:present>
            </html:select>
          </td>
        </tr>
  
        <tr class="priority-reason" style="height:45px;display:none;">
          <td class="fieldName"></td>
          <td>
            <label class="checkbox-inline">
              <html:multibox name="inputBean" property="priorityReasonArr" styleId="software-issue" value="Software Issue"/>
              Software Issue
            </label>
          </td>
          <td>
            <html:select name="inputBean" property="softwareIssue" styleClass="form-control input-sm" style="display:none;">
              <html:option value="" style="color:#aaa;">Select reason...</html:option>
              <logic:present name="softwareIssueList"><html:options collection="softwareIssueList" property="value"/></logic:present>
            </html:select>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>
  
  
    <tr><th>Unit Information</th></tr>
    <tr><td class="nobordered" align="left">
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
            <html:select name="inputBean" property="shipPk" styleId="shipPk" styleClass="form-control input-sm">
              <html:option value=""/>
              <logic:present name="shipList"><html:options collection="shipList" property="shipPk" labelProperty="shipNameTypeHull"/></logic:present>
            </html:select>
          </td>
        </tr>
  
        <tr>
          <td class="fieldName">Configured System:</td>
          <td colspan="2">
            <html:select name="inputBean" property="configuredSystemFk" styleId="configuredSystemFk" styleClass="form-control input-sm">
              <html:option value=""/>
            </html:select>
          </td>
        </tr>
  
        <%--
        <tr id="facetInfoTr" style="display:none;">
          <td class="fieldName">Computer Name:</td>
          <td colspan="2" id="facetInfoTd"></td>
        </tr>
         --%>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Division:</td>
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
        </tr>
  
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Initiated By:</td>
          <td class="form-group">
            <label class="radio-inline">
              <html:radio name="inputBean" property="initiatedBy" value="PSHI"/>
              PSHI
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="initiatedBy" value="Unit"/>
              Unit
            </label>
          </td>
          <td>
            <label class="checkbox-inline">
              <html:checkbox name="inputBean" property="isEmailSent" value="Y" styleId="isEmailSent"/>
              PSHI Email Sent?
            </label>
            <label class="checkbox-inline emailResponseTd">
              <html:checkbox name="inputBean" property="isEmailResponded" value="Y" styleId="isEmailResponded"/>
              Unit Responded?
            </label>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>
  
  
    <tr><th>Issue Summary</th></tr>
    <tr><td class="nobordered" align="left">
      <table id="bootstrapFormTable" class="border-zero" style="width:100%;">
      <colgroup>
        <col width="120"/>
      </colgroup>
      <tbody>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Title/Summary:</td>
          <td><html:text name="inputBean" property="title" maxlength="100" styleClass="form-control input-sm"/></td>
        </tr>
  
        <tr>
          <td class="fieldName TOP" style="padding-top:10px;">Description:</td>
          <td><html:textarea name="inputBean" property="description" rows="4" styleClass="form-control input-sm"/></td>
        </tr>
  
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Category:</td>
          <td>
            <html:select name="inputBean" property="issueCategoryFk" styleId="category" styleClass="form-control input-sm">
              <html:option value=""/>
              <logic:present name="categoryList"><html:options collection="categoryList" property="key" labelProperty="value"/></logic:present>
            </html:select>
          </td>
        </tr>
  
        <tr class="followup-training">
          <td></td>
          <td colspan="3" class="form-group">
            <span class="fieldName">Follow-Up Training Provided?</span>
            &nbsp;
            <label class="radio-inline">
              <html:radio name="inputBean" property="isTrainingProvided" value="Y"/> Y
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="isTrainingProvided" value="N"/> N
            </label>
          </td>
        </tr>
  
        <tr class="followup-training followup-onsite">
          <td></td>
          <td colspan="3" class="form-group">
            <span class="fieldName">Follow-Up Training Onsite?</span>
            &nbsp;
  
            <label class="radio-inline">
              <html:radio name="inputBean" property="isTrainingOnsite" value="Y" styleId="isTrainingOnsite"/> Y
            </label>
            <label class="radio-inline">
              <html:radio name="inputBean" property="isTrainingOnsite" value="N" styleId="isTrainingOnsite"/> N
            </label>
          </td>
        </tr>
      </tbody>
      </table>
    </td></tr>

    <tr><th>Issue Information</th></tr>
    <tr><td class="nobordered" align="left">
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
            <html:select name="inputBean" property="currPersonAssigned" styleClass="form-control input-sm" onchange="checkNew(this, 'personAssigned');">
              <logic:present name="supportTeamList"><html:options name="supportTeamList"/></logic:present>
              <html:option value="null">Add new...</html:option>
            </html:select>
          </td>
          <td colspan="2"><html:text name="inputBean" property="personAssigned" styleId="personAssigned" style="display:none;" styleClass="form-control input-sm"/></td>
        </tr>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Opened By:</td>
          <td><html:text name="inputBean" property="openedBy" maxlength="75" styleId="openedBy" styleClass="form-control input-sm"/></td>
          <td class="fieldName"><span class="regAsterisk">*</span> Opened Date:</td>
          <td><html:text name="inputBean" property="openedDate" styleClass="form-control input-sm datepicker"/></td>
        </tr>
      </tbody>
      </table>
    </td></tr>
  
  
    <tr><th>Status</th></tr>
    <tr><td class="nobordered" align="left">
      <table class="border-zero cellspacing-zero cellpadding-3">
      <colgroup><col width="120"/></colgroup>
      <tbody>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Status:</td>
          <td>
            <html:select name="inputBean" property="status" styleId="status" styleClass="form-control input-sm">
              <html:option value="1 - New"/>
              <html:option value="2 - Active"/>
              <logic:equal name="inputBean" property="status" value="3 - Resolved">
                 <html:option value="3 - Resolved"/>
              </logic:equal>
              <html:option value="4 - Pending Possible Resolution"/>
              <logic:equal name="inputBean" property="status" value="5 - Closed">
                <html:option value="5 - Closed"/>
              </logic:equal>
              <html:option value="6 - Closed (Successful)"/>
              <html:option value="7 - Closed (No Response)"/>
              <html:option value="8 - Closed (Unavailable)"/>
            </html:select>
          </td>
          <td class="closedDateTd fieldName"><span class="regAsterisk">*</span> Closed Date:</td>
          <td class="closedDateTd"><html:text name="inputBean" property="closedDate" styleClass="form-control input-sm datepicker"/></td>
        </tr>
        <tr id="resolution-row">
          <td class="fieldName"><span class="regAsterisk">*</span> Resolution:</td>
          <td><html:text name="inputBean" property="resolution" maxlength="50" styleClass="form-control input-sm"/></td>
          <td class="fieldName"><span class="regAsterisk">*</span> Total Time:</td>
          <td>
            <html:select name="inputBean" property="totalTime" styleClass="form-control input-sm">
              <html:option value=""/>
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
        <tr class="auto-close-section">
          <td class="auto-close-info fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close On:</td>
          <td class="auto-close-info"><html:text name="inputBean" property="autoCloseDate" styleClass="form-control input-sm datepicker"/></td>
          <td class="auto-close-info fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close To:</td>
          <td class="auto-close-info">
            <html:select name="inputBean" property="autoCloseStatus" styleClass="form-control input-sm">
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
    <tr class="close-related-issues"><td class="nobordered" align="left" id="related-issues">
    </td></tr>
  
    <tr class="support-visit-section"><th>On-Site Support Visit</th></tr>
    <tr class="support-visit-section"><td class="nobordered" align="left">
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
          <td><html:text name="inputBean" property="supportVisitDate" styleId="supportVisitDate" styleClass="form-control input-sm datepicker"/></td>
          <td class="support-visit-info fieldName">Time(HHMM):</td>
          <td class="support-visit-info"><html:text name="inputBean" property="supportVisitTime" maxlength="4" styleClass="form-control input-sm" style="text-align:center;"/></td>
          <td class="support-visit-info" align="center">-</td>
          <td class="support-visit-info"><html:text name="inputBean" property="supportVisitEndTime" maxlength="4" styleClass="form-control input-sm" style="text-align:center;"/></td>
          <td class="support-visit-info fieldName">Reason:</td>
          <td class="support-visit-info">
            <html:select name="inputBean" property="supportVisitReason" styleClass="form-control input-sm">
              <html:option value="Follow-Up Training"/>
              <html:option value="Support"/>
              <html:option value="Training/Support"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="support-visit-info fieldName">Trainer:</td>
          <td class="support-visit-info" colspan="2">
            <html:select name="inputBean" property="currTrainer" styleId="currTrainer" styleClass="form-control input-sm" onchange="checkNew(this, 'trainer');">
              <logic:present name="supportTeamList"><html:options name="supportTeamList"/></logic:present>
              <html:option value="null">Add new...</html:option>
            </html:select>
          </td>
          <td class="support-visit-info" colspan="3"><html:text name="inputBean" property="trainer" style="display:none;" styleClass="form-control input-sm"/></td>
        </tr>
        <tr>
          <td class="support-visit-info fieldName"><span class="regAsterisk">*</span> Location:</td>
          <td class="support-visit-info" colspan="2">
            <html:select name="inputBean" property="supportVisitLoc" styleClass="form-control input-sm">
              <html:option value=""/>
              <logic:present name="locationList"><html:options collection="locationList" property="value"/></logic:present>
            </html:select>
          </td>
  
        </tr>
        <tr>
          <td class="support-visit-info fieldName">Location Notes:</td>
          <td class="support-visit-info" colspan="7"><html:text name="inputBean" property="supportVisitLocNotes" styleClass="form-control input-sm"/></td>
        </tr>
      </tbody>
      </table>
    </td></tr>
  
  
    <tr><th>Internal Comments</th></tr>
    <tr><td class="nobordered" align="left">
      <table id="commentsTable" class="border-zero cellspacing-zero alt-color" style="width:100%">
      <colgroup>
        <col width="620">
      </colgroup>
      <thead>
        <tr class="ignore"><td colspan="2" class="newRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAdd">Add New Comment</a></td></tr>
      </thead>
      <tbody>
      <logic:notEmpty name="inputBean" property="issueCommentsList">
        <logic:iterate id="commentsBean" name="inputBean" property="issueCommentsList" type="com.premiersolutionshi.old.bean.SupportBean">
          <% if (editType.equals("edit")) { %>
            <tr class="nohover"><td colspan="2">
              <div style="float:left;"><b>${commentsBean.createdBy}</b></div>
              <div style="float:right;"><i>${commentsBean.createdDate}</i></div>
              <br clear="all"/>
              <div style="padding-left:15px;">
                <logic:iterate id="comments" name="commentsBean" property="commentsBr">
                  ${comments}<br/>
                </logic:iterate>
              </div>
            </td></tr>
          <% } else if (editType.equals("copy") && !commentsBean.getComments().equals("Support issue created") && !commentsBean.getComments().startsWith("Status changed to ")) { %>
            <tr>
              <td class="TOP"><textarea name="commentsArr" rows="10" class="form-control input-sm">${commentsBean.comments}</textarea></td>
              <td class="TOP"><input type="button" class="ibtnDel btn btn-danger" value="Delete"/></td>
            </tr>
          <% } %>
        </logic:iterate>
      </logic:notEmpty>
      </tbody>
      </table>
    </td></tr>
  
  
    <tr><th>Files</th></tr>
    <tr><td class="nobordered" align="left">
      <logic:notEmpty name="inputBean" property="issueFileList">
        <table class="file-list-table alt-color" width="100%">
        <thead>
          <tr>
            <th>File</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
        <% if (editType.equals("edit")) { %>
        <logic:iterate id="fileBean" name="inputBean" property="issueFileList" type="com.premiersolutionshi.old.bean.FileBean">
          <tr>
            <td><img src="${fileBean.smlImage}"/>
            ${fileBean.filename}</td>
            <td align="center"><input type="checkbox" name="deleteFilePkArr" value="${fileBean.filePk}"/></td>
          </tr>
        </logic:iterate>
        <% } %>
        </tbody>
        </table>
      </logic:notEmpty>
  
      <table id="newUploadFileTbl">
      <colgroup>
        <col width="400">
      </colgroup>
      <tbody>
      </tbody>
      <tfoot>
        <tr><td colspan="2" class="newRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAdd">Add New File</a></td></tr>
      </tfoot>
      </table>
    </td></tr>
  </tbody>
  </table>
  
  <table id="borderlessTable" class="border-zero cellspacing-zero"><tbody>
    <tr>
      <td align="center">
        <% if (editType.equals("add") || editType.equals("copy")) { %>
          <button type="submit" value="Submit" class="btn btn-success"><span class="glyphicon glyphicon-ok"></span> Insert</button>
        <% } else { %>
          <button type="submit" value="Submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Save</button>
        <% } %>
      </td>
      <td align="center">
        <% if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) { %>
          <a class="btn btn-default" href="support.do?action=${pageFrom}&projectPk=${projectPk}"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
        <% } else { %>
          <a class="btn btn-default" href="support.do?action=issueList&projectPk=${projectPk}"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
        <% } %>
      </td>
    </tr>
  </tbody></table>
  </html:form>
</div>

<%@ include file="../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<%--Build Data in JavaScript here. --%>
<script type="text/javascript">
var projectPk = '${projectPk}';
var editType = '${editType}';
var shipPk = '${inputBean.shipPk}';
var configuredSystemFk = '${inputBean.configuredSystemFk}';
var issuePk = '${inputBean.issuePk}';
var userFullName = '${loginBean.fullName}';
var openedByList = [];
var homeportList = [];
<logic:iterate id="homeport" name="homeportList" type="java.lang.String">homeportList.push('${homeport}');
</logic:iterate>
<logic:iterate id="openedBy" name="openedByList" type="java.lang.String">openedByList.push('${openedBy}');
</logic:iterate>
var shipFkToCsListMap = {};
<logic:iterate id="cs" name="configuredSystemAllList" type="com.premiersolutionshi.old.bean.SystemBean">addShipFkToCsListMap('${cs.shipFk}', { id: '${cs.configuredSystemPk}', computerName: '${cs.computerName}' });
</logic:iterate>

function addShipFkToCsListMap(shipFk, configuredSystem) {
  if (!shipFkToCsListMap[shipFk]) {
    shipFkToCsListMap[shipFk] = [];
  }
  shipFkToCsListMap[shipFk].push(configuredSystem);
}
</script>
<script type="text/javascript" src="js/support/issueEdit.js"></script>

</body>
</html>
