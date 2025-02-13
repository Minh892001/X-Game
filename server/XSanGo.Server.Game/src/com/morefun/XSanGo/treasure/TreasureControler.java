package com.morefun.XSanGo.treasure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.XSanGo.Protocol.AMD_Treasure_getAccidentFriend;
import com.XSanGo.Protocol.AMD_Treasure_getRescueFriend;
import com.XSanGo.Protocol.AMD_Treasure_rescue;
import com.XSanGo.Protocol.AMD_Treasure_sendRescueMsg;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RecommendHero;
import com.XSanGo.Protocol.TextMessage;
import com.XSanGo.Protocol.TreasureAccidentLog;
import com.XSanGo.Protocol.TreasureEvent;
import com.XSanGo.Protocol.TreasureFriend;
import com.XSanGo.Protocol.TreasureGroup;
import com.XSanGo.Protocol.TreasureRecourseView;
import com.XSanGo.Protocol.TreasureRescueFriend;
import com.XSanGo.Protocol.TreasureRescueLog;
import com.XSanGo.Protocol.TreasureView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleTreasure;
import com.morefun.XSanGo.db.game.RoleTreasureParam;
import com.morefun.XSanGo.event.protocol.ITreasureAddGroup;
import com.morefun.XSanGo.event.protocol.ITreasureDepart;
import com.morefun.XSanGo.event.protocol.ITreasureGain;
import com.morefun.XSanGo.event.protocol.ITreasureHelpFriend;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.sns.XsgSnsManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class TreasureControler implements ITreasureControler {
	private IRole iRole;
	private Role role;
	private ITreasureDepart treasureDepartEvent;
	private ITreasureGain treasureGainEvent;
	private ITreasureAddGroup treasureAddGroupEvent;
	private ITreasureHelpFriend treasureHelpFriend;

	/**
	 * 单次矿难已发送援救请求的好友id
	 */
	private List<String> sendRoleIds = new ArrayList<String>();

	public TreasureControler(IRole rt, Role db) {
		this.role = db;
		this.iRole = rt;
		treasureDepartEvent = iRole.getEventControler().registerEvent(ITreasureDepart.class);
		treasureGainEvent = iRole.getEventControler().registerEvent(ITreasureGain.class);
		treasureAddGroupEvent = iRole.getEventControler().registerEvent(ITreasureAddGroup.class);
		treasureHelpFriend = iRole.getEventControler().registerEvent(ITreasureHelpFriend.class);

		if (role.getRoleTreasureParam() == null) {
			role.setRoleTreasureParam(new RoleTreasureParam(GlobalDataManager.getInstance().generatePrimaryKey(), role));
			role.getRoleTreasureParam().setRescueMsg(Messages.getString("TreasureControler.defaultMsg"));
		}
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!iRole.isOnline()) {
			return null;
		}
		checkAccident();

		if (role.getRoleTreasureParam().isAccidentRedPoint()) {// 有矿难
			return new MajorUIRedPointNote(MajorMenu.TreasureMenu, true);
		}

		List<RoleTreasure> roleTreasures = new ArrayList<RoleTreasure>(role.getRoleTreasures().values());
		for (RoleTreasure t : roleTreasures) {
			if (TextUtil.isBlank(t.getHeroIds())) {
				continue;
			}
			long passTime = DateUtil.compareTime(new Date(), t.getDepartDate());
			int stage = XsgTreasureManage.getInstance().getStageTByTime((int) (passTime / 60000)).stage;
			// 寻宝进度
			int progress = XsgTreasureManage.getInstance().getProgressByTime(passTime);
			progress = Math.min(progress, 100);
			if (stage >= XsgTreasureManage.getInstance().getMaxStage() && progress == 100) {
				return new MajorUIRedPointNote(MajorMenu.TreasureMenu, false);
			}
		}
		return null;
	}

	@Override
	public TreasureView getTreasureView() throws NoteException {
		TreasureConfT conf = XsgTreasureManage.getInstance().getTreasureConfT();
		if (iRole.getLevel() < conf.openLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("TreasureControler.openLevel"), conf.openLevel));
		}
		initTreasure();
		refreshRecommend();
		checkAccident();
		// 按队伍编号排序
		List<RoleTreasure> roleTreasures = new ArrayList<RoleTreasure>(role.getRoleTreasures().values());
		Collections.sort(roleTreasures, new Comparator<RoleTreasure>() {

			@Override
			public int compare(RoleTreasure o1, RoleTreasure o2) {
				return o1.getGroupNum() - o2.getGroupNum();
			}
		});
		List<TreasureGroup> groups = new ArrayList<TreasureGroup>();
		for (RoleTreasure t : roleTreasures) {
			TreasureGroup group = null;
			// 武将为空表示未出发
			if (TextUtil.isBlank(t.getHeroIds())) {
				group = new TreasureGroup(t.getGroupNum(), new String[0], TextUtil.GSON.fromJson(
						t.getRecommendHeroJson(), RecommendHero[].class), true, 0, 0, false, 0, 0, false, 0, false,
						new TreasureEvent[0], 0, new ItemView[0]);
			} else {
				String[] heroIds = t.getHeroIds().split(",");
				long passTime = DateUtil.compareTime(new Date(), t.getDepartDate());
				int stage = XsgTreasureManage.getInstance().getStageTByTime((int) (passTime / 60000)).stage;
				// 寻宝进度
				int progress = XsgTreasureManage.getInstance().getProgressByTime(passTime);
				progress = Math.min(progress, 100);
				int remainMinute = XsgTreasureManage.getInstance().getRemainMinuteByTime(passTime);
				boolean isAllOver = stage >= XsgTreasureManage.getInstance().getMaxStage() && progress == 100;

				// 处理事件
				double baseValue = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).baseValue;
				double recommendAdd = conf.additional * t.getRecommendHeroNum();
				baseValue = baseValue * (1 + recommendAdd / 100);

				List<String> ids = addTreasureEvent(t, stage, progress);
				TreasureEvent[] events = new TreasureEvent[ids.size()];
				for (int i = 0; i < ids.size(); i++) {
					TreasureEventT eventT = XsgTreasureManage.getInstance().getEventById(
							NumberUtil.parseInt(ids.get(i)));
					double addValue = baseValue * eventT.result / 100;
					if (addValue > 0 && addValue < 1) {
						addValue = 1;
					} else if (addValue < 0 && addValue > -1) {
						addValue = -1;
					}
					events[i] = new TreasureEvent(eventT.describe.replace("N%", String.valueOf(i + 1)), (int) addValue);
				}
				TreasureStageT stageT = XsgTreasureManage.getInstance().getStageT(stage);
				group = new TreasureGroup(t.getGroupNum(), heroIds, TextUtil.GSON.fromJson(t.getRecommendHeroJson(),
						RecommendHero[].class), true, 0, 0, true, remainMinute, progress, stage > 1, stage, isAllOver,
						events, stageT.speedPrice, getGainItem(t, false));
			}
			groups.add(group);
		}
		// 处理未开启的队伍
		List<TreasureGroupT> notOpen = XsgTreasureManage.getInstance().getNotOpenGroup(iRole.getLevel(),
				iRole.getVipLevel());
		for (TreasureGroupT t : notOpen) {
			if (!role.getRoleTreasures().containsKey(t.groupNum)) {
				TreasureGroup group = new TreasureGroup(t.groupNum, new String[0], new RecommendHero[0], false,
						t.openLevel, t.openVipLevel, false, conf.duration, 0, false, 1, false, new TreasureEvent[0], 0,
						new ItemView[0]);
				groups.add(group);
			}
		}

		RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureAccidentT accidentT = XsgTreasureManage.getInstance().getTreasureAccidentByLevel(iRole.getLevel());
		TreasureAccidentLog accidentLog = getNewAccidentLog();
		if (accidentLog == null) {
			return new TreasureView(groups.toArray(new TreasureGroup[0]), conf.additional, param.getGainNum(),
					accidentT.maxGainNum, param.getRescueNum(), accidentT.maxRescueNum, 0, 0, 0, 0, 0, 0);
		} else {
			TreasureRescueT rescueT = XsgTreasureManage.getInstance().getRescueByLevel(accidentLog.accidentLevel);
			Date time = DateUtil.parseDate(accidentLog.datetime);// 矿难发生时间
			int accidentPassSecond = (int) (DateUtil.compareTime(new Date(), time) / 1000);// 矿难已过去秒

			int baseValue = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).baseValue;
			baseValue = baseValue * rescueT.punishment / 100;
			return new TreasureView(groups.toArray(new TreasureGroup[0]), conf.additional, param.getGainNum(),
					accidentT.maxGainNum, param.getRescueNum(), accidentT.maxRescueNum, accidentLog.accidentLevel,
					Math.max(1, baseValue), rescueT.continueMinute * 60 - accidentPassSecond,
					accidentLog.rescueFriend.length, rescueT.rescuePeople, rescueT.effect);
		}
	}

	/**
	 * 检测矿难
	 */
	private void checkAccident() {
		RoleTreasureParam param = role.getRoleTreasureParam();

		List<RoleTreasure> roleTreasures = new ArrayList<RoleTreasure>(role.getRoleTreasures().values());
		boolean hasDepart = false;
		for (RoleTreasure t : roleTreasures) {
			if (TextUtil.isNotBlank(t.getHeroIds())) {
				hasDepart = true;
				break;
			}
		}

		if (param.getCheckAccidentDate() == null && hasDepart) {
			param.setCheckAccidentDate(new Date());
		}
		// 刷新收获和援救次数
		if (param.getRefreshDate() == null || !DateUtil.isSameDay(new Date(), param.getRefreshDate())) {
			param.setGainNum(0);
			param.setRescueNum(0);
			param.setAccidentNum(0);
			param.setRefreshDate(new Date());
		}

		TreasureAccidentT accidentT = XsgTreasureManage.getInstance().getTreasureAccidentByLevel(iRole.getLevel());
		// 处理矿难信息
		TreasureAccidentLog[] accidentLogs = TextUtil.GSON.fromJson(param.getAccidentLogs(),
				TreasureAccidentLog[].class);
		List<TreasureAccidentLog> accidentList = new ArrayList<TreasureAccidentLog>();
		for (TreasureAccidentLog a : accidentLogs) {
			accidentList.add(a);
			// 处理过期
			TreasureRescueT rescueT = XsgTreasureManage.getInstance().getRescueByLevel(a.accidentLevel);
			Date time = DateUtil.parseDate(a.datetime);// 矿难发生时间
			int accidentPassSecond = (int) (DateUtil.compareTime(new Date(), time) / 1000);// 矿难已过去秒
			// 矿难已过期
			if (accidentPassSecond / 60 >= rescueT.continueMinute && a.status == 0) {
				a.status = 2;// 援救失败
				// 更新损失宝石个数
				int baseValue = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).baseValue;
				baseValue = baseValue * rescueT.punishment / 100;
				a.lossNum = Math.max(1, baseValue);
			}
		}

		if (hasDepart && param.getAccidentNum() < accidentT.maxAccidentNum
				&& DateUtil.compareTime(new Date(), param.getCheckAccidentDate()) > accidentT.interval * 60000) {
			TreasureRescueT rescueT = XsgTreasureManage.getInstance().randomRescueT();// 随机一个矿难
			param.setAccidentNum(param.getAccidentNum() + 1);
			param.setCheckAccidentDate(new Date());

			if (rescueT.level != 0) {
				param.setAccidentRedPoint(true);// 矿难红点
				sendRoleIds.clear();// 清空上次矿难发送的援救请求

				TreasureAccidentLog addLog = new TreasureAccidentLog(GlobalDataManager.getInstance()
						.generatePrimaryKey(), DateUtil.format(new Date()), rescueT.level, 0,
						new TreasureRescueFriend[0], 0);
				accidentList.add(0, addLog);
				// 出发的寻宝队伍增加矿难记录
				for (RoleTreasure t : roleTreasures) {
					if (!TextUtil.isBlank(t.getHeroIds())) {
						List<String> accidentIds = TextUtil.stringToList(t.getAccidentIds());
						accidentIds.add(addLog.id);
						if (accidentIds.size() > 10) {
							accidentIds.remove(0);
						}
						t.setAccidentIds(TextUtil.join(accidentIds, ","));
					}
				}
				// 最多20条记录
				if (accidentList.size() > 20) {
					accidentList.remove(accidentList.size() - 1);
				}
			}
		}
		param.setAccidentLogs(TextUtil.GSON.toJson(accidentList.toArray(new TreasureAccidentLog[0])));
	}

	/**
	 * 初始化寻宝队伍
	 */
	private void initTreasure() {
		List<TreasureGroupT> open = XsgTreasureManage.getInstance().getOpenGroup(iRole.getLevel(), iRole.getVipLevel());
		boolean isAdd = false;
		for (TreasureGroupT g : open) {
			if (!role.getRoleTreasures().containsKey(g.groupNum)) {
				RoleTreasure treasure = new RoleTreasure(GlobalDataManager.getInstance().generatePrimaryKey(), role,
						"", "", null, null, 0, g.groupNum);
				role.getRoleTreasures().put(treasure.getGroupNum(), treasure);
				isAdd = true;
			}
		}
		if (isAdd) {
			treasureAddGroupEvent.onAddGroup(role.getRoleTreasures().size());
		}
	}

	/**
	 * 刷新推荐武将
	 */
	private void refreshRecommend() {
		// 存在的推荐武将
		List<Integer> existIds = new ArrayList<Integer>();
		TreasureConfT conf = XsgTreasureManage.getInstance().getTreasureConfT();
		for (RoleTreasure t : role.getRoleTreasures().values()) {
			// 已经出发不刷新
			if (!TextUtil.isBlank(t.getHeroIds())) {
				continue;
			}
			if (t.getRefreshDate() == null || DateUtil.isPass(conf.refreshDate, "HH:mm:ss", t.getRefreshDate())) {
				t.setRecommendHeroJson(TextUtil.GSON.toJson(XsgTreasureManage.getInstance().randomRecommend(existIds,
						t.getGroupNum())));
				t.setRefreshDate(new Date());
			}
		}
	}

	/**
	 * 增加寻宝事件
	 */
	private List<String> addTreasureEvent(RoleTreasure t, int stage, int progress) {
		List<String> ids = TextUtil.stringToList(t.getEventIds());
		int addCount = stage - ids.size();// 需要增加的事件个数
		if (progress != 100) {
			addCount--;
		}
		for (int i = 0; i < addCount; i++) {
			ids.add(String.valueOf(XsgTreasureManage.getInstance().randomEventT().id));
		}
		t.setEventIds(TextUtil.join(ids, ","));
		return ids;
	}

	@Override
	public void depart(int id, String heroIds) throws NoteException {
		String[] heroArr = heroIds.split(",");
		if (heroArr.length != 3) {
			throw new NoteException("The hero number of errors");
		}
		RoleTreasure treasure = role.getRoleTreasures().get(id);
		if (treasure == null) {
			throw new NoteException("group not find by " + id);
		}
		if (!TextUtil.isBlank(treasure.getHeroIds())) {
			throw new NoteException("Treasure team has been set out");
		}
		// 验证武将信息
		int recommendNum = 0;// 上阵的推荐武将个数
		int activityNum = 0;// 运营活动推荐武将个数
		for (String hid : heroArr) {
			IHero hero = iRole.getHeroControler().getHero(hid);
			if (hero == null) {
				throw new NoteException("Hero do not exist");
			}
			if (isInTreasureGroup(hid)) {
				throw new NoteException("Hero on the other team");
			}
			for (RecommendHero h : TextUtil.GSON.fromJson(treasure.getRecommendHeroJson(), RecommendHero[].class)) {
				if (h.id == hero.getTemplateId() && hero.getLevel() >= h.level && hero.getStar() >= h.star
						&& hero.getQualityLevel() >= h.advance) {
					recommendNum++;

					// 计算运营活动武将个数
					RecommendHeroT recommendHeroT = XsgTreasureManage.getInstance().getByHeroId(h.id, id);
					if (recommendHeroT != null && recommendHeroT.isActivity == 1) {
						activityNum++;
					}
				}
			}
		}
		treasure.setHeroIds(heroIds);
		treasure.setRecommendHeroNum(recommendNum);
		treasure.setDepartDate(new Date());
		treasure.setActivityHeroNum(activityNum);

		treasure.setSpeedNum(0);
		treasure.setEventIds("");
		// 处理矿难
		TreasureAccidentLog log = getNewAccidentLog();
		if (log != null) {
			treasure.setAccidentIds(log.id);
		} else {
			treasure.setAccidentIds("");
		}
		// 武将状态变更
		for (String hid : heroArr) {
			IHero hero = iRole.getHeroControler().getHero(hid);
			iRole.getNotifyControler().onHeroChanged(hero);
		}

		treasureDepartEvent.onDepart(heroIds, recommendNum);
	}

	@Override
	public boolean isInTreasureGroup(String heroId) {
		for (RoleTreasure t : role.getRoleTreasures().values()) {
			List<String> heroIds = TextUtil.stringToList(t.getHeroIds());
			if (heroIds.contains(heroId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void recall(int id) throws NoteException {
		RoleTreasure treasure = role.getRoleTreasures().get(id);
		if (treasure == null || TextUtil.isBlank(treasure.getHeroIds())) {
			throw new NoteException(Messages.getString("TreasureControler.notDepart"));
		}
		List<String> heroIds = TextUtil.stringToList(treasure.getHeroIds());
		treasure.setHeroIds("");
		// 武将状态变更
		for (String hid : heroIds) {
			IHero hero = iRole.getHeroControler().getHero(hid);
			iRole.getNotifyControler().onHeroChanged(hero);
		}
	}

	@Override
	public ItemView[] gain(int id) throws NoteException {
		RoleTreasure treasure = role.getRoleTreasures().get(id);
		RoleTreasureParam param = role.getRoleTreasureParam();
		if (treasure == null || TextUtil.isBlank(treasure.getHeroIds())) {
			throw new NoteException(Messages.getString("TreasureControler.notDepart"));
		}
		TreasureConfT conf = XsgTreasureManage.getInstance().getTreasureConfT();
		long passTime = DateUtil.compareTime(new Date(), treasure.getDepartDate());
		int stage = XsgTreasureManage.getInstance().getStageTByTime((int) (passTime / 60000)).stage;
		// 寻宝进度
		int progress = XsgTreasureManage.getInstance().getProgressByTime(passTime);
		progress = Math.min(progress, 100);
		if (stage < 2) {
			throw new NoteException(Messages.getString("TreasureControler.minDeficiency"));
		}

		TreasureAccidentT accidentT = XsgTreasureManage.getInstance().getTreasureAccidentByLevel(iRole.getLevel());
		if (param.getGainNum() >= accidentT.maxGainNum) {
			throw new NoteException(Messages.getString("TreasureControler.notGainCount"));
		}
		param.setGainNum(param.getGainNum() + 1);

		// 清理队伍寻宝状态
		List<String> heroIds = TextUtil.stringToList(treasure.getHeroIds());
		treasure.setHeroIds("");
		// 武将状态变更
		for (String hid : heroIds) {
			IHero hero = iRole.getHeroControler().getHero(hid);
			iRole.getNotifyControler().onHeroChanged(hero);
		}

		// 处理寻宝活动
		if (treasure.getActivityHeroNum() > 0) {
			MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
					.get(MailTemplate.TreasureActivityHero.value());
			Property[] pro = new Property[] { new Property(conf.activityItemId, treasure.getActivityHeroNum()) };
			// 邮件发放奖励
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, iRole
							.getRoleId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
							.serializeMailAttach(pro), Calendar.getInstance().getTime()));
		}
		ItemView[] itemViews = getGainItem(treasure, true);
		// 结算奖励
		iRole.getRewardControler().acceptReward(itemViews);
		return itemViews;
	}

	/**
	 * 收获可获得物品
	 * 
	 * @param id
	 * @return
	 */
	private ItemView[] getGainItem(RoleTreasure treasure, boolean isGain) {
		TreasureConfT conf = XsgTreasureManage.getInstance().getTreasureConfT();
		// 基础奖励个数
		double baseValue = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).baseValue;
		// 推荐武将加成
		double recommendAdd = conf.additional * treasure.getRecommendHeroNum();
		baseValue = baseValue * (1 + recommendAdd / 100);

		long passTime = DateUtil.compareTime(new Date(), treasure.getDepartDate());
		int stage = XsgTreasureManage.getInstance().getStageTByTime((int) (passTime / 60000)).stage;
		// 寻宝进度
		int progress = XsgTreasureManage.getInstance().getProgressByTime(passTime);
		progress = Math.min(progress, 100);
		if (stage < 2) {
			return new ItemView[0];
		}

		// 处理事件加成
		List<String> eventIds = addTreasureEvent(treasure, stage, progress);
		int eventAdd = 0;// 事件加成总个数
		for (String eid : eventIds) {
			TreasureEventT eventT = XsgTreasureManage.getInstance().getEventById(NumberUtil.parseInt(eid));
			double addValue = baseValue * eventT.result / 100;
			if (addValue > 0 && addValue < 1) {
				addValue = 1;
			} else if (addValue < 0 && addValue > -1) {
				addValue = -1;
			}
			eventAdd = eventAdd + (int) addValue;
		}
		// 处理矿难加成
		int accidentAdd = 0;// 矿难加成总个数
		if (TextUtil.isNotBlank(treasure.getAccidentIds())) {
			List<String> accidentIds = TextUtil.stringToList(treasure.getAccidentIds());
			TreasureAccidentLog[] accidentLog = TextUtil.GSON.fromJson(role.getRoleTreasureParam().getAccidentLogs(),
					TreasureAccidentLog[].class);
			int base = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).baseValue;
			for (String aid : accidentIds) {
				for (TreasureAccidentLog ta : accidentLog) {
					if (aid.equals(ta.id) && ta.status != 1) {
						int punishment = XsgTreasureManage.getInstance().getRescueByLevel(ta.accidentLevel).punishment;
						accidentAdd += Math.max(1, base * punishment / 100);
					}
				}
			}
		}

		String tc = XsgTreasureManage.getInstance().getActivityTc(treasure.getGroupNum(), new Date());
		if (tc == null || treasure.getRecommendHeroNum() != 3) {
			tc = XsgTreasureManage.getInstance().getVipTc(iRole.getVipLevel()).tc;
		}
		ItemView[] itemViews = XsgRewardManager.getInstance().doTcToItem(iRole, tc);

		for (ItemView i : itemViews) {
			// 向上取整 公式--baseValue*(1+推荐武将加成/100) + baseValue*(1+推荐武将加成/100) *
			// 事件加成/100 + baseValue*矿难加成/100
			i.num = (int) Math.ceil(baseValue + eventAdd - accidentAdd);
			i.num = Math.max(1, i.num);// 至少有1个
		}
		if (isGain) {
			treasureGainEvent.onGain(itemViews, TextUtil.format("{0},{1},{2}", eventAdd, accidentAdd, recommendAdd));
		}
		return itemViews;
	}

	@Override
	public TreasureRescueLog[] getRescueLog() throws NoteException {
		RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureRescueLog[] logs = TextUtil.GSON.fromJson(param.getRescueLogs(), TreasureRescueLog[].class);
		for (TreasureRescueLog l : logs) {
			l.datetime = l.datetime.substring(5);
		}
		return logs;
	}

	@Override
	public TreasureAccidentLog[] getAccidentLog() throws NoteException {
		RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureAccidentLog[] logs = TextUtil.GSON.fromJson(param.getAccidentLogs(), TreasureAccidentLog[].class);
		for (TreasureAccidentLog l : logs) {
			l.datetime = l.datetime.substring(5);
		}
		return logs;
	}

	@Override
	public void speed(int id) throws NoteException {
		RoleTreasure treasure = role.getRoleTreasures().get(id);
		if (treasure == null || TextUtil.isBlank(treasure.getHeroIds())) {
			throw new NoteException(Messages.getString("TreasureControler.notDepart"));
		}
		long passTime = DateUtil.compareTime(new Date(), treasure.getDepartDate());
		int stage = XsgTreasureManage.getInstance().getStageTByTime((int) (passTime / 60000)).stage;
		// 寻宝进度
		int progress = XsgTreasureManage.getInstance().getProgressByTime(passTime);
		progress = Math.min(progress, 100);
		if (progress == 100) {
			throw new NoteException(Messages.getString("TreasureControler.notSpeed"));
		}
		// 该阶段剩余分钟
		int remainMinute = XsgTreasureManage.getInstance().getRemainMinuteByTime(passTime);

		TreasureStageT stageT = XsgTreasureManage.getInstance().getStageT(stage);
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, stageT.speedPrice));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("CornucopiaControler.notYuanbao"));
		}
		Calendar t = Calendar.getInstance();
		t.setTime(treasure.getDepartDate());
		// 出发时间减去剩余分钟来达到加速效果
		t.add(Calendar.MINUTE, -remainMinute);

		treasure.setDepartDate(t.getTime());
		treasure.setSpeedNum(treasure.getSpeedNum() + 1);
	}

	@Override
	public void rescue(final AMD_Treasure_rescue __cb, final String friendId) throws NoteException {
		final RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureAccidentT accidentT = XsgTreasureManage.getInstance().getTreasureAccidentByLevel(iRole.getLevel());
		if (param.getRescueNum() >= accidentT.maxRescueNum) {
			__cb.ice_exception(new NoteException(Messages.getString("TreasureControler.notRescueCount")));
			return;
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(friendId, new Runnable() {

			@Override
			public void run() {
				IRole f = XsgRoleManager.getInstance().findRoleById(friendId);
				if (f != null) {
					try {
						ItemView[] is = f.getTreasureControler().rescueSelf(
								new TreasureRescueFriend(iRole.getRoleId(), iRole.getName()));
						param.setRescueNum(param.getRescueNum() + 1);

						// 寻宝救援好友
						treasureHelpFriend.onTreasureHelpFriend();
						__cb.ice_response(LuaSerializer.serialize(is));
					} catch (Exception e) {
						__cb.ice_exception(e);
						return;
					}
				}
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException("friend not exist"));
			}
		});
	}

	@Override
	public ItemView[] rescueSelf(TreasureRescueFriend friend) throws NoteException {
		RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureAccidentLog[] accidentLogs = TextUtil.GSON.fromJson(param.getAccidentLogs(),
				TreasureAccidentLog[].class);

		if (accidentLogs.length > 0) {
			TreasureAccidentLog accidentLog = accidentLogs[0];
			TreasureRescueT rescueT = XsgTreasureManage.getInstance().getRescueByLevel(accidentLog.accidentLevel);
			Date time = DateUtil.parseDate(accidentLog.datetime);// 矿难发生时间
			int accidentPassSecond = (int) (DateUtil.compareTime(new Date(), time) / 1000);// 矿难已过去秒
			// 矿难已过期
			if (accidentPassSecond / 60 >= rescueT.continueMinute) {
				throw new NoteException(Messages.getString("TreasureControler.accidentOutdate"));
			}
			if (accidentLog.rescueFriend.length >= rescueT.rescuePeople) {
				throw new NoteException(Messages.getString("TreasureControler.rescueOverrun"));
			}
			List<TreasureRescueFriend> rescueList = new ArrayList<TreasureRescueFriend>();
			for (TreasureRescueFriend f : accidentLog.rescueFriend) {
				rescueList.add(f);
				if (f.roleId.equals(iRole.getRoleId())) {
					throw new NoteException(Messages.getString("TreasureControler.alreadyRescue"));
				}
			}
			rescueList.add(friend);

			// 自己得到援救物品
			IRole fr = XsgRoleManager.getInstance().findRoleById(friend.roleId);
			ItemView[] item = XsgRewardManager.getInstance().doTcToItem(fr, rescueT.rescueTc);
			fr.getRewardControler().acceptReward(item);
			fr.getTreasureControler()
					.addRescueLog(
							new TreasureRescueLog(DateUtil.format(new Date()), iRole.getName(),
									accidentLog.accidentLevel, item));

			if (rescueList.size() >= rescueT.rescuePeople) {// 援救成功
				accidentLog.status = 1;
				// 自己得到被援救奖励 发邮件。。。
				MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
						.get(MailTemplate.RescueSelfAward.value());
				// 替换矿难等级
				String content = mailRewardT.body.replace("$l", String.valueOf(accidentLog.accidentLevel));
				Property[] pro = getPropertyArr(rescueT.byRescueItems);

				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
								iRole.getRoleId(), mailRewardT.title, content, XsgMailManager.getInstance()
										.serializeMailAttach(pro), Calendar.getInstance().getTime()));

				// 所有援救者得到额外奖励
				mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
						.get(MailTemplate.RescueFriendAward.value());
				pro = getPropertyArr(rescueT.extraItems);

				// 替换名字和矿难等级
				content = mailRewardT.body.replace("$l", iRole.getName()).replace("$m",
						String.valueOf(accidentLog.accidentLevel));

				for (TreasureRescueFriend f : rescueList) {
					XsgMailManager.getInstance().sendMail(
							new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName,
									f.roleId, mailRewardT.title, content, XsgMailManager.getInstance()
											.serializeMailAttach(pro), Calendar.getInstance().getTime()));
				}
			}

			// 保存矿难数据
			accidentLog.rescueFriend = rescueList.toArray(new TreasureRescueFriend[0]);
			param.setAccidentLogs(TextUtil.GSON.toJson(accidentLogs));
			// 通知刷新
			ChatCallbackPrx call = iRole.getChatControler().getChatCb();
			if (call != null) {
				call.begin_refreshTreasure(LuaSerializer.serialize(getTreasureView()));
			}
			return item;
		} else {
			throw new NoteException(Messages.getString("TreasureControler.accidentNotExist"));
		}
	}

	/**
	 * 解析物品
	 * 
	 * @param itemStr
	 * @return
	 */
	private Property[] getPropertyArr(String itemStr) {
		String[] its = itemStr.split(",");
		Property[] pro = new Property[its.length];
		for (int j = 0; j < pro.length; j++) {
			String[] id_num = its[j].split(":");
			pro[j] = new Property(id_num[0], Integer.parseInt(id_num[1]));
		}
		return pro;
	}

	@Override
	public void addRescueLog(TreasureRescueLog rescueLog) {
		RoleTreasureParam param = role.getRoleTreasureParam();
		// 增加救援记录
		TreasureRescueLog[] rescueLogs = TextUtil.GSON.fromJson(param.getRescueLogs(), TreasureRescueLog[].class);
		List<TreasureRescueLog> list = new ArrayList<TreasureRescueLog>();
		for (TreasureRescueLog t : rescueLogs) {
			list.add(t);
		}
		list.add(0, rescueLog);
		if (list.size() > 20) {// 最多保留20条记录
			list.remove(list.size() - 1);
		}
		param.setRescueLogs(TextUtil.GSON.toJson(list.toArray(new TreasureRescueLog[0])));
	}

	@Override
	public TreasureAccidentLog getNewAccidentLog() {
		RoleTreasureParam param = role.getRoleTreasureParam();
		TreasureAccidentLog[] accidentLogs = TextUtil.GSON.fromJson(param.getAccidentLogs(),
				TreasureAccidentLog[].class);
		if (accidentLogs.length > 0) {
			TreasureAccidentLog accidentLog = accidentLogs[0];
			TreasureRescueT rescueT = XsgTreasureManage.getInstance().getRescueByLevel(accidentLog.accidentLevel);
			Date time = DateUtil.parseDate(accidentLog.datetime);// 矿难发生时间
			int accidentPassSecond = (int) (DateUtil.compareTime(new Date(), time) / 1000);// 矿难已过去秒
			// 矿难已过期或者援救成功了
			if (accidentPassSecond / 60 >= rescueT.continueMinute || accidentLog.status != 0) {
				return null;
			} else {
				return accidentLog;
			}
		} else {
			return null;
		}
	}

	@Override
	public int getCanRescueCount() {
		RoleTreasureParam param = role.getRoleTreasureParam();
		// 刷新收获和援救次数
		if (param.getRefreshDate() == null || !DateUtil.isSameDay(new Date(), param.getRefreshDate())) {
			param.setGainNum(0);
			param.setRescueNum(0);
			param.setAccidentNum(0);
			param.setRefreshDate(new Date());
		}
		TreasureAccidentT accidentT = XsgTreasureManage.getInstance().getTreasureAccidentByLevel(iRole.getLevel());
		return accidentT.maxRescueNum - param.getRescueNum();
	}

	@Override
	public void getAccidentFriend(final AMD_Treasure_getAccidentFriend __cb) throws NoteException {
		final Collection<String> friends = iRole.getSnsController().getFriends();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(friends), new Runnable() {

			@Override
			public void run() {
				List<TreasureFriend> tf = new ArrayList<TreasureFriend>();

				for (String id : friends) {
					IRole f = XsgRoleManager.getInstance().findRoleById(id);
					if (f != null) {
						TreasureAccidentLog log = f.getTreasureControler().getNewAccidentLog();
						if (log != null) {
							boolean enabled = true;
							for (TreasureRescueFriend t : log.rescueFriend) {
								if (t.roleId.equals(iRole.getRoleId())) {// 已经援救过了
									enabled = false;
									break;
								}
							}
							tf.add(new TreasureFriend(f.getRoleId(), f.getHeadImage(), f.getName(), f.getLevel(), f
									.getVipLevel(), log.accidentLevel, enabled));
						}
					}
				}
				__cb.ice_response(LuaSerializer.serialize(tf.toArray(new TreasureFriend[0])));
			}
		});
	}

	@Override
	public void getRescueFriend(final AMD_Treasure_getRescueFriend __cb) throws NoteException {
		final Collection<String> friends = iRole.getSnsController().getFriends();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(friends), new Runnable() {

			@Override
			public void run() {
				List<TreasureFriend> tf = new ArrayList<TreasureFriend>();

				for (String id : friends) {
					IRole f = XsgRoleManager.getInstance().findRoleById(id);
					if (f != null) {
						int count = f.getTreasureControler().getCanRescueCount();
						if (count > 0) {
							boolean enabled = true;
							if (sendRoleIds.contains(id)) {
								enabled = false;
							}
							tf.add(new TreasureFriend(f.getRoleId(), f.getHeadImage(), f.getName(), f.getLevel(), f
									.getVipLevel(), count, enabled));
						}
					}
				}
				// 小林志玲放到最后
				for (TreasureFriend f : tf) {
					if (f.name.equals(XsgSnsManager.getInstance().NAME_OF_MS_LING)) {
						tf.remove(f);
						tf.add(f);
						break;
					}
				}
				TreasureRecourseView view = new TreasureRecourseView(tf.toArray(new TreasureFriend[0]), sendRoleIds
						.size(), XsgTreasureManage.MAX_RECOURSE_NUM, role.getRoleTreasureParam().getRescueMsg());
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
	}

	@Override
	public void sendRescueMsg(final AMD_Treasure_sendRescueMsg __cb, String friendIds) throws NoteException {
		if (sendRoleIds.size() >= XsgTreasureManage.MAX_RECOURSE_NUM) {
			__cb.ice_exception(new NoteException(TextUtil.format(
					Messages.getString("TreasureControler.sendRecourseError"), XsgTreasureManage.MAX_RECOURSE_NUM)));
			return;
		}
		final List<String> ids = TextUtil.stringToList(friendIds);
		Iterator<String> it = ids.iterator();
		while (it.hasNext()) {
			String id = it.next();
			if (sendRoleIds.contains(id)) {
				it.remove();
			}
		}

		XsgRoleManager.getInstance().loadRoleAsync(ids, new Runnable() {

			@Override
			public void run() {
				for (String id : ids) {
					IRole r = XsgRoleManager.getInstance().findRoleById(id);
					if (r != null) {
						sendRoleIds.add(id);
						// 小林志玲陈潇自动救援
						if (r.getName().equals(XsgSnsManager.getInstance().NAME_OF_MS_LING)) {
							try {
								rescueSelf(new TreasureRescueFriend(r.getRoleId(), r.getName()));
							} catch (NoteException e) {

							}
						} else {
							try {
								iRole.getChatControler()
										.speakTo(
												null,
												r,
												makeTextMessage(ChatChannel.Private, role.getRoleTreasureParam()
														.getRescueMsg()));
							} catch (NoteException e) {
								LogManager.error(e);
							}
						}
					}
				}
				__cb.ice_response();
			}
		});
	}

	private TextMessage makeTextMessage(ChatChannel channel, String content) {
		ChatRole cr = new ChatRole();
		cr.id = iRole.getRoleId();
		cr.name = iRole.getName();
		cr.level = (short) iRole.getLevel();
		cr.vip = iRole.getVipLevel();
		cr.icon = iRole.getHeadImage();
		cr.chatTime = DateUtil.toString(System.currentTimeMillis(), "HH:mm");
		cr.OfficalRankId = iRole.getOfficalRankId();
		return new TextMessage(channel, null, cr, 1, content, 0, 0);
	}

	@Override
	public void saveRescueMsg(String msg) throws NoteException {
		if (msg.length() > 50) {
			msg = msg.substring(0, 50);
		}
		role.getRoleTreasureParam().setRescueMsg(msg);
	}
}
