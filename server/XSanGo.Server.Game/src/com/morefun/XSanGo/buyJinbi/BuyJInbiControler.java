/**
 * 
 */
package com.morefun.XSanGo.buyJinbi;

import java.util.Date;

import com.XSanGo.Protocol.BuyJinbiView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IBuyJinbi;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * 点金手 功能
 * 
 * @author 吕明涛
 * 
 */
class BuyJInbiControler implements IBuyJInbiControler {

	// private static final Log log =
	// LogFactory.getLog(BuyJInbiControler.class);

	private IRole roleRt;
	private Role roleDb;

	private IBuyJinbi eventBuy; // 点金手事件

	public BuyJInbiControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.eventBuy = this.roleRt.getEventControler().registerEvent(IBuyJinbi.class);
	}

	/**
	 * 查询金币的购买次数
	 */
	@Override
	public String selectBuyNum() throws NoteException {

		// 每天重置购买次数
		if (this.roleDb.getBuyJinbiTime() == null || DateUtil.isSameDay(new Date(), this.roleDb.getBuyJinbiTime())) {
			return String.valueOf(this.roleDb.getBuyJinbiNum());
		} else {
			this.roleDb.setBuyJinbiNum(0);
			return "0"; //$NON-NLS-1$
		}
	}

	/**
	 * 元宝购买金币
	 */
	@Override
	public BuyJinbiView buy() throws NoteException, NotEnoughYuanBaoException {
		BuyJinbiView resJinbiView = new BuyJinbiView();

		VipT vipT = XsgVipManager.getInstance().findVipT(this.roleRt.getVipLevel());
		if (this.roleDb.getBuyJinbiNum() >= vipT.GoldNum) {
			throw new NoteException(Messages.getString("BuyJInbiControler.1")); //$NON-NLS-1$
		}
		BuyJInbiT buyJInbiT = XsgBuyJInbiManager.getInstance().getBuyJInbiT(roleDb.getBuyJinbiNum() + 1);
		if (buyJInbiT == null) {
			throw new NoteException(Messages.getString("BuyJInbiControler.2")); //$NON-NLS-1$
		}
		if (this.roleRt.getTotalYuanbao() < buyJInbiT.costYuanbao) {
			throw new NotEnoughYuanBaoException();
		}
		// 公会科技加成
		int technologyValue = roleRt.getFactionControler().getTechnologyValue(101);
		// 计算暴击的概率
		if (NumberUtil.isHit(buyJInbiT.crit, 10000)) {
			int minCrite = Integer.valueOf(buyJInbiT.rate.split(",")[0]); //$NON-NLS-1$
			int maxCrite = Integer.valueOf(buyJInbiT.rate.split(",")[1]) + 1; //$NON-NLS-1$
			int crit = NumberUtil.random(minCrite, maxCrite);

			resJinbiView.crit = crit;
			resJinbiView.money = (buyJInbiT.minJinbi + technologyValue) * crit;
		} else {
			resJinbiView.crit = 0;
			resJinbiView.money = buyJInbiT.minJinbi + technologyValue;
		}

		try {
			// 元宝和金币变化
			this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, buyJInbiT.costYuanbao));
			this.roleRt.winJinbi(resJinbiView.money);

			// 点金手次数增加 和 购买日期
			this.roleDb.setBuyJinbiNum(this.roleDb.getBuyJinbiNum() + 1);
			this.roleDb.setBuyJinbiTime(new Date());

		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("BuyJInbiControler.5")); //$NON-NLS-1$
		} catch (NotEnoughMoneyException e) {
			e.printStackTrace();
		}

		// 点金手事件
		try {
			eventBuy.onbuy(this.roleDb.getBuyJinbiNum(), resJinbiView.crit, resJinbiView.money);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resJinbiView;
	}
}