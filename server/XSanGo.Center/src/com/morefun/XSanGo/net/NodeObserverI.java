/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import Ice.Current;
import IceGrid.AdapterDynamicInfo;
import IceGrid.NodeDynamicInfo;
import IceGrid.ServerDynamicInfo;
import IceGrid._NodeObserverDisp;

public class NodeObserverI extends _NodeObserverDisp {

	/**
	 * @see IceGrid._NodeObserverOperations#nodeInit(IceGrid.NodeDynamicInfo[],
	 *      Ice.Current)
	 */
	@Override
	public void nodeInit(NodeDynamicInfo[] nodes, Current __current) {

		// for (NodeDynamicInfo node : nodes) {
		//
		// System.out.println("node init:" + node.info);
		// for (ServerDynamicInfo server : node.servers) {
		// System.out.println("server init:" + server.id + "  " +server.state);
		// }
		//
		// for (AdapterDynamicInfo adapter : node.adapters) {
		// System.out.println("adapter init:" + adapter.id + adapter.proxy );
		// }
		// }

	}

	@Override
	public void nodeUp(NodeDynamicInfo node, Current __current) {

	}

	@Override
	public void nodeDown(String name, Current __current) {

	}

	@Override
	public void updateServer(String node, ServerDynamicInfo updatedInfo,
			Current __current) {

		IceEntry.instance().updateServerState(updatedInfo.id);

	}

	@Override
	public void updateAdapter(String node, AdapterDynamicInfo updatedInfo,
			Current __current) {
		IceEntry.instance().updateServerState(updatedInfo.id);
	}

}
