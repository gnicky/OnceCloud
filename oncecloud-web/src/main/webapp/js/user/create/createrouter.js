$(document).ready(function () {
    loadFWList();
});

function loadFWList() {
    $.ajax({
        type: 'post',
        async: false,
        url: '/FirewallAction/AvailableFirewalls',
        dataType: 'json',
        success: function (response) {
        	if(response.length>0)
    		{
        		 var deitem = response[0];
                 $('#rt_firewall').html('<option value="' + deitem.firewallId + '">' + decodeURI(deitem.firewallName) + '</option>');
                 var detype = $('input:radio').eq(0).val();
                 setTypeInfo(detype);
                 for (var i = 1; i < response.length; i++) {
                     $('#rt_firewall').append('<option value="' + response[i].firewallId + '">fw-' + response[i].firewallId.substring(0, 8) + '&nbsp;(' + response[i].firewallName + ')</option>');
                 }
    		}
        	else
    		{
    		   $('#rt_firewall').html('<option>没有可选择的防火墙</option>');
               $('#createRouterAction').attr('disabled', true);
    		}
        },
        error: function () {
            $('#rt_firewall').html('<option>没有可选择的防火墙</option>');
            $('#createRouterAction').attr('disabled', true);
        }
    });
}


$('#createRouterAction').on('click', function (event) {
    event.preventDefault();
    var valid = $("#create-form").valid();
    if (valid) {
        var name = $('#rt_name').val();
        var capacity = $('input[name="rt_type"]:checked').val();
        var firewall = $('#rt_firewall').val();
        $.ajax({
            type: 'post',
            url: '/RouterAction/Quota',
            dataType: 'json',
            success: function (array) {
                var obj = array[0];
                if (obj.result == false) {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + obj.free + ']个路由器配额，您可以通过联系我们来申请扩大配额</div>',
                        title: "提示",
                        buttons: {
                            main: {
                                label: "确定",
                                className: "btn-primary",
                                calrtack: function () {
                                }
                            },
                            cancel: {
                                label: "取消",
                                className: "btn-default",
                                calrtack: function () {
                                }
                            }
                        }
                    });
                } else {
                    var rtuuid = uuid.v4();
                    preCreateRouter(rtuuid, name, capacity, firewall);
                }
                $('#RouterModalContainer').modal('hide');
            },
            error: function () {
            }
        });
    }
});

function preCreateRouter(rtuuid, name, capacity, firewall) {
    var showid = "rt-" + rtuuid.substring(0, 8);
    var capStr = "小型";
    if (capacity == 500) {
        capStr = "中型";
    } else if (capacity == 1000) {
        capStr = "大型";
    }
    var stateStr = '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
    var thistr = '<tr rowid="' + rtuuid + '" rtname="' + name + '"><td class="rcheck"><input type="checkbox" name="rtrow"></td>'
        + '<td><a class="id">' + showid + '</a></td><td>' + name + '</td>' + stateStr + '<td name="capacity">' + capStr + '</td><td name="sip"></td><td name="eip"></td>'
        + '<td class="time"><1分钟</td></tr>';
    $("#tablebody").prepend(thistr);
    createRouter(rtuuid, name, capacity, firewall);
}

function createRouter(rtuuid, name, capacity, firewall) {
    $.ajax({
        type: 'post',
        url: '/RouterAction/Create',
        data: {uuid:rtuuid,name:name,capacity:capacity,fwUuid:firewall},
        dataType: 'json',
    });
}

$('input:radio').change(function () {
    var rttype = $('input:radio:checked').val();
    setTypeInfo(rttype);
});
function setTypeInfo(rttype) {
    if (250 == rttype) {
        $('#price-hour').text('0.02');
        $('#price-total').text('0.02');
        $('#price-month').text('14.40');
        $('#typeinfo').text('推荐用于1个私有网络，峰值带宽250Mbps');
    } else if (500 == rttype) {
        $('#price-hour').text('0.04');
        $('#price-total').text('0.04');
        $('#price-month').text('28.80');
        $('#typeinfo').text('推荐用于2个私有网络，峰值带宽500Mbps');
    } else if (1000 == rttype) {
        $('#price-hour').text('0.08');
        $('#price-total').text('0.08');
        $('#price-month').text('57.60');
        $('#typeinfo').text('推荐用于3~4个私有网络，峰值带宽1000Mbps');
    }
}

$("#create-form").validate({
    rules: {
        rt_name: {
            required: true,
            minlength: 3,
            maxlength: 80
        },
        rt_type: {
            required: true,
        }
    },
    messages: {
        rt_name: {
            required: "<span class='help'>名称不能为空</span>",
            minlength: "<span class='help'>名称不能少于3个字符</span>",
            maxlength: "<span class='help'>名称不能多于80个字符</span>"
        },
        rt_type: {
            required: "<span class='help'>请选择一种类型</span>",
        }
    }
});