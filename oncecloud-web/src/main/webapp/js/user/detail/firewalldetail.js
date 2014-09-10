getFirewallBasicList();
reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var firewallId = $('#platformcontent').attr("firewallId");
    getRuleList(page, limit, "", firewallId);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="rulerow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $('#deleterule').addClass('btn-disable').attr('disabled', true);
}

$('.btn-refresh').unbind();
$('.btn-refresh').on('click', function (event) {
    getFirewallBasicList();
});

$('.rule-refresh').on('click', function (event) {
    reloadList(1);
});

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var count = 0;
    $('input[name="rulerow"]:checked').each(function () {
        count++;
    });
    if (count > 0) {
        $('#deleterule').removeClass('btn-disable').attr('disabled', false);
    }
});

$('#createrule').on('click', function (event) {
    event.preventDefault();
    $('#RuleModalContainer').load($(this).attr('url'), '', function () {
        $('#RuleModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getFirewallBasicList() {
    var firewallId = $('#platformcontent').attr("firewallId");
    $('#basic-list').html("");
    $.ajax({
        type: 'get',
        url: '/FirewallAction/BasicList',
        data: {firewallId: firewallId},
        dataType: 'json',
        success: function (obj) {
            var firewallName = decodeURIComponent(obj.firewallName);
            var ruleSize = obj.ruleSize;
            var createDate = obj.createDate;
            var showid = "fw-" + firewallId.substring(0, 8);
            $('#basic-list').html('<dt>防火墙&nbsp;ID</dt><dd><a href="javascript:void(0)">'
                + showid + '</a></dd><dt>名称</dt><dd>'
                + firewallName + '</dd><dt>规则总数</dt><dd>' + ruleSize + '</dd>'
                + '<dt>创建时间</dt><dd>' + createDate + '</dd>');
        }
    });
}

function getRuleList(page, limit, search, firewallId) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/FirewallAction/RuleList',
        data: {page: page, limit: limit, search: search, uuid: firewallId},
        dataType: 'json',
        success: function (array) {
            var totalnum = array[0].total;
            var totalp = 1;
            if (totalnum != 0) {
                totalp = Math.ceil(totalnum / limit);
            }
            options = {
                totalPages: totalp
            };
            $('#pageDivider').bootstrapPaginator(options);
            pageDisplayUpdate(page, totalp);
            if (array[0].confirm == 0) {
                $('#confirm').removeClass('btn-default').addClass('btn-primary');
                $('#suggestion').show();
            }
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var ruleId = obj.ruleId;
                var ruleName = decodeURIComponent(obj.ruleName);
                var rulePriority = obj.rulePriority;
                var ruleProtocol = obj.ruleProtocol;
                var ruleSport = obj.ruleSport;
                var ruleEport = obj.ruleEport;
                var ruleIp = obj.ruleIp;
                var ruleState = obj.ruleState;
                var opStr = '';
                var trClass = "";
                if (ruleState == 1) {
                    opStr = '<td class="operate"><a href="javascript:void(0)">禁用</a></td>';
                } else if (ruleState == 0) {
                    opStr = '<td class="operate"><a href="javascript:void(0)">启用</a></td>';
                    trClass = 'class="idle"';
                }
                if (ruleIp == "") {
                    ruleIp = "所有地址";
                }
                var thistr = '<tr ruleid="' + ruleId + '" state="' + ruleState + '"' + trClass + '><td class="rcheck"><input type="checkbox" name="rulerow"></td><td name="rulename">'
                    + ruleName + '</td><td name="priority">' + rulePriority + '</td><td name="protocol">'
                    + ruleProtocol + '</td><td name="sport">' + ruleSport + '</td><td name="eport">'
                    + ruleEport + '</td><td name="ip">' + ruleIp + '</td>' + opStr + '</tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

$('#deleterule').on('click', function (event) {
    event.preventDefault();
    var firewallId = $('#platformcontent').attr("firewallId");
    $('input[name="rulerow"]:checked').each(function () {
        var ruleId = $(this).parent().parent().attr("ruleid");
        deleteRule(ruleId, firewallId);
    });
    removeAllCheck();
});

function deleteRule(ruleId, firewallId) {
    var thistr = $("#tablebody").find('[ruleid="' + ruleId + '"]');
    $.ajax({
        type: 'post',
        url: '/FirewallAction/DeleteRule',
        data: {ruleId: ruleId, firewallId: firewallId},
        dataType: 'json',
        success: function (obj) {
            if (obj.result == true) {
                $(thistr).remove();
                $('#deleterule').addClass('btn-disable').attr('disabled', true);
                $("#confirm").removeClass('btn-default').addClass('btn-primary');
                $("#suggestion").show();
                getFirewallBasicList();
            }
        }
    });
}

$('#confirm').on('click', function (event) {
    event.preventDefault();
    var firewallId = $('#platformcontent').attr("firewallId");
    updateFirewall(firewallId);
    removeAllCheck();
});

function updateFirewall(firewallId) {
    $.ajax({
        type: 'get',
        url: '/FirewallAction/UpdateFirewall',
        data: {firewallId: firewallId},
        dataType: 'json',
        success: function (obj) {
            if (obj.result == true) {
                $("#confirm").removeClass('btn-primary').addClass('btn-default');
                $("#suggestion").hide();
            }
        }
    });
}

$('#tablebody').on('click', '.operate', function (event) {
    event.preventDefault();
    var thistd = $(this);
    var ruleId = $(this).parent().attr('ruleid');
    var ruleState = $(this).parent().attr('state');
    var firewallId = $('#platformcontent').attr("firewallId");
    $.ajax({
        type: 'get',
        url: '/FirewallAction/OperateRule',
        data: {ruleId: ruleId},
        dataType: 'json',
        success: function (obj) {
            if (obj.isSuccess) {
                if (ruleState == '1') {
                    $(thistd).parent().attr('state', '0');
                    $(thistd).parent().addClass('idle');
                    $(thistd).html('<a>启用</a>');
                } else if (ruleState == '0') {
                    $(thistd).parent().attr('state', '1');
                    $(thistd).parent().removeClass('idle');
                    $(thistd).html('<a>禁用</a>');
                }
                $('#confirm').removeClass('btn-default').addClass('btn-primary');
                $('#suggestion').show();
            }
        }
    });
});