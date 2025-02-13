// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TreasureHelpFriendEvent implements ITreasureHelpFriend{
   private IEventDispatcher dispatcher;
   public TreasureHelpFriendEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onTreasureHelpFriend() {
      this.dispatcher.emit(ITreasureHelpFriend.class,new Object[]{});
   }

}
