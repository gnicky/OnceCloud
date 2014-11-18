(function(){
	var vmuuid = "";
	$('input[name="vmrow"]:checked').each(function () {
    	vmuuid = $(this).parents('tr').attr('rowid');
    });
    $.ajax({
    	type: 'get',
    	url: "/HostAction/MigrationTar",
    	data: {vmuuid:vmuuid},
    	dataType: 'json',
    	success: function(obj) {
    		$("#hostlist").html("");
    		var tablestr = "";
    		$.each(obj, function(index, json){
    			tablestr += '<div name="host-item" class="pool-item" uuid="' + json.uuid + '">'
                            + json.ip + '</div>';
    		});
    		if (tablestr == "") {
    			$("#hostlist").html('<div class="none">无可选服务器</div>');
    		} else {
    			$("#hostlist").html(tablestr);
    		}
    	}
    });
})();

$("#hostlist").on('click', '.pool-item', function(){
	event.preventDefault();
	$('div', $('#hostlist')).removeClass('selected');
	$(this).addClass('selected');
	$("#migratesubmit").attr('disabled', false);
	$("#hostlist").attr("uuid", $(this).attr("uuid"));
});

$("#migratesubmit").on('click', function(){
	var uuid = "";
	$('input[name="vmrow"]:checked').each(function () {
    	uuid = $(this).parents('tr').attr('rowid');
    });
	var tarHost = "";
	tarHost = $('#hostlist').attr("uuid");
	var content = "<div class='alert alert-warning'>正在迁移</div>";
    var conid = showMessageNoAutoClose(content);
    $.ajax({
    	type: 'post',
    	url: '/VMAction/Migration',
    	data: {uuid:uuid, tarHost:tarHost, content:content, conid:conid},
    	dataType: 'json',
    	success: function(obj) {
    		
    	}
    });
    $('#ServerModalContainer').modal('hide');
});