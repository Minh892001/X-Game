/**
 * 
 */
package com.morefun.XSanGo.ladder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.XSanGo.Protocol.ActionN;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.DamageN;
import com.XSanGo.Protocol.DuelBuffView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.DuelTemplateType;
import com.XSanGo.Protocol.EffectN;
import com.XSanGo.Protocol.LadderRankListSub;
import com.XSanGo.Protocol.LoadingRankSub;
import com.XSanGo.Protocol.SceneDuelView;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.ArenaAwardLog;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleLadderDao;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 群雄争霸 全局管理
 * 
 * @author 吕明涛
 */
public class XsgLadderManager {
	private static XsgLadderManager instance = new XsgLadderManager();
	/** 基本参数 */
	private LadderInitT initT;
	/** 争霸赛等级 */
	private Map<Integer, LadderLevelT> levelMap = new HashMap<Integer, LadderLevelT>(0);
	/** 购买挑战次数 */
	private Map<Integer, LadderChallengeBuyT> challengeBuyMap = new HashMap<Integer, LadderChallengeBuyT>(0);
	/** 等级奖励 */
	private List<LadderLevelAwardT> levelAwardList;
	private HashMap<Integer, LadderLevelAwardT> levelAwardMap = new HashMap<Integer, LadderLevelAwardT>(0);
	/** 赛季排行奖励 */
	private List<LadderAwardT> awardList;
	/** 新赛季降级规则 */
	private List<LadderDegradeT> degradeList;
	/** 赛季结束日期 */
	private Date seasonEndDate;
	/** 排行榜 */
	private List<LadderRankListSub> ladderRankList = new ArrayList<LadderRankListSub>();

	/**
	 * 排名匹配模版
	 */
	private List<LadderMatchT> ladderMatchTs = new ArrayList<LadderMatchT>();

	private List<LadderLevelScoreT> ladderLevelScoreTs;

	/**
	 * 战报生成接口地址
	 */
	public String movieUrl;

	public static XsgLadderManager getInstance() {
		return instance;
	}

	/**
	 * 创建群雄争霸 的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public ILadderControler createLadderControler(IRole roleRt, Role roleDB) {
		return new LadderControler(roleRt, roleDB);
	}

	private XsgLadderManager() {
		initT = ExcelParser.parse(LadderInitT.class).get(0);

		for (LadderLevelT levelT : ExcelParser.parse(LadderLevelT.class)) {
			levelMap.put(levelT.lv, levelT);
		}

		for (LadderChallengeBuyT buyT : ExcelParser.parse(LadderChallengeBuyT.class)) {
			challengeBuyMap.put(buyT.time, buyT);
		}

		loadLadderLevelAwardScript();

		awardList = ExcelParser.parse(LadderAwardT.class);
		degradeList = ExcelParser.parse(LadderDegradeT.class);
		ladderLevelScoreTs = ExcelParser.parse(LadderLevelScoreT.class);

		RoleLadderDao ladderDAO = RoleLadderDao.getFromApplicationContext(ServerLancher.getAc());
		// 设置赛季结束时间
		calcEndDate();
		// calcSeasonDate(ladderDAO.findLadderSeanson());
		// 赛季内的排行
		// String seasonDate = DateUtil.toString(DateUtil.setMonyhDay(
		// this.initT.rewardDate, 0).getTime());
		ladderRankList = ladderDAO.findLadder(initT.rankNumAll,
				DateUtil.toString(this.seasonEndDate.getTime(), "yyyy-MM-dd"));

		// 每天检查是否发送奖励
		String[] refreshDate = initT.rewardStarTime.split(":");
		long interval = DateUtil
				.betweenTaskHourMillis(Integer.valueOf(refreshDate[0]), Integer.valueOf(refreshDate[1]));
		LogicThread.scheduleTask(new DelayedTask(interval, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				XsgLadderManager.getInstance().levelAward();
			}
		});

		movieUrl = (String) ServerLancher.getAc().getBean("FightMovieUrl");
	}

	// /** 初始化老数据积分 */
	// private void initOldDate() {
	// for (LadderRankListSub l : ladderRankList) {
	// if (l.ladderScore == 0) {
	// int score = getLevelScore(l.ladderLevel, l.ladderStar);
	// l.ladderScore = score;
	// }
	// }
	// rankSort(ladderRankList);
	// }

	/**
	 * 加载群雄争霸等级奖励脚本
	 */
	public void loadLadderLevelAwardScript() {
		levelAwardList = ExcelParser.parse(LadderLevelAwardT.class);
		levelAwardMap.clear();
		for (LadderLevelAwardT levelAwardT : levelAwardList) {
			levelAwardMap.put(levelAwardT.id, levelAwardT);
		}
		ladderMatchTs = ExcelParser.parse(LadderMatchT.class);
	}

	public LadderInitT getInitT() {
		return initT;
	}

	public LadderLevelT getLevelMap(int id) {
		return levelMap.get(id);
	}

	public LadderChallengeBuyT getChallengeBuyMap(int id) {
		return challengeBuyMap.get(id);
	}

	public List<LadderLevelAwardT> getLevelAwardList() {
		return levelAwardList;
	}

	public LadderLevelAwardT getLevelAwardMap(int id) {
		return levelAwardMap.get(id);
	}

	public List<LadderAwardT> getAwardList() {
		return awardList;
	}

	public List<LadderDegradeT> getDegradeList() {
		return degradeList;
	}

	/**
	 * 获取群雄争霸结束时间
	 * 
	 * @return
	 */
	public Date getSeasonEndDate() {
		return seasonEndDate;
	}

	/** 查询玩家的排名 */
	public int getLadderRank(String roleId) {
		int rankNum = -1;
		for (int i = 0; i < ladderRankList.size(); i++) {
			LadderRankListSub sub = ladderRankList.get(i);
			if (sub.roleId.equals(roleId)) {
				rankNum = i + 1;
				break;
			}
		}
		return rankNum;
	}

	public int getLadderRankListNum() {
		return ladderRankList.size();
	}

	public void addLadderRankList(LadderRankListSub ladderRank) {
		ladderRankList.add(ladderRank);
	}

	public LadderRankListSub getLadderRankList(int i) {
		return ladderRankList.get(i);
	}

	public void setLadderRankList(LadderRankListSub ladderRank) {
		// 排行榜数量是否达到要求数量 ，如到了数量后，和最后一名的比较
		if (this.getLadderRankListNum() < this.initT.rankNumAll
				|| ladderRank.ladderLevel >= this.getLadderRankList(this.getLadderRankListNum() - 1).ladderLevel
				|| ladderRank.ladderStar >= this.getLadderRankList(this.getLadderRankListNum() - 1).ladderStar) {
			// 删除已经存在的，加入新排行榜数据，重新排序
			this.rankRemove(ladderRank.roleId);
			ladderRankList.add(ladderRank);
			this.rankSort(this.ladderRankList);

			// 排行榜 数量显示数量超出时，删除最后一列
			if (this.getLadderRankListNum() > this.initT.rankNumAll) {
				ladderRankList.remove(this.getLadderRankListNum() - 1);
			}
		}
	}

	/**
	 * 移除已经存在的排行榜数据
	 * 
	 * @param rankList
	 * @param roleId
	 */
	private void rankRemove(String roleId) {
		for (LadderRankListSub sub : this.ladderRankList) {
			if (sub.roleId.equals(roleId)) {
				this.ladderRankList.remove(sub);
				break;
			}
		}
	}

	/**
	 * 排行榜 排序
	 * 
	 * @param rankList
	 */
	private void rankSort(List<LadderRankListSub> rankList) {
		Comparator<LadderRankListSub> comparator = new Comparator<LadderRankListSub>() {
			public int compare(LadderRankListSub s1, LadderRankListSub s2) {
				int order = s1.ladderLevel - s2.ladderLevel;
				if (order == 0) {
					order = s2.ladderStar - s1.ladderStar;
					if (order == 0) {
						order = s2.ladderScore - s1.ladderScore;
					}
					if (order == 0) {
						order = (int) (s1.rankTime - s2.rankTime);
					}
					if (order == 0) {
						order = s2.roleId.compareTo(s1.roleId);
					}
				}

//				if (order > 1) {
//					return 1;
//				} else if (order < 0) {
//					return -1;
//				} else {
//					return order;
//				}
				
				return order;
			}
		};

		Collections.sort(rankList, comparator);
	}

	/**
	 * 计算赛季结束的日期<br>
	 * 查询数据库中的最大赛季结束时间<br>
	 * 数据库不存在，根据配置文件计算<br>
	 * 当前日期 大于 配置日期，默认下一个月的日期
	 * 
	 * @return
	 */
	// private void calcSeasonDate(Date seasonDate) {
	// if (seasonDate == null) {
	// this.calcEndDate();
	// } else {
	// if (seasonDate.before(new Date())) {
	// this.calcEndDate();
	// } else {
	// this.setSeasonEndDate(seasonDate);
	// }
	// }
	// }

	/**
	 * 计算群雄争霸结束日期
	 */
	private void calcEndDate() {
		// 当前日期 大于 配置日期，默认下一个月的日期
		if (DateUtil.getMonyhDay(Calendar.getInstance()) < this.initT.rewardDate) {
			this.seasonEndDate = DateUtil.setMonyhDay(this.initT.rewardDate, this.initT.seasonInterval);
		} else {
			this.seasonEndDate = DateUtil.setMonyhDay(this.initT.rewardDate, 1);
		}
	}

	/**
	 * 每个赛季结束，发送奖励
	 */
	public void levelAward() {
		// 每天检查是否发送奖励
		if (DateUtil.isSameDay(new Date(), this.getSeasonEndDate())) {
			StringBuilder awardStr = new StringBuilder();

			for (int i = 0; i < this.ladderRankList.size(); i++) {
				LadderRankListSub ladderRank = this.getLadderRankList(i);

				int rank = i + 1;
				for (LadderAwardT awardT : XsgLadderManager.getInstance().awardList) {
					if ((rank >= awardT.start && rank <= awardT.end) || (rank >= awardT.start && 0 == awardT.end)) {

						Map<String, Integer> rewardMap = new HashMap<String, Integer>();
						for (String item : awardT.items.split(",")) {
							String[] itemSub = item.split(":");
							rewardMap.put(itemSub[0], Integer.valueOf(itemSub[1]));
						}

						awardStr.append(TextUtil
								.format("{0},{1},{2};", rank, ladderRank.roleId, ladderRank.ladderScore));

						Map<String, String> replaceMap = new HashMap<String, String>(2);
						replaceMap.put("$z", String.valueOf(rank));

						// 模板ID：8，发送争霸赛赛季排名奖励
						XsgMailManager.getInstance().sendTemplate(ladderRank.roleId,
								MailTemplate.LadderSeason, rewardMap, replaceMap);

						break;
					}
				}
			}

			// 保存奖励 日志
			ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 2, awardStr.toString());
			XsgArenaRankManager.getInstance().saveAwardLogAsync(awardlog);
			// 清除排行榜
			ladderRankList.clear();
			// 重新设定 赛季结束日期
			int interval = XsgLadderManager.getInstance().initT.seasonInterval;
			if (XsgLadderManager.getInstance().initT.seasonInterval == 0) {
				interval = 1;
			}
			this.seasonEndDate = DateUtil.setMonyhDay(XsgLadderManager.getInstance().initT.rewardDate, interval);
		}
	}

	/**
	 * 立即发放奖励
	 */
	public void sendAward() {
		StringBuilder awardStr = new StringBuilder();

		for (int i = 0; i < this.ladderRankList.size(); i++) {
			LadderRankListSub ladderRank = this.getLadderRankList(i);

			int rank = i + 1;
			for (LadderAwardT awardT : XsgLadderManager.getInstance().awardList) {
				if ((rank >= awardT.start && rank <= awardT.end) || (rank >= awardT.start && 0 == awardT.end)) {

					Map<String, Integer> rewardMap = new HashMap<String, Integer>();
					for (String item : awardT.items.split(",")) {
						String[] itemSub = item.split(":");
						rewardMap.put(itemSub[0], Integer.valueOf(itemSub[1]));
					}

					awardStr.append(TextUtil.format("{0},{1},{2};", rank, ladderRank.roleId, ladderRank.ladderScore));

					Map<String, String> replaceMap = new HashMap<String, String>(2);
					replaceMap.put("$z", String.valueOf(rank));

					// 模板ID：8，发送争霸赛赛季排名奖励
					XsgMailManager.getInstance().sendTemplate(ladderRank.roleId,
							MailTemplate.LadderSeason, rewardMap, replaceMap);

					break;
				}
			}
		}

		// 保存奖励 日志
		ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 2, awardStr.toString());
		XsgArenaRankManager.getInstance().saveAwardLogAsync(awardlog);
		// 清除排行榜
		ladderRankList.clear();
		// 重新设定 赛季结束日期
		int interval = XsgLadderManager.getInstance().initT.seasonInterval;
		if (XsgLadderManager.getInstance().initT.seasonInterval == 0) {
			interval = 1;
		}
		this.seasonEndDate = DateUtil.setMonyhDay(XsgLadderManager.getInstance().initT.rewardDate, interval);
	}

	/**
	 * 角色登录加载，显示群雄争霸的排行榜
	 * 
	 * @param loadingShowNum
	 * @return
	 */
	public LoadingRankSub[] getLadderRankListSub(int loadingShowNum) {
		LoadingRankSub[] LoadingRankArr = null;
		if (this.ladderRankList.size() >= loadingShowNum) {
			LoadingRankArr = new LoadingRankSub[loadingShowNum];
			for (int i = 0; i < loadingShowNum; i++) {
				LadderRankListSub sub = this.ladderRankList.get(i);

				LoadingRankSub LoadingRank = new LoadingRankSub();
				LoadingRank.roleId = sub.roleId;
				LoadingRank.roleName = sub.roleName;
				LoadingRank.icon = sub.icon;
				LoadingRank.vipLevel = sub.vipLevel;
				LoadingRank.showThing = String.valueOf(sub.ladderLevel);
				LoadingRankArr[i] = LoadingRank;
			}
		}

		return LoadingRankArr;
	}

	/**
	 * 根据自身排名获取匹配范围模版
	 * 
	 * @param rank
	 * @return
	 */
	public LadderMatchT getMatchTByRank(int rank) {
		for (LadderMatchT m : this.ladderMatchTs) {
			if (rank >= m.selfStart && rank <= m.selfEnd) {
				return m;
			}
		}
		return ladderMatchTs.get(0);
	}

	/**
	 * 获取等级星级对应的分数
	 * 
	 * @param level
	 * @param star
	 * @return
	 */
	public int getLevelScore(int level, int star) {
		for (LadderLevelScoreT s : this.ladderLevelScoreTs) {
			if (s.level == level && s.star == star) {
				return s.score;
			}
		}
		return 0;
	}

	/**
	 * 去除战报里面的NULL对象
	 * 
	 * @param movieView
	 */
	public void replaceNull(CrossMovieView movieView) {
		try {
			if (movieView.soloMovie.length > 0 && movieView.soloMovie[0].reports.length > 0) {
				for (ActionN a : movieView.soloMovie[0].reports[0].soloReport[0].actions) {
					if (a.damages != null) {
						if (a.damages[0].buffs == null) {
							a.damages[0].buffs = new DuelBuffView[0];
						} else {
							if (a.damages[0].buffs[0] == null) {
								a.damages[0].buffs = new DuelBuffView[0];
							}
						}
					} else {
						a.damages = new DamageN[0];
					}
					if (a.effects == null) {
						a.effects = new EffectN[0];
					}
				}
				movieView.soloMovie[0].reports[0].result = DuelResult.DuelResultFail;
				if (movieView.soloMovie[0].reports[0].firsts.length > 0) {
					movieView.soloMovie[0].reports[0].firsts[0].type = DuelTemplateType.DuelTemplateTypeHero;
				}
				if (movieView.soloMovie[0].reports[0].seconds.length > 0) {
					movieView.soloMovie[0].reports[0].seconds[0].type = DuelTemplateType.DuelTemplateTypeHero;
				}
			}
		} catch (Exception e) {
			LogManager.error(e);
			movieView.soloMovie = new SceneDuelView[0];
		}
	}
}
