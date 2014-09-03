<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User) session.getAttribute("user");
	} else {
%>
<script>
	window.location.href = "<%=basePath%>account/login.jsp";
</script>
<%
	return;
	}
%>
<link rel="stylesheet" href="<%=basePath%>css/bwizard.css" />
<script src="<%=basePath%>js/bwizard.js"></script>
<script src="<%=basePath%>js/jquery.validate.js"></script>
<script src="<%=basePath%>js/uuid.js"></script>
<script src="<%=basePath%>user/js/create/createalarm.js"></script>
<div class="modal-dialog" style="width:580px; margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				创建警告策略<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span> </a>
			</h4>
		</div>
		<div class="modal-body">
			<div class="row" style="margin:0">
				<div class="col-md-12">
					<div id="wizard" class="wizard">
						<ol>
							<li class="li-disable">参数设置</li>
							<li class="li-disable">警告规则</li>
							<li class="li-disable">警告行为</li>
						</ol>
						<div>
							<form id="paramsetting-form">
								<div class="wizard-inner">
									<div class="item">
										<div class="control-label">名称</div>
										<div class="controls">
											<input type="text" id="alarm_name" name="alarm_name" value="">
										</div>
									</div>
									<div class="item">
										<div class="control-label">资源类型</div>
										<div class="controls">
											<div class="select-con">
												<select class="dropdown-select" name="alarm_policy_type"
													id="policy">
													<option value="0" selected>主机</option>
													<option value="1">公网IP</option>
													<option value="2">路由器</option>
													<option value="3">负载均衡器监听器—HTTP协议</option>
													<option value="4">负载均衡器监听器—HTTPS协议</option>
													<option value="5">负载均衡器监听器—TCP协议</option>
													<option value="6">负载均衡器后端—HTTP协议</option>
													<option value="7">负载均衡器后端—TCP协议</option>
												</select>
											</div>
											<span class="help">只有类型匹配的规则才能应用于该类型的资源上</span>
										</div>
									</div>
									<div class="item">
										<div class="control-label">监控周期</div>
										<div class="controls">
											<div class="select-con">
												<select class="dropdown-select" name="period" id="period">
													<option value="1">1分钟</option>
													<option value="5" selected="">5分钟</option>
												</select>
											</div>
											<div class="alarm-price-info">
												<div class="unit-price alert alert-info" style="">本档位告警策略免费</div>
											</div>
										</div>
									</div>
								</div>
								<div class="wizard-action">
									<button class="btn btn-default btn-next first-next" type="button">下一步</button>
								</div>
							</form>
						</div>
						<div>
							<form id ="rulessetting-form">
							<div class="wizard-inner">
								<div class="item">
									<div class="alarm-rules">
										<div class="alert alert-info">
											<span>提示：任何一条规则满足条件都会触发告警</span>
										</div>
										<ul class="rules" id="rules">
											<li class="rule">
												<div class="select-con">
													<select class="dropdown-select rule-meter" name="meter"
														id="meter">
														<option value='0'>CPU利用率</option>
														<option value='2'>内存利用率</option>
														<option value='4'>磁盘使用量</option>
														<option value='6'>内网进流量</option>
														<option value='8'>内网出流量</option>
													</select>
												</div>
												<div class="select-con">
													<select class="dropdown-select rule-condition-type" name="condition_type" id="condition_type">
														<option value="0">&lt;</option>
														<option value="1">&gt;</option>
													</select>
												</div> <input class="short-input" type="text" name="count" id="count" value="70">
												<label class="alarm-unit" for="thresholds" id="alarm-unit">%</label>
												<a href="#" id="remove-rule" style="display:none;">
													<span class="glyphicon glyphicon-remove delete-rule"></span> 
												</a>
											</li>
										</ul>
										<a class="btn btn-default" id="add-rule" type="button"> 
											<span class="glyphicon glyphicon-plus"></span> 
											<span class="text">添加规则</span>
										</a>
									</div>
								</div>
							</div>
							</form>
							<div class="wizard-action">
								<button class="btn btn-default btn-back" type="button">上一步</button>
								<button class="btn btn-default btn-next second-next" type="button">下一步</button>
							</div>
						</div>
						<div>
							<form id="basicinfo-form">
								<div class="wizard-inner">
									<div class="item">
										<div class="control-label">发送通知</div>
										<div class="controls">
											<input type="radio" name="action_type" value="1" id="send_yes" checked=""> 
											<label class="inline name first-label" for="send_yes">是</label> 
											<input type="radio" name="action_type" value="0" id="send_no">
											<label class="inline name" for="send_no">否</label>
										</div>
										<div class="item advanced-options" style="display: block;" id="advanced-options">
											<div class="control-label">触发条件</div>
											<div class="controls">
												<input type="checkbox" name="trigger_status" value="1" id="alarm" checked="">
												<label class="inline name" for="alarm">资源变为告警时</label>
												<input type="checkbox" name="trigger_status" value="2" id="ok">
												<label class="inline name" for="ok">资源恢复正常时</label>
											</div>
										</div>
									</div>
								</div>
								<div class="wizard-action">
									<button class="btn btn-default btn-back" type="button">上一步</button>
									<button id="createAlarmAction" class="btn btn-primary btn-create"
										type="button">创建</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
