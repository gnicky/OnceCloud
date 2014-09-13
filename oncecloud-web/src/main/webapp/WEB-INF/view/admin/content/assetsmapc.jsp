<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content views-map" id="platformcontent">
	<div class="intro">
		<h1>用户展示地图&nbsp;CompanyMap <button class="btn small"> 缩小 </button> </h1>
		<p class="lead" style="margin-top:10px">
			<em>用户展示地图&nbsp;(CompanyMap)</em>
		</p>
		<button class="btn big"> 最大化 </button>
	</div>
	<div id="companyMap">
		<iframe id="map-iframe" src="${basePath}CompanyMap"></iframe>
	</div>
</div>