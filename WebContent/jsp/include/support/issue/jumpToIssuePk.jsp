<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<input type="hidden" id="issuePkJumpProjectPk${projectPk}" value="${projectPk}"/>
<logic:empty name="pageFrom">
  <input type="hidden" id="issuePkJumpPageFrom${projectPk}" value="issueList"/>
</logic:empty>
<logic:notEmpty name="pageFrom">
  <input type="hidden" id="issuePkJumpPageFrom${projectPk}" value="${pageFrom}"/>
</logic:notEmpty>

Jump to issue # <input type="text" id="issuePkJump${projectPk}" size="4"
  onkeypress="if (event.keyCode === 13) { jumpToIssuePk(${projectPk}); }" />
&nbsp;
<button onclick="return jumpToIssuePk(${projectPk});">Go</button>

<script type="text/javascript" src="js/support/jumpToIssuePk.js"></script>