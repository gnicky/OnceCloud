package com.oncecloud.ui.action;

import java.io.UnsupportedEncodingException;

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

	@RequestMapping(value = "/SendMsg", method = {RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String sendMsg(HttpServletRequest request) throws UnsupportedEncodingException {
		String telphone =new String(request.getParameter("telphone").getBytes("ISO-8859-1"), "UTF-8");
		String content = new String(request.getParameter("content").getBytes("ISO-8859-1"), "UTF-8");
		return this.getSendMsgApi().SenMessageGBK(telphone, content);
	}
	
	@RequestMapping(value = "/SendEmail", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String sendEmail(HttpServletRequest request) throws UnsupportedEncodingException {
		String subject = new String(request.getParameter("subject").getBytes("ISO-8859-1"), "UTF-8");
		String content = new String(request.getParameter("content").getBytes("ISO-8859-1"), "UTF-8");
		String toAddress = new String(request.getParameter("toaddress").getBytes("ISO-8859-1"), "UTF-8");
		this.getSendEmailApi().sendEmail(subject, content, toAddress);
		return "Ok";
	}
}