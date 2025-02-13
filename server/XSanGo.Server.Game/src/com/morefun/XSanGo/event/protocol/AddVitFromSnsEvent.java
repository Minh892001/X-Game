// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AddVitFromSnsEvent implements IAddVitFromSns{
   private IEventDispatcher dispatcher;
   public AddVitFromSnsEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAddVitFromSns(java.lang.String from) {
      this.dispatcher.emit(IAddVitFromSns.class,new Object[]{from});
   }

}
