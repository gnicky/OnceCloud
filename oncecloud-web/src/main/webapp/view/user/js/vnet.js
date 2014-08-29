$(document).ready(function () {

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = $('#limit').val();
            var search = $('#search').val();
            getVnetList(page, limit, search);
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
    $('#pageDivider').bootstrapPaginator(options);
    getVnetList(1, 10, "");

    $('#limit').on('focusout', function () {
        var limit = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $("#limit").val(10);
        }
        reloadList();
    });

    $('#search').on('focusout', function () {
        reloadList();
    });

    $('#search').keypress(function (e) {
        var key = e.which;
        if (key == 13) {
            reloadList();
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

    function reloadList() {
        var limit = $('#limit').val();
        var search = $('#search').val();
        var type = $('.once-tab').find('.active').attr("type");
        getVnetList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    $('.btn-refresh').on('click', function (event) {
        reloadList();
    });

    function getVnetList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/VnetAction',
            data: 'action=getlist&page=' + page + '&limit=' + limit + '&search=' + search,
            dataType: 'json',
            success: function (array) {
                var total = array[0];
                var totalp = 1;
                if (total != 0) {
                    totalp = Math.ceil(total / limit);
                }
                options = {
                    totalPages: totalp
                }
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
            },
            error: function () {
            }
        });
    }

    $('#tablebody').on('click', '.id', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().attr('rowid');
        var routerid = $(this).parent().parent().attr('routerrid');
        $.ajax({
            type: 'get',
            url: '/VnetAction',
            data: 'action=detail&vnetUuid=' + uuid + '&routerid=' + routerid,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/vnetdetail.jsp";
            },
            error: function () {
            }
        });
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

    function removeAllCheck() {
        $('input[name="vnrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
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
            $("#addvm").attr('disabled', false).removeClass('btn-forbidden');
            if (hasrouter == 1) {
                $("#unlink").attr('disabled', false).removeClass('btn-forbidden');
            } else {
                $("#link").attr('disabled', false).removeClass('btn-forbidden');
                $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            }
        } else if (total > 1) {
            if (hasrouter == total) {
                $("#unlink").attr('disabled', false).removeClass('btn-forbidden');
            } else if (norouter == total) {
                $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            }
        }
    });

    function allDisable() {
        $("#addvm").attr("disabled", true).addClass('btn-forbidden');
        $("#delete").attr("disabled", true).addClass('btn-forbidden');
        $("#link").attr("disabled", true).addClass('btn-forbidden');
        $("#unlink").attr("disabled", true).addClass('btn-forbidden');
    }

    function unlink(uuid) {
        $.ajax({
            type: 'get',
            url: '/VnetAction',
            data: 'action=unlink&vnetid=' + uuid,
            dataType: 'text',
            success: function () {
                var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
                thistr.children('td').eq(3).html('');
            },
            error: function () {
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
            },
            error: function () {
            }
        });
    }
});