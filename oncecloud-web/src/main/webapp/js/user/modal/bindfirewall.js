$("#FirewallModalContainer").on("hidden", function () {
    $(this).removeData("modal");
    $(this).children().remove();
});

$('#bind2vm').on('click', function (event) {
    event.preventDefault();
    var firewallId;
    $('input[name="firewallrow"]:checked').each(function () {
        firewallId = $(this).parent().parent().attr('firewallid');
    });
    var bindtype1 = $('#firewall-modal').attr('type');
    var vmboxes = document.getElementsByName("image-item");
    var vmuuidStr = '[';
    var flag = 0;
    for (var i = 0; i < vmboxes.length; i++) {
        if ($(vmboxes[i]).hasClass('selected')) {
            flag++;
            var vmuuid = $(vmboxes[i]).attr("vmuuid");
            if (flag == 1) {
                vmuuidStr = vmuuidStr + vmuuid;
            } else {
                vmuuidStr = vmuuidStr + ',' + vmuuid;
            }
        }
    }
    vmuuidStr = vmuuidStr + ']';
    bindfirewall(firewallId, vmuuidStr, bindtype1);
    removeAllCheck();
});

$('#cancelbind').on('click', function (event) {
    event.preventDefault();
    removeAllCheck();
});

var bindtype = $('#firewall-modal').attr('type');
if ('lb' == bindtype) {
    $('#modaltitle').html('负载均衡列表<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');
    $('#alert').html('请选择需要绑定防火墙的负载均衡');
} else if ('rt' == bindtype) {
    $('#modaltitle').html('路由器列表<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');
    $('#alert').html('请选择需要绑定防火墙的虚拟路由器');
} else if ('db' == bindtype) {
    $('#modaltitle').html('选择要加载防火墙的数据库<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');
    $('#alert').html('只有位于基础网络&nbsp;vlan-0&nbsp;中的数据库才需要使用防火墙');
}

var options = {
    bootstrapMajorVersion: 3,
    currentPage: 1,
    totalPages: 1,
    numberOfPages: 0,
    onPageClicked: function (e, originalEvent, type, page) {
        if ('vm' == bindtype) {
            getInstanceList(page, 5, "");
        } else if ('lb' == bindtype) {
            getLoadBalanceList(page, 5, "");
        } else if ('rt' == bindtype) {
            getRouterList(page, 5, "");
        }
    },
    shouldShowPage: function (type, page, current) {
        switch (type) {
            case "first":
            case "last":
                return false;
            default:
                return true;
        }
    }
}

$('#bindPS').bootstrapPaginator(options);
if ('vm' == bindtype) {
    getInstanceList(1, 5, "");
} else if ('lb' == bindtype) {
    getLoadBalanceList(1, 5, "");
} else if ('rt' == bindtype) {
    getRouterList(1, 5, "");
}

function getRouterList(page, limit, search) {
    $('#instancelist').html("");
    $.ajax({
        type: 'post',
        url: '/RouterAction/RoutersOfUser',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var totalnum = array[0];
            var totalp = 1;
            if (totalnum != 0) {
                totalp = Math.ceil(totalnum / limit);
            }
            options = {
                totalPages: totalp
            }
            $('#bindPS').bootstrapPaginator(options);
            modalPageUpdate(page, totalp);
            var tableStr = "";
            if (0 != totalnum) {
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var vmuuid = obj.vmid;
                    var vmName = decodeURIComponent(obj.vmname);
                    var showuuid = "i-" + vmuuid.substring(0, 8);
                    tableStr = tableStr + '<div name="image-item" class="image-item" vmuuid="'
                        + vmuuid + '"><div class="image-left">'
                        + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                }
            } else {
                $('#alert').html('没有可选择的虚拟路由器');
            }
            $('#instancelist').html(tableStr);
        }
    });
}

function getLoadBalanceList(page, limit, search) {
    $('#instancelist').html("");
    $.ajax({
        type: 'post',
        url: '/LBAction/LBsOfUser',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var totalnum = array[0];
            var totalp = 1;
            if (totalnum != 0) {
                totalp = Math.ceil(totalnum / limit);
            }
            options = {
                totalPages: totalp
            }
            $('#bindPS').bootstrapPaginator(options);
            modalPageUpdate(page, totalp);
            var tableStr = "";
            if (0 != totalnum) {
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var vmuuid = obj.vmid;
                    var vmName = decodeURIComponent(obj.vmname);
                    var showuuid = "i-" + vmuuid.substring(0, 8);
                    tableStr = tableStr + '<div name="image-item" class="image-item" vmuuid="'
                        + vmuuid + '"><div class="image-left">'
                        + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                }
            } else {
                $('#alert').html('没有可选择的负载均衡');
            }
            $('#instancelist').html(tableStr);
        }
    });
}

function getInstanceList(page, limit, search) {
    $('#instancelist').html("");
    $.ajax({
        type: 'post',
        url: '/VMAction/VMsOfUser',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var totalnum = array[0];
            var totalp = 1;
            if (totalnum != 0) {
                totalp = Math.ceil(totalnum / limit);
            }
            options = {
                totalPages: totalp
            }
            $('#bindPS').bootstrapPaginator(options);
            modalPageUpdate(page, totalp);
            var tableStr = "";
            if (0 != totalnum) {
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var vmuuid = obj.vmid;
                    var vmName = decodeURIComponent(obj.vmname);
                    var showuuid = "i-" + vmuuid.substring(0, 8);
                    tableStr = tableStr + '<div name="image-item" class="image-item" vmuuid="'
                        + vmuuid + '"><div class="image-left">'
                        + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                }
            } else {
                $('#alert').html('没有可选择的主机');
            }
            $('#instancelist').html(tableStr);
        }
    });
}

$('.instancelist').on('click', '.image-item', function (event) {
    event.preventDefault();
    if ($(this).hasClass('selected')) {
        $(this).removeClass('selected');
    } else {
        $(this).addClass('selected');
    }
    var count = 0;
    $('div[name="image-item"]').each(function () {
        if ($(this).hasClass('selected')) {
            count++;
        }
    });
    if (count == 0) {
        $('#bind2vm').attr('disabled', true);
    } else {
        $('#bind2vm').attr('disabled', false);
    }
});

function bindfirewall(firewallId, vmuuidStr, bindtype) {
    $.ajax({
        type: 'post',
        url: '/FirewallAction/Bind',
        data: {firewallId: firewallId, vmUuidStr: vmuuidStr, bindType: bindtype},
        dataType: 'text',
        success: function (response) {
        }
    });
}

function modalPageUpdate(current, total) {
    $('#currentPS').html(current);
    $('#totalPS').html(total);
}

function removeAllCheck() {
    $('input[name="firewallrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}