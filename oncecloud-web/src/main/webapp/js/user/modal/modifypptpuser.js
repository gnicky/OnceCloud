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
    if (pwvalid()) {
    	var pwd = $("#pptp_pwd").val();
	    var pptpid = $("#pptpuser-form").attr("pptpid");
    	$.ajax({
    		type : "post",
    		url : "/RouterAction/ModifyPPTPUser",
    		data : {pwd:pwd, pptpid:pptpid},
    		dataType : "json",
    		success : function(obj) {
    			if(obj.result) {
    				$('#RouterModalContainer').modal('hide');
    			}
    		}
    	});
    }
});