#ifndef  _BUYJINBI_ICE_
#define  _BUYJINBI_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//点金手，元宝购买金币
module com{
	module XSanGo{
		module Protocol{
			//购买返回的数据
			struct BuyJinbiView{
				int money;	//得到的金币
				int crit;	//暴击概率，0：没有概率
			};
			
			interface BuyJinbi { 
				//查询购买次数
				["ami"] string selectBuyNum() throws NoteException;
				
				//元宝购买金币
				//["ami"] BuyJinbiView buyMoney() throws NoteException;
				["ami"] string buy() throws NoteException,NotEnoughYuanBaoException;
			};
		};   
	};
};

#endif
