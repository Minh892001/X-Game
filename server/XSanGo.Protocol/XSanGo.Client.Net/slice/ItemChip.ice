#ifndef  _ITEMCHIP_ICE_
#define  _ITEMCHIP_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"
#include "Hero.ice"

module com{
	module XSanGo{
		module Protocol{ 
			
			//抢夺结果
			enum ChipResult {
				failed,	//失败
				noChip,	//没有碎片
				noNum,	//没有数量
				sucessess	//成功
			};
			
			//掠夺 和 复仇 返回属性
			struct RobResult {
				ChipResult chipRs;	//抢夺结果 碎片结果
				int fightStar;		//战斗结果的星级
				int money;			//金币
				int exp;			//经验
			};
			
			//可以掠夺 属性描述类
			struct RobRole {
				string id;
				string name;
				short level;
				string icon;
				int sex;
				int relation;						//表示是否是仇人关系，暂定，无效值
				int vip;
				int compositeCombat;				//综合战力
				int successRate;					//成功率 1：低 2：中 3：高
				FormationSummaryViewSeq guardHeroArr;//防守部队 武将信息
			};
			sequence<RobRole> RobRoleViewSeq;	
			
			//可以掠夺  列表
			struct RobRoleShow {
				int vit;	//当前行动力
				int vitNum; //当前行动力购买次数
				RobRoleViewSeq RobRoleList; //掠夺角色列表
			};
			
			//战报 属性描述类
			struct ReportView { 
				string id;
				string name;
				short level;
				string icon;
				int sex;
				int vip;
				int flag;				//胜败 0：输，1：赢
				int compositeCombat;	//综合战力
				string fightId;			//战报ID
				long reportTime;		//战报发生时间
				string chipId;			//碎片ID
				int chipNum;			//碎片数量
				string reportId;		//战报ID
			};
			sequence<ReportView> reportViewSeq;
			
			//道具碎片的属性
			struct ChipItemCompound {
				string chipItemId;	//碎片ID
				int num;			//数量
			};
			sequence<ChipItemCompound> chipItemCompoundSeq;
			
			//合成后 返回属性
			struct CompoundResult {
				string itemId;					//抢夺结果 碎片结果
				chipItemCompoundSeq chipList; 	//碎片属性
			};
			
			// 武将下野操作返回结果
			struct HeroResetResult {
				int released; // 0, 已释放; 1, 未释放
			};
			
			// 武将下野请求返回view
			struct HeroResetView {
				string heroTemplateId;
				HeroConsumeView consume;
			};
			
			// 武将传承请求返回View
			struct HeroInheritView {
				string consumeTemplateId; // 传承令模版id
				int consumeNum; // 消耗的传承令数量
				int maxLevel; // 可传承的最高等级
			};
			
			interface ItemChip {
				
				//碎片合成 返回：合成后的实例ID
				//["ami"] CompoundResult compoundChip(string itemId) throws NoteException;
				["ami"] string compoundChipWithExtraId(string itemId, string extraId) throws NoteException;
				["ami"] string compoundChip(string itemId) throws NoteException;
				["ami"] string compoundGem(string itemId, int num) throws NoteException;
				
				
				//炫耀装备 itemId：合成后的实例ID
				["ami"] void strutItem(string itemId) throws NoteException;
				
				// 请求武将下野界面 return HeroResetView[]
				["ami"] string requestHeroReset() throws NoteException;
				
				//武将下野, isPay: 0,不付费;1,付费, return HeroResetResult
				["ami"] string heroReset(string heroId, int isPay) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;

				// 请求武将传承界面, 返回HeroInheritView
				["ami"] string requestHeroInherit() throws NoteException;
				// 武将传承
				["ami"] void heroInherit(string inheritHeroId, string baseHeroId) throws NoteException;
			};
		};   
	};
};

#endif
