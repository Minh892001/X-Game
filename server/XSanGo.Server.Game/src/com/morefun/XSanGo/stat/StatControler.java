package com.morefun.XSanGo.stat;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.CurrencyType;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.stat.LoginAndOutLog;
import com.morefun.XSanGo.db.stat.StatDao;
import com.morefun.XSanGo.event.protocol.IAuctionMoneyChange;
import com.morefun.XSanGo.event.protocol.IItemCountChange;
import com.morefun.XSanGo.event.protocol.IJinbiChange;
import com.morefun.XSanGo.event.protocol.IOffline;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;

public class StatControler implements IStatControler, IOffline, IJinbiChange, IYuanbaoChange, IItemCountChange,
		IAuctionMoneyChange {
	private static final Logger logger = LoggerFactory.getLogger(StatControler.class);
	private IRole role;

	public StatControler(IRole role) {
		this.role = role;
		this.role.getEventControler().registerHandler(IOffline.class, this);
		this.role.getEventControler().registerHandler(IYuanbaoChange.class, this);
		this.role.getEventControler().registerHandler(IJinbiChange.class, this);
		this.role.getEventControler().registerHandler(IItemCountChange.class, this);
		this.role.getEventControler().registerHandler(IAuctionMoneyChange.class, this);
	}

	@Override
	public void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int change) {
		XsgStatManager.getInstance().onBindYuanbaoChange(newBind - oldBind);
		XsgStatManager.getInstance().onUnbindYuanbaoChange(newUnbind - oldUnbind);
	}

	@Override
	public void onJinbiChange(long change) throws Exception {
		XsgStatManager.getInstance().onJinbiChange(change);
	}

	@Override
	public void onRoleOffline(long onlineInterval) {
		// 记录统计数据
		Date leave = this.role.getLogoutTime();
		Date enter = this.role.getLoginTime();
		int interval = (int) ((leave.getTime() - enter.getTime()) / 1000);

		try {
			final StatDao dao = StatDao.getFromApplicationContext(ServerLancher.getAc());

			final LoginAndOutLog log = new LoginAndOutLog(ServerLancher.getServerId(), this.role.getAccount(),
					this.role.getAccountChannel(), this.role.getRoleId(), enter, leave, interval);

			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					dao.save(log);
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void onItemCountChange(IItem item, int change) {
		String templateId = item.getTemplate().getId();
		if (templateId.equals("g_exp")) {// 公会奖章
			XsgStatManager.getInstance().onFactionTokenChange(change);
		} else if (templateId.equals("med1")) {// 军令
			XsgStatManager.getInstance().handleValueChange(CurrencyType.MilitaryOrder, change);
		}
	}

	@Override
	public void onAuctionMoneyChange(long change) {
		XsgStatManager.getInstance().handleValueChange(CurrencyType.AuctionMoney, change);
	}
}
