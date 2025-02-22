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
// Generated from file `Haoqingbao.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _HaoqingbaoOperations
{
    void openHaoqingbao_async(AMD_Haoqingbao_openHaoqingbao __cb, Ice.Current __current)
        throws NoteException;

    void sendRedPacket_async(AMD_Haoqingbao_sendRedPacket __cb, int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, Ice.Current __current)
        throws NoteException;

    String recvRedPacket(String packetId, Ice.Current __current)
        throws NoteException;

    void rankList_async(AMD_Haoqingbao_rankList __cb, int type, Ice.Current __current)
        throws NoteException;

    void getRedPacketDetail_async(AMD_Haoqingbao_getRedPacketDetail __cb, String packetId, Ice.Current __current)
        throws NoteException;

    void myRedPacket_async(AMD_Haoqingbao_myRedPacket __cb, Ice.Current __current)
        throws NoteException;

    void checkout_async(AMD_Haoqingbao_checkout __cb, int num, Ice.Current __current)
        throws NoteException;

    void charge(Ice.Current __current)
        throws NoteException;

    String preRecvRedPacket(String packetId, Ice.Current __current)
        throws NoteException;

    void claimRedPacket(String roleId, String packetId, Ice.Current __current)
        throws NoteException;
}
