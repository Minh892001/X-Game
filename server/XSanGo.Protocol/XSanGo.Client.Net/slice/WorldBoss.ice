#ifndef  _WORLDBOSS_ICE_
#define  _WORLDBOSS_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
			
			/**Boss状态 0-可挑战1-死亡2-未开启*/
			enum WorldBossState{
				CanChallenge,Death,Away
			};
			
			/**尾刀奖励*/
			struct WorldBossTailAward{
				int hp;
				string roleId;//尾刀所属者
				bool isReceive;//是否领取
				ItemViewSeq items;
			};
			sequence<WorldBossTailAward> WorldBossTailAwardSeq;
			
			/**世界BOSS主View*/
			struct WorldBossView {
				int id;//bossID
				int bossLevel;
				WorldBossState state;//boss状态
				int awaySecond;//逃跑剩余秒
				long blood;//boss剩余血量
				long sumBlood;//boss总血量
				int cd;
				int addType;//加成类型
				int addValue;//加成值
				long harm;//总伤害
				string customsName;//关卡名称
				int scene;// 场景资源
				int position;//怪物位置
				ItemViewSeq joinItems;//参与奖
				WorldBossTailAwardSeq tailAward;//尾刀奖励
				bool isTrust;//是否已托管
				int trustYuanbao;//托管需要元宝
				ItemViewSeq trustItems;//托管获得物品
				int returnYuanbao;//取消托管返还元宝
				int canTrustNum;//可托管次数
			};
			
			/**世界BOSS排行榜*/
			struct WorldBossRank{
				string roleId;	//角色ID
				string roleName;//角色名称
				string icon;	//头像
				int level;		//等级
				int vipLevel;	//VIP等级
				long count;		//伤害或者上榜次数
				long maxHarm;    //上榜次数排行榜时可用
			};
			sequence<WorldBossRank> WorldBossRankSeq;
			
			struct WorldBossRankView{
				WorldBossRankSeq rank;
				long myCount;//我的伤害或者上榜次数
				int myRank;//我的排名
			};
			
			/**世界BOSS挑战View*/
			struct WorldBossChallengeView{
				int id;//bossID
				long blood;//boss剩余血量
				long sumBlood;//boss总血量
				int addType;//加成类型
				int addValue;//加成值
				ItemViewSeq items;//掉落物品
				string movieId;// 战报ID
			};
			
			interface WorldBoss {
				/**返回worldBossView的lua*/
				["ami"] string getWorldBossView() throws NoteException;
				
				/**获取伤害排名，返回WorldBossRankView的lua*/
				["ami","amd"] string getHarmRank() throws NoteException;
				
				/**获取伤害上榜排名，返回WorldBossRankView的lua*/
				["ami","amd"] string getCountRank() throws NoteException;
				
				/**购买鼓舞*/
				["ami"] void buyInspire() throws NoteException;
				
				/**清除CD*/
				["ami"] void clearCd() throws NoteException;
				
				/**开始挑战，返回WorldBossChallengeView的lua*/
				["ami"] string beginChallenge() throws NoteException;
				
				/**结束挑战传入真实伤害，返回false表示挑战无效 */
				["ami"] bool endChallenge(int harm,int heroNum) throws NoteException;
				
				/**领取尾刀奖励*/
				["ami"] void getTailAward(int hp) throws NoteException;
				
				/**托管*/
				["ami"] void trust() throws NoteException;
				
				/**取消托管*/
				["ami"] void cancelTrust() throws NoteException;
			};
				
		};   
	};
};

#endif