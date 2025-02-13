package com.morefun.XSanGo.treasure;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.RecommendHero;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 寻宝管理类
 * 
 * @author lixiongming
 *
 */
public class XsgTreasureManage {
	private static XsgTreasureManage instance = new XsgTreasureManage();
	/**
	 * 寻宝配置
	 */
	private TreasureConfT treasureConfT;

	/**
	 * 寻宝推荐武将模版
	 */
	private List<RecommendHeroT> heroTs;

	/**
	 * 队伍开放配置
	 */
	private List<TreasureGroupT> groupTs;

	/**
	 * 寻宝不同VIP的奖励TC
	 */
	private List<TreasureVipAwardT> awardTs;

	/**
	 * 寻宝队伍活动奖励 key-队伍编号
	 */
	private Map<Integer, TreasureActivityT> activityTs = new HashMap<Integer, TreasureActivityT>();

	/**
	 * 寻宝阶段
	 */
	private List<TreasureStageT> stageTs = new ArrayList<TreasureStageT>();

	/**
	 * 寻宝事件
	 */
	private Map<Integer, TreasureEventT> eventMap = new HashMap<Integer, TreasureEventT>();

	/**
	 * 寻宝矿难参数
	 */
	private List<TreasureAccidentT> accidentTs = new ArrayList<TreasureAccidentT>();

	/**
	 * 寻宝矿难等级参数
	 */
	private Map<Integer, TreasureRescueT> rescueMap = new HashMap<Integer, TreasureRescueT>();
	
	/**
	 * 最大求援次数
	 */
	public static final int MAX_RECOURSE_NUM = 10;  

	public static XsgTreasureManage getInstance() {
		return instance;
	}

	public XsgTreasureManage() {
		treasureConfT = ExcelParser.parse(TreasureConfT.class).get(0);
		heroTs = ExcelParser.parse(RecommendHeroT.class);
		groupTs = ExcelParser.parse(TreasureGroupT.class);
		awardTs = ExcelParser.parse(TreasureVipAwardT.class);
		List<TreasureActivityT> list = ExcelParser.parse(TreasureActivityT.class);
		for (TreasureActivityT a : list) {
			activityTs.put(a.groupNum, a);
		}

		stageTs = ExcelParser.parse(TreasureStageT.class);

		List<TreasureEventT> eventTs = ExcelParser.parse(TreasureEventT.class);
		for (TreasureEventT e : eventTs) {
			eventMap.put(e.id, e);
		}

		accidentTs = ExcelParser.parse(TreasureAccidentT.class);

		List<TreasureRescueT> rescueTs = ExcelParser.parse(TreasureRescueT.class);
		for (TreasureRescueT t : rescueTs) {
			rescueMap.put(t.level, t);
		}
	}

	public ITreasureControler createTreasureControler(IRole rt, Role db) {
		return new TreasureControler(rt, db);
	}

	/**
	 * 获取当前等级可开启的寻宝队伍
	 * 
	 * @param vipLevel
	 * @return
	 */
	public List<TreasureGroupT> getOpenGroup(int level, int vipLevel) {
		List<TreasureGroupT> open = new ArrayList<TreasureGroupT>();
		for (TreasureGroupT v : groupTs) {
			if (level >= v.openLevel && vipLevel >= v.openVipLevel) {
				open.add(v);
			}
		}
		return open;
	}

	/**
	 * 获取当前VIP等级未开启的寻宝队伍
	 * 
	 * @param vipLevel
	 * @return
	 */
	public List<TreasureGroupT> getNotOpenGroup(int level, int vipLevel) {
		List<TreasureGroupT> notOpen = new ArrayList<TreasureGroupT>();
		for (TreasureGroupT v : groupTs) {
			if (level < v.openLevel || vipLevel < v.openVipLevel) {
				notOpen.add(v);
			}
		}
		return notOpen;
	}

	/**
	 * 最多开放队伍数量
	 * 
	 * @return
	 */
	public int getMaxOpenGroup() {
		return groupTs.size();
	}

	public TreasureConfT getTreasureConfT() {
		return this.treasureConfT;
	}

	/**
	 * 随机获取3个推荐武将
	 * 
	 * @param existHero
	 *            去重武将ID
	 * @return
	 */
	public RecommendHero[] randomRecommend(List<Integer> existHero, int index) {
		List<RecommendHeroT> refreshs = new ArrayList<RecommendHeroT>();
		for (RecommendHeroT ht : this.heroTs) {
			if (ht.index == index) {
				refreshs.add(ht);
			}
		}
		if (refreshs.isEmpty()) {
			return new RecommendHero[0];
		}
		List<RecommendHero> heros = new ArrayList<RecommendHero>();
		int count = 0;// 循环次数 避免死循环
		while (heros.size() < 3 && count < 10000) {
			count++;
			RecommendHeroT heroT = refreshs.get(NumberUtil.random(refreshs.size()));
			if (existHero.contains(heroT.id)) {
				continue;
			}
			List<String> levels = TextUtil.stringToList(heroT.levels);
			List<String> stars = TextUtil.stringToList(heroT.stars);
			int level = Integer.parseInt(levels.get(NumberUtil.random(levels.size())));
			int star = Integer.parseInt(stars.get(NumberUtil.random(stars.size())));
			heros.add(new RecommendHero(heroT.id, heroT.name, level, star, 0, XsgHeroManager.getInstance()
					.getShowColor(heroT.id, 0)));
			existHero.add(heroT.id);
		}
		return heros.toArray(new RecommendHero[0]);
	}

	// public RecommendHero[] randomRecommend(List<Integer> existHero, int
	// index) {
	// List<RecommendHeroT> refreshs = new ArrayList<RecommendHeroT>();
	// for (RecommendHeroT ht : this.heroTs) {
	// if (ht.index == index) {
	// refreshs.add(ht);
	// }
	// }
	// List<RecommendHero> heros = new ArrayList<RecommendHero>();
	// while (heros.size() < 3) {
	// RecommendHeroT heroT = heroTs.get(NumberUtil.random(heroTs.size()));
	// if (existHero.contains(heroT.id)) {
	// continue;
	// }
	// List<String> levels = TextUtil.stringToList(heroT.levels);
	// List<String> stars = TextUtil.stringToList(heroT.stars);
	// int level = Integer.parseInt(levels.get(NumberUtil.random(levels
	// .size())));
	// int star = Integer.parseInt(stars.get(NumberUtil.random(stars
	// .size())));
	// heros.add(new RecommendHero(heroT.id, heroT.name, level, star, 0,
	// XsgHeroManager.getInstance().getShowColor(heroT.id, 0)));
	// existHero.add(heroT.id);
	// }
	// return heros.toArray(new RecommendHero[0]);
	// }

	/**
	 * 获取VIP等级对应的TC奖励
	 * 
	 * @param vipLevel
	 * @return
	 */
	public TreasureVipAwardT getVipTc(int vipLevel) {
		for (TreasureVipAwardT t : awardTs) {
			if (vipLevel >= t.startVip && vipLevel <= t.endVip) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 获取指定编号的武将
	 * 
	 * @param heroId
	 * @param index
	 * @return
	 */
	public RecommendHeroT getByHeroId(int heroId, int index) {
		for (RecommendHeroT ht : this.heroTs) {
			if (ht.index == index && ht.id == heroId) {
				return ht;
			}
		}
		return null;
	}

	/**
	 * 获取指定日期指定队伍活动奖励TC
	 * 
	 * @param groupNum
	 * @param date
	 * @return tc
	 */
	public String getActivityTc(int groupNum, Date date) {
		TreasureActivityT activityT = activityTs.get(groupNum);
		if (activityT != null && DateUtil.isBetween(date, activityT.startDate, activityT.endDate)) {
			return activityT.tc;
		}
		return null;
	}

	/**
	 * 获取寻宝阶段
	 * 
	 * @param minute
	 * @return
	 */
	public TreasureStageT getStageTByTime(int minute) {
		for (TreasureStageT t : this.stageTs) {
			if (minute < t.minute) {
				return t;
			}
			minute -= t.minute;
		}
		return stageTs.get(stageTs.size() - 1);
	}

	/**
	 * 获取寻宝阶段进度
	 * 
	 * @param passTime
	 * @return
	 */
	public int getProgressByTime(long passTime) {
		for (TreasureStageT t : this.stageTs) {
			if (passTime / 60000 < t.minute) {
				return (int) (passTime * 100 / (t.minute * 60000));
			}
			passTime -= t.minute * 60000;
		}
		return 100;
	}

	/**
	 * 获取寻宝阶段剩余分钟
	 * 
	 * @param passTime
	 * @return
	 */
	public int getRemainMinuteByTime(long passTime) {
		for (TreasureStageT t : this.stageTs) {
			if (passTime / 60000 < t.minute) {
				return (int) (t.minute - (passTime / 60000));
			}
			passTime -= t.minute * 60000;
		}
		return 0;
	}

	/**
	 * 获取总阶段数量
	 * 
	 * @return
	 */
	public int getMaxStage() {
		return stageTs.size();
	}

	/**
	 * 根据阶段获取阶段配置
	 * 
	 * @param stage
	 * @return
	 */
	public TreasureStageT getStageT(int stage) {
		for (TreasureStageT t : this.stageTs) {
			if (t.stage == stage) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 随机一个寻宝事件
	 * 
	 * @return
	 */
	public TreasureEventT randomEventT() {
		int max = 0;
		for (TreasureEventT e : this.eventMap.values()) {
			max += e.weight;
		}

		int random = NumberUtil.random(max);
		int temp = 0;
		for (TreasureEventT e : this.eventMap.values()) {
			temp += e.weight;
			if (random < temp) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 随机一个矿难级别
	 * 
	 * @return
	 */
	public TreasureRescueT randomRescueT() {
		int max = 0;
		for (TreasureRescueT e : this.rescueMap.values()) {
			max += e.weight;
		}

		int random = NumberUtil.random(max);
		int temp = 0;
		for (TreasureRescueT e : this.rescueMap.values()) {
			temp += e.weight;
			if (random < temp) {
				return e;
			}
		}
		return null;
	}

	public TreasureEventT getEventById(int id) {
		return eventMap.get(id);
	}

	public TreasureAccidentT getTreasureAccidentByLevel(int level) {
		for (TreasureAccidentT t : this.accidentTs) {
			if (level >= t.beginLevel && level <= t.endLevel) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 根据矿难等级获取配置模板
	 * 
	 * @param level
	 * @return
	 */
	public TreasureRescueT getRescueByLevel(int level) {
		return rescueMap.get(level);
	}
}
