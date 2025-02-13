// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class JinbiChangeEvent implements IJinbiChange{
   private IEventDispatcher dispatcher;
   public JinbiChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onJinbiChange(long change) {
      this.dispatcher.emit(IJinbiChange.class,new Object[]{change});
   }

}
