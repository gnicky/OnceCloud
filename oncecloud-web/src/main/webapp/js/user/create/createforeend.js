$(document).ready(function () {
    var maintype = $("#editForeend").attr('type');
    var lbuuid = $("#platformcontent").attr("lbuuid");

    if (maintype == "update") {
        document.getElementById('fe_protocol').disabled = true;
        $('#fe_protocol').css("background-color", "#eee");
        document.getElementById('fe_port').disabled = true;
        $('#fe_port').css("background-color", "#eee");
    }

    $("#ForeendAction").on('click', function (event) {
        event.preventDefault();
        var valid = $('#editForeend-form').valid();
        var type = $("#editForeend").attr('type');
        console.log(type);
        if (valid) {
            if (type == "new") {
                var name = $('#fe_name').val();
                var protocol = $('#fe_protocol').val();
                var port = parseInt($('#fe_port').val());
                var policy = parseInt($('#fe_policy').val());
                var showMessage = '<div class="alert alert-info" style="margin:10px">'
                    + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;监听端口&nbsp;'
                    + port + '&nbsp;已被使用';
                var result = checkFEPort(lbuuid, port);
                if (result == true) {
                    createListener(name, lbuuid, protocol, port, policy);
                    $('#LbModalContainer').modal('hide');
                } else {
                    bootbox.dialog({
                        className: "oc-bootbox",
                        message: showMessage,
                        title: "端口重复",
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
            } else {
                var name = $('#fe_name').val();
                var policy = $('#fe_policy').val();
                var foreuuid = $('#editForeend').attr('foreuuid');
                updateListener(name, policy, foreuuid);
                $('#LbModalContainer').modal('hide');
            }
        } else {
        }
    });

    function createListener(name, lbuuid, protocol, port, policy) {
        var basicPath = basePath;
        var feuuid = uuid.v4();
        var way = '轮询';
        if (1 == policy) {
            way = '最小响应时间';
        }
        var btngroup = '<div class="btn-group"><button class="btn fe_forbid btn-default"><span class="glyphicon glyphicon-ban-circle"></span>禁用</button>'
            + '<button id="fe_view" class="btn btn-default"><span class="glyphicon glyphicon-stats"></span>监控</button><button class="btn btn-default fe_update">'
            + '<span class="glyphicon glyphicon-pencil"></span>修改</button><button class="btn btn-danger fe_delete"><span class="glyphicon glyphicon-trash"></span>删除</button></div>';
        var settings = '<ul class="settings"><li><label>监听协议&nbsp;:&nbsp;' + protocol + '</label><label>端口&nbsp;:&nbsp;' + port + '</label></li>'
            + '<li><label>监听协议&nbsp;:&nbsp;' + way + '</label></li></ul>';
        var bebar = '<div class="once-toolbar"><button id="be_frash" class="btn btn-default rule-refresh">'
            + '<span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button><button class="btn btn-default be_create">'
            + '&nbsp;+&nbsp;添加后端</button><button class="btn btn-default btn-disable be_delete" order="0" name="be_delete" disabled><span class="glyphicon glyphicon-trash"></span>删除</button></div>';
        var forecontent = '<div class="foreend" feid="' + feuuid + '" fename="' + name + '" feprotocol="' + protocol + '" feport="'
            + port + '" fepolicy="' + policy + '" festate="1"><div class="fe_bar"><div class="foretitle">监听器:&nbsp;lbl-'
            + feuuid.substring(0, 8) + '&nbsp;(' + name + ')</div>' + btngroup + '</div>' + settings + bebar
            + '<table class="table table-bordered once-table"><thead><tr><th></th><th>名称</th><th>状态</th><th>主机/路由器</th><th>端口</th>'
            + '<th>权重</th><th>监控</th><th>操作</th></tr></thead><tbody class="backbody"></tbody></table></div>';
        $.ajax({
            type: 'post',
            url: '/LBAction/CreateFore',
            data: {name: name, foreuuid: foreuuid, lbuuid: lbuuid, protocol: protocol, port: port, policy: policy},
            dataType: 'text',
            success: function () {
                var hasListener = $('#fore_list').find('.unit');
                if (hasListener.size() != 0) {
                    $('#fore_list').html("");
                }
                $("#fore_list").prepend(forecontent);
                needApply();
            },
            error: function () {
            }
        });
    }

    function needApply() {
        $('#fe_apply').removeClass('btn-default').addClass('btn-primary');
        $('#suggestion').show();
    }

    function checkFEPort(lbuuid, port) {
        var result = false;
        $.ajax({
            type: 'post',
            async: false,
            url: '/LBAction/CheckFore',
            data: {port: port, lbuuid: lbuuid},
            dataType: 'text',
            success: function (response) {
                if (response == "true") {
                    result = true;
                }
            },
            error: function () {
            }
        });
        return result;
    }

    function updateListener(name, policy, foreuuid) {
        $.ajax({
            type: 'post',
            url: '/LBAction/UpdateFore',
            data: {name: name, policy: policy, foreuuid: foreuuid, lbuuid: lbuuid},
            dataType: 'text',
            complete: function () {
                window.location = window.location.href;
            }
        });
    }

    $("#editForeend-form").validate({
        rules: {
            fe_port: {
                required: true,
                digits: true,
                range: [1, 65535]
            },
            fe_name: {
                required: true,
                maxlength: 20,
                legal: true
            }
        },
        messages: {
            fe_port: {
                required: "<span class='unit'>端口不能为空</span>",
                digits: "<span class='unit'>端口必须是1~65535之间的整数</span>",
                range: "<span class='unit'>端口必须是1~65535之间的整数</span>"
            },
            fe_name: {
                required: "<span class='help'>名称不能为空</span>",
                maxlength: "<span class='help'>名称不能超过20个字符</span>",
                legal: "<span class='unit'>名称包含非法字符</span>"
            }
        }
    });
});