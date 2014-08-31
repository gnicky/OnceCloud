$(document).ready(function () {
    getRouterBasicList();
    getVxnets();
    $('#RouterModalContainer').on('hide', function (event) {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $('#modify').on('click', function (event) {
        event.preventDefault();
        var url = $('#platformcontent').attr('platformBasePath') + 'common/modify.jsp';
        var rtName = $('#rtname').text();
        var rtDesc = $('#rtdesc').text();
        var rtUuid = $('#platformcontent').attr("rtUuid");
        $('#RouterModalContainer').load(url, {"modifytype": "rt", "modifyuuid": rtUuid,
            "modifyname": rtName, "modifydesc": rtDesc}, function () {
            $('#RouterModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        getRouterBasicList();
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

    function getRouterBasicList() {
        $('#basic-list').html("");
        var rtUuid = $('#platformcontent').attr("rtUuid");
        $.ajax({
            type: 'get',
            url: '/RouterAction',
            data: "action=getonerouter&routerUuid=" + rtUuid,
            dataType: 'text',
            success: function (response) {
                var obj = jQuery.parseJSON(response);
                var rtName = decodeURI(obj.routerName);
                var rtDesc = decodeURI(obj.routerDesc);
                var rtIp = obj.routerIp;
                var rtMac = obj.routerMac;
                var rtStatus = obj.routerStatus;
                if (rtStatus == 2) {
                    $('#fe_apply').removeClass('btn-default').addClass('btn-primary');
                    $('#suggestion').show();
                }
                var rtUID = obj.routerUID;
                var rtCapacity = obj.routerCapacity;
                var rtPower = obj.routerPower;
                var rtFirewall = obj.routerFirewall;
                var createDate = obj.createDate;
                var useDate = decodeURI(obj.useDate);
                var stateStr = '';
                var showstr = '';
                var showuuid = "rt-" + rtUuid.substring(0, 8);
                if (rtPower == 0) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-stopped" name="stateicon"></span><span name="stateword">已关机</span></td>';
                } else if (rtPower == 1) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-running" name="stateicon"></span><span name="stateword">活跃</span></td>';
                } else if (rtPower == 2) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
                } else if (rtPower == 3) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">销毁中</span></td>';
                } else if (rtPower == 4) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">启动中</span></td>';
                } else if (rtPower == 5) {
                    stateStr = '<td><span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">关机中</span></td>';
                }
                showstr = "<a class='id'>" + showuuid + '</a>';
                if ("&nbsp;" != rtFirewall) {
                    rtFirewall = '<a id="firewallid" uuid="' + rtFirewall + '">fw-' + rtFirewall.substring(0, 8) + '</a>';
                }
                var network;
                if (rtIp == "null") {
                    network = '<a class="id">(基础网络)</a>';
                } else {
                    network = '<a class="id">(基础网络)&nbsp;/&nbsp;' + rtIp + '</a>';
                }
                var rtEip = obj.eip;
                if (rtEip == "") {
                    rtEip = "&nbsp;";
                } else {
                    rtEip = '<a class="id" id="eip" eipip="' + rtEip + '">' + rtEip + '</a>';
                }
                $('#basic-list').html('<dt>ID</dt><dd>'
                    + showstr + '</dd><dt>名称</dt><dd id="rtname">'
                    + rtName + '</dd><dt>描述</dt><dd id="rtdesc">'
                    + rtDesc + '</dd><dt>状态</dt><dd>'
                    + stateStr + '</dd><dt>峰值带宽</dt><dd>'
                    + rtCapacity + '&nbsp;Mbps</dd><dt>Mac地址</dt><dd>'
                    + rtMac + '</dd><dt>创建时间</dt><dd class="time">'
                    + createDate + '</dd><dt>运行时间</dt><dd class="time">'
                    + useDate + '</dd>');
                $('#depend-list').html('<dt>网络</dt><dd>'
                    + network + '</dd><dt>公网IP</dt><dd>'
                    + rtEip + '</dd><dt>防火墙</dt><dd>'
                    + rtFirewall + '</dd>');
            },
            error: function () {
            }
        });
    }

    $('#depend-list').on('click', '#eip', function (event) {
        event.preventDefault();
        var eip = $(this).attr("eipip");
        var basePath = $("#platformcontent").attr("platformBasePath");
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: 'action=detail&eip=' + eip,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/elasticipdetail.jsp";
            },
            error: function () {

            }
        });
    });


    $('#apply').on('click', function (event) {
        event.preventDefault();
        var rtstate = $('#rt-status').hasClass('icon-running');
        if (rtstate) {
            var rtUuid = $('#platformcontent').attr("rtUuid");
            $.ajax({
                type: 'get',
                url: '/RouterAction',
                data: "action=applyrouter&routeruuid=" + rtUuid,
                dataType: 'json',
                success: function (obj) {
                    if (obj.result == true) {
                        $("#apply").removeClass('btn-primary').addClass('btn-default');
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


    function needApply() {
        $('#apply').removeClass('btn-default').addClass('btn-primary');
        $('#suggestion').show();
    }

    /**
     * 2014/08/14 hty
     */
    $('.filter-once-tab').on('click', '.tab-filter', function (event) {
        event.preventDefault();
        $('li', $('.filter-once-tab')).removeClass('selected');
        $(this).addClass('selected');
        var type = $(this).attr("type").toString();
        $(".pane-filter").hide();
        $("#" + type).show();
    });

    $('#vxnets-t').on('click', '.id', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().attr('rowid');
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=detail&instanceuuid=' + uuid,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/instancedetail.jsp";
            },
            error: function () {
            }
        });
    });
    /**
     * 2014/08/14 hty
     */
    function getVxnets() {
        $('#vxnets-t').html("");
        var rtUuid = $('#platformcontent').attr("rtUuid");
        var basePath = $('#platformcontent').attr("platformBasePath");
        $.ajax({
            type: 'get',
            url: '/RouterAction',
            data: "action=getvxnets&routerUuid=" + rtUuid,
            dataType: 'json',
            success: function (array) {
                if (array.length == 0) {
                    var _p = $('<p></p>');
                    _p.addClass("none");
                    _p.text("当前没有私有网络连接到此路由器，请点击");
                    var _a = $('<a href="' + basePath + 'user/vnet.jsp"></a>');
                    _a.text("这里");
                    _p.append(_a);
                    _p.append("创建。");
                    $('#vxnets-t').append(_p);
                } else {
                    $.each(array, function (index, json) {
                        var divone = $('<div></div>');
                        divone.addClass("static-title");
                        divone.html("私有网络:&nbsp;");
                        var _a = $('<a></a>');
                        _a.addClass("static-title");
                        _a.text(json.vn_name);
                        divone.append(_a);
                        var __a = $('<a></a>');
                        __a.addClass("static-title");
                        __a.html("&nbsp;[关闭 DHCP 服务]");
                        divone.append(__a);
                        var divinner = $('<div></div>');
                        var spanone = $('<span></span>');
                        spanone.addClass("none");
                        spanone.text("网络地址：192.168.");
                        spanone.append(json.vn_net + ".0/24");
                        divinner.append(spanone);
                        var spantwo = $('<span></span>');
                        spantwo.addClass("none");
                        spantwo.text("管理地址：192.168.");
                        spantwo.append(json.vn_net + json.vn_gate);
                        divinner.append(spantwo);
                        var spanthree = $('<span></span>');
                        spanthree.addClass("none");
                        spanthree.text("范围：192.168.");
                        spanthree.append(json.vn_net + "." + json.vn_dhcp_start + " - " +
                            "192.168." + json.vn_net + "." + json.vn_dhcp_end);
                        divinner.append(spanthree);
                        divone.append(divinner);
                        $('#vxnets-t').append(divone);
                        var table = $('<table></table>');
                        table.addClass("table table-bordered once-table");
                        if (json.ocvm.toString() == "null") {
                            var pvm = $('<p></p>');
                            pvm.addClass("none");
                            pvm.text("当前私有网络中没有主机。");
                            $('#vxnets-t').append(table);
                            $('#vxnets-t').append(pvm);
                        } else {
                            var thead = $('<thead></thead>');
                            thead.html('<tr><th>主机 ID</th><th>名称</th><th>状态</th><th>IP 地址</th><th>DHCP 选项</th><th>操作</th></tr>');
                            table.append(thead);
                            $.each(json.ocvm, function (index, jsonocvm) {
                                var stateStr = '';
                                if (jsonocvm.hoststatus == 0) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-stopped" name="stateicon"></span>';
                                } else if (jsonocvm.hoststatus == 1) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-running" name="stateicon"></span>';
                                } else if (jsonocvm.hoststatus == 2) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span>';
                                } else if (jsonocvm.hoststatus == 3) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span>';
                                } else if (jsonocvm.hoststatus == 4) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span>';
                                } else if (jsonocvm.hoststatus == 5) {
                                    stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon">';
                                }
                                var hosip = jsonocvm.hostip;
                                if (hosip == "null") {
                                    hosip = '<span class="none">分配中...</span>';
                                }
                                var tbody = $('<tbody></tbody>');
                                tbody.html('<tr rowid="' + jsonocvm.hostid + '"><td><a class="id">i-' + jsonocvm.hostid.substring(0, 8) + '</a></td><td>'
                                    + decodeURI(jsonocvm.hostname) + '</td><td>' + stateStr + '</td><td>' + hosip
                                    + '</td><td></td><td><a href="#">修改</a></td>');
                                table.append(tbody);
                            });
                            $('#vxnets-t').append(table);
                        }
                    });
                }
            }
        });
    }

});