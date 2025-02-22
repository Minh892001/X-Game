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
// Generated from file `ItemChip.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class HeroResetView implements java.lang.Cloneable, java.io.Serializable
{
    public String heroTemplateId;

    public HeroConsumeView consume;

    public HeroResetView()
    {
    }

    public HeroResetView(String heroTemplateId, HeroConsumeView consume)
    {
        this.heroTemplateId = heroTemplateId;
        this.consume = consume;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        HeroResetView _r = null;
        if(rhs instanceof HeroResetView)
        {
            _r = (HeroResetView)rhs;
        }

        if(_r != null)
        {
            if(heroTemplateId != _r.heroTemplateId)
            {
                if(heroTemplateId == null || _r.heroTemplateId == null || !heroTemplateId.equals(_r.heroTemplateId))
                {
                    return false;
                }
            }
            if(consume != _r.consume)
            {
                if(consume == null || _r.consume == null || !consume.equals(_r.consume))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::HeroResetView");
        __h = IceInternal.HashUtil.hashAdd(__h, heroTemplateId);
        __h = IceInternal.HashUtil.hashAdd(__h, consume);
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
        __os.writeString(heroTemplateId);
        consume.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        heroTemplateId = __is.readString();
        consume = new HeroConsumeView();
        consume.__read(__is);
    }

    public static final long serialVersionUID = 1849555673L;
}
