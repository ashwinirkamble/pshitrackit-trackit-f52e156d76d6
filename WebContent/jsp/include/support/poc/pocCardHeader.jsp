<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<div class="card-header card-header-info card-header-icon">
  <div class="card-icon">
    <i class="material-icons">import_contacts</i>
  </div>
  <div style="display: inline-flex; float:left;">
    <h4 class="card-title">${pageTitle}</h4>
  </div>
  <logic:notEmpty name="pocForm" property="id">
    <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
      <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
        id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        <i class="material-icons">settings</i>
      </a>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
        <logic:equal name="action" value="form">
          <a class="dropdown-item" href="${path}?action=view&projectPk=${projectPk}&id=${pocForm.id}">
            <i class="material-icons">search</i>
            View POC
          </a>
        </logic:equal>
        <logic:equal name="action" value="view">
          <a class="dropdown-item" href="poc.do?action=form&projectPk=${projectPk}&id=${pocForm.id}">
            <i class="material-icons">edit</i>
            Edit POC
          </a>
        </logic:equal>
        <a class="dropdown-item" onclick="return confirmDeletePoc(${pocForm.id}, '${pocForm.fullName}');"
          href="poc.do?action=delete&projectPk=${projectPk}&id=${pocForm.id}">
          <i class="material-icons">delete</i>
          Delete POC
        </a>
      </div>
    </div>
  </logic:notEmpty>
</div>