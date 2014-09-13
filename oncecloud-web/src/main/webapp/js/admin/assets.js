
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
		var temp = $.extend({
			page:1,
			limit:10,
			search:""
		},data);
		return this._excut(url, method, temp);	
	},
	findCostPage:function(data){
		var url = "/AssetsMonthList";
		var method = "get";
		var temp = $.extend({
			page:1,
			limit:10,
			search:""
		},data);
		return this._excut(url, method, temp);	
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
        			<p class="no">'+data.assetsName+' <em>'+data.assetsNum+' '+data.assersUnit+'</em></p>\
        			<p class="price">'+data.assersShow +'<em> ￥'+data.assetsPermonth+'/月</em></p>\
    			</div>\
			</div>';
		return h;
	}
};

views.AssetsQuta = function(options){
	
	this.ele = $('');
	
	this.pageNumber = 1;
	this.pageSize = 100;
	this.totalPages = 0;
	this.searchValue = "";
	
	this.class_content = "#tablebody";
	this.class_title = "#tablethead";
	this.class_pageValue = ".page";
	this.class_prev = ".previous";
	this.class_next = ".next";
	
	this.init(options);
	this.bindEvents();
};
views.AssetsQuta.prototype = {
	init:function(options){
		for(var x in options){
			this[x] = options[x];
		}
		
		var $title = this.ele.find(this.class_title);
		$title.html(this.creatTitle());
	},
	bindEvents:function(){
		
	},
	load:function(){
		var _this = this;
		var fu = api.assets.findQutaPage({
			page:this.pageNumber,
			limit:this.pageSize,
			search:this.searchValue
		})
		fu.events.success.attach(function(datas){
			_this.creatItems(datas);
		});
	},
	creatTitle:function(){
		var h = '\
		<tr>\
			<th width="15%">用户名</th>\
			<th width="17%">CPU</th>\
			<th width="17%">内存</th>\
			<th width="17%">网络</th>\
			<th width="17%">磁盘</th>\
			<th width="17%">总计</th>\
		</tr>';
		return h;
	},
	creatItems:function(datas){
		
		var $con = this.ele.find(this.class_content);
		$con.empty();
		
		var x =0;
		for(;x<datas.length;x++){
			var data = datas[x];
			$con.append(this.creatItem(data));
		}
		
	},
	creatItem:function(data){
		
		var user = data.user || {};
		var quota = data.quota || {};
		
		var h = '\
			<tr>\
				<td>'+user.userName+'</td>\
				<td class="price">￥'+data.cpuMoney+'</td>\
				<td class="price">￥'+data.memMoney+'</td>\
				<td class="price">￥'+data.netMoney+'</td>\
				<td class="price">￥'+data.diskMoney+'</td>\
				<td class="price">￥'+data.allMoney+'</td>\
			</tr>';
		return h;
	}
};
views.AssetsCost = function(){
	
};
views.AssetsCost.prototype = {
	init:function(options){
		for(var x in options){
			this[x] = options[x];
		}
	},
	load:function(){
		
	},
	creatItems:function(datas){
		
	},
	creatItem:function(data){
		
	}
};

$(document).ready(function(){
	
	$(".views-assets").each(function(){
		var view = new views.Assets({ele:$(this)});
		view.load();
	});
	
	$(".views-assetsQuota").each(function(){
		var view = new views.AssetsQuta({ele:$(this)});
		view.load();
	});
	
	$(".views-assetsCost").each(function(){
		var view = new views.AssetsCost({ele:$(this)});
		view.load();
	});
});
