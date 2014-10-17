<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/admin/modal/savetodb.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div id="modalcontent" userid="" class="modal-content">
		<div class="modal-header">
			<h4 id="modaltitle" oldname="" class="modal-title">新建用户<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">虚拟机uuid</div>
						<div class="controls">
							<input type="text" id="vm_uuid" name="vm_uuid" autofocus="">
						</div>
					</div>
                    
					<div id="pwd">
                    <div class="item">
						<div class="control-label">虚拟机密码</div>
						<div class="controls">
							<input type="text" id="vm_pwd" name="vm_pwd" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">虚拟机名称</div>
						<div class="controls">
							<input type="text" id="vm_name" name="vm_name" autofocus="">
						</div>
					</div>
					</div>
                    <div class="item">
						<div class="control-label">虚拟机ip</div>
						<div class="controls">
							<input type="text" id="vm_ip" name="vm_ip" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">平台</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="vm_platform" name="vm_platform">
									<option value="0">windows</option>
                                    <option value="1">Linux</option>
								</select>
							</div>
						</div>
					</div>
					<div id="pool">
                    <div class="item">
						<div class="control-label">用户</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="vm_user" name="vm_user">
								</select>
							</div>
						</div>
					</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="savetodbcommit" type="button" class="btn btn-primary">提交</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>
