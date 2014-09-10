$(document).ready(function () {
    $('#addIPAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        var errorLen = $('#create-form').find('.oc-error').length;
        if (valid && errorLen == 0) {
            var start_addr_a = $("#start_addr_a").val();
            var start_addr_b = $("#start_addr_b").val();
            var start_addr_c = $("#start_addr_c").val();
            var start_addr_d = $("#start_addr_d").val();
            var end_addr_d = $("#end_addr_d").val();
            var start_int = parseInt(start_addr_d);
            var end_int = parseInt(end_addr_d);
            if (start_addr_a == "" || start_addr_b == "" || start_addr_c == "") {
                $("#start_addr_d").parent().append('<label class="oc-error"><span class="help">地址不完整</span></label>');
                return false;
            }
            if (end_int < start_int) {
                $("#start_addr_d").parent().append('<label class="oc-error"><span class="help">地址范围不合法</span></label>');
                return false;
            }
            var prefix = start_addr_a + "." + start_addr_b + "." + start_addr_c + ".";
            var eiptype = $("#eip_type").val();
            var eipif = $("#eip_if").val();
            var selected = $('.once-tab').find('.active');
            var type = selected.get(0).getAttribute("oc-type");
            if (type == "dhcp") {
                $.ajax({
                    type: 'post',
                    url: '/AddressAction/AddDHCP',
                    data: {prefix:prefix, start:start_addr_d, end:end_addr_d},
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            if (array[0].result == true) {
                                getIPList(1, 10, "", "dhcp");
                            }
                        }
                        $('#IPModalContainer').modal('hide');
                    }
                });
            } else {
                $.ajax({
                    type: 'post',
                    url: '/AddressAction/AddEIP',
                    data : {
								prefix : prefix,
								start : start_addr_d,
								end : end_addr_d,
								eiptype : eiptype,
								eipif : eipif
							},
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            if (array[0].result == true) {
                                getIPList(1, 10, "", "publicip");
                            }
                        }
                        $('#IPModalContainer').modal('hide');
                    }
                });
            }

        }
    });

    loadinit();

    function loadinit() {
        var type = $('.once-tab').find('.active').attr("oc-type");
        if (type == "dhcp") {
            $("#typeitem").hide();
            $("#ifitem").hide();
        } else {
            $("#typeitem").show();
            $("#ifitem").show();
        }
    }


    $("#start_addr_a").blur(function (e) {
        $("#start_addr_d").parent().find('.oc-error1').remove();
        if (checkaddr($(this).val(), 1, 255)) {
            $("#end_addr_a").val($(this).val());
        } else {
            $("#start_addr_d").parent().find('.oc-error').remove();
            $("#start_addr_d").parent().append('<label class="oc-error oc-error1"><span class="help">地址不合法</span></label>');
        }
    });

    $("#start_addr_b").blur(function (e) {
        $("#start_addr_d").parent().find('.oc-error2').remove();
        if (checkaddr($(this).val(), 0, 255)) {
            $("#end_addr_b").val($(this).val());
        }
        else {
            $("#start_addr_d").parent().find('.oc-error').remove();
            $("#start_addr_d").parent().append('<label class="oc-error oc-error2"><span class="help">地址不合法</span></label>');
        }
    });

    $("#start_addr_c").blur(function (e) {
        $("#start_addr_d").parent().find('.oc-error3').remove();
        if (checkaddr($(this).val(), 0, 255)) {
            $("#end_addr_c").val($(this).val());
        }
        else {
            $("#start_addr_d").parent().find('.oc-error').remove();
            $("#start_addr_d").parent().append('<label class="oc-error oc-error3"><span class="help">地址不合法</span></label>');
        }
    });

    $("#start_addr_d").blur(function (e) {
        $("#start_addr_d").parent().find('.oc-error4').remove();
        if (checkaddr($(this).val(), 0, 255)) {
            $("#end_addr_d").val($(this).val());
        }
        else {
            $("#start_addr_d").parent().find('.oc-error').remove();
            $("#start_addr_d").parent().append('<label class="oc-error oc-error4"><span class="help">地址不合法</span></label>');
        }
    });

    $("#end_addr_d").blur(function (e) {
        $("#end_addr_d").parent().find('.oc-error').remove();
        if (!checkaddr($(this).val(), $("#start_addr_d").val(), 255)) {
            $("#end_addr_d").parent().append('<label class="oc-error"><span class="help">地址范围不正确</span></label>');
        }
    });

    function checkaddr(addr, num1, num2) {
        var result = false;
        if ($.isNumeric(addr)) {
            if (addr >= num1 && addr <= num2) {
                result = true;
            }
        }
        return result;
    }

    function getIPList(page, limitnum, search, type) {
        if (type == "dhcp") {
            $.ajax({
                type: 'get',
                url: '/AddressAction/DHCPList',
                data: {page:page, limit:limitnum, search:search},
                dataType: 'json',
                success: function (array) {
                    if (array.length >= 1) {
                        var totalnum = array[0];
                        var totalp = Math.ceil(totalnum / limitnum);
                        if (totalp == 0) {
                            totalp = 1;
                        }
                        options = {
                            totalPages: totalp
                        }
                        $('#pageDivider').bootstrapPaginator(options);
                        pageDisplayUpdate(page, totalp);
                        var btable = document.getElementById("tablebody");
                        var tableStr = "";
                        $("#tablethead").html('<tr><th width="4%"></th><th width="20%">MAC</th><th width="20%">IP</th><th width="12%">状态</th><th width="16%">应用资源</th><th width="12%">所属用户</th><th width="16%">创建时间</th> </tr>');

                        for (var i = 1; i < array.length; i++) {
                            var obj = array[i];
                            var ip = obj.dhcpip;
                            var mac = obj.dhcpmac;
                            var uuid = obj.tenantuuid;
                            var showid = obj.showid;
                            var type = obj.depenType;
                            var stateStr = '<td state="free"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">空闲</span></td>';
                            if (uuid != "") {
                                stateStr = '<td state="using"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">租用中</span></td>';
                            }
                            if (0 == type) {
                                showid = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + showid;
                            } else if (1 == type) {
                                showid = '<span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;' + showid;
                            } else if (2 == type) {
                                showid = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;' + showid;
                            } else if (3 == type) {
                                showid = '<span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;' + showid;
                            }
                            var username = obj.tenantuser;
                            var createdate = obj.createdate;
                            var mytr = '<tr mac="' + mac + '" ip="' + ip + '"><td class="rcheck"><input type="checkbox" name="addressrow"></td>'
                                + '<td><a class="id">' + mac + '</a></td><td>' + ip + '</td>' + stateStr + '<td><a class="id">' + showid + '</a></td><td>' + username + '</td><td class="time">' + createdate + '</td>';
                            tableStr += mytr;
                        }
                        btable.innerHTML = tableStr;
                    }
                }
            });
        } else if (type == "publicip") {
            $.ajax({
                type: 'get',
                url: '/AddressAction/EIPList',
                data: {page:page, limit:limitnum, search:search},
                dataType: 'json',
                success: function (array) {
                    if (array.length >= 1) {
                        var totalnum = array[0];
                        var totalp = Math.ceil(totalnum / limitnum);
                        if (totalp == 0) {
                            totalp = 1;
                        }
                        options = {
                            totalPages: totalp
                        }
                        $('#pageDivider').bootstrapPaginator(options);
                        pageDisplayUpdate(page, totalp);
                        var btable = document.getElementById("tablebody");
                        var tableStr = "";
                        $("#tablethead").html('<tr><th width="4%"></th><th width="12%">IP</th><th width="12%">'
                            + '状态</th><th width="12%">应用资源</th><th width="12%">带宽&nbsp;(Mbps)</th><th width="12%">IP分组</th><th width="12%">网关接口</th><th width="12%">所属用户</th><th width="12%">创建时间</th> </tr>');
                        for (var i = 1; i < array.length; i++) {
                            var obj = array[i];
                            var eip = obj.eip;
                            var euuid = obj.euuid;
                            var depenType = obj.depenType;
                            var eipDependency = "";
                            if (obj.eipDependency != "") {
                                eipDependency = obj.eipDependency.substring(0, 8);
                                if (0 == depenType) {
                                    eipDependency = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;i-' + eipDependency;
                                } else if (1 == depenType) {
                                    eipDependency = '<span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;lb-' + eipDependency;
                                } else if (2 == depenType) {
                                    eipDependency = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;db-' + eipDependency;
                                } else if (3 == depenType) {
                                    eipDependency = '<span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;db-' + eipDependency;
                                }
                            }
                            var eipBandwidth = obj.eipBandwidth;
                            var eipDescription = decodeURI(obj.eipDescription);
                            var eipType = decodeURI(obj.eipType);
                            var eipIf = obj.eif;
                            var euername = decodeURI(obj.euername);
                            if (!eipBandwidth) {
                                eipBandwidth = "";
                            }
                            var stateStr = '<td state="free"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">空闲</span></td>';
                            if (euername != "") {
                                stateStr = '<td state="using"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">租用中</span></td>';
                            }
                            var createdate = obj.createdate;
                            var mytr = '<tr ip="' + eip + '" euuid="' + euuid + '"><td class="rcheck"><input type="checkbox" name="addressrow"></td>'
                                + '<td>' + eip + '</td>' + stateStr + '<td><a class="id">' + eipDependency + '</a></td><td>' + eipBandwidth + '</td><td>' + eipType + '</td><td>Interface&nbsp;' + eipIf + '</td><td>' + euername + '</td><td class="time">' + createdate + '</td></tr>';
                            tableStr += mytr;
                        }
                        btable.innerHTML = tableStr;
                    }
                }
            });
        }
    }


    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPS");
        var t = document.getElementById("totalPS");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    $("#create-form").validate({
        rules: {
            start_addr_d: {
                required: true
            },
            end_addr_d: {
                required: true
            }
        },
        messages: {
            start_addr_d: {
                required: "<span class='help'>地址不能为空</span>"
            },
            end_addr_d: {
                required: "<span class='help'>地址不能为空</span>"
            }
        }
    });
});