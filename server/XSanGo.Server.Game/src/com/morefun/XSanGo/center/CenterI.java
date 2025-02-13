/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.center;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Center_charge;
import com.XSanGo.Protocol.AMD_Center_deleteItem;
import com.XSanGo.Protocol.AMD_Center_findRoleViewList;
import com.XSanGo.Protocol.AMD_Center_findRoleViewListById;
import com.XSanGo.Protocol.AMD_Center_findRoleViewListByRole;
import com.XSanGo.Protocol.AMD_Center_findRoleViewListBySimpleAccount;
import com.XSanGo.Protocol.AMD_Center_findRoleViewListBySimpleRole;
import com.XSanGo.Protocol.AMD_Center_getChargeItem;
import com.XSanGo.Protocol.AMD_Center_getFactionMemberList;
import com.XSanGo.Protocol.AMD_Center_getPayLog;
import com.XSanGo.Protocol.AMD_Center_getRankList;
import com.XSanGo.Protocol.AMD_Center_getRecentMessages;
import com.XSanGo.Protocol.AMD_Center_getRoleDB;
import com.XSanGo.Protocol.AMD_Center_queryItemNum;
import com.XSanGo.Protocol.AMD_Center_queryRoleByCDK;
import com.XSanGo.Protocol.AMD_Center_saveRoleData;
import com.XSanGo.Protocol.AMD_Center_sendMail;
import com.XSanGo.Protocol.AMD_Center_sendMailByRoleId;
import com.XSanGo.Protocol.AMD_Center_silence;
import com.XSanGo.Protocol.CenterCallbackPrx;
import com.XSanGo.Protocol.CenterCallbackPrxHelper;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.GmFactionMemberView;
import com.XSanGo.Protocol.GmFactionView;
import com.XSanGo.Protocol.GmRankView;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QueryItemResponse;
import com.XSanGo.Protocol.RankListSub;
import com.XSanGo.Protocol.RoleView;
import com.XSanGo.Protocol.RoleViewForGM;
import com.XSanGo.Protocol.ServerDetail;
import com.XSanGo.Protocol._CenterDisp;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerCombiner;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.GroovyManager;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCDKRecord;
import com.morefun.XSanGo.db.game.RoleCDKRecordDao;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.rankList.XsgRankListManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.RoleI;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.ChargeItemT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * 中心服务器通讯接口类，单例，1个游戏服务器只需要1个连接
 * 
 * @author Su LingYun
 * 
 */
public class CenterI extends _CenterDisp {
	/** 序列化版本号 */
	private static final long serialVersionUID = 1L;

	private static Log logger = LogFactory.getLog(CenterI.class);

	private static final CenterI instance = new CenterI();

	public static CenterI instance() {
		return instance;
	}

	private CenterI() {
	}

	@Override
	public void setCallback(CenterCallbackPrx cb, Current __current) {
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "t"); //$NON-NLS-1$ //$NON-NLS-2$
		XsgCenterManager.instance().setCb(CenterCallbackPrxHelper.checkedCast(cb.ice_context(ctx)));

	}

	@Override
	public void sendTocken(int id, String account, String tocken, DeviceInfo device, Current __current)
			throws NoteException {
		if (LogicThread.isOverload()) {
			throw new NoteException(Messages.getString("CenterI.2")); //$NON-NLS-1$
		}
		account = account.toLowerCase();
		XsgCenterManager.instance().addVerify(account, tocken, device);
	}

	@Override
	public void findRoleViewList_async(final AMD_Center_findRoleViewList __cb, final String accountName,
			Current __current) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				final List<Role> roles = RoleI.getRoleDAO().findByAccount(accountName);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						List<RoleViewForGM> list = new ArrayList<RoleViewForGM>();
						for (Role role : roles) {
							IRole roleRt = XsgRoleManager.getInstance().findRoleById(role.getId());
							if (roleRt == null) {
								roleRt = XsgRoleManager.getInstance().loadRole(role);
							}
							list.add(roleRt.getRoleViewForGM());
						}

						__cb.ice_response(TextUtil.GSON.toJson(list.toArray()));
					}
				});
			}
		});
	}

	@Override
	public void findRoleViewListByRole_async(final AMD_Center_findRoleViewListByRole __cb, final String roleName,
			Current __current) {
		XsgRoleManager.getInstance().loadRoleByNameAsync(roleName, new Runnable() {

			@Override
			public void run() {
				IRole roleRt = XsgRoleManager.getInstance().findRoleByName(roleName);
				__cb.ice_response(TextUtil.GSON.toJson(new RoleViewForGM[] { roleRt.getRoleViewForGM() }));
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.3"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public void getRecentMessages_async(final AMD_Center_getRecentMessages __cb, Current __current) {
		// 直接从数据库获取最近100条消息
		DBThreads.execute(new Runnable() {
			public void run() {
				try {
				} catch (Exception e) {
					__cb.ice_exception(e);
				}
			}
		});
	}

	@Override
	public void systemAnnounce(String announce, Current __current) {
		XsgChatManager.getInstance().sendAnnouncementForGm(announce);
	}

	@Override
	public void findRoleViewListById_async(final AMD_Center_findRoleViewListById __cb, final String roleId, Current arg2) {

		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {

			@Override
			public void run() {
				IRole roleRt = XsgRoleManager.getInstance().findRoleById(roleId);
				__cb.ice_response(TextUtil.GSON.toJson(new RoleViewForGM[] { roleRt.getRoleViewForGM() }));
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.4"))); //$NON-NLS-1$
			}
		});

	}

	@Override
	public ServerDetail ping(Current __current) {
		ServerDetail state = new ServerDetail(XsgCenterManager.instance().getCb() != null, LogicThread.isOverload(),
				GameSessionManagerI.getInstance().getOnlineCount());
		return state;
	}

	@Override
	public void charge_async(final AMD_Center_charge __cb, final String roleId, final int yuan,
			final CustomChargeParams params, final String orderId, final String currency, Current __current) {
		String json = TextUtil.GSON.toJson(params);
		logger.info(TextUtil.format("Accept a charge notify,[{0},{1}],{2}", //$NON-NLS-1$
				roleId, yuan, json));
		List<String> list = new ArrayList<String>();
		list.add(roleId);
		if (params.roles != null) {
			for (String id : params.roles) {
				list.add(id);
			}
		}

		final ChargeItemT template = XsgVipManager.getInstance().getChargeItemT(params.item);
		if (template == null) {
			logger.error("Error charge params," + json); //$NON-NLS-1$
			__cb.ice_response();
			return;
		}
		XsgRoleManager.getInstance().loadRoleAsync(list, new Runnable() {

			@Override
			public void run() {
				try {
					IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
					if (params.roles == null || params.roles.length == 0) {// 自己充
						if (yuan >= template.rmb) {
							role.getVipController().acceptCharge(params, yuan, orderId, currency);

							rewardAuctionMoneyAfterCharge(yuan, role);
						} else {
							LogManager.warn(TextUtil.format(Messages.getString("CenterI.7"), roleId, //$NON-NLS-1$
									template.id, yuan));
						}
					} else {
						if (yuan >= template.rmb * params.roles.length) {
							for (String id : params.roles) {
								// BI数据还是要算在花钱人的身上
								role.getMFBIControler().onCharge(params, 0, orderId, currency);

								IRole friend = XsgRoleManager.getInstance().findRoleById(id);
								if (friend == null) {
									logger.error(TextUtil.format(Messages.getString("CenterI.8"), //$NON-NLS-1$
											id, roleId, template.id));
									continue;
								}
								friend.getVipController().acceptChargeFromFriend(params.item, yuan, role.getAccount());
							}
						} else {
							LogManager.warn(TextUtil.format(Messages.getString("CenterI.9"), //$NON-NLS-1$
									roleId, params.roles.length, template.id, yuan));
						}
					}

					__cb.ice_response();
				} catch (Exception e) {
					__cb.ice_exception(e);
				}
			}
		});
	}

	@Override
	public String[] getItemConfig(Current __current) throws NoteException {
		return XsgItemManager.getInstance().getItemConfig();
	}

	@Override
	public String[] getPropertyConfig(Current __current) throws NoteException {
		return XsgHeroManager.getInstance().getPropertyConfig();
	}

	@Override
	public IntString[] getPlayerSkillConfig(Current __current) throws NoteException {
		return XsgRoleManager.getInstance().getPlayerSkillConfig();
	}

	@Override
	public IntString[] getHeroSkillConfig(Current __current) throws NoteException {
		return XsgHeroManager.getInstance().getHeroSkillConfig();
	}

	@Override
	public IntString[] getRelationConfig(Current __current) throws NoteException {
		return XsgHeroManager.getInstance().getRelationConfig();
	}

	@Override
	public void executeGroovyScript(String script, Current __current) throws NoteException {
		try {
			GroovyManager.getInstance().executeScript(script);
		} catch (ScriptException e) {
			LogManager.error(e);
			throw new NoteException(e.getMessage());
		}
	}

	@Override
	public void silence_async(final AMD_Center_silence __cb, final String roleId, final String releaseTime,
			Current __current) {
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {

			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
				role.getChatControler().setForbiddenExpireTime(DateUtil.parseDate(releaseTime));
				__cb.ice_response();
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.10"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public void kick(String account, String roleId, Current __current) {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(account, roleId);
		if (session != null) {
			session.destroyWarp(false);
		}
	}

	@Override
	public void sendMail_async(final AMD_Center_sendMail __cb, final String targetName, final String title,
			final String body, final Property[] attach,final String senderName, Current __current) throws NoteException {
		if (TextUtil.isBlank(targetName)) {
			__cb.ice_exception(new NoteException(Messages.getString("CenterI.11"))); //$NON-NLS-1$
			return;
		}
		XsgRoleManager.getInstance().loadRoleByNameAsync(targetName, new Runnable() {
			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleByName(targetName);

				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", //$NON-NLS-1$
								senderName!=null?senderName:"", role.getRoleId(), title, //$NON-NLS-1$
								body, XsgMailManager.getInstance().serializeMailAttach(attach), Calendar.getInstance()
										.getTime()));
				__cb.ice_response();
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.14"))); //$NON-NLS-1$
			}
		});

	}

	@Override
	public void sendMailByRoleId_async(final AMD_Center_sendMailByRoleId __cb, final String roleId, final String title,
			final String body, final Property[] attach, final String senderName, Current __current) throws NoteException {
		if (TextUtil.isBlank(roleId)) {
			__cb.ice_exception(new NoteException(Messages.getString("CenterI.15"))); //$NON-NLS-1$
			return;
		}

		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleById(roleId);

				XsgMailManager.getInstance().sendMail(
						new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", //$NON-NLS-1$
								senderName!=null?senderName:"", role.getRoleId(), title, //$NON-NLS-1$
								body, XsgMailManager.getInstance().serializeMailAttach(attach), Calendar.getInstance()
										.getTime()));
				__cb.ice_response();
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.18"))); //$NON-NLS-1$
			}
		});

	}

	@Override
	public void sendServerMail(String title, String body, Property[] attach, String conditionParams,String senderName, Current __current)
			throws NoteException {
		// 全服邮件
		Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0,
				"", StringUtils.isNotEmpty(senderName)?senderName:"", Const.Mail.CACHE_All_MAIL, title, //$NON-NLS-1$ //$NON-NLS-2$
				body, XsgMailManager.getInstance().serializeMailAttach(attach), Calendar.getInstance().getTime());
		mail.setParams(conditionParams);
		XsgMailManager.getInstance().sendMail(mail);

		// 通知邮件红点
		List<IRole> onlineList = XsgRoleManager.getInstance().findOnlineList();
		for (IRole role : onlineList) {
			role.getMailControler().notifyRedPoint();
		}
	}

	/**
	 * 充值赠送拍卖币
	 * 
	 * @param yuan
	 * @param role
	 */
	private void rewardAuctionMoneyAfterCharge(final int yuan, IRole role) {
		int auctionMoney = yuan * XsgGameParamManager.getInstance().getAuctionExchageRate();
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("$e", String.valueOf(yuan)); //$NON-NLS-1$
		replaceMap.put("$f", String.valueOf(auctionMoney)); //$NON-NLS-1$

		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		rewardMap.put(Const.PropertyName.AUCTION_COIN, auctionMoney);
		role.getMailControler().receiveRoleMail(MailTemplate.ReceiveAuctionMoneyAfterCharge, rewardMap, replaceMap);
	}

	@Override
	public GmFactionView[] getFactionList(String factionName, Current __current) throws NoteException {
		List<GmFactionView> views = new ArrayList<GmFactionView>();
		List<IFaction> factions = XsgFactionManager.getInstance().likeFactionByName(factionName);
		for (IFaction f : factions) {
			views.add(new GmFactionView(f.getSubId(), f.getName(), f.getLevel(), f.getAllMember().size(), DateUtil
					.toString(f.getCreateTime().getTime())));
		}
		return views.toArray(new GmFactionView[0]);
	}

	@Override
	public void getFactionMemberList_async(final AMD_Center_getFactionMemberList __cb, String factionName,
			Current __current) throws NoteException {
		IFaction faction = XsgFactionManager.getInstance().findFactionByName(factionName);
		if (faction == null) {
			__cb.ice_response(new GmFactionMemberView[0]);
			return;
		}
		final Set<FactionMember> members = faction.getAllMember();
		final List<String> roleIds = new ArrayList<String>();
		for (FactionMember member : members) {
			roleIds.add(member.getRoleId());
		}
		if (roleIds.isEmpty()) {
			__cb.ice_response(new GmFactionMemberView[0]);
			return;
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIds, new Runnable() {

			@Override
			public void run() {
				List<GmFactionMemberView> views = new ArrayList<GmFactionMemberView>();
				for (FactionMember fm : members) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(fm.getRoleId());
					if (iRole == null) {
						continue;
					}
					fm.setLevel(iRole.getLevel());
					String duty = null;
					if (fm.getDutyId() == Const.Faction.DUTY_BOSS) {
						duty = Messages.getString("CenterI.23"); //$NON-NLS-1$
					} else if (fm.getDutyId() == Const.Faction.DUTY_ELDER) {
						duty = Messages.getString("CenterI.24"); //$NON-NLS-1$
					} else {
						duty = Messages.getString("CenterI.25"); //$NON-NLS-1$
					}
					views.add(new GmFactionMemberView(iRole.getName(), duty, fm.getContribution(), DateUtil.toString(fm
							.getParticipateTime().getTime())));
				}
				__cb.ice_response(views.toArray(new GmFactionMemberView[0]));
			}
		});
	}

	@Override
	public void getRankList_async(AMD_Center_getRankList __cb, int type, Current __current) throws NoteException {
		List<RankListSub> listSubs = XsgRankListManager.getInstance().getRankList(type);
		List<GmRankView> rankViews = new ArrayList<GmRankView>();
		for (RankListSub ls : listSubs) {
			rankViews.add(new GmRankView(ls.rank, ls.roleName, ls.level, ls.count));
		}
		__cb.ice_response(rankViews.toArray(new GmRankView[0]));
	}

	@Override
	public void getPayLog_async(final AMD_Center_getPayLog __cb, final String roleName, Current __current)
			throws NoteException {
		XsgRoleManager.getInstance().loadRoleByNameAsync(roleName, new Runnable() {
			@Override
			public void run() {
				IRole role = XsgRoleManager.getInstance().findRoleByName(roleName);
				__cb.ice_response(role.getVipController().getChargeHistory());
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.26"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public void getChargeItem_async(AMD_Center_getChargeItem __cb, int yuan, Current __current) {

		final ChargeItemT template = XsgVipManager.getInstance().getChargeItemByMoney(yuan);
		__cb.ice_response(template.id + "");

	}

	@Override
	public void queryItemNum_async(final AMD_Center_queryItemNum __cb, final String roleName, final String itemId,
			Current __current) {

		XsgRoleManager.getInstance().loadRoleByNameAsync(roleName, new Runnable() {

			@Override
			public void run() {
				IRole roleRt = XsgRoleManager.getInstance().findRoleByName(roleName);
				int num = roleRt.getItemControler().getItemCountInPackage(itemId);
				QueryItemResponse res = new QueryItemResponse(itemId, num, "SUCC");
				__cb.ice_response(TextUtil.GSON.toJson(res));
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.3"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public void deleteItem_async(final AMD_Center_deleteItem __cb, final String roleId, final String itemId, final int num,
			Current __current) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				XsgRoleManager.getInstance().loadRoleById(roleId);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
						if (role == null) {
							__cb.ice_exception(null);
							return;
						}
						role.getItemControler().changeItemById(itemId, num);
						__cb.ice_response("ok");
					}
				});
			}
		});
	}

	@Override
	public void getRoleDB_async(final AMD_Center_getRoleDB __cb, final String roleId, Current __current) throws NoteException {

		if (TextUtil.isBlank(roleId)) {
			__cb.ice_exception(new NoteException(Messages.getString("CenterI.15"))); //$NON-NLS-1$
			return;
		}

		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				Role db = XsgRoleManager.getInstance().getRoleDbFromMemory(roleId);
				
				//role数据
				byte[] data = TextUtil.objectToBytes(db);
				
				__cb.ice_response(data);
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CenterI.18"))); //$NON-NLS-1$
			}
		});

	
	}

	@Override
	public void saveRoleData_async(final AMD_Center_saveRoleData __cb, final int serverId, final byte[] data, String importType,
			Current __current) throws NoteException {

		// 正常返回
		final Role role = (Role) TextUtil.bytesToObject(data);
		try {
			XsgCacheManager.getInstance().persistentDataCompatible(role);
		} catch (Exception e) {
			__cb.ice_exception(e);
			return;
		}
		final int oriServerId = role.getServerId();
		
		role.setServerId(serverId);
		
		String oriAccount = role.getAccount();
		if("1".equals(importType) && !oriAccount.endsWith(".mf")){
			String tem = oriAccount.substring(0,oriAccount.indexOf("."));
			role.setAccount(tem+".mf");
		}
//		kick(role.getAccount(),role.getId());
		//清空Role相关数据
		resetRoleInfo(role);
		final String name = role.getName();
		final IRole result = XsgRoleManager.getInstance().findRoleByAccount(role.getAccount(), serverId);
		//重置账号
		if(result == null){
			// 数据库执行逻辑
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					resetRoleAccount(role.getId(), role.getAccount(), serverId, result);
					//查询是否有重复角色名称
					resetRoleNameAsync(__cb, role, oriServerId, name);
				}
			});
		}else{
			if(result.getRoleId().equals(role.getId())){
				//缓存存在则先移除缓存数据
				XsgRoleManager.getInstance().removeRole(role.getId());
			}

			// 查询DB的账号数据
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					resetRoleAccount(role.getId(), role.getAccount(), serverId, result);
					resetRoleNameAsync(__cb, role, oriServerId, name);
				}
			});
		}
	}

	/**
	 * 当导入角色的账号在DB中存在时候，若roleId也重复则删除DB中重复id的记录，否则将重复的账号改为系统账号
	 * @param roleId 导入角色ID
	 * @param account 导入角色账号
	 * @param serverId 当前serverId
	 * @param subRole 账号重复的角色
	 */
	private void resetRoleAccount(String roleId, String account, int serverId,IRole subRole){
		List<Role> roleList = RoleDAO.getFromApplicationContext(ServerLancher.getAc())
				.findByAccountAndServer(account, serverId);
		//DB有重复账号且ID重复时删除DB数据
		if(!CollectionUtils.isEmpty(roleList)){
			for (Role oldRole : roleList) {
				if(oldRole.getId().equals(roleId)){
					kick(account, roleId,false);
					RoleDAO.getFromApplicationContext(ServerLancher.getAc()).deleteRole(oldRole);
				}else{
					subRole.setAccount2System();
					subRole.saveAsyn();
				}
			}
		}
	}
	
	/**
	 * T下线
	 * @param account
	 * @param roleId
	 * @param flag 是否需要保存角色数据
	 */
	private void kick(String account, String roleId, boolean flag) {

		if(flag){
			kick(account, roleId);
		}else{
			GameSessionI session = GameSessionManagerI.getInstance().findSession(account, roleId);
			if (session != null) {
				session.destroyOnly(false);
			}
		}
	}

	/**
	 * 导入角色时重置roleName
	 * @param __cb
	 * @param role
	 * @param oriServerId
	 * @param name
	 */
	private void resetRoleNameAsync(final AMD_Center_saveRoleData __cb, final Role role,
			final int oriServerId, final String name) {
		XsgRoleManager.getInstance().loadRoleByNameAsync(name, new Runnable() {
			
			@Override
			public void run() {
				IRole roleRt = XsgRoleManager.getInstance().findRoleByName(name);
				if(roleRt != null){
					//重置玩家名称
					role.setName(ServerCombiner.generateNameForDuplicate(oriServerId, role.getName()));
					//重置名称后如果还有重复，则抛出异常，不再处理
					XsgRoleManager.getInstance().loadRoleByNameAsync(role.getName(), new Runnable() {
						
						@Override
						public void run() {
							IRole roleRt = XsgRoleManager.getInstance().findRoleByName(name);
							if(roleRt != null){
								__cb.ice_exception(new NoteException(TextUtil.format("主公，[{0}]这个角色name已经存在", name))); //$NON-NLS-1$
								return;
							}
						}
					}, new Runnable() {
						
						@Override
						public void run() {
							importRoleIntoGame(__cb, role);
						}
					});
				}
			}
		}, new Runnable() {
			
			@Override
			public void run() {
				importRoleIntoGame(__cb, role);
			}
		});
	}
	
	/**
	 * 重置role相关数据
	 * @param role
	 */
	private void resetRoleInfo(final Role role) {
		if(!CollectionUtils.isEmpty(role.getFriendApplyingHistory())){
			role.getFriendApplyingHistory().clear();
		}
		//清空好友数据
		if(!CollectionUtils.isEmpty(role.getSns())){
			role.getSns().clear();
		}
		//清空竞技场战报
		if(!CollectionUtils.isEmpty(role.getLadderReportList())){
			role.getLadderReportList().clear();
		}
		//清空好友邀请数据
		if(null != role.getRoleFriendsInvitation()){
			role.setRoleFriendsInvitation(null);
		}
		//清空好友回归数据
		if(null != role.getRoleFriendsRecalled()){
			role.setRoleFriendsRecalled(null);
		}
		//清空好友邀请任务数据
		if(!CollectionUtils.isEmpty(role.getRoleRecallTask())){
			role.getRoleRecallTask().clear();
		}
							
	}
	
	/**
	 * 加载角色到内存并异步保存到DB
	 * @param __cb
	 * @param role
	 */
	private void importRoleIntoGame(final AMD_Center_saveRoleData __cb, final Role role) {
		IRole loadRole = XsgRoleManager.getInstance().loadRole(role);
		loadRole.saveAsyn();
		__cb.ice_response();
	}

	@Override
	public void findRoleViewListBySimpleRole_async(final AMD_Center_findRoleViewListBySimpleRole __cb, final String roleName,
			Current __current) {

		try{
			List<IRole> results = XsgRoleManager.getInstance().findRolesLikeName(roleName);
			if(!CollectionUtils.isEmpty(results)){
				List<RoleViewForGM> list = new ArrayList<RoleViewForGM>();
				for (IRole roleRt : results) {
					RoleViewForGM roleViewForGM = roleRt.getRoleViewForGM();
					RoleView roleView = new RoleView();
					roleView.id = roleViewForGM.baseView.id;
					roleView.vip = roleViewForGM.baseView.vip;
					roleView.level = roleViewForGM.baseView.level;
					roleView.name = roleViewForGM.baseView.name;
					roleViewForGM.baseView = roleView;
					list.add(roleViewForGM);
				}
				__cb.ice_response(TextUtil.GSON.toJson(list.toArray()));
				return;
			}
		
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					final List<Role> roles = RoleI.getRoleDAO().findBySimpleRoleName(roleName);
					LogicThread.execute(new Runnable() {
						@Override
						public void run() {
							List<RoleViewForGM> list = new ArrayList<RoleViewForGM>();
							for (Role role : roles) {
								IRole roleRt = XsgRoleManager.getInstance().findRoleById(role.getId());
								if (roleRt == null) {
									roleRt = XsgRoleManager.getInstance().loadRole(role);
								}
								RoleViewForGM roleViewForGM = roleRt.getRoleViewForGM();
								RoleView roleView = new RoleView();
								roleView.id = roleViewForGM.baseView.id;
								roleView.vip = roleViewForGM.baseView.vip;
								roleView.level = roleViewForGM.baseView.level;
								roleView.name = roleViewForGM.baseView.name;
								roleViewForGM.baseView = roleView;
								list.add(roleViewForGM);
							}
	
							__cb.ice_response(TextUtil.GSON.toJson(list.toArray()));
						}
					});
				}
			});
		}catch(Exception e){
			__cb.ice_exception(e);
		}
	
	}

	@Override
	public void findRoleViewListBySimpleAccount_async(final AMD_Center_findRoleViewListBySimpleAccount __cb, final String account,
			Current __current) {
		try{
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					final List<Role> roles = RoleI.getRoleDAO().findBySimpleAccount(account);
					LogicThread.execute(new Runnable() {
						@Override
						public void run() {
							List<RoleViewForGM> list = new ArrayList<RoleViewForGM>();
							for (Role role : roles) {
								IRole roleRt = XsgRoleManager.getInstance().findRoleById(role.getId());
								if (roleRt == null) {
									roleRt = XsgRoleManager.getInstance().loadRole(role);
								}
								RoleViewForGM roleViewForGM = roleRt.getRoleViewForGM();
								RoleView roleView = new RoleView();
								roleView.id = roleViewForGM.baseView.id;
								roleView.vip = roleViewForGM.baseView.vip;
								roleView.level = roleViewForGM.baseView.level;
								roleView.name = roleViewForGM.baseView.name;
								roleViewForGM.baseView = roleView;
								list.add(roleViewForGM);
							}
	
							__cb.ice_response(TextUtil.GSON.toJson(list.toArray()));
						}
					});
				}
			});
		}catch(Exception e){
			__cb.ice_exception(e);
		}
	}

	@Override
	public void queryRoleByCDK_async(final AMD_Center_queryRoleByCDK __cb, final String cdk, Current __current)
			throws NoteException {


		try{
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					RoleCDKRecordDao dao = RoleCDKRecordDao.getFromApplicationContext(ServerLancher.getAc());
					List<RoleCDKRecord> list = dao.findRoleCDKRecordByCdk(cdk);
					
					if(CollectionUtils.isEmpty(list)){
						__cb.ice_response(null);
						return;
					}else{
						RoleCDKRecord record = list.get(0);
						String time = DateUtil.format(record.getCreateTime());
						__cb.ice_response(record.getRole().getId()+"#"+time);
					}
				}
			});
		}catch(Exception e){
			logger.error(e);
			__cb.ice_exception(e);
		}
	
	}

}