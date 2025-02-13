/**
 * 
 */
package com.morefun.XSanGo.notify;

import java.util.List;

import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.RoleCallbackPrx;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.IItem;

/**
 * 通知消息控制接口，用于玩家数据变更时使用
 * 
 * @author sulingyun
 * 
 */
public interface INotifyControler {
	/**
	 * 设置自动通知，默认为true，即调用相关通知方法时直接通知客户端，当设置为false时，相关通知方法调用会被缓存，
	 * 当调用notifyIfHasChange方法时一起通知 内部会自动进行优化，同一个属性，武将或者物品以最后一次数据为准
	 * 
	 * @param auto
	 */
	void setAutoNotify(boolean auto);

	/**
	 * 物品变化通知
	 * 
	 * @param item
	 */
	void onItemChange(IItem item);

	/**
	 * 武将变化通知
	 * 
	 * @param hero
	 */
	void onHeroChanged(IHero hero);

	/**
	 * 部队变化通知
	 * 
	 * @param formation
	 */
	void onFormationChange(IFormation formation);

	/**
	 * 元宝变化
	 */
	void onYuanBaoChanged(int rmby);

	/**
	 * 属性变更通知
	 * 
	 * @param propertyName
	 * @param value
	 */
	void onPropertyChange(String propertyName, Number value);
	
	/**
	 * String类型属性变更通知
	 */
	void onStrPropertyChange(String propertyName, String value);
	
	/**
	 * 红点提示变更
	 * 
	 * @param redPoint
	 */
	void onMajorUIRedPointChange(MajorUIRedPointNote... redPoint);

	void showTips(String tips);

	/**
	 * 成就提示
	 * 
	 * @param list
	 */
	void showAchieve(List<Integer> aIds);

	/**
	 * 推送通知
	 * 
	 * @param msgs
	 * */
	void pushMsgs();

	/**
	 * 公会副本挑战者变更
	 * 
	 * @param roleName
	 * @param icon
	 * @param vipLevel
	 */
	void factionCopyState(String roleName, String icon, int vipLevel);

	/**
	 * 获取客户端回调接口
	 * 
	 * @return
	 */
	RoleCallbackPrx getRoleCallback();
}
