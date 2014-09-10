$(document).ready(function () {
    loadDCList();
    fillBlank();

    function loadDCList() {
        $.ajax({
            type: 'get',
            async: false,
            url: '/DatacenterAction/AllList',
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    $("#rack_location").html("");
                    $.each(array, function (index, item) {
                        $("#rack_location").append("<option value='" + item.dcid + "'>" + decodeURI(item.dcname) + "</option>");
                    });
                }
                else {
                    $('#rack_location').html('<option>请先添加数据中心</option>');
                    $('#rack_location').attr('disabled', true).addClass('oc-disable');
                    $('#createRackAction').attr('disabled', true);
                }
            },
            error: function () {
                $('#rack_location').html('<option>请先添加数据中心</option>');
                $('#rack_location').attr('disabled', true).addClass('oc-disable');
                $('#createRackAction').attr('disabled', true);
            }
        });
    }

    function fillBlank() {
        var type = $('#RackModalContainer').attr('type');
        if ('edit' == type) {
            $('#modaltitle').html('修改机架<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');

            var rackid = '';
            var rackname = '';
            var rackdesc = '';
            var dcuuid = '';

            $('input[name="rackrow"]:checked').each(function () {
                rackid = $(this).parent().parent().attr("rowid");
                rackname = $(this).parent().parent().attr("rackname");
                rackdesc = $(this).parent().parent().attr("rackdesc");
                dcuuid = $(this).parent().parent().attr("dcid");
            });
            $('#modalcontent').attr('rackid', rackid);

            $('#rack_name').val(rackname);
            if ('' != dcuuid) {
                $('#rack_location').val(dcuuid);
            }
            $('#rack_desc').val(rackdesc);
        }
    }

    $('#createRackAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        var errorLen = $('#rack_location').parent().find('.oc-error').length;
        if (valid && errorLen == 0) {
            var rackName = document.getElementById("rack_name").value;
            var rackLocation = $("#rack_location").find("option:selected").text();
            var dcid = $("#rack_location").val();

            var rackDesc = document.getElementById("rack_desc").value;
            var type = $('#RackModalContainer').attr('type');

            if ('new' == type) {
                $.ajax({
                    type: 'post',
                    url: '/RackAction/Create',
                    data: {rackname:rackName, rackdesc:rackDesc, dcid:dcid},
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            var obj = array[0];
                            var rackname = decodeURI(obj.rackname);
                            var rackdesc = decodeURI(obj.rackdesc);
                            var rackid = obj.rackid;
                            var dcname = decodeURI(obj.dcname);
                            var createdate = obj.createdate;
                            var dcid = obj.dcid;
                            var showid = "rack-" + rackid.substring(0, 8);
                            var mytr = '<tr rowid="' + rackid + '" rackname="' + rackname + '" rackdesc="' + rackdesc + '" dcid="' + dcid + '"><td class="rcheck"><input type="checkbox" name="rackrow"></td>'
                                + '<td><a class="id">' + showid + '</a></td><td>' + rackname + '</td><td>' + dcname + '</td><td class="time">' + createdate + '</td></tr>';
                            $("#tablebody").prepend(mytr);
                        }
                    },
                    error: function () {
                    }
                });
            } else if ('edit' == type) {
                var rackid = $('#modalcontent').attr('rackid');
                $.ajax({
                    type: 'post',
                    url: '/RackAction/Update',
                    data: {rackid:rackid, rackname:rackName, rackdesc:rackDesc, dcid:dcid},
                    dataType: 'text',
                    success: function () {
                        var thistr = $("#tablebody").find('[rowid="' + rackid + '"]');
                        thistr.attr('rackname', rackName);
                        thistr.attr('rackdesc', rackDesc);
                        thistr.attr('dcid', dcid);
                        thistr.children('td').eq(2).html(rackName);
                        thistr.children('td').eq(3).html(rackLocation);
                    }
                });
            }
            $('#RackModalContainer').modal('hide');
        }
    });

    $("#create-form").validate({
        rules: {
            rack_name: {
                required: true,
                maxlength: 20
            },
            rack_desc: {
                required: true,
                maxlength: 80
            }
        },
        messages: {
            rack_name: {
                required: "<span class='help'>机架名称不能为空</span>",
                maxlength: "<span class='help'>机架名称不能超过20个字符</span>"
            },
            rack_desc: {
                required: "<span class='help'>机架描述不能为空</span>",
                maxlength: "<span class='help'>机架描述不能超过80个字符</span>"
            }
        }
    });
});