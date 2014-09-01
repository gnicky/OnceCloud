reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getDCList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="dcrow"]').each(function () {
        $(this).attr("checked", false);
        $(this).change();
    });
}

function allDisable() {
    $("#delete").addClass('btn-forbidden');
    $("#update").addClass('btn-forbidden');
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var count = 0;
    $('input[name="dcrow"]:checked').each(function () {
        count++;
    });
    if (count > 0) {
        $("#delete").removeClass('btn-forbidden');
        if (count == 1) {
            $("#udpate").removeClass('btn-forbidden');
        }
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#DCModalContainer').attr('type', 'new');
    $('#DCModalContainer').load($(this).attr('url'), '', function () {
        $('#DCModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#update').on('click', function (event) {
    event.preventDefault();
    $('#DCModalContainer').attr('type', 'edit');
    $('#DCModalContainer').load($(this).attr('url'), '', function () {
        $('#DCModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getDCList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/DatacenterAction/DCList',
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
                    var dcname = decodeURI(obj.dcname);
                    var dcdesc = decodeURI(obj.dcdesc);
                    var dcid = obj.dcid;
                    var dclocation = decodeURI(obj.dclocation);
                    var createdate = obj.createdate;
                    var showid = "dc-" + dcid.substring(0, 8);
                    var totalcpu = obj.totalcpu;
                    var totalmem = Math.round(obj.totalmem / 1024);
                    var cpustr = totalcpu + "";
                    if (totalcpu != 0) {
                        cpustr = cpustr + "&nbsp;核";
                    }
                    var memstr = totalmem + "";
                    if (totalmem != 0) {
                        memstr = memstr + "&nbsp;GB";
                    }
                    var thistr = '<tr dcid="' + dcid + '" dcname="' + dcname + '" dclocation="' + dclocation + '" dcdesc="' + dcdesc + '"><td class="rcheck"><input type="checkbox" name="dcrow"></td>'
                        + '<td><a  class="id ">' + showid + '</a></td><td>' + dcname + '</td><td>' + dclocation + '</td><td>' + cpustr + '</td><td>'
                        + memstr + '</td><td class="time">' + createdate + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            }
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    var dcid = $(this).parent().parent().attr('dcid');
    $.ajax({
        type: 'get',
        url: '/DatacenterAction/Detail',
        data: {dcid: dcid},
        dataType: 'text',
        success: function (response) {
            window.location.href = $('#platformcontent').attr('platformBasePath') + "admin/detail/datacenterdetail.jsp";
        },
        error: function () {

        }
    });
});


$('#delete').on('click', function (event) {
    event.preventDefault();
    var boxes = document.getElementsByName("dcrow");
    var infoList = "";
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked == true) {
            infoList += "[" + $(boxes[i]).parent().parent().attr("dcname") + "]&nbsp;";
        }
    }
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除数据中心&nbsp;' + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    for (var i = 0; i < boxes.length; i++) {
                        if (boxes[i].checked == true) {
                            deleteDC($(boxes[i]).parent().parent().attr("dcid"), $(boxes[i]).parent().parent().attr("dcname"));
                        }
                    }
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

function deleteDC(dcid, dcname) {
    $.ajax({
        type: 'post',
        url: '/DatacenterAction',
        data: 'action=delete&dcid=' + dcid + '&dcname=' + dcname,
        dataType: 'json',
        success: function (array) {
            if (array.length == 1) {
                var result = array[0].result;
                if (result) {
                    var thistr = $("#tablebody").find('[dcid="' + dcid + '"]');
                    $(thistr).remove();
                }
            }
        }
    });
}

function removeAllCheck() {
    var boxes = document.getElementsByName("dcrow");
    for (var i = 0; i < boxes.length; i++) {
        boxes[i].checked = false;
        $(boxes[i]).change();
    }
}

