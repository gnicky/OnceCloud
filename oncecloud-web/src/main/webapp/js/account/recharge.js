$(document).ready(function () {

    getBalance();

    $('#confirmCharge').on('click', function (event) {
        event.preventDefault();
        var errorLen = $('#charge-form').find('.success-message').length;
        if (errorLen == 0) {
            window.open("/alipay/SubmitAlipay?bill_number=" + $("#bill_number").val(), '_blank')
            $('#RechargeModalContainer').modal({
                backdrop: false,
                show: true
            });
        }
    });

    $('#chargeok').on('click', function (event) {
        event.preventDefault();
        window.location.reload();
    });

    $('#bill_number').on('focusout', function () {
        validBill();
    });

    function validBill() {
        $('#bill_number').parent().find('.success-message').remove();
        $('#bill_number').parent().find('.error-message').remove();
        var bill = $('#bill_number').val();
        if (bill == "") {
            $('#yuan').after('<span class="help-inline success-message">请输入充值金额</span>');
        } else if (isNaN(bill)) {
            $('#yuan').after('<span class="help-inline error-message">请输入正确的充值金额</span>');
        } else {
            var billInt = parseInt(bill);
            if (billInt < 1 || billInt > 10000) {
                $('#yuan').after('<span class="help-inline error-message">单笔充值金额最少1元，若充值金额大于1万元请使用银行转账</span>');
            }
        }
    }

    function getBalance() {
        $.ajax({
            type: 'get',
            url: '/UserAction/Balance',
            dataType: 'json',
            success: function (obj) {
                $('#balance').html(obj.balance.toFixed(2) + '<span class="my-unit">元</span>');
            },
            error: function () {
            }
        });
    }
})