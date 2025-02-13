// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CornucopiaBuyOneEvent implements ICornucopiaBuyOne{
   private IEventDispatcher dispatcher;
   public CornucopiaBuyOneEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCornucopiaBuyOne(int id,int yuanbao) {
      this.dispatcher.emit(ICornucopiaBuyOne.class,new Object[]{id,yuanbao});
   }

}
