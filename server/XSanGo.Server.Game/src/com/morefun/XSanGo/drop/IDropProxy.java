/**
 * 
 */
package com.morefun.XSanGo.drop;

/**
 * 玩家掉落代理接口，提供关于控制掉落概率的角色数据获取方法
 * 
 * @author sulingyun
 * 
 */
public interface IDropProxy {
	/**
	 * 获取掉落统计数据，掉落控制系统应该在给玩家结算奖励时更新该对象状态，以保证应用程序可以在持久化时保存该状态
	 * 
	 * @return
	 */
	IDropStatisticsData getDropStatistics();

	/**
	 * 获取指定副本的总共完成次数
	 * 
	 * @param copyId
	 * @return
	 */
	int getCopyComlpeteTime(int copyId);

	/**
	 * 获取总在线时间，单位：秒
	 * 
	 * @return
	 */
	int getTotalOnlineSeconds();

	/**
	 * 获取AFK时间，单位：秒。AFK：away from keyboard，离开游戏
	 * 
	 * @return
	 */
	int getAfkSeconds();

	/**
	 * 获取充值次数
	 * 
	 * @return
	 */
	int getChargeTime();

	/**
	 * 获取累计消费元宝数量
	 * 
	 * @return
	 */
	int getConsumeYuanbao();

	/**
	 * 获取VIP等级
	 * 
	 * @return
	 */
	int getVipLevel();

	/**
	 * 是否拥有某个武将
	 * 
	 * @param heroId
	 * @return
	 */
	boolean hasHero(int heroId);

	/**
	 * 是否拥有某个物品或装备
	 * 
	 * @param template
	 * @return
	 */
	boolean hasItem(String template);

	/**
	 * 获取开宝箱次数
	 * 
	 * @return
	 */
	int getOpenChestTime();

	/**
	 * 获取主公等级
	 * 
	 * @return
	 */
	int getLevel();

	/**
	 * 获得战斗力
	 * 
	 * @return
	 */
	int getBattlePower();

	/**
	 * 获取外部修正
	 * 
	 * @return
	 */
	int getRateRevise();
}
