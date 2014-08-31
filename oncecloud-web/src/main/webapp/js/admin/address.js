reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    var type = $('.once-tab').find('.active').attr("oc-type");
    getAddressList(page, limit, search, type);
    if (page == 1) {
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="addressrow"]').each(function () {
        $(this).attr("checked", false);
        $(this).change();
    });
}

function allDisable() {
    $("#delete").addClass('btn-forbidden').attr('disabled', true);
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var free = 0;
    var total = 0;
    $('input[name="addressrow"]:checked').each(function () {
        if ($(this).parent().parent().find('[state]').attr('state') == 'free') {
            free++;
        }
        total++;
    });
    if (free > 0 && free == total) {
        $("#delete").removeClass('btn-forbidden').attr('disabled', false);
    }
});

$('.once-tab').on('click', '.tab-filter', function (event) {
    $('li', '.once-tab').removeClass('active');
    $(this).addClass('active');
    reloadList(1);
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#IPModalContainer').load($(this).attr('url'), '', function () {
        $('#IPModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getAddressList(page, limit, search, type) {
    $('#tablebody').html("");
    if (type == "dhcp") {
        $('#tablethead').html('<tr><th width="4%"></th><th width="20%">MAC</th><th width="20%">IP</th>'
            + '<th width="12%">状态</th><th width="16%">应用资源</th><th width="12%">所属用户</th>'
            + '<th width="16%">创建时间</th></tr>');
        $.ajax({
            type: 'get',
            url: '/AddressAction/DHCPList',
            data: {page: page, limit: limit, search: search},
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    var totalnum = array[0];
                    var totalp = 1;
                    if (totalnum != 0) {
                        totalp = Math.ceil(totalnum / limit);
                    }
                    options = {
                        totalPages: totalp
                    }
                    $('#pageDivider').bootstrapPaginator(options);
                    pageDisplayUpdate(page, totalp);
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var ip = obj.dhcpip;
                        var mac = obj.dhcpmac;
                        var uuid = obj.tenantuuid;
                        var showid = obj.showid;
                        var type = obj.depenType;
                        var stateStr = '<td state="free"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">空闲</span></td>';
                        if (uuid != "") {
                            stateStr = '<td state="using"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">租用中</span></td>';
                        }
                        if (0 == type) {
                            showid = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + showid;
                        } else if (1 == type) {
                            showid = '<span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;' + showid;
                        } else if (2 == type) {
                            showid = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;' + showid;
                        } else if (3 == type) {
                            showid = '<span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;' + showid;
                        }
                        var username = obj.tenantuser;
                        var createdate = obj.createdate;
                        var thistr = '<tr mac="' + mac + '" ip="' + ip + '"><td class="rcheck"><input type="checkbox" name="addressrow"></td>'
                            + '<td><a class="id">' + mac + '</a></td><td>' + ip + '</td>' + stateStr + '<td><a class="id">' + showid + '</a></td><td>' + username + '</td><td class="time">' + createdate + '</td>';
                        tableStr += thistr;
                    }
                    $('#tablebody').html(tableStr);
                }
            }
        });
    } else if (type == "publicip") {
        $('#tablethead').html('<tr><th width="4%"></th><th width="12%">IP</th><th width="12%">'
            + '状态</th><th width="12%">应用资源</th><th width="12%">带宽&nbsp;(Mbps)</th><th width="12%">'
            + 'IP分组</th><th width="12%">网关接口</th><th width="12%">所属用户</th><th width="12%">创建时间</th> </tr>');
        $.ajax({
            type: 'get',
            url: '/AddressAction/EIPList',
            data: {page: page, limit: limit, search: search},
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    var totalnum = array[0];
                    var totalp = 1;
                    if (totalnum != 0) {
                        totalp = Math.ceil(totalnum / limit);
                    }
                    options = {
                        totalPages: totalp
                    }
                    $('#pageDivider').bootstrapPaginator(options);
                    pageDisplayUpdate(page, totalp);
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var eip = obj.eip;
                        var euuid = obj.euuid;
                        var depenType = obj.depenType;
                        var eipDependency = "";
                        if (obj.eipDependency != "") {
                            eipDependency = obj.eipDependency.substring(0, 8);
                            if (0 == depenType) {
                                eipDependency = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;i-' + eipDependency;
                            } else if (1 == depenType) {
                                eipDependency = '<span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;lb-' + eipDependency;
                            } else if (2 == depenType) {
                                eipDependency = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;db-' + eipDependency;
                            } else if (3 == depenType) {
                                eipDependency = '<span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;db-' + eipDependency;
                            }
                        }
                        var eipBandwidth = obj.eipBandwidth;
                        var eipDescription = decodeURI(obj.eipDescription);
                        var eipType = decodeURI(obj.eipType);
                        var eipIf = obj.eif;
                        var euername = decodeURI(obj.euername);
                        if (!eipBandwidth) {
                            eipBandwidth = "";
                        }
                        var stateStr = '<td state="free"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">空闲</span></td>';
                        if (euername != "") {
                            stateStr = '<td state="using"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">租用中</span></td>';
                        }
                        var createdate = obj.createdate;
                        var thistr = '<tr ip="' + eip + '" euuid="' + euuid + '"><td class="rcheck"><input type="checkbox" name="addressrow"></td>'
                            + '<td>' + eip + '</td>' + stateStr + '<td><a class="id">' + eipDependency + '</a></td><td>' + eipBandwidth + '</td><td>' + eipType + '</td><td>Interface&nbsp;' + eipIf + '</td><td>' + euername + '</td><td class="time">' + createdate + '</td></tr>';
                        tableStr += thistr;
                    }
                    $('#tablebody').html(tableStr);
                }
            }
        });
    }
}

$('#delete').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    $('input[name="addressrow"]:checked').each(function () {
        infoList += "[" + $(this).parent().parent().attr("ip") + "]&nbsp;";
    });
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除地址&nbsp;' + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    var type = $('.once-tab').find('.active').attr("oc-type");
                    $('input[name="addressrow"]:checked').each(function () {
                        if (type == "dhcp") {
                            deleteDHCP($(this).parent().parent().attr("ip"), $(this).parent().parent().attr("mac"));
                        }
                        else if (type == "publicip") {
                            deleteEIP($(this).parent().parent().attr("ip"), $(this).parent().parent().attr("euuid"));
                        }
                    });
                    removeAllCheck();
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
});

function deleteDHCP(ip, mac) {
    $.ajax({
        type: 'post',
        url: '/AddressAction',
        data: {action: "deleteDHCP", ip: ip, mac: mac},
        dataType: 'json',
        success: function (array) {
            if (array.length == 1) {
                var result = array[0].result;
                if (result) {
                    var thistr = $("#tablebody").find('[ip="' + ip + '"]');
                    $(thistr).remove();
                }
            }
        }
    });
}

function deleteEIP(ip, uuid) {
    $.ajax({
        type: 'post',
        url: '/AddressAction',
        data: {action: "deleleEIP", ip: ip, uuid: uuid},
        dataType: 'json',
        success: function (array) {
            if (array.length == 1) {
                var result = array[0].result;
                if (result) {
                    var thistr = $("#tablebody").find('[euuid="' + uuid + '"]');
                    $(thistr).remove();
                }
            }
        }
    });
}