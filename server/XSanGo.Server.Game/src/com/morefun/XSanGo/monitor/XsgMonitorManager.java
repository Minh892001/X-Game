/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.monitor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author BruceSu
 * 
 */
public class XsgMonitorManager {
	private static Logger log = LoggerFactory
			.getLogger(XsgMonitorManager.class);
	private static XsgMonitorManager instance = new XsgMonitorManager();

	public static XsgMonitorManager getInstance() {
		return instance;
	}

	private Map<String, ProcessStat> cpuMap = new ConcurrentHashMap<String, ProcessStat>();

	private XsgMonitorManager() {
	}

	/**
	 * CPU时间统计
	 * 
	 * @param name
	 * @param time
	 */
	public void process(String name, int time) {
		if (!this.cpuMap.containsKey(name)) {
			this.cpuMap.put(name, new ProcessStat(name));
		}

		this.cpuMap.get(name).increase(time);
	}

	public void showStatInfo() {
		for (ProcessStat stat : this.cpuMap.values()) {
			if (stat.getAvgCostTime() > 30 || stat.getMaxCostTime() > 1000) {
				log.warn(stat.toString());
			}
		}
	}

	public Collection<ProcessStat> getAllMonitorItems() {
		return this.cpuMap.values();
	}
}
