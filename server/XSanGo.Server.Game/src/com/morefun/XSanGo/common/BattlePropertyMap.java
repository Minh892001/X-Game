/**
 * 
 */
package com.morefun.XSanGo.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.GrowableProperty;

/**
 * 战斗属性生成器
 * 
 * @author sulingyun
 * 
 */
public class BattlePropertyMap implements Iterable<Entry<String, GrowableProperty>> {
	private Map<String, GrowableProperty> propertyMap = new HashMap<String, GrowableProperty>();

	public void combine(BattlePropertyMap other) {
		if (other == null) {
			return;
		}

		for (String key : other.propertyMap.keySet()) {
			this.combine(other.propertyMap.get(key));
		}
	}

	public void combine(GrowableProperty property) {
		this.combine(property.code, property.value);
	}

	public void combine(String key, int value) {
		if (this.propertyMap.containsKey(key)) {
			this.propertyMap.get(key).value += value;
		} else {
			this.propertyMap.put(key, new GrowableProperty(key, value, 0));
		}

	}

	public int getValue(String key) {
		GrowableProperty p = this.propertyMap.get(key);
		return (int) (p == null ? 0 : p.value);
	}

	@Override
	public Iterator<Entry<String, GrowableProperty>> iterator() {
		return this.propertyMap.entrySet().iterator();
	}

	public GrowableProperty[] getProperties() {
		return this.propertyMap.values().toArray(new GrowableProperty[0]);
	}
}
