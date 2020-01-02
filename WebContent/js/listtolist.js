  function delOpt(object,index) {
    object.options[index] = null;
  }

  function addOpt(object,text,value) {
    var defaultSelected = false;
    var selected = false;
    var optionName = new Option(text, value, defaultSelected, selected)
    object.options[object.length] = optionName;
    object.options[object.length-1].selected = false;
  }

  function sortOpt(what) {
    var copyOption = new Array();
    for(var i=0;i<what.options.length;i++)
      copyOption[i] = new Array(what[i].text,what[i].value);

    copyOption.sort();

    for(var i=what.options.length-1;i>-1;i--)
      delOpt(what,i);

    for(var i=0;i<copyOption.length;i++)
      addOpt(what,copyOption[i][0],copyOption[i][1])
  }

  function deleteOption(object,index) {
    delOpt(object, index);
  }

  function addOption(object,text,value) {
    addOpt(object, text, value);
    //var defaultSelected = true;
    //var selected = true;
    //var optionName = new Option(text, value, defaultSelected, selected)
    //object.options[object.length] = optionName;
  }

  function copySelected(fromObject,toObject) {
    for(var i=0, l=fromObject.options.length;i<l;i++) {
      if (fromObject.options[i].selected)
        addOption(toObject,fromObject.options[i].text,fromObject.options[i].value);
    }

    for(var i=fromObject.options.length-1;i>-1;i--) {
      if (fromObject.options[i].selected)
        deleteOption(fromObject,i);
    }
  }

  function copyAll(fromObject,toObject) {
    for(var i=0, l=fromObject.options.length;i<l;i++) {
      addOption(toObject,fromObject.options[i].text,fromObject.options[i].value);
    }
    for(var i=fromObject.options.length-1;i>-1;i--) {
      deleteOption(fromObject,i);
    }
  }

  function copySelectedToMultiple(fromObject,toObjectArray) {
    for(var i=0, l=fromObject.options.length;i<l;i++) {
      if (fromObject.options[i].selected) {
        for(var j=0, m=toObjectArray.length;j<m;j++) {
          addOption(toObjectArray[j],fromObject.options[i].text,fromObject.options[i].value);
        }
      }
    }

    for(var i=fromObject.options.length-1;i>-1;i--) {
      if (fromObject.options[i].selected)
        deleteOption(fromObject,i);
    }
  }

  function copyAllToMultiple(fromObject,toObjectArray) {
    for(var i=0, l=fromObject.options.length;i<l;i++) {
      for(var j=0, m=toObjectArray.length;j<m;j++) {
        addOption(toObjectArray[j],fromObject.options[i].text,fromObject.options[i].value);
      }
    }
    for(var i=fromObject.options.length-1;i>-1;i--) {
      deleteOption(fromObject,i);
    }
  }

  function copySelectedFromMultiple(fromObjectArray,toObject) {
    for(var i=0, l=fromObjectArray[0].options.length;i<l;i++) {
      if (fromObjectArray[0].options[i].selected)
        addOption(toObject,fromObjectArray[0].options[i].text,fromObjectArray[0].options[i].value);
    }

    for(var i=fromObjectArray[0].options.length-1;i>-1;i--) {
      if (fromObjectArray[0].options[i].selected) {
        for(var j=1, m=fromObjectArray.length;j<m;j++) {
          for(var k=fromObjectArray[j].options.length-1;k>-1;k--) {
            if (fromObjectArray[j].options[k].text == fromObjectArray[0].options[i].text) {
              deleteOption(fromObjectArray[j],k);
            }
          }
        }

        deleteOption(fromObjectArray[0],i);
      }
    }
  }

  function copyAllFromMultiple(fromObjectArray,toObject) {
    for(var i=0, l=fromObjectArray[0].options.length;i<l;i++) {
      addOption(toObject,fromObjectArray[0].options[i].text,fromObjectArray[0].options[i].value);
    }
    for(var j=0, m=fromObjectArray.length;j<m;j++) {
      for(var i=fromObjectArray[j].options.length-1;i>-1;i--) {
        deleteOption(fromObjectArray[j],i);
      }
    }
  }

  function buildSelectList(theObject, theTarget) {
    for (var i = 0; i < theObject.options.length; i++) {
      theTarget.value = theTarget.value + (i > 0 ? "," : "") + theObject.options[i].value;
    }
  }