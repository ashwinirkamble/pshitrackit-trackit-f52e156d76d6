<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Issue Detail"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"        scope="request" class="java.lang.String"/>
<jsp:useBean id="resultBean"       scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currOsVersion"    scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"   scope="request" class="java.lang.String"/>

<jsp:useBean id="currStr"  scope="request" class="java.lang.String"/>
<jsp:useBean id="prevBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="nextBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="pageFrom" scope="request" class="java.lang.String"/>

<jsp:useBean id="pocBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-popup.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <p align="center">
    <div class="center">
    <table id="borderlessTable" border="0" cellspacing="0">
    <tbody>
      <tr>
        <td align="left" width="16" nowrap>
          <% if (prevBean != null && !CommonMethods.isEmpty(prevBean.getIssuePk())) { %>
            <a href="issue.do?id=${prevBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}"><img src="images/arrow_left.png" height="16" width="16"/></a>
          <% } %>
        </td>
        <td align="center" nowrap>${currStr}</td>
        <td align="right" width="16" nowrap>
          <% if (nextBean != null && !CommonMethods.isEmpty(nextBean.getIssuePk())) { %>
            <a href="issue.do?id=${nextBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}"><img src="images/arrow_right.png" height="16" width="16"/></a>
          <% } %>
        </td>
      </tr>
    </tbody>
    </table>
    </div>
    </p>

    <p align="center">
    <div class="center">
    <table id="tanTable_style2" border="0" cellspacing="0" width="740">
    <tbody>
      <% if (CommonMethods.nes(resultBean.getPriority()).equals("High")) { %>
      <tr>
        <td style="color:#fff;background:#d00;font-weight:bold;text-align:center;padding:5px;">
          <span style="letter-spacing:3px;">HIGH PRIORITY</span>
          <logic:notEmpty name="resultBean" property="priorityReason">
            <br/>
            ${resultBean.priorityReason}
          </logic:notEmpty>
        </td>
      </tr>
      <% } %>
      <tr><th>Unit Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="detailTable" border="0" cellspacing="0">
        <colgroup>
          <col width="130"/>
          <col width="60"/>
          <col width="120"/>
          <col width="440"/>
        </colgroup>
        <tbody>
          <% if (!CommonMethods.isEmpty(resultBean.getShipName())) { %>
            <tr>
              <td class="fieldName TOP">Unit:</td>
              <td colspan="3" width="630">
                <a href="#" class="ship-popup">${resultBean.shipName}</a><br/>
                <span style="color:#777;"><i>${resultBean.homeport}</i></span>
              </td>
            </tr>
          <% } %>

          <% if (!CommonMethods.isEmpty(resultBean.getComputerName())) { %>
            <tr>
              <td class="fieldName TOP">Configured System:</td>
              <td colspan="3" width="630">
                ${resultBean.computerName}<br/>
              </td>
            </tr>
          <% } %>

          <% if (!CommonMethods.isEmpty(resultBean.getDept())) { %>
            <tr>
              <td class="fieldName TOP">Division:</td>
              <td colspan="3">${resultBean.dept}</td>
            </tr>
          <% } %>

          <% if (!CommonMethods.isEmpty(resultBean.getInitiatedBy())) { %>
            <tr>
              <td class="fieldName TOP">Initiated By:</td>
              <td colspan="3">${resultBean.initiatedBy}</td>
            </tr>
          <% } %>

          <% if (CommonMethods.nes(resultBean.getIsEmailSent()).equals("Y") || CommonMethods.nes(resultBean.getIsEmailResponded()).equals("Y")) { %>
            <tr>
              <% if (CommonMethods.nes(resultBean.getIsEmailSent()).equals("Y")) { %>
                <td class="fieldName TOP">PSHI Email Sent?</td>
                <td>Yes</td>
              <% } %>
              <% if (CommonMethods.nes(resultBean.getIsEmailResponded()).equals("Y")) { %>
                <td class="fieldName TOP">Unit Responded?</td>
                <td>Yes</td>
              <% } %>
            </tr>
          <% } %>

        </tbody>
        </table>
      </td></tr>


      <tr><th>Issue Summary</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="borderlessTable border-zero cellspacing-zero">
        <colgroup>
          <col width="130"/>
          <col width="60"/>
          <col width="120"/>
          <col width="450"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Title/Summary:</td>
            <td colspan="3" width="630">${resultBean.title}</td>
          </tr>

          <logic:notEmpty name="resultBean" property="description">
            <tr>
              <td class="fieldName" style="vertical-align:top;">Description:</td>
              <td colspan="3">
                <logic:iterate id="description" name="resultBean" property="descriptionBr">
                  ${description}<br/>
                </logic:iterate>
              </td>
            </tr>
          </logic:notEmpty>

          <tr>
            <td class="fieldName">Category:</td>
            <td colspan="3">${resultBean.category}</td>
          </tr>

          <% if (!CommonMethods.isEmpty(resultBean.getIsTrainingProvided())) { %>
            <tr>
              <td class="fieldName TOP">Training Provided?</td>
              <td>
                <% if (CommonMethods.nes(resultBean.getIsTrainingProvided()).equals("Y")) { %>
                  Yes
                <% } else { %>
                  No
                <% } %>
              </td>
              <% if (CommonMethods.nes(resultBean.getIsTrainingProvided()).equals("Y")) { %>
                <td class="fieldName TOP">Training Onsite?</td>
                <td>
                  <% if (CommonMethods.nes(resultBean.getIsTrainingOnsite()).equals("Y")) { %>
                    Yes
                  <% } else { %>
                    No
                  <% } %>
                </td>
              <% } %>
            </tr>
          <% } %>
        </tbody>
        </table>
      </td></tr>


      <tr><th>Issue Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="borderlessTable" border="0" cellspacing="0">
        <colgroup>
          <col width="130"/>
          <col width="230"/>
          <col width="110"/>
        </colgroup>
        <tbody>
          <logic:notEmpty name="resultBean" property="personAssigned">
            <tr>
              <td class="fieldName">Person Assigned:</td>
              <td><a href="#" class="poc-popup">${resultBean.personAssigned}</a></td>
            </tr>
          </logic:notEmpty>

          <tr>
            <td class="fieldName">Opened By:</td>
            <td>${resultBean.openedBy}</td>
            <td class="fieldName">Opened Date:</td>
            <td>${resultBean.openedDate}</td>
          </tr>
        </tbody>
        </table>
      </td></tr>


      <tr><th>Status</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="borderlessTable" border="0" cellspacing="0">
        <colgroup>
          <col width="130"/>
          <col width="230"/>
          <col width="110"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Status:</td>
            <td style="${resultBean.statusCss}">${resultBean.status}</td>

            <logic:notEmpty name="resultBean" property="closedDate">
              <td class="fieldName">Closed Date:</td>
              <td>${resultBean.closedDate}</td>
            </logic:notEmpty>
          </tr>

          <tr id="resolutionRow">
            <td class="fieldName" style="vertical-align:top;">
              <logic:notEmpty name="resultBean" property="resolution">
                Resolution:
              </logic:notEmpty>
            </td>
            <td>${resultBean.resolution}</td>

            <logic:notEmpty name="resultBean" property="totalTime">
              <td class="fieldName" style="vertical-align:top;">Total Time:</td>
              <td style="vertical-align:top;">
                <logic:equal name="resultBean" property="totalTime" value="15">15 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="30">30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="45">45 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="60">1 hour</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="90">1 hour 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="120">2 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="150">2 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="180">3 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="210">3 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="240">4 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="270">4 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="300">5 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="330">5 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="360">6 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="390">6 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="420">7 hours</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="450">7 hours 30 min</logic:equal>
                <logic:equal name="resultBean" property="totalTime" value="480">8 hours</logic:equal>
              </td>
            </logic:notEmpty>
          </tr>

          <logic:notEmpty name="resultBean" property="autoCloseDate">
            <tr>
              <td></td>
              <td colspan="3" nowrap>
                <span class="glyphicon glyphicon-time"></span>
                Set to auto close on ${resultBean.autoCloseDate}
                <logic:notEmpty name="resultBean" property="autoCloseStatus">
                  [${resultBean.autoCloseStatus}]
                </logic:notEmpty>
              </td>
            </tr>
          </logic:notEmpty>
        </tbody>
        </table>
      </td></tr>

      <logic:notEmpty name="resultBean" property="supportVisitDate">
      <tr><th>On-Site Support Visit</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="borderlessTable" border="0" cellspacing="0">
        <colgroup>
          <col width="130"/>
          <col width="270"/>
          <col width="110"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">Scheduled Visit:</td>
            <td>
              ${resultBean.supportVisitDate}
              ${resultBean.supportVisitTime}
              <logic:notEmpty name="resultBean" property="supportVisitEndTime">
                -
                ${resultBean.supportVisitEndTime}
              </logic:notEmpty>
            </td>
            <logic:notEmpty name="resultBean" property="supportVisitReason">
              <td class="fieldName">Reason:</td>
              <td>${resultBean.supportVisitReason}</td>
            </logic:notEmpty>
            <logic:empty name="resultBean" property="supportVisitReason">
              <td></td>
              <td></td>
            </logic:empty>
          </tr>

          <logic:notEmpty name="resultBean" property="trainer">
            <tr>
              <td class="fieldName">Trainer:</td>
              <td>${resultBean.trainer}</td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="supportVisitLoc">
          <tr>
            <td class="fieldName">Location:</td>
            <td>${resultBean.supportVisitLoc}</td>
          </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="supportVisitLocNotes">
          <tr>
            <td class="fieldName">Location Notes:</td>
            <td>${resultBean.supportVisitLocNotes}</td>
          </tr>
          </logic:notEmpty>
        </tbody>
        </table>
      </td></tr>
      </logic:notEmpty>

      <logic:notEmpty name="resultBean" property="issueCommentsList">
      <tr><th>Internal Comments</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="commentsTable" border="0" cellspacing="0" class="alt-color" width="100%">
        <tbody>
          <logic:iterate id="commentsBean" name="resultBean" property="issueCommentsList" type="com.premiersolutionshi.old.bean.SupportBean">
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
          </logic:iterate>
        </tbody>
        </table>
      </td></tr>
      </logic:notEmpty>

      <logic:notEmpty name="resultBean" property="issueFileList">
      <tr><th>Files</th></tr>
      <tr><td class="nobordered" align="left">
        <div style="text-align:left;">
          <logic:iterate id="fileBean" name="resultBean" property="issueFileList" type="com.premiersolutionshi.old.bean.FileBean">
          <div style="padding:10px;text-align:center;display:inline-block;">
            <a href="download.do?filePk=${fileBean.filePk}"><img src="${fileBean.image}" width="55" height="55"/><br/>
            ${fileBean.filename}
            </a>
          </div>
          </logic:iterate>
        </div>
      </td></tr>
      </logic:notEmpty>
    </tbody>
    </table>
    </div>
    </p><br/>

    <p align="center" style="color:#999;text-decoration:italic;">
    Last updated ${resultBean.lastUpdatedDate}
    <logic:notEmpty name="resultBean" property="lastUpdatedBy">
    by ${resultBean.lastUpdatedBy}
    </logic:notEmpty>
    </p><br/>

    <p align="center">
    <a class="btn btn-primary" href="issue.do?action=edit&id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}">
      <span class="glyphicon glyphicon-pencil"></span>
      Edit
    </a>
    <a class="btn btn-info" href="issue.do?action=copy&id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=${pageFrom}">
      <span class="glyphicon glyphicon-retweet"></span>
      Copy
    </a>
    <a class="btn btn-warning" href="support.do?action=generateFeedbackForm&issuePk=${resultBean.issuePk}&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-file"></span>
      Generate Support Feedback Form
      </a>
    <a class="btn btn-danger" href="javascript:confirmDelete(${resultBean.issuePk});">
      <span class="glyphicon glyphicon-trash"></span>
      Delete
    </a>
    <% if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) { %>
      <a class="btn btn-default" href="support.do?action=${pageFrom}&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-list-alt"></span>
        Return to Issue List
      </a>
    <% } else { %>
      <a class="btn btn-default" href="support.do?action=issueList&projectPk=${projectPk}">
        <span class="glyphicon glyphicon-list-alt"></span>
        Return to Issue List
      </a>
    <% } %>
    </p>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<div id="toPopup">
  <div class="close"></div>
  <span class="ecs_tooltip">Press Esc to close <span class="arrow"></span></span>
  <div id="popup_content"> <!--your content start-->
    <div id="poc_content"> <!-- poc content -->
      <%@ include file="../include/popup-poc.jsp" %>
    </div> <!-- poc content -->

    <div id="ship-popup"> <!-- ship content -->
      <%@ include file="../include/popup-ship.jsp" %>
    </div> <!-- ship-content -->
  </div> <!--your content end-->
</div> <!--toPopup end-->
<div class="loader"></div>
<div id="backgroundPopup"></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-popup.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
  function confirmDelete(issuePk) {
    if (confirm("Are you sure you want to delete issue #" + issuePk)) {
      window.location = "support.do?action=issueDeleteDo&issuePk=" + issuePk + "&projectPk=${projectPk}&pageFrom=${pageFrom}";
    } //end of if
  } //end of confirmDelete
</script>

</body>
</html>
