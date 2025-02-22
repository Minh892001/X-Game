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
// Generated from file `Equip.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class EquipLevelEntity implements java.lang.Cloneable, java.io.Serializable
{
    public String id;

    public int level;

    public EquipLevelEntity()
    {
    }

    public EquipLevelEntity(String id, int level)
    {
        this.id = id;
        this.level = level;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        EquipLevelEntity _r = null;
        if(rhs instanceof EquipLevelEntity)
        {
            _r = (EquipLevelEntity)rhs;
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
            if(level != _r.level)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::EquipLevelEntity");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, level);
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
        __os.writeInt(level);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readString();
        level = __is.readInt();
    }

    public static final long serialVersionUID = -107070786L;
}
