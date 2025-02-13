#ifndef _FRIENDSRECALL_ICE_
#define _FRIENDSRECALL_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com {
	module XSanGo {
		module Protocol {
			// 任务数据
			struct FriendsRecallTaskView {
				int taskId; // 任务id
				int state; //状态, 0:可见,未完成; 1:已完成，未领奖; 2:已领奖
				string title; // 任务抬头
				string content;	// 内容
				string icon; // 图标
				int rewardExp; //奖励经验
				int rewardGold; //奖励金币
				string itemCode; //奖励道具code
				int itemNum; //奖励道具数量
				int currNum; //当前完成数量
				int targetNum; //需要达成数量
				int totalYuanbao; //本类任务共可获得的元宝数
			};
			sequence<FriendsRecallTaskView> FriendsRecallTaskViewSeq;
			
			//已召回玩家数据
			struct RecallRoleView{
				string roleId; //角色id
				string roleName; //角色名字
				int level; //等级
				int vipLevel; //vip等级
				string headImg; //任务头像
				string guildName; //公会名称
				string recallDate; //召回日期描述
			};
			sequence<RecallRoleView> RecallRoleViewSeq;
			
			// 回归有礼和邀请任务界面
			struct RecallView {
				bool canRecall; //是否有资格回归
				bool isFirstOpen; // 是否第一次打开界面
				bool isRecalled; //是否已回归
				FriendsRecallTaskViewSeq taskList; //回归有礼任务列表
				FriendsRecallTaskViewSeq invitationTaskList; //邀请任务列表
			};
			
			//随机的一个离线玩家数据
			struct OfflineRoleView {
				string roleId; //角色id
				string roleName; //角色名字
				string headImg; //任务头像
				int level; //等级
				int vipLevel; //vip等级
				string guildName; //公会名称
				int offlineDays; //离线天数
			};
			
			sequence<OfflineRoleView> OfflineRoleViewSeq;
			
			// 邀请玩家界面
			struct InvitationView {
				string invitationCode;  // 我的邀请码
				RecallRoleViewSeq roleList; // 已召回的玩家列表
				OfflineRoleViewSeq offlineViewSeq ; //离线玩家		
			};
			
			struct showIconView{
				bool result; //是否显示图标
			};
			
			interface FriendsRecall {
				// 打开回归有礼界面, return RecallView
				["ami"] string openRecall() throws NoteException;
				
				// 打开邀请玩家界面, return InvitationView
				["ami","amd"] string openInvitation() throws NoteException;
				
				// 激活邀请码,  return FriendsRecallTaskViewSeq
				["ami"] string activeInvitationCode(string code) throws NoteException;
									
				// 领奖, return  FriendsRecallTaskViewSeq
				["ami"] string receiveTaskReward(int taskId) throws NoteException, NotEnoughMoneyException;
				
				// 主界面是否显示入口图标, return  showIconView
				["ami"] string showFriendsRecallIcon() throws NoteException;
				
				// 随机几个离线的有回归资格的玩家，返回OfflineRoleViewSeq
				["ami","amd"] string randomOfflineRole(string currOfflineRoleId) throws NoteException;
			};
		};
	};
};

#endif