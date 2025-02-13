package com.morefun.XSanGo.timeBattle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.BattleChallengeResultView;
import com.XSanGo.Protocol.BattleTimesView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.SceneDuelView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.battle.DuelBattle;
import com.morefun.XSanGo.battle.DuelReport;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.copy.MonsterT;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleTimeBattle;
import com.morefun.XSanGo.event.protocol.ITimeBattle;
import com.morefun.XSanGo.event.protocol.ITimeBattleBegin;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.PVEMovieParam;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class TimeBattleControler implements ITimeBattleControler {

	private IRole roleRt;
	private Role roleDb;
	private BattleTimesView[] times;
	/**
	 * 每次挑战的物品缓存
	 */
	private Map<Integer, ItemView[]> itemViews = new HashMap<Integer, ItemView[]>();
	// private ItemView[] itemViews;// 挑战关卡获得的奖励
	private int includeHeroNum;// 上阵武将个数
	private Map<String, Integer> heroExpMap;// 武将经验
	private BattleTimesView battleTimesView;
	private ITimeBattleBegin timeBattleBeginEvent;
	private ITimeBattle timeBattleEvent;

	private int junling;// 需要扣除的军令
	private int lastChallengeId;// 最后挑战的关卡ID
	// 是否发送过红点
	private boolean isSendRedPoint = false;
	// 战报ID上下文缓存
	private String fightMovieIdContext;

	public TimeBattleControler(IRole iRole, Role role) {
		this.roleRt = iRole;
		this.roleDb = role;
		this.times = XsgTimeBattleManage.getInstance().getChallengeTimes();
		this.timeBattleEvent = this.roleRt.getEventControler().registerEvent(ITimeBattle.class);
		this.timeBattleBeginEvent = this.roleRt.getEventControler().registerEvent(ITimeBattleBegin.class);
		// 处理可挑战次数
		for (BattleTimesView b : times) {
			RoleTimeBattle battle = this.roleDb.getTimeBattle().get(b.id);
			if (battle != null) {
				b.num = b.num - battle.getChallengeTimes();
			}
		}
	}

	@Override
	public BattleTimesView[] getChallengeTimes() throws NoteException {
		// 是否重置
		if (this.roleDb.getLastTimeBattleDate() == null
				|| DateUtil.isPass(XsgTimeBattleManage.getInstance().configT.resetTime, "HH:mm:ss",
						this.roleDb.getLastTimeBattleDate())) {
			this.times = XsgTimeBattleManage.getInstance().getChallengeTimes();
			for (RoleTimeBattle b : roleDb.getTimeBattle().values()) {
				b.setChallengeTimes(0);
			}
			// this.roleDb.setTimeBattle(new HashMap<Integer,
			// RoleTimeBattle>());
			this.roleDb.setLastTimeBattleDate(new Date());
		}

		for (BattleTimesView b : this.times) {
			RoleTimeBattle bt = this.roleDb.getTimeBattle().get(b.id);
			if (bt != null) {
				b.star = bt.getMaxStar();
			}
			if (bt != null && bt.getLastPassDate() != null) {
				// 已经过去的秒数
				int pastSecond = (int) (DateUtil.compareTime(new Date(), bt.getLastPassDate()) / 1000);
				TimeBattleT battleT = XsgTimeBattleManage.getInstance().getTimeBattleTById(b.id);
				if (pastSecond >= battleT.cdMinute * 60) {
					b.time = 0;
				} else {
					b.time = battleT.cdMinute * 60 - pastSecond;
				}
			}
		}
		return this.times;
	}

	@Override
	public BattleChallengeResultView beginChallenge(String formationId, int id, boolean isClear) throws NoteException {
		TimeBattleT timeBattleT = XsgTimeBattleManage.getInstance().getTimeBattleTById(id);
		// 验证部队是否正确
		IFormation formation = this.roleRt.getFormationControler().getFormation(formationId);
		if (formation == null) {
			throw new NoteException(Messages.getString("TimeBattleControler.1"));
		}
		if (!this.passCdTime(id) && !isClear) {
			throw new NoteException(Messages.getString("TimeBattleControler.2"));
		}
		for (BattleTimesView b : times) {
			if (b.id == id) {
				battleTimesView = b;
				break;
			}
		}
		if (battleTimesView == null) {
			throw new NoteException(Messages.getString("TimeBattleControler.3"));
		}
		if (battleTimesView.num <= 0) {
			throw new NoteException(Messages.getString("TimeBattleControler.4"));
		}
		if (this.roleDb.getLevel() < timeBattleT.level) {
			throw new NoteException(
					Messages.getString("TimeBattleControler.5") + timeBattleT.level + Messages.getString("TimeBattleControler.6")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (this.roleRt.getItemControler().getItemCountInPackage(Const.PropertyName.JUNLING_TEMPLATE_ID) < timeBattleT.junling) {
			throw new NoteException(Messages.getString("TimeBattleControler.7")); //$NON-NLS-1$
		}
		lastChallengeId = id;
		// 扣除1军令
		this.roleRt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, -1);
		if (!this.itemViews.containsKey(id)) {
			// 执行TC生成奖励物品
			TcResult tcResult = XsgRewardManager.getInstance().doTc(this.roleRt, timeBattleT.tc);
			// 增加和军令一样的经验
			int exp = timeBattleT.junling;
			if (roleRt.isDoubleExp()) {
				exp += exp;
			}
			tcResult.appendProperty(Const.PropertyName.EXP, exp);
			this.itemViews.put(id, XsgRewardManager.getInstance().generateItemView(tcResult));
		}
		this.includeHeroNum = formation.getHeroCountIncludeSupport();
		this.heroExpMap = XsgRewardManager.getInstance().calculateHeroExp(formation.getHeros());
		this.junling = timeBattleT.junling - 1;
		SceneDuelView[] reports = this.tryGenerateDuelReport(formation, timeBattleT);
		PVEMovieParam param = new PVEMovieParam(id + "", "");
		fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(Type.TimeBattle, roleRt, param);

		timeBattleBeginEvent.onBattleBegin(id, isClear, 1);
		return generateRewardView(reports);
	}

	/**
	 * 生成奖励品的视图信息，这里的物品表示广义物品，包括金钱经验等
	 * 
	 * @param ccr
	 * @param duelReport
	 * @return
	 */
	private BattleChallengeResultView generateRewardView(SceneDuelView[] reports) {
		// 武将经验
		List<Property> heroExpList = new ArrayList<Property>();
		for (String heroId : this.heroExpMap.keySet()) {
			heroExpList.add(new Property(heroId, this.heroExpMap.get(heroId)));
		}
		return new BattleChallengeResultView(heroExpList.toArray(new Property[0]), this.itemViews.get(lastChallengeId),
				reports, fightMovieIdContext);
	}

	@Override
	public int endChallenge(int heroNum, boolean isClear) throws NoteException {
		if (heroNum <= 0) {// 挑战失败
			// this.itemViews.remove(lastChallengeId);
			return 0;
		}
		if (this.itemViews.get(lastChallengeId) == null) {
			throw new NoteException(Messages.getString("TimeBattleControler.8")); //$NON-NLS-1$
		}
		if (heroNum > this.includeHeroNum) {
			throw new NoteException(Messages.getString("TimeBattleControler.9")); //$NON-NLS-1$
		}
		if (battleTimesView.num <= 0) {
			throw new NoteException(Messages.getString("TimeBattleControler.4"));
		}
		battleTimesView.num--;
		// 更新可挑战次数到数据库
		RoleTimeBattle battle = this.roleDb.getTimeBattle().get(battleTimesView.id);
		if (battle == null) {
			battle = new RoleTimeBattle(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
					battleTimesView.id, 1);
			this.roleDb.getTimeBattle().put(battleTimesView.id, battle);
		} else {
			battle.setChallengeTimes(battle.getChallengeTimes() + 1);
			if (battleTimesView.num > 0 && !isClear) {
				// 更新关卡最后挑战通过时间 用来控制时间CD
				battle.setLastPassDate(new Date());
			}
		}
		roleRt.getNotifyControler().setAutoNotify(false);
		// 扣除军令
		this.roleRt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, -junling);
		ItemView[] ivs = this.itemViews.get(lastChallengeId);
		this.roleRt.getRewardControler().acceptReward(ivs);
		this.roleRt.getHeroControler().winHeroExp(this.heroExpMap);
		roleRt.getNotifyControler().setAutoNotify(true);

		this.itemViews.remove(lastChallengeId);
		this.heroExpMap = null;
		int star = XsgCopyManager.getInstance().calculateStar((byte) this.includeHeroNum, (byte) heroNum);
		if (heroNum > 0) {
			XsgFightMovieManager.getInstance()
					.endFightMovie(roleRt.getRoleId(), fightMovieIdContext, 1, (byte) heroNum);
		} else {
			XsgFightMovieManager.getInstance().anomalyEndFightMovie(roleRt.getRoleId(), fightMovieIdContext, 0,
					(byte) heroNum);
		}
		// 如果得到了紫装就发送公告
		for (ItemView i : ivs) {
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(i.templateId);
			if (itemT != null
					&& itemT.getColor().ordinal() > QualityColor.Blue.ordinal()
					&& (itemT.getItemType() == ItemType.EquipItemType || itemT.getItemType() == ItemType.FormationBuffItemType)) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.CopyEquip);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				String content = this.roleRt.getChatControler().parseAdContentItem(itemT, adT.content);
				content = parseAdContent(content, lastChallengeId);
				XsgChatManager.getInstance().sendAnnouncement(content);
			}
		}
		// 更新最大过关星级
		if (!isClear && star > battle.getMaxStar()) {
			battle.setMaxStar(star);
		}
		timeBattleEvent.onPassBattle(battleTimesView.id, isClear, junling);
		return star;
	}

	/**
	 * 组织不同难度关卡文字
	 * 
	 * @param content
	 * @param timeBattleId
	 * @return
	 */
	private String parseAdContent(String content, int timeBattleId) {
		TimeBattleT timeBattleT = XsgTimeBattleManage.getInstance().getTimeBattleTById(timeBattleId);
		if (timeBattleT.diff == 0) {// 简单
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_GREEN).replace("~diff_type~", "新手");
		} else if (timeBattleT.diff == 1) {// 高手
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_BLUE).replace("~diff_type~", "高手");
		} else {// 大神
			content = content.replace("~color~", Const.Chat.CHAT_COLOR_PURPLE).replace("~diff_type~", "大神");
		}
		content = content.replace("~copy_name~", timeBattleT.name);
		return content;
	}

	@Override
	public void failChallenge() throws NoteException {
		XsgFightMovieManager.getInstance().anomalyEndFightMovie(roleRt.getRoleId(), fightMovieIdContext, 0, (byte) 0);
	}

	/**
	 * 进行单挑匹配，成功则返回相应战报，未触发则直接返回NULL
	 * 
	 * @param formation
	 * @param timeBattleT
	 * @return
	 */
	private SceneDuelView[] tryGenerateDuelReport(IFormation formation, TimeBattleT battleT) {
		List<SceneDuelView> list = new ArrayList<SceneDuelView>();
		// 常规单挑逻辑
		IHero hero = formation.randomDuelHero();
		if (hero == null) {
			return list.toArray(new SceneDuelView[0]);
		}

		Map<Integer, MonsterT> monsterMap = battleT.getDuelMonsterMap();
		for (int sceneSeq : monsterMap.keySet()) {
			DuelUnit first = hero.createDuelUnit();
			DuelUnit second = XsgCopyManager.getInstance().createDuelUnitFromMonster(monsterMap.get(sceneSeq));
			DuelBattle battle = new DuelBattle(first, second);
			DuelReport report = battle.fuckEachOther();
			list.add(new SceneDuelView(sceneSeq, 0, new DuelReportView[] { report.generateView() }));
		}
		return list.toArray(new SceneDuelView[0]);
	}

	/**
	 * 验证关卡是否过了CD时间
	 * 
	 * @return
	 */
	private boolean passCdTime(int id) {
		RoleTimeBattle bt = this.roleDb.getTimeBattle().get(id);
		if (bt != null) {
			TimeBattleT battleT = XsgTimeBattleManage.getInstance().getTimeBattleTById(id);
			// 已经过去的秒数
			int pastSecond = (int) (DateUtil.compareTime(new Date(), bt.getLastPassDate()) / 1000);
			if (pastSecond >= battleT.cdMinute * 60) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (isSendRedPoint) {
			return null;
		}
		try {
			BattleTimesView[] bvs = getChallengeTimes();
			for (BattleTimesView bv : bvs) {
				TimeBattleT timeBattleT = XsgTimeBattleManage.getInstance().getTimeBattleTById(bv.id);
				// 次数、等级、CD都满足才有红点
				if (bv.num > 0 && bv.time == 0 && this.roleRt.getLevel() >= timeBattleT.level) {
					isSendRedPoint = true;
					return new MajorUIRedPointNote(MajorMenu.TimeBattleMenu, true);
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void resetRedPoint() {
		this.isSendRedPoint = false;
	}

	@Override
	public int endLimitChallenge(int heroNum, String items) throws NoteException {
		if (heroNum <= 0) {// 挑战失败
			// this.itemViews.remove(lastChallengeId);
			// 设置战报结果
			XsgFightMovieManager.getInstance().anomalyEndFightMovie(roleRt.getRoleId(), fightMovieIdContext, 0,
					(byte) heroNum);
			return 0;
		}
		if (this.itemViews.get(lastChallengeId) == null) {
			throw new NoteException(Messages.getString("TimeBattleControler.10"));
		}
		if (heroNum > this.includeHeroNum) {
			throw new NoteException(Messages.getString("TimeBattleControler.11"));
		}
		if (battleTimesView.num <= 0) {
			throw new NoteException(Messages.getString("TimeBattleControler.4"));
		}
		battleTimesView.num--;
		// 更新可挑战次数到数据库
		RoleTimeBattle battle = this.roleDb.getTimeBattle().get(battleTimesView.id);
		if (battle == null) {
			battle = new RoleTimeBattle(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
					battleTimesView.id, 1);
			this.roleDb.getTimeBattle().put(battleTimesView.id, battle);
		} else {
			battle.setChallengeTimes(battle.getChallengeTimes() + 1);
			if (battleTimesView.num > 0) {
				// 更新关卡最后挑战通过时间 用来控制时间CD
				battle.setLastPassDate(new Date());
			}
		}
		roleRt.getNotifyControler().setAutoNotify(false);
		// 扣除军令
		this.roleRt.getItemControler().changeItemByTemplateCode(Const.PropertyName.JUNLING_TEMPLATE_ID, -junling);
		this.roleRt.getHeroControler().winHeroExp(this.heroExpMap);
		// 验证物品数量
		ItemView[] addItems = checkItems(items);
		this.roleRt.getRewardControler().acceptReward(addItems);
		roleRt.getNotifyControler().setAutoNotify(true);

		this.itemViews.remove(lastChallengeId);
		this.heroExpMap = null;
		int star = XsgCopyManager.getInstance().calculateStar((byte) this.includeHeroNum, (byte) heroNum);
		// 发送公告
		// 如果得到了紫装就发送公告
		for (ItemView i : addItems) {
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(i.templateId);
			if (itemT != null
					&& itemT.getColor().ordinal() > QualityColor.Blue.ordinal()
					&& (itemT.getItemType() == ItemType.EquipItemType || itemT.getItemType() == ItemType.FormationBuffItemType)) {
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.CopyEquip);
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				String content = this.roleRt.getChatControler().parseAdContentItem(itemT, adT.content);
				content = parseAdContent(content, lastChallengeId);
				XsgChatManager.getInstance().sendAnnouncement(content);
			}
		}
		// 设置战报结果
		XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(), fightMovieIdContext, 1, (byte) heroNum);
		// 更新最大过关星级
		if (star > battle.getMaxStar()) {
			battle.setMaxStar(star);
		}
		timeBattleEvent.onPassBattle(battleTimesView.id, false, junling);
		return star;
	}

	/**
	 * 验证并获取发放的物品
	 * 
	 * @param items
	 * @return
	 */
	private ItemView[] checkItems(String items) {
		List<ItemView> itemList = new ArrayList<ItemView>();
		if (!TextUtil.isBlank(items)) {
			for (String it : items.split(";")) {
				boolean exist = false;
				String[] its = it.split(",");
				String templateId = its[0];
				int num = Integer.parseInt(its[1]);
				for (ItemView i : this.itemViews.get(lastChallengeId)) {
					if (i.templateId.equals(templateId) && i.num >= num) {
						exist = true;
						itemList.add(new ItemView("", i.iType, i.templateId, num, "")); // 道具extendsProperty为空
						break;
					}
				}
				if (!exist) {
					LogManager.warn(Messages.getString("TimeBattleControler.15") + it);
					throw new IllegalStateException(Messages.getString("TimeBattleControler.16") + it);
				}
			}
		}
		// 经验和金币必得
		for (ItemView i : this.itemViews.get(lastChallengeId)) {
			if (i.templateId.equals(Const.PropertyName.EXP) || i.templateId.equals(Const.PropertyName.MONEY)) {
				itemList.add(new ItemView("", i.iType, i.templateId, i.num, ""));
			}
		}
		return itemList.toArray(new ItemView[0]);
	}

	@Override
	public CopyClearResultView clear(int id) throws NoteException {
		String code = XsgCopyManager.getInstance().getClearItemTemplate();
		if (roleRt.getItemControler().getItemCountInPackage(code) < 1) {
			throw new NoteException(Messages.getString("CopyControler.12"));
		}
		TimeBattleConfigT conf = XsgTimeBattleManage.getInstance().configT;
		if (roleRt.getLevel() < conf.clearLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("TimeBattleControler.clearLevel"),
					conf.clearLevel));
		}
		if (roleRt.getVipLevel() < conf.clearVipLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("TimeBattleControler.clearVipLevel"),
					conf.clearVipLevel));
		}
		RoleTimeBattle battle = this.roleDb.getTimeBattle().get(id);
		if (battle == null || battle.getMaxStar() < Const.MaxStar) {
			throw new NoteException(Messages.getString("CopyControler.14"));
		}
		this.beginChallenge(roleRt.getFormationControler().getDefaultFormation().getId(), id, true);
		ItemView[] ivs = this.itemViews.get(id);
		CopyClearResultView view = new CopyClearResultView(id, ivs, new ItemView[0], 0);
		this.endChallenge(1, true);
		// 扣除扫荡令
		roleRt.getRewardControler().acceptReward(code, -1);
		return view;
	}

	/**
	 * 判断指定的关卡是否可扫荡
	 * 
	 * @param id
	 * @return
	 */
	// private boolean isCanClear(int id) {
	// TimeBattleConfigT conf = XsgTimeBattleManage.getInstance().configT;
	// if (roleRt.getLevel() < conf.clearLevel) {
	// return false;
	// }
	// if (roleRt.getVipLevel() < conf.clearVipLevel) {
	// return false;
	// }
	// RoleTimeBattle battle = this.roleDb.getTimeBattle().get(id);
	// if (battle == null || battle.getMaxStar() < Const.MaxStar) {
	// return false;
	// }
	// return true;
	// }
}
