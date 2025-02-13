package com.morefun.XSanGo.sns;

import com.morefun.XSanGo.Messages;

public enum SNSType {
	/**
	 * 好友
	 */
	FRIEND(1, Messages.getString("SNSType.0")), //$NON-NLS-1$
	/**
	 * 仇人
	 */
	FOE(2, Messages.getString("SNSType.1")), //$NON-NLS-1$
	/**
	 * 黑名单
	 */
	BLACKLIST(3, Messages.getString("SNSType.2")); //$NON-NLS-1$

	private String n;
	private int value;

	public String getName() {
		return n;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return n;
	}

	SNSType(int v, String n) {
		this.value = v;
		this.n = n;
	}

	static SNSType of(int v) {
		for (SNSType type : values()) {
			if (type.value == v) {
				return type;
			}
		}
		return null;
	}
}