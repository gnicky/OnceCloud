/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core;

import com.oncecloud.ha.core.refs.PoolRef;

/**
 * @author henry(wuheng09@gmail.com)
 * @date   2014年9月19日
 *
 * 配置文件
 */
public abstract class Config {

	protected final PoolRef pool;

	public Config(PoolRef pool) {
		super();
		this.pool = pool;
	}
	
	public abstract void config(String name) throws HAException;
	
}
