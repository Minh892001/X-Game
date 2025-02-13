package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.role.IRole;

public class XsgWorshipManage {
	private static XsgWorshipManage instance = new XsgWorshipManage();
	public static XsgWorshipManage getInstance() {
		return instance;
	}

	public IWorshipRankControler createWorshipRankControler(IRole iRole) {
		return new WorshipControler(iRole);
	}
}
