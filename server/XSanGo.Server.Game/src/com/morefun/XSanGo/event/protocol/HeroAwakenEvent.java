// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroAwakenEvent implements IHeroAwaken{
   private IEventDispatcher dispatcher;
   public HeroAwakenEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroAwaken(int templeId,int star,boolean isAwaken) {
      this.dispatcher.emit(IHeroAwaken.class,new Object[]{templeId,star,isAwaken});
   }

}
