$(document).ready(function () {
    fillBlank();

    function fillBlank() {
        var type = $('#UserModalContainer').attr('type');
        if ('edit' == type) {
            $('#modaltitle').html('修改用户<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a>');
            var userid = $('#platformcontent').attr('userid');
            $('#modalcontent').attr('userid', userid);

            var name = $('#username').text();
            $('#modalcontent').attr('oldname', name);
            var mail = $('#usermail').text();
            var phone = $('#userphone').text();
            var company = $('#usercom').text();
            var level = $('#userlevel').attr('level');
            $('#pwd').hide();
            $('#user_name').val(name);
            $('#user_name').attr('disabled', true).addClass('oc-disable');
            $('#user_email').val(mail);
            $('#user_tel').val(phone);
            $('#user_company').val(company);
            $('#user_level').val(level);
        }
    }


    $('#createUserAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        var errorLen = $('#create-form').find('.oc-error').length;
        var type = $('#UserModalContainer').attr('type');
        if (valid && errorLen == 0) {
            var userName = document.getElementById("user_name").value;
            var userPwd = document.getElementById("user_pwd").value;
            var userEmail = document.getElementById("user_email").value;
            var userTel = document.getElementById("user_tel").value;
            var userCom = document.getElementById("user_company").value;
            var userLevel = document.getElementById("user_level").value;
            console.log(type);
            if ('new' == type) {
                console.log("aaa");
                $.ajax({
                    type: 'post',
                    url: '/UserAction/Create',
                    data: {
                        userName: userName,
                        userPassword: userPwd,
                        userEmail: userEmail,
                        userTelephone: userTel,
                        userCompany: userCom,
                        userLevel: userLevel
                    },
                    dataType: 'json',
                    success: function (array) {
                        console.log(array);
                        if (array.length == 1) {
                            var obj = array[0];
                            var username = decodeURIComponent(obj.username);
                            var userid = obj.userid;
                            var usercom = decodeURIComponent(obj.usercom);
                            var userdate = obj.userdate;
                            var userlevel = obj.userlevel;
                            var usermail = obj.usermail;
                            var userphone = obj.userphone;
                            var levelstr = "<a><span class='glyphicon glyphicon-user' style='margin-right:7px'></span>平台用户</a>";
                            if (userlevel == 0) {
                                levelstr = "<a><span class='glyphicon glyphicon-star' style='margin-right:7px'></span>管理员</a>";
                            }
                            var mytr = '<tr userid="' + userid + '" username="' + username + '"><td class="rcheck"><input type="checkbox" name="userrow"></td>'
                                + '<td><a class="username">' + username + '</a></td><td>' + usermail + '</td><td>' + userphone + '</td><td>' + usercom + '</td><td>' + levelstr
                                + '</td><td class="time">' + userdate + '</td></tr>';
                            $("#tablebody").prepend(mytr);
                        }
                        $('#UserModalContainer').modal('hide');
                    }
                });
            } else if ('edit' == type) {
                var userId = $('#platformcontent').attr('userid');
                $.ajax({
                    type: 'post',
                    url: '/UserAction/Update',
                    data: {
                        userName: userName,
                        changeId: userId,
                        userEmail: userEmail,
                        userTelephone: userTel,
                        userCompany: userCom,
                        userLevel: userLevel
                    },
                    dataType: 'text',
                    success: function () {
                        var levelstr = "<a><span id='userlevel' level='1' class='glyphicon glyphicon-user' style='margin-right:7px'></span>平台用户</a>";
                        if (userlevel == 0) {
                            levelstr = "<a><span id='userlevel' level='0' class='glyphicon glyphicon-star' style='margin-right:7px'></span>管理员</a>";
                        }
                        $('#username').text(userName);
                        $('#usermail').text(userEmail);
                        $('#userphone').text(userTel);
                        $('#usercom').text(userCom);
                        $('#leveltd').html(levelstr);
                        $('#UserModalContainer').modal('hide');
                    }
                });
            }
        }
    });

    $('#user_rpwd').on('focusout', function () {
        pwdValid();
    });

    $('#user_pwd').on('focusout', function () {
        pwdValid();
    });

    function pwdValid() {
        $('#user_rpwd').parent().find('.oc-error').remove();
        var pwd = document.getElementById("user_pwd").value;
        var rpwd = document.getElementById("user_rpwd").value;
        if (pwd != rpwd) {
            $('#user_rpwd').parent().append('<label class="oc-error"><span class="help">两次输入的密码不一致</span></label>');
            return false;
        } else {
            return true;
        }
    }

    $('#user_name').on('focusout', function () {
        nameValid();
    });

    function nameValid() {
        $('#user_name').parent().find('.oc-error').remove();
        var userName = document.getElementById("user_name").value;
        var oldname = $('#modalcontent').attr('oldname');
        if (userName == "") {
            $('#user_name').parent().append('<label class="oc-error"><span class="help">用户名不能为空</span></label>');
            return false;
        } else if (userName.length < 3) {
            $('#user_name').parent().append('<label class="oc-error"><span class="help">用户名不能少于3个字符</span></label>');
            return false;
        } else if (userName == oldname) {
            return true;
        } else {
            $.ajax({
                type: 'get',
                async: false,
                url: '/UserAction/QueryUser',
                data: {userName: userName},
                dataType: 'json',
                success: function (array) {
                    if (array.length == 1) {
                        var exist = array[0].exist;
                        if (exist == true) {
                            $('#user_name').parent().append('<label class="oc-error"><span class="help">用户名已存在</span></label>');
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            });
        }
    }

    $("#create-form").validate({
        rules: {
            user_pwd: {
                required: true,
                minlength: 5,
                maxlength: 20,
                legal: true
            },
            user_email: {
                required: true
            },
            user_tel: {
                required: true
            },
            user_company: {
                required: true
            }
        },
        messages: {
            user_pwd: {
                required: "<span class='help'>密码不能为空</span>",
                minlength: "<span class='help'>密码不能超过20个字符</span>",
                maxlength: "<span class='help'>密码不能超过20个字符</span>",
                legal: "<span class='help'>密码包含非法字符</span>"
            },
            user_email: {
                required: "<span class='help'>邮箱不能为空</span>"
            },
            user_tel: {
                required: "<span class='help'>联系电话不能为空</span>"
            },
            user_company: {
                required: "<span class='help'>公司不能为空</span>"
            }
        }
    });
});