package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.MarksmanScoreRankView;
import com.XSanGo.Protocol.MarksmanScoreReward;
import com.XSanGo.Protocol.MarksmanScoreRewardView;
import com.XSanGo.Protocol.MarksmanView;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShootAwardInfo;
import com.XSanGo.Protocol.ShootScoreRankSub;
import com.XSanGo.Protocol.ShootScoreRewardSub;
import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ShootSystemType;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.ShootScoreRank;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IApproveJoin;
import com.morefun.XSanGo.event.protocol.IQuitFaction;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IShootReward;
import com.morefun.XSanGo.event.protocol.IShootScoreChange;
import com.morefun.XSanGo.event.protocol.IShootScoreRecvReward;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 
 * @author zhouming
 * 
 */
@RedPoint(isTimer = true)
public class ShootControler implements IShootControler, IRoleNameChange, IRoleLevelup, IVipLevelUp, IApproveJoin,
		IQuitFaction {
	private IRole rt;
	private IShootScoreChange shootScoreChangeEvent;
	private IShootReward shootRewardEvent;
	private IShootScoreRecvReward shootScoreRecvRewardEvent;

	public ShootControler(IRole rt, Role db) {
		this.rt = rt;
		// 膜拜事件
		IEventControler eventControler = rt.getEventControler();
		shootRewardEvent = eventControler.registerEvent(IShootReward.class);
		shootScoreRecvRewardEvent = eventControler.registerEvent(IShootScoreRecvReward.class);
		shootScoreChangeEvent = eventControler.registerEvent(IShootScoreChange.class);

		eventControler.registerHandler(IRoleLevelup.class, this);
		eventControler.registerHandler(IVipLevelUp.class, this);
		eventControler.registerHandler(IRoleNameChange.class, this);
		eventControler.registerHandler(IApproveJoin.class, this);
		eventControler.registerHandler(IQuitFaction.class, this);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (currentMarksManParamT == null || !XsgActivityManage.getInstance().isInShootActiveTime()) {
			return null;
		}
		// 射箭结束判断
		boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(currentMarksManParamT.startTime),
				DateUtil.parseDate(currentMarksManParamT.endTime));
		boolean free = false;
		// 抽取CD验证
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (isBetween) {
			// 抽取是否免费
			free = XsgActivityManage.getInstance().isShootOneFree(myScoreRank, Calendar.getInstance().getTime());
		}
		// 可领积分宝箱
		int remainderTimes = 0;
		if (myScoreRank != null) {
			remainderTimes = canRecScoreRewardS();
		}
		if (free) {
			return new MajorUIRedPointNote(MajorMenu.MarksManShootFree, false);
		}
		if (remainderTimes > 0) {
			return new MajorUIRedPointNote(MajorMenu.MarksManMenu, false);
		}
		return null;
	}

	/**
	 * 打开百步穿杨界面
	 */
	@Override
	public MarksmanView openMarksmanView(int systemType) throws NoteException {
		MarksmanView view = null;
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (currentMarksManParamT != null && XsgActivityManage.getInstance().isInShootActiveTime()) {
			// // 角色等级是否符合要求
			// if (rt.getLevel() < currentMarksManParamT.lv) {
			// throw new NoteException(Messages.getString("ShootControler.1"));
			// }
			// // 角色VIP等级是否符合要求
			// if ((systemType == ShootSystemType.Shoot.getValue() &&
			// rt.getVipLevel() < currentMarksManParamT.vip)
			// || (systemType == ShootSystemType.ShootSuper.getValue() &&
			// rt.getVipLevel() < currentMarksManParamT.needVIP)) {
			// throw new NoteException(Messages.getString("ShootControler.2"));
			// }
			view = createMarksmanView(currentMarksManParamT, null, systemType);
		} else {
			throw new NoteException(Messages.getString("ShootControler.0"));
		}
		return view;
	}

	/**
	 * 生成百步穿杨界面组合数据
	 * 
	 * @param currentMarksManParamT
	 *            界面配置
	 * @param index
	 *            射中下标列表
	 * @return 百步穿杨界面组合数据
	 */
	private MarksmanView createMarksmanView(MarksManParamT currentMarksManParamT, int[] index, int systemType) {
		Date endTime = DateUtil.parseDate(currentMarksManParamT.endTime);// 射箭结束时间
		MarksmanView view = null;

		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank == null) {
			myScoreRank = addShootScoreRank();
		}

		// 积分奖励集合
		List<ShootRewardT> shootRewardList = XsgActivityManage.getInstance().getShootRewardTsList(systemType);
		IntString[] items = new IntString[shootRewardList.size()];// 奖励道具列表
		for (int i = 0; i < items.length; i++) {
			items[i] = new IntString(shootRewardList.get(i).num, shootRewardList.get(i).itemId);
		}

		// 单次抽奖成本花费
		int singleShootCost = (systemType == ShootSystemType.Shoot.getValue()) ? currentMarksManParamT.deplete01
				: currentMarksManParamT.deplete03;
		boolean free = false;
		if (systemType == ShootSystemType.Shoot.getValue()) { // 超级百步不免费
			free = XsgActivityManage.getInstance().isShootOneFree(myScoreRank, Calendar.getInstance().getTime());
		}
		if (free) {
			singleShootCost = 0;
		}
		// 十连抽成本花费
		int tenShootCost = (systemType == ShootSystemType.Shoot.getValue()) ? currentMarksManParamT.deplete02
				: currentMarksManParamT.deplete04;// 十连抽成本花费

		int singleShootCostType = XsgActivityManage.getInstance().getCurrencyType(
				systemType == ShootSystemType.Shoot.getValue() ? currentMarksManParamT.currency01
						: currentMarksManParamT.currency03);
		int tenShootCostType = XsgActivityManage.getInstance().getCurrencyType(
				systemType == ShootSystemType.Shoot.getValue() ? currentMarksManParamT.currency02
						: currentMarksManParamT.currency04);
		long freeTime = XsgActivityManage.getInstance().getFreeTime(myScoreRank, Calendar.getInstance().getTime());// 距离下一次的免费时间(秒)
		if (freeTime == Integer.MAX_VALUE) {
			// 需求变更 改为到明天刷新时间的 时间差
			freeTime = XsgActivityManage.getInstance().getNextCycleInterval();
		}
		ShootSpecialRewardT shootSpecialRewardT = XsgActivityManage.getInstance().getShootSpecialRewardT(
				XsgActivityManage.ShootType.ShootOne.getValue(), systemType);
		int needNum = shootSpecialRewardT.Number;
		int hasNum = 0;
		int showMyRecord = 1; // 显示我的中奖记录
		if (null != myScoreRank) {
			hasNum = systemType == ShootSystemType.Shoot.getValue() ? myScoreRank.getShootOneCnt() % needNum
					: myScoreRank.getShootCntSuper() % needNum;
			showMyRecord = myScoreRank.getShowMyRecord();
		}
		ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.SHOOT_ACTIVEID);
		Date end = DateUtil.parseDate(t.endTime);// 活动结束时间
		view = new MarksmanView(XsgActivityManage.getInstance().getAwardRecords().toArray(new ShootAwardInfo[0]),
				end.getTime() - System.currentTimeMillis(), items, singleShootCostType, tenShootCostType,
				singleShootCost, tenShootCost, index, currentMarksManParamT.Basis, currentMarksManParamT.one,
				currentMarksManParamT.ten, (int) (freeTime / 1000), needNum, hasNum,
				(endTime.getTime() - System.currentTimeMillis()) / 1000, myScoreRank == null ? 0
						: myScoreRank.getTotalScore(),
				getScoreRewardView(), // 积分奖励信息
				(showMyRecord == 1) ? true : false, currentMarksManParamT.lv,
				systemType == ShootSystemType.Shoot.getValue() ? currentMarksManParamT.vip
						: currentMarksManParamT.needVIP);
		return view;
	}

	/**
	 * 生成物品数组
	 * 
	 * @param itemsMap
	 *            奖励配置表
	 * @return 物品数组
	 */
	private IntString[] wrapRewardItem(Map<String, Integer> itemsMap) {
		if (itemsMap.size() == 0) {
			return null;
		}
		IntString[] items = new IntString[itemsMap.size()];
		Iterator<String> it = itemsMap.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			String itemId = it.next();
			int itemNum = itemsMap.get(itemId);
			items[i++] = new IntString(itemNum, itemId);
		}
		return items;
	}

	/**
	 * 射击抽奖
	 * 
	 * @param shootType
	 *            射击类型
	 * 
	 */
	@Override
	public MarksmanView shootReward(int shootType, int systemType) throws NotEnoughMoneyException,
			NotEnoughYuanBaoException, NotEnoughException, NoteException {
		MarksmanView view = null;
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (currentMarksManParamT != null && XsgActivityManage.getInstance().isInShootActiveTime()) {
			if (!(shootType == XsgActivityManage.ShootType.ShootOne.getValue() || shootType == XsgActivityManage.ShootType.ShootTen
					.getValue())) {
				return null;// 非法类型
			}
			// 判断是否在射击时间内
			boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(currentMarksManParamT.startTime),
					DateUtil.parseDate(currentMarksManParamT.endTime));
			if (!isBetween) {
				throw new NoteException(Messages.getString("ShootControler.0"));
			}

			// 判断VIP和Level
			if (rt.getLevel() < currentMarksManParamT.lv) {
				throw new NoteException(Messages.getString("FormationControler.2"));
			}
			if (systemType == ShootSystemType.Shoot.getValue()) {
				if (rt.getVipLevel() < currentMarksManParamT.vip) {
					throw new NoteException(Messages.getString("ArenaRankControler.51"));
				}
			} else {
				if (rt.getVipLevel() < currentMarksManParamT.needVIP) {
					throw new NoteException(Messages.getString("ArenaRankControler.51"));
				}
			}

			Date currentDate = Calendar.getInstance().getTime();
			ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
			int[] shootIndex = null;
			boolean free = false;
			int score = 0;// 增加的积分
			int beforeScore = myScoreRank.getTotalScore();
			Map<String, Integer> rewardMap = new HashMap<String, Integer>();
			int dayCnt = 0;
			int totalCnt = 0;
			ShootSpecialRewardT shootSpecialRewardT = XsgActivityManage.getInstance().getShootSpecialRewardT(shootType,
					systemType);
			if (shootType == XsgActivityManage.ShootType.ShootOne.getValue()) {
				// 射一发
				// 超级百步不免费
				if (systemType == ShootSystemType.Shoot.getValue()) {
					free = XsgActivityManage.getInstance().isShootOneFree(myScoreRank, currentDate);// 抽取是否免费
				}
				// 判断付费抽取下金币和元宝是否足够
				if (!free) {
					checkAndReduceCost(shootType, currentMarksManParamT, systemType);
					score = currentMarksManParamT.one;
				} else {
					score = currentMarksManParamT.free;
				}
				shootIndex = new int[1];
				ShootRewardT shootRewardT = null;
				totalCnt = systemType == ShootSystemType.Shoot.getValue() ? myScoreRank.getShootOneCnt() + 1
						: myScoreRank.getShootCntSuper() + 1;
				// 达到特殊奖励要求
				if (totalCnt % shootSpecialRewardT.Number == 0) {
					List<ShootRewardT> shootRewardTList = XsgActivityManage.getInstance().getShootRewardTsList(
							systemType);
					shootRewardT = shootRewardTList.get(shootSpecialRewardT.Index - 1);
				} else {
					shootRewardT = XsgActivityManage.getInstance().getRandomShootReward(systemType);
				}
				shootIndex[0] = shootRewardT.id;
				IItem item = this.rt.getRewardControler().acceptReward(shootRewardT.itemId, shootRewardT.num);
				// 公告和记录中奖信息
				sendNoticeOrRecord(item, shootRewardT.num, shootRewardT, systemType, shootType);
				rewardMap.put(shootRewardT.itemId, shootRewardT.num);
				dayCnt = systemType == ShootSystemType.Shoot.getValue() ? myScoreRank.getDayOneCnt() + 1 : myScoreRank
						.getShootCntSuper() + 1;
			} else if (shootType == XsgActivityManage.ShootType.ShootTen.getValue()) {
				checkAndReduceCost(shootType, currentMarksManParamT, systemType);
				totalCnt = systemType == ShootSystemType.Shoot.getValue() ? myScoreRank.getShootTenCnt() + 1
						: myScoreRank.getShootCntSuper() + 1;
				shootIndex = new int[10];
				int ramdIndex = -1;
				if (totalCnt % shootSpecialRewardT.Number == 0) {
					Random rand = new Random(System.currentTimeMillis());
					ramdIndex = rand.nextInt(shootIndex.length);
				}
				// 十连射
				for (int i = 0; i < shootIndex.length; i++) {
					ShootRewardT shootRewardT = null;
					if (ramdIndex >= 0 && i == ramdIndex) {
						List<ShootRewardT> shootRewardTList = XsgActivityManage.getInstance().getShootRewardTsList(
								systemType);
						shootRewardT = shootRewardTList.get(shootSpecialRewardT.Index - 1);
					} else {
						shootRewardT = XsgActivityManage.getInstance().getRandomShootReward(systemType);
					}
					shootIndex[i] = shootRewardT.id;
					// 公告和记录中奖信息
					IItem item = this.rt.getRewardControler().acceptReward(shootRewardT.itemId, shootRewardT.num);
					sendNoticeOrRecord(item, shootRewardT.num, shootRewardT, systemType, shootType);
					if (rewardMap.containsKey(shootRewardT.itemId)) {
						rewardMap.put(shootRewardT.itemId, rewardMap.get(shootRewardT.itemId) + shootRewardT.num);
					} else {
						rewardMap.put(shootRewardT.itemId, shootRewardT.num);
					}
				}
				score = currentMarksManParamT.ten;
			}
			// // 积分已满，清空原有积分
			// if (myScoreRank.getScore() >= currentMarksManParamT.integration)
			// {
			// beforeScore = 0;
			// myScoreRank.setScore(0);
			// myScoreRank.setRec("");
			// }
			// 积分不能大于上限值
			// if (afterScore > currentMarksManParamT.integration) {
			// afterScore = currentMarksManParamT.integration;
			// }
			shootScoreChange(shootType, free, beforeScore, score, systemType);

			// 记录累计获得的奖励
			HashMap<String, Integer> histroyRewardMap = TextUtil.GSON.fromJson(myScoreRank.getHistoryAward(),
					new TypeToken<Map<String, Integer>>() {
					}.getType());
			if (histroyRewardMap == null) {
				histroyRewardMap = new HashMap<String, Integer>();
				;
			}
			Iterator<String> iter = rewardMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (histroyRewardMap.get(key) == null) {
					histroyRewardMap.put(key, rewardMap.get(key));
				} else {
					histroyRewardMap.put(key, histroyRewardMap.get(key) + rewardMap.get(key));
				}
			}
			myScoreRank.setHistoryAward(TextUtil.GSON.toJson(histroyRewardMap));
			// 保存数据库
			XsgActivityManage.getInstance().save2DbAsync(myScoreRank);

			shootRewardEvent.onShoot(systemType, shootType, free, dayCnt, totalCnt, rewardMap,
					myScoreRank.getShowMyRecord() == 1);
			view = createMarksmanView(currentMarksManParamT, shootIndex, systemType);
		} else {
			throw new NoteException(Messages.getString("ShootControler.0"));
		}
		return view;
	}

	/**
	 * 积分宝箱可领数量
	 * 
	 * @return
	 */
	private int canRecScoreRewardS() {
		int num = 0;
		Map<Integer, ShootScoreRewardT> config = XsgActivityManage.getInstance().getShootScoreRewardTs();
		for (int score : config.keySet()) {
			if (1 == getScoreRewardStatus(score))
				num += 1;
		}
		return num;
	}

	/**
	 * 验证相应货币是否足够，并且扣除相应货币
	 * 
	 * @param shootType
	 *            射击类型
	 * @param currentMarksManParamT
	 *            当前百步穿杨配置
	 * @throws NotEnoughMoneyException
	 *             货币是否足够异常
	 */
	private void checkAndReduceCost(int shootType, MarksManParamT currentMarksManParamT, int systemType)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException {
		int cost = 0;
		String costType = Const.PropertyName.MONEY;
		if (systemType == ShootSystemType.Shoot.getValue()) {
			if (shootType == XsgActivityManage.ShootType.ShootOne.getValue()) {
				costType = currentMarksManParamT.currency01;
				cost = currentMarksManParamT.deplete01;
			} else if (shootType == XsgActivityManage.ShootType.ShootTen.getValue()) {
				costType = currentMarksManParamT.currency02;
				cost = currentMarksManParamT.deplete02;
			}
		} else {
			if (shootType == XsgActivityManage.ShootType.ShootOne.getValue()) {
				costType = currentMarksManParamT.currency03;
				cost = currentMarksManParamT.deplete03;
			} else if (shootType == XsgActivityManage.ShootType.ShootTen.getValue()) {
				costType = currentMarksManParamT.currency04;
				cost = currentMarksManParamT.deplete04;
			}
		}
		// 判断射击花费是否满足
		if (Const.PropertyName.MONEY.equals(costType)) {
			if (this.rt.getJinbi() < cost) {// 游戏币
				throw new NotEnoughMoneyException();
			}
		} else if (Const.PropertyName.RMBY.equals(costType)) {
			if (this.rt.getTotalYuanbao() < cost) {// 元宝
				throw new NotEnoughYuanBaoException();
			}
		} else if (Const.PropertyName.ORDER.equals(costType)) {// 竞技币
			if (this.rt.getArenaRankControler().getRoleArenaRank().getChallengeMoney() < cost) {
				throw new NotEnoughException();
			}
		} else if (Const.PropertyName.AUCTION_COIN.equals(costType)) {// 拍卖币
			if (this.rt.getAuctionHouseController().getAuctionMoney() < cost) {
				throw new NotEnoughException();
			}
		}
		this.rt.getRewardControler().acceptReward(costType, -cost);
	}

	/**
	 * 获取积分段 宝箱的状态(state，0：不可领取，1：可领取 2：已领取)
	 * 
	 * @param score
	 * @return
	 */
	private byte getScoreRewardStatus(int score) {
		Map<Integer, ShootScoreRewardT> config = XsgActivityManage.getInstance().getShootScoreRewardTs();
		ShootScoreRewardT t = config.get(score);
		if (t == null)
			return 0;
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank == null) {
			return 0;// 没有积分排行，非法领奖
		}
		String rec = myScoreRank.getRec(); // 已领取积分
		if (!TextUtil.isBlank(rec) && rec.indexOf(score + ",") != -1) {
			return 2;
		}
		if (myScoreRank.getTotalScore() >= score)
			return 1;
		return 0;
	}

	/**
	 * 积分领奖
	 */
	@Override
	public String getScoreReward(int score) throws NoteException {
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		Map<Integer, ShootScoreRewardT> config = XsgActivityManage.getInstance().getShootScoreRewardTs();
		if (currentMarksManParamT != null && XsgActivityManage.getInstance().isInShootActiveTime()) {
			ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
			if (myScoreRank == null) {
				throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));// 没有积分排行，非法领奖
			}

			Map<String, Integer> recvivedMap = new HashMap<String, Integer>();
			// 单次领奖
			ShootScoreRewardT shootScoreRewardT = config.get(score);
			int status = getScoreRewardStatus(score);
			if (shootScoreRewardT != null && status == 1) {
				this.rt.getRewardControler().acceptReward(shootScoreRewardT.itemsMap.entrySet());
				recvivedMap.putAll(shootScoreRewardT.itemsMap);
			} else {
				if (status == 0) // 不可领取
					throw new NoteException(Messages.getString("ShootControler.5"));
				if (status == 2) // 已领取
					throw new NoteException(Messages.getString("ShootControler.4"));
			}
			if (TextUtil.isBlank(myScoreRank.getRec())) {
				myScoreRank.setRec(score + ",");
			} else {
				myScoreRank.setRec(myScoreRank.getRec() + score + ",");
			}

			XsgActivityManage.getInstance().save2DbAsync(myScoreRank);

			// 获取还能领取的积分段数量
			shootScoreRecvRewardEvent.onShootScoreRecvReward(myScoreRank.getRec(), recvivedMap);

			return LuaSerializer.serialize(getScoreRewardView()); // 返回积分奖励师徒
		} else {
			throw new NoteException(Messages.getString("ShootControler.0"));
		}
	}

	/**
	 * 发送全服公告或者记录中奖信息
	 */
	private void sendNoticeOrRecord(IItem item, int itemNum, ShootRewardT shootRewardT, int systemType, int shootType) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (XsgActivityManage.ShootNoticeType.Yes.getValue() == shootRewardT.notice) {
			sendNotice(item);
		} else if (XsgActivityManage.ShootNoticeType.Record.getValue() == shootRewardT.notice) {
			if (myScoreRank.getShowMyRecord() == 1) {
				XsgActivityManage.getInstance().recordAwardInfo(
						new ShootAwardInfo(this.rt.getRoleId(), this.rt.getName(), this.rt.getVipLevel(),
								new IntString[] { new IntString(itemNum, item.getTemplate().getId()) }, systemType,
								shootType));
			}
		} else if (XsgActivityManage.ShootNoticeType.RecordAndNotice.getValue() == shootRewardT.notice) {
			if (myScoreRank.getShowMyRecord() == 1) {
				XsgActivityManage.getInstance().recordAwardInfo(
						new ShootAwardInfo(this.rt.getRoleId(), this.rt.getName(), this.rt.getVipLevel(),
								new IntString[] { new IntString(itemNum, item.getTemplate().getId()) }, systemType,
								shootType));
				sendNotice(item);
			}
		}
	}

	/**
	 * 获取积分奖励数据
	 * 
	 * @return
	 */
	private MarksmanScoreReward[] getScoreRewardView() {
		Map<Integer, ShootScoreRewardT> shootScoreRewardMap = XsgActivityManage.getInstance().getShootScoreRewardTs();
		MarksmanScoreReward[] awards = new MarksmanScoreReward[shootScoreRewardMap.size()];
		int i = 0;
		for (ShootScoreRewardT t : shootScoreRewardMap.values()) {
			MarksmanScoreReward award = new MarksmanScoreReward();
			award.icons = t.icons;
			award.score = t.score;
			award.state = getScoreRewardStatus(t.score);
			award.items = wrapRewardItem(t.itemsMap);
			awards[i] = award;
			i += 1;
		}
		return awards;
	}

	/**
	 * 发送领取奖励公告
	 * 
	 * @param item
	 *            领取奖励
	 */
	private void sendNotice(IItem item) {
		// 获奖公告
		List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.MarksMan);
		if (adTList != null && adTList.size() > 0) {
			ChatAdT chatAdT = adTList.get(0);
			if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
				XsgChatManager.getInstance().sendAnnouncement(
						this.rt.getChatControler().parseAdContentItem(item.getTemplate(), chatAdT.content));
			}
		}
	}

	/**
	 * 打开百步穿杨积分排名界面
	 */
	@Override
	public MarksmanScoreRankView openMarksmanScoreRankView() throws NoteException {
		MarksmanScoreRankView view = null;
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (currentMarksManParamT != null && XsgActivityManage.getInstance().isInShootActiveTime()) {
			int myRank = 0; // 我的排名
			int myScore = 0; // 我的积分
			ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
			if (myScoreRank != null) {
				myRank = myScoreRank.getRank();
				myScore = myScoreRank.getTotalScore();
			}
			List<ShootScoreRank> scoreRankList = XsgActivityManage.getInstance().getShootScoreList();
			int size = Math.min(currentMarksManParamT.number, scoreRankList.size()); // 最大显示条目
			ShootScoreRankSub[] scoreRankSubList = new ShootScoreRankSub[size];
			for (int i = 0; i < size; i++) {
				ShootScoreRank shootScoreRank = scoreRankList.get(i);
				scoreRankSubList[i] = new ShootScoreRankSub();
				scoreRankSubList[i].rank = shootScoreRank.getRank(); // 排名
				scoreRankSubList[i].roleId = shootScoreRank.getRoleId(); // 角色ID
				scoreRankSubList[i].roleName = shootScoreRank.getRoleName(); // 角色名称
				scoreRankSubList[i].vip = shootScoreRank.getVip(); // 主公vip
				scoreRankSubList[i].level = shootScoreRank.getLevel(); // 主公等级
				scoreRankSubList[i].icon = shootScoreRank.getIcon(); // 头像
				scoreRankSubList[i].score = shootScoreRank.getTotalScore(); // 积分
			}
			view = new MarksmanScoreRankView(XsgActivityManage.getInstance().formatShootEndDate(
					Messages.getString("ShootControler.3"), currentMarksManParamT.endTime), scoreRankSubList, myRank,
					myScore, XsgActivityManage.getInstance().formatShootEndDate(Messages.getString("ShootControler.3"),
							currentMarksManParamT.endTime));
		} else {
			throw new NoteException(Messages.getString("ShootControler.0"));
		}
		return view;
	}

	/**
	 * 打开百步穿杨积分排名奖励界面
	 */
	@Override
	public MarksmanScoreRewardView openMarksmanScoreRewardView() throws NoteException {
		MarksmanScoreRewardView view = null;
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (currentMarksManParamT != null && XsgActivityManage.getInstance().isInShootActiveTime()) {
			int myRank = 0; // 我的排名
			IntString[] myRewardArray = null; // 我的排名对应的奖励
			ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
			if (myScoreRank != null) {
				myRank = myScoreRank.getRank();
			}
			// 积分排行榜显示数据
			List<ShootScoreRankT> scoreRankList = XsgActivityManage.getInstance().getShootScoreRankTsList();
			ShootScoreRewardSub[] rewardSub = new ShootScoreRewardSub[scoreRankList.size()];
			for (int i = 0; i < rewardSub.length; i++) {
				ShootScoreRankT scoreRank = scoreRankList.get(i);
				rewardSub[i] = new ShootScoreRewardSub();
				if (scoreRank.startRank == scoreRank.stopRank) {
					rewardSub[i].rank = String.valueOf(scoreRank.startRank);
				} else {
					rewardSub[i].rank = String.valueOf(scoreRank.startRank) + "-" + String.valueOf(scoreRank.stopRank);
				}
				// 奖励物品数组
				IntString[] rewardArray = new IntString[scoreRank.rewardMap.size()];
				Iterator<String> it = scoreRank.rewardMap.keySet().iterator();
				int k = 0;
				while (it.hasNext()) {
					String key = it.next();
					rewardArray[k++] = new IntString(scoreRank.rewardMap.get(key), key);
				}
				rewardSub[i].items = rewardArray;
				rewardSub[i].baseScore = XsgActivityManage.getInstance().getRankBaseScore(scoreRank.startRank,
						scoreRank.stopRank);
				// 计算我的排名奖励
				if (myRank > 0) {
					if (myRank >= scoreRank.startRank && myRank <= scoreRank.stopRank) {
						myRewardArray = rewardArray;
					}
				}
			}
			// 组合界面数据
			view = new MarksmanScoreRewardView(XsgActivityManage.getInstance().formatShootEndDate(
					Messages.getString("ShootControler.3"), currentMarksManParamT.endTime), rewardSub, myRank,
					myRewardArray, myScoreRank == null ? 0 : myScoreRank.getTotalScore());
		} else {
			throw new NoteException(Messages.getString("ShootControler.0"));
		}
		return view;
	}

	/**
	 * 积分变化
	 * 
	 * @param shootType
	 *            射击类型
	 * @param isFree
	 *            是否免费
	 * @param beforeScore
	 *            变更前积分
	 * @param score
	 *            增加的积分
	 * @param afterScore
	 *            变更后积分
	 */
	private void shootScoreChange(int shootType, boolean isFree, int beforeScore, int score, int systemType) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		Date currentDate = Calendar.getInstance().getTime();
		int prevTotalScore = myScoreRank.getTotalScore();
		// 积分奖励
		if (systemType == ShootSystemType.Shoot.getValue()) {
			myScoreRank.setDayOneCnt(myScoreRank.getDayOneCnt() + 1);
			myScoreRank.setShootOneCnt(myScoreRank.getShootOneCnt() + 1);
		} else {
			myScoreRank.setShootCntSuper(myScoreRank.getShootCntSuper() + 1);
		}

		myScoreRank.setShootOneTime(currentDate);

		if (isFree) {
			myScoreRank.setDayFreeCnt(myScoreRank.getDayFreeCnt() + 1);
			myScoreRank.setShootFreeTime(currentDate);
		}
		myScoreRank.setTotalScore(myScoreRank.getTotalScore() + score);
		// 积分达到上榜要求
		MarksManParamT currentMarksManParamT = XsgActivityManage.getInstance().getMarksManParam();
		if (prevTotalScore < currentMarksManParamT.Basis && myScoreRank.getTotalScore() >= currentMarksManParamT.Basis) {
			XsgActivityManage.getInstance().getShootScoreList().add(myScoreRank);
			myScoreRank.setRank(XsgActivityManage.getInstance().getShootScoreList().size());
		}

		shootScoreChangeEvent.onShootScoreChange(shootType, isFree, beforeScore, score, myScoreRank.getTotalScore(),
				myScoreRank.getTotalScore());
		// 积分未达到上榜要求，直接保存后退出
		if (myScoreRank.getRank() == 0) {
			// XsgActivityManage.getInstance().save2DbAsync(myScoreRank); 最后再保存
			return;
		}
		// 调整排名
		int prevRank = myScoreRank.getRank();
		XsgActivityManage.getInstance().adjustShootScoreRank();
		LinkedList<ShootScoreRank> shootScoreList = XsgActivityManage.getInstance().getShootScoreList();
		for (int i = 0; i < shootScoreList.size(); i++) {
			shootScoreList.get(i).setRank(i + 1);
		}
		// 排名有变动，受影响的排名对象都更新到数据库
		if (prevRank != myScoreRank.getRank()) {
			for (int i = myScoreRank.getRank() - 1; i < prevRank; i++) {
				XsgActivityManage.getInstance().save2DbAsync(shootScoreList.get(i));
			}
		} else {
			// XsgActivityManage.getInstance().save2DbAsync(myScoreRank); 最后再保存
		}
	}

	/**
	 * 创建当前的射箭记录
	 * 
	 * @return
	 */
	private ShootScoreRank addShootScoreRank() {
		ShootScoreRank myScoreRank = new ShootScoreRank();
		myScoreRank.setRoleId(this.rt.getRoleId());
		myScoreRank.setRoleName(this.rt.getName());
		myScoreRank.setFactionId(this.rt.getFactionControler().getFactionId());
		myScoreRank.setVip(this.rt.getVipLevel());
		myScoreRank.setLevel(this.rt.getLevel());
		myScoreRank.setIcon(this.rt.getHeadImage());

		// myScoreRank.setDayOneCnt(1);
		// myScoreRank.setShootOneCnt(1);
		// 默认显示中奖信息
		myScoreRank.setShowMyRecord(1);

		XsgActivityManage.getInstance().addToShootScoreMap(myScoreRank);
		return myScoreRank;
	}

	/**
	 * 角色VIP等级提升重新设定排名对象信息
	 */
	@Override
	public void onVipLevelUp(int newLevel) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank != null) {
			myScoreRank.setVip(newLevel);
		}
	}

	/**
	 * 角色升级重新设定排名对象信息
	 */
	@Override
	public void onRoleLevelup() {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank != null) {
			myScoreRank.setLevel(this.rt.getLevel());
		}
	}

	/**
	 * 角色改名重新设定排名对象信息
	 */
	@Override
	public void onRoleNameChange(String old, String name) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank != null) {
			myScoreRank.setRoleName(name);
		}
	}

	/**
	 * 角色退出公会重新设定排名对象信息
	 */
	@Override
	public void onQuitFaction(String factionId, String roleId) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank != null) {
			myScoreRank.setFactionId(null);
		}
	}

	/**
	 * 角色加入公会重新设定排名对象信息
	 */
	@Override
	public void onApproveJoin(String factionId, String roleId) {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(roleId);
		if (myScoreRank != null) {
			myScoreRank.setFactionId(factionId);
		}
	}

	/**
	 * 是否显示我的中奖记录
	 */
	@Override
	public void showMyRecord(boolean show) throws NoteException {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());

		myScoreRank.setShowMyRecord(show ? 1 : 0);
		XsgActivityManage.getInstance().save2DbAsync(myScoreRank);
	}

	/**
	 * 累计获得的奖励
	 */
	@Override
	public String historyAward() throws NoteException {
		ShootScoreRank myScoreRank = XsgActivityManage.getInstance().getMyShootScore(rt.getRoleId());
		if (myScoreRank != null) {
			Map<String, Integer> historyReward = TextUtil.GSON.fromJson(myScoreRank.getHistoryAward(),
					new TypeToken<Map<String, Integer>>() {
					}.getType());
			System.out.println(myScoreRank.getHistoryAward());
			if (historyReward != null) {
				List<IntString> list_rewards = new ArrayList<IntString>();
				Iterator<String> iter = historyReward.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					list_rewards.add(new IntString(historyReward.get(key), key));
				}
				return LuaSerializer.serialize(list_rewards.toArray(new IntString[0]));
			}
		}
		return LuaSerializer.serialize(new IntString[0]);
	}

}
