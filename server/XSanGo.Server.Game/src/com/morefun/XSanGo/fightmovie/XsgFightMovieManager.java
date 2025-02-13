package com.morefun.XSanGo.fightmovie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.FightMovie;
import com.morefun.XSanGo.db.game.FightMovieDao;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleValidation;
import com.morefun.XSanGo.db.stat.StatDao;
import com.morefun.XSanGo.db.stat.StatValidateInfo;
import com.morefun.XSanGo.fightmovie.FightMovieValidater.ValidateResult;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 * @date Apr 9, 2015
 */
public class XsgFightMovieManager {
	public static enum Type {
		/** 竞技场 */
		ArenaRank,
		/** 群雄争霸 */
		Ladder,
		/** 切磋 */
		ChatFight,
		/** 挑战他 */
		Challenge,
		/** PVE副本 */
		Copy,
		/** 时空战役 */
		TimeBattle,
		/** 公会战 */
		GVG,
		/** 副本热身赛 */
		CopyWarmup,
		/** 北伐 */
		AttackCastle,
		/** 世界BOSS */
		WorldBoss,
		/** 比武大会 */
		Tournament,
		/** 新公会战 */
		FactionBattle,
		/** 南华幻境 */
		Dreamland, CrossArena,
	};

	/** 战报验证状态 */
	public static enum Validation {
		/** 初始创建战报 */
		Create(-2),
		/** 不完整战报(完成了创建及战斗结果设置) */
		UnFinished(-1),
		/** 未验证 */
		None(0),
		/** 有效的 */
		Valid(1),
		/** 无效的 */
		Invalid(2),
		/** 格式错误 */
		FormatError(3), ;

		private int _value;

		private Validation(int v) {
			_value = v;
		}

		public int getValue() {
			return _value;
		}
	};

	/** 战报验证结果 */
	public static class ValidationStatus {
		static final int Normal = 0; // 1, 正常返回
		static final int Retry = 1; // 2, 重试
		static final int Error = 2; // 3, 错误战报
	};

	/**
	 * 玩家非法状态
	 */
	public static enum ROLE_VALIDATION_TYPE {
		FightMovie, // 战报验证
		ClientLog, // 客户端日志
	};

	// final static FightMovieRemoveStrategy[] fightMovieRemoveStrategy = {
	// /** 竞技场战报, 未经验证的不能删除, 清理 1天(24h) 之前的战报, 7天 (7*24h)
	// 之前的经过验证的(不管是否是合法的)战报强制删除 */
	// new FightMovieRemoveStrategy(Type.ArenaRank.ordinal(), false, true, 24, 7
	// * 24),
	//
	// /** 群雄争霸战报, 未经验证的不能删除, 清理 2h 之前的战报, 7天 (7*24h) 之前的经过验证的(不管是否是合法的)战报强制删除
	// */
	// new FightMovieRemoveStrategy(Type.Ladder.ordinal(), false, true, 2, 7 *
	// 24),
	//
	// /** 切磋战报, 未经验证的可以删除, 清理 1h 之前的战报, 7天 (7*24h) 之前的经过验证的(不管是否是合法的)战报强制删除 */
	// new FightMovieRemoveStrategy(Type.ChatFight.ordinal(), false, false, 1, 7
	// * 24),
	//
	// /** 挑战他战报, 未经验证的不能删除, 清理 2h 之前的战报, 7天 (7*24h) 之前的经过验证的(不管是否是合法的)战报强制删除 */
	// new FightMovieRemoveStrategy(Type.Challenge.ordinal(), false, true, 2, 7
	// * 24),
	//
	// /** 副本战报,未经验证的不能删除,清理1h之前的战报, 7天站前的经过验证的战报强制删除 */
	// new FightMovieRemoveStrategy(Type.Copy.ordinal(), false, true, 1, 7 *
	// 24),
	//
	// /** 时空战意战报, 未经验证的不能删除,清理1h之前的战报,7天之前的经过验证的战报强制删除 */
	// new FightMovieRemoveStrategy(Type.TimeBattle.ordinal(), false, true, 1, 7
	// * 24),
	//
	// /** 公会战,未经验证不能删除,清理2h之前的战报,7天战报强制删除 */
	// new FightMovieRemoveStrategy(Type.GVG.ordinal(), false, true, 2, 7*24),
	//
	// /** 副本热身赛, 未经验证不能删除,清理2h之前的战报,7天战报强制删除 */
	// new FightMovieRemoveStrategy(Type.CopyWarmup.ordinal(), false, true, 2,
	// 7*24),
	//
	// /** 北伐, 未经验证不能删除,清理2h之前的战报,7天战报强制删除 */
	// new FightMovieRemoveStrategy(Type.AttackCastle.ordinal(), false, true, 2,
	// 7*24),
	// };

	// 需要验证的战报类型
	private static List<String> validateFightMovieType = Arrays.asList(Type.Copy.ordinal() + "",
			Type.TimeBattle.ordinal() + "", Type.CopyWarmup.ordinal() + "", Type.AttackCastle.ordinal() + "",
			Type.WorldBoss.ordinal() + "");

	private static final Logger log = LoggerFactory.getLogger(XsgFightMovieManager.class);
	private static XsgFightMovieManager instance = new XsgFightMovieManager();

	// 战报录像上下文缓存
	private Map<String, FightMovie> fightMovieContext = new HashMap<String, FightMovie>();
	// 战报验证每次查询的limit数量
	private static int ValidateCountLimit = 1000;
	// 战报验证查询的时间间隔
	private static long ValidateIntervalTime = 5 * 60 * 1000L;
	// 战报清理多长时间之前的战报(小时)
	private static int FightMovieRemoveTime = 24;
	// 战报强制清理多长时间之前的战报(小时)
	private static int ForceFightMovieRemoveTime = 7 * 24;

	/**
	 * 战报验证线程运行标记
	 */
	private volatile boolean validateRunning = false;

	/**
	 * 战报验证URL
	 */
	private String fightMovieValidateUrl = ServerLancher.getAc().getBean("FightMovie_Validate_Url", String.class);

	public static XsgFightMovieManager getInstance() {
		return instance;
	}

	private FightMovieDao fightMovieDao = null;
	private StatDao statDao = null;
	private RoleDAO roleDao = null;

	/** 类型映射 */
	private Map<Integer, Integer> TypeMap = new HashMap<Integer, Integer>();
	private Map<Integer, FightLifeNumT> fightLifeMap = new HashMap<Integer, FightLifeNumT>();

	private XsgFightMovieManager() {
		if (ServerLancher.getAc().containsBean("FightMovie_Validate_Open")) {
			validateRunning = !ServerLancher.getAc().getBean("FightMovie_Validate_Open", Boolean.class);
		}
		fightMovieDao = FightMovieDao.getFromApplicationContext(ServerLancher.getAc());
		roleDao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		statDao = StatDao.getFromApplicationContext(ServerLancher.getAc());
		if (fightMovieDao == null || statDao == null || roleDao == null) {
			log.error("RoleFightMovieDao 初始化错误...");
		}
		initTypeMap();
		loadFightLifeScript();
	}

	// 加载生命翻倍脚本
	private void loadFightLifeScript() {
		List<FightLifeNumT> list = ExcelParser.parse(FightLifeNumT.class);
		if (list != null) {
			for (FightLifeNumT numT : list) {
				fightLifeMap.put(numT.id, numT);
			}
		}
	}

	public FightLifeNumT getFightLifeT(int type) {
		if (TypeMap.containsKey(type)) {
			return fightLifeMap.get(TypeMap.get(type));
		}
		return null;
	}

	/** 更新类型映射 */
	private void initTypeMap() {
		TypeMap.put(Type.ArenaRank.ordinal(), 1);
		TypeMap.put(Type.Ladder.ordinal(), 2);
		TypeMap.put(Type.Copy.ordinal(), 3);
		TypeMap.put(Type.Challenge.ordinal(), 4);
		TypeMap.put(Type.AttackCastle.ordinal(), 5);
		TypeMap.put(Type.TimeBattle.ordinal(), 6);
		TypeMap.put(Type.CopyWarmup.ordinal(), 7);
		TypeMap.put(Type.ChatFight.ordinal(), 8);
		TypeMap.put(Type.WorldBoss.ordinal(), 9);
		TypeMap.put(Type.Tournament.ordinal(), 10);
		TypeMap.put(Type.FactionBattle.ordinal(), 11);
		TypeMap.put(Type.CrossArena.ordinal(), 14);
	}

	/**
	 * 保存战报录像
	 * 
	 * @param fightId
	 *            战报ID
	 * @param view
	 *            录像数据
	 */
	public void saveFightMovie(final String roleId, final String movieId, final FightMovieView view) {
		saveFightMovie(roleId, movieId, view, Validation.None);
	}

	/**
	 * 保存战报录像
	 * 
	 * @param fightId
	 *            战报ID
	 * @param view
	 *            录像数据
	 */
	public void saveFightMovie(final String roleId, final String movieId, final FightMovieView view,
			final Validation validation) {
		final FightMovie movie = checkAndRemoveContext(roleId);
		if (movie == null || !movie.getId().equals(movieId)) {
			// 战报录像ID不存在在上下文环境中, 直接返回
			log.error(TextUtil.format("Missing FightMovie ... {0}-{1}", roleId, movieId));
			if (movie != null && !movieId.equals(movie.getId())) {
				log.error(TextUtil.format("Missing FightMovie wrong id ... {0}-{1}", roleId, movieId));
			}
			return;
		}

		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// 序列化工作在数据库线程中完成
					String data = "";
					if (view != null) {
						movie.setValidated(validation.getValue());
						data = TextUtil.gzip(TextUtil.GSON.toJson(view));
					}
					movie.setData(data);
					movie.setEndDate(Calendar.getInstance().getTime());
					fightMovieDao.save(movie);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void findFightMovie(final String fightId, final FindMovieCallback cb) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FightMovie movie = null;
				try {
					movie = fightMovieDao.findMoviesById(fightId);
					if (movie != null && TextUtil.isNotBlank(movie.getData())) {
						// 查找成功, 反序列化数据对象, 执行后返回
						// 反序列化工作在数据库线程中完成
						final FightMovieView movieView = TextUtil.GSON.fromJson(TextUtil.ungzip(movie.getData()),
								FightMovieView.class);
						// 返回到逻辑线程执行
						LogicThread.execute(new Runnable() {
							@Override
							public void run() {
								cb.onFindMovie(movieView);
							}
						});
						return;
					}
				} catch (IOException e) {
					log.error("ungzip movie data error.");
					if (movie != null) {
						log.error(TextUtil.format("ungzip movie data error: {0}", movie.getData()));
					}
				}
				// 返回到逻辑线程执行
				// 查找失败或者反序列化失败
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						cb.onFindMovie(null);
					}
				});
			}
		});
	}

	/**
	 * 根据发起方的角色ID查找战报
	 * 
	 * @param fightId
	 * @param cb
	 */
	public void findFightMovieByRoleId(final String roleId, final int type, final int limit,
			final FindMovieListCallback cb) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				final List<Object[]> list_movie = fightMovieDao.findMoviesByRoleId(roleId, type, limit);
				// if (list_movie != null && list_movie.size() > 0) {
				// 返回到逻辑线程执行
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						cb.onFindMovieList(list_movie);
					}
				});
				return;
				// }
			}
		});
	}

	public void removeFightMoviesByReportId(final String reportId) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				fightMovieDao.deleteMoviesByFightId(reportId);
			}
		});
	}

	/**
	 * 检查上下文id是否存在, 如果存在将之移除并返回.
	 */
	private FightMovie checkAndRemoveContext(String id) {
		return fightMovieContext.remove(id);
	}

	private void addMovieToContext(String key, FightMovie movie) {
		fightMovieContext.put(key, movie);
	}

	/**
	 * 移除缓存上下文中的一个小时以前的数据, 应该是战斗过程中用户掉线了, 没有来得及上传战报
	 */
	public void removeOldMovieContext() {
		// 60分钟以前
		long checkpoint = System.currentTimeMillis() - 60 * 60 * 1000L;
		List<String> removeList = new ArrayList<String>();
		for (Map.Entry<String, FightMovie> entry : fightMovieContext.entrySet()) {
			// 战报的开始时间在检查点之前
			if (entry.getValue().getStartDate().getTime() < checkpoint) {
				// 加入移除列表
				removeList.add(entry.getKey());
			}
		}
		// 移除数据
		for (String str : removeList) {
			fightMovieContext.remove(str);
		}
		if (removeList.size() > 0) {
			log.warn(TextUtil.format("remove old movie context count {0}", removeList.size()));
		}
	}

	private PvpOpponentFormationView getFormationViewOf(IRole role) {
		String targetFormationId = role.getArenaRankControler().findFormationId();
		if (TextUtil.isBlank(targetFormationId)) {
			targetFormationId = role.getFormationControler().getDefaultFormation().getId();
		}
		PvpOpponentFormationView view = role.getFormationControler().getPvpOpponentFormationView(targetFormationId);
		return view;
	}

	/**
	 * 生成战报ID</br> 用于副本和时空战役 PVE场景
	 * 
	 * @param type
	 *            战报类型
	 * @param self
	 *            用户IRole对象
	 * @param pveParam
	 *            验证相关参数(关卡ID,数据),数据由验证服读表,此处可以为空
	 */
	public String generateMovieId(XsgFightMovieManager.Type type, IRole self, PVEMovieParam pveParam) {
		try {
			int typeOrdinal = type.ordinal();
			String selfId = self.getRoleId();
			String opponentId = pveParam.level;
			PvpOpponentFormationView selfFormationView = getFormationViewOf(self);

			// 产生一个ID
			String movieId = GlobalDataManager.getInstance().generatePrimaryKey();
			FightMovie movie = new FightMovie();
			movie.setId(movieId);
			movie.setType(typeOrdinal);
			movie.setSelfId(selfId);
			if (TextUtil.isBlank(opponentId)) {
				if (opponentId == null) {
					log.warn(TextUtil.format("FightMovieValidate: opponentId is null {0}", type.ordinal()));
				}
				opponentId = "";
			}
			movie.setOpponentId(opponentId);
			// 设置双方阵容
			movie.setSelfFormation(TextUtil.gzip(TextUtil.GSON.toJson(selfFormationView)));
			movie.setOpponentFormation(pveParam.data);
			// 设置时间, 用于清理过期坏数据, 比如战斗结束之前用户掉线
			movie.setStartDate(Calendar.getInstance().getTime());
			// 处理之前的缓存
			processPreviousContext(selfId);
			// 添加到上下文缓存中
			addMovieToContext(selfId, movie);
			return movieId;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 处理之前的缓存, 可能由于掉线之类的引起的没有正常的移除
	 */
	private void processPreviousContext(String roleId) {
		FightMovie movie = fightMovieContext.get(roleId);
		if (movie != null) {
			saveFightMovie(roleId, movie.getId(), null);
		}
	}

	/**
	 * 生成战报ID</br> 用于竞技场等PVP实时战斗
	 * 
	 * @param type
	 *            战报类型
	 * @param self
	 *            用户自己的IRole对象
	 * @param opponent
	 *            对手的IRole对象
	 */
	public String generateMovieId(XsgFightMovieManager.Type type, IRole self, IRole opponent) {
		String selfId = self.getRoleId();
		String opponentId = opponent.getRoleId();
		PvpOpponentFormationView selfFormationView = getFormationViewOf(self);
		PvpOpponentFormationView opponentFormationView = getFormationViewOf(opponent);
		return generateMovieId(type, selfId, opponentId, selfFormationView, opponentFormationView);
	}

	/**
	 * 生成战报ID</br> 用于北伐等PVP快照数据战斗
	 * 
	 * @param type
	 *            战报类型
	 * @param selfId
	 *            用户自己的ID
	 * @param opponentId
	 *            对手ID
	 * @param selfFormationView
	 *            用户自己的阵容
	 * @param opponentFormationView
	 *            对手阵容
	 */
	public String generateMovieId(XsgFightMovieManager.Type type, String selfId, String opponentId,
			PvpOpponentFormationView selfFormationView, PvpOpponentFormationView opponentFormationView) {
		try {
			int typeOrdinal = type.ordinal();
			// 产生一个ID
			String movieId = GlobalDataManager.getInstance().generatePrimaryKey();
			FightMovie movie = new FightMovie();
			movie.setId(movieId);
			movie.setType(typeOrdinal);
			movie.setValidated(Validation.Create.getValue());
			movie.setSelfId(selfId);
			if (TextUtil.isBlank(opponentId)) {
				if (opponentId == null) {
					log.warn(TextUtil.format("FightMovieValidate: opponentId is null {0}", type.ordinal()));
				}
				opponentId = "";
			}
			movie.setOpponentId(opponentId);
			// 设置双方阵容
			String selfFormationViewStr = TextUtil.GSON.toJson(selfFormationView);
			log.debug("Self: " + selfFormationViewStr);
			movie.setSelfFormation(TextUtil.gzip(selfFormationViewStr));
			String opponentFormationViewStr = TextUtil.GSON.toJson(opponentFormationView);
			log.debug("Opponent: " + opponentFormationViewStr);
			movie.setOpponentFormation(TextUtil.gzip(opponentFormationViewStr));
			// 设置时间, 用于清理过期坏数据, 比如战斗结束之前用户掉线
			movie.setStartDate(Calendar.getInstance().getTime());
			// 处理之前的缓存
			processPreviousContext(selfId);
			// 添加到上下文缓存中
			addMovieToContext(selfId, movie);
			return movieId;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 增加一条战报战报
	 */
	public String addFightMovie(XsgFightMovieManager.Type type, String selfId, String opponentId, int result,
			byte remainHero, PvpOpponentFormationView selfFormationView,
			PvpOpponentFormationView opponentFormationView, final FightMovieView view) {
		String movieId = generateMovieId(type, selfId, opponentId, selfFormationView, opponentFormationView);
		endFightMovie(selfId, movieId, result, remainHero);
		saveFightMovie(selfId, movieId, view, Validation.Valid);
		return movieId;
	}

	private FightMovie getFightMovieFromContext(String key) {
		return fightMovieContext.get(key);
	}

	/**
	 * 结束战报, 玩家副本退出, 副本打输了等
	 * 
	 * @param roleId
	 *            用户id
	 * @param movieId
	 *            战报id
	 * @param result
	 *            结果
	 * @param remainHero
	 *            剩余武将数量
	 */
	public String anomalyEndFightMovie(String roleId, String movieId, int result, byte remainHero) {
		String mId = innerEndFightMovie(roleId, movieId, result, remainHero, Validation.Valid);
		saveFightMovie(roleId, movieId, null);
		return mId;
	}

	/**
	 * 设置战斗结果
	 * 
	 * @param roleId
	 *            用户ID
	 * @param movieId
	 *            战报ID
	 * @param result
	 *            战斗结果
	 * @param remainHero
	 *            剩余武将数量
	 */
	public String endFightMovie(String roleId, String movieId, int result, byte remainHero) {
		return innerEndFightMovie(roleId, movieId, result, remainHero, Validation.UnFinished);
	}

	public String endFightMovieWithExtra(String roleId, String movieId, int result, byte remainHero, String extra) {
		FightMovie movie = getFightMovieFromContext(roleId);
		if (movie != null && movie.getId().equals(movieId)) {
			if (TextUtil.isBlank(extra)) {
				if (extra == null) {
					log.warn(TextUtil.format("FightMovieValidate: extra is null {0}", movie.getType()));
				}
				extra = "";
			}
			// 更新附加参数
			movie.setOpponentId(extra);
		}
		return innerEndFightMovie(roleId, movieId, result, remainHero, Validation.UnFinished);
	}

	private String innerEndFightMovie(String roleId, String movieId, int result, byte remainHero, Validation validation) {
		FightMovie movie = getFightMovieFromContext(roleId);
		if (movie != null && movie.getId().equals(movieId)) {
			// 设置战斗结果
			movie.setResult(result);
			// 设置剩余武将数量
			movie.setRemainHero(remainHero);
			// 更新战报的状态
			movie.setValidated(validation.getValue());
			return movie.getId();
		} else {
			log.error(TextUtil.format("FightMovie end missing ... {0}-{1}", roleId, movieId));
			if (movie != null && !movie.getId().equals(movieId)) {
				log.error(TextUtil.format("FightMovie end wrong id ... {0}-{1}", roleId, movieId));
			}
		}
		return null;
	}

	/**
	 * 移除老的战报
	 */
	public static void removeOldMovies() {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FightMovieDao dao = FightMovieDao.getFromApplicationContext(ServerLancher.getAc());
				Calendar cal = Calendar.getInstance();
				// 正常删除
				cal = DateUtil.addHours(cal, -XsgFightMovieManager.FightMovieRemoveTime);
				dao.removeMovieWithValidatedAndDateEq(Validation.Valid.getValue(), cal.getTime());

				cal = Calendar.getInstance();
				// 强制删除七天前的所有战报
				cal = DateUtil.addHours(cal, -XsgFightMovieManager.ForceFightMovieRemoveTime);
				dao.removeMovieWithValidatedAndDateNotEq(cal.getTime());
			}
		});
	}

	public static interface FindMovieCallback {
		void onFindMovie(FightMovieView movieView);
	}

	public static interface FindMovieListCallback {
		void onFindMovieList(List<Object[]> list_movie);
	}

	public String getValidateUrl() {
		return fightMovieValidateUrl;
	}

	/**
	 * 执行验证战报逻辑
	 */
	private void doValidate(List<FightMovie> movies) {
		for (FightMovie movie : movies) {
			if (!validateRunning) {
				break;
			}
			ValidateResult check = null;
			String selfAccount = null;
			try {
				// 只验证胜利的需要验证的战报
				if (movie.getResult() == 1 && validateFightMovieType.contains(movie.getType() + "")) {
					// 验证
					selfAccount = roleDao.findAccountById(movie.getSelfId());
					FightMovieValidater validater = new FightMovieValidater(selfAccount, movie);
					check = validater.call();
				} else {
					check = new ValidateResult(ValidationStatus.Normal, "输了的或者切磋战报不用验证");
				}
			} catch (Exception e) {
				log.error("validation error.", e);
				check = new ValidateResult(ValidationStatus.Error, "unknown error: " + e.getMessage());
			}
			if (check != null) {
				// 远程返回重试的错误码, 则忽略,继续下一次循环
				if (check.result == ValidationStatus.Retry) {
					// log.warn(TextUtil.format("战报验证远端返回重试:{0}",
					// check.reason));
					continue;
				}

				int validateStatus = Validation.None.getValue();
				switch (check.result) {
				case ValidationStatus.Normal:
					validateStatus = Validation.Valid.getValue();
					break;
				case ValidationStatus.Error:

					RoleValidation validation = new RoleValidation(movie.getSelfId(),
							ROLE_VALIDATION_TYPE.FightMovie.ordinal(), check.reason, check.result + "", Calendar
									.getInstance().getTime());
					// 关联战报 ID
					validation.setReferId(movie.getId());
					fightMovieDao.save(validation);

					validateStatus = Validation.FormatError.getValue();
					break;
				default:
					break;
				}
				movie.setValidated(validateStatus);
				fightMovieDao.saveOrUpdate(movie);
			}
		}
	}

	/**
	 * 开始战报验证线程
	 */
	public void startValidateFightMovie() {
		if (validateRunning) {
			return;
		}
		validateRunning = true;
		new Thread() {
			@Override
			public void run() {
				while (validateRunning) {
					try {
						// 查询待验证战报
						List<FightMovie> movies = fightMovieDao.findMovieByValidation(Validation.None.getValue(),
								ValidateCountLimit);
						long lastSelectTime = System.currentTimeMillis();
						if (movies != null && movies.size() > 0) {
							doValidate(movies);
						}
						// 保证每 ValidateIntervalTime 执行一次查询, 减小数据库压力
						long selectTimeInterval = System.currentTimeMillis() - lastSelectTime;
						if (selectTimeInterval < ValidateIntervalTime) {
							Thread.sleep(ValidateIntervalTime - selectTimeInterval);
						}
					} catch (Exception e) {
						log.error("validation thread error.", e);
					}
				}
			}
		}.start();
	}

	public void stopValidateFightMovie() {
		validateRunning = false;
	}

	/**
	 * PVE战报参数
	 */
	public static class PVEMovieParam {
		public String level;
		public String data;

		public PVEMovieParam(String level, String data) {
			this.level = level;
			this.data = data;
		}
	}

	/**
	 * 战报清理策略定义
	 */
	// public static class FightMovieRemoveStrategy {
	// int type; // 战报类型
	// boolean created;// 创建状态的战报是否删除(没有正常完成战斗, 比如战斗过程中退出)
	// boolean needValidated; // 未经过验证的战报能否删除
	// int hour;// 删除几小时之前的战报
	// int forceDelHour; // 强制删除几小时之前的战报
	//
	// public FightMovieRemoveStrategy(int type, boolean created, boolean
	// needValidated,
	// int hour, int forceDelHour) {
	// this.type = type;
	// this.created = created;
	// this.needValidated = needValidated;
	// this.hour = hour;
	// this.forceDelHour = forceDelHour;
	// }
	// }

	public void saveClientLog(final String account, final String roleId, final String type, final int lvlId,
			final String params) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				RoleValidation validation = new RoleValidation(roleId,
						XsgFightMovieManager.ROLE_VALIDATION_TYPE.ClientLog.ordinal(), params, type, Calendar
								.getInstance().getTime());
				// 关联关卡
				validation.setReferId(lvlId + "");
				// 保存数据库
				fightMovieDao.save(validation);

				StatValidateInfo statInfo = new StatValidateInfo(account, roleId, ServerLancher.getServerId(),
						XsgFightMovieManager.ROLE_VALIDATION_TYPE.ClientLog.ordinal(), TextUtil.format(
								"[{0}][{1}][{2}]", type, lvlId, params), Calendar.getInstance().getTime());
				statDao.save(statInfo);
			}
		});
	}

	public static void main(String[] args) {
		try {
			final String test = "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";
			FightMovieView movieView = TextUtil.GSON.fromJson(TextUtil.ungzip(test), FightMovieView.class);
			File f = new File("zhanbao.txt");
			boolean res = f.createNewFile();
			if (res) {
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(movieView.fightMovie);
				fos.flush();
				fos.close();
			}
			// System.out.println(HttpUtil.doPost(
			// "http://xsghttp.ops.morefuntek.com/1.3.8/api/report",
			// movieView.fightMovie));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateMovieId(Type type, IRole self, String targetId, PvpOpponentFormationView opponent) {
		try {
			int typeOrdinal = type.ordinal();
			String selfId = self.getRoleId();
			String opponentId = targetId;
			PvpOpponentFormationView selfFormationView = getFormationViewOf(self);

			// 产生一个ID
			String movieId = GlobalDataManager.getInstance().generatePrimaryKey();
			FightMovie movie = new FightMovie();
			movie.setId(movieId);
			movie.setType(typeOrdinal);
			movie.setValidated(Validation.Create.getValue());
			movie.setSelfId(selfId);
			if (TextUtil.isBlank(opponentId)) {
				if (opponentId == null) {
					log.warn(TextUtil.format("FightMovieValidate: opponentId is null {0}", type.ordinal()));
				}
				opponentId = "";
			}
			movie.setOpponentId(opponentId);
			// 设置双方阵容
			movie.setSelfFormation(TextUtil.gzip(TextUtil.GSON.toJson(selfFormationView)));
			movie.setOpponentFormation(TextUtil.gzip(TextUtil.GSON.toJson(opponent)));
			// 设置时间, 用于清理过期坏数据, 比如战斗结束之前用户掉线
			movie.setStartDate(Calendar.getInstance().getTime());
			// 处理之前的缓存
			processPreviousContext(selfId);
			// 添加到上下文缓存中
			addMovieToContext(selfId, movie);

			return movieId;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
