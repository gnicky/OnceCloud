$(document).ready(function () {
    getSwitchList();

    $('#tagdiv').on('mouseenter', 'a', function (event) {
        event.preventDefault();
        $(this).find(".prompt_b").show();
    });
    $('#tagdiv').on('mouseleave', 'a', function (event) {
        event.preventDefault();
        $(this).find(".prompt_b").hide();
    });
});


function getSwitchList() {
    $.ajax({
        type: 'get',
        url: '/DatacenterAction/Switch',
        data: {uuid: $("#platformcontent").attr("uuid")},
        dataType: 'json',
        success: function (array) {
            $("#tagdiv").html("");
            $.each(array, function (index, item) {
                var srhtml = "";
                var portcount = 0;
                var switchobj = eval('(' + item.SwitchObj + ')');
                var title = decodeURIComponent(switchobj.swName);
                if (switchobj.swType == 2) {
                    title += "(" + "万兆" + ")";
                }
                else {
                    title += "(" + "千兆" + ")";
                }
                title += " Vlan划分:";
                $.each(item.vlanlist, function (key, vlan) {
                    var vlanobj = eval('(' + vlan.vlanObj + ')');
                    title += decodeURIComponent(vlanobj.valnName) + " ";
                    $.each(vlan.portlist, function (indexid, port) {
                        var portobj = eval('(' + port.portObj + ')');
                        portcount++;   ///有一个端口就加了
                        if (portcount % 16 == 1) {
                            srhtml += "<ul>";
                        }
                        if (portcount % 2 == 1) {
                            srhtml += "<li>";
                        }


                        if (portobj.portUsing == 1) {
                            var hostObj = eval('(' + port.hostObj + ')');
                            var serverportObj = eval('(' + port.serverportObj + ')');
                            var vlantype = portobj.portType;
                            var servertype = serverportObj.protType;

                            vlantype = vlantype <= servertype ? vlantype : servertype;
                            var tyle = vlantype == 2 ? "class='green'" : "class='blue'";

                            srhtml += "<a " + tyle + ">";
                            srhtml += '<div class="prompt_b"><div class="prompt">\
							    <div class="arrow"></div>\
							    <span>Vlan:' + decodeURIComponent(vlanobj.valnName) + '<br/>端口编号:'
                                + decodeURIComponent(portobj.portId)
                                + '<br/>速率：' + (vlantype == 2 ? "万兆" : "千兆")
                                + '<br/>连接服务器：' + decodeURIComponent(hostObj.hostName) + '(' + hostObj.hostIP + ')'
                                + '<br/>连接网口：' + decodeURIComponent(serverportObj.serverportName)
                                + '</span>\
							</div>\
							</div>';

                            $("#tablebody").append('<tr><td>' + decodeURIComponent(portobj.portId) + '</td><td>' + decodeURIComponent(switchobj.swName) + '</td><td>' + decodeURIComponent(vlanobj.valnName) + '</td><td>使用中</td><td>' + (vlantype == 2 ? "万兆" : "千兆") + '</td><td>' + decodeURIComponent(hostObj.hostName) + '(' + hostObj.hostIP + ')' + '</td><td>' + decodeURIComponent(serverportObj.serverportName) + '</td></tr>');
                        }
                        else {
                            srhtml += "<a>";
                            srhtml += '<div class="prompt_b"><div class="prompt">\
							    <div class="arrow"></div>\
								  <span>Vlan:' + decodeURIComponent(vlanobj.valnName) + '<br/>端口编号:'
                                + decodeURIComponent(portobj.portId)
                                + '<br/>尚未使用'
                                + '</span>\
							</div>\
							</div>';

                            $("#tablebody").append('<tr><td>' + decodeURIComponent(portobj.portId) + '</td><td>' + decodeURIComponent(switchobj.swName) + '</td><td>' + decodeURIComponent(vlanobj.valnName) + '</td><td>尚未使用</td><td></td><td></td><td></td></tr>');
                        }
                        srhtml += "</a>";

                        if (portcount % 2 == 0) {
                            srhtml += "</li>";
                        }
                        if (portcount % 16 == 0) {
                            srhtml += "</ul>";
                        }

                    });
                });

                ///最后如果没有满16的倍数，要加ul结果
                if (portcount % 16 != 0) {
                    srhtml += "</ul>";
                }
                $("#tagdiv").append(' <div class="switch">\
					    <div class="switch_left"></div>\
					    <div class="switch_mid">\
					        <div class="interface">' + srhtml + ' </div>\
					        <div class="clear_both"></div></div>\
					    <div class="switch_right"></div>\
					      <div class="switch_title">' + title + '</div>\
					    </div>');
            });

            $(".prompt_b").hide();
        },
        error: function () {

        }
    });

}