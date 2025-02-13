// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionGvgEndEvent implements IFactionGvgEnd{
   private IEventDispatcher dispatcher;
   public FactionGvgEndEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionGvgEnd(boolean isWin,int addHonor) {
      this.dispatcher.emit(IFactionGvgEnd.class,new Object[]{isWin,addHonor});
   }

}
