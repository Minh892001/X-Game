#ifndef  _FORMATION_ICE_
#define  _FORMATION_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{		
		
			//铁匠铺 商城 道具数据
			struct SmithyMall {
				int id;			//ID
				string itemId;	//商品ID
				int num;		//商品数量
				int price;		//商品价格
				int coinType;	//商品价格,0:必须消耗竞技币,1:大于等于该vip等级可免费领取
				int flag;		//商品状态 0：已经售完，1：可以购买
				int halfPriceFlag;		//是否半价 0：全价，1：显示半价
			};
			
			sequence<SmithyMall> SmithyMallSeq;
			struct SmithyMallSel {
				int exchangeRefreshNum; 	//铁匠铺 商城 刷新次数
				SmithyMallSeq SmithyMallList;	//铁匠铺商城 道具列表
				string word;//NPC台词
				int cost;//刷新消耗元宝数
			};
			
			struct Preview{
				int type;//道具类型  0-武器  1-防具  2-饰品  3-宝物  4-坐骑
				StringSeq itemIds;//道具id集合
			};
			sequence<Preview> PreviewSeq;
			
			interface SmithyExchange{
				//兑换列表
				//["ami"] SmithyMallSel selMallList() throws NoteException;
				["ami"] string selMallList() throws NoteException;
				
				//刷新兑换列表
				//["ami"] SmithyMallSel refMallList() throws NoteException, NotEnoughYuanBaoException;
				["ami"] string refMallList() throws NoteException, NotEnoughYuanBaoException;
				
				//兑换, 目前数量是全部兑换，预留参数
				//["ami"] SmithyMallSel exchangeItem(int storId) throws NoteException;
				["ami"] string exchangeItem(int storId) throws NoteException;
				
				//蓝装兑换
				["ami"] string selBlueMallList() throws NoteException;
				//刷新蓝装兑换列表
				["ami"] string refBlueMallList() throws NoteException, NotEnoughMoneyException;
				//兑换蓝装
				["ami"] string exchangeBlueItem(int storId) throws NoteException;
				
				//紫装兑换预览图鉴
				["ami"] string preview() throws NoteException;
			};
		};
	};
};	  

#endif