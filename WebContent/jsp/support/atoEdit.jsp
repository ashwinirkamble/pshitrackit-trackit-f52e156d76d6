<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Add/Edit ATO"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/>
<bean:define id="pageTitle" name="customPageTitle"/>
<logic:equal name="pageTitle" value="">
  <bean:define id="pageTitle" name="defaultPageTitle"/>
</logic:equal>

<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>
<jsp:useBean id="atoDetailList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="currFacetVersion"  scope="request" class="java.lang.String"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

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

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <p align="center">
    <html:form action="support.do" onsubmit="return valFields();" method="POST">
    <input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
    <% if (editType.equals("add")) { %>
      <input type="hidden" name="action" value="atoAddDo"/>
    <% } else { %>
      <input type="hidden" name="action" value="atoEditDo"/>
      <html:hidden name="inputBean" property="atoPk"/>
    <% } %>
    <table id="tanTable_style2" border="0" cellspacing="0" width="980">
    <tbody>
      <tr><th>Basic ATO Information</th></tr>
      <tr><td class="nobordered" align="center"> <!-- tan_table -->
        ATO Date: <html:text name="inputBean" property="atoDate" styleId="atoDate" styleClass="datepicker" size="9"/>
        Opened Date: <html:text name="inputBean" property="openedDate" styleClass="datepicker" size="9"/>
      </td></tr> <!-- end tan_table -->

      <tr class="emailBodyTr"><th>E-Mail Body</th></tr>
      <tr class="emailBodyTr"><td class="nobordered" align="center"> <!-- tan_table -->
        <textarea name="commentsArr" id="commentsArr" rows="10" class="form-control input-sm"><logic:present name="inputBean" property="commentsArr"><logic:iterate id="comments" name="inputBean" property="commentsArr"><bean:write name="comments"/></logic:iterate></logic:present></textarea><br/>
        Note: Do not use any &quot; character in the e-mail body as the e-mail may not be properly generated
      </td></tr> <!-- end tan_table -->

      <tr><th>Completed</th></tr>
      <tr><td class="nobordered" align="center"> <!-- tan_table -->
        <table id="completedTable" class="display" cellspacing="0">
        <thead>
          <tr>
            <th>Unit Assigned</th>
            <th>Homeport</th>
            <th>Opened Date</th>
            <th>Status</th>
            <th>Closed Date</th>
          </tr>
        </thead>
        <tbody>
        <logic:iterate id="resultBean" name="atoDetailList" type="com.premiersolutionshi.old.bean.SupportBean">
        <logic:equal name="resultBean" property="status" value="6 - Closed (Successful)">
          <tr style="vertical-align:top;" align="left">
            <html:hidden name="resultBean" property="shipName"/>
            <html:hidden name="resultBean" property="primaryPocEmails"/>
            <html:hidden name="resultBean" property="pocEmails"/>
            <td><a href="issue.do?id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>">
              <bean:write name="resultBean" property="shipName"/></a>
            </td>
            <td><bean:write name="resultBean" property="homeport"/></td>
            <td align="center"><bean:write name="resultBean" property="openedDate"/></td>
            <td><bean:write name="resultBean" property="status"/></td>
            <td align="center"><bean:write name="resultBean" property="closedDate"/></td>
          </tr>
        </logic:equal>
        </logic:iterate>
        </tbody>
        </table>
      </td></tr> <!-- end tan_table -->

      <tr><th>In Progress</th></tr>
      <tr><td class="nobordered" align="center"> <!-- tan_table -->
        <table id="inProgressTable" class="display" cellspacing="0">
        <thead>
          <tr>
            <th>Unit Assigned</th>
            <th>Homeport</th>
            <th>Opened Date</th>
            <th>Status</th>
            <th>E-Mail</th>
            <th>Reminder<br/>Sent</th>
            <th>ATO<br/>Installed</th>
            <th>Remove</th>
          </tr>
        </thead>
        <tbody>
        <logic:iterate id="resultBean" name="atoDetailList" type="com.premiersolutionshi.old.bean.SupportBean">
        <logic:notEqual name="resultBean" property="status" value="6 - Closed (Successful)">
          <tr style="vertical-align:top;" align="left">
            <html:hidden name="resultBean" property="shipName"/>
            <html:hidden name="resultBean" property="primaryPocEmails"/>
            <html:hidden name="resultBean" property="pocEmails"/>
            <td><a href="issue.do?id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>"><bean:write name="resultBean" property="shipName"/></a></td>
            <td><bean:write name="resultBean" property="homeport"/></td>
            <td align="center"><bean:write name="resultBean" property="openedDate"/></td>
            <td><bean:write name="resultBean" property="status"/></td>
            <td align="center"><button class="reminderEmailLink">Reminder E-Mail</button></td>
            <td align="center"><input type="checkbox" name="reminderIssuePkArr" value="<bean:write name="resultBean" property="issuePk"/>"/></td>
            <td align="center"><input type="checkbox" name="appliedIssuePkArr" value="<bean:write name="resultBean" property="issuePk"/>"/></td>
            <td align="center"><input type="checkbox" name="removeIssuePkArr" value="<bean:write name="resultBean" property="issuePk"/>"/></td>
          </tr>
        </logic:notEqual>
        </logic:iterate>
        </tbody>
        </table>
      </td></tr> <!-- end tan_table -->

      <tr><th>Select FACET Units to Add</th></tr>
      <tr><td class="nobordered" align="center"> <!-- tan_table -->

        <table id="availableTable" class="display" cellspacing="0">
        <thead>
          <tr>
            <th>Unit Assigned</th>
            <th>Homeport</th>
            <th>Computer Name</th>
            <th>E-Mail</th>
            <th>
              Include<br/>
              <a class="selectAllLink" href="javascript:void(0);">Select All</a><br/>
              <a class="emailAllLink" href="javascript:void(0);">E-Mail Selected</a>
            </th>
          </tr>
        </thead>
        <tbody>
        <% String currShipPk = "-1"; %>
        <logic:iterate id="resultBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
        <logic:notEmpty name="resultBean" property="shipName">
          <% if (!currShipPk.equals(resultBean.getShipPk())) { %>
            <tr style="vertical-align:top;" align="left">
              <td><b><bean:write name="resultBean" property="shipName"/></b></td>
              <td><bean:write name="resultBean" property="homeport"/></td>
              <td><b><bean:write name="resultBean" property="computerName"/></b></td>

              <input type="hidden" name="shipPkArr" value="<bean:write name="resultBean" property="shipPk"/>"/>
              <html:hidden name="resultBean" property="shipName"/>
              <html:hidden name="resultBean" property="primaryPocEmails"/>
              <html:hidden name="resultBean" property="pocEmails"/>

              <td align="center"><button class="standardEmailLink">E-Mail</button></td>
              <td align="center"><input type="checkbox" name="includeShipPkArr" value="<bean:write name="resultBean" property="shipPk"/>"/></td>
            </tr>
            <% currShipPk = resultBean.getShipPk(); %>
          <% } %>
        </logic:notEmpty>
        </logic:iterate>
        </tbody>
        </table>
          </td></tr> <!-- end tan_table -->

          <tr><td class="nobordered" align="center"> <!-- tan_table -->
            <table id="borderlessTable" border="0" cellspacing="0"><tbody>
              <tr>
                <td align="center">
                  <% if (editType.equals("add")) { %>
                    <html:submit value="Insert"/>
                  <% } else { %>
                    <html:submit value="Save"/>
                  <% } %>
                </td>
                <td align="center"><input type="button" onclick="window.location='support.do?action=atoList&projectPk=<bean:write name="projectPk"/>';" value="Cancel"/></td>
              </tr>
            </tbody></table>
      </td></tr> <!-- end tan_table -->
    </tbody>
    </table>
    </html:form>
    </p>

  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
  $(function() {
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();

    var table = $('#completedTable').DataTable( {
      paging: false,
      searching: false,
      stateSave: false
    });

    var table = $('#inProgressTable').DataTable( {
      paging: false,
      searching: false,
      columnDefs: [
        { "orderable": false, "targets": [4,5] }
      ],
      stateSave: false
    });

    var table = $('#availableTable').DataTable( {
      paging: false,
      searching: false,
      columnDefs: [
        { "orderable": false, "targets": [3,4] }
      ],
      stateSave: false
    });

    $('.standardEmailLink').on('click', function() {
      var d = new Date($('#atoDate').val());
      window.location.href = 'mailto:' + $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value') + '?subject=FACET Security Update - ATO_' + d.getFullYear() + padLeft((d.getMonth()+1),2) + padLeft(d.getDate(),2) + ' for ' + $(this).closest('tr').find('[name="shipName"]').attr('value') + '&body=' + $('#commentsArr').val().replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
      $(this).closest('tr').find('[name="includeShipPkArr"]').attr('checked', true);
      return false;
    });

    $('.reminderEmailLink').on('click', function() {
      var d = new Date($('#atoDate').val());
      window.location.href = 'mailto:' + $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value') + '?subject=FACET Security Update - ATO_' + d.getFullYear() + padLeft((d.getMonth()+1),2) + padLeft(d.getDate(),2) + ' for ' + $(this).closest('tr').find('[name="shipName"]').attr('value') + ' (Reminder)&body=' + $('#commentsArr').val().replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
      $(this).closest('tr').find('[name="reminderIssuePkArr"]').attr('checked', true);
      return false;
    });

    $('.selectAllLink').on('click', function() {
      $('[name="includeShipPkArr"]').each(function(key, value) {
        $(this).attr('checked', true);
      });
    });

    $('.emailAllLink').on('click', function() {
      var allemail = '';
      $('[name="includeShipPkArr"]').each(function(key, value) {
        var email = $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value');
        if (value.checked && email != '') {
          allemail += email + ';';
        } //end of if
      });

      if (allemail != '') {
        if (prompt('Copy all e-mail addresses to your clipboard and click OK to continue', allemail)) {
          var d = new Date($('#atoDate').val());
          window.location.href = 'mailto:?subject=FACET Security Update - ATOUpdates_' + d.getFullYear() + padLeft((d.getMonth()+1),2) + padLeft(d.getDate(),2) + '&body=' + $('#commentsArr').val().replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
        } //end of if
      } else {
        alert('No e-mail addresses found');
      } //end of else
    });
  });

  $(document).ready(function () {
    <% if (editType.equals("add")) { %>
      var d = new Date();
      document.supportForm.openedDate.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();

      $('#commentsArr').val('ALCON,\n\nThis is email is to inform you of the latest ATO updates for your FACET system.  You all will be receiving an AMRDEC notification to download the latest updates and instructions online. Please burn these files to a disk and follow the attached instructions on running the file. Running the update is critical to ensuring you have the most up to date patches and security precautions.\n\nAs a reminder, all the updates for your FACET system are located on the LOGCOP-FACET site as well.  It can be found in the black ribbon under the File Repository tab.\n\nLatest FACET.mdb version  (<bean:write name="currFacetVersion"/>)\nDefectiveMaterialReference.xls\n\nFeel free to contact us for any questions regarding this notice.\n\nV/r,\n\nFACET Support Team\nPremier Solutions HI\n808-396-4444');

    <% } %>
  });

  function valFields() {
    var atoDate = stripSpaces(document.supportForm.atoDate.value);
    var openedDate = stripSpaces(document.supportForm.openedDate.value);

    document.supportForm.atoDate.value = atoDate;
    document.supportForm.openedDate.value = openedDate;

    if (atoDate.length < 1) {
      alert('You must enter in an ATO date.');
      document.supportForm.atoDate.focus();
      return false;
    } //end of if

    if (openedDate.length < 1) {
      alert('You must enter in an opened date.');
      document.supportForm.openedDate.focus();
      return false;
    } //end of if

    return true;
  } //end of valFields
</script>

</body>
</html>
