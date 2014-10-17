(function(){
	$.ajax({
		type: "get",
		url: "/UserAction/UserListSave",
		dataType: "json",
		success: function(obj) {
			$.each(obj, function(index, json){
				$("#vm_user").append('<option value="'+json.userid+'">'+json.username+'</option>');
			});
		}
	});
})();

$('#savetodbcommit').on('click', function(event) {
	event.preventDefault();
	var vmUuid = $("#vm_uuid").val();
	var vmPWD = $("#vm_pwd").val();
	var vmName = $("#vm_name").val();
	var vmIP = $("#vm_ip").val();
	var vmPlatform = $("#vm_platform option:selected").val();
	var vmUID = $("#vm_user option:selected").val();
	$.ajax({
		type : 'post',
		url : '/VMAction/SaveToDataBase',
		data : {
			vmUuid : vmUuid,
			vmPWD : vmPWD,
			vmName : vmName,
			vmIP : vmIP,
			vmPlatform : vmPlatform,
			vmUID : vmUID
		}
	});
	$('#InstanceModalContainer').modal('hide');
});