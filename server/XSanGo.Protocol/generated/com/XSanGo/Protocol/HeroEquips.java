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
// Generated from file `Role.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class HeroEquips implements java.lang.Cloneable, java.io.Serializable
{
    public int heroId;

    public ItemView[] equips;

    public HeroEquips()
    {
    }

    public HeroEquips(int heroId, ItemView[] equips)
    {
        this.heroId = heroId;
        this.equips = equips;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        HeroEquips _r = null;
        if(rhs instanceof HeroEquips)
        {
            _r = (HeroEquips)rhs;
        }

        if(_r != null)
        {
            if(heroId != _r.heroId)
            {
                return false;
            }
            if(!java.util.Arrays.equals(equips, _r.equips))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::HeroEquips");
        __h = IceInternal.HashUtil.hashAdd(__h, heroId);
        __h = IceInternal.HashUtil.hashAdd(__h, equips);
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
        __os.writeInt(heroId);
        ItemViewSeqHelper.write(__os, equips);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        heroId = __is.readInt();
        equips = ItemViewSeqHelper.read(__is);
    }

    public static final long serialVersionUID = -2089524195L;
}
