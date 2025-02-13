#ifndef  _CENTER_ICE_
#define  _CENTER_ICE_

#include "Common.ice"
#include "RoleF.ice"
#include "LoginSessionF.ice"
#include "ExceptionDef.ice" 
  
module com{
	module XSanGo{
		module Protocol{
			struct YuanbaoCharge{ 
				int  id ;
             	int  total ;
             	int  recharge ;
             };
             
             class ServerDetail{
             	bool hasCallback;
             	bool overload;
             	int onlineCount;
             };
             
           //充值自定义参数
             struct CustomChargeParams{
             	int item;		//购买的虚拟货币编号
             	string mac;		//MAC地址            	
             	StringSeq roles;	//接受元宝的角色ID，给好友充值时使用
             };
             
             class DeviceInfo{
            	 int channel;
            	 string mac;
            	 string ip;
            	 string version;
            	 long firstInTime;
            	 int requestServerId;//玩家请求的服务器ID，与实际进入的服务器ID相对应
            	 int currentChannel; // 当前的渠道
             };
             
             class GmFactionView{
			 	string id;
			 	string name;
			 	int level;
			 	int memberNum;//公会人数
			 	string createDate;
			 };
			 sequence<GmFactionView> GmFactionViewSeq;
			 
			 class GmFactionMemberView{
			 	string name;
			 	string duty;//职务
			 	int donate;//贡献
			 	string joinDate;//入会时间
			 };
			 sequence<GmFactionMemberView> GmFactionMemberViewSeq;
			 
			 // GM排行榜
			 class GmRankView{
			 	int rank;
			 	string name;
			 	int level;
			 	int quantity;
			 };
			 sequence<GmRankView> GmRankViewSeq;
			 
			 // GM充值记录
			 class GmPayLogView{
			 	string roleName;
			 	string createDate;
			 	int payMoney;//充值金额 元
			 };
			 sequence<GmPayLogView> GmPayLogViewSeq;
			 
			 //GM充值视图
			 class GmPayView{
			 	int totalMoney;//充值总金额
			 	GmPayLogViewSeq payLogs;//充值记录
			 };
			 
			 enum AlarmType{
				SaveError, 
				ServerFull,
			 };
             
			interface CenterCallback{
				["ami"] void addRole(string account,string roleId,string roleName)  ;
				["ami"] void newMaxLevel(string account,string roleId,string roleName,int newLevel)  ;
				["ami"] void frozenAccount(string account,string remark);
				
				//开始兑换码使用流程，返回物品列表
				["ami","amd"] PropertySeq beginUseCDK(string account,string cdk,int roleLevel,string factionName) throws NoteException;
		        //确认兑换码使用完成
		        ["ami","amd"] void endUseCDK(string cdk);
		        
		        //从支付中心获取第三方充值订单号
		        ["ami","amd"] string getChannelOrderIdFromPayCenter(int channel,int appId,int money,string mac,string username,string roleid,string parmas)throws NoteException;
		        
		        //创建苹果应用商店的订单,money单位元
				["ami","amd"] void createOrderForAppleAppStore(string appStoreOrderId,int channel,int appId,int money,string itemId,string mac,string username,string roleid,string params) throws NoteException;
				
				//发送报警短信
				["ami"] void sendAlarmSMS(AlarmType type,string smsText);
         	};
         	
         	class ServerMailParam{
         		int serverId;
         		int minLevel;
         		long maxRoleCreateTime;
         		int maxLevel;
         		long lastLoginTime;
         		int minVip;
         		int maxVip;
         	};
         	
         	interface Center{
//         		["ami"] void onewayTest();
         		["ami"] void setCallback(CenterCallback* cb)  ;
         		//同步服务器状态
				["ami"] ServerDetail ping();
         		["ami"] void sendTocken(int id, string account,string tocken,DeviceInfo device)  throws NoteException;
			
          
				//以下为GM相关功能协议
				["ami","amd"] string findRoleViewList(string accountName);
				["ami","amd"] string findRoleViewListByRole(string roleName);
				["ami","amd"] string findRoleViewListById(string roleId);
				["ami","amd"] string getRecentMessages();
				["ami","amd"] void silence(string roleId,string releaseTime);
				["ami"] void systemAnnounce(string announce);
				["ami"] void kick(string account,string roleId);
				//玩家日志记录查询，返回值为OperateHistory[]类型的json格式字符串
//				["amd"] string queryOperateHistory(string roleId,long timestamp,int offset);
				
				["ami","amd"] void charge(string roleId,int yuan,CustomChargeParams params,string orderId,string currency);
				
				//获取物品配置表信息
         		["ami"] StringSeq getItemConfig() throws NoteException;
         		//属性名称对应
         		["ami"] StringSeq getPropertyConfig() throws NoteException;
         		//主公技能模板
         		["ami"] IntStringSeq getPlayerSkillConfig() throws NoteException;
         		//武将技能模板
         		["ami"] IntStringSeq getHeroSkillConfig() throws NoteException;
         		//缘分模板
         		["ami"] IntStringSeq getRelationConfig() throws NoteException;
         		//发邮件,参数传递用ServerMailParam的json格式
         		["ami"] void sendServerMail(string title,string body,PropertySeq attach,string conditionParams,string senderName)  throws NoteException;
         		["ami","amd"] void sendMail(string targetName,string title,string body,PropertySeq attach,string senderName)  throws NoteException;
         		["ami","amd"] void sendMailByRoleId(string roleId,string title,string body,PropertySeq attach, string senderName)  throws NoteException;
             	//执行动态脚本
             	["ami"] void executeGroovyScript(string script) throws NoteException;
             	/**根据CDK查询玩家信息*/
    			["amd"]string queryRoleByCDK(string cdk) throws NoteException;
             	//公会
             	/**查找公会列表，通过factionName模糊查找*/
    			["ami"] GmFactionViewSeq getFactionList(string factionName) throws NoteException;
    			/**获取公会所有成员*/
    			["ami","amd"] GmFactionMemberViewSeq getFactionMemberList(string factionName) throws NoteException;
    			
    			/**获取排行榜 type-0部队 1-公会 2-大神*/
    			["amd"] GmRankViewSeq getRankList(int type) throws NoteException;
    			
    			/**充值记录查询*/
    			//["amd"] GmPayView getPayLog(string roleName) throws NoteException;
    			["amd"] GmPayView getPayLog(string roleName) throws NoteException;
    			/**根据金额获取充值选项*/
    			["ami","amd"] string getChargeItem(int yuan);
    			/**查询物品数量*/
    			["ami","amd"] string queryItemNum(string roleName,string itemId);
    			/** 删除物品 */
    			["ami","amd"] string deleteItem(string roleId, string itemId, int num);
    			
    			/**获取角色相关数据*/
    			["amd"] ByteSeq getRoleDB(string roleId) throws NoteException;
    			
    			/**导入role数据*/
    			["amd"] void saveRoleData(int serverId,ByteSeq data, string roleId) throws NoteException;
    			
    			/**根据roleName模糊查询*/
    			["ami","amd"] string findRoleViewListBySimpleRole(string roleName);
    			/**根据账号模糊查询*/
    			["ami","amd"] string findRoleViewListBySimpleAccount(string account);
         	};
         	
         	struct QueryItemResponse{ 
             	string itemId;
             	int num;
				string message;// 消息说明
             };
         	
			struct GameServerView{ 
             	int  id;
             	string  name ;
				int  state ;
             	int  roleCount;
             	int showId;
             	string netgateAddress;
             	bool isNew;			//客户端显示新或爆满
             	bool isCpShowOnly;	//是否仅CP可见
             	bool isCpEnterOnly;	//是否仅CP可进
             	int onlineLimit;	//人数上限设置，默认为0表示不做特殊控制
             	int targetId;
             };
			 sequence<GameServerView> GameServerViewSeq;
			 
			 enum WhiteListType{
					Normal,
					Cp
			 };
			 struct WhiteList {
				 WhiteListType type;			//白名单种类
				 bool enable;					//是否启用
				 StringSeq whiteIpList;			//IP白名单
				 StringSeq whiteAccountList;	//帐号白名单
				 string tip;					//非白名单用户看到的提示
			 };
			 sequence<WhiteList> WhiteListSeq;
			 
			 struct ScriptReloadConfig{
				 bool activityList;			//活动列表
				 bool announce;				//公告
				 bool sumChargeConsume;		//累计充值消费
				 bool dayChargeConsume;		//日充值消费
				 bool growFoundation;		//成长基金
				 bool sign;					//签到
				 bool heroAdmire;			//名将仰慕
				 bool heroSoulCollect;		//名将召唤
				 bool levelupGift;			//升级奖励
				 bool levelReward;			//等级奖励
				 bool secondKill;			//天天秒杀
				 bool makeVip;				//我要做VIP
				 bool inviteFriend;			//邀请好友
				 bool online;				//在线时长
				 bool tcReward;				//TC奖励
				 bool mail;					//邮件
				 bool mall;					//商城
				 bool fortuneWheel;			//魂魄转盘
				 bool dayLogin;				//登陆奖励
				 bool sendJunLing;			//军令补偿
				 bool powerReward;			//最强战力
				 bool limitHero;			//酒馆魂匣
				 bool monthFirstCharge;		//首充礼包
				 bool vitBuy;				//体力购买
				 bool ladder;				//群雄争霸
				 bool jinbiBuy;				//点金手
				 bool faction;				//公会相关
				 bool luckyBag;				//福袋
				 bool cornucopia;			//聚宝盆
				 bool goodsExchange;		//物物兑换
				 bool haoqingbao;			//豪情堡
				 bool dayForeverLogin;      //永久每日登录
				 bool share;				//分享活动
				 bool football;				//足球
			 };
			 
			 //小黑屋规则配置
			 struct GuideConfig{
				 //如果enable为开启，则channelIds、beginTime和endTime必须至少配置一个，
				 //targetServers也至少要有一个元素
				 bool enable;
				 IntSeq channelIds;
				 string beginTime;
				 string endTime;
				 IntSeq targetServers; 
			 };
			 struct CdkView{
				 string name;
				 IntSeq channelIds;
				 string prefix;
				 int maxLevel;
				 int minLevel;
				 string union;
				 int needPay;
				 string startTime;
				 string endTime;
				 string adjunct;
				 string remark;
				 int number;
				 IntSeq serverIds;
			 };
			 
			  struct CdkDetailView{
				 CdkView detailView;
				 string useDate;//使用日期
				 string roleName;//使用玩家名称
				 int useNum;//使用次数
			 };
         	interface Gm{
         		//获取物品配置表信息
         		["amd"] StringSeq getItemConfig() throws NoteException;
         		//获取指定服务器的物品配置表信息
         		["amd"] StringSeq getServerItemConfig(int serverId) throws NoteException;
         		//属性名称对应
         		["amd"] StringSeq getPropertyConfig() throws NoteException;
         		//主公技能模板
         		["amd"] IntStringSeq getPlayerSkillConfig() throws NoteException;
         		//武将技能模板
         		["amd"] IntStringSeq getHeroSkillConfig() throws NoteException;
         		//缘分模板
         		["amd"] IntStringSeq getRelationConfig() throws NoteException;
         		//渠道列表的json格式
         		["ami"] string getChannelConfig() throws NoteException;
         		
         		//同步服务器状态
         		["ami"] GameServerViewSeq ping();
         		
         		//根据账号查询角色列表，返回RoleView的json格式
    			["amd"] string findRoleViewList(int serverId, string accountName,string roleName,string roleId) throws NoteException;
             	//发送系统邮件
             	["amd"] void sendSystemMail(int serverId,string roleId,string roleName,string title,string content,PropertySeq attach,string sendName) throws NoteException;
             	//全服邮件，带匹配参数，参数格式为ServerMailParam的json格式
             	["amd"] void sendSystemServerMail(int serverId,string title,string content,PropertySeq attach,string conditionParams, string senderName) throws NoteException;
             	//执行动态脚本
             	["amd"] void executeGroovyScript(int serverId,string script) throws NoteException;
             	//执行合服动态脚本
             	["amd"] void executeGroovyCombineScript(int serverId) throws NoteException;
             	//设置VIP等级
             	["amd"] void setVipLevel(int serverId,string roleName,int vip) throws NoteException;
    			//生成CDK
    			["amd"] string generateCDK(string name,string category,int number,IntSeq channels,IntSeq servers,int minLevel,int maxLevel,string factionName,int chargeMoney,string beginTime,string endTime,string remark,PropertySeq content) throws NoteException;
    			//更新CDK
    			["amd"] string updateCDK(string name,string category,int number,IntSeq channels,IntSeq servers,int minLevel,int maxLevel,string factionName,int chargeMoney,string beginTime,string endTime,string remark) throws NoteException;
    			//加载CDK
    			["amd"] string loadCDK(string category) throws NoteException;
    			//删除CDK
    			["amd"] void deleteCDK(string category) throws NoteException;
    			
    			/*
    			1、单个服务器发送公告。
    			2、单个服务器群发邮件。
    			3、角色管理里面要增加一些信息，比如 帐号信息、状态、渠道、关卡进度等。
    			4、原理的角色查询里面需要兼容用户名和角色名查询。
    			5、T下线、禁言、冻结、解冻。
    			*/
    			["amd"] void sendAnnounce(int serverId,string content) throws NoteException;
    			["amd"] void kickRole(int serverId,string account,string roleId) throws NoteException;
    			["amd"] void fobidenChat(int serverId,string roleId,string releaseTime) throws NoteException;
    			["amd"] void forzenAccount(string account,string releaseTime) throws NoteException;
    			["amd"] void unforzenAccount(string account) throws NoteException;
    			
    			//公会
    			/**查找公会列表，通过factionName模糊查找*/
    			["amd"] GmFactionViewSeq getFactionList(int serverId,string factionName) throws NoteException;
    			
    			/**获取公会所有成员*/
    			["amd"] GmFactionMemberViewSeq getFactionMemberList(int serverId,string factionName) throws NoteException;
    			
    			//排行榜
    			/**获取排行榜 type-0部队 1-公会 2-大神*/
    			["amd"] GmRankViewSeq getRankList(int serverId,int type) throws NoteException;
    			
    			/**充值记录查询*/
    			["amd"] GmPayView getPayLog(int serverId,string roleName) throws NoteException;
    			
    			//模拟充值
    			["amd"] void mockCharge(string account,string roleId,int channelId,int serverId,int cent)throws NoteException;
    			
    			//。。。。。。。。。。。。。。。。这里开始都是运维相关接口。。。。。。。。。。。。。。
    			//编辑游戏服，没有则创建新的
    			
    			["amd"] void editGameServer(int id,string name,int showId,string gateIp,bool isNew,bool cpShowOnly,bool cpEnterOnly,int onlineLimit, int targetId) throws NoteException;
    			//编辑渠道，没有则创建新的
    			["amd"] void editChannel(int id,string name,string orderUrl) throws NoteException;
    			//上传登录公告
    			["amd"] void uploadLoginAnnounce(ByteSeq file) throws NoteException;
    			//设置推荐服务器
    			["amd"] void setRecommendServer(int serverId) throws NoteException;
    			//获取当前白名单配置
    			["ami"] WhiteListSeq getCurrentWhiteList() throws NoteException;
    			//设置白名单
    			["amd"] void setWhiteList(WhiteList param) throws NoteException;
    			//获取小黑屋配置
    			["ami"] GuideConfig getGuideConfig() throws NoteException;
    			//设置小黑屋规则
    			["ami"] void setGuideConfig(GuideConfig config) throws NoteException;
    			//热加载脚本
    			["amd"] void reloadScript(int serverId,ScriptReloadConfig config)  throws NoteException;
    			/** 删除物品 */
    			["amd"] string deleteItem(int serverId, string roleId, string itemId, int num);
    			//添加元宝
    			["amd"] void addSycee(int serverId,string roleName,int sycee) throws NoteException;
    			
    			/**添加vip经验*/
    			["amd"] void addVipExp(int serverId,string roleName,int exp) throws NoteException;
    			
    			/**调整指定难度副本到指定关卡*/
    			["amd"] void skipCopy(int serverId, string roleId, int diff, int copyId) throws NoteException;
    			
    			/**增加大转盘次数 wheelType=0幸运转盘 1豪华转盘*/
    			["amd"] void addWheelCount(int serverId,string roleId,int count,int wheelType) throws NoteException;
    			
    			/**下载角色二进制数据存档*/
    			["amd"] ByteSeq download(int serverId,string roleId) throws NoteException;
    			/**导入role数据*/
    			["amd"] void importRole(int serverId,ByteSeq data, string roleId) throws NoteException;
    			/**根据roleName模糊查询*/
    			["amd"]string findFuzzyRoleViewList(int serverId, string accountName,string roleName) throws NoteException;
    			/**根据CDK查询具体信息*/
    			["amd"]string queryCdkByCDK(int serverId, string cdk) throws NoteException;
    			//。。。。。。。。。。。。。。。。运维相关接口到此结束。。。。。。。。。。。。。。
         	};
      };
        
    };
      
};
  

#endif
