$(document).ready(function () {
    loadVmList();

    function loadVmList() {
        $.ajax({
            type: 'get',
            async: false,
            url: '/VMAction',
            data: 'action=simplelist',
            dataType: 'json',
            success: function (array) {
                var vmlistHtml = '';
                if (0 == array.length) {
                    vmlistHtml = '<option>请先创建一台主机</option>';
                    $('#be_vm').attr('disabled', true).addClass('oc-disable');
                    $('#be_vm').html(vmlistHtml);
                    $('#createBEAction').attr('disabled', true);
                    $('#createBEAction').addClass('btn-forbidden');
                } else {
                    $('#be_vm').attr('disabled', false);
                    $('#createBEAction').attr('disabled', false).removeClass('oc-disable');
                    $('#createBEAction').removeClass('btn-forbidden');
                    for (var i = 0; i < array.length; i++) {
                        vmlistHtml = vmlistHtml + '<option value="' + array[i].vmid + '&' + array[i].vmip + '">i-'
                            + array[i].vmid.substring(0, 8) + '&nbsp;(' + decodeURI(array[i].vmname) + ')</option>';
                    }
                    $('#be_vm').html(vmlistHtml);
                }
            },
            error: function () {
            }
        });
    }

    function checkBEPort(beuuid, port) {
        var result = false;
        $.ajax({
            type: 'post',
            async: false,
            url: '/LBAction',
            data: 'action=checkback&port=' + port + '&beuuid=' + beuuid,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    result = true;
                }
            },
            error: function () {
            }
        });
        return result;
    }

    $('#createBEAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#editBackend-form").valid();
        var lbUuid = $("#platformcontent").attr("lbUuid");
        if (valid) {
            var feuuid = $('#backend').attr('foreuuid');
            var name = $('#be_name').val();
            var network = $('#be_network').val();
            var port = $('#be_port').val();
            var weight = $('#be_weight').val();
            var vmstring = $('#be_vm').val();
            if (checkBEPort(vmstring, port)) {
                var vm = new Array();
                vm = vmstring.split("&");
                var beuuid = uuid.v4();
                $.ajax({
                    type: 'post',
                    url: '/LBAction',
                    data: 'action=createback&name=' + name + '&backuuid=' + beuuid + '&vmuuid=' + vm[0]
                        + '&vmip=' + vm[1] + '&port=' + port + '&weight=' + weight + '&feuuid=' + feuuid + "&lbuuid=" + lbUuid,
                    dataType: 'text',
                    success: function () {
                        window.location = window.location.href;
                    },
                    error: function () {
                    }
                });
            } else {
                var showMessage = '<div class="alert alert-info" style="margin:10px">'
                    + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;后端服务&nbsp;[i-'
                    + vmstring.substring(0, 8) + ':' + port + ']&nbsp;重复，不允许重复添加';
                bootbox.dialog({
                    className: "oc-bootbox",
                    message: showMessage,
                    title: "后端服务重复",
                    buttons: {
                        main: {
                            label: "确定",
                            className: "btn-primary",
                            callback: function () {
                            }
                        }
                    }
                });
            }
        }
    });

    $("#editBackend-form").validate({
        rules: {
            be_weight: {
                required: true,
                digits: true,
                range: [1, 100]
            },
            be_port: {
                required: true,
                digits: true,
                range: [1, 65535]
            },
            be_name: {
                required: true,
                minlength: 3,
                maxlength: 80
            }
        },
        messages: {
            be_weight: {
                required: "<span class='unit'>权重不能为空</span>",
                digits: "<span class='unit'>权重必须是1~100内的整数</span>",
                range: "<span class='unit'>权重必须是1~100内的整数</span>"
            },
            be_port: {
                required: "<span class='unit'>端口不能为空</span>",
                digits: "<span class='unit'>端口必须是1~65535内的整数</span>",
                range: "<span class='unit'>端口必须是1~65535内的整数</span>"
            },
            be_name: {
                required: "<span class='help'>名称不能为空</span>",
                minlength: "<span class='help'>名称不能少于3个字符</span>",
                maxlength: "<span class='help'>名称不能多于80个字符</span>"
            }
        }
    });
});