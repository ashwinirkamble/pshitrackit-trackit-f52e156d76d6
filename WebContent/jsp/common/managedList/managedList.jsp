<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="System Variables - ${selectedManagedList.name}"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}" />

<div class="row">
  <div class="col-sm-3"></div>
  <div class="col-sm-6">
    <div class="card">
      <div class="card-header card-header-info card-header-icon">
        <div class="card-icon">
          <i class="material-icons">settings</i>
        </div>
        <div style="display: inline-flex; float:left;">
          <h4 class="card-title">${selectedManagedList.name}</h4>
        </div>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col-sm-4">
            <table id="tanTable_style2" style="width:100%;">
              <thead>
                <tr>
                  <th>Managed Lists</th>
                </tr>
              </thead>
              <tbody>
                <logic:notEmpty name="managedLists">
                  <logic:iterate id="list" name="managedLists">
                  <tr>
                    <td style="vertical-align:middle;">
                      ${list.code == selectedManagedList.code ? '&#187;' : ''}
                      <a href="managedList.do?managedListCode=${list.code}${projectPk == null ? '' : '&projectPk='.concat(projectPk)}"
                        ${list.code == selectedManagedList.code ? 'class="disabled selected"' : ''}>
                        ${list.name}
                      </a>
                    </td>
                  </tr>
                  </logic:iterate>
                </logic:notEmpty>
                <logic:notEmpty name="projectPk">
                  <tr>
                    <td>
                      <a href="system.do?action=systemVariables&projectPk=${projectPk}">
                        Issue Categories
                      </a>
                    </td>
                  </tr>
                </logic:notEmpty>
              </tbody>
            </table>
          </div>
          <div class="col-sm-8">
            <table id="tanTable_style2" class="${selectedManagedList.useSortOrder ? 'sortable ' : ''}border-zero cellspacing-zero"
              style="width:100%;">
              <colgroup>
                <col width="260"/>
                <col width="30"/>
              </colgroup>
              <thead>
                <tr class="ignore">
                  <td colspan="2" align="center" class="noborder header" style="background:#fff;font-size:13px;font-weight:bold;vertical-align:middle;">
                    ${selectedManagedList.name}
                    ${selectedManagedList.global ? '<i class="material-icons" title="This list affects all projects.">language</i>' : ''}
                    <img id="sort-status" src="images/icon_delete.png" style="display:none;" height="20" width="20">
                  </td>
                </tr>
                <tr>
                  <th>${selectedManagedList.itemLabel}</th>
                  <th>Action</th>
                </tr>
              </thead>
              <logic:present name="itemList">
                <tfoot${selectedManagedList.useSortOrder ? ' class="ui-sortable"' : ''}>
                  <logic:iterate id="item" name="itemList" type="com.premiersolutionshi.common.domain.ManagedListItem">
                    <tr>
                      <td align="left">
                        <input type="hidden" name="sortPk" value="${item.id}"/>
                        <logic:equal name="selectedManagedList" property="useSortOrder" value="true">
                          <span class="ui-icon ui-icon-arrowthick-2-n-s"></span>
                        </logic:equal>
                        ${item.itemValue}
                      </td>
                      <td class="TOP" align="center">
                        <table><tr>
                          <logic:equal name="selectedManagedList" property="useCurrentDefault" value="true">
                            <td>
                              <% if (item.isCurrentDefault()) { %>
                                <span><i class="material-icons" title="Current/Default">star</i></span>
                              <% } else { %>
                                <span>
                                  <a href="managedList.do?projectPk=${projectPk}&action=currentDefault&id=${item.id}&managedListCode=${item.managedListCode}"
                                  ><i class="material-icons" title="Set Current/Default">star_border</i></a>
                                </span>
                              <% } %>
                            </td>
                          </logic:equal>
                          <td>
                            <a href="managedList.do?projectPk=${projectPk}&action=delete&id=${item.id}&managedListCode=${item.managedListCode}" onclick="return confirmDelete('${item.itemValue}');"
                              ><i class="material-icons">delete</i></a>
                          </td>
                        </tr></table>
                      </td>
                    </tr>
                  </logic:iterate>
                </tfoot>
              </logic:present>
            </table>
            <table>
              <tbody>
                <tr>
                  <td colspan="2" class="navbar-form">
                    <html:form action="managedList.do?projectPk=${projectPk}&managedListCode=${selectedManagedList.code}" method="POST" styleClass="form-horizontal">
                      <input type="hidden" name="action" value="save"/>
                      <input type="hidden" name="projectFk" value="${projectPk}"/>
                      <input type="hidden" name="managedListCode" value="${selectedManagedList.code}"/>
                      <input type="hidden" name="sortOrder" value="${itemList.size()}"/>
                      <table style="width:100%"><tr>
                        <td>
                          <input type="text" class="form-control" name="itemValue" id="new-managed-list-item-value"
                            placeholder="New ${selectedManagedList.itemLabel}"/>
                        </td>
                        <td>
                          <button type="submit" class="btn btn-success">Add</button>
                        </td>
                        </tr></table>
                    </html:form>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="col-sm-3"></div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/common/managedList.js"></script>
</body>
</html>

