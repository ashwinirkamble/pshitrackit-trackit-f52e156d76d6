<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods"%>

<table class="border-zero cellspacing-zero cellpadding-0" style="margin: 0 auto;">
<tbody>
  <tr style="vertical-align:top;">
    <td id="VESSEL_INFORMATION">
      <logic:present name="ship">
      <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width: 280px;">
      <thead>
        <tr><th>Vessel Information</th></tr>
      </thead>
      <tbody>
        <tr><td class="nobordered">
          <table id="borderlessTable" class="border-zero cellspacing-zero">
          <tbody>
            <tr>
              <td class="fieldName">Name:</td>
              <td class="fieldValue">${ship.shipName}</td>
            </tr>
            <tr>
              <td class="fieldName">Type/Hull:</td>
              <td class="fieldValue">${ship.type}&nbsp;${ship.hull}</td>
            </tr>
            <tr>
              <td class="fieldName">UIC:</td>
              <td class="fieldValue">${ship.uic}</td>
            </tr>
            <tr>
              <td class="fieldName">Homeport:</td>
              <td class="fieldValue">${ship.homeport}</td>
            </tr>
            <tr>
              <td class="fieldName">TYCOM:</td>
              <td class="fieldValue">${ship.tycomDisplay}</td>
            </tr>
            <tr>
              <td class="fieldName">RSupply Version:</td>
              <td class="fieldValue">${ship.rsupply}</td>
            </tr>
            <logic:present name="configuredSystemList">
            <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean" indexId="i">
            <% if (i.intValue() == 0) { %>
              <logic:notEmpty name="configuredSystemBean" property="decomDate">
                <tr>
                  <td class="fieldName">DECOM Date:</td>
                  <td style="color:red;font-weight:bold;">${configuredSystemBean.decomDate}</td>
                </tr>
              </logic:notEmpty>

              <% if (!CommonMethods.isEmpty(configuredSystemBean.getS2ClosureDate()) || !CommonMethods.isEmpty(configuredSystemBean.getFuelClosureDate())) { %>
                <tr>
                  <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                <logic:notEmpty name="configuredSystemBean" property="s2ClosureDate">
                  <tr>
                    <td class="fieldName">S-2 Galley Closed Until</td>
                    <td style="color:red;"><bean:write name="configuredSystemBean" property="s2ClosureDate"/></td>
                  </tr>
                </logic:notEmpty>
                <logic:notEmpty name="configuredSystemBean" property="fuelClosureDate">
                  <tr>
                    <td class="fieldName">Fuel Closed Until</td>
                    <td style="color:red;">${configuredSystemBean.fuelClosureDate}</td>
                  </tr>
                </logic:notEmpty>
                </tr>
              <% } %>
            <% } %>
            </logic:iterate>
            </logic:present>

            <logic:present name="shipLastVisitBean">
            <logic:notEmpty name="shipLastVisitBean" property="supportVisitDate">
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>
              <tr>
                <td class="fieldName">Last Support Visit:</td>
                <td><a href="issue.do?id=${shipLastVisitBean.issuePk}&projectPk=${projectPk}">${shipLastVisitBean.supportVisitDate} (${shipLastVisitBean.category})</a></td>
              </tr>
            </logic:notEmpty>
            </logic:present>

            <logic:notEmpty name="ship" property="shipPk">
              <tr>
                <td colspan="2">&nbsp;</td>
              </tr>
              <tr>
                <td class="fieldName">LOGCOP:</td>
                <td><a href="report.do?action=transmittalDetail&shipPk=${ship.shipPk}&projectPk=${projectPk}">View Transmittal Details</a></td>
              </tr>
            </logic:notEmpty>
          </tbody>
          </table>
        </td></tr>
      </tbody>
      </table>
      </logic:present>
    </td>

    <td id="CONFIGURED_SYSTEM">
      <logic:present name="configuredSystemList">
      <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
      <table id="tanTable_style2" class="border-zero cellspacing-zero"  style="width:430px;">
      <thead>
        <tr>
          <th>
            FACET Configured System
            <a href="system.do?action=configuredSystemDetail&configuredSystemPk=${configuredSystemBean.configuredSystemPk}&projectPk=${projectPk}">
              <img src="images/icon_view.png" height="12" title="View"/>
            </a>
            <a href="system.do?action=configuredSystemEdit&configuredSystemPk=${configuredSystemBean.configuredSystemPk}&projectPk=${projectPk}">
              <img src="images/icon_edit.png" height="12" title="Edit"/>
            </a>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr><td class="nobordered">
          <table id="borderlessTable" class="border-zero cellspacing-zero">
          <tbody>
            <tr>
              <td class="fieldName">Computer Name:</td>
              <td>
                ${configuredSystemBean.computerName}
                <logic:empty name="configuredSystemBean" property="computerName">--</logic:empty>
              </td>
            </tr>
            <tr>
              <td class="fieldName">Current Location:</td>
              <td>
                <b>${configuredSystemBean.location}</b>
                <logic:empty name="configuredSystemBean" property="location">--</logic:empty>
              </td>
            </tr>

            <tr><td colspan="2">&nbsp;</td></tr>

            <tr>
              <td class="fieldName" nowrap>FACET Version:</td>
              <td>
                <logic:notEmpty name="configuredSystemBean" property="facetVersion">
                  ${configuredSystemBean.facetVersion}
                  <logic:equal name="configuredSystemBean" property="facetVersion" value="${currFacetVersion}">
                    <img src="images/checkbox_checked.png"/>
                  </logic:equal>
                  <logic:notEqual name="configuredSystemBean" property="facetVersion" value="${currFacetVersion}">
                    <img src="images/icon_error.gif"/>
                  </logic:notEqual>
                </logic:notEmpty>
                <logic:empty name="configuredSystemBean" property="facetVersion">--</logic:empty>
              </td>
            </tr>

            <tr>
              <td class="fieldName" nowrap>OS Version:</td>
              <td>
                <logic:notEmpty name="configuredSystemBean" property="osVersion">
                  ${configuredSystemBean.osVersion}
                  <logic:equal name="configuredSystemBean" property="osVersion" value="${currOsVersion}">
                    <img src="images/checkbox_checked.png"/>
                  </logic:equal>
                  <logic:notEqual name="configuredSystemBean" property="osVersion" value="${currOsVersion}">
                    <img src="images/icon_error.gif"/>
                  </logic:notEqual>
                </logic:notEmpty>
                <logic:empty name="configuredSystemBean" property="osVersion">--</logic:empty>
              </td>
            </tr>

            <tr>
              <td class="fieldName" nowrap>DMS List as of:</td>
              <td>
                <logic:notEmpty name="configuredSystemBean" property="dmsVersion">
                  ${configuredSystemBean.dmsVersion}
                  
                  <logic:equal name="configuredSystemBean" property="dmsVersion" value="${currDmsVersion}">
                    <img src="images/checkbox_checked.png"/>
                  </logic:equal>
                  <logic:notEqual name="configuredSystemBean" property="dmsVersion" value="${currDmsVersion}">
                    <img src="images/icon_error.gif"/>
                  </logic:notEqual>
                </logic:notEmpty>
                <logic:empty name="configuredSystemBean" property="dmsVersion">--</logic:empty>
              </td>
            </tr>

            <tr><td colspan="2">&nbsp;</td></tr>

            <tr>
              <td class="fieldName" nowrap>Network Adapter Type:</td>
              <td>
                <logic:notEmpty name="configuredSystemBean" property="networkAdapter">
                  ${configuredSystemBean.networkAdapter}
                  <% if (configuredSystemBean.getNetworkAdapter().equals("MS Loopback Adapter")) { %>
                    <img src="images/checkbox_checked.png"/>
                  <% } else { %>
                    <img src="images/icon_error.gif"/>
                  <% } %>
                </logic:notEmpty>
                <logic:empty name="configuredSystemBean" property="networkAdapter">--</logic:empty>
              </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
              <td class="fieldName" nowrap>Admin password:</td>
              <td>
                ${configuredSystemBean.adminPassword}
                <logic:empty name="configuredSystemBean" property="adminPassword">--</logic:empty>
              </td>
            </tr>
          </tbody>
          </table>
        </td></tr>
      </tbody>
      </table>
      </logic:iterate>
      </logic:present>
    </td>
  </tr>

  <tr style="vertical-align:top;">
    <td id="SHIP_POCs">
      <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width:280px;">
      <thead>
        <tr>
          <th>
            POCs
            <logic:present name="ship">
              <a href="user.do?action=pocList&shipPk=${ship.shipPk}&projectPk=${projectPk}">
                <img src="images/icon_edit.png" height="12" title="Edit"/>
              </a>
            </logic:present>
          </th>
        </tr>
      </thead>
      <logic:present name="shipPocList">
      <tbody>
        <logic:empty name="shipPocList">
          <tr><td class="error" align="center">No POCs on record</td></tr>
        </logic:empty>
        <logic:notEmpty name="shipPocList">
          <tr><td class="nobordered">
            <table id="borderlessTable" class="border-zero cellspacing-zero">
            <tbody>
              <tr>
                <td colspan="2">
                  <a href="mailto:
                    <logic:iterate id="shipPocBean" name="shipPocList" type="com.premiersolutionshi.old.bean.UserBean">
                      <logic:notEmpty name="shipPocBean" property="email">${shipPocBean.email};</logic:notEmpty>
                    </logic:iterate>
                    "><img src="images/icon_email_send.png" height="15" width="15"/> E-Mail All</a>
                </td>
              </tr>

              <logic:iterate id="shipPocBean" name="shipPocList" type="com.premiersolutionshi.old.bean.UserBean">
                <logic:notEmpty name="shipPocBean" property="email">
                <tr>
                  <td>
                    <a href="mailto:${shipPocBean.email}">
                      ${shipPocBean.rank}
                      ${shipPocBean.firstName}
                      ${shipPocBean.lastName}
                    </a>
                  </td>
                  <td>${shipPocBean.workNumber}</td>
                </tr>
                </logic:notEmpty>
              </logic:iterate>
            </tbody>
            </table>
          </td></tr>
        </logic:notEmpty>
      </tbody>
      </logic:present>
      </table>
    </td>

    <td id="OPEN_ISSUE">
      <table id="tanTable_style2" class="border-zero cellspacing-zero alt-color" style="width:410px;">
      <thead>
        <tr>
          <th>
            Open Issues
            <logic:present name="ship">
              <a href="support.do?action=issueList&uic=${ship.uic}&projectPk=${projectPk}"><img src="images/icon_view.png" height="12" title="View"/></a>
              <a href="issue.do?action=add&shipPk=${ship.id}&projectPk=${projectPk}"><img src="images/icon_plus.gif" title="Add"/></a>
            </logic:present>
          </th>
        </tr>
      </thead>
      <logic:present name="shipIssueList">
      <tbody>
      <logic:empty name="shipIssueList">
        <tr><td class="error" align="center">No open issues found</td></tr>
      </logic:empty>
      <logic:notEmpty name="shipIssueList">
        <logic:iterate id="shipIssueBean" name="shipIssueList" type="com.premiersolutionshi.old.bean.SupportBean">
          <tr style="vertical-align:top;">
            <td>
              <a href="issue.do?id=${shipIssueBean.issuePk}&projectPk=${shipIssueBean.projectPk}"><b>Issue #${shipIssueBean.issuePk}</b></a>
              <logic:notEmpty name="shipIssueBean" property="openedDate"><i>(Opened ${shipIssueBean.openedDate})</i></logic:notEmpty>
              <br/>
              ${shipIssueBean.title}
            </td>
          </tr>
        </logic:iterate>
      </logic:notEmpty>
      </tbody>
      </logic:present>
      </table>

      <logic:present name="shipUpcomingVisitList">
        <logic:notEmpty name="shipUpcomingVisitList">
          <br/>
          <table id="tanTable_style2" class="alt-color border-zero cellspacing-zero" style="width:410px;">
            <thead>
              <tr><th>Upcoming Scheduled Support Visits</th></tr>
            </thead>
            <tbody>
            <logic:iterate id="shipVisitBean" name="shipUpcomingVisitList" type="com.premiersolutionshi.old.bean.SupportBean">
              <tr>
                <td align="left">
                  <a href="issue.do?id=${shipVisitBean.issuePk}&projectPk=${projectPk}">
                  ${shipVisitBean.supportVisitDate}
                  ${shipVisitBean.supportVisitTime}
                  </a>
                  <logic:notEmpty name="shipVisitBean" property="supportVisitLoc">
                    (${shipVisitBean.supportVisitLoc})
                  </logic:notEmpty>
                  - ${shipVisitBean.category}
                  <logic:notEmpty name="shipVisitBean" property="trainer">
                    <br/>Trainer: ${shipVisitBean.trainer}
                  </logic:notEmpty>
                </td>
              </tr>
            </logic:iterate>
            </tbody>
          </table>
        </logic:notEmpty>
      </logic:present>
    </td>
  </tr>
</tbody>
</table>