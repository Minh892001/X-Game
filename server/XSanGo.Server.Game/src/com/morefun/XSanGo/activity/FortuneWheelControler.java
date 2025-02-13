package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.FortuneWheelResultView;
import com.XSanGo.Protocol.FortuneWheelRewardItemView;
import com.XSanGo.Protocol.FortuneWheelView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFortuneWheel;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.IFortuneWheel;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.vip.ChargeItemT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * @author qinguofeng
 */
@RedPoint(isTimer = true)
public class FortuneWheelControler implements IFortuneWheelControler, ICharge {

	private IRole roleRt;
	private Role roleDB;

	private RoleFortuneWheel roleFortuneWheel;

	private IFortuneWheel wortuneWheelEvent;

	private boolean firstOpen = true;

	public FortuneWheelControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		// 注册重置事件
		roleRt.getEventControler().registerHandler(ICharge.class, this);

		wortuneWheelEvent = roleRt.getEventControler().registerEvent(IFortuneWheel.class);

		roleFortuneWheel = roleDB.getRoleFortuneWheel();
		initRoleFortuneWheel();

		firstOpen = true;
	}

	private void initRoleFortuneWheel() {
		if (roleFortuneWheel == null) {
			roleFortuneWheel = new RoleFortuneWheel(roleDB);
			ActivityT activityT = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.FortuneWheel);
			if (activityT != null) {
				roleFortuneWheel.setActivityStartTime(activityT.startTime);
			}
		}
		if (checkWheelCount()) {
			saveToDB();
		}
	}

	private void saveToDB() {
		roleDB.setRoleFortuneWheel(roleFortuneWheel);
	}

	/**
	 * 是否需要重置
	 * */
	private boolean needReset(FortuneWheelConfigT config) {
		if (roleFortuneWheel.getUpdateTime() == null) {
			return true;
		}
		Date lastDate = roleFortuneWheel.getUpdateTime();
		Date updateTime = DateUtil.joinTime(config.resetTime);
		Calendar updateCal = Calendar.getInstance();
		updateCal.setTime(updateTime);
		Date lastUpdateTime = DateUtil.addDays(updateCal, -1).getTime();

		return DateUtil.isPass(updateTime, lastDate) || DateUtil.isPass(lastUpdateTime, lastDate);
	}

	/**
	 * 检测累计次数和剩余次数
	 * */
	private boolean checkWheelCount() {
		FortuneWheelConfigT config = XsgActivityManage.getInstance().getFortuneWheelConfig();
		if (needReset(config)) {
			if (1 == config.resetType) {
				roleFortuneWheel.setLastCount(0);
			}
			roleFortuneWheel.setTotalCount(0);
			roleFortuneWheel.setChargeCount(0);
			roleFortuneWheel.setUpdateTime(Calendar.getInstance().getTime());
			return true;
		}

		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.FortuneWheel);
		if (activityT != null && !activityT.startTime.equals(roleFortuneWheel.getActivityStartTime())) { // 活动重新开始
			roleFortuneWheel.setLastCount(0);
			roleFortuneWheel.setTotalCount(0);
			roleFortuneWheel.setChargeCount(0);
			roleFortuneWheel.setUpdateTime(Calendar.getInstance().getTime());
			roleFortuneWheel.setActivityStartTime(activityT.startTime);
			return true;
		}

		return false;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.FortuneWheel)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if (roleRt.getChatControler().getChatCb() == null) {
			return null;
		}
		checkWheelCount();
		if (roleFortuneWheel.getLastCount() > 0 || firstOpen) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.FortuneWheel, true);
		}
		return null;
	}

	private FortuneWheelRewardTypeT getCurrentRewardType() {
		List<FortuneWheelRewardTypeT> typeList = XsgActivityManage.getInstance().getFortuneWheelRewardTypes();
		long current = System.currentTimeMillis();
		for (FortuneWheelRewardTypeT type : typeList) {
			if (DateUtil.parseDate(type.startTime).getTime() <= current
					&& current <= DateUtil.parseDate(type.endTime).getTime()) {
				return type;
			}
		}
		return null;
	}

	@Override
	public FortuneWheelView getFortuneWheelView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		// 检查是否需要重置
		if (checkWheelCount()) {
			saveToDB();
		}

		FortuneWheelConfigT config = manager.getFortuneWheelConfig();
		FortuneWheelVipT vipConfig = manager.getFortuneWheelVipConfig(roleRt.getVipLevel());
		// 正在进行中的转盘奖励类型
		FortuneWheelRewardTypeT wheelType = getCurrentRewardType();
		if (wheelType == null) {
			throw new NoteException(Messages.getString("FortuneWheelControler.Closed"));
		}
		// 奖励
		List<FortuneWheelRewardT> rewards = manager.getFortuneWheelRewardsByType(wheelType.type);
		// 构造奖励列表
		List<FortuneWheelRewardItemView> rewardItemList = new ArrayList<FortuneWheelRewardItemView>();

		for (FortuneWheelRewardT reward : rewards) {
			rewardItemList
					.add(new FortuneWheelRewardItemView(reward.id, reward.templateId, reward.num, reward.itemType));
		}
		FortuneWheelRewardItemView[] rewardItems = rewardItemList.toArray(new FortuneWheelRewardItemView[0]);

		// vip等级和最大次数对应列表
		List<FortuneWheelVipT> vipCountList = manager.getFortuneWheelVipConfigs();
		List<IntIntPair> vipCountPair = new ArrayList<IntIntPair>();
		for (FortuneWheelVipT vipT : vipCountList) {
			IntIntPair iip = new IntIntPair(vipT.vipLv, vipT.maxCount);
			vipCountPair.add(iip);
		}
		// 构造返回的view
		FortuneWheelView view = new FortuneWheelView(roleFortuneWheel.getLastCount(), roleFortuneWheel.getTotalCount(),
				vipConfig.maxCount,
				((DateUtil.parseDate(wheelType.endTime).getTime() - System.currentTimeMillis()) / 1000), rewardItems,
				config.helpContent, vipCountPair.toArray(new IntIntPair[0]));

		return view;
	}

	private List<FortuneWheelRewardT> doFortuneWheelInner(int count) throws NoteException {
		// 正在进行中的转盘奖励类型
		FortuneWheelRewardTypeT wheelType = getCurrentRewardType();
		if (wheelType == null) {
			throw new NoteException(Messages.getString("FortuneWheelControler.Closed"));
		}

		// 开始结束时间
		long startTime = DateUtil.parseDate(wheelType.startTime).getTime();
		long endTime = DateUtil.parseDate(wheelType.endTime).getTime();
		long currentTime = System.currentTimeMillis();
		if (!(startTime <= currentTime && currentTime <= endTime)) {
			throw new NoteException(Messages.getString("FortuneWheelControler.Closed"));
		}
		// 奖励
		List<FortuneWheelRewardT> rewards = XsgActivityManage.getInstance()
				.getFortuneWheelRewardsByType(wheelType.type);
		// 构造随机
		List<RandomFortuneWheelReward> randomRewards = new ArrayList<FortuneWheelControler.RandomFortuneWheelReward>();
		for (FortuneWheelRewardT reward : rewards) {
			randomRewards.add(new RandomFortuneWheelReward(reward, reward.weight));
		}
		// 执行随机
		RandomRange<RandomFortuneWheelReward> random = new RandomRange<FortuneWheelControler.RandomFortuneWheelReward>(
				randomRewards);
		List<FortuneWheelRewardT> totalItems = new ArrayList<FortuneWheelRewardT>();
		for (int i = 0; i < count; i++) {
			RandomFortuneWheelReward randomReward = random.random();
			// 得到的奖励
			FortuneWheelRewardT reward = randomReward.getReward();

			totalItems.add(reward);
		}

		return totalItems;
	}

	@Override
	public FortuneWheelResultView doFortuneWheel() throws NoteException {
		// 检查次数
		if (roleFortuneWheel.getLastCount() < 1) {
			throw new NoteException(Messages.getString("FortuneWheelControler.UseUp"));
		}
		// 执行随机
		List<FortuneWheelRewardT> rewardList = doFortuneWheelInner(1);
		// 获得的奖励
		FortuneWheelRewardT reward = rewardList.get(0);
		// 更改玩家道具数量
		IItem item = roleRt.getRewardControler().acceptReward(reward.templateId, reward.num);
		// 更新剩余次数
		roleFortuneWheel.setLastCount(roleFortuneWheel.getLastCount() - 1);

		// 公告
		if (reward.notice == 1) {
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.FortuneWheel);
			if (adTList.size() > 0) {
				XsgChatManager chat = XsgChatManager.getInstance();
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("XXX", reward.num + "");
				XsgChatManager.getInstance().sendAnnouncement(
						this.roleRt.getChatControler().parseAdConent(chat.replaceRoleContent(adT.content, roleRt),
								replaceMap));
			}
		}

		// 返回
		List<ItemView> itemViews = new ArrayList<ItemView>();
		if (item != null) {
			itemViews.add(item.getView());
		} else {
			itemViews.add(new ItemView("", ItemType.DefaultItemType, reward.templateId, reward.num, ""));
		}
		ItemView[] itemsRes = itemViews.toArray(new ItemView[0]);

		FortuneWheelResultView resultView = new FortuneWheelResultView(reward.id, itemsRes);
		// 事件
		wortuneWheelEvent.onFortuneWheel(1, itemsRes, roleFortuneWheel.getLastCount());

		return resultView;
	}

	@Override
	public BuyHeroResult[] doRortuneWheelFor10() throws NoteException {
		// 检查次数
		if (roleFortuneWheel.getLastCount() < 10) {
			throw new NoteException(Messages.getString("FortuneWheelControler.UseUp"));
		}
		// 执行随机
		List<FortuneWheelRewardT> rewardList = doFortuneWheelInner(10);
		// 更改玩家道具数量
		List<BuyHeroResult> result = new ArrayList<BuyHeroResult>();
		// 总奖励
		int sum = 0;
		List<ItemView> rewardItems = new ArrayList<ItemView>();
		for (FortuneWheelRewardT reward : rewardList) {
			// 给玩家加奖励
			IItem item = roleRt.getRewardControler().acceptReward(reward.templateId, reward.num);

			if (item == null) {
				rewardItems.add(new ItemView("", ItemType.DefaultItemType, reward.templateId, reward.num, ""));
			} else {
				rewardItems.add(item.getView());
			}

			sum += reward.num;

			result.add(new BuyHeroResult(0, reward.templateId, reward.num, 0, 0, 0));
		}
		// 更新剩余次数
		roleFortuneWheel.setLastCount(roleFortuneWheel.getLastCount() - 10);
		// 公告
		List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
				XsgChatManager.AdContentType.FortuneWheel10);
		if (adTList.size() > 0) {
			ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
			XsgChatManager chat = XsgChatManager.getInstance();
			Map<String, String> replaceMap = new HashMap<String, String>();
			replaceMap.put("XXX", sum + "");
			XsgChatManager.getInstance().sendAnnouncement(
					this.roleRt.getChatControler().parseAdConent(chat.replaceRoleContent(adT.content, roleRt),
							replaceMap));
		}
		// 事件
		wortuneWheelEvent.onFortuneWheel(10, rewardItems.toArray(new ItemView[0]), roleFortuneWheel.getLastCount());

		// 返回
		return result.toArray(new BuyHeroResult[0]);
	}

	public static class RandomFortuneWheelReward implements IRandomHitable {
		private int rank;
		private FortuneWheelRewardT reward;

		public RandomFortuneWheelReward(FortuneWheelRewardT reward, int rank) {
			this.reward = reward;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}

		public FortuneWheelRewardT getReward() {
			return reward;
		}
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.FortuneWheel);
		if (activityT == null) {
			return;
		}
		Date startTime = DateUtil.parseDate(activityT.startTime);
		Date endTime = DateUtil.parseDate(activityT.endTime);
		long current = System.currentTimeMillis();
		// 不在活动期间
		if (current < startTime.getTime() || endTime.getTime() < current) {
			return;
		}
		// 检查重置
		if (checkWheelCount()) {
			saveToDB();
		}
		ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(params.item);
		FortuneWheelConfigT config = XsgActivityManage.getInstance().getFortuneWheelConfig();
		FortuneWheelVipT vipT = XsgActivityManage.getInstance().getFortuneWheelVipConfig(roleRt.getVipLevel());
		// 累加用户重置金额
		roleFortuneWheel.setChargeCount(roleFortuneWheel.getChargeCount() + template.rmb);
		while (roleFortuneWheel.getChargeCount() >= config.prize) {
			// 如果已达到最大次数则不做操作
			if (roleFortuneWheel.getTotalCount() >= vipT.maxCount) {
				break;
			}
			// 增加次数
			roleFortuneWheel.setTotalCount(roleFortuneWheel.getTotalCount() + config.count);
			roleFortuneWheel.setLastCount(roleFortuneWheel.getLastCount() + config.count);
			// 更新累计金额
			roleFortuneWheel.setChargeCount(roleFortuneWheel.getChargeCount() - config.prize);
		}
		saveToDB();
	}

	@Override
	public void setFirstOpen(boolean v) {
		firstOpen = v;
	}

	@Override
	public void addLastCount(int count) {
		roleFortuneWheel.setLastCount(roleFortuneWheel.getLastCount() + count);
		if (roleFortuneWheel.getLastCount() < 0) {
			roleFortuneWheel.setLastCount(0);
		}
	}
}
