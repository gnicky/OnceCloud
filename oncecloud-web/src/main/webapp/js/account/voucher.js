$(document).ready(function () {
    $("#applyVoucherAction").on('click', function (event) {
        event.preventDefault();
        var voucher = $('#apply_list').find('.selected').attr("value");
        $.ajax({
            type: 'post',
            url: '/VoucherAction/Apply',
            data: {voucher: voucher},
            dataType: 'json',
            success: function (obj) {
                if (obj.result) {
                    bootbox.dialog({
                        message: '<div class="alert alert-success" style="margin:10px">'
                            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;您的申请已发送，请耐心等待管理员批准</div>',
                        title: "提示",
                        buttons: {
                            main: {
                                label: "确定",
                                className: "btn-primary",
                                callback: function () {
                                }
                            }
                        }
                    });
                } else {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px">'
                            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;您已经申请过体验券</div>',
                        title: "提示",
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
    });

    $('#apply_list').on('click', 'li', function (event) {
        event.preventDefault();
        $('li', $('#apply_list')).removeClass('selected');
        $(this).addClass('selected');
    });
});