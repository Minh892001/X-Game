package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OpenServerActivityInfoView;
import com.XSanGo.Protocol.OpenServerActivityListInfoView;
import com.XSanGo.Protocol.OpenServerActivityNodeView;
import com.XSanGo.Protocol.OpenServerSaleActivityNodeView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleOpenServerActive;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IArenaFight;
import com.morefun.XSanGo.event.protocol.IAttendantChange;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IFormationBuffChange;
import com.morefun.XSanGo.event.protocol.IFormationBuffLevelUp;
import com.morefun.XSanGo.event.protocol.IFormationClear;
import com.morefun.XSanGo.event.protocol.IFormationPosChange;
import com.morefun.XSanGo.event.protocol.IHeroEquipChange;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IItemStoneMix;
import com.morefun.XSanGo.event.protocol.IOpenServerActiveDrawAward;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;

@RedPoint
public class OpenServerActiveControler implements IArenaFight, IItemStoneMix, IOpenServerActiveControler,
		IHeroQualityUp, IFormationClear, IFormationBuffChange, IAttendantChange, IHeroEquipChange, IHeroSkillUp,
		IFormationBuffLevelUp, IEquipRebuild, IEquipStrengthen, IEquipStarUp, IHeroStarUp, IFormationPosChange {
	private IRole rt;
	private Role db;

	// 普通活动的数据模板
	private static final int normalActiveType = 1;
	// 开服半价活动的数据模板
	private static final int saleActiveType = 2;
	// 半价礼包不在活动时间内
	private final int SALE_NOT_INTIME = 0;
	// 半价礼包可购买
	private final int SALE_CAN_BUY = 1;
	// 半价礼包已购买
	private final int SALE_ALREADY_BUY = 2;
	// 不满足购买条件
	private final int SALE_CONDITION_CANNOT_BUY = 3;
	
	/** 领奖事件 */
	private IOpenServerActiveDrawAward eventDrawAward;

	public OpenServerActiveControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		IEventControler evtContrl = rt.getEventControler();

		evtContrl.registerHandler(IArenaFight.class, this);
		evtContrl.registerHandler(IItemStoneMix.class, this);
		evtContrl.registerHandler(IHeroEquipChange.class, this);
		evtContrl.registerHandler(IAttendantChange.class, this);
		evtContrl.registerHandler(IFormationBuffChange.class, this);
		evtContrl.registerHandler(IEquipStrengthen.class, this);
		evtContrl.registerHandler(IHeroSkillUp.class, this);
		evtContrl.registerHandler(IHeroStarUp.class, this);
		evtContrl.registerHandler(IHeroQualityUp.class, this);
		evtContrl.registerHandler(IEquipRebuild.class, this);
		evtContrl.registerHandler(IEquipStarUp.class, this);
		evtContrl.registerHandler(IFormationBuffLevelUp.class, this);
		evtContrl.registerHandler(IFormationClear.class, this);
		evtContrl.registerHandler(IFormationPosChange.class, this);
		
		eventDrawAward = rt.getEventControler().registerEvent(IOpenServerActiveDrawAward.class);
	}

	/**
	 * 开服活动列表
	 */
	@Override
	public String getOpenServerActiveView() {
		OpenServerActivityListInfoView allView = new OpenServerActivityListInfoView();
		Map<Integer, Map<Integer, AllServerActiveT>> allServerActionMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, TreeMap<Integer, AllServerActiveDetailT>> configMap = XsgActivityManage.getInstance()
				.getAllServerActionDetailMap();
		List<OpenServerActivityInfoView> list = new ArrayList<OpenServerActivityInfoView>();
		for (Integer type : allServerActionMap.keySet()) {
			Map<Integer, AllServerActiveT> idMap = allServerActionMap.get(type);
			if (idMap == null || idMap.size() == 0)
				continue;
			for (Integer aId : idMap.keySet()) {
				// 活动时间判断
				if (!isInTime(type, aId))
					continue;
				OpenServerActivityInfoView view = new OpenServerActivityInfoView();
				int viewType = normalActiveType;
				if (idMap.get(aId).activeType == SALES) {
					viewType = saleActiveType;
				}
				view.type = viewType;
				view.id = aId;
				view.name = idMap.get(aId).activeName;
				view.startDay = idMap.get(aId).startTime;
				view.endDay = idMap.get(aId).overTime;
				view.description = idMap.get(aId).description;
				OpenServerActivityNodeView[] activityNodeViewSe = null;
				OpenServerSaleActivityNodeView[] saleViewSeq = null;
				// 普通活动列表
				if (viewType == normalActiveType) {
					TreeMap<Integer, AllServerActiveDetailT> nodeMap = configMap.get(aId);
					if (nodeMap == null || nodeMap.size() == 0)
						continue;
					activityNodeViewSe = new OpenServerActivityNodeView[nodeMap.size()];
					int index = 0;
					for (Integer nodeId : nodeMap.keySet()) {
						OpenServerActivityNodeView nodeView = new OpenServerActivityNodeView();
						nodeView.activeId = aId;
						nodeView.nodeId = nodeId;
						nodeView.condition1 = nodeMap.get(nodeId).condition1;
						nodeView.condition2 = nodeMap.get(nodeId).condition2;
						nodeView.describe = nodeMap.get(nodeId).description;
						nodeView.status = getActiveStatus(aId, nodeId);
						nodeView.reward = getAward(nodeMap.get(nodeId).rewardMap);
						nodeView.conditionNum = nodeMap.get(nodeId).conditionNum;
						activityNodeViewSe[index] = nodeView;
						index += 1;
					}
				}
				// 半价礼包活动列表
				if (viewType == saleActiveType) {
					saleViewSeq = getSaleViewById(aId);
				}
				view.saleViewSeq = saleViewSeq;
				view.activityNodeViewSeq = activityNodeViewSe;
				list.add(view);
			}
		}
		allView.openServerActivityListViewSeq = list.toArray(new OpenServerActivityInfoView[0]);
		return LuaSerializer.serialize(allView);
	}

	/**
	 * 根据半价礼包活动ID 获得对应视图显示
	 * 
	 * @param aId
	 * @return
	 */
	private OpenServerSaleActivityNodeView[] getSaleViewById(Integer aId) {
		TreeMap<Integer, AllServerActiveSaleT> OpenServerSaleMap = XsgActivityManage.getInstance()
				.getOpenServerSaleMap();
		if (OpenServerSaleMap == null || OpenServerSaleMap.size() == 0) {
			return null;
		}
		Map<Integer, Map<Integer, AllServerActiveT>> allActiveMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, AllServerActiveT> saleActive = allActiveMap.get(SALES);
		if (saleActive == null || !saleActive.containsKey(aId)) {
			return null;
		}
		if (!isInTime(SALES, aId)) {
			return null;
		}
		// int specialDay = OpenServerSaleMap.lastKey();
		int size = OpenServerSaleMap.size();
		// boolean isShowSpecial = isBuyAllSaleExceptSpecial(aId);
		// if (!isShowSpecial) {
		// size = size - 1;
		// }
		OpenServerSaleActivityNodeView[] saleViewSeq = new OpenServerSaleActivityNodeView[size];
		int index = 0;
		for (AllServerActiveSaleT t : OpenServerSaleMap.values()) {
			// if (!isShowSpecial && t.openDay == specialDay)
			// continue;
			OpenServerSaleActivityNodeView saleView = new OpenServerSaleActivityNodeView();
			saleView.id = aId;
			saleView.openDay = t.openDay;
			saleView.bePrice = t.bePrice;
			saleView.coinType = t.coinType;
			saleView.price = t.price;
			saleView.status = getSaleStatus(aId, t.openDay);
			saleView.reward = new IntString(t.num, t.itemId);
			saleViewSeq[index] = saleView;
			index += 1;
		}
		return saleViewSeq;
	}

	private IntString[] getAward(Map<String, Integer> rewardMap) {
		if (rewardMap == null || rewardMap.size() == 0) {
			return new IntString[0];
		}
		IntString[] is = new IntString[rewardMap.size()];
		int index = 0;
		for (String item : rewardMap.keySet()) {
			IntString d = new IntString(rewardMap.get(item), item);
			is[index] = d;
			index += 1;
		}
		return is;
	}

	/**
	 * 领取开服活动奖励
	 */
	@Override
	public void acceptOpenServerActiveReward(int active, int nodeId) throws NoteException {
		Map<Integer, TreeMap<Integer, AllServerActiveDetailT>> configMap = XsgActivityManage.getInstance()
				.getAllServerActionDetailMap();
		TreeMap<Integer, AllServerActiveDetailT> nodeConfig = configMap.get(active);
		if (nodeConfig == null || nodeConfig.size() == 0) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.0"));
		}
		AllServerActiveDetailT detailNodeCfg = nodeConfig.get(nodeId);
		if (detailNodeCfg == null) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.0"));
		}
		if (getActiveStatus(active, nodeId) != 1) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.1"));
		}
		recAward(detailNodeCfg);
	}

	/**
	 * 购买半价道具
	 */
	@Override
	public String buySaleItem(int activeId, int day) throws NotEnoughMoneyException, NotEnoughYuanBaoException,
			NoteException {
		Map<Integer, Map<Integer, AllServerActiveT>> allActiveMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, AllServerActiveT> saleActive = allActiveMap.get(SALES);
		if (saleActive == null || !saleActive.containsKey(activeId)) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.0"));
		}
		TreeMap<Integer, AllServerActiveSaleT> OpenServerSaleMap = XsgActivityManage.getInstance()
				.getOpenServerSaleMap();
		AllServerActiveSaleT t = OpenServerSaleMap.get(day);
		if (t == null) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.0"));
		}
		if (getSaleStatus(activeId, day) != SALE_CAN_BUY) {
			throw new NoteException(Messages.getString("OpenServerActiveControler.2"));
		}
		if (t.coinType != 1 && t.coinType != 2)
			throw new NoteException(Messages.getString("OpenServerActiveControler.2"));
		String costType = Const.PropertyName.MONEY;
		// 钱币判断
		if (t.coinType == 2) {
			if (this.rt.getJinbi() < t.price) {// 游戏币
				throw new NotEnoughMoneyException();
			}
		} else if (t.coinType == 1) {
			costType = Const.PropertyName.RMBY;
			if (this.rt.getTotalYuanbao() < t.price) {// 元宝
				throw new NotEnoughYuanBaoException();
			}
		}
		this.rt.getRewardControler().acceptReward(costType, -t.price);
		this.rt.getRewardControler().acceptReward(t.itemId, t.num);

		Map<Integer, RoleOpenServerActive> openServerAct = db.getOpenServerAct();
		RoleOpenServerActive dbDatail = openServerAct.get(day);
		if (dbDatail == null) {
			dbDatail = new RoleOpenServerActive(GlobalDataManager.getInstance().generatePrimaryKey(), db, day, "",
					new Date());
			dbDatail.setRecDate(new Date());
			openServerAct.put(day, dbDatail);
		} else {
			dbDatail.setRecDate(new Date());
		}
		return LuaSerializer.serialize(getSaleViewById(activeId));
	}

	/**
	 * 更新活动进度
	 * 
	 * @param type
	 * @param para
	 */
	public void updateProgress(int type, String para) {
		Map<Integer, AllServerActiveT> typeMap = XsgActivityManage.getInstance().getAllServerActionMap().get(type);
		if (typeMap == null || typeMap.size() == 0) {
			return;
		}
		Map<Integer, RoleOpenServerActive> openServerAct = db.getOpenServerAct();
		for (Integer aId : typeMap.keySet()) {
			if (!isInTime(type, aId))
				continue;
			TreeMap<Integer, AllServerActiveDetailT> nodeMap = XsgActivityManage.getInstance()
					.getAllServerActionDetailMap().get(aId);
			if (nodeMap == null || nodeMap.size() == 0) {
				continue;
			}
			for (Integer nodeId : nodeMap.keySet()) {
				if (getActiveStatus(aId, nodeId) == 1 || getActiveStatus(aId, nodeId) == 2) {
					continue;
				}
				AllServerActiveDetailT nodeT = nodeMap.get(nodeId);
				String cond1 = nodeT.condition1;
				String cond2 = nodeT.condition2;
				RoleOpenServerActive dbInfo = openServerAct.get(nodeId);
				switch (type) {
				case FIRST_FIGHT_POINT:
					if (dbInfo == null) {
						dbInfo = new RoleOpenServerActive(GlobalDataManager.getInstance().generatePrimaryKey(), db,
								nodeId, para, new Date());
						openServerAct.put(nodeId, dbInfo);
					} else {
						if (Integer.valueOf(para) > Integer.valueOf(dbInfo.getProgress())) {
							dbInfo.setProgress(para);
						}
					}
					break;
				case HERO_COLOR_UP:
					int count = 0;
					Map<Integer, Integer> color4Num = getHeroColorNumInfo();
					for (Integer colorLvl : color4Num.keySet()) {
						if (colorLvl >= Integer.valueOf(cond2)) {
							count = count + color4Num.get(colorLvl);
						}
					}
					if (dbInfo == null) {
						dbInfo = new RoleOpenServerActive(GlobalDataManager.getInstance().generatePrimaryKey(), db,
								nodeId, count + "", new Date());
						openServerAct.put(nodeId, dbInfo);
					} else {
						if (Integer.valueOf(count) > Integer.valueOf(dbInfo.getProgress())) {
							dbInfo.setProgress(count + "");
						}
					}
					break;
				case STONE_NUM:
					int lvl = Integer.valueOf(para.split("_")[0]);
					int num = Integer.valueOf(para.split("_")[1]);
					if (Integer.valueOf(cond2) == lvl) {
						if (dbInfo == null) {
							dbInfo = new RoleOpenServerActive(GlobalDataManager.getInstance().generatePrimaryKey(), db,
									nodeId, num + "", new Date());
							openServerAct.put(nodeId, dbInfo);
						} else {
							int curNum = Integer.valueOf(dbInfo.getProgress()) + num;
							dbInfo.setProgress(curNum + "");
						}
					}
					break;
				case AREAN_NUM:
					int curRank = Integer.valueOf(para);
					if (dbInfo == null) {
						dbInfo = new RoleOpenServerActive(GlobalDataManager.getInstance().generatePrimaryKey(), db,
								nodeId, curRank + "", new Date());
						openServerAct.put(nodeId, dbInfo);
					} else {
						if (curRank <= Integer.valueOf(cond1)) {
							dbInfo.setProgress(curRank + "");
						}
					}
					break;
				default:
					return;
				}
			}
		}
		notifyRedPoint();
	}

	/**
	 * 获取活动节点的领奖状态 0：未完成 1：可领取 2：已领取
	 * 
	 * @param nodeId
	 * @return
	 */
	private int getActiveStatus(int activeId, int nodeId) {
		int type = XsgActivityManage.getInstance().getAllServerActionId4Type().get(activeId);
		TreeMap<Integer, AllServerActiveDetailT> nodeMap = XsgActivityManage.getInstance()
				.getAllServerActionDetailMap().get(activeId);
		AllServerActiveDetailT t = nodeMap.get(nodeId);
		Map<Integer, RoleOpenServerActive> openServerAct = db.getOpenServerAct();
		RoleOpenServerActive dbNodeInfo = openServerAct.get(nodeId);
		if (dbNodeInfo == null) {
			return 0;
		}
		if (dbNodeInfo.getRecDate() != null) {
			return 2;
		}
		String dbProgress = dbNodeInfo.getProgress();
		String cond1 = t.condition1;
		switch (type) {
		case HERO_COLOR_UP:
		case FIRST_FIGHT_POINT:
		case STONE_NUM:
			if (Integer.valueOf(dbProgress) >= Integer.valueOf(cond1)) {
				return 1;
			}
			break;
		case AREAN_NUM:
			if (Integer.valueOf(dbProgress) <= Integer.valueOf(cond1)) {
				return 1;
			}
			break;
		default:
			return 0;
		}
		return 0;
	}

	/**
	 * 领取活动奖励(只适用于全部活动)
	 * 
	 * @param AllServerActiveDetailT
	 *            t
	 */
	private void recAward(AllServerActiveDetailT t) {
		Map<Integer, RoleOpenServerActive> openServerAct = db.getOpenServerAct();
		RoleOpenServerActive dbDatail = openServerAct.get(t.activeNum);
		if (dbDatail == null)
			return;
		Map<String, Integer> rewardMap = t.rewardMap;
		for (String item : rewardMap.keySet()) {
			this.rt.getRewardControler().acceptReward(item, rewardMap.get(item));
		}
		dbDatail.setRecDate(new Date());
		
		this.eventDrawAward.onDraw(t.activeId, t.activeNum, rewardMap.toString());
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (isCanRec()) {
			return new MajorUIRedPointNote(MajorMenu.OpenServerActive, false);
		}
		return null;
	}

	private boolean isCanRec() {
		boolean flag = false;
		Map<Integer, Map<Integer, AllServerActiveT>> allServerActionMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, TreeMap<Integer, AllServerActiveDetailT>> configMap = XsgActivityManage.getInstance()
				.getAllServerActionDetailMap();
		for (Integer type : allServerActionMap.keySet()) {
			Map<Integer, AllServerActiveT> idMap = allServerActionMap.get(type);
			if (idMap == null || idMap.size() == 0)
				continue;
			for (Integer aId : idMap.keySet()) {
				TreeMap<Integer, AllServerActiveDetailT> nodeMap = configMap.get(aId);
				if (nodeMap == null || nodeMap.size() == 0)
					continue;
				// 活动时间判断
				if (!isInTime(type, aId))
					continue;
				for (Integer nodeId : nodeMap.keySet()) {
					if (getActiveStatus(aId, nodeId) == 1) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 红点提示
	 */
	private void notifyRedPoint() {
		if (isCanRec()) {
			this.rt.getNotifyControler().onMajorUIRedPointChange(
					new MajorUIRedPointNote(MajorMenu.OpenServerActive, false));
		}

	}

	/**
	 * 是否在活动时间内
	 * 
	 * @param type
	 * @param activeId
	 * @return
	 */
	private boolean isInTime(int type, int activeId) {
		if (!XsgActivityManage.getInstance().isInOpenServerCfgTime()) {
			return false;
		}
		Map<Integer, Map<Integer, AllServerActiveT>> allServerActionMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, AllServerActiveT> idMap = allServerActionMap.get(type);
		if (idMap == null || idMap.size() == 0)
			return false;
		AllServerActiveT t = idMap.get(activeId);
		if (t == null)
			return false;
		return DateUtil.isBetween(new Date(), t.startDate, t.endDate);
	}

	/**
	 * 拥有n名+N武将
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getHeroColorNumInfo() {
		Map<Integer, Integer> infos = new HashMap<Integer, Integer>();
		if (null != rt.getHeroControler().getAllHero()) {
			Iterable<IHero> heros = rt.getHeroControler().getAllHero();
			for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
				IHero hero = (IHero) iter.next();
				int color = hero.getQualityLevel();
				if (null == infos.get(color))
					infos.put(color, 1);
				else
					infos.put(color, infos.get(color) + 1);
			}
		}
		return infos;
	}

	/**
	 * 获取半价礼包状态
	 * 
	 * @param day
	 * @return
	 */
	private int getSaleStatus(int activeId, int day) {
		Map<Integer, Map<Integer, AllServerActiveT>> allActiveMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, AllServerActiveT> saleActive = allActiveMap.get(SALES);
		if (saleActive == null || !saleActive.containsKey(activeId)) {
			return SALE_NOT_INTIME;
		}

		TreeMap<Integer, AllServerActiveSaleT> OpenServerSaleMap = XsgActivityManage.getInstance()
				.getOpenServerSaleMap();
		AllServerActiveSaleT config = OpenServerSaleMap.get(day);
		// 特殊礼包 第8天的 需完成前面所有的 才可购买
		int specialDay = OpenServerSaleMap.lastKey();
		if (config == null) {
			return SALE_NOT_INTIME;
		}
		if (!isInTime4Sale(saleActive.get(activeId), config) && day != specialDay) {
			return SALE_NOT_INTIME;
		}
		Map<Integer, RoleOpenServerActive> openServerAct = db.getOpenServerAct();
		// 特殊礼包的判断
		if (day == specialDay) {
			if (!isBuyAllSaleExceptSpecial(activeId)) {
				return SALE_CONDITION_CANNOT_BUY;
			}
		}
		RoleOpenServerActive dbNodeInfo = openServerAct.get(day);
		if (dbNodeInfo == null) {
			return SALE_CAN_BUY;
		}
		if (dbNodeInfo.getRecDate() != null) {
			return SALE_ALREADY_BUY;
		}
		return SALE_CAN_BUY;
	}

	/**
	 * 是否购买了除特殊道具的 其他全部半价道具
	 * 
	 * @return
	 */
	private boolean isBuyAllSaleExceptSpecial(int activeId) {
		TreeMap<Integer, AllServerActiveSaleT> OpenServerSaleMap = XsgActivityManage.getInstance()
				.getOpenServerSaleMap();
		Map<Integer, Map<Integer, AllServerActiveT>> allActiveMap = XsgActivityManage.getInstance()
				.getAllServerActionMap();
		Map<Integer, AllServerActiveT> saleActive = allActiveMap.get(SALES);
		if (saleActive == null)
			return false;
		AllServerActiveT config = saleActive.get(activeId);
		if (config == null)
			return false;
		// 特殊礼包 第8天的 需完成前面所有的 才可购买
		int specialDay = OpenServerSaleMap.lastKey();
		for (Integer day : OpenServerSaleMap.keySet()) {
			if (specialDay == day)
				continue;
			if (getSaleStatus(activeId, day) != SALE_ALREADY_BUY) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否在可购买时间内
	 * 
	 * @param day
	 * @return
	 */
	private boolean isInTime4Sale(AllServerActiveT actConfig, AllServerActiveSaleT config) {
		if (actConfig == null || config == null || config.openDate == null)
			return false;
		if (DateUtil.isBetween(config.openDate, actConfig.endDate))
			return true;
		return false;
	}

	@Override
	public void onFormationClear(IFormation formation) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onFormationBuffChange(IFormation formation, FormationBuffItem book) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onAttendantChange(IHero hero, byte pos, IHero attendant) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onHeroEquipChange(IHero hero, EquipItem equip) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onFormationBuffLevelUp(FormationBuffItem buff, int money,int expDiff,int beforeLevel,int beforeExp,int afterLevel,int afterExp) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onEquipRebuild(EquipItem equip) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onEquipStarUp(EquipItem equip, int uplevel, List<EquipItem> deleteList, int money, int addExp,
			Map<String, Integer> consumeStars) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	/** 武将星级变更事件 */
	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onFormationPositionChange(IFormation formation, int pos, IHero hero) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
	}

	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLevel) {
		updateProgress(FIRST_FIGHT_POINT, rt.calculateBattlePower() + "");
		updateProgress(HERO_COLOR_UP, "");
	}

	@Override
	public void onStoneMixAddChange(int lvl, int num) {
		updateProgress(STONE_NUM, lvl + "_" + num);
	}

	@Override
	public void onArenaFight(int resFlag, int roleRank, int rivalRank, int sneerId, String reward) {
		updateProgress(AREAN_NUM, roleRank + "");
	}

	@Override
	public void update4Login() {
		// 竞技排行的处理
		ArenaRank rank = rt.getArenaRankControler().getArenaRank();
		if (rank == null)
			return;
		int rankNum = rank.getRank();
		rt.getOpenServerActiveControler();
		updateProgress(IOpenServerActiveControler.AREAN_NUM, rankNum + "");
	}
}
