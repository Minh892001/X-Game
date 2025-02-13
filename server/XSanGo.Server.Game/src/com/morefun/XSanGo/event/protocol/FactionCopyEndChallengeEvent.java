// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionCopyEndChallengeEvent implements IFactionCopyEndChallenge{
   private IEventDispatcher dispatcher;
   public FactionCopyEndChallengeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionCopyEndChallenge(java.lang.String roleId,java.lang.String factionId,int copyId,int harm) {
      this.dispatcher.emit(IFactionCopyEndChallenge.class,new Object[]{roleId,factionId,copyId,harm});
   }

}
