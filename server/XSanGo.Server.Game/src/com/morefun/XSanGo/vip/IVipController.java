package com.morefun.XSanGo.vip;

import java.util.Collection;
import java.util.Date;

import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.GmPayView;
import com.XSanGo.Protocol.GmPayView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TopupView;
import com.morefun.XSanGo.db.game.RoleVipGiftPacks;
import com.morefun.XSanGo.db.game.RoleVipTraderItem;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface IVipController extends IRedPointNotable {
	/**
	 * @return 当前vip等级
	 */
	int getLevel();

	/**
	 * @return 可升级到的最高vip等级
	 */
	int getMaxLevel();

	/**
	 * @return 当前的vip经验值
	 */
	int getExperience();

	/**
	 * @param exp
	 *            增加vip经验
	 * @throws IllegalArgumentException
	 *             不接受负数经验值
	 */
	void addExperience(int exp) throws IllegalArgumentException;

	/**
	 * @return 客户端自己读配置，这个方法也没用到
	 */
	IPrivilegeView getPrivilegeView();

	/**
	 * 商城中的道具
	 */
	Collection<RoleVipTraderItem> getTraderItems();

	/**
	 * @return 刷新vip商城的时间
	 */
	String getTraderRefreshingTime();

	/**
	 * 购买vip礼包
	 * 
	 * @param vipLevel
	 *            vip等级
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	void buyGiftPacks(int vipLevel) throws NotEnoughYuanBaoException, NoteException;

	/**
	 * @return vip礼包
	 */
	Collection<RoleVipGiftPacks> getRoleGiftPacks();

	/**
	 * vip商城中购买道具
	 * 
	 * @param Id
	 * @throws NotEnoughYuanBaoException
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	void buyVipTraderItems(int Id) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException;

	/**
	 * 升级奖励更改vip等级，不需要处理vip经验
	 * 
	 * @param vipLevel
	 */
	void setVipLevel(int vipLevel);

	/**
	 * 获取充值界面信息
	 * 
	 * @return
	 */
	TopupView getChargeView();

	/**
	 * 检查充值选项
	 * 
	 * @param chargeId
	 * @param chargeForFriend
	 * @throws NoteException
	 */
	void checkChargeStatus(int chargeId, boolean chargeForFriend) throws NoteException;

	/**
	 * 接收自己的充值结果
	 * 
	 * @param params
	 * @param yuan
	 * @param orderId
	 * @param currency
	 */
	void acceptCharge(CustomChargeParams params, int yuan, String orderId, String currency);

	/**
	 * 来自好友的充值结果
	 * 
	 * @param item
	 * @param yuan
	 * @param friendAccount
	 */
	void acceptChargeFromFriend(int item, int yuan, String friendAccount);

	/**
	 * VIP 颜色
	 * 
	 * @return 十六进制颜色表示
	 */
	String getVipColor();

	/**
	 * 是否有月度首充
	 * 
	 * @return
	 */
	boolean hasFirstCharge();

	/**
	 * 是否有VIP月卡
	 * 
	 * @return
	 */
	boolean hasMonthCard();

	/**
	 * 使用道具增加月卡天数
	 * */
	boolean useMonthCardItem(int dayCount) throws NoteException;

	/**
	 * 获取指定时间范围内充值（非绑定）元宝数量
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	int getChargeYuanbao(Date begin, Date end);

	/**
	 * @return 绑定的元宝
	 */
	long getBindYuanbao();

	/**
	 * @return 非绑定
	 */
	long getUnBindYuanbao();

	/**
	 * 获取累计充值金额
	 * 
	 * @return
	 */
	int getSumCharge();

	/**
	 * 获取技能点上限
	 * 
	 * @return
	 */
	int getMaxSkillPoint();

	/**
	 * 获取当前VIP等级对应的模板配置
	 * 
	 * @return
	 */
	VipT getVipT();

	/**
	 * 获取充值记录
	 * 
	 * @return
	 */
	GmPayView getChargeHistory();

	/**
	 * 获取当月累计充值天数
	 * 
	 * @param date
	 * @return
	 */
	int getMonthChargeDays(Date date);
}
