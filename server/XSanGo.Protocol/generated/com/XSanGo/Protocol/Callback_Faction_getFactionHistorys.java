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
// Generated from file `Faction.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 查看公会动态(历史记录) 返回FactionHistoryViewSeq的lua
 **/

public abstract class Callback_Faction_getFactionHistorys extends Ice.TwowayCallback
{
    public abstract void response(String __ret);

    public final void __completed(Ice.AsyncResult __result)
    {
        FactionPrx __proxy = (FactionPrx)__result.getProxy();
        String __ret = null;
        try
        {
            __ret = __proxy.end_getFactionHistorys(__result);
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
