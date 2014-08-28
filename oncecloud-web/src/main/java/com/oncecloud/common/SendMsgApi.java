package com.oncecloud.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

// 发送短信API
public class SendMsgApi {
	// 用户ID
	private String uid = "beyondcent";
	// 密码
	private String key = "40690A648F5AF1E440C124D298239434";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SendMsgApi() {
	}

	public SendMsgApi(String uid, String key) {
		this.setUid(uid);
		this.setKey(key);
	}

	// GBK编码发送短信
	public String SenMessageGBK(String telphone, String strcontent) {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("Uid", this.getUid()),
					new NameValuePair("KeyMD5", this.getKey()), new NameValuePair("smsMob", telphone),
					new NameValuePair("smsText", strcontent) };
			post.setRequestBody(data);
			client.executeMethod(post);
			String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
			System.out.println(result); // 打印返回消息状态
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "-1000";
		} finally {
			post.releaseConnection();
		}
	}
}
