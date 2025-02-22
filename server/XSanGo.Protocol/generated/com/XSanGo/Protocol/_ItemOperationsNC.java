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
// Generated from file `Item.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.XSanGo.Protocol;

public interface _ItemOperationsNC
{
    ItemView levelUpFormationBuff(String id, String[] idArray)
        throws NoteException;

    void sale(String id, int count)
        throws NoteException;

    void useItem(String id, int count, String params)
        throws NoteException;

    ItemView[] useChestItem(String id, int count)
        throws NotEnoughMoneyException,
               NoteException;

    void drawCompositeChestItem(int itemIndex, String itemId)
        throws NoteException;

    /**
     * 切换阵法进阶类型,type对应脚本里面值
     **/
    void selectAdvancedType(int type)
        throws NoteException;

    /**
     * 阵法进阶,id用英文逗号分隔
     **/
    void advancedFormationBuff(String ids)
        throws NoteException;
}
