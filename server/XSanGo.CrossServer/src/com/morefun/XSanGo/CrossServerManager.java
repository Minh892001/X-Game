package com.morefun.XSanGo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.XSanGo.Protocol.ActionN;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRankItem;
import com.XSanGo.Protocol.CrossRankView;
import com.XSanGo.Protocol.CrossRivalView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CrossScheduleView;
import com.XSanGo.Protocol.CrossServerPrx;
import com.XSanGo.Protocol.DamageN;
import com.XSanGo.Protocol.DuelBuffView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.DuelTemplateType;
import com.XSanGo.Protocol.EffectN;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.SceneDuelView;
import com.XSanGo.Protocol.ScheduleRoleView;
import com.morefun.XSanGo.db.CrossLog;
import com.morefun.XSanGo.db.CrossLogDAO;
import com.morefun.XSanGo.db.CrossRank;
import com.morefun.XSanGo.db.CrossRankDAO;
import com.morefun.XSanGo.db.CrossStage;
import com.morefun.XSanGo.db.CrossStageDAO;
import com.morefun.XSanGo.db.Schedule;
import com.morefun.XSanGo.db.ScheduleDAO;
import com.morefun.XSanGo.db.ScoreLog;
import com.morefun.XSanGo.db.ScoreLogDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.template.CrossConfT;
import com.morefun.XSanGo.template.CrossMatchT;
import com.morefun.XSanGo.template.CrossOutT;
import com.morefun.XSanGo.template.CrossRangeT;
import com.morefun.XSanGo.template.CrossRewardT;
import com.morefun.XSanGo.template.CrossScheduleConfT;
import com.morefun.XSanGo.template.CrossScoreRewardT;
import com.morefun.XSanGo.template.CrossScoreT;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class CrossServerManager {

	public CrossConfT confT;

	/**
	 * 跨服配置
	 */
	public List<CrossRangeT> crossRangeTs;

	/**
	 * 匹配规则
	 */
	public CrossMatchT crossMatchT;

	/**
	 * 淘汰赛赛程
	 */
	public List<CrossOutT> crossOutTs;

	/**
	 * 积分计算规则
	 */
	public List<CrossScoreT> crossScoreTs;

	/**
	 * 跨服排行榜数据 key-跨服ID
	 */
	public Map<Integer, CopyOnWriteArrayList<CrossRank>> crossRanks = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<CrossRank>>();

	/**
	 * 玩家对手数据
	 */
	public Map<String, List<CrossRivalView>> roleRivalMap = new ConcurrentHashMap<String, List<CrossRivalView>>();

	/**
	 * 跨服淘汰赛数据 key-跨服ID
	 */
	public Map<Integer, CopyOnWriteArrayList<Schedule>> crossScheduleMap = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<Schedule>>();

	/**
	 * 战报生成接口地址
	 */
	public String movieHttpUrl;

	/**
	 * 机器人数据
	 */
	public Map<String, CrossRankItem> robotMap = new ConcurrentHashMap<String, CrossRankItem>();

	/**
	 * 战胜机器人随机得分集合 1 2 4 6 8
	 */
	public List<Integer> winRobotScore = new ArrayList<Integer>();

	/**
	 * 历史比武大会冠军记录
	 */
	// public List<CrossLog> crossLogs = new ArrayList<CrossLog>();

	/**
	 * 每个跨服当前已完成阶段
	 */
	public Map<Integer, CrossStage> crossFinishStage = new ConcurrentHashMap<Integer, CrossStage>();

	/**
	 * 合过服的目标服ID,key-原来服务器ID，value-目标服务器ID
	 */
	public Map<Integer, Integer> targetServerIdMap = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 合服列表获取接口地址
	 */
	public String mergerServerHttpUrl;

	/**
	 * 排名奖励
	 */
	public List<CrossRewardT> rewardTs;

	/**
	 * 积分奖励
	 */
	public List<CrossScoreRewardT> scoreRewardTs;

	/**
	 * 玩家历史积分，处理不发第二次积分奖励
	 */
	public Map<String, Integer> roleScoreMap = new ConcurrentHashMap<String, Integer>();

	private static final class Inner {
		private static final CrossServerManager instance = new CrossServerManager();
	}

	private CrossServerManager() {
		movieHttpUrl = (String) CrossServerMain.getAC().getBean("movieHttpUrl");
		mergerServerHttpUrl = (String) CrossServerMain.getAC().getBean("mergerServerHttpUrl");
		confT = ExcelParser.parse(CrossConfT.class).get(0);
		crossRangeTs = ExcelParser.parse(CrossRangeT.class);
		for (CrossRangeT r : crossRangeTs) {
			List<String> list = TextUtil.stringToList(r.serverIds);
			for (String id : list) {
				r.serverList.add(NumberUtil.parseInt(id));
			}
		}
		crossMatchT = ExcelParser.parse(CrossMatchT.class).get(0);
		crossOutTs = ExcelParser.parse(CrossOutT.class);
		crossScoreTs = ExcelParser.parse(CrossScoreT.class);
		rewardTs = ExcelParser.parse(CrossRewardT.class);
		scoreRewardTs = ExcelParser.parse(CrossScoreRewardT.class);

		// winRobotScore.add(5);
		// winRobotScore.add(10);
		// winRobotScore.add(15);
		// winRobotScore.add(20);
		winRobotScore.add(25);

		// 获取最新合服id数据
		refreshTargetServerList();

		// 初始化排行榜
		CrossRankDAO crossRankDAO = CrossRankDAO.getFromApplicationContext(CrossServerMain.getAC());
		List<CrossRank> ranks = crossRankDAO.findAll();
		for (CrossRank r : ranks) {
			int crossId = getCrossIdByServerId(TextUtil.GSON.fromJson(r.getRoleView(), CrossRoleView.class).serverId);
			CopyOnWriteArrayList<CrossRank> list = crossRanks.get(crossId);
			if (list == null) {
				list = new CopyOnWriteArrayList<CrossRank>();
				crossRanks.put(crossId, list);
			}
			r.setFormationView(TextUtil.GSON.fromJson(r.getPvpView(), PvpOpponentFormationView.class));
			list.add(r);
		}
		// 初始化对阵表
		ScheduleDAO scheduleDAO = ScheduleDAO.getFromApplicationContext(CrossServerMain.getAC());
		List<Schedule> schedulesList = scheduleDAO.findAll();
		for (Schedule s : schedulesList) {
			// 解压战报
			String[] movieStr = TextUtil.GSON.fromJson(s.getMovieView(), String[].class);
			for (String ms : movieStr) {
				try {
					CrossMovieView mv = TextUtil.GSON.fromJson(TextUtil.ungzip(ms), CrossMovieView.class);
					if (mv != null) {
						s.getMovieList().add(mv);
					}
				} catch (IOException e) {
					LogManager.error(e);
				}
			}
			CopyOnWriteArrayList<Schedule> crossList = crossScheduleMap.get(s.getCrossId());
			if (crossList == null) {
				crossList = new CopyOnWriteArrayList<Schedule>();
				crossList.add(s);
				crossScheduleMap.put(s.getCrossId(), crossList);
			} else {
				crossList.add(s);
			}
		}

		// 初始化淘汰赛阶段
		CrossStageDAO crossStageDAO = CrossStageDAO.getFromApplicationContext(CrossServerMain.getAC());
		List<CrossStage> stageList = crossStageDAO.findAll();
		for (CrossStage s : stageList) {
			this.crossFinishStage.put(s.getCrossId(), s);
		}

		ScoreLogDAO scoreLogDAO = ScoreLogDAO.getFromApplicationContext(CrossServerMain.getAC());
		List<ScoreLog> scoreList = scoreLogDAO.findAll();
		for (ScoreLog l : scoreList) {
			roleScoreMap.put(l.getRoleId(), l.getScore());
		}

		// 开始报名清空上届数据
		Calendar date = Calendar.getInstance();
		date.setTime(confT.beginApplyDate);
		long delayed = date.getTimeInMillis() - new Date().getTime();
		delayed -= 60000;// 提前1分钟
		if (delayed >= 0) {
			LogicThread.scheduleTask(new DelayedTask(delayed) {
				@Override
				public void run() {
					deleteAllData();
					crossRanks.clear();
					crossScheduleMap.clear();
					crossFinishStage.clear();
				}
			});
		}

		// 资格赛结束生成32强定时器
		date = Calendar.getInstance();
		date.setTime(confT.zigeEndDate);
		delayed = date.getTimeInMillis() - new Date().getTime();
		delayed += confT.delayHour * 60 * 60 * 1000;
		if (delayed >= 0) {
			LogicThread.scheduleTask(new DelayedTask(delayed) {

				@Override
				public void run() {
					// 读取对阵配置表
					List<CrossScheduleConfT> scheduleConfTs = ExcelParser.parse(CrossScheduleConfT.class);
					Map<Integer, List<CrossScheduleConfT>> crossMaps = new ConcurrentHashMap<Integer, List<CrossScheduleConfT>>();
					for (CrossScheduleConfT c : scheduleConfTs) {
						List<CrossScheduleConfT> crossList = crossMaps.get(c.crossId);
						if (crossList == null) {
							crossList = new ArrayList<CrossScheduleConfT>();
							crossMaps.put(c.crossId, crossList);
						}
						crossList.add(c);
					}

					// 验证脚本配置是否重复配置id
					for (Entry<Integer, List<CrossScheduleConfT>> entry : crossMaps.entrySet()) {
						List<String> ids = new ArrayList<String>();
						for (CrossScheduleConfT c : entry.getValue()) {
							if (ids.contains(c.leftRoleId) || ids.contains(c.rightRoleId)) {
								crossMaps.remove(entry.getKey());
								LogManager.warn(TextUtil.format("跨服{0}有重复的ID", entry.getKey()));
								break;
							}
							ids.add(c.leftRoleId);
							ids.add(c.rightRoleId);
						}
					}

					for (Entry<Integer, CopyOnWriteArrayList<CrossRank>> entry : crossRanks.entrySet()) {
						int length = Math.min(confT.outPeople, entry.getValue().size());
						if (length <= 0) {
							continue;
						}
						int stage = 32;
						List<CrossRank> ranks = new ArrayList<CrossRank>(entry.getValue());
						sortRank(ranks);
						ranks = new ArrayList<CrossRank>(ranks.subList(0, length));

						// 验证脚本ID是否存在
						boolean auto = false;
						List<CrossScheduleConfT> crossList = crossMaps.get(entry.getKey());
						if (crossList != null) {
							for (CrossScheduleConfT c : crossList) {
								boolean leftExist = false;
								boolean rightExist = false;
								for (CrossRank r : ranks) {
									if (c.leftRoleId.equals(r.getRoleId())) {
										leftExist = true;
									}
									if (c.rightRoleId.equals(r.getRoleId())) {
										rightExist = true;
									}
								}
								if (!leftExist) {// 如果有不存在的id就走自动分组
									LogManager.warn(TextUtil.format("跨服{0}有不存在的ID{1}", entry.getKey(), c.leftRoleId));
									auto = true;
									break;
								}
								if (!rightExist) {// 如果有不存在的id就走自动分组
									LogManager.warn(TextUtil.format("跨服{0}有不存在的ID{1}", entry.getKey(), c.rightRoleId));
									auto = true;
									break;
								}
							}
						} else {
							auto = true;
						}

						CopyOnWriteArrayList<Schedule> schedules = new CopyOnWriteArrayList<Schedule>();
						if (auto) {
							Collections.shuffle(ranks);
							if (ranks.size() <= 16) {// 16组的对手全部为空
								for (int i = 0; i < 16; i++) {
									String leftRole = "";
									if (i < ranks.size()) {
										leftRole = ranks.get(i).getRoleView();
									}
									Schedule schedule = new Schedule(entry.getKey(), stage, i + 1, leftRole, "", 0, 0);
									if (TextUtil.isNotBlank(leftRole)) {// 对手为空直接胜利
										schedule.setWinRoleView(leftRole);
									}
									addSchedule(schedule);
									schedules.add(schedule);
								}
							} else {
								int realGroup = ranks.size() - 16;// 真实组
								int virtualGroup = ranks.size() - realGroup * 2;// 虚拟组无对手
								int index = 0;
								for (int i = 0; i < realGroup; i++) {
									CrossRank left = ranks.get(index);
									CrossRank right = ranks.get(index + 1);

									Schedule schedule = new Schedule(entry.getKey(), stage, i + 1, left.getRoleView(),
											right.getRoleView(), left.getFormationView().view.battlePower, right
													.getFormationView().view.battlePower);
									addSchedule(schedule);
									schedules.add(schedule);
									index += 2;
								}
								for (int i = 0; i < virtualGroup; i++) {
									CrossRank left = ranks.get(index + i);

									Schedule schedule = new Schedule(entry.getKey(), stage, realGroup + i + 1, left
											.getRoleView(), "", left.getFormationView().view.battlePower, 0);
									schedule.setWinRoleView(schedule.getRoleView1());
									addSchedule(schedule);
									schedules.add(schedule);
								}
							}
						} else {
							int i = 0;
							for (CrossScheduleConfT c : crossList) {
								CrossRank left = null;
								CrossRank right = null;
								for (CrossRank r : ranks) {
									if (r.getRoleId().equals(c.leftRoleId)) {
										left = r;
									}
									if (r.getRoleId().equals(c.rightRoleId)) {
										right = r;
									}
								}
								Schedule schedule = new Schedule(entry.getKey(), stage, i + 1, left.getRoleView(),
										right.getRoleView(), left.getFormationView().view.battlePower, right
												.getFormationView().view.battlePower);
								addSchedule(schedule);
								schedules.add(schedule);
								i++;
							}
						}
						crossScheduleMap.put(entry.getKey(), schedules);
					}
				}
			});
		}

		// 开始淘汰赛生成战报定时器
		date = Calendar.getInstance();
		date.setTime(confT.outTime);
		delayed = DateUtil.betweenTaskHourMillis(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
		LogicThread.scheduleTask(new DelayedTask(delayed, TimeUnit.DAYS.toMillis(1)) {

			@Override
			public void run() {
				final int stage = getStage();
				if (stage == -1) {// 不在淘汰赛时间内
					return;
				}
				LogicThread.scheduleTask(createMovieTask(stage, 0));
			}
		});

		// 比武大会结束发放排行奖
		date = Calendar.getInstance();
		date.setTime(confT.end2Date);
		delayed = date.getTimeInMillis() - new Date().getTime();
		// 不判断会重复执行
		if (delayed >= 0) {
			LogicThread.scheduleTask(new DelayedTask(delayed) {

				@Override
				public void run() {
					refreshTargetServerList();
					sendAttendAward();
					sendStageAward();
				}
			});
		}
	}

	/**
	 * 生成战报任务
	 * 
	 * @param stage
	 * @param delayed
	 * @return
	 */
	private DelayedTask createMovieTask(final int stage, final long delayed) {
		DelayedTask movieTask = new DelayedTask(delayed) {

			@Override
			public void run() {
				boolean isAllOver = true;
				for (Entry<Integer, CopyOnWriteArrayList<Schedule>> entry : crossScheduleMap.entrySet()) {
					CopyOnWriteArrayList<Schedule> schedules = entry.getValue();
					List<Schedule> sub = new ArrayList<Schedule>();
					boolean isOver = true;
					for (Schedule s : schedules) {
						// 不是虚拟组才处理(两边都为空)
						if (s.getStage() == stage && TextUtil.isNotBlank(s.getRoleView1())) {
							sub.add(s);
							if (TextUtil.isBlank(s.getWinRoleView())) {
								isOver = false;
							}
						}
					}
					if (isOver) {
						continue;
					}
					try {
						isOver = doGenerateMovie(entry.getKey(), sub, stage);
					} catch (Exception e) {
						LogManager.error(e);
					}
					if (!isOver) {
						isAllOver = false;
					}
				}
				if (isAllOver) {
					refreshTargetServerList();
					for (Entry<Integer, CopyOnWriteArrayList<Schedule>> entry : crossScheduleMap.entrySet()) {
						// 处理避免重复开打的情况
						CrossStage cs = crossFinishStage.get(entry.getKey());
						if (cs == null) {
							cs = new CrossStage(entry.getKey(), stage);
							addCrossStage(cs);
							crossFinishStage.put(entry.getKey(), cs);
						} else if (cs.getStage() <= stage) {// 阶段已完成
							continue;
						} else {
							cs.setStage(stage);
							updateCrossStage(cs);
						}

						CopyOnWriteArrayList<Schedule> schedules = entry.getValue();
						List<Schedule> sub = new ArrayList<Schedule>();
						for (Schedule s : schedules) {
							// 不是虚拟组才处理(两边都为空)
							if (s.getStage() == stage && TextUtil.isNotBlank(s.getRoleView1())) {
								sub.add(s);
							}
						}

						updateStage(stage, sub);
						for (Schedule s : sub) {
							// 对手为空不发奖
							if (TextUtil.isBlank(s.getRoleView2())) {
								continue;
							}
							CrossRoleView winRole = TextUtil.GSON.fromJson(s.getWinRoleView(), CrossRoleView.class);
							// 通知游戏服发放竞猜奖励
							List<Integer> serverIds = getCrossServerIds(entry.getKey());
							for (Integer sid : serverIds) {
								CrossServerPrx prx = IceEntry.getCrossServerPrx(sid);
								if (prx != null) {
									prx.begin_guessResult(s.getId(), winRole.roleId);
								}
							}
						}
						if (stage <= 2) {// 已经是决赛了
							continue;
						}
						// 生成下一阶段对阵表
						int newStage = stage / 2;
						int maxGroup = newStage / 2;
						if (sub.size() <= maxGroup) {// 对手全部为空
							for (int i = 0; i < maxGroup; i++) {
								String leftRole = "";
								if (i < sub.size()) {
									leftRole = sub.get(i).getWinRoleView();
								}
								Schedule schedule = new Schedule(entry.getKey(), newStage, i + 1, leftRole, "", 0, 0);
								if (TextUtil.isNotBlank(leftRole)) {// 对手为空直接胜利
									schedule.setWinRoleView(leftRole);
									CrossRank r = getCrossRank(entry.getKey(),
											TextUtil.GSON.fromJson(leftRole, CrossRoleView.class).roleId);
									schedule.setBattlePower1(r.getFormationView().view.battlePower);
								}
								addSchedule(schedule);
								schedules.add(schedule);
							}
						} else {
							int realGroup = sub.size() - maxGroup;// 真实组
							int virtualGroup = sub.size() - realGroup * 2;// 虚拟组无对手
							int index = 0;
							for (int i = 0; i < realGroup; i++) {
								Schedule schedule = new Schedule(entry.getKey(), newStage, i + 1, sub.get(index)
										.getWinRoleView(), sub.get(index + 1).getWinRoleView(), 0, 0);

								CrossRank r1 = getCrossRank(
										entry.getKey(),
										TextUtil.GSON.fromJson(sub.get(index).getWinRoleView(), CrossRoleView.class).roleId);
								CrossRank r2 = getCrossRank(entry.getKey(), TextUtil.GSON.fromJson(sub.get(index + 1)
										.getWinRoleView(), CrossRoleView.class).roleId);
								schedule.setBattlePower1(r1.getFormationView().view.battlePower);
								schedule.setBattlePower2(r2.getFormationView().view.battlePower);
								addSchedule(schedule);
								schedules.add(schedule);
								index += 2;
							}
							for (int i = 0; i < virtualGroup; i++) {
								Schedule schedule = new Schedule(entry.getKey(), newStage, realGroup + i + 1, sub.get(
										index + i).getWinRoleView(), "", 0, 0);

								CrossRank r = getCrossRank(entry.getKey(), TextUtil.GSON.fromJson(sub.get(index + i)
										.getWinRoleView(), CrossRoleView.class).roleId);
								schedule.setWinRoleView(schedule.getRoleView1());
								schedule.setBattlePower1(r.getFormationView().view.battlePower);
								addSchedule(schedule);
								schedules.add(schedule);
							}
						}
					}
				} else {
					LogicThread.scheduleTask(createMovieTask(stage, TimeUnit.MINUTES.toMillis(confT.outInterval)));
				}
			}
		};
		return movieTask;
	}

	private void updateCrossStage(final CrossStage cs) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossStageDAO crossStageDAO = CrossStageDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossStageDAO.update(cs);
			}
		});
	}

	private void addCrossStage(final CrossStage cs) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossStageDAO crossStageDAO = CrossStageDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossStageDAO.save(cs);
			}
		});
	}

	/**
	 * 处理种子玩家，如果有32人种子玩家一定要4强赛才碰面
	 * 
	 * @param ranks
	 */
	// private void calculateSeedPlayer(List<CrossRank> ranks) {
	// Collections.sort(ranks, new Comparator<CrossRank>() {
	//
	// @Override
	// public int compare(CrossRank o1, CrossRank o2) {
	// PvpOpponentFormationView p1 = TextUtil.GSON.fromJson(o1.getPvpView(),
	// PvpOpponentFormationView.class);
	// PvpOpponentFormationView p2 = TextUtil.GSON.fromJson(o2.getPvpView(),
	// PvpOpponentFormationView.class);
	// return p2.view.battlePower - p1.view.battlePower;
	// }
	// });
	// // 战力前四
	// List<CrossRank> top4 = new ArrayList<CrossRank>(ranks.subList(0, 4));
	// ranks.removeAll(top4);
	// Collections.shuffle(ranks);
	//
	// ranks.add(0, top4.get(0));
	// ranks.add(8, top4.get(1));
	// ranks.add(16, top4.get(2));
	// ranks.add(24, top4.get(3));
	// }

	/**
	 * 获取人数对应的比赛阶段
	 * 
	 * @param people
	 * @return
	 */
	public int getStage(int people) {
		if (people <= 32 && people > 16) {
			return 32;
		} else if (people <= 16 && people > 8) {
			return 16;
		} else if (people <= 8 && people > 4) {
			return 8;
		} else if (people <= 4 && people > 2) {
			return 4;
		} else {
			return 2;
		}
	}

	/**
	 * 调用远程接口生成战报
	 * 
	 * @param crossId
	 * @param sub
	 * @param stage
	 * @return
	 */
	public boolean doGenerateMovie(int crossId, List<Schedule> sub, int stage) {
		for (Schedule s : sub) {
			if (TextUtil.isNotBlank(s.getWinRoleView()) || TextUtil.isBlank(s.getRoleView1())) {
				continue;
			}
			CrossRoleView r1 = TextUtil.GSON.fromJson(s.getRoleView1(), CrossRoleView.class);
			CrossRoleView r2 = TextUtil.GSON.fromJson(s.getRoleView2(), CrossRoleView.class);
			CrossRank cr1 = getCrossRank(crossId, r1.roleId);
			CrossRank cr2 = getCrossRank(crossId, r2.roleId);
			CrossPvpView pvpView = new CrossPvpView(10, r1, cr1.getFormationView(), r2, cr2.getFormationView(), "", 0); // hard
			String result = HttpUtil.sendPost(movieHttpUrl, TextUtil.GSON.toJson(pvpView));
			// 保存战报
			CrossMovieView movieView = TextUtil.GSON.fromJson(result, CrossMovieView.class);
			if (movieView != null) {
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
				s.getMovieList().add(movieView);
				String[] mvs = TextUtil.GSON.fromJson(s.getMovieView(), String[].class);
				List<String> mvList = new ArrayList<String>(Arrays.asList(mvs));
				try {
					mvList.add(TextUtil.gzip(TextUtil.GSON.toJson(movieView)));
				} catch (IOException e) {
					LogManager.error(e);
				}
				s.setMovieView(TextUtil.GSON.toJson(mvList.toArray(new String[0])));

				int winNum = getWinNum(stage);
				// 判断胜负
				if (mvList.size() >= winNum) {
					int leftWin = 0;
					int rightWin = 0;
					for (CrossMovieView m : s.getMovieList()) {
						if (r1.roleId.equals(m.winRoleId)) {
							leftWin++;
						}
						if (r2.roleId.equals(m.winRoleId)) {
							rightWin++;
						}
					}
					if (leftWin >= winNum || rightWin >= winNum) {
						if (leftWin > rightWin) {
							s.setWinRoleView(s.getRoleView1());
						} else if (leftWin < rightWin) {
							s.setWinRoleView(s.getRoleView2());
						}
					}
				}
				updateSchedule(s);
			} else {
				LogManager.warn("movieView is null");
			}
		}
		for (Schedule s : sub) {
			if (TextUtil.isBlank(s.getWinRoleView()) && TextUtil.isNotBlank(s.getRoleView1())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据当前时间判断淘汰赛阶段
	 * 
	 * @return
	 */
	private int getStage() {
		if (DateUtil.isBetween(confT.begin32Date, confT.end32Date)) {
			return 32;
		}
		if (DateUtil.isBetween(confT.begin16Date, confT.end16Date)) {
			return 16;
		}
		if (DateUtil.isBetween(confT.begin8Date, confT.end8Date)) {
			return 8;
		}
		if (DateUtil.isBetween(confT.begin4Date, confT.end4Date)) {
			return 4;
		}
		if (DateUtil.isBetween(confT.begin2Date, confT.end2Date)) {
			return 2;
		}
		return -1;
	}

	/**
	 * 异步增加对阵表
	 * 
	 * @param schedule
	 */
	public void addSchedule(final Schedule schedule) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				ScheduleDAO scheduleDAO = ScheduleDAO.getFromApplicationContext(CrossServerMain.getAC());
				scheduleDAO.save(schedule);
			}
		});
	}

	/**
	 * 异步更新对阵表
	 * 
	 * @param schedule
	 */
	public void updateSchedule(final Schedule schedule) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				ScheduleDAO scheduleDAO = ScheduleDAO.getFromApplicationContext(CrossServerMain.getAC());
				scheduleDAO.update(schedule);
			}
		});
	}

	/**
	 * 删除历史数据
	 */
	public void deleteAllData() {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossRankDAO crossRankDAO = CrossRankDAO.getFromApplicationContext(CrossServerMain.getAC());
				ScheduleDAO scheduleDAO = ScheduleDAO.getFromApplicationContext(CrossServerMain.getAC());
				CrossStageDAO crossStageDAO = CrossStageDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossRankDAO.deleteAll();
				scheduleDAO.deleteAll();
				crossStageDAO.deleteAll();
			}
		});
	}

	/**
	 * 获取玩家信息排行榜信息
	 * 
	 * @param crossId
	 * @param roleId
	 * @return
	 */
	public CrossRank getCrossRank(int crossId, String roleId) {
		if (crossRanks.get(crossId) == null) {
			return null;
		}
		for (CrossRank r : crossRanks.get(crossId)) {
			if (r.getRoleId().equals(roleId)) {
				return r;
			}
		}
		return null;
	}

	public static CrossServerManager getInstance() {
		return Inner.instance;
	}

	/** 初始化 */
	public void init() {
		// 初始化ICE框架相关逻辑
		IceEntry.activeIceAdapter();
	}

	/**
	 * 根据服务器ID获取跨服ID
	 * 
	 * @param serverId
	 * @return
	 */
	public int getCrossIdByServerId(int serverId) {
		if (targetServerIdMap.isEmpty()) {
			refreshTargetServerList();
		}
		if (targetServerIdMap.containsKey(serverId)) {
			serverId = targetServerIdMap.get(serverId);
		}
		for (CrossRangeT r : crossRangeTs) {
			if (r.serverList.contains(serverId)) {
				return r.id;
			}
		}
		return -1;
	}

	/**
	 * 添加排行榜
	 * 
	 * @param roleView
	 */
	public synchronized void addRank(CrossRoleView roleView, PvpOpponentFormationView pvpView) {
		int crossId = getCrossIdByServerId(roleView.serverId);
		if (crossId == -1) {
			return;
		}
		// 这里可以考虑用crossId作为锁提升处理效率
		CopyOnWriteArrayList<CrossRank> ranks = this.crossRanks.get(crossId);
		if (ranks == null) {
			ranks = new CopyOnWriteArrayList<CrossRank>();
			crossRanks.put(crossId, ranks);
		}
		// 检测是否已存在
		for (CrossRank c : ranks) {
			if (c.getRoleId().equals(roleView.roleId)) {
				return;
			}
		}
		CrossRank rank = new CrossRank(roleView.roleId, TextUtil.GSON.toJson(roleView), confT.initScore,
				TextUtil.GSON.toJson(pvpView), crossId);
		rank.setFormationView(pvpView);
		ranks.add(rank);

		addCrossRank(rank);
	}

	/**
	 * 排行榜排序
	 * 
	 * @param ranks
	 */
	public void sortRank(List<CrossRank> ranks) {
		Collections.sort(ranks, new Comparator<CrossRank>() {

			@Override
			public int compare(CrossRank o1, CrossRank o2) {
				int i = o2.getScore() - o1.getScore();
				if (i == 0) {
					i = (int) (o1.getUpdateDate().getTime() - o2.getUpdateDate().getTime());
				}
				if (i == 0) {
					i = (int) (o1.getCreateDate().getTime() - o2.getCreateDate().getTime());
				}
				return i;
			}
		});
	}

	/**
	 * 皇榜排序
	 * 
	 * @param ranks
	 */
	public void sortEmperorRank(List<CrossRank> ranks) {
		Collections.sort(ranks, new Comparator<CrossRank>() {

			@Override
			public int compare(CrossRank o1, CrossRank o2) {
				int i = o1.getIntoStage() - o2.getIntoStage();
				if (i == 0) {
					i = o2.getScore() - o1.getScore();
				}
				return i;
			}
		});
	}

	/**
	 * 异步增加排行榜
	 * 
	 * @param rank
	 */
	public void addCrossRank(final CrossRank rank) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossRankDAO crossRankDAO = CrossRankDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossRankDAO.save(rank);
			}
		});
	}

	/**
	 * 异步更新排行榜对象
	 * 
	 * @param rank
	 */
	public void updateCrossRank(final CrossRank rank) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossRankDAO crossRankDAO = CrossRankDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossRankDAO.update(rank);
			}
		});
	}

	/**
	 * 获取跨服排行榜
	 * 
	 * @param serverId
	 * @return
	 */
	public CrossRankView getCrossRankView(int serverId, String roleId) {
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = null;
		if (new Date().after(confT.begin32Date)) {// 到了32强时间就是皇榜了
			ranks = new ArrayList<CrossRank>();
			List<CrossRank> all = new ArrayList<CrossRank>(this.crossRanks.get(crossId));
			for (CrossRank r : all) {
				if (r.getIntoStage() > 0) {
					ranks.add(r);
				}
			}
			sortEmperorRank(ranks);
		} else {
			ranks = new ArrayList<CrossRank>(this.crossRanks.get(crossId));
			sortRank(ranks);
		}
		int length = Math.min(ranks.size(), confT.outPeople);
		CrossRankItem[] showRanks = new CrossRankItem[length];
		for (int i = 0; i < length; i++) {
			CrossRoleView roleView = TextUtil.GSON.fromJson(ranks.get(i).getRoleView(), CrossRoleView.class);
			showRanks[i] = new CrossRankItem(roleView, ranks.get(i).getFormationView(), ranks.get(i).getScore());
		}
		int myScore = 0;
		int myRank = 1;
		for (CrossRank r : ranks) {
			if (r.getRoleId().equals(roleId)) {
				myScore = r.getScore();
				serverId = TextUtil.GSON.fromJson(r.getRoleView(), CrossRoleView.class).serverId;
				break;
			}
			myRank++;
		}
		CrossRankView view = new CrossRankView(showRanks, myScore, myRank, serverId);
		return view;
	}

	/**
	 * 获取32强数据
	 * 
	 * @param serverId
	 * @return
	 */
	public List<CrossRank> getCross32(int serverId) {
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = new ArrayList<CrossRank>(this.crossRanks.get(crossId));
		sortRank(ranks);
		int length = Math.min(ranks.size(), confT.outPeople);
		return ranks.subList(0, length);
	}

	/**
	 * 保存角色部队阵容
	 * 
	 * @param serverId
	 * @param roleId
	 * @param pvpView
	 */
	public void saveRoleBattle(int serverId, CrossRoleView roleView, PvpOpponentFormationView pvpView) {
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = crossRanks.get(crossId);
		for (CrossRank r : ranks) {
			if (r.getRoleId().equals(roleView.roleId)) {
				r.setPvpView(TextUtil.GSON.toJson(pvpView));
				r.setRoleView(TextUtil.GSON.toJson(roleView));
				r.setFormationView(pvpView);
				updateCrossRank(r);
			}
		}

		// 更新没开打的对阵战力
		List<Schedule> ss = crossScheduleMap.get(crossId);
		if (ss == null) {
			return;
		}
		for (Schedule s : ss) {
			if (!s.getMovieList().isEmpty()) {
				continue;
			}
			if (TextUtil.isNotBlank(s.getRoleView1())) {
				CrossRoleView r1 = TextUtil.GSON.fromJson(s.getRoleView1(), CrossRoleView.class);
				if (r1.roleId.equals(roleView.roleId)) {
					s.setBattlePower1(pvpView.view.battlePower);
					updateSchedule(s);
					return;
				}
			}
			if (TextUtil.isNotBlank(s.getRoleView2())) {
				CrossRoleView r2 = TextUtil.GSON.fromJson(s.getRoleView2(), CrossRoleView.class);
				if (r2.roleId.equals(roleView.roleId)) {
					s.setBattlePower2(pvpView.view.battlePower);
					updateSchedule(s);
					return;
				}
			}
		}
	}

	/**
	 * 匹配对手
	 * 
	 * @param serverId
	 * @param roleId
	 * @return
	 */
	public CrossRivalView[] matchRival(int serverId, String roleId) {
		if (!DateUtil.isBetween(confT.zigeBeginDate, confT.zigeEndDate)) {
			return new CrossRivalView[0];
		}
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = new ArrayList<CrossRank>(crossRanks.get(crossId));
		List<CrossRivalView> rival = roleRivalMap.get(roleId);

		CrossRank my = getCrossRank(crossId, roleId);

		if (ranks.size() < 4 && rival == null) {// 人数不足时
			rival = new ArrayList<CrossRivalView>();
			for (CrossRank r : ranks) {
				if (r.getRoleId().equals(roleId)) {
					continue;
				}
				CrossRoleView roleView = TextUtil.GSON.fromJson(r.getRoleView(), CrossRoleView.class);
				rival.add(new CrossRivalView(roleView, r.getFormationView(), r.getScore(), getWinScore(my, r), false));
			}
			if (!robotMap.isEmpty()) {
				int count = 0;
				while (rival.size() < 3 && count < 10) {
					count++;
					CrossRankItem item = robotMap.get(getRandomRobotId());
					rival.add(new CrossRivalView(item.roleView, item.pvpView, 0, item.score, false));
				}
			}
			roleRivalMap.put(roleId, rival);
			return rival.toArray(new CrossRivalView[0]);
		}

		if (rival == null) {
			List<String> exist = new ArrayList<String>();
			exist.add(my.getRoleId());// 添加自己用来做排除
			String rid = null;

			boolean isNull = true;// 是否一个合适对手都没有

			// 第一个位置
			List<String> idList = randomRival(exist, my.getFormationView().view.battlePower, ranks, 0);
			if (idList.isEmpty()) {
				idList = randomRival(exist, my.getFormationView().view.battlePower, ranks, 3);
			}
			if (idList.isEmpty()) {// 没有合适的就全部随机一个
				int count = 0;
				rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				while (exist.contains(rid) && count < 10000) {
					rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				}
				exist.add(rid);
			} else {
				isNull = false;
				exist.add(idList.get(NumberUtil.random(idList.size())));
			}

			// 第二个位置
			idList = randomRival(exist, my.getFormationView().view.battlePower, ranks, 1);
			if (idList.isEmpty()) {
				rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				while (exist.contains(rid)) {
					rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				}
				exist.add(rid);
			} else {
				isNull = false;
				rid = idList.get(NumberUtil.random(idList.size()));
				int count = 0;
				while (exist.contains(rid) && count < 10000) {
					count++;
					rid = idList.get(NumberUtil.random(idList.size()));
				}
				exist.add(rid);
			}

			// 第三个位置
			idList = randomRival(exist, my.getFormationView().view.battlePower, ranks, 2);
			if (idList.isEmpty()) {
				idList = randomRival(exist, my.getFormationView().view.battlePower, ranks, 3);
			}
			if (idList.isEmpty()) {
				rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				while (exist.contains(rid)) {
					rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				}
				exist.add(rid);
			} else {
				isNull = false;
				rid = idList.get(NumberUtil.random(idList.size()));
				int count = 0;
				while (exist.contains(rid) && count < 10000) {
					count++;
					rid = idList.get(NumberUtil.random(idList.size()));
				}
				exist.add(rid);
			}

			// 处理不满3个人的情况
			int count = 0;
			while (exist.size() < 4 && count < 10000) {
				count++;
				rid = ranks.get(NumberUtil.random(ranks.size())).getRoleId();
				if (!exist.contains(rid)) {
					exist.add(rid);
				}
			}

			// 删除自己
			exist.remove(my.getRoleId());

			if (isNull) {// 特殊处理，取战力小于且最接近自己的
				sortByPower(ranks);
				int myRank = 0;
				for (CrossRank c : ranks) {
					if (c.getRoleId().equals(roleId)) {
						break;
					}
					myRank++;
				}
				for (int i = 1; i <= 3; i++) {
					if (ranks.size() > myRank + i) {
						CrossRank c = ranks.get(myRank + i);
						if (!exist.contains(c.getRoleId())) {
							exist.remove(0);
							exist.add(c.getRoleId());
						}
					}
				}
			}

			// Collections.shuffle(exist);
			rival = new ArrayList<CrossRivalView>();
			roleRivalMap.put(roleId, rival);
			for (String id : exist) {
				for (CrossRank r : ranks) {
					if (id.equals(r.getRoleId())) {
						CrossRoleView roleView = TextUtil.GSON.fromJson(r.getRoleView(), CrossRoleView.class);
						rival.add(new CrossRivalView(roleView, r.getFormationView(), r.getScore(), getWinScore(my, r),
								false));
					}
				}
			}
		}
		return rival.toArray(new CrossRivalView[0]);
	}

	public List<String> randomRival(List<String> exist, int selfPower, List<CrossRank> ranks, int index) {
		List<String> idList = new ArrayList<String>();
		// 自己战力档次
		selfPower = selfPower / 10000;
		if (index == 0) {
			// 处理第一个位置，高于自己1-5万战力
			for (CrossRank r : ranks) {
				if (exist.contains(r.getRoleId())) {
					continue;
				}
				if (selfPower >= 50) {// 最高档次
					if (r.getFormationView().view.battlePower >= 500000) {
						idList.add(r.getRoleId());
					}
				} else if (selfPower >= 45) {
					if (r.getFormationView().view.battlePower >= (selfPower + 1) * 10000) {
						idList.add(r.getRoleId());
					}
				} else {
					if (r.getFormationView().view.battlePower >= (selfPower + 1) * 10000
							&& r.getFormationView().view.battlePower < (selfPower + 6) * 10000) {
						idList.add(r.getRoleId());
					}
				}
			}
		} else if (index == 1) {
			// 处理第二个位置，自己及上下1万战力
			for (CrossRank r : ranks) {
				if (exist.contains(r.getRoleId())) {
					continue;
				}
				if (selfPower >= 50) {// 最高档次
					if (r.getFormationView().view.battlePower >= 490000) {
						idList.add(r.getRoleId());
					}
				} else if (selfPower == 49) {
					if (r.getFormationView().view.battlePower >= 480000) {
						idList.add(r.getRoleId());
					}
				} else {
					if (r.getFormationView().view.battlePower >= (selfPower - 1) * 10000
							&& r.getFormationView().view.battlePower < (selfPower + 2) * 10000) {
						idList.add(r.getRoleId());
					}
				}
			}
		} else if (index == 2) {
			// 处理第三个位置，低于自己1-5万战力
			for (CrossRank r : ranks) {
				if (exist.contains(r.getRoleId())) {
					continue;
				}
				if (selfPower >= 50) {
					if (r.getFormationView().view.battlePower >= 440000
							&& r.getFormationView().view.battlePower < 490000) {
						idList.add(r.getRoleId());
					}
				} else {
					if (r.getFormationView().view.battlePower >= (selfPower - 5) * 10000
							&& r.getFormationView().view.battlePower < selfPower * 10000) {
						idList.add(r.getRoleId());
					}
				}
			}
		} else {// 和自己是同档位
			for (CrossRank r : ranks) {
				if (exist.contains(r.getRoleId())) {
					continue;
				}
				if (selfPower >= 50) {
					if (r.getFormationView().view.battlePower >= 500000) {
						idList.add(r.getRoleId());
					}
				} else {
					if (r.getFormationView().view.battlePower >= selfPower * 10000
							&& r.getFormationView().view.battlePower < (selfPower + 1) * 10000) {
						idList.add(r.getRoleId());
					}
				}
			}
		}
		return idList;
	}

	/**
	 * 刷新对手
	 * 
	 * @param serverId
	 * @param roleId
	 * @return
	 */
	public CrossRivalView[] refreshRival(int serverId, String roleId) {
		roleRivalMap.remove(roleId);
		return matchRival(serverId, roleId);
	}

	/**
	 * 处理战斗结果
	 * 
	 * @param serverId
	 * @param winRoleView
	 * @param isWin
	 * @param rivalRoleId
	 */
	public String endChallenge(int serverId, CrossRoleView myRoleView, boolean isWin, String rivalRoleId) {
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = crossRanks.get(crossId);
		CrossRank my = null;
		CrossRank rival = null;
		for (CrossRank r : ranks) {
			if (r.getRoleId().equals(myRoleView.roleId)) {
				my = r;
			}
			if (r.getRoleId().equals(rivalRoleId)) {
				rival = r;
			}
			if (my != null && rival != null) {
				break;
			}
		}
		int oldScore = my.getScore();
		if (!isWin) {
			my.setScore(my.getScore() + confT.loseScore);
			my.setFailNum(my.getFailNum() + 1);
		} else {
			my.setWinNum(my.getWinNum() + 1);
			List<CrossRivalView> rv = roleRivalMap.get(myRoleView.roleId);
			boolean isAllWin = true;// 3个对手都战胜过
			for (CrossRivalView r : rv) {
				if (r.roleView.roleId.equals(rivalRoleId)) {
					r.isWin = true;// 标记战胜过不能再打
					my.setScore(my.getScore() + getWinScore(my, rival));
				}
				if (!r.isWin) {
					isAllWin = false;
				}
			}

			if (isAllWin) {// 自动刷新
				roleRivalMap.remove(myRoleView.roleId);
			}

		}
		// 发放积分奖励
		int newScore = my.getScore();
		for (CrossScoreRewardT s : this.scoreRewardTs) {
			Integer history = roleScoreMap.get(myRoleView.roleId);
			if (oldScore < s.score && newScore >= s.score && (history == null || newScore > history)) {// 首次达到
				CrossServerPrx prx = IceEntry.getCrossServerPrx(serverId);
				if (prx != null) {
					prx.begin_sendScoreAward(s.score, myRoleView.roleId);
				}
			}
		}

		my.setRoleView(TextUtil.GSON.toJson(myRoleView));
		my.setUpdateDate(new Date());

		updateCrossRank(my);
		// sortRank(ranks);
		return TextUtil.format("{0},{1},{2}", oldScore, rival == null ? 0 : rival.getScore(), my.getScore() - oldScore);
	}

	/**
	 * 计算自己胜利获得分值
	 * 
	 * @param my
	 * @param rival
	 * @return
	 */
	private int getWinScore(CrossRank my, CrossRank rival) {
		// int scale = (int) ((rival.getScore() - my.getScore()) * (double) 100
		// / my.getScore());
		// scale = Math.max(0, scale);
		// for (CrossScoreT s : crossScoreTs) {
		// if (scale >= s.scale) {
		// return s.addScore;
		// }
		// }
		// return 0;
		int winScore = rival.getFormationView().view.battlePower / 10000 + 1;
		winScore = Math.min(50, winScore);
		winScore = Math.max(6, winScore);
		return winScore;
	}

	/**
	 * 获取跨服淘汰赛对阵表
	 * 
	 * @param serverId
	 * @return
	 */
	public CrossScheduleView[] getSchedule(int serverId) {
		List<CrossScheduleView> views = new ArrayList<CrossScheduleView>();
		int crossId = getCrossIdByServerId(serverId);
		List<Schedule> schedules = crossScheduleMap.get(crossId);
		if (schedules == null) {
			return new CrossScheduleView[0];
		}
		for (Schedule s : schedules) {
			CrossRoleView r1 = TextUtil.GSON.fromJson(s.getRoleView1(), CrossRoleView.class);
			CrossRoleView r2 = TextUtil.GSON.fromJson(s.getRoleView2(), CrossRoleView.class);

			if (r1 == null && r2 == null) {// 虚拟组
				views.add(new CrossScheduleView(s.getId(), null, null, "", s.getStage(), s.getGroupNum(), s
						.getOrderNum()));
				continue;
			}

			List<CrossMovieView> mvs = s.getMovieList();
			int leftWin = 0;
			int rightWin = 0;
			for (CrossMovieView m : mvs) {
				if (r1.roleId.equals(m.winRoleId)) {
					leftWin++;
				}
				if (r2 != null && r2.roleId.equals(m.winRoleId)) {
					rightWin++;
				}
			}
			String winRoleId = "";
			if (TextUtil.isNotBlank(s.getWinRoleView())) {
				winRoleId = TextUtil.GSON.fromJson(s.getWinRoleView(), CrossRoleView.class).roleId;
			}
			CrossRank cr1 = getCrossRank(crossId, r1.roleId);

			ScheduleRoleView srv1 = new ScheduleRoleView(r1, s.getBattlePower1(), cr1.getWinNum(), cr1.getFailNum(),
					cr1.getToastNum(), leftWin);

			ScheduleRoleView srv2 = null;
			if (r2 != null) {
				CrossRank cr2 = getCrossRank(crossId, r2.roleId);
				srv2 = new ScheduleRoleView(r2, s.getBattlePower2(), cr2.getWinNum(), cr2.getFailNum(),
						cr2.getToastNum(), rightWin);
			}

			views.add(new CrossScheduleView(s.getId(), srv1, srv2, winRoleId, s.getStage(), s.getGroupNum(), s
					.getOrderNum()));
		}
		return views.toArray(new CrossScheduleView[0]);
	}

	/**
	 * 获取战报
	 * 
	 * @param serverId
	 * @param id
	 * @param index
	 * @return
	 */
	public CrossMovieView getScheduleMovieData(int serverId, int id, int index) {
		int crossId = getCrossIdByServerId(serverId);
		List<Schedule> schedules = crossScheduleMap.get(crossId);
		for (Schedule s : schedules) {
			if (s.getId() == id) {
				return s.getMovieList().get(index);
			}
		}
		return null;
	}

	/**
	 * 获取战报列表
	 * 
	 * @param serverId
	 * @param id
	 * @return
	 */
	public String[] getScheduleMovieList(int serverId, int id) {
		int crossId = getCrossIdByServerId(serverId);
		List<Schedule> schedules = crossScheduleMap.get(crossId);

		List<String> winRole = new ArrayList<String>();
		for (Schedule s : schedules) {
			if (s.getId() == id) {
				for (CrossMovieView m : s.getMovieList()) {
					winRole.add(m.winRoleId);
				}
			}
		}
		return winRole.toArray(new String[0]);
	}

	/**
	 * 获取每个阶段需要胜利的局数
	 * 
	 * @param stage
	 * @return
	 */
	public int getWinNum(int stage) {
		for (CrossOutT o : this.crossOutTs) {
			if (o.people == stage) {
				return o.winNum;
			}
		}
		return 2;
	}

	/**
	 * 获取跨服的所有服务器ID
	 * 
	 * @param crossId
	 * @return
	 */
	public List<Integer> getCrossServerIds(int crossId) {
		for (CrossRangeT r : crossRangeTs) {
			if (r.id == crossId) {
				return r.serverList;
			}
		}
		return new ArrayList<Integer>();
	}

	/**
	 * 获取自己排名和积分
	 * 
	 * @param serverId
	 * @param roleId
	 * @return
	 */
	public IntIntPair getMyRankScore(int serverId, String roleId) {
		int crossId = getCrossIdByServerId(serverId);
		List<CrossRank> ranks = new ArrayList<CrossRank>(crossRanks.get(crossId));
		sortRank(ranks);
		int myRank = 1;
		for (CrossRank r : ranks) {
			if (r.getRoleId().equals(roleId)) {
				return new IntIntPair(myRank, r.getScore());
			}
			myRank++;
		}
		return new IntIntPair(1000, 0);
	}

	/**
	 * 发放参与奖(没进淘汰赛)
	 */
	public void sendAttendAward() {
		for (Entry<Integer, CopyOnWriteArrayList<CrossRank>> entry : crossRanks.entrySet()) {
			List<CrossRank> crossRanks = new ArrayList<CrossRank>(entry.getValue());
			sortRank(crossRanks);
			if (crossRanks.size() <= confT.outPeople) {
				continue;
			}
			int i = confT.outPeople;
			for (CrossRewardT r : this.rewardTs) {
				if (r.rank <= 32) {
					continue;
				}
				// 每个服务器批量发送一次
				Map<Integer, List<String>> serverRoleMap = new HashMap<Integer, List<String>>();
				for (; i < crossRanks.size() && i < r.rank; i++) {
					CrossRank rank = crossRanks.get(i);
					int serverId = TextUtil.GSON.fromJson(rank.getRoleView(), CrossRoleView.class).serverId;
					// 处理合过服的服务器ID
					if (this.targetServerIdMap.containsKey(serverId)) {
						serverId = this.targetServerIdMap.get(serverId);
					}
					List<String> serverRole = serverRoleMap.get(serverId);
					if (serverRole == null) {
						serverRole = new ArrayList<String>();
						serverRoleMap.put(serverId, serverRole);
					}
					serverRole.add(rank.getRoleId());
				}

				for (Entry<Integer, List<String>> srEt : serverRoleMap.entrySet()) {
					CrossServerPrx prx = IceEntry.getCrossServerPrx(srEt.getKey());
					if (prx != null) {
						prx.begin_sendCrossAward(r.rank, srEt.getValue().toArray(new String[0]));
					}
				}
			}
		}
	}

	/**
	 * 更新淘汰赛阶段
	 */
	public void updateStage(int stage, List<Schedule> schedules) {
		for (Schedule s : schedules) {
			CrossRoleView left = TextUtil.GSON.fromJson(s.getRoleView1(), CrossRoleView.class);
			CrossRoleView right = TextUtil.GSON.fromJson(s.getRoleView2(), CrossRoleView.class);
			CrossRoleView winRole = TextUtil.GSON.fromJson(s.getWinRoleView(), CrossRoleView.class);

			if (left == null) {
				continue;
			}

			if (stage == 2) {// 决赛
				// 更新冠军进入的阶段
				int crossId = getCrossIdByServerId(winRole.serverId);
				CrossRank first = getCrossRank(crossId, winRole.roleId);
				first.setIntoStage(1);
				updateCrossRank(first);
			}

			if (right == null) {
				continue;
			}

			// 败者才更新
			int failSserverId = 0;
			String failRoleId = null;
			if (winRole.roleId.equals(left.roleId)) {
				failSserverId = right.serverId;
				failRoleId = right.roleId;
			} else {
				failSserverId = left.serverId;
				failRoleId = left.roleId;
			}

			// 处理合过服的服务器ID
			if (this.targetServerIdMap.containsKey(failSserverId)) {
				failSserverId = this.targetServerIdMap.get(failSserverId);
			}
			// 更新败者进入的阶段
			int crossId = getCrossIdByServerId(failSserverId);
			CrossRank failRnak = getCrossRank(crossId, failRoleId);
			failRnak.setIntoStage(stage);
			updateCrossRank(failRnak);
		}
	}

	/**
	 * 发放淘汰赛奖励
	 */
	public void sendStageAward() {
		for (Entry<Integer, CopyOnWriteArrayList<CrossRank>> entry : crossRanks.entrySet()) {
			for (CrossRank r : entry.getValue()) {
				if (r.getIntoStage() > 0) {
					CrossRoleView roleView = TextUtil.GSON.fromJson(r.getRoleView(), CrossRoleView.class);
					if (r.getIntoStage() == 1) {// 增加冠军记录
						CrossLog crossLog = new CrossLog(confT.periodNum, roleView.serverId, roleView.roleId);
						addCrossLog(crossLog);
					}
					// 处理合过服的服务器ID
					if (this.targetServerIdMap.containsKey(roleView.serverId)) {
						roleView.serverId = this.targetServerIdMap.get(roleView.serverId);
					}
					CrossServerPrx prx = IceEntry.getCrossServerPrx(roleView.serverId);
					if (prx != null) {
						prx.begin_sendCrossAward(r.getIntoStage(), new String[] { roleView.roleId });
					}
				}
			}
		}
	}

	/**
	 * 异步增加冠军记录
	 * 
	 * @param rank
	 */
	public void addCrossLog(final CrossLog crossLog) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossLogDAO crossLogDAO = CrossLogDAO.getFromApplicationContext(CrossServerMain.getAC());
				crossLogDAO.save(crossLog);
			}
		});
	}

	/**
	 * 加载机器人
	 */
	public void loadRobot() {
		CrossServerPrx prx = IceEntry.getRandomServerPrx();
		if (prx != null) {
			CrossRankItem[] robots = prx.getRobot(50);
			for (CrossRankItem r : robots) {
				r.score = winRobotScore.get(NumberUtil.random(winRobotScore.size()));
				this.robotMap.put(r.roleView.roleId, r);
				// addRank(r.roleView, r.pvpView);
			}
		}
	}

	/**
	 * 随机获取一个机器人ID
	 * 
	 * @return
	 */
	public String getRandomRobotId() {
		String[] keys = robotMap.keySet().toArray(new String[0]);
		return keys[NumberUtil.random(keys.length)];
	}

	/**
	 * 押注
	 * 
	 * @param roleId
	 */
	public void crossBet(int serverId, String roleId) {
		int crossId = getCrossIdByServerId(serverId);
		CrossRank rank = getCrossRank(crossId, roleId);
		if (rank != null) {
			rank.setToastNum(rank.getToastNum() + 1);
			updateCrossRank(rank);
		}
	}

	/**
	 * 获取玩家阵容
	 * 
	 * @param serverId
	 * @param roleId
	 * @return
	 */
	public PvpOpponentFormationView getRoleFormationView(int serverId, String roleId) {
		int crossId = getCrossIdByServerId(serverId);
		CrossRank rank = getCrossRank(crossId, roleId);
		if (rank != null) {
			return rank.getFormationView();
		} else {
			if (robotMap.containsKey(roleId)) {
				return robotMap.get(roleId).pvpView;
			}
			return null;
		}
	}

	/**
	 * 获取最新合服id数据
	 */
	public void refreshTargetServerList() {
		String result = HttpUtil.sendPost(this.mergerServerHttpUrl, "");
		if (TextUtil.isNotBlank(result)) {
			IntIntPair[] is = TextUtil.GSON.fromJson(result, IntIntPair[].class);
			this.targetServerIdMap.clear();
			for (IntIntPair i : is) {
				this.targetServerIdMap.put(i.first, i.second);
			}
		}
	}

	/**
	 * 按战力倒序
	 * 
	 * @param ranks
	 */
	public void sortByPower(List<CrossRank> ranks) {
		Collections.sort(ranks, new Comparator<CrossRank>() {

			@Override
			public int compare(CrossRank o1, CrossRank o2) {
				return o2.getFormationView().view.battlePower - o1.getFormationView().view.battlePower;
			}
		});
	}
}
