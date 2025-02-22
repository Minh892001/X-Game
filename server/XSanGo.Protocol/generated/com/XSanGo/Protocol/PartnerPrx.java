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
// Generated from file `partner.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface PartnerPrx extends Ice.ObjectPrx
{
    /**
     * 获取伙伴列表
     **/
    public String getPartnerViewList()
        throws NoteException;

    /**
     * 获取伙伴列表
     * @param __ctx The Context map to send with the invocation.
     **/
    public String getPartnerViewList(java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 获取伙伴列表
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList();

    /**
     * 获取伙伴列表
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList(java.util.Map<String, String> __ctx);

    /**
     * 获取伙伴列表
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList(Ice.Callback __cb);

    /**
     * 获取伙伴列表
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 获取伙伴列表
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList(Callback_Partner_getPartnerViewList __cb);

    /**
     * 获取伙伴列表
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getPartnerViewList(java.util.Map<String, String> __ctx, Callback_Partner_getPartnerViewList __cb);

    /**
     * 获取伙伴列表
     * @param __result The asynchronous result object.
     **/
    public String end_getPartnerViewList(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 获取伙伴列表
     * @param __cb The callback object for the operation.
     **/
    public boolean getPartnerViewList_async(AMI_Partner_getPartnerViewList __cb);

    /**
     * 获取伙伴列表
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean getPartnerViewList_async(AMI_Partner_getPartnerViewList __cb, java.util.Map<String, String> __ctx);

    /**
     * 设置伙伴武将
     **/
    public void setHeroPosition(byte postion, String heroId, String oldHeroId)
        throws NoteException;

    /**
     * 设置伙伴武将
     * @param __ctx The Context map to send with the invocation.
     **/
    public void setHeroPosition(byte postion, String heroId, String oldHeroId, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 设置伙伴武将
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId);

    /**
     * 设置伙伴武将
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId, java.util.Map<String, String> __ctx);

    /**
     * 设置伙伴武将
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId, Ice.Callback __cb);

    /**
     * 设置伙伴武将
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 设置伙伴武将
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId, Callback_Partner_setHeroPosition __cb);

    /**
     * 设置伙伴武将
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_setHeroPosition(byte postion, String heroId, String oldHeroId, java.util.Map<String, String> __ctx, Callback_Partner_setHeroPosition __cb);

    /**
     * 设置伙伴武将
     * @param __result The asynchronous result object.
     **/
    public void end_setHeroPosition(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 设置伙伴武将
     * @param __cb The callback object for the operation.
     **/
    public boolean setHeroPosition_async(AMI_Partner_setHeroPosition __cb, byte postion, String heroId, String oldHeroId);

    /**
     * 设置伙伴武将
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean setHeroPosition_async(AMI_Partner_setHeroPosition __cb, byte postion, String heroId, String oldHeroId, java.util.Map<String, String> __ctx);

    /**
     * 开启伙伴阵位
     **/
    public String openPartnerShipPos(byte postion)
        throws NoteException;

    /**
     * 开启伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     **/
    public String openPartnerShipPos(byte postion, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 开启伙伴阵位
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion);

    /**
     * 开启伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion, java.util.Map<String, String> __ctx);

    /**
     * 开启伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion, Ice.Callback __cb);

    /**
     * 开启伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 开启伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion, Callback_Partner_openPartnerShipPos __cb);

    /**
     * 开启伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_openPartnerShipPos(byte postion, java.util.Map<String, String> __ctx, Callback_Partner_openPartnerShipPos __cb);

    /**
     * 开启伙伴阵位
     * @param __result The asynchronous result object.
     **/
    public String end_openPartnerShipPos(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 开启伙伴阵位
     * @param __cb The callback object for the operation.
     **/
    public boolean openPartnerShipPos_async(AMI_Partner_openPartnerShipPos __cb, byte postion);

    /**
     * 开启伙伴阵位
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean openPartnerShipPos_async(AMI_Partner_openPartnerShipPos __cb, byte postion, java.util.Map<String, String> __ctx);

    /**
     * 重置伙伴阵位
     **/
    public String resetPartnerPos(byte postion, int cost, int isLock)
        throws NoteException;

    /**
     * 重置伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     **/
    public String resetPartnerPos(byte postion, int cost, int isLock, java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 重置伙伴阵位
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock);

    /**
     * 重置伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock, java.util.Map<String, String> __ctx);

    /**
     * 重置伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock, Ice.Callback __cb);

    /**
     * 重置伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 重置伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock, Callback_Partner_resetPartnerPos __cb);

    /**
     * 重置伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_resetPartnerPos(byte postion, int cost, int isLock, java.util.Map<String, String> __ctx, Callback_Partner_resetPartnerPos __cb);

    /**
     * 重置伙伴阵位
     * @param __result The asynchronous result object.
     **/
    public String end_resetPartnerPos(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 重置伙伴阵位
     * @param __cb The callback object for the operation.
     **/
    public boolean resetPartnerPos_async(AMI_Partner_resetPartnerPos __cb, byte postion, int cost, int isLock);

    /**
     * 重置伙伴阵位
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean resetPartnerPos_async(AMI_Partner_resetPartnerPos __cb, byte postion, int cost, int isLock, java.util.Map<String, String> __ctx);

    /**
     * 清空所有伙伴阵位
     **/
    public void clearAll()
        throws NoteException;

    /**
     * 清空所有伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     **/
    public void clearAll(java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 清空所有伙伴阵位
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll();

    /**
     * 清空所有伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll(java.util.Map<String, String> __ctx);

    /**
     * 清空所有伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll(Ice.Callback __cb);

    /**
     * 清空所有伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 清空所有伙伴阵位
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll(Callback_Partner_clearAll __cb);

    /**
     * 清空所有伙伴阵位
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_clearAll(java.util.Map<String, String> __ctx, Callback_Partner_clearAll __cb);

    /**
     * 清空所有伙伴阵位
     * @param __result The asynchronous result object.
     **/
    public void end_clearAll(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 清空所有伙伴阵位
     * @param __cb The callback object for the operation.
     **/
    public boolean clearAll_async(AMI_Partner_clearAll __cb);

    /**
     * 清空所有伙伴阵位
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean clearAll_async(AMI_Partner_clearAll __cb, java.util.Map<String, String> __ctx);

    /**
     * 获取伙伴开启等级条件
     **/
    public String getLevelRequired()
        throws NoteException;

    /**
     * 获取伙伴开启等级条件
     * @param __ctx The Context map to send with the invocation.
     **/
    public String getLevelRequired(java.util.Map<String, String> __ctx)
        throws NoteException;

    /**
     * 获取伙伴开启等级条件
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired();

    /**
     * 获取伙伴开启等级条件
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired(java.util.Map<String, String> __ctx);

    /**
     * 获取伙伴开启等级条件
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired(Ice.Callback __cb);

    /**
     * 获取伙伴开启等级条件
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * 获取伙伴开启等级条件
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired(Callback_Partner_getLevelRequired __cb);

    /**
     * 获取伙伴开启等级条件
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getLevelRequired(java.util.Map<String, String> __ctx, Callback_Partner_getLevelRequired __cb);

    /**
     * 获取伙伴开启等级条件
     * @param __result The asynchronous result object.
     **/
    public String end_getLevelRequired(Ice.AsyncResult __result)
        throws NoteException;

    /**
     * 获取伙伴开启等级条件
     * @param __cb The callback object for the operation.
     **/
    public boolean getLevelRequired_async(AMI_Partner_getLevelRequired __cb);

    /**
     * 获取伙伴开启等级条件
     * @param __cb The callback object for the operation.
     * @param __ctx The Context map to send with the invocation.
     **/
    public boolean getLevelRequired_async(AMI_Partner_getLevelRequired __cb, java.util.Map<String, String> __ctx);
}
