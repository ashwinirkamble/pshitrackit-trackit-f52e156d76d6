
<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Support Issue List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<jsp:useBean id="inputBean"               scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="searchMode"               scope="request" class="java.lang.String"/>
<jsp:useBean id="uic"                     scope="request" class="java.lang.String"/>
<jsp:useBean id="issueList"               scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="summaryByMonthList"       scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="summaryByShipList"       scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="totalByShipBean"         scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="recentIssueTotal"         scope="request" class="java.lang.String"/>
<jsp:useBean id="recentIssueSummaryList"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="recentIssueList"         scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="recentClosedTotal"       scope="request" class="java.lang.String"/>
<jsp:useBean id="recentClosedList"         scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="configuredSystemList"     scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contractNumberList"       scope="request" class="java.util.ArrayList"/>

<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>
<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

    <table class="issue-list-control-panel">
      <tbody><tr>
        <% if (contractNumberList.size() > 0) { %>
        <td>
            <form class="form-inline" action="support.do" method="GET">
              <input type="hidden" name="action" value="issueList"/>
              <input type="hidden" name="searchPerformed" value="Y"/>
              <input type="hidden" name="projectPk" value="${projectPk}"/>
              <div class="form-group">
                <label for="contractNumber">Contract Number:</label>
                <html:select name="inputBean" property="contractNumber" styleClass="form-control input-sm">
                  <html:option value="">View All</html:option>
                  <html:options name="contractNumberList"/>
                </html:select>
              </div>
              <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
            </form>
        </td>
        <% } %>
        <td>
          <a href="export.do?action=issueListXlsx&type=${searchMode}&projectPk=${projectPk}" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
          <a href="support.do?action=issueReports&projectPk=${projectPk}" class="btn btn-default"><img src="images/sml_chart.png"/> Reports</a>
        </td>
        <td>
          <%@ include file="../include/support/issue/jumpToIssuePk.jsp" %>
        </td>
        <td>
          <a href="issue.do?action=add&projectPk=${projectPk}&pageFrom=issueList" class="btn btn-success">
            <span class="glyphicon glyphicon-plus"></span>
            Add Issue
          </a>
        </td>
      </tr></tbody>
    </table>

    <div>
      <div id="radio" align="center" style="display:none;">
        <input type="radio" name="searchMode" id="radio_open_non_monthly" value="open_non_monthly"/><label for="radio_open_non_monthly">Open (Non-Monthly)</label>
        <input type="radio" name="searchMode" id="radio_all_non_monthly_60" value="all_non_monthly_60"/><label for="radio_all_non_monthly_60">All Non-Monthly (last 60 days)</label>
        <input type="radio" name="searchMode" id="radio_open" value="open"/><label for="radio_open">All Open</label>
        <input type="radio" name="searchMode" id="radio_facetupdate" value="facetupdate"/><label for="radio_facetupdate">Open FACET Updates</label>
        <input type="radio" name="searchMode" id="radio_ato" value="ato"/><label for="radio_ato">Open ATO</label>
        <input type="radio" name="searchMode" id="radio_dms" value="dms"/><label for="radio_dms">Open DMS</label>
        <input type="radio" name="searchMode" id="radio_dacsinactivity" value="dacsinactivity"/><label for="radio_dacsinactivity">Open DACS Inactivity</label>
        <input type="radio" name="searchMode" id="radio_dacsmissingtransmittal" value="dacsmissingtransmittal"/><label for="radio_dacsmissingtransmittal">Open Missing Transmittals</label>
        <input type="radio" name="searchMode" id="radio_ship" value="ship"/><label for="radio_ship">By Unit</label>
      </div>
      <div id="ship_div" style="display:none;font-size:100%;" align="center">
        <br/>
        Select Unit:
        <html:select name="inputBean" property="uic" styleId="uic" style="font-size:inherit;">
          <html:options collection="configuredSystemList" property="uic" labelProperty="shipName"/>
        </html:select>
        <button id="ship_btn" style="font-size:inherit;">Go</button>
      </div>
    </div><br/>

    <% if (issueList.size() == 0) { %>
      <p class="error" align="center">
      No Issues Found
      </p>
    <% } else { %>
      <p align="center">
      <div class="center">
      <table id="issueTable" class="stripe row-border border-zero cellspacing-zero">
      <thead>
        <tr>
          <th>#</th>
          <th>Title/Summary</th>
          <th>Status</th>
          <th>Unit Name</th>
          <th>Category</th>
          <th>Opened<br/>Date</th>
          <th>Person<br/>Assigned</th>
          <th>Last Updated Date</th>
          <th>Last Updated By</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
      <logic:iterate id="resultBean" name="issueList" type="com.premiersolutionshi.old.bean.SupportBean">
        <% if (CommonMethods.nes(resultBean.getPriority()).equals("High")) { %>
          <tr style="vertical-align:top;" align="left" class="highpriority">
        <% } else { %>
          <tr style="vertical-align:top;" align="left">
        <% } %>
          <td align="center"><a href="issue.do?id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=issueList">${resultBean.issuePk}</a></td>
          <td style="width:250px">
            <a href="issue.do?id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=issueList"><b>Issue #${resultBean.issuePk}</b></a>
            <% if (CommonMethods.nes(resultBean.getPriority()).equals("High")) { %>
              &nbsp;<span style="color:#f00;font-weight:bold;">HIGH PRIORITY</span>
            <% } %>
            <br/>
            ${resultBean.title}
          </td>
          <td style="${resultBean.statusCss} width:175px">
            ${resultBean.status}<br/>
            <i>${resultBean.resolution}</i>
          </td>
          <td title="Unit Name">
            ${resultBean.shipName}<br/>
            <span style="color:#777;font-style:italic;">${resultBean.homeport}</span>
          </td>
          <td>${resultBean.category}</td>
          <td align="center">${resultBean.openedDate}</td>
          <td>${resultBean.personAssigned}</td>
          <td style="font-style:italic;">${resultBean.lastUpdatedDate}</td>
          <td style="font-style:italic;">${resultBean.lastUpdatedBy}</td>
          <td nowrap align="center">
            <a href="issue.do?action=edit&id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=issueList"><img src="images/icon_edit.png" title="Edit Issue #${resultBean.issuePk}"/></a>
            &nbsp;<a href="issue.do?action=copy&id=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=issueList"><img src="images/icon_copy.png" title="Copy Issue #${resultBean.issuePk}"/></a>
            &nbsp;<a href="support.do?action=issueDeleteDo&issuePk=${resultBean.issuePk}&projectPk=${projectPk}&pageFrom=issueList" onclick="return confirmDelete('${resultBean.issuePk}');"><img src="images/icon_delete.png" title="Delete Issue #${resultBean.issuePk}"/></a>
          </td>
        </tr>
      </logic:iterate>
      </tbody>
      </table>
      </div>

      <p>
        <a href="support.do?action=generateFeedbackForm&projectPk=${projectPk}">
          <img src="images/file_icons/file_txt.png"/>
          <br/>Generate Support Feedback Form
        </a>
      </p>
    <% } %>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
  $(function() {
    $('input:radio[name="searchMode"]').checkboxradio({
      icon: false
    });

    var table = $('#issueTable').DataTable( {
      "dom": "<'row'<'col-sm-3'l><'col-sm-3'f><'col-sm-6'p>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-5'i><'col-sm-7'p>>",
      "order": [[ 0, "desc" ]],
      "lengthMenu": [[10, 50, 100, -1], [10, 50, 100, "All"]],
      "pageLength": 50,
      "columnDefs": [
        { orderable: false, targets: 1 },
        { orderable: false, targets: 9 },
        { type: "date", targets: 5 }, //Opened Date
        { type: "date", targets: 7 }, //Last Updated Date
      ],
      "aoColumns": [
        { "orderSequence": [ "desc", "asc", "asc" ] },
        null,
        null,
        null,
        null,
        { "orderSequence": [ "desc", "asc", "asc" ] },
        null,
        { "orderSequence": [ "desc", "asc", "asc" ] },
        null,
        null
      ],
      stateSave: false
    });

    $('#radio').on('change', function(event) {
      if ($('input:radio[name="searchMode"]:checked').val() == 'ship') {
        $('#ship_div').show();
      } else {
        window.location.href = 'support.do?action=issueList&searchMode=' + $('input:radio[name="searchMode"]:checked').val() + '&projectPk=${projectPk}';
      }
    });

    $('#ship_btn').on('click', function(event) {
      window.location.href = 'support.do?action=issueList&searchMode=ship&projectPk=${projectPk}&uic=' + $('#uic').val();
    });
  });

  $(document).ready(function () {
    $('#radio_${searchMode}').prop('checked', true);
    $('#radio').buttonset();
    $('#radio').show();

    if ($('input:radio[name="searchMode"]:checked').val() == 'ship') {
      $('#ship_div').show();
      $('#uic').val('${uic}');
    } else {
      $('#ship_div').hide();
    }
  });

  function confirmDelete(issuePk) {
    return confirm("Are you sure you want to delete issue #" + issuePk);
  }
</script>

</body>
</html>
