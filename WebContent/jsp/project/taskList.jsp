<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Task List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"     scope="request" class="java.lang.String"/>
<jsp:useBean id="projectBean"  scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="resultList"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="inputBean"    scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="statusList"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="sortBy"       scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir"      scope="request" class="java.lang.String"/>

<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

    <a href="project.do?action=taskAdd&projectPk=${projectPk}" class="btn btn-success">
      <span class="glyphicon glyphicon-plus"></span>
      Add New Task
    </a>
  
    <%@ include file="../include/query-taskList.jsp" %>

    <p align="center">
    <table id="dataTable_style2" class="alt-color border-zero cellspacing-zero">
      <% if (!CommonMethods.isEmpty(inputBean.getContractNumber()) ||
           !CommonMethods.isEmpty(inputBean.getSearchTitleDescription()) ||
           !CommonMethods.isEmpty(inputBean.getPersonAssigned()) ||
           !CommonMethods.isEmpty(inputBean.getCategory()) ||
           !CommonMethods.isEmpty(inputBean.getStatus()) ||
           !CommonMethods.isEmpty(inputBean.getPriority()) ||
           !CommonMethods.isEmpty(inputBean.getUic()) ||
           !CommonMethods.isEmpty(inputBean.getHomeport()) ||
           !CommonMethods.isEmpty(inputBean.getDueDateStart()) ||
           !CommonMethods.isEmpty(inputBean.getDueDateEnd()) ||
           inputBean.getSearchStatusArr().length > 0 ||
           inputBean.getSearchMeetingArr().length > 0 ||
           !CommonMethods.isEmpty(inputBean.getNotes()) ||
           !CommonMethods.isEmpty(inputBean.getQuarterYear()) ||
           !CommonMethods.isEmpty(inputBean.getSearchSubTask())
           ) { %>
        <thead>
          <tr class="ignore">
            <td colspan="9" class="recordCnt">
              Parameters:<br/>
              <div style="margin-left:10px;">
                <logic:notEmpty name="inputBean" property="contractNumber"><span style="color:#777">Contract Number:</span> ${inputBean.contractNumber}<br/></logic:notEmpty>
  
                <logic:notEmpty name="inputBean" property="searchTitleDescription"><span style="color:#777">Title/Description:</span> ${inputBean.searchTitleDescription}<br/></logic:notEmpty>
  
                <logic:notEmpty name="inputBean" property="personAssigned"><span style="color:#777">Person Assigned:</span> ${inputBean.personAssigned}<br/></logic:notEmpty>
  
                <logic:notEmpty name="inputBean" property="category"><span style="color:#777">Category:</span> ${inputBean.category}<br/></logic:notEmpty>
  
                <logic:notEmpty name="inputBean" property="quarterYear"><span style="color:#777">Target Quarter:</span> ${inputBean.quarterYear}<br/></logic:notEmpty>
  
                <% if (inputBean.getSearchStatusArr().length > 0) { %>
                  <span style="color:#777">Status:</span>
                  <logic:iterate id="searchStatus" name="inputBean" property="searchStatusArr" type="java.lang.String" indexId="i">
                    ${searchStatus}<% if (i < inputBean.getSearchStatusArr().length - 1) { %>,<% } %>
                  </logic:iterate><br/>
                <% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getPriority())) { %><span style="color:#777">Priority:</span> ${inputBean.priority}<br/><% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getDueDateStart()) || !CommonMethods.isEmpty(inputBean.getDueDateEnd())) { %>
                  <span style="color:#777">Due Date:</span>
                  <% if (!CommonMethods.isEmpty(inputBean.getDueDateStart())) { %><span style="color:#777">from</span> ${inputBean.dueDateStart}<% } %>
                  <% if (!CommonMethods.isEmpty(inputBean.getDueDateEnd())) { %><span style="color:#777">to</span> ${inputBean.dueDateEnd}<% } %><br/>
                <% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getUic())) { %><span style="color:#777">UIC:</span> ${inputBean.uic}<br/><% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getHomeport())) { %><span style="color:#777">Homeport:</span> ${inputBean.homeport}<br/><% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getNotes())) { %><span style="color:#777">Notes:</span> ${inputBean.notes}<br/><% } %>
  
                <% if (!CommonMethods.isEmpty(inputBean.getSearchSubTask())) { %><span style="color:#777">Sub-Tasks:</span> ${inputBean.searchSubTask}<br/><% } %>
  
                <% if (inputBean.getSearchMeetingArr().length > 0) { %>
                  <span style="color:#777">Weekly Meeting Agenda:</span>
                  <logic:iterate id="searchMeeting" name="inputBean" property="searchMeetingArr" type="java.lang.String" indexId="i">
                    ${searchMeeting}<% if (i < inputBean.getSearchMeetingArr().length - 1) { %>,<% } %>
                  </logic:iterate>
                <% } %>
              </div>
            </td>
          </tr>
        </thead>
      <% } %>
    <tbody>
    <% if (resultList.size() == 0) { %>
      <tr class="ignore">
        <td colspan="9" class="error" align="center" style="width:800px">
          No Tasks Found
        </td>
      </tr>
    <% } else { %>
      <tr class="ignore">
        <td colspan="9" class="recordCnt">
          <b><%= resultList.size() %> tasks found</b>
        </td>
      </tr>
      <tr>
        <th nowrap>
          <a href="javascript: changeSort('task_pk', '${sortBy}', '${sortDir}');">Title/Description
            <% if (sortBy.equals("task_pk") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("task_pk") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>
        <% if (!CommonMethods.isEmpty(inputBean.getCategory())) { /* only show if searching by this field */ %>
          <th nowrap>
            <a href="javascript: changeSort('category', '${sortBy}', '${sortDir}');">Category
              <% if (sortBy.equals("category") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
              <% if (sortBy.equals("category") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } %>
        <th nowrap style="text-align:center;">
          <a href="javascript: changeSort('status', '${sortBy}', '${sortDir}');">Status
            <% if (sortBy.equals("status") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("status") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>
        <th nowrap style="text-align:center;">
          <a href="javascript: changeSort('priority', '${sortBy}', '${sortDir}');">Priority
            <% if (sortBy.equals("priority") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("priority") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>

        <% if (inputBean.getCategory().equals("Future Requests")) {  /* only show if category is future requests, else show unit name */ %>
          <th nowrap>
            <a href="javascript: changeSort('effort_area', '${sortBy}', '${sortDir}');">Area of Effort
              <% if (sortBy.equals("effort_area") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
              <% if (sortBy.equals("effort_area") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
          <th nowrap style="text-align:center;">
            <a href="javascript: changeSort('quarter_year', '${sortBy}', '${sortDir}');">Quarter
            <% if (sortBy.equals("quarter_year") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("quarter_year") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } else { %>
          <th nowrap>
            <a href="javascript: changeSort('ship_name', '${sortBy}', '${sortDir}');">Unit Name
              <% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
              <% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } %>

        <th nowrap style="text-align:center;">
          <a href="javascript: changeSort('due_date', '${sortBy}', '${sortDir}');">Due Date
          <% if (sortBy.equals("due_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
          <% if (sortBy.equals("due_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>
        <% if (inputBean.getStatus().equals("Completed")) { /* only show if searching by this field */ %>
          <th nowrap>
            <a href="javascript: changeSort('completed_date', '${sortBy}', '${sortDir}');">Completed Date
            <% if (sortBy.equals("completed_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("completed_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } %>
        <% if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Staff")) { /* only show if searching by this field */ %>
          <th nowrap>
            <a href="javascript: changeSort('staff_meeting_ind', '${sortBy}', '${sortDir}');">Staff<br/>Meeting
            <% if (sortBy.equals("staff_meeting_ind") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("staff_meeting_ind") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } %>
        <% if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Client")) { /* only show if searching by this field */ %>
          <th nowrap>
            <a href="javascript: changeSort('client_meeting_ind', '${sortBy}', '${sortDir}');">Client<br>Meeting
            <% if (sortBy.equals("client_meeting_ind") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("client_meeting_ind") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
            </a>
          </th>
        <% } %>
        <th nowrap>
          <a href="javascript: changeSort('person_assigned', '${sortBy}', '${sortDir}');">Person Assigned
            <% if (sortBy.equals("person_assigned") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
            <% if (sortBy.equals("person_assigned") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>
        <th nowrap>
          <a href="javascript: changeSort('last_updated_date', '${sortBy}', '${sortDir}');">Last Updated
          <% if (sortBy.equals("last_updated_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
          <% if (sortBy.equals("last_updated_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
          </a>
        </th>

        <th nowrap style="text-align:center;">Action</th>
      </tr>

      <logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ProjectBean">
        <tr style="vertical-align:top;">
          <td style="width:380px" class="titleDescription TOP">
            <a href="project.do?action=taskDetail&taskPk=${resultBean.taskPk}&projectPk=${projectPk}"><b>Task #${resultBean.taskPk}: ${resultBean.title}</b></a><br/>
              <logic:iterate id="description" name="resultBean" property="descriptionBr">
                ${description}<br/>
              </logic:iterate>
          </td>

          <% if (!CommonMethods.isEmpty(inputBean.getCategory())) { /* only show if searching by this field */ %>
            <td align="center">${resultBean.category}</td>
          <% } %>

          <td align="center" style="${resultBean.statusCss}">${resultBean.status}</td>
          <td align="center" style="${resultBean.priorityCss}">${resultBean.priority}</td>

          <% if (inputBean.getCategory().equals("Future Requests")) {  /* only show if category is future requests, else show unit name */ %>
            <td>
              <logic:iterate id="effortArea" name="resultBean" property="effortAreaArr">
                ${effortArea}<br/>
              </logic:iterate>
            </td>
            <td align="center">${resultBean.quarterYear}</td>
          <% } else { %>
            <td>
              ${resultBean.shipName}<br/>
              <span style="color:#777;font-style:italic;">${resultBean.homeport}</span>
            </td>
          <% } %>

          <td align="center" style="${resultBean.dueDateCss}">${resultBean.dueDate}</td>

          <% if (inputBean.getStatus().equals("Completed")) {  /* only show if searching by this field */ %>
            <td align="center">${resultBean.completedDate}</td>
          <% } %>

          <% if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Staff")) { /* only show if searching by this field */ %>
            <td align="center"><% if(resultBean.getStaffMeetingInd().equals("Y")) { %><img src="images/icon_checkmark.png" height="16" style="width:16px"/><% } else { %>&nbsp;<% } %></td>
          <% } %>
          <% if (CommonMethods.isIn(inputBean.getSearchMeetingArr(), "Client")) { /* only show if searching by this field */ %>
            <td align="center"><% if(resultBean.getClientMeetingInd().equals("Y")) { %><img src="images/icon_checkmark.png" height="14" style="width:14px"/><% } else { %>&nbsp;<% } %></td>
          <% } %>

          <td>${resultBean.personAssigned}</td>

          <td nowrap>
            ${resultBean.lastUpdatedDate}<br/>
            <% if (!CommonMethods.isEmpty(resultBean.getLastUpdatedBy())) { %>
              by ${resultBean.lastUpdatedBy}
            <% } %>
          </td>
          <td nowrap align="center">
            <a href="project.do?action=taskDetail&taskPk=${resultBean.taskPk}&projectPk=${projectPk}"><img src="images/icon_view.png" title="View Task #${resultBean.taskPk}"/></a>
            &nbsp;<a href="project.do?action=taskEdit&taskPk=${resultBean.taskPk}&projectPk=${projectPk}"><img src="images/icon_edit.png" title="Edit Task #${resultBean.taskPk}"/></a>
            &nbsp;<a href="project.do?action=taskCopy&taskPk=${resultBean.taskPk}&projectPk=${projectPk}"><img src="images/icon_copy.png" title="Copy Task #${resultBean.taskPk}"/></a>
            &nbsp;<a href="project.do?action=taskDeleteDo&taskPk=${resultBean.taskPk}&projectPk=${projectPk}" onclick="return confirmDelete('${resultBean.taskPk}');"><img src="images/icon_delete.png" title="Delete Task #${resultBean.taskPk}"/></a>
          </td>
        </tr>
      </logic:iterate>
    <% } %>
    </tbody>
    </table>

    <form name="sortForm" action="project.do" method="GET">
      <input type="hidden" name="action" value="taskList"/>
      <html:hidden name="projectBean" property="projectPk"/>
      <html:hidden name="inputBean" property="searchTitleDescription"/>
      <html:hidden name="inputBean" property="personAssigned"/>
      <html:hidden name="inputBean" property="status"/>
      <html:hidden name="inputBean" property="priority"/>
      <html:hidden name="inputBean" property="category"/>
      <html:hidden name="inputBean" property="dueDateStart"/>
      <html:hidden name="inputBean" property="dueDateEnd"/>
      <html:hidden name="inputBean" property="uic"/>
      <html:hidden name="inputBean" property="homeport"/>
      <html:hidden name="inputBean" property="notes"/>
      <html:hidden name="inputBean" property="searchSubTask"/>
      <logic:iterate id="searchStatus" name="inputBean" property="searchStatusArr" type="java.lang.String">
        <input type="hidden" name="searchStatusArr" value="${searchStatus}"/>
      </logic:iterate>
      <logic:iterate id="searchMeeting" name="inputBean" property="searchMeetingArr" type="java.lang.String">
        <input type="hidden" name="searchMeetingArr" value="${searchMeeting}"/>
      </logic:iterate>
      <logic:iterate id="effortArea" name="inputBean" property="effortAreaArr" type="java.lang.String">
        <input type="hidden" name="effortAreaArr" value="${effortArea}"/>
      </logic:iterate>
      <input type="hidden" name="sortBy" value="${sortBy}"/>
      <input type="hidden" name="sortDir" value="${sortDir}"/>
      <input type="hidden" name="searchPerformed" value="Y"/>
    </form>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
  $(function() {
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();

    //Set default status to ALL if none is checked
    if ($(".statusBox:checked").length == 0) {
      document.projectForm.statusAll.checked = true;
    } //end of if

    $("#allStatusBox").on("change", function() {
      //Uncheck the others
      if ($(this).is(":checked")) {
        $('.statusBox:checked').each(function() {
          $(this).click();
        });
      } else if ($(".statusBox:checked").length == 0) {
        document.projectForm.statusAll.checked = true;
      } //end of else-if
    });

    $(".statusBox").on("change", function() {
      if ($(this).is(":checked")) {
        document.projectForm.statusAll.checked = false;
      } else if ($(".statusBox:checked").length == 0) {
        document.projectForm.statusAll.checked = true;
      } //end of else-if
    });

    $('#category').on('change', function() {
      if ($(this).val() == "Future Requests") {
        $('.futureRequestFields').show();
      } else {
        $('#quarterYear_ALL').prop('checked', true);
        $('.futureRequestFields').hide();
      } //end of if
    });
  });

  jQuery.fn.highlight = function (str, className) {
    var regex = new RegExp(str, "gi");
    return this.each(function () {
      this.innerHTML = this.innerHTML.replace(regex, function(matched) {return "<span class=\"" + className + "\">" + matched + "</span>";});
    });
  };

  $(document).ready(function () {
    $("#category").change();
    <% if (!CommonMethods.isEmpty(inputBean.getSearchTitleDescription())) { %>
      $(".titleDescription").highlight("${inputBean.searchTitleDescription}", "highlight-class");
    <% } %>
  });

  function toggleStatus(obj) {
    $(obj).click();
  } //end of toggleStatus

  function confirmDelete(taskPk) {
    return confirm("Are you sure you want to delete task #" + taskPk);
  } //end of confirmDelete
</script>

</body>
</html>
