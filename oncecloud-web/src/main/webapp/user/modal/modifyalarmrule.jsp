<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	String alarmType = request.getParameter("alarmType");
	String ruleId = request.getParameter("ruleId");
	String ruleName = request.getParameter("ruleName");
	String ruleprio = request.getParameter("ruleprio");
	String ruleprot = request.getParameter("ruleprot");
	String rulesport = request.getParameter("rulesport");
	String unit = request.getParameter("unit");
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
<script src="<%=basePath %>js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="<%=basePath %>js/jquery.validate.js"></script>
<script src="<%=basePath %>js/uuid.js"></script>
<script src="<%=basePath %>user/js/modal/modifyalarmrule.js"></script>
<div class="modal-dialog" style="margin-top:100px" id="modal-type" alatype=<%=alarmType %> ruleId=<%=ruleId %> ruleName=<%=ruleName %> ruleprio=<%=ruleprio %> ruleprot=<%=ruleprot %> rulesport=<%=rulesport %> unit=<%=unit %>>
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">添加警告策略规则<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<div class="alarm-price-info">
				<div class="unit-price alert alert-info" style="">规则中的监控项是"监控周期"内每分钟的平均值</div>
			</div>
			<form class="form form-horizontal" id="createrule-form">
				<fieldset>
					<div class="item">
						<div class="control-label">监控项</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="rule_protocol" name="rule_protocol">
								</select>
							</div>
						</div>
					</div>
					<div class="item">
						<div class="control-label">条件</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="rule_mm" name="rule_mm">
									<option value="0">&lt;</option>
									<option value="1">&gt;</option>
								</select>
							</div>
						</div>
					</div>
					<div class="item">
						<div class="control-label">阈值</div>
						<div class="controls">
							<input type="text" id="rule_threshold" name="rule_threshold" style="width: 70px !important">
							<label class="alarm-unit" for="rule_threshold" id="alarm-unit"></label>
						</div>
					</div>
					<div class="item">
						<div class="control-label">触发条件</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="rule_condition" name="rule_condition">
									<option value="once">满足条件一次</option>
									<option value="multiple">连续多个周期满足条件</option>
								</select>
							</div>
						</div>
					</div>
					<div class="item" id="rperiod" style="display:none">
						<div class="control-label">连续周期数</div>
						<div class="controls">
							<input type="text" id="rule_period" name="rule_period" value="1" style="width: 70px !important">
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="createruleAction" type="button" class="btn btn-primary" style="margin-left:-100px">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>