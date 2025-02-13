package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.XSanGo.Protocol.DayLoginItemView;
import com.XSanGo.Protocol.DayLoginView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDayLoginReward;
import com.morefun.XSanGo.event.protocol.IAcceptDayLoginReward;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 累计登录
 * 
 * @author qinguofeng
 */
@RedPoint
public class DayLoginControler implements IDayLoginControler {

	private IRole roleRt;
	private Role roleDB;

	/** DB对象 */
	private RoleDayLoginReward dayLoginReward;
	// 已经领取过的天数
	private Set<String> alreadyReceivedDay = new HashSet<String>();

	private IAcceptDayLoginReward acceptDayLoginReward;

	public DayLoginControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		acceptDayLoginReward = roleRt.getEventControler().registerEvent(
				IAcceptDayLoginReward.class);

		initRoleLevelReward(roleDB.getDayLoginReward());
	}

	private int getLoginDayCount() {
		return dayLoginReward.getDayCount();
	}

	/**
	 * 更新累计登录天数
	 **/
	public int updateLoginCount() {
		if (!XsgActivityManage.getInstance().isActivityOpen(
				ActivityTemplate.LeijiLogin)) {
			return 0;
		}
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(
				ActivityTemplate.LeijiLogin);
		long current = System.currentTimeMillis();
		long startTime = DateUtil.parseDate(activityT.startTime).getTime();
		// 活动还没开始
		if (current < startTime) {
			// 登录计数状态归零
			if (dayLoginReward.getDayCount() != 0
					|| dayLoginReward.getLastLoginTime() != null) {
				dayLoginReward.setLastLoginTime(null);
				dayLoginReward.setDayCount(0);
				alreadyReceivedDay.clear();
				saveToDb(dayLoginReward);
			}
			return 0;
		}
		// 活动已开始, 计算累计登录次数
		boolean sameDay = false;
		boolean update = false;
		if (dayLoginReward.getLastLoginTime() != null) {
			Date lastLoginTime = dayLoginReward.getLastLoginTime();
			sameDay = DateUtil.isSameDay(lastLoginTime.getTime(),
					System.currentTimeMillis());
		}
		if (!sameDay) {
			dayLoginReward.setDayCount(dayLoginReward.getDayCount() + 1);
			dayLoginReward.setLastLoginTime(Calendar.getInstance().getTime());
			update = true;
		}
		// 活动时间发生了变化,即开始了新一轮的活动,则计数从一开始重新计数
		if (!activityT.startTime.equals(dayLoginReward.getStartTime())) {
			dayLoginReward.setStartTime(activityT.startTime);
			dayLoginReward.setLastLoginTime(Calendar.getInstance().getTime());
			alreadyReceivedDay.clear();
			dayLoginReward.setDayCount(1);
			update = true;
		}
		if (update || dayLoginReward.getLastUpdateTime() == null) {
			saveToDb(dayLoginReward);
		}

		return dayLoginReward.getDayCount();
	}

	private void initRoleLevelReward(RoleDayLoginReward reward) {
		if (reward == null) {
			reward = new RoleDayLoginReward(roleDB);
			reward.setDayCount(0);
		}

		this.dayLoginReward = reward;
		if (!TextUtil.isBlank(reward.getAcceptedLevelRewards())) {
			alreadyReceivedDay = TextUtil.GSON.fromJson(
					reward.getAcceptedLevelRewards(), Set.class);
		}
	}

	private void saveToDb(RoleDayLoginReward reward) {
		reward.setAcceptedLevelRewards(TextUtil.GSON.toJson(alreadyReceivedDay));
		reward.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setDayLoginReward(reward);
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedDay.contains(checkId + ""); //$NON-NLS-1$
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(
				ActivityTemplate.LeijiLogin)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}

		int levelLimit = XsgGameParamManager.getInstance()
				.getDayLoginLevelLimit();
		// 等级不够, 不显示红点
		if (roleRt.getLevel() < levelLimit) {
			return null;
		}

		int day = getLoginDayCount();
		// 根据当前等级获取可以领取的最大等级的奖励
		int nextDay = getNextRewardDay();
		if (nextDay > 0 && day >= nextDay) {
			return new MajorUIRedPointNote(MajorMenu.LeijiLogin, true);
		}
		return null;
	}

	/**
	 * 获取下一个可以领取的等级奖励
	 * */
	private int getNextRewardDay() {
		List<DayLoginRewardT> rewards = XsgActivityManage.getInstance()
				.getDayLoginRewards();
		// 按照奖励等级排序
		Collections.sort(rewards, new Comparator<DayLoginRewardT>() {
			@Override
			public int compare(DayLoginRewardT o1, DayLoginRewardT o2) {
				return o1.day - o2.day;
			}
		});
		if (rewards != null) {
			for (DayLoginRewardT reward : rewards) {
				if (!isReceivedReward(reward.day)) {
					return reward.day;
				}
			}
		}
		return 0;
	}

	@Override
	public DayLoginView getLevelRewardView() throws NoteException {
		if (!XsgActivityManage.getInstance().isActivityOpen(
				ActivityTemplate.LeijiLogin)) {
			return null;
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		// 更新累计登录天数
		updateLoginCount();
		// 获取脚本配置
		List<DayLoginRewardT> rewards = manager.getDayLoginRewards();
		List<DayLoginItemView> rewardItems = new ArrayList<DayLoginItemView>();
		// 包装所有奖励
		for (DayLoginRewardT reward : rewards) {
			rewardItems.add(new DayLoginItemView(reward.day,
					isReceivedReward(reward.day), reward.rewardTemplateId));
		}
		// 排序,按照等级升序
		Collections.sort(rewardItems, new Comparator<DayLoginItemView>() {
			@Override
			public int compare(DayLoginItemView o1, DayLoginItemView o2) {
				return o1.day - o2.day;
			}
		});
		DayLoginItemView[] items = rewardItems.toArray(new DayLoginItemView[0]);
		ActivityT activityT = manager.getActivityT(ActivityTemplate.LeijiLogin);
		long startTime = DateUtil.parseDate(activityT.startTime).getTime() / 1000;
		long endTime = DateUtil.parseDate(activityT.endTime).getTime() / 1000;
		int levelLimit = XsgGameParamManager.getInstance()
				.getDayLoginLevelLimit();
		DayLoginView view = new DayLoginView(startTime, endTime,
				getLoginDayCount(), items, levelLimit);

		return view;
	}

	/**
	 * 增加一个已经领取过的等级, 并持久化到数据库
	 * 
	 * @param day
	 *            天数
	 * */
	private void addReceivedDay(int day) {
		alreadyReceivedDay.add(day + ""); //$NON-NLS-1$
		saveToDb(dayLoginReward);
	}

	@Override
	public void acceptDayLoginReward(int day) throws NoteException,
			NotEnoughMoneyException {
		int levelLimit = XsgGameParamManager.getInstance()
				.getDayLoginLevelLimit();
		if (roleRt.getLevel() < levelLimit) {
			throw new NoteException(
					Messages.getString("LevelRewardControler.2"));
		}
		if (day > getLoginDayCount()) {
			throw new NoteException(Messages.getString("DayLoginControler.2")); //$NON-NLS-1$
		}
		if (isReceivedReward(day)) {
			throw new NoteException(
					Messages.getString("LevelRewardControler.3")); //$NON-NLS-1$
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		DayLoginRewardT rewardT = manager.getDayLoginRewardT(day);
		if (rewardT == null) {
			throw new NoteException(
					Messages.getString("LevelRewardControler.4")); //$NON-NLS-1$
		}

		// 发奖励
		roleRt.getRewardControler().acceptReward(rewardT.rewardTemplateId, 1);
		// 增加已领取等级
		addReceivedDay(day);
		// 事件通知
		acceptDayLoginReward.onAcceptDayLoginReward(day,
				rewardT.rewardTemplateId);
	}

	/**
	 * 是否已经把全部奖励领完了
	 * */
	@Override
	public boolean hasAcceptAll() {
		updateLoginCount();
		if (alreadyReceivedDay != null) {
			List<DayLoginRewardT> rewards = XsgActivityManage.getInstance()
					.getDayLoginRewards();
			return alreadyReceivedDay.size() >= rewards.size();
		}
		return false;
	}

}
