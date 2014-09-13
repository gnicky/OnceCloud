
$(function () {
	doshow();
    
    $("#userId").change(function(){
    	doshow();
    });
});


function doshow(){
	
	 $('#picnet').highcharts({
	        title: {
	            text: '网络带宽使用变化',
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
	                text: '网络带宽 (MB)'
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
	            name: '带宽',
	            data: [2, 4, 1,7, 2,10,3, 5,9]
	        }]
	    });
	    
	    
	    $('#piccpu').highcharts({
	        title: {
	            text: 'CPU平均使用率变化',
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
	                text: 'CPU使用率 (%)'
	            },
	            allowDecimals:true,
	            max:100,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: '%'
	        },
	       /* legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },*/
	        series: [{
	            name: '使用率',
	            data: [73, 85, 100, 91, 53, 68,63, 43,58]
	        }]
	    });
	    
	    $('#picmemory').highcharts({
	        title: {
	            text: '内存使用变化',
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
	                text: '使用内存 (GB)'
	            },
	            allowDecimals:false,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: 'GB'
	        },
	       /* legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },*/
	        series: [{
	            name: '使用大小',
	            data: [7, 5, 10, 9, 3, 6,13, 13,5]
	        }]
	    });
	    
	    $('#picdisk').highcharts({
	        title: {
	            text: '硬盘使用变化',
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
	                text: '使用硬盘(GB)'
	            },
	            allowDecimals:false,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: 'GB'
	        },
	       /* legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },*/
	        series: [{
	            name: '使用大小',
	            data: [70, 155, 110, 79, 93, 86,130, 113,170]
	        }]
	    });
	    
	    
	    $('#picmoney').highcharts({
	        title: {
	            text: '财务报表',
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
	                text: '金额(元)'
	            },
	            allowDecimals:false,
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: '元'
	        },
	       /* legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },*/
	        series: [{
	            name: '金额',
	            data: [7000, 5000, 10000, 9000, 3900, 6100,13000, 13100,9500]
	        }]
	    });
	    
}