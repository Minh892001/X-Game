/**
 * 
 */
package com.morefun.XSanGo.chat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AMD_Chat_selectChallenge;
import com.XSanGo.Protocol.AMD_Chat_speakTo;
import com.XSanGo.Protocol.AMD_Chat_voteForbidSpeak;
import com.XSanGo.Protocol.ChallengeResult;
import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatSetView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.role.IRole;

/**
 * 聊天功能控制器
 * 
 * @author BruceSu
 * 
 */
public interface IChatControler {

	/**
	 * 接受聊天信息
	 * 
	 * @param senderId
	 * @param senderName
	 * @param msg
	 * @param sendTime
	 * @throws NoteException
	 */
	void messageReceived(String senderId, String senderName,
			TextMessage msg);

	/**
	 * 接受系统消息
	 * 
	 * @param content
	 * @throws NoteException
	 */
	void receiveSystemMessage(TextMessage msg, IRole target)
			throws NoteException;

	/**
	 * 接受跑马灯消息
	 * 
	 * @param content
	 * @throws NoteException
	 */
	void receiveAdMessage(String content, ChatChannel channel);

	/**
	 * 设置禁言状态
	 * 
	 * @param expire
	 */
	void setForbiddenExpireTime(Date expire);

	/**
	 * 是否被禁言
	 * 
	 * @return
	 */
	boolean isForbidden();

	/**
	 * 公共聊天频道发言
	 * 
	 * @param msg
	 * @throws NoGroupException
	 * @throws NoFactionException
	 */
	void speak(TextMessage msg) throws NoteException, NoFactionException,
			NoGroupException;

	/**
	 * 公共聊天频道发言 对目前玩家做动作
	 * 
	 * @param msg
	 * @param targetId
	 * @throws NoteException
	 * @throws NoFactionException
	 * @throws NoGroupException
	 */
	void speak(TextMessage msg, String targetId) throws NoteException,
			NoFactionException, NoGroupException;

	/**
	 * 私聊聊天频道发言
	 * 
	 * @param target
	 * @param msg
	 * @throws NoteException
	 */
	void speakTo(final AMD_Chat_speakTo __cb, IRole target, TextMessage msg)
			throws NoteException;

	/**
	 * 私聊聊天频道发言 ,目前不在线离线留言
	 * 
	 * @param sysFlag
	 */
	void speakToOffline(AMD_Chat_speakTo __cb, String targetName,
			TextMessage msg, boolean sysFlag) throws NoteException;

	/**
	 * 查询聊天设置
	 * 
	 * @return
	 * @throws NoteException
	 */
	ChatSetView selSet() throws NoteException;

	/**
	 * 保存聊天设置
	 * 
	 * @param set
	 * @throws NoteException
	 */
	void saveSet(ChatSetView set) throws NoteException;

	/**
	 * 根据频道 查询聊天 设置
	 * 
	 * @return List.get(0):这个频道 是否屏蔽 0：未屏蔽，1：已经屏蔽,
	 *         List.get(1):这个频道聊天字颜色吗，默认颜色：4008419071（lua使用的颜色）
	 */
	List<String> selSet(ChatChannel channel);

	/**
	 * 保存聊天设置 自定义颜色
	 * 
	 * @param set
	 * @throws NoteException
	 */
	void saveSetColor(int type, String color);

	/**
	 * 查看装备,装备属性封装在ItemView.extendsProperty里
	 * 
	 * @param itemId
	 * @return
	 * @throws NoteException
	 */
	ItemView viewEquip(String itemId) throws NoteException;

	/**
	 * 查看 道具
	 * 
	 * @param itemId
	 * @return
	 * @throws NoteException
	 */
	ItemView viewItem(String itemId) throws NoteException;

	/**
	 * 查看武将
	 * 
	 * @param itemId
	 * @return
	 * @throws NoteException
	 */
	HeroView viewHero(String heroId) throws NoteException;

	/**
	 * 查询离线消息，查询后删除
	 */
	void selectOfflineMess();

	/**
	 * 竞技场 炫耀 聊天显示格式：
	 * 
	 * @param targetId
	 * @param content
	 * @param reportId
	 * @param channelType
	 * @throws NoteException
	 * @throws NoGroupException
	 * @throws NoFactionException
	 */
	void strutArenaRankMessage(int channelType, String targetId,
			String reportContent, String strutContent) throws NoteException,
			NoGroupException, NoFactionException;

	/**
	 * 合成装备后， 炫耀
	 * 
	 * @param content
	 * @param itemType
	 * @param itemId
	 * @param itemTemplId
	 * @throws NoteException
	 */
	void strutItemMessage(String content, int itemType, String itemId,
			String itemTemplId) throws NoteException;

	/**
	 * 保存离线消息
	 * 
	 * @param msg
	 * @param sendRole
	 */
	void saveMessOffline(TextMessage msg, IRole sendRole);

	/**
	 * 系统发送的私聊消息
	 * 
	 * @param content
	 *            发送内容
	 * @param targetId
	 *            接受者角色ID
	 * @throws NoteException
	 */
	public void sendPrivateSystemMsg(String content, String targetId)
			throws NoteException;

	/**
	 * 聊天中 查看 切磋 对象
	 * 
	 * @param targetId
	 * @return
	 * @throws NoteException
	 */
	public void selectChallenge(AMD_Chat_selectChallenge __cb, String targetId)
			throws NoteException;

	/**
	 * 聊天中切磋 开始
	 * 
	 * @param targetId
	 * @return
	 * @throws NoteException
	 */
	public PvpOpponentFormationView beginChallenge(String targetId,
			String formationId) throws NoteException;

	/**
	 * 聊天中切磋 结束
	 * 
	 * @param resFlag
	 * @return
	 * @throws NoteException
	 */
	public ChallengeResult endChallenge(String targetId, int resFlag,
			byte remainHero) throws NoteException;

	/**
	 * 投票禁言
	 * @param type
	 * 			操作类型 0:发起禁言 1:参与投票
	 * @param targetID
	 * 			禁言目标角色ID
	 * @throws NoteException
	 */
	public void voteForbidSpeak(AMD_Chat_voteForbidSpeak __cb, int type, String targetID) throws NoteException;
	
	/**
	 * 解析公告的内容
	 * 
	 * @param content
	 *            公告内容
	 * @param replaceMap
	 *            替换的数据结构，value替换key
	 * @return
	 */
	String parseAdConent(String content, Map<String, String> replaceMap);

	/**
	 * 解析默认的道具 公告的内容<br>
	 * 默认规则
	 * 
	 * @param item
	 *            道具模板对象
	 * @param content
	 *            公告内容
	 * @return
	 */
	String parseAdContentItem(AbsItemT item, String content);

	/**
	 * 解析默认的道具 公告的内容<br>
	 * 默认规则，以及自定义替换规则
	 * 
	 * @param item
	 *            道具模板对象
	 * @param content
	 *            公告内容
	 * @param replaceMap
	 *            替换的数据结构，value替换key
	 * @return
	 */
	String parseAdContentItem(AbsItemT item, String content,
			Map<String, String> replaceMap);

	/**
	 * 解析默认的武将 公告的内容<br>
	 * 默认规则
	 * 
	 * @param heroStar3
	 *            武将对象
	 * @param content
	 *            公告内容
	 * @return
	 */
	String parseAdContentHeroT(HeroT heroT, String content);

	/**
	 * 解析默认的武将 公告的内容<br>
	 * 默认规则 以及自定义替换规则
	 * 
	 * @param heroStar3
	 *            武将对象
	 * @param content
	 *            公告内容
	 * @param replaceMap
	 *            替换的数据结构，value替换key
	 * @return
	 */
	String parseAdContentHeroT(HeroT heroT, String content,
			Map<String, String> replaceMap);

	String parseAdContentHero(IHero hero, String content);

	String parseAdContentHero(IHero hero, String content,
			Map<String, String> replaceMap);

	/**
	 * 获取回调接口
	 * 
	 * @return
	 */
	ChatCallbackPrx getChatCb();
	
	/**
	 * 获取静默禁言过期时间
	 * @return
	 */
	Date getSilenceExpireQuietly();
	
	/**
	 * 设置静默禁言过期时间
	 * @param silenceExpireQuietly
	 */
	void setSilenceExpireQuietly(Date silenceExpireQuietly);
}
