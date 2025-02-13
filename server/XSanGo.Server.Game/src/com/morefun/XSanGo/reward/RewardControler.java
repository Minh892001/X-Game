/**
 * 
 */
package com.morefun.XSanGo.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleChestMock;
import com.morefun.XSanGo.db.game.RoleMock;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.NormalItem;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;

class RewardControler implements IRewardControler {

	private IRole role;
	private Role roleDB;

	public RewardControler(IRole role, Role roleDB) {
		this.role = role;
		this.roleDB = roleDB;
	}

	@Override
	public List<IItem> acceptReward(Iterable<Entry<String, Integer>> items) {
		List<IItem> list = new ArrayList<IItem>();
		// 结算物品奖励
		for (Entry<String, Integer> entry : items) {
			String code = entry.getKey();
			int num = entry.getValue();

			list.add(this.acceptReward(code, num));
		}

		return list;
	}

	@Override
	public List<IItem> acceptReward(ItemView[] items) {
		List<IItem> list = new ArrayList<IItem>();
		for (ItemView item : items) {
			list.add(this.acceptReward(item.templateId, item.num));
		}

		return list;
	}

	@Override
	public IItem acceptReward(String code, int num) {
		IItem result = null;
		if (code.equals(Const.PropertyName.EXP)) {
			this.role.winPrestige(num);
		} else if (code.equals(Const.PropertyName.MONEY)) {
			try {
				this.role.winJinbi(num);
			} catch (NotEnoughMoneyException e) {
				LogManager.error(e);
			}
		} else if (code.equals(Const.PropertyName.RMBY)) {
			try {
				this.role.winYuanbao(num, true);
			} catch (NotEnoughYuanBaoException e) {
				LogManager.error(e);
			}
		} else if (code.equals(Const.PropertyName.ORDER)) {
			this.role.winChallegeMoney(num);
		} else if (code.equals(Const.PropertyName.AUCTION_COIN)) {
			try {
				this.role.addAuctionCoin(num);
			} catch (NoteException e) {
				LogManager.error(e);
			}
		} else if (Const.PropertyName.SKILL.equals(code)) {
			role.getHeroControler().addHeroSkillPoint(num);
		} else if (Const.PropertyName.POP.equals(code)) {
			role.getAttackCastleController().addCoin(num);
		} else if (Const.PropertyName.ZZB.equals(code)) {
			role.getTournamentController().addCoin(num);
		} else if (Const.PropertyName.ZZYB.equals(code)) {
			role.getTournamentController().addYBCoin(num);
		} else if (Const.PropertyName.ARMY.equals(code)) {
			IFaction faction = role.getFactionControler().getMyFaction();
			if (faction != null) {
				faction.setScore(faction.getScore() + num);
			}
		} else if (Const.PropertyName.HONOR.equals(code)) {
			IFaction faction = role.getFactionControler().getMyFaction();
			if (faction != null) {
				FactionMember fm = faction.getMemberByRoleId(role.getRoleId());
				fm.setHonor(fm.getHonor() + num);
			}
		} else if (Const.PropertyName.NanHuaLing.equals(code)) {
			role.getDreamlandController().addNanHuaLing(num);
		} else {// 物品奖励
			List<IItem> list = this.role.getItemControler().changeItemByTemplateCode(code, num);
			if (!list.isEmpty()) {
				result = list.get(0);
			}
		}

		return result;
	}

	@Override
	public String doMockTcForChestItem(NormalItem chestItem) {
		NormalItemT template = chestItem.getTemplate(NormalItemT.class);
		String result = template.useValue;
		ChestItemMockT mockT = XsgRewardManager.getInstance().findMockT(template.id);
		if (mockT != null) {
			RoleChestMock rm = this.getOrCreateChestMockData(template.id);
			// 现在是第几次了
			rm.setNum(rm.getNum() + 1);
			if (rm.getNum() >= mockT.maxNum) {// 已经点背到达到最大值了，此时必出
				rm.setNum(0);
				return mockT.mockTc;
			}

			if (rm.getNum() >= mockT.minNum) {// 达到伪随机触发条件
				if (NumberUtil.isHit(1, mockT.maxNum - mockT.minNum + 1)) {
					rm.setNum(0);
					return mockT.mockTc;
				}
			}
		}

		return result;
	}

	/**
	 * 获取指定的宝箱伪随机控制数据
	 * 
	 * @param itemTemplate
	 * @return
	 */
	private RoleChestMock getOrCreateChestMockData(String itemTemplate) {
		RoleChestMock rm = this.roleDB.getRoleChestMocks().get(itemTemplate);
		if (rm == null) {
			rm = new RoleChestMock(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, itemTemplate, 0);
			this.roleDB.getRoleChestMocks().put(itemTemplate, rm);
		}

		return rm;
	}

	/**
	 * 获取指定的宝箱伪随机控制数据
	 * 
	 * @param itemTemplate
	 * @return
	 */
	private RoleMock getOrCreateMockData(String tc) {
		RoleMock rm = this.roleDB.getRoleMocks().get(tc);
		if (rm == null) {
			rm = new RoleMock(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, tc);
			this.roleDB.getRoleMocks().put(tc, rm);
		}

		return rm;
	}

	@Override
	public TcResult doMockTc(TcResult realTc, TcMockT tmt) {
		TcResult result = realTc;
		if (tmt != null) {
			RoleMock rm = this.getOrCreateMockData(tmt.tc);
			int rate = tmt.baseRate + tmt.addWhenFail * rm.getTotalNum() - tmt.reduceWhenSucess * rm.getSucessNum();
			if (rate > 0) {// 命中伪随机，则增加成功次数且执行TC
				boolean hit = NumberUtil.isHit(rate, 100);
				if (hit) {
					TcResult mockResult = XsgRewardManager.getInstance().doTc(role, tmt.mockTc);
					rm.setSucessNum(rm.getSucessNum() + 1);

					if (tmt.override == 1) {// 覆盖真随机结果
						result = mockResult;
					} else {
						result.add(mockResult);
					}
				}
			}
			rm.setTotalNum(rm.getTotalNum() + 1);
		}

		return result;
	}

//	@Override
//	public TcResult doAwakenMockTc(TcResult realTc, AwakenMockT amt) {
//		TcResult result = realTc;
//		if (amt != null) {
//			RoleMock rm = this.getOrCreateMockData(amt.mockTc);
//			int rate = amt.baseRate + amt.addWhenFail * (rm.getTotalNum() + 1) - amt.reduceWhenSucess
//					* rm.getSucessNum();
//			if (rate > 0) {// 命中伪随机，则增加成功次数且执行TC
//				boolean hit = NumberUtil.isHit(rate, 100);
//				if (hit) {
//					TcResult mockResult = XsgRewardManager.getInstance().doTc(role, amt.mockTc);
//					rm.setSucessNum(rm.getSucessNum() + 1);
//					result.add(mockResult);
//				}
//			}
//			rm.setTotalNum(rm.getTotalNum() + 1);
//		}
//		return result;
//	}
}
