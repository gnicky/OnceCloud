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
				<li class="current">
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
		<h2 style="margin-top:40px">
			充值记录
		</h2>
		<div class="col-md-12 once-toolbar" style="padding:0">
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentP"></a>&nbsp;/&nbsp;<a id="totalP"></a></td>
						<td style="padding-left:10px">
							<div><ul id="pageDivider" style="display:inline"></ul></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
  		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th>充值金额（单位：元）</th>
					<th>类型</th>
					<th>时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
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