<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1>用户&nbsp;Users</h1>
		<p class="lead" style="margin-top:10px">
			<em>用户&nbsp;(User)</em>是云平台的具体使用者，可以通过管理后台对用户进行增删改查，并对用户的资源限额进行调整。
		</p>
	</div>
    <div class="once-pane">
    	<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="<%=basePath %>admin/create/createuser.jsp">
				<span class="glyphicon glyphicon-user"></span>新建用户
			</button>
            <div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="delete" disabled><span class="glyphicon glyphicon-trash"></span>删除</a></li>
				</ul>
			</div>
            <input class="search" id="search" value="">
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentP"></a>&nbsp;/&nbsp;<a id="totalP"></a></td>
						<td style="padding-left:10px">
							<div>
								<ul id="pageDivider" style="display:inline">
								</ul>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
        <table class="table table-bordered once-table">
			<thead>
				<tr>
                	<th width="4%"></th>
					<th width="10%">用户名</th>
					<th width="15%">邮箱</th>
					<th width="10%">联系电话</th>
					<th width="10%">公司</th>
                    <th width="10%">级别</th>
					<th width="15%">注册时间</th>
					<th width="10%">申请代金券</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="UserModalContainer" type="new" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
    </div> 
</div>