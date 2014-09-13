<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/jquery.validate.js"></script>
<script src="${basePath}js/uuid.js"></script>
<script src="${basePath}js/user/create/createloadbalance.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">新建负载均衡<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="lb_name" name="lb_name" autofocus="">
						</div>
					</div>
                    <div class="item">
						<div class="control-label">最大连接数</div>
						<div class="controls">
							<label class="oc-inline"><input checked type="radio" name="lb_type" value="5000">&nbsp;5,000</label>
							<label class="oc-inline"><input type="radio" name="lb_type" value="20000">&nbsp;20,000</label>
							<label class="oc-inline"><input type="radio" name="lb_type" value="40000">&nbsp;40,000</label>
							<label class="oc-inline"><input type="radio" name="lb_type" value="100000">&nbsp;100,000</label>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer" style="margin-top:0">
			<button id="createLBAction" type="button" class="btn btn-primary" >确定</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>