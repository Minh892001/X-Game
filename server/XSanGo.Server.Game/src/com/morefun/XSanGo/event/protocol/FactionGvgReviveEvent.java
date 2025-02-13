// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionGvgReviveEvent implements IFactionGvgRevive{
   private IEventDispatcher dispatcher;
   public FactionGvgReviveEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionGvgRevive(int money) {
      this.dispatcher.emit(IFactionGvgRevive.class,new Object[]{money});
   }

}
