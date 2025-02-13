#ifndef  _MAKEWINE_ICE_
#define  _MAKEWINE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{ 
	module XSanGo{
		module Protocol{
			// 一条酿酒记录
			struct OneWineItem {
				int id;
				string targetItem;		// 合成目标
				IntStringSeq needItems; // 酿酒需求素材
				int lastCount;		    // 还可酿造次数
			};

			sequence<OneWineItem> OneWineItemSeq;
			
			// 定时领取的材料View
			struct MakeWineMaterialView{
				IntString receiveMaterial; // 下次可领取的材料
				IntString material;		// 返回当前领取的物品的数量
				long receiveTime; // 下次领取剩余时间(long 在协议里会转换成String)
				int receiveMaterialState; // 0:不可领取 1:可领取  2:已领取
			};
			
			// 积分奖励View
			struct MakeWineScoreAwardView {
				IntString scoreAward; // 下次可领取的积分奖励道具
				int receiveScoreLimit; // 积分奖励领取限制
				int receiveScoreState; // 当期领取状态 0:不可领取 1:可领取  2:已领取
			};
			
			// 酿酒界面
			struct MakeWineView {
				OneWineItemSeq oneWineItems; // 合成目标
				long activityLastTime; // 活动剩余时间(long 在协议里会转换成String)
				IntString receiveMaterial; // 下次可领取的材料
				long receiveTime; // 下次领取剩余时间(long 在协议里会转换成String)
				int receiveMaterialState; // 0:不可领取 1:可领取  2:已领取
				int myScore; // 我的当前积分
				IntString scoreAward; // 下次可领取的积分奖励道具
				int receiveScoreLimit; // 积分奖励领取限制
				int receiveScoreState; // 0:不可领取 1:可领取  2:已领取
			};
			
			// 酿酒返回结果
			struct MakeResult{
				int targetNum; // 目标拥有的数量
				IntStringSeq materialNums; // 当前材料剩余数量(防止物品回调很久才到的问题)
				int makedCount; // 当前请求酿造的次数
				int composeSocre; // 当前的酿酒积分
				int receiveMaterialState; // 领取积分奖励状态
				int gotScore;// 酿酒获得的积分
			};
			
			// 分享的一个物品
			struct MakeWineShareTarget{
				int id;
				string targetItem;		// 分享目标
				int groupNum;			// 一组的数量
			};
			sequence<MakeWineShareTarget> ShareTargetSeq;
			
			// 分享记录
			struct MakeWineShareRecord {
				string recordId; // 记录ID
				string itemId;   // 分享的道具
				string roleId;	 // 角色Id
				string name;	 // 玩家名字
				int lastCount;	 // 剩余数量
				int isFriend;	 // 是否是好友0:非 1:是
				int state;		 // 0:不可领取 1:可领取 2:已领取
				int toped;		 // 是否已经置顶 0:未 1:已置顶 2:不能置顶
				int topCost;	 // 置顶花费
				int topLastCount;// 置顶剩余次数
				string shareTime;// 置顶时间
				int receiveShareScore; // 被领取1瓶能获得的积分
			}; 
			sequence<MakeWineShareRecord> ShareRecordSeq;
			
			// 分享界面
			struct MakeWineShareView{
				long nextReceiveTime; // 下次可领取CD
				ShareTargetSeq shareTargets; // 分享的目标
				ShareRecordSeq shareRecords; // 分享列表记录
				int totalRecords; // 总记录条数
				int shareScore;	// 分享积分
			};

			//兑换条目
			struct MakeWineExchangeItem{
				int id;			// 编号
				IntString item; // 兑换的道具和数量
				int needScore; // 需要的积分
			};
			
			sequence<MakeWineExchangeItem> MakeWineExchangeItemSeq;
			
			// 兑换界面
			struct MakeWineExchangeView{
				MakeWineExchangeItemSeq makeWineExchangeItems;
				int myComposeScore; // 我的酿酒积分
			}; 
			
			// 积分排名一条数据
			struct MakeWineScoreRankItem {
				int rank;
				int score;
				int level;
				int vip;
				string name;
				string headImg;
			};
			
			sequence<MakeWineScoreRankItem> MakeWineScoreRankItemSeq;
			
			// 积分排名界面
			struct MakeWineScoreRank{
				int intoRankScore; // 入榜积分
				string sendAwardTime;// 奖励发放时间
				MakeWineScoreRankItemSeq makeWineScoreRankItems; // 排行记录
				int myRank;
				int myScore;
				int myLevel;
				int myVip;
				string myName;
				string myHeadImg;
			};
	
			// 积分排名奖励的一条数据
			struct MakeWineScoreRankAwardItem {
				string rank;
				IntStringSeq awards;
				int sendAwardScore; // 发放奖励的积分限制
			};
			
			sequence<MakeWineScoreRankAwardItem> MakeWineScoreRankAwardItemSeq;
			
			// 积分排名奖励界面
			struct MakeWineScoreRankAward {
				long sendAwardTime; // 发奖时间
				MakeWineScoreRankAwardItemSeq makeWineScoreRankAwardItems;
				int myScore; // 我的当前积分
			};
			
			// 酒的信息和奖励信息
			struct MakeWineAwardInfo {
				string itemId;//酒的模版ID
				IntString shareAwardItemId;// 分享获得的奖励ID
				IntString receiveItemId; // 领取分享获得的道具ID
				int makeScore; // 酿造获得的酿造积分
				int shareScore; // 分享被领取获得的积分
			};
			
			interface MakeWineInfo {
				/** 酿酒界面, 返回 MakeWineView */
				["ami"] string makeWineView() throws NoteException;
				
				/** 定时 领取材料  MaterialView*/
				["ami"] string receiveMaterial() throws NoteException;
				
				/** 定时 积分奖励 return ScoreAwardView*/
				["ami"] string receiveScoreAward() throws NoteException;
				
				/** 酿酒 id:目标ID,   type: 0:酿酒一瓶, 1:全部酿酒,酿完该种类的酒  return MakeResult */
				["ami"] string make(int id, int type) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException;
				
				// 分享界面 condition 0:全部   1: 只看好友 return 2:我的分享 ,   startIndex:分页起始记录     MakeWineShareView
				["ami", "amd"] string shareView(int condition, int startIndex) throws NoteException;
				
				/** 分享 id:目标 , count:多少组*/
				["ami"] void share(int id, int count) throws NoteException;
				
				/** 领取分享奖励 condition 0:全部   1: 只看好友 return 2:我的分享 ,   startIndex:分页起始记录     MakeWineShareView*/
				["ami", "amd"] string receiveShare(string id, int condition, int startIndex) throws NoteException;
				
				/** 置顶  id:记录ID*/
				["ami", "amd"] void topUp(string id) throws NoteException,NotEnoughYuanBaoException;
				
				/**兑换界面 MakeWineExchangeView*/
				["ami"] string exchangeView() throws NoteException;
				
				/** 兑换 id:编号, num:多少组*/
				["ami"] void exchange(int id, int num) throws NoteException;
				
				/** 查看积分榜 return MakeWineScoreRank*/
				["ami"] string scoreRank() throws NoteException;
				
				/** 查看排名奖励 return MakeWineScoreRankAward*/
				["ami"] string scoreRankAward() throws NoteException;
				
				/** 查看 酒的详情和奖励 return MakeWineAwardInfoSeq*/
				["ami"] string wineInfoView() throws NoteException;
				
				/** 查看 日志信息 1:酿造   2:领取分享  3:兑换
					酿酒日志：【05-06 17：30：59】,酿造【果酒×10】，【酿造积分+10】，【兑换积分+10】
					领取日志：【05-06 17：30：59】,玩家【玩家ID】领取你分享的【葡萄酒X2】，【分享积分+10】
					兑换日志：【05-06 17：30：59】,兑换【紫武器宝箱X1】，花费【100兑换积分】
				*/
				["ami"] string seeLog(int type) throws NoteException;
				
			};
		};
	};  
};

#endif