package com.oncecloud.common.helper;

import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HashHelper {
	private StringHelper stringHelper;

	private StringHelper getStringHelper() {
		return stringHelper;
	}

	@Autowired
	private void setStringHelper(StringHelper stringHelper) {
		this.stringHelper = stringHelper;
	}

	public String sha1Hash(String source) {
		try {
			String ret = new String(source);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			ret = this.getStringHelper().convertToHexString(
					md.digest(ret.getBytes()));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String md5Hash(String source) {
		try {
			String ret = new String(source);
			MessageDigest md = MessageDigest.getInstance("MD5");
			ret = this.getStringHelper().convertToHexString(
					md.digest(ret.getBytes()));
			return ret.toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
