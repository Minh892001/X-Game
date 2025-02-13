/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.http.handler.ActiveAccountHandler;
import com.morefun.XSanGo.http.handler.AnnounceHandler;
import com.morefun.XSanGo.http.handler.CDKHandler;
import com.morefun.XSanGo.http.handler.ChargeItemHandler;
import com.morefun.XSanGo.http.handler.ChargeOrderHandler;
import com.morefun.XSanGo.http.handler.ChargeRebateForMiyuHandler;
import com.morefun.XSanGo.http.handler.LoginHandler;
import com.morefun.XSanGo.http.handler.PassportHandler;
import com.morefun.XSanGo.http.handler.QueryItemCountsHandler;
import com.morefun.XSanGo.http.handler.RoleQueryHandler;
import com.morefun.XSanGo.http.handler.SelectServerHandler;
import com.morefun.XSanGo.http.handler.ServerCombineMapHandler;
import com.morefun.XSanGo.http.handler.ServerMapHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * 内嵌的HTTP服务
 * 
 * @author sulingyun
 * 
 */
public class HttpLancher {
	private static HttpServer server;

	// 启动服务，监听来自客户端的请求
	public synchronized static void startup() throws IOException {
		// 设置读写超时，虽然开发环境测试无效
		// 如果不设置，开发环境仍会抛出超时异常，但生产环境并不会
		// 1.5/1.6版本JDK使用下面参数，单位为秒
		// sun.net.httpserver.writeTimeout
		// sun.net.httpserver.readTimeout
		// 1.7版本JDK使用如下参数
		// System.setProperty("sun.net.httpserver.clockTick",
		// String.valueOf(5000));
		System.setProperty("sun.net.httpserver.maxReqTime", String.valueOf(3));
		System.setProperty("sun.net.httpserver.maxRspTime", String.valueOf(3));

		HttpServerProvider provider = HttpServerProvider.provider();
		server = provider.createHttpServer(
				new InetSocketAddress(LoginDatabase.instance().getAc().getBean("HttpPort", Integer.class)), 2000);// 监听端口8888,能同时接受1000个请求

		server.createContext("/announce", new AnnounceHandler());
		
		server.createContext("/login", new LoginHandler());
		server.createContext("/select", new SelectServerHandler());
		server.createContext("/order", new ChargeOrderHandler());//注册处理来自paycenter的充值订单通知
		server.createContext("/rebateForMiyu", new ChargeRebateForMiyuHandler());//注册处理来自米娱的充值返利请求
		//帐号中心未建立之前，模拟美峰一号通的帐号验证接口
		server.createContext("/common", new PassportHandler());
		
		server.createContext("/active", new ActiveAccountHandler());
		server.createContext("/cdk", new CDKHandler());
		server.createContext("/queryRole", new RoleQueryHandler());
		server.createContext("/findChargeItem", new ChargeItemHandler());
		server.createContext("/queryItemCounts", new QueryItemCountsHandler());
		server.createContext("/serverCombineMap", new ServerCombineMapHandler());
		server.createContext("/serverMap", new ServerMapHandler());

		ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		server.setExecutor(executor);
		server.start();
		System.out.println("server started");
	}

	public synchronized static void stop() {
		if (server != null) {
			server.stop(0);
		}
	}
}
