<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<div class="card" style="margin: 0 auto;">
  <div class="card-body">
      <div class="row">
        <div class="col-md-4">
          <%@ include file="pocSearchBox.jsp" %>
        </div>
        <div class="col-md-6">
          <select class="form-control" onchange="handleSelectGroupPoc(this.value)"
            onload="${ship == null ? '' : 'this.value='.concat(ship.id)}" >
            <option value="">(Select a Group)</option>
            <option value="" disabled="disabled">-------------------- Organizations --------------------</option>
            <logic:iterate id="orgDropdownItem" name="organizationList" type="com.premiersolutionshi.common.domain.Organization">
              <option value="org-${orgDropdownItem.id}">${orgDropdownItem.name}</option>
            </logic:iterate>
            <option value="" disabled="disabled">-------------------- Units --------------------</option>
            <logic:iterate id="shipDropdownItem" name="unitPocList" type="com.premiersolutionshi.support.domain.Ship">
              <option value="ship-${shipDropdownItem.id}">${shipDropdownItem.fullName}</option>
            </logic:iterate>
          </select>
          <script type="text/javascript">
          var projectPk = ${projectPk};
          function handleSelectGroupPoc(value) {
            if (value !== '') {
              var arr = value.split('-');
              var pocType = arr[0];
              if (pocType === 'ship') {
                var shipFk = arr[1];
                window.location = "organization.do?action=unitPocList&projectPk=" + projectPk + '&shipFk=' + shipFk;
              }
              else if (pocType === 'org') {
                var orgFk = arr[1];
                window.location = "organization.do?action=view&projectPk=" + projectPk + '&id=' + orgFk;
              }
            }
          }
          </script>
        </div>
        <div class="col-md-2">
          <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
              id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="material-icons">settings</i>
            </a>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
              <a class="dropdown-item" href="poc.do?action=form&pocTypeCode=1&projectPk=${projectPk}">
                <i class="material-icons">add</i>
                Add New Organization POC
              </a>
              <a class="dropdown-item" href="poc.do?action=form&pocTypeCode=2&projectPk=${projectPk}">
                <i class="material-icons">add</i>
                Add New Unit POC
              </a>
            </div>
          </div>
        </div>
      </div>
  </div>
</div>