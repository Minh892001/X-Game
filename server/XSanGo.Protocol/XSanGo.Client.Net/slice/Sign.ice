#ifndef  _SIGN_ICE_
#define  _SIGN_ICE_

#include "RoleF.ice"
#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			// 签到相关的道具描述
			struct SignAward {
				int awardTypeId;	// 道具类型
				string templateId; // 奖励Id
				int count;	//道具数量
			};
			sequence<SignAward> SignAwards;
			
			struct SignItem {
				int date;	//日期
				SignAwards items;
				int alreadyTimes;	//已签到次数
				int vipLevelLimit;	//翻倍需要的vip等级 0无双倍
				int price;	//补签需要的金币
				int quality; //品质
			};
			// 抽奖奖励数据
			struct LotteryView {
				int needGold; // 需要消耗元宝
				IntSeq types; // 奖励类型Id  1、道具 2、装备 3、武将 4、阵法
				int remainTimes;	// 剩余抽奖次数
				StringSeq templateIds; // 模版Id
				IntSeq nums; // 奖励数量
			};
			
			sequence<SignItem> SignItemViewSequence;
			
			struct TotalSignItem {
				int timesLimit;	//需要累计签到的次数
				int status; // 1 已领取  2可领取 3 不可领取
				SignAwards items;
				bool showSpecialEffect;
			};
			sequence<TotalSignItem> TotalSignPackViews;
			
			struct OpenSignView {
				//签到选项
				SignItemViewSequence signItems;
				//累计签到奖励
				TotalSignPackViews allSignPackViews;
				//抽奖, 值为Null时传空数组
				LotteryView theLotteryView;
				// 当前月份
				byte currentMonth; 
				int currentDate;
				// 累积签到次数
				byte signCount;
				bool canResign;
				bool hasMonthCard;//是否有月卡
			};
			
			interface Sign {
				// 签到界面 OpenSignView
				["ami","amd"] string openTheSignView();
				//签到,补签
				["ami"] void signIn(string itemId) throws NoteException,NotEnoughYuanBaoException;
				//一键补签到, return 0, 正常; 1, 非月卡用户,前往购买月卡
				["ami"] int autoResign() throws NoteException, NotEnoughYuanBaoException;
				//领取累计签到奖励 参数是领取奖励对应的次数
				["ami"] void collectGiftPack(int count) throws NoteException;
				//请求抽奖数据
				// LotteryView
				["ami","amd"] string reqLottery() throws NoteException;
				//抽奖
				["ami","amd"] string roulette() throws NotEnoughMoneyException;
				// itemViewSequence
				["ami","amd"] string cdkey(string cdkeyCode) throws NoteException;
				// 输入邀请码
				["ami", "amd"] string inviteCode(string code) throws NoteException;
				// 对上一次抽奖操作发送系统公告(客户端在抽奖动画播放完毕后会请求这个操作,避免动画未播放完毕而公告已经发送)
				["ami"] void broadcastLastLottery();
            };
            
        };
 	};
 };

#endif