<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<script src="<%=basePath %>js/jquery.validate.js"></script>
<script src="<%=basePath %>admin/js/create/createaddress.js"></script>
<div class="modal-dialog" style="margin-top:100px; width:550px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">添加地址池<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body" style="padding:10px 0">
			<div class="alert alert-info modal-alert" style="margin:10px 70px 20px 70px; font-size:16px; text-align:center">
				可添加地址的范围：*.*.*.1 - *.*.*.255
			</div>
			<form class="form form-horizontal" id="create-form">
				<fieldset>
					<div class="item">
						<div class="control-label" style="width:150px">起始地址</div>
						<div class="controls" style="margin-left:170px">
							<input type="text" style="width:60px" id="start_addr_a" name="start_addr_a" >
							<input type="text" style="width:60px; margin-left:5px" id="start_addr_b" name="start_addr_b" >
							<input type="text" style="width:60px; margin-left:5px" id="start_addr_c" name="start_addr_c" >
							<input type="text" style="width:60px; margin-left:5px"  id="start_addr_d" name="start_addr_d" >
						</div>
					</div>
					<div class="item">
						<div class="control-label" style="width:150px">结束地址</div>
						<div class="controls" style="margin-left:170px">
							<input type="text" class="oc-disable" style="width:60px" id="end_addr_a" name="end_addr_a"  disabled>
							<input type="text" class="oc-disable" style="width:60px; margin-left:5px" id="end_addr_b" name="end_addr_b"  disabled>
							<input type="text" class="oc-disable" style="width:60px; margin-left:5px" id="end_addr_c" name="end_addr_c"  disabled>
							<input type="text" style="width:60px; margin-left:5px"  id="end_addr_d" name="end_addr_d" >
						</div>
					</div>	
					<div class="item" id="typeitem">
						<div class="control-label" style="width:150px">网络类型</div>
						<div class="controls" style="margin-left:170px">
								<div class="select-con">
									<select class="dropdown-select" id="eip_type" name="eip_type">
										<option value="0">电信</option>
	                                    <option value="1">联通</option>
									</select>
								</div>
					   	</div>
					</div>
					<div class="item" id="ifitem">
						<div class="control-label" style="width:150px">网关接口</div>
						<div class="controls" style="margin-left:170px">
								<div class="select-con">
									<select class="dropdown-select" id="eip_if" name="eip_if">
										<option value="eth1">Interface eth1</option>
	                                    <option value="eth2">Interface eth2</option>
	                                    <option value="eth3">Interface eth3</option>
	                                    <option value="eth4">Interface eth4</option>
	                                    <option value="eth5">Interface eth5</option>
	                                    <option value="eth6">Interface eth6</option>
	                                    <option value="eth7">Interface eth7</option>
	                                    <option value="eth8">Interface eth8</option>
	                                    <option value="eth9">Interface eth9</option>
									</select>
								</div>
					   	</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="modal-footer">
			<button id="addIPAction" type="button" class="btn btn-primary">添加</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
      	</div>
	</div>
</div>