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

public interface HaoqingbaoPrx extends Ice.ObjectPrx
{
    public String openHaoqingbao()
        throws NoteException;

    public String openHaoqingbao(java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_openHaoqingbao();

    public Ice.AsyncResult begin_openHaoqingbao(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_openHaoqingbao(Ice.Callback __cb);

    public Ice.AsyncResult begin_openHaoqingbao(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_openHaoqingbao(Callback_Haoqingbao_openHaoqingbao __cb);

    public Ice.AsyncResult begin_openHaoqingbao(java.util.Map<String, String> __ctx, Callback_Haoqingbao_openHaoqingbao __cb);

    public String end_openHaoqingbao(Ice.AsyncResult __result)
        throws NoteException;

    public boolean openHaoqingbao_async(AMI_Haoqingbao_openHaoqingbao __cb);

    public boolean openHaoqingbao_async(AMI_Haoqingbao_openHaoqingbao __cb, java.util.Map<String, String> __ctx);

    public String sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg)
        throws NoteException;

    public String sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg);

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, Callback_Haoqingbao_sendRedPacket __cb);

    public Ice.AsyncResult begin_sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, java.util.Map<String, String> __ctx, Callback_Haoqingbao_sendRedPacket __cb);

    public String end_sendRedPacket(Ice.AsyncResult __result)
        throws NoteException;

    public boolean sendRedPacket_async(AMI_Haoqingbao_sendRedPacket __cb, int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg);

    public boolean sendRedPacket_async(AMI_Haoqingbao_sendRedPacket __cb, int type, int minLevel, int minVipLevel, int range, int minFriendPoint, int totalYuanbaoNum, int packetNum, String msg, java.util.Map<String, String> __ctx);

    public String recvRedPacket(String packetId)
        throws NoteException;

    public String recvRedPacket(String packetId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_recvRedPacket(String packetId);

    public Ice.AsyncResult begin_recvRedPacket(String packetId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_recvRedPacket(String packetId, Ice.Callback __cb);

    public Ice.AsyncResult begin_recvRedPacket(String packetId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_recvRedPacket(String packetId, Callback_Haoqingbao_recvRedPacket __cb);

    public Ice.AsyncResult begin_recvRedPacket(String packetId, java.util.Map<String, String> __ctx, Callback_Haoqingbao_recvRedPacket __cb);

    public String end_recvRedPacket(Ice.AsyncResult __result)
        throws NoteException;

    public boolean recvRedPacket_async(AMI_Haoqingbao_recvRedPacket __cb, String packetId);

    public boolean recvRedPacket_async(AMI_Haoqingbao_recvRedPacket __cb, String packetId, java.util.Map<String, String> __ctx);

    public String rankList(int type)
        throws NoteException;

    public String rankList(int type, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_rankList(int type);

    public Ice.AsyncResult begin_rankList(int type, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_rankList(int type, Ice.Callback __cb);

    public Ice.AsyncResult begin_rankList(int type, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_rankList(int type, Callback_Haoqingbao_rankList __cb);

    public Ice.AsyncResult begin_rankList(int type, java.util.Map<String, String> __ctx, Callback_Haoqingbao_rankList __cb);

    public String end_rankList(Ice.AsyncResult __result)
        throws NoteException;

    public boolean rankList_async(AMI_Haoqingbao_rankList __cb, int type);

    public boolean rankList_async(AMI_Haoqingbao_rankList __cb, int type, java.util.Map<String, String> __ctx);

    public String getRedPacketDetail(String packetId)
        throws NoteException;

    public String getRedPacketDetail(String packetId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId);

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId, Ice.Callback __cb);

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId, Callback_Haoqingbao_getRedPacketDetail __cb);

    public Ice.AsyncResult begin_getRedPacketDetail(String packetId, java.util.Map<String, String> __ctx, Callback_Haoqingbao_getRedPacketDetail __cb);

    public String end_getRedPacketDetail(Ice.AsyncResult __result)
        throws NoteException;

    public boolean getRedPacketDetail_async(AMI_Haoqingbao_getRedPacketDetail __cb, String packetId);

    public boolean getRedPacketDetail_async(AMI_Haoqingbao_getRedPacketDetail __cb, String packetId, java.util.Map<String, String> __ctx);

    public String myRedPacket()
        throws NoteException;

    public String myRedPacket(java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_myRedPacket();

    public Ice.AsyncResult begin_myRedPacket(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_myRedPacket(Ice.Callback __cb);

    public Ice.AsyncResult begin_myRedPacket(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_myRedPacket(Callback_Haoqingbao_myRedPacket __cb);

    public Ice.AsyncResult begin_myRedPacket(java.util.Map<String, String> __ctx, Callback_Haoqingbao_myRedPacket __cb);

    public String end_myRedPacket(Ice.AsyncResult __result)
        throws NoteException;

    public boolean myRedPacket_async(AMI_Haoqingbao_myRedPacket __cb);

    public boolean myRedPacket_async(AMI_Haoqingbao_myRedPacket __cb, java.util.Map<String, String> __ctx);

    public String checkout(int num)
        throws NoteException;

    public String checkout(int num, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_checkout(int num);

    public Ice.AsyncResult begin_checkout(int num, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_checkout(int num, Ice.Callback __cb);

    public Ice.AsyncResult begin_checkout(int num, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_checkout(int num, Callback_Haoqingbao_checkout __cb);

    public Ice.AsyncResult begin_checkout(int num, java.util.Map<String, String> __ctx, Callback_Haoqingbao_checkout __cb);

    public String end_checkout(Ice.AsyncResult __result)
        throws NoteException;

    public boolean checkout_async(AMI_Haoqingbao_checkout __cb, int num);

    public boolean checkout_async(AMI_Haoqingbao_checkout __cb, int num, java.util.Map<String, String> __ctx);

    public void charge()
        throws NoteException;

    public void charge(java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_charge();

    public Ice.AsyncResult begin_charge(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_charge(Ice.Callback __cb);

    public Ice.AsyncResult begin_charge(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_charge(Callback_Haoqingbao_charge __cb);

    public Ice.AsyncResult begin_charge(java.util.Map<String, String> __ctx, Callback_Haoqingbao_charge __cb);

    public void end_charge(Ice.AsyncResult __result)
        throws NoteException;

    public boolean charge_async(AMI_Haoqingbao_charge __cb);

    public boolean charge_async(AMI_Haoqingbao_charge __cb, java.util.Map<String, String> __ctx);

    public String preRecvRedPacket(String packetId)
        throws NoteException;

    public String preRecvRedPacket(String packetId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId);

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId, Ice.Callback __cb);

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId, Callback_Haoqingbao_preRecvRedPacket __cb);

    public Ice.AsyncResult begin_preRecvRedPacket(String packetId, java.util.Map<String, String> __ctx, Callback_Haoqingbao_preRecvRedPacket __cb);

    public String end_preRecvRedPacket(Ice.AsyncResult __result)
        throws NoteException;

    public boolean preRecvRedPacket_async(AMI_Haoqingbao_preRecvRedPacket __cb, String packetId);

    public boolean preRecvRedPacket_async(AMI_Haoqingbao_preRecvRedPacket __cb, String packetId, java.util.Map<String, String> __ctx);

    public void claimRedPacket(String roleId, String packetId)
        throws NoteException;

    public void claimRedPacket(String roleId, String packetId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId);

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId, Ice.Callback __cb);

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId, Callback_Haoqingbao_claimRedPacket __cb);

    public Ice.AsyncResult begin_claimRedPacket(String roleId, String packetId, java.util.Map<String, String> __ctx, Callback_Haoqingbao_claimRedPacket __cb);

    public void end_claimRedPacket(Ice.AsyncResult __result)
        throws NoteException;

    public boolean claimRedPacket_async(AMI_Haoqingbao_claimRedPacket __cb, String roleId, String packetId);

    public boolean claimRedPacket_async(AMI_Haoqingbao_claimRedPacket __cb, String roleId, String packetId, java.util.Map<String, String> __ctx);
}
