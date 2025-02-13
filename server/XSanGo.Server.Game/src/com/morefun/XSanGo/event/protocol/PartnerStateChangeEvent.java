// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class PartnerStateChangeEvent implements IPartnerStateChange{
   private IEventDispatcher dispatcher;
   public PartnerStateChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPartnerStateChange() {
      this.dispatcher.emit(IPartnerStateChange.class,new Object[]{});
   }

}
