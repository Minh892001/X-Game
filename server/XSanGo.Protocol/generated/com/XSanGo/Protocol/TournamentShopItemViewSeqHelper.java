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

public final class TournamentShopItemViewSeqHelper
{
    public static void
    write(IceInternal.BasicStream __os, TournamentShopItemView[] __v)
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

    public static TournamentShopItemView[]
    read(IceInternal.BasicStream __is)
    {
        TournamentShopItemView[] __v;
        final int __len0 = __is.readAndCheckSeqSize(19);
        __v = new TournamentShopItemView[__len0];
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v[__i0] = new TournamentShopItemView();
            __v[__i0].__read(__is);
        }
        return __v;
    }
}
