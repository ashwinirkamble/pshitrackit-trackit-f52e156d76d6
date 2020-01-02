<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle"
  value="Task ${inputBean.taskPk == null || inputBean.taskPk == '' ? 'Add' : 'Edit #'.concat(inputBean.taskPk)}"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"   scope="request" class="java.lang.String"/>
<jsp:useBean id="projectBean" scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="inputBean"   scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>
<jsp:useBean id="editType"    scope="request" class="java.lang.String"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>

  <style>
    input[type="radio"], input[type="checkbox"] {
      margin-top: -1px;
      vertical-align: middle;
    }
  </style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>
<%--ashwini project.do ./project.do --%>
  <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
    parentUrl="./project.do?action=taskList&projectPk=${projectPk}" parentTitle="Task List"/>

    <logic:notEmpty name="inputBean" property="taskPk">
      <p align="center">
        <a href="project.do?action=taskDetail&taskPk=${inputBean.taskPk}&projectPk=${projectPk}">View Task #${inputBean.taskPk}</a>
      </p>
    </logic:notEmpty>

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <html:form action="project.do" onsubmit="return valFields();" method="POST" enctype="multipart/form-data">
    <input type="hidden" name="projectPk" value="${projectPk}"/>

    <div class="center">
      <table id="borderlessTable" class="border-zero cellspacing-zero">
        <tbody>
          <tr>
            <td align="center">
              <button type="submit" value="Submit" class="btn btn-primary">
                <span class="glyphicon glyphicon-ok"></span> Save
              </button>
            </td>
            <td align="center">
              <a class="btn btn-default" href="project.do?action=taskList&projectPk=${projectPk}"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
            </td>
          </tr>
        </tbody>
      </table>
      <br/>

    <table id="tanTable_style2" class="border-zero cellspacing-zero">
    <% if (editType.equals("add")) { %>
      <input type="hidden" name="action" value="taskAddDo"/>
    <% } else { %>
      <input type="hidden" name="action" value="taskEditDo"/>
      <html:hidden name="inputBean" property="taskPk"/>
    <% } %>
    <tbody>
      <tr><th>General Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3" style="width:800px">
        <colgroup><col style="width:105px"/></colgroup>
          <tr>
            <td class="fieldName">Contract Number:</td>
            <td nowrap>
              <html:select name="inputBean" property="currContractNumber" onchange="checkNew(this, 'contractNumber');" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="contractNumberList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="contractNumber" style="display:none;"/>
            </td>

          </tr>
          <tr>
            <td class="fieldName">Category:</td>
            <td nowrap>
              <html:select name="inputBean" property="currCategory" onchange="checkCategory(this);" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="categoryList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="category" style="display:none;"/>
            </td>
            <td class="fieldName" nowrap>Person Assigned:</td>
            <td nowrap>
              <html:select name="inputBean" property="currPersonAssigned" onchange="checkNew(this, 'personAssigned');" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="personAssignedList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="personAssigned" style="display:none;"/>
            </td>
            <td colspan="2">
              <label class="checkbox-inline">
                <html:checkbox name="inputBean" property="isInternal" styleId="isInternal" value="Y"/>
                Internal
              </label>
            </td>
          </tr>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span>Title:</td>
            <td colspan="5"><html:text name="inputBean" property="title" maxlength="75" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td class="fieldName TOP">Description:</td>
            <td colspan="5"><html:textarea name="inputBean" property="description" rows="6" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td class="fieldName">Source:</td>
            <td colspan="1">
              <html:select name="inputBean" property="currSource" onchange="checkNew(this, 'source');" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="sourceList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
            </td>
            <td><html:text name="inputBean" property="source" style="display:none;" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td class="fieldName">Ship:</td>
            <td colspan="5">
              <html:select name="inputBean" property="uic" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options collection="shipList" property="uic" labelProperty="shipNameTypeHull"/>
              </html:select>
            </td>
          </tr>
          <!--tr>
            <td class="fieldName">Level of Effort:</td>
            <td colspan="5"></td>
          </tr-->
        </table>
      </td></tr>

      <tr><th>Status</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col style="width:105px"/></colgroup>
          <tr>
            <td class="fieldName">Priority:</td>
            <td>
              <html:select name="inputBean" property="priority" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="priorityList"/>
              </html:select>
            </td>
            <td class="fieldName">Date Created</td>
            <td><html:text name="inputBean" property="createdDate" styleClass="form-control input-sm datepicker"/></td>
          </tr>
        </table>
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col style="width:105px"/></colgroup>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span>Status:</td>
            <td>
              <html:select name="inputBean" property="currStatus" onchange="checkStatus(this);" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options name="statusList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="status" style="display:none;" styleClass="form-control input-sm"/>
            </td>
            <td class="fieldName"><span id="completedField" style="display:none;"><span class="regAsterisk">*</span>Completed:</span></td>
            <td><span id="completedValue" style="display:none;"><html:text name="inputBean" property="completedDate" styleClass="form-control input-sm datepicker"/></span></td>
            <td class="fieldName">Due Date:</td>
            <td><html:text name="inputBean" property="dueDate" styleClass="form-control input-sm datepicker"/></td>
            <td class="fieldName">Follow Up:</td>
            <td><html:text name="inputBean" property="followUpDate" styleClass="form-control input-sm datepicker"/></td>
          </tr>
        </table>
        <table class="border-zero cellspacing-zero cellpadding-3" id="futureFeatureTable">
        <colgroup><col style="width:105px"/></colgroup>
          <tr>
            <td class="fieldName"><span id="deployedDateReq" class="regAsterisk" style="display:none;">*</span> Deployed Date</td>
            <td><html:text name="inputBean" property="deployedDate" styleClass="form-control input-sm datepicker"/></td>
            <td class="fieldName"><span id="docUpdatedIndReq" class="regAsterisk" style="display:none;">*</span> Documentation Updated?</td>
            <td>
              <html:select name="inputBean" property="docUpdatedInd" styleClass="form-control input-sm">
                <html:option value="Y">Y - Yes</html:option>
                <html:option value="N">N - No</html:option>
                <html:option value="N/A">N/A</html:option>
              </html:select>
            </td>
          <tr>
            <td colspan="3" class="fieldName">Doc Updated Notes:</td>
            <td><html:text name="inputBean" property="docNotes" size="41" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td colspan="3" class="fieldName TOP">Fixed/Added in Version:</td>
            <td><html:textarea name="inputBean" property="versionIncluded" rows="5" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr>

      <tr><th id="futureFeatureHead" style="display:none;">Future Features</th></tr>
      <tr><td id="futureFeatureBody" class="nobordered" align="left" style="display:none;">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col style="width:105px"/></colgroup>
          <tr>
            <td class="TOP" rowspan="8" nowrap>
              <span class="fieldName">Target Quarter:</span><br/>

              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="2014Q1"/>2014Q1 (Target 2/1)</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="2014Q2"/>2014Q2 (Target 4/1)</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="2014Q3"/>2014Q3</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="2014Q4"/>2014Q4</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="Sustainment"/>Sustainment</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="OOS"/>Out of Scope</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="quarterYear" value="TBD"/>TBD</label>
            </td>

            <td class="TOP" rowspan="8" nowrap>
              <span class="fieldName">Area of Effort:</span><br/>

              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="Admin Receipt DB"/>Admin Receipt DB</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="FACET DB"/>FACET DB</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="Kofax"/>Kofax</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="LOGCOP"/>LOGCOP</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="Manual"/>Manual</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="Support"/>Support</label><br/>
              <label class="checkbox-inline"><html:multibox name="inputBean" property="effortAreaArr" value="Training"/>Training</label><br/>
            </td>

            <td class="TOP" rowspan="3" nowrap>
              <span class="fieldName">Type of Effort:</span><br/>

              <label class="radio-inline"><html:radio name="inputBean" property="effortType" value="New Development"/>New Development</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="effortType" value="Update"/>Update</label><br/>
              <label class="radio-inline"><html:radio name="inputBean" property="effortType" value="Bug Fixes"/>Bug Fixes</label>
            </td>

            <td class="fieldName">Level of Effort:</td>
            <td colspan="3"><html:text name="inputBean" property="loe" size="4" maxlength="10"/></td>
          </tr>
          <tr>
            <td class="fieldName">Client Approved:</td>
            <td style="width:20px"><html:checkbox name="inputBean" property="isClientApproved" value="Y"/></td>
            <td class="fieldName" style="width:45px">Priority:</td>
            <td><html:text name="inputBean" property="clientPriority" size="12"/></td>
          </tr>
          <tr>
            <td class="fieldName">PSHI Approved:</td>
            <td colspan="3"><html:checkbox name="inputBean" property="isPshiApproved" value="Y"/></td>
          </tr>
          <tr>
            <td class="fieldName" colspan="2">Recommendation:</td>
            <td colspan="3">
              <html:select name="inputBean" property="recommendation">
                <html:option value=""/>
                <html:option value="R - Recommend"/>
                <html:option value="N - Nice to have"/>
                <html:option value="X - Not recommend"/>
                <html:option value="U - Unsure"/>
              </html:select>
            </td>
          </tr>
        </table>
      </td></tr>

      <tr><th>Sub-Tasks</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="subtask" style="width:775px">
        <thead>
          <tr>
            <th>&nbsp;</th>
            <th>Description</th>
            <th>Due</th>
            <th>Person Assigned</th>
            <th>Status</th>
            <th>Completed</th>
            <th>Duplicate</th>
            <th>Delete</th>
            <th>Insert Row</th>
          </tr>
        </thead>
        <tbody id="sortable">
          <logic:iterate id="subTaskBean" name="inputBean" property="subTaskList" type="com.premiersolutionshi.old.bean.ProjectBean">
          <tr>
            <input type="hidden" name="subTaskIdArr" value="${subTaskBean.subTaskId}"/>
            <input type="hidden" name="origPersonAssignedArr" value="${subTaskBean.personAssigned}"/>
            <td><span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>
            <td><input type="text" name="descriptionArr" size="68" value="${subTaskBean.description}"/></td>
            <td><input type="text" name="dueDateArr" class="datepicker" size="9" value="${subTaskBean.dueDate}"/></td>
            <td>
              <select name="personAssignedArr">
                <option value=""></option>
                <logic:iterate id="personAssigned" name="personAssignedList" type="java.lang.String">
                  <option value="${personAssigned}" <% if(subTaskBean.getPersonAssigned().equals(personAssigned)) { %>selected<% } %>>${personAssigned}</option>
                </logic:iterate>
              </select>
            </td>
            <td>
              <select name="statusArr">
                <option value=""></option>
                <logic:iterate id="status" name="statusList" type="java.lang.String">
                  <option value="${status}"<% if(subTaskBean.getStatus().equals(status)) { %> selected<% } %>>${status}</option>
                </logic:iterate>
              </select>
            </td>
            <td><input type="text" name="completedDateArr" class="datepicker" size="9" value="${subTaskBean.completedDate}"/></td>
            <td><input type="button" class="ibtnCopy" value="Duplicate"/></td>
            <td><input type="button" class="ibtnDel" value="Delete"/></td>
            <td><input type="button" class="ibtnAdd" value="Insert Row"/></td>
          </tr>
          </logic:iterate>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="7" class="newRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAddAtEnd">Add New Sub-Task</a></td>
          </tr>
        </tfoot>
        </table>

        <img src="images/icon_success.png" height="12" style="width:12px"/> <a href="javascript:completeAllSubTask();">Mark All Sub-Tasks as Completed</a>
      </td></tr>


      <tr><td class="nobordered" align="left">
        Free-Text:<br/>
        <html:textarea name="inputBean" property="subTasks" rows="5" styleClass="form-control input-sm"/>
      </td></tr>


      <tr><th>Files</th></tr>
      <tr><td class="nobordered" align="left">

      <% if (inputBean.getTaskFileList().size() > 0) { %>
          <table id="file-list-table">
          <thead>
            <tr>
              <th colspan="2">File</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>
          <logic:iterate id="fileBean" name="inputBean" property="taskFileList" type="com.premiersolutionshi.old.bean.FileBean">
            <tr>
              <td><img src="${fileBean.smlImage}"/></td>
              <td>${fileBean.filename}</td>
              <td align="center"><input type="checkbox" name="deleteFilePkArr" value="${fileBean.filePk}"/></td>
            </tr>
          </logic:iterate>
          </tbody>
          </table>
      <% } %>

        <table id="newUploadFileTbl">
        <tbody></tbody>
        <tfoot><tr><td colspan="2" class="newRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAdd">Add New File</a></td></tr></tfoot>
        </table>
      </td></tr>


      <tr><th>Weekly Meeting Agenda</th></tr>
      <tr><td class="nobordered" align="left" style="padding-left:5px;padding-top:3px;">
        <label class="checkbox-inline"><html:checkbox name="inputBean" property="staffMeetingInd"  value="Y"/>Add to Weekly Staff Meeting Agenda</label><br/>
        <label class="checkbox-inline"><html:checkbox name="inputBean" property="clientMeetingInd" value="Y"/>Add to Weekly Client Meeting Agenda</label><br/>
      </td></tr>


      <tr><th>Internal Notes</th></tr>
      <tr><td class="nobordered" align="left">
        <html:textarea name="inputBean" property="notes" rows="5" styleClass="form-control input-sm"/>
      </td></tr>
    </tbody>
    </table>

      <table id="borderlessTable" class="border-zero cellspacing-zero">
        <tbody>
          <tr>
            <td align="center">
              <button type="submit" value="Submit" class="btn btn-primary">
                <span class="glyphicon glyphicon-ok"></span> Save
              </button>
            </td>
            <td align="center">
              <a class="btn btn-default" href="project.do?action=taskList&projectPk=${projectPk}"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </html:form>

  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
  $(function() {
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();

    $( "#sortable" ).sortable();
    $( "#sortable" ).disableSelection();

    var newTrLine ='';
    newTrLine += '<tr>';
    newTrLine += '<input type="hidden" name="subTaskIdArr" value="">';
    newTrLine += '<input type="hidden" name="origPersonAssignedArr" value="">';
    newTrLine += '<td><span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>';
    newTrLine += '  <td><input type="text" name="descriptionArr" size="68"/></td>';
    newTrLine += '  <td><input type="text" name="dueDateArr" class="datepicker" size="9"/></td>';
    newTrLine += '  <td>';
    newTrLine += '    <select name="personAssignedArr">';
    newTrLine += '      <option value=""></option>';
<logic:iterate id="personAssigned" name="personAssignedList" type="java.lang.String">
    newTrLine += '        <option value="${personAssigned}">${personAssigned}</option>';
</logic:iterate>;
    newTrLine += '    </select>';
    newTrLine += '  </td>';
    newTrLine += '  <td>';
    newTrLine += '    <select name="statusArr">';
    newTrLine += '      <option value=""></option>';
<logic:iterate id="status" name="statusList" type="java.lang.String">
    newTrLine += '        <option value="${status}">${status}</option>';
</logic:iterate>
    newTrLine += '    </select>';
    newTrLine += '  </td>';
    newTrLine += '  <td><input type="text" name="completedDateArr" class="datepicker" size="9"/></td>';
    newTrLine += '  <td><input type="button" class="ibtnCopy" value="Duplicate"/></td>';
    newTrLine += '  <td><input type="button" class="ibtnDel" value="Delete"/></td>';
    newTrLine += '  <td><input type="button" class="ibtnAdd" value="Insert Row"/></td>';
    newTrLine += '</tr>';

    $('#subtask').on('click', '.ibtnAdd', function (event) {
      $(newTrLine).insertAfter($(this).closest('tr'));

      $(".datepicker").attr('autocomplete', 'off');
      $(".datepicker").datepicker();

      //Set focus
      $(this).closest('tr').next('tr').find('[name="descriptionArr"]').focus();
    });


    $('#subtask').on('click', '.ibtnAddAtEnd', function (event) {
      $(newTrLine).insertBefore($(this).closest('tr'));

      $(".datepicker").attr('autocomplete', 'off');
      $(".datepicker").datepicker();

      //Set focus
      $(this).closest('tr').prev('tr').find('[name="descriptionArr"]').focus();
    });

    $('#subtask').on('click', '.ibtnCopy', function (event) {
      $(this).closest('tr').clone().insertAfter($(this).closest('tr'));

      $(".datepicker").attr('autocomplete', 'off');
      $(".datepicker").datepicker();

      //Set focus
      $(this).closest('tr').next('tr').find('[name="descriptionArr"]').value();
    });

    $('#subtask').on('click', '.ibtnDel', function (event) {
      $(this).closest('tr').remove();
    });

    $('#newUploadFileTbl').on('click', '.ibtnAdd', function (event) {
      var newTrLine ='';
      newTrLine += '<tr>';
      newTrLine += '  <td><input type="file" name="fileList" size="70"/></td>';
      newTrLine += '  <td><input type="button" class="ibtnDel" value="Delete"/></td>';
      newTrLine += '</tr>';
      $('#newUploadFileTbl > tbody:last').append(newTrLine);
    });

    $('#newUploadFileTbl').on('click', '.ibtnDel', function (event) {
      $(this).closest('tr').remove();
    });
  });

  $(document).ready(function () {
    checkStatus(document.projectForm.currStatus);
    checkCategory(document.projectForm.currCategory);
    document.projectForm.category.focus();
  });

  function valFields() {
    var title           = stripSpaces(document.projectForm.title.value);
    var status           = document.projectForm.status.value;
    var completedDate   = stripSpaces(document.projectForm.completedDate.value);
    var category         = document.projectForm.category.value;
    var deployedDate     = document.projectForm.deployedDate.value;
    var docUpdatedInd   = stripSpaces(document.projectForm.docUpdatedInd.value);

    document.projectForm.title.value = title;
    document.projectForm.completedDate.value = completedDate;
    document.projectForm.deployedDate.value = deployedDate;

    if (title.length < 1) {
      alert("You must enter in a title.");
      document.projectForm.title.focus();
      return false;
    } //end of if

    if (status.length < 1) {
      alert("You must enter in a status.");
      document.projectForm.status.focus();
      return false;
    } else if ((status == 'Completed' || status == 'Not Needed') && completedDate.length < 1) {
      alert("You must enter in a completed date.");
      document.projectForm.completedDate.focus();
      return false;
    } else if (status == 'Completed' && category == 'Future Requests' && deployedDate == '') {
      alert("You cannot complete this Future Request until you enter in a deployed date.");
      document.projectForm.deployedDate.focus();
      return false;
    } else if (status == 'Completed' && category == 'Future Requests' && docUpdatedInd == 'N') {
      alert("You cannot complete this Future Request until the documentation has been updated (or is not applicable).");
      document.projectForm.docUpdatedInd.focus();
      return false;
    } //end of else

    if (deployedDate.length > 1 && (new Date(deployedDate) - new Date()) > 0) {
      alert("Future dates cannot be entered in for deployed date.");
      document.projectForm.deployedDate.focus();
      return false;
    } //end of if

    return true;
  } //end of valFields

  function checkNew(currObj, elementName) {
    if (currObj.value == 'null') {
      document.getElementsByName(elementName)[0].value = '';
      document.getElementsByName(elementName)[0].style.display = 'inline';
      document.getElementsByName(elementName)[0].focus();
    } else {
      document.getElementsByName(elementName)[0].value = currObj.value;
      document.getElementsByName(elementName)[0].style.display = 'none';
    } //end of if
  } //end of checkNew

  function checkStatus(currObj) {
    if (currObj.value == 'null') {
      document.projectForm.status.value = '';
      document.projectForm.status.style.display = 'inline';
      document.projectForm.status.focus();
      document.getElementById('completedField').style.display = 'none';
      document.getElementById('completedValue').style.display = 'none';
      document.getElementById('deployedDateReq').style.display = 'none';
      document.getElementById('docUpdatedIndReq').style.display = 'none';
    } else if (currObj.value == 'Completed' || currObj.value == 'Not Needed') {
      document.projectForm.status.value = currObj.value;
      document.projectForm.status.style.display = 'none';
      document.getElementById('completedField').style.display = 'block';
      document.getElementById('completedValue').style.display = 'block';
      var d = new Date();
      if (document.projectForm.completedDate.value == '') document.projectForm.completedDate.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();
      if (currObj.value == 'Completed' && document.projectForm.category.value == 'Future Requests') {
        document.getElementById('deployedDateReq').style.display = 'inline';
        document.getElementById('docUpdatedIndReq').style.display = 'inline';
      } //end of if
    } else { //Regular menu item
      document.projectForm.status.value = currObj.value;
      document.projectForm.status.style.display = 'none';
      document.getElementById('completedField').style.display = 'none';
      document.getElementById('completedValue').style.display = 'none';
      document.getElementById('deployedDateReq').style.display = 'none';
      document.getElementById('docUpdatedIndReq').style.display = 'none';
    } //end of if
  } //end of checkStatus

  function checkCategory(currObj) {
    if (currObj.value == 'null') {
      document.projectForm.category.value = '';
      document.projectForm.category.style.display = 'inline';
      document.projectForm.category.focus();
      document.getElementById('futureFeatureTable').style.display = 'none';
      document.getElementById('futureFeatureHead').style.display = 'none';
      document.getElementById('futureFeatureBody').style.display = 'none';
    } else if (currObj.value == 'Future Requests') {
      document.projectForm.category.value = currObj.value;
      document.projectForm.category.style.display = 'none';
      document.getElementById('futureFeatureTable').style.display = 'block';
      document.getElementById('futureFeatureHead').style.display = 'block';
      document.getElementById('futureFeatureBody').style.display = 'block';
    } else { //Regular menu item
      document.projectForm.category.value = currObj.value;
      document.projectForm.category.style.display = 'none';
      document.getElementById('futureFeatureTable').style.display = 'none';
      document.getElementById('futureFeatureHead').style.display = 'none';
      document.getElementById('futureFeatureBody').style.display = 'none';
    } //end of if
  } //end of checkCategory

  function completeAllSubTask() {
    $("select[name=statusArr]").each(function(idx) {
      $(this).val('Completed');
    });

    var d = new Date();

    $("input[name=completedDateArr]").each(function(idx) {
      $(this).val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());
    });
  } //end of completeAllSubTask
</script>

</body>
</html>
