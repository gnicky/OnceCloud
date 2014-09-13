
window.views = window.views || {};

views.Map = function(options){
	
	this.ele = $('');
	this.url = "";
	
	this.class_full = ".map-full";
	this.class_cancel = ".map-cancel";
	this.class_new = ".map-new";
	
	this.init(options);
	this.bindEvents();
};
views.Map.prototype = {
	init:function(o){
		for(var x in o){
			this[x] = o[x];
		}
	},
	bindEvents:function(){
		
		var _this = this;
		var $ele = this.ele;
		
		$ele.find(this.class_full).click(function(){
			_this.full();
		});
		
		$ele.find(this.class_cancel).click(function(){
			_this.cancelFull();
		});
		
		$ele.find(this.class_new).click(function(){
			_this.openNew();
		});
		
	},
	full:function(){
		this.ele.addClass("full");
	},
	cancelFull:function(){
		this.ele.removeClass("full");
	},
	openNew:function(){
		window.open(this.url);
	}
};

$(document).ready(function(){
	
	$(".views-map").each(function(){
		var view = new views.Map({
			ele:$(this),
			class_full : ".big",
			class_cancel : ".small"
		});
	});
});

