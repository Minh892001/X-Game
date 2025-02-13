/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import Ice.ObjectAdapter;

import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ArenaRank.CrossArenaI;
import com.morefun.XSanGo.center.CenterI;
import com.morefun.XSanGo.crossServer.CrossServerI;

public class IceEntry {
	private static Ice.Communicator communicator;
	private static ObjectAdapter adapter;
	private static int serverId;

	public static Ice.ObjectAdapter getAdapter() {
		return adapter;
	}

	public static Ice.Communicator getCommunicator() {
		return communicator;
	}

	/**
	 * 初始化ICE运行时，并激活
	 */
	public static void init(String[] args) {
		final Ice.InitializationData initData = new Ice.InitializationData();
		initData.properties = Ice.Util.createProperties();
		initData.logger = new IceLogger();
		initData.observer = new IceCommunicatorObserver();
		initData.dispatcher = new Ice.Dispatcher() {
			public void dispatch(Runnable runnable, Ice.Connection connection) {
				// TODO S Ice.UnmarshalOutOfBoundsException
				// LogicThread.execute(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
				// });
				LogicThread.execute(runnable);
			}
		};
		communicator = Ice.Util.initialize(args, initData);

		String idStr = communicator.getProperties().getProperty("Ice.Admin.ServerId");
		serverId = Integer.parseInt(idStr);

		adapter = getCommunicator().createObjectAdapter("GameAdapter");

		adapter.add(GameSessionManagerI.getInstance(), getCommunicator()
				.stringToIdentity(idStr + ".GameSessionManager"));

		adapter.add(PermissionsVerifierI.getInstance(),
				getCommunicator().stringToIdentity(idStr + ".GamePermissionsVerifier"));

		ObjectAdapter adminAdapter = getCommunicator().createObjectAdapter(
				"AdminAdapter");
		adminAdapter.add(CenterI.instance(), getCommunicator()
				.stringToIdentity(idStr + ".Center"));
		
		adminAdapter.add(CrossServerI.instance(), getCommunicator()
				.stringToIdentity(idStr + ".CrossServer"));
		
		adminAdapter.add(CrossArenaI.instance(), getCommunicator()
				.stringToIdentity(idStr + ".CrossArena"));
		
		adminAdapter.activate();

		adapter.activate();
	}

	public static int getServerId() {
		return serverId;
	}
}
