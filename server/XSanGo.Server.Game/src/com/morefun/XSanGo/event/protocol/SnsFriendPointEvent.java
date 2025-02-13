// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SnsFriendPointEvent implements ISnsFriendPoint{
   private IEventDispatcher dispatcher;
   public SnsFriendPointEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAddFriendPoint(java.lang.String targetId,int addNum,int currentNum) {
      this.dispatcher.emit(ISnsFriendPoint.class,new Object[]{targetId,addNum,currentNum});
   }

}
