$(document).ready(function () {
    //基本连接线样式
    var connectorPaintStyle = {
        lineWidth: 4,
        strokeStyle: "#61B7CF",
        joinstyle: "round",
        //outlineColor: "white",
        outlineWidth: 2
    };
    // 鼠标悬浮在连接线上的样式
    var connectorHoverStyle = {
        lineWidth: 4,
        strokeStyle: "#216477",
        outlineWidth: 2
        //outlineColor: "white"
    };

    var hollowCircle = {
        endpoint: ["Dot", { radius: 8 }],  //端点的形状
        connectorStyle: connectorPaintStyle,//连接线的颜色，大小样式
        connectorHoverStyle: connectorHoverStyle,
        paintStyle: {
            strokeStyle: "#1e8151",
            fillStyle: "transparent",
            radius: 2,
            lineWidth: 2
        },        //端点的颜色样式
        //anchor: "AutoDefault",
        isSource: true,    //是否可以拖动（作为连线起点）
        connector: ["Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true }],  //连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
        isTarget: true,    //是否可以放置（连线终点）
        maxConnections: -1,    // 设置连接点最多可以连接几条线
        connectorOverlays: [
            ["Arrow", { width: 10, length: 10, location: 1 }]
        ]
    };

    var i = 0;

    $.ajax({
        type: 'post',
        url: '/RackAction/RackDetail',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                var rack = array[0];
                $.each(rack.hostlist, function (index, host) {
                    var hostobj = eval('(' + host.hostobj + ')');
                    var id = hostobj.hostUuid;
                    $("#right").append('<div class="node2"   id="' + id + '" >' + decodeURIComponent(hostobj.hostName) + '</div>');
                    $("#" + id).css("left", host.hostX).css("top", host.hostY);
                    /* jsPlumb.addEndpoint(id, { anchors: "TopCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "RightMiddle" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "BottomCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "LeftMiddle" }, hollowCircle);*/
                    jsPlumb.draggable(id);
                    $("#" + id).draggable({ containment: "parent" });
                });

                $.each(rack.storagelist, function (index, storage) {
                    var srobj = eval('(' + storage.srobj + ')');
                    var id = srobj.srUuid;
                    $("#right").append('<div class="node2"   id="' + id + '" >' + decodeURIComponent(srobj.srName) + '</div>');
                    $("#" + id).css("left", storage.srX).css("top", storage.srY);
                    /* jsPlumb.addEndpoint(id, { anchors: "TopCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "RightMiddle" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "BottomCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "LeftMiddle" }, hollowCircle);*/
                    jsPlumb.draggable(id);
                    $("#" + id).draggable({ containment: "parent" });
                });

                $.each(rack.switchlist, function (index, switchobj) {
                    var switchobject = eval('(' + switchobj.SwitchObj + ')');
                    var id = switchobject.swUuid;
                    $("#right").append('<div class="node2"   id="' + id + '" >' + decodeURIComponent(switchobject.swName) + '</div>');
                    $("#" + id).css("left", switchobj.SwitchX).css("top", switchobj.SwitchY);
                    /*  jsPlumb.addEndpoint(id, { anchors: "TopCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "RightMiddle" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "BottomCenter" }, hollowCircle);
                     jsPlumb.addEndpoint(id, { anchors: "LeftMiddle" }, hollowCircle);*/
                    jsPlumb.draggable(id);
                    $("#" + id).draggable({ containment: "parent" });
                });

                $.each(rack.connectlist, function (index, connectobj) {
                    /*var begin = connectobj.startObj + new Date().getTime();
                     var end = connectobj.endObj + new Date().getTime();
                     jsPlumb.addEndpoint(connectobj.startObj, { uuid: begin}, hollowCircle);
                     jsPlumb.addEndpoint(connectobj.endObj, { uuid: end }, hollowCircle);
                     jsPlumb.connect({uuids:[begin, end]});*/

                    jsPlumb.connect({source: $("#" + connectobj.startObj),
                        target: $("#" + connectobj.endObj),
                        endpoint: ["Dot", { radius: 8 }],  //端点的形状

                        paintStyle: connectorPaintStyle,        //端点的颜色样式
                        //anchor: "AutoDefault",
                        hoverPaintStyle: connectorHoverStyle,
                        connector: ["Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true }],  //连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
                        endpointStyle: {
                            strokeStyle: "#1e8151",
                            fillStyle: "transparent",
                            radius: 2,
                            lineWidth: 2
                        },
                        overlays: [
                            ["Arrow", { width: 10, length: 10, location: 1 }]
                        ]
                    });

                });


            }
        }
    });
});