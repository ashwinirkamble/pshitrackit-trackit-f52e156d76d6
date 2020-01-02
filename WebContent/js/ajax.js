/****************************************************************************
 * Function: createRequestObject                                            *
 ****************************************************************************/
function createRequestObject() {
  var ro;
  var browser = navigator.appName;
  if (browser == "Microsoft Internet Explorer") {
    ro = new ActiveXObject("Microsoft.XMLHTTP");
  } else {
    ro = new XMLHttpRequest();
  } //end of else
  return ro;
} //end of createRequestObject

/****************************************************************************
 * Variable: http                                                           *
 ****************************************************************************/
var http = createRequestObject();

/****************************************************************************
 * Function: ajaxRequest                                                    *
 ****************************************************************************/
function ajaxRequest(d) {
  http.open('POST', d);
  http.onreadystatechange = ajaxResponse;
  http.send(null);
} //end of ajaxRequest

/****************************************************************************
 * Function: ajaxResponse                                                   *
 ****************************************************************************/
function ajaxResponse() {
  if (http.readyState == 4) {
    var results = http.responseText;
    eval(results);
  } //end of if
} //end of ajaxResponse