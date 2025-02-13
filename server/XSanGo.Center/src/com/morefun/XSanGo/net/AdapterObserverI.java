/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import Ice.Current;
import IceGrid.AdapterInfo;
import IceGrid._AdapterObserverDisp;

public class AdapterObserverI extends _AdapterObserverDisp {

	/**
	 * @see IceGrid._AdapterObserverOperations#adapterInit(IceGrid.AdapterInfo[],
	 *      Ice.Current)
	 */
	@Override
	public void adapterInit(AdapterInfo[] adpts, Current __current) {

		// System.out.println("adapter init:");
		// for (AdapterInfo adapter : adpts) {
		//
		// System.out.println( adapter);
		// for (ServerDynamicInfo server : node.servers) {
		// System.out.println("server init:" + server.id + "  " +server.state);
		// }

		// }

	}

	@Override
	public void adapterAdded(AdapterInfo info, Current __current) {

	}

	@Override
	public void adapterUpdated(AdapterInfo info, Current __current) {

	}

	@Override
	public void adapterRemoved(String id, Current __current) {

	}

}
