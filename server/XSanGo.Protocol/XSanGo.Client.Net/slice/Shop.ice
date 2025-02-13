//商城功能模块协议
#ifndef  _SHOP_ICE_
#define  _SHOP_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{	
			//商城商品view
			struct ShopView {
				// ItemView			 
			 	string id;
				ItemType iType;
				string templateId;
				int num;    
				// ItemView end
				
				string name;//商品名称
				string remark;//描述
				int price;//原价
				int discountPrice;//折扣价
				string startTime;
				string endTime;
				int buyTimes;//已购买数量
				int maxBuyTimes;//可购买数量-1表示无限
				int buyVipLevel;//购买需要vip等级
				int buyLevel;//购买需要主公等级
				int tag;//角标 -1无0-限时1-热卖2-新品3-等级礼包
				string tips;//购买提示
				string icon;//商品图片 不为空就使用
				int remainSecond;//折扣倒计时，为0或负数不显示
				bool isDiscount;//是否显示折扣价
				bool isUseOut;//购买完是否永久消失
				int discountIcon;// 折扣图标-1-不显示折扣图标0-免费1-1折2-2折……
				int fadeSecond;// 消失倒计时，为0或负数不显示
			};
			
			interface Shop{
				/**获取商品列表 lua格式的ShopView[] type 0-商城 1-礼包*/
				["ami"] string getShopView(int type) throws NoteException;
				/**购买商品 type 0-商城 1-礼包*/
				["ami"] void buyItem(int num,string id,int type) throws NoteException,NotEnoughYuanBaoException;
			};
		};   
	};
};


#endif