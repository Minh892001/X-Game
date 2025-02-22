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
// Generated from file `Center.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class CdkView implements java.lang.Cloneable, java.io.Serializable
{
    public String name;

    public int[] channelIds;

    public String prefix;

    public int maxLevel;

    public int minLevel;

    public String union;

    public int needPay;

    public String startTime;

    public String endTime;

    public String adjunct;

    public String remark;

    public int number;

    public int[] serverIds;

    public CdkView()
    {
    }

    public CdkView(String name, int[] channelIds, String prefix, int maxLevel, int minLevel, String union, int needPay, String startTime, String endTime, String adjunct, String remark, int number, int[] serverIds)
    {
        this.name = name;
        this.channelIds = channelIds;
        this.prefix = prefix;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
        this.union = union;
        this.needPay = needPay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.adjunct = adjunct;
        this.remark = remark;
        this.number = number;
        this.serverIds = serverIds;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CdkView _r = null;
        if(rhs instanceof CdkView)
        {
            _r = (CdkView)rhs;
        }

        if(_r != null)
        {
            if(name != _r.name)
            {
                if(name == null || _r.name == null || !name.equals(_r.name))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(channelIds, _r.channelIds))
            {
                return false;
            }
            if(prefix != _r.prefix)
            {
                if(prefix == null || _r.prefix == null || !prefix.equals(_r.prefix))
                {
                    return false;
                }
            }
            if(maxLevel != _r.maxLevel)
            {
                return false;
            }
            if(minLevel != _r.minLevel)
            {
                return false;
            }
            if(union != _r.union)
            {
                if(union == null || _r.union == null || !union.equals(_r.union))
                {
                    return false;
                }
            }
            if(needPay != _r.needPay)
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
            if(adjunct != _r.adjunct)
            {
                if(adjunct == null || _r.adjunct == null || !adjunct.equals(_r.adjunct))
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
            if(number != _r.number)
            {
                return false;
            }
            if(!java.util.Arrays.equals(serverIds, _r.serverIds))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CdkView");
        __h = IceInternal.HashUtil.hashAdd(__h, name);
        __h = IceInternal.HashUtil.hashAdd(__h, channelIds);
        __h = IceInternal.HashUtil.hashAdd(__h, prefix);
        __h = IceInternal.HashUtil.hashAdd(__h, maxLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, minLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, union);
        __h = IceInternal.HashUtil.hashAdd(__h, needPay);
        __h = IceInternal.HashUtil.hashAdd(__h, startTime);
        __h = IceInternal.HashUtil.hashAdd(__h, endTime);
        __h = IceInternal.HashUtil.hashAdd(__h, adjunct);
        __h = IceInternal.HashUtil.hashAdd(__h, remark);
        __h = IceInternal.HashUtil.hashAdd(__h, number);
        __h = IceInternal.HashUtil.hashAdd(__h, serverIds);
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
        __os.writeString(name);
        IntSeqHelper.write(__os, channelIds);
        __os.writeString(prefix);
        __os.writeInt(maxLevel);
        __os.writeInt(minLevel);
        __os.writeString(union);
        __os.writeInt(needPay);
        __os.writeString(startTime);
        __os.writeString(endTime);
        __os.writeString(adjunct);
        __os.writeString(remark);
        __os.writeInt(number);
        IntSeqHelper.write(__os, serverIds);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        name = __is.readString();
        channelIds = IntSeqHelper.read(__is);
        prefix = __is.readString();
        maxLevel = __is.readInt();
        minLevel = __is.readInt();
        union = __is.readString();
        needPay = __is.readInt();
        startTime = __is.readString();
        endTime = __is.readString();
        adjunct = __is.readString();
        remark = __is.readString();
        number = __is.readInt();
        serverIds = IntSeqHelper.read(__is);
    }

    public static final long serialVersionUID = -2010725403L;
}
