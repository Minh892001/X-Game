/**
 * 
 */
package com.morefun.XSanGo.drop;

/**
 * 角色掉落统计数据接口
 * 
 * @author sulingyun
 * 
 */
public interface IDropStatisticsData {
	/**
	 * 序列化成字符串
	 * 
	 * @return
	 */
	String convert2String();

	/**
	 * 从字符串反序列化
	 * 
	 * @param input
	 */
	void convertFromString(String input);
}
