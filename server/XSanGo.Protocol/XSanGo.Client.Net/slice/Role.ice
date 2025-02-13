#ifndef  _ROLE_ICE_
#define  _ROLE_ICE_

#include "Common.ice"
#include "ExceptionDef.ice"
#include "RoleF.ice"

module com{
	module XSanGo{
		module Protocol{
			//活动公告
			struct ActivityAnnounceView{
				int id;
				//type 0 默认，没有任何标识 1 新公告，增加‘新’类型标识 2 重要公告，增加‘重要’类型标识
				int type;
				bool read;
				string firstTitle;
				string secondTitle;
				string content;
			};
			sequence<ActivityAnnounceView> ActivityAnnounceViewSeq;
			
			//任务类型
			enum LoadingRankType {
				LoadingRankArean, 	//竞技场
				LoadingLadder,		//群雄争霸
				LoadingRankworship,	//大神
				LoadingRankFame,	//名人堂
				LoadingRankCombat, 	//战力
				LoadingRankFaction	//公会
			};
			
			//登录显示，排行榜基本数据
			struct LoadingRankSub {
				string roleId;		//角色ID
				string roleName;	//角色名称
				string icon;		//头像
				int vipLevel;		//VIP等级
				string showThing;	//显示的东西，如：数量，包括： 战力、膜拜次数等
			};
			sequence<LoadingRankSub> LoadingRankSeq;
			
			//登录显示，排行榜基本数据
			struct LoadingRankList {
				LoadingRankType type; 
				LoadingRankSeq LoadingRank;
			};
			//扩展对象
			struct ExtendObject{
				long 		auctionMoney;		//	 拍卖币
				int 		sevenTarget;		//	 七日目标状态(0:未完成 1：全部完成)
				StringSeq 	extraHeadImage; 	//	 额外获得头像(eg:比武大会第一名, 成就累计活跃度赠送)
				StringSeq 	extraHeadBorder; 	//	 获得的头像边框
				int			nanHuaLing;			//	南华令
			};
			struct RoleView{
				IntSeq completedGuides;	//已完成的操作引导步骤列表
				int sex;
				string headImg;
				ActivityAnnounceViewSeq activities;
				
				string id;
				string supportId;
				string name;
				short level;
				int vip;			//vip等级
				int topupYuanbao;	//玩家充值获得的元宝数
				int exp;
				int levelupExp;
				short rank;			//官阶
				int remainSalaryTimes; //今日剩余领取俸禄次数
				int compositeCombat;//综合战力
				int yuanbao;
				long jinbi;
				
				HeroViewSeq heroList;
				ItemViewSeq items;
				FormationViewSeq formations;
				int vit;	//行动力
				int vitNum;	//行动力 购买次数
				bool hasFaction;		//是否有公会
				bool hasFirstCharge;	//是否有首充
				
				string extend;          //ExtendObject的lua格式
				
				int maxTournamentRank; // 比武大会历史最高排名，0 表示没有排名
				long createDate;//创建时间 秒
				long levelUpDate;//升级时间 秒
			};
			sequence<RoleView> RoleViewSeq;
			
			struct HeroEquipView{
				string id; //hero id
				string itemId; //装备id
				int level; //强化等级
				int star; //星级
				int starExp; //星级经验
				float grow1; //重铸属性1
				float grow2; //重铸属性2
				string gemNames; //镶嵌宝石名称，用逗号分隔
			};
			
			sequence<HeroEquipView> HeroEquipViewSeq;
			
			struct RoleViewForGM{
				RoleView baseView;
				IntStringSeq copyProgresses;
				AccountView account;
				string createTime;
				string lastLoginTime;
				int expForBeiFa;
				int arenaMoney;
				long moneyForAuction;
				bool online;
				HeroEquipViewSeq heroEquipSeq;
				ItemViewSeq itemView4GMSeq;
			};
			
			// 武将装备
			struct HeroEquips{
				int heroId;
				ItemViewSeq equips;
			};
			sequence<HeroEquips> HeroEquipsSeq;
			
			// 查看其他玩家武将信息(武将列表+装备列表)
			struct OthersHeroView {
				HeroViewSeq heroList;
				HeroEquipsSeq heroEquips;
			};
			
			
			
			interface RoleCallback{
				//显示改名UI
				["ami"] void showRenameUI();
				
				//经验，金币，元宝等数据变更通知
				["ami"] void rolePropertyChange(Property pro);
				
				//string类型数据变更通知
				["ami"] void strPropertyChange(string code, string value);
				
				//武将数据变更通知，如穿装备，激活缘分等都会触发
				["ami"] void heroChange(HeroView view);
				
				//物品变更，可能只是数量改变，也可能是新增加物品
				["ami"] void itemChange(ItemView view);
				
				["ami"] void showRedPointOnMajorUI(MajorUIRedPointNoteSeq points);
				
				//显示tips提示
				["ami"] void showTips(string tips);
				
				//显示成就提示
				["ami"] void showAchieves(IntSeq achieves);
				
				//推送通知
				["ami"] void pushMsgs(string msgs);
				 
				//登录加载显示排行榜  
				["ami"] void loginRankList(LoadingRankList listShow);
				
				//公会副本挑战者变更  roleName=null表示没有人挑战
				["ami"] void factionCopyState(string roleName,string icon,int vipLevel);
			};
			
			interface Role{
				//游戏服连接建立后，应首先调用该方法，同时获得连接标识
				["ami"] string setRoleCallback(RoleCallback* cb)  throws NoteException ;
				
				//获取开服时间
				["ami"] StringSeq getServerOpenTime();
				
				//获取角色数据列表，没有创建角色则返回空
				["ami","amd"] RoleViewSeq getRoleViewList() throws NoteException;
				
				//获取随机主公名，这里用属性中的code表示名字，value表示需要元宝
				["ami","amd"] Property randomName(int sex);
				//设置性别、名字、邀请人的邀请码
				["ami","amd"] string setSexAndName(int sex,string name,string inviteCode) throws NoteException;
				//角色重命名
				["ami","amd"] void rename(string name) throws NoteException,NotEnoughYuanBaoException;
				
				//手动升级
				["ami"] void levelUp() throws NoteException;
				
				//阅读活动
				["ami"] bool readActivityAnnounce(int id);
				
				//领取俸禄
				["ami"] void salary() throws NoteException;
				
				//查看其他玩家信息
//				["ami"] RoleViewForOtherPlayer getOtherPlayInfo(string targetId);
				["ami","amd"] string getOtherPlayInfo(string targetId) throws NoteException;
				
				//设置系统自带头像
				["ami"] void setHeadImage(string img) throws NoteException;
				
				//设置头像边框
				["ami"] void setHeadBorder(string border) throws NoteException;
				
				//获取指定编号单挑战报，这里用json格式
//				["ami"] DuelReportView getReportView(string reportId) throws NoteException; 
				["ami"] string getReportView(string reportId) throws NoteException;
				
				//完成引导步骤
				["ami"] void completeGuide(int guideId);
				
				//获取开篇剧情单挑数据
				["ami"] SceneDuelViewSeq openCeremony(int id)throws NoteException;
				
				//获取单挑策略配置
				["ami"] DuelSkillTemplateViewSeq getDuelStrategyConfig() throws NoteException;
				
				//自定义心跳协议
				["ami"] void xsgPing() throws NoteException;
				
				//处理二次确认
				["ami","amd"] void resetRole() throws NoteException;
				
				//活动公告
				["ami"] ActivityAnnounceViewSeq getActivityAnnounce();
				
				/**获取双倍卡剩余秒数,返回IntIntPairSeq的lua,0-经验 1-掉落*/
				["ami"] string getDoubleCardTime() throws NoteException;
				
				//微信分享通知服务器
				["ami"] void shareWeixin() throws NoteException;
				
				// 查看玩家的全部武将
				["ami", "amd"] OthersHeroView getRoleHeros(string roleId) throws NoteException;
			};
		};
	};  
};

#endif
