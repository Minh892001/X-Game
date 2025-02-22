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

public class MarksmanView implements java.lang.Cloneable, java.io.Serializable
{
    public ShootAwardInfo[] awardList;

    public long remainderTime;

    public IntString[] items;

    public int singleShootType;

    public int tenShootCostType;

    public int singleShoot;

    public int tenShootCost;

    public int[] channelIds;

    public int basis;

    public int singleShootScore;

    public int tenShootScore;

    public int freeTime;

    public int totalNeedNum;

    public int curNum;

    public long shootLastTime;

    public int myScore;

    public MarksmanScoreReward[] marksmanScoreRewards;

    public boolean showMyRecord;

    public int needLevel;

    public int needVip;

    public MarksmanView()
    {
    }

    public MarksmanView(ShootAwardInfo[] awardList, long remainderTime, IntString[] items, int singleShootType, int tenShootCostType, int singleShoot, int tenShootCost, int[] channelIds, int basis, int singleShootScore, int tenShootScore, int freeTime, int totalNeedNum, int curNum, long shootLastTime, int myScore, MarksmanScoreReward[] marksmanScoreRewards, boolean showMyRecord, int needLevel, int needVip)
    {
        this.awardList = awardList;
        this.remainderTime = remainderTime;
        this.items = items;
        this.singleShootType = singleShootType;
        this.tenShootCostType = tenShootCostType;
        this.singleShoot = singleShoot;
        this.tenShootCost = tenShootCost;
        this.channelIds = channelIds;
        this.basis = basis;
        this.singleShootScore = singleShootScore;
        this.tenShootScore = tenShootScore;
        this.freeTime = freeTime;
        this.totalNeedNum = totalNeedNum;
        this.curNum = curNum;
        this.shootLastTime = shootLastTime;
        this.myScore = myScore;
        this.marksmanScoreRewards = marksmanScoreRewards;
        this.showMyRecord = showMyRecord;
        this.needLevel = needLevel;
        this.needVip = needVip;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        MarksmanView _r = null;
        if(rhs instanceof MarksmanView)
        {
            _r = (MarksmanView)rhs;
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(awardList, _r.awardList))
            {
                return false;
            }
            if(remainderTime != _r.remainderTime)
            {
                return false;
            }
            if(!java.util.Arrays.equals(items, _r.items))
            {
                return false;
            }
            if(singleShootType != _r.singleShootType)
            {
                return false;
            }
            if(tenShootCostType != _r.tenShootCostType)
            {
                return false;
            }
            if(singleShoot != _r.singleShoot)
            {
                return false;
            }
            if(tenShootCost != _r.tenShootCost)
            {
                return false;
            }
            if(!java.util.Arrays.equals(channelIds, _r.channelIds))
            {
                return false;
            }
            if(basis != _r.basis)
            {
                return false;
            }
            if(singleShootScore != _r.singleShootScore)
            {
                return false;
            }
            if(tenShootScore != _r.tenShootScore)
            {
                return false;
            }
            if(freeTime != _r.freeTime)
            {
                return false;
            }
            if(totalNeedNum != _r.totalNeedNum)
            {
                return false;
            }
            if(curNum != _r.curNum)
            {
                return false;
            }
            if(shootLastTime != _r.shootLastTime)
            {
                return false;
            }
            if(myScore != _r.myScore)
            {
                return false;
            }
            if(!java.util.Arrays.equals(marksmanScoreRewards, _r.marksmanScoreRewards))
            {
                return false;
            }
            if(showMyRecord != _r.showMyRecord)
            {
                return false;
            }
            if(needLevel != _r.needLevel)
            {
                return false;
            }
            if(needVip != _r.needVip)
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
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::XSanGo::Protocol::MarksmanView");
        __h = IceInternal.HashUtil.hashAdd(__h, awardList);
        __h = IceInternal.HashUtil.hashAdd(__h, remainderTime);
        __h = IceInternal.HashUtil.hashAdd(__h, items);
        __h = IceInternal.HashUtil.hashAdd(__h, singleShootType);
        __h = IceInternal.HashUtil.hashAdd(__h, tenShootCostType);
        __h = IceInternal.HashUtil.hashAdd(__h, singleShoot);
        __h = IceInternal.HashUtil.hashAdd(__h, tenShootCost);
        __h = IceInternal.HashUtil.hashAdd(__h, channelIds);
        __h = IceInternal.HashUtil.hashAdd(__h, basis);
        __h = IceInternal.HashUtil.hashAdd(__h, singleShootScore);
        __h = IceInternal.HashUtil.hashAdd(__h, tenShootScore);
        __h = IceInternal.HashUtil.hashAdd(__h, freeTime);
        __h = IceInternal.HashUtil.hashAdd(__h, totalNeedNum);
        __h = IceInternal.HashUtil.hashAdd(__h, curNum);
        __h = IceInternal.HashUtil.hashAdd(__h, shootLastTime);
        __h = IceInternal.HashUtil.hashAdd(__h, myScore);
        __h = IceInternal.HashUtil.hashAdd(__h, marksmanScoreRewards);
        __h = IceInternal.HashUtil.hashAdd(__h, showMyRecord);
        __h = IceInternal.HashUtil.hashAdd(__h, needLevel);
        __h = IceInternal.HashUtil.hashAdd(__h, needVip);
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
        ShootAwardInfoSeqHelper.write(__os, awardList);
        __os.writeLong(remainderTime);
        IntStringSeqHelper.write(__os, items);
        __os.writeInt(singleShootType);
        __os.writeInt(tenShootCostType);
        __os.writeInt(singleShoot);
        __os.writeInt(tenShootCost);
        IntSeqHelper.write(__os, channelIds);
        __os.writeInt(basis);
        __os.writeInt(singleShootScore);
        __os.writeInt(tenShootScore);
        __os.writeInt(freeTime);
        __os.writeInt(totalNeedNum);
        __os.writeInt(curNum);
        __os.writeLong(shootLastTime);
        __os.writeInt(myScore);
        MarksmanScoreRewardSeqHelper.write(__os, marksmanScoreRewards);
        __os.writeBool(showMyRecord);
        __os.writeInt(needLevel);
        __os.writeInt(needVip);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        awardList = ShootAwardInfoSeqHelper.read(__is);
        remainderTime = __is.readLong();
        items = IntStringSeqHelper.read(__is);
        singleShootType = __is.readInt();
        tenShootCostType = __is.readInt();
        singleShoot = __is.readInt();
        tenShootCost = __is.readInt();
        channelIds = IntSeqHelper.read(__is);
        basis = __is.readInt();
        singleShootScore = __is.readInt();
        tenShootScore = __is.readInt();
        freeTime = __is.readInt();
        totalNeedNum = __is.readInt();
        curNum = __is.readInt();
        shootLastTime = __is.readLong();
        myScore = __is.readInt();
        marksmanScoreRewards = MarksmanScoreRewardSeqHelper.read(__is);
        showMyRecord = __is.readBool();
        needLevel = __is.readInt();
        needVip = __is.readInt();
    }

    public static final long serialVersionUID = 1007903998L;
}
