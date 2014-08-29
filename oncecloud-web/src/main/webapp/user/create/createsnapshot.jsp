<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String rsid = null;
	String rstype = null;
	String rsname = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		rsid = request.getParameter("rsid");
		rstype = request.getParameter("rstype");
		rsname = request.getParameter("rsname");
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
<script src="<%=basePath %>js/jquery.validate.js"></script>
<script src="<%=basePath %>js/uuid.js"></script>
<script src="<%=basePath %>user/js/create/createsnapshot.js"></script>
<div class="modal-dialog" id="modal-dialog" style="margin-top:100px" rsid="<%=rsid%>" rstype="<%=rstype%>" rsname="<%=rsname%>">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">新建备份<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<div class="alert alert-info modal-alert">
				<h4>计费说明：</h4>
				<div>备份的价格 ＝ 备份链上所有备份点空间总和 (GB) * 0.001 元/小时，<span class="none">不足 1GB 按 1GB 计费</span>。</div>
			</div>
			<div class="alert alert-warning modal-alert" style="height: 0px; overflow: hidden; padding: 0px 10px;">
			</div>
			<form class="form" id="backupinfo-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="snapshot_name" name="snapshot_name" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">资源</div>
						<div class="controls" id="resource-list">
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="createsnapshotAction" type="button" class="btn btn-primary">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>