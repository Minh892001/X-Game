#ifndef  _ARENARANK_ICE_
#define  _ARENARANK_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"
#include "CrossServer.ice"

//在线奖励
module com {
	module XSanGo{
		module Protocol{
			//对手排名
			struct RivalRank {
				string id;
				string name;
				string icon;
				int level;
				int vip;
				int sex;
				int rank;
				int compositeCombat;					//综合战力
				string groupName;						//工会名称
				float attack;	 						//进攻胜率
				float guard;	 						//防守胜率
				int sneerId;							//嘲讽类型
				string sneerStr; 						//嘲讽显示文字
				string guardId;  						//防守队伍
				string formationBuffID;					//阵法书ID
				FormationSummaryViewSeq guardHeroArr;	//防守部队 武将信息
				FormationSummaryViewSeq supportHeroArr;	//防守部队 援军信息
				
				int serverId;//服务器id，跨服竞技场使用
			};
			sequence<RivalRank> RivalRankSeq; 
			
			//竞技场排名
			struct OwnRank {
				int rank;		 	//排名
				int sneerId;	 	//嘲讽类型
				string SneerStr; 	//嘲讽显示文字
				string guardId;  	//防守队伍
				float attack;	 	//进攻胜率
				float guard;	 	//防守胜率
				int challenge;	 	//挑战令 数量
				int challengeBuy;	//挑战令 购买次数
				int fightCdTime; 	//挑战CD时间
				int challengeMoney; //排名获得的挑战币
				int clearCdNum; 	//清除CD次数
				RivalRankSeq rivalRankList; //对手排名列表
			};
			
			//竞技场 商城 道具数据
			struct ArenaMall {
				int id;			//ID
				string itemId;	//商品ID
				int num;		//商品数量
				int price;		//商品价格
				int coinType;	//商品价格,0:必须消耗竞技币,1:大于等于该vip等级可免费领取
				int flag;		//商品状态 0：已经售完，1：可以购买
			};
			sequence<ArenaMall> ArenaMallSeq;
			
			struct ArenaMallSel {
				int exchangeRefreshNum; 	//竞技场 商城 刷新次数
				ArenaMallSeq ArenaMallList;	//竞技场 商城 道具列表
			};
			
			//战报 属性描述类
			struct ArenaReportView { 
				string id; 
				string name;
				short level;
				string icon;
				int vip;
				int sex;
				int rank;
				int flag;			//胜败 0：输，1：赢
				int compositeCombat;//综合战力
				int rankChange;		//排名变化, 负数：下降名次
				int sneerId;		//炫耀ID,是否炫耀
				PropertySeq reward;	//获得奖励物品参数，格式和邮件附件相同
				long reportTime;	//战报发生时间
				string reportId;	//战报详情ID
				string fightId;		//战报显示ID
				string fightMovieId; // 战报录像ID
				int type;           // 战报类型 0：主动发起，1：被动接受挑战, 2: 主动复仇, 3: 被复仇
				
				int serverId;//服务器id，跨服竞技场使用
				string signature;//个性签名
			};
			sequence<ArenaReportView> ArenaReportViewSeq;

			//战斗结束活动的奖励
			struct FightResult {
				int maxNum;			//历史最高数量
				int historyRank;	//历史最高排名
				int maxRank;		//当前高排名
				
				int firstWinNum;	//首胜数量
				int firsChangeRank;	//排名变化
				
				int sneerNum;		//嘲讽获得竞技币 数量
				int sneerhangeRank;	//嘲讽排名变化

				int fightStar;		//战斗结果的星级

			    string reportMovieId;     // 战报ID
			};
			
			// 战报数据
			struct FightMovieView {
    			int isWin; // 战斗结果，0:失败，1：胜利
    			int starCount; // 星级
			    SceneDuelViewSeq soloMovie;
			    ByteSeq fightMovie;
			    ByteSeq validateMovie;
			};
			sequence<FightMovieView> FightMovieViewSeq;
			
			// 新规则战斗结果
			struct FightResultView {
				FightResult result;
				//PvpOpponentFormationView opponentFormation;
				FightMovieByteViewSeq movie;
				int winType;
			};
			
			/**跨服竞技场PVP战斗VIEW*/
			struct CrossArenaPvpView{
				CrossRoleView roleView;
				PvpOpponentFormationView pvpView;
				int rank;
			};
			sequence<CrossArenaPvpView> CrossArenaPvpViewSeq;
			
			/**跨服竞技场回调*/
			interface CrossArenaCallback {
				/**更新跨服竞技场数据*/
				["ami"] RivalRank updateArena(RivalRank rank,PvpOpponentFormationView pvpView);
				
				/**获取玩家跨服竞技场数据*/
				["ami"] RivalRank getRoleRivalRank(string roleId);
				
				/**获取玩家保存的阵容信息*/
				["ami"] PvpOpponentFormationView getRolePvpView(string roleId);
				
				/**获取跨服竞技场排行榜*/
				["ami"] RivalRankSeq getArenaRank(int size);
				
				/**刷新对手*/
				["ami"] RivalRankSeq refreshRival(string roleId);
				
				/**获取双方竞技场PVPView*/
				["ami"] CrossArenaPvpViewSeq getCrossArenaPvpView(string leftRoleId,string rightRoleId);
				
				/**结束战斗，返回当前排名和排名变化*/
				["ami"] IntIntPair endFight(string sourceRoleId, bool isWin, string rivalRoleId, string movieId, FightMovieView movieView);
				
				/**设置个性签名*/
				["ami"] void setSignature(string roleId, string signature);
				
				/**获取跨服竞技场战报*/
				["ami","amd"] FightMovieView getCrossMovie(string id) throws NoteException;
			};
			
			/**跨服竞技场通信*/
			interface CrossArena {
				["ami"] bool ping(long time);
				["ami"] void setCallback(CrossArenaCallback *cb);
				["ami","amd"] void initRank();
				
				/**增加战斗记录*/
				["ami"] void addCrossArenaLog(string roleId, ArenaReportView report);
			};
			
			interface ArenaRank {
				//竞技场排名查询
				//["ami","amd"] OwnRank selectRank() throws NoteException;
				["ami","amd"] string selectRank() throws NoteException;	
				
				//刷新对手排行榜
				//["ami"] RivalRankSeq selectRivalRank() throws NoteException;
				["ami","amd"] string selectRivalRank() throws NoteException;
				
				//保存防守队伍
				["ami"] void saveGuard(string guardId) throws NoteException;
				
				//设置嘲讽模式
				["ami"] void setSneer(int sneerId, string sneerStr) throws NoteException,
						NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				//购买挑战令 
				["ami"] void buyChallenge() throws NoteException, NotEnoughMoneyException, 
						NotEnoughYuanBaoException;
				
				//排名榜显示,显示1到100名
				//["ami","amd"] RivalRankSeq selHundredRank() throws NoteException;
				["ami","amd"] string selHundredRank() throws NoteException;
				
				//排行兑换列表
				//["ami"] ArenaMallSel selMallList() throws NoteException;
				["ami"] string selMallList() throws NoteException;
				
				//刷新兑换列表
				//["ami"] ArenaMallSel refMallList() throws NoteException;
				["ami"] string refMallList() throws NoteException;
				
				//兑换, 目前数量是全部兑换，预留参数
				//["ami"] ArenaMallSel exchangeItem(int storId) throws NoteException;
				["ami"] string exchangeItem(int storId) throws NoteException;
				
				//排名赛 战报列表
				//["ami"] reportViewSeq robFightReport() throws NoteException;
				["ami","amd"] string robFightReport() throws NoteException;
				
				//复仇 获取对手数据
				["ami","amd"] PvpOpponentFormationView beginRevenge(string targetId, 
									string formationId) throws NoteException;
				
				//复仇 战斗结束，结果通知，targetId:对手ID，resFlag:战斗结果，0:失败，1：胜利
				//复仇返回的数据，只有历史最高排名
				//["ami"] FightResult endRevenge(string targetId, int resFlag) throws NoteException;
				["ami", "amd"] string endRevenge(string targetId, int resFlag, byte remainHero) throws NoteException;
				
				//复仇 生成战报
				["ami","amd"] FightResultView revenge(string targetId, 
									string formationId) throws NoteException;
				
				//炫耀,reportId:报ID，channel：聊天频道，targetId:私聊的玩家ID,content：炫耀的字符 
				["ami"] void strutReport(string reportId, int channelType, 
							string targetId, string content) throws NoteException,
								NoGroupException, NoFactionException;
				
				//产看战报
				
				//每日排行发放奖励
				
				//获取对手数据 ,参数：对手ID和阵容ID 
				["ami","amd"] PvpOpponentFormationView beginChallenge(string targetId, 
										string formationId) throws NoteException, 
										NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				//战斗结束，结果通知，
				//参数：targetId:对手ID，resFlag:战斗结果，0:失败，1：胜利
				//["ami"] fightResult endChallenge(string targetId, int resFlag) throws NoteException, Exception;
				["ami", "amd"] string endChallenge(string targetId, int resFlag, byte remainHero) throws NoteException;
				
				// 新的战斗机制
				["ami","amd"] FightResultView challenge(string targetId, 
										string formationId) throws NoteException, 
										NotEnoughMoneyException, NotEnoughYuanBaoException;
				 
				//清除挑战的CD时间 
				["ami"] void clearCD() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 上传战斗录像
				["ami"] void uploadFightMovie(string id, FightMovieView movie) throws NoteException;
				
				// 查看战报录像
				["ami","amd"] FightMovieViewSeq getFightMovie(string id) throws NoteException;
				
				
				//跨服竞技场协议
				/**进入跨服竞技场，return OwnRank的lua*/
				["ami","amd"] string enterCrossArena() throws NoteException;
				
				/**刷新跨服竞技场对手，return RivalRankSeq的lua*/
				["ami","amd"] string refreshCrossRival() throws NoteException;
				
				/**获取跨服竞技场排行榜，return RivalRankSeq的lua*/
				["ami","amd"] string getCrossRank() throws NoteException;
				
				/**设置个性签名*/
				["ami"] void setSignature(string signature) throws NoteException;
				
				/**保存跨服竞技场阵容*/
				["ami","amd"] void saveBattle() throws NoteException;
				
				/**购买跨服竞技场挑战次数*/
				["ami"] void buyCrossChallenge() throws NoteException;
				
				/**获取跨服竞技场战报 return ArenaReportViewSeq的lua*/
				["ami","amd"] string getCrossReport() throws NoteException;
				
				/**跨服竞技场挑战*/
				["ami","amd"] FightResultView crossFight(string rivalId) throws NoteException;
				
				/**跨服竞技场复仇*/
				["ami","amd"] FightResultView crossRevenge(string rivalId) throws NoteException;
				
				/**查看跨服竞技场战报*/
				["ami","amd"] FightMovieViewSeq getCrossMovie(string id) throws NoteException;
				
				/**清除跨服竞技场CD*/
				["ami"] void clearCrossCD() throws NoteException;
			};
		};   
	};
};

#endif
