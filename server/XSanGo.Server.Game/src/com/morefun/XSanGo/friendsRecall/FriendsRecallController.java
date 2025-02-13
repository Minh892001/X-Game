package com.morefun.XSanGo.friendsRecall;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import com.XSanGo.Protocol.AMD_FriendsRecall_openInvitation;
import com.XSanGo.Protocol.AMD_FriendsRecall_randomOfflineRole;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.FriendsRecallTaskView;
import com.XSanGo.Protocol.InvitationView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OfflineRoleView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RecallRoleView;
import com.XSanGo.Protocol.RecallView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleFriendsInvitation;
import com.morefun.XSanGo.db.game.RoleFriendsRecalled;
import com.morefun.XSanGo.db.game.RoleRecallTask;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IAutoResign;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.ISign;
import com.morefun.XSanGo.event.protocol.IYuanbaoChange;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.XsgVipManager;

import Ice.Current;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

public class FriendsRecallController implements IFriendsRecallController, IRoleLevelup, ICharge, IAutoResign, ISign,
		IYuanbaoChange {

	private IRole roleRt;

	private Role roleDB;

	public FriendsRecallController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		IEventControler evtContrl = roleRt.getEventControler();
		evtContrl.registerHandler(IRoleLevelup.class, this);
		evtContrl.registerHandler(ICharge.class, this);
		evtContrl.registerHandler(IAutoResign.class, this);
		evtContrl.registerHandler(ISign.class, this);
		evtContrl.registerHandler(IYuanbaoChange.class, this);
	}

	@Override
	public RecallView openRecall() throws NoteException {
		String errorMsg = XsgFriendsRecallManager.getInstance().canRecall(roleDB);
		boolean canRecall = false;
		boolean isRecalled = false;
		boolean isFirstOpen = false;
		List<FriendsRecallTaskView> taskList = new ArrayList<FriendsRecallTaskView>();
		if (errorMsg == null) {
			canRecall = true;
			RoleFriendsRecalled recallVo = roleDB.getRoleFriendsRecalled();
			isRecalled = recallVo != null && recallVo.getState() == 2 ? true : false;
			isFirstOpen = recallVo == null || recallVo.getState() == 0 ? true : false;
			if (recallVo == null) {
				recallVo = new RoleFriendsRecalled(roleDB, "", 0, 0, 0, Calendar.getInstance().getTime());
				roleDB.setRoleFriendsRecalled(recallVo);
			}

			// 回归任务
			if (!isRecalled) {
				recallVo.setState(1);
			} else {
				taskList = queryRecallTaskList(XsgFriendsRecallManager.TASK_TYPE_RECALL);
			}
		}

		// 邀请任务
		List<FriendsRecallTaskView> invitationList = queryRecallTaskList(XsgFriendsRecallManager.TASK_TYPE_INVITATION);

		return new RecallView(canRecall, isFirstOpen, isRecalled, taskList.toArray(new FriendsRecallTaskView[0]),
				invitationList.toArray(new FriendsRecallTaskView[0]));
	}

	@Override
	public void openInvitation_async(final AMD_FriendsRecall_openInvitation __cb, Current __current)
			throws NoteException {
		if (!XsgFriendsRecallManager.getInstance().isOpened()) {
			__cb.ice_exception(new NoteException(Messages.getString("FriendsRecallController.notOpen")));
			return;
		}
		RoleFriendsInvitation fi = roleDB.getRoleFriendsInvitation();
		if (fi == null) {
			fi = new RoleFriendsInvitation(roleDB, XsgFriendsRecallManager.getInstance().generateInvitationCode(), 0,
					0, Calendar.getInstance().getTime());
			roleDB.setRoleFriendsInvitation(fi);
			XsgFriendsRecallManager.getInstance().addInvitationCodeRole(fi.getRecallCode(), roleDB.getId());
		}
		final String recallCode = fi.getRecallCode();

		// 加载一个随机可回归玩家
		loadRandomRoleAsync(roleDB.getId(), "", new Runnable() {
			@Override
			public void run() {
				final List<OfflineRoleView> viewList = new ArrayList<OfflineRoleView>();
				List<String> randomIds = XsgFriendsRecallManager.getInstance().getRandomRoleIdMap(roleDB.getId());
				if (randomIds != null && randomIds.size() > 0) {
					for (String randomId : randomIds) {
						if (TextUtil.isNotBlank(randomId)) {
							IRole randomRole = XsgRoleManager.getInstance().findRoleById(randomId);
							if (randomRole != null) {
								String facName = "";
								if (randomRole.getFactionControler().isInFaction()) {
									facName = randomRole.getFactionControler().getFactionName();
								}
								int offlineDays = DateUtil.diffDate(Calendar.getInstance().getTime(),
										randomRole.getLogoutTime());
								viewList.add(new OfflineRoleView(randomId, randomRole.getName(), randomRole
										.getHeadImage(), randomRole.getLevel(), randomRole.getVipLevel(), facName,
										offlineDays));
							}
						}
					}
				}
				// 已召回玩家数据
				final List<String> ids = XsgFriendsRecallManager.getInstance().getRecalledFriends(roleDB.getId());
				if (ids == null || ids.size() == 0) {
					__cb.ice_response(LuaSerializer.serialize(new InvitationView(recallCode,
							new ArrayList<RecallRoleView>().toArray(new RecallRoleView[0]), viewList
									.toArray(new OfflineRoleView[0]))));
					return;
				}
				XsgRoleManager.getInstance().loadRoleAsync(ids, new Runnable() {
					@Override
					public void run() {
						final List<RecallRoleView> list = new ArrayList<RecallRoleView>();
						IRole recalled = null;
						for (String id : ids) {
							recalled = XsgRoleManager.getInstance().findRoleById(id);
							if (recalled == null) {
								continue;
							}
							String fname = "";
							if (recalled.getFactionControler().isInFaction()) {
								fname = recalled.getFactionControler().getFactionName();
							}
							list.add(new RecallRoleView(recalled.getRoleId(), recalled.getName(), recalled.getLevel(),
									recalled.getVipLevel(), recalled.getHeadImage(), fname,
									combineRecallDateStr(recalled.friendsRecallTime())));
						}
						__cb.ice_response(LuaSerializer.serialize(new InvitationView(recallCode, list
								.toArray(new RecallRoleView[0]), viewList.toArray(new OfflineRoleView[0]))));
					}
				});
			}
		});
	}

	@Override
	public List<FriendsRecallTaskView> activeInvitationCode(String code) throws NoteException {
		if (roleDB.getRoleFriendsRecalled() != null && roleDB.getRoleFriendsRecalled().getState() == 2) {
			throw new NoteException(Messages.getString("FriendsRecallController.alreadyRecalled")); // 已经被召回了
		}
		String errorMsg = XsgFriendsRecallManager.getInstance().canRecall(roleDB);
		if (errorMsg != null) {
			throw new NoteException(errorMsg);
		}
		final String invRoleId = XsgFriendsRecallManager.getInstance().getRoleByInvitationCode(code);
		if ((TextUtil.isBlank(invRoleId) && !XsgFriendsRecallManager.DEFAULT_INVITATION_CODE.equalsIgnoreCase(code))
				|| this.roleDB.getId().equals(invRoleId)) {
			throw new NoteException(Messages.getString("FriendsRecallController.invalidInvitationCode")); // 非法邀请码，无效
		}
		List<String> recalled = XsgFriendsRecallManager.getInstance().getRecalledFriends(invRoleId);
		if (TextUtil.isNotBlank(invRoleId) && recalled != null
				&& recalled.size() >= XsgFriendsRecallManager.getInstance().getFriendsRecallCfgT().recallLimit) {
			throw new NoteException(Messages.getString("FriendsRecallController.overRecalledLimit")); // 邀请人数已达到上限
		}

		// 增加或者更新回归数据
		RoleFriendsRecalled fr = roleDB.getRoleFriendsRecalled();
		if (fr == null) {
			fr = new RoleFriendsRecalled(roleDB, invRoleId, 0, 0, 2, Calendar.getInstance().getTime());
			roleDB.setRoleFriendsRecalled(fr);
		} else {
			fr.setInviteRoleId(invRoleId);
			fr.setRecallTime(Calendar.getInstance().getTime());
			fr.setState(2);
		}

		// 邮件发放回归奖励
		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.FriendsRecallRecalled.value());
		List<Property> proList = XsgFriendsRecallManager.getInstance().getFriendsRecallCfgT().getRecallReward();
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, roleDB
						.getId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
						.serializeMailAttach(proList.toArray(new Property[0])), Calendar.getInstance().getTime()));

		// 获得回归任务
		roleDB.getRoleRecallTask().addAll(queryInitTask(XsgFriendsRecallManager.TASK_TYPE_RECALL, roleDB));

		// 邀请人处理
		if (TextUtil.isNotBlank(invRoleId)) {
			List<String> idList = new ArrayList<String>();
			idList.add(invRoleId);
			XsgRoleManager.getInstance().loadRoleAsync(idList, new Runnable() {
				@Override
				public void run() {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(invRoleId);
					iRole.getFriendsRecallController().addSuccessfulInvitation(roleDB);
				}
			});
		}

		return queryRecallTaskList(XsgFriendsRecallManager.TASK_TYPE_RECALL);
	}

	@Override
	public List<FriendsRecallTaskView> receiveTaskReward(int taskId) throws NoteException, NotEnoughMoneyException {
		RoleRecallTask rt = null;
		if (roleDB.getRoleRecallTask() != null && roleDB.getRoleRecallTask().size() > 0) {
			for (RoleRecallTask t : roleDB.getRoleRecallTask()) {
				if (t.getTaskId() == taskId && t.getState() == 1) {
					rt = t;
					if (t.getState() == 1) { // 可能会有重复任务
						break;
					}
				}
			}
		}
		if (rt == null) {
			throw new NoteException(Messages.getString("FriendsRecallController.noSuchTask")); // 没有接取过该任务
		}
		if (rt.getState() == 0) {
			throw new NoteException(Messages.getString("FriendsRecallController.taskNotComplete")); // 该任务尚未完成
		}
		if (rt.getState() == 2) {
			throw new NoteException(Messages.getString("FriendsRecallController.alreadyReceiveReward")); // 奖品已领取
		}

		// 改变任务状态为完成
		rt.setState(2);
		processNextTask(rt.getTaskId());

		// 发放奖励
		FriendsRecallBaseT taskT = XsgFriendsRecallManager.getInstance().getTaskMap().get(taskId);
		if (taskT.rewardGold > 0) {
			this.roleRt.winJinbi(taskT.rewardGold);
		}
		if (taskT.rewardExp > 0) {
			this.roleRt.winPrestige(taskT.rewardExp);
		}
		if (TextUtil.isNotBlank(taskT.rewardItem)) {
			this.roleRt.getRewardControler().acceptReward(taskT.rewardItem, taskT.rewardItemCount);
		}

		return queryRecallTaskList(taskT.taskType);
	}

	@Override
	public void randomOfflineRole(final AMD_FriendsRecall_randomOfflineRole __cb, String currOfflineRoleId) {
		loadRandomRoleAsync(roleDB.getId(), currOfflineRoleId, new Runnable() {
			@Override
			public void run() {
				List<OfflineRoleView> offlineViewList = new ArrayList<OfflineRoleView>();
				List<String> randomIds = XsgFriendsRecallManager.getInstance().getRandomRoleIdMap(roleDB.getId());
				if (randomIds != null && randomIds.size() > 0) {
					for (String randomId : randomIds) {
						if (TextUtil.isNotBlank(randomId)) {
							IRole randomRole = XsgRoleManager.getInstance().findRoleById(randomId);
							if (randomRole != null) {
								String facName = "";
								if (randomRole.getFactionControler().isInFaction()) {
									facName = randomRole.getFactionControler().getFactionName();
								}
								int offlineDays = DateUtil.diffDate(Calendar.getInstance().getTime(),
										randomRole.getLogoutTime());
								offlineViewList.add(new OfflineRoleView(randomId, randomRole.getName(), randomRole
										.getHeadImage(), randomRole.getLevel(), randomRole.getVipLevel(), facName,
										offlineDays));
							}
						}
					}
				}
				__cb.ice_response(LuaSerializer.serialize(offlineViewList));
			}
		});
	}

	/**
	 * 处理下一个任务
	 * 
	 * @param currTaskId
	 */
	private void processNextTask(int currTaskId) {
		FriendsRecallBaseT taskT = XsgFriendsRecallManager.getInstance().getTaskMap().get(currTaskId);
		if (taskT.suffixTask == 0) {
			return;
		}
		final FriendsRecallBaseT nextTaskT = XsgFriendsRecallManager.getInstance().getTaskMap().get(taskT.suffixTask);

		// 回归任务
		if (nextTaskT.taskType == XsgFriendsRecallManager.TASK_TYPE_RECALL) {
			addNewStepTask(nextTaskT);
		} else {
			// 邀请任务
			// final String inviteRoleId =
			// roleDB.getRoleFriendsRecalled().getInviteRoleId();
			if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9Growup)
					|| taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccConsumption)
					|| taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccRecall)) {
				addNewStepTask(nextTaskT);
				// if (StringUtils.isNotBlank(inviteRoleId)) {
				// List<String> idList = new ArrayList<String>();
				// idList.add(inviteRoleId);
				// XsgRoleManager.getInstance().loadRoleAsync(idList, new
				// Runnable() {
				// @Override
				// public void run() {
				// IRole iRole =
				// XsgRoleManager.getInstance().findRoleById(inviteRoleId);
				// iRole.getFriendsRecallController().addNewStepTask(nextTaskT);
				// }
				// });
				// }
			}
		}
	}

	@Override
	// 更新邀请记录
	public void addSuccessfulInvitation(Role recalled) {
		XsgFriendsRecallManager.getInstance().addRecalledFriends(roleDB.getId(), recalled.getId());

		// 邮件发放邀请奖励
		MailRewardT mailRewardT = XsgMailManager.getInstance().getMailRewardTList()
				.get(MailTemplate.FriendsRecallInvitation.value());
		List<Property> proList = XsgFriendsRecallManager.getInstance().getFriendsRecallCfgT().getInvitationReward();
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", mailRewardT.sendName, roleDB
						.getId(), mailRewardT.title, mailRewardT.body, XsgMailManager.getInstance()
						.serializeMailAttach(proList.toArray(new Property[0])), Calendar.getInstance().getTime()));

		// 更新邀请任务
		boolean hasInvitationTask = false;
		if (roleDB.getRoleRecallTask() != null && roleDB.getRoleRecallTask().size() > 0) {
			FriendsRecallBaseT bt = null;
			for (RoleRecallTask t : roleDB.getRoleRecallTask()) {
				bt = XsgFriendsRecallManager.getInstance().getTaskMap().get(t.getTaskId());
				if (bt.taskType == XsgFriendsRecallManager.TASK_TYPE_INVITATION) {
					hasInvitationTask = true;
					break;
				}
			}
		}
		if (!hasInvitationTask) {
			roleDB.getRoleRecallTask().addAll(queryInitTask(XsgFriendsRecallManager.TASK_TYPE_INVITATION, recalled));
		} else {
			processStepTask(recalled.getLevel(), XsgFriendsRecallManager.taskTarget9Growup);
			processStepTask(XsgFriendsRecallManager.getInstance().getRecalledFriends(roleDB.getId()).size(),
					XsgFriendsRecallManager.taskTarget9AccRecall);
		}
	}

	@Override
	public void addNewStepTask(FriendsRecallBaseT taskT) {
		int state = 0;
		// 如果已经完成，直接设置可领奖状态
		if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget8KingLv)) {
			if (roleDB.getLevel() >= taskT.demand) {
				state = 1;
			}
		} else if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget8AccCharge)) {
			if (roleDB.getRoleFriendsRecalled().getChargeAmount() >= taskT.demand) {
				state = 1;
			}
		} else if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget8Sign)) {
			if (roleDB.getRoleFriendsRecalled().getSignCount() >= taskT.demand) {
				state = 1;
			}
		} else if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9Growup)) {
			if (roleDB.getRoleFriendsInvitation().getMaxLevel() >= taskT.demand) {
				state = 1;
			}
		} else if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccConsumption)) {
			if (roleDB.getRoleFriendsInvitation().getConsumeYuanBao() >= taskT.demand) {
				state = 1;
			}
		} else if (taskT.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccRecall)) {
			if (XsgFriendsRecallManager.getInstance().getRecalledFriends(roleDB.getId()).size() >= taskT.demand) {
				state = 1;
			}
		}
		roleDB.getRoleRecallTask().add(
				new RoleRecallTask(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, taskT.taskId, state,
						Calendar.getInstance().getTime()));
		if (state == 1) {
			this.notifyRedPoint();
		}
	}

	/**
	 * 获取初始任务
	 * 
	 * @param type
	 * @return
	 */
	private List<RoleRecallTask> queryInitTask(int type, Role recalled) {
		List<RoleRecallTask> taskList = new ArrayList<RoleRecallTask>();
		List<FriendsRecallBaseT> initList = XsgFriendsRecallManager.getInstance().getInitTask(type);
		for (FriendsRecallBaseT t : initList) {
			int state = 0;
			if (t.prefixTask == 0) {
				if (t.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget8KingLv)
						&& roleDB.getLevel() >= t.demand) {
					state = 1;
				}
				if (recalled != null && t.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9Growup)
						&& recalled.getLevel() >= t.demand) {
					state = 1;
				}
				if (recalled != null && t.target.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccRecall)) {
					state = 1;
				}
				if (state == 1) {
					this.notifyRedPoint();
				}
			}
			taskList.add(new RoleRecallTask(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, t.taskId,
					state, Calendar.getInstance().getTime()));
		}

		return taskList;
	}

	/**
	 * 获取任务列表
	 * 
	 * @param type
	 * @return
	 */
	private List<FriendsRecallTaskView> queryRecallTaskList(int type) {
		List<FriendsRecallTaskView> taskList = new ArrayList<FriendsRecallTaskView>();
		FriendsRecallBaseT taskT = null;

		boolean hasInvitationTask = false;
		for (RoleRecallTask rt : roleDB.getRoleRecallTask()) {
			taskT = XsgFriendsRecallManager.getInstance().getTaskMap().get(rt.getTaskId());
			if (taskT == null) {
				continue;
			}
			if (taskT.taskType == XsgFriendsRecallManager.TASK_TYPE_INVITATION) {
				hasInvitationTask = true;
			}
			if ((rt.getState() == 2 && taskT.endTask == 0) || taskT == null || taskT.taskType != type
					|| !DateUtil.isBetween(DateUtil.parseDate(taskT.startTime), DateUtil.parseDate(taskT.endTime))) {
				continue;
			}
			taskList.add(generateTaskVo(taskT, rt.getState()));
		}

		if (taskList.size() == 0 && type == XsgFriendsRecallManager.TASK_TYPE_INVITATION && !hasInvitationTask) {
			List<RoleRecallTask> tl = queryInitTask(XsgFriendsRecallManager.TASK_TYPE_INVITATION, null);
			if (tl.size() == 0) {
				return taskList;
			}
			roleDB.getRoleRecallTask().addAll(tl);
			return queryRecallTaskList(type);
		}

		Collections.sort(taskList, new TaskSort());
		return taskList;
	}

	/**
	 * 拼召回日期字符串
	 * 
	 * @param time
	 * @return
	 */
	private String combineRecallDateStr(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return MessageFormat.format(Messages.getString("FriendsRecallController.recallDateStr"), month, day);
	}

	private FriendsRecallTaskView generateTaskVo(FriendsRecallBaseT t, int state) {
		FriendsRecallTaskView view = new FriendsRecallTaskView();
		view.content = t.content;
		view.icon = t.icon;
		view.itemCode = t.rewardItem;
		view.itemNum = t.rewardItemCount;
		view.rewardExp = t.rewardExp;
		view.rewardGold = t.rewardGold;
		view.state = state;
		view.taskId = t.taskId;
		view.title = t.title;
		view.totalYuanbao = XsgFriendsRecallManager.getInstance().getTaskYuanbaoCount(t.target);
		if (t.maxCount > 0) {
			if (t.target == XsgFriendsRecallManager.taskTarget8AccCharge) {
				view.currNum = roleDB.getRoleFriendsRecalled().getChargeAmount();
				view.targetNum = t.maxCount;
			} else if (t.target == XsgFriendsRecallManager.taskTarget9AccRecall) {
				List<String> idList = XsgFriendsRecallManager.getInstance().getRecalledFriends(roleDB.getId());
				view.currNum = idList == null ? 0 : idList.size();
				view.targetNum = t.maxCount;
			}
		}
		return view;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean note = false;
		List<RoleRecallTask> taskList = roleDB.getRoleRecallTask();
		if (taskList == null || taskList.size() == 0) {
			return null;
		}
		for (RoleRecallTask task : taskList) {
			if (task.getState() == 1) {
				note = true;
				break;
			}
		}
		return note ? new MajorUIRedPointNote(MajorMenu.FriendsRecallTask, false) : null;
	}

	@Override
	public void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int changeValue) {
		RoleFriendsRecalled fr = roleDB.getRoleFriendsRecalled();
		// 如果本人还没有召回，不处理
		if (fr == null || fr.getState() != 2) {
			return;
		}

		int consumeAmount = oldBind + oldUnbind - newBind - newUnbind;
		if (consumeAmount > 0) {
			processTaskOfInviteRoleAsync(fr.getInviteRoleId(), consumeAmount, null,
					XsgFriendsRecallManager.taskTarget9AccConsumption);
		}
	}

	@Override
	public void sign(int day) {
		processSign(1);
	}

	@Override
	public void resign(Integer[] resignedDay, int totalCost) {
		processSign(resignedDay.length);
	}

	@Override
	public void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency) {
		RoleFriendsRecalled fr = roleDB.getRoleFriendsRecalled();
		// 如果本人还没有召回，不处理
		if (fr == null || fr.getState() != 2) {
			return;
		}
		int oldAmount = fr.getChargeAmount();
		int amount = XsgVipManager.getInstance().getChargeItemT(params.item).yuanbao;
		fr.setChargeAmount(oldAmount + amount);

		processUncompleteTask(fr.getChargeAmount(), XsgFriendsRecallManager.taskTarget8AccCharge);
		processTaskOfInviteRoleAsync(fr.getInviteRoleId(), oldAmount, fr.getChargeAmount(),
				XsgFriendsRecallManager.taskTarget9AccCharge);
	}

	@Override
	public void onRoleLevelup() {
		RoleFriendsRecalled fr = roleDB.getRoleFriendsRecalled();
		// 如果本人还没有召回，不处理
		if (fr == null || fr.getState() != 2) {
			return;
		}
		processUncompleteTask(roleDB.getLevel(), XsgFriendsRecallManager.taskTarget8KingLv);
		processTaskOfInviteRoleAsync(fr.getInviteRoleId(), roleDB.getLevel(), null,
				XsgFriendsRecallManager.taskTarget9Growup);
	}

	@Override
	public void processStepTask(int currValue, String taskTarget) {
		if (taskTarget.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9Growup)) {
			if (currValue > roleDB.getRoleFriendsInvitation().getMaxLevel()) {
				roleDB.getRoleFriendsInvitation().setMaxLevel(currValue);
			}
			currValue = roleDB.getRoleFriendsInvitation().getMaxLevel();
		} else if (taskTarget.equalsIgnoreCase(XsgFriendsRecallManager.taskTarget9AccConsumption)) {
			roleDB.getRoleFriendsInvitation().setConsumeYuanBao(
					roleDB.getRoleFriendsInvitation().getConsumeYuanBao() + currValue);
			currValue = roleDB.getRoleFriendsInvitation().getConsumeYuanBao();
		}

		processUncompleteTask(currValue, taskTarget);
	}

	@Override
	public void processRepeatTask(int oldValue, int currValue, String taskTarget) {
		TreeMap<Integer, FriendsRecallTaskT> baseT = XsgFriendsRecallManager.getInstance()
				.getRecallTaskTMap(taskTarget);
		for (int demand : baseT.keySet()) {
			if (demand > currValue) {
				break;
			}
			if (demand > oldValue) {
				roleDB.getRoleRecallTask().add(
						new RoleRecallTask(GlobalDataManager.getInstance().generatePrimaryKey(), roleDB, baseT
								.get(demand).taskId, 1, Calendar.getInstance().getTime()));
				this.notifyRedPoint();
			}
		}
	}

	/**
	 * 红点提示
	 */
	private void notifyRedPoint() {
		this.roleRt.getNotifyControler().onMajorUIRedPointChange(
				new MajorUIRedPointNote(MajorMenu.FriendsRecallTask, false));
	}

	/**
	 * 处理我的邀请人的邀请任务
	 * 
	 * @param inviteRoleId
	 * @param value1
	 * @param value2
	 * @param target
	 */
	private void processTaskOfInviteRoleAsync(final String inviteRoleId, final int value1, final Integer value2,
			final String target) {
		if (TextUtil.isNotBlank(inviteRoleId)) {
			List<String> idList = new ArrayList<String>();
			idList.add(inviteRoleId);
			XsgRoleManager.getInstance().loadRoleAsync(idList, new Runnable() {
				@Override
				public void run() {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(inviteRoleId);
					if (value2 == null) {
						iRole.getFriendsRecallController().processStepTask(value1, target);
					} else {
						iRole.getFriendsRecallController().processRepeatTask(value1, value2, target);
					}
				}
			});
		}
	}

	/**
	 * 处理身上的未完成的任务（state=0）
	 * 
	 * @param currValue
	 * @param target
	 */
	private void processUncompleteTask(int currValue, String target) {
		// 当前回归任务
		RoleRecallTask currTask = findBindedUncompleteTask(target);
		// 如果任务已全部完成，则不处理
		if (currTask != null) {
			if (currTask.getState() == 0
					&& currValue >= XsgFriendsRecallManager.getInstance().getTaskMap().get(currTask.getTaskId()).demand) {
				currTask.setState(1);
				this.notifyRedPoint();
			}
		}
	}

	/**
	 * 处理签到任务，包含每日签到和补签
	 * 
	 * @param days
	 */
	private void processSign(int days) {
		final RoleFriendsRecalled fr = roleDB.getRoleFriendsRecalled();
		// 如果本人还没有召回，不处理
		if (fr == null || fr.getState() != 2) {
			return;
		}
		final int oldSign = fr.getSignCount();
		fr.setSignCount(oldSign + days);

		processUncompleteTask(fr.getSignCount(), XsgFriendsRecallManager.taskTarget8Sign);

		processTaskOfInviteRoleAsync(fr.getInviteRoleId(), oldSign, fr.getSignCount(),
				XsgFriendsRecallManager.taskTarget8Sign);
	}

	/**
	 * 根据任务目标查找身上的未完成任务
	 * 
	 * @param target
	 * @return
	 */
	private RoleRecallTask findBindedUncompleteTask(String target) {
		for (RoleRecallTask rt : roleDB.getRoleRecallTask()) {
			if (XsgFriendsRecallManager.getInstance().getTaskMap().get(rt.getTaskId()) == null)
				continue;
			if (rt.getState() != 2
					&& XsgFriendsRecallManager.getInstance().getTaskMap().get(rt.getTaskId()).target
							.equalsIgnoreCase(target)) {
				return rt;
			}
		}
		return null;
	}

	private void loadRandomRoleAsync(final String ownerId, final String currRoleIds, final Runnable logicCallback) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				int needCount = 3;
				try {
					FriendsRecallCfgT config = XsgFriendsRecallManager.getInstance().getFriendsRecallCfgT();
					List<String> list = RoleDAO.getFromApplicationContext(ServerLancher.getAc()).findOfflineRoleIdList(
							config.roleLevel, config.offlineDays);
					if (list == null || list.size() == 0) {
						XsgFriendsRecallManager.getInstance().removeRandomRole(ownerId);
					} else if (list.size() <= needCount) {
						batchLoadRole(list);
						XsgFriendsRecallManager.getInstance().setRandomRoleIdMap(ownerId, list);
					} else {
						List<String> otherList = new ArrayList<String>();
						List<String> currList = new ArrayList<String>();
						if (TextUtil.isNotBlank(currRoleIds)) {
							currList = Arrays.asList(currRoleIds.split(","));
						}
						while (list.size() > 0) {
							String randomId = list.get(NumberUtil.random(list.size()));
							if (!currList.contains(randomId)) {
								otherList.add(randomId);
								if (otherList.size() >= needCount) {
									break;
								}
							}
							list.remove(randomId);
						}
						for (int i = needCount - otherList.size(); i > 0; i--) {
							String randomId = currList.get(NumberUtil.random(currList.size()));
							otherList.add(randomId);
							currList.remove(randomId);
						}
						XsgFriendsRecallManager.getInstance().setRandomRoleIdMap(ownerId, otherList);
						batchLoadRole(otherList);
					}
				} catch (Exception e) {
					LogManager.error(e);
				}
				LogicThread.execute(logicCallback);
			}
		});
	}

	private void batchLoadRole(List<String> roleIds) {
		for (String id : roleIds) {
			XsgRoleManager.getInstance().loadRoleById(id);
		}
	}

	class TaskSort implements Comparator<FriendsRecallTaskView> {
		FriendsRecallBaseT taskT1 = null;

		FriendsRecallBaseT taskT2 = null;

		@Override
		public int compare(FriendsRecallTaskView v1, FriendsRecallTaskView v2) {
			if (v1.state > v2.state) {
				return -1;
			}
			if (v1.state < v2.state) {
				return 1;
			}
			taskT1 = XsgFriendsRecallManager.getInstance().getTaskMap().get(v1.taskId);
			taskT2 = XsgFriendsRecallManager.getInstance().getTaskMap().get(v2.taskId);
			if (taskT1.showOrder > taskT2.showOrder) {
				return 1;
			}
			if (taskT1.showOrder < taskT2.showOrder) {
				return -1;
			}
			if (taskT1.demand > taskT2.demand) {
				return 1;
			}
			if (taskT1.demand < taskT2.demand) {
				return -1;
			}
			return 0;
		}
	}

}
