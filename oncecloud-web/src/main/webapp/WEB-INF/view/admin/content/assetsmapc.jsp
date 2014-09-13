<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>用户展示地图&nbsp;CompanyMap</h1>
		<p class="lead" style="margin-top:10px">
			<em>用户展示地图&nbsp;(CompanyMap)</em>
		</p>
	</div>
 	<div class="once-pane">
 		<button id="full" class="btn"> 最大化 </button>
		<button id="new" class="btn"> 新窗口打开 </button>
 	</div>
	<div id="companyMap">
		<iframe id="map-iframe" src="${basePath}CompanyMap"></iframe>
	</div>
</div>