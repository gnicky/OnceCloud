/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core;

/**
 * @author henry
 * @date   2014年9月19日
 *
 */
public class HAException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3305021526905369282L;

	public HAException() {
		super();
	}


	public HAException(String message, Throwable cause) {
		super(message, cause);
	}

	public HAException(String message) {
		super(message);
	}

	public HAException(Throwable cause) {
		super(cause);
	}

}
