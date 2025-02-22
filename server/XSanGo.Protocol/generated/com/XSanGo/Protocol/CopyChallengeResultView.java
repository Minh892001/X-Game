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
// Generated from file `Copy.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public class CopyChallengeResultView implements java.lang.Cloneable, java.io.Serializable
{
    public Property[] heroExps;

    public ItemView[] items;

    public DuelUnitView[] duelCandidate;

    public SceneDuelView[] reports;

    public CaptureView[] capture;

    public String movieId;

    public int buff;

    public CopyChallengeResultView()
    {
    }

    public CopyChallengeResultView(Property[] heroExps, ItemView[] items, DuelUnitView[] duelCandidate, SceneDuelView[] reports, CaptureView[] capture, String movieId, int buff)
    {
        this.heroExps = heroExps;
        this.items = items;
        this.duelCandidate = duelCandidate;
        this.reports = reports;
        this.capture = capture;
        this.movieId = movieId;
        this.buff = buff;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CopyChallengeResultView _r = null;
        if(rhs instanceof CopyChallengeResultView)
        {
            _r = (CopyChallengeResultView)rhs;
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(heroExps, _r.heroExps))
            {
                return false;
            }
            if(!java.util.Arrays.equals(items, _r.items))
            {
                return false;
            }
            if(!java.util.Arrays.equals(duelCandidate, _r.duelCandidate))
            {
                return false;
            }
            if(!java.util.Arrays.equals(reports, _r.reports))
            {
                return false;
            }
            if(!java.util.Arrays.equals(capture, _r.capture))
            {
                return false;
            }
            if(movieId != _r.movieId)
            {
                if(movieId == null || _r.movieId == null || !movieId.equals(_r.movieId))
                {
                    return false;
                }
            }
            if(buff != _r.buff)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::CopyChallengeResultView");
        __h = IceInternal.HashUtil.hashAdd(__h, heroExps);
        __h = IceInternal.HashUtil.hashAdd(__h, items);
        __h = IceInternal.HashUtil.hashAdd(__h, duelCandidate);
        __h = IceInternal.HashUtil.hashAdd(__h, reports);
        __h = IceInternal.HashUtil.hashAdd(__h, capture);
        __h = IceInternal.HashUtil.hashAdd(__h, movieId);
        __h = IceInternal.HashUtil.hashAdd(__h, buff);
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
        PropertySeqHelper.write(__os, heroExps);
        ItemViewSeqHelper.write(__os, items);
        DuelUnitViewSeqHelper.write(__os, duelCandidate);
        SceneDuelViewSeqHelper.write(__os, reports);
        CaptureViewSeqHelper.write(__os, capture);
        __os.writeString(movieId);
        __os.writeInt(buff);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        heroExps = PropertySeqHelper.read(__is);
        items = ItemViewSeqHelper.read(__is);
        duelCandidate = DuelUnitViewSeqHelper.read(__is);
        reports = SceneDuelViewSeqHelper.read(__is);
        capture = CaptureViewSeqHelper.read(__is);
        movieId = __is.readString();
        buff = __is.readInt();
    }

    public static final long serialVersionUID = 863748171L;
}
