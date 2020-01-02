if (!jumpToTaskPk) {
  function jumpToTaskPk(projectPk) {
    var taskPkJumpInput = document.getElementById("taskPkJump" + projectPk);
    var projectPkInput = document.getElementById("taskPkJumpProjectPk" + projectPk);
    if (taskPkJumpInput && projectPkInput) {
      var projectPk = projectPkInput.value;
      var taskPk = taskPkJumpInput.value.replace(/\D/g,'');
      taskPkJumpInput.value = taskPk;
      if (taskPk !== '') {
        //project.do?action=taskDetail&taskPk=1140&projectPk=1
        var url = "project.do?action=taskDetail&taskPk=" + taskPk + "&projectPk=" + projectPk;
        //console.log(url);
        window.location = url;
      }
    }
    else {
      console.log("taskPkJumpInput" + projectPk + " and/or projectPkInput" + projectPk + " not found.")
    }
    return 0;
  }
}