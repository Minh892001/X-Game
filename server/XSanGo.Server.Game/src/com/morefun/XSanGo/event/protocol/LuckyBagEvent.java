// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LuckyBagEvent implements ILuckyBag{
   private IEventDispatcher dispatcher;
   public LuckyBagEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceiveLuckyBag(int type,int scriptId) {
      this.dispatcher.emit(ILuckyBag.class,new Object[]{type,scriptId});
   }

}
