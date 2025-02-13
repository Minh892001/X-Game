/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.db.game.RoleItem;

/**
 * 物品接口
 * 
 * @author sulingyun
 * 
 */
public interface IItem {
	/**
	 * 获取唯一标识
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 获取模板数据
	 * 
	 * @return
	 */
	AbsItemT getTemplate();

	/**
	 * 获取模板数据的泛型实现，除非确定类型转换肯定成功，否则可能抛出ClassCastException
	 * 
	 * @return
	 */
	<T> T getTemplate(Class<T> type);

	/**
	 * 设置物品数量，实现方法应遵循物品叠加规则设定，不符合的调用将抛出异常
	 * 
	 * @param i
	 */
	void setNum(int i);

	/**
	 * 获取物品数量
	 * 
	 * @return
	 */
	int getNum();

	/**
	 * 获取前台视图数据
	 * 
	 * @return
	 */
	ItemView getView();

	/**
	 * 复制数据实体
	 * 
	 * @return
	 */
	RoleItem cloneData();
}
