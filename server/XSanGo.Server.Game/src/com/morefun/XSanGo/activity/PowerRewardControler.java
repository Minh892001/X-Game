package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PowerRewardItemView;
import com.XSanGo.Protocol.PowerRewardView;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RolePowerReward;
import com.morefun.XSanGo.event.protocol.IAcceptPowerReward;
import com.morefun.XSanGo.event.protocol.ICombatPowerChange;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 */
@RedPoint(isTimer = true)
public class PowerRewardControler implements IPowerRewardControler, ICombatPowerChange {

	private IRole roleRt;
	private Role roleDB;

	/** DB对象 */
	public RolePowerReward powerReward;

	private Set<String> alreadyReceivedLevel = new HashSet<String>();
	
	private IAcceptPowerReward acceptPowerReward;
	
	public PowerRewardControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;
		
		acceptPowerReward = roleRt.getEventControler().registerEvent(IAcceptPowerReward.class);
		roleRt.getEventControler().registerHandler(ICombatPowerChange.class, this);

		initRolePowerReward(roleDB.getPowerReward());
	}

	private void initRolePowerReward(RolePowerReward reward) {
		if (reward == null) {
			reward = new RolePowerReward(roleDB);
			saveToDb(reward);
		}

		this.powerReward = reward;
		alreadyReceivedLevel = TextUtil.GSON.fromJson(
				reward.getAcceptedPowerRewards(), Set.class);
	}

	private void saveToDb(RolePowerReward reward) {
		reward.setAcceptedPowerRewards(TextUtil.GSON
				.toJson(alreadyReceivedLevel));
		reward.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setPowerReward(reward);
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedLevel.contains(checkId + ""); //$NON-NLS-1$
	}

	/**
	 * 获取下一个可以领取的战力
	 * */
	private int getNextRewardPower() {
		List<PowerRewardT> rewards = XsgActivityManage.getInstance()
				.getPowerRewards();
		// 按照奖励等级排序
		Collections.sort(rewards, new Comparator<PowerRewardT>() {
			@Override
			public int compare(PowerRewardT o1, PowerRewardT o2) {
				return o1.power - o2.power;
			}
		});
		if (rewards != null) {
			for (PowerRewardT reward : rewards) {
				if (!isReceivedReward(reward.power)) {
					return reward.power;
				}
			}
		}
		return 0;
	}

	/**
	 * 增加一个已经领取过的等级, 并持久化到数据库
	 * 
	 * @param level
	 *            等级
	 * */
	private void addReceivedLevel(int power) {
		alreadyReceivedLevel.add(power + ""); //$NON-NLS-1$
		saveToDb(powerReward);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ZuiqingZhanli)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if(roleRt.getChatControler().getChatCb() == null){
			return null;
		}
		
		int power = roleRt.getCachePower();
		// 根据当前等级获取可以领取的最大等级的奖励
		int nextPower = getNextRewardPower();
		if (nextPower > 0 && power >= nextPower) {
			return new MajorUIRedPointNote(MajorMenu.PowerRewardMenu, false);
		}
		return null;
	}

	@Override
	public PowerRewardView getPowerRewardView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		// 获取脚本配置
		List<PowerRewardT> rewards = manager.getPowerRewards();
		List<PowerRewardItemView> rewardItems = new ArrayList<PowerRewardItemView>();
		// 包装所有奖励
		for (PowerRewardT reward : rewards) {
			rewardItems.add(new PowerRewardItemView(reward.power,
					isReceivedReward(reward.power), reward.rewardTemplateId));
		}
		// 排序,按照战力升序
		Collections.sort(rewardItems, new Comparator<PowerRewardItemView>() {
			@Override
			public int compare(PowerRewardItemView o1, PowerRewardItemView o2) {
				return o1.power - o2.power;
			}
		});
		PowerRewardItemView[] items = rewardItems
				.toArray(new PowerRewardItemView[0]);
		PowerRewardView view = new PowerRewardView(-1L,
				roleRt.getCachePower(), items);
		// 如果是限时的活动, 设置剩余时间
		ActivityT activityT = manager
				.getActivityT(ActivityTemplate.ZuiqingZhanli);
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

	@Override
	public void acceptPowerReward(int power) throws NoteException {
		if (roleRt.getCachePower() < power) {
			throw new NoteException(Messages.getString("PowerRewardControler.2")); //$NON-NLS-1$
		}
		if (isReceivedReward(power)) {
			throw new NoteException(Messages.getString("PowerRewardControler.3")); //$NON-NLS-1$
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		PowerRewardT rewardT = manager.getPowerRewardT(power);
		if (rewardT == null) {
			throw new NoteException(Messages.getString("PowerRewardControler.4")); //$NON-NLS-1$
		}
		
		// 发奖励
		roleRt.getRewardControler().acceptReward(rewardT.rewardTemplateId, 1);
		// 增加已领取等级
		addReceivedLevel(power);
		// 事件通知
		acceptPowerReward.onAcceptPowerReward(power, rewardT.rewardTemplateId);
	}

	/**
	 * 是否已经把全部奖励领完了
	 * */
	@Override
	public boolean hasAcceptAll() {
		if (alreadyReceivedLevel != null) {
			List<PowerRewardT> rewards = XsgActivityManage.getInstance().getPowerRewards();
			return alreadyReceivedLevel.size() >= rewards.size();
		}
		return false;
	}

	@Override
	public void onCombatPowerChange(int old, int newValue) {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.ZuiqingZhanli)) {
			return;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return;
		}

		int power = newValue;
		// 根据当前等级获取可以领取的最大等级的奖励
		int nextPower = getNextRewardPower();
		if (nextPower > 0 && power >= nextPower) {
			roleRt.getNotifyControler()
					.onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.PowerRewardMenu, false));
		}
	}

}
