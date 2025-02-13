/**
 * 
 */
package com.morefun.XSanGo.drop;

/**
 * 掉落/收益控制接口
 * 
 * @author sulingyun
 * 
 */
public interface IDropControler {
	/**
	 * 执行掉落算法，返回掉落结果
	 * 
	 * @param proxy
	 * @return
	 */
	DropResult doDrop(IDropProxy proxy);
}
