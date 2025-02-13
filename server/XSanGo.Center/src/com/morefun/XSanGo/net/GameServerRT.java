/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

public class GameServerRT {
	public int id;
	private boolean isCenterPrxOk;
	public String ip;
	public int port;

	public void setId(int id) {
		this.id = id;
	}

	public void setCenterPrxOk(boolean isCenterPrxOk) {
		this.isCenterPrxOk = isCenterPrxOk;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return
	 */
	public boolean getCenterPrxOk() {
		return isCenterPrxOk;
	}

}
