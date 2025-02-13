// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SignEvent implements ISign{
   private IEventDispatcher dispatcher;
   public SignEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void sign(int day) {
      this.dispatcher.emit(ISign.class,new Object[]{day});
   }

}
