#ifndef  _ONLINEAWARD_ICE_
#define  _ONLINEAWARD_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//在线奖励
module com {
	module XSanGo{
		module Protocol{
			
			//在线礼包显示
			struct OnlineAwardView{
				int id;			//礼包模板ID
				long reqTime;	//剩余领取时间
				ItemViewSeq itemView;	//礼包中道具显示
			};
			
			interface OnlineAward {  
				//查询在线时间 ，剩余时间，单位：秒
				//["ami"] OnlineAwardView selectOnlineTime() throws NoteException;
				["ami"] string selectOnlineTime() throws NoteException;
				
				//领取在线奖励，返回下一个在线奖励的剩余时间，单位：秒
				//["ami"] OnlineAwardView getAward() throws NoteException;
				["ami"] string getAward() throws NoteException;
			};
		};   
	};
};

#endif
