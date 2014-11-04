<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="modal-dialog" style="width: 700px; margin-top: 100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				资源配额<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span></a>
			</h4>
		</div>
		<div class="modal-body">
			<div class="con">
				<p class="none">* 限定配额是为了防止资源滥用，如果有需要，可以联系我们申请扩大配额。</p>
			</div>
			<div class="row" style="margin: 30px 0">
				<div class="col-md-6">
					<table class="table table-bordered once-table">
						<thead>
							<tr>
								<th>资源类型</th>
								<th>已用配额</th>
								<th>总配额</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>公网IP</td>
								<td>${quotaUsed.quotaIP}</td>
								<td>${quotaTotal.quotaIP}</td>
							</tr>
							<tr>
								<td>主机</td>
								<td>${quotaUsed.quotaVM}</td>
								<td>${quotaTotal.quotaVM}</td>
							</tr>
							<tr>
								<td>备份</td>
								<td>${quotaUsed.quotaSnapshot}</td>
								<td>${quotaTotal.quotaSnapshot}</td>
							</tr>
							<tr>
								<td>映像</td>
								<td>${quotaUsed.quotaImage}</td>
								<td>${quotaTotal.quotaImage}</td>
							</tr>
							<tr>
								<td>硬盘</td>
								<td>${quotaUsed.quotaDiskN}</td>
								<td>${quotaTotal.quotaDiskN}</td>
							</tr>
							<tr>
								<td>SSH密钥</td>
								<td>${quotaUsed.quotaSsh}</td>
								<td>${quotaTotal.quotaSsh}</td>
							</tr>
							<tr>
								<td>防火墙</td>
								<td>${quotaUsed.quotaFirewall}</td>
								<td>${quotaTotal.quotaFirewall}</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-md-6">
					<table class="table table-bordered once-table">
						<thead>
							<tr>
								<th>资源类型</th>
								<th>已用配额</th>
								<th>总配额</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>路由器</td>
								<td>${quotaUsed.quotaRoute}</td>
								<td>${quotaTotal.quotaRoute}</td>
							</tr>
							<tr>
								<td>私有网络</td>
								<td>${quotaUsed.quotaVlan}</td>
								<td>${quotaTotal.quotaVlan}</td>
							</tr>
							<tr>
								<td>负载均衡器</td>
								<td>${quotaUsed.quotaLoadBalance}</td>
								<td>${quotaTotal.quotaLoadBalance}</td>
							</tr>
							<tr>
								<td>硬盘容量(GB)</td>
								<td>${quotaUsed.quotaDiskS}</td>
								<td>${quotaTotal.quotaDiskS}</td>
							</tr>
							<tr>
								<td>带宽(Mbps)</td>
								<td>${quotaUsed.quotaBandwidth}</td>
								<td>${quotaTotal.quotaBandwidth}</td>
							</tr>
							<tr>
								<td>内存(GB)</td>
								<td>${quotaUsed.quotaMemory}</td>
								<td>${quotaTotal.quotaMemory}</td>
							</tr>
							<tr>
								<td>CPU(个)</td>
								<td>${quotaUsed.quotaCpu}</td>
								<td>${quotaTotal.quotaCpu}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
