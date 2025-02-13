package com.morefun.XSanGo.colorfulEgg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;

public class XsgColorfullEggManager {

	private static XsgColorfullEggManager instance = new XsgColorfullEggManager();
	/** 活动基础开关 */
	private EggBasisConfT eggConf;
	/** 活动时间 */
	private List<EggPeriodsT> periodList;
	/** 奖励控制 */
	private List<EggRewardConfT> rewardConf;
	/** 奖池 type-List<EggRewardsT> */
	private Map<Integer, List<EggRewardsT>> rewardMap = new HashMap<Integer, List<EggRewardsT>>();
	/** 奖池 id-EggRewardsT */
	private Map<Integer, EggRewardsT> rewardPool = new HashMap<Integer, EggRewardsT>();

	/** 彩蛋数量 */
	public static final int EGG_NUM = 3;

	public static XsgColorfullEggManager getInstance() {
		return instance;
	}

	private XsgColorfullEggManager() {
		loadScript();
	}

	private void loadScript() {

		List<EggBasisConfT> tempList = ExcelParser.parse(EggBasisConfT.class);
		if (tempList != null) {
			eggConf = tempList.get(0);
		}

		periodList = ExcelParser.parse(EggPeriodsT.class);

		rewardConf = ExcelParser.parse(EggRewardConfT.class);

		List<EggRewardsT> rewards = ExcelParser.parse(EggRewardsT.class);
		for (EggRewardsT eggRewardsT : rewards) {
			rewardPool.put(eggRewardsT.id, eggRewardsT);
			List<EggRewardsT> list = rewardMap.get(eggRewardsT.type);
			if (list == null) {
				list = new ArrayList<EggRewardsT>();
			}
			list.add(eggRewardsT);
			rewardMap.put(eggRewardsT.type, list);
		}

	}

	public IColorfullEggController createEggController(IRole roleRt, Role roleDB) {
		return new ColorfullEggController(roleRt, roleDB);
	}

	public EggBasisConfT getEggConf() {
		return eggConf;
	}

	public List<EggPeriodsT> getPeriodList() {
		return periodList;
	}

	public List<EggRewardConfT> getRewardConf() {
		return rewardConf;
	}

	public Map<Integer, List<EggRewardsT>> getRewardMap() {
		return rewardMap;
	}

	/** 活动是否开放 */
	public boolean isOpen(int level) {
		for (EggPeriodsT timeT : periodList) {
			if (DateUtil.isBetween(timeT.beginTime, timeT.endTime) && level >= eggConf.limitLevel) {
				return true;
			}
		}
		return false;
	}

	/** 活动是否开放 */
	public boolean isOpen() {
		for (EggPeriodsT timeT : periodList) {
			if (timeT.beginTime != null && timeT.endTime != null
					&& DateUtil.isBetween(timeT.beginTime, timeT.endTime)) {
				return true;
			}
		}
		return false;
	}

	/** 指定时间与当前时间是否在活动配置的有效時間段里 */
	public boolean isCurrBetween(Date date) {

		if (null == date) {
			return false;
		}

		for (EggPeriodsT timeT : periodList) {
			if (DateUtil.isBetween(timeT.beginTime, timeT.endTime)
					&& DateUtil.isBetween(date, timeT.beginTime, timeT.endTime)) {
				return true;
			}
		}
		return false;
	}

	public Map<Integer, EggRewardsT> getRewardPool() {
		return rewardPool;
	}

}
