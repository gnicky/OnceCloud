<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>监控告警&nbsp;Alarms</h1>
		<p class="lead">
			使用<em>监控告警&nbsp;(Alarms)</em>系统，您可以对自己的资源进行状态监控，并针对资源的监控属性制定告警规则，在资源状态异常时发出警告。创建一个告警策略，定义监控规则以及发生告警时的行为，将要监控的资源绑定到该策略上，即可令这些资源在规则生效时触发告警。
		</p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="${basePath}alarm/create">+&nbsp;新建</button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="destroy"><span class="glyphicon glyphicon-trash"></span>删除</a></li>
					<li><a class="btn-forbidden" id="bindalarm" url="${basePath}user/modal/bindalarm"><span class="glyphicon glyphicon-cloud"></span>绑定资源</a></li>
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
							<div><ul class="pagination" id="pageDivider" style="display:inline"></ul></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="4%"><!-- checkbox --></th>
					<th width="12%">ID</th>
					<th width="12%">名称</th>
					<th width="10%">状态</th>
					<th width="20%">类型</th>
					<th width="10%">监控周期</th>
					<th width="15%">最近修改是否应用</th>
					<th width="15%">创建时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
		<div id="applet"></div>
	</div>
	<div id="AlarmModalContainer" type="new" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>