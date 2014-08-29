$(document).ready(function() {

	$('#modifyinfomation').on('click', function(event) {
		event.preventDefault();
		var valid = $("#modify-form").valid();
		if(valid) {
			var modifytype = $('#modify-form').attr('type');
			var modifyuuid = $('#modify-form').attr('uuid');
			var modifyname = $('#name').val();
			var modifydesc = $('#desc').val();
			$.ajax({
				type: 'post',
				url: '/ModifyAction',
				data: "action=basicinformation&modifytype="+modifytype+"&modifyuuid="+modifyuuid
				+"&modifyname="+modifyname+"&modifydesc="+modifydesc,
				dataType: 'text',
				success: function(response){
					var obj = jQuery.parseJSON(response);
					if(obj.isSuccess) {
						if ("" == trim(modifydesc)) {
							modifydesc = '&nbsp;';
						}
						if ('instance'== modifytype) {
							$('#instancename').text(modifyname);
							$('#instancedesc').html(modifydesc);
						} else if ('volume'== modifytype) {
							$('#volumename').text(modifyname);
							$('#volumedesc').html(modifydesc);
						}else if ('eip'== modifytype) {
							$('#eipname').text(modifyname);
							$('#eipdesc').html(modifydesc);
						} else if ('image'== modifytype) {
							$('#imagename').text(modifyname);
							$('#imagedesc').html(modifydesc);
						} else if ('lb'== modifytype) {
							$('#lbname').text(modifyname);
							$('#lbdesc').html(modifydesc);
						} else if ('rt'== modifytype) {
							$('#rtname').text(modifyname);
							$('#rtdesc').html(modifydesc);
						} else if ('db'== modifytype) {
							$('#dbname').text(modifyname);
							$('#dbdesc').html(modifydesc);
						} else if ('al' == modifytype) {
							$('#alName').text(modifyname);
							$('#alDesc').html('<div>'+modifydesc+'</div>');
						}
					}
				},
				error: function() {}
			});

			if ('instance'== modifytype) {
				$("#InstanceModalContainer").modal('hide');
			} else if ('volume'== modifytype) {
				$("#VolumeModalContainer").modal('hide');
			} else if ('image'== modifytype) {
				$("#ImageModalContainer").modal('hide');
			} else if ('eip'== modifytype) {
				$("#EipModalContainer").modal('hide');
			} else if ('lb'== modifytype) {
				$("#LbModalContainer").modal('hide');
			} else if ('rt'== modifytype) {
				$("#RouterModalContainer").modal('hide');
			} else if ('db'== modifytype) {
				$("#DataBaseModalContainer").modal('hide');
			} else if ('al' == modifytype) {
				$('#RuleModalContainer').modal('hide');
			}
		}
	});

	$("#modify-form").validate({
		rules: {
			name: {
				required: true,
				minlength: 3,
				maxlength: 80
			},
			desc: {
				maxlength: 80
			}
		},
		messages: {
			name: {
				required: "<span class='unit'>名称不能为空</span>",
				minlength: "<span class='unit'>名称不能少于3个字符</span>",
				maxlength: "<span class='unit'>名称不能多于80个字符</span>"
			},
			desc: {
				maxlength: "<span class='unit'>描述不能多于80个字符</span>"
			}
		}
	});
	
	function trim(string){
		return string.replace(/( +)$/g,"").replace(/^( +)/g,"");
	}
});