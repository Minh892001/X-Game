package com.morefun.XSanGo.worldboss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.WorldBossRank;
import com.XSanGo.Protocol.WorldBossTailAward;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.ArenaAwardLog;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.db.game.WorldBoss;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * 世界BOSS管理
 * 
 * @author xiongming.li
 *
 */
public class WorldBossManager {

	private static WorldBossManager instance = new WorldBossManager();

	public static WorldBossManager getInstance() {
		return instance;
	}

	public WorldBoss worldBoss;

	private WorldBossConfT confT;

	private Map<Integer, WorldBossCustomsT> customsMap = new HashMap<Integer, WorldBossCustomsT>();

	private List<WorldBossHurtRankT> hurtRankTs;

	private List<WorldBossTailT> tailTs;

	private WorldBossActivityT activityT;

	/**
	 * 最高伤害配置
	 */
	private List<MaxHarmConfT> harmConfTs;

	/**
	 * 购买鼓舞没用掉的玩家
	 */
	private List<String> buyInspireRole = new ArrayList<String>();

	/**
	 * 托管配置
	 */
	private List<WorldBossTrustT> trustTs = new ArrayList<WorldBossTrustT>();

	public WorldBossManager() {
		SimpleDAO simpleDAO = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<WorldBoss> worldBosses = simpleDAO.findAll(WorldBoss.class);
		if (!worldBosses.isEmpty()) {
			worldBoss = worldBosses.get(0);
		}

		confT = ExcelParser.parse(WorldBossConfT.class).get(0);
		List<WorldBossCustomsT> cList = ExcelParser.parse(WorldBossCustomsT.class);
		for (WorldBossCustomsT c : cList) {
			String[] its = c.joinItems.split(",");
			c.joinIt = new ItemView[its.length];
			for (int j = 0; j < its.length; j++) {
				String[] id_num = its[j].split(":");
				c.joinIt[j] = new ItemView("", ItemType.DefaultItemType, id_num[0], NumberUtil.parseInt(id_num[1]), "");
			}
			customsMap.put(c.id, c);
		}

		List<WorldBossMonsterT> mList = ExcelParser.parse(WorldBossMonsterT.class);
		for (WorldBossMonsterT m : mList) {
			WorldBossCustomsT c = customsMap.get(m.customsId);
			if (c != null) {
				c.monsterTs.add(m);
			}
		}

		hurtRankTs = ExcelParser.parse(WorldBossHurtRankT.class);
		tailTs = ExcelParser.parse(WorldBossTailT.class);
		for (WorldBossTailT t : tailTs) {
			String[] its = t.itemIds.split(",");
			t.items = new ItemView[its.length];
			for (int j = 0; j < t.items.length; j++) {
				String[] id_num = its[j].split(":");
				t.items[j] = new ItemView("", ItemType.DefaultItemType, id_num[0], NumberUtil.parseInt(id_num[1]), "");
			}
		}
		activityT = ExcelParser.parse(WorldBossActivityT.class).get(0);
		harmConfTs = ExcelParser.parse(MaxHarmConfT.class);
		trustTs = ExcelParser.parse(WorldBossTrustT.class);

		createWorldBoss();

		// 公告定时器
		Calendar beginDate = Calendar.getInstance();
		beginDate.setTime(confT.beginTime);
		beginDate.add(Calendar.MINUTE, -15);
		long delayed = DateUtil.betweenTaskHourMillis(beginDate.get(Calendar.HOUR_OF_DAY),
				beginDate.get(Calendar.MINUTE));
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					if (!isOpenWeedDay()) {
						return;
					}
					// 这里再次检查上次重置是否出现异常
					if (worldBoss.getBossSumBlood() != worldBoss.getBossRemainBlood()) {
						resetWorldBoss();
					}

					List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
							XsgChatManager.AdContentType.WorldBossOpen10);
					if (adTList != null && adTList.size() > 0) {
						final ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
						if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
							XsgChatManager.getInstance().sendAnnouncement(chatAdT.content);
						}
						LogicThread.scheduleTask(new DelayedTask(600000) {// 10分钟后执行

									@Override
									public void run() {
										List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
												XsgChatManager.AdContentType.WorldBossOpen5);
										if (adTList != null && adTList.size() > 0) {
											ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
											if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
												XsgChatManager.getInstance().sendAnnouncement(chatAdT.content);
											}
										}
									}
								});
					}
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});

		// 重置BOSS定时器
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(confT.endTime);
		delayed = DateUtil.betweenTaskHourMillis(endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE));
		delayed += TimeUnit.MINUTES.toMillis(5);// 延迟5分钟重置
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				try {
					// 没开启就不重置了
					if (!isOpenWeedDay()) {
						return;
					}
					if (worldBoss.getBossRemainBlood() > 0) {
						sendAward();
					}
					resetWorldBoss();
				} catch (Exception e) {
					LogManager.error(e);
				}
			}
		});
	}

	/**
	 * 初始化boss数据
	 * 
	 * @return
	 */
	public WorldBoss createWorldBoss() {
		if (this.worldBoss == null) {
			List<Integer> ids = new ArrayList<Integer>();
			for (Integer id : customsMap.keySet()) {
				ids.add(id);
			}
			// 随机获取关卡ID
			int customsId = ids.get(NumberUtil.random(0, ids.size()));
			WorldBossMonsterT monsterT = customsMap.get(customsId).monsterTs.get(0);
			long bossHp = (long) (monsterT.hp);
			this.worldBoss = new WorldBoss(GlobalDataManager.getInstance().generatePrimaryKey(), customsId,
					monsterT.id, 0, bossHp, bossHp);
			saveWorldBoss();
		}
		return this.worldBoss;
	}

	/**
	 * 结算后重置boss
	 * 
	 * @return
	 */
	public void resetWorldBoss() {
		// 发放未领取的尾刀奖
		WorldBossTailAward[] tailAward = getWorldBossTailAward();
		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.WorldBossTail.value());
		for (WorldBossTailAward a : tailAward) {
			if (!a.isReceive && TextUtil.isNotBlank(a.roleId)) {
				Property[] pro = new Property[a.items.length];
				for (int j = 0; j < pro.length; j++) {
					pro[j] = new Property(a.items[j].templateId, a.items[j].num);
				}
				String hp = String.valueOf(a.hp);
				// 邮件发放奖励
				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
								a.roleId, mailRewardT.title, mailRewardT.body.replace("$m", hp), XsgMailManager
										.getInstance().serializeMailAttach(pro), Calendar.getInstance().getTime()));
			}
		}

		List<Integer> ids = new ArrayList<Integer>();
		for (Integer id : customsMap.keySet()) {
			ids.add(id);
		}
		// 随机获取关卡ID
		int customsId = ids.get(NumberUtil.random(0, ids.size()));
		WorldBossMonsterT monsterT = customsMap.get(customsId).monsterTs.get(0);
		worldBoss.setBossId(monsterT.id);
		worldBoss.setCustomsId(monsterT.customsId);
		long bossHp = 0;
		if (worldBoss.getBossRemainBlood() <= 0) {
			Date beginDate = DateUtil.joinTime(DateUtil.format(confT.beginTime, "HH:mm:ss"));
			int deathSecond = (int) (DateUtil.compareTime(worldBoss.getBossDeathDate(), beginDate) / 1000);
			bossHp = (long) (worldBoss.getBossSumBlood() * 1200 / deathSecond);
		} else {
			bossHp = (long) (worldBoss.getBossSumBlood() * 1200 / 1800);
		}
		if (bossHp <= 0) {
			bossHp = (long) monsterT.hp;
		}
		if (bossHp >= Integer.MAX_VALUE) {// 客户端不支持超过int最大值
			bossHp = Integer.MAX_VALUE - 1;
		}
		worldBoss.setBossSumBlood(bossHp);
		worldBoss.setBossRemainBlood(bossHp);
		worldBoss.setHarmRankJson("[]");
		worldBoss.setTailAwardJson("[]");
		saveWorldBoss();
	}

	/**
	 * 返还购买鼓舞未使用的元宝
	 */
	public void returnBuyInspireYuanbao() {
		for (String rid : buyInspireRole) {
			MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
					.get(MailTemplate.WorldBossInspireReturn.value());
			Property[] pro = new Property[] { new Property(Const.PropertyName.RMBY, confT.inspireYuanbao) };
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, rid,
							mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance().serializeMailAttach(pro),
							Calendar.getInstance().getTime()));
		}
		if (!buyInspireRole.isEmpty()) {
			XsgRoleManager.getInstance().loadRoleAsync(buyInspireRole, new Runnable() {

				@Override
				public void run() {
					for (String rid : buyInspireRole) {
						IRole role = XsgRoleManager.getInstance().findRoleById(rid);
						if (role != null) {
							role.getRoleWorldBoss().setInspireType(0);
							role.getRoleWorldBoss().setInspireValue(0);
						}
					}
					buyInspireRole = new ArrayList<String>();
				}
			});
		}
	}

	/**
	 * 异步保存boss数据
	 */
	public void saveWorldBoss() {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDAO = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				simpleDAO.attachDirty(worldBoss);
			}
		});
	}

	/**
	 * 添加伤害排行榜
	 * 
	 * @param addHarm
	 * @return
	 */
	public WorldBossRank[] addHarmRank(int addHarm, IRole iRole) {
		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getHarmRankJson(), WorldBossRank[].class);
		if (ranks == null) {
			ranks = new WorldBossRank[0];
		}
		List<WorldBossRank> listRank = new ArrayList<WorldBossRank>();
		for (WorldBossRank r : ranks) {
			listRank.add(r);
		}

		WorldBossRank rank = null;
		for (WorldBossRank r : ranks) {
			if (r.roleId.equals(iRole.getRoleId())) {
				rank = r;
				break;
			}
		}
		if (rank == null) {
			rank = new WorldBossRank(iRole.getRoleId(), iRole.getName(), iRole.getHeadImage(), iRole.getLevel(),
					iRole.getVipLevel(), addHarm, addHarm);
			listRank.add(rank);
			sortHarmRank(listRank);
		} else if (addHarm > 0) {
			rank.count += addHarm;
			rank.maxHarm += addHarm;
			rank.roleName = iRole.getName();
			rank.vipLevel = iRole.getVipLevel();
			rank.level = iRole.getLevel();
			sortHarmRank(listRank);
		}

		ranks = listRank.toArray(new WorldBossRank[0]);
		worldBoss.setHarmRankJson(TextUtil.GSON.toJson(ranks));

		saveWorldBoss();
		return ranks;
	}

	/**
	 * 伤害排行榜排序
	 * 
	 * @param harmRank
	 */
	private void sortHarmRank(List<WorldBossRank> harmRank) {
		Collections.sort(harmRank, new Comparator<WorldBossRank>() {

			@Override
			public int compare(WorldBossRank o1, WorldBossRank o2) {
				int r = (int) (o2.count - o1.count);
				if (r == 0) {
					r = o1.roleId.compareTo(o2.roleId);
				}
				return r;
			}
		});
	}

	/**
	 * 发放伤害排行榜奖励
	 */
	public void sendAward() {
		// 发奖记录
		Map<String, Map<String, String>> awardMap = new HashMap<String, Map<String, String>>();

		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getHarmRankJson(), WorldBossRank[].class);
		// 伤害排行榜奖励
		int length = Math.min(10, ranks.length);

		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.WorldBossRank.value());
		for (int i = 0; i < length; i++) {
			WorldBossRank rank = ranks[i];
			WorldBossHurtRankT awardT = hurtRankTs.get(i);
			if (awardT == null) {
				continue;
			}
			// 保存奖励 日志
			Map<String, String> awardSub = new HashMap<String, String>();
			awardSub.put("items", awardT.items);
			awardSub.put("rank", String.valueOf(i + 1));
			awardMap.put(rank.roleId, awardSub);

			String[] its = awardT.items.split(",");
			Property[] pro = new Property[its.length];
			for (int j = 0; j < pro.length; j++) {
				String[] id_num = its[j].split(":");
				pro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
			}
			// 邮件发放奖励
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
							rank.roleId, mailRewardT.title, mailRewardT.body.replace("$m", String.valueOf(i + 1)),
							XsgMailManager.getInstance().serializeMailAttach(pro), Calendar.getInstance().getTime()));
		}
		// 更新上次伤害排行榜
		worldBoss.setCountRankJson(TextUtil.GSON.toJson(ranks));

		// 发放参与奖
		String joinItems = customsMap.get(worldBoss.getCustomsId()).joinItems;
		String[] its = joinItems.split(",");
		Property[] pro = new Property[its.length];
		for (int j = 0; j < pro.length; j++) {
			String[] id_num = its[j].split(":");
			pro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
		}
		mailRewardT = XsgMailManager.getInstance().getMailRewardTList().get(MailTemplate.WorldBossJoin.value());
		for (int i = 0; i < ranks.length; i++) {
			WorldBossRank rank = ranks[i];
			// 邮件发放奖励
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
							rank.roleId, mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
									.serializeMailAttach(pro), Calendar.getInstance().getTime()));
		}

		ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 3, TextUtil.GSON.toJson(awardMap));
		XsgArenaRankManager.getInstance().saveAwardLogAsync(awardlog);

		// 发放托管奖
		final List<String> ids = TextUtil.stringToList(worldBoss.getParticipatorIds());
		worldBoss.setParticipatorIds("");
		XsgRoleManager.getInstance().loadRoleAsync(ids, new Runnable() {

			@Override
			public void run() {
				for (String id : ids) {
					IRole r = XsgRoleManager.getInstance().findRoleById(id);
					if (r != null) {
						r.getWorldBossControler().sendTrstAward();
					}
				}
			}
		});

		saveWorldBoss();
		returnBuyInspireYuanbao();
		sendWorldBossNotice();
	}

	/**
	 * 世界BOSS活动结束公告
	 */
	public void sendWorldBossNotice() {
		List<ChatAdT> adTList = null;
		if (worldBoss.getBossRemainBlood() <= 0) {// boss死亡
			// 死亡公告
			adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.WorldBossDeath);
		} else {
			// 未死亡公告
			adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.WorldBossNotDeath);
		}
		if (adTList != null && adTList.size() > 0) {
			ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
			if (adt.onOff == 1) {
				XsgChatManager.getInstance().sendAnnouncement(adt.content);
			}
		}
	}

	/**
	 * 获取尾刀奖励View
	 * 
	 * @return
	 */
	public WorldBossTailAward[] getWorldBossTailAward() {
		WorldBossTailAward[] tailAwards = new WorldBossTailAward[tailTs.size()];
		TailAward[] awards = TextUtil.GSON.fromJson(worldBoss.getTailAwardJson(), TailAward[].class);
		for (int i = 0; i < tailTs.size(); i++) {
			WorldBossTailT tailT = tailTs.get(i);
			for (TailAward t : awards) {
				if (t.hp == tailT.hp) {
					tailAwards[i] = new WorldBossTailAward(tailT.hp, t.roleId, t.isReceive, tailT.items);
					break;
				}
			}
			if (tailAwards[i] == null) {
				tailAwards[i] = new WorldBossTailAward(tailT.hp, "", false, tailT.items);
			}
		}
		return tailAwards;
	}

	/**
	 * 获取尾刀奖励模版
	 * 
	 * @return
	 */
	public List<WorldBossTailT> getWorldBossTailTs() {
		return this.tailTs;
	}

	/**
	 * 获取玩家本轮世界BOSS总伤害
	 * 
	 * @param roleId
	 * @return
	 */
	public long getRoleMaxHarm(String roleId) {
		WorldBossRank[] ranks = TextUtil.GSON.fromJson(worldBoss.getHarmRankJson(), WorldBossRank[].class);
		for (WorldBossRank r : ranks) {
			if (r.roleId.equals(roleId)) {
				return r.count;
			}
		}
		return 0;
	}

	/**
	 * 判断当前时间是否能挑战世界BOSS
	 * 
	 * @return
	 */
	public boolean isCanChallenge() {
		if (isOpenWeedDay() && DateUtil.checkTimeRange(new Date(), confT.beginTime, confT.endTime)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前时间是否能进入世界BOSS界面
	 * 
	 * @return
	 */
	public boolean isEnterble() {
		if (isOpenWeedDay() && DateUtil.checkTimeRange(new Date(), confT.enterbleDate, confT.endTime)) {
			return true;
		}
		return false;
	}

	/**
	 * 当天是否开启世界BOSS
	 * 
	 * @return
	 */
	public boolean isOpenWeedDay() {
		return isOpenWeedDay(Calendar.getInstance());
	}

	/**
	 * 指定日期是否开启世界BOSS
	 * 
	 * @return
	 */
	public boolean isOpenWeedDay(Calendar date) {
		int weekDay = date.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekDay == 0) {
			weekDay = 7;
		}
		List<String> openWeekDay = TextUtil.stringToList(confT.openWorkDay);
		if (openWeekDay.contains(String.valueOf(weekDay))) {
			return true;
		}
		return false;
	}

	/**
	 * 世界BOSS开启倒计时秒
	 * 
	 * @return
	 */
	public int getOpenSecond() {
		Calendar now = Calendar.getInstance();
		Calendar beginDate = Calendar.getInstance();
		beginDate.setTime(DateUtil.joinTime(DateUtil.toString(confT.beginTime.getTime(), "HH:mm:ss")));
		if (now.after(beginDate)) {
			beginDate.add(Calendar.DATE, 1);
		}
		int count = 0;
		while (!isOpenWeedDay(beginDate) && count <= 7) {
			count++;
			beginDate.add(Calendar.DATE, 1);
		}
		int second = (int) ((beginDate.getTimeInMillis() - now.getTimeInMillis()) / 1000);
		return second;
	}

	/**
	 * 获取活动配置
	 * 
	 * @return
	 */
	public WorldBossActivityT getWorldBossActivityT() {
		return this.activityT;
	}

	/**
	 * 添加购买鼓舞玩家
	 * 
	 * @param roleId
	 */
	public void addInspireRole(String roleId) {
		if (!this.buyInspireRole.contains(roleId)) {
			this.buyInspireRole.add(roleId);
		}
	}

	/**
	 * 移除购买鼓舞玩家
	 * 
	 * @param roleId
	 */
	public void removeInspireRole(String roleId) {
		if (this.buyInspireRole.contains(roleId)) {
			this.buyInspireRole.remove(roleId);
		}
	}

	public WorldBossConfT getWorldBossConfT() {
		return confT;
	}

	public WorldBossCustomsT getWorldBossCustomsT(int customsId) {
		return customsMap.get(customsId);
	}

	public IWorldBossControler createWorldBossControler(IRole rt, Role db) {
		return new WorldBossControler(rt, db);
	}

	/**
	 * 获取最高伤害
	 * 
	 * @param type
	 * @param level
	 * @return
	 */
	public int getMaxHarm(int type, int level) {
		for (MaxHarmConfT m : this.harmConfTs) {
			if (m.type == type) {
				String[] levels = m.level.split(",");
				if (level >= NumberUtil.parseInt(levels[0]) && level <= NumberUtil.parseInt(levels[1])) {
					return m.maxHarm;
				}
			}
		}
		return 6000000;
	}

	/**
	 * 根据等级获取托管模板
	 * 
	 * @param level
	 * @return
	 */
	public WorldBossTrustT getTrustTByLevel(int level) {
		for (int i = 0; i < this.trustTs.size(); i++) {
			if (i == trustTs.size() - 1) {
				return trustTs.get(i);
			}
			if (level >= trustTs.get(i).level && level < trustTs.get(i + 1).level) {
				return trustTs.get(i);
			}
		}
		return null;
	}

	/**
	 * 根据战斗力获取托管模板
	 * 
	 * @param combatPower
	 * @return
	 */
	public WorldBossTrustT getTrustTByCombatPower(int combatPower) {
		for (int i = 0; i < this.trustTs.size(); i++) {
			if (i == trustTs.size() - 1) {
				return trustTs.get(i);
			}
			if (combatPower >= trustTs.get(i).power && combatPower < trustTs.get(i + 1).power) {
				return trustTs.get(i);
			}
		}
		return null;
	}

}
