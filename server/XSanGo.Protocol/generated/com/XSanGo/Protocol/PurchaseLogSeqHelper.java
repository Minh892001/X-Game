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

public final class PurchaseLogSeqHelper
{
    public static void
    write(IceInternal.BasicStream __os, PurchaseLog[] __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.length);
            for(int __i0 = 0; __i0 < __v.length; __i0++)
            {
                __v[__i0].__write(__os);
            }
        }
    }

    public static PurchaseLog[]
    read(IceInternal.BasicStream __is)
    {
        PurchaseLog[] __v;
        final int __len0 = __is.readAndCheckSeqSize(7);
        __v = new PurchaseLog[__len0];
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v[__i0] = new PurchaseLog();
            __v[__i0].__read(__is);
        }
        return __v;
    }
}
