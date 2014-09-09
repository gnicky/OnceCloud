package com.oncecloud.ui.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.alipay.config.AlipayConfig;
import com.oncecloud.alipay.util.AlipayNotify;
import com.oncecloud.alipay.util.AlipaySubmit;
import com.oncecloud.common.SendEmailApi;
import com.oncecloud.common.SendMsgApi;
import com.oncecloud.dao.ChargeDAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.entity.ChargeRecord;
import com.oncecloud.entity.User;

@RequestMapping("/alipay")
@Controller
public class AliPayAction {
	
	private ChargeDAO chargeDAO;
    private UserDAO userDAO;
    private SendEmailApi sendEmailApi;
    private SendMsgApi sendMsgApi;


	public ChargeDAO getChargeDAO() {
		return chargeDAO;
	}

	@Autowired
	public void setChargeDAO(ChargeDAO chargeDAO) {
		this.chargeDAO = chargeDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public SendEmailApi getSendEmailApi() {
		return sendEmailApi;
	}

	@Autowired
	public void setSendEmailApi(SendEmailApi sendEmailApi) {
		this.sendEmailApi = sendEmailApi;
	}

	public SendMsgApi getSendMsgApi() {
		return sendMsgApi;
	}

	@Autowired
	public void setSendMsgApi(SendMsgApi sendMsgApi) {
		this.sendMsgApi = sendMsgApi;
	}

	@RequestMapping(value = "/SubmitAlipay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String submitAlipay(HttpServletRequest request) throws UnsupportedEncodingException {
		// 商户订单号
		String out_trade_no = UUID.randomUUID().toString(); 
		// 订单名称
		String subject = "充值";
		// 付款金额
		String total_fee = new String(request.getParameter("bill_number").getBytes("ISO-8859-1"), "UTF-8");
		// 订单描述
		String body = "自助充值";
		// 商品展示地址
		String show_url = "http://www.bncloud.com.cn"; 
		// 防钓鱼时间戳
		//String anti_phishing_key = "";
		// 客户端的IP地址,非局域网的外网IP地址，如：221.0.0.1
		//String exter_invoke_ip = "";
		User user = (User) request.getSession().getAttribute("user");
		double totalFee = Double.parseDouble(total_fee);
		this.getChargeDAO().createChargeRecord(out_trade_no, totalFee, 0,
				new Date(), user.getUserId(), 0);
		
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url); // AlipayConfig.notify_url
		// sParaTemp.put("return_url", "");
		sParaTemp.put("seller_email", AlipayConfig.seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		//sParaTemp.put("anti_phishing_key", anti_phishing_key);
		//sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

		// 建立请求
		return AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
	}
	
	@RequestMapping(value = "/NotifyUrl", method = { RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String notifyUrl(HttpServletRequest request) throws UnsupportedEncodingException{
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		
		if(AlipayNotify.verify(params)){//验证成功
			if(trade_status.equals("TRADE_FINISHED")){
			    ChargeRecord chargerecord = this.getChargeDAO().getChargeRecord(out_trade_no);
				if(chargerecord!=null)
				{
					User user = this.getUserDAO().getUser(chargerecord.getRecordUID());
					if(user!=null)
					{
						double bill = chargerecord.getRecordBill();
						this.getUserDAO().updateBalance(user.getUserId(), bill);
						this.getChargeDAO().updateChargeRecord(out_trade_no, 1);
						this.getSendEmailApi().sendEmail("客户充值", "客户充值"+bill+"元，充值成功", user.getUserMail());
						this.getSendMsgApi().SenMessageGBK(user.getUserPhone(), "客户充值"+bill+"元，充值成功");
					}
				}
			} else if (trade_status.equals("TRADE_SUCCESS")){
				ChargeRecord chargerecord = this.getChargeDAO().getChargeRecord(out_trade_no);
				if(chargerecord!=null)
				{
					User user = this.getUserDAO().getUser(chargerecord.getRecordUID());
					if(user!=null)
					{
						double bill = chargerecord.getRecordBill();
						this.getUserDAO().updateBalance(user.getUserId(), bill);
						this.getChargeDAO().updateChargeRecord(out_trade_no, 1);
						this.getSendEmailApi().sendEmail("客户充值", "客户充值"+bill+"元，充值成功", user.getUserMail());
						this.getSendMsgApi().SenMessageGBK(user.getUserPhone(), "客户充值"+bill+"元，充值成功");
					}
				}
			}
			return "success";	//请不要修改或删除
		}else{//验证失败
			return "fail";
		}
	}
}