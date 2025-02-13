package com.morefun.XSanGo.sns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Sns_acceptJunLing;
import com.XSanGo.Protocol.AMD_Sns_changeMorePlayers;
import com.XSanGo.Protocol.AMD_Sns_queryAllBlacklist;
import com.XSanGo.Protocol.AMD_Sns_queryAllFoes;
import com.XSanGo.Protocol.AMD_Sns_queryAllFreinds;
import com.XSanGo.Protocol.AMD_Sns_queryAllFreindsView;
import com.XSanGo.Protocol.AMD_Sns_queryBattleRecordView;
import com.XSanGo.Protocol.AMD_Sns_queryPlayersLike;
import com.XSanGo.Protocol.AMD_Sns_sendJunLing;
import com.XSanGo.Protocol.AMD_Sns_untreatedFriendApplyings;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OpenSnsView;
import com.XSanGo.Protocol.SnsRoleView;
import com.XSanGo.Protocol._SnsDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.db.game.FriendApplyingHistory;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.sns.SnsController.OnSnsRoleViewCallback;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 好友,仇人,黑名单相关的功能
 */
public class SnsI extends _SnsDisp {
	private static final long serialVersionUID = -1547471257063795992L;
	// private static final Log log = LogFactory.getLog(SnsI.class);

	private IRole roleRt;
	
	public SnsI(IRole r) {
		this.roleRt = r;
	}

	@Override
	public void applyForFriend(String player, Current __current) throws NoteException {
		if (roleRt.getSnsController().getFriends().size() >= roleRt.getSnsController().maxSize(SNSType.FRIEND)) {
			throw new NoteException(Messages.getString("SnsI.0")); //$NON-NLS-1$
		}
		if (roleRt.getSnsController().applyForFriend(player)) {
			IRole target = XsgRoleManager.getInstance().findRoleById(player);
			if (target == null) {
				return;
			}
			GameSessionI session = GameSessionManagerI.getInstance().findSession(target.getAccount(),
					target.getRoleId());
			if (session != null) {
				session.getSnsCallback().begin_applying(player);
			}
			roleRt.getSnsController().makeSnsRoleViewExpire();
		}
	}

	@Override
	public void accept(String player, Current __current) throws NoteException {
		if (roleRt.getSnsController().getFriends().size() >= roleRt.getSnsController().maxSize(SNSType.FRIEND)) {
			throw new NoteException(Messages.getString("SnsI.1")); //$NON-NLS-1$
		}
		try {
			if (player == null) {
				return;
			} else if (player.isEmpty()) {
				List<String> acceptList = new ArrayList<String>();
				for (FriendApplyingHistory applying : roleRt.getSnsController().unTreatedApplying()) {
					acceptList.add(applying.getApplicantRoleId());
				}
				if (acceptList.size() > 0) {
					for (String applyId : acceptList) {
						// 检查好友数量上限限制
						if (roleRt.getSnsController().getFriends().size() >= roleRt.getSnsController()
								.maxSize(SNSType.FRIEND)) {
							break;
						}
						roleRt.getSnsController().accept(SNSType.FRIEND, applyId);
					}
				}
			} else {
				roleRt.getSnsController().accept(SNSType.FRIEND, player);
			}
		} catch (Exception e) {
			throw new NoteException(Messages.getString("SnsI.2"), e); //$NON-NLS-1$
		}
	}

	@Override
	public void refuse(String player, Current __current) {
		if (player == null) {
			return;
		} else if (player.isEmpty()) {
			List<String> removeList = new ArrayList<String>();
			for (FriendApplyingHistory applying : roleRt.getSnsController().unTreatedApplying()) {
				removeList.add(applying.getApplicantRoleId());
				// roleRt.getSnsController().refuse(applying.getApplicantRoleId());
			}
			for (String str : removeList) {
				roleRt.getSnsController().refuse(str);
			}
		} else {
			roleRt.getSnsController().refuse(player);
		}
	}

	@Override
	public void addFoe(String player, Current __current) throws NoteException {
		if (roleRt.getSnsController().getFoes().size() >= roleRt.getSnsController().maxSize(SNSType.FOE)) {
			throw new NoteException(Messages.getString("SnsI.3")); //$NON-NLS-1$
		}
		try {
			roleRt.getSnsController().accept(SNSType.FOE, player);
		} catch (Exception e) {
			throw new NoteException(Messages.getString("SnsI.4"), e); //$NON-NLS-1$
		}
	}

	@Override
	public void addBlacklist(String player, Current __current) throws NoteException {
		if (roleRt.getSnsController().getBlacklist().size() >= roleRt.getSnsController().maxSize(SNSType.BLACKLIST)) {
			throw new NoteException(Messages.getString("SnsI.5")); //$NON-NLS-1$
		}
		try {
			roleRt.getSnsController().accept(SNSType.BLACKLIST, player);
			
			// 拉黑操作则检测是否要静默禁言
			XsgChatManager.getInstance().addBlackCount(player, new Date(), this.roleRt.getRoleId());
		} catch (Exception e) {
			throw new NoteException(Messages.getString("SnsI.6"), e); //$NON-NLS-1$
		}
	}

	@Override
	public void removeFriend(String player, Current __current) {
		roleRt.getSnsController().remove(SNSType.FRIEND, player);
		roleRt.getSnsController().makeSnsRoleViewExpire();
		IRole target = XsgRoleManager.getInstance().findRoleById(player);
		if (target != null) {
			target.getSnsController().makeSnsRoleViewExpire();
		}
	}

	@Override
	public void removeFoe(String player, Current __current) {
		roleRt.getSnsController().remove(SNSType.FOE, player);
	}

	@Override
	public void removeBlacklist(String player, Current __current) {
		roleRt.getSnsController().remove(SNSType.BLACKLIST, player);
	}

	@Override
	public void cleanFoes(Current __current) {
		roleRt.getSnsController().clear(SNSType.FOE);
	}

	@Override
	public void cleanBlacklist(Current __current) {
		roleRt.getSnsController().clear(SNSType.BLACKLIST);
	}

	@Override
	public void queryAllFreindsView_async(final AMD_Sns_queryAllFreindsView __cb, Current __current) {
		final Collection<String> collection = roleRt.getSnsController().getFriends();

		roleRt.getSnsController().clearJunLingStatus();
		if (collection == null || collection.size() <= 0) {
			__cb.ice_response(LuaSerializer.serialize(new SnsRoleView[0]));
			return;
		}

		roleRt.getSnsController().getSnsRoleViews(collection, new OnSnsRoleViewCallback() {
			@Override
			public void onSnsRoleView(Collection<SnsRoleView> snsRoleViewList) {
				__cb.ice_response(LuaSerializer.serialize(snsRoleViewList.toArray(new SnsRoleView[collection.size()])));
			}
		});
	}

	@Override
	public void untreatedFriendApplyings_async(final AMD_Sns_untreatedFriendApplyings __cb, Current __current) {
		final Collection<String> collection = new HashSet<String>();
		Collection<FriendApplyingHistory> applyings = roleRt.getSnsController().unTreatedApplying();
		for (FriendApplyingHistory applying : applyings) {
			String rid = applying.getApplicantRoleId();
			collection.add(rid);
		}
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				SnsRoleView MsLingView = null;
				List<SnsRoleView> result = new ArrayList<SnsRoleView>();
				int size = 0;
				for (String rid : collection) {
					if (++size >= 20) {
						break;
					}
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						if (XsgSnsManager.roleOfMsLing != null
								&& XsgSnsManager.roleOfMsLing.getRoleId().equals(role.getRoleId())) {
							MsLingView = roleRt.getSnsController().asView(role, true);
						} else {
							result.add(roleRt.getSnsController().asView(role, true));
						}
					}
				}
				if (MsLingView != null) {
					result.add(0, MsLingView);
				}
				__cb.ice_response(LuaSerializer.serialize(result.toArray(new SnsRoleView[result.size()])));
			}
		});
	}

	@Override
	public void queryPlayersLike_async(final AMD_Sns_queryPlayersLike __cb, int type, String partOfNickname,
			Current __current) {
		final Collection<String> collection = new HashSet<String>();
		List<IRole> list;
		if (partOfNickname == null || partOfNickname.isEmpty()) {
			list = XsgRoleManager.getInstance().findPlayersBetweenLevel(roleRt.getLevel() - 5, roleRt.getLevel() + 5);
			if (list.contains(roleRt)) {
				list.remove(roleRt);
			}
		} else {
			list = XsgRoleManager.getInstance().findRolesLikeName(partOfNickname);
		}
		final Collection<String> snsList = new HashSet<String>();
		if (type == 1) {
			snsList.addAll(roleRt.getSnsController().getFoes());
			snsList.addAll(roleRt.getSnsController().getBlacklist());
			snsList.addAll(roleRt.getSnsController().getFriends());
		} else if (type == 2) {
			snsList.addAll(roleRt.getSnsController().getFriends());
			snsList.addAll(roleRt.getSnsController().getBlacklist());
		} else if (type == 3) {
			snsList.addAll(roleRt.getSnsController().getFriends());
			snsList.addAll(roleRt.getSnsController().getFoes());
		} else {
			snsList.addAll(roleRt.getSnsController().getFriends());
			snsList.addAll(roleRt.getSnsController().getFoes());
			snsList.addAll(roleRt.getSnsController().getBlacklist());
		}
		int size = 0;
		for (IRole role : list) {
			if (++size >= 10) {
				break;
			}
			if (partOfNickname == null || partOfNickname.isEmpty()) {
				if (snsList.contains(role.getRoleId()) || role.isRobot()) {
					continue;
				}
			}
			if (role.getRoleId() != roleRt.getRoleId() && role.getName().contains(partOfNickname)) {
				collection.add(role.getRoleId());
			}
		}
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				Collection<SnsRoleView> result = new HashSet<SnsRoleView>();
				for (String rid : collection) {
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						result.add(
								roleRt.getSnsController().asView(role,
										!roleRt.getSnsController().getFriends().contains(role.getRoleId()) && !roleRt
												.getSnsController().getFoes().contains(role.getRoleId())
								&& !roleRt.getSnsController().getBlacklist().contains(role.getRoleId())));
					}
				}
				__cb.ice_response(LuaSerializer.serialize(result.toArray(new SnsRoleView[collection.size()])));

			}
		});
	}

	int onlineCount(Collection<String> collection) {
		int online = 0;
		for (String rid : collection) {
			if (XsgRoleManager.getInstance().findRoleById(rid) != null
					&& XsgRoleManager.getInstance().findRoleById(rid).isOnline()) {
				online++;
			}
		}
		return online;
	}

	@Override
	public void queryAllFreinds_async(final AMD_Sns_queryAllFreinds __cb, Current __current) {
		final Collection<String> collection = roleRt.getSnsController().getFriends();

		roleRt.getSnsController().clearJunLingStatus();
		final SnsJunLingT snsJunLingT = XsgSnsManager.getInstance().getSnsJunLingT(roleRt.getVipLevel());
		final int sendJunLingNum = roleRt.getSnsController().getSendJunLingNum();
		final int acceptJunLingNum = roleRt.getSnsController().getAcceptJunLingNum();
		final Collection<String> fcollection = collection;
		roleRt.getSnsController().getSnsRoleViews(collection, new OnSnsRoleViewCallback() {
			@Override
			public void onSnsRoleView(Collection<SnsRoleView> snsRoleViewList) {
				__cb.ice_response(LuaSerializer.serialize(
						new OpenSnsView(onlineCount(fcollection), roleRt.getSnsController().maxSize(SNSType.FRIEND),
								roleRt.getSnsController().getFriends().size(),
								roleRt.getSnsController().unTreatedApplying().size(), roleRt.getVit(), 0,
								snsRoleViewList.toArray(new SnsRoleView[snsRoleViewList.size()]),
								snsJunLingT.sendMaxNum, snsJunLingT.acceptMaxNum, sendJunLingNum, acceptJunLingNum)));
			}
		});
	}

	@Override
	public void queryAllFoes_async(final AMD_Sns_queryAllFoes __cb, Current __current) {
		final Collection<String> collection = roleRt.getSnsController().getFoes();
		final SnsJunLingT snsJunLingT = XsgSnsManager.getInstance().getSnsJunLingT(roleRt.getVipLevel());
		final int sendJunLingNum = roleRt.getSnsController().getSendJunLingNum();
		final int acceptJunLingNum = roleRt.getSnsController().getAcceptJunLingNum();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				Collection<SnsRoleView> result = new HashSet<SnsRoleView>();
				for (String rid : collection) {
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						result.add(roleRt.getSnsController().asView(role, false));
					}
				}
				__cb.ice_response(LuaSerializer.serialize(
						new OpenSnsView(onlineCount(collection), roleRt.getSnsController().maxSize(SNSType.FRIEND),
								roleRt.getSnsController().getFriends().size(),
								roleRt.getSnsController().unTreatedApplying().size(), roleRt.getVit(), 0,
								result.toArray(new SnsRoleView[result.size()]), snsJunLingT.sendMaxNum,
								snsJunLingT.acceptMaxNum, sendJunLingNum, acceptJunLingNum)));
			}
		});
	}

	@Override
	public void queryAllBlacklist_async(final AMD_Sns_queryAllBlacklist __cb, Current __current) {
		final Collection<String> collection = roleRt.getSnsController().getBlacklist();
		final SnsJunLingT snsJunLingT = XsgSnsManager.getInstance().getSnsJunLingT(roleRt.getVipLevel());
		final int sendJunLingNum = roleRt.getSnsController().getSendJunLingNum();
		final int acceptJunLingNum = roleRt.getSnsController().getAcceptJunLingNum();
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				Collection<SnsRoleView> result = new HashSet<SnsRoleView>();
				for (String rid : collection) {
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						result.add(roleRt.getSnsController().asView(role, false));
					}
				}
				__cb.ice_response(LuaSerializer.serialize(
						new OpenSnsView(onlineCount(collection), roleRt.getSnsController().maxSize(SNSType.FRIEND),
								roleRt.getSnsController().getFriends().size(),
								roleRt.getSnsController().unTreatedApplying().size(), roleRt.getVit(), 0,
								result.toArray(new SnsRoleView[result.size()]), snsJunLingT.sendMaxNum,
								snsJunLingT.acceptMaxNum, sendJunLingNum, acceptJunLingNum)));
			}
		});
	}

	@Override
	public void sendJunLing_async(final AMD_Sns_sendJunLing __cb, String targetId, Current __current)
			throws NoteException {
		roleRt.getSnsController().sendJunLing(targetId, __cb);
	}

	@Override
	public void acceptJunLing_async(AMD_Sns_acceptJunLing __cb, String targetId, Current __current)
			throws NoteException {
		roleRt.getSnsController().acceptJunLing(targetId, __cb);
	}

	/**
	 * 搜索好友换一批
	 */
	@Override
	public void changeMorePlayers_async(final AMD_Sns_changeMorePlayers __cb,
			Current __current) {
		final Collection<String> collection = new HashSet<String>();
		List<IRole> list = XsgRoleManager.getInstance().findPlayersBetweenLevel(roleRt.getLevel() - 5, roleRt.getLevel() + 5);
		if (list.contains(roleRt)) {
			list.remove(roleRt);
		}

		final Collection<String> snsList = new HashSet<String>();
		snsList.addAll(roleRt.getSnsController().getFoes());
		snsList.addAll(roleRt.getSnsController().getBlacklist());
		snsList.addAll(roleRt.getSnsController().getFriends());
		
		int size = 0;
		for (IRole role : list) {
			if (++size >= 10) {
				break;
			}
			if (snsList.contains(role.getRoleId()) || role.isRobot()) {
				continue;
			}
			if (role.getRoleId() != roleRt.getRoleId()) {
				collection.add(role.getRoleId());
			}
		}
		XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(collection), new Runnable() {
			public void run() {
				Collection<SnsRoleView> result = new HashSet<SnsRoleView>();
				for (String rid : collection) {
					IRole role = XsgRoleManager.getInstance().findRoleById(rid);
					if (role != null) {
						result.add(
								roleRt.getSnsController().asView(role,
										!roleRt.getSnsController().getFriends().contains(role.getRoleId()) && !roleRt
												.getSnsController().getFoes().contains(role.getRoleId())
								&& !roleRt.getSnsController().getBlacklist().contains(role.getRoleId())));
					}
				}
				if(result.size() > 0) {
					__cb.ice_response(LuaSerializer.serialize(result.toArray(new SnsRoleView[collection.size()])));
				} else {
					__cb.ice_exception(new NoteException(Messages.getString("SnsI.noplayer")));
					return;
				}

			}
		});
	}

	/**
	 * 战报记录界面
	 */
	@Override
	public void queryBattleRecordView_async(final AMD_Sns_queryBattleRecordView __cb,
			Current __current) throws NoteException {
		this.roleRt.getSnsController().queryBattleRecordView(__cb);
	}

}
