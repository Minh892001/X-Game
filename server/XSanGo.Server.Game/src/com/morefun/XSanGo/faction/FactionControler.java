package com.morefun.XSanGo.faction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;

import com.XSanGo.Protocol.AMD_Faction_approveJoin;
import com.XSanGo.Protocol.AMD_Faction_createFaction;
import com.XSanGo.Protocol.AMD_Faction_getFactionInfo;
import com.XSanGo.Protocol.AMD_Faction_getHarmRank;
import com.XSanGo.Protocol.AMD_Faction_getJoinRequestList;
import com.XSanGo.Protocol.AMD_Faction_getMemberRank;
import com.XSanGo.Protocol.AMD_Faction_getMyFaction;
import com.XSanGo.Protocol.AMD_Faction_getRivalFormation;
import com.XSanGo.Protocol.AMD_Faction_getTechnologyDonateLog;
import com.XSanGo.Protocol.AMD_Faction_getWarehouseItemQueue;
import com.XSanGo.Protocol.AMD_Faction_invite;
import com.XSanGo.Protocol.AMD_Faction_selectRival;
import com.XSanGo.Protocol.AMD_Faction_warehouseAllot;
import com.XSanGo.Protocol.AdditionType;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.CopyHarmRankView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FactionAllotLog;
import com.XSanGo.Protocol.FactionCopyInfoView;
import com.XSanGo.Protocol.FactionCopyResultView;
import com.XSanGo.Protocol.FactionHistoryView;
import com.XSanGo.Protocol.FactionMailLog;
import com.XSanGo.Protocol.FactionMemberView;
import com.XSanGo.Protocol.FactionOviStoreView;
import com.XSanGo.Protocol.FactionRankElement;
import com.XSanGo.Protocol.FactionRankView;
import com.XSanGo.Protocol.FactionReqView;
import com.XSanGo.Protocol.FactionShop;
import com.XSanGo.Protocol.FactionShopView;
import com.XSanGo.Protocol.FactionStorehouseView;
import com.XSanGo.Protocol.FactionTechnology;
import com.XSanGo.Protocol.FactionTechnologyView;
import com.XSanGo.Protocol.FactionView;
import com.XSanGo.Protocol.FactionWarehouseView;
import com.XSanGo.Protocol.GvgChallengeView;
import com.XSanGo.Protocol.GvgView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.MemberRankElement;
import com.XSanGo.Protocol.MemberRankView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.MonsterView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OviStoreItem;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PurchaseLog;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalView;
import com.XSanGo.Protocol.StorehouseItem;
import com.XSanGo.Protocol.TechnologyDonateLog;
import com.XSanGo.Protocol.TechnologyDonateView;
import com.XSanGo.Protocol.TextMessage;
import com.XSanGo.Protocol.WarehouseItem;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.Const.PropertyName;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.copy.MonsterT;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Faction;
import com.morefun.XSanGo.db.game.FactionCopy;
import com.morefun.XSanGo.db.game.FactionDAO;
import com.morefun.XSanGo.db.game.FactionHistory;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.FactionMemberRank;
import com.morefun.XSanGo.db.game.FactionReq;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFaction;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.event.protocol.IApproveJoin;
import com.morefun.XSanGo.event.protocol.IBuyFactionShop;
import com.morefun.XSanGo.event.protocol.ICreateFaction;
import com.morefun.XSanGo.event.protocol.IDeleteFactionMenber;
import com.morefun.XSanGo.event.protocol.IDenyJoin;
import com.morefun.XSanGo.event.protocol.IFactionApply;
import com.morefun.XSanGo.event.protocol.IFactionCopyEndChallenge;
import com.morefun.XSanGo.event.protocol.IFactionDonateTec;
import com.morefun.XSanGo.event.protocol.IFactionDonation;
import com.morefun.XSanGo.event.protocol.IFactionGvgEnd;
import com.morefun.XSanGo.event.protocol.IFactionGvgRevive;
import com.morefun.XSanGo.event.protocol.IFactionLevelUp;
import com.morefun.XSanGo.event.protocol.IFactionRename;
import com.morefun.XSanGo.event.protocol.IQuitFaction;
import com.morefun.XSanGo.faction.FactionShopT.CoinType;
import com.morefun.XSanGo.faction.XsgFactionManager.TechnologyType;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.IItemControler;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.SensitiveWordManager;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.worldboss.WorldBossManager;

@RedPoint(isTimer = true)
public class FactionControler implements IFactionControler {

	private IRole roleRt;
	private Role roleDb;
	private ICreateFaction createFactionEvent;
	private IQuitFaction quitFactionEvent;
	private IApproveJoin approveJoinEvent;
	private IDenyJoin denyJoinEvent;
	private IDeleteFactionMenber deleteFactionMenberEvent;
	private IFactionLevelUp factionLevelUpEvent;
	private IFactionDonation factionDonationEvent;
	private IFactionCopyEndChallenge factionCopyEndChallengeEvent;
	private IBuyFactionShop buyFactionShopEvent;
	private IFactionApply factionApplyEvent;
	private IFactionGvgEnd factionGvgEndEvent;
	private IFactionGvgRevive factionGvgReviveEvent;
	private IFactionRename factionRenameEvent;
	private IFactionDonateTec factionDonateTecEvent;
	private static Pattern factionNamePattern = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5]{2,8}$");
	/** 合服后的公会名判定规则 */
	private static Pattern combineServerNamePattern = Pattern.compile("^s\\d+\\.\\S+$");
	private ItemView[] randomItems;// 每次挑战都掉落
	private ItemView[] killItems;// 杀死小怪掉落
	private ItemView[] bloodItems;// boss掉血量达到掉落
	/** 公会战挑战对手ID */
	private String rivalId;
	/** 战报ID上下文缓存 */
	private String fightMovieIdContext;

	/**
	 * 是否有捐赠CD
	 */
	private boolean hasCd;

	/**
	 * 捐赠CD时间
	 */
	private Date donateCd;

	/**
	 * 副本开始挑战时间，用来检测异常中断
	 */
	private Date copyBeginDate;

	/**
	 * 仓库每天可分配物品数量
	 */
	private int allotNum = 3;

	public FactionControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;
		this.createFactionEvent = this.roleRt.getEventControler().registerEvent(ICreateFaction.class);
		this.quitFactionEvent = this.roleRt.getEventControler().registerEvent(IQuitFaction.class);
		this.approveJoinEvent = this.roleRt.getEventControler().registerEvent(IApproveJoin.class);
		this.denyJoinEvent = this.roleRt.getEventControler().registerEvent(IDenyJoin.class);
		this.deleteFactionMenberEvent = this.roleRt.getEventControler().registerEvent(IDeleteFactionMenber.class);
		this.factionLevelUpEvent = this.roleRt.getEventControler().registerEvent(IFactionLevelUp.class);
		this.factionDonationEvent = this.roleRt.getEventControler().registerEvent(IFactionDonation.class);
		this.buyFactionShopEvent = this.roleRt.getEventControler().registerEvent(IBuyFactionShop.class);
		this.factionDonateTecEvent = this.roleRt.getEventControler().registerEvent(IFactionDonateTec.class);
		factionCopyEndChallengeEvent = this.roleRt.getEventControler().registerEvent(IFactionCopyEndChallenge.class);
		factionApplyEvent = this.roleRt.getEventControler().registerEvent(IFactionApply.class);
		factionGvgEndEvent = this.roleRt.getEventControler().registerEvent(IFactionGvgEnd.class);
		factionGvgReviveEvent = this.roleRt.getEventControler().registerEvent(IFactionGvgRevive.class);
		factionRenameEvent = this.roleRt.getEventControler().registerEvent(IFactionRename.class);

		if (roleDb.getRoleFaction() == null) {
			roleDb.setRoleFaction(new RoleFaction(GlobalDataManager.getInstance().generatePrimaryKey(), roleDb, null,
					"", 0));
		}
	}

	@Override
	public boolean isInFaction() {
		boolean inFaction = true;
		if (TextUtil.isBlank(this.roleDb.getFactionId())) {
			inFaction = false;
		} else {
			IFaction faction = getMyFaction();
			if (faction == null) {
				inFaction = false;
			} else {
				FactionMember member = faction.getMemberByRoleId(this.roleRt.getRoleId());
				if (member == null) {
					this.setNoFaction();
					inFaction = false;
				}
			}
		}
		return inFaction;
	}

	@Override
	public String getFactionName() {
		IFaction faction = getMyFaction();
		return faction == null ? Messages.getString("FactionControler.1") : faction.getName(); //$NON-NLS-1$
	}

	@Override
	public void setFaction(String id) {
		if (this.isInFaction()) {
			throw new IllegalStateException(Messages.getString("FactionControler.2")); //$NON-NLS-1$
		}

		this.roleDb.setFactionId(id);
		this.clearApplications();
		this.roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.HasFaction, 1);
	}

	private void clearApplications() {
		List<FactionReq> reqList = XsgFactionManager.getInstance().findCandidate(this.roleRt.getRoleId());
		for (FactionReq factionReq : reqList) {
			XsgFactionManager.getInstance().removeFactionReq(factionReq.getId());
		}
	}

	@Override
	public void applyForFaction(IFaction faction) throws NoteException {
		if (this.isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.3"));
		}
		if (this.isFull(faction)) {
			throw new NoteException(Messages.getString("FactionControler.4"));
		}
		List<FactionReq> reqList = XsgFactionManager.getInstance().findCandidate(this.roleRt.getRoleId());
		if (reqList.size() >= 5) {
			throw new NoteException(Messages.getString("FactionControler.5"));
		}
		if (this.roleDb.getQuitFactionDate() != null) {
			long time = DateUtil.compareTime(new Date(), this.roleDb.getQuitFactionDate());
			FactionConfigT config = XsgFactionManager.getInstance().getFactionConfigT();
			if (time < config.quitJoinMinute * 60 * 1000) {
				String tips = TextUtil.format(Messages.getString("FactionControler.6"), config.quitJoinMinute / 60);
				throw new NoteException(tips);
			}
		}
		if (roleRt.getLevel() < faction.getJoinLevel()) {
			throw new NoteException(Messages.getString("FactionControler.notJoinLevel"));
		}
		if (roleRt.getVipLevel() < faction.getJoinVip()) {
			throw new NoteException(Messages.getString("FactionControler.notJoinVip"));
		}
		if (faction.getJoinType() == Const.Faction.JOIN_TYPE_REFUSE) {
			throw new NoteException(Messages.getString("FactionControler.7"));
		} else if (faction.getJoinType() == Const.Faction.JOIN_TYPE_NOTCHECK) {
			faction.approveReq(this.roleRt);
			String tips = TextUtil.format(Messages.getString("FactionControler.8"), faction.getName());
			this.roleRt.getNotifyControler().showTips(tips);
			approveJoinEvent.onApproveJoin(faction.getId(), this.roleRt.getRoleId());
			return;
		}
		for (FactionReq r : reqList) {
			if (r.getFactionId().equals(faction.getId())) {
				throw new NoteException(Messages.getString("FactionControler.9"));
			}
		}
		XsgFactionManager.getInstance().addFactionReq(
				new FactionReq(GlobalDataManager.getInstance().generatePrimaryKey(), faction.getId(), this.roleRt
						.getRoleId()));
	}

	@Override
	public void getMyFactionInfo(final AMD_Faction_getMyFaction __cb) {
		if (!this.isInFaction()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.10")));
			return;
		}
		final List<String> roleIds = new ArrayList<String>();
		final IFaction myFaction = getMyFaction();
		for (FactionMember member : myFaction.getAllMember()) {
			roleIds.add(member.getRoleId());
		}
		XsgFactionManager.getInstance().autoTransfer(myFaction);
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<FactionMemberView> list = new ArrayList<FactionMemberView>();
				List<FactionMember> factionMembers = new ArrayList<FactionMember>();
				for (FactionMember fm : myFaction.getAllMember()) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					if (iRole == null) {
						continue;
					}
					fm.setLevel(iRole.getLevel());
					factionMembers.add(fm);
				}
				// 排序 职位、等级、VIP等级、ID
				Collections.sort(factionMembers, new Comparator<FactionMember>() {

					@Override
					public int compare(FactionMember o1, FactionMember o2) {
						if (o1.getDutyId() != o2.getDutyId()) {
							return o2.getDutyId() - o1.getDutyId();
						} else if (o1.getLevel() != o2.getLevel()) {
							return o2.getLevel() - o1.getLevel();
						} else {
							IRole iRole1 = XsgRoleManager.getInstance().findRoleById(o1.getRoleId());
							IRole iRole2 = XsgRoleManager.getInstance().findRoleById(o2.getRoleId());
							if (iRole2.getVipLevel() != iRole1.getVipLevel()) {
								return iRole2.getVipLevel() - iRole1.getVipLevel();
							} else {
								return o1.getId().compareTo(o2.getId());
							}
						}
					}
				});
				for (FactionMember fm : factionMembers) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					int passMinute = (int) (DateUtil.compareTime(new Date(), iRole.getLoginTime()) / 60000);
					list.add(new FactionMemberView(fm.getId(), iRole.getRoleId(), iRole.getName(),
							iRole.getHeadImage(), iRole.getLevel(), fm.getDutyId(), iRole.getVipLevel(), fm
									.getContribution(), passMinute, fm.getHonor(), iRole.getFactionControler()
									.getCanAllotItemNum(), fm.getDemandItem()));
				}
				int levelUpExp = XsgFactionManager.getInstance().getLevelUpExp(myFaction.getLevel());
				FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(myFaction.getLevel());
				FactionMember self = myFaction.getMemberByRoleId(roleRt.getRoleId());

				refreshMailCount();
				int maxMailTimes = XsgFactionManager.getInstance().getFactionConfigT().sendMailTimes;
				boolean isRedPoint = roleRt.getFactionBattleController().isRedPoint();
				boolean technologyRedPoint = false;
				if (isBossOrElder(roleRt.getRoleId())) {
					try {
						FactionTechnologyView view = technologyList();
						for (FactionTechnology t : view.technologys) {
							if (t.isCanStudy) {
								technologyRedPoint = true;
								break;
							}
						}
					} catch (NoteException e) {

					}
				}
				FactionView view = new FactionView(myFaction.getSubId(), myFaction.getIcon(), myFaction.getName(),
						myFaction.getLevel(), myFaction.getExp(), levelUpExp, myFaction.getQQ(), myFaction
								.getAnnounce(), myFaction.getJoinType(), myFaction.getJoinLevel(), levelT.people, self
								.getContribution(), self.getHonor(), getRenameYuanbao(), maxMailTimes
								- myFaction.getSendMailTimes(), list.toArray(new FactionMemberView[0]), myFaction
								.getJoinVip(), myFaction.getManifesto(), isRedPoint, technologyRedPoint, myFaction
								.getDeleteDay());
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
	}

	/**
	 * 获取改名元宝
	 * 
	 * @return
	 */
	private int getRenameYuanbao() {
		// 合服后免费改名
		return combineServerNamePattern.matcher(getMyFaction().getName()).matches() ? 0 : 2000;
	}

	@Override
	public void approveReq(final String applyId, final AMD_Faction_approveJoin __cb) {
		final IFaction myFaction = this.getMyFaction();
		// 检查自己是否有权限
		if (!myFaction.hasApproveNewMemberDuty(this.roleRt.getRoleId())) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.12"))); //$NON-NLS-1$
			return;
		}
		// 对方是否已加入其它公会
		final FactionReq req = XsgFactionManager.getInstance().findCandidateById(myFaction.getId(), applyId);
		if (req == null) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.13"))); //$NON-NLS-1$
			return;
		}
		// 公会人数是否已满
		if (this.isFull(myFaction)) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.14"))); //$NON-NLS-1$
			return;
		}

		XsgRoleManager.getInstance().loadRoleByIdAsync(req.getRoleId(), new Runnable() {
			@Override
			public void run() {
				IRole candidate = XsgRoleManager.getInstance().findRoleById(req.getRoleId());
				XsgFactionManager.getInstance().removeFactionReq(applyId);
				myFaction.approveReq(candidate);
				myFaction.saveAsyn();
				if (candidate.isOnline()) {
					String tips = TextUtil.format(Messages.getString("FactionControler.15"), //$NON-NLS-1$
							myFaction.getName());
					candidate.getNotifyControler().showTips(tips);
				}
				approveJoinEvent.onApproveJoin(req.getFactionId(), req.getRoleId());
				__cb.ice_response();
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("FactionControler.16"))); //$NON-NLS-1$
				return;
			}
		});
	}

	@Override
	public IFaction getMyFaction() {
		return XsgFactionManager.getInstance().findFaction(this.roleDb.getFactionId());
	}

	@Override
	public void denyReq(String applyId) {
		IFaction myFaction = this.getMyFaction();
		// 检查自己是否有权限
		if (!myFaction.hasApproveNewMemberDuty(this.roleRt.getRoleId())) {
			throw new IllegalStateException(Messages.getString("FactionControler.17"));
		}
		// 移除申请记录
		final FactionReq req = XsgFactionManager.getInstance().findCandidateById(myFaction.getId(), applyId);
		if (req != null) {
			XsgFactionManager.getInstance().removeFactionReq(req.getId());
			IRole iRole = XsgRoleManager.getInstance().findRoleById(req.getRoleId());
			if (iRole != null) {
				String tips = TextUtil.format(Messages.getString("FactionControler.18"), //$NON-NLS-1$
						myFaction.getName());
				iRole.getNotifyControler().showTips(tips);
			}
			denyJoinEvent.onDenyJoin(req.getFactionId(), req.getRoleId());
		}
	}

	@Override
	public void divorceFaction() throws NoteException {
		if (!this.isInFaction()) {
			return;
		}
		IFaction faction = getMyFaction();
		if (faction == null) {
			throw new NoteException(Messages.getString("FactionControler.19"));
		}
		FactionMember member = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (isBoss(member)) {
			throw new NoteException(Messages.getString("FactionControler.20"));
		}

		// 检测当前公会是否以参与公会战
		roleRt.getFactionBattleController().checkFactionBattle2LeaveFaction();

		faction.removeMember(member, roleRt);
		this.setNoFaction();
		faction.addHistory(this.roleRt, Messages.getString("FactionControler.21"), this.roleRt);
		this.roleDb.setQuitFactionDate(new Date());
		quitFactionEvent.onQuitFaction(faction.getId(), member.getRoleId());
		// 持久化保存
		faction.saveAsyn();
		this.roleRt.saveAsyn();
	}

	/**
	 * 成员是否为帮主
	 * 
	 * @param member
	 * @return
	 */
	private boolean isBoss(FactionMember member) {
		return member.getDutyId() == Const.Faction.DUTY_BOSS;
	}

	@Override
	public void deleteMember(String memberId) throws NoteException {
		if (!this.isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.22"));
		}

		final IFaction faction = getMyFaction();
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getId().equals(memberId)) {// 不能T自己
			throw new IllegalStateException();
		}

		// 检测当前公会是否以参与公会战
		roleRt.getFactionBattleController().checkFactionBattle2LeaveFaction();

		final FactionMember target = faction.getMemberByRoleId(memberId);
		if (target == null) {
			throw new NoteException(Messages.getString("FactionControler.23"));
		}
		int myPower = self.getDutyId();
		int targetPower = target.getDutyId();
		if (myPower > targetPower && faction.hasApproveNewMemberDuty(self.getRoleId())) {// 自己有T人权限且对方职位比自己低
			XsgRoleManager.getInstance().loadRoleByIdAsync(target.getRoleId(), new Runnable() {
				@Override
				public void run() {
					IRole temp = XsgRoleManager.getInstance().findRoleById(target.getRoleId());
					temp.getFactionControler().setNoFaction();
					temp.setQuitFactionTime(new Date());
					temp.saveAsyn();
					faction.removeMember(target, temp);
					faction.saveAsyn();
					faction.addHistory(temp, Messages.getString("FactionControler.24"), roleRt); //$NON-NLS-1$
					deleteFactionMenberEvent.onDeleteFactionMenber(target.getFaction().getId(), target.getRoleId());
				}
			}, new Runnable() {
				@Override
				public void run() {// 失败什么都不做
				}
			});
		} else {
			throw new NoteException(Messages.getString("FactionControler.25"));
		}

	}

	@Override
	public void setNoFaction() {
		this.roleDb.setFactionId("");
		try {
			this.roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.HasFaction, 0);
		} catch (Exception e) {
		}
	}

	@Override
	public void getJoinRequestList(final AMD_Faction_getJoinRequestList __cb) {
		if (getMyFaction().hasApproveNewMemberDuty(this.roleRt.getRoleId())) {
			final List<FactionReq> factionReqs = XsgFactionManager.getInstance().findFactionReq(
					this.roleDb.getFactionId());
			if (factionReqs.isEmpty()) {
				__cb.ice_response("{}"); //$NON-NLS-1$
				return;
			}
			FactionConfigT config = XsgFactionManager.getInstance().getFactionConfigT();
			// 最多取指定的条数
			final int length = Math.min(factionReqs.size(), config.applyNum);
			List<String> roleIds = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				roleIds.add(factionReqs.get(i).getRoleId());
			}
			XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {
				@Override
				public void run() {
					List<FactionReqView> list = new ArrayList<FactionReqView>();
					for (int i = 0; i < length; i++) {
						FactionReq r = factionReqs.get(i);
						IRole iRole = XsgRoleManager.getInstance().findRoleById(r.getRoleId());
						if (iRole == null) {
							continue;
						}
						list.add(new FactionReqView(r.getId(), r.getRoleId(), iRole.getName(), iRole.getLevel(), iRole
								.getVipLevel(), iRole.getHeadImage()));
					}
					__cb.ice_response(LuaSerializer.serialize(list));
				}
			});
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.28"))); //$NON-NLS-1$
		}
	}

	@Override
	public void cancelApplication(String factionId) {

	}

	@Override
	public void updateNotice(String newNotice) throws NoteException {
		FactionMember factionMember = this.getMyFaction().getMemberByRoleId(this.roleRt.getRoleId());
		if (isBoss(factionMember)) {
			if (SensitiveWordManager.getInstance().hasSensitiveWord(newNotice)) {
				throw new NoteException(Messages.getString("FactionControler.29")); //$NON-NLS-1$
			}
			if (newNotice == null || newNotice.length() > 75) {
				throw new NoteException(Messages.getString("FactionControler.30")); //$NON-NLS-1$
			}
			this.getMyFaction().setAnnouncement(newNotice);
		} else {
			throw new NoteException(Messages.getString("FactionControler.31")); //$NON-NLS-1$
		}
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		IFaction faction = getMyFaction();
		if (faction == null) {
			return null;
		}
		try {
			// 为了自动检测研究升级
			technologyList();
		} catch (NoteException e) {
			LogManager.error(e);
		}
		if (faction.hasApproveNewMemberDuty(this.roleRt.getRoleId())) {
			List<FactionReq> factionReqs = XsgFactionManager.getInstance().findFactionReq(this.roleDb.getFactionId());
			if (factionReqs.size() > 0) {
				return new MajorUIRedPointNote(MajorMenu.FactionReqMenu, false);
			}
		}
		return null;
	}

	@Override
	public void setCommon(final String targetId) throws NoteException {
		if (!this.isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.32")); //$NON-NLS-1$
		}
		final IFaction faction = getMyFaction();
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getId().equals(targetId)) {
			throw new NoteException(Messages.getString("FactionControler.33")); //$NON-NLS-1$
		}
		final FactionMember target = faction.getMemberByRoleId(targetId);
		if (target == null) {
			throw new NoteException(Messages.getString("FactionControler.34")); //$NON-NLS-1$
		}
		if (target.getDutyId() == Const.Faction.DUTY_JUNIOR) {
			throw new NoteException(Messages.getString("FactionControler.35")); //$NON-NLS-1$
		}
		int myPower = self.getDutyId();
		int targetPower = target.getDutyId();
		if (myPower > targetPower && faction.hasApproveNewMemberDuty(self.getRoleId())) {// 有权限且对方职位比自己低
			target.setDutyId(Const.Faction.DUTY_JUNIOR);
			XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {

				@Override
				public void run() {
					IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
					if (targetRole != null) {
						faction.addHistory(targetRole, Messages.getString("FactionControler.36"), roleRt); //$NON-NLS-1$
					}
				}
			}, new Runnable() {

				@Override
				public void run() {

				}
			});
		} else {
			throw new NoteException(Messages.getString("FactionControler.37")); //$NON-NLS-1$
		}
	}

	@Override
	public void upElder(final String targetId) throws NoteException {
		if (!this.isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.38")); //$NON-NLS-1$
		}
		final IFaction faction = getMyFaction();
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getId().equals(targetId)) {
			throw new NoteException(Messages.getString("FactionControler.39")); //$NON-NLS-1$
		}
		final FactionMember target = faction.getMemberByRoleId(targetId);
		if (target == null) {
			throw new NoteException(Messages.getString("FactionControler.40")); //$NON-NLS-1$
		}
		if (target.getDutyId() == Const.Faction.DUTY_ELDER) {
			throw new NoteException(Messages.getString("FactionControler.41")); //$NON-NLS-1$
		}
		FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(faction.getLevel());
		if (this.countElder() >= levelT.elder) {
			throw new NoteException(Messages.getString("FactionControler.42")); //$NON-NLS-1$
		}
		int myPower = self.getDutyId();
		int targetPower = target.getDutyId();
		if (myPower > targetPower && faction.hasApproveNewMemberDuty(self.getRoleId())) {// 有权限且对方职位比自己低
			target.setDutyId(Const.Faction.DUTY_ELDER);
			XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {

				@Override
				public void run() {
					IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
					if (targetRole != null) {
						faction.addHistory(targetRole, Messages.getString("FactionControler.43"), roleRt); //$NON-NLS-1$
					}
				}
			}, new Runnable() {

				@Override
				public void run() {

				}
			});
		} else {
			throw new NoteException(Messages.getString("FactionControler.44")); //$NON-NLS-1$
		}
	}

	@Override
	public void transferFaction(final String targetId) throws NoteException {
		if (!this.isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.45")); //$NON-NLS-1$
		}
		final IFaction faction = getMyFaction();
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getId().equals(targetId)) {
			throw new NoteException(Messages.getString("FactionControler.46")); //$NON-NLS-1$
		}
		final FactionMember target = faction.getMemberByRoleId(targetId);
		if (target == null) {
			throw new NoteException(Messages.getString("FactionControler.47")); //$NON-NLS-1$
		}
		if (isBoss(self)) {
			target.setDutyId(Const.Faction.DUTY_BOSS);
			self.setDutyId(Const.Faction.DUTY_JUNIOR);
			faction.setBossId(targetId);
			XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {

				@Override
				public void run() {
					IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
					if (targetRole != null) {
						faction.addHistory(targetRole, Messages.getString("FactionControler.48"), roleRt); //$NON-NLS-1$
					}
				}
			}, new Runnable() {

				@Override
				public void run() {

				}
			});
		} else {
			throw new NoteException(Messages.getString("FactionControler.49")); //$NON-NLS-1$
		}
	}

	/**
	 * 统计长老数量
	 * 
	 * @return
	 */
	private int countElder() {
		IFaction faction = getMyFaction();
		int count = 0;
		for (FactionMember m : faction.getAllMember()) {
			if (m.getDutyId() == Const.Faction.DUTY_ELDER) {
				count++;
			}
		}
		return count;
	}

	@Override
	public void factionConfig(String icon, String qq, String notice, int joinType, int joinLevel, int joinVip,
			String manifesto, int deleteDay) throws NoteException {
		IFaction faction = getMyFaction();
		if (faction.hasApproveNewMemberDuty(this.roleRt.getRoleId())) {
			if (SensitiveWordManager.getInstance().hasSensitiveWord(notice)) {
				throw new NoteException(Messages.getString("FactionControler.55"));
			}
			if (SensitiveWordManager.getInstance().hasSensitiveWord(manifesto)) {
				throw new NoteException(Messages.getString("FactionControler.55"));
			}
			if (manifesto.length() > 8) {
				manifesto = manifesto.substring(0, 8);
			}
			faction.saveConfig(icon, qq, notice, joinType, joinLevel, joinVip, manifesto, deleteDay);
			roleRt.getRankListControler().factionChannge(faction.getId());
		} else {
			throw new NoteException(Messages.getString("FactionControler.50"));
		}
	}

	@Override
	public FactionHistoryView[] getFactionHistorys() {
		if (!this.isInFaction()) {
			throw new IllegalStateException(Messages.getString("FactionControler.51")); //$NON-NLS-1$
		}
		IFaction faction = getMyFaction();
		List<FactionHistory> histories = faction.getAllHistory();
		FactionConfigT config = XsgFactionManager.getInstance().getFactionConfigT();
		int length = Math.min(histories.size(), config.historyNum);
		List<FactionHistoryView> factionHistoryViews = new ArrayList<FactionHistoryView>();
		SimpleDateFormat sdf = new SimpleDateFormat(Messages.getString("FactionControler.52")); //$NON-NLS-1$
		for (int i = 0; i < length; i++) {
			FactionHistory fh = histories.get(i);
			factionHistoryViews.add(new FactionHistoryView(sdf.format(fh.getCreateTime()), fh.getRoleName(), fh
					.getRemark(), fh.getVipLevel()));
		}
		return factionHistoryViews.toArray(new FactionHistoryView[0]);
	}

	@Override
	public String getFactionId() {
		return this.roleDb.getFactionId();
	}

	@Override
	public void createFaction(String name, String icon, final AMD_Faction_createFaction __cb) {
		if (this.roleRt.getLevel() < XsgFactionManager.getInstance().getCreateLevel()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.53"))); //$NON-NLS-1$
			return;
		}
		if (this.isInFaction()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.54"))); //$NON-NLS-1$
			return;
		}
		if (SensitiveWordManager.getInstance().hasSensitiveWord(name)) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.55"))); //$NON-NLS-1$
			return;
		}
		if (!factionNamePattern.matcher(name).matches()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.56"))); //$NON-NLS-1$
			return;
		}
		if (this.roleRt.getTotalYuanbao() < XsgFactionManager.getInstance().getCreateYuanbao()) {
			__cb.ice_exception(new NotEnoughYuanBaoException());
			return;
		}
		if (XsgFactionManager.getInstance().findFactionByName(name) != null) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.57"))); //$NON-NLS-1$
			return;
		}
		if (this.roleDb.getQuitFactionDate() != null) {
			long time = DateUtil.compareTime(new Date(), this.roleDb.getQuitFactionDate());
			FactionConfigT config = XsgFactionManager.getInstance().getFactionConfigT();
			if (time < config.quitCreateMinute * 60 * 1000) {
				String tips = TextUtil.format(Messages.getString("FactionControler.58"), //$NON-NLS-1$
						config.quitCreateMinute / 60);
				__cb.ice_exception(new NoteException(tips));
				return;
			}
		}
		final Faction newFaction = XsgFactionManager.getInstance().createFaction(roleRt, name, icon);
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				try {
					FactionDAO dao = FactionDAO.getFromApplicationContext(ServerLancher.getAc());
					dao.save(newFaction);
				} catch (DataIntegrityViolationException e) {
					__cb.ice_exception(new NoteException(Messages.getString("FactionControler.59"), e)); //$NON-NLS-1$
					return;
				}

				// 正常返回
				LogicThread.execute(new Runnable() {

					@Override
					public void run() {
						// 扣除损耗
						try {
							roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, XsgFactionManager.getInstance()
									.getCreateYuanbao()));
						} catch (NotEnoughMoneyException e) {
							__cb.ice_exception(e);
							return;
						} catch (NotEnoughYuanBaoException e) {
							__cb.ice_exception(e);
							return;
						}
						XsgFactionManager.getInstance().loadFaction(newFaction);
						// 设置人物的帮派归属
						setFaction(newFaction.getId());
						createFactionEvent.onCreateFaction(newFaction.getId());
						__cb.ice_response();
					}
				});

			}
		});
	}

	/**
	 * 验证公会人数是否已满
	 * 
	 * @return
	 */
	private boolean isFull(IFaction faction) {
		FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(faction.getLevel());
		return faction.getAllMember().size() >= levelT.people;
	}

	@Override
	public int getFactionLevel() {
		IFaction faction = getMyFaction();
		if (faction == null) {
			return 0;
		}
		return faction.getLevel();
	}

	@Override
	public void donation(int num) throws NoteException {
		IItemControler itemControler = this.roleRt.getItemControler();
		if (num > itemControler.getItemCountInPackage("g_exp")) {
			throw new NoteException(Messages.getString("FactionControler.61"));
		}
		if (num <= 0) {
			throw new NoteException(Messages.getString("FactionControler.62"));
		}
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.63"));
		}
		IFaction faction = getMyFaction();
		// if (faction.getLevel() >= XsgFactionManager.getInstance()
		// .getFactionMaxLevel()) {
		// throw new
		// NoteException(Messages.getString("FactionControler.maxLevel"));
		// }
		String content = TextUtil.format(Messages.getString("FactionControler.64"), num);
		faction.addHistory(this.roleRt, content, this.roleRt);
		int oldLevel = faction.getLevel();
		itemControler.changeItemByTemplateCode("g_exp", -num);
		faction.addExp(num, this.roleRt.getRoleId());
		if (faction.getLevel() > oldLevel) {
			// 升级事件
			factionLevelUpEvent.onFactionLevelUp(faction.getId(), faction.getLevel());
		}
		factionDonationEvent.onFactionDonation(this.roleRt.getRoleId(), num);
	}

	@Override
	public IntIntPair factionCopyList() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.66"));
		}
		resetOpenCopyNum();
		IntIntPair i = new IntIntPair();
		FactionCopy copy = getMyFaction().getFactionCopy();
		if (copy == null) {
			i.first = 0;
		} else {
			i.first = copy.getCopyId();
		}
		i.second = XsgFactionManager.getInstance().copyConfT.openCopyNum - getMyFaction().getOpenCopyNum();
		return i;
	}

	@Override
	public int openFactionCopy(int copyId) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.67")); //$NON-NLS-1$
		}
		if (!hasOpenCopyAuth(this.roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.68")); //$NON-NLS-1$
		}
		IFaction myFaction = getMyFaction();
		FactionCopy copy = myFaction.getFactionCopy();
		if (copy != null) {
			throw new NoteException(Messages.getString("FactionControler.69")); //$NON-NLS-1$
		}
		if (myFaction.getOpenCopyNum() >= XsgFactionManager.getInstance().copyConfT.openCopyNum) {
			throw new NoteException(Messages.getString("FactionControler.70")); //$NON-NLS-1$
		}
		myFaction.setLastOpenCopyTime(new Date());
		myFaction.setOpenCopyNum(myFaction.getOpenCopyNum() + 1);

		// 生成初始怪物
		List<MonsterView> mv = new ArrayList<MonsterView>();
		FactionCopyStageT stageT = XsgFactionManager.getInstance().getFactionCopyStageT(copyId, 1);
		for (Entry<Integer, Integer> entry : stageT.monsterIndexMap.entrySet()) {
			MonsterT mt = XsgCopyManager.getInstance().findMonsterT(entry.getValue());
			mv.add(new MonsterView(String.valueOf(entry.getKey()), mt.hp, 0));
		}

		copy = new FactionCopy(GlobalDataManager.getInstance().generatePrimaryKey(), null, copyId, null, 1,
				TextUtil.GSON.toJson(mv.toArray(new MonsterView[0])));

		// 随机加成类型
		copy.setAdditionType(randomType(stageT).value());
		myFaction.setFactionCopy(copy);
		Set<FactionMember> allMembers = myFaction.getAllMember();
		// 邮件模版
		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.FactionCopyOpen.value());
		FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copyId);
		String mailContent = mailRewardT.body.replace("$b", copyT.copyName); //$NON-NLS-1$
		// 邮件通知
		for (FactionMember f : allMembers) {
			f.setSumCopyHarm(0);// 清空累计伤害
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, //$NON-NLS-1$
							f.getRoleId(), mailRewardT.title, mailContent, "", //$NON-NLS-1$
							Calendar.getInstance().getTime()));
		}
		// 添加公会动态
		myFaction.addHistory(roleRt, TextUtil.format(Messages.getString("FactionControler.copyOpen"), copyT.copyName),
				roleRt);
		return XsgFactionManager.getInstance().copyConfT.openCopyNum - myFaction.getOpenCopyNum();
	}

	/**
	 * 重置副本开启次数
	 */
	private void resetOpenCopyNum() {
		IFaction faction = getMyFaction();
		if (faction.getLastOpenCopyTime() == null) {
			return;
		}
		String patten = "HH:mm:ss"; //$NON-NLS-1$
		if (DateUtil.isPass(XsgFactionManager.getInstance().copyConfT.resetTime, patten, faction.getLastOpenCopyTime())) {
			faction.setOpenCopyNum(0);
		}
	}

	/**
	 * 判断是否有开启副本权限
	 * 
	 * @param roleId
	 * @return
	 */
	private boolean hasOpenCopyAuth(String roleId) {
		IFaction faction = getMyFaction();
		FactionCopyConfT confT = XsgFactionManager.getInstance().copyConfT;
		if (faction.getLevel() < confT.factionLevel) {
			return false;
		}
		FactionMember fm = getMyFaction().getMemberByRoleId(roleId);
		// 判断是否是帮主和长老
		if (fm.getDutyId() < confT.openAuth) {
			return false;
		}
		return true;
	}

	@Override
	public FactionCopyInfoView factionCopyInfo() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.76")); //$NON-NLS-1$
		}
		FactionCopy copy = getMyFaction().getFactionCopy();
		if (copy == null) {
			throw new NoteException(Messages.getString("FactionControler.77")); //$NON-NLS-1$
		}
		if (copy.getRoleId() != null && isTimeout(copy)) {
			copy.setRoleId(null);
		}
		FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copy.getCopyId());
		// 副本进度%
		int progress = (int) ((double) copy.getHarmBlood() * 100 / getCopyAllBlood(copyT.id));
		progress = Math.min(progress, 99);
		resetChallengeTimes();
		int vipAddition = getVipAddition(copy.getCopyId());
		// 已挑战次数和可挑战次数
		IntIntPair ip = new IntIntPair(roleDb.getRoleFaction().getCopyChallengeNum(),
				XsgFactionManager.getInstance().copyConfT.challengeNum + vipAddition);
		IRole iRole = XsgRoleManager.getInstance().findRoleById(copy.getRoleId());
		String name = null;
		String icon = null;
		int vipLevel = 0;
		if (iRole != null) {
			name = iRole.getName();
			icon = iRole.getHeadImage();
			vipLevel = iRole.getVipLevel();
		}
		// 剩余时间
		long passTime = DateUtil.compareTime(new Date(), copy.getOpenDate());
		int minute = copyT.timeLimit - (int) (passTime / 1000 / 60);
		if (minute < 0) {
			minute = 0;
		}
		FactionCopyStageT stageT = XsgFactionManager.getInstance().getFactionCopyStageT(copy.getCopyId(),
				copy.getStageNum());
		FactionCopyInfoView view = new FactionCopyInfoView(copy.getStageNum(), progress, ip, name, icon, vipLevel,
				minute, AdditionType.valueOf(copy.getAdditionType()), stageT.addValue);
		return view;
	}

	/**
	 * 重置挑战次数
	 * 
	 * @param fm
	 */
	private void resetChallengeTimes() {
		String patten = "HH:mm:ss"; //$NON-NLS-1$
		if (roleDb.getRoleFaction().getRefreshChallengeDate() == null
				|| DateUtil.isPass(XsgFactionManager.getInstance().copyConfT.resetTime, patten, roleDb.getRoleFaction()
						.getRefreshChallengeDate())) {
			roleDb.getRoleFaction().setRefreshChallengeDate(new Date());
			roleDb.getRoleFaction().setCopyChallengeNum(0);
		}
	}

	@Override
	public FactionCopyResultView beginChallenge() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.79")); //$NON-NLS-1$
		}
		FactionCopy copy = getMyFaction().getFactionCopy();
		if (copy == null) {
			throw new NoteException(Messages.getString("FactionControler.80")); //$NON-NLS-1$
		}
		FactionCopyConfT configT = XsgFactionManager.getInstance().copyConfT;
		if (!DateUtil.checkTimeRange(new Date(), configT.startTime, configT.endTime)) {
			throw new NoteException(Messages.getString("FactionControler.81")); //$NON-NLS-1$
		}

		int vipAddition = getVipAddition(copy.getCopyId());
		if (roleDb.getRoleFaction().getCopyChallengeNum() >= configT.challengeNum + vipAddition) {
			throw new NoteException(Messages.getString("FactionControler.82")); //$NON-NLS-1$
		}
		if (copy.getRoleId() != null && !isTimeout(copy)) {// 有人在挑战
			throw new NoteException(Messages.getString("FactionControler.83")); //$NON-NLS-1$
		}
		// 是否异常中断
		if (copyBeginDate != null) {
			int pastSecond = (int) ((System.currentTimeMillis() - copyBeginDate.getTime()) / 1000);
			int cd = configT.quitMinute * 60 - pastSecond;
			if (cd > 0) {
				throw new NoteException(TextUtil.format(Messages.getString("FactionControler.copyChallengeError"),
						cd / 60, cd % 60));
			}
		}
		copyBeginDate = new Date();

		copy.setRoleId(this.roleRt.getRoleId());
		copy.setChallengeTime(System.currentTimeMillis());
		// 处理场景挑战随机掉落TC
		FactionCopyStageT stageT = XsgFactionManager.getInstance().getFactionCopyStageT(copy.getCopyId(),
				copy.getStageNum());
		TcResult tcResult = XsgRewardManager.getInstance().doTc(this.roleRt, stageT.randomTc);
		randomItems = XsgRewardManager.getInstance().generateItemView(tcResult);
		// 小怪掉落
		tcResult = XsgRewardManager.getInstance().doTc(this.roleRt, stageT.killDropTc);
		killItems = XsgRewardManager.getInstance().generateItemView(tcResult);
		// boss血量掉落
		tcResult = XsgRewardManager.getInstance().doTc(this.roleRt, stageT.bossDropTc);
		bloodItems = XsgRewardManager.getInstance().generateItemView(tcResult);
		MonsterView[] mvs = TextUtil.GSON.fromJson(copy.getMonsterJson(), MonsterView[].class);
		FactionCopyResultView view = new FactionCopyResultView(copy.getCopyId(), copy.getStageNum(), mvs, randomItems,
				killItems, bloodItems, AdditionType.valueOf(copy.getAdditionType()), stageT.addValue);
		// 挑战状态变更通知
		Set<FactionMember> allMembers = getMyFaction().getAllMember();
		for (FactionMember f : allMembers) {
			IRole iRole = XsgRoleManager.getInstance().findRoleById(f.getRoleId());
			if (iRole != null) {
				iRole.getNotifyControler().factionCopyState(this.roleRt.getName(), this.roleRt.getHeadImage(),
						this.roleRt.getVipLevel());
			}
		}
		return view;
	}

	/**
	 * 随机获取场景战斗的加成类型
	 * 
	 * @param stageT
	 * @return
	 */
	private AdditionType randomType(FactionCopyStageT stageT) {
		int total = stageT.noneAdd + stageT.weiGuoAdd + stageT.shuGuoAdd + stageT.wuGuoAdd + stageT.otherAdd;
		if (NumberUtil.isHit(stageT.noneAdd, total)) {
			return AdditionType.NONE;
		} else if (NumberUtil.isHit(stageT.weiGuoAdd, total)) {
			return AdditionType.WEIGUO;
		} else if (NumberUtil.isHit(stageT.shuGuoAdd, total)) {
			return AdditionType.SHUGUO;
		} else if (NumberUtil.isHit(stageT.wuGuoAdd, total)) {
			return AdditionType.WUGUO;
		} else {
			return AdditionType.OTHER;
		}
	}

	/**
	 * 获取VIP额外获得挑战次数
	 * 
	 * @param copyId
	 * @return
	 */
	private int getVipAddition(int copyId) {
		FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copyId);
		// VIP额外获得挑战次数
		int vipAddition = 0;
		if (this.roleRt.getVipLevel() >= copyT.vipAdditionLevel) {
			vipAddition = 1;
		}
		return vipAddition;
	}

	/**
	 * 判断战斗是否超时
	 * 
	 * @param copy
	 * @return
	 */
	private boolean isTimeout(FactionCopy copy) {
		if (System.currentTimeMillis() - copy.getChallengeTime() > XsgFactionManager.getInstance().copyConfT.challengeTime * 1000 + 30000) {
			return true;
		}
		return false;
	}

	@Override
	public void endChallenge(MonsterView[] monsterViews, boolean isKill, boolean isHurtBlood, int dropBlood)
			throws NoteException {
		if (!isInFaction()) {
			copyBeginDate = null;
			throw new NoteException(Messages.getString("FactionControler.84"));
		}
		IFaction myFaction = getMyFaction();
		FactionCopy copy = myFaction.getFactionCopy();
		if (copy == null) {
			copyBeginDate = null;
			throw new NoteException(Messages.getString("FactionControler.85"));
		}
		int copyId = copy.getCopyId();
		// 结束的人和挑战的人不一致
		if (copy.getRoleId() == null || !copy.getRoleId().equals(this.roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.86"));
		}
		copy.setRoleId(null);
		if (isTimeout(copy)) {
			throw new NoteException(Messages.getString("FactionControler.87"));
		}

		FactionCopyConfT configT = XsgFactionManager.getInstance().copyConfT;
		int vipAddition = getVipAddition(copy.getCopyId());
		if (roleDb.getRoleFaction().getCopyChallengeNum() >= configT.challengeNum + vipAddition) {
			throw new NoteException(Messages.getString("FactionControler.82"));
		}

		copyBeginDate = null;
		FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copy.getCopyId());
		int maxHarm = WorldBossManager.getInstance().getMaxHarm(1, roleRt.getLevel());
		if (dropBlood > maxHarm && copyT.diff == 2) {// 地狱副本
			// 扣挑战次数
			roleDb.getRoleFaction().setCopyChallengeNum(roleDb.getRoleFaction().getCopyChallengeNum() + 1);
			LogManager.warn("WorldBossControler.challengeError " + roleRt.getRoleId() + " " + dropBlood);
			throw new NoteException(Messages.getString("WorldBossControler.challengeError"));
		}
		// 血量验证
		MonsterView[] ms = TextUtil.GSON.fromJson(copy.getMonsterJson(), MonsterView[].class);
		long serverBlood = 0;
		for (MonsterView m : ms) {
			serverBlood += m.blood;
		}
		// 减去剩余血量就是打掉血量
		for (MonsterView m : monsterViews) {
			serverBlood -= m.blood;
		}
		serverBlood = serverBlood * 80 / 100;// 取80%
		if (dropBlood < serverBlood) {
			// 扣挑战次数
			roleDb.getRoleFaction().setCopyChallengeNum(roleDb.getRoleFaction().getCopyChallengeNum() + 1);
			LogManager.warn("WorldBossControler.challengeError " + roleRt.getRoleId() + " " + dropBlood);
			throw new NoteException(Messages.getString("WorldBossControler.challengeError"));
		}

		// 扣挑战次数
		roleDb.getRoleFaction().setCopyChallengeNum(roleDb.getRoleFaction().getCopyChallengeNum() + 1);
		// 发放物品
		this.roleRt.getRewardControler().acceptReward(this.randomItems);
		if (isKill) {
			this.roleRt.getRewardControler().acceptReward(this.killItems);
		}
		if (isHurtBlood) {
			this.roleRt.getRewardControler().acceptReward(this.bloodItems);
		}
		// 根据掉血量计算获得金币
		int addGold = (int) Math.min(dropBlood * configT.scale, configT.maxGold);
		try {
			this.roleRt.winJinbi(addGold);
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}
		copy.setHarmBlood(copy.getHarmBlood() + dropBlood);
		FactionMember self = myFaction.getMemberByRoleId(roleRt.getRoleId());
		// 增加自己打出的伤害
		self.setSumCopyHarm(self.getSumCopyHarm() + dropBlood);
		Set<FactionMember> allMembers = myFaction.getAllMember();// 公会所有会员
		// 把所有怪物都打死了
		if (monsterViews == null || monsterViews.length == 0) {
			copy.setMonsterJson("[]");
			int stageCount = XsgFactionManager.getInstance().getFactionCopyStageCount(copy.getCopyId());
			if (stageCount <= copy.getStageNum()) {// 如果是关卡最后一个场景
				long passTime = DateUtil.compareTime(new Date(), copy.getOpenDate());
				if (passTime <= copyT.timeLimit * 60 * 1000) {// 在指定时间内通关了
					sendCopyItems(copyT, true);
				} else {
					sendCopyItems(copyT, false);
				}
				// 关闭通关的副本
				copy = null;
				myFaction.setFactionCopy(null);
				// 通关后重置挑战次数
				for (FactionMember f : allMembers) {
					f.setChallengeNum(0);
				}
				// 添加公会动态
				myFaction.addHistory(null,
						TextUtil.format(Messages.getString("FactionControler.copyPass"), copyT.copyName), roleRt);
			} else {
				copy.setStageNum(copy.getStageNum() + 1);
				FactionCopyStageT stageT = XsgFactionManager.getInstance().getFactionCopyStageT(copy.getCopyId(),
						copy.getStageNum());
				copy.setAdditionType(randomType(stageT).value());

				// 生成初始怪物
				List<MonsterView> mv = new ArrayList<MonsterView>();
				for (Entry<Integer, Integer> entry : stageT.monsterIndexMap.entrySet()) {
					MonsterT mt = XsgCopyManager.getInstance().findMonsterT(entry.getValue());
					mv.add(new MonsterView(String.valueOf(entry.getKey()), mt.hp, 0));
				}
				copy.setMonsterJson(TextUtil.GSON.toJson(mv.toArray(new MonsterView[0])));
			}
		} else {
			copy.setMonsterJson(TextUtil.GSON.toJson(monsterViews));
		}
		// 挑战状态变更通知
		for (FactionMember f : allMembers) {
			IRole iRole = XsgRoleManager.getInstance().findRoleById(f.getRoleId());
			if (iRole != null && iRole.isOnline()) {
				iRole.getNotifyControler().factionCopyState(null, null, 0);
			}
		}
		factionCopyEndChallengeEvent.onFactionCopyEndChallenge(this.roleRt.getRoleId(), getMyFaction().getId(), copyId,
				dropBlood);
	}

	@Override
	public void clearCopyRole() {
		IFaction myFaction = getMyFaction();
		if (myFaction == null) {
			return;
		}
		FactionCopy copy = myFaction.getFactionCopy();
		if (copy == null || copy.getRoleId() == null || !copy.getRoleId().equals(roleRt.getRoleId())) {
			return;
		}
		copy.setRoleId(null);
		// 挑战状态变更通知
		for (FactionMember f : myFaction.getAllMember()) {
			IRole iRole = XsgRoleManager.getInstance().findRoleById(f.getRoleId());
			if (iRole != null && iRole.isOnline()) {
				iRole.getNotifyControler().factionCopyState(null, null, 0);
			}
		}
	}

	/**
	 * 执行TC发放公会通关奖励
	 * 
	 * @param copyT
	 * @param isLimitTime
	 *            是否有限时奖励
	 */
	public void sendCopyItems(final FactionCopyT copyT, final boolean isLimitTime) {
		List<String> roleIds = new ArrayList<String>();
		final IFaction myFaction = getMyFaction();
		final Set<FactionMember> allMembers = myFaction.getAllMember();
		final List<FactionMember> memberRank = getMemberHarmRank();
		if (isLimitTime) {
			for (FactionMember fm : allMembers) {
				roleIds.add(fm.getRoleId());
			}
		} else {
			for (FactionMember fm : memberRank) {
				roleIds.add(fm.getRoleId());
			}
		}
		final FactionCopyConfT copyConfT = XsgFactionManager.getInstance().copyConfT;

		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				LogicThread.execute(new Runnable() {

					@Override
					public void run() {
						MailRewardT mailRewardT = null;
						ItemView[] itemViews = null;
						List<Property> pro = null;
						if (isLimitTime) {
							itemViews = XsgRewardManager.getInstance().doTcToItem(roleRt, copyT.timeLimitTc);
							pro = new ArrayList<Property>();
							for (int i = 0; i < itemViews.length; i++) {
								ItemView iv = itemViews[i];
								pro.add(new Property(iv.templateId, iv.num));
							}
							mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
									.get(MailTemplate.FactionCopyPassInTime.value());
							String mailContent = mailRewardT.body.replace("$b", copyT.copyName);
							// 邮件发放奖励
							for (FactionMember f : allMembers) {
								IRole sendRole = XsgRoleManager.getInstance().findRoleById(f.getRoleId());
								if (sendRole == null) {
									continue;
								}
								refreshCopyAward(sendRole);
								// 判断当天发奖次数是否达到上限
								RoleFaction sendFaction = sendRole.getRoleFaction();
								if ((TextUtil.isNotBlank(sendFaction.getAwardFactionId()) && !sendFaction
										.getAwardFactionId().equals(myFaction.getId()))
										|| sendFaction.getLimitTimeAwardNum() >= copyConfT.openCopyNum + 1) {
									continue;
								}
								sendFaction.setLimitTimeAwardNum(sendFaction.getLimitTimeAwardNum() + 1);
								// sendFaction.setAwardFactionId(myFaction.getId());

								Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "",
										mailRewardT.sendName, f.getRoleId(), mailRewardT.title, mailContent,
										XsgMailManager.getInstance().serializeMailAttach(pro.toArray(new Property[0])),
										Calendar.getInstance().getTime());
								XsgMailManager.getInstance().sendMail(mail);
							}
						}
						itemViews = XsgRewardManager.getInstance().doTcToItem(roleRt, copyT.tc);
						pro = new ArrayList<Property>();
						for (int i = 0; i < itemViews.length; i++) {
							ItemView iv = itemViews[i];
							pro.add(new Property(iv.templateId, iv.num));
						}
						mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
								.get(MailTemplate.FactionCopyPassAll.value());
						// 邮件发放奖励
						for (int i = 0; i < memberRank.size(); i++) {
							String mailContent = mailRewardT.body.replace("$b", copyT.copyName);
							mailContent = mailContent.replace("$g", String.valueOf(i + 1));
							FactionMember f = memberRank.get(i);

							IRole sendRole = XsgRoleManager.getInstance().findRoleById(f.getRoleId());
							if (sendRole == null) {
								continue;
							}
							refreshCopyAward(sendRole);
							// 判断当天发奖次数是否达到上限
							RoleFaction sendFaction = sendRole.getRoleFaction();
							if ((TextUtil.isNotBlank(sendFaction.getAwardFactionId()) && !sendFaction
									.getAwardFactionId().equals(myFaction.getId()))
									|| sendFaction.getCopyAwardNum() >= copyConfT.openCopyNum + 1) {
								continue;
							}
							sendFaction.setCopyAwardNum(sendFaction.getCopyAwardNum() + 1);
							// sendFaction.setAwardFactionId(myFaction.getId());

							Integer rankScale = XsgFactionManager.getInstance().getCopyRankScale(i + 1);
							if (rankScale == null) {
								rankScale = XsgFactionManager.getInstance().getCopyRankScale(-1);
							}
							long addGold = (long) rankScale * copyT.sumGold / 10000;
							pro.add(new Property(PropertyName.MONEY, (int) addGold));
							XsgMailManager.getInstance().sendMail(
									new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "",
											mailRewardT.sendName, f.getRoleId(), mailRewardT.title, mailContent,
											XsgMailManager.getInstance().serializeMailAttach(
													pro.toArray(new Property[0])), Calendar.getInstance().getTime()));
							// 移除掉最后的金币
							pro.remove(pro.size() - 1);
						}
					}
				});
			}
		});
	}

	/**
	 * 刷新公会数据
	 * 
	 * @param role
	 */
	private void refreshCopyAward(IRole role) {
		if (role.getRoleFaction().getReceiveAwardDate() == null
				|| !DateUtil.isSameDay(new Date(), role.getRoleFaction().getReceiveAwardDate())) {
			role.getRoleFaction().setReceiveAwardDate(new Date());
			role.getRoleFaction().setCopyAwardNum(0);
			role.getRoleFaction().setLimitTimeAwardNum(0);
			role.getRoleFaction().setAwardFactionId("");
		}
	}

	@Override
	public void closeFactionCopy() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (!hasOpenCopyAuth(this.roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.92"));
		}
		IFaction myFaction = getMyFaction();
		FactionCopy copy = myFaction.getFactionCopy();
		if (copy == null) {
			throw new NoteException(Messages.getString("FactionControler.93"));
		}
		if (copy.getRoleId() != null && !isTimeout(copy)) {
			throw new NoteException(Messages.getString("FactionControler.94"));
		}
		Set<FactionMember> allMembers = myFaction.getAllMember();
		// 重置挑战次数
		for (FactionMember f : allMembers) {
			f.setChallengeNum(0);
		}
		FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copy.getCopyId());
		// 添加公会动态
		myFaction.addHistory(roleRt, TextUtil.format(Messages.getString("FactionControler.copyClose"), copyT.copyName),
				roleRt);
		myFaction.setFactionCopy(null);
	}

	@Override
	public FactionShopView getFactionShops() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		FactionShop[] shopViews = XsgFactionManager.getInstance().getShopViews();
		// 检测是否可刷新商品
		if (roleDb.getRoleFaction().getShopRefreshDate() == null
				|| DateUtil.isPass(configT.shopRefreshDate, "HH:mm", roleDb.getRoleFaction().getShopRefreshDate())) {
			roleDb.getRoleFaction().setShopRefreshDate(new Date());
			roleDb.getRoleFaction().setBuyShopIds("");
		}
		String refreshStr = TextUtil.format(Messages.getString("FactionControler.96"), configT.shopRefreshDate);
		// 验证是否购买过
		List<String> shopIds = TextUtil.stringToList(roleDb.getRoleFaction().getBuyShopIds());
		for (FactionShop s : shopViews) {
			if (shopIds.contains(String.valueOf(s.id))) {
				s.isBuy = true;
			}
		}
		FactionShopView view = new FactionShopView(refreshStr, shopViews);
		return view;
	}

	@Override
	public void buyFactionShop(int id) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());

		FactionShopT factionShop = XsgFactionManager.getInstance().getFactionShopT(id);
		if (factionShop == null) {
			throw new NoteException(Messages.getString("FactionControler.97"));
		}
		// 购买过的物品ID
		List<String> shopIds = TextUtil.stringToList(roleDb.getRoleFaction().getBuyShopIds());
		if (shopIds.contains(String.valueOf(id))) {
			throw new NoteException(Messages.getString("FactionControler.notRepeatBuy"));
		}

		if (factionShop.coinType == CoinType.HONOR.ordinal()) {// 荣誉
			if (factionShop.freeValue == -1) {// 不免费走扣除流程
				if (self.getHonor() < factionShop.price) {
					throw new NoteException(Messages.getString("FactionControler.98"));
				}
				self.setHonor(self.getHonor() - factionShop.price);
			} else {
				// 没有达到领取条件
				if (self.getHonor() < factionShop.freeValue) {
					throw new NoteException(Messages.getString("FactionControler.98"));
				}
			}
		} else if (factionShop.coinType == CoinType.CONTRIBUTION.ordinal()) {// 贡献
			if (factionShop.freeValue == -1) {// 不免费走扣除流程
				// 没有达到领取条件
				if (self.getContribution() < factionShop.price) {
					throw new NoteException(Messages.getString("FactionControler.99"));
				}
				self.setContribution(self.getContribution() - factionShop.price);
			} else {
				// 没有达到领取条件
				if (self.getContribution() < factionShop.freeValue) {
					throw new NoteException(Messages.getString("FactionControler.99"));
				}
			}
		}
		this.roleRt.getRewardControler().acceptReward(factionShop.itemId, factionShop.num);
		shopIds.add(String.valueOf(id));
		roleDb.getRoleFaction().setBuyShopIds(TextUtil.join(shopIds, ","));
		// 事件
		buyFactionShopEvent.onBuyFactionShop(id);
	}

	@Override
	public GvgView getGvgInfo() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		IFaction faction = getMyFaction();
		if (faction.getLevel() < configT.openGvgLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("FactionControler.100"), configT.openGvgLevel));
		}
		GvgView gvgView = new GvgView(false, 0, false, false, configT.beginTime, 0, 0, 0, 0, 0, 0, 0,
				configT.reviveMoney, 0);
		// 公会战开始时间
		Date beginDate = DateUtil.joinTime(configT.beginTime);
		// 判断是否已报名
		FactionMember self = faction.getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getApplyDate() != null && DateUtil.isSameDay(new Date(), self.getApplyDate())
				&& new Date().before(beginDate)) {
			gvgView.isApply = true;
		}
		gvgView.isCanApply = isApplyTime();
		gvgView.isBegin = isGvgTime();
		// 已报名但是未开战需要显示倒计时
		if (gvgView.isApply && !gvgView.isBegin) {
			gvgView.beginSecond = (int) ((beginDate.getTime() - System.currentTimeMillis()) / 1000);
		}
		if (gvgView.isBegin) {
			// 隐式报名
			applyGvg();
			// 处理死亡倒计时
			if (self.getDeathDate() != null
					&& System.currentTimeMillis() < self.getDeathDate().getTime() + configT.deathWait * 1000) {
				int scond = (int) (configT.deathWait - (System.currentTimeMillis() - self.getDeathDate().getTime()) / 1000);
				gvgView.deathScond = scond;
			}
			// 处理自己的排名
			List<FactionMemberRank> rankList = XsgFactionManager.getInstance().getFactionMemberRankList();
			for (int i = 0; i < rankList.size(); i++) {
				if (rankList.get(i).getRoleId().equals(roleRt.getRoleId())) {
					gvgView.myRank = i + 1;
					gvgView.myHonor = rankList.get(i).getHonor();
					break;
				}
			}
			// 公会排名
			FactionRankView factionRankView = getFactionRank();
			gvgView.factionRank = factionRankView.myRank;
			gvgView.factionHonor = factionRankView.myFactionHonor;
			// 处理人数
			List<IFaction> allFaction = XsgFactionManager.getInstance().getFactionRank();
			for (IFaction f : allFaction) {
				for (FactionMember fm : f.getAllMember()) {
					// 报过名的人
					if (fm.getApplyDate() != null && DateUtil.isSameDay(new Date(), fm.getApplyDate())) {
						gvgView.applyNum++;
						// 没有死亡的人
						if (fm.getDeathDate() == null
								|| System.currentTimeMillis() > fm.getDeathDate().getTime() + configT.deathWait * 1000) {
							gvgView.currentNum++;
						}
					}
				}
			}
			// 处理结束倒计时
			long milliseconds = beginDate.getTime() + configT.gvgMinute * 60 * 1000 - new Date().getTime();
			gvgView.endSecond = (int) (milliseconds / 1000);
		}
		return gvgView;
	}

	/**
	 * 是否是报名时间
	 * 
	 * @return
	 */
	private boolean isApplyTime() {
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		Calendar now = Calendar.getInstance();
		int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
		List<String> openWeekDay = TextUtil.stringToList(configT.openWeekDay);
		if (openWeekDay.contains(String.valueOf(weekDay))) {
			Date beginDate = DateUtil.joinTime(configT.beginTime);
			// 到了报名时间
			if (System.currentTimeMillis() > beginDate.getTime() - configT.beforeMinute * 60 * 1000
					&& System.currentTimeMillis() <= beginDate.getTime() + configT.gvgMinute * 60 * 1000) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是公会战时间
	 * 
	 * @return
	 */
	private boolean isGvgTime() {
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		Calendar now = Calendar.getInstance();
		int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
		List<String> openWeekDay = TextUtil.stringToList(configT.openWeekDay);
		if (openWeekDay.contains(String.valueOf(weekDay))) {
			Date beginDate = DateUtil.joinTime(configT.beginTime);
			// 到了公会战时间
			if (System.currentTimeMillis() > beginDate.getTime()
					&& System.currentTimeMillis() <= beginDate.getTime() + configT.gvgMinute * 60 * 1000) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void applyGvg() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (!isApplyTime()) {
			throw new NoteException(Messages.getString("FactionControler.notApply"));
		}
		FactionMember self = getMyFaction().getMemberByRoleId(this.roleRt.getRoleId());
		if (self.getApplyDate() == null || !DateUtil.isSameDay(new Date(), self.getApplyDate())) {
			self.setApplyDate(new Date());
			XsgFactionManager.getInstance().addApplyMember(roleRt.getRoleId());
			factionApplyEvent.onApply();
		}
	}

	@Override
	public void selectRival(final AMD_Faction_selectRival __cb, int index) throws NoteException {
		index -= 1;// 客户端是从1开始的所以要减一
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (!isGvgTime()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.GvgNotOpen")));
			return;
		}
		final List<String> roleIds = XsgFactionManager.getInstance().getApplyMemberList(index);
		if (roleIds == null || roleIds.size() == 0) {
			__cb.ice_response("[]");
			return;
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<RivalView> views = new ArrayList<RivalView>();
				for (String id : roleIds) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(id);
					if (iRole != null && !iRole.getFactionControler().getFactionId().equals(roleDb.getFactionId())
							&& !XsgFactionManager.getInstance().isExistGvgLog(roleDb.getId(), iRole.getRoleId())) {
						views.add(new RivalView(iRole.getRoleId(), iRole.getName(), iRole.getHeadImage(), iRole
								.getFactionControler().getFactionName(), iRole.getLevel(), iRole.getVipLevel(), iRole
								.getCachePower()));
					}
				}
				__cb.ice_response(LuaSerializer.serialize(views.toArray(new RivalView[0])));
			}
		});
	}

	@Override
	public void getRivalFormation(final AMD_Faction_getRivalFormation __cb, final String roleId) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		// 查询对手的阵容
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				IRole rivalRole = XsgRoleManager.getInstance().findRoleById(roleId);
				// rivalLevel = rivalRole.getLevel();
				rivalId = roleId;
				String formationId = rivalRole.getFormationControler().getDefaultFormation().getId();
				PvpOpponentFormationView pvpView = rivalRole.getFormationControler().getPvpOpponentFormationView(
						formationId);

				GvgChallengeView view = new GvgChallengeView(roleId, rivalRole.getName(), rivalRole.getHeadImage(),
						rivalRole.getSex(), rivalRole.getVipLevel(), rivalRole.getLevel(), pvpView);
				__cb.ice_response(view);
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("LadderControler.9")));
			}
		});
	}

	@Override
	public String beginGvg() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (!isGvgTime()) {
			throw new NoteException(Messages.getString("FactionControler.GvgNotOpen"));
		}
		final FactionMember self = getMyFaction().getMemberByRoleId(this.roleRt.getRoleId());
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		if (self.getDeathDate() != null
				&& System.currentTimeMillis() < self.getDeathDate().getTime() + configT.deathWait * 1000) {
			throw new NoteException(Messages.getString("FactionControler.DeathWait"));
		}
		if (self.getGvgEndDate() != null
				&& System.currentTimeMillis() < self.getGvgEndDate().getTime() + configT.waitScond * 1000) {
			int scond = (int) (configT.waitScond - (System.currentTimeMillis() - self.getGvgEndDate().getTime()) / 1000);
			String tips = TextUtil.format(Messages.getString("FactionControler.Wait"), scond);
			throw new NoteException(tips);
		}
		// 设置挑战时间
		self.setGvgEndDate(new Date());
		self.setDeathDate(new Date());
		// 生成战报ID
		IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
		fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(XsgFightMovieManager.Type.GVG, roleRt,
				rivalRole);
		return fightMovieIdContext;
	}

	@Override
	public IntIntPair endGvg(boolean isWin, int heroNum) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
		if (rivalRole == null) {
			throw new NoteException(Messages.getString("LadderControler.9"));
		}
		IntIntPair intIntPair = new IntIntPair(0, 0);

		IFaction myFaction = getMyFaction();
		FactionMember self = myFaction.getMemberByRoleId(this.roleRt.getRoleId());
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		int addHonor = 0;
		int selfWinNum = XsgFactionManager.getInstance().getWinNum(roleRt.getRoleId());

		if (isWin) {
			selfWinNum++;
			// 累计胜利次数
			int sumWinNum = XsgFactionManager.getInstance().getSumWinNum(roleRt.getRoleId());
			sumWinNum++;
			XsgFactionManager.getInstance().setSumWinNum(roleRt.getRoleId(), sumWinNum);
			addHonor = configT.winHonor;
			// 处理自己的连胜
			if (selfWinNum > 1) {
				HonorAdditionT additionT = XsgFactionManager.getInstance().getHonorAdditionT(selfWinNum);
				addHonor = addHonor + additionT.addHonor;
			}
			XsgFactionManager.getInstance().setWinNum(roleRt.getRoleId(), selfWinNum);
			// 处理终结对方连胜
			int rivalWinNum = XsgFactionManager.getInstance().getWinNum(rivalId);
			if (rivalWinNum > 1) {
				HonorAdditionT additionT = XsgFactionManager.getInstance().getHonorAdditionT(rivalWinNum);
				addHonor = addHonor + additionT.killAddHonor;
				// 终结连胜公告
				sendWinOverNotice(roleRt, rivalRole, rivalWinNum);
			}
			// 清空对方连胜
			XsgFactionManager.getInstance().setWinNum(rivalId, 0);

			IFaction rivalFaction = rivalRole.getFactionControler().getMyFaction();
			if (rivalFaction != null) {
				FactionMember rivalMem = rivalFaction.getMemberByRoleId(rivalId);
				int rivalHonor = rivalMem.getHonor() - configT.deductHonor;
				rivalMem.setHonor(Math.max(0, rivalHonor));// 不能为负数

				int oldAdd = XsgFactionManager.getInstance().getGvgRankHonor(rivalFaction);
				XsgFactionManager.getInstance().addGvgHonor(rivalFaction.getId(), rivalId, -configT.deductHonor);
				int newAdd = XsgFactionManager.getInstance().getGvgRankHonor(rivalFaction);
				rivalFaction.setFactionHonor(rivalFaction.getFactionHonor() - oldAdd + newAdd);
				if (rivalFaction.getFactionHonor() < 0) {
					rivalFaction.setFactionHonor(0);
				}

				FactionMemberRank rank = XsgFactionManager.getInstance().getFactionMemberRank(rivalId);
				if (rank != null) {
					int rivalRankHonor = rank.getHonor() - configT.deductHonor;
					rank.setHonor(Math.max(0, rivalRankHonor));
					saveMemberRank(rank);
				}
			}

			self.setHonor(self.getHonor() + addHonor);
			// 永远取最新排行里面公会最大人数的总荣誉
			int oldAdd = XsgFactionManager.getInstance().getGvgRankHonor(myFaction);
			XsgFactionManager.getInstance().addGvgHonor(myFaction.getId(), roleRt.getRoleId(), addHonor);
			int newAdd = XsgFactionManager.getInstance().getGvgRankHonor(myFaction);
			myFaction.setFactionHonor(myFaction.getFactionHonor() - oldAdd + newAdd);

			FactionMemberRank rank = XsgFactionManager.getInstance().getFactionMemberRank(roleRt.getRoleId());
			if (rank == null) {
				rank = new FactionMemberRank(GlobalDataManager.getInstance().generatePrimaryKey(), roleRt.getRoleId(),
						new Date(), 0);
				XsgFactionManager.getInstance().putFactionMemberRank(rank);
			}
			rank.setHonor(rank.getHonor() + addHonor);
			saveMemberRank(rank);

			// 返回星级和荣誉
			intIntPair.second = addHonor;
			int star = XsgCopyManager.getInstance().calculateStar(
					roleRt.getFormationControler().getDefaultFormation().getHeroCountIncludeSupport(), (byte) heroNum);
			intIntPair.first = star;

			// 添加战斗记录
			GvgLog gvgLog = new GvgLog(this.roleRt.getRoleId(), rivalId);
			XsgFactionManager.getInstance().addGvgLog(gvgLog);
			// 公告
			sendGvgNotice(rivalRole, selfWinNum);
			// 处理热血公会战
			sendWarmGvgMail(sumWinNum);
			self.setDeathDate(null);
		} else {
			self.setGvgEndDate(null);
			self.setDeathDate(new Date());
			sendWinOverNotice(rivalRole, roleRt, selfWinNum);
			XsgFactionManager.getInstance().setWinNum(roleRt.getRoleId(), 0);
		}
		rivalId = null;
		XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(), fightMovieIdContext, isWin ? 1 : 0,
				(byte) heroNum);

		// 事件
		factionGvgEndEvent.onFactionGvgEnd(isWin, addHonor);
		return intIntPair;
	}

	/**
	 * 发送热血公会战奖励邮件
	 * 
	 * @param sumWinNum
	 */
	private void sendWarmGvgMail(int sumWinNum) {
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		if (DateUtil.isBetween(configT.warmStartDate, configT.warmEndDate)) {
			WarmGvgAwardT warmGvgAwardT = XsgFactionManager.getInstance().getWarmGvgAwardT();
			if (sumWinNum % warmGvgAwardT.winNum == 0) {
				MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
						.get(MailTemplate.WarmGvgActivity.value());

				String[] itemArr = warmGvgAwardT.itemId.split(",");
				Property[] pros = new Property[itemArr.length];
				for (int i = 0; i < pros.length; i++) {
					String[] idNum = itemArr[i].split(":");
					pros[i] = new Property(idNum[0], Integer.parseInt(idNum[1]));
				}

				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
								roleRt.getRoleId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
										.serializeMailAttach(pros), Calendar.getInstance().getTime()));
			}
		}
	}

	/**
	 * 公会战连胜滚动公告
	 * 
	 * @param rivalRole
	 * @param winNum
	 */
	private void sendGvgNotice(IRole rivalRole, int winNum) {
		if (winNum % 5 == 0 && winNum > 0) {
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.GvgSuccessionWin);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					String content = chatAdT.content.replaceAll("~guide_name_1~", getMyFaction().getName());
					content = content.replaceAll("~guide_player_1~",
							TextUtil.format("{0}|{1}|{2}", roleRt.getRoleId(), roleRt.getName(), roleRt.getVipLevel()));
					content = content.replace("~kill_time~", String.valueOf(winNum));
					XsgChatManager.getInstance().sendAnnouncement(content);
				}
			}
		}
	}

	/**
	 * 公会战终结别人连胜滚动公告
	 * 
	 * @param rivalRole
	 * @param winNum
	 */
	private void sendWinOverNotice(IRole winRole, IRole failRole, int winNum) {
		if (winNum % 5 == 0 && winNum > 0) {
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.GvgWinOver);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					String content = chatAdT.content.replaceAll("~guide_name_1~",
							winRole.getFactionControler().getFactionName()).replaceAll("~guide_name_2~",
							failRole.getFactionControler().getFactionName());
					content = content.replaceAll(
							"~guide_player_1~",
							TextUtil.format("{0}|{1}|{2}", winRole.getRoleId(), winRole.getName(),
									winRole.getVipLevel())).replaceAll(
							"~guide_player_2~",
							TextUtil.format("{0}|{1}|{2}", failRole.getRoleId(), failRole.getName(),
									failRole.getVipLevel()));
					content = content.replace("~param_1~", String.valueOf(winNum));
					XsgChatManager.getInstance().sendAnnouncement(content);
				}
			}
		}
	}

	/**
	 * 保存个人荣誉排行榜
	 * 
	 * @param rank
	 */
	private void saveMemberRank(final FactionMemberRank rank) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				simpleDao.attachDirty(rank);
			}
		});
	}

	@Override
	public void reviveGvg() throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, configT.reviveMoney));
		FactionMember self = getMyFaction().getMemberByRoleId(this.roleRt.getRoleId());
		self.setDeathDate(null);

		factionGvgReviveEvent.onFactionGvgRevive(configT.reviveMoney);
	}

	@Override
	public void getMemberRank(final AMD_Faction_getMemberRank __cb) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		final List<FactionMemberRank> rankList = XsgFactionManager.getInstance().getFactionMemberRankList();
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		int size = Math.min(rankList.size(), configT.peopleRankSize);
		final List<String> roleIds = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			roleIds.add(rankList.get(i).getRoleId());
		}
		// 处理自己的排名
		int myRank = 0;
		int myHonor = 0;
		for (int i = 0; i < rankList.size(); i++) {
			if (rankList.get(i).getRoleId().equals(roleRt.getRoleId())) {
				myRank = i + 1;
				myHonor = rankList.get(i).getHonor();
				break;
			}
		}
		final MemberRankView view = new MemberRankView(myRank, roleRt.getName(), myHonor, getMyFaction().getName(),
				new MemberRankElement[0]);
		if (size == 0) {
			__cb.ice_response(LuaSerializer.serialize(view));
			return;
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<MemberRankElement> rankViews = new ArrayList<MemberRankElement>();
				for (int i = 0; i < roleIds.size(); i++) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(roleIds.get(i));
					if (iRole != null) {
						rankViews.add(new MemberRankElement(i + 1, iRole.getRoleId(), iRole.getName(), iRole
								.getHeadImage(), rankList.get(i).getHonor(), iRole.getVipLevel(), iRole
								.getFactionControler().getFactionName()));
					}
				}
				view.elements = rankViews.toArray(new MemberRankElement[0]);
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
	}

	@Override
	public FactionRankView getFactionRank() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction myFaction = getMyFaction();
		List<IFaction> factionRank = XsgFactionManager.getInstance().getFactionRank();

		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		int size = Math.min(factionRank.size(), configT.factionRankSize);
		FactionRankElement[] rankArray = new FactionRankElement[size];
		for (int i = 0; i < size; i++) {
			IFaction faction = factionRank.get(i);
			int people = 0;
			for (FactionMember fm : faction.getAllMember()) {
				if (fm.getApplyDate() != null && DateUtil.isSameDay(new Date(), fm.getApplyDate())) {
					people++;
				}
			}
			FactionRankElement rank = new FactionRankElement(i + 1, faction.getSubId(), faction.getName(),
					faction.getIcon(), faction.getFactionHonor(), people);
			rankArray[i] = rank;
		}
		int myFactionRank = 0;
		int myFactionNum = 0;
		// 自己公会排名
		for (int i = 0; i < factionRank.size(); i++) {
			if (factionRank.get(i).getId().equals(myFaction.getId())) {
				myFactionRank = i + 1;
				break;
			}
		}
		// 自己公会参加人数
		for (FactionMember fm : myFaction.getAllMember()) {
			if (fm.getApplyDate() != null && DateUtil.isSameDay(new Date(), fm.getApplyDate())) {
				myFactionNum++;
			}
		}
		FactionRankView factionRankView = new FactionRankView(myFactionRank, myFaction.getName(),
				myFaction.getFactionHonor(), myFactionNum, rankArray);
		return factionRankView;
	}

	@Override
	public void getHarmRank_async(final AMD_Faction_getHarmRank __cb) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		List<String> roleIds = new ArrayList<String>();
		final List<FactionMember> fms = getMemberHarmRank();
		for (FactionMember fm : fms) {
			roleIds.add(fm.getRoleId());
		}
		final List<CopyHarmRankView> views = new ArrayList<CopyHarmRankView>();
		FactionCopy copy = getMyFaction().getFactionCopy();
		if (roleIds.isEmpty() || copy == null) {
			__cb.ice_response(LuaSerializer.serialize(views));
			return;
		}
		final FactionCopyT copyT = XsgFactionManager.getInstance().getFactionCopyT(copy.getCopyId());
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				int rank = 1;
				for (FactionMember fm : fms) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					if (iRole == null) {
						continue;
					}
					int passMinute = (int) (DateUtil.compareTime(new Date(), iRole.getLoginTime()) / 60000);
					FactionMemberView fv = new FactionMemberView(fm.getId(), iRole.getRoleId(), iRole.getName(), iRole
							.getHeadImage(), iRole.getLevel(), fm.getDutyId(), iRole.getVipLevel(), fm
							.getContribution(), passMinute, fm.getHonor(), 3, fm.getDemandItem());
					int progress = (int) ((double) fm.getSumCopyHarm() * 100 / getCopyAllBlood(copyT.id));
					progress = Math.min(progress, 100);
					views.add(new CopyHarmRankView(rank, fm.getSumCopyHarm(), progress, fv));
					rank++;
				}
				__cb.ice_response(LuaSerializer.serialize(views));
			}
		});
	}

	/**
	 * 统计副本所有怪物血量
	 * 
	 * @param copyId
	 * @return
	 */
	private long getCopyAllBlood(int copyId) {
		long blood = 0;
		Map<Integer, FactionCopyStageT> map = XsgFactionManager.getInstance().getFactionCopyStageTMap(copyId);
		for (FactionCopyStageT st : map.values()) {
			for (int mid : st.monsterIndexMap.values()) {
				MonsterT mt = XsgCopyManager.getInstance().findMonsterT(mid);
				if (mt != null) {
					blood += mt.hp;
				}
			}
		}
		return blood;
	}

	/**
	 * 获取副本伤害排行榜
	 * 
	 * @return
	 */
	private List<FactionMember> getMemberHarmRank() {
		Set<FactionMember> members = getMyFaction().getAllMember();
		List<FactionMember> memberList = new ArrayList<FactionMember>();
		for (FactionMember fm : members) {
			// 没有伤害的不上榜
			if (fm.getSumCopyHarm() <= 0) {
				continue;
			}
			memberList.add(fm);
		}
		Collections.sort(memberList, new Comparator<FactionMember>() {

			@Override
			public int compare(FactionMember o1, FactionMember o2) {
				int i = (int) (o2.getSumCopyHarm() - o1.getSumCopyHarm());
				if (i == 0) {
					i = o1.getId().compareTo(o2.getId());
				}
				return i;
			}
		});
		return memberList;
	}

	@Override
	public void rename(String newName) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionMember member = faction.getMemberByRoleId(roleRt.getRoleId());
		if (!isBoss(member)) {
			throw new NoteException(Messages.getString("FactionControler.onlyBoosRename"));
		}
		if (SensitiveWordManager.getInstance().hasSensitiveWord(newName)) {
			throw new NoteException(Messages.getString("FactionControler.55"));
		}
		if (faction.getRenameDate() != null
				&& DateUtil.diffDate(DateUtil.getFirstSecondOfToday().getTime(), faction.getRenameDate()) < 7) {
			int days = 7 - DateUtil.diffDate(DateUtil.getFirstSecondOfToday().getTime(), faction.getRenameDate());
			throw new NoteException(TextUtil.format(Messages.getString("FactionControler.daysToRename"),
					String.valueOf(days)));
		}
		if (!factionNamePattern.matcher(newName).matches()) {
			throw new NoteException(Messages.getString("FactionControler.56"));
		}
		if (XsgFactionManager.getInstance().findFactionByName(newName) != null) {
			throw new NoteException(Messages.getString("FactionControler.57"));
		}
		try {
			roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, getRenameYuanbao()));
		} catch (Exception e) {
			throw new NoteException(Messages.getString("FactionControler.notYuanbaoRename"));
		}
		String oldName = faction.getName();
		faction.setName(newName);
		faction.setRenameDate(DateUtil.getFirstSecondOfToday().getTime());

		// 改名事件
		factionRenameEvent.onFactionRename(faction.getId(), oldName, newName);
	}

	@Override
	public int sendFactionMail(int type, String title, String content) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		// 判断是否是帮主和长老
		if (my.getDutyId() < Const.Faction.DUTY_ELDER) {
			throw new NoteException(Messages.getString("FactionControler.17"));
		}
		refreshMailCount();
		int maxTimes = XsgFactionManager.getInstance().getFactionConfigT().sendMailTimes;
		if (faction.getSendMailTimes() >= maxTimes) {
			throw new NoteException(Messages.getString("FactionControler.notSendMailTimes"));
		}

		title = SensitiveWordManager.getInstance().shieldSensitiveWord(title);
		if (title.length() > 8) {
			title = title.substring(0, 8);
		}

		content = SensitiveWordManager.getInstance().shieldSensitiveWord(content);
		if (content.length() > 50) {
			title = title.substring(0, 50);
		}

		for (FactionMember fm : faction.getAllMember()) {
			if (fm.getRoleId().equals(roleRt.getRoleId())) {// 发件人收不到邮件
				continue;
			}
			if (type == 1 && fm.getDutyId() < Const.Faction.DUTY_ELDER) {// 1发送给会长和长老
				continue;
			} else if (type == 2 && fm.getDutyId() >= Const.Faction.DUTY_ELDER) {// 2发送给普通成员
				continue;
			}
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", roleRt.getName(), fm
							.getRoleId(), title, content, "", Calendar.getInstance().getTime()));
		}
		faction.setSendMailDate(new Date());
		faction.setSendMailTimes(faction.getSendMailTimes() + 1);

		// 记录发送日志
		List<FactionMailLog> mails = new ArrayList<FactionMailLog>();
		String mailLogs = faction.getMailLogs();

		FactionMailLog[] mailArr = null;
		if (TextUtil.isBlank(mailLogs)) {
			mailArr = new FactionMailLog[0];
		} else {
			mailArr = TextUtil.GSON.fromJson(mailLogs, FactionMailLog[].class);
		}
		boolean exist = false;
		for (FactionMailLog m : mailArr) {
			mails.add(m);
			if (m.roleId.equals(roleRt.getRoleId())) {
				m.sendCount++;
				m.roleName = roleRt.getName();
				m.vipLevel = roleRt.getVipLevel();
				exist = true;
			}
		}
		if (!exist) {
			mails.add(new FactionMailLog(roleRt.getRoleId(), roleRt.getName(), roleRt.getVipLevel(), 1));
		}
		faction.setMailLogs(TextUtil.GSON.toJson(mails.toArray(new FactionMailLog[0])));
		return maxTimes - faction.getSendMailTimes();
	}

	/**
	 * 刷新邮件发送次数
	 */
	private void refreshMailCount() {
		IFaction faction = getMyFaction();
		if (faction.getSendMailDate() != null && !DateUtil.isSameDay(new Date(), faction.getSendMailDate())) {
			faction.setSendMailTimes(0);
			faction.setMailLogs("[]");
		}
	}

	@Override
	public FactionMailLog[] getFactionMailLog() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		String mailLogs = faction.getMailLogs();
		if (TextUtil.isBlank(mailLogs)) {
			return new FactionMailLog[0];
		}
		return TextUtil.GSON.fromJson(mailLogs, FactionMailLog[].class);
	}

	@Override
	public FactionWarehouseView openWarehouse() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		faction.initWarehouseItem();
		WarehouseItemBean[] items = TextUtil.GSON.fromJson(faction.getWarehouseData(), WarehouseItemBean[].class);

		List<WarehouseItem> itemList = new ArrayList<WarehouseItem>();
		for (WarehouseItemBean i : items) {
			itemList.add(new WarehouseItem(i.id, true, 0, i.itemId, i.itemNum, 0, false, false));
		}

		int warehouseLevel = faction.getWarehouseLevel();
		int volume = XsgFactionManager.getInstance().getFactionWarehouseTByLevel(warehouseLevel).volume;
		while (itemList.size() < volume) {
			itemList.add(new WarehouseItem(0, true, 0, "", 0, 0, false, false));// 开放了但是没放物品
		}
		// 显示下一级开放的格子
		FactionWarehouseT ft = XsgFactionManager.getInstance().getFactionWarehouseTByLevel(warehouseLevel + 1);
		if (ft != null) {
			for (int i = 0; i < ft.volume - volume; i++) {
				itemList.add(new WarehouseItem(0, false, warehouseLevel + 1, "", 0, 0, false, false));
			}
		}
		FactionWarehouseView view = new FactionWarehouseView(warehouseLevel, items.length, volume,
				itemList.toArray(new WarehouseItem[0]));
		return view;
	}

	@Override
	public void warehouseAllot(final AMD_Faction_warehouseAllot cb, final String roleId, String itemId, final int num)
			throws NoteException {
		if (num <= 0) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.allotNumError")));
			return;
		}
		if (!isInFaction()) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.91")));
			return;
		}
		final IFaction faction = getMyFaction();
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		if (!isBoss(my)) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.onlyBossAllot")));
			return;
		}
		WarehouseItemBean[] items = null;
		if (TextUtil.isNotBlank(faction.getWarehouseData())) {
			items = TextUtil.GSON.fromJson(faction.getWarehouseData(), WarehouseItemBean[].class);
		} else {
			items = new WarehouseItemBean[0];
		}

		final List<WarehouseItemBean> itemList = new ArrayList<WarehouseItemBean>();
		for (WarehouseItemBean i : items) {
			itemList.add(i);
		}
		WarehouseItemBean allotItem = null;
		for (WarehouseItemBean wi : itemList) {
			if (wi.itemId.equals(itemId)) {
				allotItem = wi;
				break;
			}
		}
		if (allotItem == null) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.itemNotExist")));
			return;
		}
		if (allotItem.itemNum < num) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.notOviStoreItemNum")));
			return;
		}
		if (faction.getMemberByRoleId(roleId) == null) {
			cb.ice_exception(new NoteException(Messages.getString("FactionControler.sideNotInFaction")));
			return;
		}
		final WarehouseItemBean fitem = allotItem;

		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {

			@Override
			public void run() {
				IRole r = XsgRoleManager.getInstance().findRoleById(roleId);
				if (r.getFactionControler().getCanAllotItemNum() < num) {
					cb.ice_exception(new NoteException(TextUtil.format(
							Messages.getString("FactionControler.notAllotNum"), allotNum)));
					return;
				}
				r.getFactionControler().addAllotItemNum(num);
				fitem.itemNum -= num;
				// 移除队列
				List<String> queue = TextUtil.stringToList(fitem.queue);
				if (queue.contains(roleId)) {
					queue.remove(roleId);
					fitem.queue = TextUtil.join(queue, ",");
				}
				faction.setWarehouseData(TextUtil.GSON.toJson(itemList.toArray(new WarehouseItemBean[0])));

				MailRewardT mailRewardT = null;
				if (roleId.equals(roleRt.getRoleId())) {
					mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
							.get(MailTemplate.FactionAllotSelfItem.value());
				} else {
					mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
							.get(MailTemplate.FactionAllotItem.value());
				}
				String content = mailRewardT.body.replace("$a", roleRt.getName());
				Property[] pro = new Property[] { new Property(fitem.itemId, num) };
				Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
						roleId, mailRewardT.title, content, XsgMailManager.getInstance().serializeMailAttach(pro),
						Calendar.getInstance().getTime());
				XsgMailManager.getInstance().sendMail(mail);
				// 增加分配日志
				String itemName = XsgItemManager.getInstance().findAbsItemT(fitem.itemId).getName();
				FactionAllotLog log = new FactionAllotLog(DateUtil.format(new Date(),
						Messages.getString("FactionControler.52")), roleRt.getName(), roleRt.getVipLevel(),
						r.getName(), r.getVipLevel(), num > 1 ? itemName + "X" + num : itemName);
				faction.addAllotLog(log);
				cb.ice_response();
			}
		}, new Runnable() {

			@Override
			public void run() {
				cb.ice_response();
			}
		});
	}

	@Override
	public FactionStorehouseView openStorehouse() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();

		List<StorehouseItem> listItem = new ArrayList<StorehouseItem>();
		int storehouseLevel = faction.getStorehouseLevel();
		List<FactionStorehouseT> storehouseTs = XsgFactionManager.getInstance().getFactionStorehouseT();
		for (FactionStorehouseT t : storehouseTs) {
			listItem.add(new StorehouseItem(storehouseLevel >= t.level, t.level, t.itemId, t.itemName,
					t.isFree == 1 ? 0 : t.price));
		}

		FactionStorehouseView view = new FactionStorehouseView(storehouseLevel, faction.getScore(),
				listItem.toArray(new StorehouseItem[0]));
		return view;
	}

	@Override
	public void storehousePurchase(String itemId, int num) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (num <= 0) {
			throw new NoteException(Messages.getString("FactionControler.allotNumError"));
		}
		IFaction faction = getMyFaction();
		if (!isBossOrElder(roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.onlyBossOrElder"));
		}

		FactionStorehouseT storehouseT = XsgFactionManager.getInstance().getFactionStorehouseT(itemId);
		if (storehouseT == null || storehouseT.level > faction.getStorehouseLevel()) {
			throw new NoteException(Messages.getString("FactionControler.itemNotExist"));
		}

		long sumPrice = storehouseT.isFree == 1 ? 0 : storehouseT.price * num;
		if (faction.getScore() < sumPrice) {
			throw new NoteException(Messages.getString("FactionControler.notPurchase"));
		}

		faction.setScore((int) (faction.getScore() - sumPrice));
		OviStoreItem[] items = null;
		if (TextUtil.isNotBlank(faction.getOviStoreData())) {
			items = TextUtil.GSON.fromJson(faction.getOviStoreData(), OviStoreItem[].class);
		} else {
			items = new OviStoreItem[0];
		}

		List<OviStoreItem> itemList = new ArrayList<OviStoreItem>();
		for (OviStoreItem o : items) {
			itemList.add(o);
		}

		boolean exist = false;
		for (OviStoreItem i : itemList) {
			if (i.itemId.equals(storehouseT.itemId)) {
				i.num += num;
				i.price = storehouseT.honorPrice;
				exist = true;
			}
		}
		// 商铺里面不存在就增加物品
		if (!exist) {
			itemList.add(new OviStoreItem(storehouseT.itemId, storehouseT.itemName, num, storehouseT.honorPrice, System
					.currentTimeMillis()));
		}
		faction.setOviStoreData(TextUtil.GSON.toJson(itemList.toArray(new OviStoreItem[0])));
		// 增加购置日志
		List<PurchaseLog> logList = new ArrayList<PurchaseLog>();
		PurchaseLog[] logs = TextUtil.GSON.fromJson(faction.getPurchaseLogs(), PurchaseLog[].class);
		for (PurchaseLog l : logs) {
			logList.add(l);
		}
		logList.add(
				0,
				new PurchaseLog(DateUtil.format(new Date(), Messages.getString("FactionControler.52")), roleRt
						.getName(), roleRt.getVipLevel(), TextUtil.format(
						Messages.getString("FactionControler.purchaseLog"), storehouseT.itemName, num, sumPrice)));
		if (logList.size() > 100) {
			logList.remove(logList.size() - 1);
		}
		faction.setPurchaseLogs(TextUtil.GSON.toJson(logList.toArray(new PurchaseLog[0])));
	}

	@Override
	public FactionOviStoreView openOviStore() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		OviStoreItem[] items = new OviStoreItem[0];
		if (TextUtil.isNotBlank(faction.getOviStoreData())) {
			items = TextUtil.GSON.fromJson(faction.getOviStoreData(), OviStoreItem[].class);
		}
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());
		FactionOviStoreView view = new FactionOviStoreView(my.getHonor(), items);
		return view;
	}

	@Override
	public void oviStoreBuy(String itemId, int num) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		if (num <= 0) {
			throw new NoteException(Messages.getString("FactionControler.allotNumError"));
		}
		IFaction faction = getMyFaction();
		OviStoreItem[] items = null;
		if (TextUtil.isNotBlank(faction.getOviStoreData())) {
			items = TextUtil.GSON.fromJson(faction.getOviStoreData(), OviStoreItem[].class);
		} else {
			items = new OviStoreItem[0];
		}

		List<OviStoreItem> itemList = new ArrayList<OviStoreItem>();
		for (OviStoreItem i : items) {
			itemList.add(i);
		}
		OviStoreItem buyItem = null;
		for (OviStoreItem o : itemList) {
			if (o.itemId.equals(itemId)) {
				buyItem = o;
				break;
			}
		}

		if (buyItem == null) {
			throw new NoteException(Messages.getString("FactionControler.itemNotExist"));
		}
		if (buyItem.num < num) {
			throw new NoteException(Messages.getString("FactionControler.notOviStoreItemNum"));
		}
		FactionMember fm = faction.getMemberByRoleId(roleRt.getRoleId());
		long sumPrice = buyItem.price * num;
		if (fm.getHonor() < sumPrice) {
			throw new NoteException(Messages.getString("FactionControler.98"));
		}

		fm.setHonor((int) (fm.getHonor() - sumPrice));
		roleRt.getItemControler().changeItemByTemplateCode(buyItem.itemId, num);
		buyItem.num -= num;
		if (buyItem.num <= 0) {
			itemList.remove(buyItem);
		}
		faction.setOviStoreData(TextUtil.GSON.toJson(itemList.toArray(new OviStoreItem[0])));
	}

	@Override
	public FactionTechnologyView technologyList() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		// 处理老数据
		mergerOldTechnology(faction);

		int num = roleRt.getItemControler().getItemCountInPackage(XsgFactionManager.WEI_ZHANG_CODE);
		int cd = 0;
		if (donateCd != null && donateCd.getTime() > System.currentTimeMillis()) {
			cd = (int) ((donateCd.getTime() - System.currentTimeMillis()) / 1000);
		}
		if (hasCd) {
			if (cd <= 0) {
				hasCd = false;
			}
		}
		List<TechnologyT> technologyTs = XsgFactionManager.getInstance().getAllTechnologyT();

		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);
		if (exist.length == 0) {// 初始化
			List<FactionTechnologyBean> listBean = new ArrayList<FactionTechnologyBean>();
			for (TechnologyT t : technologyTs) {
				listBean.add(new FactionTechnologyBean(t.id, t.initLevel, 0, 0, false));
			}
			exist = listBean.toArray(new FactionTechnologyBean[0]);
			faction.setTechnologyData(TextUtil.GSON.toJson(exist));
		}

		Map<Integer, FactionTechnologyBean> existMap = new HashMap<Integer, FactionTechnologyBean>();
		for (FactionTechnologyBean b : exist) {
			existMap.put(b.id, b);
		}

		List<FactionTechnology> ft = new ArrayList<FactionTechnology>();
		boolean technologyLevelUp = false;// 是否升级
		for (TechnologyT t : technologyTs) {
			FactionTechnologyBean bean = existMap.get(t.id);
			if (bean == null) {
				ft.add(new FactionTechnology(t.id, t.initLevel, 0, false, false, 0, 0, false, false, false));
			} else {
				if (bean.level >= XsgFactionManager.getInstance().getTechnologyMaxLevel(t.type)) {
					ft.add(new FactionTechnology(t.id, bean.level, bean.exp, false, false, 0, 0, bean.isRecommend,
							false, true));
				} else {
					int levelExp = 0;
					int levelStudyTime = 0;
					if (t.type == TechnologyType.HOT.value) {// 热门类
						TechnologyHotLevelT hotLevelT = XsgFactionManager.getInstance().getTechnologyHotLevelT(
								bean.level + 1);
						levelExp = hotLevelT.exp;
						levelStudyTime = hotLevelT.studyMinute;
					} else {
						TechnologyLevelT levelT = XsgFactionManager.getInstance().getTechnologyLevelT(bean.level + 1);
						levelExp = levelT.exp;
						levelStudyTime = levelT.studyMinute;
					}
					// 可用元宝捐赠的科技ID
					List<String> canDonateIds = TextUtil.stringToList(roleDb.getRoleFaction().getCanYuanbaoDonate());
					FactionTechnology technology = new FactionTechnology(t.id, bean.level, bean.exp, false, false, 0,
							0, bean.isRecommend, canDonateIds.contains(String.valueOf(bean.id)), false);
					if (bean.studyTime != 0) {
						// 已经研究秒数
						int studySecond = (int) ((System.currentTimeMillis() - bean.studyTime) / 1000);
						if (studySecond >= levelStudyTime * 60) {// 已经研究完毕
							bean.level++;
							// 减去升级消耗的经验
							bean.exp -= levelExp;
							bean.studyTime = 0;
							faction.setStudyNum(faction.getStudyNum() + 1);
							faction.setTechnologyData(TextUtil.GSON.toJson(exist));

							technology.exp = bean.exp;
							technology.level = bean.level;
							if (t.type != TechnologyType.HOT.value) {
								technologyLevelUp = true;
							}
						} else {
							technology.isOnStudy = true;
							technology.studySecond = studySecond;
							technology.studyProgress = studySecond * 100 / (levelStudyTime * 60);
						}
					} else if (bean.exp >= levelExp) {
						technology.isCanStudy = true;
					}
					ft.add(technology);
				}
			}
		}

		if (technologyLevelUp) {
			faction.onTechnologyLevelUp();
		}

		FactionTechnologyView view = new FactionTechnologyView(ft.toArray(new FactionTechnology[0]),
				faction.getStudyNum(), num, hasCd, cd, faction.getScore());
		return view;
	}

	/**
	 * 合并老公会科技
	 */
	private void mergerOldTechnology(IFaction faction) {
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);
		List<FactionTechnologyBean> listBean = new ArrayList<FactionTechnologyBean>();

		Map<String, List<FactionTechnologyBean>> oldMap = new HashMap<String, List<FactionTechnologyBean>>();
		for (FactionTechnologyBean b : exist) {
			if (b.id >= 601) {// 已经合并过了
				return;
			}
			if (b.id >= 201 && b.id <= 509) {
				if (b.studyTime != 0) {// 研究中的直接升级
					b.studyTime = 0;
					b.exp = 0;
					b.level++;
				}
				String sameId = String.valueOf(b.id).substring(2, 3);
				List<FactionTechnologyBean> oldList = oldMap.get(sameId);
				if (oldList == null) {
					oldList = new ArrayList<FactionTechnologyBean>();
					oldMap.put(sameId, oldList);
				}
				oldList.add(b);
			}
			listBean.add(b);
		}

		for (Entry<String, List<FactionTechnologyBean>> entry : oldMap.entrySet()) {
			TechnologyT t = XsgFactionManager.getInstance().getTechnologyTBySameId(entry.getKey());
			FactionTechnologyBean maxLevelBean = new FactionTechnologyBean();

			for (FactionTechnologyBean b : entry.getValue()) {
				if (b.level > maxLevelBean.level) {
					maxLevelBean = b;
				}
			}

			int sumExp = maxLevelBean.exp;// 最高等级只要他的剩余经验
			int sumScore = 0;
			for (FactionTechnologyBean b : entry.getValue()) {
				if (b.id == maxLevelBean.id) {
					continue;
				}
				sumExp += b.exp;
				for (int i = 1; i <= b.level; i++) {// 每级经验都要
					TechnologyLevelT levelT = XsgFactionManager.getInstance().getTechnologyLevelT(i);
					sumExp += levelT.exp;
					sumScore += levelT.studyScore;
				}
			}
			// 返还粮草
			faction.setScore(faction.getScore() + sumScore);

			listBean.add(new FactionTechnologyBean(t.id, maxLevelBean.level, sumExp, 0, false));
		}

		faction.setTechnologyData(TextUtil.GSON.toJson(listBean.toArray(new FactionTechnologyBean[0])));
	}

	@Override
	public int setRecommendTechnology(int id) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		if (faction.getRecommendRefreshDate() == null
				|| !DateUtil.isSameDay(new Date(), faction.getRecommendRefreshDate())) {
			faction.setRecommendNum(0);
			faction.setRecommendRefreshDate(new Date());
		}
		TechnologyConfT tf = XsgFactionManager.getInstance().getTechnologyConfT();
		if (faction.getRecommendNum() >= tf.recommendNum) {
			throw new NoteException(Messages.getString("FactionControler.recommentNumFull"));
		}
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);

		List<FactionTechnologyBean> existList = new ArrayList<FactionTechnologyBean>();
		TechnologyT technologyT = XsgFactionManager.getInstance().getTechnologyTById(id);
		if (technologyT == null) {
			throw new NoteException(Messages.getString("id not exist!"));
		}

		FactionTechnologyBean bean = null;
		for (FactionTechnologyBean b : exist) {
			b.isRecommend = false;
			if (b.id == id) {
				if (b.level == XsgFactionManager.getInstance().getTechnologyMaxLevel(technologyT.type)) {
					throw new NoteException(Messages.getString("FactionControler.isFullNotSet"));
				}
				bean = b;
				b.isRecommend = true;
			}
			existList.add(b);
		}
		if (bean == null) {
			bean = new FactionTechnologyBean(id, technologyT.initLevel, 0, 0, true);
			existList.add(bean);
		}
		faction.setTechnologyData(TextUtil.GSON.toJson(existList.toArray(new FactionTechnologyBean[0])));
		faction.setRecommendNum(faction.getRecommendNum() + 1);
		faction.addHistory(roleRt,
				TextUtil.format(Messages.getString("FactionControler.recommentLog"), technologyT.logStr), roleRt);

		return tf.recommendNum - faction.getRecommendNum();
	}

	@Override
	public void donateTechnology(int id, int type) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionTechnologyView view = technologyList();
		if (view.hasCd) {
			throw new NoteException(Messages.getString("FactionControler.cdNotDonate"));
		}
		TechnologyT technologyT = XsgFactionManager.getInstance().getTechnologyTById(id);
		if (technologyT == null) {
			throw new NoteException(Messages.getString("id not exist!"));
		}
		TechnologyConfT confT = XsgFactionManager.getInstance().getTechnologyConfT();
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);
		List<FactionTechnologyBean> existList = new ArrayList<FactionTechnologyBean>();

		FactionTechnologyBean bean = null;
		for (FactionTechnologyBean b : exist) {
			if (b.id == id) {
				bean = b;
			}
			existList.add(b);
		}
		if (bean == null) {
			bean = new FactionTechnologyBean(id, technologyT.initLevel, 0, 0, false);
			existList.add(bean);
		}

		int levelExp = 0;
		if (technologyT.type == TechnologyType.HOT.value) {// 热门类
			TechnologyHotLevelT hotLevelT = XsgFactionManager.getInstance().getTechnologyHotLevelT(bean.level + 1);
			levelExp = hotLevelT.exp;
		} else {
			TechnologyLevelT levelT = XsgFactionManager.getInstance().getTechnologyLevelT(bean.level + 1);
			levelExp = levelT.exp;
		}
		if (bean.exp >= levelExp) {
			throw new NoteException(Messages.getString("FactionControler.expIsFull"));
		}
		// 可用元宝捐赠的科技ID
		List<String> canDonateIds = TextUtil.stringToList(roleDb.getRoleFaction().getCanYuanbaoDonate());
		FactionMember my = faction.getMemberByRoleId(roleRt.getRoleId());

		DonateLog donateLog = new DonateLog(DateUtil.format(new Date()), 0, 0);
		RoleFaction rf = roleDb.getRoleFaction();
		if (type == 1) {// 元宝
			if (!canDonateIds.contains(String.valueOf(id))) {
				throw new NoteException(Messages.getString("FactionControler.notUseYuanbao"));
			}
			try {
				roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, confT.donateYuanbao));
			} catch (NotEnoughMoneyException e) {
			} catch (NotEnoughYuanBaoException e) {
				throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
			}
			my.setHonor(my.getHonor() + confT.yuanbaoHonor);
			bean.exp += confT.yuanbaoExp;
			canDonateIds.remove(String.valueOf(id));
			rf.setCanYuanbaoDonate(TextUtil.join(canDonateIds, ","));

			donateLog.donateYuanbao = confT.donateYuanbao;
			rf.setDonateYuanbao(rf.getDonateYuanbao() + confT.donateYuanbao);
		} else {
			if (view.weizhangNum < confT.useWeiZhang) {
				throw new NoteException(Messages.getString("FactionControler.notWeiZhang"));
			}
			roleRt.getItemControler().changeItemByTemplateCode(XsgFactionManager.WEI_ZHANG_CODE, -confT.useWeiZhang);
			my.setHonor(my.getHonor() + confT.weiZhangHonor);
			bean.exp += confT.weiZhangExp;
			// 触发元宝捐赠
			if (NumberUtil.random(100) < confT.yuanbaoPro && !canDonateIds.contains(String.valueOf(id))) {
				canDonateIds.add(String.valueOf(id));
				rf.setCanYuanbaoDonate(TextUtil.join(canDonateIds, ","));
			}
			// 处理CD
			Calendar now = Calendar.getInstance();
			if (donateCd != null && donateCd.getTime() > System.currentTimeMillis()) {
				now.setTime(donateCd);
			}
			now.add(Calendar.SECOND, confT.oneCD);
			donateCd = now.getTime();
			if (view.cd + confT.oneCD >= confT.maxCD) {
				hasCd = true;
			}

			donateLog.donateWeizhang = confT.useWeiZhang;
			rf.setDonateWeizhang(rf.getDonateWeizhang() + confT.useWeiZhang);
		}
		faction.setTechnologyData(TextUtil.GSON.toJson(existList.toArray(new FactionTechnologyBean[0])));
		// 保存捐赠日志
		refreshDonateLog(my);
		List<DonateLog> logList = new ArrayList<DonateLog>();
		DonateLog[] logs = TextUtil.GSON.fromJson(my.getDonateLogs(), DonateLog[].class);
		for (DonateLog l : logs) {
			logList.add(l);
		}
		logList.add(donateLog);
		my.setDonateLogs(TextUtil.GSON.toJson(logList.toArray(new DonateLog[0])));

		this.factionDonateTecEvent.onDonateTec();
	}

	/**
	 * 刷新科技捐赠记录
	 * 
	 * @param faction
	 */
	private void refreshDonateLog(FactionMember fm) {
		DonateLog[] logs = TextUtil.GSON.fromJson(fm.getDonateLogs(), DonateLog[].class);
		if (logs.length > 0) {
			Date date = DateUtil.parseDate(logs[0].datetime);
			if (!DateUtil.isSameWeek(new Date(), date)) {
				fm.setDonateLogs("[]");
			}
		}
	}

	@Override
	public void clearDonateCD() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		FactionTechnologyView view = technologyList();
		if (!view.hasCd) {
			throw new NoteException(Messages.getString("FactionControler.notExistCd"));
		}
		TechnologyConfT confT = XsgFactionManager.getInstance().getTechnologyConfT();
		try {
			roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, confT.clearCDYuanbao));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		this.hasCd = false;
		this.donateCd = null;
	}

	/**
	 * 是否会长或长老
	 * 
	 * @param roleId
	 * @return
	 */
	private boolean isBossOrElder(String roleId) {
		FactionMember fm = getMyFaction().getMemberByRoleId(roleId);
		// 判断是否是帮主和长老
		if (fm.getDutyId() < Const.Faction.DUTY_ELDER) {
			return false;
		}
		return true;
	}

	@Override
	public void studyTechnology(int id) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		// 判断是否是帮主和长老
		if (!isBossOrElder(roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.17"));
		}

		TechnologyT technologyT = XsgFactionManager.getInstance().getTechnologyTById(id);
		if (technologyT == null) {
			throw new NoteException(Messages.getString("id not exist!"));
		}
		// 刷新下研究进度
		technologyList();
		int type = technologyT.type;
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);
		for (FactionTechnologyBean b : exist) {
			if (b.studyTime != 0) {
				throw new NoteException(Messages.getString("FactionControler.otherStudyIng"));
			}
		}
		FactionTechnologyBean bean = null;
		for (FactionTechnologyBean b : exist) {
			if (b.id == id) {
				bean = b;
				if (bean.studyTime != 0) {
					throw new NoteException(Messages.getString("FactionControler.studyIng"));
				}
				break;
			}
		}

		int levelExp = 0;
		int levelStudyScore = 0;
		if (type == TechnologyType.HOT.value) {// 热门类
			TechnologyHotLevelT hotLevelT = XsgFactionManager.getInstance().getTechnologyHotLevelT(bean.level + 1);
			levelExp = hotLevelT.exp;
			levelStudyScore = hotLevelT.studyScore;
		} else {
			TechnologyLevelT levelT = XsgFactionManager.getInstance().getTechnologyLevelT(bean.level + 1);
			levelExp = levelT.exp;
			levelStudyScore = levelT.studyScore;
		}
		if (bean.exp < levelExp) {
			throw new NoteException(Messages.getString("FactionControler.notExp"));
		}
		if (faction.getScore() < levelStudyScore) {
			throw new NoteException(Messages.getString("FactionControler.notFactionScore"));
		}
		faction.setScore(faction.getScore() - levelStudyScore);
		bean.studyTime = System.currentTimeMillis();
		faction.setTechnologyData(TextUtil.GSON.toJson(exist));
		faction.addHistory(roleRt,
				TextUtil.format(Messages.getString("FactionControler.studyLog"), technologyT.logStr), roleRt);
	}

	@Override
	public int getTechnologyValue(int id) {
		if (!isInFaction()) {
			return 0;
		}
		IFaction faction = getMyFaction();
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(faction.getTechnologyData(),
				FactionTechnologyBean[].class);
		FactionTechnologyBean bean = null;
		for (FactionTechnologyBean b : exist) {
			if (b.id == id) {
				bean = b;
				break;
			}
		}
		if (bean == null) {
			return 0;
		}
		return XsgFactionManager.getInstance().getTechnologyValue(bean.id, bean.level);
	}

	@Override
	public int getCanAllotItemNum() {
		RoleFaction roleFaction = roleDb.getRoleFaction();
		if (roleFaction.getRefreshAllotDate() == null
				|| !DateUtil.isSameDay(new Date(), roleFaction.getRefreshAllotDate())) {
			roleFaction.setAllotItemNum(0);
			roleFaction.setRefreshAllotDate(new Date());
		}
		return allotNum - roleFaction.getAllotItemNum();
	}

	@Override
	public void addAllotItemNum(int num) {
		roleDb.getRoleFaction().setAllotItemNum(roleDb.getRoleFaction().getAllotItemNum() + num);
	}

	@Override
	public FactionAllotLog[] getFactionAllotLog() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionAllotLog[] logs = TextUtil.GSON.fromJson(faction.getAllotLog(), FactionAllotLog[].class);
		return logs;
	}

	@Override
	public void getTechnologyDonateLog(final AMD_Faction_getTechnologyDonateLog __cb) throws NoteException {
		if (!isInFaction()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.91")));
			return;
		}
		final IFaction faction = getMyFaction();
		final List<String> roleIds = new ArrayList<String>();
		for (FactionMember member : faction.getAllMember()) {
			refreshDonateLog(member);
			roleIds.add(member.getRoleId());
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<TechnologyDonateLog> today = new ArrayList<TechnologyDonateLog>();
				List<TechnologyDonateLog> week = new ArrayList<TechnologyDonateLog>();
				List<TechnologyDonateLog> history = new ArrayList<TechnologyDonateLog>();
				for (String id : roleIds) {
					IRole r = XsgRoleManager.getInstance().findRoleById(id);
					if (r != null) {
						FactionMember fm = faction.getMemberByRoleId(id);
						DonateLog result = countDonate(fm, 0);
						today.add(new TechnologyDonateLog(id, r.getName(), r.getHeadImage(), r.getLevel(), r
								.getVipLevel(), result.donateWeizhang, result.donateYuanbao));

						result = countDonate(fm, 1);
						week.add(new TechnologyDonateLog(id, r.getName(), r.getHeadImage(), r.getLevel(), r
								.getVipLevel(), result.donateWeizhang, result.donateYuanbao));

						history.add(new TechnologyDonateLog(id, r.getName(), r.getHeadImage(), r.getLevel(), r
								.getVipLevel(), r.getRoleFaction().getDonateWeizhang(), r.getRoleFaction()
								.getDonateYuanbao()));
					}
				}
				sortTechnologyDonateLog(today);
				sortTechnologyDonateLog(week);
				sortTechnologyDonateLog(history);
				TechnologyDonateView view = new TechnologyDonateView(today.toArray(new TechnologyDonateLog[0]), week
						.toArray(new TechnologyDonateLog[0]), history.toArray(new TechnologyDonateLog[0]));
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
	}

	/**
	 * 统计成员科技捐赠
	 * 
	 * @param fm
	 * @param type
	 * @return
	 */
	private DonateLog countDonate(FactionMember fm, int type) {
		DonateLog result = new DonateLog();
		DonateLog[] logs = TextUtil.GSON.fromJson(fm.getDonateLogs(), DonateLog[].class);
		if (type == 0) {// 当天
			for (DonateLog l : logs) {
				Date date = DateUtil.parseDate(l.datetime);
				if (DateUtil.isSameDay(new Date(), date)) {
					result.donateWeizhang += l.donateWeizhang;
					result.donateYuanbao += l.donateYuanbao;
				}
			}
		} else {// 本周
			for (DonateLog l : logs) {
				Date date = DateUtil.parseDate(l.datetime);
				if (DateUtil.isSameWeek(new Date(), date)) {
					result.donateWeizhang += l.donateWeizhang;
					result.donateYuanbao += l.donateYuanbao;
				}
			}
		}
		return result;
	}

	/**
	 * 科技捐赠记录排序
	 * 
	 * @param list
	 */
	private void sortTechnologyDonateLog(List<TechnologyDonateLog> list) {
		Collections.sort(list, new Comparator<TechnologyDonateLog>() {

			@Override
			public int compare(TechnologyDonateLog o1, TechnologyDonateLog o2) {
				int i = o2.weizhang - o1.weizhang;
				if (i == 0) {
					i = o2.yuanbao - o1.yuanbao;
				}
				if (i == 0) {
					i = o1.id.compareTo(o2.id);
				}
				return i;
			}
		});
	}

	@Override
	public PurchaseLog[] getPurchaseLog() throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		return TextUtil.GSON.fromJson(faction.getPurchaseLogs(), PurchaseLog[].class);
	}

	@Override
	public void getWarehouseItemQueue(final AMD_Faction_getWarehouseItemQueue __cb, int id) {
		if (!isInFaction()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.91")));
			return;
		}
		IFaction faction = getMyFaction();
		WarehouseItemBean[] items = null;
		if (TextUtil.isNotBlank(faction.getWarehouseData())) {
			items = TextUtil.GSON.fromJson(faction.getWarehouseData(), WarehouseItemBean[].class);
		} else {
			items = new WarehouseItemBean[0];
		}

		final List<String> roles = new ArrayList<String>();
		for (WarehouseItemBean i : items) {
			if (i.id == id) {
				roles.addAll(TextUtil.stringToList(i.queue));
				break;
			}
		}
		final List<TechnologyDonateLog> result = new ArrayList<TechnologyDonateLog>();
		if (roles.isEmpty()) {
			__cb.ice_response(LuaSerializer.serialize(result.toArray(new TechnologyDonateLog[0])));
		} else {
			XsgRoleManager.getInstance().loadRoleAsync(roles, new Runnable() {

				@Override
				public void run() {
					for (String id : roles) {
						IRole r = XsgRoleManager.getInstance().findRoleById(id);
						if (r != null) {
							result.add(new TechnologyDonateLog(r.getRoleId(), r.getName(), r.getHeadImage(), r
									.getLevel(), r.getVipLevel(), 0, 0));
						}
					}
					__cb.ice_response(LuaSerializer.serialize(result.toArray(new TechnologyDonateLog[0])));
				}
			});
		}
	}

	@Override
	public void demandItem(String itemId, int type) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionMember fm = faction.getMemberByRoleId(roleRt.getRoleId());
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemId);
		if (itemT == null) {
			fm.setDemandItem("");
			return;
		}
		if (type == 0) {// 索要
			fm.setDemandItem(itemId);
		} else {// 取消
			fm.setDemandItem("");
		}
	}

	@Override
	public void applyItem(int id, int type) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		FactionMember fm = faction.getMemberByRoleId(roleRt.getRoleId());
		if (DateUtil.compareDate(new Date(), fm.getParticipateTime()) < 1) {
			throw new NoteException(TextUtil.format(Messages.getString("FactionControler.canotApplyItem"), 24));
		}

		WarehouseItemBean[] items = TextUtil.GSON.fromJson(faction.getWarehouseData(), WarehouseItemBean[].class);

		if (type == 0) {// 申请
			for (WarehouseItemBean i : items) {
				List<String> queue = TextUtil.stringToList(i.queue);
				if (queue.contains(roleRt.getRoleId())) {
					throw new NoteException(Messages.getString("FactionControler.onlyApplyOnItem"));
				}
			}
		}

		for (WarehouseItemBean i : items) {
			if (i.id == id) {
				List<String> queue = TextUtil.stringToList(i.queue);
				if (type == 0) {// 申请
					if (!queue.contains(roleRt.getRoleId())) {
						queue.add(roleRt.getRoleId());
					}
				} else {// 取消
					queue.remove(roleRt.getRoleId());
				}
				i.queue = TextUtil.join(queue, ",");
				faction.setWarehouseData(TextUtil.GSON.toJson(items));
				break;
			}
		}
	}

	@Override
	public int getBeforePeople(String itemId) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		WarehouseItemBean[] items = TextUtil.GSON.fromJson(faction.getWarehouseData(), WarehouseItemBean[].class);

		int people = 0;
		for (WarehouseItemBean b : items) {
			if (String.valueOf(b.id).equals(itemId)) {
				List<String> list = TextUtil.stringToList(b.queue);
				for (String id : list) {
					if (roleRt.getRoleId().equals(id)) {
						break;
					}
					people++;
				}
				break;
			}
		}
		return people;
	}

	@Override
	public boolean recruit(boolean isFree) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		if (!isBossOrElder(roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.17"));
		}
		IntIntPair ii = getRecruitCount(0);

		String[] contents = Messages.getString("FactionControler.recruitMessages").split(";");
		String content = contents[NumberUtil.random(contents.length)].replace("%n", faction.getName()).replace("%i",
				faction.getSubId());
		if (isFree) {
			if (ii.first <= 0) {// 免费时次数不足
				return false;
			}
			try {
				roleRt.getChatControler().speak(makeTextMessage(ChatChannel.World, content));
				faction.setRecruitNum(faction.getRecruitNum() + 1);
				return true;
			} catch (NoteException e) {
				throw e;
			} catch (Exception e) {
				return false;
			}
		} else {
			if (ii.first > 0) {
				try {
					roleRt.getChatControler().speak(makeTextMessage(ChatChannel.World, content));
					faction.setRecruitNum(faction.getRecruitNum() + 1);
					return true;
				} catch (NoteException e) {
					throw e;
				} catch (Exception e) {
					return false;
				}
			} else {
				if (roleRt.getTotalYuanbao() < 50) {
					throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
				}
				try {
					roleRt.getChatControler().speak(makeTextMessage(ChatChannel.World, content));
				} catch (NoteException e) {
					throw e;
				} catch (Exception e) {
					return false;
				}
				try {
					roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, 50));
				} catch (NotEnoughMoneyException e) {
				} catch (NotEnoughYuanBaoException e) {
					throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
				}
				return true;
			}
		}
	}

	@Override
	public void invite(final AMD_Faction_invite __cb, final boolean isFree, final String roleId) {
		if (!isInFaction()) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.91")));
			return;
		}
		if (!isBossOrElder(roleRt.getRoleId())) {
			__cb.ice_exception(new NoteException(Messages.getString("FactionControler.17")));
			return;
		}
		final IFaction faction = getMyFaction();
		final RoleFaction roleFaction = roleDb.getRoleFaction();

		final String content = Messages.getString("FactionControler.inviteMessage").replace("%n", faction.getName())
				.replace("%i", faction.getSubId());
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {

			@Override
			public void run() {
				IRole target = XsgRoleManager.getInstance().findRoleById(roleId);
				if (faction.getJoinLevel() > target.getLevel() || faction.getJoinVip() > target.getVipLevel()) {
					__cb.ice_exception(new NoteException(Messages.getString("FactionControler.notInvite")));
					return;
				}
				IntIntPair ii = null;
				try {
					ii = getRecruitCount(1);
				} catch (NoteException e1) {
					__cb.ice_exception(e1);
					return;
				}
				if (isFree) {
					if (ii.first <= 0) {// 免费时次数不足
						__cb.ice_response(false);
						return;
					}
					try {
						roleRt.getChatControler().speakTo(null, target, makeTextMessage(ChatChannel.Private, content));
						roleFaction.setInviteNum(roleFaction.getInviteNum() + 1);
						__cb.ice_response(true);
					} catch (NoteException e) {
						__cb.ice_exception(e);
					}
				} else {
					if (ii.first > 0) {
						try {
							roleRt.getChatControler().speakTo(null, target,
									makeTextMessage(ChatChannel.Private, content));
							roleFaction.setInviteNum(roleFaction.getInviteNum() + 1);
							__cb.ice_response(true);
							return;
						} catch (NoteException e) {
							__cb.ice_exception(e);
							return;
						}
					} else {
						if (roleRt.getTotalYuanbao() < 50) {
							__cb.ice_exception(new NoteException(Messages.getString("WorldBossControler.notYuanbao")));
							return;
						}
						try {
							roleRt.getChatControler().speakTo(null, target,
									makeTextMessage(ChatChannel.Private, content));
						} catch (NoteException e) {
							__cb.ice_exception(e);
							return;
						}
						try {
							roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, 50));
							__cb.ice_response(true);
							return;
						} catch (NotEnoughMoneyException e) {
						} catch (NotEnoughYuanBaoException e) {
							__cb.ice_exception(new NoteException(Messages.getString("WorldBossControler.notYuanbao")));
						}
					}
				}
			}
		}, null);
	}

	private TextMessage makeTextMessage(ChatChannel channel, String content) {
		ChatRole cr = new ChatRole();
		cr.id = roleRt.getRoleId();
		cr.name = roleRt.getName();
		cr.level = (short) roleRt.getLevel();
		cr.vip = roleRt.getVipLevel();
		cr.icon = roleRt.getHeadImage();
		cr.chatTime = DateUtil.toString(System.currentTimeMillis(), "HH:mm");
		cr.OfficalRankId = roleRt.getOfficalRankId();
		return new TextMessage(channel, null, cr, 1, content, 0, 0);
	}

	@Override
	public IntIntPair getRecruitCount(int type) throws NoteException {
		if (!isInFaction()) {
			throw new NoteException(Messages.getString("FactionControler.91"));
		}
		IFaction faction = getMyFaction();
		if (!isBossOrElder(roleRt.getRoleId())) {
			throw new NoteException(Messages.getString("FactionControler.17"));
		}
		refreshRecruitCount(faction);
		IntIntPair ii = null;
		if (type == 0) {// 招贤
			ii = new IntIntPair(5 - faction.getRecruitNum(), 5);
		} else {// 邀请
			ii = new IntIntPair(3 - roleDb.getRoleFaction().getInviteNum(), 3);
		}
		return ii;
	}

	/**
	 * 刷新招贤邀请次数
	 * 
	 * @param faction
	 */
	private void refreshRecruitCount(IFaction faction) {
		if (faction.getRefreshRecruitDate() == null
				|| DateUtil.isPass("06:00:00", "HH:mm:ss", faction.getRefreshRecruitDate())) {
			faction.setRefreshRecruitDate(new Date());
			faction.setRecruitNum(0);
			roleDb.getRoleFaction().setInviteNum(0);
		}
	}

	@Override
	public void getFactionInfo(final AMD_Faction_getFactionInfo __cb, String id) {
		final List<String> roleIds = new ArrayList<String>();
		final IFaction myFaction = XsgFactionManager.getInstance().findByShowId(id);
		for (FactionMember member : myFaction.getAllMember()) {
			roleIds.add(member.getRoleId());
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<FactionMemberView> list = new ArrayList<FactionMemberView>();
				List<FactionMember> factionMembers = new ArrayList<FactionMember>();
				for (FactionMember fm : myFaction.getAllMember()) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					if (iRole == null) {
						continue;
					}
					fm.setLevel(iRole.getLevel());
					factionMembers.add(fm);
				}
				// 排序 职位、等级、VIP等级、ID
				Collections.sort(factionMembers, new Comparator<FactionMember>() {

					@Override
					public int compare(FactionMember o1, FactionMember o2) {
						if (o1.getDutyId() != o2.getDutyId()) {
							return o2.getDutyId() - o1.getDutyId();
						} else if (o1.getLevel() != o2.getLevel()) {
							return o2.getLevel() - o1.getLevel();
						} else {
							IRole iRole1 = XsgRoleManager.getInstance().findRoleById(o1.getRoleId());
							IRole iRole2 = XsgRoleManager.getInstance().findRoleById(o2.getRoleId());
							if (iRole2.getVipLevel() != iRole1.getVipLevel()) {
								return iRole2.getVipLevel() - iRole1.getVipLevel();
							} else {
								return o1.getId().compareTo(o2.getId());
							}
						}
					}
				});
				for (FactionMember fm : factionMembers) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					int passMinute = (int) (DateUtil.compareTime(new Date(), iRole.getLoginTime()) / 60000);
					list.add(new FactionMemberView(fm.getId(), iRole.getRoleId(), iRole.getName(),
							iRole.getHeadImage(), iRole.getLevel(), fm.getDutyId(), iRole.getVipLevel(), fm
									.getContribution(), passMinute, fm.getHonor(), iRole.getFactionControler()
									.getCanAllotItemNum(), fm.getDemandItem()));
				}
				int levelUpExp = XsgFactionManager.getInstance().getLevelUpExp(myFaction.getLevel());
				FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(myFaction.getLevel());
				FactionView view = new FactionView(myFaction.getSubId(), myFaction.getIcon(), myFaction.getName(),
						myFaction.getLevel(), myFaction.getExp(), levelUpExp, myFaction.getQQ(), myFaction
								.getAnnounce(), myFaction.getJoinType(), myFaction.getJoinLevel(), levelT.people, 0, 0,
						0, 0, list.toArray(new FactionMemberView[0]), myFaction.getJoinVip(), myFaction.getManifesto(),
						false, false, myFaction.getDeleteDay());
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
	}
}
