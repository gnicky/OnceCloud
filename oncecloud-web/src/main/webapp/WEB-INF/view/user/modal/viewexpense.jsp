<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<script src="${basePath}js/user/modal/viewexpense.js"></script>
<div class="modal-dialog" style="margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">查看&nbsp;${showId}&nbsp;消费详情<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<div class="table-inner">
				<div class="once-pane">
					<div class="once-toolbar">
						<div class="fee-total">总计：<span id="detail-total" class="price"></span><span class="unit">元</span></div>
						<div class="toolbar-right">
							<table>
								<tr>
									<td>页数&nbsp;<a id="currentPsdetail"></a>&nbsp;/&nbsp;<a id="totalPsdetail"></a></td>
									<td style="padding-left:10px"><div><ul id="sdetailP" style="display:inline"></ul></div></td>
								</tr>
							</table>
						</div>
					</div>
					<table class="table table-bordered once-table">
						<thead>
							<tr>
								<th>计费时长</th>
								<th style="text-align:right">金额&nbsp;(元)</th>
								<th style="text-align:right">单价</th>
							</tr>
						</thead>
						<tbody id="tablebodydetail" type="${type}" uuid="${resourceUuid}">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>