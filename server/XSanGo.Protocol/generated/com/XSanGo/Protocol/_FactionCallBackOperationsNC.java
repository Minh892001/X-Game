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
// Generated from file `Faction.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _FactionCallBackOperationsNC
{
    /**
     * 据点状态通知
     **/
    void strongholdStateNotify(String stateMsg);

    /**
     * 公会战排行结果通知
     **/
    void factionBattleRankResultNotify(String result);

    /**
     * 据点角色列表动态刷新
     **/
    void strongholdRoleListChangeNotify(String result);

    /**
     * 行军时间通知
     **/
    void strongholdMarchingTimeNotify(int time);

    /**
     * 挖宝时间通知
     **/
    void diggingTreasureTimeNotify(int time);

    /**
     * 锦囊获取消息通知
     **/
    void gainKitsMessageNotify(String content, String icon);

    /**
     * 锦囊变更通知包
     **/
    void kitsChangeNotify(String views);

    /**
     * 公会战消息通知
     **/
    void factionBattleMessageNotify(String message);

    /**
     * 公会战公会资源变更通知 各数值为当前最终数值 徽章+粮草
     **/
    void factionBattleResourceNotify(int badge, int forage);

    /**
     * 公会战事件通知  第一个参数：抹去给定据点事件（大于0有效），第二个参数：新增事件的据点（大于0有效），第三个参数：新增事件的图标
     **/
    void factionBattleEventNotify(int strongholdId, int addStrongholdId, String eventIcon);

    /**
     * 公会战斗结果通知
     **/
    void factionBattleResultNotify(String result);

    /**
     * 公会战连杀通知播放特效
     **/
    void factionBattleEvenkillNotify();

    /**
     * 行军冷却解封通知
     **/
    void openMarchCoolingNotify();
}
