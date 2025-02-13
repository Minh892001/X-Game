// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroBaptizeResetEvent implements IHeroBaptizeReset{
   private IEventDispatcher dispatcher;
   public HeroBaptizeResetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReset(java.lang.String heroId,int baptizeLvl,java.lang.String baptizeProps,java.lang.String items) {
      this.dispatcher.emit(IHeroBaptizeReset.class,new Object[]{heroId,baptizeLvl,baptizeProps,items});
   }

}
