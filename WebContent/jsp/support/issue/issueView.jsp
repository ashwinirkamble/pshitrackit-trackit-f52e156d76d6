<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="issueForm" scope="request" class="com.premiersolutionshi.support.ui.form.IssueForm"/>
<jsp:useBean id="pageFrom" scope="request" class="java.lang.String"/>

<bean:define id="pageTitle" value="View Support Issue ${issueForm.id == null ? '' : '#'.concat(issueForm.id)}"/>
<bean:define id="ship"             name="issueForm" property="ship"/>
<bean:define id="configuredSystem" name="issueForm" property="configuredSystem"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<logic:present name="project"><logic:notEmpty name="project">
  <logic:empty name="issueForm" property="id">
    <script type="text/javascript">
    window.location = 'support.do?action=issueList&projectPk=${projectPk}';
    </script>
  </logic:empty>
<%--ashwini /support to ./support --%>
  <tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
    parentUrl="./support.do?action=${pageFrom}&projectPk=${projectPk}"
    parentTitle="Issue List${pageFrom.equals('issueListAll') ? ' (All)' : ''}"/>

  <logic:notEmpty name="issueForm" property="id">
    <p class="center">
      <a href="issue.do?action=${action == 'edit' ? 'view' : 'edit'}&id=${issueForm.id}&projectPk=${projectPk}&pageFrom=${pageFrom}">
        <i class="material-icons" style="font-size: 13px;">${action == 'edit' ? 'pageview' : 'edit'}</i>
        ${action == 'edit' ? 'View' : 'Edit'} Issue #${issueForm.id}
      </a>
      <br/>
      <%@ include file="../../include/support/issue/jumpToIssuePk.jsp" %>
    </p>

    <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width:740px">
    <tbody>
      <logic:equal name="issueForm" property="priority" value="High">
        <tr>
          <td style="color:#fff;background:#d00;font-weight:bold;text-align:center;padding:5px;">
            <span style="letter-spacing:3px;">HIGH PRIORITY</span>
            <logic:notEmpty name="issueForm" property="priorityReason">
              <div>${issueForm.priorityReasonBanner}</div>
            </logic:notEmpty>
          </td> 
        </tr>
      </logic:equal>
      <tr><th class="center">Unit Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="detailTable" class="border-zero cellspacing-zero">
        <colgroup>
          <col style="width:130px"/>
          <col style="width:60px"/>
          <col style="width:120px"/>
          <col style="width:440px"/>
        </colgroup>
        <tbody>
          <logic:notEmpty name="issueForm" property="shipFk">
            <tr>
              <td class="fieldName TOP">Unit:</td>
              <td colspan="3" style="width:630px">
<%--                  <a href="./unit.do?id=${ship.id}&projectPk=${projectPk}" target="_blank">${ship.shipName}</a> 
 --%>                <a href="javascript:void(0);" onclick="return showUnitPopup(${ship.id},${projectPk});">${ship.shipName}</a>
                <br/>
                <span style="color:#777;"><i>${ship.homeport}</i></span>
              </td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="issueForm" property="dept">
            <logic:notEqual name="issueForm" property="dept" value="N/A">
              <tr>
                <td class="fieldName TOP">Division:</td>
                <td colspan="3">${issueForm.dept}</td>
              </tr>
            </logic:notEqual>
          </logic:notEmpty>

          <logic:notEmpty name="issueForm" property="initiatedBy">
            <tr>
              <td class="fieldName TOP">Initiated By:</td>
              <td colspan="3">${issueForm.initiatedBy}</td>
            </tr>
          </logic:notEmpty>

          <logic:equal name="issueForm" property="isEmailSent" value="Y">
            <tr>
              <td class="fieldName TOP">PSHI Email Sent?</td>
              <td>Yes</td>
              <logic:equal name="issueForm" property="isEmailResponded" value="Y">
                <td class="fieldName TOP">Unit Responded?</td>
                <td>Yes</td>
              </logic:equal>
            </tr>
          </logic:equal>
        </tbody>
        </table>
      </td></tr>

      <logic:notEmpty name="configuredSystem"><logic:notEmpty name="issueForm" property="configuredSystemFk">
        <logic:notEqual name="issueForm" property="configuredSystemFk" value="0">
          <tr><th>Configured System</th></tr>
          <tr><td class="nobordered" align="left">
            <table style="width:100%" class="pshi-table-small">
              <%@ include file="../../include/support/configuredSystem/configuredSystemDetailTable.jsp" %>
            </table>
          </td></tr>
        </logic:notEqual></logic:notEmpty>
      </logic:notEmpty>

      <tr><th>Issue Summary</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="pshi-table-small border-zero">
        <colgroup>
          <col style="width:130px"/>
          <col style="width:60px"/>
          <col style="width:120px"/>
          <col style="width:450px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Title/Summary:</td>
            <td colspan="3" style="width:630px">${issueForm.title}</td>
          </tr>

          <logic:notEmpty name="issueForm" property="description">
            <tr>
              <td class="fieldName" style="vertical-align:top;">Description:</td>
              <td colspan="3">
                <div class="issue-comment">${issueForm.description.replaceAll('\\n','<br/>')}</div>
              </td>
            </tr>
          </logic:notEmpty>
  
          <tr>
            <td class="fieldName">Category:</td>
            <td colspan="3">${issueForm.category}</td>
          </tr>
  
          <% if (!CommonMethods.isEmpty(issueForm.getIsTrainingProvided())) { %>
            <tr>
              <td class="fieldName TOP">Training Provided?</td>
              <td>${issueForm.isTrainingProvided() ? 'Yes' : 'No'}</td>
              <% if (CommonMethods.nes(issueForm.getIsTrainingProvided()).equals("Y")) { %>
                <td class="fieldName TOP">Training Onsite?</td>
                <td>
                  ${issueForm.isTrainingOnsite != null && issueForm.isTrainingOnsite.equals("Y") ? 'Yes' : 'No'}
                </td>
              <% } %>
            </tr>
          <% } %>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Issue Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="pshi-table-small border-zero">
        <colgroup>
          <col style="width:130px"/>
          <col style="width:230px"/>
          <col style="width:110px"/>
        </colgroup>
        <tbody>
          <logic:notEmpty name="issueForm" property="personAssigned">
            <tr>
              <td class="fieldName">Person Assigned:</td>
              <td>
                <%-- <a href="#" class="poc-popup">${issueForm.personAssigned}</a> --%>
                ${issueForm.personAssigned}
              </td>
            </tr>
          </logic:notEmpty>
  
          <tr>
            <td class="fieldName">Opened By:</td>
            <td>${issueForm.openedBy}</td>
            <td class="fieldName">Opened Date:</td>
            <td>${issueForm.openedDateStr}</td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Status</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="pshi-table-small border-zero">
        <colgroup>
          <col style="width:130px"/>
          <col style="width:230px"/>
          <col style="width:110px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Status:</td>
            <td style="color:#${issueForm.statusCss}" ${issueForm.closed ? '' : 'colspan="3"'}>
              ${issueForm.status}
            </td>

            <logic:notEmpty name="issueForm" property="closedDate">
              <td class="fieldName">Closed Date:</td>
              <td>${issueForm.closedDateStr}</td>
            </logic:notEmpty>
          </tr>

          <tr id="resolutionRow">
            <td class="fieldName" style="vertical-align:top;">
              <logic:notEmpty name="issueForm" property="resolution">
                Resolution:
              </logic:notEmpty>
            </td>
            <td>${issueForm.resolution.replaceAll('\\n','<br/>')}</td>
  
            <logic:notEmpty name="issueForm" property="totalTime">
              <logic:notEqual name="issueForm" property="totalTime" value="0">
                <td class="fieldName" style="vertical-align:top;">Total Time:</td>
                <td style="vertical-align:top;">${issueForm.totalTimeStr}</td>
              </logic:notEqual>
            </logic:notEmpty>
          </tr>
  
          <logic:notEmpty name="issueForm" property="autoCloseDate">
            <tr>
              <td></td>
              <td colspan="3" nowrap>
                <span class="glyphicon glyphicon-time"></span>
                Set to auto close on ${issueForm.autoCloseDateStr}
                <logic:notEmpty name="issueForm" property="autoCloseStatus">
                  [${issueForm.autoCloseStatus}]
                </logic:notEmpty>
              </td>
            </tr>
          </logic:notEmpty>
        </tbody>
        </table>
      </td></tr>

      <logic:notEmpty name="issueForm" property="supportVisitDate">
        <tr><th>On-Site Support Visit</th></tr>
        <tr><td class="nobordered" align="left">
          <table class="pshi-table-small border-zero">
          <colgroup>
            <col style="width:130px"/>
            <col style="width:270px"/>
            <col style="width:110px"/>
          </colgroup>
          <tbody>
            <tr>
              <td class="fieldName">Scheduled Visit:</td>
              <td>
                ${issueForm.supportVisitDateStr}
                ${issueForm.supportVisitTimeStr}
                <logic:notEmpty name="issueForm" property="supportVisitEndTime">
                  - ${issueForm.supportVisitEndTimeStr}
                </logic:notEmpty>
              </td>
              <logic:notEmpty name="issueForm" property="supportVisitReason">
                <td class="fieldName">Reason:</td>
                <td>${issueForm.supportVisitReason}</td>
              </logic:notEmpty>
              <logic:empty name="issueForm" property="supportVisitReason">
                <td></td>
                <td></td>
              </logic:empty>
            </tr>
  
            <logic:notEmpty name="issueForm" property="trainer">
              <tr>
                <td class="fieldName">Trainer:</td>
                <td>${issueForm.trainer}</td>
              </tr>
            </logic:notEmpty>
  
            <logic:notEmpty name="issueForm" property="supportVisitLoc">
            <tr>
              <td class="fieldName">Location:</td>
              <td>${issueForm.supportVisitLoc}</td>
            </tr>
            </logic:notEmpty>
  
            <logic:notEmpty name="issueForm" property="supportVisitLocNotes">
            <tr>
              <td class="fieldName">Location Notes:</td>
              <td>${issueForm.supportVisitLocNotes}</td>
            </tr>
            </logic:notEmpty>
          </tbody>
          </table>
        </td></tr>
      </logic:notEmpty>
  
      <%@ include file="../../include/issue/issueCommentsDisplay.jsp" %>

      <%@ include file="../../include/issue/issueFilesDisplay.jsp" %>
      </tbody>
    </table>

    <tags:lastUpdatedBy date="${issueForm.lastUpdatedDateStr}" by="${issueForm.lastUpdatedBy}" />

    <div style="width:740px;">
      <a class="btn btn-primary" href="issue.do?action=edit&id=${issueForm.id}&projectPk=${projectPk}&pageFrom=${pageFrom}">
        <i class="material-icons">edit</i> Edit
      </a>
      <a class="btn btn-info" href="issue.do?action=copy&id=${issueForm.id}&projectPk=${projectPk}&pageFrom=${pageFrom}">
        <i class="material-icons">flip_to_front</i> Copy
      </a>
      <a class="btn btn-danger" href="javascript:confirmDeleteIssue(${issueForm.id});">
        <i class="material-icons">delete</i> Delete
      </a>
      <a class="btn btn-warning" href="support.do?action=generateFeedbackForm&issuePk=${issueForm.id}&projectPk=${projectPk}">
        <i class="material-icons">compare_arrows</i> Generate Support Feedback Form
        </a>
      <% if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) { %>
        <a class="btn btn-default" href="support.do?action=${pageFrom}&projectPk=${projectPk}">
          <i class="material-icons">view_list</i> Return to Issue List
        </a>
      <% } else { %>
        <a class="btn btn-default" href="support.do?action=${pageFrom}&projectPk=${projectPk}">
          <i class="material-icons">view_list</i> Return to Issue List
        </a>
      <% } %>
    </div>
  </logic:notEmpty>
</logic:notEmpty></logic:present>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<%--
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
  aria-hidden="true" style="width:1020px;height:600px;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <iframe src=""></iframe>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<div id="toPopup">
  <div class="close"></div>
  <span class="ecs_tooltip">Press Esc to close <span class="arrow"></span></span>
  <div id="popup_content"> <!--your content start-->
    <div id="poc_content"> <!-- poc content -->
      <%@ include file="../../include/popup-poc.jsp" %>
    </div> <!-- poc content -->
  </div> <!--your content end-->
</div> <!--toPopup end-->
<div class="loader"></div>
<div id="backgroundPopup"></div>

<tags:infoDialog/>

<script type="text/javascript" src="js/jquery-popup.js"></script>
--%>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/support/issueAdmin.js"></script>
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>
<script type="text/javascript">
var projectPk = '${projectPk}';
var pageFrom = '${pageFrom}';
var editType = '${editType}';
var shipPk = '${issueForm.shipFk}';
var originalShipPk = '${issueForm.shipFk}';
var configuredSystemFk = '${issueForm.configuredSystemFk}';
var issuePk = '${issueForm.id}';
var userFullName = '${loginBean.fullName}';
</script>

</body>
</html>
