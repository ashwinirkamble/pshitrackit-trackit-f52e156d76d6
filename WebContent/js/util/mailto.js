var MAILTO_URL_MAX_SIZE = 2000;
var emailDialog = null;

$(function() {
  setupEmailDialog();

  $('.email-dialog-input').on('change keyup paste', function() {
    onEmailDialogChange();
  });
});

/**
 * This requires a <div id="email-dialog"></div> to work 
 * @returns 0;
 */
function setupEmailDialog() {
  emailDialog = $("#email-dialog").dialog({
    autoOpen : false,
    width : 680, // height: 600,
    modal : true,
    show : { effect : "blind", duration : 400 },
    hide : { effect : "blind", duration : 400 },
  });
  return 0;
}

/**
 * Given an address, subject, and body. This will open a modal with a email
 * dialog.
 * @param addresses
 * @param subject
 * @param body
 * @returns 0.
 */
function showEmailDialog(addresses, subject, body) {
  var addressesInput = document.getElementById("dialog-addresses")
    , subjectInput = document.getElementById("dialog-subject")
    , bodyInput = document.getElementById("dialog-body")
  ;
  if (addressesInput && subjectInput && bodyInput) {
    addressesInput.value = addresses;
    subjectInput.value = subject;
    bodyInput.value = body;
    onEmailDialogChange();
    emailDialog.dialog("open");
  }
  else {
    alert("Email Dialog not setup.");
  }
  return 0;
}

function sendToEmailClient() {
  var formData = getEmailFormData();
  if (formData) {
    var addresses = formData.addresses
      , subject = formData.subject
      , body = formData.body
    ;
    sendEmail(addresses, subject, body);
  }
  return 0;
}
/**
 * This tracks the length of the mailto URL.
 * 
 * @returns 0.
 */
function onEmailDialogChange() {
  var mailtoSizeSpan = document.getElementById("email-dialog-mailto-size");
  if (mailtoSizeSpan) {
    var formData = getEmailFormData();
    if (formData) {
      var addresses = formData.addresses, subject = formData.subject, body = formData.body, mailToLink = 'mailto:' + addresses
          + '?subject=' + subject + '&body=' + formatToUrlFriendly(body), linkSize = mailToLink.length, message = linkSize;;
      if (linkSize > MAILTO_URL_MAX_SIZE) {
        message = linkSize + " (exceeds limit " + MAILTO_URL_MAX_SIZE + ") Body will be copied to clipboard."
      }
      mailtoSizeSpan.innerHTML = message;
    }
  }
  return 0;
}

/**
 * Given addresses, subject, and body, a mailto link is generated and sends the
 * user to that mailto link which usually results in the local email client
 * opening a new message.
 * @param addresses
 * @param subject
 * @param body
 * @returns 0.
 */
function sendEmail(addresses, subject, body) {
  var mailToLinkAddresses = 'mailto:' + addresses
    , mailToLinkSubject = '?subject=' + subject
    , mailToSubjectOnly = 'mailto:' + mailToLinkSubject
    , mailToLinkNoAddresses = mailToSubjectOnly + '&body=' + formatToUrlFriendly(body)
    , mailToLinkNoBody = mailToLinkAddresses + mailToLinkSubject
    , mailToLink = mailToLinkNoBody + '&body=' + formatToUrlFriendly(body)
  ;
  if (mailToLink.length < MAILTO_URL_MAX_SIZE) {
    window.location.href = mailToLink;
  }
  else if (mailToLinkNoAddresses.length < MAILTO_URL_MAX_SIZE) {
    var addressesInput = document.getElementById("dialog-addresses");
    if (addressesInput) {
      addressesInput.select();
      document.execCommand('copy');
      alert('Addresses line copied.');
    }
    window.location.href = mailToLinkNoAddresses;
  }
  else if (mailToLinkNoBody.length < MAILTO_URL_MAX_SIZE) {
    var bodyInput = document.getElementById('dialog-body');
    if (bodyInput) {
      bodyInput.select();
      document.execCommand('copy');
      alert('E-mail Body copied.');
    }
    window.location.href = mailToLinkNoBody;
  }
  else if (mailToLinkAddresses.length < MAILTO_URL_MAX_SIZE) {
    var subjectInput = document.getElementById("dialog-subject");
    if (subjectInput) {
      subjectInput.select();
      document.execCommand('copy');
      alert('Subject line copied.');
    }
    window.location.href = mailToLinkAddresses;
  }
  else if (mailToSubjectOnly.length < MAILTO_URL_MAX_SIZE) {
    var addressesInput = document.getElementById("dialog-addresses");
    if (addressesInput) {
      addressesInput.select();
      document.execCommand('copy');
      alert('Addresses line copied. Please copy-paste the body.');
    }
    window.location.href = mailToSubjectOnly;
  }
  else {
    alert("You need to copy-paste each part of the email manually.")
  }
  return 0;
}

function getEmailFormData() {
  var addressesInput = document.getElementById("dialog-addresses")
  , subjectInput = document.getElementById("dialog-subject")
  , bodyInput = document.getElementById("dialog-body")
  ;
  if (addressesInput && subjectInput && bodyInput) {
    var addresses = addressesInput.value
      , subject =  subjectInput.value
      , body = bodyInput.value
    ;
    return { addresses: addresses, subject: subject, body: body };
  }
  console.log("Email form data could not be retreived.")
  return null;
}

/**
 * Converts strings to a URL friendly version for the mailto URL.
 * @param string
 * @returns Formatted string.
 */
function formatToUrlFriendly(string) {
  if (!string) {
    return '';
  }
  return string.replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
}
