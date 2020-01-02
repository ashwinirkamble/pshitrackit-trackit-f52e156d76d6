<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Training Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>
<jsp:useBean id="projectList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="trainingMonthList" scope="request" class="java.util.ArrayList"/>

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
     .pshiBoxCnt {
       text-align: center;
       }
     .boxCnt {
       text-align: center;
       }
    .workflowTd {
      font-size: 80%;
      text-align: center;
      vertical-align: bottom;
      }

    .workflowTd select, .workflowTd input {
      font-size: inherit;
      }

    .workflowDate {
      text-align: center;
      }
   </style>
</head>
<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>
  
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="training.do?action=workflowSummary&projectPk=${projectPk}" parentTitle="Training Workflow List"/>
  
    <p align="center">
      Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <div class="center">
      <html:form action="training.do" onsubmit="return valFields();" method="POST">
        <div class="center">
          <input type="hidden" name="projectPk" value="${projectPk}"/>
          <html:hidden name="inputBean" property="shipName" styleId="shipName"/>
          <html:hidden name="inputBean" property="trainingWorkflowPk"/>
          <input type="hidden" name="action" value="workflowEditDo"/>
          <table id="tanTable_style2" class="border-zero cellspacing-zero">
            <tbody>
              <tr><th colspan="10">Training Workflow</th></tr>
              <tr><td colspan="10" class="nobordered" align="center">
                <div class="center">
                <table class="border-zero cellspacing-zero cellpadding-3">
                <tbody>
                  <tr>
                    <td class="fieldName">Ship</td>
                    <td><b>${inputBean.shipName}</b></td>
                  </tr>
                </tbody>
                </table>
                </div>
              </td></tr>
              <tr>
                <td class="workflowTd">
                  Location<br/>File<br/>Received<br/>
                  <html:text name="inputBean" property="locFileRecvDate" size="9" styleClass="workflowDate" styleId="firstWorkflow"/>
                </td>
                <td class="workflowTd">
                  Location<br/>File<br/>Reviewed<br/>
                <html:text name="inputBean" property="locFileRevDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report<br/>
                  <html:text name="inputBean" property="pacfltFoodReportDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  System<br/>Shipped<br/>
                  <html:text name="inputBean" property="systemReadyDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  Computer<br/>Name<br/>in Database<br/>
                  <html:text name="inputBean" property="computerNameDbDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  Computer<br/>Name<br/>Provided to<br/>DACS<br/>
                  <html:text name="inputBean" property="computerNameLogcopDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  Training Kit<br/>Ready<br/>
                  <html:text name="inputBean" property="trainingKitReadyDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd" style="background:#0aa;">
                  Estimated<br/>Training<br/>Month<br/>
                  <html:select name="inputBean" property="estTrainingMonth">
                    <html:option value=""/>
                    <html:options name="trainingMonthList"/>
                  </html:select>
                </td>
                <td class="workflowTd">
                  Scheduled<br/>Training<br/>Date<br/>
                  <html:text name="inputBean" property="schedTrainingDate" size="9" styleClass="workflowDate"/>
                </td>
                <td class="workflowTd">
                  Actual<br/>Training<br/>Date<br/>
                  <html:text name="inputBean" property="actualTrainingDate" size="9" styleClass="workflowDateWithLimit"/>
                </td>
              </tr>
              <tr>
                <td colspan="8" rowspan="5"></td>
                <td align="center">
                  Sched. Training Time<br/>
                  <html:text name="inputBean" property="schedTrainingTime" size="3" maxlength="4" style="font-size:inherit;text-align:center;"/><br/>
                  <span style="font-size:75%;">(HHMM)</span>
                </td>
                <td rowspan="5"></td>
              </tr>
              <tr>
                <td align="center">
                  <label>
                    <html:checkbox name="inputBean" property="clientConfirmedInd" value="Yes"/>
                    Confirmed By Client
                  </label>
                </td>
              </tr>
              <tr>
                <td align="center">
                  Primary Trainer<br/>
                  <html:select name="inputBean" property="trainerUserFk" style="font-size:inherit;">
                    <html:option value=""/>
                    <html:options collection="userList" property="userPk" labelProperty="fullName"/>
                  </html:select>
                </td>
              </tr>
              <tr>
                <td align="center">
                  Trainer Notes<br/>
                  <html:text name="inputBean" property="trainer" size="23" style="font-size:inherit;"/>
                </td>
              </tr>
              <tr>
                <td align="center">
                  Location<br/>
                  <html:text name="inputBean" property="schedTrainingLoc" styleId="homeport" size="23" style="font-size:inherit;"/>
                </td>
              </tr>
              <tr><th colspan="10">Comments</th></tr>
              <tr><td colspan="10" class="nobordered" align="center">
                <html:textarea name="inputBean" property="comments" rows="7" styleClass="form-control input-sm"/>
              </td></tr>
            </tbody>
          </table>
      
          <table id="borderlessTable" class="border-zero cellspacing-zero">
            <tbody>
              <tr>
                <td align="center">
                  <button type="submit" value="Submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Save</button>
                </td>
                <td align="center">
                  <a class="btn btn-default" href="training.do?action=workflowSummary&projectPk=${projectPk}">
                    <span class="glyphicon glyphicon-remove"></span> Cancel
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </html:form>
    </div>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
  $(function() {
    var homeportList = [<logic:iterate id="homeport" name="homeportList" type="java.lang.String"
        indexId="i">${i > 0 ? ',' : ''} "${homeport}"</logic:iterate>];

    $("#homeport").autocomplete({
      source: homeportList
    });

    $(".workflowDate").attr('autocomplete', 'off');
    $(".workflowDate").datepicker();
    $(".workflowDateWithLimit").datepicker(
      { maxDate: 0 } //forces selected date to be before or equal to today.
    );
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();

    //Workflow dates
    $(".workflowDate").on("change", function() {
      $('.workflowDate').each(function() {
        if ($(this).val().length > 0 && validateDate($(this).val(), "date")) {
          $(this).closest('td').attr('style', 'background:#fff;font-weight:normal;color:#000;');
        } else {
          $(this).closest('td').attr('style', 'background:#ccc;font-weight:normal;color:#000;');
        }
      });
    });
  });

  $(document).ready(function () {
    $("#firstWorkflow").change();
  });

  function checkNew(currObj, elementName) {
    if (currObj.value == 'null') {
      document.getElementsByName(elementName)[0].value = '';
      document.getElementsByName(elementName)[0].style.display = 'inline';
      document.getElementsByName(elementName)[0].focus();
    } else {
      document.getElementsByName(elementName)[0].value = currObj.value;
      document.getElementsByName(elementName)[0].style.display = 'none';
    }
  }

  function valFields() {
    var actualTrainingDate = document.trainingForm.actualTrainingDate;
    var actualTrainingDateStr = actualTrainingDate.value.trim();
    if (actualTrainingDateStr && actualTrainingDateStr != '') {
      var now = new Date();
      var date = new Date(actualTrainingDateStr);
      if (!(date.getFullYear() === now.getFullYear()
          && date.getMonth() === now.getMonth()
          && date.getDate() === now.getDate())
          && date.getTime() > now.getTime()
         ) {
        alert('"Actual Training Date" needs to be today or before today.');
        actualTrainingDate.focus();
        return false;
      }
    }
    return true;
  }
</script>

</body>
</html>
