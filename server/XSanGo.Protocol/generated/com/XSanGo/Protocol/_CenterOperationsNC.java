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
// Generated from file `Center.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _CenterOperationsNC
{
    void setCallback(CenterCallbackPrx cb);

    ServerDetail ping();

    void sendTocken(int id, String account, String tocken, DeviceInfo device)
        throws NoteException;

    void findRoleViewList_async(AMD_Center_findRoleViewList __cb, String accountName);

    void findRoleViewListByRole_async(AMD_Center_findRoleViewListByRole __cb, String roleName);

    void findRoleViewListById_async(AMD_Center_findRoleViewListById __cb, String roleId);

    void getRecentMessages_async(AMD_Center_getRecentMessages __cb);

    void silence_async(AMD_Center_silence __cb, String roleId, String releaseTime);

    void systemAnnounce(String announce);

    void kick(String account, String roleId);

    void charge_async(AMD_Center_charge __cb, String roleId, int yuan, CustomChargeParams params, String orderId, String currency);

    String[] getItemConfig()
        throws NoteException;

    String[] getPropertyConfig()
        throws NoteException;

    IntString[] getPlayerSkillConfig()
        throws NoteException;

    IntString[] getHeroSkillConfig()
        throws NoteException;

    IntString[] getRelationConfig()
        throws NoteException;

    void sendServerMail(String title, String body, Property[] attach, String conditionParams, String senderName)
        throws NoteException;

    void sendMail_async(AMD_Center_sendMail __cb, String targetName, String title, String body, Property[] attach, String senderName)
        throws NoteException;

    void sendMailByRoleId_async(AMD_Center_sendMailByRoleId __cb, String roleId, String title, String body, Property[] attach, String senderName)
        throws NoteException;

    void executeGroovyScript(String script)
        throws NoteException;

    /**
     * 根据CDK查询玩家信息
     * @param __cb The callback object for the operation.
     **/
    void queryRoleByCDK_async(AMD_Center_queryRoleByCDK __cb, String cdk)
        throws NoteException;

    /**
     * 查找公会列表，通过factionName模糊查找
     **/
    GmFactionView[] getFactionList(String factionName)
        throws NoteException;

    /**
     * 获取公会所有成员
     * @param __cb The callback object for the operation.
     **/
    void getFactionMemberList_async(AMD_Center_getFactionMemberList __cb, String factionName)
        throws NoteException;

    /**
     * 获取排行榜 type-0部队 1-公会 2-大神
     * @param __cb The callback object for the operation.
     **/
    void getRankList_async(AMD_Center_getRankList __cb, int type)
        throws NoteException;

    /**
     * 充值记录查询
     * @param __cb The callback object for the operation.
     **/
    void getPayLog_async(AMD_Center_getPayLog __cb, String roleName)
        throws NoteException;

    /**
     * 根据金额获取充值选项
     * @param __cb The callback object for the operation.
     **/
    void getChargeItem_async(AMD_Center_getChargeItem __cb, int yuan);

    /**
     * 查询物品数量
     * @param __cb The callback object for the operation.
     **/
    void queryItemNum_async(AMD_Center_queryItemNum __cb, String roleName, String itemId);

    /**
     * 删除物品
     * @param __cb The callback object for the operation.
     **/
    void deleteItem_async(AMD_Center_deleteItem __cb, String roleId, String itemId, int num);

    /**
     * 获取角色相关数据
     * @param __cb The callback object for the operation.
     **/
    void getRoleDB_async(AMD_Center_getRoleDB __cb, String roleId)
        throws NoteException;

    /**
     * 导入role数据
     * @param __cb The callback object for the operation.
     **/
    void saveRoleData_async(AMD_Center_saveRoleData __cb, int serverId, byte[] data, String roleId)
        throws NoteException;

    /**
     * 根据roleName模糊查询
     * @param __cb The callback object for the operation.
     **/
    void findRoleViewListBySimpleRole_async(AMD_Center_findRoleViewListBySimpleRole __cb, String roleName);

    /**
     * 根据账号模糊查询
     * @param __cb The callback object for the operation.
     **/
    void findRoleViewListBySimpleAccount_async(AMD_Center_findRoleViewListBySimpleAccount __cb, String account);
}
