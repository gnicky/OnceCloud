$(document).ready(function () {
    getLbBasicList();
    getForeList();

    $('#LbModalContainer').on('hide', function (event) {
        $(this).removeData("modal");
        $(this).children().remove();
        getLbBasicList();
    });

    $('#modify').on('click', function (event) {
        event.preventDefault();
        var url = $('#platformcontent').attr('platformBasePath') + 'common/modify.jsp';
        var lbName = $('#lbname').text();
        var lbDesc = $('#lbdesc').text();
        var lbUuid = $('#platformcontent').attr("lbUuid");
        $('#LbModalContainer').load(url, {"modifytype": "lb", "modifyuuid": lbUuid,
            "modifyname": lbName, "modifydesc": lbDesc}, function () {
            $('#LbModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        getLbBasicList();
    });

    $('#depend-list').on('click', '#firewallid', function (event) {
        event.preventDefault();
        var uuid = $(this).attr('uuid');
        var basePath = $("#platformcontent").attr("platformBasePath");
        $.ajax({
            type: 'get',
            url: '/FirewallAction',
            data: 'action=detail&firewallId=' + uuid,
            dataType: 'text',
            success: function (response) {
                window.location.href = basePath + "user/detail/firewalldetail.jsp";
            },
            error: function () {
            }
        });
    });

    function getLbBasicList() {
        $('#basic-list').html("");
        var lbUuid = $('#platformcontent').attr("lbUuid");
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: "action=getonelb&lbUuid=" + lbUuid,
            dataType: 'text',
            success: function (response) {
                var obj = jQuery.parseJSON(response);
                var lbName = decodeURI(obj.lbName);
                var lbDesc = decodeURI(obj.lbDesc);
                var lbIp = obj.lbIp;
                var lbMac = obj.lbMac;
                var lbStatus = obj.lbStatus;
                if (lbStatus == 2) {
                    $('#fe_apply').removeClass('btn-default').addClass('btn-primary');
                    $('#suggestion').show();
                }
                var lbUID = obj.lbUID;
                var lbCapacity = obj.lbCapacity;
                var lbPower = obj.lbPower;
                var lbFirewall = obj.lbFirewall;
                var createDate = obj.createDate;
                var useDate = decodeURI(obj.useDate);
                var stateStr = '';
                var showstr = '';
                var showuuid = "lb-" + lbUuid.substring(0, 8);
                if (lbPower == 0) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-stopped" name="stateicon"></span><span name="stateword">已关机</span></td>';
                } else if (lbPower == 1) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-running" name="stateicon"></span><span name="stateword">活跃</span></td>';
                } else if (lbPower == 2) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
                } else if (lbPower == 3) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">销毁中</span></td>';
                } else if (lbPower == 4) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">启动中</span></td>';
                } else if (lbPower == 5) {
                    stateStr = '<td><span id="lb-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">关机中</span></td>';
                }
                showstr = "<a class='id'>" + showuuid + '</a>';
                if ("&nbsp;" != lbFirewall) {
                    lbFirewall = '<a id="firewallid" uuid="' + lbFirewall + '">fw-' + lbFirewall.substring(0, 8) + '</a>';
                }
                var network;
                if (lbIp == "null") {
                    network = '<a class="id">(基础网络)</a>';
                } else {
                    network = '<a class="id">(基础网络)&nbsp;/&nbsp;' + lbIp + '</a>';
                }
                var lbEip = obj.eip;
                $('#basic-list').html('<dt>ID</dt><dd>'
                    + showstr + '</dd><dt>名称</dt><dd id="lbname">'
                    + lbName + '</dd><dt>描述</dt><dd id="lbdesc">'
                    + lbDesc + '</dd><dt>状态</dt><dd>'
                    + stateStr + '</dd><dt>最大连接数</dt><dd>'
                    + lbCapacity + '</dd><dt>Mac地址</dt><dd>'
                    + lbMac + '</dd><dt>创建时间</dt><dd class="time">'
                    + createDate + '</dd><dt>运行时间</dt><dd class="time">'
                    + useDate + '</dd>');
                $('#depend-list').html('<dt>网络</dt><dd>'
                    + network + '</dd><dt>公网IP</dt><dd>'
                    + lbEip + '</dd><dt>防火墙</dt><dd>'
                    + lbFirewall + '</dd>');
            },
            error: function () {
            }
        });
    }

    function getForeList() {
        $('#fore_list').html("");
        var lbUuid = $('#platformcontent').attr("lbUuid");
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: 'action=getforelist&lbuuid=' + lbUuid,
            dataType: 'json',
            success: function (array) {
                if (array.length == 0) {
                    $('#fore_list').html('<span class="unit">没有监听器</span>');
                } else {
                    var content = "";
                    for (var i = 0; i < array.length; i++) {
                        var feobj = array[i];
                        var foreUuid = feobj.foreUuid;
                        var foreName = decodeURI(feobj.foreName);
                        var foreProtocol = feobj.foreProtocol;
                        var forePort = feobj.forePort;
                        var forePolicy = feobj.forePolicy;
                        var foreStatus = feobj.foreStatus;
                        var foreClass = 'class="foreend"';
                        var disableBtn = '<span class="glyphicon glyphicon-ban-circle"></span>禁用';
                        if (foreStatus == 0) {
                            foreClass = 'class="foreend lb-disable"';
                            disableBtn = '<span class="glyphicon glyphicon-play"></span>启动';
                        }
                        var beArray = feobj.beArray;
                        var policy = "轮询";
                        if (forePolicy == 1) {
                            policy = "最小响应时间";
                        }
                        var btngroup = '<div class="btn-group"><button class="btn fe_forbid btn-default">' + disableBtn + '</button>'
                            + '<button id="fe_view" class="btn btn-default"><span class="glyphicon glyphicon-stats"></span>监控</button><button class="btn btn-default fe_update">'
                            + '<span class="glyphicon glyphicon-pencil"></span>修改</button><button class="btn btn-danger fe_delete"><span class="glyphicon glyphicon-trash"></span>删除</button></div>';
                        var settings = '<ul class="settings"><li><label>监听协议&nbsp;:&nbsp;' + foreProtocol + '</label><label>端口&nbsp;:&nbsp;' + forePort + '</label></li>'
                            + '<li><label>负载方式&nbsp;:&nbsp;' + policy + '</label></li></ul>';
                        var bebar = '<div class="once-toolbar"><button id="be_frash" class="btn btn-default rule-refresh">'
                            + '<span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button><button class="btn btn-default be_create">'
                            + '&nbsp;+&nbsp;添加后端</button><button class="btn btn-disable be_delete" order="' + i + '" name="be_delete" disabled><span class="glyphicon glyphicon-trash"></span>删除</button></div>';
                        var forecontent = '<div ' + foreClass + ' feid="' + foreUuid + '" fename="' + foreName + '" feprotocol="' + foreProtocol + '"  feport="'
                            + forePort + '" fepolicy="' + forePolicy + '"festate="' + foreStatus + '"><div class="fe_bar"><div class="foretitle">监听器:&nbsp;lbl-'
                            + foreUuid.substring(0, 8) + '&nbsp;(' + foreName + ')</div>' + btngroup + '</div>' + settings + bebar
                            + '<table class="table table-bordered once-table"><thead><tr><th></th><th>名称</th><th>状态</th><th>主机</th><th>端口</th>'
                            + '<th>权重</th><th>监控</th><th>操作</th></tr></thead><tbody class="backbody">';
                        for (var j = 0; j < beArray.length; j++) {
                            var beobj = beArray[j];
                            var backUuid = beobj.backUuid;
                            var backName = decodeURI(beobj.backName);
                            var vmUuid = beobj.vmUuid;
                            var vmIp = beobj.vmIp;
                            var vmPort = beobj.vmPort;
                            var backWeight = beobj.backWeight;
                            var backStatus = beobj.backStatus;
                            if (0 == backStatus) {
                                bstate = '启用';
                            } else {
                                bstate = '禁用';
                            }
                            var beClass = "";
                            if (backStatus == 0) {
                                beClass = 'class="idle"';
                            }
                            var backcontent = '<tr rowid="' + backUuid + '" ' + beClass + '><td class="rcheck"><input type="checkbox" name="berow' + i + '"></td><td>'
                                + backName + '</td><td><span class="icon-status icon-running" name="stateicon">'
                                + '</span><span name="stateword">可用</span></td><td><a>i-' + vmUuid.substring(0, 8) + '</a></td><td>'
                                + vmPort + '</td><td>' + backWeight + '</td><td><span class="glyphicon glyphicon-stats"></span></td>'
                                + '<td class="be_show"><a bestate="' + backStatus + '" class="id be_forbid">' + bstate + '</a></td></tr>';
                            forecontent = forecontent + backcontent;
                        }
                        forecontent = forecontent + '</tbody></table></div>';
                        content = content + forecontent;
                    }
                    $('#fore_list').html(content);
                }
            },
            error: function () {
            }
        });
    }

    $('#fe_apply').on('click', function (event) {
        event.preventDefault();
        var lbstate = $('#lb-status').hasClass('icon-running');
        if (lbstate) {
            var lbUuid = $('#platformcontent').attr("lbUuid");
            $.ajax({
                type: 'get',
                url: '/LBAction',
                data: "action=applylb&lbuuid=" + lbUuid,
                dataType: 'json',
                success: function (obj) {
                    if (obj.result == true) {
                        $("#fe_apply").removeClass('btn-primary').addClass('btn-default');
                        $("#suggestion").hide();
                    }
                },
                error: function () {
                }
            });
        } else {
            var message = '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;负载均衡处于不活跃状态，无法进行更新</div>';
            bootbox.dialog({
                className: "oc-bootbox",
                message: message,
                title: '状态提示',
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary"
                    }
                }
            });
        }
    });

    $('#fe_create').on('click', function (event) {
        event.preventDefault();
        var lbUuid = $('#platformcontent').attr("lbUuid");
        var url = $("#platformcontent").attr("platformBasePath") + 'user/create/createforeend.jsp';
        $('#LbModalContainer').load(url, { "name": "", "protocol": "", "port": "", "policy": "", "foreuuid": "", "type": "new", "lbuuid": lbUuid}, function () {
            $('#LbModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#fore_list').on('click', '.be_create', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().attr('feid');
        var url = $("#platformcontent").attr("platformBasePath") + 'user/create/createbackend.jsp';
        $('#LbModalContainer').load(url, {"foreuuid": uuid}, function () {
            $('#LbModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#fore_list').on('click', '.fe_update', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().parent().attr('feid');
        var name = $(this).parent().parent().parent().attr('fename');
        var protocol = $(this).parent().parent().parent().attr('feprotocol');
        var port = $(this).parent().parent().parent().attr('feport');
        var policy = $(this).parent().parent().parent().attr('fepolicy');
        var lbUuid = $('#platformcontent').attr("lbUuid");
        var url = $("#platformcontent").attr("platformBasePath") + 'user/create/createforeend.jsp';
        $('#LbModalContainer').load(url, {"name": name, "protocol": protocol, "port": port, "policy": policy, "foreuuid": uuid, "type": "update", "lbuuid": lbUuid}, function () {
            $('#LbModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#fore_list').on('click', '.be_forbid', function (event) {
        event.preventDefault();
        var beuuid = $(this).parent().parent().attr('rowid');
        var lbUuid = $('#platformcontent').attr("lbUuid");
        var state = $(this).attr('bestate');
        if (state == 0) {
            state = 1;
        } else {
            state = 0;
        }
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: "action=forbidback&state=" + state + "&backuuid=" + beuuid + "&lbuuid=" + lbUuid,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    var thisbe = $('tr[rowid="' + beuuid + '"]');
                    var thisbtn = thisbe.find('.be_show');
                    if (state == 0) {
                        thisbtn.html('<a bestate="0" class="id be_forbid">启用</a>');
                        thisbe.addClass('idle');
                    } else {
                        thisbtn.html('<a bestate="1" class="id be_forbid">禁用</a>');
                        thisbe.removeClass('idle');
                    }
                    needApply();
                }
            },
            error: function () {
            }
        });
    });

    $('#fore_list').on('click', '.fe_forbid', function (event) {
        event.preventDefault();
        var feuuid = $(this).parent().parent().parent().attr('feid');
        var lbUuid = $('#platformcontent').attr("lbUuid");
        var state = $(this).parent().parent().parent().attr('festate');
        if (state == 0) {
            state = 1;
        } else {
            state = 0;
        }
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: "action=forbidfore&state=" + state + "&foreuuid=" + feuuid + '&lbuuid=' + lbUuid,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    var thisfe = $('div[feid="' + feuuid + '"]');
                    var thisbtn = thisfe.find('.fe_forbid');
                    thisfe.attr("festate", state);
                    if (state == 0) {
                        thisfe.addClass('lb-disable');
                        thisbtn.html('<span class="glyphicon glyphicon-play"></span>启用');
                    } else {
                        thisfe.removeClass('lb-disable');
                        thisbtn.html('<span class="glyphicon glyphicon-ban-circle"></span>禁用');
                    }
                    needApply();
                }
            },
            error: function () {
            }
        });
    });

    $('#fore_list').on('click', '.rule-refresh', function (event) {
        event.preventDefault();
        getForeList();
    });

    $('#fore_list').on('click', '.fe_delete', function (event) {
        event.preventDefault();
        var feuuid = $(this).parent().parent().parent().attr('feid');
        showbox(0, feuuid);
    });

    $('#fore_list').on('click', '.be_delete', function (event) {
        event.preventDefault();
        var order = $(this).attr('order');
        showbox(1, order);
    });

    function deleteFE(feuuid) {
        var lbUuid = $('#platformcontent').attr("lbUuid");
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: "action=deletefore&foreuuid=" + feuuid + "&lbUuid=" + lbUuid,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    $('div[feid="' + feuuid + '"]').remove();
                    needApply();
                }
            },
            error: function () {
            }
        });
    }

    function deleteBE(beuuid) {
        var lbUuid = $('#platformcontent').attr("lbUuid");
        $.ajax({
            type: 'get',
            url: '/LBAction',
            data: "action=deleteback&backuuid=" + beuuid + "&lbuuid=" + lbUuid,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    $('tr[rowid="' + beuuid + '"]').remove();
                    needApply();
                }
            },
            error: function () {
            }
        });
    }

    function needApply() {
        $('#fe_apply').removeClass('btn-default').addClass('btn-primary');
        $('#suggestion').show();
    }

    $('#fore_list').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        var total = 0;
        var name = $(this).attr('name');
        $('input[name="' + name + '"]:checked').each(function () {
            total++;
        });
        var dlt = $(this).parent().parent().parent().parent().parent().find('[name="be_delete"]');
        if (total != 0) {
            dlt.attr("disabled", false).removeClass('btn-disable').addClass('btn-danger');
        } else {
            dlt.attr("disabled", true).removeClass('btn-danger').addClass('btn-disable');
        }
    });

    function getInfoList(feid) {
        var infoList = "";
        $('input[name="berow' + feid + '"]:checked').each(function () {
            var beuuid = $(this).parent().parent().attr("rowid");
            infoList += "[lbb-" + beuuid.substring(0, 8) + "]&nbsp;";
        });
        return infoList;
    }

    function removeAllCheck() {
        $('input[type=checkbox]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    function showbox(type, feuuid) {
        var infoArray = new Array("删除监听器", "删除后端");
        var showMessage = '';
        var showTitle = '';
        if (type == 0) {
            showMessage = '<div class="alert alert-info" style="margin:10px 10px 0"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除监听器将级联删除监听器后端，是否继续？</div>';
            showTitle = infoArray[type] + '&nbsp;[lbl-' + feuuid.substring(0, 8) + ']&nbsp;?';
        } else if (type == 1) {
            var info = getInfoList(feuuid);
            showMessage = '<div class="alert alert-info" style="margin:10px">'
                + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
                + infoArray[type] + '&nbsp;' + info + '?</div>';
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
                        if (type == 0) {
                            deleteFE(feuuid);
                        } else if (type == 1) {
                            $('input[name="berow' + feuuid + '"]:checked').each(function () {
                                var beuuid = $(this).parent().parent().attr("rowid");
                                deleteBE(beuuid);
                            });
                            removeAllCheck();
                        }
                    }
                },
                cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {
                        removeAllCheck();
                    }
                }
            }
        });
    }
});