// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorldBossBuyCdEvent implements IWorldBossBuyCd{
   private IEventDispatcher dispatcher;
   public WorldBossBuyCdEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyCd(int yuanbao) {
      this.dispatcher.emit(IWorldBossBuyCd.class,new Object[]{yuanbao});
   }

}
