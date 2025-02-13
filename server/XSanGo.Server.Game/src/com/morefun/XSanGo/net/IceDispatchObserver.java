/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

import Ice.Current;
import Ice.Instrumentation.DispatchObserver;

import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 分发监控器
 * 
 * @author Su LingYun
 * 
 */
public class IceDispatchObserver implements DispatchObserver {
	private Current current;
	private StopWatch watch;
	/** 日志 */
	protected final static Log logger = LogFactory.getLog(IceDispatchObserver.class);

	public IceDispatchObserver(Current current) {
		this.current = current;
		this.watch = new StopWatch();
	}

	@Override
	public void attach() {
		String remoteAddress = this.current.ctx.get("_con.remoteAddress");
		if (!TextUtil.isBlank(remoteAddress)) {
			remoteAddress += ":";
			remoteAddress += this.current.ctx.get("_con.remotePort");
			XsgDDOSDefiner.getInstance().receiveRequest(this.current.operation, remoteAddress);
		}
		this.watch.start();
	}

	@Override
	public void detach() {
		this.watch.stop();
		long interval = this.watch.getLastTaskTimeMillis();
		String facet = TextUtil.isBlank(this.current.facet) ? "" : this.current.facet + ".";
		XsgMonitorManager.getInstance().process(facet + this.current.operation, (int) interval);
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
