#ifndef  _FORMATION_ICE_
#define  _FORMATION_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{		
		
			//单个彩蛋数据
			struct EggInfo {
				int id;			//ID 彩蛋1、2、3
				string itemId;	//奖励道具ID
				int num;		//奖励道具数量
			};
			
			sequence<EggInfo> EggInfoSeq;
			
			//彩蛋视图
			struct ColorfullEggView {
				EggInfoSeq eggInfoList;	//彩蛋列表
				int reqLevel; 	//活动等级要求
				int entry;//功能入口  1-进游戏后直接弹出  0-玩家在活动页面点击弹出 
				IntSeq colors; //所有彩蛋对应的颜色
				int joinTimes;//活动期间可参与次数
				string opentime;//活动有效时间
			};
			
			interface ColorfulEgg{
				//获取彩蛋视图
				["ami"] string getView() throws NoteException;
				
				//砸蛋
				["ami"] string brokenEgg(byte eggId) throws NoteException;
				
				//领取奖励
				["ami"] void acceptReward(string itemId, int num) throws NoteException;
				
			};
		};
	};
};	  

#endif