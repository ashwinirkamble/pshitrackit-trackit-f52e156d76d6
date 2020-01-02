<div class="col3">
<!-- Right Column 3 start -->
	<div id="user-task-list">

	<h1>Pending tasks for <bean:write name="loginBean" property="fullName"/></h1>

	<logic:notEmpty name="rightbar_followUpTasks">
		<p align="left">
		<h2><img src="images/pushpin_red.png" height="16" width="16"/> Tasks to Follow-Up:</h2>
		<ul>
		<logic:iterate id="resultBean" name="rightbar_followUpTasks" type="com.premiersolutionshi.old.bean.ProjectBean">
			<li>
			<a href="project.do?action=taskDetail&taskPk=<bean:write name="resultBean" property="taskPk"/>&projectPk=<bean:write name="resultBean" property="projectPk"/>&pagefrom=rightbar">
				<bean:write name="resultBean" property="followUpDate"/>
				<bean:write name="resultBean" property="title"/>
			</a>
			</li>
		</logic:iterate>
		</ul>
		</p>

		<hr width="100%" style="border-color: #aaa;"/>
	</logic:notEmpty>

	<p align="left">
	<logic:empty name="rightbar_overdueTasks">
		<h2><img src="images/icon_success.png" height="16" width="16"/> No overdue tasks</h2>
	</logic:empty>
	<logic:notEmpty name="rightbar_overdueTasks">
		<h2><img src="images/icon_error.png" height="16" width="16"/> Overdue tasks:</h2>
		<ul>
		<logic:iterate id="resultBean" name="rightbar_overdueTasks" type="com.premiersolutionshi.old.bean.ProjectBean">
			<li>
			<a href="project.do?action=taskDetail&taskPk=<bean:write name="resultBean" property="taskPk"/>&projectPk=<bean:write name="resultBean" property="projectPk"/>&pagefrom=rightbar">
				<bean:write name="resultBean" property="dueDate"/>
				<bean:write name="resultBean" property="title"/>
			</a>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	</p>

	<p align="left">
	<logic:empty name="rightbar_dueTasks">
		<h2><img src="images/icon_success.png" height="16" width="16"/> No tasks due today</h2>
	</logic:empty>
	<logic:notEmpty name="rightbar_dueTasks">
		<h2><img src="images/icon_warning.png" height="16" width="16"/> Tasks due today:</h2>
		<ul>
		<logic:iterate id="resultBean" name="rightbar_dueTasks" type="com.premiersolutionshi.old.bean.ProjectBean">
			<li>
				<a href="project.do?action=taskDetail&taskPk=<bean:write name="resultBean" property="taskPk"/>&projectPk=<bean:write name="resultBean" property="projectPk"/>&pagefrom=rightbar">
					<bean:write name="resultBean" property="dueDate"/>
					<bean:write name="resultBean" property="title"/>
				</a>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	</p>

	<p align="left">
	<logic:empty name="rightbar_pending7Tasks">
		<h2><img src="images/icon_success.png" height="16" width="16"/> No tasks due in the next 7 days</h2>
	</logic:empty>
	<logic:notEmpty name="rightbar_pending7Tasks">
		<h2>Tasks due in the next 7 days:</h2>
		<ul>
		<logic:iterate id="resultBean" name="rightbar_pending7Tasks" type="com.premiersolutionshi.old.bean.ProjectBean">
			<li>
				<a href="project.do?action=taskDetail&taskPk=<bean:write name="resultBean" property="taskPk"/>&projectPk=<bean:write name="resultBean" property="projectPk"/>&pagefrom=rightbar">
					<bean:write name="resultBean" property="dueDate"/>
					<bean:write name="resultBean" property="title"/>
				</a>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	</p>

	<p align="left">
	<logic:empty name="rightbar_pending31Tasks">
		<h2><img src="images/icon_success.png" height="16" width="16"/> No tasks in the next 31 days</h2>
	</logic:empty>
	<logic:notEmpty name="rightbar_pending31Tasks">
		<h2>Tasks due in the next 31 days:</h2>
		<ul>
		<logic:iterate id="resultBean" name="rightbar_pending31Tasks" type="com.premiersolutionshi.old.bean.ProjectBean">
			<li>
				<a href="project.do?action=taskDetail&taskPk=<bean:write name="resultBean" property="taskPk"/>&projectPk=<bean:write name="resultBean" property="projectPk"/>&pagefrom=rightbar">
					<bean:write name="resultBean" property="dueDate"/>
					<bean:write name="resultBean" property="title"/>
				</a>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	</p>

<!--
	<hr width="100%" style="border-color: #aaa;"/>
	<p align="left">
	<a href="project.do?action=outlookCsv">Download Outlook Calendar File</a>
	</p>
-->
	</div>

<!-- Right Column 3 end -->
</div>
