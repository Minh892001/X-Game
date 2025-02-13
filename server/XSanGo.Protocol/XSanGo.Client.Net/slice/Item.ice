#ifndef  _ITEM_ICE_
#define  _ITEM_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			interface Item{
				//升级阵法书 返回 FormationBuffView
				["ami"] ItemView levelUpFormationBuff(string id,StringSeq idArray) throws NoteException;
				
				//出售
				["ami"] void sale(string id,int count) throws NoteException;
				
				//物品使用
				["ami"] void useItem(string id,int count,string params)  throws NoteException;
				["ami"] ItemViewSeq useChestItem(string id,int count) throws NoteException,NotEnoughMoneyException;
//				["ami"] string useChestItem(string id,int count) throws NoteException;
				// 领取复合宝箱产出
				["ami"] void drawCompositeChestItem(int itemIndex, string itemId) throws NoteException;
				
				/**切换阵法进阶类型,type对应脚本里面值*/
				["ami"] void selectAdvancedType(int type) throws NoteException;
				
				/**阵法进阶,id用英文逗号分隔*/
				["ami"] void advancedFormationBuff(string ids) throws NoteException;
			};
		};   
	};
};


#endif