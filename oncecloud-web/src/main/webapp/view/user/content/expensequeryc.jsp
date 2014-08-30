<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1 style="font-size:22px; margin-top:30px">消费记录&nbsp;Consumptions</h1>
		<p class="lead" style="margin-top:10px"></p>
	</div>
	<ul class="nav nav-tabs once-tab">
    	<li class="tab-filter"><a href="javascript:void(0)" id="summary"><span class="glyphicon glyphicon-list"></span>摘要</a></li>
  		<li class="tab-filter active"><a href="javascript:void(0)" id="query"><span class="glyphicon glyphicon-search"></span>查询</a></li>
	</ul>
	<div class="once-pane">
		<div class="query-bar">
			<form class="form form-horizontal">
				<h3>查询条件:</h3>
				<div class="item">
					<div class="control-label">数据中心</div>
					<div class="controls">
						<label class="inline"><a>苏州</a></label>
					</div>
				</div>
				<div class="item">
					<div class="control-label">资源类型</div>
					<div class="controls">
						<label class="inline"><input type="radio" value="instance" name="resource-type">主机</label>
						<label class="inline"><input type="radio" value="volume" name="resource-type">硬盘</label>
						<label class="inline"><input type="radio" value="snapshot" name="resource-type">备份</label>
						<label class="inline"><input type="radio" value="eip" name="resource-type">公网</label>
						<label class="inline"><input type="radio" value="image" name="resource-type">映像</label>
					</div>
				</div>
				<div class="item">
					<div class="control-label">月份</div>
					<div class="controls">
						<input type="text" value="" id="start-time" style="margin-left:10px">
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" id="query-submit">查询</button>
					<button class="btn btn-default" id="reset-submit">重置</button>
				</div>
			</form>
		</div>
		<div id="query-result" style="display:none">
			<h3>查询结果:</h3>
			<div class="once-toolbar">
				<div class="fee-total">总计：<span id="total-price" class="price"></span><span class="unit">元</span></div>
				<div class="toolbar-right">
					<table>
						<tr>
							<td>每页&nbsp;</td>
							<td><input id="limit" name="limit" class="page" value="10"></td>
							<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentPquery"></a>&nbsp;/&nbsp;<a id="totalPquery"></a></td>
							<td style="padding-left:10px"><div><ul id="queryP" style="display:inline"></ul></div></td>
						</tr>
					</table>
				</div>
			</div>
			<table class="table table-bordered once-table">
				<thead>
					<tr>
						<th width="30%">ID</th>
						<th width="30%">计费时长</th>
						<th style="text-align:right">金额&nbsp;(元)</th>
						<th style="text-align:right">单价</th>
					</tr>
				</thead>
				<tbody id="tablebody">
				</tbody>
			</table>
		</div>
	</div>
	<div id="QueryModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>