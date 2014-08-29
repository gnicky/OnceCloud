<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	User user = null;
	String alarmUuid = null;
	if (session.getAttribute("user") != null) {
		user = (User) session.getAttribute("user");
		alarmUuid = request.getParameter("alarmUuid");
	} else {
%>
<script>
	window.location.href = "<%=basePath%>account/login.jsp";
</script>
<%
	return;
	}
%>
<script src="<%=basePath%>js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="<%=basePath%>js/jquery.validate.js"></script>
<script src="<%=basePath%>js/uuid.js"></script>
<script src="<%=basePath%>user/js/modal/modifyalert.js"></script>
<link rel="stylesheet" href="<%=basePath%>css/alarms.css" />
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				修改警告规则<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span> </a>
			</h4>
		</div>
		<div class="modal-body">
			<form class="form form-horizontal" id="modify-form"
				uuid=<%=alarmUuid%>>
				<fieldset>
					<div class="item">
						<div class="alarm-price-info">
							<div class="unit-price alert alert-info" style="">本档位告警策略免费</div>
						</div>
						<div class="control-label">发送通知</div>
						<div class="controls">
							<input type="radio" name="action_type" value="1" id="send_yes"
								checked=""> <label class="inline name first-label"
								for="send_yes">是</label> <input type="radio" name="action_type"
								value="0" id="send_no"> <label class="inline name"
								for="send_no">否</label>
						</div>
						<div class="item advanced-options" style="display: block;"
							id="advanced-options">
							<div class="control-label">触发条件</div>
							<div class="controls">
								<input type="checkbox" name="trigger_status" value="1"
									id="alarm" checked=""> <label class="inline name"
									for="alarm">资源变为告警时</label> <input type="checkbox"
									name="trigger_status" value="2" id="ok"> <label
									class="inline name" for="ok">资源恢复正常时</label>
							</div>
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