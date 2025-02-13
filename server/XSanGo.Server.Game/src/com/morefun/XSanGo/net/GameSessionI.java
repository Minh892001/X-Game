/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;
import Ice.Identity;
import Ice.LocalException;
import Ice.ObjectAdapterDeactivatedException;

import com.XSanGo.Protocol.Callback_GameSessionCallback_timeSyncronized;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.ChatCallbackPrxHelper;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.FactionCallBackPrx;
import com.XSanGo.Protocol.FactionCallBackPrxHelper;
import com.XSanGo.Protocol.GameSessionCallbackPrx;
import com.XSanGo.Protocol.GameSessionCallbackPrxHelper;
import com.XSanGo.Protocol.RoleCallbackPrx;
import com.XSanGo.Protocol.RolePrx;
import com.XSanGo.Protocol.RolePrxHelper;
import com.XSanGo.Protocol.SnsCallBackPrx;
import com.XSanGo.Protocol.SnsCallBackPrxHelper;
import com.XSanGo.Protocol._GameSessionDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.RoleI;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;

@SuppressWarnings("serial")
public class GameSessionI extends _GameSessionDisp {

	protected final static Log logger = LogFactory.getLog(GameSessionI.class);
	private static Map<String, ModuleConfigT> Module_Map;
	static {
		Module_Map = CollectionUtil.toMap(
				ExcelParser.parse(ModuleConfigT.class), "moduleName");
	}

	protected String account;
	private Identity id;
	protected IRole roleRt;
	protected RolePrx rolePrx;
	private RoleCallbackPrx roleCb;
	private GameSessionCallbackPrx sessionCb;
	private GameSessionManagerI sessionManager;
	private DeviceInfo deviceInfo;
	private String remoteAddress;

	public GameSessionI(String userId, GameSessionManagerI sessionManager,
			DeviceInfo device, String remoteAddress) {
		this.account = userId;
		this.sessionManager = sessionManager;
		this.deviceInfo = device;
		this.remoteAddress = remoteAddress;
	}

	private boolean hasBindRole() {
		return this.roleRt != null && this.roleRt.isOnline();
	}

	@Override
	public RolePrx getRole(Current __current) {
		if (rolePrx == null) {
			try {
				RoleI roleI = new RoleI(account, this);
				rolePrx = RolePrxHelper.uncheckedCast(IceEntry.getAdapter()
						.addWithUUID(roleI));
			} catch (ObjectAdapterDeactivatedException e) {
				logger.error("Cannot create role for user " + account + "!");

			} catch (Exception e) {
				LogManager.error(e);
			}
		}

		return rolePrx;
	}

	@Override
	public void destroy(Current __current) {
		this.destroyWarp(true);
	}

	public void setId(Identity id) {
		this.id = id;
	}

	public Identity getId() {
		return id;
	}

	public RoleCallbackPrx getRoleCb() {
		return roleCb;
	}

	public void setRoleCb(RoleCallbackPrx roleCb) {
		this.roleCb = roleCb;
	}

	/**
	 * 初始化各种角色相关的facet
	 */
	public void bindRole(IRole role) {
		// 初始化各种角色相关的facet
		if (role == null) {
			throw new IllegalArgumentException();
		}

		this.roleRt = role;

		// 加载功能模块
		try {
			for (String facet : Module_Map.keySet()) {
				Class<?> meta = Class.forName(Module_Map.get(facet).className);
				Constructor con = meta.getConstructor(IRole.class);
				con.setAccessible(true);
				Ice.Object servant = (Ice.Object) con.newInstance(role);

				this.addRoleFact(servant, facet);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private void addRoleFact(Ice.Object servant, String facetName) {
		IceEntry.getAdapter().addFacet(servant,
				this.getRole().ice_getIdentity(), facetName);
	}

	public IRole getRoleRt() {
		return roleRt;
	}

	@Override
	public void setCallback(final GameSessionCallbackPrx cb, Current __current) {
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "o");
		this.sessionCb = GameSessionCallbackPrxHelper.checkedCast(cb
				.ice_context(ctx));
		// this.sessionCb = cb;

		// LogicThread.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		// sessionCb.begin_close("test close.");
		// }
		// });
	}

	/**
	 * 通知客户端关闭连接
	 * 
	 * @param note
	 */
	public void notifyClose(String note) {
		if (hasBindRole()) {
			this.roleRt.notifyOffline();
		}
		if (this.sessionCb == null) {
			return;
		}
		this.sessionCb.begin_close(note);
	}

	@Override
	public void syncTime(Current __current) {
		this.sessionCb.begin_timeSyncronized(
				DateUtil.toString(System.currentTimeMillis()),
				new Callback_GameSessionCallback_timeSyncronized() {

					@Override
					public void response() {
					}

					@Override
					public void exception(LocalException ex) {
					}
				});
	}

	public ChatCallbackPrx getChatCb() {
		return ChatCallbackPrxHelper.uncheckedCast(this.roleCb, "chatCallback");
	}

	public SnsCallBackPrx getSnsCallback() {
		return SnsCallBackPrxHelper.uncheckedCast(this.roleCb, "snsCallback");
	}
	
	public FactionCallBackPrx getFactionCallBack() {
		return FactionCallBackPrxHelper.uncheckedCast(this.roleCb, "factionCallback");
	}

	public DeviceInfo getDevice() {
		return this.deviceInfo;
	}

	@Override
	public void destroyWarp(boolean reconnectable, Current __current) {
		if (!reconnectable) {
			sessionManager.removeReconnectKey(this.account);
		}

		if (hasBindRole()) {
			this.roleRt.notifyOffline();
			this.roleRt.saveAsyn();
			// 加入缓存，确保即使现在数据不在缓存里，也可以走角色缓存的统一数据移除流程
			XsgRoleManager.getInstance().addRole(roleRt);
		}

		if (sessionManager != null) {
			sessionManager.removeSession(this.account);
		}

		try {
			if (this.rolePrx != null) {
				IceEntry.getAdapter().removeAllFacets(
						this.rolePrx.ice_getIdentity());
			}
			IceEntry.getAdapter().remove(id);

		} catch (ObjectAdapterDeactivatedException e) {
			// No need to clean up, the server is shutting down.
		}

	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public void ice_ping(Current current) {
		super.ice_ping(current);
		if (this.roleRt != null) {
			XsgRoleManager.getInstance().findRoleById(this.roleRt.getRoleId());
		}
	}

	public void destroyOnly(boolean reconnectable) {
		if (!reconnectable) {
			sessionManager.removeReconnectKey(this.account);
		}

		if (hasBindRole()) {
			this.roleRt.notifyOffline();
//			this.roleRt.saveAsyn();
			// 加入缓存，确保即使现在数据不在缓存里，也可以走角色缓存的统一数据移除流程
			XsgRoleManager.getInstance().removeRole(this.roleRt.getRoleId());
		}

		if (sessionManager != null) {
			sessionManager.removeSession(this.account);
		}

		try {
			if (this.rolePrx != null) {
				IceEntry.getAdapter().removeAllFacets(
						this.rolePrx.ice_getIdentity());
			}
			IceEntry.getAdapter().remove(id);

		} catch (ObjectAdapterDeactivatedException e) {
			// No need to clean up, the server is shutting down.
		}

	
	}
}
