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
// Generated from file `GameSession.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public abstract class Callback_GameSession_getRole extends Ice.TwowayCallback
{
    public abstract void response(RolePrx __ret);

    public final void __completed(Ice.AsyncResult __result)
    {
        GameSessionPrx __proxy = (GameSessionPrx)__result.getProxy();
        RolePrx __ret = null;
        try
        {
            __ret = __proxy.end_getRole(__result);
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
