<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>

<html:form method="POST" action="poc.do?action=search" styleId="pocSearchForm">
  <input type="hidden" name="action" value="search"/>
  <input type="hidden" name="projectFk" value="${projectPk}"/>
  <table><tbody><tr>
  <td><input type="text" name="searchText" class="form-control" placeholder="POC Search" value="${searchText}"/></td>
  <td><button type="submit" class="btn btn-primary"><i class="material-icons">search</i></button></td>
  </tbody></table>
</html:form>