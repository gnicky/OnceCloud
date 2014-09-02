$('a').on('click', function (event) {
    if ($(this).hasClass("btn-forbidden")) {
        event.stopImmediatePropagation();
        return false;
    }
});

calendar();

function calendar() {
    var date = new Date();
    var monthStr = new Array("Jan", "Feb", "March", "April", "May", "June", "July", "August", "Sep", "Oct", "Nov", "Dec");
    $("#avartar-top").html(monthStr[date.getMonth()]);
    $("#avartar-bottom").html(date.getDate());
}

$('body').delegate("input", 'focusout', function (event) {
    if (event.which == 16 || event.which == 17 || event.which == 18) {
        return false;
    }
    var errorChar = /^[^`~!$%^&#*()+=|\\\][\]\{\}:;'\,<>?]*$/;
    var self = $(this);
    if (!errorChar.test($(this).val())) {
        bootbox.dialog({
            className: "bootbox-small",
            message: '<div class="alert alert-warning" style="margin:10px; color:#c09853; text-align:center; font-size:14px"><span class="glyphicon glyphicon-warning-sign"></span><br/>输入的信息含有非法字符<br/>如：[!#$%^&*(){}|\]等<br/>请重新输入！<br/></div>',
            title: "提示",
            buttons: {
                cancel: {
                    label: "确定",
                    className: "btn-default",
                    callback: function () {
                        self.val("");
                    }
                }
            }
        });
        return false;
    }
});

$('body').delegate("textarea", 'focusout', function (event) {
    if (event.which == 16 || event.which == 17 || event.which == 18) {
        return false;
    }
    var errorChar = /^[^`~!@$%^&#*()+=|\\\][\]\{\}:;'\<>?]*$/;
    var self = $(this);
    if (!errorChar.test($(this).val())) {
        bootbox.dialog({
            className: "bootbox-small",
            message: '<div class="alert alert-warning" style="margin:10px; color:#c09853; text-align:center; font-size:14px"><span class="glyphicon glyphicon-warning-sign"></span><br/>输入的信息含有非法字符<br/>如：[!@#$%^&*(){}|\]等<br/>请重新输入！<br/></div>',
            title: "提示",
            buttons: {
                cancel: {
                    label: "确定",
                    className: "btn-default",
                    callback: function () {
                        self.val("");
                    }
                }
            }
        });
        return false;
    }
});

var options = {
    bootstrapMajorVersion: 3,
    currentPage: 1,
    totalPages: 1,
    numberOfPages: 0,
    onPageClicked: function (e, originalEvent, type, page) {
        reloadList(page);
    },
    shouldShowPage: function (type, page, current) {
        switch (type) {
            case "first":
            case "last":
                return false;
            default:
                return true;
        }
    }
};

$('#pageDivider').bootstrapPaginator(options);

$('.btn-refresh').on('click', function (event) {
    reloadList(1);
});

$('#limit').on('focusout', function () {
    var limit = $("#limit").val();
    var reg = /^[0-9]*[1-9][0-9]*$/;
    if (!reg.test(limit)) {
        $("#limit").val(10);
    }
    reloadList(1);
});

$('#search').on('focusout', function () {
    reloadList(1);
});

$('#search').keypress(function (e) {
    var key = e.which;
    if (key == 13) {
        reloadList(1);
    }
});

function pageDisplayUpdate(current, total) {
    $('#currentP').html(current);
    $('#totalP').html(total);
}