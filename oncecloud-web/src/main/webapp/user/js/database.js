$(document).ready(function () {
    $('#create').on('click', function (event) {
        event.preventDefault();
        $('#DatabaseModalContainer').load($(this).attr('url'), '', function () {
            $('#DatabaseModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = $("#limit").val();
            var search = $("#search").val();
            getDatabaseList(page, limit, search);
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
    getDatabaseList(1, 10, "");

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

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        reloadList();
    });

    $('#start').on('click', function (event) {
        event.preventDefault();
        $('input[name="dbrow"]:checked').each(function () {
            var uuid = $(this).parent().parent().attr("rowid");
            startDB(uuid);
        });
        removeAllCheck();
    });

    $('#close').on('click', function (event) {
        event.preventDefault();
        $('input[name="dbrow"]:checked').each(function () {
            var uuid = $(this).parent().parent().attr("rowid");
            shutdownDB(uuid, true);
        });
        removeAllCheck();
    });

    $('#delete').on('click', function (event) {
        event.preventDefault();
        $('input[name="dbrow"]:checked').each(function () {
            var uuid = $(this).parent().parent().attr("rowid");
            destroyDB(uuid);
        });
        removeAllCheck();
    });

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        allDisable();
        var running = 0;
        var process = 0;
        var stopped = 0;
        var total = 0;
        $('input[name="dbrow"]:checked').each(function () {
            var stateicon = $(this).parent().parent().find('[name="stateicon"]');
            if (stateicon.hasClass('icon-running')) {
                running++;
            } else if (stateicon.hasClass('icon-stopped')) {
                stopped++;
            } else {
                process++;
            }
            total++;
        });
        if (total != 0 && process == 0) {
            $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            if (running > 0 && stopped == 0) {
                $("#close").attr('disabled', false).removeClass('btn-forbidden');
            } else if (running == 0 && stopped > 0) {
                $("#start").attr('disabled', false).removeClass('btn-forbidden');
            }
        }
    });


    function reloadList() {
        var limit = $("#limit").val();
        var search = $("#search").val();
        getDatabaseList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    function removeAllCheck() {
        $('input[name="dbrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    function getDatabaseList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/DatabaseAction',
            data: "action=getlist&page=" + page + "&limit=" + limit + "&search=" + search,
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
                $('#pageDivider').bootstrapPaginator(options);
                pageDisplayUpdate(page, totalp);
                var tableStr = "";
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var dbid = obj.dbid;
                    var dbname = decodeURI(obj.dbname);
                    var dbstate = obj.dbstate;
                    var dbtype = obj.dbtype;
                    var dbip = obj.dbip;
                    var network;
                    if (dbip == "null") {
                        network = '<a>(基础网络)</a>';
                    } else {
                        network = '<a>(基础网络)&nbsp;/&nbsp;' + dbip + '</a>';
                    }
                    var dbeip = obj.dbeip;
                    if (dbeip != "") {
                        dbeip = '<a><span class="glyphicon glyphicon-globe"></span>&nbsp;&nbsp;' + dbeip + '</a>'
                    }
                    var dbport = obj.dbport;
                    var dbthroughout = obj.dbthroughout;
                    var createdate = decodeURI(obj.createdate);
                    var iconStr = new Array("stopped", "running", "process", "process", "process", "process");
                    var nameStr = new Array("已关机", "活跃", "创建中", "销毁中", "启动中", "关机中");
                    var stateStr = '<td><span class="icon-status icon-' + iconStr[dbstate] + '" name="stateicon">'
                        + '</span><span name="stateword">' + nameStr[dbstate] + '</span></td>';
                    var showid = "db-" + dbid.substring(0, 8);
                    var showstr = "<a class='id'>" + showid + '</a>';
                    var thistr = '<tr rowid="' + dbid + '"><td class="rcheck"><input type="checkbox" name="dbrow"></td><td>'
                        + showstr + '</td><td name="dbname">' + dbname + '&nbsp;(' + dbtype + ')</td>' + stateStr + '<td name="dbthroughout">'
                        + dbthroughout + '</td><td name="dbport">' + dbport + '</td><td name="dbip">' + network + '</td><td name="dbeip">' + dbeip + '</td><td class="time">' + createdate + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
    }

    function startDB(uuid) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-stopped");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('启动中');
        $.ajax({
            type: 'get',
            url: '/DatabaseAction',
            data: 'action=startup&uuid=' + uuid,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function destroyDB(uuid) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        var thisicon = thistr.find('[name="stateicon"]');
        thisicon.removeClass("icon-stopped");
        thisicon.removeClass("icon-running");
        thisicon.addClass('icon-process');
        thistr.find('[name="stateword"]').text('销毁中');
        $.ajax({
            type: 'get',
            url: '/DatabaseAction',
            data: 'action=destroy&uuid=' + uuid,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function shutdownDB(uuid, force) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-running");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('关机中');
        $.ajax({
            type: 'get',
            url: '/DatabaseAction',
            data: 'action=shutdown&uuid=' + uuid + '&force=' + force,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }

    function allDisable() {
        $("#start").attr("disabled", true).addClass('btn-forbidden');
        $("#close").attr("disabled", true).addClass('btn-forbidden');
        $("#delete").attr("disabled", true).addClass('btn-forbidden');
    }
});