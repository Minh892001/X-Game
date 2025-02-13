package com.morefun.XSanGo.smithyExchange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Preview;
import com.XSanGo.Protocol.SmithyMall;
import com.XSanGo.Protocol.SmithyMallSel;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleBlueSmithy;
import com.morefun.XSanGo.db.game.RoleSmithy;
import com.morefun.XSanGo.event.protocol.ISmithyBlueMallExchange;
import com.morefun.XSanGo.event.protocol.ISmithyBlueMallRefresh;
import com.morefun.XSanGo.event.protocol.ISmithyMallExchange;
import com.morefun.XSanGo.event.protocol.ISmithyMallRefresh;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class SmithyExchangeController implements ISmithyExchangeController {

	private static final Log log = LogFactory.getLog(SmithyExchangeController.class);

	private IRole roleRt;
	private Role roleDb;
	/** 商城紫装置换 */
	private ISmithyMallExchange eventMallExchange;
	/** 紫装商城刷新 */
	private ISmithyMallRefresh eventMallRefresh;
	/** 商城蓝装置换 */
	private ISmithyBlueMallExchange eventBlueMallExchange;
	/** 蓝装商城刷新 */
	private ISmithyBlueMallRefresh eventBlueMallRefresh;

	public SmithyExchangeController() {
	}

	public SmithyExchangeController(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.eventMallExchange = this.roleRt.getEventControler().registerEvent(ISmithyMallExchange.class);
		this.eventMallRefresh = this.roleRt.getEventControler().registerEvent(ISmithyMallRefresh.class);
		this.eventBlueMallExchange = this.roleRt.getEventControler().registerEvent(ISmithyBlueMallExchange.class);
		this.eventBlueMallRefresh = this.roleRt.getEventControler().registerEvent(ISmithyBlueMallRefresh.class);

		// 角色创建，就初始化铁匠铺数据
		this.initRoleSmithy();
	}

	private void initRoleSmithy() {
		// 初始化紫装兑换数据
		if (this.roleDb.getSmithy() == null) {
			RoleSmithy smithy = new RoleSmithy();

			smithy.setRoleId(this.roleRt.getRoleId());
			smithy.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(false))); // $NON-NLS-1$
			smithy.setExchangeRefreshDate(new Date());
			smithy.setExchangeRefreshNum(0);
			smithy.setRole(this.roleDb);
			this.roleDb.setSmithy(smithy);
		}
		// 初始化蓝装兑换
		if (this.roleDb.getBlueSmithy() == null) {
			RoleBlueSmithy blueSmithy = new RoleBlueSmithy();

			blueSmithy.setRoleId(this.roleRt.getRoleId());
			blueSmithy.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(true))); // $NON-NLS-1$
			blueSmithy.setExchangeRefreshDate(new Date());
			blueSmithy.setExchangeRefreshNum(0);
			blueSmithy.setRole(this.roleDb);
			this.roleDb.setBlueSmithy(blueSmithy);
		}
	}

	@Override
	public SmithyMallSel selMallList() {
		RoleSmithy smity = roleDb.getSmithy();
		Date exchangeRefreshDate = smity.getExchangeRefreshDate();
		Date joinTime = DateUtil.joinTime(XsgGameParamManager.getInstance().getSmithyMall() + ":00");
		boolean refFlag = false;
		if (DateUtil.isPass(joinTime, exchangeRefreshDate)) {
			smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(false)));
			smity.setExchangeRefreshNum(0);
			smity.setExchangeRefreshDate(new Date());
			refFlag = true;
		}
		SmithyMallSel mallSel = new SmithyMallSel();
		SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getMallRefreshMap()
				.get(smity.getExchangeRefreshNum() + 1);

		mallSel.cost = refreshT == null ? 0 : refreshT.Cost;
		mallSel.word = XsgSmithyManager.getInstance().generateRandomWord();
		mallSel.exchangeRefreshNum = smity.getExchangeRefreshNum();
		SmithyMall[] fromJson = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);
		mallSel.SmithyMallList = fromJson == null ? new SmithyMall[0] : fromJson;

		if(refFlag){
			// 添加刷新刷新 事件
			eventMallRefresh.onRefresh(0, 0,
					smity.getExchangeItemStr(), exchangeRefreshDate);
		}
		return mallSel;
	}

	/**
	 * 铁匠铺蓝装兑换商城
	 * 
	 * @return
	 */
	@Override
	public SmithyMallSel selBlueMallList() {

		RoleBlueSmithy smity = roleDb.getBlueSmithy();
		Date exchangeRefreshDate = smity.getExchangeRefreshDate();
		Date joinTime = DateUtil.joinTime(XsgGameParamManager.getInstance().getSmithyMall() + ":00");
		boolean refFlag = false;
		if (DateUtil.isPass(joinTime, exchangeRefreshDate)) {
			smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(true)));
			smity.setExchangeRefreshNum(0);
			smity.setExchangeRefreshDate(new Date());
			refFlag = true;
		}
		SmithyMallSel mallSel = new SmithyMallSel();
		SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getBlueMallRefreshMap()
				.get(smity.getExchangeRefreshNum() + 1);

		mallSel.cost = refreshT == null ? 0 : refreshT.Cost;
		mallSel.word = XsgSmithyManager.getInstance().generateBlueRandomWord();
		mallSel.exchangeRefreshNum = smity.getExchangeRefreshNum();
		SmithyMall[] fromJson = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);
		mallSel.SmithyMallList = fromJson == null ? new SmithyMall[0] : fromJson;

		if(refFlag){
			// 添加刷新刷新 事件
			eventBlueMallRefresh.onRefresh(0, 0, smity.getExchangeItemStr(), exchangeRefreshDate);
		}
		return mallSel;
	}

	/**
	 * 设置铁匠铺商品列表
	 * 
	 * @return
	 */
	private SmithyMall[] selMallItem(boolean isBlue) {

		int mallNum1 = XsgGameParamManager.getInstance().getSmithyMallNum()[0];
		int mallNum2 = XsgGameParamManager.getInstance().getSmithyMallNum()[1];

		if (isBlue) {// 蓝装
			mallNum1 = XsgGameParamManager.getInstance().getBlueSmithyMallNum()[0];
			mallNum2 = XsgGameParamManager.getInstance().getBlueSmithyMallNum()[1];
		}
		List<SmithyMallStoreT> mallStoreList = new ArrayList<SmithyMallStoreT>(mallNum1 + mallNum2);

		// 固定道具和概率随机道具配置脚本
		Map<Integer, SmithyMallStoreT> storeMap1 = XsgSmithyManager.getInstance().getMallStoreMap1();
		Map<Integer, SmithyMallStoreT> storeMap2 = XsgSmithyManager.getInstance().getMallStoreMap2();

		if (isBlue) {// 蓝装
			storeMap1 = XsgSmithyManager.getInstance().getBlueMallStoreMap1();
			storeMap2 = XsgSmithyManager.getInstance().getBlueMallStoreMap2();
		}

		// 从配置中获取固定道具
		if (storeMap1.size() > mallNum1) {
			if(mallNum1>0){
				mallStoreList.addAll(this.calcRandPro(mallNum1, storeMap1));
			}
		} else {
			mallStoreList.addAll(new ArrayList<SmithyMallStoreT>(storeMap1.values()));
		}

		// 从配置中装载概率随机 道具
		if (storeMap2.size() > mallNum2) {
			if(mallNum2>0){
				mallStoreList.addAll(this.calcRandPro(mallNum2, storeMap2));
			}
		} else {
			mallStoreList.addAll(storeMap2.values());
		}

		SmithyMall[] arenaMallArr = new SmithyMall[mallStoreList.size()];
		for (int i = 0; i < mallStoreList.size(); i++) {
			SmithyMall arenaMall = new SmithyMall();
			SmithyMallStoreT storeT = mallStoreList.get(i);
			if (storeT != null) {
				arenaMall.id = storeT.ID;
				arenaMall.itemId = storeT.ItemID;
				arenaMall.num = storeT.NumMax;
				arenaMall.price = storeT.NumMax * storeT.Price;
				arenaMall.coinType = storeT.CoinType;
				arenaMall.flag = 1;
				arenaMall.halfPriceFlag = storeT.isHalf;

				arenaMallArr[i] = arenaMall;
			}
		}

		return arenaMallArr;

	}

	@Override
	public SmithyMallSel refMallList() throws NoteException, NotEnoughYuanBaoException {
		RoleSmithy smity = roleDb.getSmithy();
		Date oldTime = smity.getExchangeRefreshDate();
		int refCost = 0;
		if (DateUtil.isPass(DateUtil.joinTime(XsgGameParamManager.getInstance().getSmithyMall() + ":00"),
				smity.getExchangeRefreshDate())) {

			smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(false)));
			smity.setExchangeRefreshNum(0);
			smity.setExchangeRefreshDate(new Date());
		} else {

			SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getMallRefreshMap()
					.get(smity.getExchangeRefreshNum() + 1);

			if (refreshT != null) {
				// 元宝数量
				int yuanbao = roleRt.getTotalYuanbao();
				if (yuanbao >= refreshT.Cost) {
					smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(false)));
					smity.setExchangeRefreshNum(smity.getExchangeRefreshNum() + 1);
					smity.setExchangeRefreshDate(new Date());
					// 扣除物品
					roleRt.winYuanbao(-refreshT.Cost, false);
				} else {
					throw new NotEnoughYuanBaoException(); // $NON-NLS-1$
				}
			} else {
				throw new NotEnoughYuanBaoException(); // $NON-NLS-1$
			}

			refCost = refreshT.Cost;
		}
		// 添加刷新刷新 事件
		eventMallRefresh.onRefresh(smity.getExchangeRefreshNum(), refCost,
				smity.getExchangeItemStr(), oldTime);

		roleDb.setSmithy(smity);
		SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getMallRefreshMap()
				.get(smity.getExchangeRefreshNum() + 1);

		SmithyMallSel mallSell = new SmithyMallSel();
		mallSell.cost = refreshT == null ? 0 : refreshT.Cost;
		mallSell.word = XsgSmithyManager.getInstance().generateRandomWord();
		mallSell.exchangeRefreshNum = smity.getExchangeRefreshNum();
		mallSell.SmithyMallList = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

		return mallSell;
	}

	/**
	 * 蓝装兑换商城刷新
	 */
	@Override
	public SmithyMallSel refBlueMallList() throws NoteException, NotEnoughMoneyException {
		RoleBlueSmithy smity = roleDb.getBlueSmithy();
		Date oldTime = smity.getExchangeRefreshDate();
		int refCost = 0;//DateUtil.isPass(joinTime, exchangeRefreshDate)
		if (DateUtil.isPass(DateUtil.joinTime(XsgGameParamManager.getInstance().getBlueSmithyMall() + ":00"), 
				smity.getExchangeRefreshDate())) {

			smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(true)));
			smity.setExchangeRefreshNum(0);
			smity.setExchangeRefreshDate(new Date());
		} else {

			SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getBlueMallRefreshMap()
					.get(smity.getExchangeRefreshNum() + 1);

			if (refreshT != null) {
				// 金币数量
				long amount = roleRt.getJinbi();
				if (amount >= refreshT.Cost) {
					smity.setExchangeItemStr(TextUtil.GSON.toJson(this.selMallItem(true)));
					smity.setExchangeRefreshNum(smity.getExchangeRefreshNum() + 1);
					smity.setExchangeRefreshDate(new Date());
					// 扣除物品
					roleRt.winJinbi(-refreshT.Cost);
				} else {
					throw new NotEnoughMoneyException(); // $NON-NLS-1$
				}
			} else {
				throw new NotEnoughMoneyException(); // $NON-NLS-1$
			}

			refCost = refreshT.Cost;
		}
		// 添加刷新刷新 事件
		eventBlueMallRefresh.onRefresh(smity.getExchangeRefreshNum(), refCost, smity.getExchangeItemStr(), oldTime);

		roleDb.setBlueSmithy(smity);
		SmithyMallRefreshT refreshT = XsgSmithyManager.getInstance().getBlueMallRefreshMap()
				.get(smity.getExchangeRefreshNum() + 1);

		SmithyMallSel mallSell = new SmithyMallSel();
		mallSell.cost = refreshT == null ? 0 : refreshT.Cost;
		mallSell.word = XsgSmithyManager.getInstance().generateBlueRandomWord();
		mallSell.exchangeRefreshNum = smity.getExchangeRefreshNum();
		mallSell.SmithyMallList = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

		return mallSell;
	}

	@Override
	public SmithyMallSel exchangeItem(int storeId) throws NoteException {

		RoleSmithy smity = roleDb.getSmithy();
		SmithyMall[] smithyMallArr = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

		// 紫装精华数量
		int equipEssencepCost = roleRt.getItemControler().getItemCountInPackage("equip_essenceP");

		// 兑换商品是否存在
		boolean checkStorId = false;

		for (int i = 0, j = smithyMallArr.length; i < j; i++) {
			SmithyMall smithyMall = smithyMallArr[i];

			if (smithyMall.id == storeId && smithyMall.num > 0) {
				checkStorId = true;

				// 0:必须消耗竞技币,1:大于等于该vip等级
				if (this.roleRt.getVipLevel() >= smithyMall.coinType) {
					if (equipEssencepCost >= smithyMall.price) {
						// 扣除竞技币 和 得到物品
						roleRt.getItemControler().changeItemByTemplateCode("equip_essenceP", -smithyMall.price);
						this.roleRt.getItemControler().changeItemByTemplateCode(smithyMall.itemId, smithyMall.num);

						smithyMall.num = 0;
						smithyMall.flag = 0;

					} else {
						throw new NoteException(Messages.getString("ArenaRankControler.50")); //$NON-NLS-1$
					}
				} else {
					throw new NoteException(Messages.getString("ArenaRankControler.51")); //$NON-NLS-1$
				}

				smithyMallArr[i] = smithyMall;

				// 添加置换 事件
				eventMallExchange.onExchange(smithyMall.itemId, smithyMall.price);

				break;
			}
		}

		// 兑换商品是否存在
		if (checkStorId) {
			smity.setExchangeItemStr(TextUtil.GSON.toJson(smithyMallArr));

			SmithyMallSel smithyMallSel = new SmithyMallSel();
			smithyMallSel.exchangeRefreshNum = smity.getExchangeRefreshNum();
			smithyMallSel.word = XsgSmithyManager.getInstance().generateRandomWord();
			smithyMallSel.SmithyMallList = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

			return smithyMallSel;
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.52")); //$NON-NLS-1$
		}

	}

	/**
	 * 铁匠铺蓝装兑换
	 * 
	 * @param storeId
	 * @return
	 * @throws NoteException
	 */
	@Override
	public SmithyMallSel exchangeBlueItem(int storeId) throws NoteException {
		RoleBlueSmithy smity = roleDb.getBlueSmithy();
		SmithyMall[] smithyMallArr = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

		// 蓝装装精华数量
		int equipEssencepCost = roleRt.getItemControler().getItemCountInPackage("equip_essenceB");

		// 兑换商品是否存在
		boolean checkStorId = false;

		for (int i = 0, j = smithyMallArr.length; i < j; i++) {
			SmithyMall smithyMall = smithyMallArr[i];

			if (smithyMall.id == storeId && smithyMall.num > 0) {
				checkStorId = true;

				// 0:必须消耗竞技币,1:大于等于该vip等级
				if (this.roleRt.getVipLevel() >= smithyMall.coinType) {
					if (equipEssencepCost >= smithyMall.price) {
						// 扣除竞技币 和 得到物品
						roleRt.getItemControler().changeItemByTemplateCode("equip_essenceB", -smithyMall.price);
						this.roleRt.getItemControler().changeItemByTemplateCode(smithyMall.itemId, smithyMall.num);

						smithyMall.num = 0;
						smithyMall.flag = 0;

					} else {
						throw new NoteException(Messages.getString("ArenaRankControler.50")); //$NON-NLS-1$
					}
				} else {
					throw new NoteException(Messages.getString("ArenaRankControler.51")); //$NON-NLS-1$
				}

				smithyMallArr[i] = smithyMall;

				// 添加置换 事件
				eventBlueMallExchange.onExchange(smithyMall.itemId, smithyMall.price);

				break;
			}
		}

		// 兑换商品是否存在
		if (checkStorId) {
			smity.setExchangeItemStr(TextUtil.GSON.toJson(smithyMallArr));

			SmithyMallSel smithyMallSel = new SmithyMallSel();
			smithyMallSel.exchangeRefreshNum = smity.getExchangeRefreshNum();
			smithyMallSel.word = XsgSmithyManager.getInstance().generateBlueRandomWord();
			smithyMallSel.SmithyMallList = TextUtil.GSON.fromJson(smity.getExchangeItemStr(), SmithyMall[].class);

			return smithyMallSel;
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.52")); //$NON-NLS-1$
		}
	}

	/**
	 * 根据概率，计算商城显示物品
	 * 
	 * @param mallNum
	 * @param storeMap
	 * @return
	 */
	private List<SmithyMallStoreT> calcRandPro(int mallNum, Map<Integer, SmithyMallStoreT> storeMap) {
		List<SmithyMallStoreT> resStoreList = new ArrayList<SmithyMallStoreT>(mallNum);

		// 合并概率
		List<Integer> proList = new ArrayList<Integer>(storeMap.size());
		Map<Integer, Integer> proMap = new HashMap<Integer, Integer>(storeMap.size());
		for (SmithyMallStoreT mallStoreT : storeMap.values()) {
			proList.add(mallStoreT.Pro);
			proMap.put(proList.size(), mallStoreT.ID);
		}

		// 概率计算
		for (int i = 0; i < mallNum; i++) {
			int index = NumberUtil.randRound(proList);
			resStoreList.add(storeMap.get(proMap.get(index)));
			proList.set(index, 0);
		}

		return resStoreList;
	}

	@Override
	public Preview[] getPreview() throws NoteException {

		Map<Integer, SmithyMallStoreT> storeMap = XsgSmithyManager.getInstance().getMallStoreMap2();
		List<SmithyMallStoreT> values = new ArrayList<SmithyMallStoreT>(storeMap.values());
		Map<Integer,List<String>> map = new HashMap<Integer,List<String>>();
		//道具类型  0-武器  1-防具  2-饰品  3-宝物  4-坐骑
		for (SmithyMallStoreT itemT : values) {
			if(TextUtil.isNotBlank(itemT.ItemID)){
				int itemType = this.getItemType(itemT.ItemID);
				if(itemType!=5){
					List<String> ids = map.get(itemType);
					if(ids == null){
						map.put(itemType, new ArrayList<String>());
					}else if(!ids.contains(itemT.ItemID)){
						ids.add(itemT.ItemID);
					}
				}
			}
		}
		List<Preview> list = new ArrayList<Preview>();
		
		for(Entry<Integer, List<String>> en : map.entrySet()){
			int type = en.getKey();
			List<String> ids = en.getValue();
			//防具的排序规则需要额外处理
			if(type!=1 && type != 5 && !CollectionUtils.isEmpty(ids)){
				Collections.sort(ids, new Comparator<String>() {
					
					@Override
					public int compare(String o1, String o2) {
						Integer v1 = Integer.valueOf(o1.substring(2));
						Integer v2 = Integer.valueOf(o2.substring(2));
						
						return v2-v1;
					}
					
				});
				list.add(new Preview(type,ids.toArray(new String[0])));
			}else if(type ==1 && !CollectionUtils.isEmpty(ids)){
				List<String> defenseIds = sortDefenseIds(ids);
				list.add(new Preview(type,defenseIds.toArray(new String[0])));
			}
		}
		return list.toArray(new Preview[0]);
	}
	
	/**
	 * 对防具排序 先橙装再紫装 然后按照防具类别分组
	 * @param ids 防具Id集合
	 * @return
	 */
	private List<String> sortDefenseIds(List<String> ids) {
		
		List<String> orangeIds = new ArrayList<>();
		List<String> purpleIds = new ArrayList<>();
		List<String> result = new ArrayList<>();
		for (String id : ids) {
			String charAt = id.charAt(2)+"";
			if(charAt.equals("4")){//橙装
				orangeIds.add(id);
			}else{
				purpleIds.add(id);
			}
		}
		
		if(!CollectionUtils.isEmpty(orangeIds)){
			List<String> list1 = getSortedList(orangeIds);
			result.addAll(list1);
		}
		
		if(!CollectionUtils.isEmpty(purpleIds)){
			List<String> list2 = getSortedList(purpleIds);
			result.addAll(list2);
		}
		return result;
	}

	private List<String> getSortedList(List<String> ids) {
		List<String> hmList = new ArrayList<String>();
		List<String> amList = new ArrayList<String>();
		List<String> btList = new ArrayList<String>();
		
		for (String id : ids) {
			if(id.startsWith("hm")){
				hmList.add(id);
			}
			if(id.startsWith("am")){
				amList.add(id);
			}
			if(id.startsWith("bt")){
				btList.add(id);
			}
		}
		
		List<String> result = new ArrayList<String>();
		sortList(hmList,result);
		sortList(amList,result);
		sortList(btList,result);
		
		return result;
	}

	private void sortList(List<String> hmList, List<String> result) {
		if(!CollectionUtils.isEmpty(hmList)){
			Collections.sort(hmList, new Comparator<String>() {
				
				@Override
				public int compare(String o1, String o2) {
					Integer v1 = Integer.valueOf(o1.substring(2));
					Integer v2 = Integer.valueOf(o2.substring(2));
					
					return v2-v1;
				}
				
			});
			result.addAll(hmList);
		}
	}

	/**
	 * 根据ItemId获取道具类型
	 * @param item 道具id 
	 * @return 道具类型  0-武器  1-防具  2-饰品  3-宝物  4-坐骑
	 */
	private int getItemType(String item){
		String prefix = item.substring(0,2);
		
		switch (prefix) {
			case "wp":
				return 0;
			case "hm":
				return 1;
			case "am":
				return 1;
			case "bt":
				return 1;
			case "or":
				return 2;
			case "tr":
				return 3;
			case "rd":
				return 4;
			default:
				return 5;
		}
	}
	
}
