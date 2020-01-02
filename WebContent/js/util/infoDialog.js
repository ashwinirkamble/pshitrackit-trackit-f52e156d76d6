var infoDialog = null;

$(function() {
  setupInfoDialog();
});

/**
 * This requires a <div id="email-dialog"></div> to work 
 * @returns 0;
 */
function setupInfoDialog() {
  infoDialog = $("#info-dialog").dialog({
    autoOpen : false,
    width : 680, // height: 600,
    modal : true,
    show : { effect : "blind", duration : 400 },
  });
  return 0;
}

/**
 * Produces a title and content.
 * @param title
 * @param content
 * @returns 0.
 */
function showInfoDialog(title, content) {
  var contentDiv = document.getElementById("info-dialog-content");
  if (contentDiv) {
    contentDiv.innerHTML = content.replace(/(?:\r\n|\r|\n)/g, "<br>");
    //infoDialog.dialog({ title });
    infoDialog.dialog("open");
  }
  else {
    alert("Info Popup not setup.");
  }
  return 0;
}

