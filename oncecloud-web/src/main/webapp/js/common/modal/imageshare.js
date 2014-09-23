(function () {
	 var pooluuid = $('input[name="imagerow"]:checked').eq(0).attr("pooluuid");
	 var images = "";
	 $('input[name="imagerow"]:checked').each(function() {
	 	images += $(this).parents("tr").attr("imageUId") + ",";
	 });
	 images = images.substring(0, images.length - 1);
	 $.ajax({
	 	type: "post",
	 	url: "/ImageAction/ShareImageList",
	 	data: {pooluuid:pooluuid,images:images},
	 	dataType: "json",
	 	success: function(obj) {
	 		if (obj.length > 0) {
		 		$.each(obj, function(index, json) {
		 			$("#htypool").append('<option value="'+json.pooluuid+'">'+json.poolname+'</option>');
		 		});
	 		} else {
	 			$("#htypool").append('<option value="empty">请重新选择需要迁移的模板</option>');
	 		}
	 	}
	 });
})();

$("#poolshareimage").on("click", function(event) {
	event.preventDefault();
	var sorpooluuid = $('input[name="imagerow"]:checked').eq(0).attr("pooluuid");
	var images = "";
	$('input[name="imagerow"]:checked').each(function() {
		images += $(this).parents("tr").attr("imageUId") + ",";
	});
	images = images.substring(0, images.length - 1);
	var despooluuid = $("#htypool").find("option:selected").val();
	$.ajax({
		type: "post",
	 	url: "/ImageAction/ImageShare",
	 	data: {sorpooluuid:sorpooluuid,despooluuid:despooluuid,images:images},
	 	dataType: "text",
	 	success: function(obj) {
	 		removeAllCheck();
	 	}
	});
	 $('#ImageModalContainer').modal('hide');
});