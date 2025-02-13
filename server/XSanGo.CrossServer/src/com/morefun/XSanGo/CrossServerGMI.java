package com.morefun.XSanGo;

import com.XSanGo.Protocol._CrossServerGMDisp;

/**
 * @author guofeng.qin
 */
public class CrossServerGMI extends _CrossServerGMDisp {

	private static final long serialVersionUID = -376661657387765881L;

	private static CrossServerGMI instance = new CrossServerGMI();

	private CrossServerGMI() {

	}

	public static CrossServerGMI getInstance() {
		return instance;
	}
}
