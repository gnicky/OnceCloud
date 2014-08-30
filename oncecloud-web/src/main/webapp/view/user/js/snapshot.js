$(document).ready(function () {

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = $('#limit').val();
            var search = $('#search').val();
            getSnapshotList(page, limit, search);
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
    getSnapshotList(1, 10, "");

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        reloadList();
    });

    $('#search').on('focusout', function () {
        search();
    });

    $('#search').keypress(function (e) {
        var key = e.which;
        if (key == 13) {
            search();
        }
    });

    $('#limit').on('focusout', function () {
        var limit = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $("#limit").val(10);
        }
        reloadList();
    });

    function reloadList() {
        var limit = $('#limit').val();
        var search = $('#search').val();
        getSnapshotList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    function getSnapshotList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/SnapshotAction',
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
                    var resourceUuid = obj.resourceUuid;
                    var resourceType = obj.resourceType;
                    var resourceName = decodeURI(obj.resourceName);
                    var snapshotCount = obj.snapshotCount;
                    var snapshotSize = obj.snapshotSize.toFixed(2);
                    var backupDate = obj.backupDate;
                    var showname;
                    if (resourceType == "instance") {
                        showname = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;主机';
                    } else {
                        showname = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;硬盘';
                    }
                    var showuuid = "bk-" + resourceUuid.substring(0, 8);
                    var thistr = '<tr rsuuid="' + resourceUuid + '" rstype="' + resourceType
                        + '" rsname="' + resourceName + '"><td class="rcheck"><input type="checkbox" name="snapshotrow"></td><td><a class="viewDetail" href="javascript:void(0)">' + showuuid + '</a></td><td>'
                        + resourceName + '</td><td state="on"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td><td>' + showname + '</td><td name="size">' + snapshotSize
                        + '</td><td name="node">' + snapshotCount
                        + '</td><td name="backupdate" class="time">' + decodeURI(backupDate)
                        + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
    }

    $('#delete').on('click', function (event) {
        event.preventDefault();
        showbox(0);
    });

    function showbox(type) {
        var infoList = getInfoList();
        var infoArray = new Array("删除备份链");
        var showMessage = '';
        var showTitle = '';
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
            + infoArray[type] + '&nbsp;' + infoList + '?</div>';
        showTitle = '提示';
        bootbox.dialog({
            className: "oc-bootbox",
            message: showMessage,
            title: showTitle,
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        $('input[name="snapshotrow"]:checked').each(function () {
                            var rsuuid = $(this).parent().parent().attr("rsuuid");
                            var rstype = $(this).parent().parent().attr("rstype");
                            if (type == 0) {
                                deleteSnapshot(rsuuid, rstype);
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

    function getInfoList() {
        var infoList = "";
        $('input[name="snapshotrow"]:checked').each(function () {
            infoList += "[bk-" + $(this).parent().parent().attr("rsuuid").substring(0, 8) + "]&nbsp;";
        });
        return infoList;
    }

    function deleteSnapshot(rsuuid, rstype) {
        $.ajax({
            type: 'get',
            url: '/SnapshotAction',
            data: "action=delete&rsuuid=" + rsuuid + "&rstype=" + rstype,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    $("#tablebody").find('[rsuuid="' + rsuuid + '"]').remove();
                }
            },
            error: function () {
            }
        });
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        $('#delete').attr('disabled', true).addClass('btn-forbidden');
        var total = 0;
        $('input[name="snapshotrow"]:checked').each(function () {
            total++;
        });
        if (total != 0) {
            $("#delete").attr('disabled', false).removeClass('btn-forbidden');
        }
    });

    $('#tablebody').on('click', '.viewDetail', function (event) {
        var resourceUuid = $(this).parent().parent().attr('rsuuid');
        var resourceType = $(this).parent().parent().attr('rstype');
        var resourceName = $(this).parent().parent().attr('rsname');
        var basePath = $("#platformcontent").attr("platformBasePath");
        $.ajax({
            type: 'post',
            url: '/SnapshotAction',
            data: 'action=detail&resourceUuid=' + resourceUuid + '&resourceType=' + resourceType + '&resourceName=' + resourceName,
            dataType: 'text',
            success: function (response) {
                window.location.href = basePath + "user/detail/snapshotdetail.jsp";
            },
            error: function () {
            }
        });
    });

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }

    function removeAllCheck() {
        $('input[name="snapshotrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});