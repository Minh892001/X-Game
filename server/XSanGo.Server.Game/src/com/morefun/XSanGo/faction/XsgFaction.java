package com.morefun.XSanGo.faction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.FactionAllotLog;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.AsynSaver;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.IAsynSavable;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.Faction;
import com.morefun.XSanGo.db.game.FactionBattle;
import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.db.game.FactionCopy;
import com.morefun.XSanGo.db.game.FactionHistory;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class XsgFaction implements IFaction, IAsynSavable {

	private Faction db;
	private DelayedTask saveTimer;
	private AsynSaver saver;

	public XsgFaction(Faction db) {
		this.db = db;
		this.scheduleSaveTimer();
		this.saver = new AsynSaver(this);
	}

	@Override
	public String getId() {
		return this.db.getId();
	}

	@Override
	public String getIcon() {
		return this.db.getIcon();
	}

	@Override
	public String getName() {
		return this.db.getName();
	}

	@Override
	public int getLevel() {
		return this.db.getLevel();
	}

	@Override
	public String getQQ() {
		return this.db.getQqNum();
	}

	@Override
	public int getMemberSize() {
		return this.db.getMembers().size();
	}

	@Override
	public int getExp() {
		return this.db.getExp();
	}

	@Override
	public String getAnnounce() {
		return this.db.getAnnouncement();
	}

	@Override
	public void approveReq(IRole candidate) {
		candidate.getFactionControler().setFaction(this.getId());
		Date time = Calendar.getInstance().getTime();
		// 如果存在垃圾数据先移除掉
		for (FactionMember m : this.db.getMembers()) {
			if (m.getRoleId().equals(candidate.getRoleId())) {
				this.db.getMembers().remove(m);
				break;
			}
		}
		this.db.getMembers().add(
				new FactionMember(GlobalDataManager.getInstance().generatePrimaryKey(), this.db,
						Const.Faction.DUTY_JUNIOR, candidate.getRoleId(), candidate.getName(), candidate.getLevel(),
						time, candidate.getLogoutTime()));

		// 欢迎[成员名]正式加入本帮。
		this.addHistory(candidate, Messages.getString("XsgFaction.0"), candidate); //$NON-NLS-1$

		candidate.getFormationControler().formationChange(candidate.getFormationControler().getDefaultFormation());
	}

	@Override
	public void saveAsyn() {
		this.saver.saveAsyn();
	}

	/**
	 * 定时保存数据到数据库
	 */
	private void scheduleSaveTimer() {
		int interval = 10 * 60 * 1000;// N分钟
		// int interval = 1 * 60 * 1000;// N分钟
		this.saveTimer = new DelayedTask(interval, interval) {

			@Override
			public void run() {
				saveAsyn();
			}
		};
		LogicThread.scheduleTask(this.saveTimer);
	}

	@Override
	public boolean hasApproveNewMemberDuty(String roleId) {
		// 暂时是帮主和长老
		FactionMember m = getMemberByRoleId(roleId);
		if (m.getDutyId() >= Const.Faction.DUTY_ELDER) {
			return true;
		}
		return false;
	}

	@Override
	public void addHistory(IRole targetRole, String remark, IRole sponsor) {
		List<FactionHistory> histories = this.db.getHistories();
		if (targetRole != null) {
			histories.add(0, new FactionHistory(GlobalDataManager.getInstance().generatePrimaryKey(), this.db,
					targetRole.getName(), remark, Calendar.getInstance().getTime(), targetRole.getVipLevel()));
		} else {
			histories.add(0, new FactionHistory(GlobalDataManager.getInstance().generatePrimaryKey(), this.db, "",
					remark, Calendar.getInstance().getTime(), 0));
		}
		FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
		if (sponsor != null) {
			// 同步动态到公会聊天频道
			List<IRole> receiveRoles = XsgRoleManager.getInstance().findChatAcceptorList(sponsor, ChatChannel.Faction);
			for (IRole r : receiveRoles) {
				ChatRole cRole = new ChatRole();
				cRole.id = "0";
				// cRole.name = "系统";
				cRole.name = Messages.getString("XsgFaction.6");
				cRole.vip = 0;
				cRole.chatTime = DateUtil.toString(System.currentTimeMillis(), "HH:mm"); //$NON-NLS-1$
				String content = remark;
				if (targetRole != null) {
					content = XsgChatManager.getInstance().orgnizeRoleText(targetRole) + remark;
				}
				content = XsgChatManager.getInstance().orgnizeColorText(content);
				TextMessage msg = new TextMessage(ChatChannel.Faction, null, cRole, 1, content, 0, 0);
				r.getChatControler().messageReceived("0", Messages.getString("XsgFaction.6"), msg);
			}
		}
		while (histories.size() > configT.historyNum) {
			FactionHistory min = Collections.min(histories, new Comparator<FactionHistory>() {
				@Override
				public int compare(FactionHistory o1, FactionHistory o2) {
					long distance = o1.getCreateTime().getTime() - o2.getCreateTime().getTime();
					return (int) distance;
				}
			});

			histories.remove(min);
		}
	}

	@Override
	public FactionMember getMemberByRoleId(final String roleId) {
		for (FactionMember m : this.db.getMembers()) {
			if (m.getRoleId().equals(roleId)) {
				return m;
			}
		}
		return null;
	}

	@Override
	public void removeMember(FactionMember member, IRole role) {
		this.db.getMembers().remove(member);
		XsgFactionManager.getInstance().removeMemberRank(member.getRoleId());
		XsgFactionManager.getInstance().removeApplyMember(member.getRoleId());

		// removeItemQueue(member.getRoleId());

		if (role != null) {
			role.getFormationControler().formationChange(role.getFormationControler().getDefaultFormation());
		}
	}

	@Override
	public void bossLeft() {
		if (this.getMemberSize() > 0) {
			FactionMember newBoss = this.findNewBoss();
			newBoss.setDutyId(Const.Faction.DUTY_BOSS);
		} else {
			this.db.setState(Faction.STATE_DISBAND);
			// XsgFactionManager.instance().removeFaction(this.getId());
		}
	}

	/**
	 * 根据职位，帮贡，加入时间等条件获取最适合的继位人选
	 * 
	 * @return
	 */
	private FactionMember findNewBoss() {
		FactionMember[] memberArray = this.db.getMembers().toArray(new FactionMember[] {});
		Arrays.sort(memberArray, MemberComparator.getInstance());

		return memberArray[0];
	}

	@Override
	public void save(byte[] data) {
		XsgFactionManager.getInstance().saveFaction((Faction) TextUtil.bytesToObject(data));
	}

	@Override
	public Set<FactionMember> getAllMember() {
		return this.db.getMembers();
	}

	@Override
	public void setAnnouncement(String announcement) {
		this.db.setAnnouncement(LuaSerializer.removeSpecialLuaString(announcement));
	}

	@Override
	public void setBossId(String roleId) {
		this.db.setBossId(roleId);
	}

	@Override
	public void saveConfig(String icon, String qq, String notice, int joinType, int joinLevel, int joinVip,
			String manifesto, int deleteDay) {
		this.db.setIcon(icon);
		this.db.setQqNum(qq);
		this.db.setJoinType(joinType);
		setAnnouncement(notice);
		this.db.setJoinLevel(joinLevel);
		this.db.setJoinVip(joinVip);
		setManifesto(manifesto);
		this.db.setDeleteDay(deleteDay);
	}

	@Override
	public int getJoinType() {
		return this.db.getJoinType();
	}

	@Override
	public List<FactionHistory> getAllHistory() {
		return this.db.getHistories();
	}

	@Override
	public String getSubId() {
		if (GlobalDataManager.getInstance().isMergeServer()) {
			String[] ids = this.db.getId().split("-");
			if (ids.length == 3) {
				return NumberUtil.parseInt(ids[1]) % 100000 + "-" + ids[2];
			}
		} else {
			String[] ids = this.db.getId().split("-");
			if (ids.length == 3) {
				return ids[2];
			}
		}
		return this.db.getId();
	}

	@Override
	public String getBossId() {
		return this.db.getBossId();
	}

	@Override
	public void addExp(int num, String roleId) {
		this.db.setExp(this.db.getExp() + num * 20);
		FactionMember member = getMemberByRoleId(roleId);
		// 增加贡献
		member.setContribution(member.getContribution() + num * 20);
		member.setLastTime(new Date());

		int maxLevel = XsgFactionManager.getInstance().getFactionMaxLevel();
		// 判断是否升级
		if (this.db.getLevel() < maxLevel) {
			FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(this.db.getLevel());
			if (this.db.getExp() >= levelT.exp) {
				this.db.setLevel(this.db.getLevel() + 1);
				// 减去升级所需经验
				this.db.setExp(this.db.getExp() - levelT.exp);
				String content = TextUtil.format(Messages.getString("XsgFaction.8"), //$NON-NLS-1$
						this.db.getLevel());
				addHistory(null, content, XsgRoleManager.getInstance().findRoleById(roleId));
			}
		} else {
			FactionLevelT levelT = XsgFactionManager.getInstance().getFactionLevelT(maxLevel);
			if (this.db.getExp() > levelT.exp) {
				this.db.setExp(levelT.exp);
			}
		}
	}

	@Override
	public byte[] cloneData() {
		return TextUtil.objectToBytes(this.db);
	}

	@Override
	public FactionCopy getFactionCopy() {
		for (FactionCopy f : db.getCopys()) {
			return f;
		}
		return null;
	}

	@Override
	public void setFactionCopy(FactionCopy factionCopy) {
		Set<FactionCopy> copys = new HashSet<FactionCopy>();
		if (factionCopy != null) {
			factionCopy.setFaction(this.db);
			copys.add(factionCopy);
		}
		db.setCopys(copys);
	}

	@Override
	public Date getLastOpenCopyTime() {
		return this.db.getLastOpenCopyTime();
	}

	@Override
	public int getOpenCopyNum() {
		return this.db.getOpenCopyNum();
	}

	@Override
	public void setLastOpenCopyTime(Date date) {
		this.db.setLastOpenCopyTime(date);
	}

	@Override
	public void setOpenCopyNum(int num) {
		this.db.setOpenCopyNum(num);
	}

	@Override
	public Date getCreateTime() {
		return this.db.getCreateTime();
	}

	@Override
	public String getShopJson() {
		return this.db.getShopJson();
	}

	@Override
	public Date getShopRefreshDate() {
		return this.db.getShopRefreshDate();
	}

	@Override
	public int getFactionHonor() {
		return this.db.getHonor();
	}

	@Override
	public void setFactionHonor(int honor) {
		this.db.setHonor(honor);
	}

	/**
	 * 刷新商品
	 */
	// @Override
	// public FactionShop[] refreshShop() {
	// // 更新刷新时间
	// this.db.setShopRefreshDate(new Date());
	// // 清空公会成员购买记录
	// for (FactionMember m : getAllMember()) {
	// m.setBuyShopIds("");
	// }
	// return null;
	// }

	@Override
	public void setShopJson(String json) {
		this.db.setShopJson(json);
	}

	@Override
	public int getApplyPeople() {
		int people = 0;
		for (FactionMember fm : getAllMember()) {
			if (fm.getApplyDate() != null && DateUtil.isSameDay(new Date(), fm.getApplyDate())) {
				people++;
			}
		}
		return people;
	}

	@Override
	public Date getRenameDate() {
		return db.getRenameDate();
	}

	@Override
	public void setRenameDate(Date date) {
		db.setRenameDate(date);
	}

	@Override
	public void setName(String name) {
		db.setName(name);
	}

	@Override
	public int getJoinLevel() {
		return db.getJoinLevel();
	}

	@Override
	public int getSendMailTimes() {
		return db.getSendMailTimes();
	}

	@Override
	public void setSendMailTimes(int times) {
		db.setSendMailTimes(times);
	}

	@Override
	public void setSendMailDate(Date date) {
		db.setSendMailDate(date);
	}

	@Override
	public Date getSendMailDate() {
		return db.getSendMailDate();
	}

	@Override
	public String getMailLogs() {
		return db.getMailLogs();
	}

	@Override
	public void setMailLogs(String mailLogs) {
		db.setMailLogs(mailLogs);
	}

	@Override
	public String getManifesto() {
		return db.getManifesto();
	}

	@Override
	public void setManifesto(String manifesto) {
		db.setManifesto(LuaSerializer.removeSpecialLuaString(manifesto));
	}

	@Override
	public int getJoinVip() {
		return db.getJoinVip();
	}

	@Override
	public void setJoinVip(int joinVip) {
		db.setJoinVip(joinVip);
	}

	@Override
	public String getWarehouseData() {
		return TextUtil.isBlank(db.getWarehouseData()) ? "[]" : db.getWarehouseData();
	}

	@Override
	public void setWarehouseData(String warehouseData) {
		db.setWarehouseData(warehouseData);
	}

	@Override
	public String getOviStoreData() {
		return db.getOviStoreData();
	}

	@Override
	public void setOviStoreData(String oviStoreData) {
		db.setOviStoreData(oviStoreData);
	}

	@Override
	public int getScore() {
		return db.getScore();
	}

	@Override
	public void setScore(int score) {
		db.setScore(score);
	}

	@Override
	public int getStudyNum() {
		return db.getStudyNum();
	}

	@Override
	public void setStudyNum(int studyNum) {
		db.setStudyNum(studyNum);
	}

	@Override
	public String getTechnologyData() {
		if (TextUtil.isBlank(db.getTechnologyData())) {
			return "[]";
		}
		return db.getTechnologyData();
	}

	@Override
	public void setTechnologyData(String technologyData) {
		db.setTechnologyData(technologyData);
	}

	@Override
	public void putWarehouseItem(ItemView... itemViews) {
		String[] itemTemplateIds = null;
		int[] counts = null;
		for (ItemView iv : itemViews) {
			itemTemplateIds = (String[]) ArrayUtils.add(itemTemplateIds, iv.templateId);
			counts = ArrayUtils.add(counts, iv.num);
		}
		putWarehouseItem(itemTemplateIds, counts);
	}

	@Override
	public void putWarehouseItem(String itemTemplateId, int count) {
		putWarehouseItem(new String[] { itemTemplateId }, new int[] { count });
	}

	/**
	 * 放入公会仓库
	 * 
	 * @param itemTemplateIds
	 * @param counts
	 */
	private void putWarehouseItem(String[] itemTemplateIds, int[] counts) {
		initWarehouseItem();
		int warehouseLevel = getWarehouseLevel();
		WarehouseItemBean[] existItems = TextUtil.GSON.fromJson(getWarehouseData(), WarehouseItemBean[].class);
		List<WarehouseItemBean> listItems = new ArrayList<WarehouseItemBean>(Arrays.asList(existItems));

		for (int i = 0; i < itemTemplateIds.length; i++) {
			boolean exist = false;
			for (WarehouseItemBean item : listItems) {
				if (itemTemplateIds[i].equals(item.itemId)) {
					item.itemNum += counts[i];
					exist = true;
					break;
				}
			}
			if (!exist) {
				int volume = XsgFactionManager.getInstance().getFactionWarehouseTByLevel(warehouseLevel).volume;
				if (existItems.length < volume) {// 有容量
					listItems.add(new WarehouseItemBean(0, itemTemplateIds[i], counts[i], ""));
				}
			}
		}
		setWarehouseData(TextUtil.GSON.toJson(listItems.toArray(new WarehouseItemBean[0])));
	}

	@Override
	public void initWarehouseItem() {
		WarehouseItemBean[] items = TextUtil.GSON.fromJson(getWarehouseData(), WarehouseItemBean[].class);
		List<WarehouseItemBean> itemList = new ArrayList<WarehouseItemBean>();

		for (WarehouseItemBean it : items) {
			if (it.itemNum <= 0) {
				continue;
			}
			itemList.add(it);
		}
		setWarehouseData(TextUtil.GSON.toJson(itemList.toArray(new WarehouseItemBean[0])));

		// WarehouseItemBean[] items = null;
		// if (TextUtil.isNotBlank(getWarehouseData())) {
		// items = TextUtil.GSON.fromJson(getWarehouseData(),
		// WarehouseItemBean[].class);
		// } else {
		// items = new WarehouseItemBean[0];
		// }
		// List<WarehouseItemBean> existList = new
		// ArrayList<WarehouseItemBean>(Arrays.asList(items));
		//
		// List<FactionWarehouseItemT> itemT =
		// XsgFactionManager.getInstance().getWarehouseItem();
		// for (FactionWarehouseItemT it : itemT) {
		// boolean exist = false;
		// for (WarehouseItemBean i : existList) {
		// if (i.itemId.equals(it.itemId)) {
		// exist = true;
		// if (i.id == 0) {
		// i.id = it.id;
		// setWarehouseData(TextUtil.GSON.toJson(existList.toArray(new
		// WarehouseItemBean[0])));
		// }
		// break;
		// }
		// }
		// // 不存在
		// if (!exist) {
		// existList.add(new WarehouseItemBean(it.id, it.itemId, 0, ""));
		// Collections.sort(existList, new Comparator<WarehouseItemBean>() {
		//
		// @Override
		// public int compare(WarehouseItemBean o1, WarehouseItemBean o2) {
		// return o1.id - o2.id;
		// }
		// });
		// setWarehouseData(TextUtil.GSON.toJson(existList.toArray(new
		// WarehouseItemBean[0])));
		// }
		// }
	}

	@Override
	public FactionBattle getFactionBattle() {
		return db.getFactionBattle();
	}

	@Override
	public FactionBattleMember getFactionBattleMember(String roleId) {
		return db.getFactionBattleMember() == null ? null : db.getFactionBattleMember().get(roleId);
	}

	@Override
	public Map<String, FactionBattleMember> getAllFactionBattleMember() {
		return db.getFactionBattleMember();
	}

	@Override
	public void resetFactionBattle() {
		db.setFactionBattle(null);
		db.getFactionBattleMember().clear();
	}

	@Override
	public void setFactionBattle(FactionBattle factionBattle) {
		factionBattle.setFaction(db);
		db.setFactionBattle(factionBattle);
	}

	@Override
	public void addFactionBattleMember(FactionBattleMember fbm) {
		Map<String, FactionBattleMember> fbms = db.getFactionBattleMember();
		if (fbms == null) {
			fbms = new HashMap<String, FactionBattleMember>();
			db.setFactionBattleMember(fbms);
		}
		fbm.setFaction(db);
		fbms.put(fbm.getRoleId(), fbm);
	}

	@Override
	public int getWarehouseLevel() {
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(getTechnologyData(), FactionTechnologyBean[].class);
		for (FactionTechnologyBean b : exist) {
			if (b.id == 104) {
				return b.level;
			}
		}
		return 1;
	}

	@Override
	public int getStorehouseLevel() {
		FactionTechnologyBean[] exist = TextUtil.GSON.fromJson(getTechnologyData(), FactionTechnologyBean[].class);
		for (FactionTechnologyBean b : exist) {
			if (b.id == 105) {
				return b.level;
			}
		}
		return 1;
	}

	@Override
	public void onTechnologyLevelUp() {
		for (FactionMember fm : getAllMember()) {
			IRole r = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
			if (r != null && r.isOnline()) {
				r.getFormationControler().formationChange(r.getFormationControler().getDefaultFormation());
			}
		}
	}

	@Override
	public void addAllotLog(FactionAllotLog log) {
		// 增加分配日志
		FactionAllotLog[] logs = TextUtil.GSON.fromJson(getAllotLog(), FactionAllotLog[].class);
		List<FactionAllotLog> listLog = new ArrayList<FactionAllotLog>(Arrays.asList(logs));

		listLog.add(0, log);
		if (listLog.size() > 50) {
			listLog.remove(listLog.size() - 1);
		}
		setAllotLog(TextUtil.GSON.toJson(listLog.toArray(new FactionAllotLog[0])));
	}

	@Override
	public String getAllotLog() {
		return TextUtil.isBlank(db.getAllotLog()) ? "[]" : db.getAllotLog();
	}

	@Override
	public void setAllotLog(String allotLogs) {
		db.setAllotLog(allotLogs);
	}

	@Override
	public int getRecommendNum() {
		return db.getRecommendNum();
	}

	@Override
	public void setRecommendNum(int recommendNum) {
		db.setRecommendNum(recommendNum);
	}

	@Override
	public Date getRecommendRefreshDate() {
		return db.getRecommendRefreshDate();
	}

	@Override
	public void setRecommendRefreshDate(Date recommendRefreshDate) {
		db.setRecommendRefreshDate(recommendRefreshDate);
	}

	@Override
	public String getPurchaseLogs() {
		return db.getPurchaseLogs();
	}

	@Override
	public void setPurchaseLogs(String purchaseLogs) {
		db.setPurchaseLogs(purchaseLogs);
	}

	@Override
	public void removeItemQueue(String roleId) {
		WarehouseItemBean[] items = TextUtil.GSON.fromJson(getWarehouseData(), WarehouseItemBean[].class);
		for (WarehouseItemBean i : items) {
			List<String> queue = TextUtil.stringToList(i.queue);
			if (queue.contains(roleId)) {
				queue.remove(roleId);
				i.queue = TextUtil.join(queue, ",");
				setWarehouseData(TextUtil.GSON.toJson(items));
			}
		}
	}

	@Override
	public int getDeleteDay() {
		return db.getDeleteDay();
	}

	@Override
	public int getRecruitNum() {
		return db.getRecruitNum();
	}

	@Override
	public void setRecruitNum(int recruitNum) {
		db.setRecruitNum(recruitNum);
	}

	@Override
	public Date getRefreshRecruitDate() {
		return db.getRefreshRecruitDate();
	}

	@Override
	public void setRefreshRecruitDate(Date refreshRecruitDate) {
		db.setRefreshRecruitDate(refreshRecruitDate);
	}

	@Override
	public void setJoinType(int joinType) {
		db.setJoinType(joinType);
	}
}

class RandomShopReward implements IRandomHitable {
	public int id;
	public int rank;

	public RandomShopReward(int id, int rank) {
		this.id = id;
		this.rank = rank;
	}

	@Override
	public int getRank() {
		return rank;
	}

	/**
	 * 出现概率设置为0, 使他不会被随机出来
	 * */
	public void disappear() {
		this.rank = 0;
	}
}

class MemberComparator implements Comparator<FactionMember> {
	private static final MemberComparator instance = new MemberComparator();

	public static MemberComparator getInstance() {
		return instance;
	}

	private MemberComparator() {
	}

	@Override
	public int compare(FactionMember o1, FactionMember o2) {
		// 职位＞帮贡＞角色进入帮会时间。
		int first = o2.getDutyId() - o1.getDutyId();// 降序
		first = first > 0 ? 1 : first == 0 ? 0 : -1;
		int second = o2.getContribution() - o1.getContribution();// 降序
		second = second > 0 ? 1 : second == 0 ? 0 : -1;
		int third = o1.getParticipateTime().compareTo(o2.getParticipateTime());// 升序

		return first * 1000 + second * 100 + third * 10;
	}
}
