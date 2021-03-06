$('.once-tab').on('click', '.tab-filter', function (event) {
    event.preventDefault();
    $('li', $('.once-tab')).removeClass('active');
    $(this).addClass('active');
    var type = $('.once-tab').find('.active').attr("type");
    if (type == "pooltab") {
        getPoolList();
    } else if (type == "racktab") {
        getrackList();
    }
});

getPoolList();

$('#tagdiv').on('click', '.id', function (event) {
    event.preventDefault();
    var hostid = $(this).attr('hostid');
    var form = $("<form></form>");
    form.attr("action", "/host/detail");
    form.attr('method', 'post');
    var input = $('<input type="text" name="hostid" value="' + hostid + '" />');
    form.append(input);
    form.css('display', 'none');
    form.appendTo($('body'));
    form.submit();
});


$('#tagdiv').on('click', '.switchid', function (event) {
    event.preventDefault();
    var switchid = $(this).attr('switchid');
    var form = $("<form></form>");
    form.attr("action", "/switch/detail");
    form.attr('method', 'post');
    var input = $('<input type="text" name="switchid" value="' + switchid + '" />');
    form.append(input);
    form.css('display', 'none');
    form.appendTo($('body'));
    form.submit();
});


$('#tagdiv').on('mouseenter', 'li', function (event) {
    event.preventDefault();
    $(this).find(".t_tips").show();
});
$('#tagdiv').on('mouseleave', 'li', function (event) {
    event.preventDefault();
    $(this).find(".t_tips").hide();
});


function getrackList() {
    $.ajax({
        type: 'get',
        url: '/DatacenterAction/RackList',
        dataType: 'json',
        success: function (array) {
            $("#tagdiv").html("");
            $.each(array, function (index, item) {
                var rack = eval('(' + item.rackobj + ')');

                var srhtml = "";

                $.each(item.switchlist, function (key, sw) {
                    var switchobj = eval('(' + sw + ')');
                    srhtml += '<li class="equipment switchid" switchid=' + switchobj.swUuid + '><a href="#">\
			                <div class="name">' + decodeURIComponent(switchobj.swName) + '</div>\
			               <div class="clear_both"></div>\
						      </a>\
						    </li>'
                });

                $.each(item.serverlist, function (key, server) {
                    var host = eval('(' + server.hostobj + ')');
                    var imagecount = server.imagecount;
                    var vmcount = server.vmcount;
                    srhtml += '<li class="one id" hostid=' + host.hostUuid + '>\
				          <div class="name">' + host.hostIP + '</div>\
				          <div class="clear_both"></div>\
				          <div class="t_tips">\
				          	   <span class="n_content">\
				               	<p>服务器：' + decodeURIComponent(host.hostName) + '</p>\
				                <p>内存：' + Math.round(host.hostMem / 1024) + 'GB</p>\
				                <p>CPU：' + host.hostCpu + '</p>\
				                <p>虚拟机：' + vmcount + '个</p>\
				               </span>\
				               <b class="arrow"></b>\
				          </div>\
				    </li>'
                });

                /*
                 * $.each(item.storagelist,function(key,sr){ var storage =
                 * eval('('+ sr +')'); srhtml+='<li class="two" srid='+ storage.srUuid +'>\
                 * <div class="name">' + decodeURIComponent(storage.srName) +'</div>\
                 * <div class="clear_both"></div>\ <div class="t_tips">\ <span
                 * class="n_content">\ <p>存储集群:'+decodeURIComponent(storage.srName)+'</p>\
                 * <p>管理节点:'+storage.srAddress+'</p>\ <p>挂载目录：'+storage.srdir+'</p>\
                 * <p>文件系统：'+storage.srtype.toUpperCase()+'</p>\ </span>\ <b
                 * class="arrow"></b>\ </div>\ </li>' });
                 */


                $("#tagdiv").append('<div class="serviceto">\
						<h2>' + decodeURIComponent(rack.rackName) + '</h2>\
						<div class="list">\
						    <ul>' + srhtml + '</ul>\
						    </div>\
						    </div>'
                );

            });

            $(".t_tips").hide();
        }
    });

}


function getPoolList() {
    $.ajax({
        type: 'get',
        url: '/DatacenterAction/PoolList',
        dataType: 'json',
        success: function (array) {
            var srhtml = "";
            var sruuid = '';
            var srname = '';
            $("#tagdiv").html("");
            $.each(array, function (index, item) {
                var pool = eval('(' + item.poolobj + ')');
                var masterIp = '192.168.1.1';

                $.each(item.storagelist, function (key, sr) {
                    var storage = eval('(' + sr + ')');
                    if (sruuid.indexOf() < 0) {
                        sruuid += storage.srUuid + ',';
                        srname += decodeURIComponent(storage.srName) + ' ';

                        srhtml += '<div class="area" id=' + storage.srUuid + '>\
						<h4>\
							<a>存储集群:' + decodeURIComponent(storage.srName) + '</a>\
						</h4>\
						<div class="inner">\
							<div class="resource">\
								<div class="srclass" >\
								    <div class="pool-item">\
								      <h5>集群管理节点：' + storage.srAddress + '</h5>\
								    </div>\
									<div class="resource-item">\
										<div class="row">\
											<div class="col-md-4 imgae">\
												<span class="glyphicon glyphicon-hdd cool-cyan"></span>\
												<h5>' + decodeURIComponent(storage.srName) + '</h5>\
											</div>\
											<div class="col-md-8 attrcontent">\
												<h5>挂载目录：' + storage.srdir + '</h5>\
												<h5>文件系统：' + storage.srtype.toUpperCase() + '</h5>\
											</div>\
										</div>\
									</div>\
								</div>\
							</div>\
					  </div>\
					 </div>'
                    }
                });

                var resorceHtml = "";
                $.each(item.serverlist, function (key, server) {
                    var host = eval('(' + server.hostobj + ')');
                    var imagecount = server.imagecount;
                    var vmcount = server.vmcount;
                    if (host.hostUuid == pool.poolMaster)
                        masterIp = host.hostIP;

                    resorceHtml += '\
					<div class="resource-item id" hostid=' + host.hostUuid + '>\
					<div class="row">\
						<div class="col-md-4 imgae">\
							<span class="glyphicon glyphicon-tasks cool-purple"></span>\
							<h5>服务器：' + decodeURIComponent(host.hostName) + '</h5>\
						</div>\
						<div class="col-md-4 attrcontent">\
							<h5>内存：' + Math.round(host.hostMem / 1024) + 'GB</h5>\
							<h5>CPU：' + host.hostCpu + '</h5>\
							<h5>虚拟机：' + vmcount + '个</h5>\
						</div>\
						<div class="col-md-4 attrcontent">\
							<h5>IP：' + host.hostIP + '</h5>\
						</div>\
					</div>\
				</div>';
                });

                $("#tagdiv").append('<div class="area" id=' + pool.poolUuid + '><h4><a>资源池:' + decodeURIComponent(pool.poolName) + '</a></h4>\
						<div class="inner">\
						<div class="resource">\
							<div class="poolclass" >\
							    <div class="pool-item">\
							      <h5>池管理节点：' + masterIp + '</h5>\
						          <h5>存储：' + srname + '</h5>\
							    </div>'
                    + resorceHtml
                    + '</div></div></div></div>');
            });
            $("#tagdiv").append(srhtml);
        }
    });
}
