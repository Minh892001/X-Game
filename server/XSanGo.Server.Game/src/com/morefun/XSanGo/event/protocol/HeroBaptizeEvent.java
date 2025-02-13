// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroBaptizeEvent implements IHeroBaptize{
   private IEventDispatcher dispatcher;
   public HeroBaptizeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroBaptize(java.lang.String heroId,java.lang.String props,int times) {
      this.dispatcher.emit(IHeroBaptize.class,new Object[]{heroId,props,times});
   }

}
