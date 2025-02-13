package com.morefun.XSanGo.sns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.XSanGo.Protocol.AMD_Sns_acceptJunLing;
import com.XSanGo.Protocol.AMD_Sns_queryBattleRecordView;
import com.XSanGo.Protocol.AMD_Sns_sendJunLing;
import com.XSanGo.Protocol.AcceptJunLingResult;
import com.XSanGo.Protocol.BattleRecord;
import com.XSanGo.Protocol.BattleRecordRole;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Result;
import com.XSanGo.Protocol.SnsRoleView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.FriendApplyingHistory;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleChallengeSummary;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.db.game.RoleSnsJunLingLimit;
import com.morefun.XSanGo.db.game.RoleVitReceiving;
import com.morefun.XSanGo.event.protocol.IFriendApplying;
import com.morefun.XSanGo.event.protocol.ISnsAcceptJunLing;
import com.morefun.XSanGo.event.protocol.ISnsFriendPoint;
import com.morefun.XSanGo.event.protocol.ISnsRelationChange;
import com.morefun.XSanGo.event.protocol.ISnsSendJunLing;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.FindMovieListCallback;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

@RedPoint
public class SnsController implements ISnsControler {

	public static enum Status {
		applying, accept, refuse
	}

	public static enum RelationChangeEventActionType {
		add {
			@Override
			public String toString() {
				return Messages.getString("SnsController.0"); //$NON-NLS-1$
			}
		},
		remove {
			@Override
			public String toString() {
				return Messages.getString("SnsController.1"); //$NON-NLS-1$
			}
		},
		modify {
			@Override
			public String toString() {
				return Messages.getString("SnsController.2"); //$NON-NLS-1$
			}
		}
	}

	IRole roleRt;

	Role role;

	ISnsRelationChange relationChangeEvent;

	IFriendApplying friendApplyingEvent;

	ISnsSendJunLing sendJunLing;

	ISnsAcceptJunLing acceptJunLing;

	ISnsFriendPoint snsFriendPoint;

	private List<SnsRoleView> snsRoleViewList = null;
	private Date lastUpdateDate;
	private static long UpdateTime = 2 * 60 * 60 * 1000L; // 好友数据过期时间，暂定2h
	
	/**切磋战报最大查看条数*/
	private final static int BATTLE_RECORD_LIMIT = 20;
	
	/**战斗胜利失败次数记录*/
	private RoleChallengeSummary challengeSummary = null; 

	public SnsController(IRole roleRt, Role role) {
		this.roleRt = roleRt;
		this.role = role;
		actAbilityController = new ActAbilityController(roleRt, role);
		relationChangeEvent = roleRt.getEventControler().registerEvent(ISnsRelationChange.class);
		friendApplyingEvent = roleRt.getEventControler().registerEvent(IFriendApplying.class);
		sendJunLing = roleRt.getEventControler().registerEvent(ISnsSendJunLing.class);
		acceptJunLing = roleRt.getEventControler().registerEvent(ISnsAcceptJunLing.class);
		snsFriendPoint = roleRt.getEventControler().registerEvent(ISnsFriendPoint.class);

		GameSessionI session = GameSessionManagerI.getInstance().findSession(roleRt.getAccount(), roleRt.getRoleId());
		if (session != null) { // 通知客户端未接受好友请求
			for (FriendApplyingHistory applying : role.getFriendApplyingHistory().values()) {
				session.getSnsCallback().begin_applying(applying.getApplicantRoleId());
			}
		}
		
		challengeSummary = role.getChallengeSummary();
		if(challengeSummary == null) {
			challengeSummary = new RoleChallengeSummary(role);
			role.setChallengeSummary(challengeSummary);
		}
	}

	private void refreshData() {

		// 清理过期数据
		for (String k : new ArrayList<String>(role.getFriendApplyingHistory().keySet())) {
			if (k.equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
				continue;
			}
			try {
				FriendApplyingHistory applying = role.getFriendApplyingHistory().get(k);
				// 申请日期不详，删除
				if (applying.getDate() == null || "".equals(applying.getDate())) { //$NON-NLS-1$
					role.getFriendApplyingHistory().remove(k);
					continue;
				}
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyyMMdd").parse(applying //$NON-NLS-1$
						.getDate()));
				// 超过7天，删除
				if (DateUtil.addDays(c, 7).before(Calendar.getInstance())) {
					role.getFriendApplyingHistory().remove(k);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		// 保留20条数据
		if (role.getFriendApplyingHistory().size() > 20) {
			try {
				List<String> ks = new ArrayList<String>(role.getFriendApplyingHistory().keySet());
				ks.subList(ks.size() - 20, ks.size()).clear();
				for (String k : ks) {
					if (k.equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
						continue;
					}
					role.getFriendApplyingHistory().remove(k);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Collection<String> getFriends() {
		return XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FRIEND);
	}

	@Override
	public Collection<String> getFoes() {
		return XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FOE);
	}

	@Override
	public Collection<String> getBlacklist() {
		return XsgSnsManager.getInstance().grep(role.getSns(), SNSType.BLACKLIST);
	}

	public boolean hasGroup() {
		return false;
	}

	@Override
	public void accept(final SNSType type, final String player) throws Exception {
		if (type == null || player == null) {
			return;
		}
		if (contains(role.getSns(), player) != null) {
			remove(player);
			insertDB(player, type);
			relationChangeEvent.relationChanged(player, type, RelationChangeEventActionType.modify);
		} else {
			for (RoleSns sns : role.getSns()) {
				if (player.equals(sns.getTargetRoleId())) {
					String typeName = ""; //$NON-NLS-1$
					for (SNSType t : SNSType.values()) {
						if (t.getValue() == sns.getRelationType()) {
							typeName = t.getName();
						}
					}
					throw new Exception(typeName + Messages.getString("SnsController.6")); //$NON-NLS-1$
				}
			}

			relationChangeEvent.relationChanged(player, type, RelationChangeEventActionType.add);
			insertDB(player, type);
		}
		if (type.equals(SNSType.FRIEND) && findFriendApplyingHistory(player) != null) {
			removeApplyingHistory(player);
			refreshData();
			notifyAccepted();
			// 如果对方为林志玲，则直接发送军令
			if (XsgSnsManager.roleOfMsLing != null && player.equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
				sendJunLingOfMsLing();
			} else { // 使对方缓存数据失效，以便得到最新好友关系
				makeSnsRoleViewExpire(player);
			}
		}
		makeSnsRoleViewExpire();
	}
	
	private void makeSnsRoleViewExpire(final String targetId) {
		IRole player = XsgRoleManager.getInstance().findRoleById(targetId);
		if (player != null) { // 如果role数据不在内存中，则不需要处理
			player.getSnsController().makeSnsRoleViewExpire();
		}
	}

	public void insertDB(String rid, SNSType type) {

		role.getSns().add(new RoleSns(GlobalDataManager.getInstance().generatePrimaryKey(), role, rid, type.getValue(),
				0, 0, null));
	}

	/**
	 * 查找 @param player 发出的好友申请
	 */
	private FriendApplyingHistory findFriendApplyingHistory(final String player) {
		if (player == null) {
			return null;
		}
		for (FriendApplyingHistory applying : role.getFriendApplyingHistory().values()) {
			if (player.equals(applying.getApplicantRoleId())) {
				return applying;
			}
		}
		return null;
	}

	private void notifyAccepted() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(roleRt.getAccount(), roleRt.getRoleId());
		if (session != null) {
			session.getSnsCallback().begin_handleApplyingWith(Result.SUCCESSFUL);
		}
	}

	private void removeApplyingHistory(String applicant) {
		if (applicant == null) {
			return;
		}
		for (String id : new ArrayList<String>(role.getFriendApplyingHistory().keySet())) {
			if (applicant.equals(role.getFriendApplyingHistory().get(id).getApplicantRoleId())) {
				role.getFriendApplyingHistory().remove(id);
			}
		}
	}

	@Override
	public void refuse(final String player) {
		if (player == null) {
			return;
		}
		if (player.equals(XsgSnsManager.roleOfMsLing.getRoleId())) {
			return;
		}
		removeApplyingHistory(player);
	}

	private RoleSns contains(Collection<RoleSns> c, String player) {
		for (RoleSns sns : c) {
			if (sns.getTargetRoleId().equals(player)) {
				return sns;
			}
		}
		return null;
	}

	@Override
	public boolean applyForFriend(final String player) throws NoteException{
		if (player == null) {
			return false;
		}
		RoleSns rs = contains(role.getSns(), player);
		if (rs != null) {
			if (rs.getRelationType() != SNSType.FRIEND.getValue()) {
				remove(player);
				insertDB(player, SNSType.FRIEND);
				relationChangeEvent.relationChanged(player, SNSType.FRIEND, RelationChangeEventActionType.add);
			} else {
				throw new NoteException(Messages.getString("SnsI.friendAlready"));
			}
			if (role.getFriendApplyingHistory().containsKey(player)) {
				role.getFriendApplyingHistory().remove(player);
				return true;
			}
			return true;
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(player, new Runnable() {
			@Override
			public void run() {
				IRole targetRoleRt = XsgRoleManager.getInstance().findRoleById(player);

				if (targetRoleRt != null) { // 申请好友直接进好友列表的，对方是需要确的
					insertDB(player, SNSType.FRIEND);
					notifyAccepted();
					friendApplyingEvent.onApplyingHappend(player);

					// 如果对方先申请的自己，这次申请直接成功删除
					if (role.getFriendApplyingHistory().containsKey(player)) {
						role.getFriendApplyingHistory().remove(player);
						return;
					}
				}

				// 在对方的申请记录中添加一条数据
				if (targetRoleRt != null
						&& !targetRoleRt.getSnsController().getFriends().contains(roleRt.getRoleId())) {
					FriendApplyingHistory historyRecord = new FriendApplyingHistory(
							GlobalDataManager.getInstance().generatePrimaryKey(), role.getId(), null,
							SNSType.FRIEND.getValue(), new SimpleDateFormat("yyyyMMdd").format(new Date())); //$NON-NLS-1$

					targetRoleRt.addFriendApplyingHistory(historyRecord);
					// 如果对方在线，则触发红点通知
					if (targetRoleRt.isOnline()) {
						targetRoleRt.getNotifyControler()
								.onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.SnsMenu, true));
					}
				}
				if (targetRoleRt != null && targetRoleRt.getSnsController().getFriends().contains(roleRt.getRoleId())) {
					targetRoleRt.getSnsController().makeSnsRoleViewExpire();
				}
			}
		}, new Runnable() {
			@Override
			public void run() {
			}
		});
		return true;
	}

	@Override
	public void remove(final SNSType type, final String player) {
		if (type == null || player == null) {
			return;
		}
		for (RoleSns r : new ArrayList<RoleSns>(role.getSns())) {
			if (type.getValue() == r.getRelationType() && player.equals(r.getTargetRoleId())) {
				role.getSns().remove(r);
				relationChangeEvent.relationChanged(player, type, RelationChangeEventActionType.remove);
			}
		}
	}

	private void remove(String player) {
		if (player == null) {
			return;
		}
		for (RoleSns r : new ArrayList<RoleSns>(role.getSns())) {
			if (player.equals(r.getTargetRoleId())) {
				role.getSns().remove(r);
				relationChangeEvent.relationChanged(player, SNSType.of(r.getRelationType()),
						RelationChangeEventActionType.remove);
			}
		}
	}

	@Override
	public void clear(final SNSType type) {
		if (type == null) {
			return;
		}

		for (RoleSns r : new ArrayList<RoleSns>(role.getSns())) {
			if (type.getValue() == r.getRelationType()) {
				role.getSns().remove(r);
				relationChangeEvent.relationChanged(r.getTargetRoleId(), type, RelationChangeEventActionType.remove);
			}
		}

	}

	IActAbilityController actAbilityController;

	@Override
	public IActAbilityController getActAbilityController() {
		return actAbilityController;
	}

	@Override
	public Collection<FriendApplyingHistory> unTreatedApplying() {
		return role.getFriendApplyingHistory().values();
	}

	/**
	 * @return 获得该列表的人数上限
	 */
	public int maxSize(SNSType type) {
		if (type == null) {
			return 0;
		}
		SnsT snsT = XsgSnsManager.getInstance().snsT.get(10 * (roleRt.getVipLevel() / 10 + 1));// 配置里是10级1个档次
		if (snsT == null) {
			LogManager.warn("sns好友数上限 配置没读取到"); //$NON-NLS-1$
			return 0;
		}
		VipT vipT = XsgVipManager.getInstance().findVipT(roleRt.getVipLevel());
		if (vipT == null) {
			LogManager.warn("vip好友数上限 配置没读取到"); //$NON-NLS-1$
			return 0;
		}
		switch (type) {
		case FRIEND:
			return snsT.MaxNum + vipT.FriendNum;
		case FOE:
			return snsT.MaxNum + vipT.EnemyNum;
		case BLACKLIST:
			return snsT.MaxNum + vipT.BlacklistNum;
		default:
			return 0;
		}
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		// return getActAbilityController().getUnCollectedVits().size() > 0 &&
		// unTreatedApplying().size() > 0 ? new MajorUIRedPointNote(
		// MajorMenu.SnsMenu, true) : null;
		boolean hasRedPoint = getActAbilityController().getUnCollectedVits().size() > 0
				|| unTreatedApplying().size() > 0;
		if (!hasRedPoint) {
			hasRedPoint = hasAcceptableJunling();
		}

		return (hasRedPoint ? new MajorUIRedPointNote(MajorMenu.SnsMenu, true) : null);
	}

	@Override
	public void giveJunLingByFriend(String friendId, int num) throws NoteException {
		if (TextUtil.isBlank(friendId)) {
			throw new NoteException(Messages.getString("SnsController.NotExist"));
		}
		RoleSns rsns = XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FRIEND, friendId);
		if (rsns == null) {
			throw new NoteException(Messages.getString("SnsController.NotExist"));
		}
		// 设置军令数量和时间
		rsns.setAcceptJunLingNum(num);
		rsns.setAcceptJunLingTime(Calendar.getInstance().getTime());
	}

	/**
	 * 获取今日送出军令的数量
	 */
	public int getSendJunLingNum() {
		int count = 0;
		Map<String, RoleSnsJunLingLimit> limitMap = role.getRoleSnsJunLingLimit();
		for (Map.Entry<String, RoleSnsJunLingLimit> entry : limitMap.entrySet()) {
			RoleSnsJunLingLimit limit = entry.getValue();
			if (limit.getSendNum() > 0) {
				count += limit.getSendNum();
			}
		}
		return count;
	}

	public int getAcceptJunLingNum() {
		int count = 0;
		Map<String, RoleSnsJunLingLimit> limitMap = role.getRoleSnsJunLingLimit();
		for (Map.Entry<String, RoleSnsJunLingLimit> entry : limitMap.entrySet()) {
			RoleSnsJunLingLimit limit = entry.getValue();
			if (limit.getRecvNum() > 0) {
				count += limit.getRecvNum();
			}
		}
		return count;
	}

	private RoleSnsJunLingLimit getJunLingLimit(String targetId) {
		return role.getRoleSnsJunLingLimit().get(targetId);
	}

	public void addOrUpdateSendJunLingLimit(String targetId, int num) {
		RoleSnsJunLingLimit limit = getJunLingLimit(targetId);
		Date current = Calendar.getInstance().getTime();
		if (limit == null) {
			limit = new RoleSnsJunLingLimit(0, role, targetId, num, current, 0, current);
			role.getRoleSnsJunLingLimit().put(limit.getTargetId(), limit);
		} else {
			limit.setSendNum(num);
			limit.setSendTime(current);
		}
	}

	private void addOrUpdateRecvJunLingLimit(String targetId, int num) {
		RoleSnsJunLingLimit limit = getJunLingLimit(targetId);
		Date current = Calendar.getInstance().getTime();
		if (limit == null) {
			limit = new RoleSnsJunLingLimit(0, role, targetId, 0, current, num, current);
			role.getRoleSnsJunLingLimit().put(limit.getTargetId(), limit);
		} else {
			limit.setRecvNum(num);
			limit.setRecvTime(current);
		}
	}

	@Override
	public void sendJunLing(String targetId, final AMD_Sns_sendJunLing __cb) {
		if (TextUtil.isBlank(targetId)) {
			__cb.ice_exception(new NoteException(Messages.getString("SnsController.NotExist")));
			return;
		}
		clearJunLingStatus();
		final SnsJunLingT snsJunLingT = XsgSnsManager.getInstance().getSnsJunLingT(roleRt.getVipLevel());
		int sendCount = getSendJunLingNum();
		int lastNum = snsJunLingT.sendMaxNum - sendCount;
		// 检查可以赠送的次数
		if (lastNum <= 0) {
			// 剩余次数已经用完，直接返回
			return;
		}

		Set<String> targetIDList = new HashSet<String>(Arrays.asList(targetId.split(",")));
		final Map<String, RoleSns> targetList = new HashMap<String, RoleSns>();
		int count = lastNum;
		final Map<String, Integer> sendNumMap = new HashMap<String, Integer>();
		for (RoleSns rs : role.getSns()) {
			// 增加没有送过的好友
			if (rs.getRelationType() == SNSType.FRIEND
					.getValue() /*
								 * && rs. getSendJunLingNum () <= 0
								 */
					&& targetIDList.contains(rs.getTargetRoleId())) {
				RoleSnsJunLingLimit limit = getJunLingLimit(rs.getTargetRoleId());
				// 当天没有送过
				if (limit == null || limit.getSendNum() <= 0) {
					int sendNum = Math.min(count, snsJunLingT.sendNum);
					targetList.put(rs.getTargetRoleId(), rs);
					sendNumMap.put(rs.getTargetRoleId(), sendNum);
					count -= sendNum; // 剩余次数检查
				}
			}
			if (count <= 0) { // 剩余次数检查
				break;
			}
		}
		// 赠送军令
		final String selfId = roleRt.getRoleId();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(targetList.keySet()), new Runnable() {
			@Override
			public void run() {
				NoteException ne = null;
				int count = 0;
				for (Map.Entry<String, RoleSns> entry : targetList.entrySet()) {
					String targetId = entry.getKey();
					IRole role = XsgRoleManager.getInstance().findRoleById(targetId);
					if (XsgSnsManager.roleOfMsLing != null && XsgSnsManager.roleOfMsLing.getRoleId().equals(targetId)) {
						role = XsgSnsManager.roleOfMsLing;
					}
					if (role != null) {
						try {
							int sendNum = sendNumMap.get(targetId);
							role.getSnsController().giveJunLingByFriend(selfId, sendNum);
							// 记录送出记录
							addOrUpdateSendJunLingLimit(targetId, sendNum);
							count++;
							// 事件
							sendJunLing.onSnsSendJunLing(roleRt.getRoleId(), targetId, sendNum);
							// 如果对方在线，则触发红点通知
							if (role.isOnline()) {
								role.getNotifyControler()
										.onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.SnsMenu, true));
							}
							if (XsgSnsManager.roleOfMsLing != null
									&& XsgSnsManager.roleOfMsLing.getRoleId().equals(targetId)) {
								role.saveAsyn();
							}
						} catch (NoteException e) {
							ne = e;
						}
					}
				}
				// 批量赠送不返回异常通知
				if (targetList.size() > 1 || ne == null) {
					__cb.ice_response(count);
					return;
				}
				if (ne != null) {
					__cb.ice_exception(ne);
				}
			}
		});

	}

	public void clearJunLingStatus() {
		Date checkPoint = DateUtil.joinTime("00:00:00");
		Map<String, RoleSnsJunLingLimit> limitMap = role.getRoleSnsJunLingLimit();
		if (limitMap != null && limitMap.size() > 0) {
			for (Map.Entry<String, RoleSnsJunLingLimit> entry : limitMap.entrySet()) {
				RoleSnsJunLingLimit limit = entry.getValue();
				if (limit.getRecvNum() > 0 && limit.getRecvTime() != null
						&& DateUtil.isPass(checkPoint, limit.getRecvTime())) {
					limit.setRecvNum(0);
					limit.setRecvTime(checkPoint);
				}
				if (limit.getSendNum() > 0 && limit.getSendTime() != null
						&& DateUtil.isPass(checkPoint, limit.getSendTime())) {
					limit.setSendNum(0);
					limit.setSendTime(checkPoint);
				}
			}
		}
		if (role.getSns() != null && role.getSns().size() > 0) {
			for (RoleSns rs : role.getSns()) {
				if (rs.getAcceptJunLingNum() > 0 && rs.getAcceptJunLingTime() != null
						&& DateUtil.isPass(checkPoint, rs.getAcceptJunLingTime())) {
					rs.setAcceptJunLingNum(0);
					rs.setAcceptJunLingTime(checkPoint);
				}
			}
		}
	}

	private boolean hasAcceptableJunling() {
		SnsJunLingT snsJunLingT = XsgSnsManager.getInstance().getSnsJunLingT(roleRt.getVipLevel());
		if (snsJunLingT != null) {
			int lastNum = snsJunLingT.acceptMaxNum - getAcceptJunLingNum();
			if (lastNum > 0 && role.getSns() != null) {
				for (RoleSns rs : role.getSns()) {
					if (rs.getRelationType() == SNSType.FRIEND.getValue()) {
						String targetId = rs.getTargetRoleId();
						if (rs.getAcceptJunLingNum() > 0) {
							RoleSnsJunLingLimit limit = getJunLingLimit(targetId);
							if (limit == null || limit.getRecvNum() <= 0) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void acceptJunLing(String targetId, final AMD_Sns_acceptJunLing __cb) {
		if (TextUtil.isBlank(targetId)) {
			__cb.ice_exception(new NoteException(Messages.getString("SnsController.NotExist")));
			return;
		}

		clearJunLingStatus();
		XsgSnsManager snsManager = XsgSnsManager.getInstance();
		final SnsJunLingT snsJunLingT = snsManager.getSnsJunLingT(roleRt.getVipLevel());
		final int lastNum = snsJunLingT.acceptMaxNum - getAcceptJunLingNum();
		if (lastNum <= 0) {
			__cb.ice_exception(new NoteException(Messages.getString("SnsController.UseLess")));
			return;
		}
		final List<String> targetIdArray = Arrays.asList(targetId.split(","));
		XsgRoleManager.getInstance().loadRoleAsync(targetIdArray, new Runnable() {
			@Override
			public void run() {
				// 还可以领取的总数量
				int allCanAcceptNum = lastNum;
				AcceptJunLingResult result = new AcceptJunLingResult(0, 0);
				Set<String> targetIdList = new HashSet<String>(targetIdArray);
				for (RoleSns rs : role.getSns()) {
					String targetId = rs.getTargetRoleId();
					RoleSnsJunLingLimit limit = getJunLingLimit(targetId);
					if (limit == null || limit.getRecvNum() <= 0) { // 还没领取过
						if (rs.getRelationType() == SNSType.FRIEND.getValue() && rs.getAcceptJunLingNum() > 0
								&& targetIdList.contains(rs.getTargetRoleId())) {
							// 可以领取的点数
							int acceptedNum = Math.min(rs.getAcceptJunLingNum(), allCanAcceptNum);
							// 领取军令
							roleRt.getRewardControler().acceptReward(Const.PropertyName.JUNLING_TEMPLATE_ID,
									acceptedNum);
							// 给对方增加友情点
							IRole friendRole = XsgRoleManager.getInstance().findRoleById(rs.getTargetRoleId());
							if (friendRole != null) {
								friendRole.getSnsController().addFriendPoint(roleRt.getRoleId(),
										snsJunLingT.friendPoint);
							}
							// 自己增加友情点
							addFriendPoint(rs.getTargetRoleId(), snsJunLingT.friendPoint);
							// 增加领取记录
							addOrUpdateRecvJunLingLimit(targetId, acceptedNum);

							result.junLingNum += acceptedNum;
							result.friendPoint += snsJunLingT.friendPoint;
							allCanAcceptNum -= acceptedNum;

							rs.setAcceptJunLingNum(0);

							if (allCanAcceptNum <= 0) {
								break;
							}
						}
					}
				}
				__cb.ice_response(LuaSerializer.serialize(result));
			}
		});

		// 事件
		acceptJunLing.onSnsAcceptJunLing(roleRt.getRoleId(), targetId);
	}

	@Override
	public int getSendJunLingNumToFriend(String targetId) {
		RoleSnsJunLingLimit limit = getJunLingLimit(targetId);
		if (limit != null) {
			return limit.getSendNum();
		}
		return 0;
	}

	@Override
	public int getAcceptJunLingNumFromFriend(String targetId) {
		RoleSns rs = XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FRIEND, targetId);
		if (rs != null) {
			return rs.getAcceptJunLingNum();
		}
		return 0;
	}

	@Override
	public int getFriendPoint(String targetId) {
		RoleSns rs = XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FRIEND, targetId);
		if (rs != null) {
			return rs.getFriendPoint();
		}
		return 0;
	}

	@Override
	public void addFriendPoint(String targetId, int num) {
		RoleSns rs = XsgSnsManager.getInstance().grep(role.getSns(), SNSType.FRIEND, targetId);
		if (rs != null) {
			rs.setFriendPoint(rs.getFriendPoint() + num);
			snsFriendPoint.onAddFriendPoint(targetId, num, rs.getFriendPoint());
		}
	}

	@Override
	public List<RoleSns> getRoleSns(SNSType type) {
		Set<RoleSns> allSns = role.getSns();
		List<RoleSns> list = new ArrayList<RoleSns>();
		for (RoleSns sns : allSns) {
			if (sns.getRelationType() == type.getValue()) {
				list.add(sns);
			}
		}
		return list;
	}

	@Override
	public int getMaxFriendPoint() {
		int max = 0;
		Set<RoleSns> allSns = role.getSns();
		if (allSns != null) {
			for (RoleSns rs : allSns) {
				if (rs.getRelationType() == SNSType.FRIEND.getValue() && rs.getFriendPoint() > max) {
					max = rs.getFriendPoint();
				}
			}
		}
		return max;
	}

	@Override
	public void processBenefitFromMsLing() {
		IRole roleOfMsLing = XsgSnsManager.roleOfMsLing;
		// 自动生成林志玲对自己的好友申请，如果还不是好友或者没有申请的话
		if (!roleRt.getSnsController().getFriends().contains(roleOfMsLing.getRoleId())) {
			boolean hasMsLing = false;
			for (FriendApplyingHistory history : role.getFriendApplyingHistory().values()) {
				if (history.getApplicantRoleId().equals(roleOfMsLing.getRoleId())) {
					hasMsLing = true;
					break;
				}
			}
			if (!hasMsLing) {
				roleRt.addFriendApplyingHistory(new FriendApplyingHistory(
						GlobalDataManager.getInstance().generatePrimaryKey(), roleOfMsLing.getRoleId(), role,
						SNSType.FRIEND.getValue(), new SimpleDateFormat("yyyyMMdd").format(new Date())));
				roleOfMsLing.getSnsController().addFriend(role.getId());
				roleOfMsLing.saveAsyn();
			}
		} else {
			sendJunLingOfMsLing();
		}
	}
	
	public void addFriend(String id) {
		role.getSns().add(new RoleSns(GlobalDataManager.getInstance().generatePrimaryKey(),
				role, id, SNSType.FRIEND.getValue(), 0, 0, null));
		roleRt.saveAsyn();
	}

	/**
	 * 林志玲发军令
	 * 
	 */
	private void sendJunLingOfMsLing() {
		IRole roleOfMsLing = XsgSnsManager.roleOfMsLing;
		try {
			roleOfMsLing.getSnsController().clearJunLingStatus();
//			RoleSnsJunLingLimit limit = roleOfMsLing.getRoleDB().getRoleSnsJunLingLimit().get(role.getId());
			int limit = roleOfMsLing.getSnsController().getSendJunLingLimit(role.getId());
			// 当天没有送过, 就发军令
			if (limit <= 0) {
				int sendNum = XsgSnsManager.getInstance().getSnsJunLingT(roleOfMsLing.getVipLevel()).sendNum;
				giveJunLingByFriend(roleOfMsLing.getRoleId(), sendNum);
				// 记录送出记录
				roleOfMsLing.getSnsController().addOrUpdateSendJunLingLimit(roleRt.getRoleId(), sendNum);
				// 事件
				sendJunLing.onSnsSendJunLing(roleOfMsLing.getRoleId(), roleRt.getRoleId(), sendNum);
				roleOfMsLing.saveAsyn();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSendJunLingLimit(String id) {
		RoleSnsJunLingLimit limit = role.getRoleSnsJunLingLimit().get(id);
		if (limit != null) {
			return limit.getSendNum();
		}
		return 0;
	}

	/**
	 * 好友缓存数据是否过期
	 */
	private boolean isSnsRoleViewDataExpire() {
		if (lastUpdateDate == null || snsRoleViewList == null) {
			return true;
		}
		long current = System.currentTimeMillis();
		long last = lastUpdateDate.getTime();
		if (current - last > UpdateTime) { // 失效
			return true;
		}
		return false;
	}

	public SnsRoleView asView(IRole rolefriend, boolean canApplying) {
		Collection<RoleVitReceiving> records = getActAbilityController().getUnCollectedVits();

		boolean canReceive = false;
		for (RoleVitReceiving record : records) {
			if (record.getSenderRoleId().equals(rolefriend.getRoleId()))
				canReceive = true;
		}
		long current = System.currentTimeMillis();
		long logoutTime = current;
		Date logoutDate = rolefriend.getLogoutTime();
		if (logoutDate != null) {
			logoutTime = logoutDate.getTime();
		}
		long timeInterval = current - logoutTime;
		return new SnsRoleView(rolefriend.getRoleId(), rolefriend.isOnline(), rolefriend.getHeadImage(),
				rolefriend.getName(), rolefriend.getLevel(), rolefriend.getVipLevel(), rolefriend.getCachePower(),
				canReceive, canApplying, getAcceptJunLingNumFromFriend(rolefriend.getRoleId()),
				getSendJunLingNumToFriend(rolefriend.getRoleId()), getFriendPoint(rolefriend.getRoleId()), timeInterval,
				rolefriend.getSnsController().isMyFriend(roleRt.getRoleId()));
	}

	private void updateJunLingStatus(Collection<SnsRoleView> snsViews) {
		if (snsViews != null) {
			for (SnsRoleView view : snsViews) {
				String friendId = view.id;
				view.junLingNum = getAcceptJunLingNumFromFriend(friendId);
				view.sendJunLingNum = getSendJunLingNumToFriend(friendId);
				view.friendPoint = getFriendPoint(friendId);
			}
		}
	}
	
	private void sortSnsRoleViews(List<SnsRoleView> snsRoleViewList) {
		Collections.sort(snsRoleViewList, new Comparator<SnsRoleView>() {
			@Override
			public int compare(SnsRoleView o1, SnsRoleView o2) {
				if (o1.eachOther & !o2.eachOther) { // 互为好友的排在前面
					return -1;
				}
				if (!o1.eachOther & o2.eachOther) {
					return 1;
				}
				if (o2.friendPoint == o1.friendPoint) { // 好友点数相同，按等级排序
					return Integer.valueOf(o2.level).compareTo(o1.level);
				}
				// 按好友点数排序
				return Integer.valueOf(o2.friendPoint).compareTo(o1.friendPoint);
			}
			
		});
	}

	@Override
	public void getSnsRoleViews(final Collection<String> collection, final OnSnsRoleViewCallback cb) {
		if (cb == null) {
			return;
		}
		if (!isSnsRoleViewDataExpire()) {
			updateJunLingStatus(snsRoleViewList);
			sortSnsRoleViews(snsRoleViewList);
			cb.onSnsRoleView(snsRoleViewList);
			return;
		}
		snsRoleViewList = new ArrayList<SnsRoleView>();
		lastUpdateDate = Calendar.getInstance().getTime();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				for (String rid : collection) {
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						snsRoleViewList.add(asView(role, false));
					}
				}
				sortSnsRoleViews(snsRoleViewList);
				cb.onSnsRoleView(snsRoleViewList);
			}
		});
	}
	
	public static interface OnSnsRoleViewCallback {
		void onSnsRoleView(Collection<SnsRoleView> snsRoleViewList);
	}

	@Override
	public void makeSnsRoleViewExpire() {
		snsRoleViewList = null;
	}

	@Override
	public boolean isMyFriend(String targetID) {
		// 小林志玲，特殊处理
		if (XsgSnsManager.getInstance().NAME_OF_MS_LING.equals(roleRt.getName()) &&
				XsgRoleManager.Robot_Account.equals(roleRt.getAccount())) {
			return true;
		}
		if (TextUtil.isNotBlank(targetID)) {
			Collection<String> friends = getFriends();
			if (friends != null && friends.size() > 0) {
				for (String id : friends) {
					if (targetID.equals(id)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 切磋战报记录
	 */
	@Override
	public void queryBattleRecordView(final AMD_Sns_queryBattleRecordView __cb)
			throws NoteException {
		XsgFightMovieManager.getInstance().findFightMovieByRoleId(
			this.roleRt.getRoleId(),
			XsgFightMovieManager.Type.ChatFight.ordinal(),
			BATTLE_RECORD_LIMIT, new FindMovieListCallback() {
				@Override
				public void onFindMovieList(final List<Object[]> list_movie) {
					if (list_movie.size() == 0) {
						__cb.ice_response(LuaSerializer.serialize(new BattleRecord(0, 0, 0, 0, 0, new BattleRecordRole[0])));
						return;
					}
					final List<String> playerList = new ArrayList<String>();
					final Map<String, Integer> map_player_count = new HashMap<String, Integer>();
					for (int i = 0; i < list_movie.size(); i++) {
						final Object[] movie = list_movie.get(i);
						if (i < BATTLE_RECORD_LIMIT) { // 客户端显示20条记录
							playerList.add(String.valueOf(movie[1]));
						}
	
						if (map_player_count.get(movie[1]) == null) {
							map_player_count.put(String.valueOf(movie[1]), 0);
						}
						map_player_count.put(String.valueOf(movie[1]), map_player_count.get(movie[1]) + 1);
					}
	
					XsgRoleManager.getInstance().loadRoleAsync(playerList,
						new Runnable() {
							@Override
							public void run() {
								final List<BattleRecordRole> list_record = new ArrayList<BattleRecordRole>();
								int lastSuccess = 0; // 最近20场胜利的场数
								for (int i = 0; i < list_movie.size(); i++) {
									final Object[] movie = list_movie.get(i);

									if (i < BATTLE_RECORD_LIMIT) { // 客户端显示20条记录
										if (Integer.valueOf(movie[2].toString()) == 1) {
											lastSuccess += 1;
										}

										IRole findRole = XsgRoleManager.getInstance().findRoleById(String.valueOf(movie[1]));
										list_record.add(new BattleRecordRole(
												roleRt.getSnsController().asView(findRole, false),
												String.valueOf(movie[0]),
												String.valueOf(System.currentTimeMillis() - DateUtil.parseDate(String.valueOf(movie[3])).getTime()),
												map_player_count.get(findRole.getRoleId()),
												Integer.parseInt(movie[2].toString())));
									}
								}
								// 累计切磋次数
								int totalCount = challengeSummary.getSuccessTimes() + challengeSummary.getFailTimes(); 
								BattleRecord record = new BattleRecord(
										totalCount,
										role.getChallengeSummary().getSuccessTimes(),
										totalCount == 0 ? 0 : ((double) challengeSummary.getSuccessTimes() * 100 / totalCount),
										list_movie.size() > BATTLE_RECORD_LIMIT ? BATTLE_RECORD_LIMIT : list_movie.size(),
										(double) lastSuccess * 100 / (BATTLE_RECORD_LIMIT > list_movie.size() ? list_movie.size() : BATTLE_RECORD_LIMIT),
										list_record.toArray(new BattleRecordRole[list_record.size()]));
								__cb.ice_response(LuaSerializer.serialize(record));
								return;
							}
						});
					}
				});
		// __cb.ice_response(LuaSerializer.serialize(new BattleRecord(0, 0, 0, 0, new BattleRecordRole[0])));
	}
}
