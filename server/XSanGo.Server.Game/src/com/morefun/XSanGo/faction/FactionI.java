package com.morefun.XSanGo.faction;

import java.util.ArrayList;
import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Faction_approveJoin;
import com.XSanGo.Protocol.AMD_Faction_createFaction;
import com.XSanGo.Protocol.AMD_Faction_findFaction;
import com.XSanGo.Protocol.AMD_Faction_getFactionInfo;
import com.XSanGo.Protocol.AMD_Faction_getFactionList;
import com.XSanGo.Protocol.AMD_Faction_getHarmRank;
import com.XSanGo.Protocol.AMD_Faction_getJoinRequestList;
import com.XSanGo.Protocol.AMD_Faction_getMemberRank;
import com.XSanGo.Protocol.AMD_Faction_getMyFaction;
import com.XSanGo.Protocol.AMD_Faction_getRivalFormation;
import com.XSanGo.Protocol.AMD_Faction_getTechnologyDonateLog;
import com.XSanGo.Protocol.AMD_Faction_getWarehouseItemQueue;
import com.XSanGo.Protocol.AMD_Faction_invite;
import com.XSanGo.Protocol.AMD_Faction_selectRival;
import com.XSanGo.Protocol.AMD_Faction_startBattle;
import com.XSanGo.Protocol.AMD_Faction_warehouseAllot;
import com.XSanGo.Protocol.FactionCopyResultView;
import com.XSanGo.Protocol.FactionListView;
import com.XSanGo.Protocol.MonsterView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpMovieView;
import com.XSanGo.Protocol._FactionDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.db.game.FactionReq;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.LuaSerializer;

public class FactionI extends _FactionDisp {
	private static final long serialVersionUID = -3603109228887606392L;

	private IRole roleRt;

	public FactionI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public void createFaction_async(final AMD_Faction_createFaction __cb, final String name, final String icon,
			Current __current) throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getFactionControler().createFaction(name, icon, __cb);
	}

	// @Override
	// public String getFactionList(int orderBy, Current __current) {
	// // FactionListView
	// List<FactionListView> list =
	// XsgFactionManager.getInstance().random20FacitonForList(this.roleRt.getRoleId());
	//
	// return LuaSerializer.serialize(list.toArray(new FactionListView[0]));
	// }

	@Override
	public void applyFor(String factionId, Current __current) throws NoteException {
		IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
		if (faction == null) {
			faction = XsgFactionManager.getInstance().findByShowId(factionId);
		}
		if (faction == null) {
			throw new NoteException(Messages.getString("FactionI.0")); //$NON-NLS-1$
		}

		this.getFactionControler().applyForFaction(faction);
	}

	private IFactionControler getFactionControler() {
		return this.roleRt.getFactionControler();
	}

	@Override
	public void cancelApplication(String factionId, Current __current) {
		this.getFactionControler().cancelApplication(factionId);
	}

	@Override
	public void getMyFaction_async(AMD_Faction_getMyFaction __cb, Current __current) {

		this.getFactionControler().getMyFactionInfo(__cb);
	}

	@Override
	public void approveJoin_async(AMD_Faction_approveJoin __cb, String applyId, Current __current) throws NoteException {
		this.getFactionControler().approveReq(applyId, __cb);
	}

	@Override
	public void denyJoin(String applyId, Current __current) {
		this.getFactionControler().denyReq(applyId);
	}

	@Override
	public void divorce(Current __current) throws NoteException {
		this.getFactionControler().divorceFaction();
	}

	@Override
	public void deleteMember(String memberId, Current __current) throws NoteException {
		this.getFactionControler().deleteMember(memberId);
	}

	@Override
	public void getJoinRequestList_async(AMD_Faction_getJoinRequestList __cb, Current __current) throws NoteException {
		this.getFactionControler().getJoinRequestList(__cb);
	}

	@Override
	public void updateNotice(String newNotice, Current __current) throws NoteException {
		this.getFactionControler().updateNotice(newNotice);
	}

	// @Override
	// public String findFaction(String factionId, Current __current) throws
	// NoteException {
	// IFaction faction =
	// XsgFactionManager.getInstance().findFactionById(factionId);
	// if (faction == null) {
	//			throw new NoteException(Messages.getString("FactionI.1")); //$NON-NLS-1$
	// }
	// List<FactionReq> reqList =
	// XsgFactionManager.getInstance().findCandidate(this.roleRt.getRoleId());
	// List<String> applyFactionList = new ArrayList<String>();
	// for (FactionReq req : reqList) {
	// applyFactionList.add(req.getFactionId());
	// }
	// int maxPeople =
	// XsgFactionManager.getInstance().getFactionLevelT(faction.getLevel()).people;
	// return LuaSerializer.serialize(new FactionListView(faction.getId(),
	// faction.getIcon(), faction.getName(),
	// faction.getLevel(), faction.getQQ(), faction.getMemberSize(),
	// applyFactionList.contains(faction.getId()), maxPeople,
	// faction.getJoinLevel()));
	// }

	@Override
	public void transferFaction(String targetId, Current __current) throws NoteException {
		this.roleRt.getFactionControler().transferFaction(targetId);
	}

	@Override
	public void upElder(String targetId, Current __current) throws NoteException {
		this.roleRt.getFactionControler().upElder(targetId);
	}

	@Override
	public void setCommon(String targetId, Current __current) throws NoteException {
		this.roleRt.getFactionControler().setCommon(targetId);
	}

	@Override
	public void factionConfig(String icon, String qq, String notice, int joinType, int joinLevel, int joinVip,
			String manifesto, int deleteDay, Current __current) throws NoteException {
		this.roleRt.getFactionControler().factionConfig(icon, qq, notice, joinType, joinLevel, joinVip, manifesto,
				deleteDay);
	}

	@Override
	public String getFactionHistorys(Current __current) {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().getFactionHistorys());
	}

	@Override
	public void donation(int num, Current __current) throws NoteException {
		this.roleRt.getFactionControler().donation(num);
	}

	@Override
	public String factionCopyList(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().factionCopyList());
	}

	@Override
	public int openFactionCopy(int copyId, Current __current) throws NoteException {
		return this.roleRt.getFactionControler().openFactionCopy(copyId);
	}

	@Override
	public String factionCopyInfo(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().factionCopyInfo());
	}

	@Override
	public FactionCopyResultView beginChallenge(Current __current) throws NoteException {
		return this.roleRt.getFactionControler().beginChallenge();
	}

	@Override
	public void endChallenge(MonsterView[] monsterViews, boolean isKill, boolean isHurtBlood, int dropBlood,
			Current __current) throws NoteException {
		this.roleRt.getFactionControler().endChallenge(monsterViews, isKill, isHurtBlood, dropBlood);
	}

	@Override
	public void closeFactionCopy(Current __current) throws NoteException {
		this.roleRt.getFactionControler().closeFactionCopy();
	}

	@Override
	public String getFactionShops(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().getFactionShops());
	}

	@Override
	public void buyFactionShop(int id, Current __current) throws NoteException {
		this.roleRt.getFactionControler().buyFactionShop(id);
	}

	@Override
	public String getGvgInfo(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().getGvgInfo());
	}

	@Override
	public void applyGvg(Current __current) throws NoteException {
		this.roleRt.getFactionControler().applyGvg();
	}

	@Override
	public void selectRival_async(AMD_Faction_selectRival __cb, int index, Current __current) throws NoteException {
		this.roleRt.getFactionControler().selectRival(__cb, index);
	}

	@Override
	public String endGvg(boolean isWin, int heroNum, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().endGvg(isWin, heroNum));
	}

	@Override
	public void reviveGvg(Current __current) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException {
		this.roleRt.getFactionControler().reviveGvg();
	}

	@Override
	public void getMemberRank_async(AMD_Faction_getMemberRank __cb, Current __current) throws NoteException {
		this.roleRt.getFactionControler().getMemberRank(__cb);
	}

	@Override
	public String getFactionRank(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFactionControler().getFactionRank());
	}

	@Override
	public void getRivalFormation_async(AMD_Faction_getRivalFormation __cb, String roleId, Current __current)
			throws NoteException {
		this.roleRt.getFactionControler().getRivalFormation(__cb, roleId);
	}

	@Override
	public String beginGvg(Current __current) throws NoteException {
		return this.roleRt.getFactionControler().beginGvg();
	}

	@Override
	public void getHarmRank_async(AMD_Faction_getHarmRank __cb, Current __current) throws NoteException {
		this.roleRt.getFactionControler().getHarmRank_async(__cb);
	}

	@Override
	public void rename(String newName, Current __current) throws NoteException {
		this.roleRt.getFactionControler().rename(newName);
	}

	@Override
	public int sendFactionMail(int type, String title, String content, Current __current) throws NoteException {
		return roleRt.getFactionControler().sendFactionMail(type, title, content);
	}

	@Override
	public void getFactionList_async(final AMD_Faction_getFactionList __cb, int orderBy, Current __current) {
		final List<FactionListView> list = XsgFactionManager.getInstance().random20FacitonForList(
				this.roleRt.getRoleId(), orderBy);
		final List<String> bossIds = new ArrayList<String>();
		for (FactionListView fv : list) {
			bossIds.add(XsgFactionManager.getInstance().findFaction(fv.id).getBossId());
		}

		XsgRoleManager.getInstance().loadRoleAsync(bossIds, new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < bossIds.size(); i++) {
					IRole bossRole = XsgRoleManager.getInstance().findRoleById(bossIds.get(i));
					if (bossRole != null) {
						list.get(i).bossName = bossRole.getName();
						list.get(i).bossVip = bossRole.getVipLevel();
					}
				}
				__cb.ice_response(LuaSerializer.serialize(list.toArray(new FactionListView[0])));
			}
		});
	}

	@Override
	public void findFaction_async(final AMD_Faction_findFaction __cb, String factionId, Current __current)
			throws NoteException {
		final List<IFaction> factions = XsgFactionManager.getInstance().findByShowIdOrName(factionId);
		if (factions.isEmpty()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionI.1")));
			return;
		}

		final List<String> ids = new ArrayList<String>();
		for (IFaction f : factions) {
			ids.add(f.getBossId());
		}

		List<FactionReq> reqList = XsgFactionManager.getInstance().findCandidate(this.roleRt.getRoleId());
		final List<String> applyFactionList = new ArrayList<String>();
		for (FactionReq req : reqList) {
			applyFactionList.add(req.getFactionId());
		}

		XsgRoleManager.getInstance().loadRoleAsync(ids, new Runnable() {

			@Override
			public void run() {
				List<FactionListView> views = new ArrayList<FactionListView>();
				for (IFaction f : factions) {
					IRole boss = XsgRoleManager.getInstance().findRoleById(f.getBossId());
					int maxPeople = XsgFactionManager.getInstance().getFactionLevelT(f.getLevel()).people;

					String bossName = boss == null ? "unknown" : boss.getName();
					int bossVip = boss == null ? 0 : boss.getVipLevel();
					views.add(new FactionListView(f.getId(), f.getIcon(), f.getName(), f.getLevel(), f.getQQ(), f
							.getMemberSize(), applyFactionList.contains(f.getId()), maxPeople, f.getJoinLevel(),
							bossName, bossVip, f.getJoinVip(), f.getManifesto()));
				}
				__cb.ice_response(LuaSerializer.serialize(views.toArray(new FactionListView[0])));
			}
		});
	}

	@Override
	public String getFactionMailLog(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().getFactionMailLog());
	}

	@Override
	public String openWarehouse(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().openWarehouse());
	}

	@Override
	public void warehouseAllot_async(AMD_Faction_warehouseAllot __cb, String roleId, String itemId, int num,
			Current __current) throws NoteException {
		roleRt.getFactionControler().warehouseAllot(__cb, roleId, itemId, num);
	}

	@Override
	public String openStorehouse(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().openStorehouse());
	}

	@Override
	public void storehousePurchase(String itemId, int num, Current __current) throws NoteException {
		roleRt.getFactionControler().storehousePurchase(itemId, num);
	}

	@Override
	public String openOviStore(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().openOviStore());
	}

	@Override
	public void oviStoreBuy(String itemId, int num, Current __current) throws NoteException {
		roleRt.getFactionControler().oviStoreBuy(itemId, num);
	}

	@Override
	public String technologyList(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().technologyList());
	}

	@Override
	public int setRecommendTechnology(int id, Current __current) throws NoteException {
		return roleRt.getFactionControler().setRecommendTechnology(id);
	}

	@Override
	public void donateTechnology(int id, int type, Current __current) throws NoteException {
		roleRt.getFactionControler().donateTechnology(id, type);
	}

	@Override
	public void clearDonateCD(Current __current) throws NoteException {
		roleRt.getFactionControler().clearDonateCD();
	}

	@Override
	public void studyTechnology(int id, Current __current) throws NoteException {
		roleRt.getFactionControler().studyTechnology(id);
	}

	@Override
	public String openFactionBattle(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().openFactionBattle());
	}

	@Override
	public String enrollFactionBattle(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().enrollFactionBattle());
	}

	@Override
	public String changeFactionBattleCamp(Current __current) throws NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().changeFactionBattleCamp());
	}

	@Override
	public String enterFactionBattle(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().enterFactionBattle());
	}

	@Override
	public void leaveFactionBattle(Current __current) throws NoteException {
		roleRt.getFactionBattleController().leaveFactionBattle();
	}

	@Override
	public String lookFactionBattleRank(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().lookFactionBattleRank());
	}

	@Override
	public String lookFactionBattlePersonalRank(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().lookFactionBattlePersonalRank());
	}

	@Override
	public String marching(boolean isUseKits, int strongholdId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().marching(isUseKits, strongholdId));
	}

	@Override
	public void buyMarchingCooling(Current __current) throws NotEnoughYuanBaoException, NoteException {
		roleRt.getFactionBattleController().buyMarchingCooling();
	}

	@Override
	public String diggingTreasure(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().diggingTreasure());
	}

	@Override
	public String useKits(int kitsId, Current __current) throws NoteException {
		return roleRt.getFactionBattleController().useKits(kitsId);
	}

	@Override
	public void startBattle_async(AMD_Faction_startBattle __cb, byte type, Current __current) throws NoteException {
		roleRt.getFactionBattleController().startBattle_async(__cb, type);
	}

	@Override
	public void resultConfirm(Current __current) throws NoteException {
		roleRt.getFactionBattleController().resultConfirm();
	}

	@Override
	public PvpMovieView lookFactionBattleMovieView(Current __current) throws NoteException {
		return roleRt.getFactionBattleController().lookFactionBattleMovieView();
	}

	@Override
	public String lookFactionBattleLog(byte logType, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionBattleController().lookFactionBattleLog(logType));
	}

	@Override
	public String getFactionAllotLog(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().getFactionAllotLog());
	}

	@Override
	public void getTechnologyDonateLog_async(AMD_Faction_getTechnologyDonateLog __cb, Current __current)
			throws NoteException {
		roleRt.getFactionControler().getTechnologyDonateLog(__cb);
	}

	@Override
	public String getPurchaseLog(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().getPurchaseLog());
	}

	@Override
	public void demandItem(String itemId, int type, Current __current) throws NoteException {
		roleRt.getFactionControler().demandItem(itemId, type);
	}

	@Override
	public void applyItem(int id, int type, Current __current) throws NoteException {
		roleRt.getFactionControler().applyItem(id, type);
	}

	@Override
	public void getWarehouseItemQueue_async(AMD_Faction_getWarehouseItemQueue __cb, int id, Current __current)
			throws NoteException {
		roleRt.getFactionControler().getWarehouseItemQueue(__cb, id);
	}

	@Override
	public int getBeforePeople(String itemId, Current __current) throws NoteException {
		return roleRt.getFactionControler().getBeforePeople(itemId);
	}

	@Override
	public boolean recruit(boolean isFree, Current __current) throws NoteException {
		return roleRt.getFactionControler().recruit(isFree);
	}

	@Override
	public void invite_async(AMD_Faction_invite __cb, boolean isFree, String roleId, Current __current)
			throws NoteException {
		roleRt.getFactionControler().invite(__cb, isFree, roleId);
	}

	@Override
	public String getRecruitCount(int type, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFactionControler().getRecruitCount(type));
	}

	@Override
	public void getFactionInfo_async(AMD_Faction_getFactionInfo __cb, String id, Current __current) {
		roleRt.getFactionControler().getFactionInfo(__cb, id);
	}

}
