
<%@ attribute name="modalId" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="btnType" required="false" %>

<button type="button" class="btn btn-${btnType ? btnType : 'primary'}" data-toggle="modal" data-target="#${modalId}">
  ${label}
</button>