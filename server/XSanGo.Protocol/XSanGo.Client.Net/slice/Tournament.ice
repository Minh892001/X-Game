#ifndef _TOURNAMENT_ICE_
#define _TOURNAMENT_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"
#include "CrossServer.ice"
#include "ArenaRank.ice"

// 比武大会，客户端接口协议
module com {
	module XSanGo {
		module Protocol {

			// 比武大会主界面
			struct TournamentView{
				int num; // 第几届比武大会
				StageIndex currentStageIndex; // 当前进行到的阶段编号
				string stageDesc; // 当前阶段汉字描述
				int totalDays; // 总的天数
				int lastDays; // 剩余天数
				string nextStageDate; // 下个阶段开始日期
				string nextStageDesc; // 下个阶段描述
				int qtDays; // 资格赛天数
				int s32Days; // 32强阶段总天数
				int s16Days;
				int s8Days;
				int s2Days;
				int s1Days;
				bool canSetupFormation; // 是否在可以更换阵容的时间段, true 可以，false 不可以
				bool qtFinished; // 资格赛是否已结束
				bool isInKnockOut; // 是否进入了淘汰赛
			};

			// 报名界面
			struct SignupView {
				int num; // 第几届比武大会
				string currentStageDesc; // 当前阶段描述
				string startSignupDate; // 报名开始日期
				string startRaceDate; // 比赛开始日期
				int isOpen; // 比武大会是否开启: 0,未开启(未开启状态是指比武大会暂时关闭，开启时间未定, 否则为开启/结束状态); 1,开启; 2, 已结束;
				int hasSignup; // 是否已经报名: 0,没有报名; 1,已经报名;
				int canSignup; // 当前是否可以报名: 0,报名时间未到; 1,可以; 2,报名时间已过
				string showDesc; // 提示信息, 当非正常进入状态时显示提示
				TournamentView mainView; // 如果已经报过名直接返回主界面，客户端直接跳转到主界面, 在未报名的情况下为空
			};
			
			// 预报名返回结果界面
			struct PreSignupView {
				int canSignup; // 0,不可以报名; 1,可以报名
				int lvLimit; // 等级限制
				int lastTime; // 报名剩余时间，秒
			};
			
			// 我的布阵界面
			struct MyFormationView {
				FormationView formation; // 阵法
				PvpOpponentFormationView heros; // 上阵武将
			};

			// 战斗匹配界面
			struct PVPInfoView {
				int lastTime; // 比赛剩余时间
				int buyFightCount; // 购买挑战次数
				int totalFightCount; // 总的可挑战次数
				int lastFightCount; // 剩余可挑战次数
				int buyRefreshCount; // 购买刷新次数
				int totalRefreshCount; // 总的可刷新次数
				int lastRefreshCount; // 剩余可刷新次数
				MyFormationView myFormation; // 我的部队阵容
				int myRank; // 我的排名
				int myScore; // 我的分数
				bool canSetupFormation; // 是否可以设置阵容
				CrossRivalViewSeq opponentViews; // 匹配到的对手
				bool qtFinished; // 资格赛是否结束
				bool isInKnockout; // 是否进入了淘汰赛
				bool hasSignup; // 是否已报名
				int battleTimeCountdown; // 下次可战斗倒计时
			};
			
			// 战报记录界面
			struct FightRecordItemView {
				CrossRoleView roleView; // 对手的View
				int addScore; // 增加积分
				int score; // 积分
				int win; // 战斗结果
				int power; // 战力
				int fightDate; // 战斗时间
				string recordId;
				string movieId; // 战报ID
			};
			sequence<FightRecordItemView> FightRecordItemViewSeq;
			
			// 押注
			struct BetView {
				int id;//对阵表唯一ID
				string betRoleId; // 押注的RoleId，为空的话表示还未押注
				int winornot; // 压的胜(1)还是败(0)
				int result; // 结果: 0,未开结果;1,压中;2,未押中
			};
			sequence<BetView> BetViewSeq;
			
			// 报名结果
			struct SignupResultView {
				int status; // 成功状态：0, 报名失败; 1, 报名成功;
				string tips; // 失败提示
			};
			
			// 战斗双方阵容
			struct FightFormations {
				PvpOpponentFormationView myFormation;
				PvpOpponentFormationView opponentFormation;
				FightMovieByteViewSeq movie;
				int isWin; // 战斗结果，0:失败，1：胜利
    			int remainHero; // 剩余武将数量
    			int winType;
			};
			
			// 比武大会时间状态
			struct TournamentStatusView {
				string currentDesc; // 当前阶段描述
				string nextTime; // 下个阶段开始时间
				string nextDesc; // 下个阶段描述
			};
			
			// 淘汰赛对阵View
			struct KnockoutView {
				CrossScheduleViewSeq scheduleViews; // 对阵
				int fightTime; // 距离战斗开始的时间
			};
			// 竞技场商城商品
			struct TournamentShopItemView {
				int type; // 类型
				string id; // 道具ID
				int price; // 单价
				int limit; // 限购数量
				int buyCount; // 已购数量
				string desc; // 描述信息
				string iconType; // 货币类型
			};
			sequence<TournamentShopItemView> TournamentShopItemViewSeq;
			
			// 竞技场商城
			struct TournamentShopView {
				TournamentShopItemViewSeq items; // 商品列表
				int coint; // 拥有至尊币数量
				int ybcoin; // 拥有的至尊银币数量
			};
			
			// 竞技场商城购买返回值
			struct ShopBuyResultView {
				int buyCount;
				int coint; // 拥有至尊币数量
				int ybcoin; // 拥有至尊银币数量
			};

			// 比武大会
			interface Tournament {
				// 进入比武大会, return SignupView, 点击比武大会请求的第一个接口
				["ami", "amd"] string enterTournament() throws NoteException;

				// 获取比武大会主页面, return TournamentView
				["ami", "amd"] string openTournamentView() throws NoteException;

				// 预报名，查询是否有报名资格, return PreSignupView
				["ami"] string preSignup() throws NoteException;

				// 报名, return SignupResultView
				["ami", "amd"] string signup() throws NoteException;

				// 购买刷新次数, return 剩余次数
				["ami"] int buyRefreshCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

				// 购买挑战次数, return 剩余次数
				["ami"] int buyFightCount() throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 进入对手匹配界面, return PVPInfoView
				["ami", "amd"] string enterPVPView() throws NoteException;
				
				// 刷新对手, return CrossRivalViewSeq
				["ami", "amd"] string refreshPVPView() throws NoteException;
				
				// 进入战斗, return PvpOpponentFormationView
				["ami", "amd"] FightFormations beginFightWith(string opponentId) throws NoteException;
				
				// 结束战斗 power 战力
				["ami"] string endFightWith(string opponentId, int flag, int remainHeroCount, int power) throws NoteException;
			
				// 战斗，后端生成战斗方式的战斗接口，return FightFormations
				["ami", "amd"] FightFormations fightWith(string opponentId) throws NoteException;
			
				// 打开比武大会布阵界面, return MyFormationView
				["ami"] string openSetupFormation() throws NoteException;

				// 布阵
				["ami", "amd"] void setupFormation() throws NoteException;
				
				// 打开战斗记录界面, return FightRecordItemViewSeq
				["ami"] string getFightRecords() throws NoteException;
				
				// 获取排行榜, return CrossRankView
				["ami", "amd"] string getRankList() throws NoteException;
				
				// 获取战报，return TournamentFightMovieView
				["ami", "amd"] string getFightMovieByRecordId(string recordId) throws NoteException;
			
				// 获取淘汰赛对阵图, return KnockoutView
				["ami", "amd"] string getKnockOutView();
				// 获取淘汰赛对阵战报列表 return StringSeq
				["ami", "amd"] string getKnockOutMovieList(int id) throws NoteException;
				// 获取淘汰赛战报, return CrossMovieView
				["ami", "amd"] CrossMovieView getKnockOutMovie(int id, int index) throws NoteException;
				
				// 获取竞猜列表, return BetViewSeq
				["ami", "amd"] string getBetView() throws NoteException;
				
				// 押注, param: stage,第几轮;id,这条对阵对应的ID;roleId,压的某玩家胜利;压的金额. return: 0,押注失败；1,押注成功
				["ami", "amd"] int bet(int stage, int id, string roleId, int num) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 获取比武大会时间状态, return TournamentStatusView
				["ami"] string getTournamentStatus() throws NoteException;
				
				/**获取自己积分和当日胜利次数 return IntIntPair*/
				["ami", "amd"] string getScoreAndWinNum() throws NoteException;
				
				// 获取商城商品列表 return TournamentShopView
				["ami"] string getTournamentShopView() throws NoteException;
				// 购买商城商品 id,商品id, return ShopBuyResultView
				["ami"] string buyShopItem(string id, int num) throws NoteException;
			};
		};
	};
};

#endif