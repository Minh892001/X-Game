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
// Generated from file `Treasure.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 寻宝好友
 **/
public class TreasureFriend implements java.lang.Cloneable, java.io.Serializable
{
    public String id;

    public String icon;

    public String name;

    public int level;

    public int vip;

    public int count;

    public boolean enabled;

    public TreasureFriend()
    {
    }

    public TreasureFriend(String id, String icon, String name, int level, int vip, int count, boolean enabled)
    {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.level = level;
        this.vip = vip;
        this.count = count;
        this.enabled = enabled;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        TreasureFriend _r = null;
        if(rhs instanceof TreasureFriend)
        {
            _r = (TreasureFriend)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                if(id == null || _r.id == null || !id.equals(_r.id))
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
            if(name != _r.name)
            {
                if(name == null || _r.name == null || !name.equals(_r.name))
                {
                    return false;
                }
            }
            if(level != _r.level)
            {
                return false;
            }
            if(vip != _r.vip)
            {
                return false;
            }
            if(count != _r.count)
            {
                return false;
            }
            if(enabled != _r.enabled)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::TreasureFriend");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, icon);
        __h = IceInternal.HashUtil.hashAdd(__h, name);
        __h = IceInternal.HashUtil.hashAdd(__h, level);
        __h = IceInternal.HashUtil.hashAdd(__h, vip);
        __h = IceInternal.HashUtil.hashAdd(__h, count);
        __h = IceInternal.HashUtil.hashAdd(__h, enabled);
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
        __os.writeString(id);
        __os.writeString(icon);
        __os.writeString(name);
        __os.writeInt(level);
        __os.writeInt(vip);
        __os.writeInt(count);
        __os.writeBool(enabled);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readString();
        icon = __is.readString();
        name = __is.readString();
        level = __is.readInt();
        vip = __is.readInt();
        count = __is.readInt();
        enabled = __is.readBool();
    }

    public static final long serialVersionUID = 465714504L;
}
