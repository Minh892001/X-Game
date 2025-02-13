#ifndef  _ACHIEVE_ICE_
#define  _ACHIEVE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{ 
	module XSanGo{
		module Protocol{
			// 成就信息
			struct AchieveInfoSub {
			    int order; //顺序
				int id; // 成就ID
				int status; //状态 0:未完成 1：可领取 2：已领取 3：完成整个条目
				int progress; // 进度
			    
			};
			sequence<AchieveInfoSub> AchieveInfoSubSeq;
			// 成就进度奖励信息
			struct AchieveProAwardSub {
			    int progress; //进度
				IntStringSeq awards;	// 奖励物品
				int status; //状态 0:未完成 1：可领取 2：已领取 
				string tipstx; // 头像说明
			};
			sequence<AchieveProAwardSub> AchieveProAwardSubSeq;
			
			// 成就界面
			struct AchievePageView {
			    int canRecProgress;//是否可领成就进度奖励 0:不可 1：可以
			    int completedNum; // 完成数量
				int maxNum; // 总数
				AchieveInfoSubSeq achieveList;
			};
			interface AchieveInfo{
				/** 成就界面, 返回 AchievePageView */
				["ami"] string achievePageView(int functionId) throws NoteException;
				/** 成就领奖, 返回 AchievePageView */
				["ami"] string achieveReward(int id) throws NoteException;
				
				/** 成就进度奖励界面, 返回 AchieveProAwardSubSeq */
				["ami"] string achieveProgressView() throws NoteException;
				/** 成就进度领奖, 返回 AchieveProAwardSubSeq */
				["ami"] string achieveProgressReward(int progress) throws NoteException;
			};
		};
	};  
};

#endif