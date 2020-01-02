<%@ page language="java"%>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="tags"  tagdir="/WEB-INF/tags"%>

<jsp:useBean id="atoUpdateForm" scope="request" class="com.premiersolutionshi.support.ui.form.AtoUpdateForm" />
<bean:define id="pageTitle" value="${editType == 'add' ? 'Add' : 'Edit'} ATO Update" />

<%@ include file="../../layout/old/layout-header.jsp" %>
<%--ashwini /atoUpdate to ./atoUpdate --%>
<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentUrl="./atoUpdate.do?action=list&projectPk=${projectPk}" parentTitle="ATO Update List"/>

<html:form action="atoUpdate.do" onsubmit="return valFields();" method="POST">
  <input type="hidden" name="projectPk" value="${projectPk}" />
  <input type="hidden" name="action" value="save" />
  <input type="hidden" id="title" value="FACET Security Update - ATOUpdates_" />
  <html:hidden name="atoUpdateForm" property="id" />
  <html:hidden name="atoUpdateForm" property="projectFk" />
  <div class="center">
    <table id="tanTable_style2" class="border-zero cellspacing-zero pshi-ato-update" style="width:740px;">
      <tbody>
        <tr>
          <th>Basic ATO Information</th>
        </tr>
        <tr>
          <td class="nobordered" align="center">
            ATO Date:<span class="regAsterisk">*</span>
            <html:text name="atoUpdateForm" styleId="atoDateStr" property="atoDateStr" styleClass="datepicker" size="10" />
            Opened Date:<span class="regAsterisk">*</span>
            <html:text name="atoUpdateForm" styleId="openedDateStr" property="openedDateStr" styleClass="datepicker" size="10" />
          </td>
        </tr>
        <tr class="emailBodyTr">
          <th>E-Mail Body</th>
        </tr>
        <tr class="emailBodyTr">
          <td class="nobordered" align="center">
            <html:textarea styleId="commentsInput" name="atoUpdateForm" property="comments" rows="10" styleClass="form-control input-sm"/>
            <br/>
            Note: Do not use any &quot; character in the e-mail body as the e-mail may not be properly generated
          </td>
        </tr>

        <logic:notEmpty name="atoUpdateForm" property="id">
          <tr>
            <th>Completed</th>
          </tr>
          <tr>
            <td class="nobordered" align="center">
              <table id="completedTable" class="display cellspacing-zero">
                <thead>
                  <tr>
                    <th>Unit Assigned</th>
                    <th>Homeport</th>
                    <th>Computer Name</th>
                    <th>Opened<br/>Date</th>
                    <th>Status</th>
                    <th>Closed<br/>Date</th>
                  </tr>
                </thead>
                <tbody>
                  <logic:iterate id="issue" name="atoIssueList" type="com.premiersolutionshi.support.domain.Issue">
                    <logic:equal name="issue" property="status" value="6 - Closed (Successful)">
                      <tr style="vertical-align: top;" align="left">
                        <bean:define id="ship" name="issue" property="ship" />
                        <html:hidden name="issue" property="computerName" />
                        <html:hidden name="ship" property="shipName" />
                        <html:hidden name="ship" property="primaryPocEmails" />
                        <html:hidden name="ship" property="pocEmails" />
                        <td><a href="issue.do?id=${issue.id}&projectPk=${projectPk}">${ship.shipName}</a></td>
                        <td>${ship.homeport}</td>
                        <td align="center">${issue.computerName}</td>
                        <td align="center">${issue.openedDateStr}</td>
                        <td>${issue.status}</td>
                        <td align="center">
                          <tags:formatDate pattern="MM/dd/yyyy" value="${issue.closedDate}" />
                        </td>
                      </tr>
                    </logic:equal>
                  </logic:iterate>
                </tbody>
              </table>
            </td>
          </tr>

          <tr>
            <th>In Progress</th>
          </tr>
          <tr>
            <td class="nobordered" align="center">
              <table id="inProgressTable" class="display cellspacing-zero">
                <thead>
                  <tr>
                    <th>Unit Assigned</th>
                    <th>Computer<br/>Name</th>
                    <th>Homeport</th>
                    <th>Opened<br/>Date</th>
                    <th style="width: 60px">Status</th>
                    <th style="width: 60px">E-Mail</th>
                    <th>Reminder<br />Sent</th>
                    <th>ATO<br />Installed
                    </th>
                    <th>Remove</th>
                  </tr>
                </thead>
                <tbody>
                  <logic:iterate id="issue" name="atoIssueList" type="com.premiersolutionshi.support.domain.Issue">
                    <logic:notEqual name="issue" property="status" value="6 - Closed (Successful)">
                      <bean:define id="ship" name="issue" property="ship" />
                      <tr style="vertical-align: top;" align="left">
                        <td><a href="issue.do?id=${issue.id}&projectPk=${projectPk}">${ship.shipName}</a></td>
                        <td align="center">${issue.computerName}</td>
                        <td>${ship.homeport}</td>
                        <td align="center">${issue.openedDateStr}</td>
                        <td>${issue.status}</td>
                        <td align="center"><button class="reminderEmailLink">Reminder E-Mail</button></td>
                        <td align="center"><input type="checkbox" name="reminderIssuePkArr" value="${issue.id}" /></td>
                        <td align="center"><input type="checkbox" name="appliedIssuePkArr" value="${issue.id}" /></td>
                        <td align="center"><input type="checkbox" name="removeIssuePkArr" value="${issue.id}" /></td>
                        <html:hidden name="issue" property="computerName" />
                        <html:hidden name="ship" property="shipName" />
                        <html:hidden name="ship" property="primaryPocEmails" />
                        <html:hidden name="ship" property="pocEmails" />
                      </tr>
                    </logic:notEqual>
                  </logic:iterate>
                </tbody>
              </table>
            </td>
          </tr>
        </logic:notEmpty>

        <tr>
          <td class="nobordered" align="center">
            <table id="borderlessTable" class="border-zero cellspacing-zero">
              <tbody>
                <tr>
                  <td align="center">
                    <html:submit value="${editType.equals('add') ? 'Insert' : 'Save'}" />
                  </td>
                  <td align="center">
                    <input type="button" onclick="window.location='atoUpdate.do?action=list&projectPk=${projectPk};" value="Cancel" />
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>

        <tr>
          <th>Select FACET Units to Add</th>
        </tr>
        <tr>
          <td class="nobordered" align="center">
            <table id="availableTable" class="display cellspacing-zero">
              <thead>
                <tr>
                  <th>Unit Assigned</th>
                  <th>Computer Name</th>
                  <th>Homeport</th>
                  <th style="width: 60px">E-Mail</th>
                  <th>Include<br />
                    <div><input type="checkbox" id="selectAll" title="Select All" /></div>
                    <a id="emailAllLink" href="javascript:void(0);">E-Mail Selected</a>
                  </th>
                </tr>
              </thead>
              <tbody>
                <logic:iterate id="configuredSystem" name="configuredSystemList"
                  type="com.premiersolutionshi.support.domain.ConfiguredSystem">
                  <bean:define id="ship" name="configuredSystem" property="ship" />
                  <bean:define id="laptop" name="configuredSystem" property="laptop" />
                  <logic:notEmpty name="configuredSystem" property="shipFk">
                    <tr style="vertical-align: top;" align="left">
                      <td><b><bean:write name="ship" property="shipName" /></b></td>
                      <td><b><bean:write name="laptop" property="computerName" /></b></td>
                      <td><bean:write name="ship" property="homeport" /></td>
                      <td align="center" style="width: 60px">
                        <button class="email-standard-dialog-btn">E-Mail</button>
                        <html:hidden name="laptop" property="computerName" />
                        <html:hidden name="ship" property="shipName" />
                        <html:hidden name="ship" property="pocEmails" />
                        <html:hidden name="ship" property="primaryPocEmails" />
                      </td>
                      <td align="center">
                        <input type="checkbox" name="includeConfiguredSystemPkArr" value="${configuredSystem.id}" />
                        <input type="hidden" name="configuredSystemPkArr" value="${configuredSystem.id}" />
                      </td>
                    </tr>
                  </logic:notEmpty>
                </logic:iterate>
              </tbody>
            </table>
          </td>
        </tr>
        <tr>
          <td class="nobordered" align="center">
            <table id="borderlessTable" class="border-zero cellspacing-zero">
              <tbody>
                <tr>
                  <td align="center">
                    <html:submit value="${editType.equals('add') ? 'Insert' : 'Save'}" />
                  </td>
                  <td align="center">
                    <input type="button" onclick="window.location='atoUpdate.do?action=list&projectPk=${projectPk};" value="Cancel" />
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</html:form>
<div>Fields marked with an asterisk <span class="regAsterisk">*</span> are required.</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<tags:emailDialog />

<script type="text/javascript" src="js/support/includeConfiguredSystemPkArr.js"></script>
<script type="text/javascript" src="js/support/atoUpdate.js"></script>
<script type="text/javascript">
$(document).ready(function() {
  <logic:equal name="editType" value="add">
    var d = new Date();
    document.atoUpdateForm.openedDateStr.value = (d.getMonth() + 1) + '/' + d.getDate() + '/' + d.getFullYear();
    document.getElementById('commentsInput').value = emailBody;
  </logic:equal>
});

var emailBody = 'ALCON,\n\nThis is email is to inform you of the latest ATO updates for your FACET system. '
  + 'You all will be receiving an AMRDEC notification to download the latest updates and instructions online. '
  + 'Please burn these files to a disk and follow the attached instructions on running the file. '
  + 'Running the update is critical to ensuring you have the most up to date patches and security precautions.\n\n'
  + 'As a reminder, all the updates for your FACET system are located on the LOGCOP-FACET site as well.  '
  + 'It can be found in the black ribbon under the File Repository tab.\n\n'
  + 'Latest FACET.mdb version  (${currFacetVersion})\n'
  + 'DefectiveMaterialReference.xls\n\nFeel free to contact us for any questions regarding this notice.\n\nV/r,\n\n'
  + 'FACET Support Team\n'
  + 'Premier Solutions HI\n'
  + '808-396-4444';
</script>

</body>
</html>
