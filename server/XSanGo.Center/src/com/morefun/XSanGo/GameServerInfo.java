/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.ServerDetail;
import com.morefun.XSanGo.db.GameServer;
import com.morefun.XSanGo.net.GameServerRT;
import com.morefun.XSanGo.net.ServerState;

public class GameServerInfo {
	protected final static Log logger = LogFactory.getLog(GameServerInfo.class);

	/** 数据库信息 */
	private GameServer db;
	/** 运行时信息 */
	private GameServerRT rt;
	private ServerDetail detail = new ServerDetail();

	private ServerState state = ServerState.DISABLED;

	public GameServerInfo(GameServer db, GameServerRT rt) {
		this.db = db;
		this.rt = rt;
	}

	public int getId() {
		return db.getId();
	}

	public boolean isNew() {
		return db.getIsNew();
	}

	public String getName() {
		return db.getName();
	}

	@Deprecated
	public String getIp() {
		return rt.ip;
	}

	public int getPort() {
		return rt.port;
	}

	public GameServer getDB() {
		return this.db;
	}

	public void setDb(GameServer db) {
		this.db = db;
	}

	public ServerState getState() {
		if (!this.isServerOk()) {
			return ServerState.DISABLED;
		}

		return this.isNew() ? ServerState.New : ServerState.FULL;
	}

	private boolean isServerOk() {
		GameServerInfo target = CenterServer.instance().findServerInfoById(this.db.getTargetId());
		if (rt.port == 0) {// 网关没开，直接无法服务
			return false;
		}
		if (target == null) {// 不存在重定向，只看自己的
			return rt.getCenterPrxOk();
		} else {// 设置了重定向，避免无限递归，需要判断是否定向到自己，否则根据目标服务器状态判断
			return target.equals(this) ? rt.getCenterPrxOk() : target.isServerOk();
		}
	}

	public void setPort(int port) {
		if (port != rt.port) {
			rt.setPort(port);
			CenterServer.instance().notifyServerStateChange(db.getId());
		}

	}

	public int getOnlineCount() {
		return this.detail.onlineCount;
	}

	/**
	 * @param count
	 * @return
	 */
	private ServerState genState(int count) {
		return ServerState.FULL;
	}

	public String getHost() {
		return db.getHost();
	}

	public void setCenterPrxState(boolean b) {
		logger.debug("ceterPrx" + db.getId() + " is " + b);
		if (!b) {
			this.detail.onlineCount = 0;
		}
		if (b != rt.getCenterPrxOk()) {
			rt.setCenterPrxOk(b);
			CenterServer.instance().notifyServerStateChange(db.getId());
		}
	}

	/**
	 * @param __ret
	 */
	public void setState(ServerDetail __ret) {
		this.detail = __ret;
		ServerState state = this.genState(this.getOnlineCount());
		if (this.state != state) {
			this.state = state;
			CenterServer.instance().notifyServerStateChange(db.getId());
		}
	}

	/**
	 * 服务器是否过载
	 * 
	 * @return
	 */
	public boolean isOverload() {
		int gmLimit = this.db.getOnlineLimit();
		return this.detail.overload || (gmLimit > 0 && this.getOnlineCount() > gmLimit);
	}

	public boolean isCpShowOnly() {
		GameServerInfo target = CenterServer.instance().findServerInfoById(this.db.getTargetId());
		if (target == null) {// 不存在重定向，只看自己的
			return this.getDB().isCpShowOnly();
		} else {// 设置了重定向，避免无限递归，需要判断是否定向到自己，否则根据目标服务器状态判断
			return target.equals(this) ? this.getDB().isCpShowOnly() : target.isCpShowOnly();
		}
	}

	public boolean isCpEnterOnly() {
		GameServerInfo target = CenterServer.instance().findServerInfoById(this.db.getTargetId());
		if (target == null) {// 不存在重定向，只看自己的
			return this.getDB().isCpEnterOnly();
		} else {// 设置了重定向，避免无限递归，需要判断是否定向到自己，否则根据目标服务器状态判断
			return target.equals(this) ? this.getDB().isCpEnterOnly() : target.isCpEnterOnly();
		}
	}

	// public void setState(ServerState state) {
	//
	// if (this.state != state) {
	// this.state = state;
	// CenterServer.instance().notifyServerStateChange(db.getId());
	// }
	// }

}
