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

public interface _HaoqingbaoOperationsNC
{
    void openHaoqingbao_async(AMD_Haoqingbao_openHaoqingbao __cb)
        throws NoteException;

    void sendRedPacket_async(AMD_Haoqingbao_sendRedPacket __cb, int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg)
        throws NoteException;

    String recvRedPacket(String packetId)
        throws NoteException;

    void rankList_async(AMD_Haoqingbao_rankList __cb, int type)
        throws NoteException;

    void getRedPacketDetail_async(AMD_Haoqingbao_getRedPacketDetail __cb, String packetId)
        throws NoteException;

    void myRedPacket_async(AMD_Haoqingbao_myRedPacket __cb)
        throws NoteException;

    void checkout_async(AMD_Haoqingbao_checkout __cb, int num)
        throws NoteException;

    void charge()
        throws NoteException;

    String preRecvRedPacket(String packetId)
        throws NoteException;

    void claimRedPacket(String roleId, String packetId)
        throws NoteException;
}
