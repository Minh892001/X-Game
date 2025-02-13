// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class RoleLevelupEvent implements IRoleLevelup{
   private IEventDispatcher dispatcher;
   public RoleLevelupEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRoleLevelup() {
      this.dispatcher.emit(IRoleLevelup.class,new Object[]{});
   }

}
