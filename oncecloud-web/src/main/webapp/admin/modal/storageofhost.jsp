<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String hostuuid = null;
	String detailstr = null;
	String hostname = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		hostuuid = request.getParameter("hostuuid");
		hostname = request.getParameter("hostname");
		detailstr = "host-" + hostuuid.substring(0, 8);
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<script src="<%=basePath %>js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="<%=basePath %>js/uuid.js"></script>
<script src="<%=basePath %>admin/js/modal/storageofhost.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">服务器&nbsp;<%=hostname %>&nbsp;存储列表<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<div class="table-inner">
				<div class="once-pane sr-pane">
					<table class="table table-bordered once-table">
						<thead>
							<tr>
								<th>ID</th>
								<th>地址</th>
								<th>挂载目录</th>
								<th>文件系统</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="tablebodydetail" hostuuid="<%=hostuuid%>">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
