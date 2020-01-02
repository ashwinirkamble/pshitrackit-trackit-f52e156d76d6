Array.prototype.clear = function() {
  while (this.length > 0) this.pop();
};

String.prototype.trim = function() {
  return this.replace(/^\s+|\s+$/g,"");
}

String.prototype.ltrim = function() {
  return this.replace(/^\s+/,"");
}

String.prototype.rtrim = function() {
  return this.replace(/\s+$/,"");
}

function unescapeElem(inputElem) {
  inputElem.value = unescapeStr(inputElem.value)
}

if (!String.prototype.startsWith) {
  Object.defineProperty(String.prototype, 'startsWith', {
    value: function(search, pos) {
      return this.substr(!pos || pos < 0 ? 0 : +pos, search.length) === search;
    }
  });
}
if (!String.prototype.includes) {
  Object.defineProperty(String.prototype, 'includes', {
    value: function(search, start) {
      if (typeof start !== 'number') {
        start = 0
      }
      
      if (start + search.length > this.length) {
        return false
      } else {
        return this.indexOf(search, start) !== -1
      }
    }
  })
}

function fixFunkyChars(str) {
  return str.replace(/[″“”]/g, '"')
    .replace(/[′’]/g, "'")
  ;
}

function unescapeStr(str) {
  return str.replace(/&amp;/g, "&")
    .replace(/&lt;/g, "<")
    .replace(/&gt;/g, ">")
    .replace(/&quot;/g, '"')
    .replace(/&#x27;/g, "'")
    .replace(/&#x2F;/g ,"/")
    ;
}

function stripSpaces(x) {
  while (x.substring(0,1) == ' ') x = x.substring(1);
  while (x.substring(x.length-1,x.length) == ' ') x = x.substring(0,x.length-1);
  return x;
}

function changeSort(sortBy, currSortBy, currSortDir) {
  var sortDir = '';
  document.sortForm.sortBy.value = sortBy;
  if (sortBy == currSortBy) {if (currSortDir == "ASC") {sortDir = "DESC";} else {sortDir = "ASC";}} else {sortDir = "ASC";}
  document.sortForm.sortDir.value = sortDir;
  document.sortForm.submit();
}

function nvl(value, nullValue) {
  if (value != "") {
    return value;
  } else {
    return nullValue;
  }
}

//see also: https://stackoverflow.com/questions/233507/how-to-log-out-user-from-web-site-using-basic-authentication
function logout() {
  try {
    document.execCommand("ClearAuthenticationCache");
    window.location.href('/');
  } catch (e) {}
}

function padLeft(nr, n, str){
    return Array(n-String(nr).length+1).join(str||'0')+nr;
}

function confirmDelete(id, name) {
  name = name || '';
  return confirm("Are you sure you want to delete #" + id + (name === '' ? '' : " (" + name + ")") + "?");
}

var updtCnt = 0, updtId = -1;
function updtIssueCount() {
  if (projectPk != '') {
    $.ajax({
      type: "POST",
      url: "ajax.do?action=updateIssueCount&projectPk=" + projectPk,
      async: false
    }).responseText;
    updtCnt++;
    // Repeat only 30 times
    if (updtCnt >= 30) {
      clearInterval(updtId);
    }
  }
}

function setupIssueCountSetter() {
  if ($('#issueCnt').length && $('#myIssueCnt').length) {
    updtIssueCount();
  $('#issueCnt').show();
  $('#myIssueCnt').show();
    updtId = setInterval(function(){ updtIssueCount(); }, 60 * 1000); //every 60 secs
  }
}
$(function() {
  var table = $('.datatable').DataTable({
    paging : false,
    searching : true,
    stateSave : false
  });
});

