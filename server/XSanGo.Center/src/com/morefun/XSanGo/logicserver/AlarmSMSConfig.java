/**
 * 
 */
package com.morefun.XSanGo.logicserver;

/**
 * 短信通知配置
 * 
 * @author linyun.su
 *
 */
public class AlarmSMSConfig {
	private boolean enable;
	private String mobileNumber;
	private String url;
	private int hours;

	public AlarmSMSConfig(boolean enable, String mobileNumber, String url,
			int hours) {
		this.enable = enable;
		this.mobileNumber = mobileNumber;
		this.url = url;
		this.hours = hours;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getUrl() {
		return url;
	}

	public int getHours() {
		return hours;
	}
}
