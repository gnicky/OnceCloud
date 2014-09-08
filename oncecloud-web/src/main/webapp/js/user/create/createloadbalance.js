
    $('#createLBAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        if (valid) {
            var name = $('#lb_name').val();
            var capacity = $('input[name="lb_type"]:checked').val();
            $.ajax({
                type: 'post',
                url: '/LBAction/Quota',
                dataType: 'json',
                success: function (array) {
                    var obj = array[0];
                    if (obj.result == false) {
                        bootbox.dialog({
                            message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + obj.free + ']个负载均衡配额，您可以通过联系我们来申请扩大配额</div>',
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
                                    }
                                }
                            }
                        });
                    } else {
                        var lbuuid = uuid.v4();
                        preCreateLB(lbuuid, name, capacity);
                    }
                    $('#LBModalContainer').modal('hide');
                },
                error: function () {
                }
            });
        }
    });

    function preCreateLB(lbuuid, name, capacity) {
        var showid = "lb-" + lbuuid.substring(0, 8);
        var stateStr = '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
        var thistr = '<tr rowid="' + lbuuid + '" lbname="' + name + '"><td class="rcheck"><input type="checkbox" name="lbrow"></td>'
            + '<td><a class="id">' + showid + '</a></td><td>' + name + '</td>' + stateStr + '<td name="eip"></td><td name="capacity">' + capacity + '</td>'
            + '<td class="time"><1分钟</td></tr>';
        $("#tablebody").prepend(thistr);
        createLB(lbuuid, name, capacity);
    }

    function createLB(lbuuid, name, capacity) {
        $.ajax({
            type: 'post',
            url: '/LBAction/Create',
            data:{uuid:lbuuid,name:name,capacity:capacity},
            dataType: 'json',
        });
    }

    $("#create-form").validate({
        rules: {
            lb_name: {
                required: true,
                minlength: 3,
                maxlength: 80
            }
        },
        messages: {
            lb_name: {
                required: "<span class='help'>名称不能为空</span>",
                minlength: "<span class='help'>名称不能少于3个字符</span>",
                maxlength: "<span class='help'>名称不能多于80个字符</span>"
            }
        }
    });