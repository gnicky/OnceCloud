<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String foreuuid = null;
	String type = null;
	String lbuuid = null;
	String fename = null;
	String feprotocol = null;
	String feport = null;
	String fepolicy = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		foreuuid = request.getParameter("foreuuid");
		type = request.getParameter("type");
		lbuuid = request.getParameter("lbuuid");
		fename = request.getParameter("name");
		feprotocol = request.getParameter("protocol");
		feport = request.getParameter("port");
		fepolicy = request.getParameter("policy");
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
<script src="<%=basePath %>user/js/create/createforeend.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px height:400px">
	<div class="modal-content" id="editForeend" type="<%=type%>" lbuuid="<%=lbuuid%>" foreuuid="<%=foreuuid%>" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<h4 class="modal-title" id="title">添加监听器<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="alert alert-info modal-alert" style="margin:12px 10px; padding:10px 10px">
			<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;添加监听器后请检查防火墙规则，确保端口流量可以通过，否则将无法从外网访问。
		</div>
		<div class="modal-body">
			<form class="form form-horizontal" id="editForeend-form">
				<fieldset>
					<div class="item">
						<div class="control-label" style="width:160px">名称</div>
						<div class="controls" style="margin-left:180px">
							<input type="text" id="fe_name" name="fe_name" autofocus="" value="<%=fename%>" />
						</div>
					</div>
				</fieldset>
				<fieldset>
					<div class="item">
						<div class="control-label" style="width:160px">监听协议</div>
						<div class="controls" style="margin-left:180px">
							<div class="select-con">
								<select class="dropdown-select" id="fe_protocol" name="fe_protocol">
									<% if("TCP".equals(feprotocol)) { %>
											<option value="HTTP">HTTP</option>
											<option value="TCP" selected>TCP</option>
										<% } else { %>
											<option value="HTTP" selected>HTTP</option>
											<option value="TCP">TCP</option>
										<% } %>
									</select>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<div class="item">
						<div class="control-label" style="width:160px">监听端口</div>
						<div class="controls" style="margin-left:180px">
							<input type="text" id="fe_port" name="fe_port" placeholder="1~65535" value="<%=feport%>" />
						</div>
					</div>
				</fieldset>
				<fieldset>
					<div class="item">
						<div class="control-label" style="width:160px">负载方式</div>
						<div class="controls" style="margin-left:180px">
							<div class="select-con">
								<select class="dropdown-select" id="fe_policy" name="fe_policy">
									<% if ("1".equals(fepolicy)) { %>
										<option value="0">轮询</option>
										<option value="1" selected>最小响应时间</option>
									<% } else { %>
									<option value="0" selected>轮询</option>
										<option value="1">最小响应时间</option>
									<% } %>
								</select>
							</div>
						</div>
					</div>
				</fieldset>	
			</form>
		</div>
		<div class="modal-footer">
			<button id="ForeendAction" type="button" class="btn btn-primary">提交</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		</div>
	</div>
</div>