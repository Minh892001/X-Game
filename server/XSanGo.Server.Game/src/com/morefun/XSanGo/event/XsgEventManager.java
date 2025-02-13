/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;

public class XsgEventManager {
	private static XsgEventManager instance;

	public static XsgEventManager getInstance() {
		if (instance == null) {
			instance = new XsgEventManager();
		}
		return instance;
	}

	public static void setInstance(XsgEventManager instance) {
		XsgEventManager.instance = instance;
	}

	public IEventControler createEventControler(IRole roleRt, Role roleDb) {
		return new EventControler(roleRt, roleDb);
	}

}
