package com.morefun.XSanGo.vip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.RandomRange;

public final class XsgVipManager {

	private static XsgVipManager instance = new XsgVipManager();

	public static XsgVipManager getInstance() {
		return instance;
	}

	/** vip */
	private Map<Integer, VipT> vipT = new HashMap<Integer, VipT>();
	public Map<String, VipTraderT> vipTraderT = new HashMap<String, VipTraderT>();
	public List<FirstTopupT> firstTopupTList = new ArrayList<FirstTopupT>();
	public Map<Integer, ChargeItemT> topupItemT = new HashMap<Integer, ChargeItemT>();
	public Map<Integer, VipColorT> vipColorT = new HashMap<Integer, VipColorT>();
	private RandomRange<VipTraderRandomHitable> range;

	private XsgVipManager() {
		for (VipT r : ExcelParser.parse(VipT.class)) {
			this.vipT.put(r.VIPLv, r);
		}

		List<VipTraderRandomHitable> list = new ArrayList<VipTraderRandomHitable>();
		for (VipTraderT r : ExcelParser.parse(VipTraderT.class)) {
			this.vipTraderT.put(r.id + "", r);
			list.add(new VipTraderRandomHitable(r));
		}
		this.range = new RandomRange<VipTraderRandomHitable>(list);

		this.loadMonthFirstChargeScript();

		for (ChargeItemT r : ExcelParser.parse(ChargeItemT.class)) {
			this.topupItemT.put(r.id, r);
		}

		for (VipColorT r : ExcelParser.parse(VipColorT.class)) {
			this.vipColorT.put(r.vipQuality, r);
		}
	}

	/**
	 * 加载月首充礼包脚本
	 */
	private void loadMonthFirstChargeScript() {
		this.firstTopupTList = ExcelParser.parse(FirstTopupT.class);
	}

	public int getMaxVipLevel() {
		if (vipT.size() == 0) {
			return 0;
		} else {
			return Collections.max(vipT.keySet());
		}
	}

	public int getMaxExperienceOfVip(int vipLevel) {
		if (vipT.get(vipLevel + 1) != null) {
			return vipT.get(vipLevel + 1).BuyYB;
		}
		return vipT.get(getMaxVipLevel()).BuyYB;
	}

	public List<ChargeItemT> getAllChargeItemT() {
		return new ArrayList<ChargeItemT>(this.topupItemT.values());
	}

	/**
	 * 根据充值选项ID获取配置模板
	 * 
	 * @param chargeId
	 * @return
	 */
	public ChargeItemT getChargeItemT(int chargeId) {
		return this.topupItemT.get(chargeId);
	}

	public IVipController createVipControler(IRole rt, Role db) {
		return new VipControler(rt, db);
	}

	/**
	 * 获取首充奖励
	 * 
	 * @return
	 */
	public FirstTopupT getFirstTopupRewards() {
		final String currentMonth = DateUtil.toString(System.currentTimeMillis(), "yyyyMM");
		FirstTopupT firstT = CollectionUtil.first(this.firstTopupTList, new IPredicate<FirstTopupT>() {
			@Override
			public boolean check(FirstTopupT item) {
				return item.RefreshTime.equals(currentMonth);
			}
		});
		if (firstT == null) {
			firstT = this.firstTopupTList.get(this.firstTopupTList.size() - 1);
		}

		return firstT;
	}

	/**
	 * 随机获取VIP商城物品
	 * 
	 * @return
	 */
	public VipTraderT randomVipTraderItem() {
		return this.range.random().getTraderT();
	}

	public VipT findVipT(int vipLevel) {
		return this.vipT.get(vipLevel);
	}

	public int getVipSize() {
		return this.vipT.size();
	}

	/**
	 * 根据金额获取充值选项ID
	 * 
	 * @param yuan
	 * @return
	 */
	public ChargeItemT getChargeItemByMoney(int yuan) {

		Set<Entry<Integer, ChargeItemT>> entrySet = this.topupItemT.entrySet();

		for (Entry<Integer, ChargeItemT> en : entrySet) {
			if (en.getValue().rmb == yuan) {
				return en.getValue();
			}
		}

		return null;
	}
}
