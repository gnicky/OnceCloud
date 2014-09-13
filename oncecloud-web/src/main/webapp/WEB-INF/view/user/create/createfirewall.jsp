<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/create/createfirewall.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">新建防火墙<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<form class="form form-horizontal" id="createfw-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="firewall_name" name="firewall_name" autofocus="">
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="createfirewallAction" type="button" class="btn btn-primary" style="margin-left:-100px">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>