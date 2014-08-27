package com.oncecloud.helper;

public final class Helper {
	private Helper() {

	}

	public static double roundTwo(double value) {
		return (double) (Math.round(value * 100) / 100.0);
	}
}
