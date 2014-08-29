var options = {
    bootstrapMajorVersion: 3,
    currentPage: 1,
    totalPages: 1,
    numberOfPages: 0,
    onPageClicked: function (e, originalEvent, type, page) {
        reloadList(page);
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
reloadList(1);

$('.btn-refresh').on('click', function (event) {
    reloadList(1);
});

$('#limit').on('focusout', function () {
    var limit = $("#limit").val();
    var reg = /^[0-9]*[1-9][0-9]*$/;
    if (!reg.test(limit)) {
        $("#limit").val(10);
    }
    reloadList(1);
});

$('#search').on('focusout', function () {
    reloadList(1);
});

$('#search').keypress(function (e) {
    var key = e.which;
    if (key == 13) {
        reloadList(1);
    }
});

function pageDisplayUpdate(current, total) {
    $('#currentP').html(current);
    $('#totalP').html(total);
}

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getRackList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#RackModalContainer').attr('type', 'new');
    $('#RackModalContainer').load($(this).attr('url'), '', function () {
        $('#RackModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#update').on('click', function (event) {
    event.preventDefault();
    $('#RackModalContainer').attr('type', 'edit');
    $('#RackModalContainer').load($(this).attr('url'), '', function () {
        $('#RackModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getRackList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/RackAction',
        data: {action: "getlist", page: page, limitnum: limit, search: search},
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
                    var rackname = decodeURI(obj.rackname);
                    var rackid = obj.rackid;
                    var rackdesc = decodeURI(obj.rackdesc);
                    var racklocation = decodeURI(obj.dcname);
                    var createdate = obj.createdate;
                    var dcid = obj.dcid;

                    var showid = "rack-" + rackid.substring(0, 8);
                    var thistr = '<tr rowid="' + rackid + '" rackname="' + rackname + '" rackdesc="' + rackdesc + '" dcid="' + dcid + '"><td class="rcheck"><input type="checkbox" name="rackrow"></td>'
                        + '<td><a class="id">' + showid + '</a></td><td>' + rackname + '</td><td>' + racklocation + '</td><td class="time">' + createdate + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            }
        },
        error: function () {
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    var rackid = $(this).parent().parent().attr('rowid');
    $.ajax({
        type: 'get',
        url: '/RackAction',
        data: {action: "detail", rackid: rackid},
        dataType: 'text',
        success: function (response) {
            window.location.href = $('#platformcontent').attr('platformBasePath') + "admin/topology.jsp";
        },
        error: function () {
        }
    });
});


$('#delete').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    $('input[name="rackrow"]:checked').each(function () {
        infoList += "[" + $(this).parent().parent().attr("rackname") + "]&nbsp;";
    });
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除机架&nbsp;' + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="rackrow"]:checked').each(function () {
                        deleteRack($(this).parent().parent().attr("rowid"), $(this).parent().parent().attr("rackname"));
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

function deleteRack(rackid, rackname) {
    $.ajax({
        type: 'post',
        url: '/RackAction',
        data: {action: "delete", rackid: rackid, rackname: rackname},
        dataType: 'json',
        success: function (array) {
        },
        error: function () {
        }
    });
}

function removeAllCheck() {
    $('input[name="rackrow"]').each(function () {
        $(this).attr("checked", false);
        $(this).change();
    });
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var count = 0;
    $('input[name="rackrow"]:checked').each(function () {
        count++;
    });
    if (count > 0) {
        $("#delete").removeClass('btn-forbidden').attr('disabled', false);
        if (count == 1) {
            $('#update').removeClass('btn-forbidden').attr('disabled', false);
        }
    }
});

function allDisable() {
    $("#delete").addClass('btn-forbidden').attr('disabled', true);
    $("#update").addClass('btn-forbidden').attr('disabled', true);
}