// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FriendApplyingEvent implements IFriendApplying{
   private IEventDispatcher dispatcher;
   public FriendApplyingEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onApplyingHappend(java.lang.String target) {
      this.dispatcher.emit(IFriendApplying.class,new Object[]{target});
   }

}
