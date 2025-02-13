// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CopyBeginEvent implements ICopyBegin{
   private IEventDispatcher dispatcher;
   public CopyBeginEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCopyBegin(com.morefun.XSanGo.copy.SmallCopyT templete,int junling) {
      this.dispatcher.emit(ICopyBegin.class,new Object[]{templete,junling});
   }

}
