<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String type = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		type = request.getParameter("type");
		
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<script src="<%=basePath %>user/js/modal/bindelasticip.js"></script>
<div class="modal-dialog" style="width:600px; margin-top:100px">
	<div id="bindmodal" class="modal-content" type="<%=type%>">
		<div class="modal-header">
			<h4 class="modal-title" id="modaltitle">
				<%
				if("vm".equals(type)){
					out.print("选择要绑定公网IP的主机");
				} else if("lb".equals(type)){
					out.print("选择要绑定公网IP的负载均衡");
				} else if("rt".equals(type)){
					out.print("选择要绑定公网IP的路由器");
				}%>
				<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:20px 30px">
			<div class="row" style="margin:0">
				<div class="col-md-12 once-toolbar" style="margin:0 0 10px 0; padding:0">
					<div class="toolbar-right">
						<table>
							<tr>
								<td>&nbsp;页数&nbsp;<a id="currentPbind"></a>&nbsp;/&nbsp;<a id="totalPbind"></a></td>
								<td style="padding-left:10px"><div class="pagination-small"><ul id="bindP" style="display:inline"></ul></div></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="instancelist" id="instancelist">
			</div>
		</div>
		<div class="modal-footer">
			<button id="bind2vm" class="btn btn-primary" type="button" data-dismiss="modal">提交</button>
			<button id="bindcancel" class="btn btn-default" type="button" data-dismiss="modal">取消</button>
		</div>
	</div>
</div>