/**
 * 
 */
package com.morefun.XSanGo.heroAdmire;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AdmireView;
import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleHeroAdmire;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.protocol.IHeroAdmireChoose;
import com.morefun.XSanGo.event.protocol.IHeroAdmirePresent;
import com.morefun.XSanGo.event.protocol.IHeroAdmireSummon;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 名将仰慕 功能
 * 
 * @author 吕明涛
 * 
 */
@RedPoint(isTimer = true)
class HeroAdmireControler implements IHeroAdmireControler {

	// private static final Log log =
	// LogFactory.getLog(HeroAdmireControler.class);

	/** 名将选择 */
	private IHeroAdmireChoose eventHeroAdmireChoose;
	/** 名将赠送 */
	private IHeroAdmirePresent eventHeroAdmirePresent;
	/** 名将招募 */
	private IHeroAdmireSummon eventHeroAdmireSummon;

	// 是否发送过红点
	//private boolean isSendRedPoint = false;

	private IRole roleRt;
	private Role roleDb;

	public HeroAdmireControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.eventHeroAdmireChoose = this.roleRt.getEventControler().registerEvent(IHeroAdmireChoose.class);
		this.eventHeroAdmirePresent = this.roleRt.getEventControler().registerEvent(IHeroAdmirePresent.class);
		this.eventHeroAdmireSummon = this.roleRt.getEventControler().registerEvent(IHeroAdmireSummon.class);
	}

	/**
	 * 客户端红点显示
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!roleRt.isOnline()) {
			return null;
		}
		if (roleRt.getRoleOpenedMenu().getOpenHeroAdmireDate() != null
				&& DateUtil.isSameDay(new Date(), roleRt.getRoleOpenedMenu().getOpenHeroAdmireDate())) {
			return null;
		}
		boolean note = false;
		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		if (admireDb != null) {
			// 是否刷道具列表
			HeroAdmireInitT admireInitT = XsgHeroAdmireManager.getInstance().getAdmireInitT();
			if (DateUtil.checkTime(admireDb.getItemRefreshDate(), DateUtil.joinTime(admireInitT.itemRefreshDate))) {
				note = true;
			} else {
				// 不用刷新道具列表，判断是否已经使用
				@SuppressWarnings("unchecked")
				Map<String, String> itemMap = TextUtil.GSON.fromJson(admireDb.getItemList(), Map.class);
				for (String val : itemMap.values()) {
					if (val.equals("0")) { //$NON-NLS-1$
						note = true;
						break;
					}
				}
			}
		}
		//isSendRedPoint = note;
		return note ? new MajorUIRedPointNote(MajorMenu.HeroAdmireMenu, true) : null;
	}

	@Override
	public AdmireView selectAdmireShow() throws NoteException {
		// 查询和初始化 武将招募信息
		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		if (admireDb == null) {
			admireDb = new RoleHeroAdmire(roleRt.getRoleId(), roleDb, 0, 0, new Date(0), new Date(0));
		}

		// 刷新武将和道具
		refreshHero(admireDb);
		refreshItem(admireDb);
		roleDb.setHeroAdmire(admireDb);

		// 返回客户端消息
		AdmireView resView = new AdmireView();
		resView.heroId = admireDb.getHeroId();
		resView.value = admireDb.getValue();
		resView.heroList = admireDb.getHeroList();
		resView.itemList = admireDb.getItemList();

		return resView;
	}

	@Override
	public void chooseHero(int heroId) throws NoteException {
		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		if (admireDb != null) {
			if (TextUtil.stringToList(admireDb.getHeroList()).contains(String.valueOf(heroId))) {
				HeroAdmireT heroT = XsgHeroAdmireManager.getInstance().getHeroMap(heroId);
				if (heroT != null) {
					admireDb.setHeroId(heroId);
					admireDb.setValue(heroT.initialNice);
				} else {
					throw new NoteException(Messages.getString("HeroAdmireControler.1")); //$NON-NLS-1$
				}
			} else {
				throw new NoteException(Messages.getString("HeroAdmireControler.2")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("HeroAdmireControler.3")); //$NON-NLS-1$
		}
		// 添加武将选择事件
		eventHeroAdmireChoose.onChoose(heroId);
	}

	@Override
	public void exchangeHero(int heroId) throws NoteException {
		chooseHero(heroId);
	}

	@Override
	public void clearHero() throws NoteException {
		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		if (admireDb != null) {
			admireDb.setHeroId(0);
			admireDb.setValue(0);
		}
	}

	@Override
	public int presentHero(int id) throws NoteException {
		HeroAdmireItemT itemT = XsgHeroAdmireManager.getInstance().getItemMap(id);
		if (itemT == null) {
			throw new NoteException(Messages.getString("HeroAdmireControler.4")); //$NON-NLS-1$
		}

		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		@SuppressWarnings("unchecked")
		Map<String, String> itemMap = TextUtil.GSON.fromJson(admireDb.getItemList(), Map.class);
		// 是否已经使用过道具，进行仰慕
		if (!itemMap.get(String.valueOf(id)).equals("0")) { //$NON-NLS-1$
			throw new NoteException(Messages.getString("HeroAdmireControler.6")); //$NON-NLS-1$
		}

		// 道具 还是 装备
		ItemType type = XsgItemManager.getInstance().getItemType(itemT.itemId);
		if (type == ItemType.DefaultItemType) {
			if (roleRt.getItemControler().getItemCountInPackage(itemT.itemId) >= itemT.num) {
				// 扣除道具
				roleRt.getItemControler().changeItemByTemplateCode(itemT.itemId, -itemT.num);
			} else {
				throw new NoteException(Messages.getString("HeroAdmireControler.7")); //$NON-NLS-1$
			}
		} else if (type == ItemType.EquipItemType) {
			List<EquipItem> equipList = roleRt.getItemControler().findEquipByTemplateCode(itemT.itemId);
			List<String> equipIdList = new ArrayList<String>();
			for (EquipItem equip : equipList) {
				if (equip.getLevel() == 1 && TextUtil.isBlank(equip.getRefereneHero())) {
					equipIdList.add(equip.getId());
				}
			}

			if (equipIdList.size() >= itemT.num) {
				for (int i = 0; i < itemT.num; i++) {
					// 扣除装备
					roleRt.getItemControler().changeItemById(equipIdList.get(i), -1);
				}
			} else {
				throw new NoteException(Messages.getString("HeroAdmireControler.8")); //$NON-NLS-1$
			}
		}

		// 增加仰慕的值
		HeroAdmireT heroT = XsgHeroAdmireManager.getInstance().getHeroMap(admireDb.getHeroId());
		int newNiceValue = admireDb.getValue() + itemT.niceValue;
		if (heroT.NiceMax < newNiceValue) {
			admireDb.setValue(heroT.NiceMax);
		} else {
			admireDb.setValue(newNiceValue);
		}

		// 标示 道具已经使用
		itemMap.put(String.valueOf(id), "1"); //$NON-NLS-1$
		admireDb.setItemList(TextUtil.GSON.toJson(itemMap));

		// 添加 武将仰慕 事件
		eventHeroAdmirePresent.onPresent(admireDb.getValue(), itemT.itemId, itemT.num);

		return admireDb.getValue();
	}

	@Override
	public int summonHero() throws NoteException {
		RoleHeroAdmire admireDb = roleDb.getHeroAdmire();
		HeroAdmireT heroT = XsgHeroAdmireManager.getInstance().getHeroMap(admireDb.getHeroId());
		if (heroT != null) {
			if (admireDb.getValue() >= heroT.NiceMax) {
				// 获得武将
				int num = this.obtainHero(admireDb.getHeroId());
				// 仰慕名将ID和仰慕值，重置
				admireDb.setHeroId(0);
				admireDb.setValue(0);
				roleDb.setHeroAdmire(admireDb);
				// 添加 武将召唤 事件
				eventHeroAdmireSummon.onSummon(admireDb.getHeroId());

				return num;
			} else {
				throw new NoteException(Messages.getString("HeroAdmireControler.10")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("HeroAdmireControler.11")); //$NON-NLS-1$
		}
	}
	
	private boolean shouldRefreshHero(Date lastRefreshTime, HeroAdmireInitT admireInitT) {
		String []timeArray = admireInitT.heroRefreshDate.split(",");
		for (String str:timeArray) {
			if (DateUtil.checkTime(lastRefreshTime, DateUtil.joinTime(str))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 刷新武将，上一次出现的武将不再随机出现
	 * 
	 * @param admireDb
	 */
	private void refreshHero(RoleHeroAdmire admireDb) {
		HeroAdmireInitT admireInitT = XsgHeroAdmireManager.getInstance().getAdmireInitT();

		// 是否刷武将列表
		if (shouldRefreshHero(admireDb.getHeroRefreshDate(), admireInitT)) {
			// 得到各个星级武将列表
			List<HeroAdmireT> HeroStar3_List = new ArrayList<HeroAdmireT>(XsgHeroAdmireManager.getInstance()
					.getHeroStarMap(3));
			List<HeroAdmireT> HeroStar2_List = new ArrayList<HeroAdmireT>(XsgHeroAdmireManager.getInstance()
					.getHeroStarMap(2));
			List<HeroAdmireT> HeroStar1_List = new ArrayList<HeroAdmireT>(XsgHeroAdmireManager.getInstance()
					.getHeroStarMap(1));

			// 去除上次已经存在武将
			if (!TextUtil.isBlank(admireDb.getHeroList())) {
				for (String heroId : TextUtil.stringToList(admireDb.getHeroList())) {
					for (HeroAdmireT heroAdmireT : HeroStar3_List) {
						if (heroAdmireT.id == Integer.valueOf(heroId)) {
							HeroStar3_List.remove(heroAdmireT);
							break;
						}
					}

					for (HeroAdmireT heroAdmireT : HeroStar2_List) {
						if (heroAdmireT.id == Integer.valueOf(heroId)) {
							HeroStar2_List.remove(heroAdmireT);
							break;
						}
					}

					for (HeroAdmireT heroAdmireT : HeroStar1_List) {
						if (heroAdmireT.id == Integer.valueOf(heroId)) {
							HeroStar1_List.remove(heroAdmireT);
							break;
						}
					}
				}
			}

			// 随机得到武将列表,每个星级武将随机的数量不等
			int[] star3 = NumberUtil.randomArry(admireInitT.num_3S, 0, HeroStar3_List.size());
			int[] star2 = NumberUtil.randomArry(admireInitT.num_2S, 0, HeroStar2_List.size());
			int[] star1 = NumberUtil.randomArry(admireInitT.num_1S, 0, HeroStar1_List.size());

			List<String> heroList = new ArrayList<String>();
			for (int i = 0; i < star3.length; i++) {
				heroList.add(String.valueOf(HeroStar3_List.get(star3[i]).id));
			}
			for (int i = 0; i < star2.length; i++) {
				heroList.add(String.valueOf(HeroStar2_List.get(star2[i]).id));
			}
			for (int i = 0; i < star1.length; i++) {
				heroList.add(String.valueOf(HeroStar1_List.get(star1[i]).id));
			}

			// 保存数据
			admireDb.setHeroList(TextUtil.join(heroList, ",")); //$NON-NLS-1$
			admireDb.setHeroRefreshDate(new Date());
		}
	}

	/**
	 * 刷新道具，上一次出现的道具不再随机出现
	 * 
	 * @param admireDb
	 */
	private void refreshItem(RoleHeroAdmire admireDb) {
		HeroAdmireInitT admireInitT = XsgHeroAdmireManager.getInstance().getAdmireInitT();
		// 是否刷道具列表
		if (DateUtil.checkTime(admireDb.getItemRefreshDate(), DateUtil.joinTime(admireInitT.itemRefreshDate))) {
			// 得到各个等级的道具
			List<HeroAdmireItemT> HeroItem2_List = new ArrayList<HeroAdmireItemT>(XsgHeroAdmireManager.getInstance()
					.getItemTypeMap(2));
			List<HeroAdmireItemT> HeroItem1_List = new ArrayList<HeroAdmireItemT>(XsgHeroAdmireManager.getInstance()
					.getItemTypeMap(1));
			List<HeroAdmireItemT> HeroItem0_List = new ArrayList<HeroAdmireItemT>(XsgHeroAdmireManager.getInstance()
					.getItemTypeMap(0));

			// 去除上次已经存在道具
			if (!TextUtil.isBlank(admireDb.getItemList())) {
				@SuppressWarnings("unchecked")
				Map<String, String> itemMap = TextUtil.GSON.fromJson(admireDb.getItemList(), Map.class);
				for (String itemId : itemMap.keySet()) {

					for (HeroAdmireItemT itemT : HeroItem2_List) {
						if (itemId.equals(itemT.id)) {
							HeroItem2_List.remove(itemT);
							break;
						}
					}

					for (HeroAdmireItemT itemT : HeroItem1_List) {
						if (itemId.equals(itemT.id)) {
							HeroItem1_List.remove(itemT);
							break;
						}
					}

					for (HeroAdmireItemT itemT : HeroItem0_List) {
						if (itemId.equals(itemT.id)) {
							HeroItem0_List.remove(itemT);
							break;
						}
					}
				}
			}
			
			// 构造随机列表
			List<RandomHeroAdmireReward> rewardRandomList2 = new ArrayList<RandomHeroAdmireReward>();
			List<RandomHeroAdmireReward> rewardRandomList1 = new ArrayList<RandomHeroAdmireReward>();
			List<RandomHeroAdmireReward> rewardRandomList0 = new ArrayList<RandomHeroAdmireReward>();
			if (HeroItem2_List != null) {
				for (HeroAdmireItemT itemT : HeroItem2_List) {
					rewardRandomList2.add(new RandomHeroAdmireReward(itemT.id, itemT.pro));
				}
			}
			if (HeroItem1_List != null) {
				for (HeroAdmireItemT itemT : HeroItem1_List) {
					rewardRandomList1.add(new RandomHeroAdmireReward(itemT.id, itemT.pro));
				}
			}
			if (HeroItem0_List != null) {
				for (HeroAdmireItemT itemT : HeroItem0_List) {
					rewardRandomList0.add(new RandomHeroAdmireReward(itemT.id, itemT.pro));
				}
			}

			// 随机得到道具列表
			Map<String, String> itemMap = new HashMap<String, String>();
			RandomRange<RandomHeroAdmireReward> randomRange2 = new RandomRange<HeroAdmireControler.RandomHeroAdmireReward>(rewardRandomList2);
			RandomRange<RandomHeroAdmireReward> randomRange1 = new RandomRange<HeroAdmireControler.RandomHeroAdmireReward>(rewardRandomList1);
			RandomRange<RandomHeroAdmireReward> randomRange0 = new RandomRange<HeroAdmireControler.RandomHeroAdmireReward>(rewardRandomList0);
			itemMap.put(String.valueOf(randomRange2.random().id), "0"); //$NON-NLS-1$
			itemMap.put(String.valueOf(randomRange1.random().id), "0"); //$NON-NLS-1$
			itemMap.put(String.valueOf(randomRange0.random().id), "0"); //$NON-NLS-1$

			// 保存数据
			admireDb.setItemList(TextUtil.GSON.toJson(itemMap));
			admireDb.setItemRefreshDate(new Date());
		}
	}

	/**
	 * 仰慕获得武将<br>
	 * 武将存在，装换成将魂
	 * 
	 * @param templateId
	 */
	private int obtainHero(int templateId) {
		int num = 0;
		IHero hero = this.roleRt.getHeroControler().getHero(templateId);
		// 是否存在名将
		if (hero == null) {
			roleRt.getHeroControler().addHero(XsgHeroManager.getInstance().getHeroT(templateId), HeroSource.ADMIRE);

		} else {
			String soulTemplateId = XsgHeroManager.getInstance().getSoulTemplateId(templateId);
			num = XsgHeroManager.getInstance().caculateSoulCountForCardTransform(hero.getTemplate().color);

			this.roleRt.getItemControler().changeItemByTemplateCode(soulTemplateId, num);
		}

		return num;
	}

	@Override
	public void resetRedPoint() {
		//this.isSendRedPoint = false;
	}
	
	public static class RandomHeroAdmireReward implements IRandomHitable {
		public int id;
		public int rank;

		public RandomHeroAdmireReward(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}

		/**
		 * 出现概率设置为0, 使他不会被随机出来
		 */
		public void disapper() {
			rank = 0;
		}
	}
}