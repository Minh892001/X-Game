// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorldBossTailAwardEvent implements IWorldBossTailAward{
   private IEventDispatcher dispatcher;
   public WorldBossTailAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onWorldBossTailAward(int hp) {
      this.dispatcher.emit(IWorldBossTailAward.class,new Object[]{hp});
   }

}
