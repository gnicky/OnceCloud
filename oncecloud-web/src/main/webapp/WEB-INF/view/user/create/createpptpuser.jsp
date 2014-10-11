<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/create/createpptpuser.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				添加VPN用户<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span> </a>
			</h4>
		</div>
		<div class="modal-body" style="padding:0px 10px">
			<form class="form form-horizontal" id="pptpuser-form">
				<fieldset>
					<div class="item">
						<div style="padding-top:10px">
							<div class="control-label">账户</div>
							<div class="controls">
								<input type="text" id="pptp_name" name="pptp_name"
									style="width: 200px !important">
							</div>
						</div>
						<div style="padding-top:10px">
							<div class="control-label">密码</div>
							<div class="controls">
								<input type="password" id="pptp_pwd" name="pptp_pwd"
									style="width: 200px !important"> 
								<label class="inline">
									<input type="checkbox" id="display-pwd" name="toggle_passwd">
									&nbsp;显示密码
								</label>
								<p class="alert alert-info" id="pw-alert"
									style="margin:10px 0; width:240px; padding:10px">密码至少8位，包括大小写字母和数字。</p>
							</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="pptpuser-submit" type="button" class="btn btn-primary">提交</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		</div>
	</div>
</div>