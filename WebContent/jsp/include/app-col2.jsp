<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import ="com.premiersolutionshi.old.util.CommonMethods" %>
<div class="col2">
<!-- Left Column 2 start -->
<% String path = request.getServletPath(); %>
<h2>Navigation</h2>
<ul>
  <% if (path.equals("/jsp/menu/index.jsp") || path.equals("/jsp/menu.jsp")) { %><li class="here"><% } else { %><li><% } %>
    <a href="menu.do">Home</a>
  </li>
  <li><a href="/wiki">Wiki</a></li>
  <% if (path.equals("/jsp/common/site/adminMenu.jsp")
      || path.equals("/jsp/changePassword.jsp")
  ) { %><li class="here"><% } else { %><li><% } %>
    <a href="page.do">Administration</a>
  </li>
</ul>

<h2>Projects</h2>
<ul>
<logic:present name="leftbar_projectList">
  <logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
    <logic:notEmpty name="leftbar_projectList">
      <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
        <% if (leftbar_resultBean.getProjectPk().equals(request.getParameter("projectPk"))) { %>
          <li class="here">
            <b><a href="page.do?projectPk=${leftbar_resultBean.projectPk}">${leftbar_resultBean.projectName}</a></b>
            <ul>
              <% if (path.equals("/jsp/support/issueList.jsp")
                  || path.equals("/jsp/support/issueDetail.jsp")
                  || path.equals("/jsp/support/issueEdit.jsp")
                  || path.equals("/jsp/support/issue/issueView.jsp")
                  || path.equals("/jsp/support/issue/issueAdmin.jsp")
                  || path.equals("/jsp/support/supportGenerateFeedbackForm.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                <a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">
                  Support Issues
                  <span id="issueCnt" class="badge badge-pill badge-primary issueCnt">0</span>
                  <span id="issueCntNotif" class="glyphicon glyphicon-exclamation-sign" style="color:red;display:none;"></span></a>
              </li>
              <logic:equal name="leftbar_resultBean" property="projectPk" value="1">
                <% if (path.equals("/jsp/support/issueListAll.jsp")
                    && request.getQueryString().startsWith("action=issueListAll")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="support.do?action=issueListAll&projectPk=${leftbar_resultBean.projectPk}">All</a>
                </li>
                <% if (path.equals("/jsp/support/issueListAll.jsp")
                    && request.getQueryString().startsWith("action=myIssueList")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="support.do?action=myIssueList&projectPk=${leftbar_resultBean.projectPk}">
                    My Issues <span id="myIssueCnt" class="badge badge-pill badge-primary myIssueCnt">0</span>
                    <span id="myIssueCntNotif" class="glyphicon glyphicon-exclamation-sign" style="color:red;display:none;">
                      <i class="material-icons">alert</i>
                    </span>
                  </a>
                </li>
                <% if (path.equals("/jsp/system/configuredSystemList.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="system.do?action=configuredSystemList&projectPk=${leftbar_resultBean.projectPk}">
                    <span>Configured Systems</span>
                  </a>
                </li>
              </logic:equal>
              <% if (path.equals("/jsp/project/taskList.jsp")
                  || path.equals("/jsp/project/taskDetail.jsp")
                  || path.equals("/jsp/project/taskEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                <a href="project.do?action=taskList&projectPk=${leftbar_resultBean.projectPk}">Task List</a>
              </li>
              <% if (path.equals("/jsp/support/poc/pocList.jsp")
                  || path.equals("/jsp/support/poc/pocForm.jsp")
                  || path.equals("/jsp/support/poc/pocSearch.jsp")
                  || path.equals("/jsp/support/poc/pocView.jsp")
                  || path.equals("/jsp/common/organization/organizationView.jsp")
                  || path.equals("/jsp/common/organization/organizationForm.jsp")
                  || path.equals("/jsp/common/organization/organizationList.jsp")
                  || path.equals("/jsp/common/organization/unitPocList.jsp")
                ) { %><li class="here2"><% } else { %><li><% } %>
                <a href="organization.do?projectPk=${leftbar_resultBean.projectPk}">POC List</a>
              </li>
            </ul>
          </li>
        <% } else { %>
          <li>
            <b><a href="page.do?projectPk=${leftbar_resultBean.projectPk}">${leftbar_resultBean.projectName}</a></b>
          </li>
        <% } %>
      </logic:iterate>
    </logic:notEmpty>
  </logic:iterate>
</logic:present>
</ul>
<%--

<ul>
<logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
  <li class="labelx">${leftbar_projectBean.customer}</li>
  <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
    <% if (leftbar_resultBean.getProjectPk().equals(request.getParameter("projectPk"))) { %>
      <li class="here">
        <logic:equal name="leftbar_resultBean" property="projectPk" value="1"> <!-- Default to Support Issue (for project 1 only) -->
          <b><a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">${leftbar_resultBean.projectName}</a></b>
        </logic:equal>
        <logic:notEqual name="leftbar_resultBean" property="projectPk" value="1"> <!-- Default to Task List -->
          <b><a href="project.do?action=taskList&searchPerformed=Y&projectPk=${leftbar_resultBean.projectPk}">${leftbar_resultBean.projectName}</a></b>
        </logic:notEqual>

        <ul>
          <logic:notEqual name="leftbar_resultBean" property="projectPk" value="1">
            <% if (path.equals("/jsp/project/taskList.jsp")
                || path.equals("/jsp/project/taskDetail.jsp")
                || path.equals("/jsp/project/taskEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="project.do?action=taskList&projectPk=${leftbar_resultBean.projectPk}">Task List</a>
            </li>

            <% if (path.equals("/jsp/user/pocList.jsp")
                || path.equals("/jsp/user/pocEdit.jsp")
                || path.equals("/jsp/user/shipPocEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="user.do?action=pocList&projectPk=${leftbar_resultBean.projectPk}">POC List</a>
            </li>

            <% if (path.equals("/jsp/support/issueList.jsp")
                || path.equals("/jsp/support/issueDetail.jsp")
                || path.equals("/jsp/support/issueEdit.jsp")
                || path.equals("/jsp/support/issue/issueView.jsp")
                || path.equals("/jsp/support/issue/issueAdmin.jsp")
                || path.equals("/jsp/support/supportGenerateFeedbackForm.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">Support Issues</a>
            </li>
          </logic:notEqual>

          <logic:equal name="leftbar_resultBean" property="projectPk" value="1">
            <% if (path.equals("/jsp/support/atoList.jsp")
                || path.equals("/jsp/support/atoEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="atoUpdate.do?action=list&projectPk=${leftbar_resultBean.projectPk}">ATO Updates</a>
            </li>

            <% if (path.equals("/jsp/support/bulkIssue/bulkIssue.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="bulkIssue.do?projectPk=${leftbar_resultBean.projectPk}">Bulk E-Mail Tool</a>
            </li>

            <% if (path.equals("/jsp/system/configuredSystemList.jsp")
                || path.equals("/jsp/system/configuredSystemEdit.jsp")
                || path.equals("/jsp/system/configuredSystemDetail.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="system.do?action=configuredSystemList&projectPk=${leftbar_resultBean.projectPk}">FACET Configured Systems</a>
            </li>

            <% if (path.equals("/jsp/user/pocList.jsp")
                || path.equals("/jsp/user/pocEdit.jsp")
                || path.equals("/jsp/user/shipPocEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="user.do?action=pocList&projectPk=${leftbar_resultBean.projectPk}">POC List</a>
            </li>

            <% if (path.equals("/jsp/support/shipVisitCalendar.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="support.do?action=shipVisitCalendar&projectPk=${leftbar_resultBean.projectPk}">Support Calendar</a>
            </li>

            <li>
              <a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">Support Issues</a>
              <ul>
                <% if (path.equals("/jsp/support/issueList.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">
                    Current <span id="issueCnt" class="badge badge-pill badge-primary">0</span>
                    <span id="issueCntNotif" class="glyphicon glyphicon-exclamation-sign" style="color:red;display:none;"></span>
                  </a>
                </li>
                <% if (path.equals("/jsp/support/issueListAll.jsp")
                    && request.getQueryString().startsWith("action=issueListAll")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="support.do?action=issueListAll&projectPk=${leftbar_resultBean.projectPk}">All</a>
                </li>
                <% if (path.equals("/jsp/support/issueListAll.jsp")
                    && request.getQueryString().startsWith("action=myIssueList")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="support.do?action=myIssueList&projectPk=${leftbar_resultBean.projectPk}">
                    My Issues <span id="myIssueCnt" class="badge badge-pill badge-primary">0</span>
                    <span id="myIssueCntNotif" class="glyphicon glyphicon-exclamation-sign" style="color:red;display:none;"></span></a>
                </li>
              </ul>
            </li>

            <% if (path.equals("/jsp/project/taskList.jsp")
                || path.equals("/jsp/project/taskDetail.jsp")
                || path.equals("/jsp/project/taskEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
              <a href="project.do?action=taskList&projectPk=${leftbar_resultBean.projectPk}">Task List</a>
            </li>

            <li>
              <span class="label2">LOGCOP Reports</span>
              <ul>
                <% if (path.equals("/jsp/dts/dtsUpload.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="dts.do?action=dtsUpload&projectPk=${leftbar_resultBean.projectPk}">Upload LOGCOP Reports</a>
                </li>
                <% if (path.equals("/jsp/report/missingTransmittal.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="report.do?action=missingTransmittal&projectPk=${leftbar_resultBean.projectPk}">Missing Transmittals</a>
                </li>
                <% if (path.equals("/jsp/report/transmittalSummary.jsp")
                    || path.equals("/jsp/report/transmittalDetail.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="report.do?action=transmittalSummary&projectPk=${leftbar_resultBean.projectPk}">Transmittal Summary</a>
                </li>
              </ul>
            </li>

            <li>
              <span class="label2">Workflows</span>
              <ul>
                <% if (path.equals("/jsp/backfile/backfileWorkflowSummary.jsp")
                    || path.equals("/jsp/backfile/backfileWorkflowEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="backfile.do?action=workflowSummary&projectPk=${leftbar_resultBean.projectPk}">Backfile Workflow</a>
                </li>

                <% if (path.equals("/jsp/training/trainingWorkflowSummary.jsp")
                    || path.equals("/jsp/training/trainingWorkflowEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="training.do?action=workflowSummary&projectPk=${leftbar_resultBean.projectPk}">Training Workflow</a>
                </li>

                <% if (path.equals("/jsp/decom/decomWorkflowSummary.jsp")
                    || path.equals("/jsp/decom/decomWorkflowEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="decom.do?action=workflowSummary&projectPk=${leftbar_resultBean.projectPk}">Decom Workflow</a>
                </li>
              </ul>
            </li>

            <li>
              <span class="label2">Hardware/Software</span>
              <ul>
                <% if (path.equals("/jsp/hardware/laptopList.jsp")
                    || path.equals("/jsp/hardware/laptopEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="hardware.do?action=laptopList&projectPk=${leftbar_resultBean.projectPk}">Laptops</a>
                </li>

                <% if (path.equals("/jsp/hardware/scannerList.jsp")
                    || path.equals("/jsp/hardware/scannerEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="hardware.do?action=scannerList&projectPk=${leftbar_resultBean.projectPk}">Scanners</a>
                </li>

                <% if (path.equals("/jsp/hardware/miscHardwareList.jsp")
                    || path.equals("/jsp/hardware/miscHardwareEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="hardware.do?action=miscList&projectPk=${leftbar_resultBean.projectPk}">Misc Hardware</a>
                </li>

                <% if (path.equals("/jsp/software/kofaxLicenseList.jsp")
                    || path.equals("/jsp/software/kofaxLicenseEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="software.do?action=kofaxLicenseList&projectPk=${leftbar_resultBean.projectPk}">Kofax Licenses</a>
                </li>

                <% if (path.equals("/jsp/software/bulkKofaxLicenseEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="software.do?action=bulkKofaxLicenseEdit&projectPk=${leftbar_resultBean.projectPk}">Bulk Kofax License Expiration</a>
                </li>

                <% if (path.equals("/jsp/software/vrsLicenseList.jsp")
                    || path.equals("/jsp/software/vrsLicenseEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="software.do?action=vrsLicenseList&projectPk=${leftbar_resultBean.projectPk}">VRS Licenses</a>
                </li>

                <% if (path.equals("/jsp/software/msOfficeLicenseList.jsp")
                  || path.equals("/jsp/software/msOfficeLicenseEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="software.do?action=msOfficeLicenseList&projectPk=${leftbar_resultBean.projectPk}">MS Office Licenses</a>
                </li>

                <% if (path.equals("/jsp/software/miscLicenseList.jsp")
                    || path.equals("/jsp/software/miscLicenseEdit.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                  <a href="software.do?action=miscLicenseList&projectPk=${leftbar_resultBean.projectPk}">Misc Software</a>
                </li>
              </ul>
            </li>

            <% if (path.equals("/jsp/common/managedList/managedList.jsp")
              || path.equals("/jsp/system/systemVariables.jsp")) { %><li class="here2"><% } else { %><li><% } %>
                <a href="managedList.do?projectPk=${leftbar_resultBean.projectPk}">System Variables</a>
            </li>
          </logic:equal>
        </ul>
      </li>
    <% } else { %>
      <logic:equal name="leftbar_resultBean" property="projectPk" value="1"> <!-- Default to Support Issue (for project 1 only) -->
        <li><b><a href="support.do?action=issueList&projectPk=${leftbar_resultBean.projectPk}">
          ${leftbar_resultBean.projectName}</a></b></li>
      </logic:equal>
      <logic:notEqual name="leftbar_resultBean" property="projectPk" value="1"> <!-- Default to Task List -->
        <li><b><a href="project.do?action=taskList&searchPerformed=Y&projectPk=${leftbar_resultBean.projectPk}">
          ${leftbar_resultBean.projectName}</a></b></li>
      </logic:notEqual>
    <% } %>
  </logic:iterate>
</logic:iterate>
</ul>
</logic:notEmpty>
</logic:present>

<% if (request.isUserInRole("sysadmin")) { %>
  <h2>Administration</h2>
  <ul>
    <% if (path.equals("/jsp/project/projectList.jsp")) { %><li class="here"><% } else { %><li><% } %>
      <a href="project.do?action=projectList">Manage Projects</a>
    </li>
    <% if (path.equals("/jsp/user/userList.jsp")
        || path.equals("/jsp/user/userEdit.jsp")) { %><li class="here"><% } else { %><li><% } %>
      <a href="user.do?action=userList">Manage Users</a>
    </li>
    <% if (path.equals("/jsp/ship/shipList.jsp")
        || path.equals("/jsp/ship/shipEdit.jsp")) { %><li class="here"><% } else { %><li><% } %>
      <a href="ship.do?action=shipList">Manage UIC List</a>
    </li>
    <% if (path.equals("/jsp/support/govProperty/govPropertyList.jsp")
        || path.equals("/jsp/support/govProperty/govPropertyForm.jsp")) { %><li class="here"><% } else { %><li><% } %>
      <a href="govProperty.do">Manage Gov Property</a>
    </li>
  </ul>
<% } else { %>
  <h2>UIC List</h2>
  <ul>
    <% if (path.equals("/jsp/ship/shipList.jsp")
        || path.equals("/jsp/ship/shipEdit.jsp")) { %><li class="here"><% } else { %><li><% } %>
      <a href="ship.do?action=shipList">View UIC List</a>
    </li>
  </ul>
<% } %>

<h2>User CP</h2>
<ul>
  <% if (path.equals("/jsp/user/ptoTravelList.jsp")
      || path.equals("/jsp/user/ptoTravelEdit.jsp")) { %><li class="here"><% } else { %><li><% } %>
    <a href="user.do?action=ptoTravelList">Manage PTO/Travel</a>
  </li>

  <% if (path.equals("/jsp/changePassword.jsp")) { %><li class="here"><% } else { %><li><% } %>
    <a href="login.do?action=changePassword">Change Password</a>
  </li>
</ul>
<!-- Left Column 3 end -->
 --%>
</div>
