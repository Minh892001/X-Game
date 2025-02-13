// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LuckyStarEvent implements ILuckyStar{
   private IEventDispatcher dispatcher;
   public LuckyStarEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onLuckyStar() {
      this.dispatcher.emit(ILuckyStar.class,new Object[]{});
   }

}
