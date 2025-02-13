package com.morefun.XSanGo.crossServer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_CrossServer_getRobot;
import com.XSanGo.Protocol.AMD_CrossServer_sendCrossAward;
import com.XSanGo.Protocol.AMD_CrossServer_sendScoreAward;
import com.XSanGo.Protocol.AMD_CrossServer_sendScript;
import com.XSanGo.Protocol.CrossRankItem;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CrossServerCallbackPrx;
import com.XSanGo.Protocol.CrossServerCallbackPrxHelper;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MyFormationView;
import com.XSanGo.Protocol._CrossServerDisp;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.RoleTournamentBet;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.robot.XsgRobotManager.LoadRobotCallback;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

import Ice.Current;

/**
 * @author guofeng.qin
 */
public class CrossServerI extends _CrossServerDisp {

	private static final long serialVersionUID = 116475665869228340L;

	private final static Log logger = LogFactory.getLog(CrossServerI.class);

	private static CrossServerI instance = new CrossServerI();

	private CrossServerI() {

	}

	public static CrossServerI instance() {
		return instance;
	}

	@Override
	public void setCallback(CrossServerCallbackPrx cb, Current __current) {
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "t"); //$NON-NLS-1$ //$NON-NLS-2$
		CrossServerCallbackPrx prx = CrossServerCallbackPrxHelper.checkedCast(cb.ice_context(ctx));
		XsgCrossServerManager.getInstance().setCrossServerCallback(prx);
	}

	@Override
	public boolean ping(long time, Current __current) {
		XsgCrossServerManager.getInstance().updateTimeDiff(time);
		return XsgCrossServerManager.getInstance().isUsable();
	}

	private boolean isBetSuccess(int id, String winer, RoleTournamentBet bet) {
		if (id == bet.getFightId() && winer.equals(bet.getBetRoleId())) {
			return true;
		}
		return false;
	}

	/**
	 * 发送押注奖励
	 */
	private void sendBetReward(int id, String winerId, final List<RoleTournamentBet> list) {
		final int BetSuccess = 1; // 押注成功
		final int BetFailure = 2; // 押注失败
		XsgTournamentBetManager betManager = XsgTournamentBetManager.getInstance();
		if (list != null) {
			final XsgTournamentManager tournamentManager = XsgTournamentManager.getInstance();
			final TournamentBetT betT = betManager.getBetT();
			for (final RoleTournamentBet bet : list) {
				if (bet.getResult() != 0) {// 已经发过奖了
					continue;
				}
				if (isBetSuccess(id, winerId, bet)) { // 发送成功奖励
					if (betT != null) {
						XsgRoleManager.getInstance().loadRoleByIdAsync(bet.getRoleId(), new Runnable() {
							@Override
							public void run() {
								IRole role = XsgRoleManager.getInstance().findRoleById(bet.getRoleId());
								if (role != null) {
									Map<String, Integer> rewardMap = new HashMap<String, Integer>();
									ItemView[] itemViews = XsgRewardManager.getInstance().doTcToItem(role, betT.winTC);
									if (itemViews != null) {
										for (ItemView view : itemViews) {
											rewardMap.put(view.templateId, view.num);
										}
										// 发送奖励邮件
										tournamentManager.sendBetSuccessMail(bet.getRoleId(), rewardMap);
									}
								}
							}
						}, new Runnable() {
							@Override
							public void run() {
								logger.error(TextUtil.format("{0}:{1}发送押注奖励，加载失败...", bet.getRoleId(), betT.winTC));
							}
						});
					}
					bet.setResult(BetSuccess); // 设置押注状态
				} else { // 发送失败奖励
					if (betT != null) {
						XsgRoleManager.getInstance().loadRoleByIdAsync(bet.getRoleId(), new Runnable() {
							@Override
							public void run() {
								IRole role = XsgRoleManager.getInstance().findRoleById(bet.getRoleId());
								if (role != null) {
									Map<String, Integer> rewardMap = new HashMap<String, Integer>();
									ItemView[] itemViews = XsgRewardManager.getInstance().doTcToItem(role, betT.loseTC);
									if (itemViews != null) {
										for (ItemView view : itemViews) {
											rewardMap.put(view.templateId, view.num);
										}
										tournamentManager.sendBetFailureMail(bet.getRoleId(), rewardMap);
									}
								}
							}
						}, new Runnable() {
							@Override
							public void run() {
								logger.error(TextUtil.format("押注发奖错误:{0}:{1}", bet.getRoleId(), betT.loseTC));
							}
						});
					}
					bet.setResult(BetFailure);
				}
			}
			// 更新记录
			betManager.updateTournamentBet(list.toArray(new RoleTournamentBet[0]));
		}
	}

	@Override
	public void guessResult(final int id, final String winRoleId, Current __current) {
		final int BatchCount = 20;
		XsgTournamentBetManager betManager = XsgTournamentBetManager.getInstance();
		List<RoleTournamentBet> list = betManager.getTournamentBetByFightId(id);
		if (list != null) {
			// 这里玩家数量可能会比较多，分批发送
			int len = list.size();
			int startIndex = 0;
			// 玩家分批发送，每批BatchCount个，用延迟任务来实现
			while (startIndex < len) {
				int endIndex = Math.min(len, startIndex + BatchCount);
				final List<RoleTournamentBet> subIdList = list.subList(startIndex, endIndex);
				LogicThread.scheduleTask(new DelayedTask(startIndex * 1000) {
					@Override
					public void run() {
						sendBetReward(id, winRoleId, subIdList);
					}
				});
				startIndex = endIndex;
			}
		}
	}

	@Override
	public void getRobot_async(final AMD_CrossServer_getRobot __cb, int num, Current __current) {
		XsgRobotManager.getInstance().loadRobot(num, new LoadRobotCallback() {

			@Override
			public void onLoadRobot(List<IRole> list) {
				if (list != null) {
					List<CrossRankItem> items = new ArrayList<CrossRankItem>();
					for (IRole role : list) {
						CrossRoleView view = new CrossRoleView(role.getRoleId(), role.getName(), role.getHeadImage(),
								role.getLevel(), role.getVipLevel(), ServerLancher.getServerId(), role.getSex(), role
										.getFactionControler().getFactionName());
						MyFormationView formation = role.getTournamentController().getMyCurrentFromation();
						CrossRankItem item = new CrossRankItem(view, formation.heros, 2);
						items.add(item);
					}
					__cb.ice_response(items.toArray(new CrossRankItem[0]));
				}
			}
		});
	}

	/**
	 * 发送排名奖励
	 */
	private void sendCrossAward(int rank, List<String> roleIds) {
		XsgTournamentManager tournamentManager = XsgTournamentManager.getInstance();
		TournamentRewardT rewardT = tournamentManager.getReward(rank);
		final TournamentConfigT config = tournamentManager.getConfig();
		if (config != null && rewardT != null && roleIds != null && TextUtil.isNotBlank(rewardT.items)) {
			Map<String, Integer> rewardMap = new HashMap<String, Integer>();
			String rewardItemPairs[] = rewardT.items.split(",");
			for (String pair : rewardItemPairs) {
				String item[] = pair.split(":");
				rewardMap.put(item[0], Integer.parseInt(item[1]));
			}
			for (String roleId : roleIds) {
				tournamentManager.sendTournamentMail(roleId, config.stageIndex, rank, rewardMap);
			}
			// 处理冠军逻辑
			if (rank == 1 && roleIds.size() > 0) {
				final String championId = roleIds.get(0);
				XsgRoleManager.getInstance().loadRoleByIdAsync(championId, new Runnable() {
					@Override
					public void run() {
						IRole champion = XsgRoleManager.getInstance().findRoleById(championId);
						if (champion != null) {
							champion.getTournamentController().setMaxRank(1);
							champion.getTournamentController().setChampionNum(config.stageIndex);

							// 新增一个扩展头像
							if (champion.getSex() == 1) {
								champion.addExtHeadImage("frist01");
							} else {
								champion.addExtHeadImage("frist02");
							}
							// 头像属性变更通知
							champion.getNotifyControler().onStrPropertyChange(Const.PropertyName.HEAD_IMAGE,
									LuaSerializer.serialize(champion.getExtHeadImage()));
						}
					}
				}, new Runnable() {
					@Override
					public void run() {
					}
				});
			}
		}
	}

	@Override
	public void sendCrossAward_async(AMD_CrossServer_sendCrossAward __cb, final int rank, String[] roleIds,
			Current __current) {
		final int BatchCount = 20;
		if (roleIds != null && roleIds.length > 0) {
			// 发送奖励, 注意这里玩家数量可能会比较多
			List<String> roleIdList = Arrays.asList(roleIds);
			int len = roleIdList.size();
			int startIndex = 0;
			// 玩家分批发送，每批BatchCount个，用延迟任务来实现
			while (startIndex < len) {
				int endIndex = Math.min(len, startIndex + BatchCount);
				final List<String> subIdList = roleIdList.subList(startIndex, endIndex);
				LogicThread.scheduleTask(new DelayedTask(startIndex * 1000) {
					@Override
					public void run() {
						sendCrossAward(rank, subIdList);
					}
				});
				startIndex = endIndex;
			}
		}
	}

	@Override
	public void sendScoreAward_async(AMD_CrossServer_sendScoreAward __cb, int score, String roleId, Current __current) {
		XsgTournamentManager tournamentManager = XsgTournamentManager.getInstance();

		TournamentScoreRewardT rewardT = tournamentManager.getScoreReward(score);
		if (rewardT != null && roleId != null && TextUtil.isNotBlank(rewardT.items)) {
			Map<String, Integer> rewardMap = new HashMap<String, Integer>();
			String rewardItemPairs[] = rewardT.items.split(",");
			for (String pair : rewardItemPairs) {
				String item[] = pair.split(":");
				rewardMap.put(item[0], Integer.parseInt(item[1]));
			}
			XsgTournamentManager.getInstance().sendScoreMail(roleId, score, rewardMap);
		}
		__cb.ice_response();
	}

	@Override
	public void sendScript_async(AMD_CrossServer_sendScript __cb, byte[] data, Current __current) {
		try {
			String path = this.getClass().getResource("/").getPath();
			File file = new File(path + "script/互动和聊天/比武大会.xls");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(data);
			out.close();

			XsgTournamentManager.getInstance().loadTournamentScript();
			XsgTournamentBetManager.getInstance().loadTournamentBetScript();
		} catch (Exception e) {
			LogManager.error(e);
		}
	}
}
