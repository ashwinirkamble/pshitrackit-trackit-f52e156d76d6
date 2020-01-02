<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ attribute name="project" required="true" type="com.premiersolutionshi.common.domain.Project" %>
<%@ attribute name="pageTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parentUrl" required="false" type="java.lang.String" %>
<%@ attribute name="parentTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Url" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Title" required="false" type="java.lang.String" %>

<logic:notEmpty name="project">
  <tags:projectPkBreadcrumb projectPk="${project.id}" projectName="${project.projectName}"
    pageTitle="${pageTitle}"
    parentUrl="${parentUrl}" parentTitle="${parentTitle}"
    parent2Url="${parent2Url}" parent2Title="${parent2Title}"
    />
</logic:notEmpty>

<logic:empty name="project">
  <logic:present name="projectSelectList">
  <table>
    <tbody>
      <tr>
        <td><strong>Select Project:</strong> </td>
        <td>
          <select id="projectSelect" class="form-control"
            onchange="return handleSelectProject(this.value);">
            <option value="">(Select one)</option>
            <logic:iterate id="projectListItem" name="projectSelectList">
              <option value="${projectListItem.id}">
                ${projectListItem.projectName} (${projectListItem.customer})
              </option>
            </logic:iterate>
          </select>
        </td>
      </tr>
    </tbody>
  </table>
  <script type="text/javascript">
  var action = '${request.getAttribute("action")}';
  function handleSelectProject(projectPk) {
    window.location = window.location.pathname + "?projectPk=" + projectPk + (action === '' ? '' : '&action=' + action);
  }
  </script>
  </logic:present>
</logic:empty>