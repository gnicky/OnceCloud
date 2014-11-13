<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input name="hidden-area" type="hidden" value="${user.userId}" level="${user.userLevel}">
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>告警日志&nbsp;AlarmLog</h1>
		<p class="lead" style="margin-top: 10px">
			<em>告警日志&nbsp;(AlarmLog)</em>云平台CPU，内存，硬盘IO等咨询的告警日志
		</p>
	</div>
	
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh">
				<span class="glyphicon glyphicon-refresh" style="margin-right: 0"></span>
			</button>

			
				<button id="delete" class="btn btn-primary btn-disable" disabled="disabled">
					<span class="glyphicon glyphicon-trash"></span>删除</button>
						<button id="read" class="btn btn-primary btn-disable" disabled="disabled">
					<span class="glyphicon glyphicon-share"></span>标为已读</button>

		
			
			<input class="search" id="search" value="">
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentP"></a>&nbsp;/&nbsp;<a
							id="totalP"></a></td>
						<td style="padding-left: 10px">
							<div>
								<ul class="pagination" id="pageDivider" style="display: inline"></ul>
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
					<th width="14%">ID</th>
					<th width="14%">告警对象</th>
					<th width="10%">告警类型</th>
					<th width="30%">告警详情</th>
					<th width="14%">状态</th>
				    <th width="14%">告警时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
    <div id="AlarmModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>
