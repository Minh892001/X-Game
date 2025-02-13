#ifndef  _LADDER_ICE_
#define  _LADDER_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//群雄争霸 天梯系统
module com{
	module XSanGo{
		module Protocol{
			//群雄争霸 战斗记录 
			struct LadderReport {
				string id;
				string name;
				string icon;
				int vipLevel;
				int ladderLevel;	//群雄争霸 等级
				int levelChange;	//等级 变化 ，负数代表降级
				int starChange;		//星级 变化 ，负数代表降星
				int state;			//战斗结果，0：败，1：胜
				long reportTime;	//战报发生时间
				string movieId; // 战斗录像ID
				int combat;//对手战力
				int level;//对手等级
			};
			sequence<LadderReport> ReportSeq; 
			
			//打开 群雄争霸 返回的数据
			struct LadderView{
				int ladderLevel;		//群雄争霸 等级
				int ladderStar;			//群雄争霸 星级
				int ladderScore;		//群雄争霸 分数
				int challengeRemain;	//剩余挑战次数
				int challengeBuyNum;	//已经购买 挑战次数
				string guardId;			//防守部队ID
				string rewardStr;		//奖励的物品，JSON格式:{}
				int remainDate;			//本赛季结束天数
				int rankNum;			//排行榜 名次，-1：不在排行中
				ReportSeq reportList; 	//群雄争霸 战报
			};
			
			//排行榜基本数据
			struct LadderRankListSub {
				int rank;			//排名
				string roleId;		//角色ID
				string roleName;	//角色名称
				string icon;		//头像
				int level;			//等级
				int ladderLevel;	//群雄争霸 等级
				int ladderStar;		//群雄争霸 星级
				int ladderScore;	//群雄争霸 分数
				int vipLevel;		//VIP等级
				string groupName;	//所属盟
				long rankTime;		//上榜时间
			};
			sequence<LadderRankListSub> LadderRankListSubSeq;
			
			//排行详情返回数据
			struct LadderRankListShow {
				LadderRankListSubSeq rankList;	//排行榜显示数据
				int ownRank; 					//自身的排名 -1：排名不具体显示
				int ladderLevel;				//自身 群雄争霸 等级
				int ladderStar;					//自身 群雄争霸 星级
				int ladderScore;
				string groupName;				//自身 所属盟
			};
			
			//挑战 战斗结束 返回数据
			struct LadderFightResult {
				int fightStar;		//战斗结果的星级
				int ladderChangerLevel;		//战斗结果 群雄争霸等级的变化
				int ladderChangerStar;		//当前星级
				int ladderScore;            //当前积分
				string movieId; // 战斗录像ID
			};
			
			//挑战 战斗结束 返回数据
			struct LadderPvpView {
				string roleId;		//角色ID
				string roleName;	//角色名称
				string icon;		//头像
				int vipLevel;		//VIP等级
				int sex;			//性别
				int level;
				PvpOpponentFormationView formationView;		//战斗结果的星级
			};
			
			/**自动战斗结果*/
			struct LadderAutoFightResult {
				string winRoleId;
			    SceneDuelViewSeq soloMovie;
			    ByteSeq fightMovie;
			    int winType;
			    
				int fightStar;		//战斗结果的星级
				int ladderChangerLevel;		//战斗结果 群雄争霸等级的变化
				int ladderChangerStar;		//当前星级
				int ladderScore;            //当前积分
				string movieId; // 战斗录像ID
				string enemyRoleName;//对手名字
				int	enemyRoleVip;//对手vip
			};
			
			interface Ladder { 
				//打开 群雄争霸界面
				["ami","amd"] string selectLadder() throws NoteException;
				
				//显示 排行榜
				["amd","ami"] string showRankList() throws NoteException;
				
				//保存防守队伍
				["ami"] void saveGurard(string guardId) throws NoteException;
				
				//购买挑战次数
				["ami"] void buyChallenge() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				//挑战 获取对手数据
				["ami","amd"] LadderPvpView beginFight(string formationId) throws NoteException;
				
				//挑战 战斗结束，结果通知，targetId:对手ID，resFlag:战斗结果，0:失败，1：胜利
				["ami"] string endFight(string rivalId, int resFlag, byte remainHero) throws NoteException;
				
				//根据等级，获得奖励
				["ami"] void reward(int rewardId) throws NoteException;
				
				/**
				 * 自动战报 return：LadderAutoFightResult的lua
				 */
				["ami","amd"] LadderAutoFightResult autoFight() throws NoteException;
			};
		};   
	};
};

#endif
