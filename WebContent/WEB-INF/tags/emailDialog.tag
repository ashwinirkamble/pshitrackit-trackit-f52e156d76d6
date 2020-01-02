
<div id="email-dialog" title="E-mail" style="display:none;">
  <form id="emailDialogForm">
    <table class="email-dialog-table">
      <tbody>
        <tr>
          <td><strong>To:</strong></td>
          <td><input type="text" class="email-dialog-input" id="dialog-addresses" /></td>
        </tr>
        <tr>
          <td><strong>Subject:</strong></td>
          <td><input type="text" class="email-dialog-input" id="dialog-subject" /></td>
        </tr>
        <tr>
          <td><strong>Body:</strong></td>
          <td><textarea class="email-dialog-input" id="dialog-body" rows="20"></textarea></td>
        </tr>
        <tr>
          <td colspan="2" align="center">
           <button onclick="sendToEmailClient(); return false;">Send to E-mail Client</button>
           <i>Mailto URL size: <span id="email-dialog-mailto-size">0</span></i>
         </td>
        </tr>
      </tbody>
    </table>
  </form>
</div>

<script type="text/javascript" src="js/util/mailto.js"></script>