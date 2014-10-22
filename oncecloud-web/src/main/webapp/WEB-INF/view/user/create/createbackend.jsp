<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="${basePath}js/jquery.validate.js"></script>
<script src="${basePath}js/uuid.js"></script>
<script src="${basePath}js/user/create/createbackend.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px height:400px">
	<div class="modal-content" id="backend" foreuuid="${foreUuid}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<h4 class="modal-title" id="title">添加后端<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
			</div>
			<div class="alert alert-info modal-alert" style="margin:12px 10px; padding:10px 10px">
				<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;添加后端服务后请检查对应主机的防火墙规则，确保端口流量可以通过。
			</div>
			<div class="modal-body">
				<form class="form form-horizontal" id="editBackend-form">
					<fieldset>
						<div class="item">
							<div class="control-label" style="width:160px">名称</div>
							<div class="controls" style="margin-left:180px">
								<input type="text" id="be_name" name="be_name" autofocus="" />
								
							</div>
						</div>
					</fieldset>
					<fieldset>
						<div class="item">
							<div class="control-label" style="width:160px">网络</div>
							<div class="controls" style="margin-left:180px">
								<a style="line-height:34px; font-size:14px;">基础网络</a>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<div class="item">
							<div class="control-label" style="width:160px">主机</div>
							<div class="controls" style="margin-left:180px">
								<div class="select-con">
									<select class="dropdown-select" id="be_vm" name="be_vm">				
  									</select>
  								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<div class="item">
							<div class="control-label" style="width:160px">端口</div>
							<div class="controls" style="margin-left:180px">
								<input type="text" id="be_port" name="be_port" placeholder="1~65535"/>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<div class="item">
							<div class="control-label" style="width:160px">权重</div>
							<div class="controls" style="margin-left:180px">
								<input type="text" id="be_weight" name="be_weight" placeholder="1~100"></input>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="modal-footer">
				<button id="createBEAction" type="button" class="btn btn-primary">提交</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
	</div>
</div>