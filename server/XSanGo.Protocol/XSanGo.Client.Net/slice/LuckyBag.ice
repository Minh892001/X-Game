#ifndef  _LUCKY_BAG_ICE_
#define  _LUCKY_BAG_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{ 
	module XSanGo{
		module Protocol{
			// 福袋奖励物品
			struct LuckyBagItem{
				int id;
				int value;//领取条件
				PropertySeq items;//物品数组
				bool received;//是否领取
			};
			sequence<LuckyBagItem> LuckyBagItemSeq;
			
			// 福袋view
			struct LuckyBagView {
				int totalCharge;//当日已充值元宝
				LuckyBagItemSeq dayBag;
				
				int chargeDay;//当月已充值天数
				LuckyBagItemSeq monthBag;
				IntSeq redNotes; //豪华返利、豪华转盘的红点flag, 1 有红点 0无红点
			};
			
			interface LuckyBag{
				/**获取福袋VIEW，返回LuckyBagView的lua*/
				["ami"] string getLuckBagView() throws NoteException;
				
				/**领取当日充值福袋*/
				["ami"] void receiveDayBag(int id) throws NoteException;
				
				/**领取月累计充值福袋*/
				["ami"] void receiveMonthBag(int id) throws NoteException;
			};
		};
	};  
};

#endif