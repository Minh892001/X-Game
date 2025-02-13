/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.DeviceInfo;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.center.AuthData;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

import Glacier2.CannotCreateSessionException;
import Glacier2.SessionControlPrx;
import Glacier2.SessionPrx;
import Glacier2.SessionPrxHelper;
import Glacier2._SessionManagerDisp;
import Ice.Current;
import Ice.LocalException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * 连接管理器，负责创建和销毁客户端连接
 * 
 * @author sulingyun
 * 
 */
@SuppressWarnings("serial")
public class GameSessionManagerI extends _SessionManagerDisp {
	private static GameSessionManagerI instance;

	/** 所有的连接 */
	private Map<String, GameSessionI> sessions;
	private Cache reconnectKeyCache;

	/** 正在尝试取得连接的设备 */
	private Map<String, DeviceInfo> connectingDeviceMap;

	public static GameSessionManagerI getInstance() {
		if (instance == null) {
			instance = new GameSessionManagerI();
		}
		return instance;
	}

	private GameSessionManagerI() {
		this.sessions = new ConcurrentHashMap<String, GameSessionI>();
		this.reconnectKeyCache = XsgCacheManager.getInstance().getCache("reconnect_key"); //$NON-NLS-1$
		this.connectingDeviceMap = new HashMap<String, DeviceInfo>();
	}

	/** 日志 */
	protected final static Log logger = LogFactory.getLog(GameSessionManagerI.class);

	@Override
	public SessionPrx create(String userId, SessionControlPrx control, Current __current)
			throws CannotCreateSessionException {
		// Glacier2.AddConnectionContext=2
		// {_con.remoteAddress=::ffff:192.168.1.252, _con.localPort=20011,
		// _con.type=tcp, _con.remotePort=47520,
		// _con.localAddress=::ffff:192.168.4.125}
		SessionPrx proxy = null;
		try {

			// 如果该账号已经有连接了,把以前的连接断了
			GameSessionI session = findSession(userId);
			if (session != null) {
				session.notifyClose(Messages.getString("GameSessionManagerI.1")); //$NON-NLS-1$
				session.destroy(null);
				logger.warn(TextUtil.format("kick another session for [{0}]!", userId)); //$NON-NLS-1$
			}

			// 创建新的连接
			DeviceInfo device = session == null ? this.connectingDeviceMap.remove(userId) : session.getDevice();
			if (device == null) {
				logger.error(TextUtil.format("[{0}]无法获取设备信息!!!", userId));
				device = new DeviceInfo(0, "", "", "", 0L, ServerLancher.getServerId(), 0); //$NON-NLS-1$
			}

			String remoteAddress = this.parseRemoteAddress(__current);
			session = new GameSessionI(userId, this, device, remoteAddress);
			proxy = SessionPrxHelper.checkedCast(__current.adapter.addWithUUID(session));
			session.setId(proxy.ice_getIdentity());

			addSession(userId, session);
		} catch (LocalException e) {
			LogManager.error(e);
			if (proxy != null) {
				proxy.destroy();
			}
			throw new CannotCreateSessionException("internal server error"); //$NON-NLS-1$
		} catch (Exception e) {
			LogManager.error(e);
			throw new CannotCreateSessionException(e.toString());
		}

		return proxy;
	}

	/**
	 * @param __current
	 * @return
	 */
	public String parseRemoteAddress(Current __current) {
		String remoteAddress = __current.ctx.get("_con.remoteAddress");
		if (!TextUtil.isBlank(remoteAddress)) {
			remoteAddress += ":";
			remoteAddress += __current.ctx.get("_con.remotePort");
		}
		return remoteAddress;
	}

	/**
	 * @param userId
	 * @param session
	 */
	public void addSession(String userId, GameSessionI session) {
		sessions.put(userId, session);
	}

	/**
	 * @param userId
	 */
	public void removeSession(String userId) {
		sessions.remove(userId);
	}

	/**
	 * @param account
	 * @return
	 */
	protected GameSessionI findSession(String account) {
		return sessions.get(account);
	}

	public GameSessionI findSession(String account, String roleId) {
		GameSessionI session = findSession(account);
		if (session != null && session.getRoleRt() != null && session.getRoleRt().getRoleId().equals(roleId)) {
			return session;
		}
		return null;
	}

	/**
	 * 发送关服通知但不不实际清理session，这是因为清理session会造成数据保存，可能跟退出保存有冲突
	 */
	public void notifyCloseAllSession() {
		for (GameSessionI session : this.sessions.values()) {
			session.notifyClose(Messages.getString("GameSessionManagerI.0")); //$NON-NLS-1$
		}

		// if (!destorySession) {
		// return;
		// }

		// // 先用一个数组存起来，避免直接遍历销毁时由于调用了MAP的remove方法产生并发修改异常
		// GameSessionI[] array = this.sessions.values().toArray(new
		// GameSessionI[0]);
		// for (GameSessionI session : array) {
		// session.destroyWarp(false);
		// }
		// // 到这正常应该容器已经空了，保险起见再清理下
		// int size = sessions.size();
		// if (size > 0) {
		// logger.warn(TextUtil.format("{0} sessions remain!!!", size));
		// sessions.clear();
		// }
	}

	/**
	 * 增加一个重连KEY，连接中断时调用
	 * 
	 * @param account
	 * @param key
	 * @param device
	 */
	public void addReconnectKey(String account, String key, DeviceInfo device) {
		this.reconnectKeyCache.put(new Element(account, new AuthData(key, device)));
	}

	/**
	 * 重连验证接口
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	public DeviceInfo reconnectVerify(String userId, String password) {
		Element element = this.reconnectKeyCache.get(userId);
		if (element == null) {
			return null;
		}

		AuthData data = ((AuthData) element.getObjectValue());
		return data.getPwd().equals(password) ? data.getDevice() : null;
	}

	public int getOnlineCount() {
		return this.sessions.size();
	}

	public void addConnectingDevice(String userId, DeviceInfo device) {
		this.connectingDeviceMap.put(userId, device);
	}

	public void removeReconnectKey(String account) {
		this.reconnectKeyCache.remove(account);
	}
}