$(function() {
  $(".datepicker").attr("autocomplete", "off");
  $(".datepicker").datepicker();

  $('.integer').on('change keyup paste', function(e) {
    var integerStr = $(this).val().replace(/\D/g,'');
    if (integerStr === '') {
      return;
    }
    var integer = 0;
    if (integerStr !== '') {
      integer = parseInt(integerStr);
    }
    var code = e.keyCode || e.which;
    if (code === 38) { //key UP
      integer++;
    }
    else if (code === 40) { //key DOWN
      integer--;
    }
    $(this).val(integer);
  });
  if (document.govPropertyForm) {
    unescapeElem(document.govPropertyForm.description);
  }
});

// Gets todays day in MM/DD/YY HHMI format
function getTodayDate() {
    var dt = new Date();
    return (dt.getMonth()+1) + '/' + dt.getDate() + '/' + dt.getYear();
}

// Removes leading and trailing spaces
function stripSpaces(x) {
  if (x) {
    x = x.trim();
  }
  else {
    x = '';
  }
  return x;
}

// Returns true if all the characters in a string are numbers
function checkAllNumbers(x) {
    for (i=0; i<=(x.length-1); i++) {
        if ((x.charCodeAt(i) < 48) || (x.charCodeAt(i)>57)) {
            return false;
        }
    }
    return true;
}

// Checks if a date is in the following format: m/d/y
function validateDate(theDate, ctrlName) {
    var firstSlash;
    var secondSlash;
    var dayPart;
    var monthPart;
    var yearPart;

    theDate = stripSpaces(theDate);
    firstSlash = theDate.indexOf('/');

    if (firstSlash < 0) {
        alert(ctrlName + ': Cannot find a slash [/] between the MONTH\nand DAY in the date you entered [' + theDate + ']');
        return false;
    }

    secondSlash = theDate.indexOf('/', firstSlash+1);

    if (secondSlash < 0) {
        alert(ctrlName + ': Cannot find a slash [/] between the DAY\nand YEAR in the date/time you entered [' + theDate + ']');
        return false;
    }

    monthPart = theDate.substr(0,firstSlash);
    dayPart = theDate.substr(firstSlash+1,secondSlash-firstSlash-1);
    yearPart = theDate.substr(secondSlash+1)

    if (!checkAllNumbers(monthPart) || monthPart.length<1) {
        alert(ctrlName + ': The MONTH portion [' + monthPart + ']\nof the date you entered [' + theDate + '] is invalid.');
        return false;
    }
    if (!checkAllNumbers(dayPart) || dayPart.length<1) {
        alert(ctrlName + ': The DAY portion [' + dayPart + ']\nof the date you entered [' + theDate + '] is invalid.');
        return false;
    }
    if (!checkAllNumbers(yearPart) || yearPart.length<1) {
        alert(ctrlName + ': The YEAR portion [' + yearPart + ']\nof the date you entered [' + theDate + '] is invalid.');
        return false;
    }
    if (((monthPart-0)<1) || ((monthPart-0)>12)) {
        alert(ctrlName + ': The MONTH portion [' + monthPart + ']\nof the date you entered [' + theDate + '] is out of range.');
        return false;
    }
    if (((dayPart-0)<1) || ((dayPart-0)>31)) {
        alert(ctrlName + ': The DAY portion [' + dayPart + ']\nof the date you entered [' + theDate + '] is out of range.');
        return false;
    }
    if (((yearPart-0)<0) || ((yearPart-0)>2099)) {
        alert(ctrlName + ': The YEAR portion [' + yearPart + ']\nof the date you entered [' + theDate + '] is out of range.');
        return false;
    }

    var testDate = new Date(2000+(yearPart-0),(monthPart-0)-1,(dayPart-0));


    if ((((yearPart-0)+2000)!=testDate.getYear() && ((yearPart-0)+100)!=testDate.getYear()) || (((monthPart-0)-1)!=testDate.getMonth()) || ((dayPart-0)!=testDate.getDate())){
        alert(ctrlName + ': The DATE [' + theDate + '] has a valid format, but is not a valid date.');
          return false;
      }

      return true;
  }

function makeArray()    {
    this[0] = makeArray.arguments.length;
    for (i = 0; i<makeArray.arguments.length; i++)
        this[i+1] = makeArray.arguments[i];
}


function LeapYear(year) {
    if ((year/4)   != Math.floor(year/4))   return false;
    if ((year/100) != Math.floor(year/100)) return true;
    if ((year/400) != Math.floor(year/400)) return false;
    return true;
}

function getJulianDay(d) {
  var accumulate    = new makeArray(  0, 31, 59, 90,120,151,181,212,243,273,304,334);
  var accumulateLY  = new makeArray(  0, 31, 60, 91,121,152,182,213,244,274,305,335);
  var day = d.getDate();
  var month = d.getMonth()+1;
  var year = d.getYear();

    if (LeapYear(year))
        return (day + accumulateLY[month]);
    else
        return (day + accumulate[month]);
}

function formatDateString(d) {
  var theDate = new Date(d);
  if (theDate.getFullYear() < 2000) {
    theDate.setFullYear(theDate.getFullYear() + 100);
  }

  var monthPart = theDate.getMonth() + 1;
  var dayPart = theDate.getDate();
  var yearPart = theDate.getFullYear();

  if (monthPart < 10) {
    monthPart = "0" + monthPart
}
if (dayPart < 10) {
  dayPart = "0" + dayPart
}

return monthPart + "/" + dayPart + "/" + yearPart;
}

function getFiscalYear(d) {
  var theDate = new Date(d);
  if (theDate.getFullYear() < 2000) {
    theDate.setFullYear(theDate.getFullYear() + 100);
  }

  var monthPart = theDate.getMonth() + 1;
  var yearPart = theDate.getFullYear();

  if (monthPart > 9) {
    yearPart = yearPart + 1;
  }

  return yearPart;
}

function getFiscalQuarter(d) {
  var theDate = new Date(d);
  if (theDate.getFullYear() < 2000) {
    theDate.setFullYear(theDate.getFullYear() + 100);
  }

  var monthPart = theDate.getMonth() + 1;

  if (monthPart < 4) {
    return 2;
  } else
  if (monthPart < 7) {
    return 3;
  } else
  if (monthPart < 10) {
    return 4;
  } else {
    return 1;
  }
}

function createDateObject(theDate) {
  var dateObj = new Date();
  var firstSlashPos = theDate.indexOf("/");
var secondSlashPos = theDate.lastIndexOf("/");
  var month = theDate.substr(0,firstSlashPos);
  var day   = theDate.substr(firstSlashPos+1, (secondSlashPos - (firstSlashPos + 1)));
  var year  = theDate.substr(secondSlashPos+1);

  if (year < 100) year = parseInt(year) + 2000;

  dateObj.setFullYear(year,month-1,day);
  return dateObj;
}

function dateDiff(theDate1, theDate2) {
  var objDate1 = createDateObject(theDate1);
  var objDate2 = createDateObject(theDate2);
  return (objDate2 - objDate1) / (24*60*60*1000);
} //end of dateDiff

function timeDiff(theDate1, theTime1, theDate2, theTime2) {
  var objDate1 = new Date();
  var objDate2 = new Date();
  var theDate;
  var firstSlash;
  var secondSlash;
  var monthPart;
  var dayPart;
  var yearPart;
  var hourPart;
  var minPart;

  //Parse Start Date
theDate = stripSpaces(theDate1);
firstSlash = theDate.indexOf('/');
secondSlash = theDate.indexOf('/', firstSlash + 1);
monthPart = theDate.substring(0, firstSlash);
dayPart = theDate.substring(firstSlash + 1,secondSlash);
yearPart = theDate.substring(secondSlash + 1)
hourPart = theTime1.substring(0,2);
minPart = theTime1.substring(2,4);

objDate1.setFullYear(yearPart, monthPart, dayPart);
objDate1.setHours(hourPart, minPart, 0);

//Part End Date
theDate = stripSpaces(theDate2);
firstSlash = theDate.indexOf('/');
secondSlash = theDate.indexOf('/', firstSlash + 1);
  monthPart = theDate.substring(0, firstSlash);
  dayPart = theDate.substring(firstSlash + 1,secondSlash);
  yearPart = theDate.substring(secondSlash + 1)
  hourPart = theTime2.substring(0,2);
  minPart = theTime2.substring(2,4);

  objDate2.setFullYear(yearPart, monthPart, dayPart);
  objDate2.setHours(hourPart, minPart, 0);

  return (objDate2 - objDate1) / (24*60*60*1000);
} //end of timeDiff


function isDate(txtDate) {
//  var currVal = txtDate;
//  if(currVal == '')
//  return false;
//  
//  //Declare Regex
//  var rxDatePattern1 = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
//  var rxDatePattern2 = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{2})$/;
//  var dtArray = currVal.match(rxDatePattern1); // is format OK?
//  
//  if (dtArray == null)
//  dtArray = currVal.match(rxDatePattern2); // is format OK?
//  
//  if (dtArray == null)
//  return false;
//  
//  //Checks for mm/dd/yyyy format.
//  dtMonth = dtArray[1];
//  dtDay= dtArray[3];
//  dtYear = dtArray[5];
//
//  if (dtMonth < 1 || dtMonth > 12)
//    return false;
//  else if (dtDay < 1 || dtDay> 31)
//    return false;
//  else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31)
//    return false;
//  else if (dtMonth == 2) {
//    var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
//    if (dtDay> 29 || (dtDay ==29 && !isleap))
//      return false;
//  }
  return true;
}
