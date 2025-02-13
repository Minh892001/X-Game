#ifndef  _FRIEND_CONTROLLER_
#define  _FRIEND_CONTROLLER_

#include "RoleF.ice"
#include "ExceptionDef.ice"

module com{
	module XSanGo{
		module Protocol{

			enum Result{SUCCESSFUL,FAILED};
			
			/*玩家2注册新好友处理方法后，当玩家1加玩家2好友，玩家2会收到此通知*/
			interface SnsCallBack {
				/*新的请求事件处理，接受或拒绝*/
				["ami"] void applying(string player);
				["ami"] void handleApplyingWith(Result r);
			};
			
			//好友列表每条记录的数据结构
			struct SnsRoleView {
				string id;
				bool isOnline;
				string icon;
				string name;
				int level;
				int vip;
				int fightingPower;
				bool remainingActActAbilityPoints;
				bool canapplying;
				int junLingNum;
				int sendJunLingNum;
				int friendPoint; // 友情点
				long offlineTime; // 离线时间跟当前时间的时间差
				bool eachOther; // 是否互为好友
			};
			
			//名单
			sequence<SnsRoleView> RoleList;

			struct OpenSnsView {
				//在线好友数
				int onlineCount;
				//好友上限
				int totleCount;
				// 好友总数
				int friendCount;
				//好友申请数量
				int applyingCount;
				//当前行动力
				int currentVit;
				//行动力最大值
				int maxVit;
				RoleList friends;
				// 送军令最大值
				int maxSendJunLing;
				// 领军令最大值
				int maxAcceptJunLing;
				// 已经送出的军令数量
				int sendJunLingNum;
				// 已经领取的军令数量
				int acceptJunLingNum;
			};
			
			// 领取军令返回结果
			struct AcceptJunLingResult {
				int friendPoint;
				int junLingNum;
			};
			
			// 一条战报记录
			struct BattleRecordRole{
				SnsRoleView roleView;
				string recordId; // 战报ID
				string date; // 战报日期
				int challengeTimes; // 切磋次数
				int result; // 0:fail 1:win
			};
			
			sequence<BattleRecordRole> BattleRecordRoleSeq;
			
			// 查看战报
			struct BattleRecord{
				int totalFight; // 累计切磋
				int totalSucess; // 累计胜利
				double totalProbability; // 累计胜率
				int lastFight;			// 最近切磋次数
				double lastProbability; // 最近胜率
				BattleRecordRoleSeq battleRecordRoles;
			};
			
			interface Sns {
				//玩家1加玩家2好友，需要玩家1调用这个方法
				["ami"] void applyForFriend(string targetPlayer) throws NoteException;
				//玩家1加玩家2，玩家2接受，调用这个方法;参数为空字符串""的时候标识全部接受
				["ami"] void accept(string player) throws NoteException;
				//玩家1加玩家2，玩家2拒绝，调用这个方法;参数为空字符串""的时候标识全部拒绝
				["ami"] void refuse(string player);
				//移除好友
				["ami"] void removeFriend(string targetPlayer);
				//未处理的好友申请
				["ami","amd"] string untreatedFriendApplyings();
				//添加仇人
				["ami"] void addFoe(string targetPlayer) throws NoteException;
				//移除仇人
				["ami"] void removeFoe(string targetPlayer);
				//清楚仇人
				["ami"] void cleanFoes();
				//添加黑名单
				["ami"] void addBlacklist(string targetPlayer) throws NoteException;
				//移除黑名单
				["ami"] void removeBlacklist(string targetPlayer);
				//清楚黑名单
				["ami"] void cleanBlacklist();
				//所有好友 OpenSnsView
				["ami","amd"] string queryAllFreinds();
				//所有好友 RoleList
				["ami","amd"] string queryAllFreindsView();
				//按昵称搜索玩家 当参数为null或空字符串时，返回系统推荐好友 type:1 好友，2仇人，3黑名单
				["ami","amd"] string queryPlayersLike(int type, string partOfNickname);
				// 搜索好友 换一批
				["ami","amd"] string changeMorePlayers() throws NoteException;
				//所有仇人
				["ami","amd"] string queryAllFoes();
				//所有黑名单
				["ami","amd"] string queryAllBlacklist();
				// 送军令
				["ami", "amd"] int sendJunLing(string targetId) throws NoteException;
				// 领军令, return AcceptJunLingResult
				["ami", "amd"] string acceptJunLing(string targetId) throws NoteException;
				// 查询战报 return BattleRecord
				["ami","amd"] string queryBattleRecordView() throws NoteException;
            };
            
        };
 	};
 };

#endif
