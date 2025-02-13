// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorldBossEndChallengeEvent implements IWorldBossEndChallenge{
   private IEventDispatcher dispatcher;
   public WorldBossEndChallengeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEndChallenge(int harm) {
      this.dispatcher.emit(IWorldBossEndChallenge.class,new Object[]{harm});
   }

}
