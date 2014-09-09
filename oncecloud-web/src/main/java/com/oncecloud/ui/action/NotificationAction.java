package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.common.SendEmailApi;
import com.oncecloud.common.SendMsgApi;

@RequestMapping("/NotificationAction")
@Controller
public class NotificationAction {
    private SendEmailApi sendEmailApi;
    private SendMsgApi sendMsgApi;

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

	@RequestMapping(value = "/SendMsg", method = { RequestMethod.POST })
	@ResponseBody
	public String sendMsg(HttpServletRequest request) {
		String telphone = request.getParameter("telphone");
		String content = request.getParameter("content");
		return this.getSendMsgApi().SenMessageGBK(telphone, content);
	}
	
	@RequestMapping(value = "/SendEmail", method = { RequestMethod.POST })
	@ResponseBody
	public String sendEmail(HttpServletRequest request) {
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		String toAddress = request.getParameter("toaddress");
		this.getSendEmailApi().sendEmail(subject, content, toAddress);
		return "Ok";
	}
}