#ifndef  _VIP_ICE_
#define  _VIP_ICE_

#include "RoleF.ice"
#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			struct VipTraderItem {
				int id;
				string itemId;
				int count;
				string name;
				int coinType;
				int price;
				int vipLevel;
				bool isBought;
			};
			
			sequence<VipTraderItem> VipTraderItemSequence;
			
			struct VipTraderView {
				//vip商城道具刷新时间
				string refreshingTime;
				//vip商城中的道具
				VipTraderItemSequence vipTraderItems;
			};
			struct TopupItem {//充值选项
				int id;
				string name;
				bool recommend;
				bool monthCard;
				string icon;
				int quality;
				int rmb;
				int yuanbao;
				string describe;
			};
			sequence<TopupItem> TopupItemSequence;
			
			struct TopupReward {
				string itemId;
				string name;
				string count;
			};
			sequence<TopupReward> TopupRewardSequence;
			struct TopupView {
				//首充奖励
				TopupRewardSequence firstTopupReward;
				//充值选项
				TopupItemSequence topupItems;
			};
			
			interface Vip {
				//购买vip特权商店中的道具 VipTraderItem
				["ami"] void buyVipTraderItems(int id) throws NoteException,NotEnoughMoneyException,NotEnoughYuanBaoException;
				//获得vip特权商店中的道具 VipTraderItem
				["ami"] string getVipTraderItems();
				//获得玩家每个vip等级的礼包是否已经购买掉了 IntIntPairSeq
				["ami"] string getGiftPackStatus();
				//获得vip特权礼包
				["ami"] void buyGiftPacks(int vipLevel) throws NoteException,NotEnoughYuanBaoException;
				//充值界面TopupVIew
				["ami"] string openTopupVIew();
				//检测月卡续费等
				["ami"] void checkChargeStatus(int chargeId,bool chargeForFriend) throws NoteException;
				
		        //从支付中心获取第三方充值订单号
				["ami","amd"] string getChannelOrderIdFromPayCenter(int channel,int appId,int money,string mac,string params)throws NoteException;
				
				//创建苹果应用商店的订单
				["ami","amd"] void createOrderForAppleAppStore(int templateId,string appStoreOrderId,int channel,int appId,string itemId,string mac,string params) throws NoteException;
            };
        };
 	};
 };

#endif