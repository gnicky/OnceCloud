getVnetBasicList();
getVxnets();

$('#VnetModalContainer').on('hide', function (event) {
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

$('.list-refresh').on('click', function (event) {
    event.preventDefault();
    getVxnets();
});

$('.btn-refresh').unbind();
$('.btn-refresh').on('click', function (event) {
    event.preventDefault();
    getVnetBasicList();
});

function getVnetBasicList() {
    $('#basic-list').html("");
    var vnetUuid = $('#platformcontent').attr("vnetUuid");
    $.ajax({
        type: 'get',
        url: '/VnetAction/VnetDetail',
        data: {uuid: vnetUuid},
        dataType: 'json',
        success: function (obj) {
            var showstr = '<a class="id">vn-' + vnetUuid.substring(0, 8) + '</a>';
            var vnName = decodeURI(obj.vnetName);
            var vnDesc = decodeURI(obj.vnetDesc);
            var createDate = obj.createDate;
            var useDate = decodeURI(obj.useDate);
            var router = obj.vnetRouter;
            var hostList = obj.vmList;
            var hostStr = "";
            for (var i = 0; i < hostList.length; i++) {
                var host = hostList[i];
                var onehost = '<a class="id basic-host" uuid="' + host.hostid + '">i-' + host.hostid.substring(0, 8) + '</a>';
                hostStr += onehost + "&nbsp;";
            }
            if (hostList.length == 0) {
                hostStr = "&nbsp;";
            }
            if (router != "&nbsp;") {
                router = '<a class="id basic-router" uuid="' + router + '">(' + obj.vnetRouterName + ')</a>';
            }
            $('#basic-list').html('<dt>ID</dt><dd>'
                + showstr + '</dd><dt>名称</dt><dd id="rtname">'
                + vnName + '</dd><dt>描述</dt><dd id="rtdesc">'
                + vnDesc + '</dd><dt>路由器</dt><dd>'
                + router + '</dd><dt>主机</dt><dd>'
                + hostStr + '</dd><dt>创建时间</dt><dd class="time">'
                + createDate + '</dd><dt>运行时间</dt><dd class="time">'
                + useDate + '</dd>');
        }
    });
}

$('#basic-list').on('click', '.basic-host', function (event) {
    event.preventDefault();
    var uuid = $(this).attr('uuid');
    $.ajax({
        type: 'get',
        url: '/VMAction',
        data: 'action=detail&instanceuuid=' + uuid,
        dataType: 'text',
        success: function (response) {
            window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/instancedetail.jsp";
        }
    });
});

$('#basic-list').on('click', '.basic-router', function (event) {
    event.preventDefault();
    var uuid = $(this).attr('uuid');
    $.ajax({
        type: 'get',
        url: '/RouterAction',
        data: 'action=detail&routerUuid=' + uuid,
        dataType: 'text',
        success: function (response) {
            window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/routerdetail.jsp";
        }
    });
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
        }
    });
});

function getVxnets() {
    $('#vxnets-t').html("");
    var vnetUuid = $('#platformcontent').attr("vnetUuid");
    $.ajax({
        type: 'get',
        url: '/VnetAction',
        data: "action=getVms&vnetUuid=" + vnetUuid,
        dataType: 'json',
        success: function (array) {
            var table = $('<table></table>');
            table.addClass("table table-bordered once-table");
            if (array.length == 0) {
                var pvm = $('<p></p>');
                pvm.addClass("none");
                pvm.text("当前私有网络中没有主机。");
                $('#vxnets-t').append(table);
                $('#vxnets-t').append(pvm);
            } else {
                var thead = $('<thead></thead>');
                thead.html('<tr><th>主机 ID</th><th>名称</th><th>状态</th><th>IP 地址</th>');
                table.append(thead);
                var tbody = "";
                for (var i = 0; i < array.length; i++) {
                    var jsonocvm = array[i];
                    var stateStr = '';
                    if (jsonocvm.hoststatus == 0) {
                        stateStr = '<span id="rt-status" class="icon-status icon-stopped" name="stateicon"></span><span name="stateword">已关机</span>';
                    } else if (jsonocvm.hoststatus == 1) {
                        stateStr = '<span id="rt-status" class="icon-status icon-running" name="stateicon"></span><span name="stateword">正在运行</span>';
                    } else if (jsonocvm.hoststatus == 2) {
                        stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span>';
                    } else if (jsonocvm.hoststatus == 3) {
                        stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">销毁中</span>';
                    } else if (jsonocvm.hoststatus == 4) {
                        stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"></span><span name="stateword">启动中</span>';
                    } else if (jsonocvm.hoststatus == 5) {
                        stateStr = '<span id="rt-status" class="icon-status icon-process" name="stateicon"><span name="stateword">关机中</span>';
                    }
                    var hosip = jsonocvm.hostip;
                    if (hosip == "null") {
                        hosip = '<span class="none">分配中...</span>';
                    }
                    tbody += '<tr rowid="' + jsonocvm.hostid + '"><td><a class="id">i-' + jsonocvm.hostid.substring(0, 8) + '</a></td><td>'
                        + decodeURI(jsonocvm.hostname) + '</td><td>' + stateStr + '</td><td>' + hosip
                        + '</td></tr>';
                }
                table.append('<tbody>' + tbody + '</tbody>');
                $('#vxnets-t').append(table);
            }
        }
    });
}

$('#router-a').on('click', function (event) {
    event.preventDefault();
    var routerUuid = $('#platformcontent').attr("routerUuid");
    $.ajax({
        type: 'get',
        url: '/RouterAction',
        data: 'action=detail&routerUuid=' + routerUuid,
        dataType: 'text',
        success: function (response) {
            window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/routerdetail.jsp";
        }
    });
});