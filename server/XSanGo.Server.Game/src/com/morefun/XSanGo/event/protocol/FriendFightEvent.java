// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FriendFightEvent implements IFriendFight{
   private IEventDispatcher dispatcher;
   public FriendFightEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFriendFight(java.lang.String targetId,int resFlag) {
      this.dispatcher.emit(IFriendFight.class,new Object[]{targetId,resFlag});
   }

}
