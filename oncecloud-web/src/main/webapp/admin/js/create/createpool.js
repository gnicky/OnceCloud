$(document).ready(function () {
    loadDCList();
    fillBlank();

    function loadDCList() {
        $.ajax({
            type: 'get',
            async: false,
            url: '/DatacenterAction',
            data: 'action=getalllist',
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    $("#dcselect").html("");
                    $.each(array, function (index, item) {
                        $("#dcselect").append("<option value='" + item.dcid + "'>" + decodeURI(item.dcname) + "</option>");
                    });
                }
                else {
                    $('#dcselect').html('<option>请先添加数据中心</option>');
                    $('#dcselect').attr('disabled', true).addClass('oc-disable');
                    $('#createPoolAction').attr('disabled', true);
                    ///没有数据中心，要先去添加数据中心，
                }
            },
            error: function () {
                $('#dcselect').html('<option>请先添加数据中心</option>');
                $('#dcselect').attr('disabled', true).addClass('oc-disable');
                $('#createPoolAction').attr('disabled', true);
            }
        });
    }

    function fillBlank() {
        var type = $('#PoolModalContainer').attr('type');
        if ('edit' == type) {
            $('#modaltitle').html('修改资源池<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');
            var poolid = '';
            var poolname = '';
            var pooldesc = '';
            var dcuuid = '';

            $('input[name="poolrow"]:checked').each(function () {
                poolid = $(this).parent().parent().attr("poolid");
                poolname = $(this).parent().parent().attr("poolname");
                pooldesc = $(this).parent().parent().attr("pooldesc");
                dcuuid = $(this).parent().parent().attr("dcid");
            });
            $('#modalcontent').attr('poolid', poolid);

            $('#pool_name').val(poolname);
            if ('' != dcuuid) {
                $('#dcselect').val(dcuuid);
            }
            $('#pool_desc').val(pooldesc);
        }
    }


    $('#createPoolAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        var errorLen = $('#dcselect').parent().find('.oc-error').length;
        if (valid && errorLen == 0) {
            var poolName = document.getElementById("pool_name").value;
            var poolDesc = document.getElementById("pool_desc").value;
            var dcuuid = $("#dcselect").val();
            var dcname = $("#dcselect").find("option:selected").text();
            var type = $('#PoolModalContainer').attr('type');
            if ('new' == type) {
                $.ajax({
                    type: 'post',
                    url: '/PoolAction',
                    data: 'action=create&poolname=' + poolName + '&pooldesc=' + poolDesc + '&dcuuid=' + dcuuid + '&dcname=' + dcname,
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            var obj = array[0];
                            var poolname = decodeURI(obj.poolname);
                            var pooldesc = decodeURI(obj.pooldesc);
                            var poolid = obj.poolid;
                            var poolmaster = obj.poolmaster;
                            var createdate = obj.createdate;
                            var dcuuid = obj.dcuuid;
                            var dcname = decodeURI(obj.dcname);
                            var showid = "pool-" + poolid.substring(0, 8);
                            var mytr = '<tr poolid="' + poolid + '" poolname="' + poolname + '" pooldesc="' + pooldesc + '" dcid="' + dcuuid + '"><td class="rcheck"><input type="checkbox" name="poolrow"></td>'
                                + '<td><a class="id">' + showid + '</a></td><td>' + poolname + '</td><td>' + poolmaster + '</td><td class="pod" state="loaded"><a>' + dcname + '</a></td><td>0</td><td>0</td><td class="time">' + createdate + '</td></tr>';
                            $("#tablebody").prepend(mytr);
                        }

                    },
                    error: function () {

                    }
                });
            } else if ('edit' == type) {
                var poolid = $('#modalcontent').attr('poolid');
                $.ajax({
                    type: 'post',
                    url: '/PoolAction',
                    data: 'action=update&pooluuid=' + poolid + '&poolname=' + poolName + '&pooldesc=' + poolDesc + '&dcuuid=' + dcuuid,
                    dataType: 'text',
                    success: function () {
                        var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
                        thistr.attr('poolname', poolName);
                        thistr.attr('pooldesc', poolDesc);
                        thistr.attr('dcid', dcuuid);
                        thistr.children('td').eq(2).html(poolName);
                        thistr.children('td').eq(4).html('<a>' + dcname + '</a>');
                    },
                    error: function () {
                    }
                });
            }
            $('#PoolModalContainer').modal('hide');
        }
    });

    $("#create-form").validate({
        rules: {
            pool_name: {
                required: true,
                minlength: 3
            },
            pool_desc: {
                required: true,
                minlength: 3,
                maxlength: 80
            }
        },
        messages: {
            pool_name: {
                required: "<span class='help'>资源池名称不能为空</span>",
                minlength: "<span class='help'>资源池名称不能少于3个字符</span>"
            },
            pool_desc: {
                required: "<span class='help'>资源池描述不能为空</span>",
                minlength: "<span class='help'>资源池描述不能少于3个字符</span>",
                maxlength: "<span class='help'>资源池描述不能超过80个字符</span>"
            }
        }
    });
});