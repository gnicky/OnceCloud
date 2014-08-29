<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%@ page language="java" import="com.oncecloud.manager.QuotaManager"%>
<%@ page language="java" import="com.oncecloud.hbm.Quota"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<div class="modal-dialog" style="width:700px; margin-top:100px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">资源配额<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
		</div>
		<div class="modal-body">
			<div class="con">
				<p class="none">* 限定配额是为了防止资源滥用，如果有需要，可以联系我们申请扩大配额。</p>
			</div>
			<%
				QuotaManager qm = new QuotaManager();
				Quota quotaU = qm.getQuotaUsed(user.getUserId());
				Quota quotaT = qm.getQuotaTotal(user.getUserId());
			%>
			<div class="row" style="margin:30px 0">
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
								<td><%=quotaU.getQuotaIP() %></td>
								<td><%=quotaT.getQuotaIP() %></td>
							</tr>
							<tr>
								<td>主机</td>
								<td><%=quotaU.getQuotaVM() %></td>
								<td><%=quotaT.getQuotaVM() %></td>
							</tr>
							<tr>
								<td>备份</td>
								<td><%=quotaU.getQuotaSnapshot() %></td>
								<td><%=quotaT.getQuotaSnapshot() %></td>
							</tr>
							<tr>
								<td>映像</td>
								<td><%=quotaU.getQuotaImage() %></td>
								<td><%=quotaT.getQuotaImage() %></td>
							</tr>
							<tr>
								<td>硬盘</td>
								<td><%=quotaU.getQuotaDiskN() %></td>
								<td><%=quotaT.getQuotaDiskN() %></td>
							</tr>
							<tr>
								<td>SSH密钥</td>
								<td><%=quotaU.getQuotaSsh() %></td>
								<td><%=quotaT.getQuotaSsh() %></td>
							</tr>
							<tr>
								<td>防火墙</td>
								<td><%=quotaU.getQuotaFirewall() %></td>
								<td><%=quotaT.getQuotaFirewall() %></td>
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
								<td><%=quotaU.getQuotaRoute() %></td>
								<td><%=quotaT.getQuotaRoute() %></td>
							</tr>
							<tr>
								<td>私有网络</td>
								<td><%=quotaU.getQuotaVlan() %></td>
								<td><%=quotaT.getQuotaVlan() %></td>
							</tr>
							<tr>
								<td>负载均衡器</td>
								<td><%=quotaU.getQuotaLoadBalance() %></td>
								<td><%=quotaT.getQuotaLoadBalance() %></td>
							</tr>
							<tr>
								<td>硬盘容量(GB)</td>
								<td><%=quotaU.getQuotaDiskS() %></td>
								<td><%=quotaT.getQuotaDiskS() %></td>
							</tr>
							<tr>
								<td>带宽(Mbps)</td>
								<td><%=quotaU.getQuotaBandwidth() %></td>
								<td><%=quotaT.getQuotaBandwidth() %></td>
							</tr>
							<tr>
								<td>内存(GB)</td>
								<td><%=quotaU.getQuotaMemory() %></td>
								<td><%=quotaT.getQuotaMemory() %></td>
							</tr>
							<tr>
								<td>CPU(个)</td>
								<td><%=quotaU.getQuotaCpu() %></td>
								<td><%=quotaT.getQuotaCpu() %></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
