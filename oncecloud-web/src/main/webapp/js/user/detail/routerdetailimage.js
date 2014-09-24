///add by cyh 
$(function(){
	init();
})


function init() {
    $('.view-types').on('click', '.view-type', function (event) {
        event.preventDefault();
        $('a', $('.view-types')).removeClass('current');
        $(this).addClass('current');
        var type = $(this).attr("oc-type");
        if (type == 'text') {
            $("#textview").show();
            $("#imageview").hide();
        } else {
            $("#textview").hide();
            $("#imageview").show();
        }
    });

   
    $(".graph-actions a").mouseenter(function () {
        if (!$(this).hasClass("btn-forbidden")) {
            $(this).find(".text").show();
        }
    }).mouseleave(function () {
        $(this).find(".text").hide();
    });

    $(".components").on("mouseenter",".component-router-sg",function(){
    	 $(this).find("#changefirewall").show();
    });
    
    $(".components").on("mouseleave",".component-router-sg",function(){
    	 $(this).find("#changefirewall").hide();
    });
    
    $("#changefirewall").click(function(){
    	bingfirewall("rt");
    });

    $('#startup').on('click', function (event) {
        event.preventDefault();
        showbox(0);
    });
    $('#destroy').on('click', function (event) {
        event.preventDefault();
        showbox(1);
    });
    $('#shutdown').on('click', function (event) {
        event.preventDefault();
        showbox(2);
    });

    $("#componentsId").on("click", "#routernoip", function () {
        bindpublicip("rt");
    });

    $("#componentsId").on("click", "#deleteIp", function () {
        unbingpublicip("rt");
    });
    
    $(".components").on("mouseenter","#addvnet",function(){
    	 $(this).find(".text").show();
    }).on("mouseleave","#addvnet",function(){
    	 $(this).find(".text").hide();
    });
    
    $(".components").on("mouseenter","#addinstance",function(){
   	 $(this).find(".text").show();
   }).on("mouseleave","#addinstance",function(){
   	 $(this).find(".text").hide();
   });
    
    $(".components").on("click", "#addvnet", function () {
        addvnet();
    });
    
    $(".components").on("click", ".btn-delete-vxnet", function () {
    	unlink($(this).attr("vnetid"));
    });
    
    $(".components").on("click", ".btn-delete-instance", function () {
    	unlinkinstance($(this).attr("intanceuuid"));
    });
}

function addvnet()
{
    $('#RouterModalContainer').load($("#platformcontent").attr('basepath')+'vnet/bindtorouter', '', function () {
        $('#RouterModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
}


function unlink(uuid) {
	
	    var infoList = $("#platformcontent").attr("showid");
	    var showMessage = '';
	    var showTitle = '';
	   
        showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">离开路由器后,该网络内将无法访问外部网络.是否继续</div>';
        showTitle = "离开路由器" + '&nbsp;' + infoList + '?';
	    
	    bootbox.dialog({
	        className: "oc-bootbox",
	        message: showMessage,
	        title: showTitle,
	        buttons: {
	            main: {
	                label: "确定",
	                className: "btn-primary",
	                callback: function () {
	                	 $.ajax({
	                            type: 'post',
	                            url: '/VnetAction/Unlink',
	                            data: {vnetId: uuid},
	                            dataType: 'text',
	                            complete: function () {
	                            	getVxnets();
	                            }
	                        });
	                }
	            },
	            cancel: {
	                label: "取消",
	                className: "btn-default",
	                callback: function () {
	                }
	            }
	        }
	    });
   
}

function unlinkinstance(uuid) {
	
    var infoList = $("#platformcontent").attr("showid");
    var showMessage = '';
    var showTitle = '';
   
    showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">离开路由器后,该网络内将无法访问外部网络.是否继续</div>';
    showTitle = "离开路由器" + '&nbsp;' + infoList + '?';
    
    bootbox.dialog({
        className: "oc-bootbox",
        message: showMessage,
        title: showTitle,
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                	 $.ajax({
                            type: 'post',
                            url: '/VnetAction/Unlink',
                            data: {vnetId: uuid},
                            dataType: 'text',
                            complete: function () {
                            	getVxnets();
                            }
                        });
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                }
            }
        }
    });

}


function showbox(type) {
    var infoList =  $("#platformcontent").attr("showid");
    var infoArray = new Array("启动路由器", "销毁路由器", "关闭路由器");
    var showMessage = '';
    var showTitle = '';
    if (type == 2) {
        showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">1. 强制关机会丢失内存中的数据<br/>'
            + '2. 为保证数据的完整性，请在强制关机前暂停所有文件的写操作，或进行正常关机。</div>'
            + '<div class="item" style="margin:0"><div class="controls" style="margin-left:100px">'
            + '<label class="inline"><input type="checkbox" id="force">&nbsp;强制关机</label></div></div>';
        showTitle = infoArray[type] + '&nbsp;' + infoList + '?';
    } else {
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
            + infoArray[type] + '&nbsp;' + infoList + '?</div>';
        showTitle = '提示';
    }
    bootbox.dialog({
        className: "oc-bootbox",
        message: showMessage,
        title: showTitle,
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    var uuid = $("#platformcontent").attr("routerUuid");
                    if (type == 0) {
                        startRouter(uuid);
                    } else if (type == 1) {
                        destroyRouter(uuid);
                    } else if (type == 2) {
                        var force = $('#force')[0].checked;
                        shutdownRouter(uuid, force);
                    }
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
              
            }
        }
    });
}

///绑定公网ip
function bindpublicip(bdtype) {
    var infoList = $("#platformcontent").attr("showid");
    var tablelist = "";
    $.ajax({
        type: 'get',
        url: '/EIPAction/AvailableEIPs',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" eid="' + item.eipUuid + '" eip="' + item.eipIp + '"><div class="image-left">' + decodeURIComponent(item.eipName) + '</div>IP:&nbsp;&nbsp;' + item.eipIp + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" eid="' + item.eipUuid + '" eip="' + item.eipIp + '"><div class="image-left">' + decodeURIComponent(item.eipName) + '</div>IP:&nbsp;&nbsp;' + item.eipIp + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="alert alert-warning" style="margin:10px 30px">默认情况下，除了少数安全端口之外，主机的大部分端口都是关闭的，您需要在防火墙中打开相应的下行规则以允许外网访问。</div>'
                + '<div class="epubliciplist" id="epubliciplist" style="margin:20px 30px">' + tablelist + '</div>';

            var showTitle = '选择路由器&nbsp;' + infoList + '&nbsp;要绑定的公网IP';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var uuid = $("#epubliciplist").find(".selected").attr("eid");
                            var eipIp = $("#epubliciplist").find(".selected").attr("eip");
                            var router = $("#platformcontent").attr("routerUuid");
                          if (bdtype == "rt") {
                                $.ajax({
                                    type: 'post',
                                    url: '/EIPAction/Bind',
                                    data: {vmUuid: router, eipIp: eipIp, bindType: bdtype},
                                    dataType: 'json',
                                    success: function (obj) {
                                        if (obj.result == true) {
                                            $("#routerip .component-id").text(eipIp);
                                            $("#routernoip").hide();
                                            $("#routerip").show();
                                        } else {

                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            }

                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(500);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        },
        error: function () {

        }
    });


}

function unbingpublicip(bdtype) {
    var vmuuid = "";
    var eipIp = "";
    if (bdtype = "rt") {
        vmuuid = $("#platformcontent").attr("routerUuid");
        eipIp = $("#routerip .component-id").text();
    }
    $.ajax({
        type: 'post',
        url: '/EIPAction/UnBind',
        data: {eipIp: eipIp, vmUuid: vmuuid, bindType: bdtype},
        dataType: 'json',
        success: function (obj) {
            if (bdtype = "rt") {
                $("#routerip .component-id").text("");
                $("#routernoip").show();
                $("#routerip").hide();
            }
        },
        error: function () {
        }
    });
}

function bingfirewall(bdtype) {
	var infoList = $("#platformcontent").attr("showid");
    var tablelist = "";
    $.ajax({
        type: 'post',
        url: '/FirewallAction/AvailableFirewalls',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" firewallId="' + item.firewallId + '" firewallName="' + decodeURIComponent(item.firewallName) + '"><div class="image-left">' + "fw-" + item.firewallId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURIComponent(item.firewallName) + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" firewallId="' + item.firewallId + '" firewallName="' + decodeURIComponent(item.firewallName) + '"><div class="image-left">' + "fw-" + item.firewallId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURIComponent(item.firewallName) + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="epubliciplist" id="epubliciplist" style="margin:20px 30px">' + tablelist + '</div>';

            var showTitle = '选择路由器&nbsp;' + infoList + '&nbsp;要绑定的防火墙';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var firewallId = $("#epubliciplist").find(".selected").attr("firewallId");
                            var firewallName = $("#epubliciplist").find(".selected").attr("firewallName");
                            var router = "[" + $("#platformcontent").attr("routerUuid") + "]";
                            if (bdtype == "rt") {
                                $.ajax({
                                    type: 'post',
                                    url: '/FirewallAction/Bind',
                                    data: {firewallId: firewallId, vmUuidStr: router, bindType: bdtype},
                                    dataType: 'json',
                                    success: function (response) {
                                        if (response.isSuccess) {
                                            $("#firewalldiv").find(".sg-name").text(firewallName);
                                        }
                                        else {
                                          
                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            }

                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(500);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        },
        error: function () {

        }
    });
}





function startRouter(uuid) {
    $.ajax({
        type: 'post',
        url: '/RouterAction/StartUp',
        data: {uuid: uuid},
        dataType: 'json',
        complete:function(){
        	getRouterBasicList();
        }
    });
}

function destroyRouter(uuid) {
    $.ajax({
        type: 'post',
        url: '/RouterAction/Destroy',
        data: {uuid: uuid},
        dataType: 'text',
        success: function (obj) {
            if (obj == "no") {
                bootbox.dialog({
                    message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;无法删除，依然有私有网络依赖于该路由器</div>',
                    title: "提示",
                    buttons: {
                        main: {
                            label: "确定",
                            className: "btn-primary",
                            callback: function () {
                            }
                        }
                    }
                });
            }else
            	{
            	  window.location.href = '/router';
            	}
            
        }
    });
}

function shutdownRouter(uuid, force) {
    $.ajax({
        type: 'post',
        url: '/RouterAction/ShutDown',
        data: {uuid: uuid, force: force},
        dataType: 'text',
        complete:function(){
        	getRouterBasicList();
        }
    });
}