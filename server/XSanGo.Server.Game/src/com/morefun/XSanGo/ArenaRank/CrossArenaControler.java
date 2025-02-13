package com.morefun.XSanGo.ArenaRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Ice.LocalException;

import com.XSanGo.Protocol.AMD_ArenaRank_crossFight;
import com.XSanGo.Protocol.AMD_ArenaRank_crossRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_enterCrossArena;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossRank;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossReport;
import com.XSanGo.Protocol.AMD_ArenaRank_refreshCrossRival;
import com.XSanGo.Protocol.AMD_ArenaRank_saveBattle;
import com.XSanGo.Protocol.ArenaReportView;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_endFight;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_getArenaRank;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_getCrossArenaPvpView;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_getRoleRivalRank;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_refreshRival;
import com.XSanGo.Protocol.Callback_CrossArenaCallback_updateArena;
import com.XSanGo.Protocol.CrossArenaCallbackPrx;
import com.XSanGo.Protocol.CrossArenaPvpView;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FightMovieByteView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.FightResult;
import com.XSanGo.Protocol.FightResultView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OwnRank;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalRank;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCrossArena;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.SensitiveWordManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class CrossArenaControler implements ICrossArenaControler {
	private IRole iRole;
	private Role role;
	private Date cdDate;
	/**
	 * 刷新对手间隔
	 */
	private static int REFRESH_CD = 10;
	private long lastUpdateTime;

	/**
	 * 对手缓存
	 */
	private RivalRank[] rivalArr;

	public CrossArenaControler(IRole roleRt, Role roleDB) {
		this.iRole = roleRt;
		this.role = roleDB;
		if (role.getCrossArena() == null) {
			RoleCrossArena crossArena = new RoleCrossArena(role, "", 0, 0, 0, 0, 0, 0, null);
			role.setCrossArena(crossArena);
		}
	}

	@Override
	public void enterCrossArena(final AMD_ArenaRank_enterCrossArena __cb) throws NoteException {
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		final CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		if (conf.isOpen == 0 || iRole.getLevel() < conf.openLevel) {
			__cb.ice_exception(new NoteException("cross arena is not open"));
			return;
		}
		refreshChallengeNum();
		// 保存 用户竞技场 排行榜
		final CrossArenaCallbackPrx call = CrossArenaManager.getInstance().getCrossArenaCbPrx();

		final RoleCrossArena crossArena = role.getCrossArena();
		int cd = 0;
		if (cdDate != null) {
			int passSecond = (int) ((System.currentTimeMillis() - cdDate.getTime()) / 1000);
			if (passSecond < conf.cdSecond) {
				cd = conf.cdSecond - passSecond;
			}
		}
		final int fcd = cd;

		call.begin_getRoleRivalRank(iRole.getRoleId(), new Callback_CrossArenaCallback_getRoleRivalRank() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			}

			@Override
			public void response(final RivalRank rivalRank) {
				if (TextUtil.isBlank(rivalRank.id)) {
					String fid = iRole.getFormationControler().getDefaultFormation().getId();
					PvpOpponentFormationView pvpView = iRole.getFormationControler().getPvpOpponentFormationView(fid);
					RivalRank rank = new RivalRank(iRole.getRoleId(), iRole.getName(), iRole.getHeadImage(), iRole
							.getLevel(), iRole.getVipLevel(), iRole.getSex(), 0, pvpView.view.battlePower, "", 0, 0, 0,
							"", "", "", iRole.getFormationControler().getDefaultFormation().getSummaryView(), iRole
									.getFormationControler().getDefaultFormation().getSupportSummaryView(), iRole
									.getServerId());
					call.begin_updateArena(rank, pvpView, new Callback_CrossArenaCallback_updateArena() {

						@Override
						public void exception(LocalException __ex) {
							__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
						}

						@Override
						public void response(final RivalRank __ret) {
							if (rivalArr == null) {
								call.begin_refreshRival(iRole.getRoleId(),
										new Callback_CrossArenaCallback_refreshRival() {

											@Override
											public void exception(LocalException __ex) {
												__cb.ice_exception(new NoteException(Messages
														.getString("CrossArenaControler.weihu")));
											}

											@Override
											public void response(RivalRank[] ret) {
												rivalArr = ret;
												OwnRank view = new OwnRank(__ret.rank, 0, __ret.sneerStr, "",
														__ret.attack, __ret.guard, crossArena.getChallenge(),
														crossArena.getChallengeBuy(), fcd, 0, 0, ret);
												__cb.ice_response(LuaSerializer.serialize(view));
											}
										});
							} else {
								OwnRank view = new OwnRank(__ret.rank, 0, __ret.sneerStr, "", __ret.attack,
										__ret.guard, crossArena.getChallenge(), crossArena.getChallengeBuy(), fcd, 0,
										0, rivalArr);
								__cb.ice_response(LuaSerializer.serialize(view));
							}
						}
					});
				} else {
					if (rivalArr == null) {
						call.begin_refreshRival(iRole.getRoleId(), new Callback_CrossArenaCallback_refreshRival() {

							@Override
							public void exception(LocalException __ex) {
								__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
							}

							@Override
							public void response(RivalRank[] ret) {
								rivalArr = ret;
								OwnRank view = new OwnRank(rivalRank.rank, 0, rivalRank.sneerStr, "", rivalRank.attack,
										rivalRank.guard, crossArena.getChallenge(), crossArena.getChallengeBuy(), fcd,
										0, 0, ret);
								__cb.ice_response(LuaSerializer.serialize(view));
							}
						});
					} else {
						OwnRank view = new OwnRank(rivalRank.rank, 0, rivalRank.sneerStr, "", rivalRank.attack,
								rivalRank.guard, crossArena.getChallenge(), crossArena.getChallengeBuy(), fcd, 0, 0,
								rivalArr);
						__cb.ice_response(LuaSerializer.serialize(view));
					}
				}
			}
		});
	}

	/**
	 * 刷新调整次数和购买次数
	 */
	private void refreshChallengeNum() {
		CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		RoleCrossArena crossArena = role.getCrossArena();
		if (crossArena.getRefreshDate() == null
				|| DateUtil.isPass(conf.resetTime, "HH:mm:ss", crossArena.getRefreshDate())) {
			crossArena.setChallenge(conf.challengeNum);
			crossArena.setChallengeBuy(0);
			crossArena.setRefreshDate(new Date());
		}
	}

	@Override
	public void refreshCrossRival(final AMD_ArenaRank_refreshCrossRival __cb) throws NoteException {
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		long current = System.currentTimeMillis();
		if ((current - lastUpdateTime) < TimeUnit.SECONDS.toMillis(REFRESH_CD)) {
			int passSecond = (int) ((current - lastUpdateTime) / 1000);
			__cb.ice_exception(new NoteException(TextUtil.format(Messages.getString("CrossArenaControler.refreshCd"),
					REFRESH_CD - passSecond)));
			return;
		}
		lastUpdateTime = current;
		CrossArenaManager.getInstance().getCrossArenaCbPrx()
				.begin_refreshRival(iRole.getRoleId(), new Callback_CrossArenaCallback_refreshRival() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
					}

					@Override
					public void response(RivalRank[] __ret) {
						rivalArr = __ret;
						__cb.ice_response(LuaSerializer.serialize(__ret));
					}
				});
	}

	@Override
	public void getCrossRank(final AMD_ArenaRank_getCrossRank __cb) throws NoteException {
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		CrossArenaManager.getInstance().getCrossArenaCbPrx()
				.begin_getArenaRank(conf.rankSize, new Callback_CrossArenaCallback_getArenaRank() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
					}

					@Override
					public void response(RivalRank[] __ret) {
						__cb.ice_response(LuaSerializer.serialize(__ret));
					}
				});
	}

	@Override
	public void setSignature(String signature) throws NoteException {
		if (SensitiveWordManager.getInstance().hasSensitiveWord(signature)) {
			throw new NoteException(Messages.getString("FactionControler.29"));
		}
		if (signature == null || signature.length() > 75) {
			throw new NoteException(Messages.getString("FactionControler.30"));
		}
		try {
			iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, 50));
		} catch (NotEnoughMoneyException e) {
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
		}
		CrossArenaManager.getInstance().getCrossArenaCbPrx().begin_setSignature(iRole.getRoleId(), signature);
	}

	@Override
	public void saveBattle(AMD_ArenaRank_saveBattle __cb) {
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		IFormation formation = iRole.getFormationControler().getDefaultFormation();
		PvpOpponentFormationView pvpView = iRole.getFormationControler().getPvpOpponentFormationView(formation.getId());
		RivalRank rank = new RivalRank(iRole.getRoleId(), iRole.getName(), iRole.getHeadImage(), iRole.getLevel(),
				iRole.getVipLevel(), iRole.getSex(), 0, pvpView.view.battlePower, iRole.getFactionControler()
						.getFactionName(), 0, 0, 0, "", "", formation.getBuff() == null ? "" : formation.getBuff()
						.getTemplate().getId(), formation.getSummaryView(), formation.getSupportSummaryView(),
				iRole.getServerId());
		CrossArenaManager.getInstance().getCrossArenaCbPrx().begin_updateArena(rank, pvpView);
		if (__cb != null) {
			__cb.ice_response();
		}
	}

	@Override
	public void buyCrossChallenge() throws NoteException {
		RoleCrossArena crossArena = role.getCrossArena();
		CrossArenaBuyT challengeT = CrossArenaManager.getInstance().getCrossArenaBuyT(crossArena.getChallengeBuy() + 1);
		if (challengeT != null && challengeT.vipLv <= iRole.getVipLevel()) {
			if (crossArena.getChallenge() <= 0) {
				// 元宝变化
				try {
					iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, challengeT.cost));
				} catch (NotEnoughMoneyException e) {
				} catch (NotEnoughYuanBaoException e) {
					throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
				}
				crossArena.setChallenge(challengeT.addNum);
				crossArena.setChallengeBuy(crossArena.getChallengeBuy() + 1);
			} else {
				throw new NoteException(Messages.getString("ArenaRankControler.9"));
			}
		} else {
			throw new NoteException(Messages.getString("ArenaRankControler.10"));
		}
	}

	@Override
	public void getCrossReport(AMD_ArenaRank_getCrossReport __cb) throws NoteException {
		CrossArenaLog[] logs = TextUtil.GSON.fromJson(role.getCrossArena().getCrossArenaLogs(), CrossArenaLog[].class);
		List<ArenaReportView> view = new ArrayList<ArenaReportView>();
		for (CrossArenaLog l : logs) {
			view.add(new ArenaReportView(l.rivalId, l.name, l.level, l.icon, l.vip, l.sex, l.rankCurrent, l.flag,
					l.combat, l.rankChange, 0, new Property[0], (System.currentTimeMillis() - l.fightTime) / 1000, "",
					"", l.movieId, l.type, l.serverId, l.signature));
		}
		__cb.ice_response(LuaSerializer.serialize(view));
	}

	@Override
	public void crossFight(final AMD_ArenaRank_crossFight __cb, final String rivalId) throws NoteException {
		rivalArr = null;
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		final RoleCrossArena crossArena = role.getCrossArena();
		final CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		
		final int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.CrossArena.ordinal()).id;
		final CrossArenaCallbackPrx call = CrossArenaManager.getInstance().getCrossArenaCbPrx();
		call.begin_getCrossArenaPvpView(iRole.getRoleId(), rivalId,
				new Callback_CrossArenaCallback_getCrossArenaPvpView() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
					}

					@Override
					public void response(CrossArenaPvpView[] ret) {
						final CrossPvpView pvpView = new CrossPvpView(type, ret[0].roleView, ret[0].pvpView,
								ret[1].roleView, ret[1].pvpView, "", 0);

						MovieThreads.execute(new Runnable() {

							@Override
							public void run() {
								final String str = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
										TextUtil.GSON.toJson(pvpView));
								LogicThread.execute(new Runnable() {

									@Override
									public void run() {
										if (crossArena.getChallenge() <= 0) {
											__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.19")));
											return;
										}
										if (cdDate != null) {
											int passSecond = (int) ((System.currentTimeMillis() - cdDate.getTime()) / 1000);
											if (passSecond < conf.cdSecond) {
												__cb.ice_exception(new NoteException(Messages.getString("WorldBossControler.cdNotChallenge")));
												return;
											}
										}
										
										final CrossMovieView movie = TextUtil.GSON.fromJson(str, CrossMovieView.class);
										if (movie == null) {
											__cb.ice_exception(new NoteException(Messages
													.getString("FactionControler.59")));
											return;
										}
										XsgLadderManager.getInstance().replaceNull(movie);

										final boolean isWin = iRole.getRoleId().equals(movie.winRoleId);
										int flag = isWin ? 1 : 0;// 胜利失败
										final int star = isWin ? XsgCopyManager.getInstance().calculateStar(
												(byte) pvpView.leftPvpView.heros.length, (byte) movie.selfHeroNum) : 0;
										final String movieId = GlobalDataManager.getInstance().generatePrimaryKey();
										FightMovieView movieView = new FightMovieView(flag, star, movie.soloMovie,
												movie.fightMovie, new byte[0]);
										
										cdDate = new Date();
										
										call.begin_endFight(iRole.getRoleId(), isWin, rivalId, movieId, movieView,
												new Callback_CrossArenaCallback_endFight() {

													@Override
													public void exception(LocalException __ex) {
														__cb.ice_exception(new NoteException(Messages
																.getString("CrossArenaControler.weihu")));
													}

													@Override
													public void response(IntIntPair __ret) {
														int firstNum = 0;
														if (isWin
																&& (crossArena.getChallengeDate() == null || DateUtil
																		.checkTime(crossArena.getChallengeDate(),
																				DateUtil.joinTime(conf.resetTime)))) {
															firstNum = CrossArenaManager.getInstance().getFirstWinItem(
																	__ret.first);
															try {
																iRole.winJinbi(firstNum);
															} catch (NotEnoughMoneyException e) {
															}
														}
														FightResult re = new FightResult(0, 0, __ret.first, firstNum,
																__ret.second, 0, 0, star, movieId);
														FightResultView view = new FightResultView(re,
																new FightMovieByteView[] { new FightMovieByteView(
																		movie.soloMovie, movie.fightMovie) },
																movie.winType);
														
														crossArena.setChallenge(crossArena.getChallenge() - 1);
														crossArena.setChallengeDate(new Date());
														
														__cb.ice_response(view);
													}
												});
									}
								});
							}
						});
					}
				});

	}

	@Override
	public void addCrossArenaLog(ArenaReportView report) {
		CrossArenaLog arenaLog = new CrossArenaLog(report.fightMovieId, report.name, report.level, report.icon,
				report.vip, report.sex, report.serverId, report.compositeCombat, report.rank, report.rankChange,
				report.flag, report.id, System.currentTimeMillis(), report.type, report.signature);
		CrossArenaLog[] logs = TextUtil.GSON.fromJson(role.getCrossArena().getCrossArenaLogs(), CrossArenaLog[].class);
		List<CrossArenaLog> logList = new ArrayList<CrossArenaLog>(Arrays.asList(logs));
		logList.add(0, arenaLog);

		CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		if (logList.size() > conf.reportSize) {
			logList.remove(logList.size() - 1);
		}
		role.getCrossArena().setCrossArenaLogs(TextUtil.GSON.toJson(logList.toArray(new CrossArenaLog[0])));
		
		this.rivalArr = null;// 打完后清空对手缓存
	}

	@Override
	public void crossRevenge(final AMD_ArenaRank_crossRevenge __cb, final String rivalId) throws NoteException {
		rivalArr = null;
		if (!CrossArenaManager.getInstance().isUsable()) {
			__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
			return;
		}
		final RoleCrossArena crossArena = role.getCrossArena();
		final CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		
		final int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.CrossArena.ordinal()).id;
		final CrossArenaCallbackPrx call = CrossArenaManager.getInstance().getCrossArenaCbPrx();
		call.begin_getCrossArenaPvpView(iRole.getRoleId(), rivalId,
				new Callback_CrossArenaCallback_getCrossArenaPvpView() {

					@Override
					public void exception(LocalException __ex) {
						__cb.ice_exception(new NoteException(Messages.getString("CrossArenaControler.weihu")));
					}

					@Override
					public void response(CrossArenaPvpView[] ret) {
						int rankRange[] = filterRankRange(ret[0].rank);
						// 检查复仇上限
						if (ret[1].rank < rankRange[0]) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.LevelTooHigh")));
							return;
						}
						if (ret[0].rank < ret[1].rank) {
							__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.17")));
							return;
						}
						final CrossPvpView pvpView = new CrossPvpView(type, ret[0].roleView, ret[0].pvpView,
								ret[1].roleView, ret[1].pvpView, "", 0);

						MovieThreads.execute(new Runnable() {

							@Override
							public void run() {
								final String str = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
										TextUtil.GSON.toJson(pvpView));
								LogicThread.execute(new Runnable() {

									@Override
									public void run() {
										if (crossArena.getChallenge() <= 0) {
											__cb.ice_exception(new NoteException(Messages.getString("ArenaRankControler.19")));
											return;
										}
										if (cdDate != null) {
											int passSecond = (int) ((System.currentTimeMillis() - cdDate.getTime()) / 1000);
											if (passSecond < conf.cdSecond) {
												__cb.ice_exception(new NoteException(Messages.getString("WorldBossControler.cdNotChallenge")));
												return;
											}
										}
										
										final CrossMovieView movie = TextUtil.GSON.fromJson(str, CrossMovieView.class);
										if (movie == null) {
											__cb.ice_exception(new NoteException(Messages
													.getString("FactionControler.59")));
											return;
										}
										XsgLadderManager.getInstance().replaceNull(movie);

										final boolean isWin = iRole.getRoleId().equals(movie.winRoleId);
										int flag = isWin ? 1 : 0;// 胜利失败
										final int star = isWin ? XsgCopyManager.getInstance().calculateStar(
												(byte) pvpView.leftPvpView.heros.length, (byte) movie.selfHeroNum) : 0;
										final String movieId = GlobalDataManager.getInstance().generatePrimaryKey();
										FightMovieView movieView = new FightMovieView(flag, star, movie.soloMovie,
												movie.fightMovie, new byte[0]);

										cdDate = new Date();

										call.begin_endFight(iRole.getRoleId(), isWin, rivalId, movieId, movieView,
												new Callback_CrossArenaCallback_endFight() {

													@Override
													public void exception(LocalException __ex) {
														__cb.ice_exception(new NoteException(Messages
																.getString("CrossArenaControler.weihu")));
													}

													@Override
													public void response(IntIntPair __ret) {
														int firstNum = 0;
														if (isWin
																&& (crossArena.getChallengeDate() == null || DateUtil
																		.checkTime(crossArena.getChallengeDate(),
																				DateUtil.joinTime(conf.resetTime)))) {
															firstNum = CrossArenaManager.getInstance().getFirstWinItem(
																	__ret.first);
															try {
																iRole.winJinbi(firstNum);
															} catch (NotEnoughMoneyException e) {
															}
														}
														FightResult re = new FightResult(0, 0, __ret.first, firstNum,
																__ret.second, 0, 0, star, movieId);
														FightResultView view = new FightResultView(re,
																new FightMovieByteView[] { new FightMovieByteView(
																		movie.soloMovie, movie.fightMovie) },
																movie.winType);
														
														crossArena.setChallenge(crossArena.getChallenge() - 1);
														crossArena.setChallengeDate(new Date());
														
														__cb.ice_response(view);
													}
												});
									}
								});
							}
						});
					}
				});
	}

	@Override
	public void clearCrossCD() throws NoteException {
		CrossArenaConfT conf = CrossArenaManager.getInstance().getCrossArenaConfT();
		if (cdDate != null) {
			int passSecond = (int) ((System.currentTimeMillis() - cdDate.getTime()) / 1000);
			if (passSecond < conf.cdSecond) {
				try {
					iRole.reduceCurrency(new Money(CurrencyType.Yuanbao, conf.clearCdYuanbao));
				} catch (NotEnoughMoneyException e) {
				} catch (NotEnoughYuanBaoException e) {
					throw new NoteException(Messages.getString("WorldBossControler.notYuanbao"));
				}
				cdDate = null;
			}
		}
	}

	/**
	 * 获取某个等级的匹配范围
	 * 
	 * @param rank
	 *            对应的等级
	 */
	private int[] filterRankRange(int rank) {
		int[] resRank = new int[] { 0, 0 };
		List<Integer> list = new ArrayList<Integer>();

		for (CrossArenaMatchT matchT : CrossArenaManager.getInstance().getCrossArenaMatch()) {
			if (rank >= matchT.start) {
				list.add(calcRank(rank, matchT.oneStart));
				list.add(calcRank(rank, matchT.oneEnd));
				list.add(calcRank(rank, matchT.twoStart));
				list.add(calcRank(rank, matchT.twoEnd));
				list.add(calcRank(rank, matchT.threeStart));
				list.add(calcRank(rank, matchT.threeEnd));
				break;
			}
		}

		// 从小到大排序
		Collections.sort(list, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		resRank[0] = list.get(0);
		resRank[1] = list.get(list.size() - 1);
		return resRank;
	}

	// 匹配等级小于0，需要关联自身的等级
	private int calcRank(int roleRank, int matchRank) {
		if (matchRank > 0) {
			return matchRank;
		} else {
			return roleRank + matchRank;
		}
	}
}
