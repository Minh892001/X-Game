// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TreasureGainEvent implements ITreasureGain{
   private IEventDispatcher dispatcher;
   public TreasureGainEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGain(com.XSanGo.Protocol.ItemView[] items,java.lang.String addArray) {
      this.dispatcher.emit(ITreasureGain.class,new Object[]{items,addArray});
   }

}
