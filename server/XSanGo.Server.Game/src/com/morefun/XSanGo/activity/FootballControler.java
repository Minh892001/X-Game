package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FootballBetLog;
import com.XSanGo.Protocol.FootballMatch;
import com.XSanGo.Protocol.FootballView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.db.game.FootballBet;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFootball;
import com.morefun.XSanGo.event.protocol.IFootballBet;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class FootballControler implements IFootballControler {
	private IRole iRole;
	private Role role;
	private IFootballBet footballBetEvent;

	public FootballControler(IRole iRole, Role role) {
		this.iRole = iRole;
		this.role = role;
		footballBetEvent = iRole.getEventControler().registerEvent(IFootballBet.class);
		if (this.role.getFootball() == null) {
			this.role.setFootball(new RoleFootball(role.getId(), role));
		}
	}

	@Override
	public FootballView getFootballView() throws NoteException {
		FootballConfT conf = XsgActivityManage.getInstance().getFootballConfT();
		if (conf.isOpen == 0) {
			throw new NoteException("is not open");
		}
		if (iRole.getLevel() < conf.openLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("FootballControler.openLevel"), conf.openLevel));
		}
		if (iRole.getVipLevel() < conf.openVipLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("FootballControler.openVipLevel"),
					conf.openVipLevel));
		}
		refreshNum();
		List<FootballMatch> matchViews = new ArrayList<FootballMatch>();
		List<FootballMatchT> matchTs = XsgActivityManage.getInstance().getFootballMatchTList();
		for (FootballMatchT m : matchTs) {
			matchViews.add(createFootballMatch(m));
		}
		RoleFootball football = role.getFootball();
		FootballView view = new FootballView(System.currentTimeMillis() / 1000, conf.startDate, conf.endDate,
				!football.isOpen(), false, 0, football.getBuyNum(), matchViews.toArray(new FootballMatch[0]));
		if (!football.isOpen()) {
			// 首次打开
			football.setOpen(true);
			iRole.getRewardControler().acceptReward(XsgActivityManage.JIANG_BEI_ID, conf.giveNum);
		}
		// 比赛结束
		if (DateUtil.parseDate(conf.overDate).getTime() <= System.currentTimeMillis() && !football.isOverOpen()) {
			view.isOverFirst = true;
			football.setOverOpen(true);
		}
		view.trophyNum = iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID);
		return view;
	}

	private void refreshNum() {
		RoleFootball football = role.getFootball();
		if (football.getRefreshDate() == null || DateUtil.isPass("06:00:00", "HH:mm:ss", football.getRefreshDate())) {
			football.setBuyNum(0);
			football.setRefreshDate(new Date());
		}
	}

	private FootballMatch createFootballMatch(FootballMatchT m) {
		int leftScore = 0;
		int rightScore = 0;
		if (m.isOver == 1 && TextUtil.isNotBlank(m.score)) {
			String[] score = m.score.split(":");
			leftScore = NumberUtil.parseInt(score[0]);
			rightScore = NumberUtil.parseInt(score[1]);
		}
		FootballBet bet = XsgActivityManage.getInstance().getFootballBet(m.id, iRole.getRoleId());
		return new FootballMatch(m.id, m.isOver == 1 ? 1 : 0, leftScore, rightScore, bet == null ? 0
				: bet.getBetCountryId(), bet == null ? 0 : bet.getBetNum(), m.date, m.time, m.leftCountryId,
				m.rightCountryId, m.leftOdds, m.rightOdds, m.drawOdds, m.minBet, m.maxBet,
				m.beginBetDate.getTime() / 1000, m.endBetDate.getTime() / 1000, m.gameType);
	}

	@Override
	public FootballMatch[] getCountryMatch(int countryId) throws NoteException {
		List<FootballMatch> matchViews = new ArrayList<FootballMatch>();
		String countryName = "";
		List<FootballMatchT> matchTs = XsgActivityManage.getInstance().getFootballMatchTList();
		for (FootballMatchT m : matchTs) {
			if (m.leftCountry.equals(countryName) || m.rightCountry.equals(countryName)) {
				matchViews.add(createFootballMatch(m));
			}
		}
		return matchTs.toArray(new FootballMatch[0]);
	}

	@Override
	public int buyTrophy() throws NoteException {
		refreshNum();
		RoleFootball football = role.getFootball();
		FootballBuyT buyT = XsgActivityManage.getInstance().getFootballBuyT(football.getBuyNum() + 1);
		if (buyT == null) {
			throw new NoteException(Messages.getString("FootballControler.buyFull"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, buyT.yuanbao));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		iRole.getRewardControler().acceptReward(XsgActivityManage.JIANG_BEI_ID, buyT.trophyNum);
		football.setBuyNum(buyT.num);
		return iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID);
	}

	@Override
	public int footballBet(int id, int countryId, int num) throws NoteException {
		FootballMatchT matchT = XsgActivityManage.getInstance().getFootballMatchT(id);
		FootballCountryT countryT = XsgActivityManage.getInstance().getFootballCountryT(countryId);
		if (matchT == null || (countryT == null && countryId != XsgActivityManage.DRAW) || num <= 0
				|| (countryT != null && matchT.leftCountryId != countryId && matchT.rightCountryId != countryId)) {
			throw new NoteException("params error");
		}
		// 奖杯数量
		int itemNum = iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID);
		if (itemNum < num) {
			throw new NoteException(Messages.getString("FootballControler.notJiangbeiBet"));
		}

		FootballBet bet = XsgActivityManage.getInstance().getFootballBet(matchT.id, iRole.getRoleId());
		if (bet != null && bet.getBetCountryId() != countryId) {
			throw new NoteException(Messages.getString("FootballControler.betOne"));
		}

		int alreadyBet = bet == null ? 0 : bet.getBetNum();
		if (alreadyBet + num > matchT.maxBet) {
			throw new NoteException(Messages.getString("FootballControler.betFull"));
		}

		if (!DateUtil.isBetween(matchT.beginBetDate, matchT.endBetDate)) {
			throw new NoteException(Messages.getString("FootballControler.betOver"));
		}

		iRole.getRewardControler().acceptReward(XsgActivityManage.JIANG_BEI_ID, -num);
		if (bet == null) {
			bet = new FootballBet(GlobalDataManager.getInstance().generatePrimaryKey(), matchT.id, iRole.getRoleId(),
					countryId, num);
			XsgActivityManage.getInstance().addFootballBet(bet);
		} else {
			bet.setBetNum(bet.getBetNum() + num);
			XsgActivityManage.getInstance().saveFootballBet(bet);
		}
		// 押注日志
		FootballBetLog[] betLogs = TextUtil.GSON.fromJson(role.getFootball().getBetLogs(), FootballBetLog[].class);
		List<FootballBetLog> logList = new ArrayList<FootballBetLog>(Arrays.asList(betLogs));
		if (countryT == null) {
			logList.add(0, new FootballBetLog(DateUtil.format(new Date(), Messages.getString("FactionControler.52")),
					TextUtil.format(Messages.getString("FootballControler.betDrawLog"), id, num)));
		} else {
			logList.add(0, new FootballBetLog(DateUtil.format(new Date(), Messages.getString("FactionControler.52")),
					TextUtil.format(Messages.getString("FootballControler.betLog"), id, num, countryT.name)));
		}
		if (logList.size() > 100) {
			logList.remove(logList.size() - 1);
		}
		role.getFootball().setBetLogs(TextUtil.GSON.toJson(logList.toArray(new FootballBetLog[0])));

		footballBetEvent.onFootballBet(id, num, countryId);
		return iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID);
	}

	@Override
	public int footballExchange(int id, int num) throws NoteException {
		if (num <= 0) {
			throw new NoteException("params error");
		}
		FootballShopT shopT = XsgActivityManage.getInstance().getFootballShopT(id);
		if (shopT == null) {
			throw new NoteException("shop is not exist");
		}
		long sumPrice = shopT.price * num;
		if (iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID) < sumPrice) {
			throw new NoteException(Messages.getString("FootballControler.notJbExchange"));
		}
		iRole.getRewardControler().acceptReward(XsgActivityManage.JIANG_BEI_ID, (int) -sumPrice);
		iRole.getRewardControler().acceptReward(shopT.itemId, num);
		return iRole.getItemControler().getItemCountInPackage(XsgActivityManage.JIANG_BEI_ID);
	}

	@Override
	public FootballBetLog[] footballBetLogs() throws NoteException {
		return TextUtil.GSON.fromJson(role.getFootball().getBetLogs(), FootballBetLog[].class);
	}

}
