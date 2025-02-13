// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionDonationEvent implements IFactionDonation{
   private IEventDispatcher dispatcher;
   public FactionDonationEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionDonation(java.lang.String roleId,int num) {
      this.dispatcher.emit(IFactionDonation.class,new Object[]{roleId,num});
   }

}
