<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/create/createfilterrule.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">新建规则<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<form class="form form-horizontal" id="createrule-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="rule_name" name="rule_name" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">优先级</div>
						<div class="controls">
							<input type="text" id="rule_priority" name="rule_priority" value="0">
							<span class="help">最多可以定义100条规则</span>
						</div>
					</div>
					<div class="item">
						<div class="control-label">协议</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="rule_protocol" name="rule_protocol">
									<option value="TCP">TCP</option>
									<option value="UDP">UDP</option>
									<option value="ICMP">ICMP</option>
								</select>
							</div>
						</div>
					</div>
					<div class="item">
						<div class="control-label">起始端口</div>
						<div class="controls">
							<input type="text" id="rule_sport" name="rule_sport">
						</div>
					</div>
					<div class="item">
						<div class="control-label">结束端口</div>
						<div class="controls">
							<input type="text" id="rule_eport" name="rule_eport">
						</div>
					</div>
					<div class="item">
						<div class="control-label">源IP</div>
						<div class="controls">
							<input type="text" id="rule_ip" name="rule_ip">
							<span class="help">例如 192.168.9.1/24，不填表示所有IP地址</span>
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