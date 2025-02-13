#ifndef  _HEROADMIRE_ICE_
#define  _HEROADMIRE_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

//名将仰慕
module com {
	module XSanGo {
		module Protocol {
			
			//显示名将仰慕的数据
			struct AdmireView {
				int heroId;				//武将ID
				int value;				//武将仰慕值
				string heroList; 		//武将ID列表,格式：武将ID，武将ID，……
				string itemList; 		//道具ID列表,JSON格式：道具ID：是否使用了，道具ID：是否使用了……
			};
			 
			interface HeroAdmire { 
				//显示名将仰慕界面
				//["ami"] AdmireView selectAdmireShow() throws NoteException;
				["ami"] string selectAdmireShow() throws NoteException;
				
				//选择名将
				["ami"] void chooseHero(int heroId) throws NoteException;
				
				//更换名将
				["ami"] void exchangeHero(int heroId) throws NoteException;
				
				//清空名将
				["ami"] void clearHero() throws NoteException;
				
				//仰慕 名将，返回仰慕之后，名将的仰慕值 
				["ami"] int presentHero(int id) throws NoteException;
				
				//召唤名将
				["ami"] int summonHero() throws NoteException;
			};
		};   
	};
};

#endif
