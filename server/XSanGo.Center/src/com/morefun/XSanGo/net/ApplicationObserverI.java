/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.List;

import Ice.Current;
import IceGrid.ApplicationInfo;
import IceGrid.ApplicationUpdateInfo;
import IceGrid._ApplicationObserverDisp;

public class ApplicationObserverI extends _ApplicationObserverDisp {

	/**
	 * @param category
	 */
	public ApplicationObserverI(String category) {
	}

	@Override
	public void applicationInit(int serial, List<ApplicationInfo> applications,
			Current __current) {

	}

	@Override
	public void applicationAdded(int serial, ApplicationInfo desc,
			Current __current) {

	}

	@Override
	public void applicationRemoved(int serial, String name, Current __current) {

	}

	@Override
	public void applicationUpdated(int serial, ApplicationUpdateInfo desc,
			Current __current) {

	}

}
