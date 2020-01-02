
/****************************************************************************
 * Function: degToRads
 ****************************************************************************/
function degToRads(deg) {
  return (parseFloat(deg * Math.PI / 180));
} //end of degToRads

/****************************************************************************
 * Function: getDistance
 ****************************************************************************/
function getDistance(Lat1, Long1, Lat2, Long2, Unit) {
  var x, km
  x = Math.acos((Math.sin(degToRads(Lat1)) * Math.sin(degToRads(Lat2)) + Math.cos(degToRads(Lat1)) * Math.cos(degToRads(Lat2)) * Math.cos(Math.abs((degToRads(Long2))-(degToRads(Long1))))));
  km = 1.852 * 60.0 * ((x/Math.PI)*180);

  switch(Unit.toLowerCase()) {
    case "mi":
      return (km / 1.609344);
      break;
    case "nmi":
      return (km / 1.852);
      break;
    default:
      return km;
  } //end of switch
} //end of getDistance

/****************************************************************************
 * Function: isNumeric
 ****************************************************************************/
function isNumeric(sText) {
  var validChar = "1234567890";
  var isNumber = true;
  var i = 0;

  while (i < sText.length && isNumber) {
    isNumber = (validChar.indexOf(sText.charAt(i)) > -1);
    i++;
  } //end of while

  return isNumber;
} //end of isNumeric

/* ========================================================================
 *  isNumericWithMsg - see DataEntry jsps
 * ========================================================================
 */
function isNumericWithMsg(elem, msg) {
  var strValidChars = "0123456789";
  var strChar;
  var blnResult = true;
  var i = 0;
  var val = document.getElementById(elem).value;

  if (val.length == 0)
    blnResult = false;

  for (i = 0; i < val.length && blnResult == true; i++) {
    strChar = val.charAt(i);
    if (strValidChars.indexOf(strChar) == -1) {
      blnResult = false;
    }
  }

  if (!blnResult) {
    alert(msg);
    //document.getElementById(elem).value = "000";
  }

  return blnResult;
}


/*
 * ========================================================================
 * isNumericPosNegWithMsg - see DataEntry jsps
 * ========================================================================
 */
function isNumericPosNegWithMsg(elem, msg) {
  var strValidChars = "0123456789";
  var strChar;
  var blnResult = true;
  var i = 0;
  var val = document.getElementById(elem).value;

  if (val.length == 0)
    blnResult = false;
  else {
    strChar = val.charAt(0);
    if (strChar == "+" || strChar == "-")
      val = val.substring(1);
  }

  for (i = 0; i < val.length && blnResult == true; i++) {
    strChar = val.charAt(i);
    if (strValidChars.indexOf(strChar) == -1) {
      blnResult = false;
    }
  }

  if (!blnResult) {
    alert(msg);
    document.getElementById(elem).value = "000";
  }

  return blnResult;
}

/****************************************************************************
 * Function: getRadioValue
 ****************************************************************************/
/*
function getRadioValue(obj) {
  var radioValue;
  var i = 0;

  while (radioValue == null && i < obj.length) {
    if (obj[i].checked) {
      radioValue = obj[i].value;
    } //end of if
    i++;
  } //end of while

  return radioValue;
} //end of getRadioValue
*/

/****************************************************************************
 * Function: radioToggle
 * Deprecated: Use label html tag
 ****************************************************************************/
/*
function radioToggle(radioObj) {
  radioObj.checked = true;
} //end of radioToggle
*/
/****************************************************************************
 * Function: checkBoxToggle
 * Deprecated: Use label html tag
 ****************************************************************************/
/*
function checkBoxToggle(checkBoxObj) {
  checkBoxObj.checked = !checkBoxObj.checked;
} //end of checkBoxToggle
*/

/****************************************************************************
 * Function: roundFloat
 ****************************************************************************/
/*
function roundFloat(x, n) {
  if (!parseInt(n)) {
  	n = 0;
	} //end of if

	if (!parseFloat(x)) {
		return false;
	} //end of if

	return Math.round(x*Math.pow(10,n))/Math.pow(10,n);
} //end of roundFloat
*/

/****************************************************************************
 * Function: isCurrency
 ****************************************************************************/
/*
function isCurrency(str) {
    var regexp = /^\d+\.\d{0,2}$/;
    if (regexp.test(str))
        return true;
    else
        return false;
}
*/
