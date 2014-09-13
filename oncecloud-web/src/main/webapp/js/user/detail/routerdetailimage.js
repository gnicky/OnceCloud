///add by cyh 
$(function(){
	init();
})


function init() {
    $('.view-types').on('click', '.view-type', function (event) {
        event.preventDefault();
        $('a', $('.view-types')).removeClass('current');
        $(this).addClass('current');
        var type = $(this).attr("oc-type");
        if (type == 'text') {
            $("#textview").show();
            $("#imageview").hide();
        } else {
            $("#textview").hide();
            $("#imageview").show();
        }
    });

    $(".graph-actions a").mouseenter(function () {
        if (!$(this).hasClass("btn-forbidden")) {
            $(this).find(".text").show();
        }
    }).mouseleave(function () {
        $(this).find(".text").hide();
    });

  


}
