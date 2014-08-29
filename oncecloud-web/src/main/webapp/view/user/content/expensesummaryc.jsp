<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1 style="font-size:22px; margin-top:30px">消费记录&nbsp;Consumptions</h1>
		<p class="lead" style="margin-top:10px"></p>
	</div>
	<ul class="nav nav-tabs once-tab">
    	<li class="tab-filter active"><a href="javascript:void(0)" id="summary"><span class="glyphicon glyphicon-list"></span>摘要</a></li>
  		<li class="tab-filter"><a href="javascript:void(0)" id="query"><span class="glyphicon glyphicon-search"></span>查询</a></li>
	</ul>
	<div class="once-pane">
		<div class="charge-summary">
			<h1>总消费额:&nbsp;&nbsp;
				<span class="price" id="resource-total"></span>
				<small>
					<a href="javascript:void(0)">查看帐户余额</a>
				</small>
			</h1>
			<div class="summary-item selected" type="instance">
				<span class="summary-name">主机</span>
				<span class="sum" id="instance-total"></span>
			</div>
			<div class="summary-item" type="volume">
				<span class="summary-name">硬盘</span>
				<span class="sum" id="volume-total"></span>
			</div>
			<div class="summary-item" type="snapshot">
				<span class="summary-name">备份</span>
				<span class="sum" id="snapshot-total"></span>
			</div>
			<div class="summary-item" type="eip">
				<span class="summary-name">公网</span>
				<span class="sum" id="eip-total"></span>
			</div>
			<div class="summary-item" type="image">
				<span class="summary-name">映像</span>
				<span class="sum" id="image-total"></span>
			</div>
		</div>
		<div class="once-toolbar">
			<button class="btn btn-primary" style="width:89px"><span class="glyphicon glyphicon-map-marker"></span>苏州</button>
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentPsummary"></a>&nbsp;/&nbsp;<a id="totalPsummary"></a></td>
						<td style="padding-left:10px"><div><ul id="summaryP" style="display:inline"></ul></div></td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="15%">ID</th>
					<th width="12%">名称</th>
					<th width="12%">状态</th>
					<th width="12%" style="text-align:right">金额 (元)</th>
					<th width="12%" style="text-align:right">单价</th>
					<th>创建时间</th>
					<th><!--op--></th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="SummaryModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>