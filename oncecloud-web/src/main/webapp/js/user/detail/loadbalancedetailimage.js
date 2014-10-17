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

    $(".components").on("click", "#bandingIp", function () {
        bindpublicip("lb");
    });

    $(".components").on("click", "#unbandingIp", function () {
        unbingpublicip("lb");
    });
    
    
    $(".components").on("click", "#btn_add_listener", function () {
    	fe_create();
    });
    
    $(".components").on("click", ".btn-delete-listener", function () {
    	 var feuuid = $(this).data('id');
    	    showbox(0, feuuid);
    });
    
    $(".components").on("click", ".btn-add-backends", function () {
    	be_create(this);
    });
    
    $(".components").on("click", ".btn-delete-backend", function () {
    	var feuuid = $(this).data('id');
	    showbox(2, feuuid);
    });
    
    
    
}

function be_create(obj)
{
	 var uuid = $(obj).parents(".component-LB-listener").data('id');
	    var url = basePath + 'backend/create';
	    $('#LbModalContainer').load(url, {"foreuuid": uuid}, function () {
	        $('#LbModalContainer').modal({
	            backdrop: false,
	            show: true
	        });
	    });
}

function fe_create()
{
	var lbUuid = $('#platformcontent').attr("lbUuid");
    var url = basePath + 'foreend/create';
    $('#LbModalContainer').load(url, { "name": "", "protocol": "", "port": "", "policy": "", "foreuuid": "", "type": "new", "lbuuid": lbUuid}, function () {
        $('#LbModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
}

function addinstance(vnetid)
{
    $('#RouterModalContainer').load($("#platformcontent").attr('basepath')+'vnet/bindvmimage',  {vnetid: vnetid}, function () {
        $('#RouterModalContainer').modal({
            backdrop: false,
            show: true
        });
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

            var showTitle = '选择负载均衡&nbsp;' + infoList + '&nbsp;要绑定的公网IP';

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
                            var router = $("#platformcontent").attr("lbUuid");
                          if (bdtype == "lb") {
                                $.ajax({
                                    type: 'post',
                                    url: '/EIPAction/Bind',
                                    data: {vmUuid: router, eipIp: eipIp, bindType: bdtype},
                                    dataType: 'json',
                                    success: function (obj) {
                                        if (obj.result == true) {
                                        	getLbBasicList();
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
    if (bdtype = "lb") {
        vmuuid = $("#platformcontent").attr("lbUuid");
        eipIp = $("#unbandingIp .component-id").text();
    }
    $.ajax({
        type: 'post',
        url: '/EIPAction/UnBind',
        data: {eipIp: eipIp, vmUuid: vmuuid, bindType: bdtype},
        dataType: 'json',
        success: function (obj) {
            if (bdtype = "lb") {
            	getLbBasicList();
            }
        },
        error: function () {
        }
    });
}

