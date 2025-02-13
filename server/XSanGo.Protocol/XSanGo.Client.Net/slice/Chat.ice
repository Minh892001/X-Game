#ifndef  _CHAT_ICE_
#define  _CHAT_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"
#include "ArenaRank.ice"

module com{
	module XSanGo{
		module Protocol{
			enum ChatChannel{
				Announce,	//公告消息
				System,		//系统消息
				Private,	//私聊
				World,		//世界消息
				Faction,	//公会
				Group,		//盟友
				Forbidden,	//禁言消息
			};
			
			//聊天附件
			struct MessageAttachObject{
				int id;
				ByteSeq content;
			};
			sequence<MessageAttachObject> MessageAttachObjectSeq;
			
			//聊天中角色信息
			struct ChatRole{
				string id;
				string name;
				short level;
				int vip;
				string icon;
				int OfficalRankId;	//官阶
				string chatTime;	//发送的聊天时间
				string targetId;	//聊天目标 角色Id
				string targetName;	//聊天目标 角色名称
				short targetLevel;
				int targetVip;
				string targetIcon;
				int targetOfficalRankId;	//官阶
			};
			
			//文本消息类
			struct TextMessage {
				// BaseChatMessage
				ChatChannel channel;
				MessageAttachObjectSeq attachs;
				ChatRole cRole;
				int type;			//聊天消息类型 1：文本消息 2：符号消息 3:动作消息 4:语音聊天ID 5：Prototype 原型模式 6: 工会红包  7：好友红包 8：全服
				
				string content;
				int isCahceMsg; // 是否是缓存消息 1:是
				int identity; // 消息的唯一索引，@ta 的时候区分多条条消息
			};
			sequence<TextMessage> TextMessageSeq;
			
						
			//聊天设置保存数据
			struct ChatSetView {
				int privateSet;			//私聊 屏蔽 0:接受 1:屏蔽
				string privateColor;	//私聊 颜色
				string privateUserColor;//私聊 自定义颜色
				int worldSet;			//世界消息 屏蔽 0:接受 1:屏蔽
				string worldColor;		//世界消息 自定义颜色
				string worldUserColor;	//世界消息 颜色
				int factionSet;			//公会 屏蔽 0:接受 1:屏蔽
				string factionColor;	//公会 颜色
				string factionUserColor;//公会 自定义颜色
				int groupSet;			//盟友 屏蔽 0:接受 1:屏蔽
				string groupColor;		//盟友 颜色
				string groupUserColor;	//盟友 自定义颜色
			};
			
			struct ChatSetColorView {
				int pos;
				string color;
			};
			
			//聊天中，切磋的结果
			struct ChallengeResult {
				string movieId; // 战报录像ID
				int star; 	//战斗后，返回星级，战斗失败：0
			};
			
			// 切磋查看详情
			struct ChatEnemyInfo {
				RivalRank rivalInfo;
				bool blackRelation; // 是否黑名单关系(把目标拉黑和被目标拉黑都返回true)
				int serverId;	// 区服信息
			};
						
			interface ChatCallback{
				//["ami"] void messageReceived(string senderId,string senderName,BaseChatMessage msg);
				["ami"] void messageReceived(string senderId, string senderName, string msg);
				
				//二次确认
				["ami"] void confirm(string text);
				
				// 红点推送
				["ami"] void redPointSmit(int type, bool show);
				
				/**更新双倍卡剩余时间,传入IntIntPairSeq的lua*/
				["ami"] void updateDoubleCard(string intPairSeq);
				
				/**刷新伤害排行榜，传入WorldBossRankView的lua*/
				["ami"] void refreshHarmRank(string harmRanks);
				
				/**登录后获取彩蛋视图*/
    			["ami"] void getColorfulEggView(string view);
				
				/**更新寻宝数据，返回TreasureView的lua*/
				["ami"] void refreshTreasure(string view);
				
				/**显示主界面的 图标 new IntSeq[]{酿酒,嘉年华}  0:隐藏  1:显示*/
				["ami"] void showMainUIButton(string status);
			};
			
			interface Chat{
				//公共频道喊话
				//["ami"] void speak(BaseChatMessage msg) throws NoteException,NoFactionException,NoGroupException;
				["ami"] void speak(string msg) throws NoteException,NoFactionException,NoGroupException;
				
				//公共频道喊话 做动作
				["ami"] void speakAction(string msg, string targetId) throws NoteException,NoFactionException,NoGroupException;
				
				//私聊 ,私聊对象不在线，保存离线消息
				//["ami","amd"] void speakTo(string targetName,BaseChatMessage msg) throws NoteException  ;
				["ami","amd"] void speakTo(string targetId, string msg) throws NoteException  ;
				
				//获取附件数据
				["ami"] ByteSeq getAttachObject(int id) throws NoteException;
				
				//查询聊天设置
				//["ami"] ChatSetView selSet() throws NoteException;
				["ami"] string selSet() throws NoteException;
				
				//保存聊天设置
				//["ami"] void saveSet(ChatSetView set) throws NoteException;
				["ami"] void saveSet(string set) throws NoteException;
				
				//保存聊天自定义颜色设置，  type : ChatChannel 枚举数值
				["ami"] void saveSetColor(int type, string userColor) throws NoteException;
				
				//查看装备,返回 EquipView
				["ami"] ItemView viewEquip(string itemId) throws NoteException;
				
				//查看道具
				["ami"] ItemView viewItem(string itemId) throws NoteException;
				
				//查看武将
				["ami"] HeroView viewHero(string heroId) throws NoteException;
				
				//查看道具
				["ami"] void selectOfflineMess() throws NoteException;
				
				//切磋 查询对手信息
				//["ami"] RivalRank selectChallenge(string targetId) throws NoteException;
				["ami","amd"] string selectChallenge(string targetId) throws NoteException;
				
				//聊天中切磋 开始
				["ami"] PvpOpponentFormationView beginChallenge(string targetId, string formationId) throws NoteException;
				
				//聊天中切磋 结束 resFlag:0-失败，1-胜利 返回：resFlag
				//["ami"] ChallengeResult endChallenge(string targetId, int resFlag, byte remainHero) throws NoteException;
				["ami"] string endChallenge(string targetId, int resFlag, byte remainHero) throws NoteException;
				
				// 投票禁言 type:0-发起禁言  1-参与投票
				["ami","amd"] void voteForbidSpeak(int type, string targetID) throws NoteException;
				
			};
		};
	};
};

#endif
