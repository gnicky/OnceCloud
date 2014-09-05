<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/jquery.validate.js"></script>
<script src="${basePath}js/uuid.js"></script>
<script src="${basePath}js/user/create/createvnet.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content" id="modalcontent" vnid="">
		<div class="modal-header" >
			<h4 class="modal-title" id="modaltitle">新建私有网络<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<div class="alert alert-warning modal-alert" style="height: 0px; overflow: hidden; padding: 0px 10px;">
			</div>
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="vn_name" name="vn_name" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">描述</div>
						<div class="controls">
							<textarea rows="5" style="width:300px" id="vn_desc" name="vn_desc"></textarea>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer" style="margin-top:0">
			<button id="createVnetAction" type="button" class="btn btn-primary" >确定</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>