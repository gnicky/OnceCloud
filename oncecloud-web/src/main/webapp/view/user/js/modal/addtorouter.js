$(document).ready(function () {
    loadRouterList();

    function loadRouterList() {
        var vnetid = '';
        $('input[name="vnrow"]:checked').each(function () {
            vnetid = $(this).parent().parent().attr("rowid");
        });
        $('#modalcontent').attr('vnetid', vnetid);
        $.ajax({
            type: 'get',
            async: false,
            url: '/VnetAction',
            data: 'action=getablerts',
            dataType: 'json',
            success: function (array) {
                var vmlistHtml = '';
                if (0 == array.length) {
                    vmlistHtml = '<option>没有路由器可连接</option>';
                    $('#vn_router').attr('disabled', true).addClass('oc-disable');
                    $('#vn_router').html(vmlistHtml);
                    $('#linkRouter').attr('disabled', true);
                    $('#linkRouter').addClass('btn-forbidden');
                } else {
                    $('#vn_router').attr('disabled', false);
                    $('#linkRouter').attr('disabled', false).removeClass('oc-disable');
                    $('#linkRouter').removeClass('btn-forbidden');
                    for (var i = 0; i < array.length; i++) {
                        vmlistHtml = vmlistHtml + '<option value="' + array[i].uuid + '">rt-'
                            + array[i].uuid.substring(0, 8) + '&nbsp;(' + decodeURI(array[i].rtname) + ')</option>';
                    }
                    $('#vn_router').html(vmlistHtml);
                }
            },
            error: function () {
            }
        });
    }

    $('#linkRouter').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        if (valid) {
            var vnetid = $('#modalcontent').attr('vnetid');
            var vn_router = $("#vn_router").val();
            var vn_net = $("#vn_net").val();
            var vn_gate = $("#vn_gate").val();
            var dhcp_status = $('input[name="dhcp_status"]:checked').val();
            var vn_start = $("#vn_start").val();
            var vn_end = $("#vn_end").val();
            if (checkRepeat(vn_router, vn_net)) {
                $.ajax({
                    type: 'post',
                    url: '/VnetAction',
                    data: 'action=linkrouter&vnetid=' + vnetid + '&routerid=' + vn_router
                        + '&net=' + vn_net + '&gate=' + vn_gate + '&start=' + vn_start + '&end=' + vn_end
                        + '&dhcpState=' + dhcp_status,
                    dataType: 'json',
                    success: function (obj) {
                        if (obj.result) {
                            var routerhtml = '<a><span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;rt-' + vn_router.substring(0, 8) + '</a>';
                            var thistr = $("#tablebody").find('[rowid="' + vnetid + '"]');
                            thistr.children('td').eq(3).html(routerhtml);
                        }
                    },
                    error: function () {
                    }
                });
                $('#VnetModalContainer').modal('hide');
                $('input[name="vnrow"]:checked').each(function () {
                    $(this)[0].checked = false;
                    $(this).change();
                });
            } else {
                bootbox.dialog({
                    className: "oc-bootbox",
                    message: '<div class="alert alert-info" style="margin:10px">'
                        + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;网络地址段重复</div>',
                    title: '重复提示',
                    buttons: {
                        main: {
                            label: "确定",
                            className: "btn-primary",
                            callback: function () {
                            }
                        }
                    }
                });
            }
        }
    });

    function checkRepeat(routerid, net) {
        var result = false;
        $.ajax({
            type: 'post',
            async: false,
            url: '/VnetAction',
            data: 'action=checknet&routerid=' + routerid + '&net=' + net,
            dataType: 'json',
            success: function (response) {
                result = response.isSuccess;
            },
            error: function () {

            }
        });
        return result;
    }

    $('input[name="dhcp_status"]').on('change', function (event) {
        event.preventDefault();
        var value = $(this).attr("value");
        if (value == "0") {
            $('#ip-range').hide();
        } else {
            $('#ip-range').show();
        }
    });

    $("#vn_net").blur(function (e) {
        var net = $(this).val();
        $('#vn_net1').val(net);
        $('#vn_net2').val(net);

        if (!checkaddr(net, 1, 254)) {
            $("#vn_net").parent().find('.oc-error').remove();
            $("#vn_net").parent().append('<label class="oc-error oc-error1"><span class="help">地址不合法</span></label>');
        } else {
            $("#vn_net").parent().find('.oc-error').remove();
        }
    });

    $("#vn_gate").blur(function (e) {
        if (!checkaddr($(this).val(), 0, 254)) {
            $("#vn_gate").parent().find('.oc-error').remove();
            $("#vn_gate").parent().append('<label class="oc-error oc-error2"><span class="help">地址不合法</span></label>');
        } else {
            $("#vn_gate").parent().find('.oc-error').remove();
        }
    });

    $("#vn_start").blur(function (e) {
        $("#vn_start").parent().find('.oc-error').remove();
        var start = $(this).val();
        var end = $('#vn_end').val();
        if (!checkaddr(start, 0, 254)) {
            $("#vn_start").parent().find('.oc-error').remove();
            $("#vn_start").parent().append('<label class="oc-error oc-error3"><span class="help">地址不合法</span></label>');
        } else {
            $("#vn_start").parent().find('.oc-error').remove();
        }
        if (!checkaddr(end, start, 254)) {
            $('#vn_end').val(254);
        }
    });

    $("#vn_end").blur(function (e) {
        var start = $('#vn_start').val();
        var end = $(this).val();
        if (!checkaddr(end, start, 254)) {
            $('#vn_end').val(254);
        }
    });


    function checkaddr(addr, num1, num2) {
        if (addr == "") {
            return false;
        }
        if ($.isNumeric(addr)) {
            if (addr >= num1 && addr <= num2) {
                return true;
            }
        }
        return false;
    }

    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPS");
        var t = document.getElementById("totalPS");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    $("#create-form").validate({
        rules: {
            vn_net: {
                required: true
            },
            vn_gate: {
                required: true
            },
            vn_start: {
                required: true
            },
            vn_end: {
                required: true
            }
        },
        messages: {
            vn_net: {
                required: "<span class='help'>地址不能为空</span>"
            },
            vn_gate: {
                required: "<span class='help'>地址不能为空</span>"
            },
            vn_start: {
                required: "<span class='help'>地址不能为空</span>"
            },
            vn_end: {
                required: "<span class='help'>地址不能为空</span>"
            }
        }
    });
});