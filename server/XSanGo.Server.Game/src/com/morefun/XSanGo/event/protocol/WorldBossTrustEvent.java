// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorldBossTrustEvent implements IWorldBossTrust{
   private IEventDispatcher dispatcher;
   public WorldBossTrustEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onTrust(int type,int yuanbao) {
      this.dispatcher.emit(IWorldBossTrust.class,new Object[]{type,yuanbao});
   }

}
