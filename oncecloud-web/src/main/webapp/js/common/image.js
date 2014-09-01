reloadList(1);

function reloadList(page) {
    var limit = $("#limit").val();
    var search = $('#search').val();
    var type = $('.once-tab').find('.active').attr("type");
    getImageList(page, limit, search, type);
    if (page == 1) {
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="imagerow"]').each(function () {
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
    var count = 0;
    $('input[name="imagerow"]:checked').each(function () {
        count++;
    });
    if (count > 0) {
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
    $('#ImageModalContainer').load($(this).attr('url'), '', function () {
        $('#ImageModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getImageList(page, limit, search, type) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/ImageAction/ImageList',
        data: { page: page, limit: limit, search: search, type: type},
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
                    var imageName = decodeURI(obj.imagename);
                    var imageUId = obj.imageid;
                    var imageSize = obj.imagesize;
                    var imageplatform = decodeURI(obj.imageplatform);
                    var imagestr = obj.imagestr;
                    var createDate = obj.createDate;
                    var showid = "img-" + imageUId.substring(0, 8);
                    var type = $('.once-tab').find('.active').attr("type");
                    $('#image-area').text("可见范围");
                    var typeStr = "公有";
                    if (type == "user") {
                        if (userid == 1) {
                            typeStr = obj.imageuser;
                            $('#image-area').text("所属用户");
                        } else {
                            typeStr = "私有";
                        }
                    }
                    var stateStr = '<td><span class="icon-status icon-running" name="stateicon">'
                        + '</span><span name="stateword">可用</span></td>';
                    var mytr = '<tr imageUId="' + imageUId + '" imageName="' + imageName
                        + '" imageType="' + type + '"><td class=“rcheck"><input type="checkbox" name="imagerow"></td>'
                        + '<td><a class="id">' + showid + '</a></td><td><a>' + imageName
                        + '</a></td><td>' + imageSize + '</td><td>' + imageplatform + '</td>' + stateStr
                        + '<td>' + typeStr + '</td><td class="time">' + createDate + '</td></tr>';
                    tableStr = tableStr + mytr;
                }
                $('#tablebody').html(tableStr);
            }
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    var imageuuid = $(this).parent().parent().attr('imageUId');
    var imagetype = $(this).parent().parent().attr('imageType');
    var form = $("<form></form>");
    form.attr("action","/image/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="imageuuid" value="' + imageuuid + '" />');
    var input1 = $('<input type="text" name="imagetype" value="' + imagetype + '" />');
    form.append(input);
    form.append(input1);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

$('#delete').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    $('input[name="imagerow"]:checked').each(function () {
        infoList += "[" + $(this).parent().parent().attr("imageName") + "]&nbsp;";
    });
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除映像&nbsp;' + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="imagerow"]:checked').each(function () {
                        deleteImage($(this).parent().parent().attr("imageUId"), $(this).parent().parent().attr("imageName"));
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

function deleteImage(imageId, imageName) {
    $.ajax({
        type: 'post',
        url: '/ImageAction/Delete',
        data: {imageId: imageId, imageName: imageName},
        dataType: 'json',
        success: function (obj) {
            if (obj.result) {
                var thistr = $("#tablebody").find('[imageUId="' + imageId + '"]');
                $(thistr).remove();
            }
        }
    });
}