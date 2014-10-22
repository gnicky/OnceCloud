$('#pptpuser-form').validate({
    rules: {
        pptp_name: {
            required: true,
            maxlength: 20,
            legal: true
        }
    },
    messages: {
        pptp_name: {
            required: "<span class='unit'>账户不能为空</span>",
            maxlength: "<span class='unit'>账户不能超过20个字符</span>",
            legal: "<span class='unit'>账户包含非法字符</span>"
        }
    }
});

$('#display-pwd').on('change', function (event) {
    event.preventDefault();
    if (this.checked == true) {
        $('#pptp_pwd').attr('type', 'text');
    } else {
        $('#pptp_pwd').attr('type', 'password');
    }
});

function pwvalid() {
    var text = $('input[name=pptp_pwd]').val();
    if (/^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(text)) {
        return true;
    } else {
        return false;
    }
}

$('input[name=pptp_pwd]').on('input', function (e) {
    var text = $('input[name=pptp_pwd]').val();
    if (/^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(text)) {
        $('#pw-alert').removeClass("alert-info");
        $('#pw-alert').removeClass("alert-danger");
        $('#pw-alert').addClass("alert-success");
    } else {
        $('#pw-alert').removeClass("alert-info");
        $('#pw-alert').removeClass("alert-success");
        $('#pw-alert').addClass("alert-danger");
    }
});

$("#pptpuser-submit").on("click", function(event){
	event.preventDefault();
    var valid = $('#pptpuser-form').valid();
    var pwval = pwvalid();
    if (valid && pwval) {
	    var routerUuid = $('#platformcontent').attr("routerUuid");
    	var name = $("#pptp_name").val();
    	var pwd = $("#pptp_pwd").val();
	    var rownum = $("#pptp-user tr").length;
    	$.ajax({
    		type : "post",
    		url : "/RouterAction/PPTPUserCreate",
    		data : {name:name, pwd:pwd, routerUuid:routerUuid},
    		dataType : "json",
    		success : function(obj) {
    			if(obj.result) {
	    			$('#RouterModalContainer').modal('hide');
	    			var tablestr = '<tr pptpid="'+ obj.pptpid +'"><td id="pname">' + name + '</td><td>******</td>';
	    			if(rownum > 0) {
	    				$("#pptp-user tr:eq(0) td:eq(2)").html('<a id="pptp-delete">[删除]&nbsp;</a><a id="pptp-modify">&nbsp;[修改]</a>');
	    				tablestr += '<td><a id="pptp-delete">[删除]&nbsp;</a><a id="pptp-modify">&nbsp;[修改]</a></td></tr>';
	    			} else {
	    				tablestr += '<td><a id="pptp-modify">[修改]</a></td></tr>';
	    			}
	    			$("#pptp-user").append(tablestr);
    			}
    		}
    	});
    }
});