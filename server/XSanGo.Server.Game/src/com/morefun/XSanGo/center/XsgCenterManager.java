/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.center;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.XSanGo.Protocol.AlarmType;
import com.XSanGo.Protocol.CenterCallbackPrx;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.role.IRole;

/**
 * 中心服务器管理
 * 
 * @author Su LingYun
 * 
 */
public class XsgCenterManager {
	private static XsgCenterManager instance;

	public static XsgCenterManager instance() {
		if (instance == null) {
			instance = new XsgCenterManager();
		}
		return instance;
	}

	public static void setInstance(XsgCenterManager instance) {
		XsgCenterManager.instance = instance;
	}

	private Ehcache verifys;

	private CenterCallbackPrx cb;

	{
		verifys = XsgCacheManager.getInstance().getCache("verify"); //$NON-NLS-1$
	}

	public void addVerify(String account, String password, DeviceInfo device) {
		this.verifys.put(new Element(account, new AuthData(password, device)));
	}

	public DeviceInfo isVerify(String account, String password) {
		account = account.toLowerCase();
		Element pass = this.verifys.get(account);
		if (pass == null) {
			return null;
		}

		AuthData data = ((AuthData) pass.getObjectValue());
		return data.getPwd().equals(password) ? data.getDevice() : null;
	}

	/**
	 * @param cb
	 */
	public void setCb(CenterCallbackPrx cb) {
		this.cb = cb;
	}

	public CenterCallbackPrx getCb() {
		return cb;
	}

	/**
	 * @param account
	 * @param id
	 */
	public void addRole(String account, String roleId, String name) {
		if (cb != null) {
			cb.begin_addRole(account, roleId, name);
		}
	}

	/**
	 * 修改后只要升级就统计
	 * 
	 * @param roleId
	 * @param tarLevel
	 * @param oldLevel
	 */
	public void levelup(final IRole role) {

		newMaxLevel(role.getAccount(), role.getRoleId(), role.getName(),
				role.getLevel());
		//
		//
		// DragonDatabase.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// int maxLevel = RoleI.getRoleDAO().findMaxLevelByAccount(
		// role.getAccount());
		//
		// if (tarLevel > maxLevel) {
		// DragonWorld.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		// newMaxLevel(role.getAccount(), role.getRoleId(),role.getName(),
		// tarLevel);
		//
		// }
		// });
		// }
		// }
		// });

	}

	public void newMaxLevel(String account, String roleId, String name,
			int maxLevel) {
		if (cb != null) {
			cb.begin_newMaxLevel(account, roleId, name, maxLevel);
		}
	}

	/**
	 * 检查与中心服务的连接
	 * 
	 * @throws NoteException
	 * 
	 */
	public void checkSession() throws NoteException {
		if (this.cb == null) {
			throw new NoteException(Messages.getString("XsgCenterManager.1")); //$NON-NLS-1$
		}
	}

	public void frozenAccount(String account, String remark) {
		if (this.cb != null) {
			this.cb.begin_frozenAccount(account, remark);
		}
	}

	public void sendAlarmSMS(AlarmType type, String text) {
		if (this.cb != null) {
			this.cb.begin_sendAlarmSMS(type, text);
		}
	}
}
