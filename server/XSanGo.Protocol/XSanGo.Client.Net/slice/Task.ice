#ifndef  _TASK_ICE_
#define  _TASK_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			//任务类型
			enum TaskType {
				defaultTaskType, 
				pass,		//主线过关任务
				daily,		//日常任务
				success,	//成就任务
				dailyTime, 	//日常限时
				vipTask,	//VIP任务
				guideTask,	//引导任务
				vipMonth,	//VIP月卡
				redPacket   //红包
			};
			
			//任务列表项数据
			struct TaskItem {
				int taskId;		//任务ID
				int type;		//任务类型 ，1：过关，2：日常，3：成就，4:日常中的限时任务
				int taskNum;	//完成任务数量
				int taskState;	//任务状态 ,0:未完成，1：可以领取任务，2：过时未完成，3：完成
				long taskTime;	//完成任务需要的时间，单位：秒, 0:代表不需要时间完成
			};
			sequence<TaskItem> TaskItemSeq;

			// 活跃奖励数据
			struct ActPointAward{
				int id;		// 奖励Id
				int point;	// 需达到的活跃点数
				IntStringSeq awards;	// 奖励物品
				int state;	// 奖励状态 0:不可领取 1:可领取 2:已领取
				string icons;	// 奖励图标
			};
			
			sequence<ActPointAward> ActPointAwardSeq;

			// 七日目标数据
			struct SevenTarget{
				int dayIndex;	// 当前第几天
				IntSeq taskId;	// 任务编号
				StringSeq contents;	// 内容描述
				IntSeq states;		// 完成状态 0:未完成  1:完成未领取 2:已领取 
				IntStringSeq golds; // 奖励道具,数量
				IntIntPairSeq progress; // 当前进度,最大数量
			};

			sequence<SevenTarget> SevenTargetSeq;

			// 七日三星奖励
			struct SevenThreeStarReward{
				string sceneId;	// 底图ID
				IntStringSeq awards;	// 奖励道具
				int states;		// 完成状态 0:未完成  1:完成未领取 2:已领取 
				string title;	// 标题
			};
			sequence<SevenThreeStarReward> SevenThreeStarRewardSeq;

			// 七日目标星际奖励
			struct SevenTargetStarReward{
				int star;	// 星数
				IntStringSeq reward;	// 奖励内容
				int state;			// 奖励状态 0:不可领取 1:可领取 2:已领取
				string icons;	// 图标
			};

			sequence<SevenTargetStarReward> SevenTargetStarRewardSeq;
			
			// 七日目标界面
			struct SevenTargetView{ 
				int currentStar;	// 当前累计星数
				int day;			// 今天是第几天
				SevenTargetStarRewardSeq sevenTargetStarRewards;
				SevenTargetSeq sevenTargets;
				SevenThreeStarRewardSeq sevenThreeStarRewards;
			};			

			// 任务界面数据
			struct TaskView{
				TaskItemSeq taskItems;
				int curActPoint;	//  当前活跃点
				ActPointAwardSeq actPointAwards; // 活跃奖励数据
				SevenTargetView sevenTarget;	// 七日目标数据
			};

			interface task {
				
				//显示任务列表
				//["ami"] TaskView selectTask() throws NoteException;
				["ami"] string selectTask() throws NoteException;
				
				//领取任务奖励
				//["ami"] TaskView finishTask(int taskId, int type) throws NoteException, NotEnoughMoneyException;
				["ami"] string finishTask(int taskId, int type) throws NoteException, NotEnoughMoneyException;

				// 领取活跃奖励
				//["ami"] void receiveActAward(int awardId) throws NoteException;
				["ami"] void receiveActAward(int awardId) throws NoteException;

				// 七日目标-领取星级奖励
				//["ami"] void receiveStarAward(int star) throws NoteException;
				["ami"] void receiveStarAward(int star) throws NoteException;
                /** 七日目标-领取每日目标奖励, 返回 SevenTargetView */
				// ["ami"] SevenTargetView receiveTodayAward(int index) throws NoteException;
				["ami"] string receiveTodayAward(int index) throws NoteException;

				// 七日目标-领取每日三星奖励
				//["ami"] void receiveThreeStarAward() throws NoteException;
				["ami"] void receiveThreeStarAward() throws NoteException;
			};
				
		};   
	};
};

#endif
