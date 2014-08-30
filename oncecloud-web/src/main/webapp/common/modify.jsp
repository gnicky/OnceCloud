<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String modifytype = null;
	String modifystr = null;
	String modifyuuid = null;
	String modifyname = null;
	String modifydesc = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		modifytype = request.getParameter("modifytype");
		modifyuuid = request.getParameter("modifyuuid");
		modifyname = request.getParameter("modifyname");
		modifydesc = request.getParameter("modifydesc");
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
<script src="js/modify.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">修改属性<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<form class="form form-horizontal" id="modify-form" type=<%=modifytype%> uuid=<%=modifyuuid%>>
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="name" name="name" autofocus="" value="<%=modifyname%>" />
						</div>
					</div>
					<div class="item">
						<div class="control-label">描述</div>
						<div class="controls">
							<textarea rows="3" style="width:300px" id="desc" name="desc"><%=modifydesc%></textarea>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="modifyinfomation" type="button" class="btn btn-primary">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>