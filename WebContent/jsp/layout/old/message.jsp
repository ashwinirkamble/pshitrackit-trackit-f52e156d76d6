<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="com.premiersolutionshi.common.util.StringUtils"%>

<%--messageTypes: info, warning, danger --%>
<logic:notEmpty name="messageType">
  <div class="alert alert-${messageType} alert-dismissible" role="alert">
    ${message}
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"
      onclick="$('.alert').fadeOut(500);">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <script type="text/javascript">
    setInterval(function() {
      $('.alert').fadeOut(500);
      var newPath = window.location.href;
      newPath = newPath.replace(/&msg=[%a-zA-Z0-9\.]+/gi, "");
      newPath = newPath.replace(/msg=[%a-zA-Z0-9\.]+&/gi, "");
      newPath = newPath.replace(/&additional=[%a-zA-Z0-9\.\s]+/gi, "");
      newPath = newPath.replace(/additional=[%a-zA-Z0-9\.\s]+&/gi, "");
      history.pushState({}, "", newPath);
    }, 5000);
  </script>
</logic:notEmpty>