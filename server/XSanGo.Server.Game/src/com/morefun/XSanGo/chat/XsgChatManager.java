/**
 * 
 */
package com.morefun.XSanGo.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.ChatMessage;
import com.morefun.XSanGo.db.game.ChatMessageDAO;
import com.morefun.XSanGo.db.game.ChatVoteForbidden;
import com.morefun.XSanGo.db.game.ChatVoteForbiddenDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LRULinkedHashMap;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 聊天全局管理
 * 
 * @author BruceSu
 * 
 */
/**
 * @author sulingyun
 * 
 */
public class XsgChatManager {

	private static XsgChatManager instance = new XsgChatManager();

	private final static Log log = LogFactory.getLog(XsgChatManager.class);

	/**
	 * 走马灯显示的类型<br>
	 * VioletHeroFromMarket :抽卡获得紫色武将<br>
	 * equipPurple :抽卡获得紫装<br>
	 * drawCard :签到抽奖获得<br>
	 * equipOrange :武将从紫色升为橙色<br>
	 * heroStar5 :武将从4星升为5星<br>
	 * soul :名将召唤获得将魂<br>
	 * jbxz:金宝箱开出紫装<br>
	 * 
	 * @author lvmingtao
	 * 
	 */
	public static enum AdContentType {
		/** 0:抽卡获得紫色武将 */
		VioletHeroFromMarket,
		/** 1:抽卡获得紫装 */
		equipPurple,
		/** 2:签到抽奖获得 */
		drawCard,
		/** 3:武将从紫色升为橙色 */
		equipOrange,
		/** 4:武将从4星升为5星 */
		heroStar5,
		/** 5:名将召唤获得将魂 */
		soul,
		/** 6:金宝箱开出紫装 */
		jbxz,
		/**
		 * vip升级
		 */
		vipUp,
		/**
		 * 购买VIP礼包
		 */
		buyVipGift,
		/**
		 * 通关大神章节
		 */
		pass,
		/**
		 * 进阶到指定级别
		 */
		advance, // 10
		/**
		 * 公会战开始报名
		 */
		gvgApply,
		/**
		 * 公会战开战
		 */
		gvgOpen,
		/**
		 * 公会战结束
		 */
		gvgOver,
		/**
		 * 公会战胜利
		 */
		gvgWin,
		/**
		 * 幸运大转盘10连抽
		 */
		FortuneWheel10,
		/**
		 * 幸运大转盘获得元宝
		 */
		FortuneWheel,
		/**
		 * 购买到限时武将整卡
		 */
		limitHero,
		/**
		 * 购买到限时装备整卡
		 */
		limitEquip,
		/** 武将突破 */
		HeroBreak,
		/** 副本随机掉紫装 */
		CopyEquip,
		/**
		 * 公会战连胜 21
		 */
		GvgSuccessionWin,
		/**
		 * 公会战开始预告 22
		 */
		GvgAD,
		/**
		 * 公会战终结连胜 23
		 */
		GvgWinOver,
		/**
		 * 公会礼包开出紫装 24
		 */
		FactionXz,
		/**
		 * 物品兑换成功 25
		 */
		ExchangeAD,
		/**
		 * 合成紫装 26
		 * */
		CompoundOrange,
		/**
		 * 合成宝石 27
		 * */
		GemCompound,
		/**
		 * 大额红包, 28
		 * */
		BigRedPacket,
		/**
		 * 红包聊天消息, 29
		 * */
		ChatRedPacket,
		/**
		 * 捕获武将, 30
		 * */
		CaptureHero,
		/**
		 * 百步穿杨 31
		 * */
		MarksMan,
		/**
		 * 世界BOSS尾刀奖励 32
		 * */
		WorldBossTail,
		/**
		 * 世界BOSS开启公告 33
		 * */
		WorldBossOpen10,
		/**
		 * 世界BOSS开启公告 34
		 * */
		WorldBossOpen5,
		/**
		 * 世界BOSS死亡35
		 * */
		WorldBossDeath,
		/**
		 * 世界BOSS未死亡36
		 * */
		WorldBossNotDeath,
		/**
		 * 随机PK认怂 37
		 * */
		WarmupEscape,
		/**
		 * 超级转盘38
		 * */
		SuperRaffle,

		/**
		 * 百步穿杨 39
		 * */
		ShootActive,
		/**
		 * 索要红包 40
		 * */
		ClaimRedPacket,
		/**
		 * 成就 41
		 * */
		AchieveFirst,
		/**
		 * 比武大会冠军登录 42
		 * */
		TournamentChampionLogin,
		/**
		 * 彩蛋活动 43
		 * */
		ColorfullEggAD,
		/**
		 * 公会战挖宝 44
		 * */
		DiggingTreasure,
		/**
		 * 公会战据点占领 45
		 */
		OccupyStronghold,
		/**
		 * 公会战挖宝 46
		 * */
		FactionBattleEvent1,
		/**
		 * 公会战挖宝 47
		 * */
		FactionBattleEvent2,
		/**
		 * 公会战挖宝 48
		 * */
		FactionBattleEvent3,
		/**
		 * 公会战挖宝 49
		 * */
		FactionBattleEvent4,
		/**
		 * 公会战挖宝 50
		 * */
		FactionBattleEvent5,
		/**
		 * 公会战挖宝 51
		 * */
		FactionBattleEvent6,
		/**
		 * 公会战挖宝 52
		 * */
		FactionBattleEvent7,
		/**
		 * 公会战挖宝 53
		 * */
		FactionBattleEvent8,
		/**
		 * 公会战结束倒计时公告 54
		 * */
		FactionBattleAd,
		/**
		 * 公会战连胜终结（机器人） 55
		 * */
		FactionBattleWinOver,
		/**
		 * 公会战机器人占领空关卡56
		 * */
		FactionBattleOccupy,
	}

	/** 聊天动作 模板数据 */
	private Map<Integer, ChatActionT> actionMap = new HashMap<Integer, ChatActionT>();

	/** 公告走马灯 模板数据 */
	private Map<Integer, List<ChatAdT>> adContentMap = new HashMap<Integer, List<ChatAdT>>();

	/** 聊天禁言参数配置 */
	private ChatForbiddenT forbiddenParams = new ChatForbiddenT();
	
	/** 聊天静默机制配置 */
	private ChatFilterRuleT filterRuleConfig = new ChatFilterRuleT();

	/** 宝箱跑马灯 */
	private Map<String, List<ChestItemBroadcastT>> chestItemBroadcastMap = new HashMap<String, List<ChestItemBroadcastT>>();

	// 聊天系统 缓存的道具 和 装备 数据
	private LRULinkedHashMap<String, ItemView> showItemMap = new LRULinkedHashMap<String, ItemView>(100);

	private LRULinkedHashMap<String, HeroView> showHeroMap = new LRULinkedHashMap<String, HeroView>(100);

	/** 聊天正在禁言的记录 */
	private List<ChatVoteForbidden> list_votes = new ArrayList<ChatVoteForbidden>();

	/** 聊天正在禁言的记录 */
	private List<IRole> list_forbidden_roles = new ArrayList<IRole>();

	/** 世界频道缓存消息 */
	private List<TextMessage> worldMsgCache = new ArrayList<TextMessage>();

	/** 公会频道缓存消息 Key:guildID */
	private LRULinkedHashMap<String, List<TextMessage>> guildMsgCache = new LRULinkedHashMap<String, List<TextMessage>>(1000);

	/** 私聊频道缓存消息 Key:roleID */
	private LRULinkedHashMap<String, List<TextMessage>> privateMsgCache = new LRULinkedHashMap<String, List<TextMessage>>(1000);
	
	/**静默禁言消息缓存队列 Key:roleID*/
	private LRULinkedHashMap<String, List<ChatMessage>> silenceMsgCache = new LRULinkedHashMap<String, List<ChatMessage>>(1000);

	/**玩家短静累计次数集合*/
	private LRULinkedHashMap<String, List<Date>> shortSilenceCache = new LRULinkedHashMap<String, List<Date>>(1000);
	
	/**玩家被拉黑集合 key:targetId, value:List<Date, operateId>*/
	private LRULinkedHashMap<String, List<Object[]>> beBlackCache = new LRULinkedHashMap<String, List<Object[]>>(1000);
	
	/**玩家发言频繁时，系统提示 最后一次的时间 */
	public long lastSilenceMsgTime = 0;
	
	public static XsgChatManager getInstance() {
		return instance;
	}

	private XsgChatManager() {
		// 聊天动作 模板加载
		List<ChatActionT> chatActionList = ExcelParser.parse(ChatActionT.class);
		for (ChatActionT action : chatActionList) {
			actionMap.put(action.id, action);
		}

		// 公告走马灯 模板数据
		for (ChatAdT adT : ExcelParser.parse(ChatAdT.class)) {
			// 是否发送 公告信息
			if (adT.onOff == 1) {
				List<ChatAdT> chatAdList = adContentMap.get(adT.type);
				if (chatAdList == null) {
					chatAdList = new ArrayList<ChatAdT>();
				}

				chatAdList.add(adT);
				adContentMap.put(adT.type, chatAdList);
			}
		}

		// 宝箱走马灯 模板数据
		for (ChestItemBroadcastT adT : ExcelParser.parse(ChestItemBroadcastT.class)) {
			// 是否发送 公告信息
			if (adT.onOff == 1) {
				List<ChestItemBroadcastT> chatAdList = chestItemBroadcastMap.get(adT.chestItemId);
				if (chatAdList == null) {
					chatAdList = new ArrayList<ChestItemBroadcastT>();
				}

				chatAdList.add(adT);
				chestItemBroadcastMap.put(adT.chestItemId, chatAdList);
			}
		}

		// 禁言参数配置
		forbiddenParams = ExcelParser.parse(ChatForbiddenT.class).get(0);
		
		filterRuleConfig = ExcelParser.parse(ChatFilterRuleT.class).get(0);

		// 开服读取数据中有效的禁言记录
		Date now = new Date();
		list_votes = ChatVoteForbiddenDAO.getFromApplicationContext(ServerLancher.getAc()).findValidVotes(
				DateUtil.addSecond(now, forbiddenParams.timeout), now);
		// 开服清空7天前的数据
		ChatVoteForbiddenDAO.getFromApplicationContext(ServerLancher.getAc()).removeRecord(DateUtil.addDays(now, -7));
	}

	/**
	 * 创建玩家的聊天控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IChatControler createChatControler(IRole roleRt, Role roleDB) {
		return new ChatControler(roleRt, roleDB);
	}

	/**
	 * 异步保存到数据库
	 * 
	 * @param message
	 */
	public void save2DbAsync(final ChatMessage message) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				ChatMessageDAO dao = ChatMessageDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.save(message);
			}
		});
	}

	/**
	 * 发送全服公告，该方法为GM工具接口方法，参数为无格式化文本
	 * 
	 * @param content 无格式化文本
	 * @throws NoteException
	 */
	public void sendAnnouncementForGm(String content) {
		content = this.orgnizeColorText(content, Const.Chat.AnnouncementDefaultColor);
		this.sendAnnouncement(content);
	}

	/**
	 * 格式化聊天消息，找出所有没用特殊格式符包围的文本用颜色格式包围之
	 * 
	 * @param content
	 * @param color
	 * @return
	 */
	public String orgnizeColorText(String content, String color) {
		Pattern p = Pattern.compile("\\$\\{[^\\}]+\\}");// 特殊格式匹配
		String[] array = p.split(content);
		Map<String, String> replaceMap = new HashMap<String, String>();

		for (String orignal : array) {// 找出所有无格式文本，并用颜色进行格式化
			if (!TextUtil.isBlank(orignal)) {
				replaceMap.put(orignal, TextUtil.format("'${COL='{0}|{1}}", color, orignal));
			}
		}

		String output = content;
		for (String key : replaceMap.keySet()) {
			output = output.replace(key, replaceMap.get(key));
		}
		return output;
	}

	/**
	 * 组织默认颜色格式文本
	 * 
	 * @param content
	 * @return
	 */
	public String orgnizeColorText(String content) {
		return this.orgnizeColorText(content, Const.Chat.CHAT_COLOR_DEFAULT);
	}

	/**
	 * 发送全服公告 道具的跑马灯消息
	 * 
	 * @param item
	 * @param adId 公告脚本中的模板Id
	 */
	public void sendAnnouncementItem(IItem item, String content) {
		// 聊天系统 缓存 道具数据
		this.setShowItemMap(item);

		this.sendAnnouncement(content);
	}

	/**
	 * 发送全服公告
	 * 
	 * @param content 已经格式化好的文本
	 */
	public void sendAnnouncement(String content) {
		List<IRole> acceptorList = XsgRoleManager.getInstance().findOnlineList();
		for (IRole acceptor : acceptorList) {
			acceptor.getChatControler().receiveAdMessage(content, ChatChannel.Announce);
		}
	}

	/**
	 * 发送全服公告
	 * 
	 * @param type
	 */
	public void sendAnnouncement(AdContentType type) {
		sendAnnouncement(type, null);
	}

	/**
	 * 发送全服公告
	 * 
	 * @param type
	 * @param params 需要替换的参数组
	 */
	public void sendAnnouncement(AdContentType type, Map<String, Object> params) {
		List<ChatAdT> adTList = adContentMap.get(type.ordinal());
		if (adTList != null && !adTList.isEmpty()) {
			ChatAdT chatAdT = adTList.get(NumberUtil.random(adTList.size()));
			if (chatAdT != null && chatAdT.onOff == 1) { // 允许公告
				String content = chatAdT.content;
				if (params != null) {
					for (String key : params.keySet()) {
						content = content.replaceAll(key, params.get(key).toString());
					}
				}
				XsgChatManager.getInstance().sendAnnouncement(content);
			}
		}
	}

	/**
	 * 发送全服公告 武将的跑马灯消息
	 * 
	 * @param hero
	 * @param adId 公告脚本中的模板Id
	 */
	public void sendAnnouncementHero(IHero hero, String content) {
		// 聊天系统 缓存 武将数据
		this.setShowHeroMap(hero);

		this.sendAnnouncement(content);
	}

	/**
	 * 全服发送禁言消息
	 * 
	 * @param content
	 */
	public void sendForbiddenMsg(String content) {
		List<IRole> acceptorList = XsgRoleManager.getInstance().findOnlineList();
		for (IRole acceptor : acceptorList) {
			acceptor.getChatControler().receiveAdMessage(content, ChatChannel.Forbidden);
		}
	}
	
	/**
	 * 取得 聊天动作模板
	 * 
	 * @param actionId 动作模板ID
	 * @return
	 */
	public String getActionName(int actionId) {
		return this.actionMap.get(actionId).content;
	}

	/**
	 * 取得 聊天动作模板
	 * 
	 * @param actionId 动作模板ID
	 * @return
	 */
	public String getActionName(String actionId) {
		// 判断动作ID是否为数值类型
		if (actionId.matches("\\d+")) {
			return this.getActionName(Integer.valueOf(actionId));
		} else {
			return null;
		}
	}

	/**
	 * 聊天系统 缓存的道具
	 * 
	 * @return
	 */
	public Map<String, ItemView> getShowItemMap() {
		return showItemMap;
	}

	/**
	 * 聊天系统 缓存的道具
	 * 
	 * @param showItem
	 */
	public void setShowItemMap(IItem showItem) {
		if (showItem != null) {
			this.showItemMap.put(showItem.getId(), showItem.getView());
		}
	}

	/**
	 * 聊天系统 缓存的武将
	 * 
	 * @return
	 */
	public Map<String, HeroView> getShowHeroMap() {
		return showHeroMap;
	}

	/*
	 * 聊天系统 缓存的武将
	 */
	public void setShowHeroMap(IHero hero) {
		if (hero != null) {
			this.showHeroMap.put(hero.getId(), hero.getHeroView());
		}
	}

	public List<ChatAdT> getAdContentMap(AdContentType type) {
		return adContentMap.get(type.ordinal());
	}

	/**
	 * 组织武将格式文本
	 * 
	 * @param hero
	 * @param heroT
	 * @return
	 */
	public String orgnizeHeroText(IHero hero, HeroT heroT) {
		// ${HRO=武将ID|武将名字|武将成色|模板ID}这里只组织 = 到 }中间的部分
		String id = hero == null ? "" : hero.getId();
		String name = hero == null ? heroT.name : hero.getName();
		return TextUtil.format("{0}|{1}|{2}|{3}", id, name, hero == null ? heroT.color : hero.getShowColor().ordinal(),
				heroT.id);
	}

	/**
	 * 替换消息中的角色数据
	 * 
	 * @param content
	 * @param role
	 * @return
	 */
	public String replaceRoleContent(String content, IRole role) {
		return content.replace("~role_id~", role.getRoleId()).replace("~role_name~", role.getName())
				.replace("~role_vip~", String.valueOf(role.getVipLevel()));
	}

	/**
	 * 替换消息中的武将数据
	 * 
	 * @param content
	 * @param heroT
	 * @return
	 */
	public String replaceHeroContent(String content, HeroT heroT) {
		String heroText = this.orgnizeHeroText(null, heroT);
		return content.replace("~star~", String.valueOf(heroT.star)).replace("~hero~", heroText);
	}

	/**
	 * 替换消息中的武将数据
	 * 
	 * @param content
	 * @param hero
	 * @return
	 */
	public String replaceHeroContent(String content, IHero hero) {
		return content.replace("~star~", String.valueOf(hero.getStar())).replace("~hero~",
				this.orgnizeHeroText(hero, hero.getTemplate()));
	}

	/**
	 * 组织道具格式文本
	 * 
	 * @param itemId
	 * @param itemT
	 * @return
	 */
	public String orgnizeItemText(String itemId, AbsItemT itemT) {
		// ${ITM=道具类型ID|模版ID|服务器ID}这里只组织 = 到 }中间的部分
		return TextUtil.format("{0}|{1}|{2}", itemT.getItemType().ordinal(), itemT.getId(), itemId);
	}

	/**
	 * 组织玩家格式文本
	 * 
	 * @param rt
	 * @return
	 */
	public String orgnizeRoleText(IRole rt) {
		String result = "${PLY=~role_id~|~role_name~|~role_vip~}";
		return this.replaceRoleContent(result, rt);
	}

	/**
	 * 禁言参数
	 * 
	 * @return
	 */
	public ChatForbiddenT getForbidden() {
		return forbiddenParams;
	}

	/**
	 * 聊天正在禁言的记录
	 * 
	 * @return
	 */
	public List<ChatVoteForbidden> getList_votes() {
		return list_votes;
	}

	/**
	 * 聊天正在禁言的记录
	 */
	public List<IRole> getList_forbidden_roles() {
		return list_forbidden_roles;
	}

	/**
	 * 刷新超时的禁言消息和解除禁言消息
	 */
	public void refreshForbiddenStatus() {
		final ChatForbiddenT forbiddenParams = this.getForbidden();
		// 正在被投票禁言的玩家集合
		List<ChatVoteForbidden> list_votes = this.getList_votes();
		// 1/移除超时的禁言记录，并且全服发送
		final Date now = new Date();
		Iterator<ChatVoteForbidden> iter_votes = list_votes.iterator();
		while (iter_votes.hasNext()) {
			final ChatVoteForbidden cvf = iter_votes.next();
			if (cvf.getAddTime().getTime() < DateUtil.addMinutes(now, -forbiddenParams.timeout).getTime()) {

				XsgRoleManager.getInstance().loadRoleByIdAsync(cvf.getTargetId(), new Runnable() {

					@Override
					public void run() {
						IRole target = XsgRoleManager.getInstance().findRoleById(cvf.getTargetId());
						String msgTemplate = forbiddenParams.timeout_template;
						msgTemplate = msgTemplate.replace("~role1~",
								"${PLY=" + target.getRoleId() + "|" + target.getName() + "|" + target.getVipLevel()
										+ "}");
						sendForbiddenMsg(msgTemplate);
					}
				}, null);
				iter_votes.remove();
			}
		}

		// 已经被禁言玩家集合
		List<IRole> list_role = this.getList_forbidden_roles();
		Iterator<IRole> iter_roles = list_role.iterator();
		while (iter_roles.hasNext()) {
			IRole target = iter_roles.next();
			if (target.getSilenceExpire() != null && target.getSilenceExpire().getTime() <= System.currentTimeMillis()) {
				try {
					target.getChatControler().sendPrivateSystemMsg(forbiddenParams.remove_template, target.getRoleId());
					iter_roles.remove();
				} catch (NoteException e) {
					log.error(e);
				}
			}
		}

		log.info("list_votes.size=" + list_votes.size() + ",list_role.size=" + list_role.size());
	}
	
	/**
	 * 刷新玩家静默禁言缓存消息队列
	 */
	public void refreshSilenceMsg(){
		// 清除一个小时之前的聊天消息
		for(List<ChatMessage> list : silenceMsgCache.values()){
			Iterator<ChatMessage> iter = list.iterator();
			while(iter.hasNext()) {
				ChatMessage cm = iter.next();
				// 清除一小时之前的消息
				if(cm.getCreateTime().getTime() < 
						Calendar.getInstance().getTime().getTime() - 3600 * 1000) {
					iter.remove();
				}
			}
//			System.out.println("silenceMsgSize=" + list.size());
		}
		// 清除一天前的短静默次数
		for(List<Date> list : shortSilenceCache.values()){
			Iterator<Date> iter = list.iterator();
			while(iter.hasNext()){
				Date d = iter.next();
				if(d.getTime() < Calendar.getInstance().getTimeInMillis() - 24 * 3600 * 1000) {
					iter.remove();
				}
			}
		}
		
		// 清除一个小时之前的被拉黑数据
		for(List<Object[]> list : beBlackCache.values()){
			Iterator<Object[]> iter = list.iterator();
			while(iter.hasNext()){
				Object[] dateAndRole = iter.next();
				if(((Date)dateAndRole[0]).getTime() < Calendar.getInstance().getTimeInMillis() - 3600 * 1000) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * 获取宝箱跑马灯对应的配置
	 * 
	 * @param templateId 宝箱物品ID
	 * @return
	 */
	public List<ChestItemBroadcastT> getChestItemBroadcastT(String templateId) {
		return this.chestItemBroadcastMap.get(templateId);
	}

	public List<TextMessage> getWorldMsgCache() {
		return worldMsgCache;
	}

	/**
	 * 缓存世界频道消息
	 */
	public void addWorldMsg(TextMessage msg) {
		if (worldMsgCache.size() >= XsgGameParamManager.getInstance().getChatMsgSave()) {
			worldMsgCache.remove(0);
		}
		worldMsgCache.add(msg);
	}

	/**
	 * 获取公会频道缓存消息
	 */
	public List<TextMessage> getGuildMsgCache(String guideId) {
		List<TextMessage> list = new ArrayList<TextMessage>();
		if (guildMsgCache.containsKey(guideId)) {
			list = guildMsgCache.get(guideId);
		}
		return list;
	}

	/**
	 * 缓存公会频道消息
	 */
	public void addGuildMsg(String guideId, TextMessage msg) {
		if (guildMsgCache.get(guideId) == null) {
			guildMsgCache.put(guideId, new ArrayList<TextMessage>());
		}
		if (guildMsgCache.get(guideId).size() >= XsgGameParamManager.getInstance().getChatMsgSave()) {
			guildMsgCache.get(guideId).remove(0);
		}
		guildMsgCache.get(guideId).add(msg);
	}

	/**
	 * 获取私聊缓存消息 私聊消息包含发送对象和接收对象
	 */
	public List<TextMessage> getPrivateMsgCache(String roleId) {
		List<TextMessage> list = new ArrayList<TextMessage>();
		if (privateMsgCache.containsKey(roleId)) {
			list = privateMsgCache.get(roleId);
		}

		return list;
	}

	/**
	 * 缓存私聊消息
	 */
	public void addPrivateMsg(String roleId, TextMessage msg) {
		if (privateMsgCache.get(roleId) == null) {
			privateMsgCache.put(roleId, new ArrayList<TextMessage>());
		}

		if (privateMsgCache.get(roleId).size() >= XsgGameParamManager.getInstance().getChatMsgSave()) {
			privateMsgCache.get(roleId).remove(0);
		}
		privateMsgCache.get(roleId).add(msg);
	}
	
	/**
	 * 静默配置
	 * 
	 * @return
	 */
	public ChatFilterRuleT getFilterRuleConfig() {
		return filterRuleConfig;
	}
	
	/**
	 * 添加一条缓存消息
	 */
	public void addForbiddenMsg(String roleID, TextMessage msg){
		ChatMessage message = new ChatMessage(msg.channel.ordinal(), msg.cRole.id, msg.cRole.name,
				msg.cRole.targetName == null ? "" : msg.cRole.targetName, msg.content,
				Calendar.getInstance().getTime());
		
		if(silenceMsgCache.get(roleID) == null) {
			silenceMsgCache.put(roleID, new ArrayList<ChatMessage>());
		}
		silenceMsgCache.get(roleID).add(message);
	}

	/**
	 * 一段时间内是否发言过于频繁
	 * @return
	 */
	public boolean isSpeakFrequently(String roleID){
		// 检测一段时间累计发言次数
		int[] periodAndCount = new int[2];
		for (int i = 0; i < filterRuleConfig.sign.split(":").length; i++) {
			periodAndCount[i] = Integer.parseInt(filterRuleConfig.sign.split(":")[i]);
		}
		
		int count = 0; // 一段时间内累计发言次数
		for(ChatMessage cm : silenceMsgCache.get(roleID)){
			if(cm.getCreateTime().getTime() >= Calendar.getInstance().getTimeInMillis() - periodAndCount[0] * 60 * 1000) {
				count += 1;
			}
		}
		return count >= periodAndCount[1];
	}
	
	/**
	 * 添加一条短静默次数
	 */
	private void addShortSilenceCount(String roleID, Date date){
		List<Date> list = shortSilenceCache.get(roleID);
		if(list == null){
			list = new ArrayList<Date>();
			shortSilenceCache.put(roleID, list);
		}
		list.add(date);
	}
	
	/**
	 * 获取角色24小时内 已经短静默的次数
	 * @param roleID
	 * @return
	 */
	public int getShortSilenceCount(String roleID){
		List<Date> list = shortSilenceCache.get(roleID);
		if(list == null)	return 0;
		
		int count = 0;
		for(Date date : list){
			if(date.getTime() >= Calendar.getInstance().getTimeInMillis() - 24 * 3600 * 1000){
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * 玩家被拉黑，集合次数+1
	 * @param targetId 被拉黑的人
	 * @param date 被拉黑时间
	 * @param operateId 操作者
	 */
	public void addBlackCount(final String targetId, Date date, String operateId){
		List<Object[]> list = beBlackCache.get(targetId);
		if(list != null){
			// 玩家已经对目标对象拉黑
			for(Object[] obj : list){
				if(String.valueOf(obj[1]).equals(operateId)){
					return;
				}
			}
		} else {
			list = new ArrayList<Object[]>();
			beBlackCache.put(targetId, list);
		}
		list.add(new Object[]{date, operateId});
		
		// 在一段时间内，达到或超过多少名玩家对同一人拉黑，则视该玩家言论不当，短时静默1次
		int[] periodAndCount = new int[2];
		for(int i = 0;i < filterRuleConfig.silence.split(":").length; i ++){
			periodAndCount[i] = Integer.parseInt(filterRuleConfig.silence.split(":")[i]);
		}
		int count = 0; // 被拉黑次数
		for(Object[] dateAndRole : list){
			if(((Date) dateAndRole[0]).getTime() >= Calendar.getInstance().getTimeInMillis() - 1000 * 60 * periodAndCount[0]) {
				count += 1;
			}
		}
		
		// 检测玩家是否达到静默条件, 标记条件为一段时间内累计发言次数。
		if (count >= periodAndCount[1]
				&& this.isSpeakFrequently(targetId)){
			XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {
				@Override
				public void run() {
					IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
					// 已经被禁言的，次数不累加
					if(targetRole.getChatControler().getSilenceExpireQuietly() != null
							&& targetRole.getChatControler().getSilenceExpireQuietly().getTime() > System.currentTimeMillis()) {
						return;
					}
					// 短静默次数+1
					addShortSilenceCount(targetId, new Date());
					
					Calendar silenceExpire = Calendar.getInstance();
					// 短静累计次数次数达到一定数量，触发长时静默
//					System.out.println("短静默次数====" + getShortSilenceCount(targetId));
					if(getShortSilenceCount(targetId) >= getFilterRuleConfig().total){
						silenceExpire.add(Calendar.MINUTE, getFilterRuleConfig().longTime);
					} else {
						silenceExpire.add(Calendar.MINUTE, getFilterRuleConfig().shortTime);
					}
					
//					targetRole.getRoleDB().setSilenceExpire(silenceExpire.getTime());
					targetRole.getChatControler().setSilenceExpireQuietly(silenceExpire.getTime());
				}
			}, new Runnable() {
				@Override
				public void run() {
//					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.80")));
				}
			});
		}
	}
	
	/**
	 * 获取当前毫秒数的最后4位
	 * @return
	 */
	public int getCurrentMillis(){
		String time = String.valueOf(System.currentTimeMillis());
		return Integer.parseInt(time.substring(time.length() - 4, time.length()));
	}
	
}
