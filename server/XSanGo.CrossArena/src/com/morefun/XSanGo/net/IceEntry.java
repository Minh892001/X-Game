/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.AsyncResult;
import Ice.Callback;
import Ice.Identity;
import Ice.LocalException;
import Ice.ObjectAdapter;
import Ice.ObjectNotExistException;
import IceGrid.AdminPrx;
import IceGrid.AdminSessionPrx;
import IceGrid.ObjectInfo;
import IceGrid.ObjectNotRegisteredException;
import IceGrid.PermissionDeniedException;

import com.XSanGo.Protocol.AMI_CrossArena_ping;
import com.XSanGo.Protocol.AMI_CrossArena_setCallback;
import com.XSanGo.Protocol.CrossArenaCallbackPrx;
import com.XSanGo.Protocol.CrossArenaCallbackPrxHelper;
import com.XSanGo.Protocol.CrossArenaPrx;
import com.XSanGo.Protocol.CrossArenaPrxHelper;
import com.morefun.XSanGo.CrossArenaCallbackI;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.template.CrossArenaRangeT;
import com.morefun.XSanGo.util.TextUtil;

public class IceEntry {
	protected final static Log logger = LogFactory.getLog(IceEntry.class);

	private static Ice.Communicator communicator;
	private static ObjectAdapter crossArenaAdapter;
	private static AdminPrx adminPrx;
	private static AdminSessionPrx adminSessionPrx;
	private static boolean connectingAdmin = false;
	private static ObjectAdapter adminAdapter;

	private static Map<Integer, CrossArenaPrx> prxMap = new ConcurrentHashMap<Integer, CrossArenaPrx>();

	public static Ice.ObjectAdapter getAdapter() {
		return crossArenaAdapter;
	}

	public static Ice.Communicator getCommunicator() {
		return communicator;
	}

	/**
	 * 初始化ICE运行时
	 */
	public static void init(String[] args) {
		initCommunicator(args);
		initAdminPrx();
		startKeepAlive();
		initAdminAdapter();
	}

	/**
	 * 初始化ICE admin adapter
	 * */
	private static void initAdminAdapter() {
		String prefix = communicator.getProperties().getProperty("Ice.Admin.ServerId");
		String adapterName = prefix + ".IceAdmin";
		adminAdapter = communicator.createObjectAdapter(adapterName);
		adminAdapter.activate();
	}

	private static void startKeepAlive() {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(3 * 1000);
						keepAlive();
					} catch (Exception e) {
						logger.warn("KeepAlive Exception: ", e);
					}
				}
			}
		};

		t.setDaemon(true);
		t.start();
	}

	/**
	 * 初始化 communicator
	 * */
	private static void initCommunicator(String[] args) {
		final Ice.InitializationData initData = new Ice.InitializationData();
		initData.properties = Ice.Util.createProperties();
		initData.logger = new IceLogger();
		initData.observer = new IceCommunicatorObserver();
		initData.dispatcher = new Ice.Dispatcher() {
			public void dispatch(final Runnable runnable, Ice.Connection connection) {
				LogicThread.execute(runnable);
			}
		};
		communicator = Ice.Util.initialize(args, initData);
	}

	/** 初始化AdminPrx */
	private static void initAdminPrx() {
		if (connectingAdmin) {
			return;
		}
		connectingAdmin = true;
		logger.debug("初始化AdminPrx...");
		Ice.ObjectPrx base = communicator.stringToProxy("xsango/Registry");
		IceGrid.RegistryPrx registry = IceGrid.RegistryPrxHelper.uncheckedCast(base);
		String userId = "xsango";
		String password = "xsango.mf";
		if (registry == null) {
			logger.error("ICE Grid Registry Error.");
			connectingAdmin = false;
			return;
		}
		registry.begin_createAdminSession(userId, password, new Callback() {
			@Override
			public void completed(AsyncResult r) {
				try {
					IceGrid.RegistryPrx __proxy = (IceGrid.RegistryPrx) r.getProxy();
					adminSessionPrx = __proxy.end_createAdminSession(r);
					if (adminSessionPrx == null) {
						logger.error("ICE Grid AdminSessionPrx Error.");
						return;
					}
					adminPrx = adminSessionPrx.getAdmin();
				} catch (PermissionDeniedException e) {
					e.printStackTrace();
					logger.error(TextUtil.format("ICE Grid AdminSessionPrx Exception:{0}", e.getMessage()));
				} finally {
					connectingAdmin = false;
				}
			}
		});
	}

	/**
	 * 确保ICE Session 存活
	 * */
	public static void keepAlive() {
		if (adminSessionPrx != null) {
			try {
				adminSessionPrx.begin_keepAlive(new Callback() {

					@Override
					public void completed(AsyncResult r) {
						try {
							AdminSessionPrx prx = (AdminSessionPrx) r.getProxy();
							prx.end_keepAlive(r);
						} catch (Exception e) {
							logger.warn(TextUtil.format("keepalive exception: {0}", e.getMessage()), e);
							initAdminPrx();
						}
					}
				});
				// logger.warn(TextUtil.format("keepalive:{0}",
				// System.currentTimeMillis()));
			} catch (Exception e) {
				logger.error("KeepAliveError: ", e);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						initAdminPrx();
					}
				});
			}
		}
	}

	/**
	 * 激活ICE adapter
	 * */
	public static void activeIceAdapter() {
		// String iceInstanceId = communicator.getProperties().getProperty(
		// ICE_ADMIN_SERVERID);
		crossArenaAdapter = communicator.createObjectAdapter("CrossArenaAdapter");

		crossArenaAdapter.activate();
	}

	/** 监控prx的状态，断开的时候重连 */
	public static void monitorStatus(final int id) {
		if (adminPrx != null && crossArenaAdapter != null) {
			CrossArenaPrx prx = prxMap.get(id);
			if (prx != null) {
				prx.ping_async(new AMI_CrossArena_ping() {

					@Override
					public void ice_response(boolean __ret) {
						if (!__ret) {
							prxMap.remove(id);
							setupCrossArenaCallbackPrx(id, null);
						}
					}

					@Override
					public void ice_exception(LocalException ex) {
						prxMap.remove(id);
						setupCrossArenaCallbackPrx(id, null);
					}
				}, System.currentTimeMillis());
			} else {
				// logger.warn(TextUtil.format("there no prx for {0}", id));
				setupCrossArenaCallbackPrx(id, null);
			}
		} else {
			logger.error("Ice Status Error...");
		}
	}

	public static void setupCrossArenaCallbackPrx(final int id, final CountDownLatch finished) {
		if (adminPrx != null && crossArenaAdapter != null) {
			Identity identity = new Identity(id + ".CrossArena", "");
			adminPrx.begin_getObjectInfo(identity, new Callback() {
				@Override
				public void completed(AsyncResult r) {
					try {
						ObjectInfo objInfo = ((AdminPrx) r.getProxy()).end_getObjectInfo(r);
						final CrossArenaPrx prx = CrossArenaPrxHelper.uncheckedCast(objInfo.proxy);
						CrossArenaCallbackPrx callbackPrx = CrossArenaCallbackPrxHelper.uncheckedCast(crossArenaAdapter
								.addWithUUID(new CrossArenaCallbackI(id)));
						Map<String, String> ctx = new HashMap<String, String>();
						ctx.put("_fwd", "t");
						callbackPrx = CrossArenaCallbackPrxHelper.checkedCast(callbackPrx, ctx);
						prx.setCallback_async(new AMI_CrossArena_setCallback() {
							@Override
							public void ice_response() {
								logger.warn(TextUtil.format("set callback success for server {0}...", id));
								prxMap.put(id, prx);
								if (finished != null) {
									finished.countDown();
								}
							}

							@Override
							public void ice_exception(LocalException ex) {
								// logger.error(TextUtil.format(
								// "CrossArena setCallback error. for server {0} reason: {1}",
								// id, ex));
								if (finished != null) {
									finished.countDown();
								}
							}
						}, callbackPrx);
					} catch (ObjectNotRegisteredException e) {
						// logger.warn(TextUtil.format("begin_getObjectInfo Callback exception. serverId: {0}, Msg: {1}",
						// id, e));
						if (finished != null) {
							finished.countDown();
						}
					} catch (ObjectNotExistException e) {
						// logger.warn(TextUtil.format("ObjectNotExistException: {0}",
						// e.getMessage()));
						if (finished != null) {
							finished.countDown();
						}
						initAdminPrx();
					} catch (Exception e) {
						// logger.warn(TextUtil.format("UnknownException: {0}",
						// e.getMessage()));
						if (finished != null) {
							finished.countDown();
						}
					}
				}
			});
		} else {
			logger.error("The Ice Status Error...");
			if (finished != null) {
				finished.countDown();
			}
		}
	}

	/**
	 * 获取服务器代理
	 * 
	 * @param serverId
	 * @return
	 */
	public static CrossArenaPrx getCrossArenaPrx(Integer serverId) {
		return prxMap.get(serverId);
	}

	/**
	 * 随机获取服务器代理
	 * 
	 * @param serverId
	 * @return
	 */
	public static CrossArenaPrx getRandomServerPrx(CrossArenaRangeT r) {
		for (Entry<Integer, CrossArenaPrx> entry : prxMap.entrySet()) {
			if (entry.getKey() >= r.beginServerId && entry.getKey() <= r.endServerId) {
				return entry.getValue();
			}
		}
		return null;
	}
}
