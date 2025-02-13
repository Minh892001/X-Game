// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class OfflineEvent implements IOffline{
   private IEventDispatcher dispatcher;
   public OfflineEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRoleOffline(long onlineInterval) {
      this.dispatcher.emit(IOffline.class,new Object[]{onlineInterval});
   }

}
