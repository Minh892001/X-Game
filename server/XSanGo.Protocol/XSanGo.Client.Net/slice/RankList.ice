#ifndef  _RANKLIST_ICE_
#define  _RANKLIST_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//各种排行榜
module com{
	module XSanGo{
		module Protocol{
		
			//排行榜基本数据
			struct RankListSub {
				int rank;		//排名
				string roleId;	//角色ID
				string roleName;//角色名称
				string icon;	//头像
				int level;		//等级
				int vipLevel;	//VIP等级
				int count;		//数量，包括： 战力、膜拜次数 
			};
			sequence<RankListSub> RankListSubSeq;
			
			//排行详情返回数据
			struct RankListShow {
				RankListSubSeq rankList;	//排行榜显示数据
				int ownRank; 				//自身的排名 -1：排名不具体显示
				int ownValue; 				//自身的数量，包括： 战力、膜拜次数 
			};
			
			//查询排行详情
			struct RankRoleDetail {
				string roleId;			//角色ID
				string factionName; 	//公会名称
				string formationBuff;	//阵法
				FormationSummaryViewSeq heroArr;	//部队 武将信息
			};
			
			//查询公会详情
			struct RankFactionDetail {
				string name; 			//公会名称
				int level; 				//公会等级
				string bossId;			//会长ID
				string bossName;		//会长名称
				int bossLevel;			//会长等级
				int bossVipLevel;		//会长VIP等级
				string qq;				//群号
				string announcement;	//公会公告
			};
			 
			interface RankList { 
				//查询 部队战力排名
				["ami"] string selRankListCombat() throws NoteException;
				
				//查询 成就排名
				["ami"] string selRankListAchieve() throws NoteException;
				
				//查询 膜拜次数 排名
				["ami"] string selrankListWorship() throws NoteException;
				
				//查询 公会 排名
				["ami"] string selRankListFaction() throws NoteException;
				
				//查询排行详情
				["ami","amd"] string selRoleDetail(string roleId) throws NoteException;
				
				//查询 公会详情
				["ami","amd"] string selFactionDetail(string id) throws NoteException;
			};
		};   
	};
};

#endif
