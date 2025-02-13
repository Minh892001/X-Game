// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TenSuperRotationEvent implements ITenSuperRotation{
   private IEventDispatcher dispatcher;
   public TenSuperRotationEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefresh(int num,java.lang.String itemStr) {
      this.dispatcher.emit(ITenSuperRotation.class,new Object[]{num,itemStr});
   }

}
