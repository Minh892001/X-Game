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
// Generated from file `Activity.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class FortuneWheelRewardItemView implements java.lang.Cloneable, java.io.Serializable
{
    public int id;

    public String itemTemplateId;

    public int num;

    public int itemType;

    public FortuneWheelRewardItemView()
    {
    }

    public FortuneWheelRewardItemView(int id, String itemTemplateId, int num, int itemType)
    {
        this.id = id;
        this.itemTemplateId = itemTemplateId;
        this.num = num;
        this.itemType = itemType;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FortuneWheelRewardItemView _r = null;
        if(rhs instanceof FortuneWheelRewardItemView)
        {
            _r = (FortuneWheelRewardItemView)rhs;
        }

        if(_r != null)
        {
            if(id != _r.id)
            {
                return false;
            }
            if(itemTemplateId != _r.itemTemplateId)
            {
                if(itemTemplateId == null || _r.itemTemplateId == null || !itemTemplateId.equals(_r.itemTemplateId))
                {
                    return false;
                }
            }
            if(num != _r.num)
            {
                return false;
            }
            if(itemType != _r.itemType)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::FortuneWheelRewardItemView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, itemTemplateId);
        __h = IceInternal.HashUtil.hashAdd(__h, num);
        __h = IceInternal.HashUtil.hashAdd(__h, itemType);
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
        __os.writeString(itemTemplateId);
        __os.writeInt(num);
        __os.writeInt(itemType);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readInt();
        itemTemplateId = __is.readString();
        num = __is.readInt();
        itemType = __is.readInt();
    }

    public static final long serialVersionUID = 1635063075L;
}
