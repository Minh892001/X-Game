#ifndef _CROSS_SERVER_ICE_
#define _CROSS_SERVER_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			// 比赛阶段编号
			enum StageIndex {
				NotStart, // 未开始
				SignUp, // 报名阶段
				QT, // 资格赛
				S32, // 32强
				S16, // 16强
				S8, // 8强
				S4, // 半决赛
				S2, // 决赛
				End, // 结束
			};

			/**跨服角色数据*/
			struct CrossRoleView{
				string roleId;
				string roleName;
				string headImg;
				int level;
				int vipLevel;
				int serverId;
				int sex;
				string factionName;
			};
			
			/**排行榜项*/
			class CrossRankItem{
				CrossRoleView roleView;
				PvpOpponentFormationView pvpView;
				int score;
			};
			sequence<CrossRankItem> CrossRankItemSeq;
			
			/**排行榜View*/
			struct CrossRankView{
				CrossRankItemSeq rankItem;
				int myScore;
				int myRank;
				int myServerId;
			};
			
			/**挑战对手*/
			struct CrossRivalView{
				CrossRoleView roleView;
				PvpOpponentFormationView pvpView;
				int score;
				int winScore;
				bool isWin;// 是否战胜过
			};
			sequence<CrossRivalView> CrossRivalViewSeq;
			
			/**跨服PVP*/
			struct CrossPvpView{
				int type; // 类型参考 游戏参数配置表.xls中的 战斗生命参数 ID列 配置
				CrossRoleView leftRoleView;
				PvpOpponentFormationView leftPvpView;
				
				CrossRoleView rightRoleView;
				PvpOpponentFormationView rightPvpView;
				
				string						sceneName;		// 场景名称
				int							sceneID;		// 场景ID
			};
			
			/**跨服战报*/
			struct CrossMovieView {
				string winRoleId;
				int selfHeroNum; // 剩余武将数量
			    SceneDuelViewSeq soloMovie;
			    ByteSeq fightMovie;
			    int winType;
			};
			sequence<CrossMovieView> CrossMovieViewSeq;
			
			/**淘汰赛玩家信息*/
			class ScheduleRoleView{
				CrossRoleView roleView;
				//PvpOpponentFormationView pvpView;
				int battlePower;
				int winNum;//胜场数
				int failNum;//负场数
				int toastNum;//敬酒数
				int score;//本局得分 用来显示 2:1
			};
			
			/**淘汰赛对阵表*/
			struct CrossScheduleView {
				int id;//对阵表唯一ID，记录押注时使用
				ScheduleRoleView leftRole;
				ScheduleRoleView rightRole;
				string winRoleId;
				int stage;// 阶段 32强 16强 8强 4强 2强
				int groupNum;//小组编号
				int orderNum;//对阵编号
			};
			sequence<CrossScheduleView> CrossScheduleViewSeq;
			
			// 用于GM工具通讯
			interface CrossServerGM {
				
			};
			interface CrossServerCallback {
				/**报名*/
				["ami"] void apply(CrossRoleView roleView, PvpOpponentFormationView pvpView) throws NoteException;
				
				/**获取排行榜*/
				["ami"] CrossRankView getCrossRank(string roleId) throws NoteException;
				
				/**获取自己排名和积分*/
				["ami"] IntIntPair getMyRankScore(string roleId) throws NoteException;
				
				/**保存部队阵容*/
				["ami"] void saveBattle(CrossRoleView roleView, PvpOpponentFormationView pvpView) throws NoteException;
				
				/**匹配对手*/
				["ami"] CrossRivalViewSeq matchRival(string roleId) throws NoteException;
				
				/**刷新对手*/
				["ami"] CrossRivalViewSeq refreshRival(string roleId) throws NoteException;
				
				/**结束挑战*/
				["ami"] string endChallenge(CrossRoleView myRoleView,bool isWin,string rivalRoleId) throws NoteException;
				
				/**获取淘汰赛对阵表*/
				["ami"] CrossScheduleViewSeq getSchedule() throws NoteException;
				
				/**获取淘汰赛对阵战报条数，return胜利者ID数组*/
				["ami"] StringSeq getScheduleMovieList(int id) throws NoteException;
				
				/**播放淘汰赛战报,index从0开始*/
				["ami"] CrossMovieView getScheduleMovieData(int id,int index) throws NoteException;
				
				/**押注(敬酒)*/
				["ami"] void crossBet(string winRoleId) throws NoteException;
				
				/**获取玩家阵容*/
				["ami"] PvpOpponentFormationView getRoleFormationView(string roleId) throws NoteException;
				
				/**获取服务器时间*/
				["ami"] long getServerTime();
				
				/**是否晋级32强*/
				["ami"] bool isInRank(string roleId) throws NoteException;
				
				/**是否报名*/
				["ami"] bool isApply(string roleId) throws NoteException;
				
				/**是否淘汰*/
				["ami"] bool isOut(string roleId) throws NoteException;
			};
			interface CrossServer {
				["ami"] bool ping(long time);
				["ami"] void setCallback(CrossServerCallback *cb);
				/**竞猜结果*/
				["ami"] void guessResult(int id,string winRoleId);
				
				/**获取机器人信息*/
				["ami", "amd"] CrossRankItemSeq getRobot(int num);
				
				/**发放各阶段奖励*/
				["ami", "amd"] void sendCrossAward(int rank,StringSeq roleIds);
				
				/**发放积分奖励*/
				["ami", "amd"] void sendScoreAward(int score,string roleId);
				
				/**发送比武大会脚本*/
				["ami", "amd"] void sendScript(ByteSeq data);
			};
		};
	};
};
		
		
#endif