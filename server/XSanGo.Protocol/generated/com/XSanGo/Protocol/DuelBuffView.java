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

public class DuelBuffView implements java.lang.Cloneable, java.io.Serializable
{
    public int targetId;

    public String desc;

    public String icon;

    public DuelBuffView()
    {
    }

    public DuelBuffView(int targetId, String desc, String icon)
    {
        this.targetId = targetId;
        this.desc = desc;
        this.icon = icon;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        DuelBuffView _r = null;
        if(rhs instanceof DuelBuffView)
        {
            _r = (DuelBuffView)rhs;
        }

        if(_r != null)
        {
            if(targetId != _r.targetId)
            {
                return false;
            }
            if(desc != _r.desc)
            {
                if(desc == null || _r.desc == null || !desc.equals(_r.desc))
                {
                    return false;
                }
            }
            if(icon != _r.icon)
            {
                if(icon == null || _r.icon == null || !icon.equals(_r.icon))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::DuelBuffView");
        __h = IceInternal.HashUtil.hashAdd(__h, targetId);
        __h = IceInternal.HashUtil.hashAdd(__h, desc);
        __h = IceInternal.HashUtil.hashAdd(__h, icon);
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeInt(targetId);
        __os.writeString(desc);
        __os.writeString(icon);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        targetId = __is.readInt();
        desc = __is.readString();
        icon = __is.readString();
    }

    public static final long serialVersionUID = 265271827L;
}
