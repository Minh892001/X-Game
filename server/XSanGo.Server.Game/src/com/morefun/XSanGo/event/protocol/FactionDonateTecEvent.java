// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionDonateTecEvent implements IFactionDonateTec{
   private IEventDispatcher dispatcher;
   public FactionDonateTecEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDonateTec() {
      this.dispatcher.emit(IFactionDonateTec.class,new Object[]{});
   }

}
