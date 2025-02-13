#ifndef  _HEROADMIRE_ICE_
#define  _HEROADMIRE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//武将觉醒
module com {
	module XSanGo {
		module Protocol {
			// 洗练属性结构表
			struct BaptizeProp {
				string			code;			// 属性代码
				int				value;			// 当前属性值
				int				maxValue;		// 最大属性值
			};
			sequence<BaptizeProp> BaptizePropSeq;
			
			// 武将洗炼数据
			struct HeroBaptizeData {
				int 			lvl;			// 洗炼等级
				BaptizePropSeq 	props;			// 洗炼属性（当前值）
			};
			
			// 洗炼结果数据
			struct HeroBaptizeResult {
				int 			times;			// 实际成功次数
				HeroBaptizeData result;			// 洗炼属性
			};
			
			// 洗炼重置结果数据
			struct HeroBaptizeResetResult {
				HeroBaptizeData result;			// 洗炼属性
				IntStringSeq	items;			// 返还道具结果
			};
			
			interface HeroAwaken { 
				// 武将觉醒
				["ami"] int awakenHero(string heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
			
				// 显示武将洗炼页面 
				// ["ami"] HeroBaptizeData heroBaptizeShow(string heroId) throws NoteException;
				["ami"] string heroBaptizeShow(string heroId) throws NoteException;
				
				// 洗炼
				//["ami"] HeroBaptizeResult heroBaptize(string heroId, bool isTenTimes) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				["ami"] string heroBaptize(string heroId, bool isTenTimes) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 洗练等级升级
				//["ami"] HeroBaptizeData baptizeUpgrade(string heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				["ami"] string baptizeUpgrade(string heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
				
				// 洗炼重置
				// ["ami"] HeroBaptizeResetResult baptizeReset(string heroId) throws NoteException;
				["ami"] string baptizeReset(string heroId) throws NoteException;
			};
		};   
	};
};

#endif
