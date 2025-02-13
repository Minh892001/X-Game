/**
 * 
 */
package com.morefun.XSanGo.drop;

/**
 * 掉落执行完成后的回调
 * 
 * @author sulingyun
 * 
 */
public interface IDropCallback {
	/**
	 * 更新掉落统计数据，该方法逻辑由掉落系统指定，由应用程序在进行正式结算后调用
	 * 
	 * @param data
	 */
	void update(IDropStatisticsData data);
}
