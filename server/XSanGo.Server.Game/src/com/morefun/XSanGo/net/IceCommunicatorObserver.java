/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import java.util.Map;

import Ice.ConnectionInfo;
import Ice.Current;
import Ice.Endpoint;
import Ice.ObjectPrx;
import Ice.Instrumentation.CommunicatorObserver;
import Ice.Instrumentation.ConnectionObserver;
import Ice.Instrumentation.ConnectionState;
import Ice.Instrumentation.DispatchObserver;
import Ice.Instrumentation.InvocationObserver;
import Ice.Instrumentation.Observer;
import Ice.Instrumentation.ObserverUpdater;
import Ice.Instrumentation.ThreadObserver;
import Ice.Instrumentation.ThreadState;

/**
 * @author Su LingYun
 * 
 */
public class IceCommunicatorObserver implements CommunicatorObserver {
	@Override
	public void setObserverUpdater(ObserverUpdater arg0) {
	}

	@Override
	public ThreadObserver getThreadObserver(String arg0, String arg1,
			ThreadState arg2, ThreadObserver arg3) {
		return null;
	}

	@Override
	public InvocationObserver getInvocationObserver(ObjectPrx arg0,
			String arg1, Map<String, String> arg2) {
		return null;
	}

	@Override
	public DispatchObserver getDispatchObserver(Current arg0, int arg1) {
		IceDispatchObserver result = new IceDispatchObserver(arg0);
		return result;
	}

	@Override
	public Observer getEndpointLookupObserver(Endpoint arg0) {
		return null;
	}

	@Override
	public ConnectionObserver getConnectionObserver(ConnectionInfo arg0,
			Endpoint arg1, ConnectionState arg2, ConnectionObserver arg3) {
		return null;
	}

	@Override
	public Observer getConnectionEstablishmentObserver(Endpoint arg0,
			String arg1) {
		return null;
	}
}
