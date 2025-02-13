// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class RouletteEvent implements IRoulette{
   private IEventDispatcher dispatcher;
   public RouletteEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRoulette(int times,com.morefun.XSanGo.sign.RandomRoulette award) {
      this.dispatcher.emit(IRoulette.class,new Object[]{times,award});
   }

}
