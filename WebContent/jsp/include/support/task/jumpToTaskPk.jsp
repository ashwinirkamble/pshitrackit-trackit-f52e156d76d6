<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<input type="hidden" id="taskPkJumpProjectPk${projectPk}" value="${projectPk}"/>

Jump to task # <input type="text" id="taskPkJump${projectPk}" size="4" onkeypress="if (event.keyCode === 13) { jumpToTaskPk(${projectPk}); }" />
&nbsp;
<button onclick="return jumpToTaskPk(${projectPk});">Go</button>

<script type="text/javascript" src="js/support/jumpToTaskPk.js"></script>