#ifndef  _COPY_ICE_
#define  _COPY_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			//热身对手信息
			struct WarmupOpponentView{
				string opponentName;		//对手名字
				int opponentLevel;			//对手等级
				int opponentVip;			//对手VIP等级
				string opponentIcon;		//对手头像ICON
                int sex;					//对手性别
				PvpOpponentFormationView opponent;	//对手战斗属性 
			};
			
			//PK周围玩家信息，由于该流程在副本挑战之前，故命名为热身赛
			struct WarmupView{
				int winCount;				//胜利次数
				int loseCount;				//失败次数
				string openDialog;			//开场白
				ItemViewSeq items;			//掉落物品，包括主公经验金币等
				
				WarmupOpponentView opponent;
			};
			sequence<WarmupView> WarmupViewSeq;
			
		    //小关卡详情
            struct SmallCopyView{
                short challengeTime;        //挑战次数
                short maxTime;              //最大挑战次数
                int clearTokenCount;        //扫荡令牌数量
                bool hasAllStar;            //是否有满星击杀者
                string firstAllStar;
                int firstAllStarLvl;        // 首次满星击杀者vip等级
                int resetCount;             //关卡重置次数
                string refreshTime;         //下次重置时间
                int needMilitaryOrder;		//需要的军令数量
            };
            
            //副本详情带热身赛数据，打开具体关卡界面时返回
            struct SmallCopyViewWithWarmup{
            	SmallCopyView copyView;			//副本数据
            	WarmupViewSeq warmupViews;		//热身赛数据
            };

		    // 关卡详情
		    struct CopyDetail {
		        int copyId; // 关卡id
		        int star; // 星级
		        SmallCopyView smallView; // 详情
		    };
		    sequence<CopyDetail> CopyDetailSeq;
	
			//大关卡详情
			struct BigCopyView{
				CopyDetailSeq copyJunior;// 新手
				CopyDetailSeq copySenior;// 高手
				CopyDetailSeq copyTop;	// 大神
				int buff; // 数值参考 CopyBuff
			};
			
			struct CaptureView{
				int catchHeroId;			//为0则表示没有抓获武将
				int releaseExp;				//释放获得威望
				ItemViewSeq killItems;		//斩首获得物品
				string releaseMsg;
				string killMsg;
				bool handle;
				int employTime;
			};
			sequence<CaptureView> CaptureViewSeq;
			enum CopyBuff {
				None,
				FastKill, // 速战速决
				FightHard, // 鏖战
				Kill3, // 三杀
				Kill4, // 四杀
				Kill5, // 五杀
				NarrowVictory, // 险胜
			};
			struct CopyChallengeResultView{
				PropertySeq heroExps;		//武将获得经验，KEY为武将唯一标识，VALUE为获得经验值
				ItemViewSeq items;			//掉落物品，包括主公经验金币等
				
				DuelUnitViewSeq duelCandidate;//单挑候选武将
				SceneDuelViewSeq reports;
				CaptureViewSeq capture;
				
				string movieId; // 战报录像ID
				int buff; // 数值参考 CopyBuff
			};
			
			struct CopyClearResultView{
				int copyId;					//关卡ID
				ItemViewSeq items;			//掉落物品，包括主公经验金币等
				ItemViewSeq additionItems;	//额外奖励
				int levelWeal; // 等级福利
			};
			
			struct ChapterReward{
				int level;				//奖励的次序索引, 按照星数多少升序, 从 1 开始, 1(10星奖励), 2(20星奖励), 3(满星奖励)
				bool canReceive;		//是否可以领取奖励
				bool hasReceived;		//是否已领取
			};
			sequence<ChapterReward> ChapterRewardSeq;
			struct ChapterRewardView {
			     int totalStar;
			     ChapterRewardSeq rewardList;
			};

			struct EmployCaptureResult{
				bool success;
				string msg;
				string soulId;	//魂魄模板ID
				int soulCount;	//魂魄数量
			};
			
			//名人堂数据
			struct HallOfFameView{
				string roleId;
				string roleName;
				int copyCount;			//首次击杀的副本数
				string headImg;
				short level;
				int vip;
			};
			sequence<HallOfFameView> HallOfFameViewSeq;
			
			struct CopySummaryView{
				int remainCount;
				int maxCount;
				bool open;
			};
			sequence<CopySummaryView> CopySummaryViewSeq;
			
			struct ChallengeTaView{
				string roleId;
				string roleName;
				string headImg;
				int sex;
				int vip;
				int level;
				PvpOpponentFormationView formationView;
			};
			
			struct HuDongView{
				int hudongCount;//关卡可挑战TA次数
				int maxHudongCount;//关卡最大挑战TA次数
				int worshipCount;// 可膜拜次数
				int maxWorshipCount;// 最大膜拜次数
				int challengeCd;// 挑战TA CD时间秒
				int buyHudongCount;//已购买互动次数
			};
			
			struct ChallengeTaResult {
			     int flag; //0-失败，1-胜利 
			     string movieId;
			};
			
			struct BuyMilitaryOrderView{
				int alreadyCount;		//已购买次数
				int maxCount;			//最大购买次数
				int yuanbao;			//消耗元宝
				int militaryOrder;		//获得军令
			};
			
			struct EndChallengeResultView {
				CopyDetail detail;
				int levelWeal; // 等级福利
				int buff; // 副本buff，数值参考CopyBuff
				bool isFirstOccupy;// 是否首次占领
			};
			
			/**关卡占领列表*/
			struct CopyOccupy{
				int copyId;
				int diff;//难度
				int sectionId;
				string sectionName;//章节
				string copyName;//名字
				bool isCanLevy;//是否可征收
			};
			sequence<CopyOccupy> CopyOccupySeq;
			
			/**挑战TA自动战报结果*/
			struct ChallengeTaAutoResult{
				SceneDuelViewSeq soloMovie;//单挑战报
			    ByteSeq fightMovie;//群战战报
				int fightStar;		//战斗结果的星级
				int flag; //0-失败，1-胜利 
			    string movieId;
			    string rivalName;//对手名字
			    int rivalVip;//对手VIP
			    int winType;
			};
			
			interface Copy{				
				//获取副本详细信息
				["ami", "amd"] BigCopyView getBigCopyView()  ;
				["ami","amd"] SmallCopyView getSmallCopyView(int copyId)  ;
				["ami","amd"] SmallCopyViewWithWarmup getSmallCopyViewWithWarmup(int copyId)  throws NoteException;
				
				//开始结束热身赛
				["ami"] string beginWarmup() throws NoteException;				
				["ami"] byte endWarmup(byte remainHero) throws NoteException;				
        	
				//挑战副本
				["ami"] CopyChallengeResultView beginChallenge(string formationId, int copyId) throws NoteException,NotEnoughException;
				
				//计算星级
				["ami"] int calculateStar(byte remainHero, byte killNum, float minTime, float maxTime) throws NoteException;
				
				//打赢或者战败时未用元宝购买时候调用
				["ami","amd"] EndChallengeResultView endChallenge() throws NoteException;
				
				// 退出战斗
				["ami"] void failChallenge() throws NoteException;
				
				//战败且使用元宝购买掉落品时调用
				["ami"] int getYuanbaoPrice(ItemViewSeq items) throws NoteException;
				//战败直接用元宝作弊
				["ami"] void buySuccess(ItemViewSeq items) throws NotEnoughYuanBaoException,NoteException;
				//购买挑战次数
				["ami"] void buyChallengeChance(int copyId) throws NotEnoughYuanBaoException,NoteException;
				//一键购买一章所有挑战次数
				["ami"] IntIntPairSeq buyChapterChallengeChance(int chapterId) throws NotEnoughYuanBaoException,NoteException; 

				//扫荡副本
//				["ami"] CopyClearResultView clear(int copyTemplateId,int count) throws NoteException;
				["ami"] string clear(int copyTemplateId,int count) throws NoteException,NotEnoughException;
				//["ami"] string clearChapter(int chapterId) throws NoteException;
				
				//获取通关奖励界面数据
				["ami"] ChapterRewardView getChapterRewardView(int chapterId);
//				["ami"] string getChapterRewardView(int chapterId);
				//领取通关奖励, 参数(chapterid, 章节索引; level, 奖励索引如: (1,10星奖励;2,20星奖励;3,满星奖励)
				["ami"] void receiveChapterReward(int chapterId, int level) throws NoteException;
				
				//释放，斩首，招录俘虏
				["ami"] void releaseCaptured();
				["ami"] int killCaptured();
//				["ami"] EmployCaptureResult employCaptured() throws NotEnoughMoneyException;
				["ami"] string employCaptured() throws NotEnoughMoneyException;
				
				//获取关卡挑战次数数据，参数是需要查询的关卡ID串用“,”分割，返回值是 剩余次数/最大次数 的数组
//				["ami"] CopySummaryViewSeq getCopyChallengeInfo(IntSeq copyIdArray);
				["ami"] string getCopyChallengeInfo(string idStr);
				
				//获取名人堂排行,返回HallOfFameViewSeq的lua形式
				["ami","amd"] string hallOfFameList();
				
				/**打开互动界面 返回HuDongView的lua格式*/
				["ami"] string getHuDongView(int copyId) throws NoteException;
				
				/**挑战TA*/
				["ami","amd"] ChallengeTaView beginChallengeTa(int copyId) throws NoteException;
				
				/**自动挑战TA*/
				["ami","amd"] ChallengeTaAutoResult autoChallengeTa(int copyId) throws NoteException;
				
				/**挑战TA结束 resFlag:0-失败，1-胜利 返回：resFlag*/
				["ami"] ChallengeTaResult endChallengeTa(int resFlag) throws NoteException;
				
				/**膜拜TA*/
				["ami"] int worshipTa(int copyId) throws NoteException;
				
				/**购买互动次数*/
				["ami"] void buyHuDong(int copyId) throws NoteException,NotEnoughYuanBaoException;
				
				//获取我的领地信息，第一个int表示当前占领数，第二个表示可占领的最大数量
				["ami"] IntIntPair getMyOccupy() throws NoteException;
				
				//获取军令购买相关数据
				["ami"] BuyMilitaryOrderView getBuyMilitaryOrderView() throws NoteException;
				//购买军令
				["ami"] void buyMilitaryOrder()  throws NoteException,NotEnoughYuanBaoException;
				//取消热身, first==true, 第一次关闭，返回提示语句，first==false， 第二次关闭，返回""
				["ami"] string cancelWarmup(bool first)  throws NoteException;
				
				/**征收*/
				["ami"] int levyCopy(int copyId) throws NoteException;
				
				/**我的占领列表 返回CopyOccupySeq的lua*/
				["ami"] string myOccupyList() throws NoteException;
				
				/**放弃占领*/
				["ami"] void giveCopy(int copyId) throws NoteException;
         };          
      };  
    };    
};

#endif