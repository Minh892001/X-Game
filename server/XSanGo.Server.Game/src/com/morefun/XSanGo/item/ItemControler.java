/**
 * 
 */
package com.morefun.XSanGo.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.EquipLevelEntity;
import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.XSanGo.Protocol.SmeltParam;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.TemplateNotFoundException;
import com.morefun.XSanGo.chat.ChestItemBroadcastT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.FormulaUtil;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.equip.EquipRebuildT;
import com.morefun.XSanGo.equip.EquipSmeltT;
import com.morefun.XSanGo.equip.EquipStarT;
import com.morefun.XSanGo.equip.EquipStarUpConditionT;
import com.morefun.XSanGo.equip.XsgEquipManager;
import com.morefun.XSanGo.event.protocol.IBuffAdvance;
import com.morefun.XSanGo.event.protocol.ICompositeChestItemDraw;
import com.morefun.XSanGo.event.protocol.IEquipHole;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipSmelt;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IGemSet;
import com.morefun.XSanGo.event.protocol.IHeroEquipChange;
import com.morefun.XSanGo.event.protocol.IHeroUseItem;
import com.morefun.XSanGo.event.protocol.IItemCountChange;
import com.morefun.XSanGo.event.protocol.IItemSale;
import com.morefun.XSanGo.event.protocol.INormalItemUse;
import com.morefun.XSanGo.event.protocol.ITcItemUse;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class ItemControler implements IItemControler {

	private IRole rt;
	private Role db;
	private Map<String, IItem> itemMap;

	private IEquipStarUp starUpEvent;
	private IEquipStrengthen strengthenEvent;
	private IEquipRebuild rebuildEvent;
	private IItemCountChange itemCountEvent;
	private IHeroUseItem heroUseItemEvent;
	private IItemSale itemSaleEvent;
	private ITcItemUse tcItemEvent;
	private INormalItemUse itemUseEvent;
	private IEquipSmelt equipSmeltEvent;
	private IGemSet gemSet;
	private IHeroEquipChange equipChange;
	private IEquipHole equipHole;
	/** 领取复合宝箱产出事件 */
	private ICompositeChestItemDraw compositeChestItemDraw;
	private IBuffAdvance buffAdvanceEvent;

	public ItemControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.starUpEvent = this.rt.getEventControler().registerEvent(IEquipStarUp.class);
		this.strengthenEvent = this.rt.getEventControler().registerEvent(IEquipStrengthen.class);
		this.rebuildEvent = this.rt.getEventControler().registerEvent(IEquipRebuild.class);
		this.itemCountEvent = this.rt.getEventControler().registerEvent(IItemCountChange.class);
		this.heroUseItemEvent = this.rt.getEventControler().registerEvent(IHeroUseItem.class);
		this.itemSaleEvent = this.rt.getEventControler().registerEvent(IItemSale.class);
		this.tcItemEvent = this.rt.getEventControler().registerEvent(ITcItemUse.class);
		this.itemUseEvent = this.rt.getEventControler().registerEvent(INormalItemUse.class);
		this.equipSmeltEvent = this.rt.getEventControler().registerEvent(IEquipSmelt.class);
		this.gemSet = this.rt.getEventControler().registerEvent(IGemSet.class);
		this.equipChange = this.rt.getEventControler().registerEvent(IHeroEquipChange.class);
		this.equipHole = this.rt.getEventControler().registerEvent(IEquipHole.class);
		this.compositeChestItemDraw = this.rt.getEventControler().registerEvent(ICompositeChestItemDraw.class);
		this.buffAdvanceEvent = this.rt.getEventControler().registerEvent(IBuffAdvance.class);

		this.itemMap = new HashMap<String, IItem>();
		for (RoleItem ri : this.db.getItems().values()) {
			this.createItemFromDb(ri);
		}
	}

	@Override
	public int getItemCountInPackage(final String template) {
		IItem item = this.getItemByTemplateId(template);
		return item == null ? 0 : item.getNum();
	}

	@Override
	public IItem getItem(String itemId) {
		return this.getItem(itemId, IItem.class);
	}

	// @Override
	// public List<EquipItem> getEquipList() {
	// List<EquipItem> list = new ArrayList<EquipItem>();
	// for (IItem item : this.itemMap.values()) {
	// if (item.getTemplate() instanceof EquipItemT) {
	// list.add((EquipItem) item);
	// }
	// }
	//
	// return list;
	// }

	@Override
	public <T> T getItem(String id, Class<T> type) {
		IItem item = this.itemMap.get(id);
		T result = null;
		try {
			if (item != null) {
				result = type.cast(item);
			}
		} catch (ClassCastException e) {

		}

		return result;
	}

	@Override
	public void changeItemById(String id, int change) {
		NumberUtil.checkRange(change, -Const.Ten_Thousand * 10, Const.Ten_Thousand * 10);
		if (change == 0) {// 不处理
			return;
		}
		IItem item = this.getItem(id);
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (item.getNum() + change < 0) {
			throw new IllegalArgumentException();
		}

		item.setNum(item.getNum() + change);
		if (item.getNum() == 0) {
			this.removeItem(id);
		}

		this.onItemCountChanged(item, change);
		this.rt.getNotifyControler().onItemChange(item);
	}

	@Override
	public List<IItem> changeItemByTemplateCode(String code, int change) {
		NumberUtil.checkRange(change, -Const.Hundred_Thousand, Const.Hundred_Thousand);
		List<IItem> list = new ArrayList<IItem>();
		if (change == 0) {
			return list;
		}

		IItem item = this.getItemByTemplateId(code);
		if (change < 1) {// 扣物品的情况
			if (item == null) {
				throw new IllegalArgumentException();
			}
			if (item.getNum() + change < 0) {
				throw new IllegalArgumentException();
			}

			item.setNum(item.getNum() + change);
			this.rt.getNotifyControler().onItemChange(item);

			if (item.getNum() == 0) {
				this.removeItem(item.getId());
			}

			this.onItemCountChanged(item, change);
			list.add(item);
			return list;
		}

		// 增加物品的情况
		if (XsgItemManager.getInstance().findAbsItemT(code).canOverlay()) {
			int oldNum = 0;
			if (item != null) {
				oldNum = item.getNum();
				item.setNum(item.getNum() + change);
			} else {
				RoleItem ri = new RoleItem(GlobalDataManager.getInstance().generatePrimaryKey(), db, code, change);
				this.db.getItems().put(ri.getId(), ri);
				item = this.createItemFromDb(ri);
			}

			if (item.getNum() > XsgItemManager.getInstance().getMaxOverlayCount()) {
				item.setNum(XsgItemManager.getInstance().getMaxOverlayCount());
				change = item.getNum() - oldNum;
			}

			list.add(item);
			this.onItemCountChanged(item, change);
			this.rt.getNotifyControler().onItemChange(item);
		} else {
			for (int i = 0; i < change; i++) {
				RoleItem ri = new RoleItem(GlobalDataManager.getInstance().generatePrimaryKey(), db, code, 1);
				list.add(this.addItemFromDb(ri));
			}
		}

		return list;
	}

	@Override
	public boolean isPackageFull() {
		return this.itemMap.size() >= XsgGameParamManager.getInstance().getBagItemLimit();

		// ItemType type =
		// XsgItemManager.getInstance().getItemType(newItemCode);
		// int count = 0;
		// for (IItem item : this.itemMap.values()) {
		// if (item.getTemplate().getItemType() == type) {
		// count++;
		// }
		// }
	}

	private void onItemCountChanged(IItem item, int change) {
		this.itemCountEvent.onItemCountChange(item, change);
	}

	/**
	 * 获取指定模板编号的物品，对于装备等不可叠加物品，不保证返回对象一致性
	 * 
	 * @param code
	 * @return
	 */
	private IItem getItemByTemplateId(final String code) {
		return CollectionUtil.first(this.itemMap.values(), new IPredicate<IItem>() {

			@Override
			public boolean check(IItem item) {
				return item.getTemplate().getId().equals(code);
			}
		});
	}

	/**
	 * 从数据库对象创建物品内存对象
	 * 
	 * @param ri
	 * @return
	 */
	private IItem createItemFromDb(RoleItem ri) {
		IItem item = null;
		try {
			item = XsgItemManager.getInstance().createItem(ri);
			this.itemMap.put(ri.getId(), item);
		} catch (TemplateNotFoundException e) {
		}

		return item;
	}

	@Override
	public void sale(String id, int count) throws NoteException {
		if (count < 1) {
			throw new NoteException(Messages.getString("ItemControler.0")); //$NON-NLS-1$
		}

		IItem item = this.getItem(id);
		if (item == null) {
			throw new NoteException(Messages.getString("ItemControler.21")); //$NON-NLS-1$
		}
		AbsItemT template = item.getTemplate();
		if (!template.canSale()) {
			throw new NoteException(Messages.getString("ItemControler.1")); //$NON-NLS-1$
		}

		int money = template.getSaleMoney() * count;
		if (item instanceof EquipItem) {
			money += ((EquipItem) item).getTotalLevelupConsume() / 3;
		}

		this.changeItemById(id, -count);
		try {
			this.rt.winJinbi(money);
		} catch (NotEnoughMoneyException e) {
			// ignore
		}

		this.itemSaleEvent.onItemSaled(item, count, money);
	}

	@Override
	public ItemView[] getItemViewList() {
		List<ItemView> list = new ArrayList<ItemView>();
		for (IItem item : this.itemMap.values()) {
			list.add(item.getView());
		}

		return list.toArray(new ItemView[0]);
	}

	@Override
	public ItemView[] getItemEquipViewList4GM() {
		List<ItemView> list = new ArrayList<ItemView>();
		for (IItem item : this.itemMap.values()) {
			if (item instanceof EquipItem) {
				list.add(((EquipItem) item).getView4GM());
			}
		}
		return list.toArray(new ItemView[0]);
	}

	@Override
	public EquipLevelEntity[] levelupEquipAll(String id) throws NoteException, NotEnoughMoneyException {
		IHero hero = this.rt.getHeroControler().getHero(id);
		if (hero == null) {
			throw new NoteException(Messages.getString("ItemControler.2")); //$NON-NLS-1$
		}

		int levelupCount = 0; // 升级的装备数量
		int maxLevel = this.rt.getLevel() * 2;
		List<EquipLevelEntity> levelList = new ArrayList<EquipLevelEntity>();
		for (EquipPosition pos : EquipPosition.values()) {
			EquipItem equip = hero.getEquipByPos(pos);
			if (equip != null) {
				int equipLevel = equip.getLevel();
				if (equipLevel < maxLevel) { // 没有达到等级上限，可以升级
					innerLevelupEquipAuto(equip, equipLevel, maxLevel);
					levelupCount++;
					this.rt.getNotifyControler().onItemChange(equip);
				}
				levelList.add(new EquipLevelEntity(equip.getId(), equip.getLevel()));
			}
		}
		if (levelupCount <= 0) {
			throw new NoteException(Messages.getString("ItemControler.3")); //$NON-NLS-1$
		}
		this.rt.getNotifyControler().onHeroChanged(hero);

		EquipLevelEntity[] levelArray = new EquipLevelEntity[levelList.size()];
		return levelList.toArray(levelArray);
	}

	private void innerLevelupEquipAuto(EquipItem equip, int equipLevel, int maxLevel) throws NoteException,
			NotEnoughMoneyException {
		int levelup = maxLevel - equipLevel;
		if (levelup <= 0) {
			return;
		}
		int money = 0;
		for (int i = equipLevel; i < maxLevel; i++) {
			int subMoney = XsgEquipManager.getInstance().caculateEquipLevelupMoney(equip.getQuatityColor(), i);
			money += subMoney;
		}

		this.rt.winJinbi(-money);
		equip.levelup(levelup, money);
		// 一键强化
		this.strengthenEvent.onEquipStrengthen(1, equip, equipLevel, equip.getLevel());
	}

	@Override
	public void levelupEquipAuto(String id) throws NoteException, NotEnoughMoneyException {
		EquipItem equip = this.getItem(id, EquipItem.class);
		if (equip == null) {
			throw new NoteException(Messages.getString("ItemControler.4")); //$NON-NLS-1$
		}

		int maxLevel = this.rt.getLevel() * 2;
		int equipLevel = equip.getLevel();
		if (equipLevel >= maxLevel) {
			throw new NoteException(Messages.getString("ItemControler.5")); //$NON-NLS-1$
		}

		innerLevelupEquipAuto(equip, equipLevel, maxLevel);
	}

	@Override
	public void levelupEquip(String id) throws NoteException, NotEnoughMoneyException {
		EquipItem equip = this.getItem(id, EquipItem.class);
		if (equip == null) {
			throw new NoteException(Messages.getString("ItemControler.6")); //$NON-NLS-1$
		}
		int origLevel = equip.getLevel();

		int maxLevel = this.rt.getLevel() * 2;
		if (equip.getLevel() >= maxLevel) {
			throw new NoteException(Messages.getString("ItemControler.7")); //$NON-NLS-1$
		}

		int levelup = Math.min(5, maxLevel - equip.getLevel()); // 默认升5级(hard
																// code),
																// 防止超过等级上限
		int money = 0;
		// 计算强化消耗金币数量
		for (int i = 0; i < levelup; i++) {
			money += XsgEquipManager.getInstance().caculateEquipLevelupMoney(equip.getQuatityColor(),
					equip.getLevel() + i);
		}

		// 强化逻辑
		// 扣钱

		this.rt.winJinbi(-money);
		equip.levelup(levelup, money);

		// 事件通知, 普通强化
		this.strengthenEvent.onEquipStrengthen(0, equip, origLevel, equip.getLevel());
	}

	@Override
	public void rebuildEquip(String id) throws NoteException {
		EquipItem equip = this.getItem(id, EquipItem.class);
		if (equip == null) {
			throw new NoteException(Messages.getString("ItemControler.8")); //$NON-NLS-1$
		}

		// 扣除道具
		EquipRebuildT template = XsgEquipManager.getInstance().findRebuildT(equip.getQuatityColor());
		this.changeItemByTemplateCode(template.itemTemplateId, -template.itemCount);
		equip.rebuild();

		// 事件通知
		this.rebuildEvent.onEquipRebuild(equip);
	}

	private void removeItem(String id) {
		IItem item = this.itemMap.remove(id);
		this.db.getItems().remove(id);
		if (item == null) {
			return;
		}

		if (item instanceof EquipItem) {
			EquipItem delEquip = (EquipItem) item;
			// 装备原先的使用者
			if (!TextUtil.isBlank(delEquip.getRefereneHero())) {
				IHero referenceHero = this.rt.getHeroControler().getHero(delEquip.getRefereneHero());
				referenceHero.setHeroEquip(delEquip.getEquipPos(), null);
				this.rt.getNotifyControler().onHeroChanged(referenceHero);
			}
		}

		if (item instanceof FormationBuffItem) {
			FormationBuffItem delBuff = (FormationBuffItem) item;
			if (!TextUtil.isBlank(delBuff.getRefereneFormationId())) {
				IFormation referenceFormation = this.rt.getFormationControler().getFormation(
						delBuff.getRefereneFormationId());
				referenceFormation.setBuff(null);
				this.rt.getNotifyControler().onFormationChange(referenceFormation);
			}
		}

	}

	@Override
	public void starUpEquip(String id, String idArray, String items) throws NoteException, NotEnoughMoneyException {
		EquipItem equip = this.getEquipItem(id);
		if (equip == null) {
			throw new NoteException(Messages.getString("ItemControler.9")); //$NON-NLS-1$
		}
		if (equip.getStar() >= Const.MaxStar) {
			throw new NoteException(Messages.getString("ItemControler.10")); //$NON-NLS-1$
		}
		if (equip.getQuatityColor().value() < QualityColor.Blue.value()) {
			throw new NoteException(Messages.getString("ItemControler.11")); //$NON-NLS-1$
		}
		// 要删除的装备
		List<EquipItem> deleteList = new ArrayList<EquipItem>();
		int exp = 0, money = 0;
		if (!TextUtil.isBlank(idArray)) {
			for (String delete : idArray.split(";")) {
				EquipItem delEquip = this.getEquipItem(delete);
				if (delEquip == null) {
					throw new NoteException(Messages.getString("ItemControler.9"));
				}
				// 拆下的宝石
				Map<String, Integer> gemList = removeAllGem(delEquip);
				if (gemList != null && gemList.size() > 0) {
					for (Map.Entry<String, Integer> entry : gemList.entrySet()) {
						// 返还给玩家
						IItem ritem = rt.getRewardControler().acceptReward(entry.getKey(), entry.getValue());
						rt.getNotifyControler().onItemChange(ritem);
					}
				}
				exp += FormulaUtil.caculateEquipProvideStarExp(delEquip);
				// 折算强化费用
				money += (delEquip.getTotalLevelupConsume() / 3);
				deleteList.add(delEquip);
			}
		}
		// 升星石
		// 本次消耗的升星石列表
		Map<String, Integer> consumeStars = new HashMap<String, Integer>();
		if (!TextUtil.isBlank(items)) {
			for (String idNum : items.split(";")) {
				String[] idNums = idNum.split(",");
				NormalItem item = this.getItem(idNums[0], NormalItem.class);
				if (item == null) {
					throw new NoteException(Messages.getString("ItemControler.notStone"));
				}
				NormalItemT template = item.getTemplate(NormalItemT.class);
				int haveNum = getItemCountInPackage(template.id);
				int useNum = Integer.parseInt(idNums[1]);
				if (useNum > haveNum) {
					throw new NoteException(Messages.getString("ItemControler.notStone"));
				}
				exp += Integer.parseInt(template.useValue) * useNum;
				if (consumeStars.containsKey(template.id)) {
					consumeStars.put(template.id, consumeStars.get(template.id) + useNum);
				} else {
					consumeStars.put(template.id, useNum);
				}
			}
		}

		StarUpResult sur = this.simulateStarUp(equip, exp);
		money -= sur.consumeMoney;
		this.rt.winJinbi(money);
		// 删除玩家物品
		for (EquipItem delete : deleteList) {
			this.changeItemById(delete.getId(), -1);
		}
		if (!TextUtil.isBlank(items)) {
			for (String idNum : items.split(";")) {
				String[] idNums = idNum.split(",");
				int useNum = Integer.parseInt(idNums[1]);
				this.changeItemById(idNums[0], -useNum);
			}
		}
		equip.setStar((byte) (equip.getStar() + sur.uplevel));
		equip.setStarExp(sur.remainExp);
		if (sur.uplevel > 0) {// 升星后免费送一次重铸
			equip.rebuild();
		}
		// 事件通知
		this.starUpEvent.onEquipStarUp(equip, sur.uplevel, deleteList, money, exp, consumeStars);
	}

	// /**
	// * 模拟升星操作
	// *
	// * @param totalExp
	// * 总经验
	// * @return
	// */
	// private StarUpResult simulateStarUp(EquipItem equip, int totalExp) {
	// if (totalExp <= equip.getStarExp()) {
	// throw new IllegalArgumentException();
	// }
	//
	// // System.out.println(TextUtil.format("升星前：{0}星{1}经验。", equip.getStar(),
	// // equip.getStarExp()));
	// byte star = equip.getStar();
	// QualityColor color = equip.getQuatityColor();
	// StarUpResult result = new StarUpResult();
	// EquipStarT template = XsgEquipManager.getInstance().findStarT(++star);
	// // 当前已有的经验需要在后面扣除，这里用负数表示
	// EquipStarUpConditionT condition = template.conditions[color.ordinal()];
	// result.consumeMoney = -condition.money * equip.getStarExp();
	// while (template != null) {
	// condition = template.conditions[color.ordinal()];
	// if (totalExp < condition.exp) {
	// result.consumeMoney += condition.money * totalExp;// 升不动了也要耗钱
	// break;
	// }
	//
	// result.uplevel++;
	// totalExp -= condition.exp;
	// result.consumeMoney += condition.money * condition.exp;
	//
	// template = XsgEquipManager.getInstance().findStarT(++star);
	// }
	//
	// result.remainExp = totalExp;
	// // System.out.println(TextUtil.GSON.toJson(result));
	// return result;
	// }

	/**
	 * 模拟升星操作
	 * 
	 * @param allAddExp
	 *            增加的总经验
	 * @return
	 */
	private StarUpResult simulateStarUp(EquipItem equipItem, long allAddExp) {
		if (allAddExp <= 0) {
			throw new IllegalArgumentException();
		}
		byte star = equipItem.getStar();
		QualityColor color = equipItem.getQuatityColor();
		int exp = equipItem.getStarExp();

		StarUpResult result = new StarUpResult();

		EquipStarT template = XsgEquipManager.getInstance().findStarT(++star);
		while (template != null) {
			EquipStarUpConditionT condition = template.conditions[color.ordinal()];
			// 升级所需要的经验
			long upExp = condition.exp - exp;// 用long类型防止超过int最大值
			if (allAddExp >= upExp) {
				allAddExp = allAddExp - upExp;
				exp = 0;
				result.consumeMoney += (int) (condition.money * upExp / condition.exp);
				result.uplevel++;
			} else {
				result.consumeMoney += (int) (condition.money * allAddExp / condition.exp);
				exp += (int) allAddExp;
				break;
			}
			template = XsgEquipManager.getInstance().findStarT(++star);
		}

		result.remainExp = exp;
		return result;
	}

	@Override
	public EquipItem getEquipItem(String id) {
		return this.getItem(id, EquipItem.class);
	}

	@Override
	public void useItem(String id, int count, String params) throws NoteException {
		if (count < 0) {
			throw new NoteException("IllegalArgument " + count); //$NON-NLS-1$
		}
		NormalItem item = this.checkItemUse(id, count);
		NormalItemT template = item.getTemplate(NormalItemT.class);
		this.checkItemUseLevel(template);
		if (item.getExpirationTime() > System.currentTimeMillis()) {// 道具已过期
			throw new NoteException(Messages.getString("ItemControler.overdue"));
		}
		if (template.useCode.equals("exp")) {// 主公经验
			this.useRoleExpItem(item, count);
		} else if (template.useCode.equals("act")) {// 行动力
			useRoleVitItem(item, count, template);
		} else if (template.useCode.equals("exp_general")) {// 武将经验
			useHeroExpItem(item, count, params, template);
		} else if (template.useCode.equals("mcard")) { // 月卡
			useMonthCard(item, count, template);
		} else if (template.useCode.equals("doubleexp")) { // 双倍威望
			useDoubleCard(item, count, template, 0);
		} else if (template.useCode.equals("doubleitem")) { // 双倍掉落
			useDoubleCard(item, count, template, 1);
		} else if (template.useCode.equals("Head")) { // 主公头像
			useHeadItem(item, count, template);
		} else if (template.useCode.equals("HeadBorder")) { // 主公头像边框
			useHeadBorderItem(item, count, template);
		} else {
			throw new NoteException(Messages.getString("ItemControler.16"));
		}

	}

	/**
	 * 使用双倍卡
	 * 
	 * @param item
	 * @param count
	 * @param template
	 * @param type
	 *            0-威望 1-掉落
	 */
	private void useDoubleCard(NormalItem item, int count, NormalItemT template, int type) throws NoteException {
		if (type == 0) {// 双倍威望
			if (db.getDoubleExpOutTime() != null
					&& db.getDoubleExpOutTime().getTime() - System.currentTimeMillis() >= 24 * 60 * 60 * 1000) {
				throw new NoteException(Messages.getString("ItemControler.doubleCardNotUse"));
			}
			if (!rt.isDoubleExp()) {
				db.setDoubleExpOutTime(new Date());
			}
			Calendar now = Calendar.getInstance();
			now.setTime(db.getDoubleExpOutTime());
			now.add(Calendar.MINUTE, NumberUtil.parseInt(template.useValue) * count);
			db.setDoubleExpOutTime(now.getTime());
		} else {
			if (db.getDoubleItemOutTime() != null
					&& db.getDoubleItemOutTime().getTime() - System.currentTimeMillis() >= 24 * 60 * 60 * 1000) {
				throw new NoteException(Messages.getString("ItemControler.doubleCardNotUse"));
			}
			if (!rt.isDoubleItem()) {
				db.setDoubleItemOutTime(new Date());
			}
			Calendar now = Calendar.getInstance();
			now.setTime(db.getDoubleItemOutTime());
			now.add(Calendar.MINUTE, NumberUtil.parseInt(template.useValue) * count);
			db.setDoubleItemOutTime(now.getTime());
		}
		changeItemById(item.getId(), -count);
		rt.getChatControler().getChatCb().begin_updateDoubleCard(LuaSerializer.serialize(rt.getDoubleCardTime()));
		this.itemUseEvent.onItemUse(item, count, count);
	}

	/**
	 * 使用月卡
	 * */
	private void useMonthCard(NormalItem item, int count, NormalItemT template) throws NoteException {
		int dayCount = Integer.parseInt(template.useValue) * count;
		if (rt.getVipController().useMonthCardItem(dayCount)) {
			changeItemById(item.getId(), -count);

			this.itemUseEvent.onItemUse(item, count, count);
		}
	}

	/**
	 * @param template
	 * @throws NoteException
	 */
	private void checkItemUseLevel(NormalItemT template) throws NoteException {
		if (this.rt.getLevel() < template.useLevel && !template.id.endsWith("_per")) {// per是武将百分比经验丹
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.17"), //$NON-NLS-1$
					template.useLevel));
		}
		if (this.rt.getVipLevel() < template.useVipLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.31"), //$NON-NLS-1$
					template.useVipLevel));
		}
	}

	private void useHeroExpItem(NormalItem item, int count, String params, NormalItemT template) throws NoteException {
		IHero hero = this.rt.getHeroControler().getHero(params);
		if (hero == null) {
			throw new NoteException(Messages.getString("ItemControler.18"));
		}
		if (hero.isExpFull()) {
			throw new NoteException(Messages.getString("ItemControler.19"));
		}
		if (hero.getLevel() < template.useLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.heroLevelError"),
					template.useLevel));
		}

		int realCount = 0;
		this.rt.getNotifyControler().setAutoNotify(false);
		while (!hero.isExpFull() && realCount < count) {
			if (template.id.endsWith("_per")) {// 百分比经验丹
				double levelUpExp = XsgHeroManager.getInstance().getLevelupExp(hero.getLevel());
				int addExp = (int) Math.ceil(levelUpExp * NumberUtil.parseInt(template.useValue) / 100);
				hero.winExp(addExp);
			} else {
				hero.winExp(NumberUtil.parseInt(template.useValue));
			}
			realCount++;
		}
		this.changeItemById(item.getId(), -realCount);
		this.rt.getNotifyControler().setAutoNotify(true);
		this.heroUseItemEvent.onItemUseForHero(item.getTemplate().getId(), realCount, hero);
		this.itemUseEvent.onItemUse(item, count, realCount);
	}

	private void useRoleVitItem(NormalItem item, int count, NormalItemT template) throws NoteException {
	}

	private void useRoleExpItem(NormalItem item, int count) throws NoteException {
		int limit = XsgRoleManager.getInstance().getExpLimit(this.rt.getLevel());
		if (this.rt.getPrestige() >= limit) {
			throw new NoteException(Messages.getString("ItemControler.20")); //$NON-NLS-1$
		}
		this.rt.winPrestige(NumberUtil.parseInt(item.getTemplate(NormalItemT.class).useValue) * count);
		this.changeItemById(item.getId(), -count);
		this.itemUseEvent.onItemUse(item, count, count);
	}

	/**
	 * 检查物品当前是否可用，通过检查则返回该物品对象
	 * 
	 * @param id
	 * @param count
	 * @return
	 * @throws NoteException
	 */
	private NormalItem checkItemUse(String id, int count) throws NoteException {
		NormalItem item = this.getItem(id, NormalItem.class);
		if (item == null) {
			throw new NoteException(Messages.getString("ItemControler.21")); //$NON-NLS-1$
		}
		if (item.getNum() < count) {
			throw new NoteException(Messages.getString("ItemControler.22")); //$NON-NLS-1$
		}
		if (!item.getTemplate(NormalItemT.class).canUse()) {
			throw new NoteException(Messages.getString("ItemControler.12")); //$NON-NLS-1$
		}
		return item;
	}

	/**
	 * 开宝箱公告, 规则是 开金宝箱, 开出紫色及以上品质的装备才跑马灯公告
	 * 
	 * */
	private void broadcastChest(List<IItem> rewardItems, String templateId) {
		List<ChestItemBroadcastT> adTList = XsgChatManager.getInstance().getChestItemBroadcastT(templateId);
		if (adTList == null || adTList.isEmpty()) {
			return;
		}

		for (IItem iitem : rewardItems) {
			if (iitem == null) {
				continue;
			}
			AbsItemT itemT = iitem.getTemplate();
			// 紫装及以上公告
			if (itemT != null && itemT.getItemType() == ItemType.EquipItemType
					&& itemT.getColor().value() >= QualityColor.Violet.value()) {
				ChestItemBroadcastT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					XsgChatManager.getInstance().sendAnnouncementItem(iitem,
							rt.getChatControler().parseAdContentItem(itemT, chatAdT.content));
				}
			}
		}

		// if (rewardItems != null
		// && ("ch_gold".equals(templateId) || templateId
		// .startsWith("ch_Sociaty"))) {
		// XsgChatManager chatManager = XsgChatManager.getInstance();
		// List<ChatAdT> adTList = null;
		// if ("ch_gold".equals(templateId)) {
		// adTList = chatManager
		// .getAdContentMap(XsgChatManager.AdContentType.jbxz);
		// } else {// 公会礼包
		// adTList = chatManager
		// .getAdContentMap(XsgChatManager.AdContentType.FactionXz);
		// }
		// if (adTList != null && adTList.size() > 0) {
		// for (IItem iitem : rewardItems) {
		// if (iitem == null) {
		// continue;
		// }
		// AbsItemT itemT = iitem.getTemplate();
		// // 紫装及以上公告
		// if (itemT != null
		// && itemT.getItemType() == ItemType.EquipItemType
		// && itemT.getColor().value() >= QualityColor.Violet
		// .value()) {
		// ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList
		// .size()));
		// if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
		// chatManager.sendAnnouncementItem(
		// iitem,
		// rt.getChatControler().parseAdContentItem(
		// itemT, chatAdT.content));
		// }
		// }
		// }
		// }
		// }
	}

	@Override
	public ItemView[] useChestItem(String id, int count) throws NotEnoughMoneyException, NoteException {
		count = 1;
		NormalItem item = this.checkItemUse(id, count);
		NormalItemT template = item.getTemplate(NormalItemT.class);
		this.checkItemUseLevel(template);
		if (!template.useCode.equals("Chest")) { //$NON-NLS-1$
			throw new NoteException("use code error."); //$NON-NLS-1$
		}
		if (item.getExpirationTime() > System.currentTimeMillis()) {// 道具已过期
			throw new NoteException(Messages.getString("ItemControler.overdue"));
		}

		String keyTemplate = XsgItemManager.getInstance().getKeyForChestItem(template.id);
		if (!TextUtil.isBlank(keyTemplate)) {
			if (this.getItemCountInPackage(keyTemplate) < count) {
				throw new NoteException(Messages.getString("ItemControler.27")); //$NON-NLS-1$
			}
			this.changeItemByTemplateCode(keyTemplate, -count);
		}
		// 伪随机判断
		String tc = this.rt.getRewardControler().doMockTcForChestItem(item);

		TcResult tcr = new TcResult(tc);
		for (int i = 0; i < count; i++) {
			tcr.add(XsgRewardManager.getInstance().doTc(this.rt, tc));
		}

		ItemView[] items = XsgRewardManager.getInstance().generateItemView(tcr);
		List<IItem> rewardItems = this.rt.getRewardControler().acceptReward(items);
		this.changeItemById(id, -count);

		// 跑马灯公告, 只有金宝箱才跑马灯
		broadcastChest(rewardItems, template.id);

		this.tcItemEvent.onItemUse(item, count, items);
		return items;
	}

	private String[] getStarTC(EquipSmeltT smeltT, int star) {
		List<String> list = new ArrayList<String>();
		switch (star) {
		case 5:
			list.add(smeltT.star5TC);
		case 4:
			list.add(smeltT.star4TC);
		case 3:
			list.add(smeltT.star3TC);
		case 2:
			list.add(smeltT.star2TC);
		case 1:
			list.add(smeltT.star1TC);
			break;
		default:
			break;
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 混合map, 把mix混合到base
	 * 
	 * @param base
	 *            基础map
	 * @param mix
	 *            混合map
	 * */
	private Map<String, Integer> mixMap(Map<String, Integer> base, Map<String, Integer> mix) {
		if (base == null || base.size() <= 0) {
			return mix;
		}
		if (mix == null || mix.size() <= 0) {
			return base;
		}
		for (Map.Entry<String, Integer> entry : mix.entrySet()) {
			if (base.get(entry.getKey()) != null) {
				Integer value = base.get(entry.getKey()) + mix.get(entry.getKey());
				base.put(entry.getKey(), value);
			} else {
				base.put(entry.getKey(), entry.getValue());
			}
		}
		return base;
	}

	@Override
	public ItemView[] smeltEquip(String idArrayStr) throws NoteException, NotEnoughMoneyException {
		if (TextUtil.isBlank(idArrayStr)) {
			throw new NoteException(Messages.getString("ItemControler.9"));
		}
		SmeltParam[] idArray = TextUtil.GSON.fromJson(idArrayStr, SmeltParam[].class);

		int count = idArray.length;
		double levelUpMoneySum = 0;
		double sellMoneySum = 0;
		EquipItem[] equips = new EquipItem[count];
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		XsgRewardManager rewardManager = XsgRewardManager.getInstance();
		TcResult tcRes = new TcResult("");
		Map<String, Integer> gemMap = null;
		Map<String, Integer> extraItemMap = new HashMap<String, Integer>();
		for (int i = 0; i < count; i++) {
			EquipItem equip = this.getEquipItem(idArray[i].id);
			// 只有 未装备的 白绿蓝紫橙 可以熔炼
			if (equip == null || equip.getQuatityColor().value() > QualityColor.Orange.value()
					|| !TextUtil.isBlank(equip.getRefereneHero())) {
				throw new NoteException(Messages.getString("ItemControler.30"));
			}
			gemMap = mixMap(gemMap, removeAllGem(equip));
			equips[i] = equip;
			int type = equip.getQuatityColor().value();
			EquipSmeltT smeltT = XsgEquipManager.getInstance().findSmeltT(type);
			// 累加强化花费
			levelUpMoneySum += ((equip.getTotalLevelupConsume() * smeltT.backRate) / 100.000);
			// 累加出售价格
			sellMoneySum += ((equip.getTemplate().getSaleMoney() * smeltT.rate) / 100.000);
			// 累计数量
			Integer typeCount = countMap.get(type);
			if (typeCount == null) {
				typeCount = 0;
			}
			typeCount += 1;
			// 达到执行TC的标准
			if (typeCount >= smeltT.num && !TextUtil.isBlank(smeltT.reward)) {
				tcRes.add(rewardManager.doTc(this.rt, smeltT.reward));
				typeCount = 0; // 执行一次TC后重新开始计数
			}
			// 执行额外返还TC
			String[] starTcs = getStarTC(smeltT, equip.getStar());
			if (starTcs != null) {
				for (String starTc : starTcs) {
					tcRes.add(rewardManager.doTc(this.rt, starTc));
				}
			}
			// 返还升星石
			final String starToolTID = "star0"; // 小升星石模版ID，硬编码
			int exp = equip.getStarExp();
			NormalItemT starItemT = (NormalItemT) XsgItemManager.getInstance().findAbsItemT(starToolTID);
			int starNum = exp / Integer.parseInt(starItemT.useValue);
			if (starNum > 0) {
				tcRes.appendProperty(starToolTID, starNum);
			}
			// 橙色装备返还合成它的装备
			if (equip.getQuatityColor().value() == QualityColor.Orange.value()) {
				AbsItemT itemT = equip.getTemplate();
				// 包含额外材料
				if (itemT != null && itemT.needExtra() == 1) {
					Property[] extraItems = itemT.extraItemAndNum();
					if (extraItems != null) {
						for (Property p : extraItems) {
							if (!extraItemMap.containsKey(p.code)) {
								extraItemMap.put(p.code, p.value);
							} else {
								int oldValue = extraItemMap.get(p.code);
								extraItemMap.put(p.code, oldValue + p.value);
							}
						}
					}
				}
			}

			countMap.put(type, typeCount);
		}
		// 返还总数为强化花费加上出售价格
		int moneySum = (int) (levelUpMoneySum + sellMoneySum);
		// 返回金币数
		tcRes.appendProperty(Const.PropertyName.MONEY, moneySum);
		// 返还的宝石
		if (gemMap != null && gemMap.size() > 0) {
			for (Map.Entry<String, Integer> entry : gemMap.entrySet()) {
				tcRes.appendProperty(entry.getKey(), entry.getValue());
			}
		}
		ItemView[] items = rewardManager.generateItemView(tcRes);

		// 改变玩家属性，减掉玩家熔炼了的装备
		for (EquipItem equip : equips) {
			changeItemById(equip.getId(), -1);
		}
		// 加上玩家熔炼奖励的装备
		if (items != null && items.length > 0) {
			this.rt.getRewardControler().acceptReward(items);
			equipSmeltEvent.onEquipSmelt(equips, items, moneySum);
		}
		// 加上玩家额外材料
		List<ItemView> extraItemView = new ArrayList<ItemView>();
		if (extraItemMap.size() > 0) {
			for (Map.Entry<String, Integer> entry : extraItemMap.entrySet()) {
				IItem iitem = rt.getRewardControler().acceptReward(entry.getKey(), entry.getValue());
				ItemView iv = iitem.getView();
				iv.num = entry.getValue();
				extraItemView.add(iv);
			}
		}

		extraItemView.addAll(Arrays.asList(items));

		return extraItemView.toArray(new ItemView[0]);
	}

	@Override
	public void checkPackageFull() throws NoteException {
		if (this.isPackageFull()) {
			throw new NoteException(Messages.getString("ItemControler.29")); //$NON-NLS-1$
		}
	}

	@Override
	public List<EquipItem> findEquipByTemplateCode(String code) {
		List<EquipItem> list = new ArrayList<EquipItem>();
		for (IItem item : this.itemMap.values()) {
			if (item instanceof EquipItem && item.getTemplate().getId().equals(code)) {
				list.add((EquipItem) item);
			}
		}

		return list;
	}

	@Override
	public IItem addItemFromDb(RoleItem itemDb) {
		itemDb.setId(GlobalDataManager.getInstance().generatePrimaryKey());// 重新设置主键，避免主键冲突
		itemDb.setRole(db);
		this.db.getItems().put(itemDb.getId(), itemDb);
		IItem item = this.createItemFromDb(itemDb);

		this.onItemCountChanged(item, 1);
		this.rt.getNotifyControler().onItemChange(item);

		return item;
	}

	@Override
	public ItemView equipHole(String equipId, int position) throws NoteException {
		EquipItem item = getEquipItem(equipId);
		if (item == null) {
			throw new NoteException(Messages.getString("ItemControler.equipNotExist"));
		}
		EquipGemT gemT = XsgItemManager.getInstance().getEquipGemWithQuality(item.getQuatityColor().ordinal());
		if (gemT == null || position < 0) {
			throw new NoteException(Messages.getString("ItemControler.paramError"));
		}
		// 当前孔的个数
		int holeNum = item.getGemPairs().size();
		// 检查最大的开孔数量
		if (holeNum >= gemT.maxHoleNum) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.maxHoleLimit"), gemT.maxHoleNum));
		}
		// 检查开孔需要的等级
		int levelLimit = gemT.getLevelLimit(position + 1);
		if (rt.getLevel() < levelLimit) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.levelLimited"), levelLimit));
		}
		if (item.getGemPairWithPosition(position) != null) {
			throw new NoteException(Messages.getString("ItemControler.cannotHole"));
		}

		// 检查开孔所需资源
		String resTempId = gemT.holeToolId;
		int resNum = gemT.num;
		if (position == 4) {
			resTempId = gemT.lvl5Info.split(":")[0];
			resNum = NumberUtil.parseInt(gemT.lvl5Info.split(",")[0].split(":")[1]);
		} else if (position == 5) {
			resTempId = gemT.lvl6Info.split(":")[0];
			resNum = NumberUtil.parseInt(gemT.lvl6Info.split(",")[0].split(":")[1]);
		}

		if (getItemCountInPackage(resTempId) < resNum) {
			throw new NoteException(Messages.getString("ItemControler.toolNotEnough"));
		}
		// 减少道具数量
		changeItemByTemplateCode(resTempId, -resNum);
		// 开孔
		item.addGemWithPosition(position);
		this.rt.getNotifyControler().onItemChange(item);
		// 事件触发
		this.equipHole.onEquipHole(item);
		return item.getView();
	}

	/**
	 * 检查是否有同类型的宝石
	 * */
	private boolean canSetGem(EquipItem item, IItem gem) {
		List<IntString> gemList = item.getGemPairs();
		String gemType = ((GemT) gem.getTemplate()).property;
		for (IntString is : gemList) {
			if (!TextUtil.isBlank(is.strValue)) {
				GemT gemT = (GemT) XsgItemManager.getInstance().findAbsItemT(is.strValue);
				if (gemType.equals(gemT.property)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void setGem(String equipId, int position, String gemId) throws NoteException {
		EquipItem item = getEquipItem(equipId);
		IItem gemItem = getItem(gemId);
		if (gemItem == null || gemItem.getNum() <= 0) {
			throw new NoteException(Messages.getString("ItemControler.gemNotEnough"));
		}
		GemT gemT = (GemT) gemItem.getTemplate();
		if (gemT == null || item == null) {
			throw new NoteException(Messages.getString("ItemControler.equipNotExist"));
		}
		if (rt.getLevel() < gemT.useLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.levelLimited"), gemT.useLevel));
		}
		if (gemItem == null || gemItem.getNum() <= 0) {
			throw new NoteException(Messages.getString("ItemControler.gemNotEnough"));
		}
		IntString gemPair = item.getGemPairWithPosition(position);
		if (gemPair == null) {
			throw new NoteException(Messages.getString("ItemControler.notHoleHere"));
		}
		if (!TextUtil.isBlank(gemPair.strValue)) {
			throw new NoteException(Messages.getString("ItemControler.unequipFirst"));
		}
		if (!canSetGem(item, gemItem)) {
			throw new NoteException(Messages.getString("ItemControler.alreadyExist"));
		}
		// 减少宝石数量
		changeItemById(gemId, -1);
		// 镶嵌宝石
		String gemTemplateId = gemItem.getTemplate().getId();
		item.setGemWithPosition(position, gemTemplateId);
		// 装备变更通知
		this.rt.getNotifyControler().onItemChange(item);
		// 武将变更通知
		if (!TextUtil.isBlank(item.getRefereneHero())) {
			IHero refHero = rt.getHeroControler().getHero(item.getRefereneHero());
			this.rt.getNotifyControler().onHeroChanged(refHero);
			equipChange.onHeroEquipChange(refHero, item);
		}
		//
		// 事件
		gemSet.onSetGem(gemTemplateId, Integer.parseInt(gemTemplateId.substring(gemTemplateId.length() - 1)));
	}

	/**
	 * 移除所有宝石
	 * */
	public Map<String, Integer> removeAllGem(EquipItem item) throws NoteException {
		List<IntString> list = item.getGemPairs();
		boolean notify = false;
		Map<String, Integer> res = new HashMap<String, Integer>();
		if (list != null) {
			for (IntString is : list) {
				if (!TextUtil.isBlank(is.strValue)) {
					// 宝石放入返回集合中
					res.put(is.strValue, 1);
					// 移除宝石
					item.setGemWithPosition(is.intValue, null);
					notify = true;
				}
			}
		}
		if (notify) {
			// 装备变更通知
			this.rt.getNotifyControler().onItemChange(item);
			// 武将变更通知
			if (!TextUtil.isBlank(item.getRefereneHero())) {
				IHero refHero = rt.getHeroControler().getHero(item.getRefereneHero());
				this.rt.getNotifyControler().onHeroChanged(refHero);
				equipChange.onHeroEquipChange(refHero, item);
			}
		}
		return res;
	}

	@Override
	public void removeGem(String equipId, int position) throws NoteException {
		EquipItem item = getEquipItem(equipId);
		if (item == null) {
			throw new NoteException(Messages.getString("ItemControler.equipNotExist"));
		}
		IntString gemPair = item.getGemPairWithPosition(position);
		if (gemPair == null || TextUtil.isBlank(gemPair.strValue)) {
			throw new NoteException(Messages.getString("ItemControler.nogemHere"));
		}
		// 宝石放入背包
		changeItemByTemplateCode(gemPair.strValue, 1);
		// 移除宝石
		item.setGemWithPosition(position, null);
		// 装备变更通知
		this.rt.getNotifyControler().onItemChange(item);
		// 武将变更通知
		if (!TextUtil.isBlank(item.getRefereneHero())) {
			IHero refHero = rt.getHeroControler().getHero(item.getRefereneHero());
			this.rt.getNotifyControler().onHeroChanged(refHero);
			equipChange.onHeroEquipChange(refHero, item);
		}
	}

	@Override
	public List<IItem> getItemList() {
		return new ArrayList<IItem>(itemMap.values());
	}

	@Override
	public void destroyItem(String code) {
		int change = -getItemCountInPackage(code);
		if (change == 0) {
			return;
		}
		changeItemByTemplateCode(code, change);
	}

	@Override
	public void isItemNumEnough(String code, int itemNum) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		if (code.equals(Const.PropertyName.MONEY)) {// 金币
			if (itemNum > this.db.getVip().getJinbi()) {
				throw new NotEnoughMoneyException();
			}
		} else if (code.equals(Const.PropertyName.RMBY)) {// 元宝
			if (itemNum > this.rt.getTotalYuanbao()) {
				throw new NotEnoughYuanBaoException();
			}
		} else if (code.equals(Const.PropertyName.ORDER)) {// 竞技币
			if (itemNum > this.rt.getArenaRankControler().getRoleArenaRank().getChallengeMoney()) {
				throw new NoteException(Messages.getString("ArenaRankControler.48"));
			}
		} else if (code.equals(Const.PropertyName.AUCTION_COIN)) {// 拍卖币
			if (itemNum > this.rt.getAuctionHouseController().getAuctionMoney()) {
				throw new NoteException(Messages.getString("AuctionHouseController.29"));
			}
		} else if (code.equals(Const.PropertyName.SKILL)) {// 技能点
			if (itemNum > this.db.getHeroSkillPoint()) {
				throw new NoteException(Messages.getString("HeroControler.25"));
			}
		} else if (code.equals(Const.PropertyName.NanHuaLing)) {// 南华幻境：南华令
			if (itemNum > this.rt.getDreamlandController().getNanHuaLing()) {
				throw new NoteException(Messages.getString("DreamlandController.nhLing.notEnough"));
			}
		} else {
			if (itemNum > this.rt.getItemControler().getItemCountInPackage(code)) {
				throw new NoteException(Messages.getString("HeroAdmireControler.7"));
			}
		}
	}

	@Override
	public void drawCompositeChestItem(int itemIndex, String itemId) throws NoteException {
		NormalItem item = this.getItem(itemId, NormalItem.class);
		if (item == null) {
			throw new NoteException(Messages.getString("ItemControler.21")); //$NON-NLS-1$
		}
		if (item.getExpirationTime() > System.currentTimeMillis()) {// 道具已过期
			throw new NoteException(Messages.getString("ItemControler.overdue"));
		}
		NormalItemT template = item.getTemplate(NormalItemT.class);
		if (template.itemTypeID != 9) {
			throw new NoteException(Messages.getString("ItemControler.noCompositeChest")); //$NON-NLS-1$
		}
		this.checkItemUseLevel(template);
		CompositeChestT ct = XsgItemManager.getInstance().getCompositeChestMap().get(item.getTemplate().getId());
		if (ct.items == null || ct.items.length < 0 || ct.items.length <= itemIndex || ct.items[itemIndex] == null) {
			throw new NoteException(Messages.getString("ItemControler.noDrawCompositeChest"));
		}
		PropertyT pt = ct.items[itemIndex];
		this.rt.getRewardControler().acceptReward(pt.code, pt.value);
		this.changeItemById(itemId, -1);
		// 触发事件
		this.compositeChestItemDraw.doDrawCompositeChestItem(itemIndex, itemId, template.getId(), pt.code, pt.value);
	}

	@Override
	public void selectAdvancedType(int type) throws NoteException {
		if (!checkOpen()) {
			throw new NoteException("Advanced not open");
		}
		if (type != 1 && type != 2 && type != 3) {
			throw new NoteException("Advanced type error");
		}
		IFormation formation = rt.getFormationControler().getDefaultFormation();
		formation.setAdvancedType(type);
		this.rt.getNotifyControler().onFormationChange(formation);

		buffAdvanceEvent.onBuffAdvanceChange(0, formation.getAdvancedType(), "");
	}

	/**
	 * 检测阵法进阶是否开启
	 * 
	 * @return
	 */
	private boolean checkOpen() {
		IFormation formation = rt.getFormationControler().getDefaultFormation();
		if (formation.getAdvancedType() != 0) {
			return true;
		}
		for (IItem i : rt.getItemControler().getItemList()) {
			if (i.getTemplate().getItemType() == ItemType.FormationBuffItemType
					&& i.getTemplate().getColor().value() >= QualityColor.Violet.value()) {
				FormationBuffItem buff = rt.getItemControler().getItem(i.getId(), FormationBuffItem.class);
				if (buff.getLevel() >= Const.MaxFormationBuffLevel) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void advancedFormationBuff(String ids) throws NoteException {
		if (!checkOpen()) {
			throw new NoteException("Advanced not open");
		}
		IFormation formation = rt.getFormationControler().getDefaultFormation();
		IntIntPair ip = null;
		List<IntIntPair> ipList = new ArrayList<IntIntPair>();
		for (IntIntPair i : TextUtil.GSON.fromJson(formation.getAdvanceds(), IntIntPair[].class)) {
			if (i.first == formation.getAdvancedType()) {
				ip = i;
			}
			ipList.add(i);
		}
		int level = ip == null ? 1 : ip.second + 1;
		FormationBuffAdvancedT advancedT = XsgItemManager.getInstance().getByLevelAndType(level,
				formation.getAdvancedType());
		if (advancedT == null) {
			throw new NoteException(Messages.getString("ItemController.advancedFull"));
		}
		List<FormationBuffItem> deleteList = new ArrayList<FormationBuffItem>();
		for (String delete : ids.split(",")) {
			FormationBuffItem delBuff = rt.getItemControler().getItem(delete, FormationBuffItem.class);
			if (delBuff.getLevel() < Const.MaxFormationBuffLevel
					|| delBuff.getColor().value() < QualityColor.Violet.value()) {
				throw new NoteException(Messages.getString("ItemController.buffLevelError"));
			}
			deleteList.add(delBuff);
		}
		if (deleteList.size() < advancedT.useBuffNum) {
			throw new NoteException(Messages.getString("ItemController.buffNumError"));
		}
		// 检查物品是否足够
		for (String idNum : advancedT.useItems.split(",")) {
			String code = idNum.split(":")[0];
			int num = NumberUtil.parseInt(idNum.split(":")[1]);
			if (getItemCountInPackage(code) < num) {
				throw new NoteException(Messages.getString("ItemController.advancedItemError"));
			}
		}

		for (FormationBuffItem delBuff : deleteList) {
			rt.getItemControler().changeItemById(delBuff.getId(), -1);
		}
		try {
			rt.winJinbi(-advancedT.useCoin);
		} catch (NotEnoughMoneyException e) {
			throw new NoteException(Messages.getString("ItemI.2"));
		}
		for (String idNum : advancedT.useItems.split(",")) {
			String code = idNum.split(":")[0];
			int num = NumberUtil.parseInt(idNum.split(":")[1]);
			changeItemByTemplateCode(code, -num);
		}
		if (ip == null) {
			ip = new IntIntPair(formation.getAdvancedType(), 1);
			ipList.add(ip);
		} else {
			ip.second = ip.second + 1;
		}
		formation.setAdvanceds(TextUtil.GSON.toJson(ipList.toArray(new IntIntPair[0])));

		this.rt.getNotifyControler().onFormationChange(formation);
		buffAdvanceEvent.onBuffAdvanceChange(1, formation.getAdvancedType(), ids);
	}

	@Override
	public String getItemColor(int quality) {
		ItemColorT colorT = XsgItemManager.getInstance().getColorT().get(quality);
		if (colorT == null) {
			return "FFFFFF";// 给定一个默认色值
		}
		return colorT.colorValue;
	}

	/**
	 * 使用主公头像道具
	 */
	private void useHeadItem(NormalItem item, int count, NormalItemT template) throws NoteException {
		if (Arrays.asList(this.rt.getExtHeadImage()).contains(template.getId())) {
			throw new NoteException(Messages.getString("ItemControler.hasGotHead"));
		}

		this.rt.addExtHeadImage(template.getId());

		this.changeItemById(item.getId(), -count);
		this.itemUseEvent.onItemUse(item, count, count);
		// 头像属性变更通知
		this.rt.getNotifyControler().onStrPropertyChange(Const.PropertyName.HEAD_IMAGE,
				LuaSerializer.serialize(this.rt.getExtHeadImage()));
	}

	/**
	 * 使用主公头像边框道具
	 */
	private void useHeadBorderItem(NormalItem item, int count, NormalItemT template) throws NoteException {
		if (Arrays.asList(this.rt.getExtHeadBorder()).contains(template.getId())) {
			throw new NoteException(Messages.getString("ItemControler.hasGotBorder"));
		}

		this.rt.addExtHeadBorder(template.getId());

		this.changeItemById(item.getId(), -count);
		this.itemUseEvent.onItemUse(item, count, count);

		// 头像框属性变更通知
		this.rt.getNotifyControler().onStrPropertyChange(Const.PropertyName.HEAD_BORDER,
				LuaSerializer.serialize(this.rt.getExtHeadBorder()));
	}
}

class StarUpResult {
	public int uplevel;
	public int consumeMoney;
	public int remainExp;
}
