if (!jumpToIssuePk) {
  function jumpToIssuePk(projectPk) {
    var issuePkJumpInput = document.getElementById("issuePkJump" + projectPk);
    var projectPkInput = document.getElementById("issuePkJumpProjectPk" + projectPk);
    var issuePkJumpPageFromInput = document.getElementById("issuePkJumpPageFrom" + projectPk);
    if (issuePkJumpInput && projectPkInput) {
      var projectPk = projectPkInput.value;
      var pageFrom = issuePkJumpPageFromInput ? issuePkJumpPageFromInput.value : '';
      var issuePk = issuePkJumpInput.value.replace(/\D/g,'');
      issuePkJumpInput.value = issuePk;
      if (issuePk !== '') {
        var url = "issue.do?id=" + issuePk + "&projectPk=" + projectPk
          + (pageFrom === '' ? '' : '&pageFrom=' + pageFrom);
        //console.log(url);
        window.location = url;
      }
    }
    else {
      console.log("issuePkJumpInput" + projectPk + " and/or projectPkInput" + projectPk + " not found.")
    }
    return 0;
  }
}