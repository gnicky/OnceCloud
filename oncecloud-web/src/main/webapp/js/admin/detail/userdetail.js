getBasicUser();
getUserQuota();

$('.btn-refresh').unbind();
$('#quota-fresh').on('click', function (event) {
    event.preventDefault();
    $('#quota_update').attr('state', 'edit');
    $('#quota_update').removeClass('btn-primary').addClass('btn-default');
    $('#quota_update').text('修改配额');
    getUserQuota();
});

$('#basic-fresh').on('click', function (event) {
    event.preventDefault();
    getBasicUser();
});

function getBasicUser() {
    var userid = $('#platformcontent').attr('userid');
    $('#basic-list').html("");
    $.ajax({
        type: 'get',
        url: '/UserAction/OneUser',
        data: {userid: userid},
        dataType: 'json',
        success: function (obj) {
            var username = decodeURIComponent(obj.username);
            var userid = obj.userid;
            var usercom = decodeURIComponent(obj.usercom);
            var userdate = obj.userdate;
            var userlevel = obj.userlevel;
            var usermail = obj.usermail;
            var userphone = obj.userphone;
            var balance = obj.balance.toFixed(2);
            var levelstr = "<a><span id='userlevel' level='1' class='glyphicon glyphicon-user' style='margin-right:7px'></span>平台用户</a>";
            if (userlevel == 0) {
                levelstr = "<a><span id='userlevel' level='0' class='glyphicon glyphicon-star' style='margin-right:7px'></span>管理员</a>";
            }
            $('#basic-list').html('<dt>用户名</dt><dd id="username">'
                + username + '</dd><dt>邮箱</dt><dd id="usermail">'
                + usermail + '</dd><dt>联系电话</dt><dd id="userphone">'
                + userphone + '</dd><dt>公司</dt><dd id="usercom">'
                + usercom + '</dd><dt>级别</dt><dd id="leveltd">'
                + levelstr + '</dd><dt>余额</dt><dd>￥'
                + balance + '</dd><dt>注册时间</dt><dd class="time">'
                + userdate + '</dd>');
        }
    });
}

function getUserQuota() {
    var userid = $('#platformcontent').attr('userid');
    $.ajax({
        type: 'get',
        url: '/UserAction/UserQuota',
        data: {userid: userid},
        dataType: 'json',
        success: function (obj) {
            var qU = obj.quotaU;
            var qT = obj.quotaT;
            $('#quotaid').val(qT.quotaID);
            $('#eipU').text(qU.quotaIP);
            $('#vmU').text(qU.quotaVM);
            $('#bkU').text(qU.quotaSnapshot);
            $('#imgU').text(qU.quotaImage);
            $('#volU').text(qU.quotaDiskN);
            $('#sshU').text(qU.quotaSsh);
            $('#fwU').text(qU.quotaFirewall);
            $('#rtU').text(qU.quotaRoute);
            $('#vlanU').text(qU.quotaVlan);
            $('#lbU').text(qU.quotaLoadBalance);
            $('#diskU').text(qU.quotaDiskS);
            $('#bwU').text(qU.quotaBandwidth);
            $('#memU').text(qU.quotaMemory);
            $('#cpuU').text(qU.quotaCpu);
            $('#eipT').text(qT.quotaIP);
            $('#vmT').text(qT.quotaVM);
            $('#bkT').text(qT.quotaSnapshot);
            $('#imgT').text(qT.quotaImage);
            $('#volT').text(qT.quotaDiskN);
            $('#sshT').text(qT.quotaSsh);
            $('#fwT').text(qT.quotaFirewall);
            $('#rtT').text(qT.quotaRoute);
            $('#vlanT').text(qT.quotaVlan);
            $('#lbT').text(qT.quotaLoadBalance);
            $('#diskT').text(qT.quotaDiskS);
            $('#bwT').text(qT.quotaBandwidth);
            $('#memT').text(qT.quotaMemory);
            $('#cpuT').text(qT.quotaCpu);
            $('#eipT').attr('old', qT.quotaIP);
            $('#vmT').attr('old', qT.quotaVM);
            $('#bkT').attr('old', qT.quotaSnapshot);
            $('#imgT').attr('old', qT.quotaImage);
            $('#volT').attr('old', qT.quotaDiskN);
            $('#sshT').attr('old', qT.quotaSsh);
            $('#fwT').attr('old', qT.quotaFirewall);
            $('#rtT').attr('old', qT.quotaRoute);
            $('#vlanT').attr('old', qT.quotaVlan);
            $('#lbT').attr('old', qT.quotaLoadBalance);
            $('#diskT').attr('old', qT.quotaDiskS);
            $('#bwT').attr('old', qT.quotaBandwidth);
            $('#memT').attr('old', qT.quotaMemory);
            $('#cpuT').attr('old', qT.quotaCpu);
        }
    });
}

$('#modify').on('click', function (event) {
    event.preventDefault();
    $('#UserModalContainer').load($(this).attr('url'), '', function () {
        $('#UserModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#quota_update').on('click', function (event) {
    event.preventDefault();
    var state = $(this).attr('state');
    if ('edit' == state) {
        var eip = $('#eipT').text();
        var vm = $('#vmT').text();
        var bk = $('#bkT').text();
        var img = $('#imgT').text();
        var vol = $('#volT').text();
        var ssh = $('#sshT').text();
        var fw = $('#fwT').text();
        var rt = $('#rtT').text();
        var vlan = $('#vlanT').text();
        var lb = $('#lbT').text();
        var disk = $('#diskT').text();
        var bw = $('#bwT').text();
        var mem = $('#memT').text();
        var cpu = $('#cpuT').text();
        $('#eipT').html('<input type="text" class="mini valid" id="eipTc" value="' + eip + '" autofocus="">');
        $('#vmT').html('<input type="text" class="mini valid" value="' + vm + '" id="vmTc" autofocus="">');
        $('#bkT').html('<input type="text" class="mini valid" value="' + bk + '" id="bkTc" autofocus="">');
        $('#imgT').html('<input type="text" class="mini valid" value="' + img + '" id="imgTc" autofocus="">');
        $('#volT').html('<input type="text" class="mini valid" value="' + vol + '" id="volTc" autofocus="">');
        $('#sshT').html('<input type="text" class="mini valid" value="' + ssh + '" id="sshTc" autofocus="">');
        $('#fwT').html('<input type="text" class="mini valid" value="' + fw + '" id="fwTc" autofocus="">');
        $('#rtT').html('<input type="text" class="mini valid" value="' + rt + '" id="rtTc" autofocus="">');
        $('#vlanT').html('<input type="text" class="mini valid" value="' + vlan + '" id="vlanTc" autofocus="">');
        $('#lbT').html('<input type="text" class="mini valid" value="' + lb + '" id="lbTc" autofocus="">');
        $('#diskT').html('<input type="text" class="mini valid" value="' + disk + '" id="diskTc" autofocus="">');
        $('#bwT').html('<input type="text" class="mini valid" value="' + bw + '" id="bwTc" autofocus="">');
        $('#memT').html('<input type="text" class="mini valid" value="' + mem + '" id="memTc" autofocus="">');
        $('#cpuT').html('<input type="text" class="mini valid" value="' + cpu + '" id="cpuTc" autofocus="">');
        $(this).attr('state', 'save');
        $(this).removeClass('btn-default').addClass('btn-primary');
        $(this).text('保存');
    } else if ('save' == state) {
        var userid = $('#platformcontent').attr('userid');
        var quotaid = $('#quotaid').val();
        var eip = $('#eipTc').val();
        var vm = $('#vmTc').val();
        var bk = $('#bkTc').val();
        var img = $('#imgTc').val();
        var vol = $('#volTc').val();
        var ssh = $('#sshTc').val();
        var fw = $('#fwTc').val();
        var rt = $('#rtTc').val();
        var vlan = $('#vlanTc').val();
        var lb = $('#lbTc').val();
        var disk = $('#diskTc').val();
        var bw = $('#bwTc').val();
        var mem = $('#memTc').val();
        var cpu = $('#cpuTc').val();
        $.ajax({
            type: 'post',
            url: '/UserAction/QuotaUpdate',
            data: {
                changerId: userid,
                quotaid: quotaid,
                eip: eip,
                vm: vm,
                bk: bk,
                img: img,
                vol: vol,
                ssh: ssh,
                fw: fw,
                rt: rt,
                vlan: vlan,
                lb: lb,
                disk: disk,
                bw: bw,
                me: mem,
                cpu: cpu
            },
            dataType: 'text',
            success: function () {
                $('#quota_update').attr('state', 'edit');
                $('#quota_update').removeClass('btn-primary').addClass('btn-default');
                $('#quota_update').text('修改配额');
                $('#eipT').text(eip);
                $('#vmT').text(vm);
                $('#bkT').text(bk);
                $('#imgT').text(img);
                $('#volT').text(vol);
                $('#sshT').text(ssh);
                $('#fwT').text(fw);
                $('#rtT').text(rt);
                $('#vlanT').text(vlan);
                $('#lbT').text(lb);
                $('#diskT').text(disk);
                $('#bwT').text(bw);
                $('#memT').text(mem);
                $('#cpuT').text(cpu);
            }
        });
    }
});

$('#quota-form').on('change', '.mini', function (event) {
    event.preventDefault();
    var value = $(this).val();
    if (!(/^(\+|-)?\d+$/.test(value)) || value < 0) {
        value = $(this).parent().attr('old');
        $(this).val(value);
    }
});