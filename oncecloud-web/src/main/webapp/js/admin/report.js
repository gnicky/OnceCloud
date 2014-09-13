$(function () {
    $('#mainreport').highcharts({
        title: {
            text: '虚拟机数目变化',
            x: -20 //center
        },
       /* subtitle: {
            text: 'Source: WorldClimate.com',
            x: -20
        },*/
        xAxis: {
            categories: ['201401', '201402', '201403', '201404', '201405', '201406','201407', '201408', '201409']
        },
        yAxis: {
            title: {
                text: '虚拟机数量 (台)'
            },
            allowDecimals:false,
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '台'
        },
       /* legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },*/
        series: [{
            name: '数量',
            data: [7, 5, 10, 9, 3, 6,13, 13,5]
        }]
    });
});
