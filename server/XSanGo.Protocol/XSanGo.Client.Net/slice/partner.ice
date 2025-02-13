#ifndef  _FORMATION_ICE_
#define  _FORMATION_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{		
		
			struct PropertyConfig {
				int propId;			//属性ID
				string propName;			//属性名称
				int color;			    //属性颜色
				int propPercent;		//属性加成百分比
			};
			
			sequence<PropertyConfig> PropertyConfigSeq;
		
			struct PartnerViewInfo {
				string heroId;			//武将ID
				string specialHero;		//特殊武将ID
				int pos;				//武将位置
				int heroFlag;			//是否有武将   0:没有武将  1:有武将
				int openFlag;			//是否开放	  0:未开放   1:已经开放
				PropertyConfig proConfig; //伙伴属性
				string posName;			//阵位名称
				int level;				//阵位开启等级
				int currencyType;       //开启阵位所需货币类型
				int currencyNum;        //开启阵位所需货币数量
				int vipLevel;			//vip免费开启阵位等级
				int lockPay;			//锁定该位置武将后仅充值属性的缘分丹消耗
				int openConditionFlag;  //主公等级与vip等级是否同时为真，还是只要有一个为真的情况下开启，1-或  2-和
				string lockPropItem;    //锁定属性消耗的道具模板id
				int lockPropCost;       //锁定属性消耗的道具数量
			};
			
			sequence<PartnerViewInfo> PartnerViewInfoSeq;
				
			struct PartnerView {
				int yfdNum;				//玩家拥有的缘分丹数量
				PartnerViewInfoSeq infoSeq;
			};
			
		
			interface Partner{
				/**获取伙伴列表*/
				["ami"] string getPartnerViewList()throws NoteException ;
				/**设置伙伴武将*/
				["ami"] void setHeroPosition(byte postion, string heroId, string oldHeroId)throws NoteException;
				/**开启伙伴阵位*/
				["ami"] string openPartnerShipPos(byte postion)throws NoteException;
				/**重置伙伴阵位*/
				["ami"] string resetPartnerPos(byte postion, int cost, int isLock)throws NoteException;
				/**清空所有伙伴阵位*/
				["ami"] void clearAll()throws NoteException;
				/**获取伙伴开启等级条件*/
				["ami"] string getLevelRequired()throws NoteException;
			};
		};
	};
};	  

#endif