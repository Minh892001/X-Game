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
// Generated from file `CrossServer.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

/**
 * 获取机器人信息
 **/

public interface AMD_CrossServer_getRobot extends Ice.AMDCallback
{
    /**
     * ice_response indicates that
     * the operation completed successfully.
     **/
    void ice_response(CrossRankItem[] __ret);
}
