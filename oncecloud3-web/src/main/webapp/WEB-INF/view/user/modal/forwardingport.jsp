<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/modal/forwardingport.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				添加端口转发规则<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span> </a>
			</h4>
		</div>
		<div class="modal-body" style="padding:0px 10px">
			<form class="form form-horizontal" id="forwardport-form">
				<fieldset>
					<div class="item">
						<div class="alarm-price-info">
							<div class="unit-price alert alert-info" style="">添加规则后请检查防火墙规则，确保源端口（范围）流量可以通过，否则从外网无法访问您的服务。</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">名称</div>
							<div class="controls">
								<input type="text" id="pf_name" name="pf_name" style="width: 70px !important">
							</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">协议</div>
							<div class="controls">
								<div class="select-con">
									<select class="dropdown-select" name="period" id="period">
										<option>TCP</option>
										<option>UDP</option>
									</select>
								</div>
							</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">源端口</div>
							<div class="controls">
								<input type="text" id="pf_srcport" name="pf_srcport" style="width: 70px !important">
							</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">内网IP</div>
							<div class="controls">
								<input type="text" id="pf_desIp" name="pf_desIp" style="width: 170px !important">
							</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">内网端口</div>
							<div class="controls">
								<input type="text" id="pf_desport" name="pf_desport" style="width: 70px !important">
							</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="pfsubmit" type="button" class="btn btn-primary">提交</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		</div>
	</div>
</div>