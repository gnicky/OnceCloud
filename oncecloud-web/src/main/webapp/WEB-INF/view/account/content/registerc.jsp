<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="row" style="margin:0">
	<div class="reg-header">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<div class="header-account">
				<a class="btn btn-outline btn-signin" href="${basePath}login">登录</a>
				<a class="btn btn-green btn-signup" href="${basePath}account/register">注册</a>
			</div>
			<ul class="header-nav">
				<li class="nav-item"><a href="javascript:void(0)">首页</a></li>
				<li class="nav-item"><a href="${basePath}login">控制台</a></li>
				<li class="nav-item"><a href="javascript:void(0)">文档</a></li>
				<li class="nav-item"><a href="javascript:void(0)">关于</a></li>
			</ul>
		</div>
	</div>
</div>
<script>
	$(function() {
		refresh();
	});

	function refresh() {
		$("#authImg").attr("src", "${basePath}captcha?" + Math.random());
	}
</script>
<div class="row" style="margin:0">
	<div class="col-md-2">
	</div>
	<div class="col-md-8">
		<form class="form form-horizontal form-wrapper" id="register-form" base="${basePath}">
			<fieldset>
				<legend>注册</legend>
				<div class="item">
					<div class="control-label">用户名</div>
					<div class="controls">
						<input type="text" id="user_name" name="user_name" autofocus="">
					</div>
				</div>
				<div class="item">
					<div class="control-label">密码</div>
					<div class="controls">
						<input type="password" id="password" name="password" autofocus="">
					</div>
				</div>
				<div class="item">
					<div class="control-label">邮箱</div>
					<div class="controls">
						<input type="text" id="email" name="email" autofocus="">
					</div>
				</div>
				<div class="item" style="margin:12px 0 0 0">
					<div class="control-label">手机号</div>
					<div class="controls">
						<input type="text" id="telephone" name="telephone" autofocus="" placeholder="仅限中国大陆手机号">
						<span class="help">请确保手机号码长期有效，若使用过期手机号码注册将会被视为无效帐户。</span>
					</div>
				</div>
				<div class="item" style="margin:0">
					<div class="control-label">验证码</div>
					<div class="controls">
						<input type="text" id="vercode" name="vercode" autofocus="" placeholder="验证码">
						<div>
							<img alt="" id="authImg" align="absmiddle">
							<a class="ver-change" onclick="refresh()">换一个</a>
						</div>
					</div>
				</div>
				<div class="item">
					<div class="control-label"></div>
					<div class="controls">
						<input class="btn btn-green btn-session" id="confirmRegister" value="注册">
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</div>
<div class="row" style="margin:0">
	<div class="reg-footer">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<p>博纳云 BeyondCloud 使计算资源的交付更加简单、高效、可靠。</p>
			<a class="btn btn-green btn-huge" href="${basePath}account/register"><span class="glyphicon glyphicon-log-in"></span>&nbsp;现在就注册</a>
		</div>
	</div>
</div>