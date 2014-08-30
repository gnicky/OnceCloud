package com.oncecloud.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oncecloud.common.SendEmailApi;
import com.oncecloud.common.SendMsgApi;

/**
 * @author cyh
 * @version 2014/07/08
 */
public class ServerApiAction extends HttpServlet {
	private static final long serialVersionUID = -1944929235768258466L;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("SendMsg")) {
				String telphone = request.getParameter("telphone");
				String content = request.getParameter("content");
				new SendMsgApi().SenMessageGBK(telphone, content);
			} else if (action.equals("SendEmail")) {
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				String toAddress = request.getParameter("toaddress");
				new SendEmailApi().sendEmail(subject, content, toAddress);
			}
		}
	}
}
