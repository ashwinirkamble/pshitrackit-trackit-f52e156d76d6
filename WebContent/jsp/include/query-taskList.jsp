<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<div class="panel panel-primary" style="margin: 10px 0 0 0;">
  <div class="panel-heading" data-toggle="collapse" data-target=".task-search-panel">
    <h3 class="panel-title">
    <a class="accordion-toggle">Search</a>
    <i class="indicator glyphicon glyphicon-chevron-down pull-right"></i>
    </h3>
  </div>
  <div class="task-search-panel collapse" style="padding:10px;">
    <html:form action="project.do" method="GET">
      <input type="hidden" name="action" value="taskList"/>
      <html:hidden name="projectBean" property="projectPk"/>
      <input type="hidden" name="sortBy" value="${sortBy}"/>
      <input type="hidden" name="sortDir" value="${sortDir}"/>
      <input type="hidden" name="searchPerformed" value="Y"/>
      <table class="pshi-menu-table"><tbody><tr>
        <td>
          <p align="left">
            Contract Number<br/>
            <html:select name="inputBean" property="contractNumber">
              <html:option value="">View All</html:option>
              <html:options name="contractNumberList"/>
            </html:select>
          </p>
          <p align="left">
            Title/Description<br/>
            <html:text name="inputBean" property="searchTitleDescription" size="30"/>
          </p>
          <p align="left">
            Person Assigned<br/>
            <html:select name="inputBean" property="personAssigned">
              <html:option value="">View All</html:option>
              <html:options name="personAssignedList"/>
            </html:select>
          </p>
        </td>
        <td>
          <p align="left">
            Status<br/>
            <label>
              <input type="checkbox" name="statusAll" value="All" id="allStatusBox"/>
              All Current Tasks
            </label><br/>
            <logic:notEmpty name="statusList">
              <logic:iterate id="status" name="statusList" type="java.lang.String" indexId="i">
                <label>
                  <html:multibox name="inputBean" property="searchStatusArr" value="<%= status %>" styleClass="statusBox"/>
                  ${status}
                </label><br/>
              </logic:iterate>
            </logic:notEmpty>
          </p>
        </td>
        <td>
          <p align="left">
            Category<br/>
            <html:select name="inputBean" property="category" styleId="category">
              <html:option value="">View All</html:option>
              <html:options name="categoryList"/>
            </html:select>
          </p>
          <p class="futureRequestFields" align="left">
            Target Quarter<br/>
            <html:radio name="inputBean" property="quarterYear" value="" styleId="quarterYear_ALL"/>
            <label for="quarterYear_ALL">View All</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="2014Q1" styleId="quarterYear_2014Q1"/>
            <label for="quarterYear_2014Q1">2014Q1 (Target 2/1)</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="2014Q2" styleId="quarterYear_2014Q2"/>
            <label for="quarterYear_2014Q2">2014Q2 (Target 4/1)</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="2014Q3" styleId="quarterYear_2014Q3"/>
            <label for="quarterYear_2014Q3">2014Q3</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="2014Q4" styleId="quarterYear_2014Q4"/>
            <label for="quarterYear_2014Q4">2014Q4</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="Sustainment" styleId="quarterYear_Sustainment"/>
            <label for="quarterYear_Sustainment">Sustainment</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="OOS" styleId="quarterYear_OOS"/>
            <label for="quarterYear_OOS">Out of Scope</label><br/>
            <html:radio name="inputBean" property="quarterYear" value="TBD" styleId="quarterYear_TBD"/>
            <label for="quarterYear_TBD">TBD</label>
          </p>
          <p class="futureRequestFields" align="left">
            Area of Effort<br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="Admin Receipt DB"/> Admin Receipt DB</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="FACET DB"/> FACET DB</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="Kofax"/> Kofax</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="LOGCOP"/> LOGCOP</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="Manual"/> Manual</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="Support"/> Support</label><br/>
            <label><html:multibox name="inputBean" property="effortAreaArr" value="Training"/> Training</label><br/>
          </p>
          <p align="left">
            Priority<br/>
            <html:select name="inputBean" property="priority">
              <html:option value="">View All</html:option>
              <html:options name="priorityList"/>
            </html:select>
          </p>
          <p align="left">
            Weekly Meeting Agenda<br/>
            <html:multibox name="inputBean" property="searchMeetingArr" value="Staff" styleId="searchMeetingArr_staff"/>
            <label for="searchMeetingArr_staff">Staff</label>
            &nbsp;
            <html:multibox name="inputBean" property="searchMeetingArr" value="Client" styleId="searchMeetingArr_client"/>
            <label for="searchMeetingArr_client">Client</label>
          </p>
        </td>
        <td>
          <p align="left">
            Unit Name<br/>
            <html:select name="inputBean" property="uic" style="font-size:9pt;width:220px;">
              <html:option value="">View All</html:option>
              <<html:options collection="shipList" property="uic" labelProperty="shipNameTypeHull"/>
            </html:select>
          </p>
          <p align="left">
            Homeport<br />
            <html:select name="inputBean" property="homeport">
              <html:option value="">View All</html:option>
              <html:options name="homeportList" />
            </html:select>
          </p>
          <p align="left">
            Due Date<br> from
            <html:text name="inputBean" property="dueDateStart" styleClass="datepicker" size="9" />
            to
            <html:text name="inputBean" property="dueDateEnd" styleClass="datepicker" size="9" />
          </p>
          <p align="left">
            Notes<br />
            <html:text name="inputBean" property="notes" />
          </p>
          <p align="left">
            Sub-Tasks<br />
            <html:text name="inputBean" property="searchSubTask" />
          </p>
        </td>
      </tr></tbody></table>
      <div class="center CENTERED">
        <html:submit value="Search" styleClass="btn btn-primary" />
        <a class="btn btn-default" href="project.do?action=taskList&projectPk=${projectPk}">
          View All
        </a>
      </div>
    </html:form>
  </div>
</div>
