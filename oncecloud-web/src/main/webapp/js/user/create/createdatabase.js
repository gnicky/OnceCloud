$('#createDBAction').on('click', function (event) {
    event.preventDefault();
    var valid = $("#create-form").valid();
    if (valid) {
        var dbname = $('#db_name').val();
        var dbuser = $('#db_user').val();
        var dbpwd = $('#db_pwd').val();
        var dbport = $('#db_port').val();
        var dbtype = $("#db_type option:selected").val();
        var dbthroughout = $('input[name="db_throughout"]:checked').val();

        var dbuuid = uuid.v4();
        createDatabase(dbuuid, dbname, dbuser, dbpwd, dbport, dbtype, dbthroughout);
    }
});

function createDatabase(dbuuid, dbname, dbuser, dbpwd, dbport, dbtype, dbthroughout) {
    var showid = "db-" + dbuuid.substring(0, 8);
    var showstr = "<a class='id'>" + showid + '</a>';
    var iconStr = new Array("stopped", "running", "process", "process", "process", "process");
    var nameStr = new Array("已关机", "活跃", "创建中", "销毁中", "启动中", "关机中");
    var stateStr = '<td><span class="icon-status icon-' + iconStr[2] + '" name="stateicon">'
        + '</span><span name="stateword">' + nameStr[2] + '</span></td>';
    var thistr = '<tr rowid="' + dbuuid + '"><td class="rcheck"><input type="checkbox" name="dbrow"></td><td>'
        + showstr + '</td><td name="dbname">' + dbname + '</td><td name="dbtype">' + dbtype + '</td>' + stateStr
        + '<td name="dbip"></td><td name="dbeip"></td><td name="dbport">' + dbport + '</td><td name="dbthroughout">'
        + dbthroughout + '</td><td></td></tr>';
    $("#tablebody").prepend(thistr);
    $.ajax({
        type: 'post',
        url: '/DatabaseAction',
        data: 'action=create&dbuuid=' + dbuuid + '&dbname=' + dbname + '&dbuser=' + dbuser + '&dbpwd=' + dbpwd + '&dbport=' + dbport + '&dbtype=' + dbtype + '&dbthroughout=' + dbthroughout,
        dataType: 'json',
        success: function (array) {
        }
    });
}

$("#create-form").validate({
    rules: {
        db_name: {
            required: true,
            maxlength: 20,
            legal: true
        },
        db_user: {
            required: true,
            maxlength: 20,
            legal: true
        },
        db_pwd: {
            required: true,
            maxlength: 20,
            legal: true
        },
        db_port: {
            required: true,
            digits: true,
            range: [1, 65535]
        }
    },
    messages: {
        db_name: {
            required: "<span class='help'>名称不能为空</span>",
            maxlength: "<span class='help'>名称不能超过20个字符</span>",
            legal: "<span class='unit'>名称包含非法字符</span>"
        },
        db_user: {
            required: "<span class='help'>用户名不能为空</span>",
            maxlength: "<span class='help'>用户名不能超过20个字符</span>",
            legal: "<span class='help'>用户名包含非法字符</span>"
        },
        db_pwd: {
            required: "<span class='help'>密码不能为空</span>",
            maxlength: "<span class='help'>密码不能超过20个字符</span>",
            legal: "<span class='help'>密码包含非法字符</span>"
        },
        db_port: {
            required: "<span class='unit'>起始端口不能为空</span>",
            digits: "<span class='unit'>起始端口必须是整数</span>",
            range: "<span class='unit'>起始端口必须在1到65535之间</span>"
        }
    }
});