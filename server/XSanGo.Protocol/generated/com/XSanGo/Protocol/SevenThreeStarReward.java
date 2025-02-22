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

public class SevenThreeStarReward implements java.lang.Cloneable, java.io.Serializable
{
    public String sceneId;

    public IntString[] awards;

    public int states;

    public String title;

    public SevenThreeStarReward()
    {
    }

    public SevenThreeStarReward(String sceneId, IntString[] awards, int states, String title)
    {
        this.sceneId = sceneId;
        this.awards = awards;
        this.states = states;
        this.title = title;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        SevenThreeStarReward _r = null;
        if(rhs instanceof SevenThreeStarReward)
        {
            _r = (SevenThreeStarReward)rhs;
        }

        if(_r != null)
        {
            if(sceneId != _r.sceneId)
            {
                if(sceneId == null || _r.sceneId == null || !sceneId.equals(_r.sceneId))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(awards, _r.awards))
            {
                return false;
            }
            if(states != _r.states)
            {
                return false;
            }
            if(title != _r.title)
            {
                if(title == null || _r.title == null || !title.equals(_r.title))
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::SevenThreeStarReward");
        __h = IceInternal.HashUtil.hashAdd(__h, sceneId);
        __h = IceInternal.HashUtil.hashAdd(__h, awards);
        __h = IceInternal.HashUtil.hashAdd(__h, states);
        __h = IceInternal.HashUtil.hashAdd(__h, title);
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
        __os.writeString(sceneId);
        IntStringSeqHelper.write(__os, awards);
        __os.writeInt(states);
        __os.writeString(title);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        sceneId = __is.readString();
        awards = IntStringSeqHelper.read(__is);
        states = __is.readInt();
        title = __is.readString();
    }

    public static final long serialVersionUID = -1787444109L;
}
