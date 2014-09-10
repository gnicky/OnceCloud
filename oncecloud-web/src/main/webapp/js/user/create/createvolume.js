$("#VolumeModalContainer").on("hidden", function () {
    $(this).removeData("modal");
    $(this).children().remove();
});

$("#slider").slider({
    min: 10,
    max: 500,
    step: 10,
    slide: function (event, ui) {
        $("#size").val(ui.value);
        var vsize = parseInt(ui.value);
        var price = (vsize / 10 * 0.02).toFixed(2);
        $("#price-hour").text(price);
    }
});

$("#size").on('focusout', function (event) {
    event.preventDefault();
    var vsize = 10;
    if (/^[1-5]{0,1}[1-9]{1}[0]{1}$/.test($('#size').val())) {
        vsize = parseInt($('#size').value);
    } else {
        $("#size").val(10);
    }
    $("#slider").slider("value", vsize);
    var price = (vsize / 10 * 0.02).toFixed(2);
    $("#price-hour").text(price);
});

$("#count").on('focusout', function (event) {
    event.preventDefault();
    $("#hour-number").text($('#count').val());
});

$("#createinfo-form").validate({
    rules: {
        volume_name: {
            required: true,
            maxlength: 20,
            legal: true
        },
        count: {
            required: true,
            digits: true
        }
    },
    messages: {
        volume_name: {
            required: "<span class='unit'>名称不能为空</span>",
            maxlength: "<span class='unit'>名称不能超过20个字符</span>",
            legal: "<span class='unit'>名称包含非法字符</span>"
        },
        count: {
            required: "<span class='unit'>数量不能为空</span>",
            digits: "<span class='unit'>数量必须是整数</span>"
        }
    }
});

$("#createvolumeAction").on('click', function (event) {
    event.preventDefault();
    var valid = $("#createinfo-form").valid();
    if (valid) {
        var volumeName = $('#volume_name').val();
        var volumeCount = parseInt($('#count').val(), 10);
        var volumeSize = parseInt($('#size').val(), 10);
        var volumeTotalSize = volumeSize * volumeCount;
        $.ajax({
            type: 'post',
            url: '/VolumeAction/Quota',
            data: {count: volumeCount, size: volumeTotalSize},
            dataType: 'text',
            success: function (msg) {
                if (msg != "ok") {
                    var quota = msg.split(":");
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + quota[0] + ']个硬盘配额，[' + quota[1] + ']&nbsp;GB硬盘容量配额，您可以通过联系我们来申请扩大配额</div>',
                        title: "提示",
                        buttons: {
                            main: {
                                label: "确定",
                                className: "btn-primary",
                                callback: function () {
                                }
                            },
                            cancel: {
                                label: "取消",
                                className: "btn-default",
                                callback: function () {
                                    $('#VolumeModalContainer').modal('hide');
                                }
                            }
                        }
                    });
                } else {
                    for (var i = 0; i < volumeCount; i++) {
                        var volumeuuid = uuid.v4();
                        createVolume(volumeuuid, volumeName, volumeSize);
                    }
                    $('#VolumeModalContainer').modal('hide');
                }
            }
        });
    }
});

function createVolume(volumeuuid, volumeName, volumeSize) {
    var showid = "vol-" + volumeuuid.substring(0, 8);
    var basePath = $('#platformcontent').attr('platformBasePath');
    var backupStr = '<a class="glyphicon glyphicon-camera backup" url="' + basePath + 'user/create/createsnapshot.jsp?rsid=' + volumeuuid + '&rstype=instance&rsname=' + volumeName + '"></a>';
    var showstr = "<a class='id'>" + showid + '</a>';
    $("#tablebody").prepend('<tr rowid="' + volumeuuid + '"><td class="rcheck"><input type="checkbox" name="volumerow"></td><td>'
        + showstr + '</td><td name="volumename">' + volumeName + '</td><td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td><td vmuuid=""></td><td name="size">'
        + volumeSize.toFixed(2) + '</th><td name="backuptime">' + backupStr + '</td><td name="createtime" class="time"><1分钟</td></tr>');
    $.ajax({
        type: 'post',
        url: '/VolumeAction/CreateVolume',
        data: {volumeUuid: volumeuuid, volumeName: volumeName, volumeSize: volumeSize},
        dataType: 'json',
        success: function (obj) {
        }
    });
}