<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Ship ${editType.equals('add') ? 'Add' : 'Edit'}"/>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.ShipBean"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>

<%@ include file="../layout/old/layout-header.jsp" %>

<p align="center">
Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
</p>

<html:form action="ship.do" onsubmit="return valFields();" method="POST" style="width:610px;">
  <% if (editType.equals("add")) { %>
    <input type="hidden" name="action" value="shipAddDo"/>
  <% } else { %>
    <input type="hidden" name="action" value="shipEditDo"/>
    <html:hidden name="inputBean" property="shipPk"/>
  <% } %>
  <table id="tanTable_style2" class="border-zero cellspacing-zero">
  <tbody>
    <tr><th>Unit Information</th></tr>
    <tr><td class="nobordered" align="left">
      <table id="borderlessTable" class="border-zero cellspacing-zero">
      <colgroup><col style="width:100px"/></colgroup>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Unit Name:</td>
          <td colspan="5">
            <html:text styleId="nameInput" name="inputBean" property="shipName" size="65" maxlength="75" styleClass="form-control"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> UIC:</td>
          <td>
            <table><tbody><tr><td>
              <html:text styleId="uicInput" name="inputBean" property="uic" size="5" maxlength="5" styleClass="form-control"/>
            </td>
            <td>
              <a id="duplicateUicButton" data-toggle="modal" data-target="#duplicateUics" style="display:none;">
                <i class="material-icons" title="A duplicate UIC has been detected.">warning</i>
              </a>
            </td></tr></tbody></table>
          </td>
          <td class="fieldName">Type:</td>
          <td>
            <html:select name="inputBean" property="type" styleClass="form-control">
              <html:option value=""/>
              <html:options collection="typeList" property="value"/>
            </html:select>
          </td>
          <td class="fieldName"><span class="regAsterisk">*</span> Hull:</td>
          <td><html:text name="inputBean" property="hull" size="4" maxlength="6" styleClass="form-control"/></td>
        </tr>
        <tr>
          <td class="fieldName">TYCOM:</td>
          <td>
            <html:select name="inputBean" property="tycom" styleClass="form-control">
              <html:option value=""/>
              <html:options name="tycomList"/>
            </html:select>
          </td>
          <td class="fieldName">Service Code:</td>
          <td colspan="3">
            <html:select name="inputBean" property="serviceCode" styleClass="form-control">
              <html:option value=""/>
              <html:option value="R">R - Pacific Fleet</html:option>
              <html:option value="V">V - Atlantic Fleet</html:option>
              <html:option value="N">N - Navy Ashore</html:option>
              <html:option value="D">D - Decomissioned</html:option>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Homeport:</td>
          <td colspan="5">
            <html:select name="inputBean" property="homeport" styleClass="form-control">
              <html:option value=""/>
              <html:options collection="homeportList" property="value"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="fieldName">RSupply:</td>
          <td colspan="5">
            <html:select name="inputBean" property="rsupply" styleClass="form-control">
              <html:option value=""/>
              <html:options name="rsupplyList"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td></tr>
  </tbody>
  </table>

  <div class="center">
    <table id="borderlessTable" class="border-zero cellspacing-zero"><tbody>
      <tr>
        <td align="center">
          <html:submit value="${editType.equals('add') ? 'Insert' : 'Save'}" styleClass="btn btn-primary"/>
        </td>
        <td align="center">
          <input type="button" onclick="window.location='ship.do?action=shipList';" value="Cancel" class="btn btn-secondary"/>
        </td>
      </tr>
    </tbody></table>
  </div>
</html:form>

<tags:modal id="duplicateUics" title="Units with the same UIC" />

<%@ include file="../layout/old/layout-footer.jsp" %>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/common/ship/shipEdit.js"></script>
<script type="text/javascript">
var shipList = [];
var originalShipPk = '${inputBean.shipPk}';
var originalShipName = '${inputBean.shipName}';
var originalUic = '${inputBean.uic}';
$(function() {
<logic:present name="shipList">
<logic:iterate id="ship" name="shipList">shipList.push({ id: '${ship.shipPk}', uic: '${ship.uic}', shipName: '${ship.shipName}'});
</logic:iterate></logic:present>

checkUicIsUnique();
});
</script>
</body>
</html>
