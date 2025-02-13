package com.morefun.XSanGo.worldboss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AMD_WorldBoss_getCountRank;
import com.XSanGo.Protocol.AMD_WorldBoss_getHarmRank;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.WorldBossChallengeView;
import com.XSanGo.Protocol.WorldBossRank;
import com.XSanGo.Protocol.WorldBossRankView;
import com.XSanGo.Protocol.WorldBossState;
import com.XSanGo.Protocol.WorldBossTailAward;
import com.XSanGo.Protocol.WorldBossView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.ArenaAwardLog;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleWorldBoss;
import com.morefun.XSanGo.db.game.WorldBoss;
import com.morefun.XSanGo.event.protocol.IWorldBossBuyCd;
import com.morefun.XSanGo.event.protocol.IWorldBossBuyInspire;
import com.morefun.XSanGo.event.protocol.IWorldBossEndChallenge;
import com.morefun.XSanGo.event.protocol.IWorldBossTailAward;
import com.morefun.XSanGo.event.protocol.IWorldBossTrust;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.PVEMovieParam;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class WorldBossControler implements IWorldBossControler {
	private IRole iRole;
	private Role role;
	private ItemView[] itemView;
	private Date beginDate;// 开始挑战时间
	private IWorldBossEndChallenge endChallengeEvent;
	private IWorldBossBuyCd worldBossBuyCdEvent;
	private IWorldBossBuyInspire worldBossBuyInspire;
	private IWorldBossTailAward worldBossTailAwardEvent;
	private String fightMovieIdContext;
	private IWorldBossTrust worldBossTrustEvent;

	public WorldBossControler(IRole rt, Role db) {
		this.iRole = rt;
		this.role = db;
		if (role.getWorldBoss() == null) {
			RoleWorldBoss roleWorldBoss = new RoleWorldBoss(GlobalDataManager.getInstance().generatePrimaryKey(), role,
					null, 0, 0);
			role.setWorldBoss(roleWorldBoss);
		}
		endChallengeEvent = iRole.getEventControler().registerEvent(IWorldBossEndChallenge.class);
		worldBossBuyCdEvent = iRole.getEventControler().registerEvent(IWorldBossBuyCd.class);
		worldBossBuyInspire = iRole.getEventControler().registerEvent(IWorldBossBuyInspire.class);
		worldBossTailAwardEvent = iRole.getEventControler().registerEvent(IWorldBossTailAward.class);
		worldBossTrustEvent = iRole.getEventControler().registerEvent(IWorldBossTrust.class);
	}

	@Override
	public WorldBossView getWorldBossView() throws NoteException {
		WorldBossConfT confT = WorldBossManager.getInstance().getWorldBossConfT();
		if (iRole.getLevel() < confT.openLevel) {
			throw new NoteException(
					TextUtil.format(Messages.getString("WorldBossControler.openLevel"), confT.openLevel));
		}
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		if (worldBoss == null) {
			worldBoss = WorldBossManager.getInstance().createWorldBoss();
		}
		boolean isChallengeTime = WorldBossManager.getInstance().isCanChallenge();
		int awaySecond = 0;// 逃跑剩余秒数
		// 可挑战又没被打死
		if (isChallengeTime && worldBoss.getBossRemainBlood() > 0) {
			Date endDate = DateUtil.joinTime(DateUtil.format(confT.endTime, "HH:mm:ss"));
			awaySecond = (int) (DateUtil.compareTime(endDate, new Date()) / 1000);
		} else {// 下次开启倒计时
			awaySecond = WorldBossManager.getInstance().getOpenSecond();
		}
		WorldBossState state = WorldBossState.CanChallenge;
		if (worldBoss.getBossRemainBlood() <= 0) {
			state = WorldBossState.Death;
		} else if (!isChallengeTime) {
			state = WorldBossState.Away;
		}
		RoleWorldBoss rw = role.getWorldBoss();
		WorldBossCustomsT customsT = WorldBossManager.getInstance().getWorldBossCustomsT(worldBoss.getCustomsId());
		int cd = 0;
		if (rw.getChallengeDate() != null) {
			int passSecond = (int) ((System.currentTimeMillis() - rw.getChallengeDate().getTime()) / 1000);
			cd = customsT.cd - passSecond;
		}
		// 托管者id
		List<String> trustIds = TextUtil.stringToList(worldBoss.getParticipatorIds());
		boolean isTrust = trustIds.contains(iRole.getRoleId());
		int bossLevel = (int) (worldBoss.getBossSumBlood() / customsT.monsterTs.get(0).hp + 40);

		WorldBossTrustT trustT1 = WorldBossManager.getInstance().getTrustTByLevel(iRole.getLevel());
		WorldBossTrustT trustT2 = WorldBossManager.getInstance().getTrustTByCombatPower(iRole.getCombatPower());
		List<ItemView> iv1 = splitItem(trustT1.items1);
		List<ItemView> iv2 = splitItem(trustT2.items2);
		for (ItemView i : iv1) {
			boolean exist = false;
			for (ItemView j : iv2) {
				if (i.templateId.equals(j.templateId)) {
					j.num += i.num;
					exist = true;
				}
			}
			if (!exist) {
				iv2.add(i);
			}
		}

		int returnYuanbao = 0;
		if (isTrust) {
			returnYuanbao = (int) (rw.getTrustYuanbao() * confT.returnSale);
		}
		if (rw.getTrustRefreshDate() == null || !DateUtil.isSameDay(new Date(), rw.getTrustRefreshDate())) {
			rw.setTrustNum(0);
			rw.setTrustRefreshDate(new Date());
		}
		WorldBossView view = new WorldBossView(worldBoss.getBossId(), bossLevel, state, awaySecond,
				worldBoss.getBossRemainBlood(), worldBoss.getBossSumBlood(), cd, rw.getInspireType(),
				rw.getInspireValue(), WorldBossManager.getInstance().getRoleMaxHarm(iRole.getRoleId()), customsT.name,
				customsT.scene, customsT.position, customsT.joinIt, WorldBossManager.getInstance()
						.getWorldBossTailAward(), isTrust, trustT1.yuanbao1 + trustT2.yuanbao2,
				iv2.toArray(new ItemView[0]), returnYuanbao, confT.trustNum - rw.getTrustNum());
		return view;
	}

	private List<ItemView> splitItem(String itemStr) {
		List<ItemView> items = new ArrayList<ItemView>();
		if (!TextUtil.isBlank(itemStr)) {
			for (String idNum : itemStr.split(",")) {
				items.add(new ItemView("", ItemType.DefaultItemType, idNum.split(":")[0], NumberUtil.parseInt(idNum
						.split(":")[1]), ""));
			}
		}
		return items;
	}

	@Override
	public void getHarmRank(AMD_WorldBoss_getHarmRank cb) throws NoteException {
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getHarmRankJson(), WorldBossRank[].class);
		WorldBossRankView view = getHarmRankView(ranks, iRole.getRoleId());
		cb.ice_response(LuaSerializer.serialize(view));
	}

	/**
	 * 获取roleId的伤害排行榜View
	 * 
	 * @return
	 */
	private WorldBossRankView getHarmRankView(WorldBossRank[] ranks, String roleId) {
		int myRank = 1;
		long myHarm = 0;
		for (int i = 0; i < ranks.length; i++) {
			WorldBossRank r = ranks[i];
			if (r.roleId.equals(roleId)) {
				myHarm = r.count;
				break;
			}
			myRank++;
		}
		// 取前10
		int length = Math.min(10, ranks.length);
		WorldBossRank[] topRanks = new WorldBossRank[length];
		for (int i = 0; i < length; i++) {
			WorldBossRank r = ranks[i];
			topRanks[i] = r;
		}
		WorldBossRankView view = new WorldBossRankView(topRanks, myHarm, myRank);
		return view;
	}

	@Override
	public void getCountRank(final AMD_WorldBoss_getCountRank cb) throws NoteException {
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getCountRankJson(), WorldBossRank[].class);
		int myRank = 1;
		long myCount = 0;
		for (int i = 0; i < ranks.length; i++) {
			WorldBossRank r = ranks[i];
			if (r.roleId.equals(iRole.getRoleId())) {
				myCount = r.maxHarm;
				break;
			}
			myRank++;
		}
		// 取前10
		int length = Math.min(10, ranks.length);
		WorldBossRank[] showRnaks = new WorldBossRank[length];
		for (int i = 0; i < length; i++) {
			WorldBossRank r = ranks[i];
			showRnaks[i] = r;
		}
		WorldBossRankView view = new WorldBossRankView(showRnaks, myCount, myRank);
		cb.ice_response(LuaSerializer.serialize(view));
	}

	@Override
	public void buyInspire() throws NoteException {
		WorldBossConfT confT = WorldBossManager.getInstance().getWorldBossConfT();
		if (!DateUtil.checkTimeRange(new Date(), confT.beginTime, confT.endTime)) {
			throw new NoteException(Messages.getString("WorldBossControler.notBuy"));
		}
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		if (worldBoss.getBossRemainBlood() <= 0) {
			throw new NoteException(Messages.getString("WorldBossControler.notBuy"));
		}
		if (role.getWorldBoss().getInspireValue() != 0) {
			throw new NoteException(Messages.getString("FactionControler.notRepeatBuy"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, confT.inspireYuanbao));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		role.getWorldBoss().setInspireType(confT.inspireType);
		role.getWorldBoss().setInspireValue(confT.inspireValue);
		WorldBossManager.getInstance().addInspireRole(iRole.getRoleId());

		worldBossBuyInspire.onBuyInspire(confT.inspireYuanbao);
	}

	@Override
	public void clearCd() throws NoteException {
		WorldBossConfT confT = WorldBossManager.getInstance().getWorldBossConfT();
		if (!DateUtil.checkTimeRange(new Date(), confT.beginTime, confT.endTime)) {
			throw new NoteException(Messages.getString("WorldBossControler.notBuy"));
		}
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		if (worldBoss.getBossRemainBlood() <= 0) {
			throw new NoteException(Messages.getString("WorldBossControler.notBuy"));
		}
		int cd = 0;
		if (role.getWorldBoss().getChallengeDate() != null) {
			int passSecond = (int) ((System.currentTimeMillis() - role.getWorldBoss().getChallengeDate().getTime()) / 1000);
			cd = WorldBossManager.getInstance().getWorldBossCustomsT(worldBoss.getCustomsId()).cd - passSecond;
		}
		if (cd <= 0) {
			throw new NoteException(Messages.getString("WorldBossControler.notCd"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, confT.cdYuanbao));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		role.getWorldBoss().setChallengeDate(null);

		worldBossBuyCdEvent.onBuyCd(confT.cdYuanbao);
	}

	@Override
	public WorldBossChallengeView beginChallenge() throws NoteException {
		WorldBossView bossView = getWorldBossView();
		if (bossView.state == WorldBossState.Death) {
			throw new NoteException(Messages.getString("WorldBossControler.bossDeath"));
		}
		if (bossView.state == WorldBossState.Away) {
			throw new NoteException(Messages.getString("WorldBossControler.notChallenge"));
		}
		if (bossView.isTrust) {
			throw new NoteException(Messages.getString("WorldBossControler.isTrustNotBegin"));
		}
		int cd = 0;
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		if (role.getWorldBoss().getChallengeDate() != null) {
			int passSecond = (int) ((System.currentTimeMillis() - role.getWorldBoss().getChallengeDate().getTime()) / 1000);
			cd = WorldBossManager.getInstance().getWorldBossCustomsT(worldBoss.getCustomsId()).cd - passSecond;
		}
		if (cd > 0) {
			throw new NoteException(Messages.getString("WorldBossControler.cdNotChallenge"));
		}
		WorldBossCustomsT customsT = WorldBossManager.getInstance().getWorldBossCustomsT(
				WorldBossManager.getInstance().worldBoss.getCustomsId());
		List<ItemView> items = splitItem(customsT.items);
		// 使用鼓舞
		role.getWorldBoss().setInspireType(0);
		role.getWorldBoss().setInspireValue(0);
		// 开始计算CD
		role.getWorldBoss().setChallengeDate(new Date());
		WorldBossManager.getInstance().removeInspireRole(iRole.getRoleId());

		beginDate = new Date();

		// 战报
		PVEMovieParam param = new PVEMovieParam(String.valueOf(worldBoss.getBossId()), "");
		fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(Type.WorldBoss, iRole, param);

		this.itemView = items.toArray(new ItemView[0]);
		WorldBossChallengeView view = new WorldBossChallengeView(bossView.id, bossView.blood, bossView.sumBlood,
				bossView.addType, bossView.addValue, this.itemView, fightMovieIdContext);
		return view;
	}

	@Override
	public boolean endChallenge(int harm, int heroNum) throws NoteException {
		if (this.itemView == null) {
			throw new NoteException("not began challenge");
		}
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		boolean isDeath = worldBoss.getBossRemainBlood() <= 0;
		// 不能超过最高伤害
		long maxHarm = (long) worldBoss.getBossSumBlood() * WorldBossManager.getInstance().getWorldBossConfT().maxHarm
				/ 10000;
		harm = (int) Math.min(harm, maxHarm);
		// 验证是否有效 如果部队未阵亡 BOSS未打死时长需要1分30秒
		if (heroNum > 0 && worldBoss.getBossRemainBlood() - harm > 0) {
			int second = (int) (DateUtil.compareTime(new Date(), beginDate) / 1000);
			if (second < 80 || harm < 0) {
				LogManager.warn("WorldBossControler.challengeError " + iRole.getRoleId());
				throw new NoteException(Messages.getString("WorldBossControler.challengeError"));
			}
		}
		maxHarm = WorldBossManager.getInstance().getMaxHarm(2, iRole.getLevel());
		if (harm < 0 || harm > maxHarm) {
			LogManager.warn("WorldBossControler.challengeError " + iRole.getRoleId() + " " + harm);
			throw new NoteException(Messages.getString("WorldBossControler.challengeError"));
		}

		worldBoss.setBossRemainBlood(worldBoss.getBossRemainBlood() - harm);
		if (worldBoss.getBossRemainBlood() < 0) {
			worldBoss.setBossRemainBlood(0);
		}
		WorldBossManager.getInstance().addHarmRank(harm, iRole);
		iRole.getRewardControler().acceptReward(itemView);
		itemView = null;
		calculateTailAward();

		// 处理BOSS死亡 之前没死，之后被打死
		if (!isDeath && worldBoss.getBossRemainBlood() <= 0) {
			worldBoss.setBossDeathDate(new Date());
			// 延迟2分钟结算 考虑到有玩家没结束战斗
			LogicThread.scheduleTask(new DelayedTask(120000) {

				@Override
				public void run() {
					WorldBossManager.getInstance().sendAward();
				}
			});
		}

		// 通知客户端刷新排行榜
		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getHarmRankJson(), WorldBossRank[].class);
		for (WorldBossRank r : ranks) {
			WorldBossRankView rankView = getHarmRankView(ranks, r.roleId);
			IRole rt = XsgRoleManager.getInstance().findRoleById(r.roleId);
			if (rt != null) {
				ChatCallbackPrx call = rt.getChatControler().getChatCb();
				if (call != null) {
					call.begin_refreshHarmRank(LuaSerializer.serialize(rankView));
				}
			}
		}

		// 运营活动
		WorldBossActivityT activityT = WorldBossManager.getInstance().getWorldBossActivityT();
		if (DateUtil.isBetween(new Date(), activityT.beginDate, activityT.endDate)) {
			RoleWorldBoss roleWorldBoss = role.getWorldBoss();
			if (roleWorldBoss.getGainItemDate() == null
					|| !DateUtil.isSameDay(new Date(), roleWorldBoss.getGainItemDate())) {
				roleWorldBoss.setGainItemNum(0);
			}
			if (roleWorldBoss.getGainItemNum() < activityT.itemNum) {
				// 邮件发放奖励
				MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
						.get(MailTemplate.WorldBossActivity.value());
				Property[] pro = new Property[] { new Property(activityT.itemId, 1) };
				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
								iRole.getRoleId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
										.serializeMailAttach(pro), Calendar.getInstance().getTime()));
				roleWorldBoss.setGainItemDate(new Date());
				roleWorldBoss.setGainItemNum(roleWorldBoss.getGainItemNum() + 1);
			}
		}
		XsgFightMovieManager.getInstance().endFightMovieWithExtra(iRole.getRoleId(), fightMovieIdContext,
				heroNum > 0 ? 1 : 0, (byte) heroNum,
				TextUtil.format("{0},{1},{2}", worldBoss.getCustomsId(), worldBoss.getBossId(), harm));
		this.endChallengeEvent.onEndChallenge(harm);
		return true;
	}

	/**
	 * 计算保存尾刀奖励者
	 * 
	 * @param roleId
	 */
	private void calculateTailAward() {
		WorldBoss worldBoss = WorldBossManager.getInstance().worldBoss;
		TailAward[] existAwards = TextUtil.GSON.fromJson(worldBoss.getTailAwardJson(), TailAward[].class);
		List<TailAward> list = new ArrayList<TailAward>();
		for (TailAward t : existAwards) {
			list.add(t);
		}
		// 怪物血量百分比
		int scale = (int) Math.ceil(worldBoss.getBossRemainBlood() / (double) worldBoss.getBossSumBlood() * 100);
		// 循环获取所有尾刀，可跨尾刀获取
		for (WorldBossTailT tailT : WorldBossManager.getInstance().getWorldBossTailTs()) {
			if (scale <= tailT.hp && !isExistAward(existAwards, tailT.hp)) {
				list.add(new TailAward(tailT.hp, iRole.getRoleId(), false));
				// 尾刀公告
				List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
						XsgChatManager.AdContentType.WorldBossTail);
				if (adTList != null && adTList.size() > 0) {
					ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
					if (adt.onOff == 1) {
						AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(tailT.items[0].templateId);
						XsgChatManager.getInstance().sendAnnouncement(
								iRole.getChatControler().parseAdContentItem(itemT, adt.content));
					}
				}
				// 事件
				worldBossTailAwardEvent.onWorldBossTailAward(tailT.hp);
			}
		}
		if (list.size() > existAwards.length) {
			worldBoss.setTailAwardJson(TextUtil.GSON.toJson(list.toArray(new TailAward[0])));
			WorldBossManager.getInstance().saveWorldBoss();
		}
	}

	/**
	 * 判断是否尾刀奖励是否存在
	 * 
	 * @param existAwards
	 * @param hp
	 * @return
	 */
	private boolean isExistAward(TailAward[] existAwards, int hp) {
		for (TailAward t : existAwards) {
			if (t.hp == hp) {// 尾刀已被别人拿走
				return true;
			}
		}
		return false;
	}

	@Override
	public void getTailAward(int hp) throws NoteException {
		WorldBossView view = getWorldBossView();
		WorldBossTailAward award = null;
		for (WorldBossTailAward wa : view.tailAward) {
			if (hp == wa.hp) {
				award = wa;
				break;
			}
		}
		if (award == null) {
			throw new NoteException("params error");
		}
		if (!award.roleId.equals(iRole.getRoleId())) {
			throw new NoteException(Messages.getString("WorldBossControler.awardIsOther"));
		}
		if (award.isReceive) {
			throw new NoteException(Messages.getString("WorldBossControler.notRepeatReceive"));
		}
		TailAward[] awards = TextUtil.GSON.fromJson(WorldBossManager.getInstance().worldBoss.getTailAwardJson(),
				TailAward[].class);
		for (TailAward t : awards) {
			if (t.hp == hp) {
				t.isReceive = true;
				WorldBossManager.getInstance().worldBoss.setTailAwardJson(TextUtil.GSON.toJson(awards));
				WorldBossManager.getInstance().saveWorldBoss();
				break;
			}
		}
		iRole.getRewardControler().acceptReward(award.items);

		Map<String, String> awardSub = new HashMap<String, String>();
		awardSub.put("hp", String.valueOf(hp));
		awardSub.put("roleId", iRole.getRoleId());
		ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 4, TextUtil.GSON.toJson(awardSub));
		XsgArenaRankManager.getInstance().saveAwardLogAsync(awardlog);
	}

	@Override
	public void trust() throws NoteException {
		WorldBossConfT confT = WorldBossManager.getInstance().getWorldBossConfT();
		Calendar now = Calendar.getInstance();
		now.setTime(confT.endTime);
		now.add(Calendar.MINUTE, 5);
		if (DateUtil.checkTimeRange(new Date(), confT.beginTime, now.getTime())) {
			throw new NoteException(Messages.getString("WorldBossControler.openedNotTrust"));
		}
		WorldBossView view = getWorldBossView();
		if (view.isTrust) {
			throw new NoteException(Messages.getString("WorldBossControler.repeatTrust"));
		}
		RoleWorldBoss rw = role.getWorldBoss();
		if (role.getWorldBoss().getTrustNum() >= confT.trustNum) {
			throw new NoteException(Messages.getString("WorldBossControler.trustFull"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, view.trustYuanbao));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		List<String> ids = TextUtil.stringToList(WorldBossManager.getInstance().worldBoss.getParticipatorIds());
		ids.add(iRole.getRoleId());
		rw.setTrustYuanbao(view.trustYuanbao);
		rw.setTrustItems(TextUtil.GSON.toJson(view.trustItems));
		rw.setTrustNum(rw.getTrustNum() + 1);
		WorldBossManager.getInstance().worldBoss.setParticipatorIds(TextUtil.join(ids, ","));

		WorldBossManager.getInstance().saveWorldBoss();
		worldBossTrustEvent.onTrust(0, -view.trustYuanbao);
	}

	@Override
	public void cancelTrust() throws NoteException {
		List<String> ids = TextUtil.stringToList(WorldBossManager.getInstance().worldBoss.getParticipatorIds());
		if (!ids.contains(iRole.getRoleId())) {
			throw new NoteException(Messages.getString("WorldBossControler.notTrust"));
		}
		WorldBossConfT confT = WorldBossManager.getInstance().getWorldBossConfT();
		ids.remove(iRole.getRoleId());
		int returnYuanbao = (int) (role.getWorldBoss().getTrustYuanbao() * confT.returnSale);
		try {
			iRole.winYuanbao(returnYuanbao, true);
		} catch (NotEnoughYuanBaoException e) {
		}
		role.getWorldBoss().setTrustYuanbao(0);
		role.getWorldBoss().setTrustItems("[]");
		WorldBossManager.getInstance().worldBoss.setParticipatorIds(TextUtil.join(ids, ","));

		WorldBossManager.getInstance().saveWorldBoss();
		worldBossTrustEvent.onTrust(1, returnYuanbao);
	}

	@Override
	public void sendTrstAward() {
		ItemView[] is = TextUtil.GSON.fromJson(role.getWorldBoss().getTrustItems(), ItemView[].class);
		role.getWorldBoss().setTrustYuanbao(0);
		role.getWorldBoss().setTrustItems("[]");

		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		for (ItemView i : is) {
			rewardMap.put(i.templateId, i.num);
		}
		XsgMailManager.getInstance().sendTemplate(iRole.getRoleId(), MailTemplate.WorldBossTrust, rewardMap, null);
	}
}
