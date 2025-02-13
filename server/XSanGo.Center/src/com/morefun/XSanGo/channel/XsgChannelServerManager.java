package com.morefun.XSanGo.channel;

import java.util.Map;

import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;

/**
 * 渠道专属服务器全局管理
 * 
 * @author linyun.su
 * 
 */
public class XsgChannelServerManager {

	private static XsgChannelServerManager instance = new XsgChannelServerManager();

	public static XsgChannelServerManager getInstance() {
		return instance;
	}

	/**
	 * 专属服务器配置
	 */
	private Map<Integer, ChannelServerT> channelServerMap;

	private XsgChannelServerManager() {
		this.loadScript();
	}

	private void loadScript() {
		this.channelServerMap = CollectionUtil.toMap(ExcelParser.parse(ChannelServerT.class), "channelId");
	}

	public ChannelServerT getChannelServerT(int channel) {
		return this.channelServerMap.get(channel);
	}

}
