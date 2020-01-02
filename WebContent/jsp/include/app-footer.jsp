<%@ page import ="com.premiersolutionshi.old.util.CommonMethods" %>
<div id="app-footer">
  &copy;2019 Premier Solutions HI, LLC

  <% if (CommonMethods.isDevEnv()) { %>
    <br/><b>DEV_MODE ON</b>
  <% } %>
</div>

<%--Ashwini js to ./js--%>
<script type="text/javascript" src="js/jquery-1.10.2.min.js?build=${buildNumber}"></script>
<script type="text/javascript" src="js/jquery-ui.min.js?build=${buildNumber}"></script>
<script type="text/javascript" src="js/jquery/datatables/jquery.dataTables.min.js?v1.10.18&build=${buildNumber}"></script>
<%--Ashwini /theme to ./theme--%>
<script type="text/javascript" src="theme/material/js/core/popper.min.js?build=${buildNumber}"></script>
<script type="text/javascript" src="theme/material/js/core/bootstrap-material-design.min.js?build=${buildNumber}"></script>
<script type="text/javascript" src="theme/material/js/plugins/jquery.validate.v1.14.0.min.js?build=${buildNumber}"></script>
<script type="text/javascript" src="js/javascript.js?build=${buildNumber}"></script>
<script type="text/javascript">
var projectPk = '${projectPk}';
$(function() {
  setupIssueCountSetter();
});
</script>