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
<script src="<%=basePath %>js/uuid.js"></script>
<script src="<%=basePath %>user/js/create/createrouter.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content" id="modalcontent" rtid="">
		<div class="modal-header">
			<h4 class="modal-title" id="modaltitle">新建路由器<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<div class="alert alert-info modal-alert">
				<h2>总价: <span id="price-hour">0.02</span><span class="unit">元/小时</span>× <span id="hour-number">1</span> = <span id="price-total">0.02</span><span class="unit">元/小时</span><span class="unit none">（合<strong><span id="price-month">14.40</span></strong>元/月）</span>
				</h2>
			</div>
			<div class="alert alert-warning modal-alert" style="height: 0px; overflow: hidden; padding: 0px 10px;">
			</div>
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="rt_name" name="rt_name" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">类型</div>
						<div class="controls">
							<label class="oc-inline"><input type="radio" name="rt_type" checked value="250">&nbsp;小型</label>
							<label class="oc-inline"><input type="radio" name="rt_type" value="500">&nbsp;中型</label>
							<label class="oc-inline"><input type="radio" name="rt_type" value="1000">&nbsp;大型</label>
						</div>
						<div class="controls help" style="padding:0px;" id="typeinfo"> </div>
					</div>
					<div class="item">
						<div class="control-label">防火墙</div>
						<div class="controls">
							<div class="select-con">
							<select class="dropdown-select" id="rt_firewall" name="rt_firewall"></select>
							</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer" style="margin-top:0">
			<button id="createRouterAction" type="button" class="btn btn-primary" >确定</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>