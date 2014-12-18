/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

/**
 * @author henry
 * @date   2014年9月24日
 *
 */
public class ObjectUtils {

	public static boolean invalid(Object obj) {
		return (obj == null) ? true : false;
	}

	public static boolean valid(Object obj) {
		return !invalid(obj);
	}
}
