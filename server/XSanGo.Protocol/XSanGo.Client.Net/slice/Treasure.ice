#ifndef  _TREASURE_ICE_
#define  _TREASURE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

/**秘境寻宝*/
module com{
	module XSanGo{
		module Protocol{
			//推荐武将
			struct RecommendHero {
				int id;
				string name;
				int level;
				int star;
				int advance;//进阶
				QualityColor color;//颜色
			};
			sequence<RecommendHero> RecommendHeroSeq;
			
			/**寻宝事件*/
			struct TreasureEvent{
				string describe;//事件描述
				int addValue;//增加或者减少X个1级宝石
			};
			sequence<TreasureEvent> TreasureEventSeq;
			
			//寻宝队伍
			struct TreasureGroup{
				int id;//队伍编号
				StringSeq huntHeros;//上阵的寻宝武将ID
				RecommendHeroSeq recommendHeros;//推荐武将
				bool isOpen;
				int openLevel;
				int openVipLevel;
				bool isDepart;//是否已出发
				int remainMinute;//寻宝当前阶段剩余分钟
				int progress;//寻宝当前阶段进度%
				bool isCanGain;//是否可收获
				int stage;//阶段
				bool isAllOver;//是否所有阶段都完成
				TreasureEventSeq events;
				int speedPrice;//加速价格
				ItemViewSeq gainItems;//收获可获得物品
			};
			sequence<TreasureGroup> TreasureGroupSeq;
			
			struct TreasureView{
				TreasureGroupSeq TreasureGroups;
				int singleAdd;//单个推荐武将奖励加成%
				
				int gainNum;//收获次数
				int maxGainNum;//最大收获次数
				int rescueNum;//救援次数
				int maxRescueNum;//最大救援次数
				
				int accidentLevel;//矿难等级 0-无矿难
				int punishment;//惩罚，减少X个1级宝石
				int rescueSecond;//救援倒计时秒
				int rescuePeople;//已经救援人数
				int needRescuePeople;//需要救援人数
				int accidentEffect;//矿难特效编号
			};
			
			/**寻宝援救记录*/
			struct TreasureRescueLog{
				string datetime;
				string friendName;
				int accidentLevel;//矿难等级
				ItemViewSeq items;//援救得到的物品
			};
			sequence<TreasureRescueLog> TreasureRescueLogSeq;
			
			/**援救矿难的好友*/
			struct TreasureRescueFriend{
				string roleId;
				string roleName;
			};
			sequence<TreasureRescueFriend> TreasureRescueFriendSeq;
			
			/**矿难记录*/
			struct TreasureAccidentLog{
				string id;
				string datetime;
				int accidentLevel;//矿难等级
				int status;//0-援救中1-援救成功2-援救失败
				TreasureRescueFriendSeq rescueFriend;//救援好友
				int lossNum;// 损失宝石个数
			};
			sequence<TreasureAccidentLog> TreasureAccidentLogSeq;
			
			/**寻宝好友*/
			struct TreasureFriend{
				string id;
				string icon;
				string name;
				int level;
				int vip;
				int count;//矿难等级或援救次数
				bool enabled;//是否可用
			};
			sequence<TreasureFriend> TreasureFriendSeq;
			
			/**求援界面view*/
			struct TreasureRecourseView{
				TreasureFriendSeq friends;
				int sendNum;//已发送求援次数
				int sumNum;//总次数
				string rescueMsg;//求援消息
			};
			
			/**寻宝*/
			interface Treasure{
				/**获取寻宝view，返回TreasureView的lua*/
				["ami"] string getTreasureView() throws NoteException;
				
				/**出发。heroIds-武将数据库ID用,分割 返回TreasureViewSeq的lua*/
				["ami"] string depart(int id,string heroIds) throws NoteException;
				
				/**召回*/
				["ami"] void recall(int id) throws NoteException;
				
				/**收获，返回ItemViewSeq的lua*/
				["ami"] string gain(int id) throws NoteException;
				
				/**获取援救记录，返回TreasureRescueLogSeq的lua*/
				["ami"] string getRescueLog() throws NoteException;
				
				/**获取矿难记录，返回TreasureAccidentLogSeq的lua*/
				["ami"] string getAccidentLog() throws NoteException;
				
				/**加速,返回TreasureViewSeq的lua*/
				["ami"] string speed(int id) throws NoteException;
				
				/**救援好友矿难，返回ItemViewSeq的lua*/
				["ami","amd"] string rescue(string friendId) throws NoteException;
				
				/**获取发生矿难的好友列表，返回TreasureFriendSeq的lua*/
				["ami","amd"] string getAccidentFriend() throws NoteException;
				
				/**打开求援界面，返回TreasureRecourseView的lua*/
				["ami","amd"] string getRescueFriend() throws NoteException;
				
				/**向好友发送求援私信，friendIds多个用,分割*/
				["ami","amd"] void sendRescueMsg(string friendIds) throws NoteException;
				
				/**保存求援私信*/
				["ami"] void saveRescueMsg(string msg) throws NoteException;
			};
		};
	};
};

#endif
