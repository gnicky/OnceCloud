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
                    data: {prefix: prefix, start: start_addr_d, end: end_addr_d},
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            if (array[0].result == true) {
                                getAddressList(1, 10, "", "dhcp");
                            }
                        }
                        $('#IPModalContainer').modal('hide');
                    }
                });
            } else {
                $.ajax({
                    type: 'post',
                    url: '/AddressAction/AddEIP',
                    data: {
                        prefix: prefix,
                        start: start_addr_d,
                        end: end_addr_d,
                        eiptype: eiptype,
                        eipif: eipif
                    },
                    dataType: 'json',
                    success: function (array) {
                        if (array.length == 1) {
                            if (array[0].result == true) {
                                getAddressList(1, 10, "", "publicip");
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