<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/create/createdatabase.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">新建数据库<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label">名称</div>
						<div class="controls">
							<input type="text" id="db_name" name="db_name" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">用户名</div>
						<div class="controls">
							<input type="text" id="db_user" name="db_user" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">密码</div>
						<div class="controls">
							<input type="text" id="db_pwd" name="db_pwd" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">端口</div>
						<div class="controls">
							<input type="text" id="db_port" name="db_port" autofocus="">
						</div>
					</div>
					<div class="item">
						<div class="control-label">数据库类型</div>
						<div class="controls">
							<div class="select-con">
								<select class="dropdown-select" id="db_type" name="db_type">
									<option value="MySQL">MySQL</option>
								</select>
							</div>
						</div>
					</div>
                    <div class="item">
						<div class="control-label">最大并发数</div>
						<div class="controls">
							<label class="oc-inline"><input checked type="radio" name="db_throughout" value="5000">&nbsp;5,000</label>
							<label class="oc-inline"><input type="radio" name="db_throughout" value="20000">&nbsp;20,000</label>
							<label class="oc-inline"><input type="radio" name="db_throughout" value="40000">&nbsp;40,000</label>
							<label class="oc-inline"><input type="radio" name="db_throughout" value="100000">&nbsp;100,000</label>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer" style="margin-top:0">
			<button id="createDatabaseAction" type="button" class="btn btn-primary" >确定</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>