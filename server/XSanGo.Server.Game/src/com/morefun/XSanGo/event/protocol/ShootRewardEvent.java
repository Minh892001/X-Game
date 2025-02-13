// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ShootRewardEvent implements IShootReward{
   private IEventDispatcher dispatcher;
   public ShootRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onShoot(int systemType,int shootType,boolean isFree,int dayCnt,int totalCnt,java.util.Map<java.lang.String,java.lang.Integer> itemsMap,boolean isHide) {
      this.dispatcher.emit(IShootReward.class,new Object[]{systemType,shootType,isFree,dayCnt,totalCnt,itemsMap,isHide});
   }

}
