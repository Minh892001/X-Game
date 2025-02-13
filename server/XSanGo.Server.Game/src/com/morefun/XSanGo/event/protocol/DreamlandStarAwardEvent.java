// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandStarAwardEvent implements IDreamlandStarAward{
   private IEventDispatcher dispatcher;
   public DreamlandStarAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDrawStarAward(int star,java.lang.String items) {
      this.dispatcher.emit(IDreamlandStarAward.class,new Object[]{star,items});
   }

}
