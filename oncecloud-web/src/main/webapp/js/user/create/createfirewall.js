$("#FirewallModalContainer").on("hidden", function () {
    $(this).removeData("modal");
    $(this).children().remove();
});

$("#createfirewallAction").on('click', function (event) {
    event.preventDefault();
    var valid = $("#createfw-form").valid();
    if (valid) {
        var firewallName = $('#firewall_name').val();
        $.ajax({
            type: 'get',
            url: '/FirewallAction/Quota',
            data: {count: 1},
            dataType: 'text',
            success: function (msg) {
                if (msg != "ok") {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + msg + ']个防火墙配额，您可以通过联系我们来申请扩大配额</div>',
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
                                    $("#FirewallModalContainer").modal('hide');
                                }
                            }
                        }
                    });
                } else {
                    createFirewall(firewallName);
                    $("#FirewallModalContainer").modal('hide');
                }
            }
        });
    }
});

$("#createfw-form").validate({
    rules: {
        firewall_name: {
            required: true,
            maxlength: 16
        }
    },
    messages: {
        firewall_name: {
            required: "<span class='unit'>防火墙名不能为空</span>",
            maxlength: "<span class='unit'>不能超过16个字符</span>"
        }
    }
});

function createFirewall(firewallName) {
    var firewallUuid = uuid.v4();
    $.ajax({
        type: 'post',
        url: '/FirewallAction/CreateFirewall',
        data: {firewallName: firewallName, firewallUuuid: firewallUuid},
        dataType: 'json',
        success: function (obj) {
            var showid = '<a>fw-' + firewallUuid.substring(0, 8) + '</a>';
            if (obj.isSuccess) {
                $("#tablebody").prepend('<tr firewallid="'
                    + firewallUuid + '"><td class="rcheck"><input type="checkbox" name="firewallrow"></td><td class="firewallid" def="0">' + showid + '</td><td>'
                    + firewallName + '</td><td>0</td><td>' + obj.createDate + '</td></tr>');
            }
        }
    });
}