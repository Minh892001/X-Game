#ifndef _FIGHT_AREA_
#define _FIGHT_AREA_

#include "ExceptionDef.ice"
#include "Common.ice"
#include "Copy.ice"

module com{
    module XSanGo{
        module Protocol {
            // 武将状态信息
            struct HeroStatus {
                string heroId; // 武将ID
                PropertySeq properties; // 非成长武将属性， 血量(hp)，hp为零的化表示该武将挂了
                GrowablePropertySeq growableProperties; // 武将成长属性
            };
            sequence<HeroStatus> HeroStatusSeq;

            // 北伐城池节点奖励
            struct CastleNodeRewardView {
                string templateId;
                int num;
            };
            sequence<CastleNodeRewardView> CastleNodeRewardSeq;

            // 北伐节点战斗信息，对应于关卡
            struct CastleNodeView {
                int id;
                PropertySeq properties; // 非成长属性集合, 星级等
                GrowablePropertySeq growableProperties; // 武将成长属性
                PvpOpponentFormationView opponent; // 敌方武将配置
                
                string movieId; // 战报ID
            };
            sequence<CastleNodeView> CastleNodeViewSeq;

            // 北伐界面显示数据
            struct AttackCastleView {
                int currentNodeId; // 当前北伐进度，关卡ID
                int attackCount; // 剩余挑战次数
                long resetTime; // 重置时间
                int prestige; // 声望
                bool clear; // 是否可以扫荡
                int clearVipLevel; // 扫荡所需vip等级
                // 关卡节点信息
                CastleNodeViewSeq castleNodes;
            };


            // 北伐节点对手信息
            struct CastleOpponentView {
                string id;
                string name;
                string icon;
                int level;
                int vip;
                int sex;
                int prestige; // 声望
                bool canRefresh; // 是否可以更换对手
                int refreshPrice;// 更换对手消耗的元宝数量

                FormationSummaryViewSeq guardHeroArr;   //防守部队 武将信息
            };

            // 结束战斗信息
            struct EndAttackCastleView {
                int star;
                CastleNodeRewardSeq rewards;
                int addGold;// 公会科技增加的金币
            };

            // 北伐 商城 道具数据
            struct AttackCastleShopItemView {
                int id;             // ID
                string itemId;      // 商品ID
                int num;            // 商品数量
                int price;          // 商品价格
                int coinType;       // 货币类型,0:必须消耗竞技币,1:大于等于该vip等级可免费领取
                int flag;       //商品状态 0：已经售完，1：可以购买
            };
            sequence<AttackCastleShopItemView> AttackCastleShopItemViewSeq;

            // 北伐 商城 数据
            struct AttackCastleShopView {
                int refreshCount; // 商城刷新次数
                AttackCastleShopItemViewSeq itemList; // 商城道具列表
            };
            
            // 北伐领取宝箱结果
            struct AcceptRewardResultView {
                int prestige;
                ItemViewSeq items;
                int heroId; // 整卡武将奖励id，0表示没有整卡武将奖励
                bool alreadyHas; // 是否已拥有
                int heroSoulNum; // 武将转换为將魂的数量
            };
            
            struct HeroSoulPair {
            	int id; // 关卡id
            	int heroId; // 整卡武将奖励id，0表示没有整卡武将奖励
            	bool alreadyHas; // 是否已拥有
                int heroSoulNum; // 武将转换为將魂的数量
            };
            sequence<HeroSoulPair> HeroSoulPairSeq;
            sequence<CopyClearResultView> CopyClearResultViewSeq;
            
            // 北伐扫荡结果
            struct ClearResultView {
            	CopyClearResultViewSeq normalResult;
            	HeroSoulPairSeq heroResult;
            };

            interface AttackCastle {
                // 进入北伐界面, 返回 AttackCastleView
                ["ami"] string requestAttackCastles() throws NoteException;
                // 重置北伐, 返回 AttackCastleView
                ["ami"] string resetAttackCastles() throws NoteException;
                // 获取关卡对手信息, 返回 CastleOpponentView
                ["ami", "amd"] string getCastleOpponentView(int castleNodeId) throws NoteException;
                // 开始一个关卡
                ["ami"] CastleNodeView beginAttackCastle(int castleNodeId) throws NoteException;
                // 退出战斗
                ["ami"] void exitAttackCastle(int castleNodeId) throws NoteException;
                // 完成一个关卡, 传入关卡ID, 现在武将状态, 输赢, 返回 EndAttackCastleView
                ["ami"] string endAttackCastle(int castleNodeId, byte remainHero) throws NoteException;
                // 领取奖励, 传入关卡 ID 和选择的星数, 返回 ItemViewSeq
                ["ami"] string acceptRewards(int castleNodeId, int startCount) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
                // 商城商品列表, 返回 AttackCastleShopView
                ["ami"] string shopRewardList() throws NoteException;
                // 刷新商城商品列表, 返回AttackCastleShopView
                ["ami"] string refreshShopList() throws NoteException;
                // 兑换商品
                ["ami"] string exchangeItem(int itemId) throws NoteException;
                // 扫荡, 返回 ClearResultView
                ["ami"] string clearLevel() throws NoteException;
                // 换人, 返回 CastleOpponentView
                ["ami", "amd"] string refresh(int castleNodeId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
            };
        };
    };
};
#endif