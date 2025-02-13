// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CollectHeroSoulDoCollectEvent implements ICollectHeroSoulDoCollect{
   private IEventDispatcher dispatcher;
   public CollectHeroSoulDoCollectEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDoCollectHeroSoul(int type,int costType,int costNum,java.lang.String soul) {
      this.dispatcher.emit(ICollectHeroSoulDoCollect.class,new Object[]{type,costType,costNum,soul});
   }

}
