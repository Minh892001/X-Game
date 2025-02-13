package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SendJunLingItemView;
import com.XSanGo.Protocol.SendJunLingView;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleSendJunLing;
import com.morefun.XSanGo.event.protocol.IAcceptSendJunLing;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author qinguofeng
 */
@RedPoint(isTimer = true)
public class SendJunLingControler implements ISendJunLingControler {

	private IRole roleRt;
	private Role roleDB;

	/** DB 对象 */
	private RoleSendJunLing sendJunLing;
	// 已经领取过的索引
	private Set<String> alreadyReceivedIndex = new HashSet<String>();

	private IAcceptSendJunLing acceptSendJunLing;

	public SendJunLingControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		acceptSendJunLing = roleRt.getEventControler().registerEvent(
				IAcceptSendJunLing.class);

		initRoleSendJunLing(roleDB.getSendJunLing());
	}

	private void initRoleSendJunLing(RoleSendJunLing sendJunLing) {
		if (sendJunLing == null) {
			sendJunLing = new RoleSendJunLing(roleDB);
			saveToDb(sendJunLing);
		}
		this.sendJunLing = sendJunLing;
		alreadyReceivedIndex = TextUtil.GSON.fromJson(
				sendJunLing.getAcceptedRewardIndex(), Set.class);
	}

	private void saveToDb(RoleSendJunLing sendJunLing) {
		sendJunLing.setAcceptedRewardIndex(TextUtil.GSON
				.toJson(alreadyReceivedIndex));
		sendJunLing.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setSendJunLing(sendJunLing);
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedIndex.contains(checkId + "");
	}

	private void addReceivedReward(int index) {
		alreadyReceivedIndex.add(index + "");
		saveToDb(sendJunLing);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(
				ActivityTemplate.SendJunLing)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}

		int lvlLimit = XsgGameParamManager.getInstance().getSendJunlingLevelLimit();
		if (roleRt.getLevel() < lvlLimit) {
			return null;
		}
		// 更新领取奖励的状态
		updateReceiveStatus();

		// 根据当前等级获取可以领取的最大等级的奖励
		long current = System.currentTimeMillis();
		List<SendJunLingT> rewards = XsgActivityManage.getInstance()
				.getCurrentSendJunLingRewards();
		if (rewards != null) {
			for (SendJunLingT reward : rewards) {
				long start = DateUtil.joinTime(reward.start + ":00").getTime();
				long end = DateUtil.joinTime(reward.end + ":00").getTime();
				if (start < current && current < end && !isReceivedReward(reward.id)) {
					return new MajorUIRedPointNote(MajorMenu.SendJunLing, true);
				}
			}
		}
		return null;
	}

	/**
	 * 根据上次的领奖日期更新领取奖励的状态, 如果日期为昨天或者为空,则视为今天还没有领过奖励
	 */
	private void updateReceiveStatus() {
		if (sendJunLing.getLastAcceptedTime() == null
				|| !DateUtil.isSameDay(sendJunLing.getLastAcceptedTime()
						.getTime(), System.currentTimeMillis())) {
			alreadyReceivedIndex.clear();
			sendJunLing.setLastAcceptedTime(Calendar.getInstance().getTime());
			saveToDb(sendJunLing);
		}
	}

	@Override
	public SendJunLingView getSendJunLingView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		int lvlLimit = XsgGameParamManager.getInstance().getSendJunlingLevelLimit();
		// 更新领取奖励的状态
		updateReceiveStatus();
		// 获取脚本配置
		List<SendJunLingT> rewards = manager.getCurrentSendJunLingRewards();
		List<SendJunLingItemView> rewardItems = new ArrayList<SendJunLingItemView>();
		if (rewards != null) {
			for (SendJunLingT reward : rewards) {
				rewardItems.add(new SendJunLingItemView(reward.id, reward.start, reward.end,
						isReceivedReward(reward.id), reward.num));
			}
		}
		// 排序,按照id升序
		Collections.sort(rewardItems, new Comparator<SendJunLingItemView>() {
			@Override
			public int compare(SendJunLingItemView o1, SendJunLingItemView o2) {
				return (int) (DateUtil.joinTime(o1.startHour + ":00").getTime() - DateUtil
						.joinTime(o2.startHour + ":00").getTime());
			}
		});
		SendJunLingItemView[] items = rewardItems
				.toArray(new SendJunLingItemView[0]);

		long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
		SendJunLingView view = new SendJunLingView(currentTime, items, lvlLimit);

		return view;
	}

	@Override
	public int acceptJunLing(int id) throws NoteException,
			NotEnoughYuanBaoException {
		int lvlLimit = XsgGameParamManager.getInstance().getSendJunlingLevelLimit();
		if (roleRt.getLevel() < lvlLimit) {
			throw new NoteException(Messages.getString("SendJunLingControler.LevelLimit"));
		}
		if (isReceivedReward(id)) {
			throw new NoteException(
					Messages.getString("SendJunLingControler.AlreadyReceived"));
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		SendJunLingT rewardT = manager.getSendJunLingReward(id);
		if (rewardT == null) {
			throw new NoteException(
					Messages.getString("LevelRewardControler.4")); //$NON-NLS-1$
		}
		long current = System.currentTimeMillis();
		if (DateUtil.joinTime(rewardT.start + ":00").getTime() > current) {
			throw new NoteException(
					Messages.getString("SendJunLingControler.OverTime"));
		}
		if (DateUtil.joinTime(rewardT.end + ":00").getTime() < current) {
			throw new NoteException(
					Messages.getString("SendJunLingControler.TimeEnd"));
		}

		// 发奖励
		// 发军令
		roleRt.getRewardControler().acceptReward(
				Const.PropertyName.JUNLING_TEMPLATE_ID, rewardT.num);
		// 发元宝
		List<RandomRmbyReward> randomRewards = new ArrayList<RandomRmbyReward>();
		randomRewards.add(new RandomRmbyReward(rewardT.chance));
		// 执行概率
		RandomRange<RandomRmbyReward> random = new RandomRange<RandomRmbyReward>(
				randomRewards);
		RandomRmbyReward reward = random.random();
		// 得到了元宝奖励
		int rmbyNum = 0;
		if (reward != null) {
			Random r = new Random();
			rmbyNum = r.nextInt(rewardT.max + 1 - rewardT.min) + rewardT.min;
			// 增加元宝
			roleRt.winYuanbao(rmbyNum, true);
		}
		// 增加已领取
		addReceivedReward(id);
		// 事件通知
		acceptSendJunLing.onAcceptSendJunLing(rewardT.num, rmbyNum);

		return rmbyNum;
	}

	public static class RandomRmbyReward implements IRandomHitable {
		private int rank;

		public RandomRmbyReward(int rank) {
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	@Override
	public boolean hasAcceptAll() {
		List<SendJunLingT> rewards = XsgActivityManage.getInstance()
				.getCurrentSendJunLingRewards();
		if (rewards == null) {
			return true;
		}
		// 更新领取奖励的状态
		updateReceiveStatus();

		for (SendJunLingT reward : rewards) {
			if (!isReceivedReward(reward.id)) {
				return false;
			}
		}
		return true;
	}

}
