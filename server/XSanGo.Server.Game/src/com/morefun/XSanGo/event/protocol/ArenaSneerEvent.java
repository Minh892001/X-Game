// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaSneerEvent implements IArenaSneer{
   private IEventDispatcher dispatcher;
   public ArenaSneerEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSet(int sneerId,java.lang.String sneerStr,int cost) {
      this.dispatcher.emit(IArenaSneer.class,new Object[]{sneerId,sneerStr,cost});
   }

}
