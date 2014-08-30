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
<script src="<%=basePath%>user/js/modal/modifyperiod.js"></script>
<link rel="stylesheet" href="<%=basePath%>css/alarms.css" />
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				修改监控周期<a class="close" data-dismiss="modal" aria-hidden="true"><span
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
						<div style="padding-left:150px">
							<div class="control-label">监控周期</div>
							<div class="controls">
								<div class="select-con">
									<select class="dropdown-select" name="period" id="period">
										<option value="1">1分钟</option>
										<option value="5" selected="">5分钟</option>
									</select>
								</div>
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