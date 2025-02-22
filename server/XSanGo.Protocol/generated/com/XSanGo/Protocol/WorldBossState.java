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
// Generated from file `WorldBoss.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * Boss状态 0-可挑战1-死亡2-未开启
 **/
public enum WorldBossState implements java.io.Serializable
{
    
    CanChallenge(0),
    
    Death(1),
    
    Away(2);

    public int
    value()
    {
        return __value;
    }

    public static WorldBossState
    valueOf(int __v)
    {
        switch(__v)
        {
        case 0:
            return CanChallenge;
        case 1:
            return Death;
        case 2:
            return Away;
        }
        return null;
    }

    private
    WorldBossState(int __v)
    {
        __value = __v;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeEnum(value(), 2);
    }

    public static WorldBossState
    __read(IceInternal.BasicStream __is)
    {
        int __v = __is.readEnum(2);
        return __validate(__v);
    }

    private static WorldBossState
    __validate(int __v)
    {
        final WorldBossState __e = valueOf(__v);
        if(__e == null)
        {
            throw new Ice.MarshalException("enumerator value " + __v + " is out of range");
        }
        return __e;
    }

    private final int __value;
}
