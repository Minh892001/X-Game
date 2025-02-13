/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.role;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.expression.Criteria;

import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.AlarmType;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.db.game.HeroSkill;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleFormation;
import com.morefun.XSanGo.db.game.RoleHero;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.db.game.RoleVip;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.XsgHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.FileUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * @author Su LingYun 角色管理类
 * 
 */
public class XsgRoleManager {

	public static final String Robot_Account = "robot.sys";
	private static XsgRoleManager instance = new XsgRoleManager();

	public static XsgRoleManager getInstance() {
		return instance;
	}

	/** 运行时角色对象缓存 */
	private Ehcache iroleCache;
	/** 失败数据缓存 */
	private Ehcache errorCache;
	/** 主公升级经验表 */
	private Map<Integer, Integer> levelupExpMap;
	private Map<Integer, RoleLevelConfigT> roleLevelConfigT;
	/** 等级上限配置 */
	private LevelConfigT levelConfigT;
	private Map<Integer, List<RandomNameT>> randomNameTMap;
	// 角色行动力购买 模板数据
	private Map<Integer, RoleVitBuyT> vitBuyMap = new HashMap<Integer, RoleVitBuyT>();

	/** 按照性别区分的头像 **/
	private Map<Integer, List<String>> headIconMap = new HashMap<Integer, List<String>>();

	/** 头像边框集合 **/
	private Map<Integer, HeadBorderT> headBorderMap = new HashMap<Integer, HeadBorderT>();
	/** 初始武将配置 */
	private InitHeroT initHeroT;
	private Map<Integer, OpenCeremonyT> openCeremonyTMap;
	private RenameConfigT renameT;

	private XsgRoleManager() {
		this.renameT = ExcelParser.parse(RenameConfigT.class).get(0);
		this.openCeremonyTMap = CollectionUtil.toMap(ExcelParser.parse(OpenCeremonyT.class), "id");
		this.initHeroT = ExcelParser.parse(InitHeroT.class).get(0);
		this.randomNameTMap = new HashMap<Integer, List<RandomNameT>>();
		this.randomNameTMap.put(0, ExcelParser.parse("女姓名", RandomNameT.class));
		this.randomNameTMap.put(1, ExcelParser.parse("男姓名", RandomNameT.class));

		this.iroleCache = XsgCacheManager.getInstance().getCache(Const.Role.CACHE_NAME_ROLE);
		this.errorCache = XsgCacheManager.getInstance().getCache(Const.ErrorData_Cache_Name);
		// 主公升级表
		this.levelupExpMap = new HashMap<Integer, Integer>();
		this.roleLevelConfigT = new HashMap<Integer, RoleLevelConfigT>();
		List<RoleLevelConfigT> configTList = ExcelParser.parse("主公升级配置表", RoleLevelConfigT.class);
		for (RoleLevelConfigT config : configTList) {
			this.levelupExpMap.put(config.level, config.exp);
			this.roleLevelConfigT.put(config.level, config);
		}
		// 等级上限控制
		this.levelConfigT = ExcelParser.parse(LevelConfigT.class).get(0);

		// 聊天动作 模板加载
		List<RoleVitBuyT> vitBuyList = ExcelParser.parse(RoleVitBuyT.class);
		for (RoleVitBuyT vitBuy : vitBuyList) {
			vitBuyMap.put(vitBuy.bugNum, vitBuy);
		}

		// 按照性别区分的头像
		List<HeadIconT> headIconList = ExcelParser.parse(HeadIconT.class);
		for (HeadIconT headIcon : headIconList) {
			// 额外获得的头像eg：比武大会头像
			if (headIcon.bornGet == 0) {
				continue;
			}
			List<String> iconStrList = headIconMap.get(headIcon.sex);
			if (iconStrList == null) {
				iconStrList = new ArrayList<String>();
			}
			iconStrList.add(headIcon.Headicon);
			headIconMap.put(headIcon.sex, iconStrList);
		}
		
		// 加载头像边框集合
		List<HeadBorderT> headBorderList = ExcelParser.parse(HeadBorderT.class);
		for(HeadBorderT headBorder : headBorderList) {
			headBorderMap.put(headBorder.Id, headBorder);
		}
	}

	/**
	 * 创建新角色
	 * 
	 * @param account
	 * @param serverId
	 * @param info
	 * @return
	 */
	public Role newRole(String account, int channel, String name, NewRoleConfig config, int serverId) {
		if (TextUtil.isBlank(name)) {
			name = account;
		}

		Date now = new Date(System.currentTimeMillis());
		short zero = 0;

		// 创建主角数据
		String roleId = GlobalDataManager.getInstance().generatePrimaryKey();
		String supportId = this.generateSupportId(roleId, account);
		Role role = new Role(roleId, account, channel, supportId, name, now, zero, XsgVipManager.getInstance()
				.findVipT(0).maxSkillPoint, serverId);
		role.setLevel(config.level);
		// 先随便给个头像
		role.setHeadImage(this.randomHeadImage(NumberUtil.random(100)));

		role.setVip(new RoleVip(role));

		// 增加武将
		if (config.heroList != null) {
			for (int heroId : config.heroList) {
				HeroT heroT = XsgHeroManager.getInstance().getHeroT(heroId);
				role.getRoleHeros().add(
						new RoleHero(GlobalDataManager.getInstance().generatePrimaryKey(), role, heroT.id,
								(byte) QualityColor.Silver.ordinal(), heroT.star));
			}
		}

		// 硬编码增加两套阵型
		role.getRoleFormations().add(
				new RoleFormation(GlobalDataManager.getInstance().generatePrimaryKey(), role, (byte) 0, "{}"));
		role.getRoleFormations().add(
				new RoleFormation(GlobalDataManager.getInstance().generatePrimaryKey(), role, (byte) 1, "{}"));

		if (config.itemMap != null) {
			for (String itemId : config.itemMap.keySet()) {
				this.addRoleItem(role, itemId, config.itemMap.get(itemId));
			}
		}

		return role;
	}

	/**
	 * @param roleId
	 * @param account
	 * @return
	 */
	private String generateSupportId(String roleId, String account) {
		int id = NumberUtil.parseInt(roleId.substring(roleId.lastIndexOf("-") + 1));
		String[] tempArray = account.split("\\.");
		return tempArray[tempArray.length - 1].toUpperCase() + id;
	}

	private void addRoleItem(Role role, String code, int num) {
		RoleItem ri = new RoleItem(GlobalDataManager.getInstance().generatePrimaryKey(), role, code, num);
		role.getItems().put(ri.getId(), ri);
	}

	/**
	 * 根据角色持久化数据加载运行时对象，如在此之前已加载，则返回内存对象
	 * 
	 * @param role
	 * @return
	 */
	public IRole loadRole(Role role) {
		return this.loadRole(role, true);

	}

	/**
	 * 根据角色持久化数据加载运行时对象，如在此之前已加载，则返回内存对象
	 * 
	 * @param role
	 * @param cache
	 *            是否加入缓存管理
	 * @return
	 */
	public synchronized IRole loadRole(Role role, boolean cache) {
		IRole result = this.findRoleById(role.getId());
		if (result != null) {
			return result;
		}

		// 创建角色对象
		try {
			result = new XsgRole(role);
			// 存入列表
			if (cache) {
				if (this.iroleCache.get(role.getId()) != null) {
					LogManager.warn(TextUtil.format("[{0},{1},{2}]重复加载。", role.getAccount(), role.getId(),
							role.getName()));
				}
				this.addRole(result);
			}
		} catch (Exception e) {
			LogManager
					.warn(TextUtil.format("[{0},{1},{2}] load fail.", role.getAccount(), role.getId(), role.getName()));
			LogManager.error(e);
		}

		return result;
	}

	/**
	 * 通过id查找角色，仅搜索内存数据，不涉及DB数据
	 * 
	 * @param id
	 * @return
	 */
	public IRole findRoleById(String id) {
		Element element = iroleCache.get(id);
		if (element == null) {// 常规缓存里面找不到，需要扫描失败数据缓存
			element = this.errorCache.get(id);
			if (element != null) {
				Role role = (Role) element.getObjectValue();
				this.errorCache.remove(id);
				return this.loadRole(role);
			}
		}

		return element != null ? (IRole) element.getObjectValue() : null;
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void removeRole(String id) {
		IRole role = this.findRoleById(id);
		if (role != null) {
			iroleCache.remove(id);
			role.destory();
		}
	}

	/**
	 * 保存角色数据
	 * 
	 * @param role
	 */
	public void saveRole(final Role role) {
		RoleDAO dao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());

		try {
			dao.customMerge(role);
			this.errorCache.remove(role.getId());
		} catch (Exception e) {
			String name = role.getName();
			LogFactory.getLog(getClass()).error(TextUtil.format("[{0}]保存失败!!!!", name));
			try {
				this.errorCache.put(new Element(role.getId(), role));
				LogFactory.getLog(getClass()).info(TextUtil.format("[{0}]备份数据成功。", name));
			} catch (Exception e2) {
				LogManager.error(e2);
				// X三国发生系统故障，故障类型[]，关键字[]
				AlarmType errorType = AlarmType.SaveError;
				XsgCenterManager.instance().sendAlarmSMS(
						errorType,
						TextUtil.format("X三国发生系统故障，故障类型[{0}]，关键字[{1},{2}]", errorType.toString(), role.getId(),
								role.getName()));

				this.saveRole2File(role);
			}
		}
	}

	/**
	 * 保存玩家数据到磁盘
	 * 
	 * @param role
	 */
	public void saveRole2File(Role role) {
		String path = TextUtil.format("{0}/error_role/{1}", XsgCacheManager.getInstance().getDiskStorePath(),
				DateUtil.toString(System.currentTimeMillis(), "yyyy-MM-dd"));
		this.checkDirectory(path);
		String current = DateUtil.toString(System.currentTimeMillis(), "yyyyMMddHHmmss");
		FileUtil.saveFile(TextUtil.format("{0}/{1}_{2}.role", path, role.getId(), current),
				TextUtil.objectToBytes(role));
	}

	/**
	 * 检查路径是否存在，不存在则创建，该方法是线程同步的
	 * 
	 * @param path
	 */
	private synchronized void checkDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	public void loadRoleByIdAsync(final String id, final Runnable suc, final Runnable fail) {
		// 如果内存里面有该角色,直接获取该角色
		IRole newRole = XsgRoleManager.getInstance().findRoleById(id);
		if (newRole != null) {
			LogicThread.execute(suc);
			return;
		}

		// 内存里面没有该角色,从数据库里面获取
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Role role = RoleDAO.getFromApplicationContext(ServerLancher.getAc()).findById(id);
					RoleLoader loader = new RoleLoader(role, suc, fail);
					loader.run();
				} catch (Exception e) {
					LogManager.error(e);
					LogicThread.execute(fail);
					return;
				}
			}
		});

	}

	/**
	 * @param iRole
	 */
	public void addRole(IRole role) {
		if (role != null) {
			this.iroleCache.put(new Element(role.getRoleId(), role));
		}
	}

	/**
	 * 异步批量查询角色，在回调函数调用时，并不保证请求的所有角色都存在，因为这受到IO，数据完整性，缓存容量等因素限制
	 * 
	 * @param waitLoadIDs
	 * @param logicCallback
	 */
	public void loadRoleAsync(final List<String> waitLoadIDs, final Runnable logicCallback) {
		List<String> realList = new ArrayList<String>();
		for (String id : waitLoadIDs) {
			if (this.findRoleById(id) == null && !realList.contains(id)) {
				realList.add(id);
			}
		}

		if (realList.size() > 200) {
			LoggerFactory.getLogger(getClass()).warn(
					TextUtil.format(">>>>>>>>>>>>>Too many roles loading at the same time.{0}", realList.size()));
		}
		BatchSelectCounter counter = new BatchSelectCounter(realList.size(), logicCallback);
		for (String id : realList) {
			this.loadRoleByIdAsync(id, counter, counter);
		}
	}

	/**
	 * 查找聊天消息相关的接受者列表
	 * 
	 * @param sender
	 * @param channel
	 * @return
	 */
	public List<IRole> findChatAcceptorList(IRole sender, ChatChannel channel) {
		Query query = this.iroleCache.createQuery().includeValues();
		Criteria criteria = this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ONLINE).eq(true);// 必须在线才能接收
		switch (channel) {
		case Faction:
			String factionId = sender.getFactionControler().getFactionId();
			criteria = criteria.and(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_FACTIONID).eq(factionId));
			break;
		case Private:
			throw new IllegalStateException();
		default:
			break;
		}

		List<Result> queryResult = query.addCriteria(criteria).execute().all();
		List<IRole> roleList = XsgCacheManager.parseCacheValue(queryResult, IRole.class);

		return roleList;
	}
	
	/**
	 * 查找指定公会的在线角色列表
	 * 
	 * @param factionId
	 * @return 
	 */
	public List<IRole> findChatAcceptorList2Faction(String factionId) {
		Query query = this.iroleCache.createQuery().includeValues();
		Criteria criteria = this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ONLINE).eq(true);// 必须在线才能接收
		criteria = criteria.and(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_FACTIONID).eq(factionId));

		List<Result> queryResult = query.addCriteria(criteria).execute().all();
		List<IRole> roleList = XsgCacheManager.parseCacheValue(queryResult, IRole.class);
		return roleList;
	}

	/**
	 * @param name
	 * @return
	 */
	public List<IRole> findRolesLikeName(String name) {
		Query query = this.iroleCache
				.createQuery()
				.includeValues()
				.addCriteria(
						this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ROLENAME).ilike(
								String.format("*%s*", name)));
		return XsgCacheManager.parseCacheValue(query.execute().all(), IRole.class);
	}

	public IRole findRoleByName(String name) {
		Query query = this.iroleCache.createQuery().includeValues()
				.addCriteria(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ROLENAME).eq(name));

		List<IRole> list = XsgCacheManager.parseCacheValue(query.execute().all(), IRole.class);
		if (list.size() > 0) {
			return list.get(0);
		}

		// 常规缓存里面找不到，需要扫描失败数据缓存
		query = this.errorCache.createQuery().includeValues()
				.addCriteria(this.errorCache.getSearchAttribute(Const.Role.CACHE_INDEX_ROLENAME).eq(name));
		List<Role> dbList = XsgCacheManager.parseCacheValue(query.execute().all(), Role.class);

		return (dbList.size() > 0) ? this.loadRole(dbList.get(0)) : null;
	}

	/**
	 * 该函数会查询数据库,导致调用线程阻塞,慎重使用
	 * 
	 * @param roleId
	 */
	public void loadRoleById(String roleId) {
		Role role = RoleDAO.getFromApplicationContext(ServerLancher.getAc()).findById(roleId);
		// 角色不存在或已被删除
		if (role == null) {
			return;
		}

		// 加载角色数据
		this.loadRole(role);
	}

	public void loadRoleByNameAsync(final String roleName, final Runnable suc, final Runnable fail) {

		// 如果内存里面有该角色,直接获取该角色
		IRole newRole = XsgRoleManager.getInstance().findRoleByName(roleName);
		if (newRole != null) {
			LogicThread.execute(suc);
			return;
		}

		// 内存里面没有该角色,从数据库里面获取
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Role role = RoleDAO.getFromApplicationContext(ServerLancher.getAc()).findByName(roleName);
					RoleLoader loader = new RoleLoader(role, suc, fail);
					loader.run();
				} catch (Exception e) {
					LogManager.error(e);
					LogicThread.execute(fail);
					return;
				}
			}
		});

	}

	/**
	 * 查找等级在min和max之间的玩家
	 */
	public List<IRole> findPlayersBetweenLevel(int min, int max) {
		Query query = this.iroleCache.createQuery().includeValues();
		Criteria criteria = this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_LEVEL).between(min, max, true,
				true);

		List<Result> queryResult = query.addCriteria(criteria).execute().all();
		List<IRole> roleList = XsgCacheManager.parseCacheValue(queryResult, IRole.class);

		return roleList;
	}

	/**
	 * 根据等级 排序
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public List<IRole> findPlayersBetweenLevelOrder(int min, int max, Direction dir) {
		Query query = this.iroleCache.createQuery().includeValues();
		Criteria criteria = this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_LEVEL).between(min, max, true,
				true);

		List<Result> queryResult = query.addCriteria(criteria)
				.addOrderBy(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_LEVEL), dir).execute().all();
		List<IRole> roleList = XsgCacheManager.parseCacheValue(queryResult, IRole.class);

		return roleList;
	}

	/**
	 * 获取所有在线玩家列表
	 * 
	 * @return
	 */
	public List<IRole> findOnlineList() {
		Query query = this.iroleCache.createQuery().includeValues();
		Criteria criteria = this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ONLINE).eq(true);// 必须在线才能接收

		List<Result> queryResult = query.addCriteria(criteria).execute().all();
		List<IRole> roleList = XsgCacheManager.parseCacheValue(queryResult, IRole.class);

		return roleList;

	}

	/**
	 * 获取内存中玩家的数据库对象，如玩家不存在于内存中，则返回null
	 * 
	 * @param roleId
	 * @return
	 */
	public Role getRoleDbFromMemory(String roleId) {
		XsgRole rt = (XsgRole) this.findRoleById(roleId);
		if (rt == null) {
			return null;
		}

		try {
			Field field = rt.getClass().getDeclaredField("db");
			field.setAccessible(true);
			return (Role) field.get(rt);
		} catch (Exception e) {
			LogManager.error(e);
			return null;
		}
	}

	public RoleLevelConfigT getRoleLevelConfigT(int currentLevel) {
		if (!this.roleLevelConfigT.containsKey(currentLevel)) {
			throw new IllegalArgumentException();
		}

		return this.roleLevelConfigT.get(currentLevel);
	}

	/**
	 * 获取自动升级的等级限制
	 * 
	 * @return
	 */
	public int getAutoLevelUpLimit() {
		return // Math.min(this.levelConfigT.autoLevelUpLimit,
		this.getCurrentLevelLimit()
		// )
		;
	}

	/**
	 * 获取主公升级所需经验
	 * 
	 * @param currentLevel
	 *            当前等级
	 * @return
	 */
	public int getLevelUpExp(int currentLevel) {
		int nextLevel = currentLevel + 1;
		if (!this.levelupExpMap.containsKey(nextLevel)) {
			throw new IllegalArgumentException();
		}

		return this.levelupExpMap.get(nextLevel);
	}

	/**
	 * 获取当前开放的等级上限
	 * 
	 * @return
	 */
	public int getCurrentLevelLimit() {
		int result = this.levelConfigT.levelLimitWhenNewServer;
		int open = GlobalDataManager.getInstance().getServerOpenDay();
		for (IntIntPair pair : this.levelConfigT.getDailyOpens()) {
			open -= pair.second;
			if (open < 0) {
				break;
			}

			result = pair.first;
		}

		return Math.min(result, this.levelConfigT.levelLimit);
	}

	/**
	 * 生成随机名字
	 * 
	 * @param sex
	 * 
	 * @return
	 */
	public String generateRandomName(int sex) {
		sex %= 2;
		String result = "";
		List<RandomNameT> randomNameTList = this.randomNameTMap.get(sex);
		int max = randomNameTList.size();
		for (int i = 0; i < 3; i++) {
			result += randomNameTList.get(NumberUtil.random(max)).getValue(i).trim();
		}

		return result;
	}

	public IRole findRoleByAccount(String account, int serverId) {
		Query query = this.iroleCache.createQuery().includeValues()
				.addCriteria(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_Account).eq(account))
				.addCriteria(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_ServerId).eq(serverId));
		List<IRole> list = XsgCacheManager.parseCacheValue(query.execute().all(), IRole.class);
		if (list.size() > 0) {
			return list.get(0);
		}

		// 常规缓存里面找不到，需要扫描失败数据缓存
		query = this.errorCache.createQuery().includeValues()
				.addCriteria(this.errorCache.getSearchAttribute(Const.Role.CACHE_INDEX_Account).eq(account))
				.addCriteria(this.errorCache.getSearchAttribute(Const.Role.CACHE_INDEX_ServerId).eq(serverId));
		List<Role> dbList = XsgCacheManager.parseCacheValue(query.execute().all(), Role.class);

		return (dbList.size() > 0) ? this.loadRole(dbList.get(0)) : null;
	}

	public boolean isRobotAccount(String account) {
		return Robot_Account.equals(account);
	}

	public Map<Integer, RoleVitBuyT> getVitBuyMap() {
		return vitBuyMap;
	}

	public void setVitBuyMap(Map<Integer, RoleVitBuyT> vitBuyMap) {
		this.vitBuyMap = vitBuyMap;
	}

	public IntString[] getPlayerSkillConfig() {
		return new IntString[0];
	}

	public InitHeroT getInitHeroT() {
		return this.initHeroT;
	}

	public OpenCeremonyT openCeremonyT(int id) {
		return this.openCeremonyTMap.get(id);
	}

	public RenameConfigT getRenameConfigT() {
		return this.renameT;
	}

	/**
	 * 根据性别随机取头像
	 * 
	 * @param sex
	 * @return
	 */
	public String randomHeadImage(int sex) {
		sex %= 2;
		List<String> list = this.headIconMap.get(sex);
		if (list == null || list.size() == 0) {
			return "";
		}

		return list.get(NumberUtil.random(list.size()));
	}
	
	/**获取头像集合*/
	public Map<Integer, List<String>> getHeadIconMap() {
		return headIconMap;
	}
	
	/**获取头像框集合*/
	public Map<Integer, HeadBorderT> getHeadBorderMap() {
		return headBorderMap;
	}

	/**
	 * 根据传入等级获取其累积经验的上限
	 * 
	 * @param level
	 * @return
	 */
	public int getExpLimit(int level) {
		int lvlLimit = this.getCurrentLevelLimit();
		if (level >= lvlLimit) {
			return this.getLevelUpExp(lvlLimit) - 1;
		}

		int result = 0;
		for (int i = level; i < lvlLimit; i++) {
			result += this.getLevelUpExp(i);
		}

		return result;
	}

	public void allHeroAndFullLevel(XsgRole role) {
		Field field;
		Role db;
		try {
			field = role.getClass().getDeclaredField("db");
			field.setAccessible(true);
			db = (Role) field.get(role);
			db.setLevel(90);
			db.setHeroSkillPoint(5000);
		} catch (Exception e) {
			LogManager.error(e);
			return;
		}

		for (final HeroT h : XsgHeroManager.getInstance().getAllHeroT().values()) {
			if (h.open == 0) {
				continue;
			}
			XsgHero hero = (XsgHero) role.getHeroControler().getHero(h.id);
			if (hero == null) {
				hero = (XsgHero) role.getHeroControler().addHero(XsgHeroManager.getInstance().getHeroT(h.id),
						HeroSource.Gift);
			}

			hero.setLevel(180);
			hero.setStar(5);
			hero.setColor(10);
			RoleHero rh = CollectionUtil.first(db.getRoleHeros(), new IPredicate<RoleHero>() {
				@Override
				public boolean check(RoleHero item) {
					return item.getTemplateId() == h.id;
				}
			});
			for (HeroSkill hs : rh.getHeroSkills().values()) {
				hs.setLevel(141);
			}
		}
	}

	/**
	 * 生成唯一名字，如果重复则重新生成，理论上存在死循环可能，且通过数据库连接检查，IO耗时运算
	 * 
	 * @param sex
	 * 
	 * @return
	 */
	public String generateUniqueName(int sex) {
		String name = XsgRoleManager.getInstance().generateRandomName(sex);
		RoleDAO roleDAO = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		while (roleDAO.findByName(name) != null) {
			name = XsgRoleManager.getInstance().generateRandomName(sex);
		}

		return name;
	}

	/**
	 * 在线人数是否过载
	 * 
	 * @return
	 */
	public boolean isOnlineOverload() {
		int current = this.findOnlineList().size();
		long max = this.iroleCache.getCacheConfiguration().getMaxEntriesLocalHeap();
		return current * 100 / max > 80;
	}

	/**
	 * 根据IP和端口的连接信息查找角色
	 * 
	 * @param remoteAddress
	 * @return
	 */
	public List<IRole> findRoleByRemoteAddress(String remoteAddress) {
		Query query = this.iroleCache
				.createQuery()
				.includeValues()
				.addCriteria(this.iroleCache.getSearchAttribute(Const.Role.CACHE_INDEX_RemoteAddress).eq(remoteAddress));
		List<IRole> list = XsgCacheManager.parseCacheValue(query.execute().all(), IRole.class);

		return list;
	}
	
}

class RoleLoader implements Runnable {

	private Role role;
	private Runnable suc;
	private Runnable fail;

	/**
	 * @param role
	 * @param suc
	 * @param fail
	 */
	public RoleLoader(Role role, Runnable suc, Runnable fail) {
		this.role = role;
		this.suc = suc;
		this.fail = fail;
	}

	@Override
	public void run() {
		// 角色不存在
		if (this.role == null) {
			LogicThread.execute(fail);
			return;
		}

		// 加载角色数据
		IRole rt = XsgRoleManager.getInstance().loadRole(this.role);
		LogicThread.execute(rt == null ? fail : suc);
	}
}

class BatchSelectCounter implements Runnable {
	private AtomicInteger count = new AtomicInteger(0);
	private int targetCount;
	private Runnable executeOnComplete;

	public BatchSelectCounter(int targetCount, Runnable executeOnComplete) {
		this.targetCount = targetCount;
		this.executeOnComplete = executeOnComplete;
		if (this.targetCount < 1) {
			this.run();
		}
	}

	@Override
	public void run() {
		int current = this.count.incrementAndGet();
		if (current >= this.targetCount) {
			if (this.executeOnComplete != null) {
				LogicThread.execute(executeOnComplete);
			}
		}
	}
}