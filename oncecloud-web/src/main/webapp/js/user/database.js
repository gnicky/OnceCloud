reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getDatabaseList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="dbrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#start").addClass('btn-forbidden');
    $("#close").addClass('btn-forbidden');
    $("#delete").addClass('btn-forbidden');
}

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
        $("#delete").removeClass('btn-forbidden');
        if (running > 0 && stopped == 0) {
            $("#close").removeClass('btn-forbidden');
        } else if (running == 0 && stopped > 0) {
            $("#start").removeClass('btn-forbidden');
        }
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#DatabaseModalContainer').load($(this).attr('url'), '', function () {
        $('#DatabaseModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
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

function getDatabaseList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/DatabaseAction/DatabaseList',
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
            };
            $('#pageDivider').bootstrapPaginator(options);
            pageDisplayUpdate(page, totalp);
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var dbid = obj.dbid;
                var dbname = decodeURIComponent(obj.dbname);
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
                var createdate = decodeURIComponent(obj.createdate);
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
        }
    });
}