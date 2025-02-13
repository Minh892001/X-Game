package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.XSanGo.Protocol.CornucopiaItem;
import com.XSanGo.Protocol.CornucopiaView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCornucopia;
import com.morefun.XSanGo.event.protocol.ICornucopiaBuyAll;
import com.morefun.XSanGo.event.protocol.ICornucopiaBuyOne;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 聚宝盆
 * @author zhuzhi.yang
 *
 */
@RedPoint(isTimer = true)
public class CornucopiaControler implements ICornucopiaControler {

	private IRole iRole;
	private Role role;
	// 购买所有
	private ICornucopiaBuyAll cornucopiaBuyAllEvent;
	// 购买一项
	private ICornucopiaBuyOne cornucopiaBuyOneEvent;
	private boolean firstOpen = true;

	public CornucopiaControler(IRole rt, Role db) {
		this.iRole = rt;
		this.role = db;
		cornucopiaBuyAllEvent = iRole.getEventControler().registerEvent(ICornucopiaBuyAll.class);
		cornucopiaBuyOneEvent = iRole.getEventControler().registerEvent(ICornucopiaBuyOne.class);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		try {
//			if (firstOpen) {
//				firstOpen = false;
//				return new MajorUIRedPointNote(MajorMenu.CornucopiaMenu, false);
//			}
			CornucopiaView view = getCornucopiaView();
			if (view.superState == 1) {// 可以领取超值物品
				return new MajorUIRedPointNote(MajorMenu.CornucopiaMenu, false);
			}
			for (CornucopiaItem i : view.items) {
				if (i.isBuy && !i.isReceive) {
					return new MajorUIRedPointNote(MajorMenu.CornucopiaMenu, false);
				}
			}
		} catch (NoteException e) {
			return null;
		}
		return null;
	}

	@Override
	public CornucopiaView getCornucopiaView() throws NoteException {
		removeOutDate();

		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		List<CornucopiaItemT> itemTs = XsgActivityManage.getInstance().getCornucopiaItemTs();

		List<CornucopiaItem> items = new ArrayList<CornucopiaItem>();
		for (CornucopiaItemT i : itemTs) {
			items.add(new CornucopiaItem(i.id, i.itemId, i.itemName, i.num, i.price, i.tips, role.getRoleCornucopia()
					.containsKey(i.id), isReceive(i.id), getRemainDays(i.id)));
		}
		int superState = 0;// 不可领
		if (role.isReceiveCornucopia()) {
			superState = 2;// 已领取
		} else if (role.getRoleCornucopia().size() == itemTs.size()) {
			superState = 1;// 可领取
		}
		CornucopiaView view = new CornucopiaView(items.toArray(new CornucopiaItem[0]),
				confT.superItems.toArray(new Property[0]), superState, confT.receiveDays, confT.vipLevel,
				confT.discount);
		return view;
	}

	/**
	 * 移除过期购买项
	 */
	private void removeOutDate() {
		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		Iterator<RoleCornucopia> it = this.role.getRoleCornucopia().values().iterator();
		while (it.hasNext()) {
			RoleCornucopia rc = it.next();
			if (DateUtil.diffDate(new Date(), rc.getBuyDate()) > confT.receiveDays) {
				it.remove();
			}
		}
	}

	/**
	 * 判断当天是否领取过
	 * 
	 * @param id
	 * @return
	 */
	private boolean isReceive(int id) {
		RoleCornucopia rc = this.role.getRoleCornucopia().get(id);
		if (rc == null) {
			return false;
		}
		return rc.getLastReceiveDate() != null && DateUtil.isSameDay(new Date(), rc.getLastReceiveDate());
	}

	/**
	 * 获取剩余天数
	 * 
	 * @param id
	 * @return
	 */
	private int getRemainDays(int id) {
		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		RoleCornucopia rc = this.role.getRoleCornucopia().get(id);
		if (rc == null) {
			return 0;
		}
		return confT.receiveDays - DateUtil.diffDate(new Date(), rc.getBuyDate());
	}

	@Override
	public void buyAllCornucopia() throws NoteException, NotEnoughYuanBaoException {
		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		CornucopiaView view = getCornucopiaView();
		List<CornucopiaItemT> buyableItemT = new ArrayList<CornucopiaItemT>();
		int sumMoney = 0;
		int buyCount = 0;
		for (CornucopiaItem i : view.items) {
			if (!i.isBuy) {
				buyableItemT.add(XsgActivityManage.getInstance().getCornucopiaItemT(i.id));
				sumMoney += i.price;
				buyCount++;
			}
		}
		if (iRole.getVipLevel() < confT.vipLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroMarketControler.needVip"), confT.vipLevel));
		}
		if (buyableItemT.isEmpty()) {
			throw new NoteException(Messages.getString("CornucopiaControler.notBuy"));
		}
		// 计算折扣，至少2项才有折扣
		if (buyCount > 1) {
			sumMoney = sumMoney * confT.discount / 100;
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, sumMoney));
		} catch (NotEnoughMoneyException e) {
		}
		for (CornucopiaItemT i : buyableItemT) {
			RoleCornucopia rc = new RoleCornucopia(GlobalDataManager.getInstance().generatePrimaryKey(), role, i.id,
					DateUtil.getFirstSecondOfToday().getTime(), null);
			role.getRoleCornucopia().put(i.id, rc);
		}

		cornucopiaBuyAllEvent.onCornucopiaBuyAll(sumMoney);
	}

	@Override
	public void buyCornucopia(int id) throws NoteException, NotEnoughYuanBaoException {
		removeOutDate();

		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		if (iRole.getVipLevel() < confT.vipLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroMarketControler.needVip"), confT.vipLevel));
		}
		CornucopiaItemT itemT = XsgActivityManage.getInstance().getCornucopiaItemT(id);
		if (itemT == null) {
			throw new NoteException(Messages.getString("CornucopiaControler.notExist"));
		}
		if (role.getRoleCornucopia().containsKey(id)) {
			throw new NoteException(Messages.getString("CornucopiaControler.notDoubleBuy"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, itemT.price));
		} catch (NotEnoughMoneyException e) {
		}
		RoleCornucopia rc = new RoleCornucopia(GlobalDataManager.getInstance().generatePrimaryKey(), role, itemT.id,
				DateUtil.getFirstSecondOfToday().getTime(), null);
		role.getRoleCornucopia().put(itemT.id, rc);

		cornucopiaBuyOneEvent.onCornucopiaBuyOne(id, itemT.price);
	}

	@Override
	public void getSupperCornucopia() throws NoteException {
		CornucopiaView view = getCornucopiaView();
		CornucopiaConfT confT = XsgActivityManage.getInstance().getCornucopiaConfT();
		if (view.superState == 0) {
			throw new NoteException(Messages.getString("CornucopiaControler.needBuyAll"));
		} else if (view.superState == 2) {
			throw new NoteException(Messages.getString("CornucopiaControler.notDoubleGet"));
		}
		role.setReceiveCornucopia(true);
		// 发放物品
		for (Property p : confT.superItems) {
			iRole.getRewardControler().acceptReward(p.code, p.value);
		}
	}

	@Override
	public void getCornucopia(int id) throws NoteException {
		removeOutDate();

		CornucopiaItemT itemT = XsgActivityManage.getInstance().getCornucopiaItemT(id);
		if (itemT == null) {
			throw new NoteException(Messages.getString("CornucopiaControler.notExist"));
		}
		RoleCornucopia rc = role.getRoleCornucopia().get(id);
		if (rc == null) {
			throw new NoteException(Messages.getString("CornucopiaControler.needBuyGet"));
		}
		Date now = new Date();
		if (rc.getLastReceiveDate() != null && DateUtil.isSameDay(now, rc.getLastReceiveDate())) {
			throw new NoteException(Messages.getString("CornucopiaControler.todayIsGet"));
		}
		rc.setLastReceiveDate(now);
		iRole.getRewardControler().acceptReward(itemT.itemId, itemT.num);
	}
	
	/**
	 * 一键领取
	 */
	@Override
	public void getAllCornucopia() throws NoteException {
		removeOutDate();

		if(role.getRoleCornucopia() == null)	return;
		
		for(RoleCornucopia rc : role.getRoleCornucopia().values()){			
			if (rc == null) { // 未购买
				continue;
			}
			CornucopiaItemT itemT = XsgActivityManage.getInstance().getCornucopiaItemT(rc.getScriptId());
			if (itemT == null) { // 物品不存在
				continue;
			}
			Date now = new Date(); // 今天已领取
			if (rc.getLastReceiveDate() != null && DateUtil.isSameDay(now, rc.getLastReceiveDate())) {
				continue;
			}
			rc.setLastReceiveDate(now);
			iRole.getRewardControler().acceptReward(itemT.itemId, itemT.num);
		}
	}

	@Override
	public void setFirstOpen(boolean value) {
		this.firstOpen = value;
	}

}
