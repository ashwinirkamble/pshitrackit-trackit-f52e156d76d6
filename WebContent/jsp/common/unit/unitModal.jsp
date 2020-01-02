<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Unit Information ${unit == null ? '' : unit.fullName}"/>

<%@ include file="../../layout/old/layout-header-meta.jsp" %>
<%-- <%@ include file="../../layout/old/layout-header.jsp" %> --%>

<div class="center">
<table class="border-zero cellspacing-zero" >
<tbody>
  <tr>
    <td class="center TOP">
      <table id="detailTable" class="pshi-table-small border-zero" style="width:100%;border:1px solid #E1EAF0;">
      <thead>
        <tr>
          <th colspan="2" align="center">
            Unit Information
            <a title="Edit ${unit.fullName}" target="_blank"
              href="ship.do?action=shipEdit&shipPk=${unit.id}&projectPk=${projectPk}">
              <i class="material-icons" style="font-size:20px;">edit</i>
            </a>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td class="fieldName">Unit:</td>
          <td>
          <%-- Ashwini ship.do to ./ship.do --%>
            <a href="./ship.do?action=shipEdit&shipPk=${unit.id}" target="_blank">
              ${unit.shipName}
            </a><br/>
            <span style="color:#777;"><i>${unit.homeport}</i></span>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Type/Hull:</td>
          <td>${unit.type} ${unit.hull}</td>
        </tr>
        <tr>
          <td class="fieldName">TYCOM:</td>
          <td>${unit.tycom}</td>
        </tr>
        <tr>
          <td class="fieldName">UIC:</td>
          <td style="white-space: nowrap;">${unit.uic}</td>
        </tr>
        <logic:notEmpty name="lastSupportVisit">
          <tr>
            <td class="fieldName">Last Support Visit:</td>
            <td>
            <%--/issue.do to ./issue.do --%>
              <a href="./issue.do?id=${lastSupportVisit.id}&projectPk=${projectPk}" target="_blank">
                ${lastSupportVisit.supportVisitDateStr} ${lastSupportVisit.category}
              </a>
              <br/>
              (Person assigned: ${lastSupportVisit.personAssigned})
            </td>
          </tr>
        </logic:notEmpty>
      </tbody>
      </table>

      <logic:notEmpty name="unit">
        <logic:notEmpty name="unit" property="pocList">
          <table class="pshi-table-small" style="width:100%">
          <thead>
            <tr>
              <th colspan="4" align="center">POCs</th>
            </tr>
            <tr>
              <th>Primary</th>
              <th>POC</th>
              <th><i class="material-icons" style="font-size:20px;">email</i></th>
              <th>Work Number</th>
            </tr>
          </thead>
          <tbody>
            <logic:iterate id="poc" name="unit" property="pocList">
              <tr>
                <td align="center">
                  ${poc.primaryPoc ? '<i class="material-icons" style="font-size:20px;color:green;">check_circle</i>' : ''}
                </td>
                <td>${poc.fullName}</td>
                <td>
                  <a href="mailto:${poc.email}" title="E-mail ${poc.email}">
                    <i class="material-icons" style="font-size:20px;">email</i>
                  </a>
                </td>
                <td align="center">
                  <logic:empty name="workNumber">--</logic:empty>
                  <logic:notEmpty name="workNumber"><a href="tel:${poc.workNumber}">${poc.workNumber}</a></logic:notEmpty>
                </td>
              </tr>
            </logic:iterate>
            <logic:empty name="unit" property="pocList">
              <tr><td colspan="4" style="text-align:center">No records found.</td></tr>
            </logic:empty>
          </tbody>
          </table>
  
          <logic:notEmpty name="unit" property="pocEmails">
            <p align="center">
              <logic:notEmpty name="unit" property="primaryPocEmails">
                <a href="mailto:${unit.primaryPocEmails}" class="btn btn-primary">
                  <i class="material-icons">email</i> Email Primary POCs
                </a>
              </logic:notEmpty>
              <a href="mailto:${unit.pocEmails}" class="btn btn-primary">
                <i class="material-icons">email</i> Email All
              </a>
            </p>
          </logic:notEmpty>
        </logic:notEmpty>
      </logic:notEmpty>
    </td>

    <td class="center TOP" style="padding-left:10px;">
      <table class="pshi-table-small border-zero" style="width:100%; border: 1px solid #E1EAF0;">
        <thead>
          <tr>
            <th colspan="2" align="center">
              Configured Systems (${configuredSystemList != null && configuredSystemList.size() > 0 ? configuredSystemList.size() : 0})
            </th>
          </tr>
        </thead>
        <logic:present name="configuredSystemList">
          <logic:iterate id="configuredSystem" name="configuredSystemList">
            <%@ include file="../../include/support/configuredSystem/configuredSystemDetailTable.jsp" %>
          </logic:iterate>
        </logic:present>
      </table>
  
      <table class="pshi-table-small" style="width:100%;">
        <thead>
          <tr>
            <th colspan="2" align="center">
              Open Issues (${issueList != null && issueList.size() > 0 ? issueList.size() : 0})
            </th>
          </tr>
          <tr>
            <th>Issue</th>
            <th>Category</th>
          </tr>
        </thead>
        <tbody>
          <logic:notEmpty name="issueList">
            <logic:iterate id="issue" name="issueList">
            <tr>
              <td>
                <div>
                  <a href="./issue.do?id=${issue.id}&projectPk=${projectPk}" target="_blank">Issue #${issue.id}</a>
                  <i>(Opened ${issue.openedDateStr})</i>
                </div>
                <div>${issue.title}</div>
              </td>
              <td>${issue.category}</td>
            </tr>
            </logic:iterate>
          </logic:notEmpty>
          <logic:empty name="issueList">
            <tr><td colspan="2" style="text-align:center">No records found.</td></tr>
          </logic:empty>
        </tbody>
        </table>
    </td>
  </tr>
</tbody>
</table>
</div>

<tags:emailDialog/>

<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script src="/theme/material/js/core/popper.min.js" type="text/javascript"></script>
<script src="/theme/material/js/core/bootstrap-material-design.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/javascript.js"></script>

<%-- <%@ include file="../../layout/old/layout-footer.jsp" %> --%>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
</body>
</html>
