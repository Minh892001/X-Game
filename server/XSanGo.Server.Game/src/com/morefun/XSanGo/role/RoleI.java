/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Role_getOtherPlayInfo;
import com.XSanGo.Protocol.AMD_Role_getRoleHeros;
import com.XSanGo.Protocol.AMD_Role_getRoleViewList;
import com.XSanGo.Protocol.AMD_Role_randomName;
import com.XSanGo.Protocol.AMD_Role_rename;
import com.XSanGo.Protocol.AMD_Role_resetRole;
import com.XSanGo.Protocol.AMD_Role_setSexAndName;
import com.XSanGo.Protocol.ActivityAnnounceView;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.DeviceInfo;
import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelSkillTemplateView;
import com.XSanGo.Protocol.LoadingRankList;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RoleCallbackPrx;
import com.XSanGo.Protocol.RoleCallbackPrxHelper;
import com.XSanGo.Protocol.RoleView;
import com.XSanGo.Protocol.RoleViewForOtherPlayer;
import com.XSanGo.Protocol.SceneDuelView;
import com.XSanGo.Protocol._RoleDisp;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.MFBI.XsgMFBIManager;
import com.morefun.XSanGo.battle.DuelBattle;
import com.morefun.XSanGo.battle.DuelReport;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.StoryEventT;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleWeixinShare;
import com.morefun.XSanGo.db.stat.RoleCreateLog;
import com.morefun.XSanGo.db.stat.StatDao;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.rankList.XsgRankListManager;
import com.morefun.XSanGo.script.CellWrap;
import com.morefun.XSanGo.sns.XsgSnsManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * @author Su LingYun角色通讯协议实现
 * 
 */
@SuppressWarnings("serial")
public class RoleI extends _RoleDisp {

	/** 日志 */
	protected final static Log logger = LogFactory.getLog(RoleI.class);

	/** 角色名规则 */
	private static Pattern roleNamePattern = Pattern.compile(Messages.getString("RoleI.0")); //$NON-NLS-1$

	/** 角色重置的上下文 */
	private static ConcurrentHashMap<String, String> roleResetContextMap = new ConcurrentHashMap<String, String>();

	/** 账号 */
	private String account;

	/** 角色 */
	private IRole roleRt;

	/** 连接session */
	private GameSessionI session;

	/** 角色数据库dao */
	private static RoleDAO roleDAO;

	static {
		// 初始化dao
		if (ServerLancher.getAc() != null) {
			roleDAO = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		}
	}

	public static RoleDAO getRoleDAO() {
		return roleDAO;
	}

	/**
	 * 构造函数
	 * 
	 * @param userId
	 * @param gameSessionI
	 */
	public RoleI(String userId, GameSessionI gameSessionI) {
		this.account = userId;
		this.session = gameSessionI;
	}

	/**
	 * 设置角色对象
	 * 
	 * @param role
	 */
	private void bindRole(IRole role) {
		if (this.roleRt != null) {
			logger.warn(TextUtil.format("Already bind a role.{0}|{1}", //$NON-NLS-1$
					this.roleRt.getRoleId(), role.getRoleId()));
			return;
		}
		if (role != null && this.session != null) {
			this.session.bindRole(role);
		}

		this.roleRt = role;
		this.roleRt.afterEnterGame();
		this.roleRt.setOnline(true);

		if (role.getRenameCount() < 1) {
			RoleCallbackPrx roleCb = this.session.getRoleCb();
			if (roleCb != null) {
				roleCb.begin_showRenameUI();
			}
		}
	}

	@Override
	public String setRoleCallback(RoleCallbackPrx cb, Current __current) {
		Map<String, String> ctx = new HashMap<String, String>();
		ctx.put("_fwd", "o"); //$NON-NLS-1$ //$NON-NLS-2$
		session.setRoleCb(RoleCallbackPrxHelper.checkedCast(cb.ice_context(ctx)));
		String tocken = session.getId().name;
		// 断线重连的标记
		GameSessionManagerI.getInstance().addReconnectKey(this.account, tocken, this.session.getDevice());

		// 登录显示排行榜
		LoadingRankList loadingRankList = XsgRankListManager.getInstance().LoadingRankList();
		if (loadingRankList.LoadingRank != null) {
			session.getRoleCb().begin_loginRankList(loadingRankList);
		}

		return tocken;
	}

	@Override
	public void rename_async(final AMD_Role_rename __cb, final String name, Current __current) throws NoteException,
			NotEnoughYuanBaoException {
		if (!roleNamePattern.matcher(name).matches()) {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.4"))); //$NON-NLS-1$
			return;
		}
		if (XsgSnsManager.getInstance().NAME_OF_MS_LING.contains(name)) {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.5"))); //$NON-NLS-1$
			return;
		}
		if (SensitiveWordManager.getInstance().hasSensitiveWord(name)) {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.5"))); //$NON-NLS-1$
			return;
		}

		if (this.roleRt.getLastRenameTime() != null) {
			int passDay = (int) ((System.currentTimeMillis() - this.roleRt.getLastRenameTime().getTime()) / TimeUnit.DAYS
					.toMillis(1));
			if (passDay < XsgRoleManager.getInstance().getRenameConfigT().intervalDay) {
				__cb.ice_exception(new NoteException(TextUtil.format(
						Messages.getString("RoleI.6"), XsgRoleManager.getInstance() //$NON-NLS-1$
								.getRenameConfigT().intervalDay - passDay)));
				return;
			}
		}

		XsgRoleManager.getInstance().loadRoleByNameAsync(name, new Runnable() {

			@Override
			public void run() {
				// 数据库存在相关对象时执行逻辑
				__cb.ice_exception(new NoteException(TextUtil.format(Messages.getString("RoleI.7"), name))); //$NON-NLS-1$
			}
		}, new Runnable() {

			@Override
			public void run() {
				try {
					roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, roleRt.getRenameYuanbao()));
				} catch (NotEnoughMoneyException e) {
					LogManager.error(e);
				} catch (NotEnoughYuanBaoException e) {
					__cb.ice_exception(new NotEnoughYuanBaoException());
					return;
				}

				roleRt.rename(name);
				roleRt.resetLastRenameTime();
				__cb.ice_response();
			}
		});
	}

	@Override
	public boolean readActivityAnnounce(int id, Current __current) {
		return this.roleRt.readAnnounce(id);
	}

	@Override
	public void levelUp(Current __current) throws NoteException {
		// 出于客户端兼容性考虑，使用废弃的手动升级功能来做帐号重置
		int limitLevel = XsgGameParamManager.getInstance().getAccountResetLevel();
		if (this.roleRt.getLevel() > limitLevel) {
			throw new NoteException(Messages.getString("RoleI.8") + limitLevel + Messages.getString("RoleI.9")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		ChatCallbackPrx prx = this.roleRt.getChatControler().getChatCb();
		if (prx != null) {
			prx.begin_confirm(Messages.getString("RoleI.10")); //$NON-NLS-1$
		}

		throw new NoteException(""); //$NON-NLS-1$
	}

	@Override
	public void getRoleViewList_async(final AMD_Role_getRoleViewList __cb, Current __current) {
		final DeviceInfo device = session.getDevice();
		// 获取玩家请求进入的服务器ID，这是合服相关逻辑处理，由哪个服务器入口进，就给哪个服务器里的角色
		final int requestServerId = device == null ? ServerLancher.getServerId() : device.requestServerId;
		// 合服，查帐号时候带上服务器ID
		IRole role = XsgRoleManager.getInstance().findRoleByAccount(this.account, requestServerId);

		if (role != null) {
			// 设置firstInTime
			if (role.getFirstInTime() <= 0L) {
				if (device != null) {
					role.updateFirstInTime(device.firstInTime);
				}
			}
			role.beforeEnterGame();
			__cb.ice_response(new RoleView[] { role.getView() });
			bindRole(role);
			return;
		}

		// 数据库执行逻辑
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				final List<Role> roleList = roleDAO.findByAccountAndServer(account, requestServerId);
				final AtomicBoolean newRole = new AtomicBoolean(false);
				if (roleList.size() == 0) {
					if (!ServerLancher.canCreateNewRole()) {
						__cb.ice_exception(new NoteException(Messages.getString("RoleI.12"))); //$NON-NLS-1$
						return;
					}

					newRole.set(true);
					// 创建新角色
					NewRoleConfig config = new NewRoleConfig();
					config.heroList = new ArrayList<Integer>();
					config.heroList.add(XsgRoleManager.getInstance().getInitHeroT().defaultHero);
					// config.heroList.add(4501);

					config.itemMap = new HashMap<String, Integer>();
					// 普通道具
					config.itemMap.put("med1", XsgGameParamManager //$NON-NLS-1$
							.getInstance().getDefaultJunLing());

					// 阵法道具
					// config.itemMap.put("m001", 1);

					// 装备道具
					// config.itemMap.put("hm0001", 1);

					Role role = XsgRoleManager.getInstance().newRole(account, device.channel,
							XsgRoleManager.getInstance().generateUniqueName(1), config, requestServerId);
					try {
						roleDAO.customMerge(role);
					} catch (Exception e) {
						__cb.ice_exception(new NoteException(Messages.getString("RoleI.14"))); //$NON-NLS-1$
						return;
					}
					roleList.add(role);

					// 记录日志
					StatDao.getFromApplicationContext(ServerLancher.getAc()).save(
							new RoleCreateLog(ServerLancher.getServerId(), account, role.getId(), device.mac,
									device.ip, device.channel));
					// 通知中心服务器
					XsgCenterManager.instance().addRole(account, role.getId(), role.getName());

					// 账号注册 发送消息
					XsgMFBIManager.getInstance().sendCreateRole(role, device);
				}

				// 正常返回
				LogicThread.execute(new Runnable() {

					@Override
					public void run() {
						for (Role db : roleList) {
							IRole roleRt = XsgRoleManager.getInstance().loadRole(db);
							// 设置firstInTime
							if (db.getFirstInTime() <= 0L) {
								if (device != null) {
									db.setFirstInTime(device.firstInTime);
								}
							}
							checkRoleReset(roleRt, db);

							if (newRole.get()) {
								noviceHandler(roleRt);
							}

							roleRt.beforeEnterGame();
							__cb.ice_response(new RoleView[] { roleRt.getView() });
							bindRole(roleRt);
							break; // 暂时只搞一个角色
						}
					}
				});
			}
		});
	}

	/**
	 * 检查是否是重置的角色，是则进行相关处理，改为原名字及跳过开篇剧情
	 * 
	 * @param roleRt
	 * @param db
	 */
	private void checkRoleReset(IRole roleRt, Role db) {
		String account = roleRt.getAccount();
		if (roleResetContextMap.containsKey(account)) {
			// db.setName(roleResetContextMap.get(account));
			roleRt.completeGuide(0);// 开篇剧情
			roleResetContextMap.remove(account);
		}
	}

	/**
	 * 新手处理
	 * 
	 * @param roleRt
	 */
	private void noviceHandler(IRole roleRt) {
		int defaultHeroId = XsgRoleManager.getInstance().getInitHeroT().defaultHero;
		IHero hero = roleRt.getHeroControler().getHero(defaultHeroId);
		if (hero == null) {
			hero = roleRt.getHeroControler().addHero(XsgHeroManager.getInstance().getHeroT(defaultHeroId),
					HeroSource.Init);
		}
		try {
			String defaultFormation = roleRt.getFormationControler().getDefaultFormation().getId();
			roleRt.getFormationControler().setFormationPosition(defaultFormation,
					XsgRoleManager.getInstance().getInitHeroT().defaultHeroPos, hero, true);
		} catch (NoteException e) {
			LogManager.error(e);
		}
	}

	@Override
	public void randomName_async(final AMD_Role_randomName __cb, final int sex, final Current __current) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				String name = XsgRoleManager.getInstance().generateUniqueName(sex);

				__cb.ice_response(new Property(name, roleRt.getRenameYuanbao()));
			}
		});

	}

	@Override
	public void salary(Current __current) throws NoteException {
		try {
			if (!roleRt.addSalary()) {
				throw new NoteException(Messages.getString("RoleI.15")); //$NON-NLS-1$
			}
		} catch (NotEnoughYuanBaoException e) {
			throw new NoteException(Messages.getString("RoleI.16"), e); //$NON-NLS-1$
		}
	}

	@Override
	public String[] getServerOpenTime(Current __current) {
		List<String> list = new ArrayList<String>();
		list.add(DateUtil.toString(GlobalDataManager.getInstance().getServerOpenTime().getTime()));
		list.add(DateUtil.toString(System.currentTimeMillis()));
		return list.toArray(new String[0]);
	}

	@Override
	public void getOtherPlayInfo_async(final AMD_Role_getOtherPlayInfo __cb, final String targetId, Current __current) {
		XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {
			@Override
			public void run() {
				IRole target = XsgRoleManager.getInstance().findRoleById(targetId);
				RoleViewForOtherPlayer view = target.getViewForOtherPlayer();
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		}, new Runnable() {

			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("RoleI.17"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public void setHeadImage(String img, Current __current) throws NoteException {
		if(TextUtil.isBlank(img)
				|| (!Arrays.asList(this.roleRt.getExtHeadImage()).contains(img)
						&& !XsgRoleManager.getInstance().getHeadIconMap().get(this.roleRt.getSex()).contains(img))){
			throw new NoteException(Messages.getString("ShareControler.0"));
		}
		
		this.roleRt.setHeadImage(img);
	}
	
	@Override
	public void setHeadBorder(String border, Current __current) throws NoteException {
		// border=空，取消边框
		if(TextUtil.isBlank(border)){
			this.roleRt.setHeadBorder(border);
			return;
		}

		if(!Arrays.asList(this.roleRt.getExtHeadBorder()).contains(border)){
			throw new NoteException(Messages.getString("ShareControler.0"));
		}
		
		this.roleRt.setHeadBorder(border);
	}

	@Override
	public String getReportView(String reportId, Current __current) throws NoteException {
		DuelReportView report = null;

		return TextUtil.GSON.toJson(report);
	}

	@Override
	public void setSexAndName_async(final AMD_Role_setSexAndName __cb, final int sex, final String name,
			final String inviteCode, Current __current) throws NoteException {
		if (!roleNamePattern.matcher(name).matches()) {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.18"))); //$NON-NLS-1$
			return;
		}
		if (SensitiveWordManager.getInstance().hasSensitiveWord(name)) {
			__cb.ice_exception(new NoteException(Messages.getString("RoleI.19"))); //$NON-NLS-1$
			return;
		}

		XsgRoleManager.getInstance().loadRoleByNameAsync(name, new Runnable() {

			@Override
			public void run() {
				// 数据库存在相关对象时执行逻辑
				__cb.ice_exception(new NoteException(TextUtil.format(Messages.getString("RoleI.20"), name))); //$NON-NLS-1$
			}
		}, new Runnable() {

			@Override
			public void run() {
				// 数据库未找到存在相关对象时执行逻辑
				roleRt.rename(name);
				roleRt.setSex(sex);
				roleRt.setHeadImage(XsgRoleManager.getInstance().randomHeadImage(sex));
				__cb.ice_response(roleRt.getHeadImage());
			}
		});

	}

	@Override
	public void completeGuide(int guideId, Current __current) {
		this.roleRt.completeGuide(guideId);
	}

	@Override
	public SceneDuelView[] openCeremony(int id, Current __current) throws NoteException {
		List<SceneDuelView> list = new ArrayList<SceneDuelView>();

		OpenCeremonyT oct = XsgRoleManager.getInstance().openCeremonyT(id);
		List<StoryEventT> eventList = oct.getDuelEventTList();
		for (StoryEventT event : eventList) {
			DuelUnit first = null;

			if (event.firstId.startsWith("M-")) {// 发起方为怪物 //$NON-NLS-1$
				first = XsgCopyManager.getInstance().createDuelUnitFromMonster(
						XsgCopyManager.getInstance().findMonsterT(event.getFirstMonsterId()));
			} else if (event.firstId.equals("0")) {// 0表示任意玩家单挑武将 //$NON-NLS-1$
				first = this.roleRt.getHeroControler().anyDuelHero().createDuelUnit();
			} else {// 发起方为武将
				first = this.roleRt.getHeroControler().getHero(event.getFirstHeroId()).createDuelUnit();
			}

			DuelUnit second = XsgCopyManager.getInstance().createDuelUnitFromMonster(
					XsgCopyManager.getInstance().findMonsterT(event.monsterId));
			DuelBattle battle = new DuelBattle(first, second);
			DuelReport report = battle.fuckEachOther();
			list.add(new SceneDuelView(1, event.id, new DuelReportView[] { report.generateView() }));
		}

		return list.toArray(new SceneDuelView[0]);
	}

	@Override
	public DuelSkillTemplateView[] getDuelStrategyConfig(Current __current) throws NoteException {
		return XsgHeroManager.getInstance().getDuelStrategyConfig();
	}

	@Override
	public void xsgPing(Current __current) throws NoteException {
	}

	@Override
	public void resetRole_async(final AMD_Role_resetRole __cb, Current __current) throws NoteException {
		final CellWrap context = new CellWrap();
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				context.value = XsgRoleManager.getInstance().generateUniqueName(roleRt.getSex());

			}
		}, new Runnable() {
			@Override
			public void run() {
				// 在改帐号前把连接获取到
				String account = roleRt.getAccount();
				GameSessionI session = GameSessionManagerI.getInstance().findSession(account, roleRt.getRoleId());

				RoleI.saveRoleResetContext(account, roleRt.getName());

				// 先改帐号再断线
				roleRt.setAccount2System();
				roleRt.rename(context.value);
				roleRt.onDeleted();
				if (session != null) {
					session.destroy();
				}

				__cb.ice_response();
			}
		});
	}

	/**
	 * 保存角色重置的上下文状态
	 * 
	 * @param account
	 * @param roleName
	 */
	public static void saveRoleResetContext(String account, String roleName) {
		roleResetContextMap.put(account, roleName);
	}

	@Override
	public ActivityAnnounceView[] getActivityAnnounce(Current __current) {
		roleRt.getRoleOpenedMenu().setOpenAnnounceDate(new Date());
		return this.roleRt.generateAnnounceViewList();
	}

	@Override
	public String getDoubleCardTime(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getDoubleCardTime());
	}

	@Override
	public void shareWeixin(Current __current) throws NoteException {
		RoleWeixinShare shareInfo = roleRt.getRoleWeixinShare();
		if(shareInfo == null)
		{
			shareInfo = roleRt.addRoleWeixinShare(1);
		}else
		{
			shareInfo.setShareNums(shareInfo.getShareNums()+1);
			shareInfo.setLastUpdateTime(new Date());
		}
	}

	@Override
	public void getRoleHeros_async(final AMD_Role_getRoleHeros __cb, final String roleId,
			Current __current) throws NoteException {
		if(!roleRt.getSnsController().isMyFriend(roleId)){
			throw new NoteException(Messages.getString("RoleI.getRoleHeros"));
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				IRole targetRole = XsgRoleManager.getInstance().findRoleById(roleId);
				if (XsgSnsManager.getInstance().NAME_OF_MS_LING.equals(targetRole.getName()) &&
						XsgRoleManager.Robot_Account.equals(targetRole.getAccount())) {
					__cb.ice_exception(new NoteException(Messages.getString("RoleI.nothingCanSee")));
					return;
				}
				if(!targetRole.getSnsController().isMyFriend(roleRt.getRoleId())){
					__cb.ice_exception(new NoteException(Messages.getString("RoleI.getRoleHeros")));
					return;
				}
				__cb.ice_response(targetRole.getHeroControler().getHerosEquips());
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("ChatControler.80")));
			}
		});
	}

}
