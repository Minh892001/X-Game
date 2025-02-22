// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `Hero.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface HeroPrx extends Ice.ObjectPrx
{
    public void heroStarUp(String heroId)
        throws NotEnoughMoneyException,
               NoteException;

    public void heroStarUp(String heroId, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException;

    public Ice.AsyncResult begin_heroStarUp(String heroId);

    public Ice.AsyncResult begin_heroStarUp(String heroId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_heroStarUp(String heroId, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroStarUp(String heroId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroStarUp(String heroId, Callback_Hero_heroStarUp __cb);

    public Ice.AsyncResult begin_heroStarUp(String heroId, java.util.Map<String, String> __ctx, Callback_Hero_heroStarUp __cb);

    public void end_heroStarUp(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException;

    public boolean heroStarUp_async(AMI_Hero_heroStarUp __cb, String heroId);

    public boolean heroStarUp_async(AMI_Hero_heroStarUp __cb, String heroId, java.util.Map<String, String> __ctx);

    public ItemView[] heroColorUp(String heroId)
        throws NotEnoughMoneyException,
               NoteException;

    public ItemView[] heroColorUp(String heroId, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException;

    public Ice.AsyncResult begin_heroColorUp(String heroId);

    public Ice.AsyncResult begin_heroColorUp(String heroId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_heroColorUp(String heroId, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroColorUp(String heroId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroColorUp(String heroId, Callback_Hero_heroColorUp __cb);

    public Ice.AsyncResult begin_heroColorUp(String heroId, java.util.Map<String, String> __ctx, Callback_Hero_heroColorUp __cb);

    public ItemView[] end_heroColorUp(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException;

    public boolean heroColorUp_async(AMI_Hero_heroColorUp __cb, String heroId);

    public boolean heroColorUp_async(AMI_Hero_heroColorUp __cb, String heroId, java.util.Map<String, String> __ctx);

    public void setHeroEquip(String heroId, String equipId)
        throws NoteException;

    public void setHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId);

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId, Ice.Callback __cb);

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId, Callback_Hero_setHeroEquip __cb);

    public Ice.AsyncResult begin_setHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx, Callback_Hero_setHeroEquip __cb);

    public void end_setHeroEquip(Ice.AsyncResult __result)
        throws NoteException;

    public boolean setHeroEquip_async(AMI_Hero_setHeroEquip __cb, String heroId, String equipId);

    public boolean setHeroEquip_async(AMI_Hero_setHeroEquip __cb, String heroId, String equipId, java.util.Map<String, String> __ctx);

    public void removeHeroEquip(String heroId, String equipId)
        throws NoteException;

    public void removeHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId);

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId, Callback_Hero_removeHeroEquip __cb);

    public Ice.AsyncResult begin_removeHeroEquip(String heroId, String equipId, java.util.Map<String, String> __ctx, Callback_Hero_removeHeroEquip __cb);

    public void end_removeHeroEquip(Ice.AsyncResult __result)
        throws NoteException;

    public boolean removeHeroEquip_async(AMI_Hero_removeHeroEquip __cb, String heroId, String equipId);

    public boolean removeHeroEquip_async(AMI_Hero_removeHeroEquip __cb, String heroId, String equipId, java.util.Map<String, String> __ctx);

    public void setHeroAttendant(String heroId, byte pos, String attendantId)
        throws NoteException;

    public void setHeroAttendant(String heroId, byte pos, String attendantId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId);

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId, Ice.Callback __cb);

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId, Callback_Hero_setHeroAttendant __cb);

    public Ice.AsyncResult begin_setHeroAttendant(String heroId, byte pos, String attendantId, java.util.Map<String, String> __ctx, Callback_Hero_setHeroAttendant __cb);

    public void end_setHeroAttendant(Ice.AsyncResult __result)
        throws NoteException;

    public boolean setHeroAttendant_async(AMI_Hero_setHeroAttendant __cb, String heroId, byte pos, String attendantId);

    public boolean setHeroAttendant_async(AMI_Hero_setHeroAttendant __cb, String heroId, byte pos, String attendantId, java.util.Map<String, String> __ctx);

    public HeroView summonHero(int templateId)
        throws NotEnoughMoneyException,
               NoteException;

    public HeroView summonHero(int templateId, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException;

    public Ice.AsyncResult begin_summonHero(int templateId);

    public Ice.AsyncResult begin_summonHero(int templateId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_summonHero(int templateId, Ice.Callback __cb);

    public Ice.AsyncResult begin_summonHero(int templateId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_summonHero(int templateId, Callback_Hero_summonHero __cb);

    public Ice.AsyncResult begin_summonHero(int templateId, java.util.Map<String, String> __ctx, Callback_Hero_summonHero __cb);

    public HeroView end_summonHero(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException;

    public boolean summonHero_async(AMI_Hero_summonHero __cb, int templateId);

    public boolean summonHero_async(AMI_Hero_summonHero __cb, int templateId, java.util.Map<String, String> __ctx);

    public int getSkillPointInterval();

    public int getSkillPointInterval(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getSkillPointInterval();

    public Ice.AsyncResult begin_getSkillPointInterval(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getSkillPointInterval(Ice.Callback __cb);

    public Ice.AsyncResult begin_getSkillPointInterval(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getSkillPointInterval(Callback_Hero_getSkillPointInterval __cb);

    public Ice.AsyncResult begin_getSkillPointInterval(java.util.Map<String, String> __ctx, Callback_Hero_getSkillPointInterval __cb);

    public int end_getSkillPointInterval(Ice.AsyncResult __result);

    public boolean getSkillPointInterval_async(AMI_Hero_getSkillPointInterval __cb);

    public boolean getSkillPointInterval_async(AMI_Hero_getSkillPointInterval __cb, java.util.Map<String, String> __ctx);

    public HeroSkillPointView getHeroSkillView();

    public HeroSkillPointView getHeroSkillView(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getHeroSkillView();

    public Ice.AsyncResult begin_getHeroSkillView(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getHeroSkillView(Ice.Callback __cb);

    public Ice.AsyncResult begin_getHeroSkillView(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getHeroSkillView(Callback_Hero_getHeroSkillView __cb);

    public Ice.AsyncResult begin_getHeroSkillView(java.util.Map<String, String> __ctx, Callback_Hero_getHeroSkillView __cb);

    public HeroSkillPointView end_getHeroSkillView(Ice.AsyncResult __result);

    public boolean getHeroSkillView_async(AMI_Hero_getHeroSkillView __cb);

    public boolean getHeroSkillView_async(AMI_Hero_getHeroSkillView __cb, java.util.Map<String, String> __ctx);

    public void buyHeroSkillPoint()
        throws NotEnoughYuanBaoException,
               NoteException;

    public void buyHeroSkillPoint(java.util.Map<String, String> __ctx)
        throws NotEnoughYuanBaoException,
               NoteException;

    public Ice.AsyncResult begin_buyHeroSkillPoint();

    public Ice.AsyncResult begin_buyHeroSkillPoint(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_buyHeroSkillPoint(Ice.Callback __cb);

    public Ice.AsyncResult begin_buyHeroSkillPoint(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_buyHeroSkillPoint(Callback_Hero_buyHeroSkillPoint __cb);

    public Ice.AsyncResult begin_buyHeroSkillPoint(java.util.Map<String, String> __ctx, Callback_Hero_buyHeroSkillPoint __cb);

    public void end_buyHeroSkillPoint(Ice.AsyncResult __result)
        throws NotEnoughYuanBaoException,
               NoteException;

    public boolean buyHeroSkillPoint_async(AMI_Hero_buyHeroSkillPoint __cb);

    public boolean buyHeroSkillPoint_async(AMI_Hero_buyHeroSkillPoint __cb, java.util.Map<String, String> __ctx);

    public void heroSkillLevelUp(String heroId, int skillId, int upLevel)
        throws NotEnoughMoneyException,
               NoteException;

    public void heroSkillLevelUp(String heroId, int skillId, int upLevel, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException;

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel);

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel, Callback_Hero_heroSkillLevelUp __cb);

    public Ice.AsyncResult begin_heroSkillLevelUp(String heroId, int skillId, int upLevel, java.util.Map<String, String> __ctx, Callback_Hero_heroSkillLevelUp __cb);

    public void end_heroSkillLevelUp(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException;

    public boolean heroSkillLevelUp_async(AMI_Hero_heroSkillLevelUp __cb, String heroId, int skillId, int upLevel);

    public boolean heroSkillLevelUp_async(AMI_Hero_heroSkillLevelUp __cb, String heroId, int skillId, int upLevel, java.util.Map<String, String> __ctx);

    public void activeHeroRelation(String heroId, int orignalRelationId, int level)
        throws NotEnoughMoneyException,
               NoteException;

    public void activeHeroRelation(String heroId, int orignalRelationId, int level, java.util.Map<String, String> __ctx)
        throws NotEnoughMoneyException,
               NoteException;

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level);

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level, Ice.Callback __cb);

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level, Callback_Hero_activeHeroRelation __cb);

    public Ice.AsyncResult begin_activeHeroRelation(String heroId, int orignalRelationId, int level, java.util.Map<String, String> __ctx, Callback_Hero_activeHeroRelation __cb);

    public void end_activeHeroRelation(Ice.AsyncResult __result)
        throws NotEnoughMoneyException,
               NoteException;

    public boolean activeHeroRelation_async(AMI_Hero_activeHeroRelation __cb, String heroId, int orignalRelationId, int level);

    public boolean activeHeroRelation_async(AMI_Hero_activeHeroRelation __cb, String heroId, int orignalRelationId, int level, java.util.Map<String, String> __ctx);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     **/
    public String getHeroPracticeList(String heroId)
        throws NoteException;

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     **/
    public String getHeroPracticeList(String heroId, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId, java.util.Map<String, String> __ctx);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId, Ice.Callback __cb);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId, Callback_Hero_getHeroPracticeList __cb);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getHeroPracticeList(String heroId, java.util.Map<String, String> __ctx, Callback_Hero_getHeroPracticeList __cb);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __result The asynchronous result object.
     **/
    public String end_getHeroPracticeList(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __cb The callback object for the operation.
     **/
    public boolean getHeroPracticeList_async(AMI_Hero_getHeroPracticeList __cb, String heroId);

    /**
     * 获取武将修炼数据，返回HeroPracticeViewSeq的lua
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean getHeroPracticeList_async(AMI_Hero_getHeroPracticeList __cb, String heroId, java.util.Map<String, String> __ctx);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     **/
    public String resetPractice(String heroId, int id)
        throws NoteException;

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     **/
    public String resetPractice(String heroId, int id, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id, java.util.Map<String, String> __ctx);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id, Ice.Callback __cb);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id, Callback_Hero_resetPractice __cb);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPractice(String heroId, int id, java.util.Map<String, String> __ctx, Callback_Hero_resetPractice __cb);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __result The asynchronous result object.
     **/
    public String end_resetPractice(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __cb The callback object for the operation.
     **/
    public boolean resetPractice_async(AMI_Hero_resetPractice __cb, String heroId, int id);

    /**
     * 重置修炼属性，返回HeroPracticeViewSeq的lua
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean resetPractice_async(AMI_Hero_resetPractice __cb, String heroId, int id, java.util.Map<String, String> __ctx);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     **/
    public void practice(String heroId, int id, String itemIds)
        throws NoteException;

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __ctx The Context map to send with the invocation.
     **/
    public void practice(String heroId, int id, String itemIds, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds, java.util.Map<String, String> __ctx);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds, Ice.Callback __cb);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds, Callback_Hero_practice __cb);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_practice(String heroId, int id, String itemIds, java.util.Map<String, String> __ctx, Callback_Hero_practice __cb);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __result The asynchronous result object.
     **/
    public void end_practice(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __cb The callback object for the operation.
     **/
    public boolean practice_async(AMI_Hero_practice __cb, String heroId, int id, String itemIds);

    /**
     * 修炼，itemIds：逗号分隔的将魂ID
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean practice_async(AMI_Hero_practice __cb, String heroId, int id, String itemIds, java.util.Map<String, String> __ctx);

    /**
     * 重置随从  当前武将id、随从位置
     **/
    public AttendantView resetAttendant(String heroId, byte pos)
        throws NoteException;

    /**
     * 重置随从  当前武将id、随从位置
     * @param __ctx The Context map to send with the invocation.
     **/
    public AttendantView resetAttendant(String heroId, byte pos, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 重置随从  当前武将id、随从位置
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos, java.util.Map<String, String> __ctx);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos, Ice.Callback __cb);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos, Callback_Hero_resetAttendant __cb);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetAttendant(String heroId, byte pos, java.util.Map<String, String> __ctx, Callback_Hero_resetAttendant __cb);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __result The asynchronous result object.
     **/
    public AttendantView end_resetAttendant(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 重置随从  当前武将id、随从位置
     * @param __cb The callback object for the operation.
     **/
    public boolean resetAttendant_async(AMI_Hero_resetAttendant __cb, String heroId, byte pos);

    /**
     * 重置随从  当前武将id、随从位置
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean resetAttendant_async(AMI_Hero_resetAttendant __cb, String heroId, byte pos, java.util.Map<String, String> __ctx);
}
