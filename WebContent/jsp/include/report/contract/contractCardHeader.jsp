<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<div class="card-header card-header-info card-header-icon">
  <div class="card-icon">
    <i class="material-icons">assignment</i>
  </div>
  <div style="display: inline-flex; float:left;">
    <h4 class="card-title">${pageTitle}</h4>
  </div>
  <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
    <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
      id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <i class="material-icons">settings</i>
    </a>
    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
      <logic:notEmpty name="contractForm">
        <logic:notEmpty name="contractForm" property="id">
          <logic:equal name="action" value="form">
            <a class="dropdown-item" href="${path}?action=view&projectPk=${projectPk}&id=${contractForm.id}">
              <i class="material-icons">search</i>
              View Contract
            </a>
          </logic:equal>
          <logic:equal name="action" value="view">
            <a class="dropdown-item" href="contract.do?action=form&projectPk=${projectPk}&id=${contractForm.id}">
              <i class="material-icons">edit</i>
              Edit Contract
            </a>
          </logic:equal>
          <a class="dropdown-item" onclick="return confirmDelete(${contractForm.id}, '${contractForm.name}');"
            href="contract.do?action=delete&projectPk=${projectPk}&id=${contractForm.id}">
            <i class="material-icons">delete</i>
            Delete Contract
          </a>
        </logic:notEmpty>
      </logic:notEmpty>
      <a class="dropdown-item" href="contract.do?action=form&projectPk=${projectPk}">
        <i class="material-icons">add</i>
        Add New Contract
      </a>
    </div>
  </div>
</div>

