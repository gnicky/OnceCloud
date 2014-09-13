
window.api = window.api || {};
window.apiClass = window.apiClass || {};
window.baseMethod = window.baseMethod || {};
window.views = window.views || {};

apiClass.Event = function() {
	this.data = [];
	this.attach = function(func) {
		if (typeof func == 'function') {
			this.data.push(func);
		}
	};
	this.notify = function(data) {
		var x = 0;
		for (; x < this.data.length; x++) {
			var func = this.data[x];
			if (typeof func == "function") {
				func(data);
			}
		}
	};
};
baseMethod.getEvents = function(){
	var re = {
		success : new apiClass.Event(),
		error : new apiClass.Event(),
		complete : new apiClass.Event()
	};
	return {
		events : re
	};
};
baseMethod.excute = function(url, method, data){
	var future = baseMethod.getEvents();
	future.xhr = $.ajax({
		url : url,
		type : method,
		dataType : "json",
		data : data,
		complete : function() {
			future.events.complete.notify();
		},
		success : function(data) {
			future.events.success.notify(data);
		},
		error : function() {
			future.events.error.notify();
		}
	});
	return future;
};

api.assets = {
	_excut:function(url,method,data){
		return baseMethod.excute("/AssetsAction"+url, method, data);
	},
	findList:function(){
		var url = "/AssetsList";
		var method = "get";
		var data = {};
		return this._excut(url, method, data);	
	},
	findQutaPage:function(data){
		var url = "/QuotaList";
		var method = "get";
		return this._excut(url, method, data);	
	},
	findMonthPage:function(data){
		var url = "/AssetsMonthList";
		var method = "get";
		return this._excut(url, method, data);
	}
};


views.Assets = function(options){
	
	this.ele = $('');
	
	this.init(options);
};
views.Assets.prototype = {
	init:function(options){
		for(var x in options){
			this[x] = options[x];
		}
	},
	load:function(){
		var _this = this;
		var fu = api.assets.findList();
		fu.events.success.attach(function(datas){
			_this.creatItems(datas);
		});
	},
	creatItems:function(datas){
		
		var $con = this.ele;
		$con.empty();
		
		var x =0;
		for(;x<datas.length;x++){
			var data = datas[x];
			$con.append(this.creatItem(data));
		}
		
	},
	creatItem:function(data){
		var h ='\
			<div class="pool_floor">\
            	<div class="pool_pic">\
        			<img src="'+basePath+'img/admin/assets/'+data.assersIcon+'.png" />\
        		</div>\
        		<div class="pool_inf">\
        			<p class="no">'+data.assetsName+' <em>'+data.assetsNum+'</em></p>\
        			<p class="price">折后单月价格 <em> ￥'+data.assetsPermonth+'/月</em></p>\
    			</div>\
			</div>';
		return h;
	}
};

views.AssetsQuta = function(options){
	
};

views.AssetsCost = function(options){
	
};

$(document).ready(function(){
	
	$(".views-assets").each(function(){
		var assets = new views.Assets({ele:$(this)});
		assets.load();
	});
	
	$("views-assetsQuta").each(function(){
		var assets = new views.AssetsQuta({ele:$(this)});
		assets.load();
	});
	
	$("views-assetsCost").each(function(){
		var assets = new views.AssetsCost({ele:$(this)});
		assets.load();
	});
});
