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
// Generated from file `Task.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class ActPointAward implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public int point;

    public IntString[] awards;

    public int state;

    public String icons;

    public ActPointAward()
    {
    }

    public ActPointAward(int id, int point, IntString[] awards, int state, String icons)
    {
        this.id = id;
        this.point = point;
        this.awards = awards;
        this.state = state;
        this.icons = icons;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        ActPointAward _r = null;
        if(rhs instanceof ActPointAward)
        {
            _r = (ActPointAward)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(point != _r.point)
            {
                return false;
            }
            if(!java.util.Arrays.equals(awards, _r.awards))
            {
                return false;
            }
            if(state != _r.state)
            {
                return false;
            }
            if(icons != _r.icons)
            {
                if(icons == null || _r.icons == null || !icons.equals(_r.icons))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::ActPointAward");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, point);
        __h = IceInternal.HashUtil.hashAdd(__h, awards);
        __h = IceInternal.HashUtil.hashAdd(__h, state);
        __h = IceInternal.HashUtil.hashAdd(__h, icons);
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
        __os.writeInt(id);
        __os.writeInt(point);
        IntStringSeqHelper.write(__os, awards);
        __os.writeInt(state);
        __os.writeString(icons);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        point = __is.readInt();
        awards = IntStringSeqHelper.read(__is);
        state = __is.readInt();
        icons = __is.readString();
    }

    public static final long serialVersionUID = 149901872L;
}
