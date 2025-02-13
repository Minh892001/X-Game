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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_Ladder_autoFight;
import com.XSanGo.Protocol.AMD_Ladder_beginFight;
import com.XSanGo.Protocol.AMD_Ladder_selectLadder;
import com.XSanGo.Protocol.AMD_Ladder_showRankList;
import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.LadderAutoFightResult;
import com.XSanGo.Protocol.LadderFightResult;
import com.XSanGo.Protocol.LadderPvpView;
import com.XSanGo.Protocol.LadderRankListShow;
import com.XSanGo.Protocol.LadderRankListSub;
import com.XSanGo.Protocol.LadderReport;
import com.XSanGo.Protocol.LadderView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.MovieThreads;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleLadder;
import com.morefun.XSanGo.db.game.RoleLadderReport;
import com.morefun.XSanGo.event.protocol.ILadderBuy;
import com.morefun.XSanGo.event.protocol.ILadderFight;
import com.morefun.XSanGo.event.protocol.ILadderLevelChange;
import com.morefun.XSanGo.event.protocol.ILadderReward;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.Type;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 群雄争霸 功能
 * 
 * @author 吕明涛
 */
@RedPoint
class LadderControler implements ILadderControler {

	private static final Log log = LogFactory.getLog(LadderControler.class);

	private IRole roleRt;
	private Role roleDb;

	/** 部队的武将数量，计算战斗后的星级 */
	private byte orignalHeroCount;

	/** 挑战 */
	private ILadderFight eventFight;
	/** 购买挑战挑战次数 */
	private ILadderBuy eventBuyChallenge;
	/** 领取奖励 */
	private ILadderReward eventReward;
	/** 战报上下文ID */
	private String fightMovieIdContext = null;

	private ILadderLevelChange ladderLevelChangeEvent;

	/**
	 * 挑战对手ID，用来控制是否异常退出
	 */
	private String rivalId;

	public LadderControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.eventFight = this.roleRt.getEventControler().registerEvent(ILadderFight.class);
		this.eventBuyChallenge = this.roleRt.getEventControler().registerEvent(ILadderBuy.class);
		this.eventReward = this.roleRt.getEventControler().registerEvent(ILadderReward.class);
		ladderLevelChangeEvent = roleRt.getEventControler().registerEvent(ILadderLevelChange.class);
	}

	/**
	 * 客户端红点显示
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean note = false;
		try {
			checkStartLadder();
		} catch (NoteException e) {
			return null;
		}
		if (getLadder() == null) {
			return null;
		}
		// 是否有新战报
		for (RoleLadderReport report : getLadderReportList()) {
			if (report.getFightTime().after(getLadder().getShowReportDate())) {
				note = true;
				break;
			}
		}

		// 是否可以领取奖励
		if (!note) {
			if (getLadder() != null) {
				@SuppressWarnings("unchecked")
				Map<String, String> rewardMap = TextUtil.GSON.fromJson(getLadder().getRewardStr(), HashMap.class);
				for (String flag : rewardMap.values()) {
					if (flag.equals("1")) { //$NON-NLS-1$
						note = true;
						break;
					}
				}
			}
		}

		return note ? new MajorUIRedPointNote(MajorMenu.LadderMenu, false) : null;
	}

	@Override
	public void selectLadder(final AMD_Ladder_selectLadder __cb) throws NoteException {
		checkStartLadder();

		final LadderView ladderView = new LadderView();
		// 角色自身 群雄争霸 数据
		RoleLadder ladderDb = this.getLadder();
		// 群雄争霸数据 不存在
		if (ladderDb == null) {
			ladderDb = initLadder(XsgLadderManager.getInstance().getInitT().initLevel, XsgLadderManager.getInstance()
					.getInitT().initStar);
		} else {
			// 赛季结束，清除群雄争霸的数据
			ladderDb = this.clearLadder();

			// 每天 挑战次数重置
			if (DateUtil.checkTime(ladderDb.getChallengeBuyDate(),
					DateUtil.joinTime(XsgLadderManager.getInstance().getInitT().interval))) {
				ladderDb.setChallengeRemain(XsgLadderManager.getInstance().getInitT().challengeNum);
				ladderDb.setChallengeBuyNum(0);
				ladderDb.setChallengeBuyDate(new Date());
			}

			ladderDb.setShowReportDate(new Date());
		}

		// 初始化老数据积分
		if (ladderDb.getLadderScore() == 0) {
			int score = XsgLadderManager.getInstance().getLevelScore(ladderDb.getLadderLevel(),
					ladderDb.getLadderStar());
			ladderDb.setLadderScore(score);
		}
		// 保存群雄争霸数据
		this.roleDb.setLadder(ladderDb);

		// 检测上次有异常退出作为失败处理
		if (this.rivalId != null) {
			this.endFight(rivalId, 0, (byte) 0);
		}

		// 角色自身数据
		ladderView.ladderLevel = ladderDb.getLadderLevel();
		ladderView.ladderStar = ladderDb.getLadderStar();
		ladderView.guardId = ladderDb.getGuardId();
		ladderView.challengeRemain = ladderDb.getChallengeRemain();
		ladderView.challengeBuyNum = ladderDb.getChallengeBuyNum();
		ladderView.rewardStr = ladderDb.getRewardStr();
		ladderView.remainDate = calcRemainDate();
		ladderView.rankNum = XsgLadderManager.getInstance().getLadderRank(roleRt.getRoleId());
		ladderView.ladderScore = ladderDb.getLadderScore();

		// 群雄争霸 战报 数据
		final List<RoleLadderReport> reportList = this.roleDb.getLadderReportList();
		if (reportList.size() > 0) {

			// 根据时间 对战报排序
			Comparator<RoleLadderReport> comparator = new Comparator<RoleLadderReport>() {
				public int compare(RoleLadderReport s1, RoleLadderReport s2) {
					return s2.getFightTime().compareTo(s1.getFightTime());
				};
			};
			Collections.sort(reportList, comparator);

			// 查询战报对手的ID
			final List<String> reportIdList = new ArrayList<String>(reportList.size());
			for (RoleLadderReport LadderReport : reportList) {
				reportIdList.add(LadderReport.getRivalId());
			}

			XsgRoleManager.getInstance().loadRoleAsync(reportIdList, new Runnable() {
				@Override
				public void run() {
					List<LadderReport> reportShow = new ArrayList<LadderReport>();
					for (int i = 0; i < reportIdList.size(); i++) {
						IRole findRole = XsgRoleManager.getInstance().findRoleById(reportIdList.get(i));
						if (findRole == null) {
							continue;
						}
						LadderReport t = new LadderReport();
						t.id = findRole.getRoleId();
						t.name = findRole.getName();
						t.icon = findRole.getHeadImage();
						t.vipLevel = findRole.getVipLevel();
						t.ladderLevel = reportList.get(i).getRivalLevel();
						t.levelChange = reportList.get(i).getLevelChange();
						t.starChange = reportList.get(i).getStarChange();
						t.state = reportList.get(i).getState();
						t.reportTime = (System.currentTimeMillis() - reportList.get(i).getFightTime().getTime()) / 1000;
						t.movieId = reportList.get(i).getFightId();
						t.combat = findRole.getFormationControler().getDefaultFormation().calculateBattlePower();
						t.level = findRole.getLevel();
						reportShow.add(t);
					}

					ladderView.reportList = reportShow.toArray(new LadderReport[0]);
					__cb.ice_response(LuaSerializer.serialize(ladderView));
				}
			});
		} else {
			__cb.ice_response(LuaSerializer.serialize(ladderView));
		}
	}

	@Override
	public void showRankList(final AMD_Ladder_showRankList __cb) throws NoteException {
		checkStartLadder();
		clearLadder();
		// 默认排名，没有进入排行榜
		final LadderRankListShow resLadderShow = new LadderRankListShow();
		resLadderShow.ownRank = XsgLadderManager.getInstance().getLadderRank(this.roleRt.getRoleId());
		resLadderShow.ladderLevel = this.getLadder().getLadderLevel();
		resLadderShow.ladderStar = this.getLadder().getLadderStar();
		resLadderShow.groupName = DateUtil.format(getLadder().getChangeLevelDate(),
				Messages.getString("LadderControler.rankTime"));
		resLadderShow.ladderScore = getLadder().getLadderScore();
		int showNum = XsgLadderManager.getInstance().getInitT().rankNum;
		// 判断显示 排行榜的数量
		int rankListNum = XsgLadderManager.getInstance().getLadderRankListNum();
		if (showNum > rankListNum) {
			showNum = rankListNum;
		}
		// 设置显示 排行榜的详细信息
		final LadderRankListSub[] rankArr = new LadderRankListSub[showNum];
		List<String> roleIds = new ArrayList<String>();
		for (int i = 0; i < showNum; i++) {
			LadderRankListSub sub = XsgLadderManager.getInstance().getLadderRankList(i);
			roleIds.add(sub.roleId);
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < rankArr.length; i++) {
					rankArr[i] = XsgLadderManager.getInstance().getLadderRankList(i);
					rankArr[i].rank = i + 1;
					// 自己在排名范围内，重新设置排名
					if (rankArr[i].roleId.equals(roleRt.getRoleId())) {
						resLadderShow.ownRank = i + 1;
					}
					IRole role = XsgRoleManager.getInstance().findRoleById(rankArr[i].roleId);
					if (role != null) {
						rankArr[i].vipLevel = role.getVipLevel();
						rankArr[i].roleName = role.getName();
					}
					rankArr[i].groupName = DateUtil.toString(rankArr[i].rankTime,
							Messages.getString("LadderControler.rankTime"));
					if (rankArr[i].ladderScore == 0) {
						int score = XsgLadderManager.getInstance().getLevelScore(rankArr[i].ladderLevel,
								rankArr[i].ladderStar);
						rankArr[i].ladderScore = score;
					}
				}
				resLadderShow.rankList = rankArr;
				__cb.ice_response(LuaSerializer.serialize(resLadderShow));
			}
		});
	}

	@Override
	public void saveGuard(String guardId) throws NoteException {
		checkStartLadder();

		int heroCount = this.roleRt.getFormationControler().getFormation(guardId).getHeroCountExcludeSupport();

		// 保存的防守部队中，武将数量必须大于0
		if (heroCount > 0) {
			this.getLadder().setGuardId(guardId);
		} else {
			throw new NoteException(Messages.getString("LadderControler.1")); //$NON-NLS-1$
		}
	}

	@Override
	public void buyChallenge() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		checkFightLadder();
		LadderChallengeBuyT buyT = XsgLadderManager.getInstance().getChallengeBuyMap(
				getLadder().getChallengeBuyNum() + 1);
		if (buyT == null || buyT.vipLv > roleRt.getVipLevel()) {
			throw new NoteException(Messages.getString("LadderControler.2")); //$NON-NLS-1$
		}

		if (getLadder().getChallengeRemain() > 0) {
			throw new NoteException(Messages.getString("LadderControler.3")); //$NON-NLS-1$
		}

		this.roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, buyT.costYuanbao));
		// 更新 购买次数 和 剩余挑战次数
		getLadder().setChallengeBuyNum(getLadder().getChallengeBuyNum() + 1);
		getLadder().setChallengeRemain(buyT.obtain);
		// 添加事件
		eventBuyChallenge.onBuy(buyT.time, getLadder().getChallengeBuyNum());
	}

	@Override
	public void reward(int rewardId) throws NoteException {
		@SuppressWarnings("unchecked")
		Map<String, String> rewardMap = TextUtil.GSON.fromJson(getLadder().getRewardStr(), HashMap.class);
		LadderLevelAwardT levelAwardT = XsgLadderManager.getInstance().getLevelAwardMap(rewardId);
		if (levelAwardT == null || !rewardMap.get(String.valueOf(rewardId)).equals("1")) { //$NON-NLS-1$
			throw new NoteException(Messages.getString("LadderControler.5")); //$NON-NLS-1$
		}

		// 保存领取的奖励
		rewardMap.put(String.valueOf(rewardId), "2"); //$NON-NLS-1$
		getLadder().setRewardStr(TextUtil.GSON.toJson(rewardMap));

		// 获得奖励的物品
		try {
			roleRt.winJinbi(levelAwardT.gold);
			roleRt.winYuanbao(levelAwardT.Yuanbao, true);
			roleRt.getItemControler().changeItemByTemplateCode(levelAwardT.item, levelAwardT.itemNum);
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}

		// 添加领取事件
		eventReward.onReward(rewardId);
	}

	@Override
	public void beginFight(final AMD_Ladder_beginFight __cb, String formationId) throws NoteException {
		checkFightLadder();
		clearLadder();
		// 本次 赛季是否结束
		if (getLadder().getSeasonEndDate().after(new Date())) {
			if (getLadder().getChallengeRemain() > 0) {
				// 保存选择了，战斗部队武将数量
				this.orignalHeroCount = this.roleRt.getFormationControler().getFormation(formationId)
						.getHeroCountIncludeSupport();
				// 匹配对手
				final String rivalRoleId = matchRivalId();

				// 查询对手的阵容
				XsgRoleManager.getInstance().loadRoleByIdAsync(rivalRoleId, new Runnable() {
					@Override
					public void run() {
						IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalRoleId);
						RoleLadder rivalLadder = rivalRole.getLadderControler().getLadder();

						String rivalFormationId = ""; //$NON-NLS-1$
						if (rivalLadder != null) {
							rivalFormationId = rivalRole.getLadderControler().getLadder().getGuardId();
						} else {
							rivalFormationId = rivalRole.getFormationControler().getDefaultFormation().getId();
						}

						LadderPvpView pvpRival = new LadderPvpView();
						pvpRival.formationView = rivalRole.getFormationControler().getPvpOpponentFormationView(
								rivalFormationId);

						if (pvpRival.formationView.heros.length > 0) {
							pvpRival.roleId = rivalRole.getRoleId();
							pvpRival.roleName = rivalRole.getName();
							pvpRival.icon = rivalRole.getHeadImage();
							pvpRival.vipLevel = rivalRole.getVipLevel();
							pvpRival.sex = rivalRole.getSex();
							pvpRival.level = rivalRole.getLevel();

							// 扣除挑战次数
							getLadder().setChallengeRemain(getLadder().getChallengeRemain() - 1);
							// 存储战报上下文
							fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(
									XsgFightMovieManager.Type.Ladder, roleRt, rivalRole);
							rivalId = rivalRoleId;
							__cb.ice_response(pvpRival);
						} else {
							__cb.ice_exception(new NoteException(Messages.getString("LadderControler.8"))); //$NON-NLS-1$
						}
					}
				}, new Runnable() {
					@Override
					public void run() {
						__cb.ice_exception(new NoteException(Messages.getString("LadderControler.9"))); //$NON-NLS-1$
					}
				});
			} else {
				throw new NoteException(Messages.getString("LadderControler.10")); //$NON-NLS-1$
			}
		} else {
			throw new NoteException(Messages.getString("LadderControler.11")); //$NON-NLS-1$
		}
	}

	@Override
	public int[] fight(int resFlag) {
		// 先清除过期数据
		clearLadder();
		// 等级 和 星级 变化
		int levelChange = 0, starChange = 0;

		LadderInitT initT = XsgLadderManager.getInstance().getInitT();
		// 不存群雄争霸数据，不进行处理
		if (getLadder() != null) {
			// 初始化老数据积分
			if (getLadder().getLadderScore() == 0) {
				int score = XsgLadderManager.getInstance().getLevelScore(getLadder().getLadderLevel(),
						getLadder().getLadderStar());
				getLadder().setLadderScore(score);
			}
			// 挑战胜利
			if (resFlag == 1) {
				// 1级 满星，顶级不用升级
				if (!(getLadder().getLadderLevel() == 1 && getLadder().getLadderStar() == XsgLadderManager
						.getInstance().getLevelMap(1).star)) {
					// 胜利默认星级升1级，
					starChange = 1;
					// 是否算连胜
					if (getLadder().getWinNum() >= XsgLadderManager.getInstance().getInitT().winNum
							&& getLadder().getLadderLevel() > initT.noWin2Level) {
						starChange = XsgLadderManager.getInstance().getInitT().winStar;
					}

					// 升级，如果星级比较多，循环升级
					int newStar = getLadder().getLadderStar() + starChange;
					int newLevel = getLadder().getLadderLevel();
					while (true) {
						int standardStar = XsgLadderManager.getInstance().getLevelMap(newLevel).star;
						if (newStar <= standardStar) {
							break;
						}
						// 满级不用在循环了
						if (newLevel > 1) {
							newStar -= standardStar + 1;
							newLevel--;
						} else {
							newStar = standardStar;
							break;
						}
					}

					// 等级的变化
					levelChange = newLevel - getLadder().getLadderLevel();

					// 更新等级、星级、 连胜记录 和 是否 可以领取奖励
					getLadder().setLadderLevel(newLevel);
					getLadder().setLadderStar(newStar);
					getLadder().setWinNum(getLadder().getWinNum() + 1);
					getLadder().setRewardStr(setReward(newLevel));
					getLadder().setChangeLevelDate(new Date());
					getLadder().setLadderScore(getLadder().getLadderScore() + starChange);
				} else {// 满星满级胜利+1分
					getLadder().setLadderScore(getLadder().getLadderScore() + 1);
				}
			} else {
				// 失败，默认星级降1星
				int scoreChange = 0;
				if (getLadder().getLadderLevel() < XsgLadderManager.getInstance().getInitT().deductStarLevel) {
					scoreChange = -1;
					if (getLadder().getLadderScore() <= 92) {
						starChange = -1;
					}
				}
				int newStar = getLadder().getLadderStar() + starChange;

				// 在一定的级别中，只掉星，不掉级
				if (getLadder().getLadderLevel() <= XsgLadderManager.getInstance().getInitT().degrade) {
					if (newStar < 0) {
						// 等级，降1级
						levelChange = 1;
						int newLevel = getLadder().getLadderLevel() + levelChange;
						newStar = XsgLadderManager.getInstance().getLevelMap(newLevel).star;

						// 更新等级
						getLadder().setLadderLevel(newLevel);
					}
					// 更新等级或星级变化的时间
					getLadder().setChangeLevelDate(new Date());
				} else {
					if (newStar < 0) {
						starChange = 0;
						newStar = 0;
						scoreChange = 0;
					}
				}

				// 更新等级、星级和 连胜记录
				getLadder().setLadderStar(newStar);
				getLadder().setWinNum(1);
				getLadder().setLadderScore(getLadder().getLadderScore() + scoreChange);
			}
			// 排行榜变化
			this.onRankChange(getLadder().getLadderLevel(), getLadder().getLadderStar(), getLadder()
					.getChangeLevelDate(), getLadder().getLadderScore());
		}

		// 等级变化，通知客户端，官阶变化
		if (levelChange != 0) {
			roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.RANK_ID, getLadder().getLadderLevel());
			if (levelChange < 0) {
				roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
			} else {
				// 升级后刷新红点
				roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
			}
			ladderLevelChangeEvent.onLevelChange(getLadder().getLadderLevel());
		}

		int[] resChang = new int[2];
		resChang[0] = levelChange;
		resChang[1] = starChange;

		return resChang;
	}

	@Override
	public void saveRport(int resFlag, int levelChange, int starChange, IRole rivalRole, String movieId) {
		if (getLadder() != null) {
			int rivalLadderLevel = XsgLadderManager.getInstance().getInitT().initLevel;
			if (rivalRole.getLadderControler().getLadder() != null) {
				rivalLadderLevel = rivalRole.getLadderControler().getLadder().getLadderLevel();
			}

			// 等级最高的时候，等级和星级变化，特别显示
			if (getLadder().getLadderLevel() == 1
					&& getLadder().getLadderStar() == XsgLadderManager.getInstance().getLevelMap(1).star) {
				levelChange = -1;
				starChange = -1;
				// 星级保护约定返回-2
			} else if (getLadder().getLadderLevel() >= XsgLadderManager.getInstance().getInitT().deductStarLevel
					&& resFlag == 0) {
				levelChange = -2;
				starChange = -2;
			}

			RoleLadderReport report = new RoleLadderReport(GlobalDataManager.getInstance().generatePrimaryKey(),
					roleDb, movieId, levelChange, starChange, resFlag, rivalRole.getRoleId(), rivalLadderLevel,
					new Date());

			addReport(report);
		}
	}

	@Override
	public LadderFightResult endFight(String rivalId, int resFlag, byte remainHero) throws NoteException {
		this.rivalId = null;
		// 跨赛区了
		if (!DateUtil.isSameDay(this.getLadder().getSeasonEndDate(), XsgLadderManager.getInstance().getSeasonEndDate())) {
			throw new NoteException(Messages.getString("LadderControler.ladderNotIn"));
		}
		IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalId);
		if (rivalRole != null) {
			// 战斗处理
			int[] roleChange = this.fight(resFlag);
			int[] rivalChange = rivalRole.getLadderControler().fight(resFlag ^ 1);

			// 保存战报
			this.saveRport(resFlag, roleChange[0], roleChange[1], rivalRole, fightMovieIdContext);
			rivalRole.getLadderControler().saveRport(resFlag ^ 1, rivalChange[0], rivalChange[1], roleRt,
					fightMovieIdContext);

			// 返回客户端 战斗星级
			LadderFightResult fightResult = new LadderFightResult();
			fightResult.fightStar = XsgCopyManager.getInstance().calculateStar(orignalHeroCount, remainHero);
			fightResult.ladderChangerLevel = roleChange[0];
			fightResult.ladderChangerStar = getLadder().getLadderStar();
			fightResult.ladderScore = getLadder().getLadderScore();
			// 生成战报录像ID
			fightResult.movieId = XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(),
					fightMovieIdContext, resFlag, remainHero);
			if (TextUtil.isBlank(fightResult.movieId)) {
				log.error(TextUtil.format(
						Messages.getString("LadderControler.13"), roleRt.getRoleId(), rivalId, resFlag)); //$NON-NLS-1$
			}
			// fightMovieIdContext = null;
			// 添加接口
			eventFight.onFight(rivalId, resFlag, fightResult.fightStar);

			return fightResult;
		} else {
			throw new NoteException(Messages.getString("LadderControler.14")); //$NON-NLS-1$
		}
	}

	public static void main(String[] args) {
		String d = "2015-03-12 00:00:00"; //$NON-NLS-1$
		Date tt = DateUtil.parseDate(d);
		System.out.println(tt.before(new Date()));
		System.out.println(DateUtil.getMonyhDay(Calendar.getInstance()));
		System.out.println(DateUtil.setMonyhDay(12, 1));

	}

	@Override
	public RoleLadder clearLadder() {
		RoleLadder ladderDb = this.getLadder();
		if (ladderDb != null) {
			if (!DateUtil.isSameDay(ladderDb.getSeasonEndDate(), XsgLadderManager.getInstance().getSeasonEndDate())) {
				// if (ladderDb.getSeasonEndDate().before(new Date())) {
				for (LadderDegradeT degradeT : XsgLadderManager.getInstance().getDegradeList()) {
					if (ladderDb.getLadderLevel() >= degradeT.startRank & ladderDb.getLadderLevel() <= degradeT.endRank) {
						ladderDb = initLadder(degradeT.lvStart, 0);

						this.roleDb.setLadder(ladderDb);
						break;
					}
				}
			}
			// 排行榜变化
			this.onRankChange(ladderDb.getLadderLevel(), ladderDb.getLadderStar(), ladderDb.getChangeLevelDate(),
					ladderDb.getLadderScore());
		}

		return ladderDb;
	}

	@Override
	public RoleLadder getLadder() {
		return this.roleDb.getLadder();
	}

	@Override
	public List<RoleLadderReport> getLadderReportList() {
		return this.roleDb.getLadderReportList();
	}

	@Override
	public void addReport(RoleLadderReport report) {
		getLadderReportList().add(0, report);

		// 超过一定数量删除
		if (getLadderReportList().size() > XsgLadderManager.getInstance().getInitT().reportNumber) {
			getLadderReportList().remove(getLadderReportList().size() - 1);
		}

		roleRt.getNotifyControler().onMajorUIRedPointChange(getRedPointNote());
	}

	/**
	 * 检查 是否 可以开启群雄争霸
	 */
	private void checkStartLadder() throws NoteException {
		if (this.roleRt.getLevel() < XsgLadderManager.getInstance().getInitT().level) {
			throw new NoteException(Messages.getString("LadderControler.16") //$NON-NLS-1$
					+ XsgLadderManager.getInstance().getInitT().level + Messages.getString("LadderControler.17")); //$NON-NLS-1$
		}

		// 结算时不能进入
		if (DateUtil.isSameDay(new Date(), XsgLadderManager.getInstance().getSeasonEndDate())) {
			throw new NoteException(Messages.getString("LadderControler.ladderNotIn"));
		}
	}

	/**
	 * 检查 是否 可以 群雄争霸 开始战斗
	 */
	private void checkFightLadder() throws NoteException {
		this.checkStartLadder();

		if (DateUtil.isBetween(new Date(), DateUtil.joinTime(XsgLadderManager.getInstance().getInitT().rewardStarTime),
				DateUtil.joinTime(XsgLadderManager.getInstance().getInitT().rewardEndTime))) {
			throw new NoteException(Messages.getString("LadderControler.18")); //$NON-NLS-1$
		}
	}

	/**
	 * 初始化 群雄争霸的数据
	 * 
	 * @param ladderDb
	 */
	private RoleLadder initLadder(int ladderLeverl, int ladderStart) {
		// 初始化 群雄争霸 数据
		RoleLadder ladderDb = new RoleLadder(roleRt.getRoleId(), roleDb, this.roleRt.getFormationControler()
				.getDefaultFormation().getId(), ladderLeverl, ladderStart, 1,
				XsgLadderManager.getInstance().getInitT().challengeNum, 0, new Date(), initReward(), new Date(),
				XsgLadderManager.getInstance().getSeasonEndDate(), new Date());
		// 排行榜变化
		this.onRankChange(ladderDb.getLadderLevel(), ladderDb.getLadderStar(), ladderDb.getChangeLevelDate(),
				ladderDb.getLadderScore());
		// 清空战报数据
		this.roleDb.setLadderReportList(new ArrayList<RoleLadderReport>());

		// 通知客户端，官阶变化
		roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.RANK_ID, ladderLeverl);

		return ladderDb;
	}

	/**
	 * 初始化奖励 字符串， 0:未完成，1:完成，未领取，2：已经领取
	 * 
	 * @return
	 */
	private String initReward() {
		Map<String, String> rewardMap = new HashMap<String, String>();
		for (LadderLevelAwardT levelAward : XsgLadderManager.getInstance().getLevelAwardList()) {
			rewardMap.put(String.valueOf(levelAward.id), "0"); //$NON-NLS-1$
		}

		return TextUtil.GSON.toJson(rewardMap);
	}

	/**
	 * 等级越小，级别越大，升级后，是否获得奖励
	 * 
	 * @param level
	 * @return
	 */
	private String setReward(int level) {
		@SuppressWarnings("unchecked")
		Map<String, String> rewardMap = TextUtil.GSON.fromJson(getLadder().getRewardStr(), HashMap.class);
		for (LadderLevelAwardT levelAward : XsgLadderManager.getInstance().getLevelAwardList()) {
			if (level <= levelAward.demand && rewardMap.get(String.valueOf(levelAward.id)) != null
					&& rewardMap.get(String.valueOf(levelAward.id)).equals("0")) { //$NON-NLS-1$
				rewardMap.put(String.valueOf(levelAward.id), "1"); //$NON-NLS-1$
			}
		}

		return TextUtil.GSON.toJson(rewardMap);
	}

	/**
	 * 根据群雄争霸排名，来过滤对手，自身排名的 上下5级，不包含自身<br>
	 * 最大或最小排名时，有多少，算多少<br>
	 * 群雄争霸排名只有自己，取得在线角色的随机数据
	 * 
	 * @return 对手ID
	 */
	private String matchRivalId() {
		// 指定等级固定匹配机器人
		if (getLadder().getLadderLevel() >= XsgLadderManager.getInstance().getInitT().matchRobotLevel) {
			int index = NumberUtil.random(XsgRobotManager.getInstance().ladderRobotIds.size());
			return XsgRobotManager.getInstance().ladderRobotIds.get(index);
		}
		// 匹配的范围
		// String[] scopeNum =
		// XsgLadderManager.getInstance().getInitT().rivalRange
		// .split(",");
		String rivalId = "";

		int rankNum = XsgLadderManager.getInstance().getLadderRankListNum();
		if (rankNum > 1) {
			int roleRank = 0;
			for (int i = 0; i < rankNum; i++) {
				LadderRankListSub rank = XsgLadderManager.getInstance().getLadderRankList(i);
				if (rank.roleId.equals(roleRt.getRoleId())) {
					break;
				}
				roleRank++;
			}
			LadderMatchT matchT = XsgLadderManager.getInstance().getMatchTByRank(roleRank + 1);
			int height = matchT.matchHeight;// 高于自己排名的范围
			int low = matchT.matchLow;// 低于自己排名的范围
			// 可能会匹配的排名对手
			List<Integer> allRival = new ArrayList<Integer>();
			// 添加高于自己排名的对手
			int temp = roleRank;
			for (; height > 0; height--) {
				temp--;
				if (temp >= 0) {
					allRival.add(temp);
				} else {
					break;
				}
			}
			// 添加低于自己排名的对手
			temp = roleRank;
			for (; low > 0; low--) {
				temp++;
				if (temp < rankNum) {
					allRival.add(temp);
				} else {
					break;
				}
			}

			int rivalRank = allRival.get(NumberUtil.random(0, allRival.size()));
			rivalId = XsgLadderManager.getInstance().getLadderRankList(rivalRank).roleId;
		} else {
			// 群雄争霸排名只有自己，取得在线角色的随机数据
			List<IRole> onlineRoleList = XsgRoleManager.getInstance().findOnlineList();
			if (onlineRoleList.size() >= 2) {
				int i = 10;
				while (true) {
					rivalId = onlineRoleList.get(NumberUtil.random(onlineRoleList.size())).getRoleId();
					// 随机出的角色是否是自身
					if (!this.roleRt.getRoleId().equals(rivalId)) {
						break;
					}
					// 随机次数过多，完成随机
					if (i > 10) {
						rivalId = ""; //$NON-NLS-1$
						break;
					}

					i++;
				}
			} else {
				int randowRank = NumberUtil.random(XsgArenaRankManager.getInstance().ArenaRankLevelMap.size());
				rivalId = XsgArenaRankManager.getInstance().ArenaRankLevelMap.get(randowRank).getRoleId();
			}
		}

		return rivalId;
	}

	// /**
	// * 设置 要显示的排行榜
	// *
	// * @param resRankListShow
	// * @param rankList
	// * @param showNum
	// */
	// private void setRankListShow(LadderRankListShow resLadderShow, int
	// showNum) {
	//
	// // 判断显示 排行榜的数量
	// int rankListNum = XsgLadderManager.getInstance().getLadderRankListNum();
	// if (showNum > rankListNum) {
	// showNum = rankListNum;
	// }
	//
	// // 设置显示 排行榜的详细信息
	// LadderRankListSub[] rankArr = new LadderRankListSub[showNum];
	// for (int i = 0; i < showNum; i++) {
	// rankArr[i] = XsgLadderManager.getInstance().getLadderRankList(i);
	// rankArr[i].rank = i + 1;
	// // 自己在排名范围内，重新设置排名
	// if (rankArr[i].roleId.equals(roleRt.getRoleId())) {
	// resLadderShow.ownRank = i + 1;
	// }
	// }
	// resLadderShow.rankList = rankArr;
	// }

	/**
	 * 设置替换排行榜的数据
	 * 
	 * @param newRole
	 * @param count
	 * @return
	 */
	private LadderRankListSub setLadderRankListSub(IRole newRole, int ladderLevel, int ladderStar,
			Date changeLevelDate, int ladderScore) {
		LadderRankListSub rsLadder = new LadderRankListSub();
		rsLadder.roleId = newRole.getRoleId();
		rsLadder.roleName = newRole.getName();
		rsLadder.level = newRole.getLevel();
		rsLadder.vipLevel = newRole.getVipLevel();
		rsLadder.icon = newRole.getArenaRankControler().rankIcon(newRole);
		rsLadder.ladderLevel = ladderLevel;
		rsLadder.ladderStar = ladderStar;
		rsLadder.rankTime = changeLevelDate.getTime();
		rsLadder.groupName = newRole.getFactionControler().getFactionName();
		rsLadder.ladderScore = ladderScore;
		return rsLadder;
	}

	/**
	 * 排行榜变化 事件
	 */
	private void onRankChange(int ladderLevel, int ladderStar, Date changeLevelDate, int ladderScore) {
		int rankListNum = XsgLadderManager.getInstance().getLadderRankListNum();
		LadderRankListSub newLadder = setLadderRankListSub(roleRt, ladderLevel, ladderStar, changeLevelDate,
				ladderScore);
		if (rankListNum > 0) {
			XsgLadderManager.getInstance().setLadderRankList(newLadder);
		} else {
			XsgLadderManager.getInstance().addLadderRankList(newLadder);
		}
	}

	/**
	 * 计算赛季剩余时间(分钟)
	 * 
	 * @return
	 */
	private int calcRemainDate() {
		// 计算赛季结束的月份
		Calendar seasonCalendar = Calendar.getInstance();
		seasonCalendar.setTime(XsgLadderManager.getInstance().getSeasonEndDate());
		return (int) (DateUtil.compareTime(seasonCalendar.getTime(), new Date()) / 60000);
	}

	@Override
	public void autoFight(final AMD_Ladder_autoFight __cb) {
		try {
			checkFightLadder();
		} catch (NoteException e) {
			__cb.ice_exception(e);
			return;
		}
		clearLadder();
		// 本次 赛季是否结束
		if (getLadder().getSeasonEndDate().after(new Date())) {
			if (getLadder().getChallengeRemain() > 0) {
				// 保存选择了，战斗部队武将数量
				this.orignalHeroCount = this.roleRt.getFormationControler().getDefaultFormation()
						.getHeroCountIncludeSupport();
				// 匹配对手
				final String rivalRoleId = matchRivalId();

				// 查询对手的阵容
				XsgRoleManager.getInstance().loadRoleByIdAsync(rivalRoleId, new Runnable() {
					@Override
					public void run() {
						final IRole rivalRole = XsgRoleManager.getInstance().findRoleById(rivalRoleId);

						String myFormationId = roleRt.getFormationControler().getDefaultFormation().getId();
						String rivalFormationId = rivalRole.getFormationControler().getDefaultFormation().getId();
						final PvpOpponentFormationView myFormationView = roleRt.getFormationControler()
								.getPvpOpponentFormationView(myFormationId);
						final PvpOpponentFormationView rivalFormationView = rivalRole.getFormationControler()
								.getPvpOpponentFormationView(rivalFormationId);

						int type = XsgFightMovieManager.getInstance().getFightLifeT(Type.Ladder.ordinal()).id;
						final CrossPvpView pvpView = new CrossPvpView(type, new CrossRoleView(roleRt.getRoleId(),
								roleRt.getName(), roleRt.getHeadImage(), roleRt.getLevel(), roleRt.getVipLevel(),
								roleRt.getServerId(), roleRt.getSex(), ""), myFormationView, new CrossRoleView(
								rivalRole.getRoleId(), rivalRole.getName(), rivalRole.getHeadImage(), rivalRole
										.getLevel(), rivalRole.getVipLevel(), rivalRole.getServerId(), rivalRole
										.getSex(), ""), rivalFormationView, "", 0);

						MovieThreads.execute(new Runnable() {

							@Override
							public void run() {
								final String content = HttpUtil.sendPost(XsgLadderManager.getInstance().movieUrl,
										TextUtil.GSON.toJson(pvpView));
								LogicThread.execute(new Runnable() {

									@Override
									public void run() {
										CrossMovieView movie = TextUtil.GSON.fromJson(content, CrossMovieView.class);
										if (movie == null) {
											__cb.ice_exception(new NoteException(Messages
													.getString("FactionControler.59")));
											return;
										}
										XsgLadderManager.getInstance().replaceNull(movie);
										// 扣除挑战次数
										getLadder().setChallengeRemain(getLadder().getChallengeRemain() - 1);
										int resFlag = movie.winRoleId.equals(roleRt.getRoleId()) ? 1 : 0;

										// 战斗处理
										int[] roleChange = fight(resFlag);
										int[] rivalChange = rivalRole.getLadderControler().fight(resFlag ^ 1);

										int star = XsgCopyManager.getInstance().calculateStar(orignalHeroCount,
												(byte) movie.selfHeroNum);
										if (resFlag == 0) {
											star = 0;
										}
										String movieId = XsgFightMovieManager.getInstance().addFightMovie(
												Type.Ladder,
												roleRt.getRoleId(),
												rivalRole.getRoleId(),
												resFlag,
												(byte) movie.selfHeroNum,
												myFormationView,
												rivalFormationView,
												new FightMovieView(resFlag, star, movie.soloMovie, movie.fightMovie,
														new byte[0]));
										// 保存战斗记录
										saveRport(resFlag, roleChange[0], roleChange[1], rivalRole, movieId);
										rivalRole.getLadderControler().saveRport(resFlag ^ 1, rivalChange[0],
												rivalChange[1], roleRt, movieId);

										// 返回客户端 战斗星级
										LadderAutoFightResult result = new LadderAutoFightResult(movie.winRoleId,
												movie.soloMovie, movie.fightMovie, movie.winType, star, roleChange[0],
												getLadder().getLadderStar(), getLadder().getLadderScore(), movieId,
												rivalRole.getName(), rivalRole.getVipLevel());
										__cb.ice_response(result);

										// 添加事件
										eventFight.onFight(rivalRoleId, resFlag, result.fightStar);
									}
								});
							}
						});
					}
				}, new Runnable() {
					@Override
					public void run() {
						__cb.ice_exception(new NoteException(Messages.getString("LadderControler.9")));
					}
				});
			} else {
				__cb.ice_exception(new NoteException(Messages.getString("LadderControler.10")));
			}
		} else {
			__cb.ice_exception(new NoteException(Messages.getString("LadderControler.11")));
		}
	}
}