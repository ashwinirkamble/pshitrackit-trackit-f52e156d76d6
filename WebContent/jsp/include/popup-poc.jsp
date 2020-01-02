<logic:present name="pocBean">
	<logic:empty name="pocBean" property="lastName">
		<p class="error" align="center">
		No user information found
		</p>
	</logic:empty>

	<logic:notEmpty name="pocBean" property="lastName">
		<p align="center">
		<center>
		<table id="tanTable_style2" border="0" cellspacing="0">
		<tbody>
			<tr><th>POC Information</th></td>
			<tr><td class="nobordered">
				<table id="borderlessTable" border="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="fieldName">Name:</td>
						<td class="fieldValue">
							<bean:write name="pocBean" property="firstName"/>
							<bean:write name="pocBean" property="lastName"/>
						</td>
						<logic:notEmpty name="pocBean" property="rank">
							<td class="fieldName">Rank:</td>
							<td class="fieldValue"><bean:write name="pocBean" property="rank"/></td>
						</logic:notEmpty>
					</tr>

					<logic:notEmpty name="pocBean" property="organization">
						<tr>
							<td class="fieldName">Company/Org:</td>
							<td class="fieldValue" colspan="3"><bean:write name="pocBean" property="organization"/>
						</tr>
					</logic:notEmpty>

					<logic:notEmpty name="pocBean" property="title">
						<tr>
							<td class="fieldName">Job Title:</td>
							<td class="fieldValue" colspan="3"><bean:write name="pocBean" property="title"/>
						</tr>
					</logic:notEmpty>

					<logic:notEmpty name="pocBean" property="email">
						<tr>
							<td class="fieldName">E-Mail:</td>
							<td class="fieldValue" colspan="3"><a href="mailto:<bean:write name="pocBean" property="email"/>"><bean:write name="pocBean" property="email"/></a></td>
						</tr>
					</logic:notEmpty>

					<tr>
						<logic:notEmpty name="pocBean" property="workNumber">
							<td class="fieldName">Work Phone:</td>
							<td class="fieldValue"><bean:write name="pocBean" property="workNumber"/></td>
						</logic:notEmpty>

						<logic:notEmpty name="pocBean" property="quickDial">
							<td class="fieldName">Quick Dial:</td>
							<td class="fieldValue"><bean:write name="pocBean" property="quickDial"/></td>
						</logic:notEmpty>
					</tr>

					<tr>
						<logic:notEmpty name="pocBean" property="faxNumber">
							<td class="fieldName">Fax Phone:</td>
							<td class="fieldValue"><bean:write name="pocBean" property="faxNumber"/></td>
						</logic:notEmpty>

						<logic:notEmpty name="pocBean" property="cellNumber">
							<td class="fieldName">Cell Number:</td>
							<td class="fieldValue"><bean:write name="pocBean" property="cellNumber"/></td>
						</logic:notEmpty>
					</tr>
				</tbody>
				</table>
			</td></tr>
		</tbody>
		</table>
		</center>
		</p>
	</logic:notEmpty>
</logic:present>
