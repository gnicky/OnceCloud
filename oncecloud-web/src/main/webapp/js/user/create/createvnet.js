$(document).ready(function () {

    $('#createVnetAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        if (valid) {
            var name = $('#vn_name').val();
            var desc = $('#vn_desc').val();
            $.ajax({
                type: 'get',
                url: '/VnetAction/Quota',
                dataType: 'json',
                success: function (array) {
                    var obj = array[0];
                    if (obj.result == false) {
                        bootbox.dialog({
                            message: '<div class="alert alevn-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + obj.free + ']个路由器配额，您可以通过联系我们来申请扩大配额</div>',
                            title: "提示",
                            buttons: {
                                main: {
                                    label: "确定",
                                    className: "btn-primary",
                                    calrtack: function () {
                                    }
                                },
                                cancel: {
                                    label: "取消",
                                    className: "btn-default",
                                    calrtack: function () {
                                    }
                                }
                            }
                        });
                    } else {
                        var vnuuid = uuid.v4();
                        preCreateVnet(vnuuid, name, desc);
                    }
                    $('#VnetModalContainer').modal('hide');
                },
                error: function () {
                }
            });
        }
    });

    function preCreateVnet(uuid, name, desc) {
        var showid = "vn-" + uuid.substring(0, 8);
        var thistr = '<tr rowid="' + uuid + '" vnname="' + name + '"><td class="rcheck"><input type="checkbox" name="vnrow"></td>'
            + '<td><a class="id">' + showid + '</a></td><td>' + name + '</td><td></td>'
            + '<td class="time"><1分钟</td></tr>';
        $("#tablebody").prepend(thistr);
        createVnet(uuid, name, desc);
    }

    function createVnet(uuid, name, desc) {
        $.ajax({
            type: 'post',
            url: '/VnetAction/Create',
            data: {uuid:uuid, name:name, desc:desc},
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }


    $("#create-form").validate({
        rules: {
            vn_name: {
                required: true,
                minlength: 3,
                maxlength: 80
            },
            vn_desc: {
                required: true,
                minlength: 3,
                maxlength: 80
            }
        },
        messages: {
            vn_name: {
                required: "<span class='help'>名称不能为空</span>",
                minlength: "<span class='help'>名称不能少于3个字符</span>",
                maxlength: "<span class='help'>名称不能多于80个字符</span>"
            },
            vn_desc: {
                required: "<span class='help'>描述不能为空</span>",
                minlength: "<span class='help'>描述不能少于3个字符</span>",
                maxlength: "<span class='help'>描述不能多于80个字符</span>"
            }
        }
    });
});