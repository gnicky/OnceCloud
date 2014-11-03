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
							<div class="unit-price alert alert-info" style="">打开 PPTP服务后请检查防火墙规则，确保 PPTP端口(默认为 UDP 1723)流量可以通过，否则从外网无法接入您的PPTP服务</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="pfsubmit" type="button" class="btn btn-primary">确定</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		</div>
	</div>
</div>