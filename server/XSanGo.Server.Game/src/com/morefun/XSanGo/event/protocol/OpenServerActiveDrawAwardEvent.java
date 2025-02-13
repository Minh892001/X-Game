// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class OpenServerActiveDrawAwardEvent implements IOpenServerActiveDrawAward{
   private IEventDispatcher dispatcher;
   public OpenServerActiveDrawAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDraw(int activeId,int nodeId,java.lang.String items) {
      this.dispatcher.emit(IOpenServerActiveDrawAward.class,new Object[]{activeId,nodeId,items});
   }

}
