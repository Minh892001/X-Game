package com.morefun.XSanGo.superCharge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AMD_SuperCharge_getRaffleView;
import com.XSanGo.Protocol.AMD_SuperCharge_getReceivedViews;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RaffleItemInfo;
import com.XSanGo.Protocol.RaffleReceivedView;
import com.XSanGo.Protocol.RaffleView;
import com.XSanGo.Protocol.ReceivedRaffleInfo;
import com.XSanGo.Protocol.SumChargeView;
import com.XSanGo.Protocol.SuperChargeView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleSuperCharge;
import com.morefun.XSanGo.db.game.RoleSuperTurntable;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.ISuperChargeRaffleNum;
import com.morefun.XSanGo.event.protocol.ISuperRaffle;
import com.morefun.XSanGo.event.protocol.ISuperuCharge;
import com.morefun.XSanGo.event.protocol.ITenSuperRotation;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.ChargeItemT;
import com.morefun.XSanGo.vip.XsgVipManager;

@RedPoint
public class SuperChargeControler implements ISuperChargeController, ICharge {

	private IRole iRole;
	private Role role;
	private ISuperuCharge superChargeEvent;
	private ISuperChargeRaffleNum superChargeRaffleEvent;
	private ISuperRaffle superRaffleEvent;
	private ITenSuperRotation tenSuperRotationEvent;

	public SuperChargeControler(IRole iRole, Role role) {
		this.iRole = iRole;
		this.role = role;

		superChargeEvent = iRole.getEventControler().registerEvent(ISuperuCharge.class);
		superChargeRaffleEvent = iRole.getEventControler().registerEvent(ISuperChargeRaffleNum.class);
		superRaffleEvent = iRole.getEventControler().registerEvent(ISuperRaffle.class);
		tenSuperRotationEvent = iRole.getEventControler().registerEvent(ITenSuperRotation.class);
		iRole.getEventControler().registerHandler(ICharge.class, this);

	}

	@Override
	public SuperChargeView getSuperChargeView() throws NoteException {
		List<SuperChargeT> chargeRebateList = XsgSuperChargeManager.getInstance().getChargeConfigList();
		SumChargeView[] viewArr = new SumChargeView[chargeRebateList.size()];

		int totalCharge = this.getTotalCharge();
		boolean redFlag = false;
		for (int i = 0; i < chargeRebateList.size(); i++) {
			SuperChargeT chargeRebate = chargeRebateList.get(i);
			viewArr[i] = new SumChargeView();

			viewArr[i].id = chargeRebate.id;
			viewArr[i].leftChargeAmount = totalCharge - chargeRebate.money;
			viewArr[i].money = chargeRebate.money;
			viewArr[i].ration = chargeRebate.rebate;
			viewArr[i].acceptChargeFlag = isAlreadyReceived(chargeRebate.id);
			
			if (viewArr[i].leftChargeAmount >= 0 && viewArr[i].acceptChargeFlag == 0) {
				redFlag = true;
			}
		}

		SuperChargeView view = new SuperChargeView();
		view.sumChargeList = this.putUnacceptedLast(viewArr);
		view.raffleNum = this.getRaffleNum();
		view.totalCharge = totalCharge;
		view.welfareDesc = XsgSuperChargeManager.getInstance().getDesc().desc;

		List<RaffleNumT> raffleNumList = XsgSuperChargeManager.getInstance().getRaffleNumList();
		RaffleNumT raffleNumT = raffleNumList.get(0);

		if (DateUtil.isBetween(raffleNumT.beginTime, raffleNumT.endTime)) {
			view.welfareDesc = raffleNumT.content;
		}
		
		if(redFlag){
			view.redPoiFlag = 1;
		}
		
		return view;
	}

	private int isAlreadyReceived(int scriptId) {

		int flag = 0;
		RoleSuperCharge rsc = this.getRoleSuperCharge();

		if (TextUtil.isBlank(rsc.getScriptId())) {
			return flag;
		}

		int[] scriptIdArr = TextUtil.GSON.fromJson(rsc.getScriptId(), int[].class);

		for (int i = 0; i < scriptIdArr.length; i++) {
			if (scriptIdArr[i] == scriptId) {
				return 1;
			}
		}

		return flag;
	}

	@Override
	public SuperChargeView receiveReward(int id) throws NoteException {
		SuperChargeT chargeConfig = null;

		List<SuperChargeT> chargeConfigList = XsgSuperChargeManager.getInstance().getChargeConfigList();
		// 参数合理性验证
		if (id <= 0 || id > chargeConfigList.size()) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}

		for (SuperChargeT superChargeT : chargeConfigList) {
			if (superChargeT.id == id) {
				chargeConfig = superChargeT;
				break;
			}
		}

		if (chargeConfig == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}

		if (chargeConfig.money > this.getTotalCharge()) {
			throw new NoteException(Messages.getString("DayChargeControler.3"));
		}

		// 重复领取判断
		if (this.isAlreadyReceived(id) == 1) {
			throw new NoteException(Messages.getString("DayChargeControler.4"));
		}

		// 结算奖励
		try {
			iRole.winYuanbao(chargeConfig.rebate, false);
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}

		// 记录数据
		this.updateRecored(id);
		superChargeEvent.onReceiveChargeReward(id);

		return this.getSuperChargeView();
	}

	/**
	 * 将数组中未领取的数据放在前面
	 * 
	 * @param sumChargeList
	 * @return
	 */
	private SumChargeView[] putUnacceptedLast(SumChargeView[] sumChargeList) {
		List<SumChargeView> list1 = new ArrayList<SumChargeView>();// 装载已经领取过的数据
		List<SumChargeView> list2 = new ArrayList<SumChargeView>();// 未领取过的数据

		for (int i = 0; i < sumChargeList.length; i++) {
			if (sumChargeList[i].acceptChargeFlag == 1) {// 已经领取
				list1.add(sumChargeList[i]);
			} else {
				list2.add(sumChargeList[i]);
			}
		}

		if (list1.size() > 0) {
			list2.addAll(list1);
		}

		return list2.toArray(new SumChargeView[0]);
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(params.item);
		// 超级充值数据变更
		this.refreshRaffleNum(template, 0);
		iRole.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}

	/**
	 * 获取本系统中的充值金额，这里并非真实的总金额，因为本系统中该金额会更新 这里是元宝，因时间关系按照金额*10处理
	 * 
	 * 这里是元宝，因时间关系按照金额*10处理
	 * 
	 * @return
	 */
	private int getTotalCharge() {
		// 每次充值达到最大活动档位时候数据库中的金额会全部更新
		RoleSuperCharge roleSuperCharge = this.getRoleSuperCharge();
		return roleSuperCharge.getChargeAmount();
	}

	private RoleSuperCharge getRoleSuperCharge() {
		RoleSuperCharge result = this.role.getRoleSuperCharge();
		if (result == null) {
			result = new RoleSuperCharge(this.role.getId(), role, null, 0, 0);
			this.role.setRoleSuperCharge(result);
		}
		return result;
	}

	private int getRaffleNum() {

		RoleSuperCharge roleSuperCharge = role.getRoleSuperCharge();
		return roleSuperCharge == null ? 0 : roleSuperCharge.getRaffleNum();
	}

	/**
	 * 领取奖励后更新记录
	 * 
	 * @param id
	 *            配置脚本Id
	 */
	private void updateRecored(int id) {
		RoleSuperCharge roleSuperCharge = this.getRoleSuperCharge();

		// 更新领取记录
		String scriptId = roleSuperCharge.getScriptId();
		Integer[] scriptIdArr = TextUtil.GSON.fromJson(scriptId, Integer[].class);
		ArrayList<Integer> ids = new ArrayList<Integer>();

		if (scriptIdArr != null) {
			for (int i = 0; i < scriptIdArr.length; i++) {
				ids.add(scriptIdArr[i]);
			}
		}

		ids.add(id);
		Integer[] array = ids.toArray(new Integer[0]);
		roleSuperCharge.setScriptId(TextUtil.GSON.toJson(array));
		// 是否重新刷新所有领取记录Id并更新金额
		List<SuperChargeT> chargeConfigList = XsgSuperChargeManager.getInstance().getChargeConfigList();

		if (array.length == chargeConfigList.size()) {
			// 当所有配置的奖励都领取过后，充值脚本Id和金额数据
			roleSuperCharge.setScriptId(null);
			int value = chargeConfigList.get(chargeConfigList.size() - 1).money;
			roleSuperCharge.setChargeAmount(roleSuperCharge.getChargeAmount() - value);
		}
	}

	@Override
	public void refreshRaffleNum(ChargeItemT template, int reduce) {
		// 2015111102:00
		RoleSuperCharge roleSuperCharge = this.getRoleSuperCharge();
		List<RaffleNumT> raffleNumList = XsgSuperChargeManager.getInstance().getRaffleNumList();

		int num = 0;
		for (RaffleNumT raffleNumT : raffleNumList) {
			if (raffleNumT.money == template.rmb) {
				num = raffleNumT.raffleNum;
				if (DateUtil.isBetween(raffleNumT.beginTime, raffleNumT.endTime)) {
					num = raffleNumT.acRaffleNum;
				}
				break;
			}
		}

		int chargeAmount = template.yuanbao;
		// 重置总金额和抽奖次数
		int oldValue = roleSuperCharge.getRaffleNum();
		roleSuperCharge.setRaffleNum(num + oldValue);
		roleSuperCharge.setChargeAmount(roleSuperCharge.getChargeAmount() + chargeAmount);

		superChargeRaffleEvent.onRaffleNumUpdate(oldValue, roleSuperCharge.getRaffleNum());
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		try {
			SuperChargeView view = this.getSuperChargeView();

			for (SumChargeView sViews : view.sumChargeList) {
				// TODO zhangxiaojun 只要没领过就能领？这里请封装统一的判断方法，在红点，领取等逻辑统一调用
				if (sViews.leftChargeAmount >= 0 && sViews.acceptChargeFlag == 0) {
					return new MajorUIRedPointNote(MajorMenu.SuperChargeMenu, false);
				}
			}

		} catch (NoteException e) {
			return null;
		}
		return null;

	}

	@Override
	public RaffleReceivedView acceptRaffleReward() throws NoteException {
		// 获取可抽奖次数
		if (role.getRoleSuperCharge() == null) {
			throw new NoteException(Messages.getString("SuperChargeControler.0"));
		}
		int raffleNum = role.getRoleSuperCharge().getRaffleNum();
		if (raffleNum <= 0) {
			throw new NoteException(Messages.getString("SuperChargeControler.0"));
		}
		// 获取随机道具
		RaffleItemT randomRaffle = this.getRandomRaffle();
		if (randomRaffle == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}
		// 增加道具
		IItem acceptReward = iRole.getRewardControler().acceptReward(randomRaffle.itemId, randomRaffle.num);

		// 更新已领取记录
		RoleSuperTurntable rst = new RoleSuperTurntable(GlobalDataManager.getInstance().generatePrimaryKey(),
				iRole.getName(), role, randomRaffle.id, randomRaffle.announceFlag, new Date(), iRole.getVipLevel());
		role.getRoleSuperTurntable().add(rst);
		XsgSuperChargeManager.getInstance().addRaffleRecord(rst, randomRaffle);

		// 更新可领取次数
		role.getRoleSuperCharge().setRaffleNum(raffleNum - 1);

		RaffleReceivedView view = new RaffleReceivedView();
		// 抽奖次数达到限制，必定获取指定物品
		int raffleTime = role.getRoleSuperTurntable().size();
		SuperChargeBaseParamT desc = XsgSuperChargeManager.getInstance().getDesc();

		List<RaffleNumT> raffleNumList = XsgSuperChargeManager.getInstance().getRaffleNumList();
		RaffleNumT raffleNumT = raffleNumList.get(0);

		view.requiredItem = desc.item;
		if (DateUtil.isBetween(raffleNumT.beginTime, raffleNumT.endTime)) {
			view.requiredItem = raffleNumT.reqItem;
		}
		if (raffleTime < desc.raffleNum) {
			view.requiredNum = desc.raffleNum - raffleTime;
		} else {
			int i = raffleTime % desc.raffleNum;
			view.requiredNum = desc.raffleNum - i;

			if (i == 0) {// 每10次发邮件奖励
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("$f", String.valueOf(raffleTime)); //$NON-NLS-1$

				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				rewardMap.put(view.requiredItem, 1);
				iRole.getMailControler().receiveRoleMail(MailTemplate.SuperRotation, rewardMap, replaceMap);

				tenSuperRotationEvent.onRefresh(1, view.requiredItem);
			}

		}

		// 公告
		if (randomRaffle.announceFlag == 1) {
			List<ChatAdT> adTList = XsgChatManager.getInstance()
					.getAdContentMap(XsgChatManager.AdContentType.SuperRaffle);

			if (adTList != null && adTList.size() > 0) {
				XsgChatManager chat = XsgChatManager.getInstance();
				ChatAdT adT = adTList.get(0);
				Map<String, String> replaceMap = new HashMap<String, String>();

				if (!randomRaffle.itemId.equals("rmby")) {// 非元宝公告
					adT = adTList.get(1);
					replaceMap.put("~item~", randomRaffle.itemId);
				} else {
					replaceMap.put("XXX", randomRaffle.num + "");
				}

				replaceMap.put("~role_id~", this.iRole.getRoleId());
				replaceMap.put("~role_name~", this.iRole.getName());
				replaceMap.put("~role_vip~", this.iRole.getVipLevel() + "");
				XsgChatManager.getInstance().sendAnnouncementItem(acceptReward, this.iRole.getChatControler()
						.parseAdConent(chat.replaceRoleContent(adT.content, this.iRole), replaceMap));
			}
		}
		
		if(null != iRole.getSuperRaffleControlle().getRedPointNote()){
			view.redPoiFlag = 1;
		}
		
		superRaffleEvent.onAcceptRaffleReward(raffleNum, randomRaffle.itemId, randomRaffle.num,
				role.getRoleSuperCharge().getRaffleNum());
		RaffleItemInfo raffleItemInfo = new RaffleItemInfo(randomRaffle.id, randomRaffle.itemId, randomRaffle.num);
		view.itemInfo = raffleItemInfo;
		return view;
	}

	/**
	 * 拼接领取间隔时间 大于30天显示一个月（依次类推），大于24小时显示天，大于60分钟显示小时，大于60秒显示分钟，小于60秒显示秒
	 * 
	 * @param lastReceiveTime
	 * @return
	 */
	private String getReceivedTimeStr(Date lastReceiveTime) {
		String receiveTime = "";
		int dayNum = DateUtil.compareDate(new Date(), lastReceiveTime);
		if (dayNum >= 30) {
			receiveTime = dayNum / 30 + "月";
		} else if (dayNum >= 1) {
			receiveTime = dayNum + "天";
		} else {
			long compareTime = DateUtil.compareTime(new Date(), lastReceiveTime);

			if (compareTime / (1000 * 60 * 60) >= 1) {// 时
				receiveTime = compareTime / (1000 * 60 * 60) + "小时";
			} else if (compareTime / (1000 * 60) >= 1) {// 分
				receiveTime = compareTime / (1000 * 60) + "分钟";
			} else {
				receiveTime = compareTime / (1000) + "秒";
			}
		}
		return receiveTime;
	}

	/**
	 * 获取随机抽奖道具
	 * 
	 * @return
	 */
	private RaffleItemT getRandomRaffle() {

		List<RaffleItemT> raffleList = XsgSuperChargeManager.getInstance().getRafflesList();

		List<SuperChargeControler.RandomProp> randomProps = new ArrayList<SuperChargeControler.RandomProp>();
		for (RaffleItemT p : raffleList) {
			randomProps.add(new RandomProp(p.id, p.weight));
		}

		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();

		RaffleItemT result = null;
		for (RaffleItemT item : raffleList) {
			if (item.id == randomReward.id) {
				result = item;
				break;
			}
		}

		return result;

	}

	static class RandomProp implements IRandomHitable {
		public int id;
		public int rank;

		public RandomProp(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	@Override
	public void getReceivedViews(final AMD_SuperCharge_getReceivedViews __cb) {
		final List<RoleSuperTurntable> recievedResults = XsgSuperChargeManager.getInstance().getRecievedResults();
		// 领奖信息
		final ReceivedRaffleInfo[] receivedList = new ReceivedRaffleInfo[recievedResults.size()];

		final List<String> findIdList = new ArrayList<String>(recievedResults.size());
		for (RoleSuperTurntable rst : recievedResults) {
			findIdList.add(rst.getRole().getId());
		}

		XsgRoleManager.getInstance().loadRoleAsync(findIdList, new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < receivedList.length; i++) {
					RoleSuperTurntable roleSuperTurntable = recievedResults.get(i);
					IRole findRole = XsgRoleManager.getInstance().findRoleById(findIdList.get(i));
					RaffleItemT item = XsgSuperChargeManager.getInstance()
							.getRaffleItemById(roleSuperTurntable.getScriptId());

					Date lastReceiveTime = roleSuperTurntable.getLastReceiveTime();

					String receiveTime = getReceivedTimeStr(lastReceiveTime);

					String name = findRole == null ? roleSuperTurntable.getRoleName() : findRole.getName();
					int vipLevel = findRole == null ? roleSuperTurntable.getVipLevel() : findRole.getVipLevel();
					receivedList[i] = new ReceivedRaffleInfo(name, item.itemId, receiveTime, item.num, vipLevel);

				}
				
				__cb.ice_response(LuaSerializer.serialize(receivedList));
			}

		});

	}

	@Override
	public void getRaffleView(final AMD_SuperCharge_getRaffleView __cb) {

		final SuperChargeBaseParamT baseConf = XsgSuperChargeManager.getInstance().getDesc();
		final List<RaffleItemT> rafflesList = XsgSuperChargeManager.getInstance().getRafflesList();
		final List<RoleSuperTurntable> recievedResults = XsgSuperChargeManager.getInstance().getRecievedResults();
		// 领奖信息
		final ReceivedRaffleInfo[] receivedList = new ReceivedRaffleInfo[recievedResults.size()];

		final List<String> findIdList = new ArrayList<String>(recievedResults.size());
		for (RoleSuperTurntable rst : recievedResults) {
			String id = rst.getRole().getId();
			findIdList.add(id);
		}

		XsgRoleManager.getInstance().loadRoleAsync(findIdList, new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < receivedList.length; i++) {
					RoleSuperTurntable roleSuperTurntable = recievedResults.get(i);
					IRole findRole = XsgRoleManager.getInstance().findRoleById(findIdList.get(i));
					RaffleItemT item = XsgSuperChargeManager.getInstance()
							.getRaffleItemById(roleSuperTurntable.getScriptId());

					Date lastReceiveTime = roleSuperTurntable.getLastReceiveTime();

					String receiveTime = getReceivedTimeStr(lastReceiveTime);

					String name = findRole == null ? roleSuperTurntable.getRoleName() : findRole.getName();
					int vipLevel = findRole == null ? roleSuperTurntable.getVipLevel() : findRole.getVipLevel();
					receivedList[i] = new ReceivedRaffleInfo(name, item.itemId, receiveTime, item.num, vipLevel);

				}
				// 抽奖信息
				RaffleItemInfo[] itemArr = new RaffleItemInfo[rafflesList.size()];
				for (int i = 0; i < itemArr.length; i++) {
					RaffleItemT raffleItemT = rafflesList.get(i);
					itemArr[i] = new RaffleItemInfo(raffleItemT.id, raffleItemT.itemId, raffleItemT.num);
				}

				RaffleView view = new RaffleView();
				view.raffleItemList = itemArr;
				view.receivedList = receivedList;
				// 必中物品剩余抽奖次数
				int raffleNum = baseConf.raffleNum;
				if (role.getRoleSuperTurntable() != null) {
					int size = role.getRoleSuperTurntable().size();
					if (size < raffleNum) {
						raffleNum = raffleNum - size;
					} else {
						int temp = size % raffleNum;
						raffleNum = raffleNum - temp;
					}
				}
				view.requiredNum = raffleNum;
				view.itemId = baseConf.item;
				view.num = role.getRoleSuperCharge() == null ? 0 : role.getRoleSuperCharge().getRaffleNum();
				view.desc = baseConf.helpInfo;

				List<RaffleNumT> raffleNumList = XsgSuperChargeManager.getInstance().getRaffleNumList();
				RaffleNumT raffleNumT = raffleNumList.get(0);

				if (DateUtil.isBetween(raffleNumT.beginTime, raffleNumT.endTime)) {
					view.desc = raffleNumT.helpIno;
					view.itemId = raffleNumT.reqItem;
				}

				__cb.ice_response(LuaSerializer.serialize(view));
			}

		});

	}

	@Override
	public void addRaffleNum(int num) {
		role.getRoleSuperCharge().setRaffleNum(role.getRoleSuperCharge().getRaffleNum() + num);
		if (role.getRoleSuperCharge().getRaffleNum() < 0) {
			role.getRoleSuperCharge().setRaffleNum(0);
		}
	}
}
