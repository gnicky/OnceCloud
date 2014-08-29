<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<script src="<%=basePath %>js/jquery.validate.js"></script>
<script src="<%=basePath %>admin/js/create/createrack.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content" id="modalcontent" rackid="">
		<div class="modal-header">
			<h4 class="modal-title" id="modaltitle">添加机架<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">机架名称</div>
						<div class="controls">
							<input type="text" id="rack_name" name="rack_name" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">数据中心</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="rack_location" name="rack_location">
								</select>
							</div>
						</div>
					</div>
                    <div class="item">
						<div class="control-label">机架描述</div>
						<div class="controls">
							<textarea rows="5" style="width:300px" id="rack_desc" name="rack_desc"></textarea>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="createRackAction" type="button" class="btn btn-primary">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>