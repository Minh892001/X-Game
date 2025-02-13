#ifndef  _HERO_ICE_
#define  _HERO_ICE_

#include "Common.ice"
#include "ExceptionDef.ice"

module com{
	module XSanGo{
		module Protocol{
			struct HeroSkillPointView{
				int heroSkillPoint;			//武将技能点
				int skillPointRemainSecond;	//技能点恢复倒计时，如果已满则为-1
				int buyTime;				//购买次数
			};
			
			// 武将升级, 进阶, 技能点的消耗
			struct HeroConsumeView {
				PropertySeq consumes;
			};

			struct HeroPracticeView{
				int id;
				string propName;//属性名
				int color;//品质颜色 1-绿2-蓝3-紫
				int level;//当前等级
				int addValue;//增加的属性值
				int exp;//当前经验值
				int nextUpExp;//下次升级需要的经验
			};
			sequence<HeroPracticeView> HeroPracticeViewSeq;
			
			interface Hero{
				//武将升星
				["ami"] void heroStarUp(string heroId) throws NoteException,NotEnoughMoneyException;
				//武将进阶
				["ami"] ItemViewSeq heroColorUp(string heroId) throws NoteException,NotEnoughMoneyException;
				
				//给武将穿装备
				["ami"] void setHeroEquip(string heroId,string equipId) throws NoteException;
				// 卸掉武将装备
				["ami"] void removeHeroEquip(string heroId, string equipId) throws NoteException;
				
				//设置武将随从
				["ami"] void setHeroAttendant(string heroId,byte pos,string attendantId) throws NoteException;
				//召唤武将
				["ami"] HeroView summonHero(int templateId) throws NoteException,NotEnoughMoneyException;
				
				//武将技能点恢复时间间隔，单位秒
				["ami"] int getSkillPointInterval();
				//武将技能数据
				["ami"] HeroSkillPointView getHeroSkillView();
				//购买武将技能点
				["ami"] void buyHeroSkillPoint() throws NotEnoughYuanBaoException,NoteException;

				//武将技能升级
				["ami"] void heroSkillLevelUp(string heroId,int skillId,int upLevel) throws NoteException,NotEnoughMoneyException;
				
				//武将缘分激活，注意，这里传的是该缘分原始模板ID
				["ami"] void activeHeroRelation(string heroId,int orignalRelationId,int level) throws NoteException,NotEnoughMoneyException;
				
				/**
				 * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
				 */
				["ami"] string getHeroPracticeList(string heroId) throws NoteException;
				
				/**
				 * 重置修炼属性，返回HeroPracticeViewSeq的lua
				 */
				["ami"] string resetPractice(string heroId,int id) throws NoteException;
				
				/**
				 * 修炼，itemIds：逗号分隔的将魂ID
				 */
				["ami"] void practice(string heroId,int id,string itemIds) throws NoteException;
				/**重置随从  当前武将id、随从位置*/
				["ami"] AttendantView resetAttendant(string heroId,byte pos) throws NoteException;

			};			
		};
	};
};

#endif