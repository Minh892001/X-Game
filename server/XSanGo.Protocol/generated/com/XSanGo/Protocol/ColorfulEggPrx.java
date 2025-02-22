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
// Generated from file `ColorfullEgg.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface ColorfulEggPrx extends Ice.ObjectPrx
{
    public String getView()
        throws NoteException;

    public String getView(java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_getView();

    public Ice.AsyncResult begin_getView(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getView(Ice.Callback __cb);

    public Ice.AsyncResult begin_getView(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getView(Callback_ColorfulEgg_getView __cb);

    public Ice.AsyncResult begin_getView(java.util.Map<String, String> __ctx, Callback_ColorfulEgg_getView __cb);

    public String end_getView(Ice.AsyncResult __result)
        throws NoteException;

    public boolean getView_async(AMI_ColorfulEgg_getView __cb);

    public boolean getView_async(AMI_ColorfulEgg_getView __cb, java.util.Map<String, String> __ctx);

    public String brokenEgg(byte eggId)
        throws NoteException;

    public String brokenEgg(byte eggId, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_brokenEgg(byte eggId);

    public Ice.AsyncResult begin_brokenEgg(byte eggId, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_brokenEgg(byte eggId, Ice.Callback __cb);

    public Ice.AsyncResult begin_brokenEgg(byte eggId, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_brokenEgg(byte eggId, Callback_ColorfulEgg_brokenEgg __cb);

    public Ice.AsyncResult begin_brokenEgg(byte eggId, java.util.Map<String, String> __ctx, Callback_ColorfulEgg_brokenEgg __cb);

    public String end_brokenEgg(Ice.AsyncResult __result)
        throws NoteException;

    public boolean brokenEgg_async(AMI_ColorfulEgg_brokenEgg __cb, byte eggId);

    public boolean brokenEgg_async(AMI_ColorfulEgg_brokenEgg __cb, byte eggId, java.util.Map<String, String> __ctx);

    public void acceptReward(String itemId, int num)
        throws NoteException;

    public void acceptReward(String itemId, int num, java.util.Map<String, String> __ctx)
        throws NoteException;

    public Ice.AsyncResult begin_acceptReward(String itemId, int num);

    public Ice.AsyncResult begin_acceptReward(String itemId, int num, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_acceptReward(String itemId, int num, Ice.Callback __cb);

    public Ice.AsyncResult begin_acceptReward(String itemId, int num, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_acceptReward(String itemId, int num, Callback_ColorfulEgg_acceptReward __cb);

    public Ice.AsyncResult begin_acceptReward(String itemId, int num, java.util.Map<String, String> __ctx, Callback_ColorfulEgg_acceptReward __cb);

    public void end_acceptReward(Ice.AsyncResult __result)
        throws NoteException;

    public boolean acceptReward_async(AMI_ColorfulEgg_acceptReward __cb, String itemId, int num);

    public boolean acceptReward_async(AMI_ColorfulEgg_acceptReward __cb, String itemId, int num, java.util.Map<String, String> __ctx);
}
