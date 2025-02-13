// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CornucopiaBuyAllEvent implements ICornucopiaBuyAll{
   private IEventDispatcher dispatcher;
   public CornucopiaBuyAllEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCornucopiaBuyAll(int yuanbao) {
      this.dispatcher.emit(ICornucopiaBuyAll.class,new Object[]{yuanbao});
   }

}
