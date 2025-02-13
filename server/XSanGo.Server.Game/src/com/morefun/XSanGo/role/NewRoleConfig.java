package com.morefun.XSanGo.role;

import java.util.List;
import java.util.Map;

public class NewRoleConfig {
	public int level = 1;
	public List<Integer> heroList;
	public Map<String, Integer> itemMap;

	public NewRoleConfig() {
	}

	public NewRoleConfig(int level) {
		this.level = level;
	}
}
