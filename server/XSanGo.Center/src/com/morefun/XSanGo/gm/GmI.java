/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.gm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;
import Ice.LocalException;
import Ice.UserException;

import com.XSanGo.Protocol.AMD_Gm_addSycee;
import com.XSanGo.Protocol.AMD_Gm_addVipExp;
import com.XSanGo.Protocol.AMD_Gm_addWheelCount;
import com.XSanGo.Protocol.AMD_Gm_deleteCDK;
import com.XSanGo.Protocol.AMD_Gm_deleteItem;
import com.XSanGo.Protocol.AMD_Gm_download;
import com.XSanGo.Protocol.AMD_Gm_editChannel;
import com.XSanGo.Protocol.AMD_Gm_editGameServer;
import com.XSanGo.Protocol.AMD_Gm_executeGroovyCombineScript;
import com.XSanGo.Protocol.AMD_Gm_executeGroovyScript;
import com.XSanGo.Protocol.AMD_Gm_findFuzzyRoleViewList;
import com.XSanGo.Protocol.AMD_Gm_findRoleViewList;
import com.XSanGo.Protocol.AMD_Gm_fobidenChat;
import com.XSanGo.Protocol.AMD_Gm_forzenAccount;
import com.XSanGo.Protocol.AMD_Gm_generateCDK;
import com.XSanGo.Protocol.AMD_Gm_getFactionList;
import com.XSanGo.Protocol.AMD_Gm_getFactionMemberList;
import com.XSanGo.Protocol.AMD_Gm_getHeroSkillConfig;
import com.XSanGo.Protocol.AMD_Gm_getItemConfig;
import com.XSanGo.Protocol.AMD_Gm_getPayLog;
import com.XSanGo.Protocol.AMD_Gm_getPlayerSkillConfig;
import com.XSanGo.Protocol.AMD_Gm_getPropertyConfig;
import com.XSanGo.Protocol.AMD_Gm_getRankList;
import com.XSanGo.Protocol.AMD_Gm_getRelationConfig;
import com.XSanGo.Protocol.AMD_Gm_getServerItemConfig;
import com.XSanGo.Protocol.AMD_Gm_importRole;
import com.XSanGo.Protocol.AMD_Gm_kickRole;
import com.XSanGo.Protocol.AMD_Gm_loadCDK;
import com.XSanGo.Protocol.AMD_Gm_mockCharge;
import com.XSanGo.Protocol.AMD_Gm_queryCdkByCDK;
import com.XSanGo.Protocol.AMD_Gm_reloadScript;
import com.XSanGo.Protocol.AMD_Gm_sendAnnounce;
import com.XSanGo.Protocol.AMD_Gm_sendSystemMail;
import com.XSanGo.Protocol.AMD_Gm_sendSystemServerMail;
import com.XSanGo.Protocol.AMD_Gm_setRecommendServer;
import com.XSanGo.Protocol.AMD_Gm_setVipLevel;
import com.XSanGo.Protocol.AMD_Gm_setWhiteList;
import com.XSanGo.Protocol.AMD_Gm_skipCopy;
import com.XSanGo.Protocol.AMD_Gm_unforzenAccount;
import com.XSanGo.Protocol.AMD_Gm_updateCDK;
import com.XSanGo.Protocol.AMD_Gm_uploadLoginAnnounce;
import com.XSanGo.Protocol.Callback_Center_deleteItem;
import com.XSanGo.Protocol.Callback_Center_executeGroovyScript;
import com.XSanGo.Protocol.Callback_Center_findRoleViewList;
import com.XSanGo.Protocol.Callback_Center_findRoleViewListById;
import com.XSanGo.Protocol.Callback_Center_findRoleViewListByRole;
import com.XSanGo.Protocol.Callback_Center_findRoleViewListBySimpleAccount;
import com.XSanGo.Protocol.Callback_Center_findRoleViewListBySimpleRole;
import com.XSanGo.Protocol.Callback_Center_getFactionList;
import com.XSanGo.Protocol.Callback_Center_getFactionMemberList;
import com.XSanGo.Protocol.Callback_Center_getHeroSkillConfig;
import com.XSanGo.Protocol.Callback_Center_getItemConfig;
import com.XSanGo.Protocol.Callback_Center_getPayLog;
import com.XSanGo.Protocol.Callback_Center_getPlayerSkillConfig;
import com.XSanGo.Protocol.Callback_Center_getPropertyConfig;
import com.XSanGo.Protocol.Callback_Center_getRankList;
import com.XSanGo.Protocol.Callback_Center_getRelationConfig;
import com.XSanGo.Protocol.Callback_Center_getRoleDB;
import com.XSanGo.Protocol.Callback_Center_kick;
import com.XSanGo.Protocol.Callback_Center_queryRoleByCDK;
import com.XSanGo.Protocol.Callback_Center_saveRoleData;
import com.XSanGo.Protocol.Callback_Center_sendMail;
import com.XSanGo.Protocol.Callback_Center_sendMailByRoleId;
import com.XSanGo.Protocol.Callback_Center_sendServerMail;
import com.XSanGo.Protocol.Callback_Center_silence;
import com.XSanGo.Protocol.Callback_Center_systemAnnounce;
import com.XSanGo.Protocol.CdkDetailView;
import com.XSanGo.Protocol.CdkView;
import com.XSanGo.Protocol.CenterPrx;
import com.XSanGo.Protocol.CustomChargeParams;
import com.XSanGo.Protocol.GameServerView;
import com.XSanGo.Protocol.GmFactionMemberView;
import com.XSanGo.Protocol.GmFactionView;
import com.XSanGo.Protocol.GmPayLogView;
import com.XSanGo.Protocol.GmPayView;
import com.XSanGo.Protocol.GmRankView;
import com.XSanGo.Protocol.GuideConfig;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RoleViewForGM;
import com.XSanGo.Protocol.ScriptReloadConfig;
import com.XSanGo.Protocol.WhiteList;
import com.XSanGo.Protocol._GmDisp;
import com.morefun.XSanGo.CdkGenerator;
import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.GameServerInfo;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.announce.XsgAnnounceManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.CDKDao;
import com.morefun.XSanGo.db.CDKDetail;
import com.morefun.XSanGo.db.CDKGroup;
import com.morefun.XSanGo.db.Channel;
import com.morefun.XSanGo.db.Charge;
import com.morefun.XSanGo.db.ChargeDAO;
import com.morefun.XSanGo.db.GameServer;
import com.morefun.XSanGo.db.stat.AccountOperateLog;
import com.morefun.XSanGo.db.stat.StatSimpleDAO;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * GM服务接口
 * 
 * @author BruceSu
 * 
 */
public class GmI extends _GmDisp {
	private static final long serialVersionUID = 8476028064863057935L;

	protected final static Log logger = LogFactory.getLog(GmI.class);

	private static final GmI instance = new GmI();

	private static final String ERROR_GAMESERVER_DISCONNECTED = "无法连接游戏服务器。";

	public static GmI instance() {
		return instance;
	}

	// private AccountDAO accountDAO = AccountDAO
	// .getFromApplicationContext(LoginDatabase.instance().getAc());
	private boolean activeCodeReuse;

	private ChargeDAO dao = ChargeDAO.getFromApplicationContext(LoginDatabase.instance().getAc());

	/** 充值配置 */
	private Map<Integer, Integer> chargeConfigMap;

	private GmI() {
		/*
		 * 1 月卡 2 6480元宝 3 3280元宝 4 1980元宝 5 980元宝 6 300元宝 7 60元宝
		 */
		this.chargeConfigMap = new HashMap<Integer, Integer>();
		chargeConfigMap.put(600, 7);
		chargeConfigMap.put(2500, 1);
		chargeConfigMap.put(3000, 6);
		chargeConfigMap.put(9800, 5);
		chargeConfigMap.put(19800, 4);
		chargeConfigMap.put(32800, 3);
		chargeConfigMap.put(64800, 2);
	}

	private GameServerView[] getGameServerState() {
		List<GameServerView> result = new ArrayList<GameServerView>();
		for (GameServerInfo gs : CenterServer.instance().getAllServers().values()) {
			result.add(new GameServerView(gs.getId(), gs.getName(), gs.getState().ordinal(), gs.getOnlineCount(), gs
					.getDB().getShowId(), gs.getHost(), gs.isNew(), gs.getDB().isCpShowOnly(), gs.getDB()
					.isCpEnterOnly(), gs.getDB().getOnlineLimit(), gs.getDB().getTargetId()));
		}

		return result.toArray(new GameServerView[] {});
	}

	@Override
	public GameServerView[] ping(Current __current) {
		return this.getGameServerState();
	}

	 
	   @Override
	   public void findRoleViewList_async(final AMD_Gm_findRoleViewList __cb, int serverId, String accountName,
	           String roleName,String roleId, Current __current) throws NoteException {
	       CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
	 
	       if (!TextUtil.isNullOrEmpty(accountName)) {
	           prx.begin_findRoleViewList(accountName.trim(), new Callback_Center_findRoleViewList() {
	 
	               @Override
	               public void exception(LocalException __ex) {
	                   __cb.ice_exception(__ex);
	               }
	 
	               @Override
	               public void response(String __ret) {
	                   __ret = attachAccountDate(__ret);
	                   __cb.ice_response(__ret);
	               }
	 
	           });
	       } else if (!TextUtil.isNullOrEmpty(roleName)) {
	           prx.begin_findRoleViewListByRole(roleName.trim(), new Callback_Center_findRoleViewListByRole() {
	 
	               @Override
	               public void exception(LocalException __ex) {
	                   __cb.ice_exception(__ex);
	               }
	 
	               @Override
	               public void response(String __ret) {
	                   __ret = attachAccountDate(__ret);
	                   __cb.ice_response(__ret);
	               }
	           });
	       }else if (!TextUtil.isNullOrEmpty(roleId)) {
	           prx.begin_findRoleViewListById(roleId, new Callback_Center_findRoleViewListById(){
	 
	               @Override
	               public void response(String __ret) {
	                   __ret = attachAccountDate(__ret);
	                   __cb.ice_response(__ret);
	               }
	 
	               @Override
	               public void exception(LocalException __ex) {
	                   __cb.ice_exception(__ex);
	               }
	               
	           });
	       }
	   }

	/**
	 * 附加帐号数据到查询结果上 ，这里可能有IO操作，可能会影响性能
	 * 
	 * @param __ret
	 * @return
	 */
	private String attachAccountDate(String __ret) {
		RoleViewForGM[] array = TextUtil.GSON.fromJson(__ret, RoleViewForGM[].class);
		for (RoleViewForGM view : array) {
			Account account = XsgAccountManager.getInstance().findAccount(view.account.username);
			if (account != null) {
				view.account.createTime = DateUtil.toString(account.getCreateTime().getTime());
				view.account.frozen = account.isFrozen();
				view.account.registerChannel = account.getChannelId();
			}
		}
		return TextUtil.GSON.toJson(array);
	}

	@Override
	public void getItemConfig_async(final AMD_Gm_getItemConfig __cb, Current __current) throws NoteException {
		CenterPrx prx = this.findAnyGameServerPrx();
		prx.begin_getItemConfig(new Callback_Center_getItemConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(String[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});

	}

	/**
	 * 获取任意一个游戏服代理，一个都没有则抛出异常
	 * 
	 * @return
	 * @throws NoteException
	 */
	private CenterPrx findAnyGameServerPrx() throws NoteException {
		CenterPrx prx = IceEntry.instance().getAnyCenterPrx();
		if (prx == null) {
			throw new NoteException(ERROR_GAMESERVER_DISCONNECTED);
		}
		return prx;
	}

	@Override
	public void getPropertyConfig_async(final AMD_Gm_getPropertyConfig __cb, Current __current) throws NoteException {
		CenterPrx prx = this.findAnyGameServerPrx();
		prx.begin_getPropertyConfig(new Callback_Center_getPropertyConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(String[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});

	}

	@Override
	public void getPlayerSkillConfig_async(final AMD_Gm_getPlayerSkillConfig __cb, Current __current)
			throws NoteException {
		CenterPrx prx = this.findAnyGameServerPrx();
		prx.begin_getPlayerSkillConfig(new Callback_Center_getPlayerSkillConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(IntString[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getHeroSkillConfig_async(final AMD_Gm_getHeroSkillConfig __cb, Current __current) throws NoteException {
		CenterPrx prx = this.findAnyGameServerPrx();
		prx.begin_getHeroSkillConfig(new Callback_Center_getHeroSkillConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(IntString[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getRelationConfig_async(final AMD_Gm_getRelationConfig __cb, Current __current) throws NoteException {
		CenterPrx prx = this.findAnyGameServerPrx();
		prx.begin_getRelationConfig(new Callback_Center_getRelationConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(IntString[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void sendSystemMail_async(final AMD_Gm_sendSystemMail __cb, int serverId, String roleId, String roleName,
			String title, String content, Property[] attach,String senderName, Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);

		if (!TextUtil.isNullOrEmpty(roleId)) {
			prx.begin_sendMailByRoleId(roleId.trim(), title, content, attach,senderName, new Callback_Center_sendMailByRoleId() {
				@Override
				public void exception(LocalException __ex) {
					__cb.ice_exception(__ex);
				}

				@Override
				public void response() {
					__cb.ice_response();
				}

				@Override
				public void exception(UserException __ex) {
					__cb.ice_exception(__ex);
				}
			});
		} else if (!TextUtil.isNullOrEmpty(roleName)) {
			prx.begin_sendMail(roleName.trim(), title, content, attach,senderName, new Callback_Center_sendMail() {

				@Override
				public void exception(LocalException __ex) {
					__cb.ice_exception(__ex);
				}

				@Override
				public void response() {
					__cb.ice_response();
				}

				@Override
				public void exception(UserException __ex) {
					__cb.ice_exception(__ex);
				}
			});
		}
	}

	@Override
	public void executeGroovyScript_async(final AMD_Gm_executeGroovyScript __cb, int serverId, String script,
			Current __current) throws NoteException {
		if (!LoginDatabase.instance().getAc().getBean("CanExecuteGroovy", Boolean.class)) {
			throw new NoteException("你是谁，我认识你吗？");
		}
		if (script.startsWith("activeCodeReuse")) {
			this.activeCodeReuse = Boolean.parseBoolean(script.split("=")[1]);
			__cb.ice_response();
			return;
		}

		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_executeGroovyScript(script, new Callback_Center_executeGroovyScript() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void generateCDK_async(final AMD_Gm_generateCDK __cb, final String name, final String category,
			final int number, final int[] channels, final int[] servers, final int minLevel, final int maxLevel,
			final String factionName, final int chargeMoney, final String beginTime, final String endTime,
			final String remark, final Property[] content, Current __current) throws NoteException {
		if (category.length() != 4) {
			__cb.ice_exception(new NoteException("类别必须为4个字符！"));
			return;
		}
		if (minLevel > maxLevel) {
			__cb.ice_exception(new NoteException("等级上下限设置错误！"));
			return;
		}
		final Date begin = DateUtil.parseDate(beginTime);
		final Date end = DateUtil.parseDate(endTime);
		if (begin.getTime() > end.getTime()) {
			__cb.ice_exception(new NoteException("有效期设置错误！"));
			return;
		}
		if (content.length < 1) {
			__cb.ice_exception(new NoteException("内容不可为空。"));
			return;
		}
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				CDKGroup group = new CDKGroup(name, category, TextUtil.GSON.toJson(channels), TextUtil.GSON
						.toJson(servers), minLevel, maxLevel, factionName, chargeMoney, begin, end, TextUtil.GSON
						.toJson(content), Calendar.getInstance().getTime(), remark);
				CdkGenerator generator = new CdkGenerator();
				List<String> cdkList = generator.generateDistinctCode(number, 6, 6);
				List<String> resultList = new ArrayList<String>();
				for (String cdk : cdkList) {
					String result = category + cdk;
					resultList.add(result);
					group.getCdks().add(new CDKDetail(result, group, 0, 1));
				}

				try {
					CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).save(group);
					__cb.ice_response(TextUtil.join(resultList, "<br>"));
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("保存出错。"));
				}
			}
		});
	}

	@Override
	public void updateCDK_async(final AMD_Gm_updateCDK __cb, final String name, final String category,
			final int number, final int[] channels, final int[] servers, final int minLevel, final int maxLevel,
			final String factionName, final int chargeMoney, final String beginTime, final String endTime,
			final String remark, final Current __current) throws NoteException {
		if (category.length() != 4) {
			__cb.ice_exception(new NoteException("Prefix's length must be 4!"));
			return;
		}
		if (minLevel > maxLevel) {
			__cb.ice_exception(new NoteException("Level error!"));
			return;
		}
		final Date begin = DateUtil.parseDate(beginTime);
		final Date end = DateUtil.parseDate(endTime);
		if (begin.getTime() > end.getTime()) {
			__cb.ice_exception(new NoteException("Date error!"));
			return;
		}
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				try {
					CDKGroup group = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).loadCdk(
							category);
					if (group == null) {
						throw new Exception();
					}
					group.setName(name);
					group.setCategory(category);
					group.setChannels(TextUtil.GSON.toJson(channels));
					group.setServers(TextUtil.GSON.toJson(servers));
					group.setMinLevel(minLevel);
					group.setMaxLevel(maxLevel);
					group.setFactionName(factionName);
					group.setChargeMoney(chargeMoney);
					group.setBeginTime(begin);
					group.setEndTime(end);
					group.setRemark(remark);

					// cdk数量增加
					List<String> resultList = new ArrayList<String>();
					if (number > group.getCdks().size()) {
						List<String> codeList = new ArrayList<String>();
						for (CDKDetail cd : group.getCdks()) {
							codeList.add(cd.getCdk());
						}
						CdkGenerator generator = new CdkGenerator();
						List<String> cdkList = null;

						while (number > codeList.size()) {
							cdkList = generator.generateDistinctCode(number - codeList.size(), 6, 6);
							for (String cdk : cdkList) {
								String result = category + cdk;
								// 防止生成重复的cdk
								if (codeList.contains(result)) {
									continue;
								}
								codeList.add(result);
								resultList.add(result);
								group.getCdks().add(new CDKDetail(result, group, 0, 1));
							}
						}
					}
					CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).update(group);
					__cb.ice_response(TextUtil.join(resultList, "<br>"));
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("Modify CDK error."));
				}
			}
		});
	}

	@Override
	public void loadCDK_async(final AMD_Gm_loadCDK __cb, final String category, Current __current) throws NoteException {
		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				try {
					CDKGroup cg = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).loadCdk(category);
					if (cg == null) {
						throw new Exception();
					}
					CdkView cv = new CdkView();
					cv.channelIds = TextUtil.GSON.fromJson(cg.getChannels(), int[].class);
					cv.endTime = DateUtil.toString(cg.getEndTime().getTime(), "MM/dd/yyyy HH:mm:ss");
					cv.maxLevel = cg.getMaxLevel();
					cv.minLevel = cg.getMinLevel();
					cv.name = cg.getName();
					cv.needPay = cg.getChargeMoney();
					cv.number = cg.getCdks().size();
					cv.prefix = cg.getCategory();
					cv.remark = cg.getRemark();
					cv.serverIds = TextUtil.GSON.fromJson(cg.getServers(), int[].class);
					cv.startTime = DateUtil.toString(cg.getBeginTime().getTime(), "MM/dd/yyyy HH:mm:ss");
					cv.union = cg.getFactionName();
					cv.adjunct = cg.getContent();
					__cb.ice_response(TextUtil.GSON.toJson(cv));
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("Prefix " + category + " is not exists!"));
				}
			}
		});

	}

	@Override
	public void deleteCDK_async(final AMD_Gm_deleteCDK __cb, final String category, Current __current)
			throws NoteException {
		if (StringUtils.isBlank(category) || category.length() != 4) {
			__cb.ice_exception(new NoteException("Prefix's length must be 4!"));
			return;
		}
		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				try {
					CDKGroup group = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).loadCdk(
							category);
					if (group == null) {
						throw new Exception();
					}
					CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).delete(group);
					__cb.ice_response();
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("Delete CDK error."));
				}
			}
		});
	}

	private CenterPrx getCenterPrxByCheck(int serverId, Ice.AMDCallback cb) throws NoteException {
		CenterPrx prx = IceEntry.instance().getCenterPrx(serverId, true);
		if (prx == null) {
			NoteException e = new NoteException(ERROR_GAMESERVER_DISCONNECTED);
			throw e;
			// if (cb == null) {
			// throw e;
			// } else {
			// cb.ice_exception(e);
			// }
		}

		return prx;
	}

	@Override
	public void sendAnnounce_async(final AMD_Gm_sendAnnounce __cb, int serverId, String content, Current __current)
			throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_systemAnnounce(content, new Callback_Center_systemAnnounce() {

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(LocalException ex) {
				__cb.ice_exception(ex);
			}
		});
	}

	@Override
	public void kickRole_async(final AMD_Gm_kickRole __cb, int serverId, String account, String roleId,
			Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_kick(account.trim(), roleId.trim(), new Callback_Center_kick() {

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(LocalException ex) {
				__cb.ice_exception(ex);
			}
		});
	}

	@Override
	public void fobidenChat_async(final AMD_Gm_fobidenChat __cb, int serverId, String roleId, String releaseTime,
			Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_silence(roleId.trim(), releaseTime, new Callback_Center_silence() {

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(LocalException ex) {
				__cb.ice_exception(ex);
			}
		});
	}

	@Override
	public void forzenAccount_async(final AMD_Gm_forzenAccount __cb, final String accountInput,
			final String releaseTime, Current __current) throws NoteException {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				String account = accountInput.trim();
				Account acc = XsgAccountManager.getInstance().findAccount(account);
				if (acc != null) {
					acc.setFrozenExpireTime(DateUtil.parseDate(releaseTime));
					XsgAccountManager.getInstance().updateAccount(acc);
					StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(
							new AccountOperateLog(account, Calendar.getInstance().getTime(), "[GM]" + releaseTime));
				}
				__cb.ice_response();
			}
		});
	}

	@Override
	public void unforzenAccount_async(final AMD_Gm_unforzenAccount __cb, final String accountInput, Current __current)
			throws NoteException {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				String account = accountInput.trim();
				Account acc = XsgAccountManager.getInstance().findAccount(account);
				acc.setFrozenExpireTime(null);
				__cb.ice_response();
				XsgAccountManager.getInstance().updateAccount(acc);
			}
		});
	}

	public boolean canReuseActiveCode() {
		return this.activeCodeReuse;
	}

	@Override
	public String getChannelConfig(Current __current) throws NoteException {
		return TextUtil.GSON.toJson(CenterServer.instance().getAllChannel().toArray());
	}

	@Override
	public void sendSystemServerMail_async(final AMD_Gm_sendSystemServerMail __cb, int serverId, String title,
			String content, Property[] attach, String mailParams,String sendName, Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_sendServerMail(title, content, attach, mailParams,sendName, new Callback_Center_sendServerMail() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void editGameServer_async(AMD_Gm_editGameServer __cb, int id, String name, int showId, String gateIp,
			boolean isNew, boolean cpShowOnly, boolean cpEnterOnly, int onlineLimit, int targetId, Current __current)
			throws NoteException {
		GameServer gs = new GameServer();
		gs.setId(id);
		gs.setName(name);
		gs.setShowId(showId);
		gs.setHost(gateIp);
		gs.setIsNew(isNew);
		gs.setCpEnterOnly(cpEnterOnly);
		gs.setCpShowOnly(cpShowOnly);
		gs.setOnlineLimit(onlineLimit);
		gs.setTargetId(targetId);

		CenterServer.instance().editGameServer(__cb, gs);
	}

	@Override
	public void editChannel_async(AMD_Gm_editChannel __cb, int id, String name, String orderUrl, Current __current)
			throws NoteException {
		Channel channel = new Channel();
		channel.setId(id);
		channel.setName(name);
		channel.setOrderUrl(orderUrl);
		channel.setCallbackUrl("");
		CenterServer.instance().editChannel(__cb, channel);
	}

	@Override
	public void uploadLoginAnnounce_async(AMD_Gm_uploadLoginAnnounce __cb, byte[] file, Current __current)
			throws NoteException {
		try {
			XsgAnnounceManager.getInstance().uploadLoginAnnounce(file);
			__cb.ice_response();
		} catch (FileNotFoundException e) {
			logger.error(e, e);
			throw new NoteException("文件不存在");
		} catch (IOException e) {
			logger.error(e, e);
			throw new NoteException("IO异常");
		}
	}

	@Override
	public void setVipLevel_async(final AMD_Gm_setVipLevel __cb, int serverId, String roleName, int vip,
			Current __current) throws NoteException {
		if (vip > 15 || vip < 1) {
			throw new NoteException("参数错误。");
		}
		roleName = roleName.trim();
		String script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
				+ "XsgRoleManager.getInstance().findRoleByName(\"" + roleName + "\").getVipController().setVipLevel("
				+ vip + ");";
		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public void getFactionList_async(final AMD_Gm_getFactionList __cb, int serverId, String factionName,
			Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_getFactionList(factionName, new Callback_Center_getFactionList() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(GmFactionView[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getFactionMemberList_async(final AMD_Gm_getFactionMemberList __cb, int serverId, String factionName,
			Current __current) throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		factionName = factionName.trim();
		prx.begin_getFactionMemberList(factionName, new Callback_Center_getFactionMemberList() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(GmFactionMemberView[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getRankList_async(final AMD_Gm_getRankList __cb, int serverId, int type, Current __current)
			throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_getRankList(type, new Callback_Center_getRankList() {

			@Override
			public void response(GmRankView[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	@Override
	public void getPayLog_async(final AMD_Gm_getPayLog __cb, int serverId, String roleName, Current __current)
	           throws NoteException {
	       CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
	       roleName = roleName.trim();
	       prx.begin_getPayLog(roleName, new Callback_Center_getPayLog() {
	 
	           @Override
	           public void response(GmPayView __ret) {
	               __cb.ice_response(__ret);
	           }
	 
	           @Override
	           public void exception(UserException __ex) {
	               __cb.ice_exception(__ex);
	           }
	 
	           @Override
	           public void exception(LocalException __ex) {
	               __cb.ice_exception(__ex);
	           }
	       });
	   }

	@Override
	public void setRecommendServer_async(AMD_Gm_setRecommendServer __cb, int serverId, Current __current)
			throws NoteException {
		CenterServer.instance().setRecommendServer(serverId);
		__cb.ice_response();
	}

	@Override
	public WhiteList[] getCurrentWhiteList(Current __current) throws NoteException {
		return CenterServer.instance().getCurrentWhiteList();
	}

	@Override
	public void setWhiteList_async(AMD_Gm_setWhiteList __cb, WhiteList param, Current __current) throws NoteException {
		// logger.info(TextUtil.GSON.toJson(param));
		CenterServer.instance().setWhiteList(param);
		__cb.ice_response();
	}

	@Override
	public void reloadScript_async(final AMD_Gm_reloadScript __cb, int serverId, ScriptReloadConfig config,
			Current __current) throws NoteException {
		String script = "import com.morefun.XSanGo.onlineAward.XsgOnlineAwardManager;\n"
				+ "import com.morefun.XSanGo.reward.XsgRewardManager;\n"
				+ "import com.morefun.XSanGo.sign.XsgSignManager;\n"
				+ "import com.morefun.XSanGo.heroAdmire.XsgHeroAdmireManager;\n"
				+ "import com.morefun.XSanGo.activity.XsgActivityManage;\n"
				+ "import com.morefun.XSanGo.activity.XsgAnnounceManager;\n"
				+ "import com.morefun.XSanGo.collect.XsgCollectHeroSoulManager;\n"
				+ "import com.morefun.XSanGo.shop.XsgShopManage;\n"
				+ "import com.morefun.XSanGo.hero.market.HeroMarketManager;\n"
				+ "import com.morefun.XSanGo.vip.XsgVipManager;\n" + "import com.morefun.XSanGo.copy.XsgCopyManager;\n"
				+ "import com.morefun.XSanGo.ladder.XsgLadderManager;\n"
				+ "import com.morefun.XSanGo.buyJinbi.XsgBuyJInbiManager;\n"
				+ "import com.morefun.XSanGo.faction.XsgFactionManager;\n"
				+ "import com.morefun.XSanGo.mail.XsgMailManager;\n"
				+ "import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager;\n"
				+ "import com.morefun.XSanGo.activity.XsgShareManage;\n";
		if (config.activityList) { // 活动列表
			script += "XsgActivityManage.getInstance().loadActivityScript();\n";
		}

		if (config.announce) {// 公告
			script += "XsgAnnounceManager.getInstance().loadAnnounceScript();\n";
		}

		if (config.sumChargeConsume) {// 累计充值，累计消费
			script += "XsgActivityManage.getInstance().loadChargeConsumeScript();\n";
		}

		if (config.dayChargeConsume) {// 日充值消费
			script += "XsgActivityManage.getInstance().loadDayChargeConsumeScript();\n";
		}

		if (config.levelReward) {// 等级奖励
			script += "XsgActivityManage.getInstance().loadLevelRewardScript();\n";
		}

		if (config.growFoundation) { // 成长基金
			script += "XsgActivityManage.getInstance().loadFundScript();\n";
		}

		if (config.sign) { // 签到
			script += "XsgSignManager.getInstance().loadSignScript();\n";
			script += "XsgSignManager.getInstance().loadTotalSignScript();\n";
			script += "XsgSignManager.getInstance().loadRouletteScript();\n";
		}

		if (config.heroAdmire) { // 名将仰慕
			script += "XsgHeroAdmireManager.getInstance().loadHeroAdmireScript();\n";
		}

		if (config.heroSoulCollect) {// 名将召唤
			script += "XsgCollectHeroSoulManager.getInstance().loadCollectHeroSoulScript();\n";
		}

		if (config.levelupGift) { // 升级奖励
			script += "XsgActivityManage.getInstance().loadUpGiftScript();\n";
		}

		if (config.secondKill) { // 天天秒杀
			script += "XsgActivityManage.getInstance().loadSeckillScript();\n";
		}

		if (config.makeVip) { // 我要做VIP
			script += "XsgActivityManage.getInstance().loadMakeVipScript();\n";
		}

		if (config.inviteFriend) { // 邀请好友
			script += "XsgActivityManage.getInstance().loadInviteFriendScript();\n";
		}

		if (config.online) { // 在线时长
			script += "XsgOnlineAwardManager.getInstance().loadOnlineRewardScript();\n";
		}

		if (config.tcReward) { // TC奖励
			script += "XsgRewardManager.getInstance().loadTcScript();\n";
		}

		if (config.mail) {// 邮件
			script += "XsgMailManager.getInstance().loadMailScript();\n";
		}
		if (config.mall) {// 商城
			script += "XsgShopManage.getInstance().loadShopScript();\n";
		}
		if (config.fortuneWheel) {// 幸运转盘
			script += "XsgActivityManage.getInstance().loadFortuneWheelScript();\n";
		}
		if (config.dayLogin) {// 登陆奖励
			script += "XsgActivityManage.getInstance().loadDayLoginScript();\n";
		}
		if (config.sendJunLing) {// 军令补偿
			script += "XsgActivityManage.getInstance().loadSendJunLingScript();\n";
		}
		if (config.powerReward) {// 最强战力
			script += "XsgActivityManage.getInstance().loadCombatPowerRewardScript();\n";
		}
		if (config.limitHero) {// 酒馆魂匣
			script += "HeroMarketManager.getInstance().loadLimitHeroScript();\n";
		}
		if (config.monthFirstCharge) {// 首充礼包
			script += "XsgVipManager.getInstance().loadMonthFirstChargeScript();\n";
		}
		if (config.vitBuy) { // 体力购买
			script += "XsgCopyManager.getInstance().loadBuyJunLingScript();\n";
		}
		if (config.ladder) {// 群雄争霸
			script += "XsgLadderManager.getInstance().loadLadderLevelAwardScript();\n";
		}
		if (config.jinbiBuy) { // 点金手
			script += "XsgBuyJInbiManager.getInstance().loadBuyJinbiScript();\n";
		}
		if (config.faction) { // 公会相关
			script += "XsgFactionManager.getInstance().loadFactionScript();\n";
		}
		if (config.luckyBag) {// 福袋
			script += "com.morefun.XSanGo.luckybag.XsgLuckyBagManager.getInstance().loadLuckyBagScript();\n";
		}
		if (config.cornucopia) {// 聚宝盆
			script += "XsgActivityManage.getInstance().loadCornucopiaScript();\n";
		}
		if (config.goodsExchange) {// 物物兑换
			script += "XsgActivityManage.getInstance().loadExchangeScript();\n";
		}
		if (config.haoqingbao) {// 豪情堡
			script += "XsgHaoqingbaoManager.getInstance().loadHaoqingbaoScript();\n";
		}
		if (config.dayForeverLogin) {// 永久累计登陆
			script += "XsgActivityManage.getInstance().loadDayforverLoginScript();\n";
		}
		if (config.share) {// 分享活动
			script += "XsgShareManage.getInstance().loadAutoBaseCfg4Id();\n";
		}
		if (config.football) {// 足球
			script += "XsgActivityManage.getInstance().loadFootballScript();\n";
		}

		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public GuideConfig getGuideConfig(Current __current) throws NoteException {
		return CenterServer.instance().getGuideConfig();
	}

	@Override
	public void setGuideConfig(GuideConfig config, Current __current) throws NoteException {
		CenterServer.instance().setGuideConfig(config);
	}

	@Override
	public void mockCharge_async(final AMD_Gm_mockCharge __cb, String account, String roleId, int channelId,
			int serverId, int cent, Current __current) throws NoteException {

		if (!this.chargeConfigMap.containsKey(cent)) {
			__cb.ice_exception(new NoteException("Invalid value!"));
			return;
		}
		account = account.trim();
		roleId = roleId.trim();
		CustomChargeParams params = new CustomChargeParams();
		params.mac = "mock";
		params.item = this.chargeConfigMap.get(cent);

		final Charge newOrder = new Charge(UUID.randomUUID().toString(), channelId, account, serverId, roleId, cent, 0,
				0, Calendar.getInstance().getTime(), Charge.State_Init, TextUtil.GSON.toJson(params), "");
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				dao.save(newOrder);
				__cb.ice_response();
			}
		});
	}

	@Override
	public void executeGroovyCombineScript_async(final AMD_Gm_executeGroovyCombineScript __cb, int serverId,
			Current __current) throws NoteException {
		if (!LoginDatabase.instance().getAc().getBean("CanExecuteGroovy", Boolean.class)) {
			throw new NoteException("你是谁，我又认识你吗？");
		}
		StringBuffer script = new StringBuffer();
		script.append("import com.morefun.XSanGo.ladder.XsgLadderManager;\n");
		script.append("import com.morefun.XSanGo.auction.XsgAuctionHouseManager;\n");
		script.append("XsgLadderManager.getInstance().sendAward();\n");
		script.append("XsgAuctionHouseManager.getInstance().finishAllAuction();\n");
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_executeGroovyScript(script.toString(), new Callback_Center_executeGroovyScript() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response() {
				__cb.ice_response();
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});
	}

	 @Override
	 public void deleteItem_async(final AMD_Gm_deleteItem __cb, final int serverId, String roleId, String itemId, int num,
	           Current __current) {
		 try {
           CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
 
           prx.begin_deleteItem(roleId, itemId, num, new Callback_Center_deleteItem() {
 
               @Override
               public void exception(LocalException __ex) {
                   __cb.ice_exception(__ex);
               }
 
               @Override
               public void response(String __ret) {
                   __cb.ice_response(__ret);
               }
 
           });
       } catch (Exception e) {
           __cb.ice_exception(e);
       }
	}

	@Override
	public void addSycee_async(final AMD_Gm_addSycee __cb, int serverId, String roleName, int sycee, Current __current)
			throws NoteException {
		String script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
				+ "XsgRoleManager.getInstance().findRoleByName(\"" + roleName + "\").winYuanbao(" + sycee + "," + true
				+ ");";
		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public void addVipExp_async(final AMD_Gm_addVipExp __cb, int serverId, String roleName, int exp, Current __current)
			throws NoteException {
		String script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
				+ "XsgRoleManager.getInstance().findRoleByName(\"" + roleName + "\").getVipController().addExperience("
				+ exp + ");";
		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public void skipCopy_async(final AMD_Gm_skipCopy __cb, int serverId, String roleId, int diff, int copyId,
			Current __current) throws NoteException {
		String script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
				+ "XsgRoleManager.getInstance().findRoleById(\"" + roleId + "\").getCopyControler().setCopyProgress("
				+ diff + "," + copyId + ");";
		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public void addWheelCount_async(final AMD_Gm_addWheelCount __cb, int serverId, String roleId, int count,
			int wheelType, Current __current) throws NoteException {
		String script = null;
		if (wheelType == 0) {
			script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
					+ "XsgRoleManager.getInstance().findRoleById(\"" + roleId
					+ "\").getFortuneWheelControler().addLastCount(" + count + ");";
		} else {
			script = "import com.morefun.XSanGo.role.XsgRoleManager;\n"
					+ "XsgRoleManager.getInstance().findRoleById(\"" + roleId
					+ "\").getSuperChargeControlle().addRaffleNum(" + count + ");";
		}
		this.executeGroovyScript_async(new AMD_Gm_executeGroovyScript() {

			@Override
			public void ice_exception(Exception ex) {
				__cb.ice_exception(ex);
			}

			@Override
			public void ice_response() {
				__cb.ice_response();
			}
		}, serverId, script);
	}

	@Override
	public void download_async(final AMD_Gm_download __cb, int serverId, String roleId, Current __current)
			throws NoteException {

		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_getRoleDB(roleId, new Callback_Center_getRoleDB() {
			
			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}
			
			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(byte[] __ret) {
				__cb.ice_response(__ret);				
			}
		});
	}

	@Override
	public void importRole_async(final AMD_Gm_importRole __cb, int serverId, byte[] data, String importType, Current __current)
			throws NoteException {

		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_saveRoleData(serverId, data, importType, new Callback_Center_saveRoleData() {
			
			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);				
			}
			
			@Override
			public void response() {
				__cb.ice_response();					
			}
			
			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);				
			}
		});
	}

	@Override
	public void findFuzzyRoleViewList_async(final AMD_Gm_findFuzzyRoleViewList __cb, int serverId, String accountName,
			String roleName, Current __current) throws NoteException {

	       CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
	 
	       if (!TextUtil.isNullOrEmpty(accountName)) {
	           prx.begin_findRoleViewListBySimpleAccount(accountName, new Callback_Center_findRoleViewListBySimpleAccount() {
	               
	               @Override
	               public void exception(LocalException __ex) {
	                   __cb.ice_exception(__ex);                    
	               }
	               
	               @Override
	               public void response(String __ret) {
	                   __ret = attachAccountDate(__ret);
	                   __cb.ice_response(__ret);
	               }
	           });
	       } else if (!TextUtil.isNullOrEmpty(roleName)) {
	           prx.begin_findRoleViewListBySimpleRole(roleName.trim(), new Callback_Center_findRoleViewListBySimpleRole(){
	 
	               @Override
	               public void response(String __ret) {
	                   __ret = attachAccountDate(__ret);
	                   __cb.ice_response(__ret);
	               }
	 
	               @Override
	               public void exception(LocalException __ex) {
	                   __cb.ice_exception(__ex);
	               }
	               
	           });
	       }
	}

	@Override
	public void queryCdkByCDK_async(final AMD_Gm_queryCdkByCDK __cb, int serverId, final String cdk, Current __current)
			throws NoteException {

	       CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
	       
	       prx.begin_queryRoleByCDK(cdk, new Callback_Center_queryRoleByCDK() {
	           
	           @Override
	           public void exception(LocalException __ex) {
	               __cb.ice_exception(__ex);                
	           }
	           
	           @Override
	           public void response(final String __ret) {
	               LoginDatabase.execute(new Runnable() {
	                   @Override
	                   public void run() {
	                       CdkView cv = null;
	                       CdkDetailView view = new CdkDetailView();
	                       try {
	                           List<Object[]> results = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).findCdkUseTimeAndGroupId(cdk);
	                           if (results != null && results.size()>0) {
	                               Object[] result = results.get(0);
	                               CDKGroup cg = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).loadCdkById((Integer)result[0]);
	                               view.useNum = (Integer) result[1];
	                               if(cg != null){
	                                   cv = new CdkView();;
	                                   cv.channelIds = TextUtil.GSON.fromJson(cg.getChannels(), int[].class);
	                                   cv.endTime = DateUtil.toString(cg.getEndTime().getTime(), "MM/dd/yyyy HH:mm:ss");
	                                   cv.maxLevel = cg.getMaxLevel();
	                                   cv.minLevel = cg.getMinLevel();
	                                   cv.name = cg.getName();
	                                   cv.needPay = cg.getChargeMoney();
	                                   cv.number = cg.getCdks().size();
	                                   cv.prefix = cg.getCategory();
	                                   cv.remark = cg.getRemark();
	                                   cv.serverIds = TextUtil.GSON.fromJson(cg.getServers(), int[].class);
	                                   cv.startTime = DateUtil.toString(cg.getBeginTime().getTime(), "MM/dd/yyyy HH:mm:ss");
	                                   cv.union = cg.getFactionName();
	                                   cv.adjunct = cg.getContent();
	                               }
	                           }
	                           
	                           view.detailView = cv;
	                           if(__ret != null && __ret.contains("#")){
	                               String[] split = __ret.split("#");
	                               String roleId = split[0];
	                               String useTime = split[1];
	                               
	                               view.roleName = roleId;
	                               view.useDate = useTime;
	                           }
	                           __cb.ice_response(TextUtil.GSON.toJson(view));
	                       } catch (Exception e) {
	                           logger.error(e);
	                           __cb.ice_exception(new NoteException("cdk " + cdk + " is not exists!"));
	                           return;
	                       }
	                   }
	               });
	           }
	           
	           @Override
	           public void exception(UserException __ex) {
	               __cb.ice_exception(__ex);                
	           }
	       });
	}

	@Override
	public void getServerItemConfig_async(final AMD_Gm_getServerItemConfig __cb, int serverId, Current __current)
			throws NoteException {
		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
		prx.begin_getItemConfig(new Callback_Center_getItemConfig() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_exception(__ex);
			}

			@Override
			public void response(String[] __ret) {
				__cb.ice_response(__ret);
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_exception(__ex);
			}
		});

	
	}

//	@Override
//	public void findFuzzyRoleViewList_async(final AMD_Gm_findFuzzyRoleViewList __cb, int serverId, String accountName,
//			String roleName, Current __current) throws NoteException {
//		CenterPrx prx = this.getCenterPrxByCheck(serverId, __cb);
//
//		if (!TextUtil.isNullOrEmpty(accountName)) {
//			prx.begin_findRoleViewList(accountName.trim(), new Callback_Center_findRoleViewList() {
//
//				@Override
//				public void exception(LocalException __ex) {
//					__cb.ice_exception(__ex);
//				}
//
//				@Override
//				public void response(String __ret) {
//					__ret = attachAccountDate(__ret);
//					__cb.ice_response(__ret);
//				}
//
//			});
//		} else if (!TextUtil.isNullOrEmpty(roleName)) {
//			prx.begin_findRoleViewListBySimpleRole(roleName.trim(), new Callback_Center_findRoleViewListBySimpleRole(){
//
//				@Override
//				public void response(String __ret) {
//					__ret = attachAccountDate(__ret);
//					__cb.ice_response(__ret);
//				}
//
//				@Override
//				public void exception(LocalException __ex) {
//					__cb.ice_exception(__ex);
//				}
//				
//			});
//		}
//	
//	}
}
