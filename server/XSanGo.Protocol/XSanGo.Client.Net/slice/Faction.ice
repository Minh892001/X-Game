//公会协议
#ifndef  _FACTION_ICE_
#define  _FACTION_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			struct FactionListView{
				string id;
				string icon;
				string name;
				int level;
				string qq;
				int memberSize;
				bool apply;
				int maxPeople;// 最大人数
				int joinLevel;// 入会主公等级
				string bossName;// 会长名字
				int bossVip;// 会长VIP
				int joinVip;// 入会VIP等级
				string manifesto;//公会宣言
			};
			sequence<FactionListView> FactionListViewSeq;
			
			struct FactionReqView{
				string reqId;		//申请记录ID
				string candidateId;	//申请者角色ID
				string name;
				int level;
				int vipLevel;
				string headImage;
			};
			sequence<FactionReqView> FactionReqViewSeq;
			
			struct FactionMemberView{
				string id;
				string roleId;
				string name;
				string headImage;
				int level;
				int duty;//职位 5-帮主 3-长老 0-普通
				int vipLevel;
				int contribution;//贡献
				int pastMinute;//上次登录过去分钟
				int honor;// 荣誉
				int canAllotItemNum;// 还可分配物品数量
				string demandItemId;//索要物品ID
			};
			sequence<FactionMemberView> FactionMemberViewSeq;
			
			
			struct FactionView{
				string id;
				string icon;
				string name;
				int level;
				int currentExp;
				int levelUpExp;
				string qq;
				string announce;
				int joinType;//0-直接加入 1-验证加入 2-拒绝加入
				int joinLevel;// 入会主公等级
				int maxPeople;//公会最大会员数
				int contribution;//贡献
				int honor;//荣誉
				int renamePrice;//改名价格
				int canSendMailCount;// 可发送邮件次数
				FactionMemberViewSeq members;
				int joinVip;// 入会VIP等级
				string manifesto;// 公会宣言
				bool isRedPoint;// 是否公会战红点
				bool technologyRedPoint;// 是否有公会科技红点
				int deleteDay;//自动移出天数0-无限制
			};
			
			//公会动态
			struct FactionHistoryView{
				string time;//日期 MM-dd HH:mm
				string roleName;
				string content;
				int vipLivel;//控制名字显示颜色
			};
			
			//副本怪物
			struct MonsterView{
				string id;
				int blood;//血量
				int anger;//怒气
			};
			sequence<MonsterView> MonsterViewSeq;
			
			// 加成类型
			enum AdditionType{
				NONE,//无加成
				WEIGUO,//魏国武将加成
				SHUGUO,//蜀国武将加成
				WUGUO,//吴国武将加成
				OTHER//群英武将加成
			};
			
			//公会副本进度信息
			struct FactionCopyInfoView{
				int stageNum;//关卡序列
				int progress;//进度%
				IntIntPair challengeNum;//挑战次数
				string roleName;//正在挑战的玩家
				string icon;//正在挑战玩家头像
				int vipLevel;//正在挑战玩家的VIP等级
				int limitTime;//额外奖励剩余时间 分钟
				AdditionType addType;//加成类型
				int addValue;//加成值%
			};
			
			//公会副本挑战结果
			struct FactionCopyResultView{
				int copyId;//副本ID
				int stageNum;//关卡序列
				MonsterViewSeq monsters;//长度为0时表示初始状态即所有怪物满血0怒气
				ItemViewSeq items;		//挑战掉落物品
				ItemViewSeq killItems;	//首次打死小怪掉落物品
				ItemViewSeq bloodItems;	//boss掉血量达到时首次掉落物品
				AdditionType addType;//加成类型
				int addValue;//加成值%
			};
			
			struct FactionShop{
            	int id;
            	string itemId;
            	string name;
            	int num;
            	int price;//总价
            	bool isBuy;//是否已购买
            	int coinType;//货币类型 1-贡献2-荣誉3-免费
            	int free;//免费领取条件，-1表示需要使用price购买
            };
            sequence<FactionShop> FactionShopSeq;
			// 公会商店View
			struct FactionShopView{
            	string refreshStr;//刷新描述
            	FactionShopSeq factionShops;
            };
            
            // 公会战信息VIEW
            struct GvgView{
            	bool isBegin;//公会战是否开战
            	int beginSecond;//开战倒计时
            	bool isCanApply;//是否可报名
            	bool isApply;//是否已报名
            	string stopTime;//截止报名时间
            	// isBegin=true以下属性才生效
            	int deathScond;//大于0时需等待复活
            	int currentNum;//当前人数
            	int applyNum;//报名人数
            	int myRank;
            	int factionRank;
            	int myHonor;
            	int factionHonor;
            	int revivePrice;//复活费用
            	int endSecond;//结束倒计时 秒
            };
            
            // 公会战对手VIEW
            struct RivalView{
            	string id;
            	string name;
            	string headImage;
            	string factionName;
            	int level;
            	int vipLevel;
            	int power;//战斗力
            };
            
            // 个人排行榜项
            struct MemberRankElement{
            	int rank;//排名
            	string id;
            	string name;
            	string headImage;
            	int honor;//荣誉
            	int vipLevel;
            	string factionName;
            };
            sequence<MemberRankElement> MemberRankElementSeq;
            
            // 公会战个人排行榜VIEW
            struct MemberRankView{
            	int myRank;
            	string myName;
            	int myHonor;
            	string myFactionName;
            	MemberRankElementSeq elements;
            };
            
            //公会排行榜项
            struct FactionRankElement{
            	int rank;//排名
            	string id;
            	string factionName;
            	string headImage;
            	int honor;//荣誉
            	int peopleNum;//参战人数
            };
            sequence<FactionRankElement> FactionRankElementSeq;
            
            // 公会战公会排行榜VIEW
            struct FactionRankView{
            	int myRank;
            	string myFactionName;
            	int myFactionHonor;
            	int myPeopleNum;
            	FactionRankElementSeq elements;
            };
            
            // 公会战战斗VIEW
            struct GvgChallengeView{
				string roleId;
				string roleName;
				string headImg;
				int sex;
				int vipLevel;
				int level;
				PvpOpponentFormationView formationView;
			};
			
			//公会副本伤害排行榜
			struct CopyHarmRankView{
				int rank;//排名
				int harm;//伤害
				int scale;//比例
				FactionMemberView member;
			};
			
			/**公会邮件发送记录*/
			struct FactionMailLog{
				string roleId;
				string roleName;
				int vipLevel;
				int sendCount;
			};
			sequence<FactionMailLog> FactionMailLogSeq;
			
			/**公会仓库物品*/
			struct WarehouseItem{
				int id;
				bool isOpen;
				int openLevel;//开启等级
				string itemId;
				long itemNum;
				int queueSize;//排队人数
				bool isApply;//是否已申请
				bool isDemand;//是否索要
			};
            sequence<WarehouseItem> WarehouseItemSeq;
            
            /**公会仓库view*/
			struct FactionWarehouseView{
				int level;
				int volume;// 已有容量
				int maxVolume;// 最大容量
				WarehouseItemSeq items;
			};
			
			/**公会栈房物品*/
			struct StorehouseItem{
				bool isOpen;
				int openLevel;// 开启等级
				string itemId;
				string itemName;
				int price;// 粮草价格
			};
			sequence<StorehouseItem> StorehouseItemSeq;
			  
			/**公会栈房view*/
			struct FactionStorehouseView{
				int level;
				int score;// 粮草
				StorehouseItemSeq items;
			};
			
			/**公会商铺物品*/
			struct OviStoreItem{
				string itemId;
				string itemName;
				int num;
				int price;// 荣誉价格
				long time;
			};
			sequence<OviStoreItem> OviStoreItemSeq;
			
			/**公会商铺view*/
			struct FactionOviStoreView{
				int myHonor;// 我的荣誉
				OviStoreItemSeq items;
			};
			
			/**公会科技技能*/
			struct FactionTechnology{
				int id;
				int level;//等级
				int exp;//经验
				bool isCanStudy;//是否可研究
				bool isOnStudy;//是否研究中
				int studyProgress;//研究进度 最大100
				int studySecond;//已研究时长 秒
				bool isRecommend;//是否推荐
				bool canYuanbaoDonate;//是否能元宝捐赠
				bool isMaxLevel;//是否已满级
			};
			sequence<FactionTechnology> FactionTechnologySeq;
			
			/**公会科技view*/
			struct FactionTechnologyView{
				FactionTechnologySeq technologys;
				int studyNum;//研究次数
				int weizhangNum;//微章数量
				bool hasCd;//是否有CD
				int cd;//CD秒数
				int score;// 粮草
			};
			
			/**公会仓库分配日志*/
			struct FactionAllotLog{
				string datetime;
				string bossName;
				int bossVip;
				string targetName;//被分配者
				int targetVip;//被分配者Vip
				string itemName;//物品名
			};
			sequence<FactionAllotLog> FactionAllotLogSeq;
			
			/*******************************新公会战协议*******************************/
			/** 公会战报名界面 */
			struct FactionBattleShow {
				byte		state;					// 0未开始报名 1已开始报名 2 已开战 
				bool 		isEnroll;				// 是否报名
				int			time;					// 开战倒计时 秒
				int			changeCampPrice;		// 更换阵营价格
				bool		isCanChangeCamp;		// 是否能更换阵营 
				string		campName;				// 阵营
				string		campIcon;				// 阵营图标
			};
			
			/** 报名结果 */
			struct EnrollResult {
				string		campName;				// 阵营
				string		campIcon;				// 阵营图标
				int 		time;					// 开战倒计时 秒
			};
			
			/** 各节点状态 */
			struct StrongHoldState {
				int 		strongholdId;			// 据点编号
				byte		state;					// 状态 0无人占领 1许昌阵营 2成都阵营3建业阵营4怪物阵营
				byte		campId;					// 自身所在的阵营
			};
			sequence<StrongHoldState> StrongHoldStateSeq;
			
			/** 公会战锦囊 */
			struct FactionBattleKitsView {
				int			kitsId;					// 编号
				int			kitsNum;				// 数量
				int			cdTime;					// CD时间 秒
			};
			sequence<FactionBattleKitsView> FactionBattleKitsViewSeq;
			
			/** 公会战据点界面数据 */
			struct FactionBattleStrongholdView {
				bool				isBaseCamp;			// 是否大本营
				int					debuffLvl;			// 玩家自身的debuff等级
				int 				factionRoleNum;		// 当前据点自身阵营人数
				int					attackTime;			// 攻击倒计时：秒
				bool				isHiddenAttack;		// 是否置灰攻方按钮
				int 				occupyIncomePer;	// 占领收益百分比
				int					curRoleNum;			// 当前人数
				int					maxRoleNum;			// 最大人数
				string				stateName;			// 状态名称
				int					incomePer;			// 收益百分比
				int					attackRoleNum;		// 攻方人数
				int					defendRoleNum;		// 守方人数
			};
			
			/** 公会战随机事件 */
			struct FactionBattleEventView {
				int					strongholdId;		// 据点编号
				string				eventIcon;			// 事件图标
			};
			sequence<FactionBattleEventView> EventViews;
			
			/** 公会战场 */
			struct FactionBattleView {
				int			surplusTime;			// 战场剩余时间 秒
				int			marchingCoolingTime;	// 行军冷却时间 秒
				int			diggingTreasureTime;	// 挖宝冷却时间 秒
				int 		marchingNum;			// 行军次数
				int  		strongholdId;			// 当前所在据点
				int 		badge;					// 徽章
				int 		forage;					// 粮草
				bool 		isOpenMarching;			// 是否开启行军冷却 0否1是
				FactionBattleKitsViewSeq	kitses;	// 锦囊列表
				StrongHoldStateSeq			states;	// 各个据点状态
				FactionBattleStrongholdView views;	// 默认打开据点角色列表界面 大本营无须显示 满足协议格式
				FactionBattleEventView		eView;	// 据点随机事件组
			};
			
			/** 公会战排行奖励 */
			struct FactionBattleRankAwardView {
				string 			rank;					// 名次
				IntStringSeq	items;					// 奖励集
			};
			sequence<FactionBattleRankAwardView> RankAwardViews;
			
			/** 公会战排行数据 */
			struct FactionBattleRankView {
				int			rank;					// 名次
				string		factionName;			// 公会名称
				int			badge;					// 徽章
			};
			sequence<FactionBattleRankView> RankViews;
			
			/** 公会战排行榜 */
			struct FactionBattleRankResultView {
				RankAwardViews 		rankAwardResults;	// 奖励数据
				RankViews			rankResults;		// 排行数据
				int					rank;				// 本会所在排名 0未入榜
			};
			
			/** 公会战个人排行数据结构 */
			struct FactionBattlePersonalRankView {
				int					rank;				// 名次
				string				roleId;				// 角色编号
				string				roleName;			// 角色名
				string				headIcon;			// 头像
				int					vipLevel;			// VIP等级
				int					roleLevel;			// 角色等级
				int 				badge;				// 徽章
			};
			sequence<FactionBattlePersonalRankView> PersonalRankViews;
			
			/** 公会战个人排行榜 */
			struct FactionBattlePersonalRankResultView {
				PersonalRankViews	ranks;				// 排行数据
				int					rank;				// 自己所在排名 0未入榜
				int					badge;				// 自己本场获取的徽章
				bool				isShowAward;		// 是否显示奖励数据
				RankAwardViews 		rankAwardResults;	// 奖励数据
				byte				selectedIndex;		// 选中的位置 0开始
			};
			
			/** 锦囊变更通知数据 */
			struct KitsChangeView {
				int					kitsId;				// 锦囊编号
				int					num;				// 数量变更，负数为减少
			};
			sequence<KitsChangeView> KitsChangeViews;
			
			/** 战斗结果数据 */
			struct FactionBattleResultView {
				bool 				isWin;				// 是否胜利
				string				content;			// 抬头消息内容
				int					killNum;			// 连斩次数 注：胜利有效
				int					cd;					// 挖宝CD 秒 
				int                 badge;				// 徽章 注：胜利有效
				string				debuffDesc;			// Debuff描述 注：胜利有效
				int					strongholdId;		// 据点编号 注：失败有效
				//string				campName;			// 阵营	注：失败有效
				//string				campIcon;			// 阵营图标	注：失败有效
				int					recoveryTime;		// 复活时间 秒 注：失败有效
			};
			
			/** 公会战日志 */
			struct FactionBattleLogView {
				string				time;				// 时间
				string				content;			// 内容
			};
			sequence<FactionBattleLogView> FactionBattleLogs;
			
			/** 公会战战报结果 */
			struct PvpMovieView {
				string 				winRoleId;
				int 				selfHeroNum; // 剩余武将数量
			    SceneDuelViewSeq 	soloMovie;
			    ByteSeq 			fightMovie;
			    int 				winType;
			};
			
			/**捐赠日志*/
			struct TechnologyDonateLog{
				string id;
				string name;
				string icon;
				int level;
				int vipLevel;
				int weizhang;//微章捐赠数量
				int yuanbao;//元宝捐赠数量
			};
			sequence<TechnologyDonateLog> TechnologyDonateLogSeq;
			
			/**捐赠日志VIEW*/
			struct TechnologyDonateView{
				TechnologyDonateLogSeq today;
				TechnologyDonateLogSeq week;
				TechnologyDonateLogSeq history;
			};
			
			/**仓库购置日志*/
			struct PurchaseLog{
				string datetime;
				string roleName;
				int roleVip;
				string remark;
			};
			sequence<PurchaseLog> PurchaseLogSeq;
			
			interface FactionCallBack {
				/** 据点状态通知 */
				// ["ami"] void strongholdStateNotify(StrongHoldStateSeq states);
				["ami"] void strongholdStateNotify(string stateMsg);
				
				/** 公会战排行结果通知 */
				// ["ami"] void factionBattleRankResultNotify(FactionBattleRankResultView view);
				["ami"] void factionBattleRankResultNotify(string result);
				
				/** 据点角色列表动态刷新 */
				// ["ami"] void strongholdRoleListChangeNotify(FactionBattleStrongholdView result);
				["ami"] void strongholdRoleListChangeNotify(string result);
				
				/** 行军时间通知 */
				["ami"] void strongholdMarchingTimeNotify(int time);
				
				/** 挖宝时间通知 */
				["ami"] void diggingTreasureTimeNotify(int time);
				
				/** 锦囊获取消息通知 */
				["ami"] void gainKitsMessageNotify(string content, string icon);
				
				/** 锦囊变更通知包 */
				// ["ami"] void kitsChangeNotify(KitsChangeViews views);
				["ami"] void kitsChangeNotify(string views);
				
				/** 公会战消息通知 */
				["ami"] void factionBattleMessageNotify(string message);
				
				/** 公会战公会资源变更通知 各数值为当前最终数值 徽章+粮草 */
				["ami"] void factionBattleResourceNotify(int badge, int forage);
				
				/** 公会战事件通知  第一个参数：抹去给定据点事件（大于0有效），第二个参数：新增事件的据点（大于0有效），第三个参数：新增事件的图标*/
				["ami"] void factionBattleEventNotify(int strongholdId, int addStrongholdId, string eventIcon);
				
				/** 公会战斗结果通知  */
				// ["ami"] void factionBattleResultNotify(FactionBattleResultView view);
				["ami"] void factionBattleResultNotify(string result);
				
				/** 公会战连杀通知播放特效 */
				["ami"] void factionBattleEvenkillNotify();
				
				/** 行军冷却解封通知 */
				["ami"] void openMarchCoolingNotify();
			};
			
			interface Faction{
				//创建帮派
				["ami","amd"] void createFaction(string name,string icon) throws NoteException,NotEnoughYuanBaoException;
				
				/**获取帮派列表，返回FactionListViewSeq的lua orderBy=0-人数 1-等级 2-随机*/
				["ami","amd"] string getFactionList(int orderBy);
				
				//申请加入指定帮派
				["ami"] void applyFor(string factionId) throws NoteException;
				//取消申请
				["ami"] void cancelApplication(string factionId);
				//获取我的帮派信息 返回FactionView的lua
				["ami","amd"] string getMyFaction() throws NoteException;
				
				//获取入会申请列表 返回FactionReqViewSeq的lua
				["ami","amd"] string getJoinRequestList() throws NoteException;
				
				//批准某人加入帮派
				["ami","amd"] void approveJoin(string applyId) throws NoteException;
				
				//拒绝某人的申请
				["ami"] void denyJoin(string applyId);
				
				/**退出公会*/
				["ami"] void divorce() throws NoteException;
				
				//开除某人
				["ami"] void deleteMember(string roleId) throws NoteException;
				
				/**修改公会公告*/
				["ami"] void updateNotice(string newNotice) throws NoteException;
				
				/**查找公会 返回FactionListViewSeq的lua*/
				["ami","amd"] string findFaction(string idOrName) throws NoteException;
				
				/**转让公会(升为会长)*/
				["ami"] void transferFaction(string targetId) throws NoteException;
				
				/**升为长老*/
				["ami"] void upElder(string targetId) throws NoteException;
				
				/**设置成普通会员*/
				["ami"] void setCommon(string targetId) throws NoteException;
				
				/**公会设置 joinType 0-直接加入 1-验证加入 2-拒绝加入*/
				["ami"] void factionConfig(string icon,string qq,string notice,int joinType,int joinLevel,int joinVip,string manifesto,int deleteDay) throws NoteException;
				
				/**查看公会动态(历史记录) 返回FactionHistoryViewSeq的lua*/
				["ami"] string getFactionHistorys();
				
				/**公会捐赠 num-捐赠数量*/
				["ami"] void donation(int num) throws NoteException;
				
				/**公会改名*/
				["ami"] void rename(string newName) throws NoteException;
				
				
				//下面是公会副本协议
				/**打开副本列表界面 返回IntIntPair,first-已开启的副本ID,second-可开启副本次数*/
				["ami"] string factionCopyList() throws NoteException;
				
				/**开启副本 返回还可开启的次数*/
				["ami"] int openFactionCopy(int copyId) throws NoteException;
				
				/**关闭开启的副本*/
				["ami"] void closeFactionCopy() throws NoteException;
				
				/**副本进度等信息 返回FactionCopyInfoView的lua*/
				["ami"] string factionCopyInfo() throws NoteException;
				
				/**开始挑战副本 返回FactionCopyResultView*/
				["ami"] FactionCopyResultView beginChallenge() throws NoteException;
				
				/**结束挑战副本 需要告诉服务器所有怪物的血量和怒气、是否击杀小怪、是否触发boss掉血量、总掉血量*/
				["ami"] void endChallenge(MonsterViewSeq monsterViews,bool isKill,bool isHurtBlood,int dropBlood) throws NoteException;
				
				/**获取公会商店商品，返回FactionShopView的lua*/
				["ami"] string getFactionShops() throws NoteException;
				
				/**购买公会商品*/
				["ami"] void buyFactionShop(int id) throws NoteException;
				
				
				/**获取公会战信息，返回GvgView的lua*/
				["ami"] string getGvgInfo() throws NoteException;
				
				/**公会战报名*/
				["ami"] void applyGvg() throws NoteException;
				
				/**选择对手，返回RivalView数组的lua*/
				["ami","amd"] string selectRival(int index) throws NoteException;
				
				/**获取公会战个人排行榜，返回MemberRankView的lua*/
				["ami","amd"] string getMemberRank() throws NoteException;
				
				/**获取公会战公会排行榜，返回FactionRankView的lua*/
				["ami"] string getFactionRank() throws NoteException;
				
				/**获取部队阵容，返回GvgChallengeView的lua*/
				["ami","amd"] GvgChallengeView getRivalFormation(string roleId) throws NoteException;
				
				/**公会战开始战斗，返回战报ID*/
				["ami"] string beginGvg() throws NoteException;
				
				/**公会战结束战斗，heroNum-剩余武将数量，返回IntIntPair的lua
				 *IntIntPair：first-星级，second-增加荣誉
				 */
				["ami"] string endGvg(bool isWin,int heroNum) throws NoteException;
				
				/**公会战死亡复活*/
				["ami"] void reviveGvg() throws NoteException,NotEnoughMoneyException,NotEnoughYuanBaoException;
				
				/**伤害排行榜，返回CopyHarmRankView数组的lua*/
				["ami","amd"] string getHarmRank() throws NoteException;
				
				//2015年12月优化功能
				/**发送公会邮件返回还可发送次数，type=0-所有,1-会长长老,2-普通人员*/
				["ami"] int sendFactionMail(int type,string title,string content) throws NoteException;
				
				/**当日公会邮件发送记录，返回FactionMailLogSeq的lua*/
				["ami"] string getFactionMailLog() throws NoteException;
				
				//2015年12月21公会宝库功能
				/**打开仓库界面，返回FactionWarehouseView的lua*/
				["ami"] string openWarehouse() throws NoteException;
				
				/**仓库分配物品*/
				["ami","amd"] void warehouseAllot(string roleId,string itemId,int num) throws NoteException;
				
				/**打开栈房界面，返回FactionStorehouseView的lua*/
				["ami"] string openStorehouse() throws NoteException;
				
				/**栈房购置物品*/
				["ami"] void storehousePurchase(string itemId,int num) throws NoteException;
				
				/**打开商铺界面，返回FactionOviStoreView的lua*/
				["ami"] string openOviStore() throws NoteException;
				
				/**商铺购买物品*/
				["ami"] void oviStoreBuy(string itemId,int num) throws NoteException;
				
				/**公会科技列表,返回FactionTechnologyView的lua*/
				["ami"] string technologyList() throws NoteException;
				
				/**设置推荐科技*/
				["ami"] int setRecommendTechnology(int id) throws NoteException;
				
				/**公会科技捐献type=0微章1元宝*/
				["ami"] void donateTechnology(int id,int type) throws NoteException;
				
				/**清除捐献CD*/
				["ami"] void clearDonateCD() throws NoteException;
				
				/**研究公会科技*/
				["ami"] void studyTechnology(int id) throws NoteException;
				
				/*******************************新公会战协议*******************************/
				/** 公会战界面 */
				// ["ami"] FactionBattleShow openFactionBattle() throws NoteException;
				["ami"] string openFactionBattle() throws NoteException;
				
				/** 报名公会战 */
				// ["ami"]	EnrollResult enrollFactionBattle() throws NoteException;
				["ami"]	string enrollFactionBattle() throws NoteException;
				
				/** 更换阵营 */
				// ["ami"] EnrollResult changeFactionBattleCamp() throws NoteException, NotEnoughYuanBaoException;
				["ami"] string changeFactionBattleCamp() throws NoteException, NotEnoughYuanBaoException;
				
				/** 进入公会战 */
				// ["ami"] FactionBattleView enterFactionBattle() throws NoteException;
				["ami"] string enterFactionBattle() throws NoteException;
				
				/** 离开公会战 */
				["ami"] void leaveFactionBattle() throws NoteException;
				
				/** 公会战排行榜信息 */
				// ["ami"] FactionBattleRankResultView lookFactionBattleRank() throws NoteException;
				["ami"] string lookFactionBattleRank() throws NoteException;
				
				/** 公会战个人排行榜信息 */
				// ["ami"] FactionBattlePersonalRankResultView lookFactionBattlePersonalRank() throws NoteException;
				["ami"] string lookFactionBattlePersonalRank() throws NoteException;
				
				/** 行军 */
				// ["ami"] FactionBattleStrongholdView marching(bool isUseKits, int strongholdId) throws NoteException;
				["ami"] string marching(bool isUseKits, int strongholdId) throws NoteException;
				
				/** 行军冷却 */
				["ami"] void buyMarchingCooling() throws NoteException,NotEnoughYuanBaoException;
				
				/** 挖宝 */
				// ["ami"] IntStringSeq diggingTreasure() throws NoteException; 
				["ami"] string diggingTreasure() throws NoteException; 
				
				/** 使用锦囊 返回为空无消息提示 否则需要消息提示*/
				["ami"] string useKits(int kitsId) throws NoteException;
				
				/** 开打  type=0守方 1攻方*/
				// ["ami","amd"] FactionBattleResultView startBattle(byte type) throws NoteException;
				["ami","amd"] string startBattle(byte type) throws NoteException;
				
				/** 战斗结果确定（复活 假象而已） */
				["ami"] void resultConfirm() throws NoteException;
				
				/** 查看战况回放 */
				["ami"] PvpMovieView lookFactionBattleMovieView() throws NoteException;
				
				/** 查看公会战日志 logType=0 战斗日志，1挖宝 */
				// ["ami"] FactionBattleLogs lookFactionBattleLog(byte logType) throws NoteException;
				["ami"] string lookFactionBattleLog(byte logType) throws NoteException;
				
				/**获取公会仓库分配日志，返回FactionAllotLog[]的lua*/
				["ami"] string getFactionAllotLog() throws NoteException;
				
				/**获取科技捐赠日志，返回TechnologyDonateView的lua*/
				["ami","amd"] string getTechnologyDonateLog() throws NoteException;
				
				/**获取栈房购置日志，返回PurchaseLogSeq的lua*/
				["ami"] string getPurchaseLog() throws NoteException;
				
				/**获取仓库物品的申请队列，返回TechnologyDonateLogSeq的lua*/
				["ami","amd"] string getWarehouseItemQueue(int id) throws NoteException;
				
				/**索要物品。type=0-索要 1-取消*/
				["ami"] void demandItem(string itemId,int type) throws NoteException;
				
				/**申请物品，进入队列排队。type=0-申请 1-取消*/
				["ami"] void applyItem(int id,int type) throws NoteException;
				
				/**获取仓库物品申请在我前面的人数*/
				["ami"] int getBeforePeople(string itemId) throws NoteException;
				
				/**招贤*/
				["ami"] bool recruit(bool isFree) throws NoteException;
				
				/**邀请*/
				["ami","amd"] bool invite(bool isFree,string roleId) throws NoteException;
				
				/**获取招贤、邀请次数。0-招贤1-邀请return:IntIntPair*/
				["ami"] string getRecruitCount(int type) throws NoteException;
				
				/**获取公会信息，返回FactionView的lua*/
				["ami","amd"] string getFactionInfo(string id);
	         };
		};
	};
};

#endif