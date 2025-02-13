#ifndef  _LUCKY_BAG_ICE_
#define  _LUCKY_BAG_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{ 
	module XSanGo{
		module Protocol{
			
			//累充返利数据
			struct SumChargeView {
				int id;//配置id
				int money;//配置的累计充值金额
				int ration;	//配置的返现元宝
				int leftChargeAmount;//领取奖励的充值差额
				int acceptChargeFlag;//是否已经领取过
			};
			
			sequence<SumChargeView> SumChargeSeq;
			
			// 超级充值view
			struct SuperChargeView {
				int totalCharge;//累计充值元宝
				SumChargeSeq sumChargeList;
				string welfareDesc;//福利说明
				int raffleNum; //抽奖次数
				int redPoiFlag;//是否有红点  1 可以领取，有红点
			};
			
			//超级转盘奖励领取
			struct ReceivedRaffleInfo{
				string roleName;//领取奖励角色名称
				string itemId;//领取物品id
				string receiveTime;//领取时间
				int num;//领取物品数量
				int vipLevel;//vip等级
			};
			sequence<ReceivedRaffleInfo> ReceivedRaffleInfoSeq;
			//超级转盘抽奖物品信息
			struct RaffleItemInfo{
				int id;//脚本id
				string itemId;//抽奖物品id
				int num;//数量
			};
			
			//超级转盘抽奖返回的view
			struct RaffleReceivedView{
				RaffleItemInfo itemInfo;//领取的物品信息
				string requiredItem;//必中物品
				int requiredNum;//达到必中物品的剩余抽奖次数数量
				int redPoiFlag;//是否有红点  1 可以领取，有红点
			};
			
			sequence<RaffleItemInfo> RaffleItemInfoSeq;
			
			struct RaffleView{
				ReceivedRaffleInfoSeq receivedList;//领奖信息
				RaffleItemInfoSeq raffleItemList;//抽奖物品信息
				int requiredNum;//必中物品的剩余抽奖次数
				string itemId;//必中物品id
				int num;//可抽奖次数
				string desc;//超级转盘抽奖说明
			};
			
			interface SuperCharge{
				/**获取感恩回馈VIEW，返回SuperChargeView的lua*/
				["ami"] string getSuperChargeView() throws NoteException;
				
				/**领取充值奖励*/
				["ami"] string receiveSuperChargeReward(int id) throws NoteException;
				
				/**获取超级转盘view,返回RaffleView的lua*/
				["ami","amd"] string getRaffleView() throws NoteException;
				
				/**转盘抽奖*/
				["ami"] string acceptRaffleReward() throws NoteException;
				
				/**获取领奖列表信息*/
				["ami","amd"] string getReceivedViews() throws NoteException;
			};
		};
	};  
};

#endif