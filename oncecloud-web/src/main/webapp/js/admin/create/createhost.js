$(document).ready(function () {
    loadRackList();
    fillBlank();
    function loadRackList() {
        $.ajax({
            type: 'get',
            async: false,
            url: '/RackAction/AllList',
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    $("#rack_location").html("");
                    $.each(array, function (index, item) {
                        $("#rack_location").append("<option value='" + item.rackid + "'>" + decodeURI(item.rackname) + "</option>");
                    });
                }
                else {
                    $('#rack_location').html('<option>请先添加机架</option>');
                    $('#rack_location').attr('disabled', true).addClass('oc-disable');
                    $('#addServerAction').attr('disabled', true);///没有机架，请先去添加机架，
                }
            },
            error: function () {
                $('#rack_location').html('<option>请先添加机架</option>');
                $('#rack_location').attr('disabled', true).addClass('oc-disable');
                $('#addServerAction').attr('disabled', true);
            }
        });
    }

    function fillBlank() {
        var type = $('#ServerModalContainer').attr('type');
        if ('edit' == type) {
            $('#modaltitle').html('修改服务器<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');

            var hostid = '';
            var hostip = '';
            var hostname = '';
            var rackUuid = '';
            var hostdesc = '';

            $('input[name="hostrow"]:checked').each(function () {
                hostid = $(this).parent().parent().attr("hostid");
                hostip = $(this).parent().parent().attr("hostip");
                hostname = $(this).parent().parent().attr("hostname");
                hostdesc = $(this).parent().parent().attr("hostdesc");
                rackUuid = $(this).parent().parent().attr("rackid");

            });
            $('#modalcontent').attr('hostid', hostid);

            $('#server_addr').val(hostip);
            $('#server_addr').attr('disabled', true).addClass('oc-disable');
            $('#pwditem').hide();
            $('#server_name').val(hostname);
            $('#server_desc').val(hostdesc);
            if ('' != rackUuid) {
                $('#rack_location').val(rackUuid);
            }
        }
    }

    $('#addServerAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();

        var errorLen = $('#server_addr').parent().find('.oc-error').length;
        var errorLenrack = $('#rack_location').parent().find('.oc-error').length;
        if (valid && errorLen == 0 && errorLenrack == 0) {
            var serverName = document.getElementById("server_name").value;
            var serverDesc = document.getElementById("server_desc").value;
            var serverPwd = document.getElementById("server_pwd").value;
            var serverIp = document.getElementById("server_addr").value;
            var rackUuid = $("#rack_location").val();
            var rackName = $("#rack_location").find("option:selected").text();

            var type = $('#ServerModalContainer').attr('type');
            if ('new' == type) {
                $.ajax({
                    type: 'post',
                    url: '/HostAction/Create',
                    data : {
						hostname : serverName,
						hostpwd : serverPwd,
						hostdesc : serverDesc,
						hostip : serverIp,
						rackUuid : rackUuid,
						rackName : rackName
					},
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            var obj = array[0];
                            var hostname = decodeURI(obj.hostname);
                            var hostid = obj.hostid;
                            var hostip = obj.hostip;
                            var createdate = obj.createdate;
                            var hostcpu = obj.hostcpu;
                            var hostmem = obj.hostmem;
                            var rackUuid = obj.rackUuid;
                            var rackName = decodeURI(obj.rackName);
                            var showid = "host-" + hostid.substring(0, 8);
                            var srsize = obj.srsize;
                            var mytr = '<tr hostid="' + hostid + '" hostname="' + hostname + '" hostip="' + hostip + '" hostdesc="' + hostdesc + '" rackid="' + rackUuid + '"><td class="rcheck"><input type="checkbox" name="hostrow"></td>'
                                + '<td><a class="id">' + showid + '</a></td><td>' + hostname + '</td><td>' + hostip + '</td><td>' + hostcpu + '&nbsp;核</td>'
                                + '<td>' + hostmem + '&nbsp;MB</td><td state="unload"></td><td><a>' + rackName + '</a></td><td><a class="srdetail" size=' + srsize + '><span class="glyphicon glyphicon-hdd"></span>&nbsp;&nbsp;' + srsize + '</a></td><td class="time">' + createdate + '</td></tr>';
                            $("#tablebody").prepend(mytr);
                            $('#ServerModalContainer').modal('hide');
                        } else {
                            bootbox.dialog({
                                className: "bootbox-large",
                                message: '<div class="alert alert-warning" style="margin:10px; color:#c09853"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;添加服务器失败，可能有下面几个原因:<br/><br/>1. 目标地址无法访问或未安装虚拟化服务；<br/>2. 服务器密码输入错误；<br/>3. 目标服务器地址已存在。</div>',
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
                                            $('#ServerModalContainer').modal('hide');
                                        }
                                    }
                                }
                            });
                        }
                    },
                    error: function () {

                    }
                });
            } else if ('edit' == type) {
                var hostid = $('#modalcontent').attr('hostid');
                $.ajax({
                    type: 'post',
                    url: '/HostAction/Update',
                    data: {hostid:hostid, hostname:serverName, hostdesc:serverDesc,rackUuid:rackUuid},
                    dataType: 'text',
                    success: function () {
                        var thistr = $("#tablebody").find('[hostid="' + hostid + '"]');
                        thistr.attr('hostname', serverName);
                        thistr.attr('hostdesc', serverDesc);
                        thistr.attr('rackid', rackUuid);

                        thistr.children('td').eq(2).html(serverName);
                        thistr.children('td').eq(7).html('<a>' + rackName + '</a>');

                    },
                    error: function () {

                    }

                });
                removeAllCheck();
                $('#ServerModalContainer').modal('hide');
            }
        }
    });

    $('#server_addr').on('focusout', function () {
        addressValid();
    });

    function addressValid() {
        $('#server_addr').parent().find('.oc-error').remove();
        var ip = $('#server_addr').val();

        if (ip != "") {
            $.ajax({
                type: 'get',
                async: true,
                url: '/HostAction/QueryAddress',
                data: {address:ip},
                dataType: 'json',
                success: function (array) {
                    if (array.length == 1) {
                        var exist = array[0].exist;
                        if (exist == true) {
                            $('#server_addr').parent().append('<label class="oc-error"><span class="help">服务器地址已存在</span></label>');
                            return false;
                        } else {
                            return true;
                        }
                    }
                },
                error: function () {

                }
            });
        }
    }


    $("#create-form").validate({
        rules: {
            server_name: {
                required: true,
                minlength: 3
            },
            server_desc: {
                required: true,
                minlength: 3,
                maxlength: 80
            },
            server_pwd: {
                required: true
            },
            server_addr: {
                required: true,
                ip: true,
                minlength: 7,
                maxlength: 15
            }
        },
        messages: {
            server_name: {
                required: "<span class='help'>服务器名称不能为空</span>",
                minlength: "<span class='help'>服务器名称不能少于3个字符</span>"
            },
            server_desc: {
                required: "<span class='help'>服务器描述不能为空</span>",
                minlength: "<span class='help'>服务器描述不能少于3个字符</span>",
                maxlength: "<span class='help'>服务器描述不能超过80个字符</span>"
            },
            server_pwd: {
                required: "<span class='help'>密码不能为空</span>"
            },
            server_addr: {
                required: "<span class='help'>服务器地址不能为空</span>",
                ip: "<span class='help'>服务器地址格式不正确</span>",
                minlength: "<span class='help'>服务器地址不能少于7个字符</span>",
                maxlength: "<span class='help'>服务器地址不能超过15个字符</span>"
            }
        }
    });
});