var map;
$(function () {
    /** 百度地图API功能 **/
    map = new BMap.Map("allmap", {minZoom: 10, maxZoom: 17});            // 创建Map实例
    var point = new BMap.Point(120.722, 31.299); // 创建点坐标
    map.centerAndZoom(point, 13);                 // 初始化地图,设置中心点坐标和地图级别。
    map.enableScrollWheelZoom();                 //启用滚轮放大缩小
    map.setCurrentCity("苏州");

    ShowCompany();


});

///展示 公司/用户列表
function ShowCompany() {
    // 随机向地图添加25个标注
    var bounds = map.getBounds();
    var sw = bounds.getSouthWest();
    var ne = bounds.getNorthEast();
    var lngSpan = Math.abs(sw.lng - ne.lng);
    var latSpan = Math.abs(ne.lat - sw.lat);

    $.ajax({
        type: 'get',
        url: '/UserAction/Companylist',
        dataType: 'json',
        success: function (response) {
            if (response.length > 0) {
                $.each(response, function (index, item) {
                    var point = new BMap.Point(sw.lng + lngSpan * (Math.random() * 0.7), ne.lat - latSpan * (Math.random() * 0.7));
                    var content = "<div class='map_info'><span><a target='_blank' href='/companymap/detail?cid=" + item.userid + "'>" + item.usedVM + "</a> 台虚拟机</span><span><a target='_blank' href='/companymap/detail?cid=" + item.userid + "'>" + item.usedDiskS + "</a> G存储空间</span><span><a target='_blank' href='/companymap/detail?cid=" + item.userid + "'>" + item.usedIP + "</a>  公网IP</span><span><a target='_blank' href='/companymap/detail?cid=" + item.userid + "'>" + item.usedBandwidth + "</a> M带宽</span></div>";
                    addMarker(point, decodeURIComponent(item.usercom), content);
                });
            }
        },
        error: function () {

        }
    });
}

// 编写自定义函数,创建标注
function addMarker(point, title, content) {
    var myIcon = new BMap.Icon("../../img/mapico2.png", new BMap.Size(32, 44));
    var marker = new BMap.Marker(point, {icon: myIcon});
    map.addOverlay(marker);

    var strcontent = content;
//创建信息窗口
    var searchInfoWindow = new BMapLib.SearchInfoWindow(map, strcontent, {
        /*  width : 200,     // 信息窗口宽度
         height: 60,     // 信息窗口高度
         */      title: title, // 信息窗口标题
        // enableMessage:false,//设置允许信息窗发送短息
        enableAutoPan: true, //自动平移
        enableSendToPhone: false, //是否启用发送到手机
        searchTypes: [
        ]
    });
    marker.addEventListener("click", function () {
        searchInfoWindow.open(this);
    });
}