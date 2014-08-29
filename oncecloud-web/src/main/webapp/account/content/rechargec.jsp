<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="row" style="margin:0">
	<div class="reg-header">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<div class="header-account">
				<a class="btn btn-outline btn-signin" href="<%=basePath %>UserAction?action=logout">退出</a>
			</div>
			<ul class="header-nav">
				<li class="nav-item"><a href="javascript:void(0)">首页</a></li>
				<li class="nav-item"><a href="<%=basePath%>account/login.jsp">控制台</a></li>
				<li class="nav-item"><a href="javascript:void(0)">文档</a></li>
				<li class="nav-item"><a href="javascript:void(0)">关于</a></li>
			</ul>
		</div>
	</div>
</div>
<div class="row" style="margin:0">
	<div class="reg-navs">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<h1>帐户信息</h1>
			<p class="lead">账户充值、充值记录查询</p>
			<ul>
				<li class="current">
					<a href="<%=basePath%>account/recharge.jsp" data-permalink="">账户余额</a>
				</li>
				<li>
					<a href="<%=basePath%>account/history.jsp" data-permalink="">充值记录</a>
				</li>
				<li>
					<a href="<%=basePath%>account/voucher.jsp" data-permalink="">体验券</a>
				</li>
			</ul>
		</div>
	</div>
</div>
<div class="row" style="margin:0">
	<div class="col-md-2">
	</div>
	<div class="col-md-8">
		<div class="promo">充值优惠：2014年12月31日前充值享受9折优惠。</div>
		<h2 style="margin-top:40px">账户余额&nbsp;:&nbsp;
			<span class="price" id="balance"></span>
		</h2>
		<ul class="nav nav-tabs" role="tablist">
  			<li class="active"><a href="#online" role="tab" data-toggle="tab">在线充值</a></li>
  			<li><a href="#bank" role="tab" data-toggle="tab">银行转账</a></li>
		</ul>
		<div class="tab-content">
  			<div class="tab-pane active" id="online">
  				<br>
				<form class="form form-horizontal" id="charge-form">
					<fieldset>
						<div class="item" style="margin-bottom:0px">
							<div class="control-label">充值金额</div>
							<div class="controls">
								<input style="width:150px" type="text" id="bill_number" name="bill_number" autofocus=""><span class="unit" id="yuan">元</span>
								<span class="help" style="margin-top:10px">提示：若充值过程中遇到<a href="http://help.alipay.com/lab/help_detail.htm?help_id=247928" target="_blank">交易限额</a>问题，请前往相应的网上银行进行调整后再继续。</span>
							</div>
						</div>
		                <div class="item">
							<div class="control-label">支付方式</div>
							<div class="controls">
								<label class="oc-inline"><input checked type="radio" name="db_throughout" value="5000">&nbsp;<img src="<%=basePath%>img/user/alipay.png"></label>
							</div>
						</div>
						<div class="charge-actions"><input class="btn btn-green btn-charge" id="confirmCharge" value="用户充值"></div>
					</fieldset>
				</form>
  			</div>
  			<div class="tab-pane" id="bank">
  				<br>
  				<table class="table table-bordered once-table">
					<thead>
						<tr>
							<th>开户银行</th>
							<th>账户名称</th>
							<th>帐号</th>
						</tr>
					</thead>
					<tbody id="tablebody">
						<tr>
							<td>中国工商银行苏州支行</td>
							<td>博纳云</td>
							<td>1234 1234 1234 1234 123</td>
						</tr>
					</tbody>
				</table>
				<div class="alert alert-warning">
  					<p>请在转帐或汇款时在留言栏或备注栏注明您单位名称或姓名、以及您在博纳云的账号，以便于我们区分汇款来源、并及时登记到您名下。建议您在完成汇款后把银行回单发送给 admin@beyondcent.com.cn</p>
  				</div>
  			</div>
		</div>
	</div>
</div>
<div class="row" style="margin:0">
	<div class="reg-footer">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<p>博纳云 BeyondCloud 使计算资源的交付更加简单、高效、可靠。</p>
			<a class="btn btn-green btn-huge" href="<%=basePath %>user/dashboard.jsp"><span class="glyphicon glyphicon-dashboard"></span>&nbsp;控制台</a>
		</div>
	</div>
</div>
<div id="RechargeModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="margin-top:100px; width:400px">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">等待支付<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
			</div>
			<div class="modal-body">
				<p class="alert alert-info modal-alert">请在新打开页面中完成充值</p>
			</div>
			<div class="modal-footer">
				<button id="chargeok" type="button" class="btn btn-primary" data-dismiss="modal">已完成支付<tton>
	        	<button type="button" class="btn btn-default" data-dismiss="modal">取消<tton>
	      	</div>
		</div>
	</div>
</div>
