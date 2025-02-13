/**
 * 
 */
package com.morefun.XSanGo.rankList;

import Ice.NoEndpointException;

import com.XSanGo.Protocol.AMD_RankList_selFactionDetail;
import com.XSanGo.Protocol.AMD_RankList_selRoleDetail;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RankFactionDetail;
import com.XSanGo.Protocol.RankListShow;
import com.XSanGo.Protocol.RankListSub;
import com.XSanGo.Protocol.RankRoleDetail;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IApproveJoin;
import com.morefun.XSanGo.event.protocol.ICombatPowerChange;
import com.morefun.XSanGo.event.protocol.ICreateFaction;
import com.morefun.XSanGo.event.protocol.IDeleteFactionMenber;
import com.morefun.XSanGo.event.protocol.IFactionLevelUp;
import com.morefun.XSanGo.event.protocol.IFactionRename;
import com.morefun.XSanGo.event.protocol.IGetAchieve;
import com.morefun.XSanGo.event.protocol.IQuitFaction;
import com.morefun.XSanGo.event.protocol.IWorship;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 各种排行榜
 * 
 * @author 吕明涛
 * 
 */
class RankListControler implements IRankListControler, IWorship, ICreateFaction, IQuitFaction, IApproveJoin,
		IDeleteFactionMenber, IFactionLevelUp, IFactionRename, ICombatPowerChange, IGetAchieve {

	private IRole roleRt;

	public RankListControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;

		// 膜拜事件
		IEventControler eventControler = roleRt.getEventControler();
		eventControler.registerHandler(IWorship.class, this);

		// 战力事件
		eventControler.registerHandler(ICombatPowerChange.class, this);

		// 公会事件
		eventControler.registerHandler(ICreateFaction.class, this);
		eventControler.registerHandler(IQuitFaction.class, this);
		eventControler.registerHandler(IApproveJoin.class, this);
		eventControler.registerHandler(IDeleteFactionMenber.class, this);
		eventControler.registerHandler(IFactionLevelUp.class, this);
		eventControler.registerHandler(IFactionRename.class, this);

		// 成就领取事件
		eventControler.registerHandler(IGetAchieve.class, this);

	}

	/**
	 * 查询 部队战力排名
	 */
	@Override
	public RankListShow selRankListCombat() throws NoteException {
		if (this.roleRt.getLevel() >= XsgGameParamManager.getInstance().getRankLevel()) {
			// 默认排名，没有进入排行榜
			RankListShow resRankListShow = new RankListShow();
			resRankListShow.ownRank = XsgRankListManager.getInstance().selectOwnCombatRank(roleRt.getRoleId());
			resRankListShow.ownValue = this.roleRt.getCachePower();

			this.setCombatShow(resRankListShow, XsgGameParamManager.getInstance().getRankListCombatNum());

			return resRankListShow;
		} else {
			throw new NoteException(Messages.getString("RankListControler.0")); //$NON-NLS-1$
		}
	}

	/**
	 * 查询 成就排名
	 */
	@Override
	public RankListShow selRankListAchieve() throws NoteException {
		if (this.roleRt.getLevel() >= XsgGameParamManager.getInstance().getRankLevel()) {
			// 默认排名，没有进入排行榜
			RankListShow resRankListShow = new RankListShow();
			resRankListShow.ownRank = XsgRankListManager.getInstance().selectOwnAchieveRank(roleRt.getRoleId());
			resRankListShow.ownValue = this.roleRt.getCompletedAchieve();

			this.setAchieveShow(resRankListShow, XsgGameParamManager.getInstance().getRankListAchieveNum());

			return resRankListShow;
		} else {
			throw new NoteException(Messages.getString("RankListControler.0")); //$NON-NLS-1$
		}
	}

	/**
	 * 查询 膜拜次数 排名
	 */
	@Override
	public RankListShow selRankListWorship() throws NoteException {
		if (this.roleRt.getLevel() >= XsgGameParamManager.getInstance().getRankLevel()) {
			// 默认排名，没有进入排行榜
			RankListShow resRankListShow = new RankListShow();
			resRankListShow.ownRank = XsgRankListManager.getInstance().selectOwnWorshipRank(roleRt.getRoleId());
			resRankListShow.ownValue = XsgRankListManager.getInstance().getWorshipMap(this.roleRt.getRoleId());

			this.setWorshipShow(resRankListShow, XsgGameParamManager.getInstance().getRankListWorshipNum());

			return resRankListShow;
		} else {
			throw new NoteException(Messages.getString("RankListControler.1")); //$NON-NLS-1$
		}
	}

	/**
	 * 查询 公会 排名
	 */
	@Override
	public RankListShow selRankListFaction() throws NoteException {
		if (this.roleRt.getLevel() >= XsgGameParamManager.getInstance().getRankLevel()) {
			// 默认排名，没有进入排行榜
			RankListShow resRankListShow = new RankListShow();
			resRankListShow.ownRank = XsgRankListManager.getInstance().selectOwnFactionRank(
					this.roleRt.getFactionControler().getFactionId());
			resRankListShow.ownValue = this.roleRt.getFactionControler().getFactionLevel();

			// 判断显示 排行榜的数量
			int showNum = XsgGameParamManager.getInstance().getRankListFactionNum();
			if (showNum > XsgRankListManager.getInstance().getFactionListNum()) {
				showNum = XsgRankListManager.getInstance().getFactionListNum();
			}

			// 设置显示 排行榜的详细信息
			RankListSub[] factionArr = new RankListSub[showNum];
			for (int i = 0; i < showNum; i++) {
				RankListSub rankListSub = XsgRankListManager.getInstance().getFaction(i);
				factionArr[i] = (RankListSub) rankListSub.clone();
				factionArr[i].rank = i + 1;

				IFaction faction = XsgFactionManager.getInstance().findFaction(factionArr[i].roleId);
				if (faction != null) {
					factionArr[i].count = faction.getMemberSize();
					factionArr[i].roleId = faction.getSubId();
				}
			}
			resRankListShow.rankList = factionArr;

			return resRankListShow;
		} else {
			throw new NoteException(Messages.getString("RankListControler.2")); //$NON-NLS-1$
		}
	}

	/**
	 * 设置 战力的显示的排行榜
	 */
	private void setCombatShow(RankListShow resRankListShow, int showNum) {

		// 判断显示 排行榜的数量
		if (showNum > XsgRankListManager.getInstance().getCombatListNum()) {
			showNum = XsgRankListManager.getInstance().getCombatListNum();
		}

		// 设置显示 排行榜的详细信息
		RankListSub[] factionArr = new RankListSub[showNum];
		for (int i = 0; i < showNum; i++) {
			factionArr[i] = (RankListSub) XsgRankListManager.getInstance().getCombat(i).clone();
			factionArr[i].rank = i + 1;

			// 在排名范围内，重新设置排名
			if (factionArr[i].roleId.equals(this.roleRt.getRoleId())) {
				resRankListShow.ownRank = i + 1;
			}
		}
		resRankListShow.rankList = factionArr;
	}

	/**
	 * 设置 成就的显示的排行榜
	 */
	private void setAchieveShow(RankListShow resRankListShow, int showNum) {

		// 判断显示 排行榜的数量
		if (showNum > XsgRankListManager.getInstance().getAchieveListNum()) {
			showNum = XsgRankListManager.getInstance().getAchieveListNum();
		}

		// 设置显示 排行榜的详细信息
		RankListSub[] factionArr = new RankListSub[showNum];
		for (int i = 0; i < showNum; i++) {
			factionArr[i] = (RankListSub) XsgRankListManager.getInstance().getAchieve(i).clone();
			factionArr[i].rank = i + 1;

			// 在排名范围内，重新设置排名
			if (factionArr[i].roleId.equals(this.roleRt.getRoleId())) {
				resRankListShow.ownRank = i + 1;
			}
		}
		resRankListShow.rankList = factionArr;
	}

	/**
	 * 设置 膜拜的显示的排行榜
	 */
	private void setWorshipShow(RankListShow resRankListShow, int showNum) {

		// 判断显示 排行榜的数量
		if (showNum > XsgRankListManager.getInstance().getWorshipListNum()) {
			showNum = XsgRankListManager.getInstance().getWorshipListNum();
		}

		// 设置显示 排行榜的详细信息
		RankListSub[] factionArr = new RankListSub[showNum];
		for (int i = 0; i < showNum; i++) {
			factionArr[i] = (RankListSub) XsgRankListManager.getInstance().getWorship(i).clone();
			factionArr[i].rank = i + 1;

			// 在排名范围内，重新设置排名
			if (factionArr[i].roleId.equals(this.roleRt.getRoleId())) {
				resRankListShow.ownRank = i + 1;
				resRankListShow.ownValue = factionArr[i].count;
			}
		}
		resRankListShow.rankList = factionArr;
	}

	/**
	 * 战力变化 事件
	 */
	private void onCombatPowerChange(int combatPower) {
		if (combatPower > 0) {
			RankListSub newCombat = this.setRankListSub(this.roleRt, combatPower);

			int combatListNum = XsgRankListManager.getInstance().getCombatListNum();
			if (combatListNum > 0) {
				// 排行榜数量是否达到要求数量 和 到了数量后，和最后一名的比较
				int showAll = XsgGameParamManager.getInstance().getRankListCombatAll();
				if (combatListNum < showAll
						|| combatPower >= XsgRankListManager.getInstance().getCombat(combatListNum - 1).count) {
					XsgRankListManager.getInstance().setCombatList(newCombat);
				}
			} else {
				XsgRankListManager.getInstance().addCombatList(newCombat);
			}
		}
	}

	/**
	 * 成就变化 事件
	 */
	private void onAchieveChange(int num) {
		if (num > 0) {
			RankListSub newAchieve = this.setRankListSub(this.roleRt, num);

			int achieveListNum = XsgRankListManager.getInstance().getAchieveListNum();
			if (achieveListNum > 0) {
				// 排行榜数量是否达到要求数量 和 到了数量后，和最后一名的比较
				int showAll = XsgGameParamManager.getInstance().getRankListAchieveAll();
				if (achieveListNum < showAll
						|| num >= XsgRankListManager.getInstance().getAchieve(achieveListNum - 1).count) {
					XsgRankListManager.getInstance().setAchieveList(newAchieve);
				}
			} else {
				XsgRankListManager.getInstance().addAchieveList(newAchieve);
			}
		}
	}

	/**
	 * 膜拜变化 事件
	 */
	@Override
	public void onWorship(final String roleId, final int count) {
		// 保存被膜拜的次数
		XsgRankListManager.getInstance().setWorshipMap(roleId, count);

		if (count > 0) {
			XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
				@Override
				public void run() {
					IRole worshipRole = XsgRoleManager.getInstance().findRoleById(roleId);
					RankListSub newWorship = setRankListSub(worshipRole, count);

					int worshipNum = XsgRankListManager.getInstance().getWorshipListNum();
					if (worshipNum > 0) {
						// 排行榜数量是否达到要求数量 和 到了数量后，和最后一名的比较
						int showAll = XsgGameParamManager.getInstance().getRankListWorshipAll();
						if (worshipNum < showAll
								|| count >= XsgRankListManager.getInstance().getWorship(worshipNum - 1).count) {
							XsgRankListManager.getInstance().setWorshipList(newWorship);
						}
					} else {
						XsgRankListManager.getInstance().setWorshipList(newWorship);
					}
				}
			}, new Runnable() {
				@Override
				public void run() {
				}
			});
		}
	}

	/**
	 * 公会 事件
	 */
	private void onFactionChange(String factionId) {
		RankListSub newFaction = this.setRankListFaction(factionId);
		XsgRankListManager.getInstance().setFactionList(newFaction);
	}

	/**
	 * 设置替换排行榜的数据
	 * 
	 * @param newRole
	 * @param count
	 * @return
	 */
	private RankListSub setRankListSub(IRole newRole, int count) {
		RankListSub rsWorship = new RankListSub();
		rsWorship.roleId = newRole.getRoleId();
		rsWorship.roleName = newRole.getName();
		rsWorship.level = newRole.getLevel();
		rsWorship.vipLevel = newRole.getVipLevel();
		rsWorship.icon = newRole.getArenaRankControler().rankIcon(newRole);
		rsWorship.count = count;

		return rsWorship;
	}

	/**
	 * 设置替换排行榜的数据
	 * 
	 * @param newRole
	 * @param count
	 * @return
	 */
	private RankListSub setRankListFaction(String factionId) {
		RankListSub rsWorship = new RankListSub();
		IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
		rsWorship.roleId = faction.getId();
		rsWorship.roleName = faction.getName();
		rsWorship.level = faction.getLevel();
		rsWorship.icon = faction.getIcon();
		rsWorship.count = faction.getMemberSize();

		return rsWorship;
	}

	/**
	 * 查询排行详情
	 * 
	 * @param ownRank
	 * @param ownValue
	 */
	@Override
	public void selRoleDetail(final AMD_RankList_selRoleDetail __cb, final String roleId) {

		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
				if (!role.equals("")) { //$NON-NLS-1$
					// 默认部队
					IFormation formation = role.getFormationControler().getDefaultFormation();

					RankRoleDetail detail = new RankRoleDetail();
					detail.roleId = role.getRoleId();
					detail.factionName = role.getFactionControler().getFactionName();
					detail.heroArr = formation.getSummaryView();
					if (formation.getBuff().getTemplate() != null) {
						detail.formationBuff = formation.getBuff().getTemplate().getName();
					} else {
						detail.formationBuff = ""; //$NON-NLS-1$
					}

					__cb.ice_response(LuaSerializer.serialize(detail));
				} else {
					__cb.ice_exception(new NoteException(Messages.getString("RankListControler.5"))); //$NON-NLS-1$
				}
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("RankListControler.6"))); //$NON-NLS-1$
			}
		});
	}

	/**
	 * 公会 创建 事件
	 */
	@Override
	public void onCreateFaction(String factionId) {
		this.onFactionChange(factionId);
	}

	/**
	 * 公会详情
	 */
	@Override
	public void selFactionDetail(final AMD_RankList_selFactionDetail __cb, String factionId) throws NoteException {
		final IFaction faction = XsgFactionManager.getInstance().findByShowId(factionId);
		if (faction != null) {

			XsgRoleManager.getInstance().loadRoleByIdAsync(faction.getBossId(), new Runnable() {
				@Override
				public void run() {
					IRole bossRole = XsgRoleManager.getInstance().findRoleById(faction.getBossId());
					RankFactionDetail detail = new RankFactionDetail();
					detail.name = faction.getName();
					detail.level = faction.getLevel();
					detail.bossId = faction.getBossId();
					detail.bossName = bossRole.getName();
					detail.bossLevel = bossRole.getLevel();
					detail.bossVipLevel = bossRole.getVipLevel();
					detail.qq = faction.getQQ();
					detail.announcement = faction.getAnnounce();

					__cb.ice_response(LuaSerializer.serialize(detail));
				}
			}, new Runnable() {
				@Override
				public void run() {
					__cb.ice_exception(new NoteException(Messages.getString("RankListControler.7"))); //$NON-NLS-1$
				}
			});

		} else {
			__cb.ice_exception(new NoEndpointException(Messages.getString("RankListControler.8"))); //$NON-NLS-1$
		}
	}

	// 公会升级事件
	@Override
	public void onFactionLevelUp(String factionId, int level) {
		this.onFactionChange(factionId);
	}

	// 删除公会会员
	@Override
	public void onDeleteFactionMenber(String factionId, String roleId) {
		this.onFactionChange(factionId);
	}

	// 批准加入公会
	@Override
	public void onApproveJoin(String factionId, String roleId) {
		this.onFactionChange(factionId);
	}

	// 退出公会
	@Override
	public void onQuitFaction(String factionId, String roleId) {
		this.onFactionChange(factionId);
	}

	@Override
	public void onFactionRename(String factionId, String oldName, String newName) {
		this.onFactionChange(factionId);
	}

	@Override
	public void factionChannge(String factionId) {
		this.onFactionChange(factionId);
	}

	@Override
	public void onCombatPowerChange(int old, int newValue) {
		this.onCombatPowerChange(newValue);
	}

	@Override
	public void onGetAchieve(int achieveId) {
		onAchieveChange(this.roleRt.getCompletedAchieve());
	}
}
