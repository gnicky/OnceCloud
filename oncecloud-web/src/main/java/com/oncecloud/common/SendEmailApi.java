package com.oncecloud.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

// 发送邮件API
@Component
public class SendEmailApi {
	private String smtpServer = "smtp.exmail.qq.com";
	private String sendAddress = "notice@bncloud.com.cn";
	private String sendUserName = "notice@bncloud.com.cn";
	private String sendPassword = "Cloudshare#1";

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendPassword() {
		return sendPassword;
	}

	public void setSendPassword(String sendPassword) {
		this.sendPassword = sendPassword;
	}

	public void sendEmail(String subject, String content, String toAddress) {
		try {
			if (toAddress != null && toAddress != "") {
				String[] toAddresseStrings = toAddress.split(",");
				Properties props = System.getProperties();
				props.setProperty("mail.smtp.host", this.getSmtpServer());
				props.put("mail.smtp.auth", true);
				Session session = Session.getInstance(props);
				MimeMessage message = new MimeMessage(session);
				InternetAddress from = new InternetAddress(this.getSendAddress());
				message.setFrom(from);
				InternetAddress[] tosAddresses = new InternetAddress[toAddresseStrings.length];
				for (int i = 0; i < tosAddresses.length; i++) {
					tosAddresses[i] = new InternetAddress(toAddresseStrings[i]);
				}
				message.setRecipients(Message.RecipientType.TO, tosAddresses);
				message.setSubject(subject);
				message.setContent(content, "text/html;charset=GBK");
				message.saveChanges();
				Transport transport = session.getTransport("smtp");
				transport.connect(this.getSmtpServer(), this.getSendUserName(), this.getSendPassword());
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}
		} catch (Exception ex) {
			System.out.println("邮件错误");
		}
	}
}
