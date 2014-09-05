$(document).ready(function () {
    refresh();

    $('#confirmRegister').on('click', function (event) {
        event.preventDefault();
        var valid = $("#register-form").valid();
        codeValid();
        var errorLen = $('#register-form').find('.my-error').length;
        if (valid && errorLen == 0) {
            var username = $('#user_name').val();
            var email = $('#email').val();
            var telephone = $('#telephone').val();
            var pwd = $('#password').val();
            var vercode = $('#vercode').val();
            $.ajax({
                type: 'get',
                url: '/UserAction/Register',
                data: {username:username, userpwd:pwd, useremail:email, usertel:telephone},
                dataType: 'json',
                success: function (array) {
                    if (array.length == 1) {
                        bootbox.dialog({
                            message: '<div class="alert alert-success" style="margin:10px">注册成功</div>',
                            title: "提示",
                            buttons: {
                                main: {
                                    label: "确定",
                                    className: "btn-primary",
                                    callback: function () {
                                        window.location.href = $('#register-form').attr("base") + 'user/dashboard.jsp';
                                    }
                                }
                            }
                        });
                    } else {
                        bootbox.dialog({
                            message: '<div class="alert alert-danger" style="margin:10px">注册失败</div>',
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
                },
                error: function () {
                }
            });
        }
    });

    $('#user_name').on('focusout', function () {
        $('#user_name').parent().find('.error-message').remove();
        var userName = $('#user_name').val();
        if (userName == "") {
            $('#user_name').after('<span class="help-inline error-message my-error">请输入用户名</span>');
        } else if (userName.length < 3) {
            $('#user_name').after('<span class="help-inline error-message my-error">用户名不得少于3个字符</span>');
        } else if (userName.length > 16) {
            $('#user_name').after('<span class="help-inline error-message my-error">用户名不得超过16个字符</span>');
        } else if (!/^[a-zA-Z0-9_]{3,16}$/.test(userName)) {
            $('#user_name').after('<span class="help-inline error-message my-error">用户名只能包含字母数字或下划线</span>');
        } else {
            $.ajax({
                type: 'get',
                url: '/UserAction/QueryUser',
                data: {username:userName},
                dataType: 'json',
                success: function (array) {
                    if (array[0].exist) {
                        $('#user_name').after('<span class="help-inline error-message my-error">用户名已存在</span>');
                    }
                },
                error: function () {
                }
            });
        }
    });

    $('#vercode').on('focusout', function (event) {
        codeValid();
    });

    function codeValid() {
        $('#vercode').parent().find('.error-message').remove();
        var ver2 = $("#register-form").data('vercode').toLowerCase();
        var ver1 = $('#vercode').val().toLowerCase();
        if (ver1 == "") {
            $('#vercode').after('<span class="help-inline error-message my-error">请输入验证码</span>');
        } else if (ver1 != ver2) {
            $('#vercode').after('<span class="help-inline error-message my-error">验证码错误</span>');
        }
    }

    $("#register-form").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            password: {
                required: true,
                pwd: true
            },
            telephone: {
                required: true,
                digits: true,
                range: [10000000000, 99999999999]
            }
        },
        messages: {
            email: {
                required: '<span class="help-inline error-message">请输入合法邮箱</span>',
                email: '<span class="help-inline error-message">请输入合法邮箱</span>'
            },
            password: {
                required: '<span class="help-inline error-message">请输入密码</span>',
                pwd: '<span class="help-inline error-message">密码不得少于8个字符，并且包含大小写和数字</span>'
            },
            telephone: {
                required: '<span class="help-inline error-message">请输入正确的手机号码</span>',
                digits: '<span class="help-inline error-message">请输入正确的手机号码</span>',
                range: '<span class="help-inline error-message">请输入正确的手机号码</span>'
            }
        }
    });
});