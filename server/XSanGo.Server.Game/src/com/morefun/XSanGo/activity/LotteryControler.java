package com.morefun.XSanGo.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.XSanGo.Protocol.GridInfoSub;
import com.XSanGo.Protocol.GridPageView;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.LettoryShopInfoSub;
import com.XSanGo.Protocol.LettoryShopView;
import com.XSanGo.Protocol.LotteryCycleSub;
import com.XSanGo.Protocol.LotteryCycleView;
import com.XSanGo.Protocol.LotteryScoreRankSub;
import com.XSanGo.Protocol.LotteryScoreRankView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RankPageView;
import com.XSanGo.Protocol.RankSub;
import com.XSanGo.Protocol.RoleBaseSub;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.LotteryScoreInfo;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IApproveJoin;
import com.morefun.XSanGo.event.protocol.ILotteryShopBuy;
import com.morefun.XSanGo.event.protocol.ILotteryThrowBall;
import com.morefun.XSanGo.event.protocol.IQuitFaction;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 
 * @author sunjie
 * 
 */
@RedPoint
public class LotteryControler implements ILotteryControler, IRoleNameChange, IRoleLevelup, IVipLevelUp, IApproveJoin,
		IQuitFaction {
	private IRole rt;
	private Role db;
	private ILotteryThrowBall lotteryThrowBallEvent;
	private ILotteryShopBuy lotteryShopBuyEvent;

	public LotteryControler(IRole rt, Role db) {
		super();
		this.rt = rt;
		this.db = db;
		IEventControler eventControler = rt.getEventControler();
		lotteryThrowBallEvent = this.rt.getEventControler().registerEvent(ILotteryThrowBall.class);
		lotteryShopBuyEvent = this.rt.getEventControler().registerEvent(ILotteryShopBuy.class);

		eventControler.registerHandler(IRoleLevelup.class, this);
		eventControler.registerHandler(IVipLevelUp.class, this);
		eventControler.registerHandler(IRoleNameChange.class, this);
		eventControler.registerHandler(IApproveJoin.class, this);
		eventControler.registerHandler(IQuitFaction.class, this);

	}

	// 普通摇骰子
	private static int NORMAL_THROW = 0;

	// 遙控摇骰子
	private static int AUTO_THROW = 1;

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (isFreeOfThrow()) {
			return new MajorUIRedPointNote(MajorMenu.Lottery, false);
		}
		return null;
	}

	/**
	 * 是否可免费投掷
	 * 
	 * @return
	 */
	private boolean isFreeOfThrow() {
		if (!isInActiveTime())
			return false;
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		// 随机生成任务 大富温数据
		if (roleLottery == null) {
			return true;
		}
		Map<Integer, LotteryTimeCostT> timeCostCfg = XsgLotteryManage.getInstance().getTimeCostCfg();
		LotteryTimeCostT t = timeCostCfg.get(roleLottery.getDailyThrowNum() + 1);
		if (t != null && t.coinType == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 红点提示
	 */
	private void notifyRedPoint() {
		if (isFreeOfThrow()) {
			this.rt.getNotifyControler()
					.onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.Lottery, false));
		}
	}

	/**
	 * 主界面
	 */
	@Override
	public GridPageView gridPageView() throws NoteException {
		if (!isInActiveTime()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		// 随机生成任务 大富温数据
		if (roleLottery == null) {
			roleLottery = new LotteryScoreInfo(rt.getRoleId(), XsgLotteryManage.START_GRID, new Date());
			roleLottery.setRoleName(this.rt.getName());
			roleLottery.setFactionId(this.rt.getFactionControler().getFactionId());
			roleLottery.setVip(this.rt.getVipLevel());
			roleLottery.setLevel(this.rt.getLevel());
			roleLottery.setIcon(this.rt.getHeadImage());
			rolesLotteryDBinfo.put(rt.getRoleId(), roleLottery);
			XsgLotteryManage.getInstance().initRoleGridItems(roleLottery);
			XsgLotteryManage.getInstance().save2DbAsync(roleLottery);
		} else {
			reset(roleLottery);
		}
		GridPageView view = new GridPageView();
		view.minLevel = XsgLotteryManage.getInstance().getMinLevel();
		view.base = getRoleBaseSub(roleLottery);
		view.list = getRoleGrids(roleLottery);
		view.shopPreview = XsgLotteryManage.getInstance().getShopPreview();

		return view;
	}

	@Override
	public RankPageView lettoryRankView() throws NoteException {
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		RankPageView view = new RankPageView();
		view.maxNum = XsgLotteryManage.rankLimit;
		view.desc = XsgLotteryManage.getInstance().getLotteryCommParaT().text1;
		if (roleLottery == null) {
			view.awards = null;
			view.rank = 0;
		} else {
			int rankNum = XsgLotteryManage.getInstance().getRankNum(roleLottery);
			view.rank = rankNum;
			Map<String, Integer> itemMap = XsgLotteryManage.getInstance().getAward4Rank(rankNum);
			if (itemMap == null) {
				view.awards = null;
			} else {
				view.awards = wrapRewardItem(itemMap);
			}
		}
		List<LotteryRankAwardT> rankAwardsCfg = XsgLotteryManage.getInstance().getRankAwardsCfg();
		RankSub[] rankList = new RankSub[rankAwardsCfg.size()];
		int index = 0;
		for (LotteryRankAwardT t : rankAwardsCfg) {
			RankSub sub = new RankSub();
			sub.rankEnd = t.stopRank;
			sub.id = t.num;
			sub.rankStart = t.startRank;
			sub.awards = wrapRewardItem(t.itemMap);
			rankList[index] = sub;
			index += 1;
		}
		view.rankList = rankList;
		return view;
	}

	/**
	 * 大富温积分排行界面
	 */
	@Override
	public LotteryScoreRankView lotteryScoreRankView() throws NoteException {
		if (!isInActiveTime()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.LOTTERY_ACTIVEID);
		LotteryScoreRankView view = new LotteryScoreRankView();
		int myRank = 0;
		int myScore = 0;
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		// 随机生成任务 大富温数据
		if (roleLottery != null) {
			myRank = XsgLotteryManage.getInstance().getRankNum(roleLottery);
			myScore = roleLottery.getScore();
		}
		view.myRank = myRank;
		view.myScore = myScore;
//		long leftTime = (DateUtil.parseDate(t.endTime).getTime() - System.currentTimeMillis()) / 1000;
		// 活动剩余时间,非活动消失时间
		LotteryCommParaT lotteryCommParaT = XsgLotteryManage.getInstance().getLotteryCommParaT();
		long leftTime = (DateUtil.parseDate(lotteryCommParaT.endTime).getTime() - System.currentTimeMillis()) / 1000;
		view.sendAwardLastTime = leftTime < 0 ? 0 : leftTime;
		List<LotteryScoreInfo> lotteryScoreRankList = XsgLotteryManage.getInstance().getLotteryScoreRankList();
		int size = lotteryScoreRankList.size();
		int index = 0;
		LotteryScoreRankSub[] lotteryRankList = new LotteryScoreRankSub[size];
		for (LotteryScoreInfo rank : lotteryScoreRankList) {
			LotteryScoreRankSub sub = new LotteryScoreRankSub();
			sub.rank = index + 1;
			sub.icon = rank.getIcon();
			sub.level = rank.getLevel();
			sub.roleId = rank.getRoleId();
			sub.roleName = rank.getRoleName();
			sub.score = rank.getScore();
			sub.vip = rank.getVip();
			lotteryRankList[index] = sub;
			index += 1;
		}
		view.lotteryRankList = lotteryRankList;
		return view;
	}

	/**
	 * 大富温巡回奖励展示
	 */
	@Override
	public LotteryCycleView lotteryCycleView() throws NoteException {
		LotteryCycleView view = new LotteryCycleView();
		Map<Integer, LotteryTourAwardT> toursAwardsCfg = XsgLotteryManage.getInstance().getToursAwardsCfg();
		int size = toursAwardsCfg.size();
		LotteryCycleSub[] lotteryCycleList = new LotteryCycleSub[size];
		int index = 0;
		for (LotteryTourAwardT t : toursAwardsCfg.values()) {
			LotteryCycleSub sub = new LotteryCycleSub();
			sub.num = t.num;
			sub.awards = wrapRewardItem(t.itemMap);
			lotteryCycleList[index] = sub;
			index += 1;
		}
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		// 随机生成任务 大富温数据
		if (roleLottery == null) {
			view.curCyc = 0;
		} else {
			view.curCyc = roleLottery.getCycleTime();
		}

		view.lotteryCycleList = lotteryCycleList;
		return view;
	}

	/**
	 * 获得大富温界面基础信息(不包含格子)
	 * 
	 * @param roleLottery
	 * @return
	 */
	private RoleBaseSub getRoleBaseSub(LotteryScoreInfo roleLottery) {
		if (roleLottery == null)
			return null;
		RoleBaseSub sub = new RoleBaseSub();
		LotteryCommParaT lotteryCommParaT = XsgLotteryManage.getInstance().getLotteryCommParaT();
		Map<Integer, LotteryTimeCostT> timeCostCfg = XsgLotteryManage.getInstance().getTimeCostCfg();
		TreeMap<Integer, LotteryGridT> lotteryGridCfg = XsgLotteryManage.getInstance().getLotteryGridCfg();
		LotteryGridT gridCfg = lotteryGridCfg.get(roleLottery.getGridId());
		sub.autoNeedNum = lotteryCommParaT.lotteryTeleoperationNum;
		sub.autoNum = roleLottery.getAutoNum();
		sub.curGrid = roleLottery.getGridId();
		// 活动剩余时间
		sub.leftTime = (DateUtil.parseDate(lotteryCommParaT.endTime).getTime() - System.currentTimeMillis()) / 1000;
		LotteryTimeCostT cost = timeCostCfg.get(roleLottery.getDailyThrowNum() + 1);
		int freeNum = 0;
		if (XsgLotteryManage.FREE_TIMES > roleLottery.getDailyThrowNum()) {
			freeNum = XsgLotteryManage.FREE_TIMES - roleLottery.getDailyThrowNum();
		} else {
			freeNum = 0;
		}
		sub.freeNum = freeNum;
		if (cost == null) {
			sub.coinType = XsgLotteryManage.COST_TYPE_YUANBAO;// 默认元宝类型
			sub.price = XsgLotteryManage.MAX_THROW_COST;
		} else {
			sub.coinType = cost.coinType;
			sub.price = cost.cost;
		}
		sub.rank = XsgLotteryManage.getInstance().getRankNum(roleLottery);
		sub.score = roleLottery.getScore();
		if (roleLottery.getThrowNum() >= lotteryCommParaT.lotteryTeleoperationNum) {
			sub.throwNum = roleLottery.getThrowNum() % lotteryCommParaT.lotteryTeleoperationNum;
		} else {
			sub.throwNum = roleLottery.getThrowNum();
		}
		if (gridCfg.pointType != XsgLotteryManage.TYPE_SHOP) {
			sub.tips = 0;
		} else {
			if (getShopLeftTime(roleLottery) < 0) {
				sub.tips = 2;
			} else {
				sub.tips = 1;
			}
		}
		return sub;
	}

	/**
	 * 是否在活动时间内
	 * 
	 * @return
	 */
	private boolean isInActiveTime() {
		ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.LOTTERY_ACTIVEID);
		if (t == null)
			return false;
		boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(t.startTime),
				DateUtil.parseDate(t.endTime));
		return isBetween;
	}

	/**
	 * 获得大富温界面格子基础信息
	 * 
	 * @param roleLottery
	 * @return
	 */
	private GridInfoSub[] getRoleGrids(LotteryScoreInfo roleLottery) {
		if (roleLottery == null)
			return null;
		if (TextUtil.isBlank(roleLottery.getGridsInfo()))
			return null;
		TreeMap<Integer, LotteryGridT> lotteryGridCfg = XsgLotteryManage.getInstance().getLotteryGridCfg();
		int size = lotteryGridCfg.size();
		GridInfoSub[] list = new GridInfoSub[size];
		int i = 0;
		for (LotteryGridT t : lotteryGridCfg.values()) {
			GridInfoSub sub = new GridInfoSub();
			sub.id = t.id;
			sub.type = t.pointType;
			int lotteryType = 0;
			if (t.pointType == XsgLotteryManage.TYPE_NORMAL_GRID) {
				LotteryGridItemT itemCfg = XsgLotteryManage.getInstance().getProFromItem(t.id, roleLottery);
				sub.item = new IntString(itemCfg.num, itemCfg.item);
				lotteryType = itemCfg.lotteryType;
			}
			sub.lotteryType = lotteryType;
			list[i] = sub;
			i++;
		}
		return list;

	}

	/**
	 * 神秘商店
	 */
	@Override
	public LettoryShopView lettoryshopView() throws NoteException {
		if (!isInActiveTime()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		if (roleLottery == null) {
			throw new NoteException(Messages.getString("LotteryControler.1"));
		}
		if (roleLottery.getUpdateTime() == null || !DateUtil.isSameDay(roleLottery.getUpdateTime(), new Date())) {
			throw new NoteException(Messages.getString("LotteryControler.4"));
		}
		int curGrid = roleLottery.getGridId();
		TreeMap<Integer, LotteryGridT> lotteryGridCfg = XsgLotteryManage.getInstance().getLotteryGridCfg();
		LotteryGridT t = lotteryGridCfg.get(curGrid);
		if (t == null) {
			throw new NoteException(Messages.getString("LotteryControler.1"));
		}
		if (t.pointType != XsgLotteryManage.TYPE_SHOP) {
			throw new NoteException(Messages.getString("LotteryControler.2"));
		}
		// 如有DB有记录就显示 否则刷出来
		if (TextUtil.isBlank(roleLottery.getShopInfo())) {
			XsgLotteryManage.getInstance().initRoleMShopItems(roleLottery);
			roleLottery.setUpdateTime(new Date());
			XsgLotteryManage.getInstance().save2DbAsync(roleLottery);
		}
		String shopInfo = roleLottery.getShopInfo();
		String items[] = shopInfo.split(",");
		LettoryShopView view = new LettoryShopView();
		LettoryShopInfoSub[] itemlist = new LettoryShopInfoSub[items.length];
		Map<Integer, LotteryMysticalShopT> lotteryMysticalShopCfg = XsgLotteryManage.getInstance()
				.getLotteryMysticalShopCfg();
		for (int i = 0; i < items.length; i++) {
			LettoryShopInfoSub sub = new LettoryShopInfoSub();
			String detail[] = items[i].split("_");
			int id = Integer.valueOf(detail[0]);
			int status = Integer.valueOf(detail[1]);
			LotteryMysticalShopT itemt = lotteryMysticalShopCfg.get(id);
			sub.coinType = itemt.coinType;
			sub.discount = itemt.discount;
			sub.id = itemt.id;
			sub.item = new IntString(itemt.num, itemt.item);
			sub.price = itemt.price;
			sub.price2 = itemt.price2;
			sub.status = status;
			itemlist[i] = sub;
		}
		view.itemlist = itemlist;
		view.curScore = roleLottery.getScore();
		view.remain = getShopLeftTime(roleLottery);
		return view;
	}

	/**
	 * 神秘商店购买
	 */
	@Override
	public LettoryShopView lettoryshopBuy(int id) throws NotEnoughMoneyException, NotEnoughYuanBaoException,
			NoteException {
		if (!isInActiveTime()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		if (roleLottery == null) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		// if (roleLottery.getUpdateTime() == null ||
		// !DateUtil.isSameDay(roleLottery.getUpdateTime(), new Date())) {
		// throw new NoteException(Messages.getString("LotteryControler.4"));
		// }
		LotteryMysticalShopT itemCfg = XsgLotteryManage.getInstance().getLotteryMysticalShopCfg().get(id);
		if (itemCfg == null) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		if (getShopLeftTime(roleLottery) < 0 || !XsgLotteryManage.getInstance().isCanThrowAndBuy()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		LotteryCommParaT lotteryCommParaT = XsgLotteryManage.getInstance().getLotteryCommParaT();
		int curGrid = roleLottery.getGridId();
		TreeMap<Integer, LotteryGridT> lotteryGridCfg = XsgLotteryManage.getInstance().getLotteryGridCfg();
		LotteryGridT t = lotteryGridCfg.get(curGrid);
		if (t == null) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		if (t.pointType != XsgLotteryManage.TYPE_SHOP) {
			throw new NoteException(Messages.getString("LotteryControler.2"));
		}
		String shopInfo = roleLottery.getShopInfo();
		String idStatus = id + "_" + 0 + ",";
		if (shopInfo.indexOf(idStatus) == -1) {
			throw new NoteException(Messages.getString("LotteryControler.3"));
		}
		String costType = Const.PropertyName.MONEY;
		int cost = itemCfg.price2;
		// 判断花费是否满足
		if (itemCfg.coinType == XsgLotteryManage.COST_TYPE_MONEY) {
			if (this.rt.getJinbi() < cost) {// 游戏币
				throw new NotEnoughMoneyException();
			}
		} else if (itemCfg.coinType == XsgLotteryManage.COST_TYPE_YUANBAO) {
			costType = Const.PropertyName.RMBY;
			if (this.rt.getTotalYuanbao() < cost) {// 元宝
				throw new NotEnoughYuanBaoException();
			}
		}
		this.rt.getRewardControler().acceptReward(costType, -cost);

		// 加积分加任性值
		int addScore = 0;
		int addSpecialScore = 0;
		if (costType == Const.PropertyName.RMBY) {
			addScore += cost * lotteryCommParaT.score2;
			addSpecialScore += cost * lotteryCommParaT.score3;
			int prevTotalScore = roleLottery.getScore();
			roleLottery.setScore(roleLottery.getScore() + addScore);
			roleLottery.setSpecialScore(roleLottery.getSpecialScore() + addSpecialScore);
			// 积分变化
			if (addScore > 0)
				throwScoreChange(prevTotalScore);
		}
		// 发奖 更新数据
		this.rt.getRewardControler().acceptReward(itemCfg.item, itemCfg.num);
		String curStatusInfo = id + "_" + 1 + ",";
		String newShopInfo = roleLottery.getShopInfo().replace(idStatus, curStatusInfo);
		roleLottery.setShopInfo(newShopInfo);
		roleLottery.setUpdateTime(new Date());
		XsgLotteryManage.getInstance().save2DbAsync(roleLottery);
		lotteryShopBuyEvent.onBuy(costType, cost, itemCfg.item, itemCfg.num);
		return lettoryshopView();
	}

	/**
	 * 获得神秘商店倒计时
	 * 
	 * @param roleLottery
	 * @return
	 */
	private long getShopLeftTime(LotteryScoreInfo roleLottery) {
		LotteryCommParaT lotteryCommParaT = XsgLotteryManage.getInstance().getLotteryCommParaT();
		Date openTime = roleLottery.getShopOpenTime();
		if (openTime == null)
			return -1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(openTime);
		// 神秘商店持续时间
		int continueHours = lotteryCommParaT.shopReset;
		Calendar nextTime = DateUtil.addHours(calendar, continueHours);
		long left = (nextTime.getTimeInMillis() - System.currentTimeMillis()) / 1000;
		return left > 0 ? left : -1;
	}

	/**
	 * 摇骰子
	 */
	@Override
	public RoleBaseSub throwBall(int type, int point) throws NoteException, NotEnoughYuanBaoException,
			NotEnoughMoneyException {
		int minLevel = XsgLotteryManage.getInstance().getMinLevel();
		if (this.rt.getLevel() < minLevel) {
			throw new NoteException(Messages.getString(TextUtil.format(
					Messages.getString("LotteryControler.NotEnoughLevel"), minLevel)));
		}
		if (!isInActiveTime() || !XsgLotteryManage.getInstance().isCanThrowAndBuy()) {
			throw new NoteException(Messages.getString("LotteryControler.0"));
		}
		LotteryCommParaT lotteryCommParaT = XsgLotteryManage.getInstance().getLotteryCommParaT();
		Map<String, LotteryScoreInfo> rolesLotteryDBinfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo();
		Map<Integer, LotteryTimeCostT> timeCostCfg = XsgLotteryManage.getInstance().getTimeCostCfg();
		LotteryScoreInfo roleLottery = rolesLotteryDBinfo.get(rt.getRoleId());
		if (roleLottery == null) {
			throw new NoteException(Messages.getString("LotteryControler.1"));
		}
		if (roleLottery.getUpdateTime() == null || !DateUtil.isSameDay(roleLottery.getUpdateTime(), new Date())) {
			throw new NoteException(Messages.getString("LotteryControler.4"));
		}
		if (type != AUTO_THROW && type != NORMAL_THROW) {
			throw new NoteException(Messages.getString("LotteryControler.1"));
		}
		if (type == AUTO_THROW) {
			if (point > XsgLotteryManage.MAX_THROW_POINT || point <= 0) {
				throw new NoteException(Messages.getString("LotteryControler.1"));
			}
			if (roleLottery.getAutoNum() <= 0) {
				throw new NoteException(Messages.getString("LotteryControler.1"));
			}
		}
		int curThrowTimes = roleLottery.getDailyThrowNum();
		int curPosition = roleLottery.getGridId();
		int throwPoint = point;
		int cost = 0;// 默认花费
		int cType = XsgLotteryManage.COST_TYPE_YUANBAO;// 默认元宝类型
		int prevTotalScore = roleLottery.getScore();
		int prevTotalSpecialScore = roleLottery.getSpecialScore();
		if (type == NORMAL_THROW) {
			LotteryTimeCostT t = timeCostCfg.get(curThrowTimes + 1);
			if (t == null) {
				cost = XsgLotteryManage.MAX_THROW_COST;
			} else {
				cost = t.cost;
				cType = t.coinType;
			}
			String costType = Const.PropertyName.MONEY;
			// 判断花费是否满足
			if (cType == XsgLotteryManage.COST_TYPE_MONEY) {
				if (this.rt.getJinbi() < cost) {// 游戏币
					throw new NotEnoughMoneyException();
				}
			} else if (cType == XsgLotteryManage.COST_TYPE_YUANBAO) {
				costType = Const.PropertyName.RMBY;
				if (this.rt.getTotalYuanbao() < cost) {// 元宝
					throw new NotEnoughYuanBaoException();
				}
			}
			if (cost > 0) {
				this.rt.getRewardControler().acceptReward(costType, -cost);
			}
			int result[] = XsgLotteryManage.getInstance().getThrowPoint(roleLottery);
			throwPoint = result[0];// 得到的点数
			int position = result[1];// 格子ID
			roleLottery.setGridId(position);
			roleLottery.setThrowNum(roleLottery.getThrowNum() + 1);
			roleLottery.setDailyThrowNum(roleLottery.getDailyThrowNum() + 1);
			if (roleLottery.getThrowNum() % lotteryCommParaT.lotteryTeleoperationNum == 0) {
				roleLottery.setAutoNum(roleLottery.getAutoNum() + 1);
			}
		} else {
			int nextPosition = 0;
			if (curPosition + point <= XsgLotteryManage.GRID_COUNT) {
				nextPosition = curPosition + point;
			} else {
				nextPosition = point - (XsgLotteryManage.GRID_COUNT - curPosition);
			}
			roleLottery.setAutoNum(roleLottery.getAutoNum() - 1);
			roleLottery.setGridId(nextPosition);
		}
		boolean isCycle = false;
		// 巡回 加次数 发奖
		if (curPosition > roleLottery.getGridId()) {
			isCycle = true;
			roleLottery.setCycleTime(roleLottery.getCycleTime() + 1);
			LotteryTourAwardT award = XsgLotteryManage.getInstance().getToursAwardsCfg()
					.get(roleLottery.getCycleTime());
			if (award == null && roleLottery.getCycleTime() > XsgLotteryManage.CYCLE_AWARD_NUM_LIMIT) {
				award = XsgLotteryManage.getInstance().getToursAwardsCfg().get(XsgLotteryManage.CYCLE_AWARD_NUM_LIMIT);
			}
			if (award != null) {
				Map<String, Integer> itemMap = award.itemMap;
				Map<String, String> replaceMap = new HashMap<String, String>();
				// 发送邮件
				XsgMailManager.getInstance().sendTemplate(roleLottery.getRoleId(), MailTemplate.LotteryCycleAward,
						itemMap, replaceMap);
			}
		}
		// 格子奖励
		LotteryGridItemT config = XsgLotteryManage.getInstance().getProFromItem(roleLottery.getGridId(), roleLottery);
		if (config != null) {
			this.rt.getRewardControler().acceptReward(config.item, config.num);
		}
		LotteryGridT gridCfg = XsgLotteryManage.getInstance().getLotteryGridCfg().get(roleLottery.getGridId());
		if (gridCfg.pointType == XsgLotteryManage.TYPE_SHOP) {
			// 刷神秘商店
			XsgLotteryManage.getInstance().initRoleMShopItems(roleLottery);
		}
		// 加积分加任性值
		int addScore = throwPoint * lotteryCommParaT.score1;
		int addSpecialScore = throwPoint * lotteryCommParaT.score3;
		if (cType == XsgLotteryManage.COST_TYPE_YUANBAO) {
			addScore += cost * lotteryCommParaT.score2;
			addSpecialScore += cost * lotteryCommParaT.score3;
		}
		roleLottery.setScore(roleLottery.getScore() + addScore);
		roleLottery.setSpecialScore(roleLottery.getSpecialScore() + addSpecialScore);
		// 积分变化
		if (addScore > 0)
			throwScoreChange(prevTotalScore);
		roleLottery.setUpdateTime(new Date());
		XsgLotteryManage.getInstance().save2DbAsync(roleLottery);
		lotteryThrowBallEvent.onThrowBall(type, curPosition, roleLottery.getGridId(), throwPoint, prevTotalScore,
				prevTotalSpecialScore, addScore, addSpecialScore, isCycle == true ? 1 : 0, roleLottery.getCycleTime(),
				roleLottery.getThrowNum(), roleLottery.getAutoNum());
		notifyRedPoint();
		return getRoleBaseSub(roleLottery);
	}

	/**
	 * 重置格子 神秘商店 循环圈数
	 * 
	 * @param roleLottery
	 */
	private void reset(LotteryScoreInfo roleLottery) {
		if (roleLottery.getUpdateTime() == null)
			return;
		if (DateUtil.isSameDay(roleLottery.getUpdateTime(), new Date())) {
			return;
		}
		// XsgLotteryManage.getInstance().initRoleGridItems(roleLottery);
		// if (!TextUtil.isBlank(roleLottery.getShopInfo())) {
		// roleLottery.setShopInfo("");
		// }
		// roleLottery.setCycleTime(0);
		roleLottery.setDailyThrowNum(0);
		// roleLottery.setGridId(XsgLotteryManage.START_GRID);
		roleLottery.setUpdateTime(new Date());
		XsgLotteryManage.getInstance().save2DbAsync(roleLottery);
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
	 * 积分变化
	 * 
	 */
	private void throwScoreChange(int prevTotalScore) {
		LotteryScoreInfo myScoreRank = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		List<LotteryScoreInfo> ranks = XsgLotteryManage.getInstance().getLotteryScoreRankList();
		if (ranks.size() == 0) {
			ranks.add(myScoreRank);
		} else {
			// 原本不在榜 现在入了
			if (prevTotalScore < ranks.get(ranks.size() - 1).getScore()
					&& myScoreRank.getScore() > ranks.get(ranks.size() - 1).getScore()) {
				ranks.add(myScoreRank);
			} else {
				if (ranks.size() <= XsgLotteryManage.rankLimit) {
					boolean isIn = false;
					for (LotteryScoreInfo info : ranks) {
						if (info.getRoleId().equalsIgnoreCase(myScoreRank.getRoleId())) {
							isIn = true;
							break;
						}
					}
					if (!isIn) {
						ranks.add(myScoreRank);
					}
				}
			}
		}
		XsgLotteryManage.getInstance().adjustLotteryScoreRank();
	}

	@Override
	public void onQuitFaction(String factionId, String roleId) {
		LotteryScoreInfo roleInfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		if (roleInfo != null) {
			roleInfo.setFactionId(null);
		}
	}

	@Override
	public void onApproveJoin(String factionId, String roleId) {
		LotteryScoreInfo roleInfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		if (roleInfo != null) {
			roleInfo.setFactionId(factionId);
		}
	}

	@Override
	public void onVipLevelUp(int newLevel) {
		LotteryScoreInfo roleInfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		if (roleInfo != null) {
			roleInfo.setVip(rt.getVipLevel());
		}
	}

	@Override
	public void onRoleLevelup() {
		LotteryScoreInfo roleInfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		if (roleInfo != null) {
			roleInfo.setLevel(rt.getLevel());
		}
	}

	@Override
	public void onRoleNameChange(String old, String name) {
		LotteryScoreInfo roleInfo = XsgLotteryManage.getInstance().getRolesLotteryDBinfo().get(rt.getRoleId());
		if (roleInfo != null) {
			roleInfo.setRoleName(rt.getName());
		}
	}

}
