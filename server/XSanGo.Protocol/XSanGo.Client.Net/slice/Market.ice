#ifndef  _MARKET_ICE_
#define  _MARKET_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			struct BuyHeroResult{
				//特殊说明：当武将和魂魄ID都不为空，则表示已有该武将，自动转为魂魄
				int heroTemplateId;			//武将模板ID，如果不是武将，则为0
				string soulTemplateId;		//魂魄模板ID，如果不是魂魄，则为空
				int num;
				int totalNum;				//累计魂魄数量
				int summonOrStarUpNum;		//召唤或升星需要数量
				int charmValue;				//最新魅力值
			};
			sequence<BuyHeroResult> BuyHeroResultSeq;
			
			struct StoreView{
				int cd;					//单位：秒
				byte remainTime;		//剩余免费次数
				byte totalTime;			//总免费次数
				Money singlePrice;		//一次价格
				Money price10Time;		//十次价格
				bool firstUsed;			//是否用过首抽
			};
			
			// 限时武将view
			struct LimitView{
				IntStringSeq weekTemplateIds;//本周热卖
				IntStringSeq todayTemplateIds;//今日热卖
				int price;//价格
				int viplimit;//购买需要的VIP等级
				int freeNum;//剩余免费次数
				string itemId;//道具购买ID
				int itemNum;//道具购买数量
				int itemVip;//道具购买需要的VIP等级
			};
			
			struct MarketView{
				StoreView oneInTenStore;	//十里挑一
				StoreView oneInHundredStore;//百里挑一
				LimitView limitHero;		//限时武将
				int currentCharm;			//当前魅力值
				int maxCharm;				//最大魅力值
				string fixRewardCode;		//固定奖励物品编号
			};
			
			interface Market{
				//获取当前抽卡状态数据，首次进入抽卡界面和倒计时读完后调用
				["ami"] string getMarketView();
				
				//十里挑一
				["ami"] string OneInTen() throws NotEnoughMoneyException,NoteException;
				
				//10次十里挑一
				["ami"] string OneInTen10() throws NotEnoughMoneyException,NoteException;
				
				//百里挑一
				["ami"] string OneInHundred() throws NotEnoughYuanBaoException,NoteException;
				
				//10次百里挑一
				["ami"] string OneInHundred10() throws NotEnoughYuanBaoException,NoteException;
				
				/**限时武将，返回BuyHeroResultSeq的lua。type-0元宝1物品*/
				["ami"] string buyLimitHero(int type) throws NotEnoughYuanBaoException,NoteException;
			};
		};   
	};
};


#endif