<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%-- POC List --%>
<logic:present name="pocList">
  <div class="card">
    <div class="card-header card-header-info card-header-icon">
      <div class="card-icon">
        <i class="material-icons">import_contacts</i>
      </div>
      <div style="display: inline-flex; float:left;">
        <h4 class="card-title">POCs</h4>
      </div>
      <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
        <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
          id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <i class="material-icons">settings</i>
        </a>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
          <logic:notEmpty name="organizationForm">
            <a class="dropdown-item" href="poc.do?action=form&pocTypeCode=1&projectPk=${projectPk}&organizationFk=${organizationForm.id}">
              <i class="material-icons">add</i>
              Add New "${organizationForm.name}" POC
            </a>
          </logic:notEmpty>
          <logic:notEmpty name="ship">
            <a class="dropdown-item" href="poc.do?action=form&pocTypeCode=2&projectPk=${projectPk}&shipFk=${ship.id}">
              <i class="material-icons">add</i>
              Add New "${ship.shipName}" POC
            </a>
          </logic:notEmpty>
        </div>
      </div>
    </div>
    <div class="card-body">
      <table class="pocTable">
        <thead>
          <tr>
            <th><i class="material-icons" style="font-size:20px;">check_circle</i></th>
            <th>Name</th>
            <th>E-mail</th>
            <th>Work Number</th>
            <th>Last Updated</th>
            <th${isSysadmin ? '' : ' style="display:none;"'}>Actions</th>
          </tr>
        </thead>
        <tbody>
          <logic:iterate id="poc" name="pocList">
            <tr>
              <td align="center">
                ${poc.primaryPoc ? '<i class="material-icons" style="font-size:20px;color:green;">check_circle</i>' : ''}
              </td>
              <td>
                <a href="poc.do?action=view&projectPk=${poc.projectFk}&id=${poc.id}">${poc.lastName}, ${poc.firstName}</a>
              </td>
              <td>
                <tags:emailLink value="${poc.email}"/>
                <logic:empty name="poc" property="email">-</logic:empty>
              </td>
              <td>${poc.workNumber} ${poc.workNumberExt == null || poc.workNumberExt <= 0 ? '' : ' x'.concat(poc.workNumberExt)}</td>
              <td align="center">${poc.lastUpdatedDateStr}</td>
              <td align="center"${isSysadmin ? '' : ' style="display:none;"'}>
                <a href="poc.do?action=form&projectPk=${projectPk}&id=${poc.id}">
                  <i class="material-icons">edit</i>
                </a>
                <a href="poc.do?action=delete&projectPk=${projectPk}&id=${poc.id}"
                  onclick="return confirmDeletePoc('${poc.id}');">
                  <i class="material-icons">delete</i>
                </a>
              </td>
            </tr>
          </logic:iterate>
        </tbody>
      </table>
    </div>
  </div>
</logic:present>