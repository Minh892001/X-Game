/**
 * 
 */
package com.morefun.XSanGo.chat;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Chat_selectChallenge;
import com.XSanGo.Protocol.AMD_Chat_speakTo;
import com.XSanGo.Protocol.AMD_Chat_voteForbidSpeak;
import com.XSanGo.Protocol.ChatSetView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.TextMessage;
import com.XSanGo.Protocol._ChatDisp;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 聊天服务接口
 * 
 * @author BruceSu
 * 
 */
public class ChatI extends _ChatDisp {

	private static final long serialVersionUID = -7886789561258634097L;

	/** 限制发言间隔 */
	private static final long SPEAK_INTERVAL = 8000;

	private IRole roleRt;
	private long lastTime;

	public ChatI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}

	@Override
	public void speak(String msg, Current __current) throws NoteException, NoFactionException, NoGroupException {
		// msg = filterEmoji(msg);
		TextMessage msgObj = TextUtil.GSON.fromJson(msg, TextMessage.class);
		msgObj.identity = XsgChatManager.getInstance().getCurrentMillis();
		
		if ("dev".equals(GlobalDataManager.getInstance().getPlaform())) { //$NON-NLS-1$
			if (Messages.getString("ChatI.1").equals(msgObj.content)) { //$NON-NLS-1$
				XsgRoleManager.getInstance().allHeroAndFullLevel((XsgRole) this.roleRt);
			} else if (msgObj.content.startsWith("大人，我要VIP")) {
				int vip = NumberUtil.parseInt(msgObj.content.substring(8));
				if (vip > 0) {
					this.roleRt.getVipController().setVipLevel(vip);
				}
			} else if(msgObj.content.startsWith("txk")){
				this.roleRt.addExtHeadBorder(msgObj.content);
				this.roleRt.setHeadBorder(msgObj.content);
				
				// 头像属性变更通知
				this.roleRt.getNotifyControler().onStrPropertyChange(Const.PropertyName.HEAD_BORDER,
						LuaSerializer.serialize(this.roleRt.getExtHeadBorder()));
			} else if(msgObj.content.startsWith("male") || msgObj.content.startsWith("female")){
				this.roleRt.addExtHeadImage(msgObj.content);
				this.roleRt.setHeadImage(msgObj.content);
			}

		}

		this.check();
		this.roleRt.getChatControler().speak(msgObj);
		this.updateLastTime();
	}

	/**
	 * 将emoji表情替换成*
	 * 
	 * @param source
	 * @return 过滤后的字符串
	 */
	public static String filterEmoji(String source) {
		if (TextUtil.isNotBlank(source)) {
			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		} else {
			return source;
		}
	}

	/**
	 * 公共频道喊话 做动作
	 * 
	 * @param msg
	 *            消息
	 * @param targetId
	 *            动作角色ID
	 */
	@Override
	public void speakAction(String msg, String targetId, Current __current) throws NoFactionException,
			NoGroupException, NoteException {
		TextMessage msgObj = TextUtil.GSON.fromJson(msg, TextMessage.class);

		this.check();
		this.roleRt.getChatControler().speak(msgObj, targetId);
		this.updateLastTime();

	}

	@Override
	public void speakTo_async(final AMD_Chat_speakTo __cb, final String targetId, String msg, Current __current)
			throws NoteException {
		// msg = filterEmoji(msg);
		final TextMessage msgObj = TextUtil.GSON.fromJson(msg, TextMessage.class);
		this.check();

		XsgRoleManager.getInstance().loadRoleByIdAsync(targetId, new Runnable() {
			@Override
			public void run() {
				IRole targetRole = XsgRoleManager.getInstance().findRoleById(targetId);
				try {
					roleRt.getChatControler().speakTo(__cb, targetRole, msgObj);
				} catch (NoteException e) {
					e.printStackTrace();
				}
				updateLastTime();
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("ChatI.2"))); //$NON-NLS-1$
			}
		});
	}

	@Override
	public byte[] getAttachObject(int id, Current __current) throws NoteException {
		return null;
	}

	private void updateLastTime() {
		this.lastTime = System.currentTimeMillis();
	}

	private void check() throws NoteException {
		if (System.currentTimeMillis() - this.lastTime < SPEAK_INTERVAL) {
			throw new NoteException(Messages.getString("ChatI.3")); //$NON-NLS-1$
		}
		// if (content.length() > 50) {
		// throw new NoteException("超出限定长度。");
		// }
	}

	/**
	 * 查询聊天设置
	 */
	@Override
	public String selSet(Current __current) throws NoteException {
		ChatSetView chatSetView = this.roleRt.getChatControler().selSet();
		return LuaSerializer.serialize(chatSetView);
	}

	/**
	 * 保存聊天设置
	 */
	@Override
	public void saveSet(String set, Current __current) throws NoteException {
		ChatSetView chatSetView = TextUtil.GSON.fromJson(set, ChatSetView.class);
		this.roleRt.getChatControler().saveSet(chatSetView);
	}

	/**
	 * 查看装备,装备属性封装在ItemView.extendsProperty里
	 */
	@Override
	public ItemView viewEquip(String itemId, Current __current) throws NoteException {
		return this.roleRt.getChatControler().viewEquip(itemId);
	}

	/**
	 * 查看 道具
	 */
	@Override
	public ItemView viewItem(String itemId, Current __current) throws NoteException {
		return this.roleRt.getChatControler().viewItem(itemId);
	}

	/**
	 * 查看武将
	 */
	@Override
	public HeroView viewHero(String heroId, Current __current) throws NoteException {
		return this.roleRt.getChatControler().viewHero(heroId);
	}

	/**
	 * 保存聊天自定义颜色设置
	 */
	@Override
	public void saveSetColor(int type, String userColor, Current __current) throws NoteException {
		this.roleRt.getChatControler().saveSetColor(type, userColor);
	}

	/**
	 * 查询离线消息，查询后删除
	 */
	@Override
	public void selectOfflineMess(Current __current) throws NoteException {
		this.roleRt.getChatControler().selectOfflineMess();
	}

	/**
	 * 聊天中 查看 切磋 对象
	 */
	@Override
	public void selectChallenge_async(AMD_Chat_selectChallenge __cb, String targetId, Current __current)
			throws NoteException {
		this.roleRt.getChatControler().selectChallenge(__cb, targetId);
	}

	/**
	 * 聊天中切磋 开始
	 */
	@Override
	public PvpOpponentFormationView beginChallenge(String targetId, String formationId, Current __current)
			throws NoteException {
		return this.roleRt.getChatControler().beginChallenge(targetId, formationId);
	}

	/**
	 * 聊天中切磋 结束
	 */
	@Override
	public String endChallenge(String targetId, int resFlag, byte remainHero, Current __current) throws NoteException {

		return LuaSerializer.serialize(this.roleRt.getChatControler().endChallenge(targetId, resFlag, remainHero));
	}

	/**
	 * 投票禁言
	 * 
	 * @param type
	 *            操作类型 0:发起禁言 1:参与投票
	 * @param targetID
	 *            禁言目标角色ID
	 */
	@Override
	public void voteForbidSpeak_async(AMD_Chat_voteForbidSpeak __cb, int type, String targetID, Current __current)
			throws NoteException {
		this.roleRt.getChatControler().voteForbidSpeak(__cb, type, targetID);
	}

}
