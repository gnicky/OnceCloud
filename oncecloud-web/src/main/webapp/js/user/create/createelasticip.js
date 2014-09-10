$("#EipModalContainer").on("hidden", function () {
    $(this).removeData("modal");
    $(this).children().remove();
});

$("#slider").slider({
    min: 1,
    max: 10,
    step: 1,
    slide: function (event, ui) {
        $("#size").val(ui.value);
        var vsize = parseInt(ui.value);
        var price = (vsize * 0.03).toFixed(2);
        $("#price-hour").text(price);
    }
});

$("#size").on('focusout', function (event) {
    event.preventDefault();
    var vsize = 1;
    if (/^([1-9]{1})|([1]{1}[0]{1})$/.test(document.getElementById("size").value)) {
        vsize = parseInt(document.getElementById("size").value);
    } else {
        $("#size").val(1);
    }
    $("#slider").slider("value", vsize);
    var price = (vsize * 0.03).toFixed(2);
    $("#price-hour").text(price);
});

$("#count").on('focusout', function (event) {
    event.preventDefault();
    $("#hour-number").text($('#count').val().value);
});

$("#applyeipAction").on('click', function (event) {
    event.preventDefault();
    var valid = $("#apply-form").valid();
    if (valid) {
        var eipName = $('#eip_name').val();
        var eipCount = parseInt($('#count').val(), 10);
        var eipSize = parseInt($('#size').val(), 10);
        var eipTotalSize = eipCount * eipSize;
        $.ajax({
            type: 'get',
            url: '/EIPAction/Quota',
            data: {count: eipCount, size: eipTotalSize},
            dataType: 'text',
            success: function (msg) {
                if (msg != "ok") {
                    var quota = msg.split(":");
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + quota[0] + ']个公网IP配额，[' + quota[1] + ']&nbsp;Mbps带宽配额，您可以通过联系我们来申请扩大配额</div>',
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
                                    $('#EipModalContainer').modal('hide');
                                }
                            }
                        }
                    });
                } else {
                    for (var i = 0; i < eipCount; i++) {
                        applyEip(eipName, eipSize);
                    }
                    $('#EipModalContainer').modal('hide');
                }
            }
        });
    }
});

$("#apply-form").validate({
    rules: {
        eip_name: {
            required: true,
            maxlength: 20
        },
        count: {
            required: true,
            digits: true
        }
    },
    messages: {
        eip_name: {
            required: "<span class='unit'>名称不能为空</span>",
            maxlength: "<span class='unit'>名称不能超过20个字符</span>"
        },
        count: {
            required: "<span class='unit'>数量不能为空</span>",
            digits: "<span class='unit'>数量必须是整数</span>"
        }
    }
});

function applyEip(eipName, eipSize) {
    var eipUuid = uuid.v4();
    $.ajax({
        type: 'post',
        url: '/EIPAction/CreateEIP',
        data: {eipName: eipName, eipSize: eipSize, eipUuid: eipUuid},
        dataType: 'json',
        success: function (obj) {
            if (obj.result == true) {
                var eipIp = obj.eipIp;
                var eipId = obj.eipId;
                var createDate = obj.createDime;
                $("#tablebody").prepend('<tr eip="' + eipIp + '" eipId="' + eipId + '"><td class="rcheck"><input type="checkbox" name="eiprow"></td><td><a class="id">' + eipId + '</a></td><td name="eipname">'
                    + eipName + '</td><td>'
                    + eipIp + '</td><td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td><td vmuuid=""></td><td name="bandwidth">'
                    + eipSize + '</td><td>电信</td><td name="createdate" class="time">' + decodeURI(createDate) + '前</td></tr>');
            }
        }
    });
}