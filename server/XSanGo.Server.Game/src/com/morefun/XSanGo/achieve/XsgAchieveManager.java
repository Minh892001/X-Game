package com.morefun.XSanGo.achieve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.IntString;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.AchieveFirstNotify;
import com.morefun.XSanGo.db.game.FirstServerNotifyDao;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 
 * @author sunjie.sun
 *
 */
public class XsgAchieveManager {

	private static XsgAchieveManager instance = new XsgAchieveManager();

	public static XsgAchieveManager getInstance() {
		return instance;
	}

	/*
	 * 成就类型
	 */
	public enum AchieveTemplate {
		KingLv, PVEAll, PVP, PVE1, PVE2, XunbaoCountTime, StoneLvl4Counts, StoneLvl5Counts, StoneLvl6Counts, Guanjie, GodNum, lineCounts, StoneCounts, ConWinDays, GuildGiftNums, SPAwardTimes, FriendNums, FriendPoint, AllAchievement, GoldNum, BeifaStarsDays,BeifaStarsDays50, BeifaStarAll, Power, FiveStarPassNum, XinshouSweepNum, GaoshouSweepNum, GodSweepNum, XinshouStarNum, GaoshouStarNum, GodStarNum, InfernalNum, SweepXinshouNum, SweepGaoshouNum, LawQlvlNumB, LawQlvlNumP, LawLvlBlueNum, LawLvlPurpleNum, HeroNum, PartnarIsYuanfen, BattleHeroStar, HeroStar, HeroStar5, Partnar, BreakThrough, LearnedPoint, PracticeNum, PracticeLv, PracticeLv10, PracticeTop, HeroAll, EquipTypes, EquipLvlNum, EquipLvlNumFull, EquipQLvlNum, EquipStarNum, EquipAll, FirstWinDays, FailedTimes, ArenaAll, JoinQunXiong, QunxiongAll, BeifaStars, PassNums, LoserNums, ChuzhengAll, ShiKongAll, ConLuanshiDays, BuyPlusDays, TotalHurt, LuanshiAll, SendJunling, QieChuoCount, FriendAll, GuildFightDays, GuildDHurtCount, DonateJiangzhang, GuildAll, AuctionSuccessNums, AuctionNums, StarHeroDays, BussinessManDays, OnlineGiftNums, loginDayCounts, LuckManNum, CompleteDayTaskNum, CompleteMainTaskNum, RecAuctionByTaskDays, TaskAll, BreakThrough1, BreakThrough2, BreakThrough3, BreakThrough4, BreakThrough5, PracticeTop8;

		public static AchieveTemplate getAchieveTemplate(String type) {
			return valueOf(type);
		}

	}

	/**
	 * 全部成就总数
	 */
	private int achieveCount;
	
	/**
	 * 成就目录信息<系统ID,<类型,vo>>
	 * */
	private Map<Integer, Map<String, AchieveCataLogT>> achieveCataLogMap = new HashMap<Integer, Map<String, AchieveCataLogT>>();
	/**
	 * 成就信息<类型,<ID,VO>>
	 * */
	private Map<String, TreeMap<Integer, AchieveT>> achieveInfoMap = new HashMap<String, TreeMap<Integer, AchieveT>>();

	/**
	 * 成就信息<类型,<list>>
	 * */
	private Map<String, List<List<AchieveT>>> splitTypeMap = new HashMap<String, List<List<AchieveT>>>();

	/**
	 * 成就基本信息<ID,VO>
	 * */
	private Map<Integer, AchieveT> achieveMap = new HashMap<Integer, AchieveT>();
	/**
	 * 功能ID对应总成就数
	 */
	private Map<Integer, Integer> functionIdOfCountMap = new HashMap<Integer, Integer>();
	/**
	 * 类型嵌套成就PVP PVE1之类
	 */
	private Map<String, String> specialTypeMap = new HashMap<String, String>();
	/**
	 * 功能ID对应特殊类型类型
	 */
	private Map<Integer, String> functionIdOfSpecialTypeMap = new HashMap<Integer, String>();
	/**
	 * 功能ID对应特殊类型成就ID
	 */
	private Map<Integer, Integer> functionIdOfSpecialIdMap = new HashMap<Integer, Integer>();
	/**
	 * 成就进度奖励配置
	 */
	private TreeMap<Integer,AchieveProAwardT> proConfigMap = new TreeMap<Integer,AchieveProAwardT>();
	/**
	 * 成就全服公告信息
	 */
	private Map<Integer,AchieveFirstNotify> notifyMap = new HashMap<Integer,AchieveFirstNotify>();

	private XsgAchieveManager() {
		// 加载成就目录
		loadAchieveCataLogScript();
		// 加载成就
		loadAchieveScript();
		loadAchieveProgressAward();
		initBseDate();
		//加载全服首次公告
		initServerFirstNotify();
	}

	/**
	 * 加载全服首次公告
	 */
	private void initServerFirstNotify()
	{
		FirstServerNotifyDao firstServerNotifyDao = FirstServerNotifyDao.getFromApplicationContext(ServerLancher.getAc());
		notifyMap = (Map<Integer,AchieveFirstNotify>) firstServerNotifyDao.findAll();
	}
	
	/**
	 * 异步保存到数据库
	 * 
	 * @param firstServerNotifyDao
	 *            积分排名对象
	 */
	public void save2DbAsync(final AchieveFirstNotify achieveFirstNotify) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FirstServerNotifyDao firstServerNotifyDao = FirstServerNotifyDao.getFromApplicationContext(ServerLancher.getAc());
				firstServerNotifyDao.save(achieveFirstNotify);
			}
		});
	}
	
	/**
	 * 加载成就进度奖励配置
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	private void loadAchieveProgressAward()
	{
		this.proConfigMap.clear();
		List<AchieveProAwardT> list = ExcelParser.parse("进度奖励", AchieveProAwardT.class);
		for(AchieveProAwardT t:list)
		{
			if(!TextUtil.isBlank(t.item1) && t.itemNum1>0)
			{
				t.itemMap.put(t.item1, t.itemNum1);
			}
			if(!TextUtil.isBlank(t.item2) && t.itemNum2>0)
			{
				t.itemMap.put(t.item2, t.itemNum2);
			}
			if(!TextUtil.isBlank(t.item3) && t.itemNum3>0)
			{
				t.itemMap.put(t.item3, t.itemNum3);
			}
			if(!TextUtil.isBlank(t.item4) && t.itemNum4>0)
			{
				t.itemMap.put(t.item4, t.itemNum4);
			}
			if(!TextUtil.isBlank(t.item5) && t.itemNum5>0)
			{
				t.itemMap.put(t.item5, t.itemNum5);
			}
			proConfigMap.put(t.progress, t);
		}
	}
	
	public IntString[] convertMap2StringIntArray(Map<String,Integer> map)
	{
		IntString[] items = new IntString[map.size()];
		int index = 0;
		for(String item:map.keySet())
		{
			IntString is = new IntString(map.get(item),item);
			items[index] = is;
			index+=1;
		}
		return items;
	}
	/**
	 * 创建成就的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public AchieveControler createAchieveControler(IRole roleRt, Role roleDB) {
		return new AchieveControler(roleRt, roleDB);
	}

	/**
	 * 基本信息的统计
	 */
	private void initBseDate() {
		this.functionIdOfCountMap.clear();
		this.functionIdOfSpecialTypeMap.clear();
		this.functionIdOfSpecialIdMap.clear();
		Map<Integer, Map<String, AchieveCataLogT>> sysMap = achieveCataLogMap;
		for (int functionId : sysMap.keySet()) {
			int count = 0;
			Map<String, AchieveCataLogT> typeMap = sysMap.get(functionId);
			int specialType4Funct = 0;
			for (String type : typeMap.keySet()) {
				TreeMap<Integer, AchieveT> detailMap = achieveInfoMap.get(type);
				if (null == detailMap)
					continue;
				AchieveCataLogT ct = typeMap.get(type);
				if (ct.isSpecial == 1) {
					specialType4Funct += 1;
					if (detailMap.size() > 1)
						LoggerFactory.getLogger(getClass()).warn("特殊类型成就：" + type + "  只能有一个条目");
					functionIdOfSpecialTypeMap.put(functionId, type);
					functionIdOfSpecialIdMap.put(functionId, detailMap.firstKey());
				}
				// 成就 前后置关系判断
				for (AchieveT t : detailMap.values()) {
					if (t.frontId != 0) {
						if (null == achieveMap.get(t.frontId))
							LoggerFactory.getLogger(getClass()).warn(t.id + "  成就：前置成就不存在" + "无效ID：" + t.frontId);
					}
					if (t.nextId != 0) {
						if (null == achieveMap.get(t.nextId))
							LoggerFactory.getLogger(getClass()).warn(t.id + "  成就：后置成就不存在" + "无效ID：" + t.nextId);
					}
					if (type.equals(AchieveTemplate.PVP.name()) || type.equals(AchieveTemplate.PVE1.name())
							|| type.equals(AchieveTemplate.PVE2.name())) {
						if (!TextUtil.isBlank(t.condition)) {
							String[] types = t.condition.split(",");
							for (int i = 0; i < types.length; i++) {
								String sp = types[i];
								if (null != specialTypeMap.get(sp)) {
									LoggerFactory.getLogger(getClass()).warn(t.id + "  嵌套成就类型 不可出现在多个条目");
								}
								// 判断 SP 必须是特殊成就类型
								specialTypeMap.put(sp, type);
							}
						}

					}
				}
				count += detailMap.size();
			}
			if (specialType4Funct > 1)
				LoggerFactory.getLogger(getClass()).warn("一个FUNCTION功能下只能有一个特殊成就类型");
			functionIdOfCountMap.put(functionId, count);
		}
		for (int functionId : sysMap.keySet()) {
			Map<String, AchieveCataLogT> typeMap = sysMap.get(functionId);
			if (typeMap == null || typeMap.size() == 0)
				continue;
			for (AchieveCataLogT t : typeMap.values()) {
				if (specialTypeMap.containsKey(t.type)) {
					if (t.isSpecial != 1)
						LoggerFactory.getLogger(getClass()).warn(t.type + "  嵌套成就类型 必须为特殊类型");
				}
			}
		}
	}

	/**
	 * 加载成就目录
	 */
	public void loadAchieveCataLogScript() {
		this.achieveCataLogMap.clear();
		List<AchieveCataLogT> achieveCataLogList = ExcelParser.parse("分类列表", AchieveCataLogT.class);
		for (AchieveCataLogT a : achieveCataLogList) {
			Map<String, AchieveCataLogT> map = achieveCataLogMap.get(a.functionId);
			if (null == map) {
				map = new HashMap<String, AchieveCataLogT>();
				map.put(a.type, a);
				achieveCataLogMap.put(a.functionId, map);
			} else {
				map.put(a.type, a);
			}
		}
	}

	/**
	 * 加载成就列表
	 */
	public void loadAchieveScript() {
		this.achieveInfoMap.clear();
		this.splitTypeMap.clear();
		List<AchieveT> achieveTList = ExcelParser.parse("成就列表", AchieveT.class);
		for (AchieveT a : achieveTList) {
			if (a.isOpen == 0)
				continue;
			if(AchieveTemplate.getAchieveTemplate(a.type)==null || TextUtil.isBlank(AchieveTemplate.getAchieveTemplate(a.type).name()))
			{
				LoggerFactory.getLogger(getClass()).warn("无效的成就类型:  "+a.type);
			}
			TreeMap<Integer, AchieveT> map = achieveInfoMap.get(a.type);
			if (null == map) {
				map = new TreeMap<Integer, AchieveT>();
				map.put(a.id, a);
				achieveInfoMap.put(a.type, map);
			} else {
				map.put(a.id, a);
			}
			achieveMap.put(a.id, a);

			// 加载界面需要的 一个类型下的分段成就显示
			List<List<AchieveT>> list = splitTypeMap.get(a.type);
			if (list == null) {
				list = new ArrayList<List<AchieveT>>();
				List<AchieveT> detailList = new ArrayList<AchieveT>();
				detailList.add(a);
				list.add(detailList);
				splitTypeMap.put(a.type, list);
			} else {
				if (a.frontId == 0) {
					List<AchieveT> detailList = new ArrayList<AchieveT>();
					detailList.add(a);
					list.add(detailList);
				} else {
					list.get(list.size() - 1).add(a);
				}
			}
		}

		// 成就总数
		achieveCount = achieveMap.size();
	}

	/**
	 * 根据成就ID 获得对应的功能ID
	 * 
	 * @param achieveId
	 * @return
	 */
	public int getFunctionByID(int achieveId) {
		AchieveT config = achieveMap.get(achieveId);
		if (null == config)
			return -1;
		return config.functionId;
	}

	/**
	 * 根据功能ID 获得特殊成就类型
	 * 
	 * @param function
	 * @return
	 */
	public String getSpecialType4FId(int function) {
		return functionIdOfSpecialTypeMap.get(function) == null ? null : functionIdOfSpecialTypeMap.get(function);
	}

	/**
	 * 根据功能ID 获得特殊成就Id
	 * 
	 * @param function
	 * @return
	 */
	public int getSpecialType4AId(int function) {
		return functionIdOfSpecialIdMap.get(function) == null ? 0 : functionIdOfSpecialIdMap.get(function);
	}

	public Map<Integer, Map<String, AchieveCataLogT>> getAchieveCataLogMap() {
		return achieveCataLogMap;
	}

	public void setAchieveCataLogMap(Map<Integer, Map<String, AchieveCataLogT>> achieveCataLogMap) {
		this.achieveCataLogMap = achieveCataLogMap;
	}

	public Map<String, TreeMap<Integer, AchieveT>> getAchieveInfoMap() {
		return achieveInfoMap;
	}

	public void setAchieveInfoMap(Map<String, TreeMap<Integer, AchieveT>> achieveInfoMap) {
		this.achieveInfoMap = achieveInfoMap;
	}

	public Map<Integer, AchieveT> getAchieveMap() {
		return achieveMap;
	}

	public void setAchieveMap(Map<Integer, AchieveT> achieveMap) {
		this.achieveMap = achieveMap;
	}

	public int getAchieveCount() {
		return achieveCount;
	}

	public void setAchieveCount(int achieveCount) {
		this.achieveCount = achieveCount;
	}

	public Map<Integer, Integer> getFunctionIdOfCountMap() {
		return functionIdOfCountMap;
	}

	public void setFunctionIdOfCountMap(Map<Integer, Integer> functionIdOfCountMap) {
		this.functionIdOfCountMap = functionIdOfCountMap;
	}

	public Map<Integer, String> getFunctionIdOfSpecialTypeMap() {
		return functionIdOfSpecialTypeMap;
	}

	public void setFunctionIdOfSpecialTypeMap(Map<Integer, String> functionIdOfSpecialTypeMap) {
		this.functionIdOfSpecialTypeMap = functionIdOfSpecialTypeMap;
	}

	public Map<Integer, Integer> getFunctionIdOfSpecialIdMap() {
		return functionIdOfSpecialIdMap;
	}

	public void setFunctionIdOfSpecialIdMap(Map<Integer, Integer> functionIdOfSpecialIdMap) {
		this.functionIdOfSpecialIdMap = functionIdOfSpecialIdMap;
	}

	public Map<String, String> getSpecialTypeMap() {
		return specialTypeMap;
	}

	public void setSpecialTypeMap(Map<String, String> specialTypeMap) {
		this.specialTypeMap = specialTypeMap;
	}

	public Map<String, List<List<AchieveT>>> getSplitTypeMap() {
		return splitTypeMap;
	}

	public void setSplitTypeMap(Map<String, List<List<AchieveT>>> splitTypeMap) {
		this.splitTypeMap = splitTypeMap;
	}

	public TreeMap<Integer, AchieveProAwardT> getProConfigMap() {
		return proConfigMap;
	}

	public Map<Integer, AchieveFirstNotify> getNotifyMap() {
		return notifyMap;
	}

	public void setNotifyMap(Map<Integer, AchieveFirstNotify> notifyMap) {
		this.notifyMap = notifyMap;
	}

}
