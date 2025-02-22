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
// Generated from file `Sns.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class BattleRecordRole implements java.lang.Cloneable, java.io.Serializable
{
    public SnsRoleView roleView;

    public String recordId;

    public String date;

    public int challengeTimes;

    public int result;

    public BattleRecordRole()
    {
    }

    public BattleRecordRole(SnsRoleView roleView, String recordId, String date, int challengeTimes, int result)
    {
        this.roleView = roleView;
        this.recordId = recordId;
        this.date = date;
        this.challengeTimes = challengeTimes;
        this.result = result;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        BattleRecordRole _r = null;
        if(rhs instanceof BattleRecordRole)
        {
            _r = (BattleRecordRole)rhs;
        }

        if(_r != null)
        {
            if(roleView != _r.roleView)
            {
                if(roleView == null || _r.roleView == null || !roleView.equals(_r.roleView))
                {
                    return false;
                }
            }
            if(recordId != _r.recordId)
            {
                if(recordId == null || _r.recordId == null || !recordId.equals(_r.recordId))
                {
                    return false;
                }
            }
            if(date != _r.date)
            {
                if(date == null || _r.date == null || !date.equals(_r.date))
                {
                    return false;
                }
            }
            if(challengeTimes != _r.challengeTimes)
            {
                return false;
            }
            if(result != _r.result)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::BattleRecordRole");
        __h = IceInternal.HashUtil.hashAdd(__h, roleView);
        __h = IceInternal.HashUtil.hashAdd(__h, recordId);
        __h = IceInternal.HashUtil.hashAdd(__h, date);
        __h = IceInternal.HashUtil.hashAdd(__h, challengeTimes);
        __h = IceInternal.HashUtil.hashAdd(__h, result);
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
        roleView.__write(__os);
        __os.writeString(recordId);
        __os.writeString(date);
        __os.writeInt(challengeTimes);
        __os.writeInt(result);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        roleView = new SnsRoleView();
        roleView.__read(__is);
        recordId = __is.readString();
        date = __is.readString();
        challengeTimes = __is.readInt();
        result = __is.readInt();
    }

    public static final long serialVersionUID = -293150907L;
}
