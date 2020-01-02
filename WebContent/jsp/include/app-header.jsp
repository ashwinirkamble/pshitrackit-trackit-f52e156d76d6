<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import ="com.premiersolutionshi.old.util.CommonMethods" %>

<div id="app-header"
 class="<% if (CommonMethods.isDevEnv()) { %>dev-watermark<% } else { %>prod<% } %>">
  <div id="banner">
    <div style="float:left;width:500px;">&nbsp;</div>
    <div style="float:right;min-width:415px;height:30px;background: url(./images/banner_username.png) no-repeat left;">
      <div style="padding-top:4px;padding-left:12px;font-weight:bold;color:#fff;">
        <table class="border-zero cellspacing-zero cellpadding-zero">
        <tr>
          <td style="padding-right:7px;"><img src="images/user-yellow.png"/></td>
          <td nowrap>Welcome, <bean:write name="loginBean" property="fullName"/></td>
<!-- 			<td style="padding-left:7px;"><a href="javascript:logout();"><img src="images/logout.png"/></a></td>
 -->			
          <% if (CommonMethods.nes(request.getHeader("user-agent")).indexOf("MSIE") > -1) { %>
            <td style="padding-left:7px;"><a href="javascript:logout();"><img src="/images/logout.png"/></a></td>
          <% } %>
        </tr>
        </table>
      </div>
    </div>
  </div>
</div>
