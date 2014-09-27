/**
 * 
 */
$("#pfsubmit").on("click", function() {
	var pfName = $("#pf_name").val();
	var protocol = $("#period").find("option:selected").text();
	var srcPort = $("#pf_srcport").val();
	var destIP = $("#pf_desIp").val();
	var destPort = $("#pf_desport").val();
	var srcIP = $("#platformcontent").attr("rtIp");
	$.ajax({
		type : "post",
		url : "/RouterAction/AddPortForwarding",
		data : {
			protocol : protocol,
			srcIP : srcIP,
			srcPort : srcPort,
			destIP : destIP,
			destPort : destPort,
			pfName : pfName
		},
		dataType : "json",
		success : function(json) {
			var uuid = json.uuid;
			var thistr = '<tr pfuuid="'
					+ uuid
					+ '" pfrouter="'
					+ srcIP
					+ '" protocol="'
					+ protocol
					+ '" srcPort="'
					+ srcPort
					+ '" destIP="'
					+ destIP
					+ '" destPort="'
					+ destPort
					+ '">'
					+ '<td class="rcheck"><input type="checkbox" name="rulerow"></td><td>'
					+ pfName + '</td><td>' + protocol + '</td><td>' + srcPort
					+ '</td><td>' + destIP + '</td><td>' + destPort
					+ '</td></tr>';
			$('#tablebody').prepend(thistr);
		}
	});
});