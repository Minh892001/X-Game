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
// Generated from file `Trader.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class TraderCallbackHolder extends Ice.ObjectHolderBase<TraderCallback>
{
    public
    TraderCallbackHolder()
    {
    }

    public
    TraderCallbackHolder(TraderCallback value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof TraderCallback)
        {
            value = (TraderCallback)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _TraderCallbackDisp.ice_staticId();
    }
}
