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
// Generated from file `Tournament.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 获取自己积分和当日胜利次数 return IntIntPair
 **/

public abstract class Callback_Tournament_getScoreAndWinNum extends Ice.TwowayCallback
{
    public abstract void response(String __ret);
    public abstract void exception(Ice.UserException __ex);

    public final void __completed(Ice.AsyncResult __result)
    {
        TournamentPrx __proxy = (TournamentPrx)__result.getProxy();
        String __ret = null;
        try
        {
            __ret = __proxy.end_getScoreAndWinNum(__result);
        }
        catch(Ice.UserException __ex)
        {
            exception(__ex);
            return;
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
