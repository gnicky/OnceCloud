(function(){
	var mtype = $("#modalcontent").attr("modify");
	var type = $("#modalcontent").attr("mtype");
	var uuid = $("#modalcontent").attr("uuid");
	if (mtype == "vlan") {
		$("#mphysical").hide();
	} else if (mtype == "physical") {
		$("#mvlan").hide();
		$.ajax({
			type: "post",
			url: "/VMAction/NetList",
			data: {uuid:uuid, type:type},
			async: false,
			dataType: "json",
			success: function(obj){
				$.each(obj, function(index, json){
					$("#physical").append('<option>'+json.nets+'</option>');
				});
			}
		});
	} else {
		$.ajax({
			type: "post",
			url: "/VMAction/NetList",
			data: {uuid:uuid, type:type},
			dataType: "json",
			success: function(obj){
				$.each(obj, function(index, json){
					$("#physical").append('<option>'+json.nets+'</option>');
				});
			}
		});
	}
})();

$("#createMac").on("click", function(){
	allDisable();
	var mtype = $("#modalcontent").attr("modify");
	var type = $("#modalcontent").attr("mtype");
	var uuid = $("#modalcontent").attr("uuid");
	if (mtype == "vlan") {
		var vifUuid;
		$('input[name="vifrow"]:checked').each(function () {
	    	vifUuid = $(this).parent().parent().attr("rowid");
		});
		var vnetid = $("#vlan").val();
		if (vnetid == "") {
			vnetid = -1;
		}
		$.ajax({
			type: "post",
			url: "/VMAction/ModifyVnet",
			data: {uuid:uuid, type:type, vifUuid:vifUuid, vnetid:vnetid},
			dataType: "json",
			success: function(obj){
				if(obj){
					$('#ModifyModalContainer').modal('hide');
					getmaclist();
				}
			}
		});
	} else if (mtype == "physical") {
		var vifUuid;
		$('input[name="vifrow"]:checked').each(function () {
	    	vifUuid = $(this).parent().parent().attr("rowid");
		});
		var physical = $("#physical option:selected").val();
		$.ajax({
			type: "post",
			url: "/VMAction/ModifyPhysical",
			data: {uuid:uuid, type:type, vifUuid:vifUuid, physical:physical},
			dataType: "json",
			success: function(obj){
				if(obj){
					$('#ModifyModalContainer').modal('hide');
					getmaclist();
				}
			}
		});
	} else {
		var vnetid = $("#vlan").val();
		if (vnetid == "") {
			vnetid = -1;
		}
		var physical = $("#physical option:selected").val();
		$.ajax({
			type: "post",
			url: "/VMAction/AddMac",
			data: {uuid:uuid, type:type, vnetid:vnetid, physical:physical},
			dataType: "json",
			success: function(obj){
				if(obj){
					$('#ModifyModalContainer').modal('hide');
					getmaclist();
				}	
			}
		});
	}
});