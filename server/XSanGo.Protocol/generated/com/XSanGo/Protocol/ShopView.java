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
// Generated from file `Shop.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class ShopView implements java.lang.Cloneable, java.io.Serializable
{
    public String id;

    public ItemType iType;

    public String templateId;

    public int num;

    public String name;

    public String remark;

    public int price;

    public int discountPrice;

    public String startTime;

    public String endTime;

    public int buyTimes;

    public int maxBuyTimes;

    public int buyVipLevel;

    public int buyLevel;

    public int tag;

    public String tips;

    public String icon;

    public int remainSecond;

    public boolean isDiscount;

    public boolean isUseOut;

    public int discountIcon;

    public int fadeSecond;

    public ShopView()
    {
    }

    public ShopView(String id, ItemType iType, String templateId, int num, String name, String remark, int price, int discountPrice, String startTime, String endTime, int buyTimes, int maxBuyTimes, int buyVipLevel, int buyLevel, int tag, String tips, String icon, int remainSecond, boolean isDiscount, boolean isUseOut, int discountIcon, int fadeSecond)
    {
        this.id = id;
        this.iType = iType;
        this.templateId = templateId;
        this.num = num;
        this.name = name;
        this.remark = remark;
        this.price = price;
        this.discountPrice = discountPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.buyTimes = buyTimes;
        this.maxBuyTimes = maxBuyTimes;
        this.buyVipLevel = buyVipLevel;
        this.buyLevel = buyLevel;
        this.tag = tag;
        this.tips = tips;
        this.icon = icon;
        this.remainSecond = remainSecond;
        this.isDiscount = isDiscount;
        this.isUseOut = isUseOut;
        this.discountIcon = discountIcon;
        this.fadeSecond = fadeSecond;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        ShopView _r = null;
        if(rhs instanceof ShopView)
        {
            _r = (ShopView)rhs;
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
            if(iType != _r.iType)
            {
                if(iType == null || _r.iType == null || !iType.equals(_r.iType))
                {
                    return false;
                }
            }
            if(templateId != _r.templateId)
            {
                if(templateId == null || _r.templateId == null || !templateId.equals(_r.templateId))
                {
                    return false;
                }
            }
            if(num != _r.num)
            {
                return false;
            }
            if(name != _r.name)
            {
                if(name == null || _r.name == null || !name.equals(_r.name))
                {
                    return false;
                }
            }
            if(remark != _r.remark)
            {
                if(remark == null || _r.remark == null || !remark.equals(_r.remark))
                {
                    return false;
                }
            }
            if(price != _r.price)
            {
                return false;
            }
            if(discountPrice != _r.discountPrice)
            {
                return false;
            }
            if(startTime != _r.startTime)
            {
                if(startTime == null || _r.startTime == null || !startTime.equals(_r.startTime))
                {
                    return false;
                }
            }
            if(endTime != _r.endTime)
            {
                if(endTime == null || _r.endTime == null || !endTime.equals(_r.endTime))
                {
                    return false;
                }
            }
            if(buyTimes != _r.buyTimes)
            {
                return false;
            }
            if(maxBuyTimes != _r.maxBuyTimes)
            {
                return false;
            }
            if(buyVipLevel != _r.buyVipLevel)
            {
                return false;
            }
            if(buyLevel != _r.buyLevel)
            {
                return false;
            }
            if(tag != _r.tag)
            {
                return false;
            }
            if(tips != _r.tips)
            {
                if(tips == null || _r.tips == null || !tips.equals(_r.tips))
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
            if(remainSecond != _r.remainSecond)
            {
                return false;
            }
            if(isDiscount != _r.isDiscount)
            {
                return false;
            }
            if(isUseOut != _r.isUseOut)
            {
                return false;
            }
            if(discountIcon != _r.discountIcon)
            {
                return false;
            }
            if(fadeSecond != _r.fadeSecond)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::ShopView");
        __h = IceInternal.HashUtil.hashAdd(__h, id);
        __h = IceInternal.HashUtil.hashAdd(__h, iType);
        __h = IceInternal.HashUtil.hashAdd(__h, templateId);
        __h = IceInternal.HashUtil.hashAdd(__h, num);
        __h = IceInternal.HashUtil.hashAdd(__h, name);
        __h = IceInternal.HashUtil.hashAdd(__h, remark);
        __h = IceInternal.HashUtil.hashAdd(__h, price);
        __h = IceInternal.HashUtil.hashAdd(__h, discountPrice);
        __h = IceInternal.HashUtil.hashAdd(__h, startTime);
        __h = IceInternal.HashUtil.hashAdd(__h, endTime);
        __h = IceInternal.HashUtil.hashAdd(__h, buyTimes);
        __h = IceInternal.HashUtil.hashAdd(__h, maxBuyTimes);
        __h = IceInternal.HashUtil.hashAdd(__h, buyVipLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, buyLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, tag);
        __h = IceInternal.HashUtil.hashAdd(__h, tips);
        __h = IceInternal.HashUtil.hashAdd(__h, icon);
        __h = IceInternal.HashUtil.hashAdd(__h, remainSecond);
        __h = IceInternal.HashUtil.hashAdd(__h, isDiscount);
        __h = IceInternal.HashUtil.hashAdd(__h, isUseOut);
        __h = IceInternal.HashUtil.hashAdd(__h, discountIcon);
        __h = IceInternal.HashUtil.hashAdd(__h, fadeSecond);
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
        iType.__write(__os);
        __os.writeString(templateId);
        __os.writeInt(num);
        __os.writeString(name);
        __os.writeString(remark);
        __os.writeInt(price);
        __os.writeInt(discountPrice);
        __os.writeString(startTime);
        __os.writeString(endTime);
        __os.writeInt(buyTimes);
        __os.writeInt(maxBuyTimes);
        __os.writeInt(buyVipLevel);
        __os.writeInt(buyLevel);
        __os.writeInt(tag);
        __os.writeString(tips);
        __os.writeString(icon);
        __os.writeInt(remainSecond);
        __os.writeBool(isDiscount);
        __os.writeBool(isUseOut);
        __os.writeInt(discountIcon);
        __os.writeInt(fadeSecond);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        id = __is.readString();
        iType = ItemType.__read(__is);
        templateId = __is.readString();
        num = __is.readInt();
        name = __is.readString();
        remark = __is.readString();
        price = __is.readInt();
        discountPrice = __is.readInt();
        startTime = __is.readString();
        endTime = __is.readString();
        buyTimes = __is.readInt();
        maxBuyTimes = __is.readInt();
        buyVipLevel = __is.readInt();
        buyLevel = __is.readInt();
        tag = __is.readInt();
        tips = __is.readString();
        icon = __is.readString();
        remainSecond = __is.readInt();
        isDiscount = __is.readBool();
        isUseOut = __is.readBool();
        discountIcon = __is.readInt();
        fadeSecond = __is.readInt();
    }

    public static final long serialVersionUID = -1263885831L;
}
