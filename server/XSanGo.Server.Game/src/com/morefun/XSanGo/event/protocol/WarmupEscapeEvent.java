// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WarmupEscapeEvent implements IWarmupEscape{
   private IEventDispatcher dispatcher;
   public WarmupEscapeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEscape() {
      this.dispatcher.emit(IWarmupEscape.class,new Object[]{});
   }

}
