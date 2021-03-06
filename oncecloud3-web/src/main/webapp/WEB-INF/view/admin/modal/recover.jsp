<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/admin/modal/recover.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div id="modalcontent" userid="" class="modal-content">
		<div class="modal-header">
			<h4 id="modaltitle" oldname="" class="modal-title">修复服务器<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
                    <div class="item">
						<div class="control-label">登录名</div>
						<div class="controls">
							<input type="text" id="host_username" name="host_username" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">密码</div>
						<div class="controls">
							<input type="text" id="host_pwd" name="host_pwd" autofocus="">
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="recovercommit" type="button" class="btn btn-primary">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>
