var openedByList = [];
var homeportList = [];
var shipFkToCsListMap = {};

$(document).ready(function() {
  if (document.issueForm) {
    unescapeElem(document.issueForm.title);
    unescapeElem(document.issueForm.description);
    unescapeElem(document.issueForm.resolution);
    unescapeElem(document.issueForm.supportVisitLocNotes);
  }
});

$(function() {
  $("#openedBy").autocomplete({
    source: openedByList
  });

  //when saved as blank, the system inserts "0", let's clear that because validation is stopping it.
  if (document.issueForm.supportVisitTime.value === '0') {
    document.issueForm.supportVisitTime.value = '';
  }
  if (document.issueForm.supportVisitEndTime.value === '0') {
    document.issueForm.supportVisitEndTime.value = '';
  }

  $('[name="supportVisitLoc"]').autocomplete({
    source: homeportList
  });

  $(".datepicker").attr('autocomplete', 'off');
  $(".datepicker").datepicker();

  $('#shipPk').on('change', function() {
    shipPk = $(this).val();
    var configuredSystemFkSelect = $("#configuredSystemFk");
    if (shipPk != '') {
      $('.support-visit-section').show();
      //handle changing configured systems select dropdown.
      var csList = shipFkToCsListMap[shipPk];
      var configuredSystemCount = $('#configuredSystemCount');
      //clear all options and add blank option
      configuredSystemFkSelect.find('option').remove().end();
      configuredSystemCount.empty();
      if (csList && csList.length > 0) {
        var size = csList.length;
        //populate with configured systems
        configuredSystemCount.append('(' + size + ')');
        configuredSystemFkSelect.prop('disabled', false);
        configuredSystemFkSelect.append('<option value="0">N/A</option>');//(' + size + ' configured system' + (size>1?'s':'') + ' found)

        var containsFk = false;
        $.each(csList, function() {
          configuredSystemFkSelect.append($("<option />").val(this.id).text(this.computerName));
          if (this.id === configuredSystemFk) {
            containsFk = true;
          }
        });
        if (containsFk) {
          $('#configuredSystemFk > option').eq(configuredSystemFk).prop('selected', true);
          configuredSystemFkSelect.val(configuredSystemFk);
        }
        else if (size === 1 && !(originalShipPk && originalShipPk === shipPk)) {
          $('#configuredSystemFk > option').eq(csList[0].id).prop('selected', true);
          configuredSystemFkSelect.val(csList[0].id);
        }
      }
      else {
        configuredSystemFkSelect.val('');
        configuredSystemFkSelect.prop('disabled', true);
        configuredSystemFkSelect.append('<option value="">No configured systems found.</option>');
      }
    } else {
      $('#facetInfoTr').hide();
      $('.support-visit-section').hide();
      configuredSystemFkSelect.prop('disabled', 'disabled');
    }
    $('#status').change();
  });

  $('#isEmailSent').on('change', function() {
    if ($(this).prop("checked")) {
      $('.emailResponseTd').show();
    } else {
      $('.emailResponseTd').hide();
    }
  });

  $('#doUpdateConfiguredSystemVersion').on('change', function() {
    if ($(this).prop("checked")) {
      $('.update-configured-system-version').css('color', '#0a0');
    } else {
      $('.update-configured-system-version').css('color', '#ccc');
    }
  });

  $('input[name="isTrainingProvided"]').on('change', function() {
    if ($(this).val() == 'Y') {
      $('.followup-onsite').fadeIn(200);
    } else {
      $('.followup-onsite').fadeOut(200);
    }
  });

  $('#category').on('change', function() {
    var selectedValue = $(this).find('option:selected').text();
    if (selectedValue === 'Backfile') {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    }
    else if (selectedValue === 'Updates') {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    }
    else if (selectedValue === 'Follow-Up Training') {
      $('.followup-training').show();
      if ($('input[name="isTrainingProvided"]:checked').val() == 'Y') {
        $('.followup-onsite').show();
      }
      else {
        $('.followup-onsite').hide();
      }
      if ($('#status').val() === '6 - Closed (Successful)') {
        $('#supportVisitDateReq').show();
        $('#followUpTrainingProvidedReq').show();
      }
      else {
        $('#supportVisitDateReq').hide();
        $('#followUpTrainingProvidedReq').hide();
      }
    }
    else {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    }
  });

  $('#supportVisitDate').on('change', function() {
    if ($(this).val() != '') {
      $('#status').change();
      $('.support-visit-info').show();
      $('input[name="autoCloseDateStr"]').val('').prop('disabled', true);
      $('select[name="autoCloseStatus"]').prop('selectedIndex', 0).prop('disabled', true);
    } else {
      $('.support-visit-info').hide();
      $('input[name="autoCloseDateStr"]').prop('disabled', false);
      $('select[name="autoCloseStatus"]').prop('disabled', false);
    }
    $('input[name=supportVisitTime]').focus();
  });

  $('#newUploadFileTbl').on('click', '.addFileBtn', function (event) {
    var newTrLine ='';
    newTrLine += '<tr class="form-group">';
    newTrLine += '<td><input type="file" name="fileList" size="75"/></td>';
    newTrLine += '<td><input type="button" class="ibtnDel btn btn-danger" value="&#10005;"/></td>';
    newTrLine += '</tr>';
    $('#newUploadFileTbl > tbody:last').append(newTrLine);
  });

  $('#newUploadFileTbl').on('click', '.ibtnDel', function (event) {
    $(this).closest('tr').remove();
  });

  $('#commentsTable').on('click', '.addCommentBtn', function (event) {
    var newTrLine ='';
    newTrLine += '<tr>';
    newTrLine += '<td class="TOP"><textarea name="commentsArr" rows="5" class="form-control input-sm"';
    newTrLine += '  onchange="this.value=fixFunkyChars(this.value);"></textarea></td>';
    newTrLine += '<td class="TOP"><input type="button" class="ibtnDel btn btn-danger" value="&#10005;"/></td>';
    newTrLine += '</tr>';
    $('#commentsTable > tbody:first').prepend(newTrLine);
    document.getElementsByName("commentsArr")[0].focus();
  });

  $('#commentsTable').on('click', '.ibtnDel', function (event) {
    $(this).closest('tr').remove();
  });

  $('#status').on('change', function (event) {
    if ($(this).val() === '5 - Closed'
      || $(this).val() === '6 - Closed (Successful)'
      || $(this).val() === '7 - Closed (No Response)'
      || $(this).val() === '8 - Closed (Unavailable)'
    ) {
      $('.closedDateTd').show();
      $('.auto-close-info').hide();
      $('#resolution-row').show();
      $('#followUpTrainingProvidedReq').hide();
      var d = new Date();
      if ($('[name="closedDate"]').val() == null || $('[name="closedDate"]').val() == '')
        $('[name="closedDate"]').val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());

      if ($('#category :selected').text() === 'Follow-Up Training') {
        $('#supportVisitDateReq').show();
        $('#followUpTrainingProvidedReq').show();
      } else {
        $('#supportVisitDateReq').hide();
      }

      //Retrieve other ship issues when closing a ship issue with a scheduled support visit
      if ($(this).val() == '6 - Closed (Successful)' && $('#shipPk').val() != "" && $('#supportVisitDate').val() != "") {
        $.ajax({
          type: 'POST',
          url: 'ajax.do?action=shipCloseOtherIssues&projectPk=' + projectPk + '&shipPk=' + $('#shipPk').val()
            + (issuePk == '' ? '' : '&currIssuePk=' + issuePk),
          async: false
          }).responseText;
      } else {
        $('.close-related-issues').hide();
      }
    } else { //Regular menu item
      $('.closedDateTd').hide();
      $('.auto-close-info').show();
      $('[name="resolution"]').val('');
      $('[name="totalTime"]').val('');
      $('#resolution-row').hide();
      $('#supportVisitDateReq').hide();
      $('.close-related-issues').hide();
      $('#followUpTrainingProvidedReq').hide();
    }
  }).change();

  $('#priority').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('.priority-reason').show();
      $('#priority-high-banner').show();
      $('#priority-normal-banner').hide();
    } else {
      //Clear checkboxes/texts and hide
      $('input[name=priorityReasonArr]').each(function() {
        $(this).removeAttr('checked');
      });
      $('select[name=laptopIssue]').val('');
      $('select[name=scannerIssue]').val('');
      $('select[name=softwareIssue]').val('');
  
      $('select[name=laptopIssue]').hide();
      $('select[name=scannerIssue]').hide();
      $('select[name=softwareIssue]').hide();
  
      $('.priority-reason').hide();
      $('#priority-high-banner').hide();
      $('#priority-normal-banner').show();
    }
  });

  $('#laptop-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=laptopIssue]').fadeIn(200);
    } else {
      $('select[name=laptopIssue]').val('');
      $('select[name=laptopIssue]').fadeOut(200);
    }
  });

  $('#scanner-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=scannerIssue]').fadeIn(200);
    } else {
      $('select[name=scannerIssue]').val('');
      $('select[name=scannerIssue]').fadeOut(200);
    }
  });

  $('#software-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=softwareIssue]').fadeIn(200);
    } else {
      $('select[name=softwareIssue]').val('');
      $('select[name=softwareIssue]').fadeOut(200);
    }
  });

  $('#shipPk').val(shipPk).trigger('change');

  if ($('#shipPk').val() != '') {
    $('.support-visit-section').show();
  } else {
    $('.support-visit-section').hide();
    $('#facetInfoTr').hide();
  }

  if ($('#priority').prop('checked')) {
    $('.priority-reason').show();
    $('#priority-high-banner').show();
    $('#priority-normal-banner').hide();

    if ($('#laptop-issue').prop('checked')) {
      $('select[name=laptopIssue]').show();
    }
    if ($('#scanner-issue').prop('checked')) {
      $('select[name=scannerIssue]').show();
    }
    if ($('#software-issue').prop('checked')) {
      $('select[name=softwareIssue]').show();
    }
  }

  if ($('#category :selected').text() == 'Follow-Up Training') {
    $('.followup-training').show();

    if ($('input[name="isTrainingProvided"]:checked').val() == 'Y') {
      $('.followup-onsite').show();
    } else {
      $('.followup-onsite').hide();
    }

    if ($('#status').val() == '6 - Closed (Successful)') {
      $('#supportVisitDateReq').show();
    }
  } else {
    $('.followup-training').hide();
  }

  if ($('#isEmailSent').prop("checked")) {
    $('.emailResponseTd').show();
  } else {
    $('.emailResponseTd').hide();
  }

  if ($('#supportVisitDate').val() != '') {
    $('.support-visit-info').show();
    $('input[name="autoCloseDateStr"]').val('').prop('disabled', true);
    $('select[name="autoCloseStatus"]').prop('selectedIndex', 0).prop('disabled', true);
  } else {
    $('.support-visit-info').hide();
    $('input[name="autoCloseDateStr"]').prop('disabled', false);
    $('select[name="autoCloseStatus"]').prop('disabled', false);
  }

  if (document.issueForm) {
    handleAddNewSelect(document.issueForm.currPersonAssigned, 'personAssigned');
    handleAddNewSelect(document.issueForm.currTrainer, 'trainer');

    if (editType === "add" || editType === "copy") {
      var d = new Date();
      document.issueForm.openedBy.value = userFullName;
      document.issueForm.openedDateStr.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();
    }

    if (editType === "copy") {
      document.issueForm.closedDateStr.value = '';
    }
  }

  $.ajax({
    type: 'POST',
    url: 'ajax.do?action=shipComputerName&projectPk=' + projectPk + '&shipPk=' + $('#shipPk').val(),
    async: false
  }).responseText;
});

function addShipFkToCsListMap(shipFk, configuredSystem) {
  if (!shipFkToCsListMap[shipFk]) {
    shipFkToCsListMap[shipFk] = [];
  }
  shipFkToCsListMap[shipFk].push(configuredSystem);
}

function handleAddNewSelect(select, elementId) {
  var addNewInput = document.getElementById(elementId);
  if (!select || !elementId || !addNewInput) {
    return;
  }
  if (select.value === 'null') {
    addNewInput.value = '';
    addNewInput.style.display = 'inline';
    addNewInput.focus();
  } else {
    addNewInput.value = select.value;
    addNewInput.style.display = 'none';
  }
}

//old version
function checkNew(currObj, elementName) {
  if (currObj.value == 'null') {
    document.getElementsByName(elementName)[0].value = '';
    document.getElementsByName(elementName)[0].style.display = 'inline';
    document.getElementsByName(elementName)[0].focus();
  } else {
    document.getElementsByName(elementName)[0].value = currObj.value;
    document.getElementsByName(elementName)[0].style.display = 'none';
  }
}

function valFields() {
  var title          = stripSpaces(document.issueForm.title.value);
  var dept           = stripSpaces(document.issueForm.dept.value);
  var personAssigned = stripSpaces(document.issueForm.personAssigned.value);
  var trainer        = stripSpaces(document.issueForm.trainer.value);
  var openedBy       = stripSpaces(document.issueForm.openedBy.value);
  var openedDate     = stripSpaces(document.issueForm.openedDateStr.value);
  var closedDate     = stripSpaces(document.issueForm.closedDateStr.value);
  var resolution     = stripSpaces(document.issueForm.resolution.value);
  var status         = document.issueForm.status.value;
  var totalTime      = document.issueForm.totalTime.value;

  var e = document.getElementById("category");
  var category = e.options[e.selectedIndex].text;

  var supportVisitDate = stripSpaces(document.issueForm.supportVisitDateStr.value);
  var supportVisitTime = stripSpaces(document.issueForm.supportVisitTime.value);
  var supportVisitEndTime = stripSpaces(document.issueForm.supportVisitEndTime.value);
  var supportVisitLoc = stripSpaces(document.issueForm.supportVisitLoc.value);

  document.issueForm.title.value = title;
  document.issueForm.personAssigned.value = personAssigned;
  document.issueForm.trainer.value = trainer;
  document.issueForm.openedBy.value = openedBy;
  document.issueForm.openedDateStr.value = openedDate;
  document.issueForm.closedDateStr.value = closedDate;
  document.issueForm.resolution.value = resolution;
  document.issueForm.supportVisitDateStr.value = supportVisitDate;
  document.issueForm.supportVisitLoc.value = supportVisitLoc;

  if (title.length < 1) {
    alert('You must enter in a title.');
    document.issueForm.title.focus();
    return false;
  }

  if (category.length < 1) {
    alert('You must select a category.');
    document.issueForm.category.focus();
    return false;
  }

  if (personAssigned.length < 1) {
    alert('You must enter in a person assigned to this issue.');
    document.issueForm.personAssigned.focus();
    return false;
  }

  if (openedBy.length < 1) {
    alert('You must enter who the issue was opened by.');
    document.issueForm.openedBy.focus();
    return false;
  }

  if (openedDate.length < 1) {
    alert('You must enter in a opened date.');
    document.issueForm.openedDate.focus();
    return false;
  } else if (!validateDate(openedDate, 'Begin Date')) {
    document.issueForm.openedDateStr.focus();
    return false;
  }

  if (status == '5 - Closed' || status == '6 - Closed (Successful)' || status == '7 - Closed (No Response)' || status == '8 - Closed (Unavailable)') {
    //Closed date is required
    if (closedDate.length < 1) {
      alert('You must enter in a closed date.');
      document.issueForm.closedDateStr.focus();
      return false;
    } else if (!validateDate(closedDate, 'Closed Date')) {
      document.issueForm.closedDateStr.focus();
      return false;
    }
  }

  if (!document.issueForm.initiatedBy[0].checked && !document.issueForm.initiatedBy[1].checked) {
    alert('You must enter who the issue was initiated by.');
    return false;
  }

  //Validate "Division"
  if ($('[name=dept]:checked').val() == null) {
    alert('You must specify a Division.');
    document.getElementsByName('dept')[0].focus();
    return false;
  }
  if (document.issueForm.isTrainingProvided.checked && dept === 'N/A') {
    alert('You must specify which department the training was provided to.');
    return false;
  }

  if ($('[name="resolution"]').is(':visible') && resolution.length < 1) {
    alert('You must enter in a resolution.');
    document.issueForm.resolution.focus();
    return false;
  }

  if ($('[name="totalTime"]').is(':visible') && totalTime.length < 1) {
    alert('You must enter in the total time.');
    document.issueForm.totalTime.focus();
    return false;
  }

  if ($('select[name="laptopIssue"]').is(':visible') && $('select[name="laptopIssue"]').val() == '') {
    alert('You must select a laptop reason.');
    $('select[name="laptopIssue"]').focus();
    return false;
  }

  if ($('select[name="scannerIssue"]').is(':visible') && $('select[name="scannerIssue"]').val() == '') {
    alert('You must select a scanner reason.');
    $('select[name="scannerIssue"]').focus();
    return false;
  }

  if ($('select[name="softwareIssue"]').is(':visible') && $('select[name="softwareIssue"]').val() == '') {
    alert('You must select a software reason.');
    $('select[name="softwareIssue"]').focus();
    return false;
  }

  if (!validateSupportVisit(supportVisitDate, supportVisitTime, supportVisitEndTime)) {
    return false;
  }

  if (category === 'Follow-Up Training' && status === '6 - Closed (Successful)') {
    if (supportVisitDate.length < 1) {
      alert('You must enter in a support visit date.');
      document.issueForm.supportVisitDateStr.focus();
      return false;
    } else if (!validateDate(supportVisitDate, 'Begin Date')) {
      document.issueForm.supportVisitDateStr.focus();
      return false;
    }

    console.log($('input[name="isTrainingProvided"]:checked').val());

    if (!$('input[name="isTrainingProvided"]:checked').val()) {
      alert('You must enter Y/N for "Follow-Up Training Provided?"');
      //alert('You must specify if the follow-up training was provided.');
      document.getElementsByName('isTrainingProvided')[0].focus();
      return false;
    }
  }

  if ($('[name=supportVisitLoc]').is(':visible') && $('[name=supportVisitLoc]').val().length < 1) {
    alert('You must enter in a support visit location.');
    document.issueForm.supportVisitLoc.focus();
    return false;
  }

  if ($('[name=isTrainingOnsite]').is(':visible') && $('[name=isTrainingOnsite]:checked').val() == null) {
    alert('You must specify if the follow-up training was performed onsite.');
    document.getElementsByName('isTrainingOnsite')[0].focus();
    return false;
  }

  return true;
}

function validateSupportVisit(supportVisitDate, supportVisitTime, supportVisitEndTime) {
  if (supportVisitDate.length <= 0) {
    return true;
  }
  if (!checkAllNumbers(supportVisitTime)) {
    alert('Please enter in a valid support visit start time');
    document.issueForm.supportVisitTime.focus();
    return false;
  }

  //Fix malformed times
  if (supportVisitTime.length == 1) { //assume this is the hour
    supportVisitTime = '0' + supportVisitTime + '00';
  } else if (supportVisitTime.length == 2) { //assume this is the hour
    supportVisitTime = supportVisitTime + '00';
  } else if (supportVisitTime.length == 3) {
    supportVisitTime = '0' + supportVisitTime;
  }

  document.issueForm.supportVisitTime.value = supportVisitTime;

  if (parseInt(supportVisitTime) > 2359) {
    alert('Please enter in a valid support visit start time (0000-2359)');
    document.issueForm.supportVisitTime.focus();
    return false;
  }
  
  if (supportVisitEndTime.length > 0) {
    if (!checkAllNumbers(supportVisitEndTime)) {
      alert('Please enter in a valid support visit end time');
      document.issueForm.supportVisitEndTime.focus();
      return false;
    }
  
    //Fix malformed times
    if (supportVisitEndTime.length == 1) { //assume this is the hour
      supportVisitEndTime = '0' + supportVisitEndTime + '00';
    } else if (supportVisitEndTime.length == 2) { //assume this is the hour
      supportVisitEndTime = supportVisitEndTime + '00';
    } else if (supportVisitEndTime.length == 3) {
      supportVisitEndTime = '0' + supportVisitEndTime;
    }
  
    document.issueForm.supportVisitEndTime.value = supportVisitEndTime;
  
    if (parseInt(supportVisitEndTime) > 2359) {
      alert('Please enter in a valid support visit end time (0000-2359)');
      document.issueForm.supportVisitEndTime.focus();
      return false;
    }
  }
  
  //Case: End time is entered but start time is missing
  if (supportVisitEndTime.length > 0 && supportVisitTime.length == 0) {
    alert('Please enter in a support visit start time');
    document.issueForm.supportVisitTime.focus();
    return false;
  }
  
  //Case: End time is later than start time
  if (supportVisitTime.length > 0 && supportVisitEndTime.length > 0 && parseInt(supportVisitTime) >= parseInt(supportVisitEndTime)) {
    alert('Support visit end time must be later than the start time');
    document.issueForm.supportVisitEndTime.focus();
    return false;
  }
  return true;
}

function confirmDeleteIssue(issuePk) {
  if (confirm("Are you sure you want to delete issue #" + issuePk)) {
    window.location = "support.do?action=issueDeleteDo&issuePk=" + issuePk + "&projectPk=" + projectPk + "&pageFrom=" + pageFrom;
  }
}

var shortenedClassName = 'issue-comment-shortened';
function toggleCommentExpansion(issueCommentId, thisElem) {
  var elem = document.getElementById('issue-comment-' + issueCommentId);
  var isShortened = elem.className.includes(shortenedClassName);
  if (elem) {
    if (isShortened) {
      elem.className = elem.className.replace(/\bissue-comment-shortened\b/g, "");
    }
    else {
      elem.className += " " + shortenedClassName;
    }
  }
  if (thisElem) {
    //DOWN triangle &#9660    UP triangle &#9650
    thisElem.innerHTML = isShortened ? "&#9650;" : "&#9660;";
    thisElem.title = isShortened ? "Shorten comment display." : "Show the rest of the comment.";
  }
  return false;
}