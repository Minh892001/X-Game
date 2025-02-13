package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.XSanGo.Protocol.FirstJiaItemView;
import com.XSanGo.Protocol.FirstJiaView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFirstJia;
import com.morefun.XSanGo.event.protocol.IAcceptFirstJiaReward;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 第一佳
 * 
 * @author qinguofeng
 */
@RedPoint
public class FirstJiaControler implements IFirstJiaControler, IRoleLevelup {

	private IRole roleRt;
	private Role roleDB;

	/** DB对象 */
	private RoleFirstJia levelReward;
	// 已经领取过的等级
	private Set<String> alreadyReceivedLevel = new HashSet<String>();

	private IAcceptFirstJiaReward acceptLevelReward;

	public FirstJiaControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		acceptLevelReward = roleRt.getEventControler().registerEvent(IAcceptFirstJiaReward.class);

		initRoleFirstJiaReward(roleDB.getFirstJia());
		
		roleRt.getEventControler().registerHandler(IRoleLevelup.class, this);
	}

	private void initRoleFirstJiaReward(RoleFirstJia reward) {
		if (reward == null) {
			reward = new RoleFirstJia(roleDB);
			saveToDb(reward);
		}

		this.levelReward = reward;
		alreadyReceivedLevel = TextUtil.GSON.fromJson(reward.getAcceptedLevelRewards(), Set.class);
	}

	private void saveToDb(RoleFirstJia reward) {
		reward.setAcceptedLevelRewards(TextUtil.GSON.toJson(alreadyReceivedLevel));
		reward.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setFirstJia(reward);
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedLevel.contains(checkId + ""); //$NON-NLS-1$
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.FirstJia)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}

		int level = roleRt.getLevel();
		// 根据当前等级获取可以领取的最大等级的奖励
		int nextLevel = getNextRewardLevel();
		if (nextLevel > 0 && level >= nextLevel) {
			return new MajorUIRedPointNote(MajorMenu.FirstJiaMenu, false);
		}
		return null;
	}

	/**
	 * 获取下一个可以领取的等级奖励
	 * */
	private int getNextRewardLevel() {
		List<FirstJiaT> rewards = XsgActivityManage.getInstance().getFirstJiaRewards();
		// 按照奖励等级排序
		Collections.sort(rewards, new Comparator<FirstJiaT>() {
			@Override
			public int compare(FirstJiaT o1, FirstJiaT o2) {
				return o1.level - o2.level;
			}
		});
		if (rewards != null) {
			for (FirstJiaT reward : rewards) {
				if (!isReceivedReward(reward.level)) {
					return reward.level;
				}
			}
		}
		return 0;
	}

	@Override
	public FirstJiaView getFirstJiaView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		// 获取脚本配置
		List<FirstJiaT> rewards = manager.getFirstJiaRewards();
		List<FirstJiaItemView> rewardItems = new ArrayList<FirstJiaItemView>();
		// 包装所有奖励
		for (FirstJiaT reward : rewards) {
			rewardItems
					.add(new FirstJiaItemView(reward.level, isReceivedReward(reward.level), reward.rewardTemplateId));
		}
		// 排序,按照等级升序
		Collections.sort(rewardItems, new Comparator<FirstJiaItemView>() {
			@Override
			public int compare(FirstJiaItemView o1, FirstJiaItemView o2) {
				return o1.level - o2.level;
			}
		});
		FirstJiaItemView[] items = rewardItems.toArray(new FirstJiaItemView[0]);
		FirstJiaView view = new FirstJiaView(-1L, roleRt.getLevel(), items);

		// 如果是限时的活动, 设置剩余时间
		ActivityT activityT = manager.getActivityT(ActivityTemplate.FirstJia);
		if (activityT.type == XsgActivityManage.TIME_LIMIT) {
			long startTime = DateUtil.parseDate(activityT.startTime).getTime();
			long endTime = DateUtil.parseDate(activityT.endTime).getTime();
			long current = System.currentTimeMillis();
			if (startTime <= current) { // 已开始
				// seconds
				view.lastTime = (endTime - current) / 1000;
			} else { // 还没开始
				view.lastTime = 0;
			}
		}

		return view;
	}

	/**
	 * 增加一个已经领取过的等级, 并持久化到数据库
	 * 
	 * @param level
	 *            等级
	 * */
	private void addReceivedLevel(int level) {
		alreadyReceivedLevel.add(level + ""); //$NON-NLS-1$
		saveToDb(levelReward);
	}

	@Override
	public void acceptLevelReward(int level) throws NoteException, NotEnoughMoneyException {
		if (level > roleRt.getLevel()) {
			throw new NoteException(Messages.getString("LevelRewardControler.2")); //$NON-NLS-1$
		}
		if (isReceivedReward(level)) {
			throw new NoteException(Messages.getString("LevelRewardControler.3")); //$NON-NLS-1$
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		FirstJiaT rewardT = manager.getFirstJiaRewardT(level);
		if (rewardT == null) {
			throw new NoteException(Messages.getString("LevelRewardControler.4")); //$NON-NLS-1$
		}

		// 发奖励
		roleRt.getRewardControler().acceptReward(rewardT.rewardTemplateId, 1);
		// 增加已领取等级
		addReceivedLevel(level);
		// 事件通知
		acceptLevelReward.onAcceptLevelReward(level, rewardT.rewardTemplateId);
	}

	/**
	 * 是否已经把全部奖励领完了
	 * */
	@Override
	public boolean hasAcceptAll() {
		if (alreadyReceivedLevel != null) {
			List<FirstJiaT> rewards = XsgActivityManage.getInstance().getFirstJiaRewards();
			return alreadyReceivedLevel.size() >= rewards.size();
		}
		return false;
	}

	@Override
	public void onRoleLevelup() {
		roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}

}
