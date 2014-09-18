<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="${basePath}css/bwizard.css" />
<script src="${basePath}js/bwizard.js"></script>
<script src="${basePath}js/user/create/createinstance.js"></script>
<div class="modal-dialog" style="width: 723px">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">
				创建新主机<a class="close" data-dismiss="modal" aria-hidden="true"><span
					class="glyphicon glyphicon-remove"></span></a>
			</h4>
		</div>
		<div class="modal-body">
			<div class="row" style="margin: 0; background-color: #cfeaf8">
				<div class="col-md-12" style="padding: 0px">
					<div id="wizard" class="wizard">
						<ol>
							<li class="li-disable">基本信息</li>
							<li class="li-disable">安装文件</li>
							<li class="li-disable">CPU和内存</li>
							<li class="li-disable">硬盘设置</li>
							<li class="li-disable">摘要概览</li>
						</ol>
						<div>
							<div class="wizard-inner">
								<div class="item">
									<div class="control-label">主机名称</div>
									<div class="controls">
										<input type="text" id="instance_name" name="instance_name"
											value="">
									</div>
								</div>
								<div class="once-toolbar" style="margin: 0 0 10px 0">
									<div class="provider">选择资源池</div>
									<div class="toolbar-right">
										<table>
											<tr>
												<td>页数&nbsp;<a id="currentPtpl"></a>&nbsp;/&nbsp;<a
													id="totalPtpl"></a></td>
												<td style="padding-left: 10px">
													<div class="pagination-small">
														<ul class="pagination" id="tplpage"
															style="display: inline"></ul>
													</div>
												</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="poollist" id="poollist"></div>
							</div>
							<div class="wizard-action">
								<button class="btn btn-default btn-next" type="button">下一步</button>
							</div>
						</div>
						<div>
							<div class="wizard-inner">
								<div class="once-toolbar" style="margin: 0 0 10px 0">
									<div class="provider">选择镜像文件</div>
									<div class="toolbar-right">
										<table>
											<tr>
												<td>页数&nbsp;<a id="currentPtpl"></a>&nbsp;/&nbsp;<a
													id="totalPtpl"></a></td>
												<td style="padding-left: 10px">
													<div class="pagination-small">
														<ul class="pagination" id="tplpage"
															style="display: inline"></ul>
													</div>
												</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="iamgelist" id="iamgelist"></div>
							</div>
							<div class="wizard-action">
								<button class="btn btn-default btn-back" type="button">上一步</button>
								<button class="btn btn-default btn-next" type="button">下一步</button>
							</div>
						</div>
						<div>
							<div class="wizard-inner">
								<h6>CPU</h6>
								<div class="cpu options">
									<div class="types-options cpu-options selected" core="1">
										1核</div>
									<div class="types-options cpu-options " core="2">2核</div>
									<div class="types-options cpu-options " core="4">4核</div>
									<div class="types-options cpu-options " core="8">8核</div>
								</div>
								<h6>内存</h6>
								<div class="memory options">
									<div class="types-options memory-options" capacity="0.5">
										512MB</div>
									<div class="types-options memory-options selected" capacity="1">
										1G</div>
									<div class="types-options memory-options" capacity="2">
										2G</div>
									<div class="types-options memory-options" capacity="4">
										4G</div>
									<div class="types-options memory-options" capacity="6">
										6G</div>
									<div class="types-options memory-options" capacity="8">
										8G</div>
									<div class="types-options memory-options" capacity="12">
										12G</div>
									<div class="types-options memory-options disabled"
										capacity="16">16G</div>
									<div class="types-options memory-options disabled"
										capacity="24">24G</div>
									<div class="types-options memory-options disabled"
										capacity="32">32G</div>
								</div>
							</div>
							<div class="wizard-action">
								<button class="btn btn-default btn-back" type="button">上一步</button>
								<button class="btn btn-default btn-next" type="button">下一步</button>
							</div>
						</div>
						<div>
							<div class="wizard-inner">
								<div class="once-toolbar" style="margin: 0 0 10px 0">
									<div class="provider">选择硬盘</div>
									<div class="toolbar-right">
										<table>
											<tr>
												<td>页数&nbsp;<a id="currentPtpl"></a>&nbsp;/&nbsp;<a
													id="totalPtpl"></a></td>
												<td style="padding-left: 10px">
													<div class="pagination-small">
														<ul class="pagination" id="tplpage"
															style="display: inline"></ul>
													</div>
												</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="volumelist" id="volumelist"></div>
								<div class="item">
									<div class="control-label">容量</div>
									<div class="controls size">
										<div id="slider" style="width: 320px; display: inline-block"></div>
										<input id="size" type="text" class="mini" value="10"
											style="margin-left: 10px"> <span class="help inline">GB</span>
										<span class="help">10GB - 500GB</span>
									</div>
								</div>
							</div>
							<div class="wizard-action">
								<button class="btn btn-default btn-back" type="button">上一步</button>
								<button class="btn btn-default btn-next" type="button">下一步</button>
							</div>
						</div>
						<div>
							<form id="basicinfo-form">
								<div class="wizard-inner">
									<div class="item">
										<div class="control-label">主机名称</div>
										<div class="controls"></div>
									</div>
									<div class="item">
										<div class="control-label">安装源</div>
										<div class="controls"></div>
									</div>
									<div class="item keypair">
										<div class="control-label">资源池</div>
										<div class="controls"></div>
									</div>
									<div class="item passwd" style="">
										<div class="control-label">CPU</div>
										<div class="controls"></div>
									</div>
									<div class="item passwd" style="">
										<div class="control-label">内存</div>
										<div class="controls"></div>
									</div>
									<div class="item passwd" style="">
										<div class="control-label">硬盘</div>
										<div class="controls"></div>
									</div>
									<div class="item passwd" style="">
										<div class="control-label">存储大小</div>
										<div class="controls"></div>
									</div>
								</div>
								<div class="wizard-action">
									<button class="btn btn-default btn-back" type="button">上一步</button>
									<button id="createvmAction" class="btn btn-primary btn-create"
										type="button">创建</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
