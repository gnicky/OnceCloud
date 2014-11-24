<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basepath}js/jquery.validate.js"></script>
<script src="${basepath}js/admin/modal/hamanager.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content" id="modalcontent" poolid="">
		<div class="modal-header">
			<h4 class="modal-title" id="modaltitle">服务器电源管理<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">资源池UUID</div>
						<div class="controls">
							<input type="text" id="pool_uuid" name="pool_uuid" disabled class="oc-disable" >
						</div>
					</div>
					<div class="item">
						<div class="control-label">资源池名称</div>
						<div class="controls">
							<input type="text" id="pool_name" name="pool_name" disabled class="oc-disable" >
						</div>
					</div>
					
					<div class="item">
						<div class="control-label">主服务器地址</div>
						<div class="controls">
							<input type="text" id="master_ip" name="master_ip" disabled class="oc-disable" >
						</div>
					</div>
					
					<div class="item">
						<div class="control-label">高可用路径</div>
						<div class="controls">
							<input type="text" id="ha_path" name="ha_path"  >
						</div>
					</div>
					
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="startHaAction" type="button" class="btn btn-default">开启高可用</button>
			<button id="closeHaAction" type="button" class="btn btn-danger">关闭高可用</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>