#ifndef  _EQUIP_ICE_
#define  _EQUIP_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
		    struct EquipLevelEntity {
		        string id;
		        int level;
		    };

		    sequence<EquipLevelEntity> EquipLevelList;

		    //struct SmeltView {
		    //    int money;
		    //    ItemViewSeq equips;
		    //};
		    struct SmeltParam {
		        string id;
		    };
		    
		    /**神器*/
		    struct ArtifactView{
		    	string dbId;	  //示例ID 
		    	int templateId;	  //模板ID
		    	int level;		  //0级表示未激活	
		    	string useHeroId;
		    };
		    sequence<ArtifactView> ArtifactViewSeq;

			interface Equip{
				
				//强化,返回 EquipView
				["ami"] ItemView levelup(string id) throws NoteException,NotEnoughMoneyException;
				
				//自动强化,返回 EquipView
                ["ami"] ItemView levelupAuto(string id) throws NoteException,NotEnoughMoneyException;

                //一键强化
                ["ami"] EquipLevelList levelupAll(string id) throws NoteException,NotEnoughMoneyException;
				
				/**升星,返回 ItemView的lua，idArray格式：id;id  items格式：id,num;id,num*/
				["ami"] string starUp(string id,string idArray,string items) throws NoteException,NotEnoughMoneyException;
				
				//重铸,返回 EquipView
				["ami"] ItemView rebuild(string id) throws NoteException;
				
				//熔炼
				["ami"] ItemViewSeq smelt(string idArrayStr) throws NoteException, NotEnoughMoneyException;
			
				// 装备开孔
				["ami"] string hole(string equipId, int position) throws NoteException;
			
				// 镶嵌宝石
				["ami"] void setGem(string equipId, int position, string gemId) throws NoteException;
				
				// 取下宝石
				["ami"] void removeGem(string equipId, int position) throws NoteException;
				
				
				// 神器协议
				/**获取所有神器return:ArtifactViewSeq的lua*/
				["ami"] string getAllArtifact() throws NoteException;
				
				/**升级神器*/
				["ami"] void upgradeArtifact(string dbId) throws NoteException;
				
				/**使用神器*/
				["ami"] void useArtifact(string dbId, string heroId) throws NoteException;
			};
		};   
	};
};


#endif