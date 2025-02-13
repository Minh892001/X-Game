// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class PartnerClearEvent implements IPartnerClear{
   private IEventDispatcher dispatcher;
   public PartnerClearEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPartnerClear(com.morefun.XSanGo.partner.IPartner partner) {
      this.dispatcher.emit(IPartnerClear.class,new Object[]{partner});
   }

}
