#ifndef _AUCTIONHOUSE_ICE_
#define _AUCTIONHOUSE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
    module XSanGo{
        module Protocol{
            // 拍卖行人物主角信息
            struct AuctionRoleView {
                string id; // ID
                string name; // 昵称
                int vipLv; // Vip 等级
            };
            sequence<AuctionRoleView> AuctionRoleViewSeq;
            // 拍卖行列表 view
            struct AuctionHouseItemView {
                string id;
                ItemView name; // 拍卖品
                long basePrice; // 起拍价
                long nextPrice; // 下次叫价价格
                long fixedPrice; // 一口价
                AuctionRoleViewSeq seller; // 卖方
                AuctionRoleViewSeq bidder; // 买方
                long lastTime; // 剩余时间
            };
            sequence<AuctionHouseItemView> AuctionHouseViewSeq;
            // 拍卖行view
            struct AuctionHouseView {
                long coin; // 当前拍卖币数量
                AuctionHouseViewSeq items; // 拍卖品列表
                int totalItemCount; // 总共的拍卖品数量
            };

            // 拍卖行,竞价结果
            struct AuctionBuyResView {
                long coin; // 当前拍卖币数量
                int flag; // 是否超过了一口价或者最大竞拍金额而直接购买, 0, 没有购买;1, 购买了
            };
            
            struct AuctionShopView{
            	int id;
            	string itemId;
            	string name;
            	int num;
            	int price;
            	bool isBuy;//是否已购买
            };
	 		sequence<AuctionShopView> AuctionShopViewSeq;
	 		//拍卖行商城view
            struct AuctionStoreView{
            	int refreshCount;//已刷新次数
            	int refreshCoin;//刷新需要的拍卖币  -1表示刷新次数已用完
            	AuctionShopViewSeq shops;
            };
	 		
            interface AuctionHouse {
                // 接口中的type定义(0,全部;20,装备宝箱;21,阵法宝箱;22,将魂宝箱;23,其他宝箱)
                // 品质 quality(0,全部;1,绿装;2,蓝装;3,紫装;4橙装)
                //兑换拍卖币
                ["ami"] long exchange(long price) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
                // 获取所有拍卖品列表 return AuctionHouseView, 参数direction表示排序的方向;0,按照上架时间正续;1,倒序
                ["ami"] string getAuctionHouseItems(int startIndex, int count, int type, string key, int quality, int direction) throws NoteException;
                // 我的竞拍(我叫价的拍卖品) return AuctionHouseView
                ["ami"] string getMyBidItems(int startIndex, int count, int type, string key, int quality, int direction) throws NoteException;
                // 我的拍卖(我卖的拍卖品) return AuctionHouseView
                ["ami"] string getMySellItems(int startIndex, int count, int type, string key, int quality, int direction) throws NoteException;
                // 卖一件东西(物品id, 个数, 基础价格, 一口价) 如果一口价没设置则传-1
                ["ami"] void sell(string id, int num, long price, long fixedPrice) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
                // 出价买一件东西, type 表示 普通叫价(1) 还是 一口价(2)
                ["ami"] AuctionBuyResView buy(string id, int type) throws NoteException;
                // 取消拍卖
                ["ami"] void cancelAuction(string id) throws NoteException;
                
                // 获取商城物品 返回AuctionStoreView的lua
                ["ami"] string getAuctionShops() throws NoteException;
                
                // 刷新商城物品 返回AuctionStoreView的lua
                ["ami"] string refreshAuctionShop() throws NoteException;
                
                // 兑换商城物品 返回当前拍卖币
                ["ami"] long buyAuctionShop(int id) throws NoteException;
            };
        };
    };
};

#endif