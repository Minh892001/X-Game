// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorldBossBuyInspireEvent implements IWorldBossBuyInspire{
   private IEventDispatcher dispatcher;
   public WorldBossBuyInspireEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyInspire(int yuanbao) {
      this.dispatcher.emit(IWorldBossBuyInspire.class,new Object[]{yuanbao});
   }

}
