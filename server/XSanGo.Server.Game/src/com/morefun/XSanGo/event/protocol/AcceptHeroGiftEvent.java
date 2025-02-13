// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptHeroGiftEvent implements IAcceptHeroGift{
   private IEventDispatcher dispatcher;
   public AcceptHeroGiftEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceiveHeroGift(com.XSanGo.Protocol.ItemView[] items) {
      this.dispatcher.emit(IAcceptHeroGift.class,new Object[]{items});
   }

}
