reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getVnetList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="vnrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#addvm").addClass('btn-forbidden');
    $("#delete").addClass('btn-forbidden');
    $("#link").addClass('btn-forbidden');
    $("#unlink").addClass('btn-forbidden');
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var total = 0;
    var hasrouter = 0;
    var norouter = 0;
    $('input[name="vnrow"]:checked').each(function () {
        total++;
        var tr = $(this).parent().parent();
        var router = tr.children('td').eq(3).text();
        if (router != "") {
            hasrouter++;
        } else {
            norouter++;
        }
    });
    if (total == 1) {
        $("#addvm").removeClass('btn-forbidden');
        if (hasrouter == 1) {
            $("#unlink").removeClass('btn-forbidden');
        } else {
            $("#link").removeClass('btn-forbidden');
            $("#delete").removeClass('btn-forbidden');
        }
    } else if (total > 1) {
        if (hasrouter == total) {
            $("#unlink").removeClass('btn-forbidden');
        } else if (norouter == total) {
            $("#delete").removeClass('btn-forbidden');
        }
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#VnetModalContainer').load($(this).attr('url'), '', function () {
        $('#VnetModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getVnetList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/VnetAction/VnetList',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var total = array[0];
            var totalp = 1;
            if (total != 0) {
                totalp = Math.ceil(total / limit);
            }
            options = {
                totalPages: totalp
            };
            $('#pageDivider').bootstrapPaginator(options);
            pageDisplayUpdate(page, totalp);
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var uuid = obj.uuid;
                var name = decodeURI(obj.name);
                var routerid = obj.routerid;
                var _routerid = routerid;
                var showid = "vn-" + uuid.substring(0, 8);
                if (routerid == "null") {
                    routerid = '';
                } else {
                    routerid = '<a><span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;rt-' + routerid.substring(0, 8) + '</a>';
                }
                var thistr = '<tr rowid="' + uuid + '" vnname="' + name + '" routerrid="' + _routerid + '"><td class="rcheck"><input type="checkbox" name="vnrow"></td><td><a class="id">' + showid + '</a></td>'
                    + '<td>' + name + '</td><td>' + routerid + '</td>' + '<td class="time">'
                    + decodeURI(obj.createdate) + '</td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
	event.preventDefault();
    var uuid = $(this).parent().parent().attr('rowid');
    var form = $("<form></form>");
    form.attr("action","/vnet/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="vnetUuid" value="' + uuid + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

function getInfoList() {
    var infoList = "";
    $('input[name="vnrow"]:checked').each(function () {
        infoList += "[vn-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
    });
    return infoList;
}

function showbox(type) {
    var infoList = getInfoList();
    var infoArray = new Array("离开路由器", "删除私有网络");
    var showMessage = '';
    var showTitle = '';
    if (type == 0) {
        showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">离开路由器后,该网络内将无法访问外部网络.是否继续</div>';
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
                    $('input[name="vnrow"]:checked').each(function () {
                        var uuid = $(this).parent().parent().attr("rowid");
                        if (type == 0) {
                            unlink(uuid);

                        } else if (type == 1) {
                            deleteVnet(uuid);
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
}

$('#unlink').on('click', function (event) {
    event.preventDefault();
    showbox(0);
});
$('#delete').on('click', function (event) {
    event.preventDefault();
    showbox(1);
});
$('#addvm').on('click', function (event) {
    event.preventDefault();
    $('#VnetModalContainer').load($(this).attr('url'), '', function () {
        $('#VnetModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});
$('#link').on('click', function (event) {
    event.preventDefault();
    $('#VnetModalContainer').load($(this).attr('url'), '', function () {
        $('#VnetModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function unlink(uuid) {
    $.ajax({
        type: 'get',
        url: '/VnetAction',
        data: 'action=unlink&vnetid=' + uuid,
        dataType: 'text',
        success: function () {
            var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
            thistr.children('td').eq(3).html('');
        }
    });
}

function deleteVnet(uuid) {
    $.ajax({
        type: 'get',
        url: '/VnetAction',
        data: 'action=delete&uuid=' + uuid,
        dataType: 'text',
        success: function (result) {
            if (result == "Using") {
                bootbox.dialog({
                    message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign">'
                        + '</span>&nbsp;无法删除，依然有主机依赖于该私有网络</div>',
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
            }
        }
    });
}