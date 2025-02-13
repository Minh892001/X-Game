package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.ActivityInfoAll;
import com.XSanGo.Protocol.ApiActView;
import com.XSanGo.Protocol.ExchangeResult;
import com.XSanGo.Protocol.ExchangeView;
import com.XSanGo.Protocol.FootballShop;
import com.XSanGo.Protocol.GridPageView;
import com.XSanGo.Protocol.LettoryShopView;
import com.XSanGo.Protocol.LevelWealView;
import com.XSanGo.Protocol.LotteryCycleView;
import com.XSanGo.Protocol.LotteryScoreRankView;
import com.XSanGo.Protocol.MarksmanScoreRankView;
import com.XSanGo.Protocol.MarksmanScoreRewardView;
import com.XSanGo.Protocol.MarksmanView;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RankPageView;
import com.XSanGo.Protocol.RoleBaseSub;
import com.XSanGo.Protocol.ShareSub;
import com.XSanGo.Protocol.ShareView;
import com.XSanGo.Protocol.SummationActivityView;
import com.XSanGo.Protocol.UpActivityInfoView;
import com.XSanGo.Protocol._ActivityInfoDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

public class ActivityI extends _ActivityInfoDisp {
	private IRole roleRt;

	public ActivityI(IRole role) {
		this.roleRt = role;
	}

	private static final long serialVersionUID = -7926356393893541057L;

	@Override
	public String activityList(Current __current) throws NoteException {
		ActivityInfoAll all = new ActivityInfoAll();
		all.actViewSeq = this.roleRt.getActivityControler().getActivityList();
		List<ApiActView> list = this.roleRt.getApiController().openApiAct();
		if (list != null && list.size() > 0) {
			all.actApiDetailSeq = list.toArray(new ApiActView[0]);
		}
		return LuaSerializer.serialize(all);
	}

	@Override
	public String upActivityInfoList(Current __current) throws NoteException {
		UpActivityInfoView[] as = this.roleRt.getActivityControler().getUpActivityList();
		return LuaSerializer.serialize(as);
	}

	@Override
	public boolean getGift(int giftId, Current __current) throws NoteException {
		return this.roleRt.getActivityControler().getGift(giftId);
	}

	@Override
	public boolean clickMakeVip(Current __current) {
		roleRt.getRoleOpenedMenu().setOpenMakeVipDate(new Date());
		return roleRt.getMakeVipControler().isAnswer();
	}

	@Override
	public String beginAnswer(Current __current) throws NoteException {
		return this.roleRt.getMakeVipControler().beginAnswer();
	}

	// @Override
	// public String answer(int id, String result, Current __current)
	// throws NoteException {
	// return LuaSerializer.serialize(this.roleRt.getMakeVipControler()
	// .answer(id, result));
	// }

	@Override
	public int endAnswer(String str, Current __current) throws NoteException {
		return this.roleRt.getMakeVipControler().endAnswer(str);
	}

	@Override
	public String getSummationActivityViewForCharge(Current __current) throws NoteException {
		SummationActivityView view = this.roleRt.getSumChargeActivityControler().getView();

		return LuaSerializer.serialize(view);
	}

	@Override
	public void receiveRewardForSumCharge(int threshold, Current __current) throws NoteException,
			NotEnoughYuanBaoException {
		this.roleRt.getSumChargeActivityControler().receiveReward(threshold);
	}

	@Override
	public String getSummationActivityViewForConsume(Current __current) throws NoteException {
		SummationActivityView view = this.roleRt.getSumConsumeActivityControler().getView();
		return LuaSerializer.serialize(view);

	}

	@Override
	public void receiveRewardForSumConsume(int threshold, Current __current) throws NoteException,
			NotEnoughYuanBaoException {
		this.roleRt.getSumConsumeActivityControler().receiveReward(threshold);
	}

	@Override
	public String getInviteActivityView(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getInviteActivityControler().getInviteActivityView());
	}

	@Override
	public void receiveRewardForInvite(int threshoId, Current __current) throws NoteException {
		this.roleRt.getInviteActivityControler().receiveRewardForInvite(threshoId);
	}

	@Override
	public String getFundView(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getFundControler().getFundView());
	}

	@Override
	public void acceptFundReward(int level, Current __current) throws NoteException {
		this.roleRt.getFundControler().acceptFundReward(level);
	}

	@Override
	public void buyFund(Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		this.roleRt.getFundControler().buyFund();
	}

	@Override
	public String getLevelRewardView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getLevelRewardControler().getLevelRewardView());
	}

	@Override
	public void acceptLevelReward(int level, Current __current) throws NotEnoughMoneyException, NoteException {
		roleRt.getLevelRewardControler().acceptLevelReward(level);
	}

	@Override
	public String getSeckillView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getSeckillControler().getSeckillActivityView());
	}

	@Override
	public void seckillItem(int id, Current __current) throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getSeckillControler().seckillItem(id);
	}

	@Override
	public String getDayChargeView(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getDayChargeControler().getDayChargeView());
	}

	@Override
	public void receiveDayCharge(int threshold, Current __current) throws NoteException {
		this.roleRt.getDayChargeControler().receiveDayCharge(threshold);
	}

	@Override
	public String getDayConsumeView(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getDayConsumeControler().getDayConsumeView());
	}

	@Override
	public void receiveDayConsume(int threshold, Current __current) throws NoteException {
		this.roleRt.getDayConsumeControler().receiveDayConsume(threshold);
	}

	@Override
	public String getPowerRewardView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getPowerRewardControler().getPowerRewardView());
	}

	@Override
	public void acceptPowerReward(int power, Current __current) throws NoteException {
		roleRt.getPowerRewardControler().acceptPowerReward(power);
	}

	@Override
	public String getFirstJiaRewardView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFirstJiaControler().getFirstJiaView());
	}

	@Override
	public void acceptFirstJiaReward(int level, Current __current) throws NotEnoughMoneyException, NoteException {
		roleRt.getFirstJiaControler().acceptLevelReward(level);
	}

	@Override
	public String getDayLoginRewardView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDayLoginControler().getLevelRewardView());
	}

	@Override
	public void acceptDayLoginReward(int day, Current __current) throws NotEnoughMoneyException, NoteException {
		roleRt.getDayLoginControler().acceptDayLoginReward(day);
	}

	@Override
	public String getDayforverLoginRewardView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDayforverLoginControler().getLevelRewardView());
	}

	@Override
	public void acceptDayforverLoginReward(int day, Current __current) throws NotEnoughMoneyException, NoteException {
		roleRt.getDayforverLoginControler().acceptDayLoginReward(day);
	}

	@Override
	public String getSendJunLingView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getSendJunLingControler().getSendJunLingView());
	}

	@Override
	public int acceptJunLing(int id, Current __current) throws NotEnoughYuanBaoException, NoteException {
		return roleRt.getSendJunLingControler().acceptJunLing(id);
	}

	@Override
	public String getBigDayChargeView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getBigDayChargeControler().getDayChargeView());
	}

	@Override
	public void receiveBigDayCharge(int threshold, Current __current) throws NoteException {
		roleRt.getBigDayChargeControler().receiveDayCharge(threshold);
	}

	@Override
	public String getBigDayConsumeView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getBigDayConsumeControler().getDayConsumeView());
	}

	@Override
	public void receiveBigDayConsume(int threshold, Current __current) throws NoteException {
		roleRt.getBigDayConsumeControler().receiveDayConsume(threshold);
	}

	@Override
	public String getBigSummationActivityViewForCharge(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getBigSumChargeActivityControler().getView());
	}

	@Override
	public void receiveBigRewardForSumCharge(int threshold, Current __current) throws NotEnoughYuanBaoException,
			NoteException {
		this.roleRt.getBigSumChargeActivityControler().receiveReward(threshold);
	}

	@Override
	public String getBigSummationActivityViewForConsume(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getBigSumConsumeActivityControler().getView());
	}

	@Override
	public void receiveBigRewardForSumConsume(int threshold, Current __current) throws NotEnoughYuanBaoException,
			NoteException {
		this.roleRt.getBigSumConsumeActivityControler().receiveReward(threshold);
	}

	@Override
	public String getFortuneWheelView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFortuneWheelControler().getFortuneWheelView());
	}

	@Override
	public String doFortuneWheel(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFortuneWheelControler().doFortuneWheel());
	}

	@Override
	public String doFortuneWheelFor10(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFortuneWheelControler().doRortuneWheelFor10());
	}

	@Override
	public String getCornucopiaView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getCornucopiaControler().getCornucopiaView());
	}

	@Override
	public void buyAllCornucopia(Current __current) throws NoteException, NotEnoughYuanBaoException {
		roleRt.getCornucopiaControler().buyAllCornucopia();
	}

	@Override
	public void buyCornucopia(int id, Current __current) throws NoteException, NotEnoughYuanBaoException {
		roleRt.getCornucopiaControler().buyCornucopia(id);
	}

	@Override
	public void getSupperCornucopia(Current __current) throws NoteException {
		roleRt.getCornucopiaControler().getSupperCornucopia();
	}

	@Override
	public void getCornucopia(int id, Current __current) throws NoteException {
		roleRt.getCornucopiaControler().getCornucopia(id);
	}

	/**
	 * 聚宝盆一键领取
	 * 
	 * @param id
	 * @param __current
	 * @throws NoteException
	 */
	@Override
	public void getAllCornucopia(Current __current) throws NoteException {
		roleRt.getCornucopiaControler().getAllCornucopia();
	}

	@Override
	public String getExchangeItems(int itemType, Current __current) throws NoteException {
		ExchangeView[] views = roleRt.getExchangeItemControler().getExchangeViews(itemType);
		return LuaSerializer.serialize(views);
	}

	@Override
	public String doExchangeItems(String exchangeNo, int itemType, Current __current) throws NoteException,
			NotEnoughYuanBaoException, NotEnoughMoneyException {
		ExchangeResult result = roleRt.getExchangeItemControler().doExchange(exchangeNo, itemType);

		// System.out.println(LuaSerializer.serialize(result));
		return LuaSerializer.serialize(result);
	}

	@Override
	public LevelWealView levelWealInfo(Current __current) throws NoteException {
		return new LevelWealView();
		// return roleRt.getLevelWealControler().levelWealInfo();
	}

	@Override
	public int getLevelWeal(Current __current) throws NoteException {
		return 0;
		// return roleRt.getLevelWealControler().getLevelWeal();
	}

	@Override
	public String openMarksmanView(int systemType, Current __current) throws NoteException {
		MarksmanView marksmanView = roleRt.getShootControler().openMarksmanView(systemType);
		return LuaSerializer.serialize(marksmanView);
	}

	@Override
	public String shootReward(int shootType, int systemType, Current __current) throws NotEnoughException,
			NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException {
		MarksmanView marksmanView = roleRt.getShootControler().shootReward(shootType, systemType);
		return LuaSerializer.serialize(marksmanView);
	}

	@Override
	public String getScoreReward(int score, Current __current) throws NoteException {
		return roleRt.getShootControler().getScoreReward(score);
	}

	@Override
	public String openMarksmanScoreRankView(Current __current) throws NoteException {
		MarksmanScoreRankView scoreRankView = roleRt.getShootControler().openMarksmanScoreRankView();
		return LuaSerializer.serialize(scoreRankView);
	}

	@Override
	public String openMarksmanScoreRewardView(Current __current) throws NoteException {
		MarksmanScoreRewardView scoreRewardView = roleRt.getShootControler().openMarksmanScoreRewardView();
		return LuaSerializer.serialize(scoreRewardView);
	}

	@Override
	public void receiveApiReward(int actId, int rewardId, Current __current) throws NoteException {
		roleRt.getApiController().receiveApiReward(actId, rewardId);
	}

	@Override
	public String getOpenServerActiveView(Current __current) throws NoteException {
		return roleRt.getOpenServerActiveControler().getOpenServerActiveView();
	}

	@Override
	public void acceptOpenServerActiveReward(int active, int nodeId, Current __current) throws NoteException {
		roleRt.getOpenServerActiveControler().acceptOpenServerActiveReward(active, nodeId);
	}

	@Override
	public String buysale(int activeId, int day, Current __current) throws NotEnoughMoneyException,
			NotEnoughYuanBaoException, NoteException {
		return roleRt.getOpenServerActiveControler().buySaleItem(activeId, day);
	}

	@Override
	public String gridPageView(Current __current) throws NoteException {
		GridPageView view = this.roleRt.getLotteryControler().gridPageView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String lettoryshopView(Current __current) throws NoteException {
		LettoryShopView view = this.roleRt.getLotteryControler().lettoryshopView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String throwBall(int type, int point, Current __current) throws NoteException, NotEnoughYuanBaoException,
			NotEnoughMoneyException {
		RoleBaseSub view = this.roleRt.getLotteryControler().throwBall(type, point);
		return LuaSerializer.serialize(view);
	}

	@Override
	public String lettoryshopBuy(int id, Current __current) throws NotEnoughMoneyException, NotEnoughYuanBaoException,
			NoteException {
		LettoryShopView view = this.roleRt.getLotteryControler().lettoryshopBuy(id);
		return LuaSerializer.serialize(view);
	}

	@Override
	public String lettoryRankView(Current __current) throws NoteException {
		RankPageView view = this.roleRt.getLotteryControler().lettoryRankView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String lotteryScoreRankView(Current __current) throws NoteException {
		LotteryScoreRankView view = this.roleRt.getLotteryControler().lotteryScoreRankView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String lotteryCycleView(Current __current) throws NoteException {
		LotteryCycleView view = this.roleRt.getLotteryControler().lotteryCycleView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String sharePageView(Current __current) throws NoteException {
		ShareView view = this.roleRt.getShareControler().sharePageView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String share(int id, Current __current) throws NoteException {
		ShareSub view = this.roleRt.getShareControler().share(id);
		return LuaSerializer.serialize(view);
	}

	@Override
	public String getResourceBackView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getResourceBackControler().getResourceBackView());
	}

	@Override
	public void acceptResourceBack(String date, int type, int slot, Current __current) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		roleRt.getResourceBackControler().accept(date, type, slot);
	}

	@Override
	public void acceptResourceBackOneKey(String date, Current __current) throws NotEnoughMoneyException,
			NotEnoughYuanBaoException, NoteException {
		roleRt.getResourceBackControler().acceptOneKey(date);
	}

	/**
	 * 是否显示我的中奖记录
	 */
	@Override
	public void showMyRecord(boolean show, Current __current) throws NoteException {
		this.roleRt.getShootControler().showMyRecord(show);
	}

	/**
	 * 累计获得的奖励
	 */
	@Override
	public String historyAward(Current __current) throws NoteException {
		return this.roleRt.getShootControler().historyAward();
	}

	@Override
	public String getFootballView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFootballControler().getFootballView());
	}

	@Override
	public String getCountryMatch(int countryId, Current __current) throws NoteException {
		return null;
	}

	@Override
	public int buyTrophy(Current __current) throws NoteException {
		return roleRt.getFootballControler().buyTrophy();
	}

	@Override
	public int footballBet(int id, int countryId, int num, Current __current) throws NoteException {
		return roleRt.getFootballControler().footballBet(id, countryId, num);
	}

	@Override
	public int footballExchange(int id, int num, Current __current) throws NoteException {
		return roleRt.getFootballControler().footballExchange(id, num);
	}

	@Override
	public String footballRank(Current __current) throws NoteException {
		return LuaSerializer.serialize(XsgActivityManage.getInstance().getFootballRank());
	}

	@Override
	public String footballBetLogs(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFootballControler().footballBetLogs());
	}

	@Override
	public String getFootballShops(Current __current) throws NoteException {
		FootballConfT conf = XsgActivityManage.getInstance().getFootballConfT();
		Date openDate = DateUtil.parseDate("yyyy-MM-dd", conf.shopDate);
		if (new Date().before(openDate)) {
			throw new NoteException(TextUtil.format(Messages.getString("FootballControler.shopOpenDate"),
					DateUtil.format(openDate, "MM月dd号")));
		}
		List<FootballShop> views = new ArrayList<FootballShop>();
		List<FootballShopT> list = XsgActivityManage.getInstance().getFootballShopT();
		for (FootballShopT s : list) {
			views.add(new FootballShop(s.id, s.itemId, s.price));
		}
		return LuaSerializer.serialize(views.toArray(new FootballShop[0]));
	}

}
