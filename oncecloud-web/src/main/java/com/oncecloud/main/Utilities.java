package com.oncecloud.main;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

/**
 * @author hehai yly
 * @version 2013/11/22
 */
public class Utilities {

	// 将时间戳转换成日期和时间
	public static String Timestamp2DateTime(long timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = format.format(timestamp);
		return dateTime;
	}

	// 将日期和时间转换成时间戳
	public static long DateTime2Timestamp(String dateTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(dateTime);
		return date.getTime();
	}
	
	// 格式化日期和时间
	public static String formatTime(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dateTime);
	}
	
	public static Date String2Month(String month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = format.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 保留小数点后两位的小数
	public static double Round2(double value) {
		return (double) (Math.round(value * 100) / 100.0);
	}

	public static Date AddMinuteForDate(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static Date AddMonthForDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	public final static String MD5(String pwd) {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
			byte[] btInput = pwd.getBytes();

			// 获得指定摘要算法的 MessageDigest对象，此处为MD5
			// MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <initialized>

			// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
			mdInst.update(btInput);
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <in progress>

			// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
			byte[] md = mdInst.digest();
			// System.out.println(md);

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			// System.out.println(j);
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) { // i = 0
				byte byte0 = md[i]; // 95
				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
				str[k++] = md5String[byte0 & 0xf]; // F
			}

			// 返回经过加密后的字符串
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String stickyToSuccess(String msg) {
		return "<div class='alert alert-success'>" + msg + "</div>";
	}

	public static String stickyToInfo(String msg) {
		return "<div class='alert alert-info'>" + msg + "</div>";
	}

	public static String stickyToError(String msg) {
		return "<div class='alert alert-danger'>" + msg + "</div>";
	}

	public static String stickyToWarning(String msg) {
		return "<div class='alert alert-warning'>" + msg + "</div>";
	}

	public static String dateToUsed(Date createDate) {
		Date current = new Date();
		long elapse = current.getTime() - createDate.getTime();
		long day = elapse / (24 * 60 * 60 * 1000);
		long hour = (elapse / (60 * 60 * 1000) - day * 24);
		long min = ((elapse / (60 * 1000)) - day * 24 * 60 - hour * 60);
		if (day > 0) {
			return day + " 天";
		} else if (hour > 0) {
			return hour + " 小时";
		} else if (min > 0) {
			return min + " 分钟";
		} else {
			return "<１分钟";
		}
	}

	public static int timeElapse(Date startTime, Date endTime) {
		long elapse = endTime.getTime() - startTime.getTime();
		int elapseInt = 0;
		if (elapse > 0) {
			elapseInt = (int) elapse / 1000;
		}
		return elapseInt;
	}

	public static String getSummary(String input) {
		if (input.length() > 10) {
			return input.substring(0, 10) + "...";
		} else {
			return input;
		}
	}

	// get random mac
	public static String randomMac() {
		String macAddr = "00:16:3e";
		int min = 0x00;
		int max = 0x7f;
		Random random = new Random(System.currentTimeMillis());
		int addr = random.nextInt(max - min + 1) + min;
		String addrStr = String.format("%02x", addr);
		macAddr += (":" + addrStr);
		max = 0xff;
		for (int i = 0; i < 2; i++) {
			addr = random.nextInt(max - min + 1) + min;
			addrStr = String.format("%02x", addr);
			macAddr += (":" + addrStr);
		}
		return macAddr;
	}
	
	public static JSONObject createLogInfo(String key, String value) {
		JSONObject logInfo = new JSONObject();
		try {
			logInfo.put("key", URLEncoder.encode(key, "utf-8"));
			logInfo.put("value", URLEncoder.encode(value, "utf-8").replace("+", "%20"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logInfo;
	}
	
	public static String encodeText(String name) {
		if(name==null || name=="" || name.length()==0)
		{
			return "";
		}
		String result = "";
		try {
			result = URLEncoder.encode(name, "utf-8").replace("+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean hasForbiddenChar(String str) {
		Pattern pattern = Pattern.compile("[`~!$%^&#*()+|\\\\\\]\\[\\]\\{\\}:;'\\,<>?]+");
		Matcher m = pattern.matcher(str);
		return m.find();
	}
}
