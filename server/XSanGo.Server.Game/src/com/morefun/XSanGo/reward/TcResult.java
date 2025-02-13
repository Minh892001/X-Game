/**
 * 
 */
package com.morefun.XSanGo.reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.morefun.XSanGo.util.TextUtil;

/**
 * TC执行结果描述，可枚举奖励的类别和相应数量
 * 
 * @author sulingyun
 * 
 */
public class TcResult implements Iterable<Entry<String, Integer>> {
	private Map<String, Integer> properties;
	private String tcCode;

	public TcResult(String tcCode, Map<String, Integer> properties) {
		this.tcCode = tcCode;
		this.properties = properties;
	}

	public TcResult(String tcCode) {
		this.tcCode = tcCode;
		this.properties = new HashMap<String, Integer>();
	}

	/**
	 * 累加奖励，返回累加后的结果即自身
	 * 
	 * @param another
	 * @return
	 */
	public TcResult add(TcResult another) {
		if (another == null) {
			return this;
		}

		for (String pName : another.properties.keySet()) {
			int value = this.properties.containsKey(pName) ? this.properties
					.get(pName) : 0;
			value += another.properties.get(pName);
			this.properties.put(pName, value);
		}

		return this;
	}

	/**
	 * 生成详情描述
	 * 
	 * @return
	 */
	public String generateDetail() {
		List<String> list = new ArrayList<String>();

		for (String name : this.properties.keySet()) {
			list.add(TextUtil.format("{0}x{1}", name, this.properties.get(name)));
		}

		return TextUtil.join(list, "，");
	}

	/**
	 * 奖励是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.properties.size() == 0;
	}

	public void appendProperty(String propertyName, int value) {
		this.properties
				.put(propertyName,
						value
								+ (this.properties.containsKey(propertyName) ? this.properties
										.get(propertyName) : 0));
	}

	public String getTcCode() {
		return this.tcCode;
	}

	@Override
	public Iterator<Entry<String, Integer>> iterator() {
		return this.properties.entrySet().iterator();
	}

	/**
	 * 是否包含指定奖励
	 * 
	 * @param name
	 * @param num
	 * @return
	 */
	public boolean contains(String name, int num) {
		return this.properties.containsKey(name) ? this.properties.get(name) >= num
				: false;
	}

}
