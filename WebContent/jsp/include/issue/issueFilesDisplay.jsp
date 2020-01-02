<%@ page language="java" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ page import="com.premiersolutionshi.common.util.StringUtils" %>

<tr><th>Files</th></tr>
<tr><td>
  <logic:notEmpty name="issueForm" property="issueFileList">
    <table class="file-list-table alt-color">
      <thead>
        <tr>
          <th></th>
          <th>File</th>
          <th align="right">Size</th>
          <th align="center">Uploaded By</th>
          <th style="text-align:right;">Uploaded Date</th>
          <logic:equal name="action" value="edit">
            <th align="center"><i class="material-icons">delete</i></th>
          </logic:equal>
        </tr>
      </thead>
      <tbody>
        <logic:iterate id="file" name="issueForm" property="issueFileList" type="com.premiersolutionshi.common.domain.FileInfo">
          <tr>
            <td align="center"><img src="${file.smallIcon}"/></td>
            <td><a href="download.do?filePk=${file.id}">${file.filename}</a></td>
            <td align="right">${file.filesizeStr}</td>
            <td align="center">${file.uploadedBy}</td>
            <td align="right">${file.uploadedDateStr}</td>
            <logic:equal name="action" value="edit">
              <td align="center"><input type="checkbox" name="deleteFilePkArr" value="${file.id}"/></td>
            </logic:equal>
          </tr>
        </logic:iterate>
      </tbody>
    </table>
  </logic:notEmpty>

  <% if (request.getAttribute("action").equals("add") || request.getAttribute("action").equals("edit")) { %>
    <table id="newUploadFileTbl">
      <colgroup>
        <col width="400">
      </colgroup>
      <tbody></tbody>
      <thead>
        <tr>
          <td colspan="2" class="newRow">
            <a href="javascript:void(0);" class="addFileBtn btn btn-info btn-sml">
              <i class="material-icons">add</i>
              Add New File
            </a>
          </td>
        </tr>
      </thead>
    </table>
  <% } %>
</td></tr>
