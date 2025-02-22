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

public class CdkDetailView implements java.lang.Cloneable, java.io.Serializable
{
    public CdkView detailView;

    public String useDate;

    public String roleName;

    public int useNum;

    public CdkDetailView()
    {
    }

    public CdkDetailView(CdkView detailView, String useDate, String roleName, int useNum)
    {
        this.detailView = detailView;
        this.useDate = useDate;
        this.roleName = roleName;
        this.useNum = useNum;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CdkDetailView _r = null;
        if(rhs instanceof CdkDetailView)
        {
            _r = (CdkDetailView)rhs;
        }

        if(_r != null)
        {
            if(detailView != _r.detailView)
            {
                if(detailView == null || _r.detailView == null || !detailView.equals(_r.detailView))
                {
                    return false;
                }
            }
            if(useDate != _r.useDate)
            {
                if(useDate == null || _r.useDate == null || !useDate.equals(_r.useDate))
                {
                    return false;
                }
            }
            if(roleName != _r.roleName)
            {
                if(roleName == null || _r.roleName == null || !roleName.equals(_r.roleName))
                {
                    return false;
                }
            }
            if(useNum != _r.useNum)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CdkDetailView");
        __h = IceInternal.HashUtil.hashAdd(__h, detailView);
        __h = IceInternal.HashUtil.hashAdd(__h, useDate);
        __h = IceInternal.HashUtil.hashAdd(__h, roleName);
        __h = IceInternal.HashUtil.hashAdd(__h, useNum);
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
        detailView.__write(__os);
        __os.writeString(useDate);
        __os.writeString(roleName);
        __os.writeInt(useNum);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        detailView = new CdkView();
        detailView.__read(__is);
        useDate = __is.readString();
        roleName = __is.readString();
        useNum = __is.readInt();
    }

    public static final long serialVersionUID = 275542477L;
}
