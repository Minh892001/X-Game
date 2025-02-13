// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SeckillOverEvent implements ISeckillOver{
   private IEventDispatcher dispatcher;
   public SeckillOverEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSeckillOver(int id,java.lang.String itemId) {
      this.dispatcher.emit(ISeckillOver.class,new Object[]{id,itemId});
   }

}
