/**
 * 碎片掠夺
 */
package com.morefun.XSanGo.itemChip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.ChipItemCompound;
import com.XSanGo.Protocol.CompoundResult;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.HeroConsumeView;
import com.XSanGo.Protocol.HeroInheritView;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.HeroResetResult;
import com.XSanGo.Protocol.HeroResetView;
import com.XSanGo.Protocol.HeroState;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.chat.XsgChatManager.AdContentType;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.protocol.IHeroInherit;
import com.morefun.XSanGo.event.protocol.IHeroReset;
import com.morefun.XSanGo.event.protocol.IItemChipCompound;
import com.morefun.XSanGo.event.protocol.IItemChipStrut;
import com.morefun.XSanGo.event.protocol.IItemCountChange;
import com.morefun.XSanGo.event.protocol.IItemStoneMix;
import com.morefun.XSanGo.hero.HeroSkillLevelupT;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.IHeroControler;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.IItemControler;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.IRewardControler;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author 吕明涛
 * 
 */
@RedPoint
class ItemChipControler implements IItemChipControler, IItemCountChange {

	private static final Log log = LogFactory.getLog(ItemChipControler.class);

	private IRole roleRt;
	private Role roleDb;
	private IItemStoneMix stoneMixEvent;
	private IItemChipStrut itemChipStrutEvent;
	private IItemChipCompound eventCompound; // 合成事件
	private IHeroReset heroReset; // 武将下野事件
	private IHeroInherit heroInherit; // 武将传承事件

	private Map<String, IRole> robRoleMap = new HashMap<String, IRole>(4); // 掠夺列表

	private final String FORMATIONID = "formationId"; // 默认 保存 防守队伍ID //$NON-NLS-1$

	public ItemChipControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		// 注册 碎片 响应事件
		roleRt.getEventControler().registerHandler(IItemCountChange.class, this);
		this.stoneMixEvent = this.roleRt.getEventControler().registerEvent(IItemStoneMix.class);

		this.itemChipStrutEvent = this.roleRt.getEventControler().registerEvent(IItemChipStrut.class);
		this.eventCompound = this.roleRt.getEventControler().registerEvent(IItemChipCompound.class);
		this.heroReset = this.roleRt.getEventControler().registerEvent(IHeroReset.class);
		this.heroInherit = this.roleRt.getEventControler().registerEvent(IHeroInherit.class);
	}

	/**
	 * 查询某个模版ID的道具或装备有多少件
	 * 
	 * @param itemTemplateId
	 *            模版ID
	 * @param inUse
	 *            正在使用的算不算数, true算数,false不算数
	 * */
	private int getItemCount(String itemTemplateId, boolean inUse) {
		// 碎片
		if (itemTemplateId.contains("-")) {
			return roleRt.getItemControler().getItemCountInPackage(itemTemplateId);
		} else { // 装备
			List<EquipItem> items = roleRt.getItemControler().findEquipByTemplateCode(itemTemplateId);
			int count = 0;
			if (items != null) {
				for (EquipItem item : items) {
					// 正在使用中的不算数
					if (!inUse && !TextUtil.isBlank(item.getRefereneHero())) {
						continue;
					}
					count++;
				}
			}
			return count;
		}
	}

	/**
	 * 减少某个道具的数量
	 * 
	 * @param templateId
	 *            模版ID
	 * @param count
	 *            数量
	 * @param inUse
	 *            准备在角色身上的能不能减, true可以,false不可以
	 * @return 剩余的数量
	 * */
	private ReduceItemCountResult reduceItemCount(String templateId, int count, boolean inUse) throws NoteException {
		ReduceItemCountResult ret = new ReduceItemCountResult();

		List<EquipItem> items = roleRt.getItemControler().findEquipByTemplateCode(templateId);

		if (items != null && items.size() > 0) { // 装备
			int sum = getItemCount(templateId, inUse);
			int reduceCount = count;
			long totalExp = 0L;
			if (items != null) {
				// 根据装备战力排序, 优先消耗低战力的
				Collections.sort(items, new Comparator<EquipItem>() {
					@Override
					public int compare(EquipItem o1, EquipItem o2) {
						return o1.caculateBattlePower() - o2.caculateBattlePower();
					}
				});
				for (EquipItem item : items) {
					if (reduceCount <= 0) {
						break;
					}
					if (!inUse && !TextUtil.isBlank(item.getRefereneHero())) {
						continue;
					}
					// 移除装备上镶嵌的宝石
					Map<String, Integer> gemMap = this.roleRt.getItemControler().removeAllGem(item);
					if (gemMap != null && gemMap.size() > 0) {
						for (Map.Entry<String, Integer> entry : gemMap.entrySet()) {
							// 返还宝石
							IItem ritem = roleRt.getRewardControler().acceptReward(entry.getKey(), entry.getValue());
							roleRt.getNotifyControler().onItemChange(ritem);
						}
					}
					totalExp += item.getAllStarExp();
					roleRt.getItemControler().changeItemById(item.getId(), -1);
					reduceCount--;
				}
			}
			if (reduceCount > 0) {
				throw new NoteException(Messages.getString("ItemChipControler.6"));
			}
			ret.exp = totalExp;
			ret.num = sum - count;
			return ret;
		} else { // 碎片 或者 普通道具
			List<IItem> result = roleRt.getItemControler().changeItemByTemplateCode(templateId, -count);
			int num = 0;
			if (result != null && result.size() > 0) {
				num = result.get(0).getNum();
			}
			ret.num = num;
			return ret;
		}
	}

	@Override
	public CompoundResult compoundGem(String itemId, int num) throws NoteException {
		CompoundGemT gemT = XsgItemChipManager.getInstance().getCompoundGemt(itemId);
		if (gemT == null || TextUtil.isBlank(gemT.needItem) || num <= 0) {
			throw new NoteException(Messages.getString("ItemChipControler.cannoteCom"));
		}
		// 检查所需材料的数量
		int totalResNum = this.roleRt.getItemControler().getItemCountInPackage(gemT.needItem);
		int needResNum = gemT.needNum * num;
		if (totalResNum < needResNum) {
			throw new NoteException(Messages.getString("ItemChipControler.chipNoteEnough"));
		}
		// 减少合成材料
		this.roleRt.getItemControler().changeItemByTemplateCode(gemT.needItem, -needResNum);
		// 增加合成物品
		IItem result = this.roleRt.getRewardControler().acceptReward(itemId, num);
		// 公告
		StringBuilder sb = new StringBuilder(result.getTemplate().getId());
		// 通过模版ID最后的数字来确定等级 很蛋疼
		int gemLevel = Integer.parseInt(sb.reverse().substring(0, 1));
		if (gemLevel >= XsgGameParamManager.getInstance().getGemNoticeLevel()) {
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
					XsgChatManager.AdContentType.GemCompound);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) {
					XsgChatManager.getInstance().sendAnnouncement(
							this.roleRt.getChatControler().parseAdContentItem(result.getTemplate(), chatAdT.content));
				}
			}
		}
		stoneMixEvent.onStoneMixAddChange(gemLevel, num);
		return new CompoundResult(result.getId(), new ChipItemCompound[] { new ChipItemCompound(gemT.needItem, roleRt
				.getItemControler().getItemCountInPackage(gemT.needItem)) });
	}

	/**
	 * 碎片合成
	 * 
	 * @throws Exception
	 */
	@Override
	public CompoundResult compoundChip(IRole roleRt, String itemId, String extraId) throws NoteException {
		// 碎片数量是否满足
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemId);
		int PieceCount = itemT.getPieceCount();
		Map<String, Integer> chipMap = new HashMap<String, Integer>(PieceCount);
		Map<String, IItem> itemMap = new HashMap<String, IItem>();
		String itemChipId = XsgItemManager.getInstance().getPieceTemplateId(itemId);
		int chipCount = this.roleRt.getItemControler().getItemCountInPackage(itemChipId);

		// 检查拥有的数量是否大于消耗数量
		if (chipCount < PieceCount) {
			throw new NoteException(Messages.getString("ItemChipControler.6")); //$NON-NLS-1$
		}
		// 增加到消耗的map中
		chipMap.put(itemChipId, PieceCount);

		// 需要额外的材料
		if (itemT.needExtra() == 1) {
			Property[] extraItems = itemT.extraItemAndNum();
			// extraId 不为空表示客户端选择了一个要消耗的装备(约定为紫装,替换配置中的第一项，即上面的extraItem1)
			if (!TextUtil.isBlank(extraId)) {
				IItem extraItem = roleRt.getItemControler().getItem(extraId);
				if (extraItem == null || !(extraItem instanceof EquipItem)) {
					throw new NoteException(Messages.getString("ItemChipControler.6"));
				}
				itemMap.put(extraId, extraItem);
			} else {
				if (extraItems != null && extraItems.length > 0) {
					int extraItemCount1 = extraItems[0].value;
					// 检查额外材料1的数量
					if (extraItemCount1 > getItemCount(extraItems[0].code, false)) {
						throw new NoteException(Messages.getString("ItemChipControler.6")); //$NON-NLS-1$
					}
					// 增加到消耗的map中
					chipMap.put(extraItems[0].code, extraItemCount1);
				}
			}
			if (extraItems != null && extraItems.length > 1) {
				int extraItemCount2 = extraItems[1].value;
				// 检查额外材料2的数量
				if (extraItemCount2 > getItemCount(extraItems[1].code, false)) {
					throw new NoteException(Messages.getString("ItemChipControler.6")); //$NON-NLS-1$
				}
				// 增加到消耗的map中
				chipMap.put(extraItems[1].code, extraItemCount2);
			}
		}

		// 碎片合成后的数据
		List<ChipItemCompound> chipItemArr = new ArrayList<ChipItemCompound>();

		// 合成 ，成功后 碎片数量减少和 合成物品增加
		long totalExp = 0L;
		for (String chipKey : chipMap.keySet()) {
			ReduceItemCountResult ret = reduceItemCount(chipKey, chipMap.get(chipKey), false);
			int num = ret.num;

			chipItemArr.add(new ChipItemCompound(chipKey, num));
			totalExp += ret.exp;
		}
		// 减少选择的整装
		for (Map.Entry<String, IItem> idEntry : itemMap.entrySet()) {
			EquipItem eitem = (EquipItem) idEntry.getValue();
			int sum = eitem.getNum();
			String tempId = eitem.getTemplate().getId();
			// 移除所镶嵌的宝石
			Map<String, Integer> gemMap = this.roleRt.getItemControler().removeAllGem(eitem);
			if (gemMap != null && gemMap.size() > 0) {
				for (Map.Entry<String, Integer> entry : gemMap.entrySet()) {
					// 返还宝石
					IItem ritem = roleRt.getRewardControler().acceptReward(entry.getKey(), entry.getValue());
					roleRt.getNotifyControler().onItemChange(ritem);
				}
			}
			totalExp += eitem.getAllStarExp();
			roleRt.getItemControler().changeItemById(idEntry.getKey(), -1);
			chipItemArr.add(new ChipItemCompound(tempId, sum - 1));
		}

		// 合成道具的增加
		List<IItem> compoundItemList = this.roleRt.getItemControler().changeItemByTemplateCode(itemId, 1);
		// 返还升星石
		if (totalExp > 0) {
			NormalItemT returnItemT = (NormalItemT) XsgItemManager.getInstance().findAbsItemT(
					XsgGameParamManager.getInstance().getStarUpReturnTempID());
			int count = (int) (totalExp / Integer.parseInt(returnItemT.useValue));
			if (count > 0) {
				roleRt.getRewardControler().acceptReward(returnItemT.id, count);
			}
		}

		// 添加合成物品
		this.eventCompound.onCompound(itemId);

		IItem resultItem = compoundItemList.get(0);
		CompoundResult compoundResult = new CompoundResult();
		compoundResult.itemId = resultItem.getId();
		compoundResult.chipList = chipItemArr.toArray(new ChipItemCompound[0]);

		if (resultItem != null && resultItem.getTemplate().getColor().value() >= QualityColor.Orange.value()) {

			// 橙装增加额外奖励, TC硬编码...
			// XsgRewardManager rewardManager = XsgRewardManager.getInstance();
			// TcResult extraReward = rewardManager.doTc(roleRt,
			// "TC-getOrangeStar");
			// roleRt.getRewardControler().acceptReward(rewardManager.generateItemView(extraReward));
			// 橙装增加额外奖励, TC硬编码...

			// 橙装公告
			ChatAdT ad = XsgChatManager.getInstance().getAdContentMap(AdContentType.CompoundOrange).get(0);
			XsgChatManager.getInstance().sendAnnouncementItem(resultItem,
					this.roleRt.getChatControler().parseAdContentItem(resultItem.getTemplate(), ad.content));
		}

		return compoundResult;
	}

	/**
	 * 得到物品碎片 合成次数增加
	 */
	@Override
	public void onItemCountChange(IItem item, int change) {
		if (item == null || change < 1) {// 大于0才处理
			return;
		}

		List<String> compoundList = XsgItemChipManager.getInstance().getCompoundByMaterial(item.getTemplate().getId());
		if (compoundList == null) { // 是合成原材料才处理
			return;
		}

		for (String compound : compoundList) {
			if (this.checkCompound(compound, XsgItemChipManager.getInstance().getAllCompoundConfig().get(compound))) {
				this.roleRt.getNotifyControler().onMajorUIRedPointChange(
						new MajorUIRedPointNote(MajorMenu.ItemChipMenu, false));
				return;
			}
		}
	}

	/**
	 * 炫耀合成后的装备
	 * 
	 * @throws NoteException
	 */
	@Override
	public void strutItem(String itemId) throws NoteException {

		IItem strutItem = this.roleRt.getItemControler().getItem(itemId);
		EquipItem strutEquipItem = this.roleRt.getItemControler().getEquipItem(itemId);

		if (strutItem == null && strutEquipItem == null) {
			throw new NoteException(Messages.getString("ItemChipControler.16")); //$NON-NLS-1$
		}

		// 缓存 合成 要炫耀的物品
		XsgChatManager.getInstance().setShowItemMap(strutItem);
		XsgChatManager.getInstance().setShowItemMap(strutEquipItem);

		// 随机取得炫耀字符
		List<String> StrutStrList = XsgItemChipManager.getInstance().getStrutStrList();
		String strutStr = StrutStrList.get(NumberUtil.random(StrutStrList.size()));

		// 显示 聊天窗口消息
		this.roleRt.getChatControler().strutItemMessage(strutStr, strutItem.getTemplate().getItemType().value(),
				itemId, strutItem.getTemplate().getId());

		// 添加合成物品 炫耀事件
		this.itemChipStrutEvent.onStrut(itemId);
	}

	/**
	 * 客户端红点显示
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean note = this.checkCompound();
		return note ? new MajorUIRedPointNote(MajorMenu.ItemChipMenu, false) : null;
	}

	// 是否可以合成碎片
	private boolean checkCompound() {
		Map<String, List<Property>> configMap = XsgItemChipManager.getInstance().getAllCompoundConfig();
		for (String key : configMap.keySet()) {
			if (this.checkCompound(key, configMap.get(key))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检查是否可以合成指定ID物品
	 * 
	 * @param compoundId
	 * @param materialList
	 * @return
	 */
	private boolean checkCompound(String compoundId, List<Property> materialList) {
		if (TextUtil.isBlank(compoundId) || materialList == null || materialList.size() == 0) {
			return false;
		}

		for (Property p : materialList) {
			if (this.roleRt.getItemControler().getItemCountInPackage(p.code) < p.value) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 角色下线时候，调用 清除数据
	 */
	@Override
	public void clearData() {
		this.robRoleMap.clear();
	}

	@Override
	public HeroResetResult heroReset(String heroId, int isPay) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		final int NOT_PAY = 0; // 删除武将
		final int PAY = 1; // 保留武将(1级),和星级
		IHeroControler heroControler = roleRt.getHeroControler();
		IHero hero = heroControler.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("ItemChipControler.19")); //$NON-NLS-1$
		}
		// 检查, 已上阵、援军、随从无法下野
		if (hero.getState() != HeroState.Default) {
			throw new NoteException(Messages.getString("ItemChipControler.20")); //$NON-NLS-1$
		}
		// 有随从的武将无法下野
		if (hero.hasAttendant()) {
			throw new NoteException(Messages.getString("ItemChipControler.21")); //$NON-NLS-1$
		}
		// 突破过的武将无法下野
		if (hero.getBreakLevel() > 0) {
			throw new NoteException(Messages.getString("ItemChipControler.breakHeroCanNotReset"));
		}
		// 寻宝的武将无法下野
		if (roleRt.getTreasureControler().isInTreasureGroup(hero.getId())) {
			throw new NoteException(Messages.getString("ItemChipControler.treasureHeroCanNotReset"));
		}
		// // 觉醒武将只能是用元宝下野
		// if ((hero.isAwaken() ||
		// hero.getId().equals(roleDb.getAwakenHeroId())) && isPay != PAY) {
		// throw new
		// NoteException(Messages.getString("ItemChipControler.awakeNotFreeReset"));
		// }
		int minLevel = XsgGameParamManager.getInstance().getHeroResetMinLevel();
		if (hero.getLevel() < minLevel) {
			throw new NoteException(
					Messages.getString("ItemChipControler.22") + minLevel + Messages.getString("ItemChipControler.23")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		double scale = 0.75;// 返还比例
		// 保留1级武将和星级
		if (isPay == PAY) {
			int yuanbaoCost = XsgGameParamManager.getInstance().getHeroResetCost();
			// 检查元宝数量是否足够
			if (yuanbaoCost > roleRt.getTotalYuanbao()) {
				throw new NotEnoughYuanBaoException();
			}
			roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, yuanbaoCost));
			scale = 1;
		}

		// 返还消耗
		HeroConsumeView consume = hero.getHeroConsumeView();
		IRewardControler rewardControler = roleRt.getRewardControler();
		if (consume.consumes != null) {
			for (Property p : consume.consumes) {
				if (p.value > 0) {
					int value = (int) (p.value * scale);
					if (value > 0) { // 乘以返还比例之后value可能会变成0.
						// 将经验转换为黑龙王將魂
						if (Const.PropertyName.EXP.equals(p.code)) {
							value = XsgHeroManager.getInstance().convertBlackDragonSoul(value);
							p.code = Const.PropertyName.BlackDragonSoul;
							p.value = value;
						}
						rewardControler.acceptReward(p.code, value);
					}
				}
			}
		}

		// 下野之前的view
		HeroView orginView = hero.getHeroView();

		// switch (isPay) {
		// case NOT_PAY : // 删除
		// // 卸下武将身上的装备
		// for (EquipPosition pos : EquipPosition.values()) {
		// EquipItem equip = hero.getEquipByPos(pos);
		// if (equip != null) {
		// // 此处由于客户端之前没有设计删除武将的功能, 为了避免更新客户端, 采用不发送通知的策略
		// heroControler.removeHeroEquipNotNotify(hero.getId(),
		// equip);
		// }
		// }
		// heroControler.removeHero(heroId);
		// break;
		// case PAY : // 保留
		hero.resetHero(1, -1, 0, true, -1, false, true);
		// break;
		// default :
		// break;
		// }

		heroReset.onHeroReset(heroId, isPay, orginView, consume);
		return new HeroResetResult(isPay);
	}

	private void convertBlackDragonSoul(HeroConsumeView consumeView) {
		for (Property prop : consumeView.consumes) {
			if (Const.PropertyName.EXP.equals(prop.code)) {
				prop.code = Const.PropertyName.BlackDragonSoul;
				prop.value = XsgHeroManager.getInstance().convertBlackDragonSoul(prop.value);
			}
		}
	}

	@Override
	public List<HeroResetView> requestHeroReset() throws NoteException {
		List<IHero> heros = roleRt.getHeroControler().getAllHero();
		List<HeroResetView> viewList = new ArrayList<HeroResetView>();
		for (IHero hero : heros) {
			HeroConsumeView consumeView = hero.getHeroConsumeView();
			convertBlackDragonSoul(consumeView);
			viewList.add(new HeroResetView(hero.getTemplateId() + "", consumeView));
		}
		return viewList;
	}

	@Override
	public HeroInheritView requestHeroInherit() throws NoteException {
		XsgGameParamManager gameParam = XsgGameParamManager.getInstance();
		return new HeroInheritView(gameParam.getHeroInheritConsumeId(), gameParam.getHeroInheritConsumeNum(),
				gameParam.getHeroInheritLevel());
	}

	@Override
	public void heroInherit(String inheritHeroId, String baseHeroId) throws NoteException {
		IHeroControler heroControler = roleRt.getHeroControler();
		IHero baseHero = heroControler.getHero(baseHeroId);
		IHero inheritHero = heroControler.getHero(inheritHeroId);
		if (baseHero == null || inheritHero == null) {
			throw new NoteException(Messages.getString("ItemControler.errorParam"));
		}
		// 传承和被传承的武将必须为空闲状态（上阵武将、援军、随从和修行等身上有状态的武将都不可传承或被传承）
		if (baseHero.getState() != HeroState.Default || inheritHero.getState() != HeroState.Default) {
			throw new NoteException(Messages.getString("ItemControler.mustBeFree"));
		}
		// 有随从的武将无法传承
		if (baseHero.hasAttendant() || inheritHero.hasAttendant()) {
			throw new NoteException(Messages.getString("ItemControler.mustNotAddAddtion"));
		}
		// 只有突破后的武将才可进行传承
		if (baseHero.getBreakLevel() <= 0) {
			throw new NoteException(Messages.getString("ItemControler.mustBreak"));
		}
		// 寻宝的武将不能传承
		if (roleRt.getTreasureControler().isInTreasureGroup(baseHero.getId())) {
			throw new NoteException(Messages.getString("ItemChipControler.treasureHeroCanNotInherit"));
		}
		// 被传承方必须为未突破且30级以下武将
		XsgGameParamManager gameParam = XsgGameParamManager.getInstance();
		int levelLimit = gameParam.getHeroInheritLevel();
		if (inheritHero.getBreakLevel() > 0 || inheritHero.getLevel() > levelLimit) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.levelLimit"), levelLimit));
		}
		// 检查突破令是否足够
		String toolTemplateId = gameParam.getHeroInheritConsumeId();
		int toolConsumeNum = gameParam.getHeroInheritConsumeNum();
		IItemControler itemControler = roleRt.getItemControler();
		if (itemControler.getItemCountInPackage(toolTemplateId) < toolConsumeNum) {
			throw new NoteException(Messages.getString("ItemControler.notEnough"));
		}

		// 减少突破令
		itemControler.changeItemByTemplateCode(toolTemplateId, -toolConsumeNum);

		// 传承逻辑,被传承的武将会继承传承武将的等级，进阶，突破等级，技能等级和修炼等级
		// 等级
		inheritHero.setLevel(baseHero.getLevel());
		// 经验
		inheritHero.setExp(baseHero.getExp());
		// 进阶
		inheritHero.setColor(baseHero.getQualityLevel());
		// 突破等级
		inheritHero.setBreakLevel(baseHero.getBreakLevel());
		// 技能等级
		IntIntPair[] baseSkills = baseHero.getSkills();
		if (baseSkills != null) {
			HeroT baseHeroT = baseHero.getTemplate();
			HeroT inheritHeroT = inheritHero.getTemplate();
			for (IntIntPair pair : baseSkills) {
				// 传承的技能对应的位置索引
				int index = baseHeroT.getSkillIndex(pair.first);
				if (index >= 0 && index < 4) {
					int inheritSkillId = inheritHeroT.getSkillIdByIndex(index);
					// 更新技能等级
					inheritHero.setHeroSkill(inheritSkillId, pair.second);
				} else {// 觉醒技能
					// 返还技能点
					heroControler.addHeroSkillPoint(pair.second);
					// 返还技能升级金币
					int sumGold = 0;
					for (int i = pair.second; i > 0; i--) {
						HeroSkillLevelupT lt = XsgHeroManager.getInstance().getHeroSkillLevelupT(i);
						sumGold += lt.conditions[index].jinbi;
					}
					try {
						roleRt.winJinbi(sumGold);
					} catch (NotEnoughMoneyException e) {
						LogManager.error(e);
					}
				}
			}
		}
		// 修炼等级
		List<HeroPracticeView> basePracticeList = baseHero.getHeroPracticeView();
		// 清空被传承者的修炼状态
		inheritHero.removeAllHeroPractice();
		if (basePracticeList != null) {
			for (HeroPracticeView view : basePracticeList) {
				// 设置修炼属性
				inheritHero.addHeroPractice(new HeroPractice(GlobalDataManager.getInstance().generatePrimaryKey(),
						null, view.id, view.propName, view.level, view.exp, view.addValue, view.nextUpExp, inheritHero
								.getPracticeSize(), view.color));
			}
		}
		// 计算需要返还的将魂
		XsgHeroManager heroManager = XsgHeroManager.getInstance();
		int soulCount = heroManager.caculateTotalStarUpSoulConsume(baseHero.getColor().ordinal(), baseHero.getStar());

		// 重置传承武将的状态
		baseHero.resetHero(1, 1, 0, true, 0, true, false);

		// 发邮件返还将魂
		if (soulCount > 0) {
			Map<String, String> replaceMap = new HashMap<String, String>();
			replaceMap.put("$o", baseHero.getName());
			Map<String, Integer> rewardMap = new HashMap<String, Integer>();
			rewardMap.put(heroManager.getSoulTemplateId(baseHero.getTemplateId()), soulCount);
			// 发送邮件
			XsgMailManager.getInstance().sendTemplate(roleRt.getRoleId(), MailTemplate.HeroInherit, rewardMap,
					replaceMap);
		}

		// 武将变更通知
		roleRt.getNotifyControler().onHeroChanged(inheritHero);

		// 事件
		heroInherit.onHeroInherit(baseHero.getTemplateId(), inheritHero.getTemplateId());
	}

	static class ReduceItemCountResult {
		public int num;
		public long exp;
	}
}
