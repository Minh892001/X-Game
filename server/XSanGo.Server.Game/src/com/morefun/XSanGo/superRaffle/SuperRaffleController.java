package com.morefun.XSanGo.superRaffle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RaffleItemInfo;
import com.XSanGo.Protocol.RaffleReceivedView;
import com.XSanGo.Protocol.RaffleView;
import com.XSanGo.Protocol.ReceivedRaffleInfo;
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
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.superCharge.RaffleItemT;
import com.morefun.XSanGo.superCharge.SuperChargeBaseParamT;
import com.morefun.XSanGo.superCharge.XsgSuperChargeManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.RandomRange;

@RedPoint
public class SuperRaffleController implements ISuperRaffleController, ICharge {

	private IRole roleRt;
	private Role roleDb;

	public SuperRaffleController(IRole roleRt, Role roleDb) {
		super();
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		roleRt.getEventControler().registerHandler(ICharge.class, this);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		RoleSuperCharge roleSuperCharge = roleDb.getRoleSuperCharge();
		int num = roleSuperCharge == null ? 0 : roleSuperCharge.getRaffleNum();
		if (num > 0) {
			return new MajorUIRedPointNote(MajorMenu.SuperRaffleMenu, false);
		}
		return null;
	}

	@Override
	public RaffleView getRaffleView() {
		SuperChargeBaseParamT baseConf = XsgSuperChargeManager.getInstance().getDesc();
		List<RaffleItemT> rafflesList = XsgSuperChargeManager.getInstance().getRafflesList();
		List<RoleSuperTurntable> recievedResults = XsgSuperChargeManager.getInstance().getRecievedResults();
		// 领奖信息
		ReceivedRaffleInfo[] receivedList = new ReceivedRaffleInfo[recievedResults.size()];

		if (recievedResults.size() > 0) {
			for (int i = 0; i < receivedList.length; i++) {
				RoleSuperTurntable roleSuperTurntable = recievedResults.get(i);

				RaffleItemT item = XsgSuperChargeManager.getInstance().getRaffleItemById(
						roleSuperTurntable.getScriptId());

				Date lastReceiveTime = roleSuperTurntable.getLastReceiveTime();

				String receiveTime = this.getReceivedTimeStr(lastReceiveTime);

				receivedList[i] = new ReceivedRaffleInfo(roleSuperTurntable.getRoleName(), item.itemId, receiveTime,
						item.num, roleSuperTurntable.getVipLevel());

			}
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
		if (roleDb.getRoleSuperTurntable() != null) {
			int size = roleDb.getRoleSuperTurntable().size();
			if (size < raffleNum) {
				raffleNum = raffleNum - size;
			} else {
				int temp = size % raffleNum;
				raffleNum = temp == 0 ? raffleNum : raffleNum - temp;
			}
		}
		view.requiredNum = raffleNum;
		view.itemId = baseConf.item;
		view.num = roleDb.getRoleSuperCharge() == null ? 0 : roleDb.getRoleSuperCharge().getRaffleNum();
		view.desc = baseConf.helpInfo;

		return view;
	}

	@Override
	public RaffleReceivedView acceptRaffleReward() throws NoteException {
		// 获取可抽奖次数
		if (roleDb.getRoleSuperCharge() == null) {
			throw new NoteException(Messages.getString("SuperChargeControler.0"));
		}
		int raffleNum = roleDb.getRoleSuperCharge().getRaffleNum();
		if (raffleNum <= 0) {
			throw new NoteException(Messages.getString("SuperChargeControler.0"));
		}
		// 获取随机道具
		RaffleItemT randomRaffle = this.getRandomRaffle();
		if (randomRaffle == null) {
			throw new NoteException(Messages.getString("DayConsumeControler.5"));
		}
		// 增加道具
		IItem acceptReward = roleRt.getRewardControler().acceptReward(randomRaffle.itemId, randomRaffle.num);

		// 更新已领取记录
		RoleSuperTurntable rst = new RoleSuperTurntable(GlobalDataManager.getInstance().generatePrimaryKey(),
				roleRt.getName(), roleDb, randomRaffle.id, randomRaffle.announceFlag, new Date(), roleRt.getVipLevel());
		roleDb.getRoleSuperTurntable().add(rst);
		XsgSuperChargeManager.getInstance().addRaffleRecord(rst, randomRaffle);

		// 更新可领取次数
		roleDb.getRoleSuperCharge().setRaffleNum(raffleNum - 1);

		RaffleReceivedView view = new RaffleReceivedView();
		// 抽奖次数达到限制，必定获取指定物品
		int raffleTime = roleDb.getRoleSuperTurntable().size();
		SuperChargeBaseParamT desc = XsgSuperChargeManager.getInstance().getDesc();

		view.requiredItem = desc.item;
		if (raffleTime < desc.raffleNum) {
			view.requiredNum = desc.raffleNum - raffleTime;
		} else {
			int i = raffleTime % desc.raffleNum;
			view.requiredNum = i == 0 ? 0 : desc.raffleNum - i;

			if (i == 0) {// 每10次发邮件奖励
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("$f", String.valueOf(raffleTime)); //$NON-NLS-1$

				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				rewardMap.put(desc.item, 1);
				roleRt.getMailControler().receiveRoleMail(MailTemplate.SuperRotation, rewardMap, replaceMap);
			}

		}

		// 公告
		if (randomRaffle.announceFlag == 1) {
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.SuperRaffle);

			if (adTList != null && adTList.size() > 0) {
				XsgChatManager chat = XsgChatManager.getInstance();
				ChatAdT adT = adTList.get(0);
				Map<String, String> replaceMap = new HashMap<String, String>();

				if (!randomRaffle.itemId.equals("rmby")) {// 元宝公告
					adT = adTList.get(1);
					replaceMap.put("~item~", randomRaffle.itemId);
				}

				replaceMap.put("~role_id~", this.roleRt.getRoleId());
				replaceMap.put("~role_name~", this.roleRt.getName());
				replaceMap.put("~role_vip~", this.roleRt.getVipLevel() + "");
				XsgChatManager.getInstance().sendAnnouncementItem(
						acceptReward,
						this.roleRt.getChatControler().parseAdConent(chat.replaceRoleContent(adT.content, this.roleRt),
								replaceMap));
			}
		}

		RaffleItemInfo raffleItemInfo = new RaffleItemInfo(randomRaffle.id, randomRaffle.itemId, randomRaffle.num);
		view.itemInfo = raffleItemInfo;
		return view;
	}

	@Override
	public ReceivedRaffleInfo[] getReceivedViews() {
		List<RoleSuperTurntable> recievedResults = XsgSuperChargeManager.getInstance().getRecievedResults();
		// 领奖信息
		ReceivedRaffleInfo[] receivedList = new ReceivedRaffleInfo[recievedResults.size()];

		if (recievedResults.size() > 0) {
			for (int i = 0; i < receivedList.length; i++) {
				RoleSuperTurntable roleSuperTurntable = recievedResults.get(i);

				RaffleItemT item = XsgSuperChargeManager.getInstance().getRaffleItemById(
						roleSuperTurntable.getScriptId());

				Date lastReceiveTime = roleSuperTurntable.getLastReceiveTime();

				String receiveTime = this.getReceivedTimeStr(lastReceiveTime);

				receivedList[i] = new ReceivedRaffleInfo(roleSuperTurntable.getRoleName(), item.itemId, receiveTime,
						item.num, roleSuperTurntable.getVipLevel());

			}
		}
		return receivedList;
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

		List<SuperRaffleController.RandomProp> randomProps = new ArrayList<SuperRaffleController.RandomProp>();
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
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}
}
