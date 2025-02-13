package com.morefun.XSanGo.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ResourceBackItemView;
import com.XSanGo.Protocol.ResourceBackRewardView;
import com.XSanGo.Protocol.ResourceBackView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCornucopia;
import com.morefun.XSanGo.db.game.RoleDailyLoginStatus;
import com.morefun.XSanGo.db.game.RoleResourceAccept;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IAcceptResourceBack;
import com.morefun.XSanGo.faction.FactionConfigT;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.reward.IRewardControler;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.worldboss.WorldBossManager;

/**
 * 资源找回(春节大管家)
 * 
 * @author guofeng.qin
 */
@RedPoint(isTimer = true)
public class ResourceBackControler implements IResourceBackControler {
	private final static Log logger = LogFactory.getLog(ResourceBackControler.class);
	// 类型定义
	private static final int TypeAttackCastle = 2; // 北伐
	private static final int TypeJubao = 5; // 聚宝盆
	private static final int TypeMonster = 7; // 混世魔王
	private static final int TypeFactionWar = 8; // 公会战

	private IRole roleRT;
	private Role roleDB;

	private IAcceptResourceBack resourceBack;

	private boolean hasRedPoint = false;

	public ResourceBackControler(IRole role, Role db) {
		this.roleRT = role;
		this.roleDB = db;

		IEventControler evtController = roleRT.getEventControler();
		resourceBack = evtController.registerEvent(IAcceptResourceBack.class);
	}

	private RoleResourceAccept getRoleResourceAccept(String dateTag, int type) {
		List<RoleResourceAccept> list = roleDB.getResourceAcceptList();
		if (list != null) {
			for (RoleResourceAccept res : list) {
				if (res.getType() == type && dateTag.equals(res.getDateTag())) {
					return res;
				}
			}
		}
		return null;
	}

	private String getTodayDateStr() {
		return DateUtil.toString(System.currentTimeMillis(), "yyyyMMdd");
	}

	private String getDateStr(Date date) {
		return DateUtil.toString(date.getTime(), "yyyyMMdd");
	}

	private String getDateShowStr(Date date) {
		return DateUtil.toString(date.getTime(), "MM月dd日");
	}

	private void addOrUpdateResAccept(String dateTag, int type, String tag, int total, String name) {
		RoleResourceAccept resourceAccept = getRoleResourceAccept(dateTag, type);
		if (resourceAccept == null) {
			resourceAccept = new RoleResourceAccept(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB,
					dateTag, type, tag, name, 0, total, 1, Calendar.getInstance().getTime());
			roleDB.getResourceAcceptList().add(resourceAccept);
		}
		resourceAccept.setRecvCount(resourceAccept.getRecvCount() + 1);
		if (resourceAccept.getTotalCount() != total) {
			resourceAccept.setTotalCount(total);
		}
		resourceAccept.setHasReceived(1);
	}

	private boolean hasLoginByDateTag(String dateTag) {
		List<RoleDailyLoginStatus> dailyLoginList = roleDB.getDailyLoginList();
		if (dailyLoginList != null) {
			for (RoleDailyLoginStatus daily : dailyLoginList) {
				if (daily.getDateTag().equals(dateTag)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将配置列表中的时间转换为时间列表，方便迭代
	 */
	private List<Date> getDateList(List<ResourceBackTimeT> timeListT) {
		List<Date> returnList = new ArrayList<Date>();
		if (timeListT != null) {
			for (ResourceBackTimeT timeT : timeListT) {
				Date temp = timeT.startTime;
				while (temp.before(timeT.endTime) || temp.getTime() == timeT.endTime.getTime()) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(temp);
					returnList.add(cal.getTime());
					temp = DateUtil.addDays(temp, 1);
				}
			}
		}
		return returnList;
	}

	private IntString[] parseRewards(ResourceBackDetailT detailT) {
		List<IntString> rlist = new ArrayList<IntString>();
		if (detailT != null && TextUtil.isNotBlank(detailT.rewards)) {
			String[] rewardPairListStr = detailT.rewards.split(",");
			if (rewardPairListStr != null) {
				for (String rewardPairStr : rewardPairListStr) {
					String rewardStr[] = rewardPairStr.split(":");
					if (rewardStr != null) {
						IntString rewardPair = new IntString(Integer.parseInt(rewardStr[1]), rewardStr[0]);
						rlist.add(rewardPair);
					}
				}
			}
		}
		return rlist.toArray(new IntString[0]);
	}

	private IntString[] combineRewardPairs(IntString[] pair1, IntString[] pair2) {
		if (pair1 == null) {
			return pair2;
		}
		if (pair2 == null) {
			return pair1;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (IntString is : pair1) {
			map.put(is.strValue, is.intValue);
		}
		for (IntString is : pair2) {
			Integer exist = map.get(is.strValue);
			if (exist == null) {
				exist = 0;
			}
			map.put(is.strValue, is.intValue + exist);
		}
		List<IntString> intStrList = new ArrayList<IntString>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			intStrList.add(new IntString(entry.getValue(), entry.getKey()));
		}
		return intStrList.toArray(new IntString[0]);
	}

	private ResourceBackRewardView[] combineRewards(List<ResourceBackRewardView> rewardList) {
		Map<Integer, ResourceBackRewardView> rewardsMap = new HashMap<Integer, ResourceBackRewardView>();
		if (rewardList != null) {
			for (ResourceBackRewardView reward : rewardList) {
				ResourceBackRewardView existReward = rewardsMap.get(reward.type);
				if (existReward == null) {
					rewardsMap.put(reward.type, reward);
				} else {
					existReward.price1.num += reward.price1.num;
					existReward.price2.num += reward.price2.num;
					existReward.rewards = combineRewardPairs(reward.rewards, existReward.rewards);
				}
			}
		}
		return rewardsMap.values().toArray(new ResourceBackRewardView[0]);
	}

	private List<ResourceBackRewardView> getResourceBackRewardView(List<ResourceBackDetailT> detailListT,
			String dateTag) {
		Date date = DateUtil.parseDate("yyyyMMdd", dateTag);
		List<ResourceBackRewardView> rewardViewList = new ArrayList<ResourceBackRewardView>();
		if (detailListT != null) {
			for (ResourceBackDetailT detailT : detailListT) {
				if (detailT.type == TypeMonster) { // 混世魔王和公会战特殊处理，需要判断当天是否开启
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					if (!WorldBossManager.getInstance().isOpenWeedDay(cal)) {
						continue;
					}
				}
				if (detailT.type == TypeFactionWar) { // 混世魔王和公会战特殊处理，需要判断当天是否开启
					FactionConfigT configT = XsgFactionManager.getInstance().getFactionConfigT();
					if (configT == null || TextUtil.isBlank(configT.openWeekDay)
							|| !configT.openWeekDay.contains("" + date.getDay())) {
						continue;
					}
				}
				ResourceBackConfigT configT = XsgActivityManage.getInstance().getResourceBackConfigT(detailT.type);
				if (configT != null && roleRT.getLevel() > detailT.levelLimit && TextUtil.isNotBlank(configT.name)) {
					RoleResourceAccept resourceAccept = getRoleResourceAccept(dateTag, detailT.type);
					int hasReceived = 0;
					if (resourceAccept != null) {
						hasReceived = resourceAccept.getHasReceived();
					}
					ResourceBackRewardView rewardView = new ResourceBackRewardView(detailT.type, configT.name,
							parseRewards(detailT), configT.rate1, detailT.getPrice1(), configT.rate2,
							detailT.getPrice2(), hasReceived);
					if (configT.type == TypeJubao) { // 聚宝盆需要判断是否购买
						RoleCornucopia pia = roleDB.getRoleCornucopia().get(detailT.id);
						if (pia != null && pia.getBuyDate().before(date)) { // 已购买,并且在当前日期之前购买
							rewardViewList.add(rewardView);
						}
					} else {
						rewardViewList.add(rewardView);
					}
					// V8及以上，北伐奖励翻倍
					if (detailT.type == TypeAttackCastle && roleRT.getVipLevel() >= 8 && rewardView.rewards != null) {
						for (IntString is : rewardView.rewards) {
							is.intValue *= 2;
						}
					}
				}
			}
		}
		return rewardViewList;
	}

	public IntString[] getIconMap() {
		Map<Integer, ResourceBackConfigT> map = XsgActivityManage.getInstance().getResourceBackConfigTMap();
		List<IntString> icons = new ArrayList<IntString>();
		if (map != null) {
			for (ResourceBackConfigT cfg : map.values()) {
				icons.add(new IntString(cfg.type, cfg.icon));
			}
		}
		return icons.toArray(new IntString[0]);
	}

	@Override
	public ResourceBackView getResourceBackView() throws NoteException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ResourceBack)) {
			throw new NoteException(Messages.getString("ResourceBackControler.notOpen"));
		}
		List<ResourceBackTimeT> timeListT = XsgActivityManage.getInstance().getResourceBackTimeTList();
		// 获取能找回的时间列表
		List<Date> dateList = getDateList(timeListT);
		List<ResourceBackItemView> ritemViews = new ArrayList<ResourceBackItemView>();
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.ResourceBack);
		ResourceBackView rbView = new ResourceBackView(XsgActivityManage.getInstance().getRemainSecond(activityT),
				getIconMap(), null);
		long current = DateUtil.joinTime("00:00:00").getTime();
		long roleCreateTime = roleRT.getCreateTime().getTime();
		for (Date date : dateList) {
			String dateTag = getDateStr(date);
			ResourceBackItemView view = new ResourceBackItemView(date.getTime(), getDateShowStr(date), dateTag, 0,
					null);
			List<ResourceBackRewardView> rewardViewList = new ArrayList<ResourceBackRewardView>();
			// 时间已过，并且没有登陆过
			if (!hasLoginByDateTag(dateTag)) {
				Map<Integer, List<ResourceBackDetailT>> detailMapT = XsgActivityManage.getInstance()
						.getResourceBackDetailTMap();
				if (detailMapT != null) {
					for (List<ResourceBackDetailT> detailListT : detailMapT.values()) {
						rewardViewList.addAll(getResourceBackRewardView(detailListT, dateTag));
					}
					// 今天及之后，以及创角时间之前的奖励不能领
					if (current > date.getTime() && roleCreateTime <= date.getTime()) {
						view.canRecv = 1;
					}
				}
			}
			view.items = combineRewards(rewardViewList);
			ritemViews.add(view);
		}

		Collections.sort(ritemViews, new Comparator<ResourceBackItemView>() {
			@Override
			public int compare(ResourceBackItemView o1, ResourceBackItemView o2) {
				return Long.valueOf(o1.dateTime).compareTo(o2.dateTime);
			}
		});

		rbView.items = ritemViews.toArray(new ResourceBackItemView[0]);

		return rbView;
	}

	private Set<String> getDateStrList() {
		List<ResourceBackTimeT> timeListT = XsgActivityManage.getInstance().getResourceBackTimeTList();
		// 获取能找回的时间列表
		List<Date> dateList = getDateList(timeListT);
		Set<String> dateStrList = new HashSet<String>();
		if (dateList != null) {
			for (Date d : dateList) {
				dateStrList.add(getDateStr(d));
			}
		}
		return dateStrList;
	}

	@Override
	public void accept(String date, int type, int slot)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ResourceBack)) {
			throw new NoteException(Messages.getString("ResourceBackControler.notOpen"));
		}
		Set<String> daySet = getDateStrList();
		if (!daySet.contains(date)) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		Date acceptDate = DateUtil.parseDate("yyyyMMdd", date);
		long current = DateUtil.joinTime("00:00:00").getTime();
		long roleCreateTime = roleRT.getCreateTime().getTime();
		if (current <= acceptDate.getTime() || roleCreateTime > acceptDate.getTime()) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		List<ResourceBackDetailT> detailListT = XsgActivityManage.getInstance().getResourceBackDetailTList(type);
		ResourceBackConfigT configT = XsgActivityManage.getInstance().getResourceBackConfigT(type);
		if (detailListT == null || configT == null) {
			throw new NoteException(Messages.getString("ResourceBackControler.rewardNotOpen"));
		}
		List<ResourceBackRewardView> rewardsList = getResourceBackRewardView(detailListT, date);
		ResourceBackRewardView[] rewardArray = combineRewards(rewardsList);
		if (rewardArray == null || rewardArray.length <= 0) {
			throw new NoteException(Messages.getString("ResourceBackControler.rewardNotOpen"));
		}
		ResourceBackRewardView reward = rewardArray[0];
		Money m = reward.price2;
		// 计算选择的返还档位
		switch (slot) {
		case 1:
			m = reward.price1;
			break;
		case 2:
			m = reward.price2;
			break;
		default:
			break;
		}

		RoleResourceAccept resAccept = getRoleResourceAccept(date, type);
		if (resAccept != null && resAccept.getHasReceived() == 1) {
			throw new NoteException(Messages.getString("ResourceBackControler.alreadyRecv"));
		}

		switch (m.type) {
		case Jinbi:
			if (roleRT.getJinbi() < m.num) {
				throw new NotEnoughMoneyException();
			}
			break;
		case Yuanbao:
			if (roleRT.getTotalYuanbao() < m.num) {
				throw new NotEnoughYuanBaoException();
			}
			break;
		default:
			throw new NoteException(Messages.getString("ResourceBackControler.chooseAType"));
		}

		// 扣钱
		roleRT.reduceCurrency(m);

		// 获取奖励
		// 计算折扣
		int[] slotRate = { configT.rate1, configT.rate2 };
		IRewardControler rewardControler = roleRT.getRewardControler();
		List<String> acceptedRewards = new ArrayList<String>();
		if (reward.rewards != null) {
			for (IntString is : reward.rewards) {
				BigDecimal bd = new BigDecimal(is.intValue * slotRate[slot - 1] / 100.0).setScale(0, RoundingMode.UP);
				int num = bd.intValue();
				if (num > 0) {
					rewardControler.acceptReward(is.strValue, num);
					acceptedRewards.add(TextUtil.format("{0}:{1}", is.strValue, num));
				}
			}
		}

		// 记录领取标记
		String acceptedReardsStr = TextUtil.join(acceptedRewards, ";");
		addOrUpdateResAccept(date, type, acceptedReardsStr, 1, configT.name);
		// 事件
		resourceBack.onAcceptResourceBack(date, acceptedReardsStr, m);
		// 重置红点状态，下次请求红点重新计算
		hasRedPoint = false;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean open = XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ResourceBack);
		if (!open) {
			return null;
		}
		if (hasRedPoint) {
			return new MajorUIRedPointNote(MajorMenu.ResourceBack, true);
		}

		try {
			ResourceBackView rbview = getResourceBackView();
			if (rbview != null) {
				ResourceBackItemView[] resbackList = rbview.items;
				if (resbackList != null) {
					int size = resbackList.length;
					for (int i = size - 1; i >= 0; i--) {
						ResourceBackItemView view = resbackList[i];
						if (view.canRecv == 1 && view.items != null) {
							for (ResourceBackRewardView reward : view.items) {
								if (reward.hasReceived == 0) {
									hasRedPoint = true;
									return new MajorUIRedPointNote(MajorMenu.ResourceBack, true);
								}
							}
						}
					}
				}
			}
		} catch (NoteException e) {
		}
		return null;
	}

	/** 清理数据 */
	private void cleanLoginStatus() {
		final int TwoMonth = 30 * 2;
		List<RoleDailyLoginStatus> list = roleDB.getDailyLoginList();
		if (list != null && list.size() > 0) {
			List<RoleDailyLoginStatus> removeList = new ArrayList<RoleDailyLoginStatus>();
			Date current = Calendar.getInstance().getTime();
			for (RoleDailyLoginStatus status : list) {
				if (DateUtil.compareDate(current, status.getUpdateTime()) > TwoMonth) {
					removeList.add(status);
				}
			}
			roleDB.getDailyLoginList().removeAll(removeList);
		}
	}

	@Override
	public void addLoginStatus() {
		cleanLoginStatus();
		String todayTag = getTodayDateStr();
		if (!hasLoginByDateTag(todayTag)) {
			RoleDailyLoginStatus dailyLoginStatus = new RoleDailyLoginStatus(
					GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, todayTag,
					Calendar.getInstance().getTime());
			roleDB.getDailyLoginList().add(dailyLoginStatus);
		}
	}

	@Override
	public void acceptOneKey(String date) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ResourceBack)) {
			throw new NoteException(Messages.getString("ResourceBackControler.notOpen"));
		}
		Set<String> daySet = getDateStrList();
		if (!daySet.contains(date)) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		Date acceptDate = DateUtil.parseDate("yyyyMMdd", date);
		long current = DateUtil.joinTime("00:00:00").getTime();
		long roleCreateTime = roleRT.getCreateTime().getTime();
		if (current <= acceptDate.getTime() || roleCreateTime > acceptDate.getTime()) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		Map<Integer, ResourceBackConfigT> configMap = XsgActivityManage.getInstance().getResourceBackConfigTMap();
		if (configMap == null) {
			throw new NoteException(Messages.getString("ResourceBackControler.rewardNotOpen"));
		}
		int totalPrice = 0;
		CurrencyType currencyType = CurrencyType.Yuanbao;
		List<ResourceBackRewardView> rewards = new ArrayList<ResourceBackRewardView>();
		for (ResourceBackConfigT config : configMap.values()) {
			int type = config.type;
			List<ResourceBackDetailT> detailListT = XsgActivityManage.getInstance().getResourceBackDetailTList(type);
			if (detailListT != null) {
				List<ResourceBackRewardView> rewardsList = getResourceBackRewardView(detailListT, date);
				ResourceBackRewardView[] rewardArray = combineRewards(rewardsList);
				if (rewardArray != null && rewardArray.length > 0) {
					ResourceBackRewardView reward = rewardArray[0];
					RoleResourceAccept resAccept = getRoleResourceAccept(date, reward.type);
					if (resAccept == null || resAccept.getHasReceived() == 0) {
						totalPrice += reward.price2.num;
						currencyType = reward.price2.type;
						rewards.add(reward);
					}
				}
			}
		}
		// 检查元宝是否充足
		switch (currencyType) {
		case Jinbi:
			if (roleRT.getJinbi() < totalPrice) {
				throw new NotEnoughMoneyException();
			}
			break;
		case Yuanbao:
			if (roleRT.getTotalYuanbao() < totalPrice) {
				throw new NotEnoughYuanBaoException();
			}
			break;
		default:
			throw new NoteException(Messages.getString("ResourceBackControler.chooseAType"));
		}

		// 扣除元宝
		Money reduceMoney = new Money(currencyType, totalPrice);
		roleRT.reduceCurrency(reduceMoney);
		// 获取奖励
		// 计算折扣
		IRewardControler rewardControler = roleRT.getRewardControler();
		List<String> totalAcceptedRewards = new ArrayList<String>();
		if (rewards != null) {
			for (ResourceBackRewardView reward : rewards) {
				// RoleResourceAccept resAccept = getRoleResourceAccept(date,
				// reward.type);
				// if (resAccept == null || resAccept.getHasReceived() == 0) {
				if (reward.rewards != null) {
					List<String> acceptedRewards = new ArrayList<String>();
					for (IntString is : reward.rewards) {
						int num = (int) Math.ceil(is.intValue * reward.rate2 / 100);
						if (num > 0) {
							rewardControler.acceptReward(is.strValue, num);
							acceptedRewards.add(TextUtil.format("{0}:{1}", is.strValue, num));
						}
					}
					// 记录领取标记
					String acceptedReardsStr = TextUtil.join(acceptedRewards, ";");
					totalAcceptedRewards.addAll(acceptedRewards);
					addOrUpdateResAccept(date, reward.type, acceptedReardsStr, 1, reward.title);
				}
				// }
			}
		}
		// 事件
		resourceBack.onAcceptResourceBack(date, TextUtil.join(totalAcceptedRewards, ";"), reduceMoney);
		// 重置红点状态，下次请求红点重新计算
		hasRedPoint = false;
	}
}