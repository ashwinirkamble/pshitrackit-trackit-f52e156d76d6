
<%@ attribute name="id" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="btnType" required="false" %>

<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="${id}Label" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="${id}Label">${title}</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body"></div>
      <%-- $("#id > modal-dialog > modal-title").html("new modal title"); --%>
      <%-- $("#id > modal-dialog > modal-body").html("modal contents"); --%>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <%-- <button type="button" class="btn btn-primary">Save changes</button> --%>
      </div>
    </div>
  </div>
</div>