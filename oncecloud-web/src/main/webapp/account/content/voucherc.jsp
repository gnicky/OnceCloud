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
				<li>
					<a href="<%=basePath%>account/recharge.jsp" data-permalink="">账户余额</a>
				</li>
				<li>
					<a href="<%=basePath%>account/history.jsp" data-permalink="">充值记录</a>
				</li>
				<li class="current">
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
		<div class="promo info">体验券申请将在24小时内得到回复，每位新注册用户只能申请一次体验券。</div>
		<h2 style="margin-top:40px">
			体验券
		</h2>
		<div class="row" style="margin:0">
			<div class="apply_list" id="apply_list" style="padding:0">
		      	<ul>
					<li value="10">面值<b>10</b>元</li>
					<li value="20">面值<b>20</b>元</li>
					<li value="50">面值<b>50</b>元</li>
					<li value="100">面值<b>100</b>元</li>
					<li value="150">面值<b>150</b>元</li>
					<li value="200">面值<b>200</b>元</li>
		      	</ul>
			</div>
		</div>
		<div class="charge-actions">
			<button id="applyVoucherAction" type="button" class="btn btn-green btn-charge">申请</button>
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