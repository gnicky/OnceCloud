package com.oncecloud.common.helper;

import java.util.Calendar;
import java.util.Date;

public final class Helper {
	private Helper() {

	}

	public static double roundTwo(double value) {
		return (double) (Math.round(value * 100) / 100.0);
	}

	public static Date addMinuteForDate(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static String stickyToSuccess(String msg) {
		return "<div class='alert alert-success'>" + msg + "</div>";
	}
}
