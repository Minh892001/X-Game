/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: AwakenMap
 * 功能描述：
 * 文件名：AwakenMap.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import java.util.LinkedHashMap;

/**
 * 武将觉醒集合实现类
 * 
 * @author zwy
 * @since 2015-11-17
 * @version 1.0
 */
public class AwakenMap extends LinkedHashMap<String, Integer> {
	private static final long serialVersionUID = -8528463763839056521L;

	/**
	 * 同KEY进行数据合并
	 * 
	 * @param code
	 * @param value
	 */
	public void combine(String code, int value) {
		put(code, get(code) + value);
	}

	@Override
	public Integer get(Object key) {
		if (!containsKey(key)) {
			return 0;
		}
		return super.get(key);
	}
}
