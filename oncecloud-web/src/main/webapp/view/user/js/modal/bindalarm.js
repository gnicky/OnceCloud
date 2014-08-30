$(document).ready(function () {
    $("#ResourceModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
        removeAllCheck();
    });

    $("#resourceRemove").on("click", function () {
        removeAllCheck();
    });

    var rsta = [];

    $('#bind2resource').on('click', function (event) {
        event.preventDefault();
        var alarmUuid;
        $('input[name="alrow"]:checked').each(function () {
            alarmUuid = $(this).parent().parent().attr('rowid');
        });
        if (alarmUuid == undefined) {
            alarmUuid = $('#platformcontent').attr("alarmUuid");
        }
        var vmboxes = document.getElementsByName("image-item");
        var rsuuidStr = '[';
        var flag = 0;
        var type = "";
        rsta.data = [];
        for (var i = 0; i < vmboxes.length; i++) {
            if ($(vmboxes[i]).hasClass('selected')) {
                var data = {};
                flag++;
                var vmuuid = $(vmboxes[i]).attr("vmuuid");
                if (flag == 1) {
                    rsuuidStr = rsuuidStr + vmuuid;
                } else {
                    rsuuidStr = rsuuidStr + ',' + vmuuid;
                }
                type = vmuuid.substring(vmuuid.length - 1, vmuuid.length);
                data.name = $(vmboxes[i]).attr("vmname");
                data.uuid = vmuuid.substring(0, vmuuid.length - 1);
                rsta.data.push(data);
            }
        }
        rsuuidStr = rsuuidStr + ']';
        if (type == "i") {
            rsta.type = 0;
        } else if (type == "p") {
            rsta.type = 1;
        } else if (type == "r") {
            rsta.type = 2;
        } else {
            rsta.type = 3;
        }
        bindvn(alarmUuid, rsuuidStr);
        removeAllCheck();
    });

    $('#cancelbind').on('click', function (event) {
        event.preventDefault();
        removeAllCheck();
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            getInstanceList(page, 5, "");
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
    getInstanceList(1, 5, "");

    function getInstanceList(page, limit, search) {
        var alarmUuid;
        $('input[name="alrow"]:checked').each(function () {
            alarmUuid = $(this).parent().parent().attr('rowid');
        });
        if (alarmUuid == undefined) {
            alarmUuid = $('#platformcontent').attr("alarmUuid");
        }
        $('#instancelist').html("");
        $.ajax({
            type: 'get',
            url: '/AlarmAction',
            data: 'action=getResource&page=' + page + '&limit=' + limit + '&search=' + search + '&alarmUuid=' + alarmUuid,
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
                pageDisplayUpdate(page, totalp);
                var tableStr = "";
                if (0 != totalnum) {
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var vmuuid = obj.resourceUuid;
                        var vmName = decodeURI(obj.resourceName);
                        tableStr = tableStr + '<div name="image-item" class="image-item" vmname="' + vmName + '" vmuuid="'
                            + vmuuid + '"><div class="image-left">'
                            + vmName + '</div>ID:&nbsp;&nbsp;' + obj.resourceUuidshow + '</div>';
                    }
                } else {
                    $('#alert').html('没有可选择的资源');
                }
                $('#instancelist').html(tableStr);
            },
            error: function () {
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
            $('#bind2resource').attr('disabled', true);
        } else {
            $('#bind2resource').attr('disabled', false);
        }
    });

    function bindvn(alarmUuid, rsuuidStr) {
        $.ajax({
            type: 'get',
            url: '/AlarmAction',
            data: 'action=addresource&alarmUuid=' + alarmUuid + '&rsuuidStr=' + rsuuidStr,
            dataType: 'text',
            success: function (response) {
                bootbox.dialog({
                    message: '<div class="alert alert-success" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;添加资源成功</div>',
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
                if ($("#rsTable").html() == '<span class="none">尚无资源</span>') {
                    $("#rsTable").html("");
                }
                $.each(rsta.data, function (index, rs) {
                    $("#rsTable").append('<div style="padding-left:102px"><a>' + rs.name +
                        '</a><a id="remove-resource" rsUuid="' + rs.uuid + '" rsType="' + rsta.type + '"><span class="glyphicon glyphicon-remove delete-resource"></span></a></div>');
                });

            },
            error: function () {
            }
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentPS').html(current);
        $('#totalPS').html(total);
    }

    function removeAllCheck() {
        $('input[name="alrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});