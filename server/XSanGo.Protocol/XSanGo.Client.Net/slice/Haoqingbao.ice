#ifndef _HAOQINGBAO_ICE_
#define _HAOQINGBAO_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com {
	module XSanGo {
		module Protocol {
			// 豪情宝账户操作记录
			struct HaoqingbaoRecordView {
				string headImage; // 头像
				int level; 		// 等级
				int vipLevel;	// vip等级
				string date; // 日期
				string desc; // 描述
				int num;     // 元宝数量
			};
			sequence<HaoqingbaoRecordView> HaoqingbaoRecordViewSeq;
			
			// 豪情宝界面
			struct HaoqingbaoView {
				int chargeStatusTime; // 充值状态维持时间
				int yuanbaoNum; // 账户余额
				HaoqingbaoRecordViewSeq records; // 历史记录
				int maxWords; // 留言最多字数
				int maxYuanbao; // 红包最大的金额
			};
			
			// 排行榜项目
			struct RankItemView {
				int rank;  // 排名
				string headImage; // 头像
				string roleId; // ID
				int level; // 等级
				int vipLevel; // vip等级
				string name; // 昵称
				long totalNum;// 总金额				
			};
			sequence<RankItemView> RankItemViewSeq;
			// 排行榜界面
			struct RankView {
				int currentRank; // 当前排名
				long currentNum; // 当前一共发了多少元宝
				RankItemViewSeq recvItems; // 红包榜
			};
			
			// 红包详情每一项
			struct RedPacketDetailItemView {
				string headImg; // 头像
				string name; // 名称
				int level; // 等级
				int vipLevel; // vip等级
				int yuanbaoNum; // 元宝数量 
				int luckyStar; // 运气王 ==1的时候   是运气王
				string date; // 时间
			};
			sequence<RedPacketDetailItemView> RedPacketDetailItemViewSeq;
			// 红包详情界面
			struct RedPacketDetailView {
				string senderHeadImg; // 发红包玩家头像
				string senderName; // 发红包玩家昵称
				int senderLevel; // 发红包玩家等级
				int senderVipLevel; // 发红包玩家vip等级
				int totalNum; // 红包数量
				int lastNum; // 剩余数量
				int endTime; // 几小时抢完
				string msg; // 留言
				RedPacketDetailItemViewSeq items; // 领取记录
			};
			// 我的红包界面
			struct MyRedPacketView {
				int totalYuanbao; // 总共收到的元宝数量
				int recvNum; // 收到的红包数量
				int luckyStarNum; // 运气王数量
				RedPacketDetailItemViewSeq items;// 领取记录
			};
			
			// 抢红包返回结果
			struct RecvRedPacketResultView {
				int num; // >0 的表示抢到了多少元宝，0表示抢光了，-1表示已经抢过了不可以重复抢, -2表示红包已经结束， -3表示抢红包次数达到上限
				int friendPoint;
			};
			
			struct PreRecvRedPacketResultView {
				int status; // 返回的状态码:0,正常可以抢;1,红包已结束;2,已经抢过了;3,今日次数已达上限;4,该红包是自己发的红包，不可以抢;5,已抢完，红包未结束
				int isMine; // 是否是自己发的红包:1,是;0,不是
			};
			
			interface Haoqingbao {
				// 获取豪情宝界面, return HaoqingbaoView
				["ami", "amd"] string openHaoqingbao() throws NoteException;
				
				// 发红包, type:类型，1，工会；2，好友；3，全服, 
				// minLevel:最低等级, minVipLevel:最低vip等级, 
				// range:派发范围(0/1/2):(在线成员/会长和长老/所以成员),(所以成员/在线好友),(所以好友), 
				// minFriendPoint:最低友情点数, 
				// totalYuanbaoNum:红包总金额, 
				// packetNum:红包数量, 
				// msg:留言
				// return HaoqingbaoView
				["ami", "amd"] string sendRedPacket(int type, int minLevel, int minVipLevel, int range, 
									int minFriendPoint, int totalYuanbaoNum, int packetNum, string msg) 
									throws NoteException;
									
				// 抢红包,return  RecvRedPacketResultView
				["ami"] string recvRedPacket(string packetId) throws NoteException;
				
				// 排行榜,type:1,发红包；2,抢红包,return RankItemView[]
				["ami", "amd"] string rankList(int type) throws NoteException;
				
				// 红包详情，return RedPacketDetailView
				["ami", "amd"] string getRedPacketDetail(string packetId) throws NoteException;
				
				// 我的红包, return MyRedPacketView
				["ami", "amd"] string myRedPacket() throws NoteException;
				
				// 提出, return HaoqingbaoView
				["ami", "amd"] string checkout(int num) throws NoteException;
				
				// 豪情宝充值
				["ami"] void charge() throws NoteException;
				
				// 预先请求抢红包接口, return PreRecvRedPacketResultView
				["ami"] string preRecvRedPacket(string packetId) throws NoteException;
				
				// 索要红包接口
				["ami"] void claimRedPacket(string roleId, string packetId) throws NoteException;
			};
		};
	};
};

#endif