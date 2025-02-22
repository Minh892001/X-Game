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

public enum QualityColor implements java.io.Serializable
{
    
    Silver(0),
    
    Green(1),
    
    Blue(2),
    
    Violet(3),
    
    Orange(4);

    public int
    value()
    {
        return __value;
    }

    public static QualityColor
    valueOf(int __v)
    {
        switch(__v)
        {
        case 0:
            return Silver;
        case 1:
            return Green;
        case 2:
            return Blue;
        case 3:
            return Violet;
        case 4:
            return Orange;
        }
        return null;
    }

    private
    QualityColor(int __v)
    {
        __value = __v;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeEnum(value(), 4);
    }

    public static QualityColor
    __read(IceInternal.BasicStream __is)
    {
        int __v = __is.readEnum(4);
        return __validate(__v);
    }

    private static QualityColor
    __validate(int __v)
    {
        final QualityColor __e = valueOf(__v);
        if(__e == null)
        {
            throw new Ice.MarshalException("enumerator value " + __v + " is out of range");
        }
        return __e;
    }

    private final int __value;
}
