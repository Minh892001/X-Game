#ifndef  _ACTIVITY_ICE_
#define  _ACTIVITY_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{ 
	module XSanGo{
		module Protocol{
			//活动信息
			struct ActivityInfoView{
				int id;
				string name;
				int type;//0-不开放 1-长期开放 2-限时开放
//				string startTime;
//				string endTime;
				string icon;//图标
			};
			
			sequence<ActivityInfoView> ActivityInfoViewSeq;
			
			//升级奖励内容信息
			struct UpActivityInfoView{
				int id;
				int level;
				int yuanbao;
				int vip;
				bool draw;//是否领取过
			};
			
			//累计类活动的单项奖励描述
			struct SummationReward{
				int threshold ;		//领取奖励的标准值
				bool received;		//是否已领取
				ItemViewSeq items;	//奖励预览
			};
			sequence<SummationReward> SummationRewardSeq; 
			
			//充值或消费等累计活动数据
			struct SummationActivityView{
				int total;			//当前已累计值
				int remainSeconds;	//活动剩余时间，单位：秒
				SummationRewardSeq configs;	//活动配置奖励项
			};
			
			//邀请活动数据
			struct InviteActivityView{
				SummationRewardSeq configs;	//活动配置奖励项
				int total;					//当前已累计值
				string inviteCode;			//要求码
			};
			
			// 成长基金奖励
			struct FundRewardView {
				int level; // 需要等级
				int reward; // 奖励元宝数量
				bool received; //是否已领取
			};
			sequence<FundRewardView> FundRewardViewSeq;

			// 成长基金
			struct FundView {
				int price; // 基金金额
				bool hasBuy; // 是否已购买基金
				FundRewardViewSeq rewards; // 奖励
			};
			
			// 冲级有奖
			struct LevelRewardItemView {
				int level;// 对应的等级
				bool hasReceived; // 是否已经领取
				string rewardTemplateId; // 奖励
			};
			sequence<LevelRewardItemView> LevelRewardItemViewSeq;
			struct LevelRewardView {
				long lastTime; // 剩余时间, 如果为负数则表示永久有效
				int level; // 当前主公等级
				LevelRewardItemViewSeq rewardItems; // 奖励
			};
			
			// 第一佳
			struct FirstJiaItemView {
				int level;// 对应的等级
				bool hasReceived; // 是否已经领取
				string rewardTemplateId; // 奖励
			};
			sequence<FirstJiaItemView> FirstJiaItemViewSeq;
			struct FirstJiaView {
				long lastTime; // 剩余时间, 如果为负数则表示永久有效
				int level; // 当前主公等级
				FirstJiaItemViewSeq rewardItems; // 奖励
			};
			
			// 每日登录
			struct DayLoginItemView {
				int day;// 对应的天数
				bool hasReceived; // 是否已经领取
				string rewardTemplateId; // 奖励
			};
			sequence<DayLoginItemView> DayLoginItemViewSeq;
			struct DayLoginView {
				long startTime; // 开始时间
				long endTime; // 结束时间
				int day; // 当前天数
				DayLoginItemViewSeq rewardItems; // 奖励
				int levelLimit; // 等级限制
			};
			
			// 永久每日登录
			struct DayforverLoginItemView {
				int day;// 对应的天数
				bool hasReceived; // 是否已经领取
				string rewardTemplateId; // 奖励
			};
			sequence<DayforverLoginItemView> DayforverLoginItemViewSeq;
			struct DayforverLoginView {
				long startTime; // 开始时间
				long endTime; // 结束时间
				int day; // 当前天数
				DayforverLoginItemViewSeq rewardItems; // 奖励
				int levelLimit; // 等级限制
			};
			
			// 领军令活动
			struct SendJunLingItemView {
				int id; // 索引
				string startHour; // 开始
				string endHour; // 结束
				bool hasReceived; // 是否已经领取
				int num; // 奖励数量
			};
			sequence<SendJunLingItemView> SendJunLingItemViewSeq;
			struct SendJunLingView {
				long currentTime; // 当前时间
				SendJunLingItemViewSeq sendJunLingItems; // 奖励
				int levelLimit; // 等级限制
			};
			
			// 战斗力嘉奖
			struct PowerRewardItemView {
				int power; // 对应的战力
				bool hasReceived; // 是否已经领取
				string rewardTemplateId; // 奖励
			};
			sequence<PowerRewardItemView> PowerRewardItemViewSeq;
			struct PowerRewardView {
				long lastTime; // 剩余时间
				int power; // 当前战力
				PowerRewardItemViewSeq rewardItems; // 奖励
			};
			
			//秒杀物品
			struct SeckillItemView{
				int id;
				string itemId;
				int price;
				int maxNum;//总数量
				int remainNum;//剩余数量
				int buyable;//0-可抢1-已抢2-未开放
				string dateDesc;//秒杀时间描述
			};
			sequence<SeckillItemView> SeckillItemViewSeq; 
			//秒杀活动
			struct SeckillActivityView{
				string seckillDate;
				int remainSecond;//结束倒计时 秒
				int startRemainSecond;// 开始 时间倒计时
				SeckillItemViewSeq items;
			};
			
			// 幸运大转盘
			struct FortuneWheelRewardItemView {
				int id; // ID
				string itemTemplateId; // 奖励的模版ID
				int num; // 奖励数量
				int itemType; // 权重,万分比形式
			};
			sequence<FortuneWheelRewardItemView> FortuneWheelRewardItemViewSeq;
			// 请求大转盘界面
			struct FortuneWheelView {
				int lastCount; // 今日剩余次数
				int totalCount; // 今日获得的总次数 
				int maxCount; // 最大次数
				long lastTime; // 剩余时间, 秒
				FortuneWheelRewardItemViewSeq rewards; // 奖励列表
				string help; // 帮助内容
				IntIntPairSeq vipCountPair; // Vip对应的次数
			};
			// 大转盘结果
			struct FortuneWheelResultView {
				int id; // 转盘结果
				ItemViewSeq items; // 奖励列表
			};
			
			//聚宝盆物品
			struct CornucopiaItem{
				int id;
				string itemId;
				string itemName;
				int num;
				int price;
				string tips;//文字提示
				bool isBuy;//是否购买
				bool isReceive;//当天是否领取
				int remainDays;//剩余天数
			};
			sequence<CornucopiaItem> CornucopiaItemSeq;
			
			//聚宝盆view
			struct CornucopiaView{
				CornucopiaItemSeq items;
				PropertySeq superItems;// 超值物品
				int superState;//超值物品状态 0-不可领 1-可领取 2-已领取
				int receiveDays;//可以持续领取的天数
				int vipLevel;
				int discount;//折扣
			};
			// 兑换所需物品
			struct ExchangeItems{
				//兑换所需物品id
				string id;
				//兑换所需物品数量
				int num;
				//兑换所需物品类型
				int type;
			};
			
			sequence<ExchangeItems> ExchangeItemConfigSeq;
			
			//兑换返回结果
			struct ExchangeResult {
				//本次兑换成功标记
				int flag;
				//剩余兑换次数
				int leftExchageNum;
				//兑换后剩余物品相关信息，包括id、数量、类型
				ExchangeItemConfigSeq itemConfigs;
			};
			
			
			// 兑换列表界面
			struct ExchangeView {
				//兑换编号
				string itemNo;
				//剩余次数
				int remainingCounts;
				// 兑换物类型 
				int itemType;
				// 兑换物id 
				string itemId;
				// 兑换物数量 
				int num;
				// 兑换物名称 
				string itemName;
				// 兑换物名称品质色 
				int colorType;
				// 是否公告 
				int annoFlag;
				// 每天兑换次数 
				int dealCountsLim;
				// 兑换后是否消失 
				int hideFlag;
				//兑换所需vip等级 
				int vipLevel;
				//兑换所需主公等级 =================
				int roleLevel;
				//兑换所需物品相关信息，包括id、数量
				ExchangeItemConfigSeq itemConfigs;
				// 是否显示活动
				int limitTimeFlag;
				
				//显示限时兑换剩余时间
				long leftTime;
			};
			sequence<ExchangeView> ExchangeViewItemViewSeq;
			
			// 等级福利
			struct LevelWealView {
				int levelDiffer; // 等级差距
				int wealNum; // 福利,威望数量
			};
			
			// 百步穿杨积分排名/////////////////////////////////////////////////////////////////////
			struct ShootScoreRankSub {
				int rank; // 排名
				string roleId; // 角色ID
				string roleName; // 角色名称
				string factionName; // 公会名称
				int vip; // 主公vip
				int level; // 主公等级
				string icon; // 头像
				int score;//积分
			};
			sequence<ShootScoreRankSub> ShootScoreRankSubSeq;
			
			// 抽奖信息
			struct ShootAwardInfo{
				string roleId;
				string rolename; 
				int vip;
				IntStringSeq items; // 射中的道具
				int systemType; 	// 1普通 2超级
				int shootType;	 	// 1:单射 2：十连射
			}; 
			sequence<ShootAwardInfo> ShootAwardInfoSeq;
			
			// 百步穿杨    积分奖励
			struct MarksmanScoreReward {
				int score; // 积分档次
				string icons; // 宝箱图标
				int state; // 领取状态 0:不可领 1:可领取 2:已领取
				IntStringSeq items; // 领奖道具
			};
			sequence<MarksmanScoreReward> MarksmanScoreRewardSeq;
			
			// 百步穿杨界面
			struct MarksmanView {
				ShootAwardInfoSeq awardList; // 左侧中奖信息
				long remainderTime; // 活动剩余时间
				IntStringSeq items; // 奖励预览
				int singleShootType; // 单射需要的成本花费类型(1金币 2元宝 3竞技币 4拍卖币)
				int tenShootCostType; // 十连射需要的成本花费类型(1金币 2元宝 3竞技币 4拍卖币)
				int singleShoot; // 单射需要的成本花费
				int tenShootCost; // 十连射需要的成本花费
				IntSeq channelIds; // 射中下标列表
				int basis; // 基础入榜积分
				int singleShootScore; // 单射能获取的积分
				int tenShootScore; // 十连射能获取的积分
				int freeTime; // 距离下一次的免费时间，单位(秒，0不免费)
				int totalNeedNum; // 单射获得特殊奖励需要的数量
				int curNum; // 当前单射数量
				long shootLastTime;//射箭剩余时间
				int myScore;
				MarksmanScoreRewardSeq marksmanScoreRewards;
				bool showMyRecord; // 是否显示我的中奖公告
				int needLevel; // 需要玩家等级
				int needVip; // 需要vip等级
			};
			// 积分奖励表
			struct ShootScoreRewardSub {
				string rank; // 排名 可以是 1-3这种
				IntStringSeq items; // 排名奖励列表
				int baseScore; // 排名积分要求
			};
			sequence<ShootScoreRewardSub> ShootScoreRewardSubSeq;
			// 百步穿杨积分排名界面
			struct MarksmanScoreRankView {
				string rewardGrantTime; // 排名截止时间
				ShootScoreRankSubSeq scoreRankList; // 积分排行榜显示数据
				int myRank; // 我的排名
				int myScore; // 我的积分
				string sendAwardLastTime;//奖励发放时间
			};
			// 百步穿杨积分排名奖励界面
			struct MarksmanScoreRewardView {
				string rewardGrantTime; // 奖励发放时间
				ShootScoreRewardSubSeq rewardList; // 积分排行榜显示数据
				int myRank; // 我的排名
				IntStringSeq items; // 我的当前排名奖励列表
				int myScore; // 我的当前积分
			};

			// API奖励数据///////////////////////////////////////////////////////////////////////////
			struct ApiRewardsView{
				string 				itemCode; 		//奖励物品代码
				int 				itemCount;		//奖励物品数量
			};
			sequence<ApiRewardsView> ApiRewardsViewSeq;
			
			// API目标节点数据
			struct ApiTargetView {
				int 				rewardId;		// 奖励节点编号
				int 				curCount;		// 当前数量/进度
				int 				targetCount; 	// 目标完成数量/进度
				ApiRewardsViewSeq 	rewards; 		//奖励
				int 				status; 		//领取状态, 1:目标未完成; 2:未领奖; 3:已领奖
				string 				desc; 			//说明
			};
			sequence<ApiTargetView> ApiTargetViewSeq;
		
			// 活动数据
			struct ApiActView {
				int actId; // 活动id
				string startTime;
				string endTime;
				string title; // 活动名称
				string intro; //活动介绍
				string icon;	// 图标
				int isGo; //是否跳转，1:有跳转; 2:无跳转
				string goto; //跳转标识
				ApiTargetViewSeq targets; //活动目标数据
			};

			sequence<ApiActView> ApiActViewSeq;

			struct ActivityInfoAll{
				ActivityInfoViewSeq actViewSeq; //所有活动的界面数据
				ApiActViewSeq actApiDetailSeq; //api活动的详细数据
			};	
			
			// 开服活动条目数据
			struct OpenServerActivityNodeView {
			    int activeId; //活动ID
				int nodeId; // 条目id
				IntStringSeq reward;	// 奖励内容
				int status;// 按钮状态  0：未完成 1：可领取 2：已领取
				string describe;//描述
				int conditionNum;//条件参数
				string condition1;//达成条件1
				string condition2;//达成条件2
			};
			sequence<OpenServerActivityNodeView> OpenServerActivityNodeViewSeq;
			
			// 开服活动半价礼包条目数据
			struct OpenServerSaleActivityNodeView {
			    int id;//活动ID
			    int openDay; //开放天数
				IntString reward; // 道具内容
				int status;// 0:不在活动时间内 1：可购买 2：已购买  3:不满足购买条件
				int coinType;//货币类型  1:元宝 2：金币
				int bePrice;//原价
				int price;//现价
			};
			sequence<OpenServerSaleActivityNodeView> openServerSaleActivityNodeViewSeq;
			
			//开服活动信息
			struct OpenServerActivityInfoView{
				int id;//活动ID
				string name;//活动名称
				string description;//活动描述
				int startDay;//开始
				int endDay;//结束
				int type;//1:普通活动列表 2：半价道具列表
				OpenServerActivityNodeViewSeq activityNodeViewSeq;//普通活动条目信息
				openServerSaleActivityNodeViewSeq saleViewSeq;//半价礼包信息
			};
			sequence<OpenServerActivityInfoView> OpenServerActivityInfoViewSeq;
			
			//开服活动所有信息
			struct OpenServerActivityListInfoView{
				OpenServerActivityInfoViewSeq openServerActivityListViewSeq;
			};
			
			//开服活动主城显示图标
			struct OpenServerActivityTipView{
				int isOpen;//是否开启 0：关闭  1：开启
			};
			
			// 格子信息
			struct GridInfoSub {
				int id; // 格子ID
				int type; //1=起点 2=神秘商店 3=普通方格
				int lotteryType;//1稀有2普通 0默认
				IntString item;//道具信息
			};
			sequence<GridInfoSub> GridInfoSubSeq;
			// 神秘商店信息
			struct LettoryShopInfoSub {
				int id; // 编号
				int status;//0:未买 1:已买
				IntString item;//道具信息
				int coinType;//1=金币 2=元宝
				int price;//原价
				int price2;//折扣价
				int discount;//折扣图标
			};
			sequence<LettoryShopInfoSub> LettoryShopInfoSubSeq;
			// 神秘商店界面
			struct LettoryShopView {
			    int curScore;//当前总积分
			    long remain;//神秘商店剩余时间 秒(注：时间小于0 表示过期)
				LettoryShopInfoSubSeq itemlist;
			};
			
			// 排行
			struct RankSub {
			    int id;
			    int rankStart;
			    int rankEnd;
				IntStringSeq awards;	// 奖励物品
			};
			sequence<RankSub> RankSubSeq;
			
			// 排行界面
			struct RankPageView {
			    int rank;//当前排名
			    IntStringSeq awards;// 当前奖励
				int maxNum; // 总数
				string desc;//描述说明
				RankSubSeq rankList;
			};
			//大富温界面个人相关信息
			struct RoleBaseSub {
			    int score;//积分
			    int rank; // 排名
				long leftTime; // 剩余时间
				int freeNum;//剩余免费次数
				int curGrid; //当前所在的位置
				int coinType;//1=金币 2=元宝
				int price;//摇骰价格
				int autoNeedNum;//遥控骰子需要次数
				int throwNum;//骰子投掷次数
				int autoNum;//遥控色子数量
				int tips;//神秘商店是否可进 0：不可1：可以2:过期
			};
			
			// 大富温主界面
			struct GridPageView {
				int minLevel;			//投掷骰子最低等级
			    RoleBaseSub base;
				GridInfoSubSeq list;
				PropertySeq shopPreview;//神秘商店预览
			};
			
			// 大富温积分排名
			struct LotteryScoreRankSub {
				int rank; // 排名
				string roleId; // 角色ID
				string roleName; // 角色名称
				string factionName; // 公会名称
				int vip; // 主公vip
				int level; // 主公等级
				string icon; // 头像
				int score;//积分
			};
			sequence<LotteryScoreRankSub> LotteryScoreRankSubSeq;
			// 大富温积分排名界面
			struct LotteryScoreRankView {
				LotteryScoreRankSubSeq lotteryRankList; // 积分排行榜显示数据
				int myRank; // 我的排名
				int myScore; // 我的积分
				long sendAwardLastTime;//奖励发放时间
			};
			// 大富温巡回圈数奖励
			struct LotteryCycleSub {
				int num; // 圈数
				IntStringSeq awards;// 当前奖励
			};
			sequence<LotteryCycleSub> LotteryCycleSubSeq;
			// 大富温巡回圈数奖励
			struct LotteryCycleView {
			    int curCyc;//当前圈数
				LotteryCycleSubSeq lotteryCycleList;
			};
			
			// 分享条目信息
			struct ShareSub {
				int taskId; //  条目ID
                int status;// 状态 0:未达到任务开始条件 1:未完成不可分享2:可分享3:已分享4:已領取
                IntStringSeq awards;	// 奖励物品
                string target;
			    string title;
			    string titleDesc;
			    string taskIcon;
			    int order;//排序
			    string bannerTitle;//Banner标题
			    int bannerType;//1：文字+图片类型（图片可为空）2：网址链接
			    string shareContent;//系统默认、玩家自定义分享内容
			    string shareImg;//分享图片
			    string shareIcon;//链接缩略图
			    string shareTitle;//分享标题
			    string shareLink;//调用网址
			};
			sequence<ShareSub> ShareSubSeq;
			// 分享活动界面
			struct ShareView {
				ShareSubSeq shareList;
			};
			
			// 资源找回，奖励
			struct ResourceBackRewardView {
				int type; // 类别
				string title; // 展示名字
				IntStringSeq rewards; // 奖励列表
				int rate1; // 找回比率
				Money price1; // 价格
				int rate2; // 找回比率
				Money price2; // 价格
				int hasReceived; // 是否已经领取. 1:已经领取；0:没有领取
			};
			sequence<ResourceBackRewardView> ResourceBackRewardViewSeq;
			
			// 资源找回界面
			struct ResourceBackItemView {
				long dateTime; // 具体时间，用于排序
				string date; // 显示日期
				string dateTag; // 领取时候的标记
				int canRecv; // 是否可领,0不可以，1可以
				ResourceBackRewardViewSeq items; // 可以找回的资源类别
			};
			sequence<ResourceBackItemView> ResourceBackItemViewSeq;
			
			struct ResourceBackView {
				long lastTime; // 剩余时间，秒
				IntStringSeq iconMap; // 类型和icon的映射集合
				ResourceBackItemViewSeq items;
			};
			
			//欧洲杯活动
			/**比赛场次*/
			struct FootballMatch{
				int id;
				int gameStatus;//0-未开始1-结束
				int leftScore;
				int rightScore;
				int betCountryId;//押注国家
				int betNum;//押注数量
				//以下是策划脚本内容
				string date;
				string time;
				int leftCountryId;
				int rightCountryId;
				double leftWinRate;
				double rightWinRate;
				double drawRate;
				int minBet;
				int maxBet;
				long startBetDate;//开始押注时间 秒
				long endBetDate;//结束押注时间 秒
				int gameType;
			};
			sequence<FootballMatch> FootballMatchSeq;
			
			/**竞猜界面VIEW*/
			struct FootballView{
				long serverTime;//服务器时间 秒
				string beginDate;
				string endDate;
				bool isFirst;//首次打开
				bool isOverFirst;//结束后首次打开
				int trophyNum;//奖杯数量
				int buyNum;//已购买次数
				FootballMatchSeq matchs;
			};
			
			/**押注日志*/
			struct FootballBetLog{
				string datetime;
				string content;
			};
			sequence<FootballBetLog> FootballBetLogSeq;
			
			struct FootballShop{
				int id;
				string itemId;
				int price;
			};
			sequence<FootballShop> FootballShopSeq;
			
			interface ActivityInfo{
				/**活动列表 lua格式activityInfoAll*/
				["ami"] string activityList() throws NoteException;
				
				/**升级奖励活动内容信息 lua格式UpActivityInfoViewSeq*/
				["ami"] string upActivityInfoList() throws NoteException;
				
				/**升级奖励领取礼包 giftId:礼包Id*/
				["ami"] bool getGift(int giftId) throws NoteException;
				
				/**点击活动列表的 我要做VIP按钮 返回是否可作答*/
				["ami"] bool clickMakeVip();
				
				/**开始答题 id,id,id*/
				["ami"] string beginAnswer() throws NoteException;
				
				/**结束答题返回增加的VIP经验 格式：id,answer;id,answer*/
				["ami"] int endAnswer(string str) throws NoteException;
				
				//获取累计充值活动信息
				["ami"] string getSummationActivityViewForCharge() throws NoteException;
				//领取累计充值奖励
				["ami"] void receiveRewardForSumCharge(int threshold) throws NotEnoughYuanBaoException,NoteException;
				
				//获取累计消费活动信息
				["ami"] string getSummationActivityViewForConsume() throws NoteException;
				//领取累计消费奖励
				["ami"] void receiveRewardForSumConsume(int threshold) throws NotEnoughYuanBaoException,NoteException;
				
				
				/**获取邀请好友活动信息，返回InviteActivityView的lua*/
				["ami"] string getInviteActivityView() throws NoteException;
				
				/**领取邀请好友奖励*/
				["ami"] void receiveRewardForInvite(int threshoId) throws NoteException;
				
				// 请求基金界面, return  FundView
				["ami"] string getFundView() throws NoteException;
				// 领取基金奖励, level 等级
				["ami"] void acceptFundReward(int level) throws NoteException;
				// 购买基金
				["ami"] void buyFund() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 请求冲级有奖界面, return LevelRewardView
				["ami"] string getLevelRewardView() throws NoteException;
				// 领取冲级有奖等级奖励
				["ami"] void acceptLevelReward(int level) throws NoteException, NotEnoughMoneyException;
				
				// 请求第一佳界面, return FirstJiaView
				["ami"] string getFirstJiaRewardView() throws NoteException;
				// 领取冲级有奖等级奖励
				["ami"] void acceptFirstJiaReward(int level) throws NoteException, NotEnoughMoneyException;
				
				// 每日登录界面, return DayLoginView
				["ami"] string getDayLoginRewardView() throws NoteException;
				["ami"] void acceptDayLoginReward(int day) throws NoteException, NotEnoughMoneyException;
				
				// 永久每日登录界面, return DayforverLoginView
				["ami"] string getDayforverLoginRewardView() throws NoteException;
				["ami"] void acceptDayforverLoginReward(int day) throws NoteException, NotEnoughMoneyException;
				
				// 开服活动界面, return OpenServerActivityListInfoView
				["ami"] string getOpenServerActiveView() throws NoteException;
				["ami"] void acceptOpenServerActiveReward(int active,int nodeId) throws NoteException;
				// 购买半价道具 return openServerSaleActivityNodeViewSeq
				["ami"] string buysale(int activeId,int day) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
	
				// 领军令界面, return SendJunLingView
				["ami"] string getSendJunLingView() throws NoteException;
				// 返回获得的元宝数量, 可能为零(没有获得元宝)
				["ami"] int acceptJunLing(int id) throws NoteException, NotEnoughYuanBaoException;
				
				// 请求战力嘉奖界面
				["ami"] string getPowerRewardView() throws NoteException;
				// 领取战力嘉奖奖励
				["ami"] void acceptPowerReward(int power) throws NoteException;
				
				/**获取秒杀活动信息 返回SeckillActivityView的lua*/
				["ami"] string getSeckillView() throws NoteException;
				/**秒杀物品*/
				["ami"] void seckillItem(int id) throws NotEnoughYuanBaoException,NoteException;
				
				/**获取日充值活动信息，返回SummationActivityView的lua*/
				["ami"] string getDayChargeView() throws NoteException;
				
				/**领取日充值奖励*/
				["ami"] void receiveDayCharge(int threshold) throws NoteException;
				
				/**获取日消费活动信息，返回SummationActivityView的lua*/
				["ami"] string getDayConsumeView() throws NoteException;
				
				/**领取日消费奖励*/
				["ami"] void receiveDayConsume(int threshold) throws NoteException;
				
				
				/**获取copy日充值活动信息，返回SummationActivityView的lua*/
				["ami"] string getBigDayChargeView() throws NoteException;
				
				/**领取copy日充值奖励*/
				["ami"] void receiveBigDayCharge(int threshold) throws NoteException;
				
				/**获取copy日消费活动信息，返回SummationActivityView的lua*/
				["ami"] string getBigDayConsumeView() throws NoteException;
				
				/**领取copy日消费奖励*/
				["ami"] void receiveBigDayConsume(int threshold) throws NoteException;
				
				
				//获取copy累计充值活动信息
				["ami"] string getBigSummationActivityViewForCharge() throws NoteException;
				//领取copy累计充值奖励
				["ami"] void receiveBigRewardForSumCharge(int threshold) throws NotEnoughYuanBaoException,NoteException;
				
				//获取copy累计消费活动信息
				["ami"] string getBigSummationActivityViewForConsume() throws NoteException;
				//领取copy累计消费奖励
				["ami"] void receiveBigRewardForSumConsume(int threshold) throws NotEnoughYuanBaoException,NoteException;
				
				// 幸运大转盘 
				// 获取幸运大转盘界面, return FortuneWheelView
				["ami"] string getFortuneWheelView() throws NoteException;
				// 请求转盘结果, return FortuneWheelResultView
				["ami"] string doFortuneWheel() throws NoteException;
				// 10次大转盘, return BuyHeroResult, 为了客户端的显示结果跟酒馆抽卡十连抽复用, 共用一个协议
				["ami"] string doFortuneWheelFor10() throws NoteException;
				
				
				/**获取聚宝盆view，返回CornucopiaView的lua*/
				["ami"] string getCornucopiaView() throws NoteException;
				
				/**聚宝盆一键购买*/
				["ami"] void buyAllCornucopia() throws NoteException,NotEnoughYuanBaoException;
				
				/**聚宝盆购买单项物品*/
				["ami"] void buyCornucopia(int id) throws NoteException,NotEnoughYuanBaoException;
				
				/**领取聚宝盆超值礼包*/
				["ami"] void getSupperCornucopia() throws NoteException;
				
				/**领取聚宝盆普通物品*/
				["ami"] void getCornucopia(int id) throws NoteException;
				
				/**一键领取聚宝盆物品*/
				["ami"] void getAllCornucopia() throws NoteException;
				
				/**获取兑换列表*/
				["ami"] string getExchangeItems(int itemType) throws NoteException;
				/**兑换物品,成功返回1，兑换失败返回0*/
				["ami"] string doExchangeItems(string exchangeNo,int itemType)throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
				
				// 等级福利
				/** 等级福利信息, 返回 LevelWealView */
				["ami"] LevelWealView levelWealInfo() throws NoteException;
				/** 领取等级福利 */
				["ami"] int getLevelWeal() throws NoteException;

				/** 百步穿杨, 返回 MarksmanView systemType 1:普通  2:超级百步*/
				["ami"] string openMarksmanView(int systemType) throws NoteException;
				/** 百步穿杨射击, 返回 MarksmanView  1:普通 2:超级百步*/
				["ami"] string shootReward(int shootType, int systemType) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException;
				/** 百步穿杨 积分领奖 return  ShootAwardInfoSeq*/
				["ami"] string getScoreReward(int score) throws NoteException;
				/** 百步穿杨积分排名, 返回 MarksmanScoreRankView */
				["ami"] string openMarksmanScoreRankView() throws NoteException;
				/** 百步穿杨积分排名奖励, 返回 MarksmanScoreRewardView */
				["ami"] string openMarksmanScoreRewardView() throws NoteException;
				/** 是否显示我的中奖记录 */
				["ami"] void showMyRecord(bool show) throws NoteException;
				/** 累计获得的奖励 */
				["ami"] string historyAward() throws NoteException;
				
				// 领取奖励, return ApiActViewSeq
				["ami"] void receiveApiReward(int actId, int targetCount) throws NoteException;
				
				/** 大富温主界面, 返回 GridPageView */
				["ami"] string gridPageView() throws NoteException;
				/** 神秘商店界面, 返回 LettoryShopView */
				["ami"] string lettoryshopView() throws NoteException;
				/** 神秘商店购买, 返回 LettoryShopView */
				["ami"] string lettoryshopBuy(int id) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
				/** 摇骰子, 返回 RoleBaseSub  type:0：普通 1：遥控*/
				["ami"] string throwBall(int type,int point) throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;
				/** 排行奖励界面, 返回 RankPageView */
				["ami"] string lettoryRankView() throws NoteException;
				/** 大富温排行界面, 返回 LotteryScoreRankView */
				["ami"] string lotteryScoreRankView() throws NoteException;
				/** 大富温巡回圈数奖励界面, 返回 LotteryCycleView */
				["ami"] string lotteryCycleView() throws NoteException;
				
				/** 分享活动主界面, 返回 ShareView */
				["ami"] string sharePageView() throws NoteException;
				/** 分享    ShareSub */
				["ami"] string share(int id) throws NoteException;
				
				/** 请求资源找回界面, return ResourceBackView */
				["ami"] string getResourceBackView() throws NoteException;
				/** 接受奖励，return ItemView */
				["ami"] void acceptResourceBack(string date, int type, int slot) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				/** 一键找回 */
				["ami"] void acceptResourceBackOneKey(string date) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				//欧洲杯活动
				/**欧洲杯活动主界面数据,return:FootballView的lua*/
				["ami"] string getFootballView() throws NoteException;
				
				/**获取对应国家的所有比赛,return:FootballMatchSeq的lua*/
				["ami"] string getCountryMatch(int countryId) throws NoteException;
				
				/**购买奖杯,return:最新奖杯数量*/
				["ami"] int buyTrophy() throws NoteException;
				
				/**押注，平局countryId传-1*/
				["ami"] int footballBet(int id,int countryId,int num) throws NoteException;
				
				/**商店物品return:FootballShopSeq的lua*/
				["ami"] string getFootballShops() throws NoteException;
				
				/**商店兑换*/
				["ami"] int footballExchange(int id,int num) throws NoteException;
				
				/**球队排行榜,return:IntIntPairSeq的lua*/
				["ami"] string footballRank() throws NoteException;
				
				/**获取竞猜日志,return:FootballBetLogSeq的lua*/
				["ami"] string footballBetLogs() throws NoteException;
			};
		};
	};  
};

#endif