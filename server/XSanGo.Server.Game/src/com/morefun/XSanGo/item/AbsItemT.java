/**
 * 
 */
package com.morefun.XSanGo.item;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;

/**
 * 物品模板抽象类
 * 
 * @author sulingyun
 * 
 */
public abstract class AbsItemT {
	/**
	 * 获取模板编号
	 * 
	 * @return
	 */
	public abstract String getId();

	/**
	 * 是否可以叠加
	 * 
	 * @return
	 */
	public abstract boolean canOverlay();

	/**
	 * 获取物品分类
	 * 
	 * @return
	 */
	public abstract ItemType getItemType();

	/**
	 * 是否可以出售
	 * 
	 * @return
	 */
	public abstract boolean canSale();

	/**
	 * 获取出售单价
	 * 
	 * @return
	 */
	public abstract int getSaleMoney();

	/**
	 * 获取碎片数量
	 * 
	 * @return
	 */
	public abstract int getPieceCount();

	/**
	 * 元宝价格，生成碎片时为装备元宝价格除以碎片数量，即为碎片单价（结果向上取整）  元宝价格表示这件物品的基础价格，在夺回战利品时直接使用  
	 * 在其他售卖功能中如果出现了该物品有没有声明价格时，按此元宝价格售卖
	 * 
	 * @return
	 */
	public abstract float getYuanbaoPrice();

	/**
	 * 道具名称
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 获取品质颜色
	 * 
	 * @return
	 */
	public abstract QualityColor getColor();

	/**
	 * 获取合成码，如果该物品不是碎片则返回null
	 * 
	 * @return
	 */
	public final String getComposeCode() {
		int index = this.getId().indexOf("-");
		if (index > -1) {
			return this.getId().substring(0, index);
		}

		return null;
	}

	/**
	 * 是否需要额外的材料
	 * */
	public abstract int needExtra();

	/**
	 * 额外材料的数量
	 * */
	public abstract Property[] extraItemAndNum();
}
