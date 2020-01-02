<%@ page language="java" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

  <logic:empty name="projectPk">
    <logic:present name="projectSelectList">
    <div class="center">
      <table>
        <tbody>
          <tr>
            <td><strong>Select Project:</strong> </td>
            <td>
              <select id="projectSelect" class="form-control"
                onchange="handleSelectProject(this.value)">
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
    </div>

    <script type="text/javascript">
    var action = '${request.getParameter("action")}';
    function handleSelectProject(projectPk) {
      window.location = window.location.pathname + "?projectPk=" + projectPk + (action === '' ? '' : '&action=' + action);
    }
    </script>
  </logic:present></logic:empty>

  <logic:notEmpty name="project">
    <div class="pshi-title-sub center">${project.projectName} (${project.customer})</div>
  </logic:notEmpty>