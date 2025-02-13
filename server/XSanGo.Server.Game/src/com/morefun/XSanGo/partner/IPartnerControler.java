package com.morefun.XSanGo.partner;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PartnerView;
import com.XSanGo.Protocol.PartnerViewInfo;
import com.morefun.XSanGo.hero.IHero;

/**
 * 伙伴操作接口
 * @author xiaojun.zhang
 *
 */
public interface IPartnerControler {
	/**
	 * 获取阵型配置表
	 * 
	 * @return
	 */
	public PartnerView getPartnerView()throws NoteException ;
	/**
	 * 设置伙伴武将的位置
	 * @param postion 伙伴阵位
	 * @param hero 武将
	 * @param oldHero 
	 * @param b 是否进行最后一名武将检查
	 * @return 
	 */
	public void setPartnerPosition(byte postion, IHero hero, IHero oldHero, boolean b) throws NoteException ;
	/**
	 * 开启伙伴武将的位置
	 * @param postion 伙伴阵位
	 * @return 
	 */
	public PartnerViewInfo openPartnerPosition(byte postion) throws NoteException ;
	/**
	 * 获取伙伴信息
	 * @return
	 */
	public IPartner getPartner();
	/**
	 * 重置指定的伙伴位
	 * @param isLock 1 锁定武将 2 锁定属性
	 * @return
	 * @throws NoteException 
	 */
	public PartnerViewInfo resetPartnerPosition(int postion, int cost, int isLock) throws NoteException;
	/**
	 * 清空所有伙伴位置
	 * @return 
	 */
	public void clearAll() throws NoteException ;
	/**
	 * 伙伴信息变更通知
	 * @param partner
	 */
	public void partnerChange(IPartner partner);
	/**
	 * 获取伙伴开启的条件等级
	 * @return
	 */
	public int getRequiredLevel();
	/**
	 * 获取玩家已经开启的伙伴位置数量
	 * @return
	 */
	public int getOpenedPartner();
	/**
	 * 获取伙伴武将激活专属伙伴并且激活上阵武将缘分的伙伴数量
	 * @return
	 */
	public int getActivedRelationPartner();
}
