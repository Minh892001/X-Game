package com.morefun.XSanGo.item;

import java.util.ArrayList;
import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._ItemDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.event.protocol.IFormationBuffLevelUp;
import com.morefun.XSanGo.formation.FormationBuffLevelUpConditionT;
import com.morefun.XSanGo.formation.FormationBuffLevelUpConfigT;
import com.morefun.XSanGo.formation.XsgFormationManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

public class ItemI extends _ItemDisp {
	private static final long serialVersionUID = 6635450939122775205L;
	private IRole roleRt;
	private IFormationBuffLevelUp formationBuffEvent;

	public ItemI(IRole role) {
		this.roleRt = role;
		this.formationBuffEvent = this.roleRt.getEventControler()
				.registerEvent(IFormationBuffLevelUp.class);
	}

	@Override
	public ItemView levelUpFormationBuff(String id, String[] idArray,
			Current __current) throws NoteException {
		FormationBuffItem buff = this.roleRt.getItemControler().getItem(id,
				FormationBuffItem.class);
		if (buff == null) {
			throw new NoteException(Messages.getString("ItemI.0")); //$NON-NLS-1$
		}
		if (buff.getLevel() >= Const.MaxFormationBuffLevel) {
			throw new NoteException(Messages.getString("ItemI.1")); //$NON-NLS-1$
		}

		int expDiff = 0; // 金币变更数量
		int beforeLevel = buff.getLevel(); // 初始等级
		int beforeExp = buff.getExp(); // 初始经验值
		
		// 计算总经验
		int exp = buff.getExp();
		List<FormationBuffItem> deleteList = new ArrayList<FormationBuffItem>();
		for (String delete : idArray) {
			FormationBuffItem delBuff = this.roleRt.getItemControler().getItem(
					delete, FormationBuffItem.class);
			exp += XsgFormationManager.getInstance().caculateBuffProvideExp(
					delBuff);
			expDiff += XsgFormationManager.getInstance().caculateBuffProvideExp(
					delBuff);
			deleteList.add(delBuff);
		}
		
		// 模拟升级过程，计算需要金钱，能够提升的等级
		FormationBuffLevelUpConfigT template = XsgFormationManager
				.getInstance().findBuffLevelUpConfigT(buff.getColor());
		byte level = buff.getLevel();
		FormationBuffLevelUpConditionT condition = template.conditions[level - 1];
		int money = -condition.money * buff.getExp();// 当前已有的经验需要在后面扣除，这里用负数表示
		while (level < Const.MaxFormationBuffLevel) {
			condition = template.conditions[level - 1];

			if (exp < condition.exp) {
				money += condition.money * exp;// 升不动了也要耗钱
				break;
			}

			exp -= condition.exp;
			money += condition.money * condition.exp;// 升一整级需要的钱
			level++;
		}

		// 实际数据变更，扣钱，扣物品，改变等级
		try {
			this.roleRt.winJinbi(-money);
		} catch (NotEnoughMoneyException e) {
			throw new NoteException(Messages.getString("ItemI.2")); //$NON-NLS-1$
		}
		for (FormationBuffItem delBuff : deleteList) {
			this.roleRt.getItemControler().changeItemById(delBuff.getId(), -1);
		}

		buff.setLevelAndExp(level, exp);
		if (!TextUtil.isBlank(buff.getRefereneFormationId())) {
			this.roleRt.getFormationControler().formationChange(
					this.roleRt.getFormationControler().getFormation(
							buff.getRefereneFormationId()));
		}

		this.formationBuffEvent.onFormationBuffLevelUp(buff, money, expDiff, beforeLevel, beforeExp, level, exp);

		return buff.getView();

	}

	@Override
	public void sale(String id, int count, Current __current)
			throws NoteException {
		this.roleRt.getItemControler().sale(id, count);
	}

	@Override
	public void useItem(String id, int count, String params, Current __current)
			throws NoteException {
		if (count < 0) {
			throw new NoteException(Messages.getString("ItemI.3") + count); //$NON-NLS-1$
		}

		this.roleRt.getItemControler().useItem(id, count, params);
	}

	@Override
	public ItemView[] useChestItem(String id, int count, Current __current)
			throws NoteException, NotEnoughMoneyException {
		if (count < 0) {
			throw new NoteException(Messages.getString("ItemI.4") + count); //$NON-NLS-1$
		}
		return this.roleRt.getItemControler().useChestItem(id, count);
		// return LuaSerializer.serialize(views);
	}

	@Override
	public void drawCompositeChestItem(int itemIndex, String itemId, Current __current) throws NoteException {
		this.roleRt.getItemControler().drawCompositeChestItem(itemIndex, itemId);
	}

	@Override
	public void selectAdvancedType(int type, Current __current) throws NoteException {
		roleRt.getItemControler().selectAdvancedType(type);
	}

	@Override
	public void advancedFormationBuff(String ids, Current __current) throws NoteException {
		roleRt.getItemControler().advancedFormationBuff(ids);
	}
}
