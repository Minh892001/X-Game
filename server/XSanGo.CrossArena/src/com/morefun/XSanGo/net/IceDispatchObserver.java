/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import org.springframework.util.StopWatch;

import Ice.Current;
import Ice.Instrumentation.DispatchObserver;

import com.morefun.XSanGo.monitor.XsgMonitorManager;

/**
 * 分发监控器
 * 
 * @author Su LingYun
 * 
 */
public class IceDispatchObserver implements DispatchObserver {
	private Current current;
	private StopWatch watch;

	public IceDispatchObserver(Current current) {
		this.current = current;
		this.watch = new StopWatch();
	}

	@Override
	public void attach() {
		this.watch.start();
	}

	@Override
	public void detach() {
		this.watch.stop();
		long interval = this.watch.getLastTaskTimeMillis();
		XsgMonitorManager.getInstance().process(this.current.operation, (int) interval);
	}

	@Override
	public void failed(String arg0) {

	}

	@Override
	public void reply(int arg0) {

	}

	@Override
	public void userException() {

	}
}
