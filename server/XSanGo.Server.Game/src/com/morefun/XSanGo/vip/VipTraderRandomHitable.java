package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.util.IRandomHitable;

class VipTraderRandomHitable implements IRandomHitable {
	private VipTraderT t;

	VipTraderRandomHitable(VipTraderT t) {
		this.t = t;
	}

	@Override
	public int getRank() {
		return t.pro;
	}

	VipTraderT getTraderT() {
		return t;
	}
}
