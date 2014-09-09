<%
/* *
 *功能：即时到账交易接口接入页
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *************************注意*****************
 *如果您在接口集成过程中遇到问题，可以按照下面的途径来解决
 *1、商户服务中心（https://b.alipay.com/support/helperApply.htm?action=consultationApply），提交申请集成协助，我们会有专业的技术工程师主动联系您协助解决
 *2、商户帮助中心（http://help.alipay.com/support/232511-16307/0-16307.htm?sh=Y&info_type=9）
 *3、支付宝论坛（http://club.alipay.com/read-htm-tid-8681712.html）
 *如果不想使用扩展功能请把扩展功能参数赋空值。
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oncecloud.alipay.config.*"%>
<%@ page import="com.oncecloud.alipay.util.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.*"%>
<%@ page import="com.oncecloud.alipay.util.*"%>
<%@ page import="com.oncecloud.alipay.config.*"%>
<%@ page import="com.oncecloud.log.*"%>
<%@ page import="com.oncecloud.dao.ChargeDAO"%>
<%@ page import="com.oncecloud.entity.User"%>
<%@ page import="java.lang.Integer"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>支付宝即时到账交易接口</title>
	</head>
	<%
		////////////////////////////////////请求参数//////////////////////////////////////
		/* //卖家支付宝帐户
		String seller_email = new String(request.getParameter("WIDseller_email").getBytes("ISO-8859-1"),"UTF-8");
		//必填 */
		//商户订单号
		String out_trade_no = UUID.randomUUID().toString(); //new String(request.getParameter("111").getBytes("ISO-8859-1"),"UTF-8");
		//商户网站订单系统中唯一订单号，必填
		//订单名称
		String subject = "充值";//new String(request.getParameter("充值").getBytes("ISO-8859-1"),"UTF-8");
		//必填
		//付款金额
		String total_fee = new String(request.getParameter("bill_number").getBytes("ISO-8859-1"),"UTF-8");
		//必填
		//订单描述
		String body = "自助充值"; //new String(request.getParameter("111").getBytes("ISO-8859-1"),"UTF-8");
		//商品展示地址
		String show_url = "http://www.bncloud.com.cn"; //new String(request.getParameter("http://www.xxx.com/myorder.html").getBytes("ISO-8859-1"),"UTF-8");
		//需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html
		//防钓鱼时间戳
		String anti_phishing_key = "";
		//若要使用请调用类文件submit中的query_timestamp函数
		//客户端的IP地址
		String exter_invoke_ip = "";
		//非局域网的外网IP地址，如：221.0.0.1
		User user = (User) session.getAttribute("user");
		double totalFee = Double.parseDouble(total_fee);
		new ChargeDAO().createChargeRecord(out_trade_no, totalFee, 0, new Date(), user.getUserId(), 0);
		//////////////////////////////////////////////////////////////////////////////////
		
		/////todo 生成新充值订单，拿到订单号，去充值，成功之后，修改订单号
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);    //AlipayConfig.notify_url
	    //sParaTemp.put("return_url", "");
		sParaTemp.put("seller_email", AlipayConfig.seller_email);
		//System.out.print(AlipayConfig.seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		
		///add by  cyh 
		// 邮件发送
		//new SendEmailApi().sendEmail("客户充值", "客户充值,订单号："+"DD1111111"+",充值成功,充值金额100", "chenyuhe@beyondcent.com");
		
		// 短信发送
		//new SendMsgApi().SenMessageGBK("18862439260", "客户充值,订单号："+"DD1111111"+",充值成功，充值金额100");

		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		out.println(sHtmlText);
	
	%>
	<body>
	</body>
</html>
