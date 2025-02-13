/**
 * 
 */
package com.morefun.XSanGo.itemChip;

import java.util.List;

import com.XSanGo.Protocol.CompoundResult;
import com.XSanGo.Protocol.HeroInheritView;
import com.XSanGo.Protocol.HeroResetResult;
import com.XSanGo.Protocol.HeroResetView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;
import com.morefun.XSanGo.role.IRole;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 功能控制器
 * 
 * @author 吕明涛
 * 
 */
public interface IItemChipControler extends IRedPointNotable {
	/**
	 * 碎片合成
	 * 
	 * @throws Exception
	 */
	CompoundResult compoundChip(IRole roleRt, String itemId, String extraId) throws NoteException;

	/**
	 * 宝石合成
	 * */
	CompoundResult compoundGem(String itemId, int num) throws NoteException;

	/**
	 * 炫耀合成后装备
	 * 
	 * @throws NoteException
	 */
	void strutItem(String itemId) throws NoteException;

	/**
	 * 角色下线时候，调用 清除数据
	 */
	void clearData();

	/**
	 * 武将下野
	 * 
	 * @param heroId
	 *            武将ID
	 * @param isPay
	 *            是否付费
	 * */
	HeroResetResult heroReset(String heroId, int isPay) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 获取武将下野界面
	 * */
	List<HeroResetView> requestHeroReset() throws NoteException;

	/**
	 * 获取武将传承界面
	 * */
	HeroInheritView requestHeroInherit() throws NoteException;

	/**
	 * 武将传承
	 * */
	void heroInherit(String inheritHeroId, String baseHeroId) throws NoteException;
}
