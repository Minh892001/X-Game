package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.XSanGo.Protocol.DayforverLoginItemView;
import com.XSanGo.Protocol.DayforverLoginView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDayforverLoginReward;
import com.morefun.XSanGo.event.protocol.IAcceptDayforverLoginReward;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 累计登录 永久一次
 * 
 * @author sunjie
 */
@RedPoint
public class DayforverLoginControler implements IDayforverLoginControler {

	private IRole roleRt;
	private Role roleDB;

	/** DB对象 */
	private RoleDayforverLoginReward dayLoginReward;
	// 已经领取过的天数
	private Set<String> alreadyReceivedDay = new HashSet<String>();

	private IAcceptDayforverLoginReward acceptDayforverLoginReward;

	public DayforverLoginControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		acceptDayforverLoginReward = roleRt.getEventControler().registerEvent(
				IAcceptDayforverLoginReward.class);

		initRoleLevelReward(roleDB.getDayforverLoginReward());
	}

	private int getLoginDayCount() {
		return dayLoginReward.getDayCount();
	}

	/**
	 * 更新累计登录天数
	 **/
	public int updateLoginCount() {
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(
				ActivityTemplate.ForverLeijiLogin);
		if (activityT == null)
			return 0;
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
		if (update || dayLoginReward.getLastUpdateTime() == null) {
			saveToDb(dayLoginReward);
		}

		return dayLoginReward.getDayCount();
	}

	private void initRoleLevelReward(RoleDayforverLoginReward reward) {
		if (reward == null) {
			reward = new RoleDayforverLoginReward(roleDB);
			reward.setDayCount(0);
		}

		this.dayLoginReward = reward;
		if (!TextUtil.isBlank(reward.getAcceptedLevelRewards())) {
			alreadyReceivedDay = TextUtil.GSON.fromJson(
					reward.getAcceptedLevelRewards(), Set.class);
		}
	}

	private void saveToDb(RoleDayforverLoginReward reward) {
		reward.setAcceptedLevelRewards(TextUtil.GSON.toJson(alreadyReceivedDay));
		reward.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setDayforverLoginReward(reward);
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedDay.contains(checkId + ""); //$NON-NLS-1$
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(
				ActivityTemplate.ForverLeijiLogin)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}

		int levelLimit = XsgGameParamManager.getInstance()
				.getDayforverLoginLevelLimit();
		// 等级不够, 不显示红点
		if (roleRt.getLevel() < levelLimit) {
			return null;
		}

		int day = getLoginDayCount();
		// 根据当前等级获取可以领取的最大等级的奖励
		int nextDay = getNextRewardDay();
		if (nextDay > 0 && day >= nextDay) {
			return new MajorUIRedPointNote(MajorMenu.ForverLeijiLogin, true);
		}
		return null;
	}

	/**
	 * 获取下一个可以领取的等级奖励
	 * */
	private int getNextRewardDay() {
		List<DayforverLoginRewardT> rewards = XsgActivityManage.getInstance()
				.getDayfarvorLoginRewards();
		// 按照奖励等级排序
		Collections.sort(rewards, new Comparator<DayforverLoginRewardT>() {
			@Override
			public int compare(DayforverLoginRewardT o1,
					DayforverLoginRewardT o2) {
				return o1.day - o2.day;
			}
		});
		if (rewards != null) {
			for (DayforverLoginRewardT reward : rewards) {
				if (!isReceivedReward(reward.day)) {
					return reward.day;
				}
			}
		}
		return 0;
	}

	@Override
	public DayforverLoginView getLevelRewardView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		ActivityT activityT = manager
				.getActivityT(ActivityTemplate.ForverLeijiLogin);
		if(activityT==null)
			return null;
		// 更新累计登录天数
		updateLoginCount();
		// 获取脚本配置
		List<DayforverLoginRewardT> rewards = manager
				.getDayfarvorLoginRewards();
		List<DayforverLoginItemView> rewardItems = new ArrayList<DayforverLoginItemView>();
		// 包装所有奖励
		for (DayforverLoginRewardT reward : rewards) {
			rewardItems.add(new DayforverLoginItemView(reward.day,
					isReceivedReward(reward.day), reward.rewardTemplateId));
		}
		// 排序,按照等级升序
		Collections.sort(rewardItems, new Comparator<DayforverLoginItemView>() {
			@Override
			public int compare(DayforverLoginItemView o1,
					DayforverLoginItemView o2) {
				return o1.day - o2.day;
			}
		});
		DayforverLoginItemView[] items = rewardItems
				.toArray(new DayforverLoginItemView[0]);
		
		long startTime = DateUtil.parseDate(activityT.startTime).getTime() / 1000;
		long endTime = DateUtil.parseDate(activityT.endTime).getTime() / 1000;
		int levelLimit = XsgGameParamManager.getInstance()
				.getDayforverLoginLevelLimit();
		DayforverLoginView view = new DayforverLoginView(startTime, endTime,
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
				.getDayforverLoginLevelLimit();
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
		DayforverLoginRewardT rewardT = manager.getDayforverLoginRewardT(day);
		if (rewardT == null) {
			throw new NoteException(
					Messages.getString("LevelRewardControler.4")); //$NON-NLS-1$
		}

		// 发奖励
		roleRt.getRewardControler().acceptReward(rewardT.rewardTemplateId, 1);
		// 增加已领取等级
		addReceivedDay(day);
		// 事件通知
		acceptDayforverLoginReward.onAcceptDayforverLoginReward(day,
				rewardT.rewardTemplateId);
	}

	/**
	 * 是否已经把全部奖励领完了
	 * */
	@Override
	public boolean hasAcceptAll() {
		updateLoginCount();
		if (alreadyReceivedDay != null) {
			List<LevelRewardT> rewards = XsgActivityManage.getInstance()
					.getLevelRewards();
			return alreadyReceivedDay.size() >= rewards.size();
		}
		return false;
	}

}
