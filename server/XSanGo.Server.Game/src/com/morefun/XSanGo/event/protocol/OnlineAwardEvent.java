// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class OnlineAwardEvent implements IOnlineAward{
   private IEventDispatcher dispatcher;
   public OnlineAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onOnlineAward(int onlineGiftId,com.XSanGo.Protocol.ItemView[] itemView) {
      this.dispatcher.emit(IOnlineAward.class,new Object[]{onlineGiftId,itemView});
   }

}
