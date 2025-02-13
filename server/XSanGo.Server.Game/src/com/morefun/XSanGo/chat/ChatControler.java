/**
 * 
 */
package com.morefun.XSanGo.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_Chat_selectChallenge;
import com.XSanGo.Protocol.AMD_Chat_speakTo;
import com.XSanGo.Protocol.AMD_Chat_voteForbidSpeak;
import com.XSanGo.Protocol.ChallengeResult;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatEnemyInfo;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.ChatSetView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalRank;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.colorfulEgg.EggBasisConfT;
import com.morefun.XSanGo.colorfulEgg.XsgColorfullEggManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.db.game.ChatMessage;
import com.morefun.XSanGo.db.game.ChatMessageOffline;
import com.morefun.XSanGo.db.game.ChatSet;
import com.morefun.XSanGo.db.game.ChatVoteForbidden;
import com.morefun.XSanGo.db.game.ChatVoteForbiddenDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IFriendFight;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.formation.IFormationControler;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.ladder.LadderLevelT;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.makewine.XsgMakeWineManager;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.SensitiveWordManager;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.sns.SNSType;
import com.morefun.XSanGo.sns.XsgSnsManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 聊天
 * 
 * @author 吕明涛
 */
class ChatControler implements IChatControler, IHeroStarUp, IHeroQualityUp, IHeroBreakUp {

	private Log log = LogFactory.getLog(this.getClass());

	private IRole roleRt;
	private Role roleDb;

	private byte orignalHeroCount; // 部队的武将数量，计算战斗后的星级

	/** 战报上下文ID */
	private String fightMovieIdContext = null;

	/** 聊天开放等级 */
	private static final int CHAT_LEVEL_LIMIT = 25;

	private ChatVoteForbiddenDAO forbiddenDao = null;

	private IFriendFight friendFightEvent;
	
	/**静默禁言过期解除时间*/
	private Date silenceExpireQuietly;
	
	/**被切磋 结果：0胜利  1失败 */
	private int lastChallengeStatus = 0;

	public ChatControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		this.roleRt.getEventControler().registerHandler(IHeroStarUp.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroQualityUp.class, this);
		this.roleRt.getEventControler().registerHandler(IHeroBreakUp.class, this);

		forbiddenDao = ChatVoteForbiddenDAO.getFromApplicationContext(ServerLancher.getAc());
		friendFightEvent = roleRt.getEventControler().registerEvent(IFriendFight.class);
	}

	@Override
	public void messageReceived(String senderId, String senderName, TextMessage msg) {
		// 判断是否过滤消息
		if (this.filterMsg(msg)) {
			ChatCallbackPrx callback = this.getChatCb();
			if (callback != null) {
				callback.begin_messageReceived(senderId, senderName, LuaSerializer.serialize(msg));
			}
		}
	}

	@Override
	public ChatCallbackPrx getChatCb() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.roleRt.getAccount(),
				this.roleRt.getRoleId());

		return session == null ? null : session.getChatCb();
	}

	/**
	 * 公共聊天频道发言
	 */
	@Override
	public void speak(TextMessage msg) throws NoteException, NoFactionException, NoGroupException {
		this.speak(msg, ""); //$NON-NLS-1$
	}

	/**
	 * 公共聊天频道发言 做动作
	 */
	@Override
	public void speak(TextMessage msg, String targetId) throws NoteException, NoFactionException, NoGroupException {
		this.checkSpeakChannel(msg.channel);

		// 过滤敏感字
		this.setShieldSensitiveWord(msg);

		// 做动作聊天，需要目前角色
		IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
		TextMessage setMsg = this.setChatMsg(msg, this.roleRt, targetRole);
		
		// 加入静默消息集合,已经被静默禁言，则不加入
		if(this.silenceExpireQuietly == null || this.silenceExpireQuietly.getTime() < System.currentTimeMillis()){
			XsgChatManager.getInstance().addForbiddenMsg(this.roleRt.getRoleId(), setMsg);
		} 
		// 频繁发言则发一条系统提示消息
		if(XsgChatManager.getInstance().isSpeakFrequently(this.roleRt.getRoleId())
				&& (this.silenceExpireQuietly == null || this.silenceExpireQuietly.getTime() < System.currentTimeMillis())){
			// 消息间隔大于5分钟，才再次提示玩家
			if(System.currentTimeMillis() - XsgChatManager.getInstance().lastSilenceMsgTime > 5 * 60 * 1000) {
				for (IRole acceptor : XsgRoleManager.getInstance().findChatAcceptorList(this.roleRt, ChatChannel.World)) {
					// 黑名单关系用户收不到刷屏的消息，没必要发拉黑提示
					if(isBlackRelation(this.roleRt, acceptor.getRoleId())
							|| isBlackRelation(acceptor, this.roleRt.getRoleId())) {
						continue;
					}
					TextMessage silenceTip = new TextMessage(ChatChannel.World, null, this.addChatRole(), 1, Messages.getString("ChatControler.slienceTip"), 0, 0);
					this.setSystemMessage(silenceTip);
					acceptor.getChatControler().messageReceived("", 
							Messages.getString("ChatControler.9"), 
							silenceTip);
				}
				XsgChatManager.getInstance().lastSilenceMsgTime = System.currentTimeMillis();
			}
		}

		for (IRole acceptor : XsgRoleManager.getInstance().findChatAcceptorList(this.roleRt, setMsg.channel)) {
			// A视为拉黑方，B视为被拉黑方，AB双方都将看不到对方在世界，公会，盟友和私聊频道的发言（只是看不到发言，能看到系统公告）
			if (isBlackRelation(this.roleRt, acceptor.getRoleId())
					|| isBlackRelation(acceptor, this.roleRt.getRoleId())) {
				continue;
			}
			
			// 被静默禁言，则只有本人能收到消息
			if(this.getSilenceExpireQuietly() != null && this.getSilenceExpireQuietly().getTime() > System.currentTimeMillis()){
				if(acceptor.getRoleId().equals(this.roleRt.getRoleId())) {
					acceptor.getChatControler().messageReceived(acceptor.getRoleId(), acceptor.getName(), setMsg);
				}
			} else {
				acceptor.getChatControler().messageReceived(acceptor.getRoleId(), acceptor.getName(), setMsg);
			}
		}

		// 保存聊天消息
		if (setMsg instanceof TextMessage) {
			this.saveMessage(setMsg);
		}
	}

	/**
	 * 私聊
	 */
	@Override
	public void speakTo(final AMD_Chat_speakTo __cb, IRole targetRole, TextMessage msg) throws NoteException {
		if (targetRole.getRoleId() == this.roleRt.getRoleId()) {
			throw new NoteException(Messages.getString("ChatControler.2")); //$NON-NLS-1$
		}

		// 过滤敏感字
		this.setShieldSensitiveWord(msg);

		// 对聊天信息，进行设置
		TextMessage setMsg = this.setChatMsg(msg, this.roleRt, targetRole);

		// 离线保存
		if (/* __cb != null && */(targetRole == null || !targetRole.isOnline())) {
			// 发送给自己
			this.messageReceived(this.roleRt.getRoleId(), this.roleRt.getName(), setMsg);
			// 发送给对方
			this.speakToOffline(__cb, targetRole.getName(), setMsg, false);
			return;
		}

		// A视为拉黑方，B视为被拉黑方，AB双方都将看不到对方在世界，公会，盟友和私聊频道的发言（只是看不到发言，能看到系统公告）
		if (!isBlackRelation(this.roleRt, targetRole.getRoleId())
				&& !isBlackRelation(targetRole, this.roleRt.getRoleId())) {
			// 发送给对方
			targetRole.getChatControler().messageReceived(targetRole.getRoleId(), targetRole.getName(), setMsg);
		}

		// 发送给自己
		this.messageReceived(this.roleRt.getRoleId(), this.roleRt.getName(), setMsg);

		// 保存聊天信息
		if (msg instanceof TextMessage) {
			this.saveMessage(msg);
		}

		if (__cb != null) {
			__cb.ice_response();
		}
	}

	@Override
	public void sendPrivateSystemMsg(String content, final String targetId) throws NoteException {
		final TextMessage msg = new TextMessage(ChatChannel.Private, null, this.addChatRole(), 1, content, 0, XsgChatManager.getInstance().getCurrentMillis());
		IRole target = XsgRoleManager.getInstance().findRoleById(targetId);
		if (target == null) {
			XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {
				@Override
				public void run() {
					IRole temp = XsgRoleManager.getInstance().findRoleById(targetId);
					try {
						sendPrivateSystemMsg(temp, msg, true);
					} catch (NoteException e) {
						LogManager.error(e);
					}
				}
			}, null);
		} else {
			sendPrivateSystemMsg(target, msg, true);
		}
	}

	private void sendPrivateSystemMsg(IRole target, TextMessage msg, boolean b) throws NoteException {
		// 离线保存
		if (target.isOnline()) {
			// 发送 私聊系统消息给对方
			target.getChatControler().receiveSystemMessage(msg, target);
		} else {
			this.speakToOffline(null, target.getName(), msg, true);
		}
	}

	/**
	 * 私聊 离线留言
	 */
	@Override
	public void speakToOffline(final AMD_Chat_speakTo __cb, final String targetName, final TextMessage msg,
			final boolean sysFlag) throws NoteException {

		XsgRoleManager.getInstance().loadRoleByNameAsync(targetName, new Runnable() {
			@Override
			public void run() {
				IRole targetRoleRt = XsgRoleManager.getInstance().findRoleByName(targetName);

				// 是否是系统离线 留言
				if (sysFlag) {
					setChatMsg(msg, roleRt, targetRoleRt);
					setSystemMessage(msg);
				}

				targetRoleRt.getChatControler().saveMessOffline(msg, roleRt);

				if (__cb != null) {
					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.4"))); //$NON-NLS-1$
				}
			}
		}, new Runnable() {
			@Override
			public void run() {
				if (__cb != null) {
					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.5"))); //$NON-NLS-1$
				}
			}
		});
	}

	/**
	 * 设置系统聊天 格式
	 * 
	 * @param msg
	 */
	private void setSystemMessage(TextMessage msg) {
		msg.cRole.id = "0"; //$NON-NLS-1$
		msg.cRole.name = Messages.getString("ChatControler.7"); //$NON-NLS-1$
		msg.cRole.vip = 0;
	}

	@Override
	public void receiveSystemMessage(TextMessage msg, IRole target) throws NoteException {
		this.setChatMsg(msg, this.roleRt, target);
		this.setSystemMessage(msg);
		this.messageReceived(msg.cRole.id, msg.cRole.name, msg);
	}

	@Override
	public void receiveAdMessage(String content, ChatChannel channel) {
		TextMessage msg = new TextMessage(channel, null, this.addChatRole(), 1, content, 0, 0);
		this.setSystemMessage(msg);
		this.messageReceived("", Messages.getString("ChatControler.9"), msg); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 碎片掠夺 炫耀 显示在聊天界面
	 */
	@Override
	public void strutItemMessage(String content, int itemType, String itemId, String itemTemplId) throws NoteException {

		TextMessage msg = new TextMessage(ChatChannel.World, null, this.addChatRole(), 1, content, 0, 0);

		this.setChatMsgItem(msg, itemType, itemId, itemTemplId);

		List<IRole> acceptorList = XsgRoleManager.getInstance().findChatAcceptorList(this.roleRt, msg.channel);
		for (IRole acceptor : acceptorList) {
			acceptor.getChatControler().messageReceived(acceptor.getRoleId(), acceptor.getName(), msg);
		}
	}

	/**
	 * 竞技场 炫耀 聊天显示格式
	 */
	@Override
	public void strutArenaRankMessage(int channelType, String targetId, String reportContent, String strutContent)
			throws NoteException, NoGroupException, NoFactionException {

		// 设置竞技场 炫耀的 聊天显示
		String channelColor = this.selChannelColor(this.roleRt.getRoleId(), ChatChannel.valueOf(channelType));
		String content = "${" + reportContent //$NON-NLS-1$
				+ TextUtil.format("COL={0}|{1}}", channelColor, strutContent); //$NON-NLS-1$

		TextMessage msg = new TextMessage(ChatChannel.valueOf(channelType), null, this.addChatRole(), 1, content, 0, 0);

		// 根据不同频道显示
		if (ChatChannel.valueOf(channelType) == ChatChannel.System) {
			this.speak(msg);
		} else if (ChatChannel.valueOf(channelType) == ChatChannel.Private) {
			IRole target = XsgRoleManager.getInstance().findRoleById(targetId);
			this.speakTo(null, target, msg);
		} else if (ChatChannel.valueOf(channelType) == null) {
			throw new NoteException(Messages.getString("ChatControler.12")); //$NON-NLS-1$
		}
	}

	/**
	 * 设置禁言状态
	 */
	@Override
	public void setForbiddenExpireTime(Date expire) {
		this.roleDb.setSilenceExpire(expire);
	}

	/**
	 * 是否被禁言
	 */
	@Override
	public boolean isForbidden() {
		return this.roleDb.getSilenceExpire() != null
				&& this.roleDb.getSilenceExpire().getTime() > System.currentTimeMillis();
	}

	/**
	 * 保存离线消息
	 */
	@Override
	public void saveMessOffline(TextMessage msg, IRole sendRole) {
		ChatMessageOffline messageOff = new ChatMessageOffline(msg.channel.ordinal(), sendRole.getRoleId(),
				sendRole.getName(), sendRole.getLevel(), sendRole.getVipController().getLevel(),
				sendRole.getHeadImage(), sendRole.getOfficalRankId(), TextUtil.GSON.toJson((TextMessage) msg),
				new Date(), this.roleDb.getName(), this.roleDb);

		this.roleDb.getChatMessageOfflineList().add(messageOff);
	}

	/**
	 * 检查是否能够发言，如频道权限，内容长度及发言频率，屏蔽字等
	 * 
	 * @param channel
	 * @throws NoteException
	 * @throws NoFactionException
	 * @throws NoGroupException
	 */
	private void checkSpeakChannel(ChatChannel channel) throws NoteException, NoFactionException, NoGroupException {
		// 检查是否能够发言，如频道权限，内容长度及发言频率等
		switch (channel) {
		case Announce:
		case System:
			throw new NoteException(Messages.getString("ChatControler.13")); //$NON-NLS-1$
		case Faction:
			if (!this.roleRt.getFactionControler().isInFaction()) {
				throw new NoFactionException();
			}
			break;
		case Group:
			if (!this.roleRt.getSnsController().hasGroup()) {
				throw new NoGroupException();
			}
			break;
		case World:
			if (this.isForbidden()) {
				throw new NoteException(Messages.getString("ChatControler.14")); //$NON-NLS-1$
			}
			if (roleRt.getLevel() < CHAT_LEVEL_LIMIT) {
				throw new NoteException(
						String.format(Messages.getString("ChatControler.91"), String.valueOf(CHAT_LEVEL_LIMIT)));
			}
			break;

		default:
			break;
		}
	}

	// 保存 聊天消息
	private void saveMessage(TextMessage msg) {
		ChatMessage message = new ChatMessage(msg.channel.ordinal(), this.roleRt.getRoleId(), this.roleRt.getName(),
				msg.cRole.targetName == null ? "" : msg.cRole.targetName, msg.content,
				Calendar.getInstance().getTime());
		XsgChatManager.getInstance().save2DbAsync(message);

		// // 缓存聊天消息
		 msg.isCahceMsg = 1;
		 if (msg.channel == ChatChannel.World) {
			 XsgChatManager.getInstance().addWorldMsg(msg);
		 } else if (msg.channel == ChatChannel.Faction &&
			 this.roleDb.getFactionId() != null) {
			 XsgChatManager.getInstance().addGuildMsg(this.roleDb.getFactionId(),
			 msg);
		 } else if (msg.channel == ChatChannel.Private) {
			 // 一条私聊消息保存两个玩家的缓存
			 XsgChatManager.getInstance().addPrivateMsg(msg.cRole.id, msg);
			 XsgChatManager.getInstance().addPrivateMsg(msg.cRole.targetId, msg);
		 }
	}

	/**
	 * 聊天消息中添加角色属性
	 * 
	 * @return
	 */
	private ChatRole addChatRole() {
		ChatRole cRole = new ChatRole();

		cRole.id = this.roleRt.getRoleId();
		cRole.name = this.roleRt.getName();
		cRole.level = (short) this.roleRt.getLevel();
		cRole.vip = this.roleRt.getVipController().getLevel();
		cRole.icon = this.roleRt.getHeadImage();
		cRole.chatTime = DateUtil.toString(System.currentTimeMillis(), "HH:mm"); //$NON-NLS-1$
		cRole.OfficalRankId = this.roleRt.getOfficalRankId();

		return cRole;
	}

	private TextMessage setChatMess(TextMessage msg, IRole targetRole) {
		msg.cRole = this.addChatRole();
		if (targetRole != null) {
			msg.cRole.targetId = targetRole.getRoleId();
			msg.cRole.targetName = targetRole.getName();
			msg.cRole.targetLevel = (short) targetRole.getLevel();
			msg.cRole.targetVip = targetRole.getVipLevel();
			msg.cRole.targetIcon = targetRole.getHeadImage();
			msg.cRole.targetOfficalRankId = targetRole.getOfficalRankId();
		}
		return msg;
	}

	/**
	 * 过滤敏感字
	 * 
	 * @param msg
	 * @return
	 */
	private void setShieldSensitiveWord(TextMessage msg) {
		TextMessage resMsg = (TextMessage) msg;
		resMsg.content = SensitiveWordManager.getInstance().shieldSensitiveWord(resMsg.content);
	}

	/**
	 * 聊天消息，是聊天动作时，做相应的处理
	 * 
	 * @param msg
	 * @param senderName
	 * @return
	 * @throws NoteException
	 */
	private TextMessage setChatMsg(TextMessage msg, IRole sendRole, IRole acceptRole) {

		// 设置 聊天 角色 属性
		this.setChatMess(msg, acceptRole);

		TextMessage resMsg = (TextMessage) msg;
		String channelColor = this.selChannelColor(resMsg);

		// 聊天内容，是否是做动作
		if (resMsg.type == 3) {
			String actionContent = XsgChatManager.getInstance().getActionName(resMsg.content);
			resMsg.content = this.replaceMsg(actionContent, sendRole, acceptRole, channelColor);
		} else if (resMsg.type != 4 && resMsg.type != 5) {// 聊天内容，不是语音及原生文本
			// if (resMsg.content.indexOf("${COL=") == -1) {
			// resMsg.content = "${"
			// + TextUtil.format("COL={0}|{1}}", channelColor,
			// resMsg.content);
			// }
			resMsg.content = XsgChatManager.getInstance().orgnizeColorText(resMsg.content, channelColor);
		}

		return resMsg;
	}

	/**
	 * 聊天消息，是动作时，进行相应替换
	 * 
	 * @param msg
	 *            消息字符
	 * @param roleName
	 *            发消息的角色名称
	 * @param senderName
	 *            接受角色的名称
	 * @param officalRankId
	 *            官阶等级，根据官阶进行替换
	 * @return
	 */
	private String replaceMsg(String msg, IRole sendRole, IRole acceptRole, String channelColor) {

		String resMsg = ""; //$NON-NLS-1$

		// 群雄争霸，关联官阶，没有官阶，默认最小官阶
		// 自己称呼
		LadderLevelT replaceT;
		if (this.roleRt.getLadderControler().getLadder() != null) {
			replaceT = XsgLadderManager.getInstance()
					.getLevelMap(this.roleRt.getLadderControler().getLadder().getLadderLevel());
		} else {
			replaceT = XsgLadderManager.getInstance().getLevelMap(XsgLadderManager.getInstance().getInitT().initLevel);
		}

		if (replaceT != null) {
			String $s_str = ""; //$NON-NLS-1$
			if (this.roleRt.getSex() == 0) {
				$s_str = replaceT.$s_str_w;
			} else {
				$s_str = replaceT.$s_str_m;
			}

			resMsg = msg.replace("$S", replaceT.$S_str).replace("$s", $s_str); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// 对手的称呼
		LadderLevelT acceptReplaceT;
		if (acceptRole != null) {
			if (acceptRole.getLadderControler().getLadder() != null) {
				acceptReplaceT = XsgLadderManager.getInstance()
						.getLevelMap(acceptRole.getLadderControler().getLadder().getLadderLevel());
			} else {
				acceptReplaceT = XsgLadderManager.getInstance()
						.getLevelMap(XsgLadderManager.getInstance().getInitT().initLevel);
			}

			if (acceptReplaceT != null) {
				String $C_str = "", $c_str = "", $r_str = ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (acceptRole.getSex() == 0) {
					$C_str = acceptReplaceT.$C_str_w;
					$c_str = acceptReplaceT.$c_str_w;
					$r_str = acceptReplaceT.$r_str_w;
				} else {
					$C_str = acceptReplaceT.$C_str_m;
					$c_str = acceptReplaceT.$c_str_m;
					$r_str = acceptReplaceT.$r_str_m;
				}

				resMsg = resMsg.replace("$C", $C_str).replace("$c", $c_str) //$NON-NLS-1$ //$NON-NLS-2$
						.replace("$R", acceptReplaceT.$R_str) //$NON-NLS-1$
						.replace("$r", $r_str); //$NON-NLS-1$
			}
		}

		// VIP等级，显示角色名称的颜色
		String sendVipColor = sendRole.getVipController().getVipColor();

		String acceptVipColor = ""; //$NON-NLS-1$
		if (acceptRole != null) {
			acceptVipColor = acceptRole.getVipController().getVipColor();
		}

		String sendNameVipColor = "${" //$NON-NLS-1$
				+ TextUtil.format("COL={0}|{1}}", //$NON-NLS-1$
						NumberUtil.parseColorRGBAFromHtml(sendVipColor), sendRole.getName());

		String acceptNameVipColor = ""; //$NON-NLS-1$
		if (acceptRole != null) {
			acceptNameVipColor = "${" //$NON-NLS-1$
					+ TextUtil.format("COL={0}|{1}}", //$NON-NLS-1$
							NumberUtil.parseColorRGBAFromHtml(acceptVipColor), acceptRole.getName());
		}

		// 替换的字符不在 开头 和 结尾
		String channelColorStr = "${" //$NON-NLS-1$
				+ TextUtil.format("COL={0}|", channelColor); //$NON-NLS-1$
		if (!resMsg.startsWith("{$N") && !resMsg.endsWith("$N") //$NON-NLS-1$ //$NON-NLS-2$
				&& !resMsg.startsWith("{$n") && !resMsg.endsWith("$n")) { //$NON-NLS-1$ //$NON-NLS-2$
			resMsg = channelColorStr + resMsg + "}"; //$NON-NLS-1$
		}

		if (resMsg.startsWith("{$N")) { //$NON-NLS-1$
			resMsg = resMsg.replace("$N", sendNameVipColor + channelColorStr); //$NON-NLS-1$
		} else if (resMsg.endsWith("$N")) { //$NON-NLS-1$
			resMsg = resMsg.replace("$N", "}" + sendNameVipColor); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			resMsg = resMsg.replace("$N", "}" + sendNameVipColor //$NON-NLS-1$ //$NON-NLS-2$
					+ channelColorStr);
		}

		if (resMsg.startsWith("{$n")) { //$NON-NLS-1$
			resMsg = resMsg.replace("$n", acceptNameVipColor + channelColorStr); //$NON-NLS-1$
		} else if (resMsg.endsWith("$n")) { //$NON-NLS-1$
			resMsg = resMsg.replace("$n", "}" + acceptNameVipColor); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			resMsg = resMsg.replace("$n", "}" + acceptNameVipColor //$NON-NLS-1$ //$NON-NLS-2$
					+ channelColorStr);
		}

		return resMsg;
	}

	/**
	 * 查询聊天频道设置的颜色
	 * 
	 * @param msg
	 * @return
	 */
	private String selChannelColor(TextMessage msg) {
		return this.selChannelColor(msg.cRole.id, msg.channel);
	}

	/**
	 * 查询聊天频道设置的颜色
	 * 
	 * @param roleId
	 * @param channel
	 * @return
	 */
	private String selChannelColor(String roleId, ChatChannel channel) {
		IRole colorRole = XsgRoleManager.getInstance().findRoleById(roleId);
		return colorRole.getChatControler().selSet(channel).get(1);
	}

	/**
	 * 设置聊天信息显示格式
	 * 
	 * @param msg
	 * @return
	 * @throws NoteException
	 */
	@Deprecated
	private void setChatMsgItem(TextMessage msg, int itemType, String itemId, String itemTemplId) throws NoteException {
		// 聊天 信息是动作 不设置聊天的颜色
		String channelColor = this.selChannelColor(msg.cRole.id, msg.channel);
		msg.content = "${" //$NON-NLS-1$
				+ TextUtil.format("COL={0}|{1}}", channelColor, msg.content); //$NON-NLS-1$

		// 设置 替换 炫耀 物品的显示
		if (!itemTemplId.equals("0")) {
			String con1 = TextUtil.format("{0}|{1}|{2}", itemType, itemTemplId, itemId);
			String con2 = TextUtil.format("{0}|", channelColor); //$NON-NLS-1$
			msg.content = msg.content.replace("$I", "}${ITM=" + con1 //$NON-NLS-1$ //$NON-NLS-2$
					+ "}${COL=" + con2); //$NON-NLS-1$
		}
	}

	/**
	 * 保存聊天 颜色自定义设置
	 */
	@Override
	public void saveSetColor(int type, String color) {
		ChatSet setDb = this.roleDb.getChatSet();

		if (setDb == null) {
			setDb = new ChatSet(GlobalDataManager.getInstance().generatePrimaryKey(), "", "", "", "", this.roleDb); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		if (ChatChannel.Private.value() == type) {
			setDb.setPrivateUserColor(color);
		} else if (ChatChannel.World.value() == type) {
			setDb.setWorldUserColor(color);
		} else if (ChatChannel.Faction.value() == type) {
			setDb.setFactionUserColor(color);
		} else if (ChatChannel.Group.value() == type) {
			setDb.setGroupUserColor(color);
		}

		this.roleDb.setChatSet(setDb);
	}

	/**
	 * 查询聊天设置
	 */
	@Override
	public ChatSetView selSet() throws NoteException {
		ChatSet setDb = this.roleDb.getChatSet();
		if (setDb != null) {
			return new ChatSetView(setDb.getPrivateSet(), setDb.getPrivateColor(), setDb.getPrivateUserColor(),
					setDb.getWorldSet(), setDb.getWorldColor(), setDb.getWorldUserColor(), setDb.getFactionSet(),
					setDb.getFactionColor(), setDb.getFactionUserColor(), setDb.getGroupSet(), setDb.getGroupColor(),
					setDb.getGroupUserColor());
		} else {
			return new ChatSetView(0, "", "", 0, "", "", 0, "", "", 0, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		}
	}

	/**
	 * 保存聊天设置
	 */
	@Override
	public void saveSet(ChatSetView setView) throws NoteException {
		ChatSet setDb = this.roleDb.getChatSet();

		if (setDb != null) {
			setDb.setPrivateSet(setView.privateSet);
			setDb.setPrivateColor(setView.privateColor);
			setDb.setWorldSet(setView.worldSet);
			setDb.setWorldColor(setView.worldColor);
			setDb.setFactionSet(setView.factionSet);
			setDb.setFactionColor(setView.factionColor);
			setDb.setGroupSet(setView.groupSet);
			setDb.setGroupColor(setView.groupColor);
		} else {
			setDb = new ChatSet(GlobalDataManager.getInstance().generatePrimaryKey(), setView.privateSet,
					setView.privateColor, setView.worldSet, setView.worldColor, setView.factionSet,
					setView.factionColor, setView.groupSet, setView.groupColor, this.roleDb);
		}

		this.roleDb.setChatSet(setDb);
	}

	/**
	 * 根据频道 查询聊天 设置 默认颜色：4008419071（lua使用的颜色）
	 */
	@Override
	public List<String> selSet(ChatChannel channel) {

		List<String> rsSetList = new ArrayList<String>(2);

		ChatSet setDb = this.roleDb.getChatSet();
		if (setDb != null) {
			switch (channel) {
			case Private:
				rsSetList.add(String.valueOf(setDb.getPrivateSet()));
				rsSetList.add(setDb.getPrivateColor());
				break;
			case World:
				rsSetList.add(String.valueOf(setDb.getWorldSet()));
				rsSetList.add(setDb.getWorldColor());
				break;
			case Faction:
				rsSetList.add(String.valueOf(setDb.getFactionSet()));
				rsSetList.add(setDb.getFactionColor());
				break;
			case Group:
				rsSetList.add(String.valueOf(setDb.getGroupColor()));
				rsSetList.add(setDb.getGroupColor());
				break;
			default:
				rsSetList.add("0"); //$NON-NLS-1$
				rsSetList.add(Const.Chat.CHAT_COLOR_DEFAULT);
				break;
			}
		} else {
			rsSetList.add("0"); //$NON-NLS-1$
			rsSetList.add(Const.Chat.CHAT_COLOR_DEFAULT);
		}

		return rsSetList;
	}

	/**
	 * 判断是否过滤消息
	 * 
	 * @param roleId
	 * @param msg
	 * @return
	 */
	private boolean filterMsg(TextMessage msg) {
		// 自己说话不用过滤
		if (msg.cRole != null && msg.cRole.id.equals(roleRt.getRoleId())) {
			return true;
		}

		// 判断是否设置了屏蔽
		// IRole filterRole = XsgRoleManager.getInstance().findRoleById(roleId);
		String channelColor = roleRt.getChatControler().selSet(msg.channel).get(0);
		if (channelColor.equals("0")) { //$NON-NLS-1$
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查看装备,装备属性封装在ItemView.extendsProperty里
	 */
	@Override
	public ItemView viewEquip(String itemId) throws NoteException {
		ItemView equipView = XsgChatManager.getInstance().getShowItemMap().get(itemId);
		if (equipView == null) {
			throw new NoteException(Messages.getString("ChatControler.77")); //$NON-NLS-1$
		}
		return equipView;
	}

	/**
	 * 查看 道具
	 */
	@Override
	public ItemView viewItem(String itemId) throws NoteException {
		ItemView item = XsgChatManager.getInstance().getShowItemMap().get(itemId);
		if (item == null) {
			throw new NoteException(Messages.getString("ChatControler.78")); //$NON-NLS-1$
		}
		return item;
	}

	/**
	 * 查看 道具
	 */
	@Override
	public HeroView viewHero(String heroId) throws NoteException {
		HeroView heroView = XsgChatManager.getInstance().getShowHeroMap().get(heroId);
		if (heroView == null) {
			throw new NoteException(Messages.getString("ChatControler.79")); //$NON-NLS-1$
		}
		return heroView;
	}

	/**
	 * 查询离线消息，查询后删除
	 */
	@Override
	public void selectOfflineMess() {

		// 先查询缓存消息,汇总3个频道的消息
		List<TextMessage> list_all = new ArrayList<TextMessage>();
		list_all.addAll(XsgChatManager.getInstance().getWorldMsgCache());
		if (this.roleDb.getFactionId() != null && this.roleDb.getFactionId() != ""
				&& XsgChatManager.getInstance().getGuildMsgCache(this.roleDb.getFactionId()) != null) {
			list_all.addAll(XsgChatManager.getInstance().getGuildMsgCache(this.roleDb.getFactionId()));
		}
		list_all.addAll(XsgChatManager.getInstance().getPrivateMsgCache(this.roleRt.getRoleId()));

		for (final TextMessage tm : list_all) {
			roleRt.getChatControler().messageReceived(tm.cRole.id, tm.cRole.name, tm);
		}

		// 再查看离线消息
		if (this.roleDb.getChatMessageOfflineList().size() > 0) {
			for (ChatMessageOffline messOffline : this.roleDb.getChatMessageOfflineList()) {
				TextMessage msg = TextUtil.GSON.fromJson(messOffline.getSenderContent(), TextMessage.class);
				this.roleRt.getChatControler().messageReceived(msg.cRole.id, msg.cRole.name, msg);

				// 保存聊天信息
				if (msg instanceof TextMessage) {
					this.saveMessage(msg);
				}
			}
		}
		// 查询查看后删除
		this.roleDb.getChatMessageOfflineList().clear();

		// 查询离线消息为客户端[加载完成标识]
		// 限时活动设置首次打开 TODO 后期客户端InitSession成功发送协议通知服务器
		this.roleRt.getSeckillControler().setFirstOpen(true);
		this.roleRt.getDayChargeControler().setFirstOpen(true);
		this.roleRt.getDayConsumeControler().setFirstOpen(true);
		this.roleRt.getSumChargeActivityControler().setFirstOpen(true);
		this.roleRt.getSumConsumeActivityControler().setFirstOpen(true);

		this.roleRt.getBigDayChargeControler().setFirstOpen(true);
		this.roleRt.getBigDayConsumeControler().setFirstOpen(true);
		this.roleRt.getBigSumChargeActivityControler().setFirstOpen(true);
		this.roleRt.getBigSumConsumeActivityControler().setFirstOpen(true);

		this.roleRt.getFortuneWheelControler().setFirstOpen(true);
		this.roleRt.getExchangeItemControler().setFirstOpen(true);
		// 累计登录的成就处理
		roleRt.getAchieveControler().updateAchieveLogin();

		roleRt.refreshRedPoint(true);

		// 登陆获取彩蛋视图
		getEggCallBack();
		
		// 显示主界面图标[酿酒，嘉年华]
		showMainUIButton();
	}

	/** 登陆获取彩蛋 */
	private void getEggCallBack() {
		boolean isOpen = XsgColorfullEggManager.getInstance().isOpen();
		if (!isOpen) {
			return;
		}

		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();

		if (eggConf == null) {
			return;
		} else if (eggConf.limitLevel > roleRt.getLevel() || eggConf.entryFlag != 1) {
			return;
		}

		ChatCallbackPrx prx = this.getChatCb();
		if (prx != null) {
			try {
				prx.begin_getColorfulEggView(LuaSerializer.serialize(roleRt.getColorfullEggController().getView()));
			} catch (NoteException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * 显示主界面图标[酿酒，嘉年华] 0:隐藏 1:显示
	 */
	private void showMainUIButton(){
		int[] ids = new int[2]; // 2个活动
		ids[0] = XsgMakeWineManager.getInstance().showIcon(this.roleRt.getLevel()) ? 1 : 0; // 酿酒
		ids[1] = XsgActivityManage.getInstance().isEndOpenServerActive() ? 0 : 1; // 嘉年华
		ChatCallbackPrx prx = this.getChatCb();
		if(prx != null) {
			try{
				prx.begin_showMainUIButton(LuaSerializer.serialize(ids));
			}catch(Exception e){
				log.error(e);
			}
		}
	}

	/**
	 * 聊天中 查看 切磋 对象
	 * 
	 * @throws NoteException
	 */
	@Override
	public void selectChallenge(final AMD_Chat_selectChallenge __cb, final String targetId) throws NoteException {

		XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {
			@Override
			public void run() {
				IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
				RivalRank resArena = new RivalRank();
				resArena.id = targetRole.getRoleId();
				resArena.name = targetRole.getName();
				resArena.icon = targetRole.getHeadImage();
				resArena.vip = targetRole.getVipLevel();
				resArena.level = targetRole.getLevel();
				resArena.compositeCombat = targetRole.getCachePower();
				if (resArena.compositeCombat <= 0) {
					resArena.compositeCombat = targetRole.calculateBattlePower();
				}
				resArena.groupName = targetRole.getFactionControler().getFactionName(); // 工会名称
				if(targetRole.getFormationControler().getDefaultFormation() != null
						&& targetRole.getFormationControler().getDefaultFormation().getBuff() != null){
					resArena.formationBuffID = targetRole.getFormationControler().getDefaultFormation().getBuff().getTemplate().getId();
				}
				if (targetRole.getArenaRankControler() != null) {
					targetRole.getArenaRankControler().selectRivalArena(resArena);
				} else {
					resArena.rank = -1;
					resArena.attack = 100;
					resArena.guard = 100;
					resArena.guardHeroArr = targetRole.getFormationControler().getDefaultFormation().getSummaryView();
					resArena.supportHeroArr = targetRole.getFormationControler().getDefaultFormation().getSupportSummaryView();
				}
				ChatEnemyInfo enemyInfo = new ChatEnemyInfo();
				enemyInfo.rivalInfo = resArena;
				if (isBlackRelation(roleRt, targetRole.getRoleId())
						|| isBlackRelation(targetRole, roleRt.getRoleId())) {
					enemyInfo.blackRelation = true;
				}
				enemyInfo.serverId = targetRole.getServerId();
				__cb.ice_response(LuaSerializer.serialize(enemyInfo));
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("ChatControler.80"))); //$NON-NLS-1$
			}
		});
	}

	// 保存选择了，战斗部队武将数量
	private void setHeroCount(String formationId) {
		this.orignalHeroCount = this.roleRt.getFormationControler().getFormation(formationId)
				.getHeroCountIncludeSupport();
	}

	/**
	 * 获取role的战斗阵容
	 */
	private PvpOpponentFormationView getPvpOpponentFormationViewWithRole(IRole role) {
		IFormationControler controler = role.getFormationControler();
		PvpOpponentFormationView view = controler.getPvpOpponentFormationView(controler.getDefaultFormation().getId());
		return view;
	}

	/**
	 * 聊天中切磋 开始
	 * 
	 * @throws NoteException
	 * 
	 */
	@Override
	public PvpOpponentFormationView beginChallenge(String targetId, String formationId) throws NoteException {
		// 保存选择了，战斗部队武将数量
		setHeroCount(formationId);

		if (!this.roleRt.getRoleId().equals(targetId)) {
			IRole revengeRole = XsgRoleManager.getInstance().findRoleById(targetId);
			if (revengeRole != null) {
				revengeRole.getFormationControler();
				// 敌方阵容
				PvpOpponentFormationView targetView = getPvpOpponentFormationViewWithRole(revengeRole);
				// 存储战报上下文
				fightMovieIdContext = XsgFightMovieManager.getInstance()
						.generateMovieId(XsgFightMovieManager.Type.ChatFight, roleRt, revengeRole);
				return targetView;
			} else {
				throw new NoteException(Messages.getString("ChatControler.81")); //$NON-NLS-1$
			}

		} else {
			throw new NoteException(Messages.getString("ChatControler.82")); //$NON-NLS-1$
		}
	}

	/**
	 * 聊天中切磋 结束
	 * 
	 * @throws NoteException
	 */
	@Override
	public ChallengeResult endChallenge(String targetId, int resFlag, byte remainHero) throws NoteException {
		ChallengeResult res = new ChallengeResult();
		if (resFlag == 1) {
			res.star = XsgCopyManager.getInstance().calculateStar(this.orignalHeroCount, remainHero);
			// 切磋记录胜利一次
			this.roleDb.getChallengeSummary().setSuccessTimes(this.roleDb.getChallengeSummary().getSuccessTimes() + 1);
		} else {
			res.star = 0;
			// 切磋记录失败一次
			this.roleDb.getChallengeSummary().setFailTimes(this.roleDb.getChallengeSummary().getFailTimes() + 1);
		}
		res.movieId = XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(), fightMovieIdContext, resFlag,
				remainHero);
		if (TextUtil.isBlank(res.movieId)) {
			log.error(TextUtil.format(Messages.getString("ChatControler.83"), roleRt.getRoleId(), targetId, resFlag)); //$NON-NLS-1$
		}
		// fightMovieIdContext = null;

		// 切磋 结束,发系统私聊消息
		this.sendSystem(targetId, resFlag);

		friendFightEvent.onFriendFight(targetId, resFlag);
		return res;
	}

	/**
	 * 切磋 结束,发系统私聊消息
	 * 
	 * @param targetId
	 * @param resFlag
	 * @throws NoteException
	 */
	private void sendSystem(String targetId, int resFlag) throws NoteException {	
		if(resFlag != lastChallengeStatus) {
			String content = XsgChatManager.getInstance().orgnizeRoleText(this.roleRt);

			// 胜利还是失败
			if (resFlag == 0) {
				content += Messages.getString("ChatControler.84"); //$NON-NLS-1$
			} else {
				content += Messages.getString("ChatControler.85"); //$NON-NLS-1$
			}

			this.sendPrivateSystemMsg(content, targetId);			
		
			lastChallengeStatus = resFlag;
		}		
	}

	@Override
	public String parseAdConent(String content, Map<String, String> replaceMap) {
		for (Entry<String, String> entry : replaceMap.entrySet()) {
			content = content.replace(entry.getKey(), entry.getValue());
		}
		return content;
	}

	@Override
	public String parseAdContentItem(AbsItemT itemT, String content) {
		String itemText = XsgChatManager.getInstance().orgnizeItemText("", //$NON-NLS-1$
				itemT);
		content = XsgChatManager.getInstance().replaceRoleContent(content, roleRt);
		return content = content.replace("~item~", itemText); //$NON-NLS-1$
	}

	@Override
	public String parseAdContentItem(AbsItemT itemT, String content, Map<String, String> replaceMap) {
		content = this.parseAdContentItem(itemT, content);
		content = this.parseAdConent(content, replaceMap);
		return content;
	}

	@Override
	public String parseAdContentHeroT(HeroT heroT, String content) {
		content = XsgChatManager.getInstance().replaceRoleContent(content, this.roleRt);
		content = XsgChatManager.getInstance().replaceHeroContent(content, heroT);
		return content;
	}

	@Override
	public String parseAdContentHeroT(HeroT heroT, String content, Map<String, String> replaceMap) {
		content = this.parseAdContentHeroT(heroT, content);
		content = this.parseAdConent(content, replaceMap);
		return content;
	}

	@Override
	public String parseAdContentHero(IHero hero, String content) {
		content = XsgChatManager.getInstance().replaceRoleContent(content, roleRt);
		content = XsgChatManager.getInstance().replaceHeroContent(content, hero);

		return content;
	}

	@Override
	public String parseAdContentHero(IHero hero, String content, Map<String, String> replaceMap) {
		content = this.parseAdContentHero(hero, content);
		content = this.parseAdConent(content, replaceMap);
		return content;
	}

	/**
	 * 武将升星
	 */
	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		if (hero.getStar() > 2) {// 3星及以上才发公告
			List<ChatAdT> adTList = XsgChatManager.getInstance()
					.getAdContentMap(XsgChatManager.AdContentType.heroStar5);
			if (adTList.size() > 1) {
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				XsgChatManager.getInstance().sendAnnouncementHero(hero, this.parseAdContentHero(hero, adT.content));
			}
		}
	}

	/**
	 * 武将颜色改变
	 */
	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLeve) {
		if (hero.getQualityLevel() > 4) {// +5及以上公告
			List<ChatAdT> adTList = XsgChatManager.getInstance()
					.getAdContentMap(XsgChatManager.AdContentType.equipOrange);
			if (adTList.size() > 1) {
				ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
				XsgChatManager.getInstance().sendAnnouncementHero(hero, this.parseAdContentHero(hero, adT.content));
			}
		}
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(XsgChatManager.AdContentType.HeroBreak);
		if (adTList.size() > 1) {
			ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
			XsgChatManager.getInstance().sendAnnouncementHero(hero, this.parseAdContentHero(hero, adT.content));
		}
	}

	/**
	 * 投票禁言
	 * 
	 * @param type
	 *            操作类型 0:发起禁言 1:参与投票
	 */
	@Override
	public void voteForbidSpeak(final AMD_Chat_voteForbidSpeak __cb, int type, final String targetID)
			throws NoteException {
		// 禁言状态无法参与/发起禁言
		if (this.isForbidden()) {
			__cb.ice_exception(new NoteException(Messages.getString("ChatControler.cannotVote")));
			return;
		}

		// 禁言配置参数
		final ChatForbiddenT forbiddenParams = XsgChatManager.getInstance().getForbidden();
		// vip和等级限制
		if (this.roleRt.getLevel() < forbiddenParams.level && this.roleRt.getVipLevel() < forbiddenParams.vip) {
			__cb.ice_exception(new NoteException(String.format(Messages.getString("ChatControler.voteLevelLimit"),
					String.valueOf(forbiddenParams.vip), String.valueOf(forbiddenParams.level))));
			return;
		}
		int voted_size = 0; // 已经存在的禁言次数
		final List<ChatVoteForbidden> list_votes = XsgChatManager.getInstance().getList_votes();
		Iterator<ChatVoteForbidden> iter_votes = list_votes.iterator();
		while (iter_votes.hasNext()) {
			ChatVoteForbidden cvf = iter_votes.next();
			if (cvf.getRoleId().equals(roleRt.getRoleId())) {
				voted_size++;
			}
		}
		// 同时参与的禁言上限
		if (voted_size > forbiddenParams.forbidden_times) {
			__cb.ice_exception(new NoteException(String.format(Messages.getString("ChatControler.voteTimesLimit"),
					forbiddenParams.forbidden_times)));
			return;
		}

		XsgRoleManager.getInstance().loadRoleByIdAsync(targetID, new Runnable() {

			@Override
			public void run() {
				IRole target = XsgRoleManager.getInstance().findRoleById(targetID);
				// 不能禁言Vip或者Lvl比自己高的人，优先Vip
				if (roleRt.getVipLevel() < target.getVipLevel()) {
					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.cannotVoteHighVip")));
					return;
				} else if (roleRt.getVipLevel() == target.getVipLevel() && roleRt.getLevel() < target.getLevel()) {
					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.cannotVoteHighLvl")));
					return;
				}

				// 目标是否已经被禁言
				if (target.getSilenceExpire() != null
						&& target.getSilenceExpire().getTime() > System.currentTimeMillis()) {
					__cb.ice_exception(new NoteException(Messages.getString("ChatControler.targetIsForbided")));
					return;
				}

				// 目标在timeout时间内是否已经被投票禁言
				Date now = new Date();
				Iterator<ChatVoteForbidden> iter = list_votes.iterator();
				while (iter.hasNext()) {
					ChatVoteForbidden cvf = iter.next();
					if (cvf.getTargetId().equals(targetID) && cvf.getAddTime().getTime() >= DateUtil
							.addMinutes(now, -forbiddenParams.timeout).getTime()) {
						final ChatVoteForbidden targetForbidden = cvf;

						// 自己在timeout时间内是否已经发起/参与了Target的禁言投票
						if (targetForbidden.getVoteIds().indexOf(roleRt.getRoleId()) != -1) {
							__cb.ice_exception(new NoteException(Messages.getString("ChatControler.hasVoted")));
							return;
						}

						// 目标玩家已存在记录，则转换成参与操作
						if (targetForbidden.getVoteIds() == null || targetForbidden.getVoteIds().trim().length() == 0) {
							targetForbidden.setVoteIds(roleRt.getRoleId());
						} else {
							targetForbidden.setVoteIds(targetForbidden.getVoteIds() + "," + roleRt.getRoleId());
						}
						// 更新数据库
						DBThreads.execute(new Runnable() {

							@Override
							public void run() {
								try {
									forbiddenDao.customMerge(targetForbidden);
								} catch (Exception e) {
									log.error(e);
								}
							}
						});
						// 全服发送禁言频道消息
						sendForBiddenMsg(roleRt, target, targetForbidden);

						// 投票次数达到则对玩家禁言并且公告
						if (targetForbidden.getVoteIds().split(",").length >= forbiddenParams.vote_need) {
							String forbidSuccessMsg = forbiddenParams.forbidd_success;
							forbidSuccessMsg = forbidSuccessMsg
									.replace("~role1~",
											"${PLY=" + target.getRoleId() + "|" + target.getName() + "|"
													+ target.getVipLevel() + "}")
									.replace("~param1~", String.valueOf(forbiddenParams.timeout))
									.replace("~param2~", String.valueOf(forbiddenParams.vote_need));
							XsgChatManager.getInstance().sendForbiddenMsg(forbidSuccessMsg);

							// 禁言成功设置禁言状态
							target.setSilenceExpire(DateUtil.addMinutes(new Date(), forbiddenParams.remove_time));

							// 将被禁言的玩家放入全局的集合,解除禁言的时候通知用
							XsgChatManager.getInstance().getList_forbidden_roles().add(target);

							// 将禁言成功的记录从正在禁言的集合中移除
							iter.remove();
						}
						__cb.ice_response();
						return;
					}
				}

				final ChatVoteForbidden targetForbidden = new ChatVoteForbidden(
						GlobalDataManager.getInstance().generatePrimaryKey(), roleDb.getId(), targetID,
						roleRt.getRoleId(), new Date());
				;
				// 保存数据库
				DBThreads.execute(new Runnable() {

					@Override
					public void run() {
						forbiddenDao.save(targetForbidden);
					}
				});

				// 全服发送禁言频道消息
				sendForBiddenMsg(roleRt, target, targetForbidden);

				// 将禁言记录放入全局集合里，供记录超时发送消息使用
				XsgChatManager.getInstance().getList_votes().add(targetForbidden);
				__cb.ice_response();
			}
		}, null);

	}

	/**
	 * 全服发送禁言通知消息
	 * 
	 * @param roleRt
	 * @param target
	 * @param forbiddenParams
	 */
	public void sendForBiddenMsg(IRole roleRt, IRole target, ChatVoteForbidden targetForbidden) {
		ChatForbiddenT forbiddenParams = XsgChatManager.getInstance().getForbidden();
		String msgTemplate = forbiddenParams.chat_template;
		msgTemplate = msgTemplate
				.replace("~role1~",
						"${PLY=" + roleRt.getRoleId() + "|" + roleRt.getName() + "|" + roleRt.getVipLevel() + "}")
				.replace("~role2~",
						"${PLY=" + target.getRoleId() + "|" + target.getName() + "|" + target.getVipLevel() + "}")
				.replace("~param1~", String.valueOf(targetForbidden.getVoteIds().split(",").length))
				.replace("~param2~", String.valueOf(forbiddenParams.timeout))
				.replace("~param3~", String.valueOf(forbiddenParams.vote_need));
		XsgChatManager.getInstance().sendForbiddenMsg(msgTemplate);
	}

	/**
	 * 是否是黑名单关系
	 * 
	 * @return
	 */
	private boolean isBlackRelation(IRole role, String roleId) {
		for (String blackId : XsgSnsManager.getInstance().grep(role.getSns(), SNSType.BLACKLIST)) {
			if (roleId.equals(blackId)) {
				return true;
			}
		}
		return false;
	}
	
	public Date getSilenceExpireQuietly() {
		return silenceExpireQuietly;
	}

	public void setSilenceExpireQuietly(Date silenceExpireQuietly) {
		this.silenceExpireQuietly = silenceExpireQuietly;
	}
}
