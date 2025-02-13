package com.morefun.XSanGo.vip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.GmPayLogView;
import com.XSanGo.Protocol.GmPayView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TopupItem;
import com.XSanGo.Protocol.TopupReward;
import com.XSanGo.Protocol.TopupView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCharge;
import com.morefun.XSanGo.db.game.RoleVip;
import com.morefun.XSanGo.db.game.RoleVipGiftPacks;
import com.morefun.XSanGo.db.game.RoleVipTraderItem;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.IFriendCharge;
import com.morefun.XSanGo.event.protocol.IVipGiftBuy;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.event.protocol.IVipShopItemBuy;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class VipControler implements IVipController {
	private static int Max_Mall_Item_Count = 8;

	IPrivilegeView privilegeView = new IPrivilegeView();

	private IRole roleRt;
	private RoleVip vipDb;
	private Random random = new Random();
	private ICharge chargeEvent;
	private IFriendCharge friendChargeEvent;
	private IVipGiftBuy buyVipGiftEvent;
	private IVipShopItemBuy vipShopBuyEvent;
	private IVipLevelUp vipLevelUpEvent;
	// 月卡道具使用时间限制
	private static int MonthCardExpireDays = 1 * 30; // 一个月

	public VipControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.vipDb = db.getVip();
		if (this.vipDb == null) {
			this.vipDb = new RoleVip(db);
		}

		this.chargeEvent = this.roleRt.getEventControler().registerEvent(ICharge.class);
		this.friendChargeEvent = this.roleRt.getEventControler().registerEvent(IFriendCharge.class);
		this.buyVipGiftEvent = this.roleRt.getEventControler().registerEvent(IVipGiftBuy.class);
		this.vipShopBuyEvent = this.roleRt.getEventControler().registerEvent(IVipShopItemBuy.class);
		this.vipLevelUpEvent = this.roleRt.getEventControler().registerEvent(IVipLevelUp.class);
	}

	@Override
	public int getLevel() {
		return this.vipDb.getVipLevel();
	}

	@Override
	public int getExperience() {
		return this.vipDb.getVipExperience();
	}

	@Override
	public void addExperience(int exp) throws IllegalArgumentException {
		if (exp == 0) {
			return;
		}

		int maxLevel = XsgVipManager.getInstance().getMaxVipLevel();
		if (getLevel() >= maxLevel && exp > 0) {
			return;
		}

		int totalExp = this.getExperience() + exp;
		totalExp = Math.max(0, totalExp);

		int roleVipLevel = 0;
		int levelUpExp = XsgVipManager.getInstance().getMaxExperienceOfVip(roleVipLevel);
		// 只要没满级就逐级判断
		while (totalExp >= levelUpExp && roleVipLevel < maxLevel) {
			roleVipLevel++;
			levelUpExp = XsgVipManager.getInstance().getMaxExperienceOfVip(roleVipLevel);
		}
		int oldVipLevel = roleRt.getVipLevel();
		this.vipDb.setVipLevel(roleVipLevel);
		if (oldVipLevel != roleVipLevel) {// 只通知一次

			roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.VIP_Level, this.getLevel());
			// 原来如果技能点是满的则上限有可能会提升，重新计时
			this.roleRt.getHeroControler().refreshHeroSkillData();
			this.vipLevelUpEvent.onVipLevelUp(this.vipDb.getVipLevel());
		}

		// 最后判断是否满级，经验超出部分丢弃
		this.vipDb.setVipExperience(this.getLevel() >= maxLevel ? levelUpExp : totalExp);
		roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.VIP_Exp, this.getExperience());

	}

	// @Override
	// public void addExperience(int exp) throws IllegalArgumentException {
	// // if (exp < 0) {
	// // throw new IllegalArgumentException(
	//		//					Messages.getString("VipControler.0")); //$NON-NLS-1$
	// // }
	// if (exp == 0) {
	// return;
	// }
	//
	// int maxLevel = XsgVipManager.getInstance().getMaxVipLevel();
	// if (getLevel() >= maxLevel && exp > 0) {
	// return;
	// }
	//
	// int totalExp = this.getExperience() + exp;
	// boolean levelUp = false;
	//
	// int levelUpExp =
	// XsgVipManager.getInstance().getMaxExperienceOfVip(getLevel());
	// // 只要没满级就逐级判断
	// while (totalExp >= levelUpExp && this.getLevel() < maxLevel) {
	// levelUp = true;
	// this.vipDb.setVipLevel(this.vipDb.getVipLevel() + 1);
	// levelUpExp =
	// XsgVipManager.getInstance().getMaxExperienceOfVip(getLevel());
	// }
	// if (levelUp) {// 只通知一次
	//
	// roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.VIP_Level,
	// this.getLevel());
	//
	// // 原来如果技能点是满的则上限有可能会提升，重新计时
	// this.roleRt.getHeroControler().refreshHeroSkillData();
	// try {
	// this.vipLevelUpEvent.vipLevelUp(this.vipDb.getVipLevel());
	// } catch (Exception e) {
	// }
	// }
	//
	// // 最后判断是否满级，经验超出部分丢弃
	// this.vipDb.setVipExperience(this.getLevel() >= maxLevel ? levelUpExp :
	// totalExp);
	// roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.VIP_Exp,
	// this.getExperience());
	// }

	@Override
	public IPrivilegeView getPrivilegeView() {
		return privilegeView;
	}

	@Override
	public int getMaxLevel() {
		return XsgVipManager.getInstance().getMaxVipLevel();
	}

	@Override
	public void buyVipTraderItems(int Id) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		String idStr = String.valueOf(Id);
		if (!XsgVipManager.getInstance().vipTraderT.containsKey(idStr)) {
			return;
		}

		VipTraderT vipTraderT = XsgVipManager.getInstance().vipTraderT.get(idStr);
		if (vipTraderT.buyVipLevel > this.getLevel()) {
			throw new NoteException(Messages.getString("VipControler.1")); //$NON-NLS-1$
		}
		String itemTemplate = vipTraderT.itemId;
		for (RoleVipTraderItem item : this.vipDb.getVipTraderItems()) {
			if (idStr.equals(item.getItemTid())) {
				if (item.getBoughtToday()) {
					throw new NoteException(Messages.getString("VipControler.2")); //$NON-NLS-1$
				}

				this.roleRt.reduceCurrency(new Money(CurrencyType.valueOf(vipTraderT.coinType), vipTraderT.price
						* item.getCount()));

				roleRt.getItemControler().changeItemByTemplateCode(itemTemplate, item.getCount());
				item.setBoughtToday(true);
				this.vipShopBuyEvent.onBuyVipShopItem(itemTemplate, item.getCount());
			}
		}
	}

	@Override
	public Collection<RoleVipTraderItem> getTraderItems() {
		String[] t = getTraderRefreshingTime().split(":"); //$NON-NLS-1$
		int h = Integer.parseInt(t[0]);
		int m = Integer.parseInt(t[1]);
		Calendar refreshPoint = Calendar.getInstance();
		refreshPoint.set(Calendar.HOUR_OF_DAY, h);
		refreshPoint.set(Calendar.MINUTE, m);
		refreshPoint.set(Calendar.SECOND, 0);
		if (this.vipDb.getLastRefreshingVipTraderTime() == null || DateUtil.isPass(getTraderRefreshingTime(), "HH:mm", //$NON-NLS-1$
				this.vipDb.getLastRefreshingVipTraderTime())) {
			refreshTrader();
			this.vipDb.setLastRefreshingVipTraderTime(new Date());
		}

		return this.vipDb.getVipTraderItems();
	}

	/**
	 * 更新商城中的道具
	 */
	void refreshTrader() {
		this.vipDb.getVipTraderItems().clear();

		Map<Integer, RoleVipTraderItem> m = new HashMap<Integer, RoleVipTraderItem>();
		while (m.size() < Max_Mall_Item_Count) {
			VipTraderT item = XsgVipManager.getInstance().randomVipTraderItem();
			if (m.containsKey(item.id)) {
				continue;
			}

			if (item.vipMin <= this.getLevel()) {
				m.put(item.id, new RoleVipTraderItem(GlobalDataManager.getInstance().generatePrimaryKey(), this.vipDb,
						item.id + "", //$NON-NLS-1$
						item.numMax - item.numMin > 0 ? item.numMin + random.nextInt(item.numMax + 1 - item.numMin)
								: item.numMax, false));
			}
		}
		this.vipDb.getVipTraderItems().addAll(m.values());
	}

	@Override
	public String getTraderRefreshingTime() {
		return "12:00"; //$NON-NLS-1$
	}

	@Override
	public void buyGiftPacks(int vipLevel) throws NotEnoughYuanBaoException, NoteException {
		if (this.vipDb.getVipGiftPacks().containsKey(vipLevel)) {
			throw new NoteException(Messages.getString("VipControler.7")); //$NON-NLS-1$
		}
		VipT vt = XsgVipManager.getInstance().findVipT(vipLevel);
		if (vt == null) {
			throw new NoteException(vipLevel + ""); //$NON-NLS-1$
		}

		this.roleRt.winYuanbao(-vt.PriceNow, true);
		roleRt.getItemControler().changeItemByTemplateCode(vt.Reward, vt.Num);
		this.vipDb.getVipGiftPacks().put(vipLevel,
				new RoleVipGiftPacks(GlobalDataManager.getInstance().generatePrimaryKey(), this.vipDb, vipLevel));
		this.buyVipGiftEvent.onVipGiftBuy(vipLevel, vt.Reward, vt.Num);
	}

	public Collection<RoleVipGiftPacks> getRoleGiftPacks() {
		return this.vipDb.getVipGiftPacks().values();
	}

	@Override
	public void setVipLevel(int vipLevel) {
		if (this.getLevel() > vipLevel) {
			return;
		}
		int max = XsgVipManager.getInstance().getMaxVipLevel();
		vipLevel = Math.min(vipLevel, max);
		// vipLevel = Math.max(vipLevel, 0);
		this.vipDb.setVipLevel(vipLevel);
		this.roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.VIP_Level, this.vipDb.getVipLevel());
		try {
			this.vipLevelUpEvent.onVipLevelUp(vipLevel);
		} catch (Exception e) {
		}
	}

	@Override
	public TopupView getChargeView() {
		FirstTopupT topupT = XsgVipManager.getInstance().getFirstTopupRewards();
		TopupReward[] firstTopup = new TopupReward[4];
		firstTopup[0] = new TopupReward(topupT.Item1ID, topupT.Name1, topupT.Num1);
		firstTopup[1] = new TopupReward(topupT.Item2ID, topupT.Name2, topupT.Num2);
		firstTopup[2] = new TopupReward(topupT.Item3ID, topupT.Name3, topupT.Num3);
		firstTopup[3] = new TopupReward(topupT.Item4ID, topupT.Name4, topupT.Num4);
		List<ChargeItemT> items = XsgVipManager.getInstance().getAllChargeItemT();
		TopupItem[] topupItems = new TopupItem[items.size()];
		for (int i = 0; i < items.size(); i++) {
			ChargeItemT item = items.get(i);
			topupItems[i] = new TopupItem(item.id, item.name, item.recommand, item.isMonthCard(), item.icon,
					item.quality, item.rmb, item.yuanbao, this.organizeChargeItemDesc(item));
		}

		return new TopupView(firstTopup, topupItems);
	}

	/**
	 * 组织单个充值选项的描述信息
	 * 
	 * @param item
	 * @return
	 */
	private String organizeChargeItemDesc(ChargeItemT item) {
		int count = this.getChargeTimes(item.id);

		String giftStr = item.normalReturnYuanbao > 0 ? TextUtil.format(
				Messages.getString("VipControler.10"), item.normalReturnYuanbao) : ""; //$NON-NLS-1$ //$NON-NLS-2$
		if (item.isMonthCard()) {
			if (this.hasMonthCard()) {
				return TextUtil.format(Messages.getString("VipControler.12"), this //$NON-NLS-1$
						.getDateIntervalDesc(this.vipDb.getMonthExpireTime().getTime() - System.currentTimeMillis()));
			} else {
				return TextUtil.format(Messages.getString("VipControler.13"), giftStr, //$NON-NLS-1$
						item.limitReturnYuanbao);
			}
		}
		if (item.timeLimit > count) {
			return TextUtil.format(Messages.getString("VipControler.14"), item.limitReturnYuanbao, //$NON-NLS-1$
					item.timeLimit - count);
		}

		return giftStr;
	}

	private String getDateIntervalDesc(long interval) {
		long hour = 3600 * 1000;
		long day = 24 * hour;
		long dayCount = interval / day;
		long hourCount = (interval % day) / hour;
		return TextUtil.format(Messages.getString("VipControler.15"), dayCount, hourCount); //$NON-NLS-1$
	}

	/**
	 * 已充过指定项目多少次
	 * 
	 * @param id
	 * @return
	 */
	private int getChargeTimes(final int id) {
		return CollectionUtil.where(this.vipDb.getRoleCharge(), new IPredicate<RoleCharge>() {
			@Override
			public boolean check(RoleCharge item) {
				return item.getChargeTemplateId() == id;
			}
		}).size();
	}

	// private int getChargeTimeThisMonth(final int id) {
	// final Calendar now = Calendar.getInstance();
	// return CollectionUtil.where(this.vipDb.getRoleCharge(), new
	// IPredicate<RoleCharge>() {
	// @Override
	// public boolean check(RoleCharge item) {
	// Calendar c = Calendar.getInstance();
	// c.setTime(item.getCreateTime());
	// return item.getChargeTemplateId() == id && DateUtil.isSameMonth(now, c);
	// }
	// }).size();
	// }

	/**
	 * 本月已充过多少次
	 * 
	 * @return
	 */
	private int getChargeTimeThisMonth() {
		final Calendar now = Calendar.getInstance();
		return CollectionUtil.where(this.vipDb.getRoleCharge(), new IPredicate<RoleCharge>() {
			@Override
			public boolean check(RoleCharge item) {
				Calendar c = Calendar.getInstance();
				c.setTime(item.getCreateTime());
				return DateUtil.isSameMonth(now, c);
			}
		}).size();
	}

	@Override
	public void checkChargeStatus(int chargeId, boolean chargeForFriend) throws NoteException {
		// 检查月卡续费等状态
		ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(chargeId);
		if (template == null) {
			throw new NoteException(Messages.getString("VipControler.16")); //$NON-NLS-1$
		}

		if (template.isMonthCard()) {
			if (chargeForFriend) {
				throw new NoteException(Messages.getString("VipControler.17")); //$NON-NLS-1$
			}
			if (this.hasMonthCard()) {
				Calendar expire = Calendar.getInstance();
				expire.setTime(this.vipDb.getMonthExpireTime());
				Calendar check = DateUtil.addDays(expire, -3);// 月卡剩余3天内才能续费
				if (Calendar.getInstance().before(check)) {
					throw new NoteException(Messages.getString("VipControler.18")); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * 是否有VIP月卡
	 * 
	 * @return
	 */
	@Override
	public boolean hasMonthCard() {
		Date now = new Date();
		return this.vipDb.getMonthExpireTime() != null && this.vipDb.getMonthExpireTime().after(now);
	}

	@Override
	public boolean useMonthCardItem(int dayCount) throws NoteException {
		Date expireTime = this.vipDb.getMonthExpireTime();
		Date currentTime = Calendar.getInstance().getTime();
		if (expireTime != null && DateUtil.diffDate(expireTime, currentTime) >= MonthCardExpireDays) {
			throw new NoteException(Messages.getString("VipControler.TooManyDays"));
		}
		Calendar c = Calendar.getInstance();
		if (hasMonthCard()) { // 当前有月卡
			c.setTime(expireTime);
		}
		c = DateUtil.addDays(c, dayCount);
		// 更新月卡时间
		this.vipDb.setMonthExpireTime(c.getTime());
		return true;
	}

	@Override
	public void acceptCharge(CustomChargeParams params, int yuan, String orderId, String currency) {
		int item = params.item;
		ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(item);

		// 这两个判断值必须在新的充值记录加入前获取
		int limitReturnCheck = this.getChargeTimes(item);
		int firstCheck = this.getChargeTimeThisMonth();

		// 先走标准流程，然后是首充，月卡等特殊处理
		try {
			// 判断是否是从豪情宝过来的充值
			int haoqingbaoYuanbao = 0;
			if (roleRt.getHaoqingbaoController().shouldAddToHaoqingbao()) {
				haoqingbaoYuanbao += template.yuanbao;
			} else { // 非豪情宝充值，进入正常账户
				this.roleRt.winYuanbao(template.yuanbao, false);
			}
			// 充值记录，增加VIP经验
			this.vipDb.getRoleCharge().add(
					new RoleCharge(GlobalDataManager.getInstance().generatePrimaryKey(), this.vipDb, item, new Date(),
							yuan));
			// 累计充值
			this.vipDb.setChargeHistory(this.vipDb.getChargeHistory() + yuan);

			this.addExperience(template.yuanbao);

			// 返利处理
			int returnYuanbao = (template.timeLimit > limitReturnCheck) ? template.limitReturnYuanbao
					: template.normalReturnYuanbao;
			// 返利元宝不加入豪情宝，直接加入元宝账户
			if (returnYuanbao > 0) {
				this.roleRt.winYuanbao(returnYuanbao, true);
			}

			if (haoqingbaoYuanbao > 0) {
				roleRt.getHaoqingbaoController().acceptCharge(haoqingbaoYuanbao,
						Messages.getString("HaoqingbaoController.checkIn"));
			}

			// 首充处理201411211540策划两巨头确认，首充只是噱头，实际不做处理，但是奖励还是要给
			// 20141218封测首日，首充计算配置失效，所有有效充值均可领取礼包
			// if (template.isEffectAtFirst()) {
			if (firstCheck == 0) {
				FirstTopupT firstT = XsgVipManager.getInstance().getFirstTopupRewards();
				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				rewardMap.put(firstT.Item1ID, Integer.valueOf(firstT.Num1));
				rewardMap.put(firstT.Item2ID, Integer.valueOf(firstT.Num2));
				rewardMap.put(firstT.Item3ID, Integer.valueOf(firstT.Num3));
				rewardMap.put(firstT.Item4ID, Integer.valueOf(firstT.Num4));
				this.roleRt.getMailControler().receiveRoleMail(MailTemplate.First_Charge_Monthly, rewardMap, null);
			}

			// 月卡处理
			if (template.isMonthCard()) {
				Calendar c = Calendar.getInstance();
				if (this.hasMonthCard()) {// 当前有月卡则延期
					c.setTime(this.vipDb.getMonthExpireTime());
				}

				c = DateUtil.addDays(c, 30);
				this.vipDb.setMonthExpireTime(c.getTime());
			}

			this.roleRt.getNotifyControler().onPropertyChange("firstCharge", 0); //$NON-NLS-1$
			this.chargeEvent.onCharge(params, returnYuanbao, orderId, currency);
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}
	}

	@Override
	public void acceptChargeFromFriend(int item, int yuan, String friendAccount) {
		ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(item);
		try {
			this.roleRt.winYuanbao(template.yuanbao, false);
			this.roleRt.winYuanbao(template.normalReturnYuanbao, true);
			this.friendChargeEvent.onChargeFromFriend(template, friendAccount);
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}

	}

	/**
	 * 根据VIP的品质，来取得VIP的颜色
	 */
	@Override
	public String getVipColor() {
		int vipQuality = XsgVipManager.getInstance().findVipT(this.getLevel()).Quality;
		return XsgVipManager.getInstance().vipColorT.get(vipQuality).ColorValue;
	}

	@Override
	public boolean hasFirstCharge() {
		return this.getChargeTimeThisMonth() < 1;
	}

	@Override
	public int getChargeYuanbao(Date begin, Date end) {
		int result = 0;
		for (RoleCharge rc : this.vipDb.getRoleCharge()) {
			if (DateUtil.isBetween(rc.getCreateTime(), begin, end)) {
				ChargeItemT item = XsgVipManager.getInstance().getChargeItemT(rc.getChargeTemplateId());
				if (item != null) {
					result += item.yuanbao;
				}
			}
		}
		return result;
	}

	@Override
	public int getMonthChargeDays(Date date) {
		int result = 0;
		Calendar cal = Calendar.getInstance();
		List<Integer> existDay = new ArrayList<Integer>();
		for (RoleCharge rc : this.vipDb.getRoleCharge()) {
			cal.setTime(rc.getCreateTime());
			int day = DateUtil.getMonyhDay(cal);
			if (DateUtil.isSameMonth(rc.getCreateTime(), date) && !existDay.contains(day)) {
				existDay.add(day);
				result++;
			}
		}
		return result;
	}

	@Override
	public long getBindYuanbao() {
		return this.vipDb.getBindYuanbao();
	}

	@Override
	public long getUnBindYuanbao() {
		return this.vipDb.getUnbindYuanbao();
	}

	@Override
	public int getSumCharge() {
		return (int) this.vipDb.getChargeHistory();
	}

	@Override
	public int getMaxSkillPoint() {
		return this.getVipT().maxSkillPoint;
	}

	@Override
	public VipT getVipT() {
		return XsgVipManager.getInstance().findVipT(getLevel());
	}

	@Override
	public GmPayView getChargeHistory() {
		String roleName = this.roleRt.getName();
		List<GmPayLogView> list = new ArrayList<GmPayLogView>();
		for (RoleCharge rc : this.vipDb.getRoleCharge()) {
			ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(rc.getChargeTemplateId());
			if (template != null) {
				list.add(new GmPayLogView(roleName, DateUtil.toString(rc.getCreateTime().getTime()), template.rmb));
			}
		}

		return new GmPayView(this.getSumCharge(), list.toArray(new GmPayLogView[0]));
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!roleRt.isOnline()) {
			return null;
		}
		if (roleRt.getRoleOpenedMenu().getOpenVipGiftDate() != null
				&& DateUtil.isSameDay(new Date(), roleRt.getRoleOpenedMenu().getOpenVipGiftDate())) {
			return null;
		}
		for (int vipLevel = 1; vipLevel < this.roleRt.getVipLevel(); vipLevel++) {
			// 没有购买特权礼包
			if (!this.vipDb.getVipGiftPacks().containsKey(vipLevel)) {
				return new MajorUIRedPointNote(MajorMenu.VipGiftPacksMenu, true);
			}
		}
		return null;
	}
}
