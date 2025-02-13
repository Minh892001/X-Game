//商人功能模块协议
#ifndef  _TRADER_ICE_
#define  _TRADER_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			//名将探访结果
			struct HeroCallResult{
				CurrencyType callType;
				int heroId;						//武将模板ID
				string rankTitle;				//官阶头衔
				int colorLevel;					//品质颜色
				byte star;						//星级
				bool challengeSuccess;			//是否已挑战成功，成功才能购买物品
				int remainChance;				//剩余挑战次数
				bool hasGift;					//是否有礼物可领
				CommodityViewSeq heroCommodities;//商品信息列表
				bool challengeEnd;				//挑战是否结束，指定次数内战胜或者次数用完
			};
			
			sequence<HeroCallResult> HeroCallResultSeq;
			
			struct TraderView{
				int traderJinbiCallPrice;		//金币召唤商人价格
				int traderYuanbaoCallPrice;		//元宝召唤商人价格
				int remainSecond;				//商人有效期倒计时，为0或负数表示当前无商人
				CommodityViewSeq commodityViews;//商品信息列表
				
				int heroJinbiCallPrice;			//名将金币召唤价格
				int heroYuanbaoCallPrice;		//名将元宝召唤价格
				int heroRemainSecond;			//有效期倒计时，为0或负数表示当前无商人
				HeroCallResultSeq heroResult;	//名将探访结果,值为Null时传空数组
			};
			
			interface TraderCallback{
				["ami"] void traderChange();
			};
			
			interface Trader{
//				["ami"] TraderView getTraderView() throws NoteException;
				["ami"] string getTraderView() throws NoteException;
				["ami"] string callJinbiTrader() throws NoteException,NotEnoughMoneyException;
				["ami"] string callYuanbaoTrader() throws NoteException,NotEnoughYuanBaoException;
				["ami"] string callJinbiHero() throws NoteException,NotEnoughMoneyException;
				["ami"] string callYuanbaoHero() throws NoteException,NotEnoughYuanBaoException;
				
				["ami"] void buyItem(string id) throws NoteException,NotEnoughYuanBaoException,NotEnoughMoneyException;
				
				//挑战名将
				["ami"] DuelReportView beginChallenge(string heroId)throws NoteException;
				["ami"] void endChallenge(DuelResult result) throws NoteException;
				//购买名将物品
				["ami"] void buyHeroItem(string id) throws NoteException,NotEnoughYuanBaoException,NotEnoughMoneyException;
				//领取礼品
//				["ami"] ItemViewSeq acceptConsolation();
				["ami"] string acceptConsolation(); 
			};
		};   
	};
};


#endif