$(document).ready(function () {

    $("#bind2rack").on('click', function (event) {
        event.preventDefault();
        var selected = $('.racklist').find('.selected');
        var showid = selected.get(0).getAttribute("showid");
        var boxes = document.getElementsByName("poolrow");
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                bind2rack(showid, $(boxes[i]).parent().parent().attr("poolid"));
            }
        }
        removeAllCheck();
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            getrackList(page, 5, "");
        },

        shouldShowPage: function (type, page, current) {
            switch (type) {
                case "first":
                case "last":
                    return false;
                default:
                    return true;
            }
        }
    }

    $('#bindP').bootstrapPaginator(options);

    var pagesbind = $('#bindP').bootstrapPaginator("getPages");
    pageDisplayUpdate(pagesbind.current, pagesbind.total);
    getrackList(1, 5, "");

    function getrackList(page, limitnum, search) {
        $.ajax({
            type: 'get',
            url: '/RackAction',
            data: 'action=getlist&page=' + page + '&limitnum=' + limitnum + '&search=' + search,
            dataType: 'json',
            success: function (array) {
                if (array.length >= 1) {
                    var totalnum = array[0];
                    var totalp = Math.ceil(totalnum / limitnum);
                    options = {
                        totalPages: totalp
                    }
                    $('#bindP').bootstrapPaginator(options);
                    pageDisplayUpdate(page, totalp);
                    var btable = document.getElementById("racklist");
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var rackname = decodeURI(obj.rackname);
                        var rackid = obj.rackid;
                        var showid = "rack-" + rackid.substring(0, 8);

                        var mytr = "";
                        if (i == 1) {
                            mytr = '<div class="image-item selected" showid="'
                                + rackid + '"><div class="image-left">' + rackname + '</div>ID:&nbsp;&nbsp;' + showid + '</div>';
                        }
                        else {
                            mytr = '<div class="image-item" showid="'
                                + rackid + '"><div class="image-left">' + rackname + '</div>ID:&nbsp;&nbsp;' + showid + '</div>';
                        }
                        tableStr += mytr;
                    }
                    $("#racklist").html(tableStr);
                }
            },
            error: function () {

            }
        });
    }

    $('.racklist').on('click', '.image-item', function (event) {
        event.preventDefault();
        $('div', $('.racklist')).removeClass('selected');
        $(this).addClass('selected');
    });

    function bind2rack(showid, poolid) {
        var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
        var thistd = thistr.find('[state]');
        $.ajax({
            type: 'get',
            url: '/PoolAction',
            data: 'action=bind&poolid=' + poolid + '&rackid=' + showid,
            dataType: 'json',
            success: function (array) {
                if (array.length == 1) {
                    var result = array[0].result;
                    var rackname = decodeURI(array[0].rackname);
                    if (result) {
                        var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
                        thistd.html('<a>' + rackname + '</a>');
                        thistd.attr("state", "loaded");
                    }
                }
            },
            error: function () {

            }
        });
    }


    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPbind");
        var t = document.getElementById("totalPbind");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    function removeAllCheck() {
        var boxes = document.getElementsByName("poolrow");
        for (var i = 0; i < boxes.length; i++) {
            boxes[i].checked = false;
            $(boxes[i]).change();
        }
    }
});