#ifndef  _CHAT_ICE_
#define  _CHAT_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			/** 关卡单元状态 */
			struct SceneStateUnit {
				int 				sceneId;			// 关卡编号
				byte				star;				// 星级
				bool				isCanChallenge;		// 是否能够挑战
				bool				isSweep;			// 是否可扫荡
			};
			sequence<SceneStateUnit>	SceneStateUnits;
			
			/** 关卡集数据 */ 
			struct SceneStateUnitGroup {
				SceneStateUnits		states;				// 关卡状态
				bool				isFront;			// 是否可往前
				bool				isBack;				// 是否可往后
			};
			
			/** 主界面数据 */ 
			struct DreamlandShow {
				SceneStateUnitGroup	group;				// 关卡组状态
				int					groupId;			// 当前关卡组ID
				int					curSceneId;			// 当前玩家所在关卡
				int					surplusChallengeNum;// 剩余挑战次数
				int					buyNum;				// 已购买次数
				bool				isRedPoint;			// 是否奖励红点
			};
			
			/** 通关奖励领取结果 */
			struct DreamlandSceneAwardResult {
				int					star;				// 当前领取奖励的星级
				IntStringSeq		awardItems;			// 奖励结果
			};
			
			/** 扫荡单元数据 */
			struct DreamlandSweepUnit {
				int 				sceneId;			// 关卡ID
				int					star;				// 当前领取奖励的星级
				IntStringSeq		awardItems;			// 奖励结果
			};
			sequence<DreamlandSweepUnit>	DreamlandSweepUnits;
			
			/** 扫荡结果数据 */
			struct DreamlandSweepResult {
				int					stopSceneId;		// 扫荡终止的关卡ID
				DreamlandSweepUnits	resultUnits;		// 扫荡结果
			};
			
			/** 排行榜单元数据 */
			struct DreamlandRankUnit {
				int					rank;				// 名次
				string				roleId;				// 角色编号
				string				roleName;			// 角色名
				string				headIcon;			// 头像
				int					vipLevel;			// VIP等级
				int					roleLevel;			// 角色等级
				int					starNum;			// 星数
				int					layerNum;			// 层数
			};
			sequence<DreamlandRankUnit>		DreamlandRankUnits;
			
			/** 排行榜结果数据 */
			struct DreamlandRankView {
				DreamlandRankUnits	ranks;				// 排行数据
				DreamlandRankUnit	selfUnit;			// 自身排行数据
			};
			
			/** 星数奖励界面单元数据 */
			struct DreamlandAwardUnit {
				int 				starNum;			// 星数
				bool 				isDraw;				// 是否已领取
			};
			sequence<DreamlandAwardUnit> 	DreamlandAwardUnits;
			
			/** 星数奖励界面数据 */
			struct DreamlandAwardView {
				int					allStarNum;			// 总星数
				DreamlandAwardUnits awardUnits;			// 界面数据
			};
			
			/** 商店界面物品单元数据 */
			struct DreamlandShopItemUnit {
				int 				id;					// 唯一编号，用于购买
				string				itemCode;			// 物品编号
				int					itemNum;			// 数量
				int					itemPrice;			// 价格
				int					rebate;				// 折扣
				bool				isExchange;			// 是否已兑换
			};
			sequence<DreamlandShopItemUnit>		 DreamlandShopItemUnits;
			
			/** 商店数据 */
			struct DreamlandShopItemView {
				int					nanHuaLing;			// 南华令
				int					refreshNum;			// 商店刷新次数
				DreamlandShopItemUnits shopItems;		// 商店物品数据
			};
			
			interface Dreamland {
				/** 幻境主界面数据(DreamlandShow) */
				["ami"] string dreamlandPage(int groupId) throws NoteException;
				
				/** 幻境关卡组切换(DreamlandShow) */
				["ami"] string dreamlandSwitchSceneGroup(int groupId, bool isFront) throws NoteException;
				
				/** 战斗开始 返回战报编号*/
				["ami"] string beginDreamland(int sceneId) throws NoteException;
				
				/** 战斗结束 返回星级 (DreamlandSceneAwardResult)*/
				["ami"]	string endDreamland(int sceneId, byte remainHero) throws NoteException;
				
				/** 扫荡(DreamlandSweepResult) */
				["ami"] string dreamlandSweep(int sceneId) throws NoteException;
				
				/** 领奖关卡通关奖励(DreamlandSceneAwardResult) */
				//["ami"] string drawSceneStarAward(int sceneId) throws NoteException;
				
				/** 排行(DreamlandRankView) */
				["ami"] string lookDreamlandRank() throws NoteException;
				
				/** 星数奖励界面(DreamlandAwardView) */
				["ami"] string dreamlandAwardPage() throws NoteException;
				
				/** 领取星数奖励(IntStringSeq) */
				["ami"] string drawStarAward(int star) throws NoteException;
				
				/** 商店界面(DreamlandShopItemView) */
				["ami"] string dreamlandShopPage() throws NoteException;
				
				/** 商店刷新 (DreamlandShopItemView) */
				["ami"] string dreamlandRefreshShop() throws NoteException,NotEnoughYuanBaoException,NotEnoughMoneyException;
				
				/** 商店物品兑换 (IntString) */
				["ami"] string buyDreamlandShopItem(int id) throws NoteException,NotEnoughYuanBaoException,NotEnoughMoneyException;
				
				/** 购买每日挑战次数 */
				["ami"] int buyChallengeNum() throws NoteException,NotEnoughYuanBaoException;
			};
		};
	};
};

#endif
