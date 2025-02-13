#ifndef  _FORMATION_ICE_
#define  _FORMATION_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{			
			interface FormationCallback{
				//部队变更通知，新增部队也使用此通知
				["ami"] void formationChange(FormationView view);
			};
			
			interface Formation{
				//获取阵法配置信息
				["ami"] FormationViewSeq getFormationList();
				
				//设置阵法书
				["ami"] void setFormationBuff(string formationId, string bookId);
				
				//设置武将位置，注意，当两名武将位置互换时，应调用两次本方法
				["ami"] void setFormationPosition(string formationId, byte postion, string heroId) throws NoteException;
				
				//设置主公技
				["ami"] void setFormationSkill(string formationId, byte pos,int skillId);
				
				//清空部队，但至少会剩一个
				["ami"] void clearFormation(string formationId) throws NoteException;
			};
		};
	};
};	  

#endif