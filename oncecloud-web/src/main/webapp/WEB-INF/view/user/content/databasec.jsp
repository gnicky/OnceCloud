<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>数据库&nbsp;Databases</h1>
		<p class="lead" style="padding-top:10px">
			<em>数据库&nbsp;(Database)</em>是按照数据结构来组织、存储和管理数据的仓库，由虚拟化技术支持，从而可以保证数据的高可用性，为用户提供快捷、稳定的数据服务。
		</p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="${basePath}user/create/createdatabase.jsp">+&nbsp;新建</button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="start"><span class="glyphicon glyphicon-play"></span>启动</a></li>
					<li><a class="btn-forbidden" id="close"><span class="glyphicon glyphicon-stop"></span>关闭</a></li>
					<li><a class="btn-forbidden" id="delete"><span class="glyphicon glyphicon-trash"></span>删除</a></li>
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
							<div><ul id="pageDivider" style="display:inline"></ul></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="4%"></th>
					<th width="12%">ID</th>
					<th width="12%">名称</th>
					<th width="12%">状态</th>
					<th width="10%">最大并发数</th>
					<th width="10%">端口</th>
					<th width="14%">网络</th>
					<th width="14%">公网IP</th>
					<th width="12%">运行时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="DatabaseModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>