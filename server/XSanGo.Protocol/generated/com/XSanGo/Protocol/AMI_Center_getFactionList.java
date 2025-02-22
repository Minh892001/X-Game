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

/**
 * 查找公会列表，通过factionName模糊查找
 **/

public abstract class AMI_Center_getFactionList extends Callback_Center_getFactionList
{
    /**
     * ice_response indicates that
     * the operation completed successfully.
     **/
    public abstract void ice_response(GmFactionView[] __ret);

    /**
     * ice_exception indicates to the caller that
     * the operation completed with an exception.
     * @param ex The Ice run-time exception to be raised.
     **/
    public abstract void ice_exception(Ice.LocalException ex);

    /**
     * ice_exception indicates to the caller that
     * the operation completed with an exception.
     * @param ex The user exception to be raised.
     **/
    public abstract void ice_exception(Ice.UserException ex);

    public final void response(GmFactionView[] __ret)
    {
        ice_response(__ret);
    }

    public final void exception(Ice.UserException __ex)
    {
        ice_exception(__ex);
    }

    public final void exception(Ice.LocalException __ex)
    {
        ice_exception(__ex);
    }

    @Override public final void sent(boolean sentSynchronously)
    {
        if(!sentSynchronously && this instanceof Ice.AMISentCallback)
        {
            ((Ice.AMISentCallback)this).ice_sent();
        }
    }
}
