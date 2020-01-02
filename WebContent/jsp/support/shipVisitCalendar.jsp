<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Support Visit Calendar"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="currMonthYear" scope="request" class="java.lang.String"/>
<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="estSchedShipList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contractNumberList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="month" scope="request" class="java.lang.String"/>
<jsp:useBean id="year" scope="request" class="java.lang.String"/>

<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
</head>
<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

<div class="center">
<% if (contractNumberList.size() > 0) { %>
    <div style="width: 350px;">
      <form class="form-inline" action="support.do" method="GET">
        <input type="hidden" name="action" value="shipVisitCalendar"/>
        <input type="hidden" name="projectPk" value="${projectPk}"/>
        <input type="hidden" name="month" value="${month}"/>
        <input type="hidden" name="year" value="${year}"/>
        <input type="hidden" name="searchPerformed" value="Y"/>
        <div class="form-group">
          <label for="contractNumber">Contract Number:</label>
          <html:select name="inputBean" property="contractNumber" styleClass="form-control input-sm">
            <html:option value="">View All</html:option>
            <html:options name="contractNumberList"/>
          </html:select>
        </div>
        <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
      </form>
    </div><br/>
<% } %>
  <div class="CENTERED" style="margin:5px;">
    <a href="user.do?action=ptoTravelList">Manage PTO/Travel</a>
  </div>
</div>

    <div class="center">
    <table class="border-zero cellspacing-zero cellpadding-3">
    <tbody>
      <tr>
        <td style="width:25px" align="left"><logic:present name="prevMonthUrl"><a href="${prevMonthUrl}"><img src="images/arrow_left.png" height="16" style="width:16px"/></a></logic:present></td>
        <td style="font-size:150%;font-weight:bold;text-align:center;">${currMonthYear}</td>
        <td style="width:25px" align="right"><logic:present name="nextMonthUrl"><a href="${nextMonthUrl}"><img src="images/arrow_right.png" height="16" style="width:16px"/></a></logic:present></td>
      </tr>
    </tbody>
    </table>
    </div>

    <p align="center">
    <div class="center">
    <table id="calendar-table" class="border-zero cellspacing-zero">
    <thead>
      <tr>
        <th>Sun</th>
        <th>Mon</th>
        <th>Tue</th>
        <th>Wed</th>
        <th>Thu</th>
        <th>Fri</th>
        <th>Sat</th>
      </tr>
    </thead>
    <tbody>
      <logic:iterate id="weekBean" name="resultList" type="com.premiersolutionshi.old.bean.CalendarBean">
        <tr>
        <logic:iterate id="dayBean" name="weekBean" property="dayList" type="com.premiersolutionshi.old.bean.CalendarBean" indexId="i">
          <% if (CommonMethods.getDate("Month YYYY").equals(currMonthYear) && CommonMethods.cInt(CommonMethods.getDate("DD")) == CommonMethods.cInt(dayBean.getDate())) { %>
            <td class="current_day_top">
          <% } else if (CommonMethods.isEmpty(dayBean.getDate())) { %>
            <td class="filler_day_top">
          <% } else if (!CommonMethods.isEmpty(dayBean.getHoliday())) { %>
            <td class="holiday_top">
          <% } else { %>
            <td class="normal_day_top">
          <% } %>

            <logic:notEmpty name="dayBean" property="date">
              <!-- Day Box -->
              <div style="float:right;">
                <% if (i.intValue() == 0 || i.intValue() == 6) { %>
                  <div style="text-align:center;border:1px solid #ddd;color:#ddd;width:20px;">
                <% } else { %>
                  <div style="text-align:center;border:1px solid #000;width:20px;">
                <% } %>
                  ${dayBean.date}
                </div>
              </div><br clear="all"/>

              <span>
                <logic:notEmpty name="dayBean" property="holiday">
                  <b>${dayBean.holiday}</b><br/>
                </logic:notEmpty>

                <logic:present name="dayBean" property="lineItemList">
                <logic:iterate id="lineItemBean" name="dayBean" property="lineItemList" type="com.premiersolutionshi.old.bean.CalendarItemBean">
                  <p>
                  <logic:notEmpty name="lineItemBean" property="time"><span style="color:#00f;">${lineItemBean.time}</span>:</logic:notEmpty>

                  <a href="${lineItemBean.url}&projectPk=${projectPk}"><span style="${lineItemBean.cssStyle}" title="${lineItemBean.comments}">${lineItemBean.shipName}</span></a>
                  <logic:notEmpty name="lineItemBean" property="location"><br/><span style="color:#aaa;"><i>${lineItemBean.location}</i></span></logic:notEmpty>
                  </p>
                </logic:iterate>
                </logic:present>
              </span>
              <a href="user.do?action=ptoTravelAdd&startDate=${month}/${dayBean.date}/${year}&projectPk=${projectPk}" title="Add PTO/Travel from ${month}/${dayBean.date}/${year}">+ Add PTO/Travel</a>
            </logic:notEmpty>
          </td>
        </logic:iterate>
        </tr>
        <tr>
        <logic:iterate id="dayBean" name="weekBean" property="dayList" type="com.premiersolutionshi.old.bean.CalendarBean" indexId="i">
          <% if (CommonMethods.getDate("Month YYYY").equals(currMonthYear) && CommonMethods.cInt(CommonMethods.getDate("DD")) == CommonMethods.cInt(dayBean.getDate())) { %>
            <td class="current_day_bottom">
          <% } else if (CommonMethods.isEmpty(dayBean.getDate())) { %>
            <td class="filler_day_bottom">
          <% } else if (!CommonMethods.isEmpty(dayBean.getHoliday())) { %>
            <td class="holiday_bottom">
          <% } else { %>
            <td class="normal_day_bottom">
          <% } %>
          <logic:present name="dayBean" property="ptoTravelList">
          <logic:iterate id="lineItemBean" name="dayBean" property="ptoTravelList" type="com.premiersolutionshi.old.bean.CalendarItemBean">
            ${lineItemBean.lineItem}<br/>
          </logic:iterate>
          </logic:present>
          </td>
        </logic:iterate>
        </tr>
      </logic:iterate>
    </tbody>
    </table>
    </div>
    </p>

    <p align="left">
    <div class="center">
    <table id="tanTable_style2" class="border-zero cellspacing-zero">
    <tbody>
      <tr><th colspan="2">Legend</th></tr>
      <tr>
        <td style="color:#4bb2c5;">FACET Training</td>
        <td style="color:#eaa228;">Support Issue Visit</td>
      </tr>
    </tbody>
    </table>
    </div>
    </p>

    <logic:notEmpty name="estSchedShipList">
      <p align="left">
      Unscheduled FACET training estimated for <b>${currMonthYear}</b>
      </p>

      <logic:iterate id="shipBean" name="estSchedShipList" type="com.premiersolutionshi.old.bean.TrainingBean">
      <p align="left">
      <a href="training.do?action=workflowEdit&trainingWorkflowPk=${shipBean.trainingWorkflowPk}&projectPk=${projectPk}">
      ${shipBean.shipName}</a><br/>
      <span style="color:#777;">${shipBean.schedTrainingLoc}</span>
      </p>
      </logic:iterate>
    </logic:notEmpty>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript">
  $(function() {
    $(document).tooltip();
  });
</script>

</body>
</html>
