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
// Generated from file `Role.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _RoleOperations
{
    String setRoleCallback(RoleCallbackPrx cb, Ice.Current __current)
        throws NoteException;

    String[] getServerOpenTime(Ice.Current __current);

    void getRoleViewList_async(AMD_Role_getRoleViewList __cb, Ice.Current __current)
        throws NoteException;

    void randomName_async(AMD_Role_randomName __cb, int sex, Ice.Current __current);

    void setSexAndName_async(AMD_Role_setSexAndName __cb, int sex, String name, String inviteCode, Ice.Current __current)
        throws NoteException;

    void rename_async(AMD_Role_rename __cb, String name, Ice.Current __current)
        throws NotEnoughYuanBaoException,
               NoteException;

    void levelUp(Ice.Current __current)
        throws NoteException;

    boolean readActivityAnnounce(int id, Ice.Current __current);

    void salary(Ice.Current __current)
        throws NoteException;

    void getOtherPlayInfo_async(AMD_Role_getOtherPlayInfo __cb, String targetId, Ice.Current __current)
        throws NoteException;

    void setHeadImage(String img, Ice.Current __current)
        throws NoteException;

    void setHeadBorder(String border, Ice.Current __current)
        throws NoteException;

    String getReportView(String reportId, Ice.Current __current)
        throws NoteException;

    void completeGuide(int guideId, Ice.Current __current);

    SceneDuelView[] openCeremony(int id, Ice.Current __current)
        throws NoteException;

    DuelSkillTemplateView[] getDuelStrategyConfig(Ice.Current __current)
        throws NoteException;

    void xsgPing(Ice.Current __current)
        throws NoteException;

    void resetRole_async(AMD_Role_resetRole __cb, Ice.Current __current)
        throws NoteException;

    ActivityAnnounceView[] getActivityAnnounce(Ice.Current __current);

    /**
     * 获取双倍卡剩余秒数,返回IntIntPairSeq的lua,0-经验 1-掉落
     * @param __current The Current object for the invocation.
     **/
    String getDoubleCardTime(Ice.Current __current)
        throws NoteException;

    void shareWeixin(Ice.Current __current)
        throws NoteException;

    void getRoleHeros_async(AMD_Role_getRoleHeros __cb, String roleId, Ice.Current __current)
        throws NoteException;
}
