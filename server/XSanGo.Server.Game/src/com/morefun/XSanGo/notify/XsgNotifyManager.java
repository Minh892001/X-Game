/**
 * 
 */
package com.morefun.XSanGo.notify;

import com.morefun.XSanGo.role.IRole;

/**
 * 消息通知管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgNotifyManager {
	private static XsgNotifyManager instance = new XsgNotifyManager();

	public static XsgNotifyManager getInstance() {
		return instance;
	}

	private XsgNotifyManager() {
	}

	public INotifyControler createNotifyControler(IRole rt) {
		return new NotifyControler(rt);
	}
}
