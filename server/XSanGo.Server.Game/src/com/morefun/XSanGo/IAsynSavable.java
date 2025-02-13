/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

/**
 * 异步保存标记接口，定义了一些对象支持异步保存需要的方法
 * 
 * @author BruceSu
 * 
 */
public interface IAsynSavable {
	/**
	 * 复制数据对象到字节数组
	 * 
	 * @return
	 */
	byte[] cloneData();

	/**
	 * 使用cloneData方法复制的数据反序列化成对象并保存
	 */
	void save(byte[] data);
}
