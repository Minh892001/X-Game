// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CdkUseEvent implements ICdkUse{
   private IEventDispatcher dispatcher;
   public CdkUseEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCdkUsed(java.lang.String cdkey,com.XSanGo.Protocol.ItemView[] items) {
      this.dispatcher.emit(ICdkUse.class,new Object[]{cdkey,items});
   }

}
