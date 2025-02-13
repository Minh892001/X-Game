/**
 * 
 */
package com.morefun.XSanGo.reward;

import java.util.List;
import java.util.Map.Entry;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.NormalItem;

public interface IRewardControler {

	/**
	 * 发放奖励给玩家
	 * 
	 * @param items
	 */
	public List<IItem> acceptReward(Iterable<Entry<String, Integer>> items);

	/**
	 * 发放奖励给玩家
	 * 
	 * @param items
	 */
	public List<IItem> acceptReward(ItemView[] items);

	/**
	 * 获取单项奖励
	 * 
	 * @param code
	 * @param num
	 */
	IItem acceptReward(String code, int num);

	/**
	 * 宝箱类物品伪随机判断，返回本次判断结果的TC值，命中则相关数据重置，否则累加
	 * 
	 * @param item
	 * @return
	 */
	public String doMockTcForChestItem(NormalItem chestItem);

	/**
	 * 执行伪随机算法，该方法负责检测是否命中伪随机及执行后的状态更新
	 * 
	 * @param realTc
	 *            真随机结果
	 * 
	 * @param tmt
	 *            伪随机配置
	 * @return 产出掉落
	 */
	public TcResult doMockTc(TcResult realTc, TcMockT tmt);
	
	/**
	 * 执行觉醒掉落的伪随机算法，该方法负责检测是否命中伪随机及执行后的状态更新
	 * 
	 * @param realTc 结果
	 * @param amt 配置
	 * @return 
	 */
	//public TcResult doAwakenMockTc(TcResult realTc, AwakenMockT amt);
}
