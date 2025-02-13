package com.morefun.XSanGo.collect;

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

import com.XSanGo.Protocol.CollectHeroSoulResView;
import com.XSanGo.Protocol.CollectHeroSoulView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.collect.CollectHeroSoulEntity.CollectConsumeData;
import com.morefun.XSanGo.collect.CollectHeroSoulEntity.HeroSoulData;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleCollectHeroSoul;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.ICollectHeroSoulDoCollect;
import com.morefun.XSanGo.event.protocol.ICollectHeroSoulDoRefresh;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.IItemControler;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 */
@RedPoint(isTimer = true)
public class CollectHeroSoulController implements ICollectHeroSoulController {
	private final static Log logger = LogFactory.getLog(CollectHeroSoulController.class);

	private IRole role;
	private Role roleDB;

	private Map<Integer, CollectHeroSoulEntity> collectHeroSoulEntitys = new HashMap<Integer, CollectHeroSoulEntity>();

	private ICollectHeroSoulDoCollect doCollect;
	private ICollectHeroSoulDoRefresh doRefresh;
	// 是否发送过红点
//	private boolean isSendRedPoint = false;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public CollectHeroSoulController(IRole role, Role roleDB) {
		this.role = role;
		this.roleDB = roleDB;
		List<RoleCollectHeroSoul> soulList = roleDB.getCollectHeroSoulList();
		if (soulList != null) {
			Map<Integer, CollectHeroSoulEntity> entityMap = new HashMap<Integer, CollectHeroSoulEntity>();
			for (RoleCollectHeroSoul soul : soulList) {
				CollectHeroSoulEntity entity = CollectHeroSoulEntity.fromCollectHeroSoul(soul);
				entityMap.put(entity.getType(), entity);
			}
			collectHeroSoulEntitys = entityMap;
		}
		if (collectHeroSoulEntitys.size() <= 0) {
			initCollectHeroSoulData();
			setToRole();
		}

		IEventControler evtControler = this.role.getEventControler();
		doCollect = evtControler.registerEvent(ICollectHeroSoulDoCollect.class);
		doRefresh = evtControler.registerEvent(ICollectHeroSoulDoRefresh.class);

		firstOpen = true;
	}

	/** 第一次进入该模块，初始化数据 */
	private void initCollectHeroSoulData() {
		CollectHeroSoulEntity[] entitys = XsgCollectHeroSoulManager.getInstance().generateHeroSouls(role);
		if (entitys == null || entitys.length <= 0) {
			return;
		}
		int count = entitys.length;
		for (int i = 0; i < count; i++) {
			CollectHeroSoulEntity entity = entitys[i];
			collectHeroSoulEntitys.put(entity.getType(), entity);
		}
	}

	/** 刷新将魂 */
	private void refreshIfNeeded(CollectHeroSoulEntity entity) throws NoteException {
		if (entity != null && needRefresh(entity)) { // 需要刷新
			boolean res = XsgCollectHeroSoulManager.getInstance().refreshHeroSoul(entity, role, true);
			if (!res) {
				throw new NoteException(Messages.getString("CollectHeroSoulController.0")); //$NON-NLS-1$
			}
			entity.setLastRefreshTime(Calendar.getInstance().getTime());
			setToRole();
		}
	}
	
	/**
	 * 每日次数刷新
	 * */
	private void dayRefresh(CollectHeroSoulEntity entity) {
		Date checkDate = DateUtil.joinTime("00:00:00");
		if (entity.getLastDayRefreshTime() == null || 
				DateUtil.isPass(checkDate, entity.getLastDayRefreshTime())) {
			entity.setDayGoldCollectCount(0);
			entity.setLastDayRefreshTime(Calendar.getInstance().getTime());
		}
	}

	@Override
	public CollectHeroSoulView[] reqCollectHeroSoul() throws NoteException {
		if (collectHeroSoulEntitys == null || collectHeroSoulEntitys.size() <= 0) {
			throw new NoteException(Messages.getString("CollectHeroSoulController.1")); //$NON-NLS-1$
		}
		List<CollectHeroSoulView> viewList = new ArrayList<CollectHeroSoulView>(collectHeroSoulEntitys.size());
		for (Map.Entry<Integer, CollectHeroSoulEntity> entry : collectHeroSoulEntitys.entrySet()) {
			CollectHeroSoulEntity entity = entry.getValue();
			refreshIfNeeded(entity);
			dayRefresh(entity);
			viewList.add(entity.getView());
		}
		Collections.sort(viewList, new Comparator<CollectHeroSoulView>() {
			@Override
			public int compare(CollectHeroSoulView o1, CollectHeroSoulView o2) {
				return o2.type - o1.type; // 按照type倒序
			}
		});
		CollectHeroSoulView[] views = viewList.toArray(new CollectHeroSoulView[0]);
		return views;
	}

	@Override
	public CollectHeroSoulResView doCollectHeroSoul(int cType, int consumeType) throws NoteException,
			NotEnoughMoneyException, NotEnoughYuanBaoException {
		CollectHeroSoulEntity entity = collectHeroSoulEntitys.get(cType);
		if (entity == null) {
			throw new NoteException(Messages.getString("CollectHeroSoulController.2")); //$NON-NLS-1$
		}
		dayRefresh(entity);
		if (!canDoCollect(entity, consumeType)) {
			throw new NoteException(Messages.getString("CollectHeroSoulController.3")); //$NON-NLS-1$
		}
		if (cType == CollectHeroSoulT.TIMELIMIT_HERO && !XsgCollectHeroSoulManager.getInstance().isGoingOn(cType)) {
			throw new NoteException(Messages.getString("CollectHeroSoulController.4")); //$NON-NLS-1$
		}

		int money = entity.getMoney(consumeType);
		switch (consumeType) {
		case CollectHeroSoulT.GOLD:
			if (this.role.getJinbi() < money) {
				throw new NoteException(Messages.getString("CollectHeroSoulController.5")); //$NON-NLS-1$
			}
			this.role.reduceCurrency(new Money(CurrencyType.Jinbi, money));
			break;
		case CollectHeroSoulT.RMBY:
			if (this.role.getTotalYuanbao() < money) {
				throw new NoteException(Messages.getString("CollectHeroSoulController.6")); //$NON-NLS-1$
			}
			this.role.reduceCurrency(new Money(CurrencyType.Yuanbao, money));
			// 增加今日金币次数
			entity.setDayGoldCollectCount(entity.getDayGoldCollectCount() + 1);
			break;
		case CollectHeroSoulT.WINE:
			final String WINE_TEMPLATE_ID = "wine"; // 美酒的模版id获取 //$NON-NLS-1$
			if (this.role.getItemControler().getItemCountInPackage(WINE_TEMPLATE_ID) < money) {
				throw new NoteException(Messages.getString("CollectHeroSoulController.8")); //$NON-NLS-1$
			}
			this.role.getItemControler().changeItemByTemplateCode(WINE_TEMPLATE_ID, -money);
			break;
		default:
			break;
		}
		// 获取一个随机的将魂
		String resultHeroId = getRandomHeroSoul(entity, consumeType);
		IItemControler itemControler = this.role.getItemControler();
		IItem item = null;
		// 随机将魂的个数
		int count = XsgCollectHeroSoulManager.getInstance().getRandomNumCount();
		// 增加将魂到玩家装备库
		if (!TextUtil.isBlank(resultHeroId)) {
			item = itemControler.changeItemByTemplateCode(resultHeroId, count).get(0);
		}
		// 减少剩余次数
		entity.getConsumeData(consumeType).count--;
		// 增加转盘次数
		entity.setDoCollectCount(entity.getDoCollectCount() + 1);
		setToRole();
		// 事件通知
		if (!TextUtil.isBlank(resultHeroId)) {
			this.doCollect.onDoCollectHeroSoul(cType, consumeType, money, resultHeroId);
		}
		// 发送广播
		if (needBroadcast(entity, resultHeroId) && item != null) {
			XsgChatManager chatManager = XsgChatManager.getInstance();
			// 5 为公告配置（聊天公告走马灯.xls-走马灯）中名将召唤的类型id
			List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.soul);
			if (adTList != null && adTList.size() > 0) {
				ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
				if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
					chatManager.sendAnnouncementItem(item,
							role.getChatControler().parseAdContentItem(item.getTemplate(), chatAdT.content));
				}
			}
		}

		return new CollectHeroSoulResView(resultHeroId, count, entity.getMoney(CollectHeroSoulT.RMBY));
	}

	@Override
	public CollectHeroSoulView doRefresh(int cType) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		CollectHeroSoulView view = null;
		switch (cType) {
		case CollectHeroSoulT.NORMAL_HERO:
			CollectHeroSoulEntity entity = collectHeroSoulEntitys.get(cType);
			// 减刷新需要的元宝
			this.role.reduceCurrency(new Money(CurrencyType.Yuanbao, entity.getRefreshMoney()));
			boolean res = XsgCollectHeroSoulManager.getInstance().refreshHeroSoul(entity, role, false);
			if (!res) {
				throw new NoteException(Messages.getString("CollectHeroSoulController.9")); //$NON-NLS-1$
			}
			view = entity.getView();
			setToRole();
			// 通知事件
			this.doRefresh.onDoRefresh(cType, entity.getRefreshMoney());
			break;
		case CollectHeroSoulT.TIMELIMIT_HERO:
			throw new NoteException(Messages.getString("CollectHeroSoulController.10")); //$NON-NLS-1$
		default:
			throw new NoteException(Messages.getString("CollectHeroSoulController.11")); //$NON-NLS-1$
		}
		return view;
	}

	/** 从给定的参数中随机抽取一个将魂 */
	private String doRandomIn(RandomHeroSoul... heros) {
		List<RandomHeroSoul> randomHeroList = new ArrayList<RandomHeroSoul>();
		for (RandomHeroSoul hero : heros) {
			randomHeroList.add(hero);
		}
		RandomRange<RandomHeroSoul> randomHero = new RandomRange<RandomHeroSoul>(randomHeroList);
		RandomHeroSoul resHero = randomHero.random();
		String id = null;
		if (resHero != null) {
			id = resHero.id;
		}
		return id;
	}

	/** 随机返回一个将魂的模版id */
	private String getRandomHeroSoul(CollectHeroSoulEntity entity, int consumeType) {
		CollectConsumeData consume = entity.getConsumeData(consumeType);
		if (consume == null) {
			return null;
		}
		int doCollectCount = entity.getDoCollectCount() + 1;
		HeroSoulData specialHeroData = entity.getSpecialHeroData();
		List<HeroSoulData> normalHeroData = entity.getNormalHeroData();
		String heroId = null;
		int randomMax = XsgCollectHeroSoulManager.getInstance().getRandomMax(entity.getType(), consumeType);
		int randomMin = XsgCollectHeroSoulManager.getInstance().getRandomMin(entity.getType(), consumeType);
		if (doCollectCount >= randomMin) { // 开始计算特殊将魂掉落
			int restRank = 100 - consume.specialRank;
			consume.specialRank += 10; // 每次增加 10% 的概率
			logger.info(Messages.getString("CollectHeroSoulController.12") + consume.specialRank //$NON-NLS-1$
					+ ": " + doCollectCount + "  ---  " + randomMin //$NON-NLS-1$ //$NON-NLS-2$
					+ ", " + randomMax); //$NON-NLS-1$
			heroId = doRandomIn(new RandomHeroSoul(specialHeroData.heroSoulTemplateId, consume.specialRank),
					new RandomHeroSoul("", restRank)); //$NON-NLS-1$
		}
		if (doCollectCount >= randomMax && TextUtil.isBlank(heroId)) { // 转盘次数达到最大值，直接掉特殊
			heroId = specialHeroData.heroSoulTemplateId;
		}
		// 随机到了特殊的将魂，将掉率和次数置零
		if (!TextUtil.isBlank(heroId)) {
			consume.specialRank = XsgCollectHeroSoulManager.getInstance().getSpecialRank(entity.getType(), consumeType);
			entity.setDoCollectCount(-1);
		}
		if (TextUtil.isBlank(heroId)) { // 没有得到特殊将魂，计算普通掉落
			RandomHeroSoul[] randoms = new RandomHeroSoul[3];
			int[] normalRandomRank = XsgCollectHeroSoulManager.getInstance().getNormalRank(entity.getType(),
					consumeType);
			for (int i = 0; i < 3; i++) {
				randoms[i] = new RandomHeroSoul(normalHeroData.get(i).heroSoulTemplateId, normalRandomRank[i]);
			}
			heroId = doRandomIn(randoms);
		}
		return heroId;
	}

	/** 是否需要发公告 */
	private boolean needBroadcast(CollectHeroSoulEntity entity, String id) {
		if (entity.getSpecialHeroData().heroSoulTemplateId.equals(id)) {
			return entity.getSpecialHeroData().notice;
		}
		for (HeroSoulData data : entity.getNormalHeroData()) {
			if (data.heroSoulTemplateId.equals(id)) {
				return data.notice;
			}
		}
		return false;
	}

	/** 是否达到限制次数 */
	private boolean canDoCollect(CollectHeroSoulEntity entity, int consumeType) {
		CollectConsumeData consume = entity.getConsumeData(consumeType);
		if (consume == null) {
			return false;
		}
		if (consume.count < 0) { // 不限制次数
			return true;
		}
		if (consume.count > 0) { // 还有剩余次数
			return true;
		}
		// count 等于零表示次数用完
		return false;
	}

	/**
	 * 返回今天某个时间点
	 * 
	 * @param time
	 *            , 表示时间的字符串，冒号分割时分秒
	 **/
	private Date timeOfToday(String time) {
		return DateUtil.joinTime(time);
	}

	/** 是否达到刷新时间 */
	private boolean needRefresh(CollectHeroSoulEntity entity) {
		XsgCollectHeroSoulManager manager = XsgCollectHeroSoulManager.getInstance();
		CollectHeroSoulT soutT = manager.getCollectHeroSoulT(entity.getType(), 0);
		// 有将魂为空, 强制刷新
		if (manager.needInit(entity, soutT)) {
			return true;
		}
		CollectTimeSettingT setting = manager.getTimeSetting(entity.getType());

		long refreshTime = Long.MAX_VALUE; // 刷新时间
		long currentTime = System.currentTimeMillis(); // 当前时间
		long lastRefreshTime = entity.getLastRefreshTime().getTime(); // 上次刷新时间
		switch (entity.getType()) {
		// 限时活动以开始时间作为刷新时间
		case CollectHeroSoulT.TIMELIMIT_HERO:
			String timeLimitStartTimeStr = setting.timelimitStart;
			if (TextUtil.isBlank(timeLimitStartTimeStr)) {
				return false;
			}
			String timeLimitRefreshTimeStr = setting.resetTime;
			if (TextUtil.isBlank(timeLimitRefreshTimeStr)) {
				return false;
			}
			long startTime = DateUtil.parseDate(timeLimitStartTimeStr).getTime();
			long tempRefreshTime = timeOfToday(timeLimitRefreshTimeStr).getTime();
			if (lastRefreshTime < startTime) { // 活动开始的时候没有刷新过
				refreshTime = startTime;
			} else { // 按每日重置时间进行刷新
				refreshTime = tempRefreshTime;
			}
			break;
		// 在野名将以重置时间作为刷新时间，每天重置
		case CollectHeroSoulT.NORMAL_HERO:
			String normalRefreshTimeStr = setting.resetTime;
			if (TextUtil.isBlank(normalRefreshTimeStr)) {
				return false;
			}
			refreshTime = timeOfToday(normalRefreshTimeStr).getTime(); // 刷新时间
			break;
		}
		long timeIntervalCurrentToRefresh = currentTime - refreshTime; // 当前时间到刷新时间的时间间隔
		long timeIntervalLastRefreshToRefresh = lastRefreshTime - refreshTime; // 上次刷新时间到刷新时间的时间间隔
		// 上次刷新时间在刷新时间之前，并且现在已经过了刷新时间
		if (timeIntervalLastRefreshToRefresh < 0L && timeIntervalCurrentToRefresh > 0L) {
			return true;
		}
		return false;
	}

	/** 保存数据到Role对象，后台线程会保存到数据库 */
	private void setToRole() {
		List<RoleCollectHeroSoul> soulList = new ArrayList<RoleCollectHeroSoul>(2);
		for (Map.Entry<Integer, CollectHeroSoulEntity> entry : collectHeroSoulEntitys.entrySet()) {
			soulList.add(entry.getValue().toCollectHeroSoul(roleDB));
		}
		roleDB.setCollectHeroSoulList(soulList);
	}

	private static class RandomHeroSoul implements IRandomHitable {
		private String id;
		private int rank;

		public RandomHeroSoul(String id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	// 判断某个消费类型是否需要显示红点
	private boolean showRedPoint(CollectHeroSoulEntity entity, int consumeType) {
		int money = entity.getMoney(consumeType);
		if (canDoCollect(entity, consumeType)) {
			switch (consumeType) {
			case CollectHeroSoulT.GOLD:
				if (this.role.getJinbi() >= money) {
					return true;
				}
				break;
			case CollectHeroSoulT.RMBY:
				if (this.role.getTotalYuanbao() >= money) {
					return true;
				}
				break;
			case CollectHeroSoulT.WINE:
				final String WINE_TEMPLATE_ID = "wine"; // 美酒的模版id获取 //$NON-NLS-1$
				if (this.role.getItemControler().getItemCountInPackage(WINE_TEMPLATE_ID) >= money) {
					return true;
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!role.isOnline()) {
			return null;
		}
		if (role.getRoleOpenedMenu().getOpenHeroCallDate() != null
				&& DateUtil.isSameDay(new Date(), role.getRoleOpenedMenu().getOpenHeroCallDate())) {
			return null;
		}
		boolean note = false;
		MajorUIRedPointNote redPoint = null;

		if (firstOpen) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.CollectHeroSoulMenu, true);
		}

		XsgCollectHeroSoulManager manager = XsgCollectHeroSoulManager.getInstance();
		for (CollectHeroSoulEntity entity : collectHeroSoulEntitys.values()) {
			if (manager.isGoingOn(entity.getType())
					&& (needRefresh(entity) || showRedPoint(entity, CollectHeroSoulT.GOLD) || showRedPoint(entity,
							CollectHeroSoulT.WINE))) {
				note = true;
				break;
			}
		}

		if (note) {
//			isSendRedPoint = true;
			redPoint = new MajorUIRedPointNote(MajorMenu.CollectHeroSoulMenu, true);
		}
		return redPoint;
	}

	@Override
	public void resetRedPoint() {
//		this.isSendRedPoint = false;
	}

	@Override
	public void setFirstOpen(boolean value) {
		firstOpen = value;
	}

}
