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
// Generated from file `Common.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public final class ActionReportViewSeqHelper
{
    public static void
    write(IceInternal.BasicStream __os, ActionReportView[] __v)
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

    public static ActionReportView[]
    read(IceInternal.BasicStream __is)
    {
        ActionReportView[] __v;
        final int __len0 = __is.readAndCheckSeqSize(35);
        __v = new ActionReportView[__len0];
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v[__i0] = new ActionReportView();
            __v[__i0].__read(__is);
        }
        return __v;
    }
}
