/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.AsyncResult;
import Ice.Callback;
import Ice.Callback_PropertiesAdmin_getProperty;
import Ice.Communicator;
import Ice.Identity;
import Ice.LocalException;
import Ice.ObjectAdapter;
import IceGrid.AdminPrx;
import IceGrid.AdminSessionPrx;
import IceGrid.ObjectInfo;
import IceGrid.ObjectNotRegisteredException;
import IceGrid.PermissionDeniedException;

import com.XSanGo.Protocol.AMI_Center_ping;
import com.XSanGo.Protocol.AMI_Center_sendTocken;
import com.XSanGo.Protocol.AMI_Center_setCallback;
import com.XSanGo.Protocol.CenterCallbackPrx;
import com.XSanGo.Protocol.CenterCallbackPrxHelper;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.CenterPrxHelper;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ServerDetail;
import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.GameServerInfo;
import com.morefun.XSanGo.gm.GmI;
import com.morefun.XSanGo.logicserver.CenterCallbackI;

/**
 * @author Su LingYun
 * 
 */
public class IceEntry {

	protected final static Log logger = LogFactory.getLog(IceEntry.class);

	private static IceEntry instance;

	public static IceEntry instance() {
		if (instance == null) {
			instance = new IceEntry();
		}
		return instance;
	}

	/** 控制器 */
	private Communicator communicator;
	/** 登陆适配器器 */
	private ObjectAdapter loginAdapter;
	/** 连接管理 */
	private IceGridSession gridSession = new IceGridSession();
	/** 游戏服务器代理 */
	private Map<Integer, CenterPrx> centerPrxs = new HashMap<Integer, CenterPrx>();

	// public void setAdapter(ObjectAdapter adapter) {
	// this.loginAdapter = adapter;
	// }

	public ObjectAdapter getLoginAdapter() {
		return this.loginAdapter;
	}

	public Communicator getCommunicator() {
		return this.communicator;
	}

	/**
	 * 此方法将使调用线程阻塞，直到调用ICE运行时被关闭
	 */
	public void init(String[] args) {
		// 初始化communicator
		initCommunicator(args);

		initAdminPrx();

		// connectGrid();
	}

	/**
	 * @param ice_getIdentity
	 * @return
	 */
	private int nameToServerId(String name) {
		String[] strs = name.split("\\.");
		return Integer.parseInt(strs[0]);
	}

	/**
	 * @param args
	 */
	private void initCommunicator(String[] args) {
		try {
			Ice.InitializationData initData = new Ice.InitializationData();

			initData.logger = new IceLogger();
			initData.properties = Ice.Util.createProperties();
			initData.dispatcher = new Ice.Dispatcher() {
				public void dispatch(final Runnable runnable, Ice.Connection connection) {
					CenterServer.execute(runnable);
				}
			};
			communicator = Ice.Util.initialize(args, initData);
		} catch (Throwable ex) {
			handleException(ex);
		}

	}

	/**
	 * 
	 */
	public void fini() {
		communicator.destroy();

	}

	/**
	 * 开始登陆服务器
	 */
	public void startLogin() {
		String iceInstance = communicator.getProperties().getProperty("Ice.Admin.ServerId");
		this.loginAdapter = getCommunicator().createObjectAdapter("LoginAdapter");
		// this.loginAdapter.add(
		// LoginSessionManagerI.instance(),
		// getCommunicator().stringToIdentity(
		// iceInstance + ".LoginSessionManager"));
		this.loginAdapter.add(GmI.instance(), getCommunicator().stringToIdentity(iceInstance + ".Gm"));

		this.loginAdapter.activate();
	}

	/**
	 * @param ex
	 */
	private void handleException(final Throwable ex) {
		// Ignore CommunicatorDestroyedException which could occur on
		// shutdown.
		if (ex instanceof Ice.CommunicatorDestroyedException) {
			return;
		}
		ex.printStackTrace();

	}

	private String genGlacier2Id(int id) {
		return Integer.toString(id) + ".Glacier2";
	}

	private void initAdminPrx() {

		logger.debug("initAdminPrx start beign!");
		try {
			Ice.ObjectPrx base = getCommunicator().stringToProxy("xsango/Registry");
			IceGrid.RegistryPrx registry = IceGrid.RegistryPrxHelper.uncheckedCast(base);
			// TODO S硬编码
			String userId = "xsango";
			String password = "xsango.mf";

			if (registry != null) {
				registry.begin_createAdminSession(userId, password, new Callback() {

					@Override
					public void completed(AsyncResult r) {

						IceGrid.RegistryPrx __proxy = (IceGrid.RegistryPrx) r.getProxy();
						AdminSessionPrx __ret = null;
						try {
							__ret = __proxy.end_createAdminSession(r);

							gridSession.login(__ret);

						} catch (Ice.LocalException e) {
							e.printStackTrace();
						} catch (PermissionDeniedException e) {
							e.printStackTrace();
						}
						logger.debug("initAdminPrx start end!");

					}
				});

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 更新游戏服务器状态
	 * 
	 * @param id
	 */
	public void updateServerState(final int id) {
		logger.debug("updateServerState start!");

		if (gridSession.getAdmin() == null) {
			// initAdminPrx();
			return;
		}

		GameServerInfo serverInfo = CenterServer.instance().findServerInfoById(id);

		if (serverInfo == null) {
			return;
		}

		// 设置该服务器网关状态
		setServerPort(serverInfo);

		// 设置该服务器centerPrx状态
		setCenterPrx(serverInfo);

		logger.debug("updateServerState end!");

	}

	/**
	 * @param serverInfo
	 */
	private void setCenterPrx(final GameServerInfo serverInfo) {

		final CenterPrx centerPrx = getCenterPrx(serverInfo.getId(), false);

		if (centerPrx == null) {

			logger.debug("get CenterPrx start!");

			gridSession.getAdmin().begin_getObjectInfo(new Identity(genCenterId(serverInfo.getId()), ""),
					new Callback() {

						@Override
						public void completed(AsyncResult r) {

							AdminPrx __proxy = (AdminPrx) r.getProxy();
							ObjectInfo __ret = null;
							try {
								__ret = __proxy.end_getObjectInfo(r);
								// 1分钟超时
								setCenterPrxCallback(serverInfo,
										CenterPrxHelper.uncheckedCast(__ret.proxy.ice_timeout(60000)));
							} catch (ObjectNotRegisteredException e) {
								e.printStackTrace();
								// 20150526
								// 未注册ICE的服务器，此前的处理方式是直接隐藏，由于加入了CP控制，这里可以不删除了
								// logger.warn(e.id.name +
								// " is not registered.");
								// CenterServer.instance().removeGameServer(
								// serverInfo.getId());
							} catch (Exception e) {
								e.printStackTrace();
							}
							logger.debug("get CenterPrx end!");

						}
					});

		} else {
			centerPrx.ping_async(new AMI_Center_ping() {
				@Override
				public void ice_response(ServerDetail __ret) {
					// 设置绿色，黄色，红色状态，同时增加极限控制，在选择服务器时拒绝进入
					serverInfo.setState(__ret);
					// serverInfo.setOnlineCount(__ret.onlineCount);

					if (!__ret.hasCallback) {
						// 服务器没设置过回调，移除该服务器连接，等待下次定时任务时重新初始化
						// 这里没有直接设置是尽量不破坏原有代码的逻辑
						serverInfo.setCenterPrxState(false);
						removeCenterPrx(serverInfo.getId());
					}
				}

				@Override
				public void ice_exception(LocalException ex) {
					serverInfo.setCenterPrxState(false);
					logger.debug("remove centerPrx" + serverInfo.getId());
					removeCenterPrx(serverInfo.getId());
				}
			});
		}
	}

	/**
	 * @param serverInfo
	 */
	private void setServerPort(final GameServerInfo serverInfo) {

		try {
			logger.debug("setServerPort start!");

			Ice.Identity serverAdminId = new Ice.Identity();
			serverAdminId.category = gridSession.getServerAdminCategory();
			serverAdminId.name = genGlacier2Id(serverInfo.getId());
			Ice.PropertiesAdminPrx props = Ice.PropertiesAdminPrxHelper.uncheckedCast(gridSession.getAdmin()
					.ice_identity(serverAdminId), "Properties");

			props.begin_getProperty("Glacier2.Client.Endpoints", new Callback_PropertiesAdmin_getProperty() {

				@Override
				public void exception(LocalException __ex) {
					// LogManager.error(__ex);
				}

				@Override
				public void response(String endpoints) {
					List<Endpoint> endpointsObj = Endpoint.parseEndpoints(endpoints, true);
					serverInfo.setPort(endpointsObj.get(0).port);
					logger.debug("setServerPort end(" + endpointsObj.get(0).port + ")!");
				}
			});

		} catch (Exception e) {// IceGrid.ObjectNotRegisteredException
			 e.printStackTrace();
		}
	}

	public CenterPrx getCenterPrx(int id, boolean redirect) {
		if (redirect) {
			id = CenterServer.instance().getRealServerId(id);
		}
		return centerPrxs.get(id);

	}

	private CenterPrx removeCenterPrx(final int id) {
		return centerPrxs.remove(id);
	}

	private void addCenterPrx(final GameServerInfo serverInfo, final CenterPrx centerPrxNew) {
		centerPrxs.put(serverInfo.getId(), centerPrxNew);
	}

	/**
	 * @param serverInfo
	 * @param centerPrxNew
	 */
	private void setCenterPrxCallback(final GameServerInfo serverInfo, final CenterPrx centerPrxNew) {
		// 创建回调
		CenterCallbackPrx centerCallbackPrx = CenterCallbackPrxHelper.uncheckedCast(gridSession
				.addCallback(new CenterCallbackI(serverInfo.getId())));
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "t");
		centerCallbackPrx = CenterCallbackPrxHelper.checkedCast(centerCallbackPrx, ctx);
		centerPrxNew.setCallback_async(new AMI_Center_setCallback() {

			@Override
			public void ice_response() {
				addCenterPrx(serverInfo, centerPrxNew);

				serverInfo.setCenterPrxState(true);
				System.out.println("add centerPrx:" + nameToServerId(centerPrxNew.ice_getIdentity().name));
			}

			@Override
			public void ice_exception(LocalException ex) {
				// ex.printStackTrace();
				serverInfo.setCenterPrxState(false);

			}
		}, centerCallbackPrx);
	}

	/**
	 * @param id
	 * @return
	 */
	private String genCenterId(int id) {
		return Integer.toString(id) + ".Center";
	}

	/**
	 * @param serverId
	 * @param tocken
	 * @param device
	 * @throws SelectServerException
	 */
	public void sendTocken_async(AMI_Center_sendTocken cb, int serverId, int accountId, String account, String tocken,
			DeviceInfo device) throws NoteException {
		CenterPrx centerPrx = getCenterPrx(serverId, true);

		if (centerPrx == null) {
			throw new NoteException("inner error!");
		}
		centerPrx.sendTocken_async(cb, accountId, account, tocken, device);

	}

	/**
	 * @param id
	 */
	public void updateServerState(String id) {
		try {
			int serverId = nameToServerId(id);
			updateServerState(serverId);
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	public AdminPrx getAdminPrx() {
		return this.gridSession.getAdmin();
	}

	public CenterPrx getAnyCenterPrx() {
		for (CenterPrx prx : this.centerPrxs.values()) {
			return prx;
		}
		return null;
	}
}
