//时空战役
#ifndef  _TIMEBATTLE_ICE_
#define  _TIMEBATTLE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			//时空战役挑战返回结果
			struct BattleChallengeResultView{
				PropertySeq heroExps;		//武将获得经验，KEY为武将唯一标识，VALUE为获得经验值
				ItemViewSeq items;			//掉落物品，包括主公经验金币等
				SceneDuelViewSeq reports;   //战报
				string movieId; // 战报ID
			};
			
			struct BattleTimesView{
				int id;		//关卡ID
				int num;	//可挑战次数
				int time;   //cd倒计时秒 0表示可立即挑战
				int star;	//星级
			};
			
			interface TimeBattle{
				/**获取所有时空战役可挑战的次数BattleTimesView[]的lua*/
				["ami"] string getChallengeTimes() throws NoteException;
				
				/**挑战副本 返回掉落物品*/
				["ami"] BattleChallengeResultView beginChallenge(string formationId,int id) throws NoteException;
				
				/**挑战过关斩将*/
				//["ami"] BattleChallengeResultView beginSingled(string heroId,int id) throws NoteException;
				
				/**打赢或者战败时调用 heroNum-剩余武将数量，返回星级*/
				["ami"] int endChallenge(int heroNum) throws NoteException;
				
				// 挑战失败
				["ami"] void failChallenge() throws NoteException;
				
				/**夷陵之战结束协议 items格式：模版id,数量;模版id,数量*/
				["ami"] int endLimitChallenge(int heroNum,string items) throws NoteException;
				
				/**扫荡，可无限出怪的关卡不可扫荡。返回CopyClearResultView的lua*/
				["ami"] string clear(int id) throws NoteException;
			};
		};
	};
};

#endif