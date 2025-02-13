package com.morefun.XSanGo.vip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IPrivilegeView {
	List<String> privileges = new ArrayList<String>();
	Map<Integer,List<String>> viplevel = new HashMap<Integer,List<String>>();
	
	Collection<String> getPrivilegesOfLevel(int i) {
		if(!viplevel.containsKey(i)) {
			return new HashSet<String>();
		}
		return viplevel.get(i);
	}
}
